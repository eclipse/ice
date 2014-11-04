package org.eclipse.ice.exodusII.test.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.exodusII.SWIGTYPE_p_float;
import org.eclipse.ice.exodusII.SWIGTYPE_p_int;
import org.eclipse.ice.exodusII.SWIGTYPE_p_int64_t;
import org.eclipse.ice.exodusII.SWIGTYPE_p_long;
import org.eclipse.ice.exodusII.SWIGTYPE_p_void;
import org.eclipse.ice.exodusII.ex_entity_type;
import org.eclipse.ice.exodusII.ex_init_params;
import org.eclipse.ice.exodusII.ex_inquiry;
import org.eclipse.ice.exodusII.exodusII;
import org.eclipse.ice.exodusII.exodusIIConstants;

public class SimpleLauncher {

	// A string used for error handling.
	private static String errMsg = null;

	// An integer for checking the return values
	private static int error = 0;

	// Create arrays to hold the individual block element, node and
	// attribute numbers.
	private static SWIGTYPE_p_int numElementsInBlockArray = null;
	private static SWIGTYPE_p_int numNodesPerElementArray = null;
	private static SWIGTYPE_p_int numAttributesArray = null;

	public static void main(String argv[]) throws IOException {

		// Load the Exodus library and our wrapper
		System.loadLibrary("exodus");
		System.loadLibrary("exodus_wrap");

		// Try to open the file
		int exoid = openFile("square.e");

		// Read the parameters off the file.
		ex_init_params params = getParams(exoid);

		// Get the number of nodes and the number of dimensions.
		int numNodes = (int) convertInt64tPtrToLong(params.getNum_nodes());
		int numDim = (int) convertInt64tPtrToLong(params.getNum_dim());

		// Get the coordinate information from the file
		// TODO - Create a good return structure to hold the coordinates
		getCoords(exoid, numNodes, numDim);

		// Get the names of the coordinates from the file
		getCoordinateNames(exoid, numDim);

		// Get the element map
		// TODO - Create a good return structure to hold the map. First, figure
		// out what the map represents!
		getElementMap(exoid, params);

		// Get the element ids
		SWIGTYPE_p_int elementIds = getElementIds(exoid, params);

		// Get the block parameters
		// TODO - Come up with a good structure to hold the parameters
		getBlockParmeters(exoid, params, elementIds);

		// Get the block properties from the file.
		// TODO - Come up with a good structure to hold these properties.
		getBlockProperties(exoid, params, elementIds);

		// Get the element connectivity information from the file.
		getElementConnectivity(exoid, params, elementIds);

		// Free everything
		// TODO - Remember, these are C pointers under the hood and there could
		// be a huge amount of memory just hanging around. Properly freeing all
		// of this up is critical, but will require some work to get correct. It
		// should probably be done in in each of the functions above with all of
		// the information being transferred to native Java arrays or objects.

		// Close the file
		error = exodusII.ex_close(exoid);

		// Throw an exception if it failed to close
		if (error != 0) {
			errMsg = "Error when closing file."
					+ " You can probably ignore this since it is the last "
					+ "thing to happen in the read, but you might want to look "
					+ "into it.";
			throw new IOException(errMsg);
		} else {
			System.out.println("File closed. Goodbye.");
		}

		return;
	}

	/**
	 * This operation retrieves the element connectivity information off of the
	 * file.
	 * 
	 * @param exoid
	 *            The handle to the exodus II file
	 * @param params
	 *            The parameters off of the exodus II file
	 * @param elementIds
	 *            The ids of the elements in the exodus II file
	 * @throws IOException
	 *             This exception is raised if the connectivity cannot be read.
	 */
	private static void getElementConnectivity(int exoid,
			ex_init_params params, SWIGTYPE_p_int elementIds)
			throws IOException {

		// Get the number of blocks
		int numBlocks = (int) convertInt64tPtrToLong(params.getNum_elem_blk());

		// Loop over each block to get its connectivity
		for (int i = 0; i < numBlocks; i++) {
			// Compute the total length of the connectivity array
			int connectivityLength = exodusII.intPtrArray_getitem(
					numNodesPerElementArray, i)
					* exodusII.intPtrArray_getitem(numElementsInBlockArray, i);
			// Create the array and make a void * for it.
			SWIGTYPE_p_int connectivityArray = exodusII
					.new_intPtrArray(connectivityLength);
			SWIGTYPE_p_void connectivityArrayVoidPtr = exodusII
					.intPtrToVoidPtr(connectivityArray);
			// Create the pointer to the element id
			SWIGTYPE_p_int64_t elementBlockIdPtr = exodusII.new_int64tPtr();
			int id = exodusII.intPtrArray_getitem(elementIds, i);
			SWIGTYPE_p_long longIdPtr = exodusII.new_longPtr();
			exodusII.longPtr_assign(longIdPtr, id);
			System.out.println(exodusII.longPtr_value(longIdPtr));
			exodusII.int64tPtr_assign(elementBlockIdPtr,
					exodusII.longToInt64tPtr(longIdPtr));
			// Get the connectivity
			error = exodusII.ex_get_elem_conn(exoid, elementBlockIdPtr,
					connectivityArrayVoidPtr);
			// Check the error and print the connectivity if everything is OK
			if (error >= 0) {
				System.out.print(id);
				for (int j = 0; j < connectivityLength; j++) {
					System.out.print(","
							+ exodusII
									.intPtrArray_getitem(connectivityArray, j));
				}
				System.out.print("\n");
			} else {
				errMsg = "Unable to read element connectivity. Aborting.";
				throw new IOException(errMsg);
			}
		}

		return;
	}

	/**
	 * This operation reads the block properties from the file.
	 * 
	 * @param exoid
	 *            The handle to the exodus II file
	 * @param params
	 *            The parameters from the file
	 * @param elementIds
	 *            The ids of the elements from the file
	 * @throws IOException
	 *             This exception is raised if the properties cannot be read.
	 */
	private static void getBlockProperties(int exoid, ex_init_params params,
			SWIGTYPE_p_int elementIds) throws IOException {

		// Get the number of blocks
		int numBlocks = (int) convertInt64tPtrToLong(params.getNum_elem_blk());

		// Create pointers to get the number of block properties
		SWIGTYPE_p_int numBlockPropertiesPtr = exodusII.new_intPtr();
		exodusII.intPtr_assign(numBlockPropertiesPtr, 0);
		// Cast this one to a void *.
		SWIGTYPE_p_void numBlockPropertiesVoidPtr = exodusII
				.intPtrToVoidPtr(numBlockPropertiesPtr);
		// Two of the pointers must be dummies
		SWIGTYPE_p_float dummyFloatPtr = exodusII.new_floatPtr();
		exodusII.floatPtr_assign(dummyFloatPtr, 0.0f);
		String dummyString = null;
		// Get the number of element block properties
		error = exodusII.ex_inquire(exoid,
				ex_inquiry.EX_INQ_EB_PROP.swigValue(),
				numBlockPropertiesVoidPtr, dummyFloatPtr, dummyString);

		// Check the return value to make sure the number of properties was
		// properly read.
		if (error < 0) {
			errMsg = "Unable to read number of element block properties. "
					+ "Aborting.";
			throw new IOException(errMsg);
		}

		int numBlockProperties = exodusII.intPtr_value(numBlockPropertiesPtr);

		// Create an array to hold the property names. It is char *[3]
		// according
		// to their reader example.
		String[] propertyNames = new String[numBlockProperties];
		// Get the property names
		error = exodusII.ex_get_prop_names(exoid, ex_entity_type.EX_ELEM_BLOCK,
				propertyNames);

		// Check the return value and print the names if they were read
		// successfully.
		if (error >= 0) {
			for (int i = 0; i < numBlockProperties; i++) {
				System.out.println("Property name " + i + " = "
						+ propertyNames[i]);
			}
		} else {
			errMsg = "Unable to read property names. Aborting.";
			throw new IOException(errMsg);
		}

		// Get the number of properties
		int numProperties = exodusII.intPtr_value(numBlockPropertiesPtr);
		// Create a pointer for the property value
		SWIGTYPE_p_int propertyValuePtr = exodusII.new_intPtr();
		exodusII.intPtr_assign(propertyValuePtr, 0);
		// Create a void * of the above
		SWIGTYPE_p_void propertyValueVoidPtr = exodusII
				.intPtrToVoidPtr(propertyValuePtr);
		// Loop over the number of block properties
		for (int i = 0; i < numProperties; i++) {
			// for each block
			for (int j = 0; j < numBlocks; j++) {
				// Create the pointer to the element id
				SWIGTYPE_p_int64_t elementBlockIdPtr = exodusII.new_int64tPtr();
				int id = exodusII.intPtrArray_getitem(elementIds, j);
				SWIGTYPE_p_long longIdPtr = exodusII.new_longPtr();
				exodusII.longPtr_assign(longIdPtr, id);
				System.out.println(exodusII.longPtr_value(longIdPtr));
				exodusII.int64tPtr_assign(elementBlockIdPtr,
						exodusII.longToInt64tPtr(longIdPtr));
				// Get the property value
				error = exodusII.ex_get_prop(exoid,
						ex_entity_type.EX_ELEM_BLOCK, elementBlockIdPtr,
						propertyNames[j], propertyValueVoidPtr);
				// Check the error value and fail if it was bad
				if (error < 0) {
					errMsg = "Unable to read property value. Aborting.";
					throw new IOException(errMsg);
				}
			}
		}
	}

	/**
	 * This operation retrieves the block parameters from the file. They are
	 * read into the appropriate arrays that are stored as class member
	 * variables.
	 * 
	 * @param exoid
	 *            The handle to the exodus II file
	 * @param params
	 *            The parameters from the exodus II file
	 * @param elementIds
	 *            The element ids for each element in the file
	 * @throws IOException
	 *             This exception is raised if the parameters cannot be read.
	 */
	private static void getBlockParmeters(int exoid, ex_init_params params,
			SWIGTYPE_p_int elementIds) throws IOException {

		// Get the number of blocks
		int numBlocks = (int) convertInt64tPtrToLong(params.getNum_elem_blk());

		// Initialize the arrays to store the ids, nodes/element and number of
		// attributes for each element block.
		numElementsInBlockArray = exodusII.new_intPtrArray(numBlocks);
		numNodesPerElementArray = exodusII.new_intPtrArray(numBlocks);
		numAttributesArray = exodusII.new_intPtrArray(numBlocks);

		// Create an element type string. This is not actually used, but it
		// is required.
		String elementType = new String(new char[255]);

		// Loop over the blocks and get the info
		for (int i = 0; i < numBlocks; i++) {
			// Create the pointer to the element id
			SWIGTYPE_p_int64_t elementBlockIdPtr = exodusII.new_int64tPtr();
			int id = exodusII.intPtrArray_getitem(elementIds, i);
			SWIGTYPE_p_long longIdPtr = exodusII.new_longPtr();
			exodusII.longPtr_assign(longIdPtr, id);
			System.out.println(exodusII.longPtr_value(longIdPtr));
			exodusII.int64tPtr_assign(elementBlockIdPtr,
					exodusII.longToInt64tPtr(longIdPtr));
			System.out.println(convertInt64tPtrToLong(elementBlockIdPtr));
			// Create the pointer to the number of elements per block
			SWIGTYPE_p_int numElementsInBlockPtr = exodusII.new_intPtr();
			// Create the pointer for the number of nodes in an element
			SWIGTYPE_p_int numNodesInElementPtr = exodusII.new_intPtr();
			// Create the pointer for the number of attributes
			SWIGTYPE_p_int numAttributesPtr = exodusII.new_intPtr();
			// Create the pointer for the number of edges per block
			SWIGTYPE_p_int numEdgesInBlockPtr = exodusII.new_intPtr();
			// Create the pointer for the number of edges per block
			SWIGTYPE_p_int numFacesInBlockPtr = exodusII.new_intPtr();
			// Cast the blocks to void *
			SWIGTYPE_p_void numElementsInBlockVoidPtr = exodusII
					.intPtrToVoidPtr(numElementsInBlockPtr);
			SWIGTYPE_p_void numNodesInElementVoidPtr = exodusII
					.intPtrToVoidPtr(numNodesInElementPtr);
			SWIGTYPE_p_void numAttributesVoidPtr = exodusII
					.intPtrToVoidPtr(numAttributesPtr);
			SWIGTYPE_p_void numEdgesInBlockVoidPtr = exodusII
					.intPtrToVoidPtr(numEdgesInBlockPtr);
			SWIGTYPE_p_void numFacesInBlockVoidPtr = exodusII
					.intPtrToVoidPtr(numFacesInBlockPtr);
			// Get the block information
			error = exodusII.ex_get_block(exoid, ex_entity_type.EX_ELEM_BLOCK,
					elementBlockIdPtr, elementType, numElementsInBlockVoidPtr,
					numNodesInElementVoidPtr, numEdgesInBlockVoidPtr,
					numFacesInBlockVoidPtr, numAttributesVoidPtr);
			// Check the error message and set the properties or complain
			if (error >= 0) {
				// Set the number of elements in the block
				exodusII.intPtrArray_setitem(numElementsInBlockArray, i,
						(int) convertInt64tPtrToLong(elementBlockIdPtr));
				// Set the number of nodes per element
				exodusII.intPtrArray_setitem(numNodesPerElementArray, i,
						exodusII.intPtr_value(numNodesInElementPtr));
				// Set the number of attributes
				exodusII.intPtrArray_setitem(numAttributesArray, i,
						exodusII.intPtr_value(numAttributesPtr));
			} else {
				errMsg = "Unable to read information for element block " + i
						+ ". Aborting.";
				throw new IOException(errMsg);
			}
		}

		return;
	}

	/**
	 * This operation returns an array of element ids with one entry for each
	 * element in the file.
	 * 
	 * @param exoid
	 *            The handle to the exodus II file
	 * @param params
	 *            The parameters from the file
	 * @return The list of ids
	 * @throws IOException
	 *             This exception is raised if the ids cannot be read.
	 */
	private static SWIGTYPE_p_int getElementIds(int exoid, ex_init_params params)
			throws IOException {

		// Get the number of blocks
		int numBlocks = (int) convertInt64tPtrToLong(params.getNum_elem_blk());
		// Create an array to hold the elements
		SWIGTYPE_p_int elementIds = exodusII.new_intPtrArray(numBlocks);
		// Cast the array to a void *
		SWIGTYPE_p_void elementIdsVoidPtr = exodusII
				.intPtrToVoidPtr(elementIds);
		// Get the element block ids
		error = exodusII.ex_get_elem_blk_ids(exoid, elementIdsVoidPtr);

		// Check the error value and complain if required
		if (error < 0) {
			errMsg = "Unable to get element ids. Aborting.";
			throw new IOException(errMsg);
		}

		return elementIds;
	}

	/**
	 * This operation gets the element map from the file.
	 * 
	 * @param exoid
	 *            The handle to the exodus II file
	 * @param params
	 *            The parameters from the exodus II file
	 * @throws IOException
	 *             This exception will be raised if the element map cannot be
	 *             read.
	 */
	private static void getElementMap(int exoid, ex_init_params params)
			throws IOException {

		// Get the number of elements
		int numElements = (int) convertInt64tPtrToLong(params.getNum_elem());
		// Create an array to hold the elements
		SWIGTYPE_p_int elementMap = exodusII.new_intPtrArray(numElements);
		// Cast the array to a void *
		SWIGTYPE_p_void elementMapVoidPtr = exodusII
				.intPtrToVoidPtr(elementMap);
		// Get the element map
		error = exodusII.ex_get_map(exoid, elementMapVoidPtr);

		// Check the return variable to make sure the element map was
		// successfully read.
		if (error < 0) {
			errMsg = "Unable to read element map. Aborting.";
			throw new IOException(errMsg);
		}

		return;
	}

	/**
	 * This operation gets the names of the coordinate axis from the file.
	 * 
	 * @param exoid
	 *            The handle to the exodus II file
	 * @param numDim
	 *            The number of dimensions in the file
	 * @return An array list with three coordinate names in it. Only the first
	 *         numDim entries will be set.
	 * @throws IOException
	 *             This exception is raised if the names cannot be read
	 */
	private static List<String> getCoordinateNames(int exoid, int numDim)
			throws IOException {

		// Create the x coordinate name. Allocating buffers in the string with
		// new String(new char[255]) causes some bad juju in the JVM for both
		// versions 7 and 8. I'm not sure why and I think this may be a bug in
		// Exodus.

		// TODO We keep getting seg faults regardless of how we allocate strings
		// (although using a char array of MAX_STR_LENGTH seems to work more
		// frequently). We have to fix this!

		List<String> names = new ArrayList<String>();

		// Create an array to hold the coordinate names. It is char ** under the
		// hood, so here there is some work required.
		String[] coordNames = new String[numDim];

		// Get the coordinate names
		error = exodusII.ex_get_coord_names(exoid, coordNames);

		// Check the return variable to make sure the coordinates names were
		// successfully read.
		if (error >= 0) {
			// Print the coordinate names for each dimension making sure to
			// decrement the number of dimensions by one to account for the zero
			// index.
			for (int i = 0; i < numDim; i++) {
				String name = coordNames[i];
				names.add(name);
				System.out.println("Coordinate " + i + " name = " + name);
			}
		} else {
			errMsg = "Unable to retrieve coordinate names from file. Aborting.";
			throw new IOException(errMsg);
		}

		return names;
	}

	/**
	 * This operation reads the nodal coordinates from the file and writes them
	 * to a csv file.
	 * 
	 * @param exoid
	 *            The handle to the exodus II file
	 * @param numNodes
	 *            The number of nodes in the file
	 * @param numDim
	 *            The number of dimensions in the file
	 * @throws IOException
	 *             This exception is raised if the coordinates cannot be read
	 */
	private static void getCoords(int exoid, int numNodes, int numDim)
			throws IOException {

		// Create arrays to store the coordinates of the mesh.
		SWIGTYPE_p_float xCoords = exodusII.new_floatPtrArray(numNodes);
		SWIGTYPE_p_float yCoords = exodusII.new_floatPtrArray(numNodes);
		SWIGTYPE_p_float zCoords = exodusII.new_floatPtrArray(numNodes);
		// Cast the arrays to void *.
		SWIGTYPE_p_void xCoordsVoidPtr = exodusII.floatPtrToVoidPtr(xCoords);
		SWIGTYPE_p_void yCoordsVoidPtr = exodusII.floatPtrToVoidPtr(yCoords);
		SWIGTYPE_p_void zCoordsVoidPtr = exodusII.floatPtrToVoidPtr(zCoords);
		// Load the coordinates
		error = exodusII.ex_get_coord(exoid, xCoordsVoidPtr, yCoordsVoidPtr,
				zCoordsVoidPtr);

		// Check the return variable to make sure the coordinates were
		// successfully read.
		if (error >= 0) {
			dumpCoords(xCoords, yCoords, zCoords, numNodes, numDim);
		} else {
			errMsg = "Unable to load coordinates from file. Aborting.";
			throw new IOException(errMsg);
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
	private static ex_init_params getParams(int exoid) throws IOException {

		ex_init_params params = new ex_init_params();
		int error = exodusII.ex_get_init_ext(exoid, params);

		// Check the return variable to make sure the parameters were
		// successfully read.
		if (error >= 0) {
			dumpParams(params);
		} else {
			errMsg = "Unable to read parameters from the file. Aborting.";
			throw new IOException(errMsg);
		}

		return params;
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
			errMsg = "Attempt to open file returned " + exoid
					+ " Error! - Exodus II file not found. Aborting!";
			throw new IOException(errMsg);
		}

		return exoid;
	}

	/**
	 * This operation dumps the coordinates of the mesh's nodes to a file called
	 * nodes.csv where each nodal position is a comma-separated triplet of
	 * values.
	 * 
	 * @param xCoords
	 *            The x coordinates
	 * @param yCoords
	 *            The y coordinates
	 * @param zCoords
	 *            The z coordinates
	 * @param numNodes
	 *            The number of nodes / length of the coordinate arrays
	 * @param numDim
	 *            The number of dimensions in the mesh
	 */
	private static void dumpCoords(SWIGTYPE_p_float xCoords,
			SWIGTYPE_p_float yCoords, SWIGTYPE_p_float zCoords, int numNodes,
			int numDim) {

		// The file name
		String fileName = "nodes.csv";
		// The string to hold the coordinate value
		String coordString = null;
		// The coordinates for a given node
		float x = 0.0f, y = 0.0f, z = 0.0f;

		try {
			// Open the file
			FileWriter writer = new FileWriter(fileName);
			// Write the coordinate data
			for (int i = 0; i < numNodes; i++) {
				x = exodusII.floatPtrArray_getitem(xCoords, i);
				y = exodusII.floatPtrArray_getitem(yCoords, i);
				z = exodusII.floatPtrArray_getitem(zCoords, i);
				coordString = x + "," + y + "," + z + "\n";
				writer.write(coordString);
			}
			// Close the file
			writer.close();
		} catch (IOException e) {
			// Complain
			System.err.println("Unable to create nodes file! Aborting.");
			e.printStackTrace();
			return;
		}

		System.out.println("File " + fileName
				+ " written with node coordinates.");
		return;
	}

	/**
	 * This operation dumps the parameters read from an Exodus file to standard
	 * out.
	 * 
	 * @param params
	 *            The parameters
	 */
	private static void dumpParams(ex_init_params params) {
		// Dump the number of dimensions. Each parameter comes back as a pointer
		// that has to be dereferenced through the appropriate SWIG call.
		SWIGTYPE_p_int64_t numDimPtr = params.getNum_dim();
		System.out.println("Number of dimensions = "
				+ convertInt64tPtrToLong(numDimPtr));

		// Dump the number of nodes
		SWIGTYPE_p_int64_t numNodesPtr = params.getNum_nodes();
		System.out.println("Number of nodes = "
				+ convertInt64tPtrToLong(numNodesPtr));

		// Dump the number of elements
		SWIGTYPE_p_int64_t numElementsPtr = params.getNum_elem();
		System.out.println("Number of elements = "
				+ convertInt64tPtrToLong(numElementsPtr));

		return;
	}

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
