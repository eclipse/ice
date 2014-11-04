package org.eclipse.ice.exodusII.test.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.widgets.jme.ViewFactory;
import org.eclipse.ice.client.widgets.mesh.MeshAppState;
import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.mesh.Polygon;
import org.eclipse.ice.datastructures.form.mesh.Vertex;
import org.eclipse.ice.exodusII.SWIGTYPE_p_float;
import org.eclipse.ice.exodusII.SWIGTYPE_p_int;
import org.eclipse.ice.exodusII.SWIGTYPE_p_int64_t;
import org.eclipse.ice.exodusII.SWIGTYPE_p_long;
import org.eclipse.ice.exodusII.SWIGTYPE_p_void;
import org.eclipse.ice.exodusII.ex_entity_type;
import org.eclipse.ice.exodusII.ex_init_params;
import org.eclipse.ice.exodusII.exodusII;
import org.eclipse.ice.exodusII.exodusIIConstants;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SimplerLauncher {
	
	private static int elementId = 0;
	private static int edgeId = 0;

	public static void main(String argv[]) throws IOException {

		// Load the Exodus library and our wrapper
		System.loadLibrary("exodus");
		System.loadLibrary("exodus_wrap");

		// Open the file.
		int exoid = openFile("coarse10_rz.e");

		if (exodusII.ex_int64_status(exoid) != 0) {
			System.out
					.println("SimplerLauncher message: "
							+ "ex_int64_status() returned true! We cannot currently process int64_t values for some types in the API.");
			closeFile(exoid);
			return;
		}

		// Open the globabl parameters in the file.
		ex_init_params parameters = getParameters(exoid);

		// Get useful global parameters.
		int numNodes = (int) convertInt64tPtrToLong(parameters.getNum_nodes());
		int numDims = (int) convertInt64tPtrToLong(parameters.getNum_dim());
		int numElementBlocks = (int) convertInt64tPtrToLong(parameters
				.getNum_elem_blk());

		// Create a list of vertices/nodes to store the node information.
		List<Vertex> nodes = getNodeCoordinates(exoid, numNodes, numDims);

		// Print out the nodes.
		System.out.println("The node coordinates are:");
		for (Vertex node : nodes) {
			float[] location = node.getLocation();
			System.out.println(location[0] + ", " + location[1] + ", "
					+ location[2]);
		}

		// Get the element blocks from the file.
		List<List<Polygon>> elementBlocks = getElementBlocks(exoid,
				numElementBlocks, nodes);

		// Add all of the elements in each element block to a mesh.
		MeshComponent mesh = new MeshComponent();
		for (List<Polygon> elementBlock : elementBlocks) {
			System.out.println("Adding block");
			for (Polygon element : elementBlock) {
				System.out.println("Adding element " + element.getId());
				mesh.addPolygon(element);
			}
			System.out.println("Finished adding block");
		}

		// Close the file.
		closeFile(exoid);

		// Create the Display.
		Display display = new Display();

		// Create the Shell (window).
		Shell shell = new Shell(display);
		shell.setText("Exodus Mesh Tester");
		shell.setSize(1024, 768);
		shell.setLayout(new FillLayout());

		// Construct the JME3 SimpleApplication inside the Shell.
		ViewFactory viewFactory = new ViewFactory(true);
		MeshAppState meshApp = viewFactory.createMeshView(mesh);
		Composite meshComposite = meshApp.createComposite(shell);

		// Open the shell.
		shell.open();

		// SOP UI loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// Close the mesh view.
		meshComposite.dispose();
		display.dispose();

		// Right now, this is the simplest way to halt the JME3 application.
		// Otherwise, the program does not actually terminate.
		System.exit(0);

		return;
	}

	/**
	 * This operation opens the Exodus file and returns a handle to it.
	 * 
	 * @param string
	 *            The name of the file to open
	 * @return The handle to the file
	 * @throws IOException
	 *             This exception is raised if the file cannot be opened
	 */
	private static int openFile(String filename) throws IOException {

		// Create the integer pointers
		SWIGTYPE_p_int CPU_word_size = exodusII.new_intPtr(), IO_word_size = exodusII
				.new_intPtr();
		SWIGTYPE_p_float version = exodusII.new_floatPtr();

		// Open the file
		int exoid = exodusII.ex_open_int(filename, exodusIIConstants.EX_READ,
				CPU_word_size, IO_word_size, version,
				exodusIIConstants.EX_API_VERS_NODOT);

		// Check the return variable to make sure the file opened successfully.
		if (exoid >= 0) {
			System.out.println("File opened. Attempting to load mesh.");
		} else {
			throw new IOException("Attempt to open file returned " + exoid
					+ " Error! - Exodus II file not found. Aborting!");
		}

		return exoid;
	}

	/**
	 * This operation closes the Exodus file.
	 * 
	 * @param exoid
	 *            The handle of the Exodus file.
	 * @throws IOException
	 *             This exception is raised if the file cannot be successfully
	 *             closed.
	 */
	private static void closeFile(int exoid) throws IOException {

		// Close the file and throw an exception if it failed to close
		if (exodusII.ex_close(exoid) != 0) {
			throw new IOException(
					"Error when closing file."
							+ " You can probably ignore this since it is the last "
							+ "thing to happen in the read, but you might want to look "
							+ "into it.");
		} else {
			System.out.println("File closed. Goodbye.");
		}

		return;
	}

	/**
	 * This operation returns the parameters from the exodus file.
	 * 
	 * @param exoid
	 *            The handle to the exodus II file.
	 * @return The parameters
	 * @throws IOException
	 *             This exception is raised if the parameters cannot be read.
	 */
	private static ex_init_params getParameters(int exoid) throws IOException {

		ex_init_params params = new ex_init_params();

		// Check the return variable to make sure the parameters were
		// successfully read.
		if (exodusII.ex_get_init_ext(exoid, params) != 0) {
			throw new IOException("Unable to read parameters from the file. "
					+ "Aborting.");
		}

		return params;
	}

	/**
	 * Gets the nodes (including their coordinates) from the mesh.
	 * 
	 * @param exoid
	 *            The file's handle.
	 * @param numNodes
	 *            The number of nodes in the mesh. This is a global parameter.
	 * @param numDims
	 *            The number of dimensions in the mesh. This is a global
	 *            parameter.
	 * @return A list containing all nodes in the mesh. This may be empty if
	 *         there are no nodes or if the parameters are invalid.
	 * @throws IOException
	 *             This method throws an exception if the x, y, and z
	 *             coordinates cannot be read from the file.
	 */
	private static List<Vertex> getNodeCoordinates(int exoid, int numNodes,
			int numDims) throws IOException {

		// Set the default return value. Try to use numNodes to define the
		// capacity of the list.
		List<Vertex> nodes = (numNodes > 0 ? new ArrayList<Vertex>(numNodes)
				: new ArrayList<Vertex>());

		// Create arrays to store the coordinates of the mesh.
		SWIGTYPE_p_float xCoords = exodusII.new_floatPtrArray(numNodes);
		SWIGTYPE_p_float yCoords = exodusII.new_floatPtrArray(numNodes);
		SWIGTYPE_p_float zCoords = exodusII.new_floatPtrArray(numNodes);
		// Cast the arrays to void *.
		SWIGTYPE_p_void xCoordsVoidPtr = exodusII.floatPtrToVoidPtr(xCoords);
		SWIGTYPE_p_void yCoordsVoidPtr = exodusII.floatPtrToVoidPtr(yCoords);
		SWIGTYPE_p_void zCoordsVoidPtr = exodusII.floatPtrToVoidPtr(zCoords);
		// Load the coordinates
		int error = exodusII.ex_get_coord(exoid, xCoordsVoidPtr,
				yCoordsVoidPtr, zCoordsVoidPtr);

		if (error != 0) {
			throw new IOException(
					"Unable to load coordinates from file. Aborting.");
		}

		float scale = 100f;
		
		// Create the nodes and add them to the list.
		Vertex node;
		for (int i = 0; i < numNodes; i++) {
			node = new Vertex(exodusII.floatPtrArray_getitem(xCoords, i) * scale,
					exodusII.floatPtrArray_getitem(yCoords, i) * scale,
					exodusII.floatPtrArray_getitem(zCoords, i) * scale);
			// The ID of the nodes are indexed starting at 1.
			node.setId(i + 1);
			nodes.add(node);
		}

		return nodes;
	}

	/**
	 * Gets all of the element blocks in the file.
	 * 
	 * @param exoid
	 *            The file's handle.
	 * @param numElementBlocks
	 *            The number of element blocks in the file. This is a global
	 *            parameter.
	 * @param nodes
	 *            The global list of nodes from the file.
	 * @return A list containing all of the element blocks in the file. This is
	 *         empty if there are none or the parameters are invalid.
	 * @throws IOException
	 *             This method throws an exception if the element block IDs
	 *             cannot be read from the file.
	 */
	private static List<List<Polygon>> getElementBlocks(int exoid,
			int numElementBlocks, List<Vertex> nodes) throws IOException {

		// Set the default return value. Try to use the number of element blocks
		// to determine the capacity of the list.
		List<List<Polygon>> elementBlocks;
		if (numElementBlocks > 0) {
			elementBlocks = new ArrayList<List<Polygon>>(numElementBlocks);
		} else {
			elementBlocks = new ArrayList<List<Polygon>>(1);
		}

		// Allocate the array of element block IDs. We need to cast it to a
		// void* for the exodusII method ex_get_ids().
		SWIGTYPE_p_int ids = exodusII.new_intPtrArray(numElementBlocks);
		SWIGTYPE_p_void idsVoid = exodusII.intPtrToVoidPtr(ids);

		// Try to read the element IDs into the pointer array.
		if (exodusII.ex_get_ids(exoid, ex_entity_type.EX_ELEM_BLOCK, idsVoid) != 0) {
			throw new IOException("Error reading element block ids. Aborting.");
		}

		// Read the element blocks and put them in the list.
		int id;
		List<Polygon> elementBlock;
		for (int i = 0; i < numElementBlocks; i++) {
			id = exodusII.intPtrArray_getitem(ids, i);
			System.out.println("Block " + i + " has id " + id);
			elementBlock = getElementBlock(exoid, id, nodes);
			elementBlocks.add(elementBlock);
		}

		// De-allocate the array of ids.
		exodusII.delete_intPtrArray(ids);

		return elementBlocks;
	}

	/**
	 * Reads an element block from the file.
	 * 
	 * @param exoid
	 *            The file's handle.
	 * @param id
	 *            The ID of the element block to read in.
	 * @param nodes
	 *            The global list of nodes from the file.
	 * @return An element block (a list of elements). This list is empty if the
	 *         block is empty or the parameters are invalid.
	 * @throws IOException
	 *             This method throws an exception if an error is encountered
	 *             either reading the element block information or connectivity
	 *             information.
	 */
	private static List<Polygon> getElementBlock(int exoid, int id,
			List<Vertex> nodes) throws IOException {

		// Set the default return value.
		List<Polygon> elements = new ArrayList<Polygon>();

		// Determine the number of elements and the number of nodes per element
		// in the block.

		// The exodus methods below require an int64_t pointer for the ID.
		// However, the ID given by ex_get_ids is an int. We have to create a
		// new int64_t pointer that points to a value identical to the ID.
		SWIGTYPE_p_int64_t idPtr = exodusII.getInt64tPtr(id);

		// Create pointers for the parameters that are passed into
		// ex_get_block().
		// TODO We need to be able to get the element type!!!
		String elementType = null;
		// void_int* num_entries_this_blk
		SWIGTYPE_p_int64_t numElementsPtr = exodusII.new_int64tPtr();
		// void_int* num_nodes_per_entry
		SWIGTYPE_p_int64_t numNodesPerElementPtr = exodusII.new_int64tPtr();
		// void_int* num_edges_per_entry
		SWIGTYPE_p_int64_t numEdgesPerElementPtr = exodusII.new_int64tPtr();
		// void_int* num_faces_per_entry
		SWIGTYPE_p_int64_t numFacesPerElementPtr = exodusII.new_int64tPtr();
		// void_int* num_attr_per_entry
		SWIGTYPE_p_int64_t numAttributesPerElementPtr = exodusII
				.new_int64tPtr();

		// Cast these int64_t* to void* for the exodus library to handle.
		SWIGTYPE_p_void numElementsVoid = exodusII
				.int64tPtrToVoidPtr(numElementsPtr);
		SWIGTYPE_p_void numNodesPerElementVoid = exodusII
				.int64tPtrToVoidPtr(numNodesPerElementPtr);
		SWIGTYPE_p_void numEdgesPerElementVoid = exodusII
				.int64tPtrToVoidPtr(numEdgesPerElementPtr);
		SWIGTYPE_p_void numFacesPerElementVoid = exodusII
				.int64tPtrToVoidPtr(numFacesPerElementPtr);
		SWIGTYPE_p_void numAttributesPerElementVoid = exodusII
				.int64tPtrToVoidPtr(numAttributesPerElementPtr);

		// Try to get the block parameters.
		int err;
		if ((err = exodusII.ex_get_block(exoid, ex_entity_type.EX_ELEM_BLOCK,
				idPtr, elementType, numElementsVoid, numNodesPerElementVoid,
				numEdgesPerElementVoid, numFacesPerElementVoid,
				numAttributesPerElementVoid)) != 0) {
			throw new IOException("Error reading block parameters for block "
					+ id + ". Aborting with error code " + err + ".");
		}

		// Get the values of interest from the SWIG pointers.
		int numElements = (int) convertInt64tPtrToLong(numElementsPtr);
		int numNodes = (int) convertInt64tPtrToLong(numNodesPerElementPtr);
		int numEdges = (int) convertInt64tPtrToLong(numEdgesPerElementPtr);
		int numFaces = (int) convertInt64tPtrToLong(numFacesPerElementPtr);

		// TODO Remove this.
		System.out.println("The type of elements in block " + id + " is "
				+ elementType);
		System.out.println("The number of elements in block " + id + " is "
				+ numElements);
		System.out.println("The number of nodes per element is " + numNodes);
		System.out.println("The number of edges per element is " + numEdges);
		System.out.println("The number of faces per element is " + numFaces);

		// Make sure the list of elements is big enough.
		((ArrayList<Polygon>) elements).ensureCapacity(numElements);

		// Allocate arrays to hold the node, edge, and face connectivity values.
		// void_int* nodeconn
		SWIGTYPE_p_int nodeConnectivity = exodusII.new_intPtrArray(numElements
				* numNodes);
		// void_int* edgeconn
		SWIGTYPE_p_int edgeConnectivity = exodusII.new_intPtrArray(numElements
				* numFaces);
		// void_int* faceconn
		SWIGTYPE_p_int faceConnectivity = exodusII.new_intPtrArray(numElements
				* numEdges);

		// Cast the int64_t* to void* for ex_get_conn().
		SWIGTYPE_p_void nodeConnectivityVoid = exodusII
				.intPtrToVoidPtr(nodeConnectivity);
		SWIGTYPE_p_void edgeConnectivityVoid = exodusII
				.intPtrToVoidPtr(edgeConnectivity);
		SWIGTYPE_p_void faceConnectivityVoid = exodusII
				.intPtrToVoidPtr(faceConnectivity);

		// Try to get the connectivity.
		if (exodusII.ex_get_conn(exoid, ex_entity_type.EX_ELEM_BLOCK, idPtr,
				nodeConnectivityVoid, edgeConnectivityVoid,
				faceConnectivityVoid) != 0) {
			throw new IOException("Error reading connectivity for block " + id
					+ ". Aborting.");
		}
		
		// We have to delete the int64_t pointer because the method called
		// allocates a new one.
		exodusII.delete_int64tPtr(idPtr);
		
		// Add all the elements in this block to the list of elements.
		Polygon element;
		for (int i = 0; i < numElements; i++) {
			element = getElement(nodeConnectivity, numNodes, i, nodes);
			elements.add(element);
		}

		// Free the arrays that hold the node, edge, and face connectivity.
		exodusII.delete_intPtrArray(nodeConnectivity);
		exodusII.delete_intPtrArray(edgeConnectivity);
		exodusII.delete_intPtrArray(faceConnectivity);

		return elements;
	}

	/**
	 * Generates an element from the node connectivity list.
	 * 
	 * @param nodeConnectivity
	 *            The array containing node connectivities for the containing
	 *            element block.
	 * @param numNodes
	 *            The number of nodes per element in the element block.
	 * @param index
	 *            The index of the element in the element block.
	 * @param nodes
	 *            The global list of nodes from the file.
	 * @return A new element, or null if one could not be created from the
	 *         specified parameters.
	 */
	private static Polygon getElement(SWIGTYPE_p_int nodeConnectivity,
			int numNodes, int index, List<Vertex> nodes) {

		// Set the default return value.
		Polygon element = null;

		int i;
		int nodeId;
		Vertex vertex;
		Edge edge;

		List<Vertex> vertices = new ArrayList<Vertex>(numNodes);
		List<Edge> edges = new ArrayList<Edge>(numNodes);

		// Get the ID of the next node from the connectivity array.
		nodeId = (exodusII.intPtrArray_getitem(nodeConnectivity, index
				* numNodes));

		// Add the first node to the list of nodes.
		vertex = nodes.get(nodeId - 1);
		vertices.add(vertex);

		// Add the remaining 3 nodes and first 3 edges.
		for (i = 1; i < numNodes; i++) {
			// Get the ID of the next node from the connectivity array.
			nodeId = (exodusII.intPtrArray_getitem(nodeConnectivity, index
					* numNodes + i));
			// Get the next node from the global list of nodes.
			vertex = nodes.get(nodeId - 1);

			// Get the edge between this node and the last.
			edge = new Edge(vertices.get(i - 1), vertex);
			// Set the edge's ID. To conform with the other components,
			// start with index 1.
//			edge.setId(index * numNodes + i + 1);
			edge.setId(++edgeId);
			// TODO We should re-use edges in blocks if possible.

			// Add the vertex and edge to the element's lists.
			vertices.add(vertex);
			edges.add(edge);
		}

		// Add the last edge to the list of edges.
		edge = new Edge(vertex, vertices.get(0));
		// Set the edge's ID. To conform with the other components,
		// start with index 1.
//		edge.setId(index * numNodes + i + 1);
		edge.setId(++edgeId);
		edges.add(edge);

		// Create the element from the nodes and node connectivity.
		element = new Polygon((ArrayList<Edge>) edges,
				(ArrayList<Vertex>) vertices);
		// Set the element's ID. Start with index 1. This is based on the order
		// of appearance in the exodus file across all element blocks.
		element.setId(++elementId);

		return element;
	}

	// TODO We can remove this method if we use typemaps to convert int64t to
	// long.
	/**
	 * This operation will dereference an int64_t * and cast it to a long int.
	 * 
	 * @param int64tPtr
	 *            The pointer to cast and dereference
	 * @return the value as a long
	 */
	private static long convertInt64tPtrToLong(SWIGTYPE_p_int64_t int64tPtr) {

		SWIGTYPE_p_long longPtr = exodusII.int64tToLongPtr(int64tPtr);
		long value = exodusII.longPtr_value(longPtr);

		return value;
	}
}
