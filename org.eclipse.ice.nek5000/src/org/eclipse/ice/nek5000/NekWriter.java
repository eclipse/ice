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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryCondition;
import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryConditionType;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;

/**
 * This class is responsible for writing the contents of a Component collection
 * into a file appropriate to be used as a Nek5000 .rea file.
 * 
 * @author Anna Wojtowicz
 * 
 */
public class NekWriter implements IComponentVisitor {

	/*
	 * HashMap of 13 Components to define a Nek5000 problem, keyed by Component
	 * name.
	 */
	private HashMap<String, Component> componentMap;

	/*
	 * Properties of the Nek problem (number of dimensions, number of thermal
	 * elements, number of fluid elements, and number of passive scalar sets),
	 * as defined by the NekReader.
	 */
	private ProblemProperties properties;

	/**
	 * Nullary constructor
	 */
	public NekWriter() {
		super();
		componentMap = new HashMap<String, Component>();
	}

	/**
	 * Reads in an ArrayList of Components, creates a HashMap keyed by Component
	 * name, and then writes the contents into the specified output file using
	 * formatting appropriate for a Nek5000 reafile. Verifies the list of
	 * Components is correctly constructed before writing.
	 * 
	 * @param components
	 *            An ArrayList of Components, can be assumed to be a mix of
	 *            DataComponents and MeshComponents
	 * @param outputFile
	 *            The output file to write to
	 * @param properties
	 *            Properties of the Nek problem
	 * @throws FileNotFoundException
	 *             Thrown when the output file cannot be found
	 * @throws IOException
	 *             Thrown when any of the writeSomething(...) methods fail to
	 *             write to the OutputStream
	 * 
	 */
	public void writeReaFile(ArrayList<Component> components, File outputFile,
			ProblemProperties properties)
					throws FileNotFoundException, IOException {

		// Check that all input parameters are valid
		if (components != null && outputFile.isFile() && properties != null
				&& properties.getNumDimensions() > 0
				&& properties.getNumThermalElements() >= 0
				&& properties.getNumFluidElements() >= 0
				&& properties.getNumPassiveScalars() >= 0) {

			// Local declarations
			OutputStream stream = null;
			Component currComponent;

			// Set the problem properties
			this.properties = properties;

			// Set the output stream to the output file
			stream = new FileOutputStream(outputFile);

			// Verify all the Components are valid and then add componentMap Map
			for (int i = 0; i < components.size(); i++) {
				currComponent = components.get(i);
				currComponent.accept(this);
			}

			// Write the output file sections
			writeParameters(stream);
			writePassiveScalarData(stream);
			writeLogicalSwitches(stream);
			writePreNekAxes(stream);
			writeMesh(stream);
			writePresolveRestartOpts(stream);
			writeInitialConditions(stream);
			writeDriveForceData(stream);
			writeVarPropertyData(stream);
			writeHistoryIntegralData(stream);
			writeOutputFieldSpec(stream);
			writeObjectSpec(stream);
			writeICEHeader(stream);

			// Close the output stream
			stream.close();

		}

		return;
	}

	/**
	 * Writes an ICE header at the top of the reafile, providing the date, time
	 * and hostname where the file was generated.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws UnknownHostException
	 *             Thrown when the host cannot be resolved
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeICEHeader(OutputStream stream)
			throws UnknownHostException, IOException {

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String date = dateFormat.format(new Date());
		String user = System.getProperty("user.name");
		String hostname = "";
		hostname = InetAddress.getLocalHost().getHostName();

		String iceHeader = String.format(
				"C *** Nek5000 reafile generated by ICE ***\n"
						+ "C  (Eclipse Integrated Computational Environment)\n"
						+ "C\n" + "C  Created:   %-30s\n"
						+ "C  User:      %-30s\n" + "C  Hostname:  %-30s",
				date, user, hostname);

		// Write to the output stream
		byte[] byteArray = iceHeader.getBytes();
		stream.write(byteArray);

		return;
	}

	/**
	 * Grabs the PARAMETERS DataComponent from the componentMap and writes the
	 * contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeParameters(OutputStream stream) throws IOException {

		// Local declarations
		DataComponent parameters = (DataComponent) componentMap
				.get("Parameters");
		int numEntries = parameters.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;
		String currName;
		String currDesc;
		String currLine;

		// Construct the header
		String paramHeader = String.format(
				" ****** PARAMETERS *****\n" + "    %9s     NEKTON VERSION\n"
						+ "            %d DIMENSIONAL RUN\n"
						+ "          %3d PARAMETERS FOLLOW\n",
				properties.getNekVersion(), properties.getNumDimensions(),
				numEntries);

		// Write the parameters header
		byte[] byteArray = paramHeader.getBytes();
		stream.write(byteArray);

		// Write the parameters
		for (int i = 0; i < numEntries; i++) {

			// Define the current parameter
			currEntry = parameters.retrieveAllEntries().get(i);
			currName = currEntry.getName();
			currDesc = currEntry.getDescription();
			currValue = currEntry.getValue();
			currLine = String.format("      %-14s     %s %s\n", currValue,
					currName, currDesc);

			// Write to the output stream
			byteArray = currLine.getBytes();
			stream.write(byteArray);
		}

		return;
	}

	/**
	 * Grabs the PASSIVE SCALARA DATA DataComponent from the componentMap and
	 * writes the contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writePassiveScalarData(OutputStream stream)
			throws IOException {

		// Local declarations
		DataComponent passiveScalars = (DataComponent) componentMap
				.get("Passive Scalar Data");
		int numEntries = passiveScalars.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;
		String currLine;

		// Construct the header
		String passiveScalarsHeader = String
				.format("    %3s  Lines of passive scalar data follows"
						+ "2 CONDUCT; 2RHOCP\n", numEntries);

		// Write the passive scalars header
		byte[] byteArray = passiveScalarsHeader.getBytes();
		stream.write(byteArray);

		// Write the passive scalars
		for (int i = 0; i < numEntries; i++) {

			// Define the current passive scalar line
			currEntry = passiveScalars.retrieveAllEntries().get(i);
			currValue = currEntry.getValue();
			currLine = String.format("      %-10s\n", currValue);

			// Write to the output stream
			byteArray = currLine.getBytes();
			stream.write(byteArray);
		}

		return;
	}

	/**
	 * Grabs the LOGICAL SWITCHES DataComponent from the componentMap and writes
	 * the contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeLogicalSwitches(OutputStream stream) throws IOException {

		// Local declarations
		DataComponent logicalSwitches = (DataComponent) componentMap
				.get("Logical Switches");
		int numEntries = logicalSwitches.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;
		String currName;
		String currLine;
		String currDesc;

		// Construct the header
		String switchesHeader = String
				.format("         %3s  LOGICAL SWITCHES FOLLOW\n", numEntries);

		// Write the logical switches header
		byte[] byteArray = switchesHeader.getBytes();
		stream.write(byteArray);

		for (int i = 0; i < numEntries; i++) {

			// Define the current logical switch
			currEntry = logicalSwitches.retrieveAllEntries().get(i);
			currName = currEntry.getName();
			currDesc = currEntry.getDescription();
			currValue = ("YES".equals(currEntry.getValue()) ? "T"
					: ("NO".equals(currEntry.getValue()) ? "F"
							: currEntry.getValue()));

			// Construct the current line
			if (currName.contains("IFNAV") && currName.contains("IFADVC")) {
				currLine = String.format("  %s IFNAV & IFADVC %s\n", // This
																		// entry's
																		// name
																		// has
																		// two
																		// ampersands
																		// because
						currValue, currDesc); // of an Eclipse forms bug, so we
												// just write the
			} // name out here
			else if (currName.contains("IFTMSH")) {
				currLine = String.format("  %s %s %s\n", currValue, currName,
						currDesc);
			} else {
				currLine = String.format("  %s     %s\n", currValue, currName);
			}
			// Write to the output stream
			byteArray = currLine.getBytes();
			stream.write(byteArray);
		}
		return;
	}

	/**
	 * Grabs the PRE-NEK AXES DataComponent from the componentMap and writes the
	 * contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writePreNekAxes(OutputStream stream) throws IOException {

		// Local declarations
		DataComponent preNekAxes = (DataComponent) componentMap
				.get("Pre-Nek Axes");
		int numEntries = preNekAxes.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;
		String currName;
		String currLine;
		byte[] byteArray;

		for (int i = 0; i < numEntries; i++) {

			// Define the current pre-nek axes
			currEntry = preNekAxes.retrieveAllEntries().get(i);
			currValue = currEntry.getValue();
			currName = currEntry.getName();
			currLine = String.format("   %s %s\n", currValue, currName);

			// Write to the output stream
			byteArray = currLine.getBytes();
			stream.write(byteArray);
		}

		return;
	}

	/**
	 * Grabs the MESH DATA MeshComponent from the componentMap and writes the
	 * contents to the specified OutputStream. In the process of iterating
	 * through each element/quad, this method also constructs 2+ ArrayLists of
	 * Strings (thermal, fluid and if any passive scalar BCs) representing lines
	 * of boundary conditions in a reafile. These ArrayLists are passed into the
	 * writeBoundaryConditions(...) method.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeMesh(OutputStream stream) throws IOException {

		// Local declarations
		MeshComponent mesh = (MeshComponent) componentMap.get("Mesh Data");
		NekPolygonController currQuad;
		AbstractController currEdge;
		int currEdgeId;
		ArrayList<VertexController> currVertices = new ArrayList<VertexController>();
		String currValue;

		// String buffer used to construct the mesh data, as the MeshComponent
		// must be read first before its header can be written, preventing a
		// simple top-down writing to the output stream
		StringBuffer buffer = new StringBuffer();
		ArrayList<String> bufferArray = new ArrayList<String>();

		ArrayList<Float> xCoords = new ArrayList<Float>();
		ArrayList<Float> yCoords = new ArrayList<Float>();

		BoundaryCondition currBC;
		ArrayList<Float> currBCValues = new ArrayList<Float>();
		ArrayList<String> thermalBCs = new ArrayList<String>();
		ArrayList<String> fluidBCs = new ArrayList<String>();
		ArrayList<ArrayList<String>> passiveScalarBCs = null;

		// Iterate through the mesh elements
		for (int i = 0; i < mesh.getPolygons().size(); i++) {

			// Define the current mesh element
			currQuad = (NekPolygonController) mesh.getPolygons().get(i);
			currValue = String.format(
					"           ELEMENT%6s [ %4s]" // FORMAT:
													// 18X,I6,4X,I3,A1,11x,i5
							+ "  GROUP   %5s\n",
					(i + 1), currQuad.getPolygonProperties().getMaterialId(),
					currQuad.getPolygonProperties().getGroupNum());

			// Write the mesh element header
			buffer.append(currValue);

			/* --- Construct mesh elements --- */

			// Check each descendent vertex. If it is not yet in the list of
			// vertices, add it
			for (AbstractController entity : currQuad
					.getEntitiesByCategory("Edges")) {
				for (AbstractController v : entity
						.getEntitiesByCategory("Vertices")) {
					if (!currVertices.contains(v)) {
						currVertices.add((VertexController) v);
					}
				}
			}

			// Extract the x, y coordinates of the Quad's vertices
			for (int k = 0; k < 4; k++) {
				xCoords.add((float) currVertices.get(k).getLocation()[0]);
				yCoords.add((float) currVertices.get(k).getLocation()[1]);
			}

			// Iterate through the Edges of the current Quad
			for (int j = 0; j < 4; j++) {

				// Define the current edge
				currEdge = currQuad.getEntitiesByCategory("Edges").get(j);
				currEdgeId = Integer.valueOf(currEdge.getProperty("Id"));

				/*
				 * Boundary condition format strings:
				 * 
				 * < 1,000 elements (1X, A3, 2I3, 5G14.6) < 100,000 elements
				 * (1X, A3, I5, I1, 5G14.6) else { (1X, A3, I10, I1, 5G14.6) }
				 * FIXME assuming only the case of < 1k elements
				 */

				/* --- Construct fluid boundary condition --- */

				// Grab the current edge's fluid boundary conditions
				currBC = currQuad.getFluidBoundaryCondition(currEdgeId);

				// Check if the current edge has a valid fluid BC
				if (currBC.getType() != BoundaryConditionType.None) {

					// Get its values
					currBCValues = currBC.getValues();

					// Construct the proper string format
					currValue = String.format(
							" %-3s%3d%3d%14.7G%14.7G%14.7G%14.7G%14.7G\n",
							currBC.getType().id, (i + 1), (j + 1),
							currBCValues.get(0), currBCValues.get(1),
							currBCValues.get(2), currBCValues.get(3),
							currBCValues.get(4));

					// Add to fluid boundary conditions list
					fluidBCs.add(currValue);
				}

				/* --- Construct thermal boundary condition --- */

				// Grab the current edge's thermal boundary condition
				currBC = currQuad.getThermalBoundaryCondition(currEdgeId);

				// Check if the current Quad has a valid thermal BC
				if (currBC.getType() != BoundaryConditionType.None) {

					// Get its values
					currBCValues = currBC.getValues();

					// Construct the proper string format
					currValue = String.format(
							" %-3s%3d%3d%14.7G%14.7G%14.7G%14.7G%14.7G\n",
							currBC.getType().id, (i + 1), (j + 1),
							currBCValues.get(0), currBCValues.get(1),
							currBCValues.get(2), currBCValues.get(3),
							currBCValues.get(4));

					// Add to thermal boundary conditions list
					thermalBCs.add(currValue);
				}

				/* --- Construct passive scalar boundary condition line --- */

				if (properties.getNumPassiveScalars() > 0) {

					passiveScalarBCs = new ArrayList<ArrayList<String>>(
							properties.getNumPassiveScalars());

					// Iterate through as many passive scalars as there are
					for (int k = 1; k <= properties
							.getNumPassiveScalars(); k++) {

						// Grab the current element's kth passive scalar
						// boundary condition
						currBC = currQuad.getOtherBoundaryCondition(currEdgeId,
								k);

						// Check if the current Quad has a valid passive scalar
						// BC
						if (currBC.getType() != BoundaryConditionType.None) {

							// Get its values
							currBCValues = currBC.getValues();

							// Construct the proper string format
							currValue = String.format(
									" %-3s%3d%3d%14.7G%14.7G%14.7G%14.7G%14.7G\n",
									currBC.getType().id, (i + 1), (j + 1),
									currBCValues.get(0), currBCValues.get(1),
									currBCValues.get(2), currBCValues.get(3),
									currBCValues.get(4));

							// Add to passive scalar boundary condition list
							passiveScalarBCs.get(k).add(currValue);
						}
					}

				}

			}

			// Write the coordinates to the buffer
			currValue = String.format(
					" %9.6G     %9.6G     %9.6G     %9.6G\n"
							+ " %9.6G     %9.6G     %9.6G     %9.6G\n",
					xCoords.get(0), xCoords.get(1), xCoords.get(2),
					xCoords.get(3), yCoords.get(0), yCoords.get(1),
					yCoords.get(2), yCoords.get(3));
			buffer.append(currValue);

			// Clear the coordinates for the next edge
			xCoords.clear();
			yCoords.clear();

		}

		// Update the ProblemProperties if any mesh element counts changed
		updateProperties(properties.getNumDimensions(),
				thermalBCs.isEmpty() ? properties.getNumThermalElements()
						: thermalBCs.size() / 4,
				fluidBCs.isEmpty() ? properties.getNumFluidElements()
						: fluidBCs.size() / 4,
				passiveScalarBCs == null ? 0 : passiveScalarBCs.size() / 4);

		// Construct the header
		String meshHeader = String.format(
				"  *** MESH DATA ***\n"
						+ "      %3d      %3d      %3d           NEL,NDIM,NELV\n",
				properties.getNumThermalElements(),
				properties.getNumDimensions(),
				properties.getNumFluidElements());

		// Write the mesh data header to the beginning of the StringBuffer
		buffer.insert(0, meshHeader);

		// Write the StringBuffer to the OutputStream
		byte[] byteArray = buffer.toString().getBytes();
		stream.write(byteArray);

		// Write curved side data
		writeCurvedSideData(stream);

		// Write boundary conditions
		writeBoundaryConditions(stream, fluidBCs, thermalBCs, passiveScalarBCs);

		return;
	}

	/**
	 * Grabs the CURVED SIDE DATA MeshComponent from the componentMap and writes
	 * the contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeCurvedSideData(OutputStream stream) throws IOException {

		// Local declarations
		MeshComponent curvedSides = (MeshComponent) componentMap
				.get("Curved Side Data");
		int numEntries = 0;

		// Construct the header
		String curvedSidesHeader = String
				.format("  ***** CURVED SIDE DATA *****\n"
						+ "   %3s Curved sides follow "
						+ "IEDGE,IEL,CURVE(I),I=1,5, CCURVE\n", numEntries);

		// Write the curved sides header
		byte[] byteArray = curvedSidesHeader.getBytes();
		stream.write(byteArray);

		// TODO implement me!

		return;
	}

	/**
	 * Reads in ArrayLists of boundary conditions constructed by writeMesh(...)
	 * and writes them to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @param fluidBCs
	 *            ArrayList of Strings representing the lines of fluid boundary
	 *            conditions in a reafile
	 * @param thermalBCs
	 *            ArrayList of Strings representing the lines of thermal
	 *            boundary conditions in a reafile
	 * @param passiveScalarBCs
	 *            ArrayList of ArrayLists of Strings representing all possible
	 *            sets (if any) of passive scalar boundary conditions, where
	 *            each String represents a line in a reafile
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeBoundaryConditions(OutputStream stream,
			ArrayList<String> fluidBCs, ArrayList<String> thermalBCs,
			ArrayList<ArrayList<String>> passiveScalarBCs) throws IOException {

		// Construct the header
		String boundaryConditionHeader = "  ***** BOUNDARY CONDITIONS *****\n";

		// Write the boundary conditions header
		byte[] byteArray = boundaryConditionHeader.getBytes();
		stream.write(byteArray);

		// Check that there are fluid boundary conditions
		if (!fluidBCs.isEmpty()) {
			// Write the fluid boundary conditions header
			String fluidHeader = "  ***** FLUID   BOUNDARY CONDITIONS *****\n";
			byteArray = fluidHeader.getBytes();
			stream.write(byteArray);

			// Write fluid boundary condition lines
			for (int i = 0; i < fluidBCs.size(); i++) {
				byteArray = fluidBCs.get(i).getBytes();
				stream.write(byteArray);
			}
		} else {
			String noFluidHeader = "  ***** NO FLUID   BOUNDARY CONDITIONS *****\n";
			byteArray = noFluidHeader.getBytes();
			stream.write(byteArray);
		}

		// Check that there are thermal boundary conditions
		if (!thermalBCs.isEmpty()) {
			// Write the thermal boundary conditions header
			String thermalHeader = "  ***** THERMAL BOUNDARY CONDITIONS *****\n";
			byteArray = thermalHeader.getBytes();
			stream.write(byteArray);

			// Write thermal boundary condition lines
			for (int i = 0; i < thermalBCs.size(); i++) {
				byteArray = thermalBCs.get(i).getBytes();
				stream.write(byteArray);
			}
		} else {
			String noThermalHeader = "  ***** NO THERMAL BOUNDARY CONDITIONS *****\n";
			byteArray = noThermalHeader.getBytes();
			stream.write(byteArray);
		}

		// Write passive scalar boundary condition lines
		if (properties.getNumPassiveScalars() > 0) {

			ArrayList<String> currPassiveScalarBC = new ArrayList<String>();

			// Write passive scalar boundary condition lines
			for (int i = 0; i < properties.getNumPassiveScalars(); i++) {

				// Define the current passive scalar set
				currPassiveScalarBC = passiveScalarBCs.get(i);

				// Write the passive scalar boundary conditions header
				String passiveScalarHeader = String.format(
						"  ***** PASSIVE SCALAR         %3s BOUNDARY CONDITIONS *****\n",
						(i + 1));
				byteArray = passiveScalarHeader.getBytes();
				stream.write(byteArray);

				// Iterate through the boundary conditions of the set
				for (int j = 0; j < currPassiveScalarBC.size(); j++) {

					// Write the current passive scalar boundary condition
					byteArray = currPassiveScalarBC.get(j).getBytes();
					stream.write(byteArray);
				}
			}
		}

		return;
	}

	/**
	 * Grabs the PRE-SOLVE/RESTART OPTIONS DataComponent from the componentMap
	 * and writes the contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writePresolveRestartOpts(OutputStream stream)
			throws IOException {

		// Local declarations
		DataComponent presolveRestartOpts = (DataComponent) componentMap
				.get("Pre-solve/Restart Options");
		int numEntries = presolveRestartOpts.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;

		// Construct the header
		String presolveRestartOptsHeader = String
				.format("%3s PRESOLVE/RESTART OPTIONS  *****\n", numEntries);

		// Write the presolve/restart options header
		byte[] byteArray = presolveRestartOptsHeader.getBytes();
		stream.write(byteArray);

		for (int i = 0; i < numEntries; i++) {

			// Define the current pre-solve/restart option
			currEntry = presolveRestartOpts.retrieveAllEntries().get(i);
			currValue = currEntry.getValue() + "\n";

			// Write to the output stream
			byteArray = currValue.getBytes();
			stream.write(byteArray);
		}

		return;
	}

	/**
	 * Grabs the INITIAL CONDITIONS DataComponent from the componentMap and
	 * writes the contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeInitialConditions(OutputStream stream)
			throws IOException {

		// Local declarations
		DataComponent initialConditions = (DataComponent) componentMap
				.get("Initial Conditions");
		int numEntries = initialConditions.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;

		// Construct the header
		String initialConditionsHeader = String
				.format("%3s         INITIAL CONDITIONS *****\n", numEntries);

		// Write the initial conditions header
		byte[] byteArray = initialConditionsHeader.getBytes();
		stream.write(byteArray);

		for (int i = 0; i < numEntries; i++) {

			// Define the current initial condition
			currEntry = initialConditions.retrieveAllEntries().get(i);
			currValue = currEntry.getValue() + "\n";

			// Write to the output stream
			byteArray = currValue.getBytes();
			stream.write(byteArray);

		}

		return;
	}

	/**
	 * Grabs the DRIVE FORCE DATA DataComponent from the componentMap and writes
	 * the contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeDriveForceData(OutputStream stream) throws IOException {

		// Local declarations
		DataComponent driveForceData = (DataComponent) componentMap
				.get("Drive Force Data");
		int numEntries = driveForceData.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;

		// Construct the header
		String driveForceDataHeader = String.format(
				"  ***** DRIVE FORCE DATA ***** BODY FORCE, FLOW, Q\n"
						+ "          %3s                 Lines of Drive force data follow\n",
				numEntries);

		// Write the drive force data header
		byte[] byteArray = driveForceDataHeader.getBytes();
		stream.write(byteArray);

		for (int i = 0; i < numEntries; i++) {

			// Define the current drive force datum
			currEntry = driveForceData.retrieveAllEntries().get(i);
			currValue = currEntry.getValue() + "\n";

			// Write to the output stream
			byteArray = currValue.getBytes();
			stream.write(byteArray);
		}

		return;
	}

	/**
	 * Grabs the VARIABLE PROPERTY DATA DataComponent from the componentMap and
	 * writes the contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeVarPropertyData(OutputStream stream) throws IOException {

		// Local declarations
		DataComponent varPropertyData = (DataComponent) componentMap
				.get("Variable Property Data");
		int numEntries = varPropertyData.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;

		// Construct the header
		String varPropertyDataHeader = String
				.format("  ***** Variable Property Data ***** Overrrides Parameter data.\n"
						+ "%3s Lines follow.\n", numEntries);

		// Write the variable property data header
		byte[] byteArray = varPropertyDataHeader.getBytes();
		stream.write(byteArray);

		for (int i = 0; i < numEntries; i++) {

			// Define the current variable property datum
			currEntry = varPropertyData.retrieveAllEntries().get(i);
			currValue = currEntry.getValue() + "\n";

			// Write to the output stream
			byteArray = currValue.getBytes();
			stream.write(byteArray);
		}

		return;
	}

	/**
	 * Grabs the HISTORY & INTEGRAL DATA DataComponent from the componentMap and
	 * writes the contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeHistoryIntegralData(OutputStream stream)
			throws IOException {

		// Local declarations
		DataComponent historyIntegralData = (DataComponent) componentMap
				.get("History and Integral Data");
		int numEntries = historyIntegralData.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;

		// Construct the header
		String historyIntegralDataHeader = String.format(
				"  ***** HISTORY AND INTEGRAL DATA *****\n"
						+ "          %3s   POINTS.  Hcode, I,J,H,IEL\n",
				numEntries);

		// Write the history and integral data header
		byte[] byteArray = historyIntegralDataHeader.getBytes();
		stream.write(byteArray);

		for (int i = 0; i < numEntries; i++) {

			// Define the current history and integral datum
			currEntry = historyIntegralData.retrieveAllEntries().get(i);
			currValue = currEntry.getValue() + "\n";

			// Write to the output stream
			byteArray = currValue.getBytes();
			stream.write(byteArray);

		}

		return;
	}

	/**
	 * Grabs the OUTPUT FIELD SPECIFCATION DataComponent from the componentMap
	 * and writes the contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeOutputFieldSpec(OutputStream stream) throws IOException {

		// Local declarations
		DataComponent outputFieldSpec = (DataComponent) componentMap
				.get("Output Field Specification");
		int numEntries = outputFieldSpec.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;
		String currName;
		String currLine;

		// Construct the header
		String outputFieldSpecHeader = String
				.format("  ***** OUTPUT FIELD SPECIFICATION *****\n"
						+ " %3s  SPECIFICATIONS FOLLOW\n", numEntries);

		// Write the output field specification header
		byte[] byteArray = outputFieldSpecHeader.getBytes();
		stream.write(byteArray);

		for (int i = 0; i < numEntries; i++) {

			// Define the current output field spec
			currEntry = outputFieldSpec.retrieveAllEntries().get(i);
			currName = currEntry.getName();
			currValue = ("YES".equals(currEntry.getValue()) ? "T"
					: ("NO".equals(currEntry.getValue()) ? "F"
							: currEntry.getValue()));
			currLine = String.format("  %s       %s\n", currValue, currName);

			// Write to the output stream
			byteArray = currLine.getBytes();
			stream.write(byteArray);

		}

		return;
	}

	/**
	 * Grabs the OBJECT SPECIFICATION DataComponent from the componentMap and
	 * writes the contents to the specified OutputStream.
	 * 
	 * @param stream
	 *            The OutputStream to write to
	 * @throws IOException
	 *             Thrown when writing to OutputStream fails
	 */
	private void writeObjectSpec(OutputStream stream) throws IOException {

		// Local declarations
		DataComponent objectSpec = (DataComponent) componentMap
				.get("Object Specification");
		int numEntries = objectSpec.retrieveAllEntries().size();
		IEntry currEntry;
		String currValue;
		String currName;
		String currLine;

		// Construct the header
		String objectSpecHeader = "  ***** OBJECT SPECIFICATION *****\n";

		// Write the object specification header
		byte[] byteArray = objectSpecHeader.getBytes();
		stream.write(byteArray);

		for (int i = 0; i < numEntries; i++) {

			// Define the current object spec
			currEntry = objectSpec.retrieveAllEntries().get(i);
			currValue = currEntry.getValue();
			currName = currEntry.getName();
			currLine = String.format("     %3s %-7s Objects\n", currValue,
					currName);

			// Write to the output stream
			byteArray = currLine.getBytes();
			stream.write(byteArray);

		}

		return;
	}

	/*
	 * Overrides the IComponentVisitor visit method. Verifies the DataComponent
	 * is non-null and then adds it to the componentMap HashMap.
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor#visit
	 * (org.eclipse.ice.datastructures.form.DataComponent)
	 */
	@Override
	public void visit(DataComponent component) {

		// Check the component is valid and then add to HashMap
		if (component != null) {
			componentMap.put(component.getName(), component);
		}
		return;
	}

	/*
	 * Overrides the IComponentVisitor visit method. Verifies the MeshComponent
	 * is non-null and then adds it to the componentMap HashMap.
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor#visit
	 * (org.eclipse.ice.datastructures.form.mesh.MeshComponent)
	 */
	@Override
	public void visit(MeshComponent component) {

		// Check the component is valid and then add to HashMap
		if (component != null) {
			componentMap.put(component.getName(), component);
		}
		return;
	}

	/**
	 * Updates the current NekWriter's ProblemProperties in the event that any
	 * initial variables may have changed (such as the number of mesh elements
	 * being changed through the addition/removal in the MeshEditor)
	 * 
	 * @param numDimensions
	 *            Number of dimensions of the current problem (cannot be
	 *            changed)
	 * @param newThermalElements
	 *            New number of elements with thermal BCs
	 * @param newFluidElements
	 *            New number of elements with fluid BCs
	 * @param newPassiveScalars
	 *            New number of passive scalar sets
	 */
	public void updateProperties(int numDimensions, int newThermalElements,
			int newFluidElements, int newPassiveScalars) {

		// Update the Writer's ProblemProperties if any mesh element counts
		// changed
		if (properties.getNumThermalElements() != newThermalElements
				|| properties.getNumFluidElements() != newFluidElements
				|| properties.getNumPassiveScalars() != newPassiveScalars) {

			properties = new ProblemProperties(numDimensions,
					newThermalElements, newFluidElements, newPassiveScalars);
		}

		return;
	}

	@Override
	public void visit(ResourceComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TableComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MatrixComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(GeometryComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MasterDetailsComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TreeComposite component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IReactorComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TimeDataComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AdaptiveTreeComposite component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EMFComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ListComponent component) {
		// TODO Auto-generated method stub

	}
}
