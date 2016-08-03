/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.nek5000;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.eavp.viz.modeling.EdgeController;
import org.eclipse.eavp.viz.modeling.Edge;
import org.eclipse.eavp.viz.modeling.factory.IControllerProviderFactory;
import org.eclipse.eavp.viz.modeling.properties.MeshCategory;
import org.eclipse.eavp.viz.modeling.properties.MeshProperty;
import org.eclipse.eavp.viz.modeling.VertexController;
import org.eclipse.eavp.viz.modeling.base.IController;
import org.eclipse.eavp.viz.modeling.Vertex;
import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryCondition;
import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryConditionType;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygon;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.entry.DiscreteEntry;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MeshComponent;

/**
 * NekReader class is responsible for reading in the contents of a Nek5000 .rea
 * file and converting it into appropriate data structures for ICE to use. Data
 * structures used are broken down by the section of a reafile as follows:
 * 
 * ID Section Component Type Default IEntry Status
 * 
 * 2) Parameters DataComponent ready 3) Passive Scalar Data DataComponent ready
 * (but empty if NPSCAL = 0) 4) Logical Switches DataComponent ready 5) Pre-Nek
 * Axes DataComponent not ready 6) Mesh Data MeshComponent ready 7) Curved Side
 * Data MeshComponent not ready Boundary Conditions(+) ready 8) Presolve/Restart
 * DataComponent ready 9) Initial Conditions DataComponent not ready (not used
 * anymore) 10) Drive Force Data DataComponent not ready (not used anymore) 11)
 * Variable Property Data DataComponent ready 12) History, Integral Data
 * DataComponent ready 13) Output Field Spec DataComponent ready 14) Object Spec
 * DataComponent ready
 * 
 * (+) Note: Boundary conditions are read and assigned to Quads during the MESH
 * construction. The mesh and boundary conditions are coupled together and are
 * not considered distinct Components.
 * 
 * Components are created and returned in the sequential order specified above.
 * 
 * The component IDs start at id = 2, as an example selection DataComponent (for
 * toggling between Nek examples) is deliberately placed at the beginning of the
 * NekModel Form (id = 1) when the Form is created.
 * 
 * @author Anna Wojtowicz
 * 
 */
public class NekReader {

	/**
	 * Number of dimensions the problem is based in, defined by the NDIM
	 * parameter in a .rea file, located on the line before the MESH COMPONENT
	 * header.
	 */
	private int numDimensions;

	/**
	 * Boolean flag to determine if heat is solved for (ie. if there are any
	 * thermal boundary conditions). Determined by the IFHEAT logical switch.
	 */
	private boolean ifHeat;

	/**
	 * Boolean flag to determine if fluid is solved for (ie. if there are any
	 * fluid boundary conditions). Determined by the IFFLOW logical switch.
	 */
	private boolean ifFlow;

	/**
	 * Number of thermal elements/quads contained in the mesh.
	 */
	private int numThermalElements;

	/**
	 * Number of elements/quads which specifically house coolant/fluids. The
	 * number of fluid elements can be less than or equal to the number of
	 * thermal elements, but not greater.
	 */
	private int numFluidElements;

	/**
	 * Number of passive scalar boundary condition sets, as defined by the
	 * NPSCAL value in the PARAMETERS section of a .rea file. Can be equal to or
	 * greater than 0.
	 */
	private int numPassiveScalars;

	/**
	 * Properties of the problem read in (number of dimension, number of thermal
	 * elements, number of fluid elements, number of passive scalar sets).
	 */
	private ProblemProperties properties;
	
	/**
	 * The factory which the reader will use to produce views and controllers
	 * for the objects it generates.
	 */
	private IControllerProviderFactory factory;

	/**
	 * Nullary constructor.
	 */
	public NekReader() {
		super();
		return;
	}

	/**
	 * Reads in a reafile, and returns an ArrayList of Components representing
	 * the contents of the file. Each section in a reafile is returned as one
	 * Component. If the reafile is invalid, the method returns a null
	 * ArrayList.
	 * 
	 * @param reaFile
	 *            The Nek5000 reafile.
	 * @return ArrayList containing all the reafile input as Components.
	 * @throws IOException
	 *             Thrown when readFileLines(...) fails to read or close its
	 *             FileInputStream
	 * @throws FileNotFoundException
	 *             Thrown when readFileLines(...) fails to find the input file
	 */
	public ArrayList<Component> loadREAFile(File reaFile)
			throws FileNotFoundException, IOException {

		// Make sure the file is valid, otherwise just stop here
		if (reaFile == null || !reaFile.isFile()) {
			return null;
		}

		ArrayList<Component> components = new ArrayList<Component>();

		// Read lines into an ArrayList of Strings
		ArrayList<String> lines = readFileLines(reaFile);

		// Load the input components
		DataComponent parameters = loadParameters(lines);
		DataComponent passiveScalarData = loadPassiveScalarData(lines);
		DataComponent switches = loadLogicalSwitches(lines);
		DataComponent preNekAxes = loadPreNekAxes(lines);
		MeshComponent mesh = loadMesh(lines);
		MeshComponent curvedSideData = loadCurvedSideData(lines);
		DataComponent presolveRestartOpts = loadPresolveRestartOpts(lines);
		DataComponent initialConditions = loadInitialConditions(lines);
		DataComponent driveForceData = loadDriveForceData(lines);
		DataComponent varPropertyData = loadVarPropertyData(lines);
		DataComponent histIntegralData = loadHistoryIntegralData(lines);
		DataComponent outputFieldSpec = loadOutputFieldSpec(lines);
		DataComponent objectSpec = loadObjectSpec(lines);

		// Add the components to the ArrayList
		components.add(parameters);
		components.add(passiveScalarData);
		components.add(switches);
		components.add(preNekAxes);
		components.add(mesh);
		components.add(curvedSideData);
		components.add(presolveRestartOpts);
		components.add(initialConditions);
		components.add(driveForceData);
		components.add(varPropertyData);
		components.add(histIntegralData);
		components.add(outputFieldSpec);
		components.add(objectSpec);

		// Set the ProblemProperties object
		ProblemProperties properties = new ProblemProperties(numDimensions,
				numThermalElements, numFluidElements, numPassiveScalars);
		this.properties = properties;

		// Return the components
		return components;
	}

	/**
	 * Utility class to read in a reafile and return its contents as an
	 * ArrayList of Strings, broken at each newline character.
	 * 
	 * @param file
	 *            The reafile to break up.
	 * @return An ArrayList of Strings representing the lines of a reafile.
	 * @throws FileNotFoundException
	 *             Thrown when input file cannot be found
	 * @throws IOException
	 *             Thrown when the FileInputStream cannot be read or closed
	 */
	public ArrayList<String> readFileLines(File file)
			throws FileNotFoundException, IOException {

		// Convert to FileInputStream
		FileInputStream fileStream = null;
		fileStream = new FileInputStream(file);

		// Read the FileInputStream and append to a StringBuffer
		StringBuffer buffer = new StringBuffer();
		int fileByte;
		while ((fileByte = fileStream.read()) != -1) {
			buffer.append((char) fileByte);
		}

		// Close the stream
		fileStream.close();

		// Break up the StringBuffer at each newline character
		String[] bufferSplit = (buffer.toString()).split("\n");
		ArrayList<String> fileLines = new ArrayList<String>(
				Arrays.asList(bufferSplit));

		return fileLines;
	}

	/**
	 * Loads the PARAMETERS section of a reafile and returns the contents as a
	 * DataComponent of Entries. Each line is set as an IEntry.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         PARAMETERS section.
	 */
	private DataComponent loadParameters(ArrayList<String> reaLines) {

		// Create a parameters component to add entries
		DataComponent parameters = new DataComponent();
		parameters.setName("Parameters");
		parameters.setDescription("Entries contained in the Parameters "
				+ "section of a Nek5000 reafile");
		parameters.setId(2);

		IEntry entry;
		// Begin reading in the lines
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the parameters heading and 3 lines below indicating
			// how many lines the parameters section is
			if (reaLines.get(i).contains("****** PARAMETERS *****")
					&& reaLines.get(i + 1).contains("NEKTON VERSION")
					&& reaLines.get(i + 2).contains("DIMENSIONAL RUN")
					&& reaLines.get(i + 3).contains("PARAMETERS FOLLOW")) {

				// Grab the number indicating the length of the parameters
				// section (number of lines)
				int strIndex = reaLines.get(i + 3).indexOf("PARAMETERS FOLLOW");
				String numLinesStr = reaLines.get(i + 3)
						.substring(0, strIndex - 1).trim();
				int numLines = Integer.parseInt(numLinesStr);

				// Jump the iterator 4 lines ahead (from the header) and begin
				// reading in Entries
				i += 4;
				String currLine;
				String currDesc;
				String[] splitLine = null;
				for (int j = 0; j < numLines; j++) {

					// Grab the current line
					currLine = reaLines.get(i + j);
					splitLine = currLine.trim().split("\\s+");

					// If the current parameter is NPSCAL, define the value of
					// numPassiveScalars
					if (currLine.contains("NPSCAL")) {
						ArrayList<String> npscalArray = (ArrayList<String>) parseLine(
								String.class, currLine);
						numPassiveScalars = Integer
								.parseInt(npscalArray.get(0).substring(0, 1));
					}

					// Create a Nek IEntry
					entry = makeNekEntry(false);

					// Construct the parameter description
					currDesc = "";
					for (int k = 2; k < splitLine.length; k++) {
						currDesc += splitLine[k] + " ";
					}

					// Set name, description, value, and ID
					entry.setValue(splitLine[0]);
					if ((j + 1) < 100) {
						entry.setName(String.format("p%02d", (j + 1)));
					} else {
						entry.setName(String.format("p%3d", (j + 1)));
					}
					entry.setDescription(currDesc);
					entry.setId(j + 1);

					// Append to DataComponent
					parameters.addEntry(entry);
				}
			}
		}

		// Return the DataComponent containing parameter entries
		return parameters;
	}

	/**
	 * Loads the PASSIVE SCALAR DATA section of a reafile and returns the
	 * contents as a DataComponent of Entries. Each line is set as an IEntry. If
	 * NPSCAL = 0, this DataComponent will have no entries.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         PASSIVE SCALAR DATA section.
	 */
	private DataComponent loadPassiveScalarData(ArrayList<String> reaLines) {

		// Create a passive scalar component to add entries
		DataComponent passiveScalarData = new DataComponent();
		passiveScalarData.setName("Passive Scalar Data");
		passiveScalarData.setDescription("Entries contained in the Passive "
				+ "Scalar Data section of a Nek5000 reafile");
		passiveScalarData.setId(3);

		// Only write these entries if there are >0 passive scalars defined
		if (numPassiveScalars > 0) {

			IEntry entry;
			for (int i = 0; i < reaLines.size(); i++) {

				// Search for the passive scalar heading
				if (reaLines.get(i).contains("Lines of passive scalar data")) {

					// Grab the number indicating the length of the Passive
					// Scalar Data section (number of lines)
					int strIndex = reaLines.get(i)
							.indexOf("Lines of passive scalar data");
					String numLinesStr = reaLines.get(i)
							.substring(0, strIndex - 1).trim();
					int numLines = Integer.parseInt(numLinesStr);

					// Jump the iterator 1 line ahead (from the header) and
					// begin
					// reading in Entries
					i += 1;
					String currLine;
					String[] splitLine;
					String currValue;
					for (int j = 0; j < numLines; j++) {

						// Grab the current line
						currLine = reaLines.get(i + j);
						splitLine = currLine.trim().split("\\s+");

						// Create a Nek IEntry
						entry = makeNekEntry(false);

						// Construct the current value
						currValue = "";
						for (int k = 0; k < splitLine.length; k++) {
							if (k != splitLine.length - 1) {
								currValue += splitLine[k] + " ";
							} else {
								currValue += splitLine[k];
							}
						}

						// Set the name, description, value, and ID
						entry.setName("Passive Scalar " + (j + 1));
						entry.setDescription("");
						entry.setValue(currValue);
						entry.setId(j + 1);

						// Append to the DataComponent
						passiveScalarData.addEntry(entry);
					}
				}
			}
		}

		return passiveScalarData;

	}

	/**
	 * Loads the LOGICAL SWITCHES section of a reafile and returns the contents
	 * as a DataComponent of Entries. Each line is set as an IEntry.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         LOGICAL SWITCHES section.
	 */
	private DataComponent loadLogicalSwitches(ArrayList<String> reaLines) {

		// Create a switches component to add entries
		DataComponent switches = new DataComponent();
		switches.setName("Logical Switches");
		switches.setDescription("Entries contained in the Logical Switches "
				+ "section of a Nek5000 reafile");
		switches.setId(4);

		IEntry entry;
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the logical switches heading indicating how many
			// lines follow
			if (reaLines.get(i).contains("LOGICAL SWITCHES FOLLOW")) {

				// Grab the number indicating the length of the switches
				// section (number of lines)
				int strIndex = reaLines.get(i).indexOf("LOGICAL SWITCHES");
				String numLinesStr = reaLines.get(i).substring(0, strIndex - 1)
						.trim();
				int numLines = Integer.parseInt(numLinesStr);

				// Jump the iterator 1 lines ahead and begin reading in Entries
				i += 1;
				String currLine;
				String[] splitLine;
				String currValue;
				String currName;
				String currDesc;
				for (int j = 0; j < numLines; j++) {

					// Grab the current line
					currLine = reaLines.get(i + j);
					splitLine = currLine.trim().split("\\s+");

					currValue = "";
					currName = "";
					currDesc = "";
					// Check if line contains passive scalar flags (ie. >1 flag)
					if (splitLine.length > 2) {

						// Create a NekEntry
						entry = makeNekEntry(false);

						// Define the current entry name depending on which line
						// it is
						if (currLine.contains("IFNAV & IFADVC")) {

							currName = "IFNAV && IFADVC"; // Eclipse form bug
															// where single
															// ampersands shows
															// up as a space
							currValue = String.format(
									"%s %s %s %s %s %s %s " + "%s %s %s %s",
									splitLine[0], splitLine[1], splitLine[2],
									splitLine[3], splitLine[4], splitLine[5],
									splitLine[6], splitLine[7], splitLine[8],
									splitLine[9], splitLine[10]);

							for (int k = 14; k < splitLine.length; k++) {
								if (k != splitLine.length - 1) {
									currDesc += splitLine[k] + " ";
								} else {
									currDesc += splitLine[k];
								}
							}
						} else if (currLine.contains("IFTMSH")) {

							currName = "IFTMSH";
							currValue = String.format(
									"%s %s %s %s %s %s %s " + "%s %s %s %s %s",
									splitLine[0], splitLine[1], splitLine[2],
									splitLine[3], splitLine[4], splitLine[5],
									splitLine[6], splitLine[7], splitLine[8],
									splitLine[9], splitLine[10], splitLine[11]);
							for (int k = 13; k < splitLine.length; k++) {
								if (k != splitLine.length - 1) {
									currDesc += splitLine[k] + " ";
								} else {
									currDesc += splitLine[k];
								}
							}
						}

					}

					// Otherwise it only contains one flag
					else {

						// Create a NekEntry
						entry = makeNekEntry(true);

						// Set the name and value
						currValue = ("T".equals(splitLine[0]) ? "YES" : "NO");
						currName = splitLine[1];

						// Flag if there are heat/fluid solutions
						if (currLine.contains("IFFLOW")) {
							ifFlow = ("T".equals(splitLine[0]) ? true : false);
						}
						if (currLine.contains("IFHEAT")) {
							ifHeat = ("T".equals(splitLine[0]) ? true : false);
						}
					}

					// Set the name, value, and ID
					entry.setName(currName);
					entry.setValue(currValue);
					entry.setId(j + 1);

					// Append to the DataComponent
					switches.addEntry(entry);
				}
			}
		}

		// Return the DataComponent containing logical switch entries
		return switches;
	}

	/**
	 * Loads the PRE-NEK AXES section of a reafile and returns the contents as a
	 * DataComponent of Entries. Each line is set as an IEntry. This
	 * DataComponent will have no entries if NPSCAL = 0.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         PRE-NEK AXES section.
	 */
	private DataComponent loadPreNekAxes(ArrayList<String> reaLines) {

		// Create a PreNek Axes component to add entries
		DataComponent preNekAxes = new DataComponent();
		preNekAxes.setName("Pre-Nek Axes");
		preNekAxes.setDescription("Entries contained in the Pre-Nek Axes "
				+ "section of a Nek5000 reafile");
		preNekAxes.setId(5);

		IEntry entry;
		String[] splitLine;
		String currValue;
		String currName;
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the Pre-Nek axes line
			if (reaLines.get(i).contains("XFAC,YFAC,XZERO,YZERO")) {

				// Grab the current line
				String currLine = reaLines.get(i);
				splitLine = currLine.trim().split("\\s+");

				// Create a Nek IEntry
				entry = makeNekEntry(false);

				// Construct the current value and name
				currValue = "";
				currName = "";
				currValue = String.format("%-13s %-13s %-13s %-13s",
						splitLine[0], splitLine[1], splitLine[2], splitLine[3]);

				for (int j = 4; j < splitLine.length; j++) {
					if (j != splitLine.length - 1) {
						currName += splitLine[j] + " ";
					} else {
						currName += splitLine[j];
					}
				}

				// Set the name, value and ID
				entry.setName(currName);
				entry.setValue(currValue);
				entry.setId(1); // There's only one Pre-Nek axes line
				entry.setReady(false); // Don't need to expose this to user

				// Append to the DataComponent
				preNekAxes.addEntry(entry);
			}
		}

		return preNekAxes;
	}

	/**
	 * Loads the MESH DATA section of a reafile and returns the contents as a
	 * MeshComponent of Quads. In the process of constructing elements (or
	 * 'Quads' in ICE lexicon), the loadBoundaryConditions() method is called,
	 * and each Quad is assigned a set of BoundaryCondition objects based on the
	 * unique Edge IDs contained in that Quad.
	 * 
	 * Each Quad should have a minimum of two BoundaryConditions associated to
	 * it (fluid and thermal boundary conditions). A Quad may have N more sets
	 * of BoundaryConditions, where N is the value defined by NPSCAL in the
	 * PARAMETERS section (ie. this.numPassiveScalars)
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return MeshComponent containing the definition of all mesh elements()
	 *         defined in the problem, with a set of BoundaryConditions
	 *         associated to each Quad.
	 **/
	private MeshComponent loadMesh(ArrayList<String> reaLines) {

		// Local declarations for file reading
		String currLine;
		ArrayList<Float> nextLine = null;
		ArrayList<String> numbersLine = null;

		// Local declarations for quad building
		VertexController vertex;
		EdgeController edge;
		NekPolygonController quad;
		ArrayList<VertexController> vertices = null;
		ArrayList<EdgeController> edges = null;
		ArrayList<VertexController> vertexCombo = null;

		// Keeps track of the unique edge IDs associated to the current quad
		// for the purpose of assigning boundary conditions keyed on edge IDs
		ArrayList<Integer> edgeIdList = null;

		// Create a mesh component, quad and edge
		MeshComponent mesh = new MeshComponent();
		mesh.setName("Mesh Data");
		mesh.setDescription("Elements contained in the Mesh section of a "
				+ "Nek5000 reafile");
		mesh.setId(6);

		// Create containers to hold/index all the different sets of boundary
		// conditions
		ArrayList<Object> boundaryConditions;
		HashMap<Integer, BoundaryCondition> fluidBoundaryConditions = null;
		HashMap<Integer, BoundaryCondition> thermalBoundaryConditions = null;
		ArrayList<HashMap<Integer, BoundaryCondition>> scalarBoundaryConditions = null;

		// Begin reading the input file lines
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the mesh data heading
			if ((reaLines.get(i).contains("**MESH DATA**")
					|| reaLines.get(i).contains("*** MESH DATA ***"))
					&& reaLines.get(i + 1).contains("NEL,NDIM,NELV")) {

				// Grab the numbers on the next line (NEL,NDIM,NELV)
				numbersLine = (ArrayList<String>) parseLine(String.class,
						reaLines.get(i + 1));

				// NEL = number of (thermal) elements used
				// NDIM = number of dimensions
				// NELV = number of fluid elements used (doesn't have to be same
				// as number of thermal elements

				numThermalElements = Integer.parseInt(numbersLine.get(0));
				numDimensions = Integer.parseInt(numbersLine.get(1));
				numFluidElements = Integer.parseInt(numbersLine.get(2));

				// Start ID counters for quads, edges and vertices, all IDs
				// must be unique
				int edgeId = 1;
				int vertexId = 1;
				int quadId = 1;

				// Load boundary conditions that will be assigned to
				// element/quad edges
				boundaryConditions = loadBoundaryConditions(reaLines);

				// Determine what position the fluid, thermal and passive scalar
				// boundary conditions are in the loaded boundaryConditions list
				int fluidPosition = 0, thermalPosition = 0,
						passiveScalPosition = 0;
				if (ifFlow) {
					fluidPosition = 0;
				}
				if (ifFlow && ifHeat) {
					thermalPosition = 1;
				} else if (!ifFlow && ifHeat) {
					thermalPosition = 0;
				}
				if (numPassiveScalars > 0) {
					passiveScalPosition = thermalPosition + 1;
				}
				if (ifFlow) {
					fluidBoundaryConditions = (HashMap<Integer, BoundaryCondition>) boundaryConditions
							.get(fluidPosition);
				}
				if (ifHeat) {
					thermalBoundaryConditions = (HashMap<Integer, BoundaryCondition>) boundaryConditions
							.get(thermalPosition);
				}

				if (numPassiveScalars > 0) {
					scalarBoundaryConditions = (ArrayList<HashMap<Integer, BoundaryCondition>>) boundaryConditions
							.get(passiveScalPosition);
				}

				// Jump the iterator 2 lines ahead and begin reading in
				// elements/quads
				i += 2;

				String materialId;
				int groupNum;
				String[] splitLine;
				int j = 0;
				while (j < numThermalElements * (numDimensions + 1)) { // Each
																		// element
																		// is (1
																		// header
																		// + #
																		// dimensions)
																		// lines

					// Grab the current line
					currLine = reaLines.get(i + j);

					// If current line is the beginning of a new element
					if (currLine.contains("ELEMENT")) {

						// Grab the material ID and group number
						splitLine = currLine.trim().split("\\s+");
						if (splitLine[3]
								.charAt(splitLine[3].length() - 1) == ']') {
							materialId = splitLine[3].substring(0,
									splitLine[3].length() - 1);
							groupNum = Integer.parseInt(splitLine[5]);
						} else {
							materialId = splitLine[3];
							groupNum = Integer.parseInt(splitLine[6]);
						}

						// Create a new current element
						ArrayList<ArrayList<Float>> currElement = new ArrayList<ArrayList<Float>>();

						// Jump to the next line and parse as many lines as
						// there
						// are dimensions (ie. 2 dimensions = 2 lines of coords)
						for (int k = 0; k < numDimensions; k++) {

							// Parse line into an ArrayList and add to current
							// element
							nextLine = (ArrayList<Float>) parseLine(Float.class,
									reaLines.get(i + j + k + 1));
							currElement.add(nextLine);
						}

						// Construct a set of vertices
						float x, y, z;
						vertices = new ArrayList<VertexController>();

						for (int k = 0; k < currElement.get(0).size(); k++) {

							// Define the x,y,z coordinates
							x = currElement.get(0).get(k);
							y = currElement.get(1).get(k);
							z = 0f;

							// Create new vertex and add to vertices ArrayList
							Vertex vertexComponent = new Vertex(x, y,
									z);
							vertex = (VertexController) factory
									.createProvider(vertexComponent)
									.createController(vertexComponent);
							vertex.setProperty(MeshProperty.ID,
									Integer.toString(vertexId)); // Set unique
																	// ID
							vertices.add(vertex);

							vertexId++;
						}

						// Construct combinations of vertices
						edges = new ArrayList<EdgeController>();
						edgeIdList = new ArrayList<Integer>();

						for (int k = 0; k < 4; k++) {

							vertexCombo = new ArrayList<VertexController>();

							// Edge 1 = Vertices 1 + 2
							// Edge 2 = Vertices 2 + 3
							// Edge 3 = Vertices 3 + 4
							// Edge 4 = Vertices 4 + 1

							// Create one of four possible combinations of
							// vertices
							switch (k) {
							case 0:
								vertexCombo.add(vertices.get(0));
								vertexCombo.add(vertices.get(1));
								break;
							case 1:
								vertexCombo.add(vertices.get(1));
								vertexCombo.add(vertices.get(2));
								break;
							case 2:
								vertexCombo.add(vertices.get(2));
								vertexCombo.add(vertices.get(3));
								break;
							case 3:
								vertexCombo.add(vertices.get(3));
								vertexCombo.add(vertices.get(0));
								break;
							default:
								break;
							}

							// Create a new edge and add to edges ArrayList
							Edge edgeComponent = new Edge(
									vertexCombo.get(0), vertexCombo.get(1));
							edge = (EdgeController) factory
									.createProvider(edgeComponent)
									.createController(edgeComponent);
							edge.setProperty(MeshProperty.ID,
									Integer.toString(edgeId)); // Set
							// unique
							// edge
							// ID
							edges.add(edge);

							edgeIdList.add(edgeId);

							edgeId++;
						}

						// Create new quad, add it to the MeshComponent
						NekPolygon quadComponent = new NekPolygon();
						quad = (NekPolygonController) factory
								.createProvider(quadComponent)
								.createController(quadComponent);

						for (EdgeController e : edges) {
							quad.addEntityToCategory(e, MeshCategory.EDGES);
						}

						quad.setPolygonProperties(materialId, groupNum);
						

						// Set the boundary conditions of the quad by edge ID
						int currEdgeId;
						for (int k = 0; k < 4; k++) { // k < 6 for
														// three-dimensional
														// cases

							// Grab the ID of one of the edges contained in the
							// quad
							currEdgeId = edgeIdList.get(k);

							// Set the fluid boundary condition for that edge
							if (ifFlow) {
								quad.setFluidBoundaryCondition(currEdgeId,
										fluidBoundaryConditions
												.get(currEdgeId));
							}

							// Set the thermal boundary condition for that edge
							if (ifHeat) {
								quad.setThermalBoundaryCondition(currEdgeId,
										thermalBoundaryConditions
												.get(currEdgeId));
							}

							// Set the passive scalar boundary condition(s) for
							// that edge (if any)
							if (numPassiveScalars > 0) {

								HashMap<Integer, BoundaryCondition> currSetScalars = new HashMap<Integer, BoundaryCondition>();

								for (int ii = 1; ii <= numPassiveScalars; ii++) {

									// Define the current set of scalar boundary
									// conditions
									currSetScalars = scalarBoundaryConditions
											.get(ii);

									// Set the passive scalar boundary condition
									// for the current edge
									quad.setOtherBoundaryCondition(currEdgeId,
											ii, currSetScalars.get(currEdgeId));
								}
							}
						}

						quad.setProperty(MeshProperty.ID,
								Integer.toString(quadId)); // Set
						// unique
						// quad
						// ID
						mesh.addPolygon(quad); // Add the quad to the mesh
						edgeIdList.clear(); // Clear the quad edge list

						quadId++;

						// Jump ahead to the next element/quad (if there is one)
						j += (numDimensions + 1);
					}

					else {
						j++;
					}
				}
			}
		}
		

		// Return the Mesh Component containing mesh elements/quads with a
		// set of (2 + NPSCAL) boundary conditions associated to each edge
		return mesh;
	}

	/**
	 * Loads the CURVED SIDES section of a reafile and returns the contents as a
	 * MeshComponent of Quads.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         CURVED SIDE DATA section.
	 */
	private MeshComponent loadCurvedSideData(ArrayList<String> reaLines) {

		MeshComponent curvedSides = new MeshComponent();
		curvedSides.setName("Curved Side Data");
		curvedSides.setDescription("Elements contained in the Curved Side "
				+ " section of a Nek5000 reafile");
		curvedSides.setId(7);

		// TODO to be implemented

		return curvedSides;
	}

	/**
	 * Reads in an ArrayList of Strings (lines read from a .rea file) and
	 * returns an ArrayList of Objects containing the boundary conditions
	 * extracted from the reafile. The returned ArrayList is of mixed type,
	 * which necessitates it being of type Object. The contents are:
	 * 
	 * [0] : HashMap of fluid BoundaryCondition objects keyed on unique edge ID
	 * [1] : HashMap of thermal BoundaryCondition objects keyed on unique edge
	 * ID [2] : ArrayList of n HashMaps of passive scalar BoundaryCondition
	 * objects keyed on unique edge ID, where n is an integer defined by the
	 * NPSCAL parameter (ie. this.numPassiveScalars)
	 * 
	 * @param realLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return An ArrayList of BoundaryCondition HashMaps keyed on unique Edge
	 *         IDs. Elements 0 and 1 are fluid and thermal boundary condition
	 *         maps respecively. Elements 2 is an ArrayList of N HashMaps of
	 *         passive scalar boundary conditions, where N is defined by NPSCAL
	 *         in the PARAMETERS section (ie. this.numPassiveScalars)
	 **/
	private ArrayList<Object> loadBoundaryConditions(
			ArrayList<String> reaLines) {

		// Local declarations
		ArrayList<Object> currCondition;

		// Create the HashMaps of thermal and fluid boundary conditions
		HashMap<Integer, BoundaryCondition> thermalBoundaryConditions = new HashMap<Integer, BoundaryCondition>();
		HashMap<Integer, BoundaryCondition> fluidBoundaryConditions = new HashMap<Integer, BoundaryCondition>();

		// Create an ArrayList to hold all HashMaps of passive scalar
		// boundary conditions
		ArrayList<HashMap<Integer, BoundaryCondition>> scalarBoundaryConditions = new ArrayList<HashMap<Integer, BoundaryCondition>>();

		// Create an ArrayList of Objects to hold all the HashMaps of
		// boundary conditions (to return)
		ArrayList<Object> allBoundaryConditions = new ArrayList<Object>();

		// Begin reading the input file lines
		for (int i = 0; i < reaLines.size(); i++) {

			/** --- Load FLUID boundary conditions --- **/

			// Search for the fluid boundary conditions header
			if (reaLines.get(i)
					.contains("***** FLUID   BOUNDARY CONDITIONS *****")) {

				// Jump the iterator 1 line ahead and begin reading in boundary
				// conditions
				i += 1;

				for (int j = 0; j < numFluidElements * 4; j++) {

					// Create the current boundary condition object
					currCondition = buildBoundaryConditionPair(reaLines, i, j);

					// Plug it (along with the unique edge ID it corresponds to)
					// into the thermal boundary conditions HashMap
					fluidBoundaryConditions.put((Integer) currCondition.get(0),
							(BoundaryCondition) currCondition.get(1));
				}

				allBoundaryConditions.add(fluidBoundaryConditions);
			}

			/** --- Load THERMAL boundary conditions --- **/

			// Search for the thermal boundary conditions header
			if (reaLines.get(i)
					.contains("***** THERMAL BOUNDARY CONDITIONS *****")) {

				// Jump the iterator 1 line ahead and begin reading in boundary
				// conditions
				i += 1;

				for (int j = 0; j < numThermalElements * 4; j++) {

					// Create the current boundary condition object
					currCondition = buildBoundaryConditionPair(reaLines, i, j);

					// Plug it (along with the unique edge ID it corresponds to)
					// into the thermal boundary conditions HashMap
					thermalBoundaryConditions.put(
							(Integer) currCondition.get(0),
							(BoundaryCondition) currCondition.get(1));
				}

				allBoundaryConditions.add(thermalBoundaryConditions);
			}

			/** --- Load PASSIVE SCALAR boundary conditions (if any) --- **/
			// Find the beginning of the passive scalar BC section
			if (numPassiveScalars > 0 && reaLines.get(i)
					.contains("***** PASSIVE SCALAR           "
							+ "1 BOUNDARY CONDITIONS *****")) {

				// Repeat the following for as many sets of passive scalar
				// BCs as there are
				for (int currScalarNum = 1; currScalarNum <= numPassiveScalars; currScalarNum++) {

					// Search for the current scalar header
					String passiveScalarHeader = "***** PASSIVE SCALAR           "
							+ currScalarNum + " BOUNDARY CONDITIONS *****";
					if (reaLines.get(i).contains(passiveScalarHeader)) {

						// Create a HashMap for the current passive scalar
						// boundary
						// conditions
						HashMap<Integer, BoundaryCondition> scalarBoundaryCondition = new HashMap<Integer, BoundaryCondition>();

						// Jump the iterator 1 line ahead and begin reading
						// in boundary
						// conditions
						i += 1;

						for (int j = 0; j < numThermalElements * 4; j++) {

							// Create the current boundary condition object
							currCondition = buildBoundaryConditionPair(reaLines,
									i, j);

							// Plug it (along with the unique edge ID it
							// corresponds to)
							// into the passive scalar boundary conditions
							// HashMap
							scalarBoundaryCondition.put(
									(Integer) currCondition.get(0),
									(BoundaryCondition) currCondition.get(1));
						}

						// Append the HashMap to the list of all passive
						// scalar
						// boundary conditions
						scalarBoundaryConditions.add(scalarBoundaryCondition);
					}

					// Append the list of all passive scalar boundary
					// condition
					// sets to the list of all boundary conditions
					allBoundaryConditions.add(scalarBoundaryConditions);
				}
			}
		}

		return allBoundaryConditions;
	}

	/**
	 * Loads the PRESOLVE/RESTART OPTIONS section of a reafile and returns the
	 * contents as a DataComponent of Entries. Each line is set an IEntry.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         PRESOLVE/RESTART OPTIONS section.
	 */
	private DataComponent loadPresolveRestartOpts(ArrayList<String> reaLines) {

		// Create a presolve/restart component to add entries
		DataComponent presolveRestart = new DataComponent();
		presolveRestart.setName("Pre-solve/Restart Options");
		presolveRestart.setDescription("Entries contained in the Pre-solve/"
				+ "Restart Options section of a Nek5000 reafile");
		presolveRestart.setId(8);

		IEntry entry;
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the presolve/restart options heading
			if (reaLines.get(i).contains("PRESOLVE/RESTART OPTIONS")) {

				// Grab the number indicating the length of the presolve/restart
				// options section (number of lines)
				int strIndex = reaLines.get(i).indexOf("PRESOLVE/RESTART");
				String numLinesStr = reaLines.get(i).substring(0, strIndex - 1)
						.trim();
				int numLines = Integer.parseInt(numLinesStr);

				// Jump the iterator 1 line ahead (from the header) and begin
				// reading in Entries
				i += 1;
				String currLine;
				String[] splitLine;
				String currValue;
				for (int j = 0; j < numLines; j++) {

					// Grab the current line
					currLine = reaLines.get(i + j);
					splitLine = currLine.trim().split("\\s+");

					// Create a Nek IEntry
					entry = makeNekEntry(false);

					// Construct the current value
					currValue = "";
					for (int k = 0; k < splitLine.length; k++) {
						if (k != splitLine.length - 1) {
							currValue += splitLine[k] + " ";
						} else {
							currValue += splitLine[k];
						}
					}

					// Set the name, value and ID
					entry.setName("Restart Option " + (j + 1));
					entry.setValue(currValue);
					entry.setId(j + 1);

					// Append to the DataComponent
					presolveRestart.addEntry(entry);
				}
			}
		}

		return presolveRestart;
	}

	/**
	 * Loads the INITIAL CONDITIONS section of a reafile and returns the
	 * contents as a DataComponent of Entries. Each line is set as an IEntry.
	 * Since Nek5000 no longer uses the Initial Conditions section, Entries are
	 * tagged as not ready and thus won't be exposed to the user.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         INITIAL CONDITIONS section.
	 */
	private DataComponent loadInitialConditions(ArrayList<String> reaLines) {

		// Create an initial conditions component to add entries
		DataComponent initialConditions = new DataComponent();
		initialConditions.setName("Initial Conditions");
		initialConditions.setDescription("Entries contained in the Initial "
				+ "Conditions section of a Nek5000 reafile");
		initialConditions.setId(9);

		IEntry entry;
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the initial conditions heading
			if (reaLines.get(i).contains("INITIAL CONDITIONS")) {

				// Grab the number indicating the length of the initial
				// conditions section (number of lines)
				int strIndex = reaLines.get(i).indexOf("INITIAL");
				String numLinesStr = reaLines.get(i).substring(0, strIndex - 1)
						.trim();
				int numLines = Integer.parseInt(numLinesStr);

				// Jump the iterator 1 line ahead (from the header) and begin
				// reading in Entries
				i += 1;
				String currLine;
				for (int j = 0; j < numLines; j++) {

					// Grab the current line
					currLine = reaLines.get(i + j);

					// Create a Nek IEntry
					entry = makeNekEntry(false);

					// Set the name, value and ID
					entry.setName("Initial Condition " + (j + 1));
					entry.setValue(currLine);
					entry.setId(j + 1);
					entry.setReady(false); // Don't need to expose to user

					// Append to the DataComponent
					initialConditions.addEntry(entry);
				}
			}
		}

		return initialConditions;
	}

	/**
	 * Loads the DRIVE FORCE DATA section of a reafile and returns the contents
	 * as a DataComponent of Entries. Each line is set as an IEntry.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the DRIVE
	 *         FORCE DATA section.
	 */
	private DataComponent loadDriveForceData(ArrayList<String> reaLines) {

		// Create a drive force data component to add entries
		DataComponent driveForceData = new DataComponent();
		driveForceData.setName("Drive Force Data");
		driveForceData.setDescription("Entries contained in the Drive Force "
				+ "Data section of a Nek5000 reafile");
		driveForceData.setId(10);

		IEntry entry;
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the drive force data heading
			if (reaLines.get(i).contains("***** DRIVE FORCE DATA *****")
					&& reaLines.get(i + 1)
							.contains("Lines of Drive force data follow")) {

				// Grab the number on the next line indicating the length of the
				// drive force data section (number of lines)
				int strIndex = reaLines.get(i + 1)
						.indexOf("Lines of Drive force data follow");
				String numLinesStr = reaLines.get(i + 1)
						.substring(0, strIndex - 1).trim();
				int numLines = Integer.parseInt(numLinesStr);

				// Jump the iterator 2 lines ahead (from the header) and begin
				// reading in Entries
				i += 2;
				String currLine;
				for (int j = 0; j < numLines; j++) {

					// Grab the current line
					currLine = reaLines.get(i + j);

					// Create a Nek IEntry
					entry = makeNekEntry(false);

					// Set the name, value and ID
					entry.setName("Drive Force Data " + (j + 1));
					entry.setValue(currLine);
					entry.setId(j + 1);
					entry.setReady(false); // Don't need to expose to user

					// Append to the DataComponent
					driveForceData.addEntry(entry);
				}
			}
		}

		return driveForceData;
	}

	/**
	 * Loads the VARIABLE PROPERTY DATA section of a reafile and returns the
	 * contents as a DataComponent of Entries. Each line is set as an IEntry.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         VARIABLE PROPERTY DATA section.
	 */
	private DataComponent loadVarPropertyData(ArrayList<String> reaLines) {

		// Create a variable property data component to add entries
		DataComponent varPropertyData = new DataComponent();
		varPropertyData.setName("Variable Property Data");
		varPropertyData.setDescription("Entries contained in the Variable "
				+ "Property Data section of a Nek5000 reafile");
		varPropertyData.setId(11);

		IEntry entry;
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the initial conditions heading
			if (reaLines.get(i).contains("***** Variable Property Data ****")
					&& reaLines.get(i + 1).contains("Lines follow")) {

				// Grab the number on the next line indicating the length of the
				// variable property data section (number of lines)
				int strIndex = reaLines.get(i + 1).indexOf("Lines follow");
				String numLinesStr = reaLines.get(i + 1)
						.substring(0, strIndex - 1).trim();
				int numLines = Integer.parseInt(numLinesStr);

				// Jump the iterator 2 lines ahead (from the header) and begin
				// reading in Entries
				i += 2;
				String currLine;
				for (int j = 0; j < numLines; j++) {

					// Grab the current line
					currLine = reaLines.get(i + j);

					// Create a Nek IEntry
					entry = makeNekEntry(false);

					// Set the name, value and ID
					entry.setName("Variable Property Data " + (j + 1));
					entry.setValue(currLine);
					entry.setId(j + 1);

					// Append to the DataComponent
					varPropertyData.addEntry(entry);
				}
			}
		}

		return varPropertyData;
	}

	/**
	 * Loads the HISTORY AND INTEGRAL DATA section of a reafile and returns the
	 * contents as a DataComponent of Entries. Each line is set as an IEntry.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         HISTORY AND INTEGRAL DATA section.
	 */
	private DataComponent loadHistoryIntegralData(ArrayList<String> reaLines) {

		// Create a history and integral data component to add entries
		DataComponent historyIntegralData = new DataComponent();
		historyIntegralData.setName("History and Integral Data");
		historyIntegralData.setDescription("Entries contained in the History "
				+ "and Integral Data section of a Nek5000 reafile");
		historyIntegralData.setId(12);

		IEntry entry;
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the initial conditions heading
			if (reaLines.get(i)
					.contains("***** HISTORY AND INTEGRAL DATA *****")
					&& reaLines.get(i + 1).contains("POINTS")) {

				// Grab the number on the next line indicating the length of the
				// History & Integral data section (number of lines)
				int strIndex = reaLines.get(i + 1).indexOf("POINTS");
				String numLinesStr = reaLines.get(i + 1)
						.substring(0, strIndex - 1).trim();
				int numLines = Integer.parseInt(numLinesStr);

				// Jump the iterator 2 lines ahead (from the header) and begin
				// reading in Entries
				i += 2;
				String currLine;
				for (int j = 0; j < numLines; j++) {

					// Grab the current line
					currLine = reaLines.get(i + j);

					// Create a Nek IEntry
					entry = makeNekEntry(false);

					/*
					 * Format string for history and integral data points:
					 * 
					 * < 100,000 elements (1x, 11a1, 1x, 4i5) => 100,000
					 * elements (1x, 11a1, 1x, 3i5, i10)
					 * 
					 * FIXME are no hist/int points for conj_ht example so this
					 * doesn't matter for now, but the history/integral section
					 * is formatting-sensitive and we will need to later figure
					 * out a way so that it's not up to the user to get the
					 * format correct
					 */

					// Set the name, value and ID
					entry.setName("Point " + (j + 1));
					entry.setValue(currLine);
					entry.setId(j + 1);

					// Append to the DataComponent
					historyIntegralData.addEntry(entry);
				}

			}

		}

		return historyIntegralData;
	}

	/**
	 * Loads the OUTPUT FIELD SPECIFICATION section of a reafile and returns the
	 * contents as a DataComponent of Entries. Each line is set as an IEntry.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         OUTPUT FIELD SPECIFICATION section.
	 */
	private DataComponent loadOutputFieldSpec(ArrayList<String> reaLines) {

		// Create a output field spec component to add entries
		DataComponent outputFieldSpec = new DataComponent();
		outputFieldSpec.setName("Output Field Specification");
		outputFieldSpec.setDescription("Entries contained in the Output Field "
				+ "Specification section of a Nek5000 reafile");
		outputFieldSpec.setId(13);

		IEntry entry;
		boolean isDiscrete;
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the initial conditions heading
			if (reaLines.get(i)
					.contains("***** OUTPUT FIELD SPECIFICATION *****")
					&& reaLines.get(i + 1).contains("SPECIFICATIONS FOLLOW")) {

				// Grab the number on the next line indicating the length of the
				// Output Field Specification section (number of lines)
				int strIndex = reaLines.get(i + 1)
						.indexOf("SPECIFICATIONS FOLLOW");
				String numLinesStr = reaLines.get(i + 1)
						.substring(0, strIndex - 1).trim();
				int numLines = Integer.parseInt(numLinesStr);

				// Jump the iterator 2 lines ahead (from the header) and begin
				// reading in Entries
				i += 2;
				String currLine;
				String[] splitLine;
				String currValue;
				String currName;
				for (int j = 0; j < numLines; j++) {

					// Grab the current line
					currLine = reaLines.get(i + j);
					splitLine = currLine.trim().split("\\s+");

					// Determine if the entry will have discrete values or not
					isDiscrete = (currLine.contains("COORDINATES")
							|| currLine.contains("VELOCITY")
							|| currLine.contains("PRESSURE")
							|| currLine.contains("TEMPERATURE"));

					// Create a Nek IEntry
					entry = makeNekEntry(isDiscrete);

					// Define the name and value
					currValue = ("T".equals(splitLine[0]) ? "YES"
							: ("F".equals(splitLine[0]) ? "NO" : splitLine[0]));
					currName = "";
					for (int k = 1; k < splitLine.length; k++) {
						if (k != splitLine.length - 1) {
							currName += splitLine[k] + " ";
						} else {
							currName += splitLine[k];
						}
					}

					// Set the name, value and ID
					entry.setName(currName);
					entry.setValue(currValue);
					entry.setId(j + 1);

					// Append to the DataComponent
					outputFieldSpec.addEntry(entry);
				}

			}

		}

		return outputFieldSpec;
	}

	/**
	 * Loads the OBJECT SPECIFICATION section of a reafile and returns the
	 * contents as a DataComponent of Entries. Each line is set as an IEntry.
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @return A DataComponent of Entries representing the contents of the
	 *         OBJECT SPECIFICATION section.
	 */
	private DataComponent loadObjectSpec(ArrayList<String> reaLines) {

		// Create a object specification component to add entries
		DataComponent objectSpec = new DataComponent();
		objectSpec.setName("Object Specification");
		objectSpec.setDescription("Entries contained in the Object "
				+ "Specification section of a Nek5000 reafile");
		objectSpec.setId(14);

		IEntry entry;
		// Begin reading in the lines
		for (int i = 0; i < reaLines.size(); i++) {

			// Search for the object specification heading
			if (reaLines.get(i).contains("***** OBJECT SPECIFICATION *****")) {

				// Jump the iterator 1 line ahead (from the header) and begin
				// reading in Entries
				i += 1;
				String currLine;
				String[] splitLine;
				String currValue;
				String currName;
				for (int j = 0; j < 4; j++) {

					// Grab the current line
					currLine = reaLines.get(i + j);
					splitLine = currLine.trim().split("\\s+");

					// Create a Nek IEntry
					entry = makeNekEntry(false);

					// Specify the name and value
					currValue = splitLine[0];
					currName = splitLine[1];

					// Set the name, value and ID
					entry.setName(currName);
					entry.setValue(currValue);
					entry.setId(j + 1);

					// Append to the DataComponent
					objectSpec.addEntry(entry);
				}
			}
		}

		return objectSpec;
	}

	/**
	 * Utility class to construct an IEntry with default Nek values.
	 * 
	 * @return Constructed IEntry with default Nek values.
	 */
	private IEntry makeNekEntry(boolean isDiscrete) {

		IEntry entry;

		// If entry's value can only be T/F
		if (isDiscrete) {
			entry = new DiscreteEntry();
			entry.setAllowedValues(Arrays.asList("NO", "YES"));

			entry.setName("Nek5000 Default Entry");
			entry.setDescription("");
			entry.setDefaultValue("NO");
			entry.setValue(entry.getDefaultValue());
		} else {
			entry = new StringEntry();
			entry.setName("Nek5000 Default Entry");
			entry.setDescription("");
			entry.setValue("");
		}
		return entry;
	}

	/**
	 * Utility class to trim a String of its whitespaces and returns any
	 * remaining sequences of chars as an ArrayList<?>, where ? is String,
	 * Integer, or Float
	 * 
	 * @param objectType
	 *            The type of the returned ArrayList.
	 * @param line
	 *            The String of mixed whitespace and non-whitespace chars to be
	 *            parsed into an ArrayList.
	 * @return An ArrayList of type objectType.class that contains any
	 *         non-whitespace sequences in line as elements.
	 */
	private ArrayList<?> parseLine(Class objectType, String line) {

		// Local declaration
		ArrayList<?> returnArray = null;

		// Break up the line into a list of non-whitespace elements
		String[] splitLine = line.trim().split("\\s+");
		ArrayList<String> stringArray = new ArrayList<String>(
				Arrays.asList(splitLine));

		// Parse to the objectType class specified
		if (objectType == String.class) {

			// Set the return
			returnArray = stringArray;
		}

		else if (objectType == Integer.class) {

			// Create an integer array
			ArrayList<Integer> intArray = new ArrayList<Integer>();

			// Cast the String array to an Integer array
			for (int j = 0; j < stringArray.size(); j++) {
				intArray.add(Integer.parseInt(stringArray.get(j)));
			}

			// Set the return
			returnArray = intArray;
		}

		else if (objectType == Float.class) {

			// Create a float array
			ArrayList<Float> floatArray = new ArrayList<Float>();

			// Cast the String array to a Float array
			for (int k = 0; k < stringArray.size(); k++) {
				floatArray.add(Float.parseFloat(stringArray.get(k)));
			}

			// Set the return
			returnArray = floatArray;
		}

		return returnArray;
	}

	/**
	 * Creates a pairing of a BoundaryCondition to its unique Edge ID, returned
	 * as an ArrayList of Objects (edge ID and BoundaryCondition are mixed type,
	 * necessitating the return to of type Object). The Edge ID is the first
	 * element, the the BoundaryCondition is the second element. This method
	 * works for all sets of BoundaryConditions (fluid, thermal, passive
	 * scalars).
	 * 
	 * @param reaLines
	 *            Lines of the reafile as an ArrayList of Strings.
	 * @param i
	 *            The reafile line at which the MESH DATA begins.
	 * @param j
	 *            The current line being read inside the MESH DATA section.
	 * @return An ArrayList containing the unique Edge ID in the first element,
	 *         and the associated BoundaryCondition in the second element.
	 */
	private ArrayList<Object> buildBoundaryConditionPair(
			ArrayList<String> reaLines, int i, int j) {

		// Local declarations
		String currLine;
		ArrayList<Float> currBoundaryValues = new ArrayList<Float>();

		int edgeId;
		ArrayList<Float> values;
		BoundaryCondition condition;
		BoundaryConditionType type;

		// Grab the current line
		currLine = reaLines.get(i + j);

		// Extract values from current boundary condition
		String[] splitLine;
		splitLine = currLine.trim().split("\\s+");

		for (int k = 1; k <= 7; k++) {
			currBoundaryValues.add(Float.parseFloat(splitLine[k]));
		}

		// Get the edge ID
		edgeId = (int) (4 * (currBoundaryValues.get(0) - 1)
				+ currBoundaryValues.get(1));

		// Create the boundary condition object
		condition = new BoundaryCondition();

		// Set the boundary condition type
		String rawType = splitLine[0];
		type = BoundaryConditionType.fromId(rawType);
		condition.setType(type);

		// Set the boundary condition values
		values = new ArrayList<Float>();
		for (int k = 2; k < 7; k++) {
			values.add(currBoundaryValues.get(k));
		}
		condition.setValues(values);

		// Create an Array of Objects to return a unique edge ID and boundary
		// condition object as a pair
		ArrayList<Object> boundaryPair = new ArrayList<Object>();
		boundaryPair.add(edgeId);
		boundaryPair.add(condition);

		return boundaryPair;
	}

	/**
	 * Returns the last ProblemProperties object constructed by the NekReader.
	 * Intended to be passed on as input for NekWriter.writeREAFile().
	 * 
	 * @return The last ProblemProperties object constructed
	 */
	public ProblemProperties getLastProperties() {
		return properties;
	}

	/**
	 * Setter method for the factory which will produce views and controllers
	 * for the objects read from the Nek file.
	 * 
	 * @param factory
	 *            The reader's new factory
	 */
	public void setControllerFactory(IControllerProviderFactory factory) {
		this.factory = factory;
	}

}
