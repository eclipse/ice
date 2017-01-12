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
package org.eclipse.ice.tests.nek5000;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.modeling.base.IController;
import org.eclipse.eavp.viz.modeling.properties.MeshCategory;
import org.eclipse.eavp.viz.modeling.properties.MeshProperty;
import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryCondition;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.ice.nek5000.NekReader;
import org.junit.Test;

/**
 * Tests the methods of the NekReader class. Tests are broken down by Nek5000
 * examples.
 * 
 * @author Anna Wojtowicz
 *
 */
public class NekReaderTester {

	/**
	 * Tests the NekReader methods for the conj_ht example.
	 */
	@Test
	public void checkConj_ht() {

		// Create a NekReader to test
		NekReader reader = new NekReader();

		// Give it a test factory to use
		reader.setControllerFactory(new TestNekControllerFactory());

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "nek5000Data";
		String filePath = userDir + separator + "conj_ht.rea";
		File testFile = new File(filePath);

		// Try to read in invalid .rea file
		File fakeFile = null;
		ArrayList<Component> component = null;
		try {
			component = reader.loadREAFile(fakeFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find fake Nek input file");
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from fake Nek input file");
			e.printStackTrace();
		}
		assertTrue(component == null);

		// Load the .rea file and parse the contents into Components
		try {
			component = reader.loadREAFile(testFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find Nek input file: " + testFile.toString());
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from Nek input file: " + testFile.toString());
			e.printStackTrace();
		}

		/* --- Checking PARAMETERS component --- */

		// Specify the Parameter DataComponent
		DataComponent paramComponent = (DataComponent) component.get(0);
		String paramName = "Parameters";
		String paramDescription = "Entries contained in the Parameters "
				+ "section of a Nek5000 reafile";

		// Check default values of the Parameter component
		assertNotNull(paramComponent);
		assertEquals(paramName, paramComponent.getName());
		assertEquals(paramDescription, paramComponent.getDescription());
		assertEquals(2, paramComponent.getId());

		// Check the Parameters component contains 103 non-null entries
		assertEquals(103, paramComponent.retrieveAllEntries().size());
		for (int i = 0; i < 103; i++) {
			assertNotNull(paramComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PASSIVE SCALAR DATA component --- */

		DataComponent passiveScalData = (DataComponent) component.get(1);
		String passiveScalDataName = "Passive Scalar Data";
		String passiveScalDataDesc = "Entries contained in the Passive "
				+ "Scalar Data section of a Nek5000 reafile";

		// Check default values of the Passive Scalar Data component
		assertNotNull(passiveScalData);
		assertEquals(passiveScalDataName, passiveScalData.getName());
		assertEquals(passiveScalDataDesc, passiveScalData.getDescription());
		assertEquals(3, passiveScalData.getId());

		// Check the component is empty for this example
		assertEquals(0, passiveScalData.retrieveAllEntries().size());

		/* --- Checking LOGICAL FLAGS component --- */

		// Specify the Logical Switches DataComponent
		DataComponent switchesComponent = (DataComponent) component.get(2);
		String switchesName = "Logical Switches";
		String switchesDescription = "Entries contained in the Logical Switches "
				+ "section of a Nek5000 reafile";

		// Check default values of the Logical Switches component
		assertNotNull(switchesComponent);
		assertEquals(switchesName, switchesComponent.getName());
		assertEquals(switchesDescription, switchesComponent.getDescription());
		assertEquals(4, switchesComponent.getId());

		// Check the DataComponent contains 13 non-null logical switches
		assertEquals(13, switchesComponent.retrieveAllEntries().size());
		for (int i = 0; i < 13; i++) {
			assertNotNull(switchesComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PRE-NEK AXES component --- */

		DataComponent preNekAxes = (DataComponent) component.get(3);
		String preNekAxesName = "Pre-Nek Axes";
		String preNekAxesDesc = "Entries contained in the Pre-Nek Axes "
				+ "section of a Nek5000 reafile";

		// Check default values of the Pre-Nek Axes component
		assertNotNull(preNekAxes);
		assertEquals(preNekAxesName, preNekAxes.getName());
		assertEquals(preNekAxesDesc, preNekAxes.getDescription());
		assertEquals(5, preNekAxes.getId());

		// Check the Pre-Nek entry is non-null
		assertEquals(1, preNekAxes.retrieveAllEntries().size());
		assertNotNull(preNekAxes.retrieveAllEntries().get(0));

		/* --- Checking MESH component and assigned BOUNDARY CONDITIONS --- */

		MeshComponent meshComponent = (MeshComponent) component.get(4);
		String meshName = "Mesh Data";
		String meshDescription = "Elements contained in the Mesh section of "
				+ "a Nek5000 reafile";

		// Check default values of the mesh component
		assertNotNull(meshComponent);
		assertEquals(meshName, meshComponent.getName());
		assertEquals(meshDescription, meshComponent.getDescription());
		assertEquals(6, meshComponent.getId());

		// Check the mesh contains 64 elements/polygons, 4 edges + 4 vertices
		// each
		assertEquals(64, meshComponent.getPolygons().size());

		// Lists of all the edges/vertices for all the polygons
		List<IController> edges = new ArrayList<IController>();
		List<IController> vertices = new ArrayList<IController>();

		// Populate the list by getting the child objects of each polygon
		for (IController polygon : meshComponent.getPolygons()) {
			edges.addAll(polygon.getEntitiesFromCategory(MeshCategory.EDGES));
			vertices.addAll(
					polygon.getEntitiesFromCategory(MeshCategory.VERTICES));
		}

		assertEquals(256, edges.size());
		assertEquals(256, vertices.size());

		// Check the individual polygons, edges and vertices
		BoundaryCondition currFluidCondition = new BoundaryCondition();
		BoundaryCondition currThermalCondition = new BoundaryCondition();
		BoundaryCondition currScalarCondition = new BoundaryCondition();
		int currEdgeId;

		// The index of the current polygon
		int index = 0;

		for (IController currQuad : meshComponent.getPolygons()) {

			/** --- Checking MeshComponent construction --- **/

			// Check current polygon has 4 edges and 4 vertices
			assertEquals(4,
					currQuad.getEntitiesFromCategory(MeshCategory.EDGES).size());
			assertEquals(4, currQuad
					.getEntitiesFromCategory(MeshCategory.VERTICES).size());

			// Verify it has non-null vertices and edges
			for (int j = 0; j < 4; j++) {
				assertNotNull(currQuad.getEntitiesFromCategory(MeshCategory.EDGES)
						.get(j));
				assertNotNull(currQuad
						.getEntitiesFromCategory(MeshCategory.VERTICES).get(j));
			}

			// No need to check if vertices and edges have unique IDs, the
			// construction of Quad does this for us and throws exceptions if
			// there are duplicates

			/** --- Checking BoundaryCondition assignment --- **/

			// Note: BoundaryConditions.type and BoundaryConditions.values don't
			// really need to be throughly tested here as
			// BoundaryConditionsTester
			// ensures they must return valid stuff

			for (int j = 0; j < 4; j++) {

				// Grab the current edge ID
				currEdgeId = Integer.parseInt(
						currQuad.getEntitiesFromCategory(MeshCategory.EDGES)
								.get(j).getProperty(MeshProperty.ID));

				// Check the fluid boundary conditions (note: only the first
				// 32 polygons have fluid boundary conditions
				if (index <= 32) {
					// Grab the fluid boundary condition associate to this edge
					currFluidCondition = ((NekPolygonController) currQuad)
							.getFluidBoundaryCondition(currEdgeId);

					// Check the boundary condition, its type and values
					assertNotNull(currFluidCondition);
					assertNotNull(currFluidCondition.getType());
					assertNotNull(currFluidCondition.getValues());
					assertTrue(currFluidCondition.getValues().size() == 5);
				}

				// Grab the thermal boundary condition associate to this edge
				currThermalCondition = ((NekPolygonController) currQuad)
						.getThermalBoundaryCondition(currEdgeId);

				// Check the boundary condition, its type and values
				assertNotNull(currThermalCondition);
				assertNotNull(currThermalCondition.getType());
				assertNotNull(currThermalCondition.getValues());
				assertTrue(currThermalCondition.getValues().size() == 5);

				// Note: conj_ht example has no passive scalar boundary
				// conditions
				currScalarCondition = ((NekPolygonController) currQuad)
						.getOtherBoundaryCondition(1, currEdgeId);
				assertTrue(currScalarCondition == null);
			}

			// Increment the index
			index++;
		}

		/* --- Checking CURVED SIDE DATA component --- */

		MeshComponent curvedSides = (MeshComponent) component.get(5);
		String curvedSidesName = "Curved Side Data";
		String curvedSidesDesc = "Elements contained in the Curved Side "
				+ " section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(curvedSides);
		assertEquals(curvedSidesName, curvedSides.getName());
		assertEquals(curvedSidesDesc, curvedSides.getDescription());
		assertEquals(7, curvedSides.getId());

		// Check that the curved sides component is empty
		assertEquals(0, curvedSides.getPolygons().size());

		/* --- Checking PRESOLVE/RESTART OPTIONS component --- */

		DataComponent presolveRestartOpts = (DataComponent) component.get(6);
		String presolveRestartOptsName = "Pre-solve/Restart Options";
		String presolveRestartOptsDesc = "Entries contained in the Pre-solve/"
				+ "Restart Options section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(presolveRestartOpts);
		assertEquals(presolveRestartOptsName, presolveRestartOpts.getName());
		assertEquals(presolveRestartOptsDesc,
				presolveRestartOpts.getDescription());
		assertEquals(8, presolveRestartOpts.getId());

		// Check that the presolve/restart options component is empty
		assertEquals(0, presolveRestartOpts.retrieveAllEntries().size());

		/* --- Checking INIITIAL CONDITIONS component --- */

		DataComponent initialConditions = (DataComponent) component.get(7);
		String initialConditionsName = "Initial Conditions";
		String initialConditionsDesc = "Entries contained in the Initial "
				+ "Conditions section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(initialConditions);
		assertEquals(initialConditionsName, initialConditions.getName());
		assertEquals(initialConditionsDesc, initialConditions.getDescription());
		assertEquals(9, initialConditions.getId());

		// Check the initial conditions component contains 7 non-null entries
		assertEquals(7, initialConditions.retrieveAllEntries().size());
		for (int i = 0; i < 7; i++) {
			assertNotNull(initialConditions.retrieveAllEntries().get(i));
		}

		/* --- Checking DRIVE FORCE DATA component --- */

		DataComponent driveForceData = (DataComponent) component.get(8);
		String driveForceDataName = "Drive Force Data";
		String driveForceDataDesc = "Entries contained in the Drive Force "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(driveForceData);
		assertEquals(driveForceDataName, driveForceData.getName());
		assertEquals(driveForceDataDesc, driveForceData.getDescription());
		assertEquals(10, driveForceData.getId());

		// Check the drive force data component contains 4 non-null entries
		assertEquals(4, driveForceData.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(driveForceData.retrieveAllEntries().get(i));
		}

		/* --- Checking VARIABLE PROPERTY DATA component --- */

		DataComponent varPropData = (DataComponent) component.get(9);
		String varPropDataName = "Variable Property Data";
		String varPropDataDesc = "Entries contained in the Variable Property "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(varPropData);
		assertEquals(varPropDataName, varPropData.getName());
		assertEquals(varPropDataDesc, varPropData.getDescription());
		assertEquals(11, varPropData.getId());

		// Check the variable property data component contains 1 non-null entry
		assertEquals(1, varPropData.retrieveAllEntries().size());
		assertNotNull(varPropData.retrieveAllEntries().get(0));

		/* --- Checking HISTORY AND INTEGRAL DATA component --- */

		DataComponent histIntegralData = (DataComponent) component.get(10);
		String histIntegralDataName = "History and Integral Data";
		String histIntegralDataDesc = "Entries contained in the History "
				+ "and Integral Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(histIntegralData);
		assertEquals(histIntegralDataName, histIntegralData.getName());
		assertEquals(histIntegralDataDesc, histIntegralData.getDescription());
		assertEquals(12, histIntegralData.getId());

		// Check the history and integral data component is empty
		assertEquals(0, histIntegralData.retrieveAllEntries().size());

		/* --- Checking OUTPUT FIELD SPECIFICATION component --- */

		DataComponent outputFieldSpec = (DataComponent) component.get(11);
		String outputFieldSpecName = "Output Field Specification";
		String outputFieldSpecDesc = "Entries contained in the Output Field "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(outputFieldSpec);
		assertEquals(outputFieldSpecName, outputFieldSpec.getName());
		assertEquals(outputFieldSpecDesc, outputFieldSpec.getDescription());
		assertEquals(13, outputFieldSpec.getId());

		// Check the output field specification contains 6 non-null Entries
		assertEquals(6, outputFieldSpec.retrieveAllEntries().size());
		for (int i = 0; i < 6; i++) {
			assertNotNull(outputFieldSpec.retrieveAllEntries().get(i));
		}

		/* --- Checking OBJECT SPECIFICATION component --- */

		DataComponent objectSpec = (DataComponent) component.get(12);
		String objectSpecName = "Object Specification";
		String objectSpecDesc = "Entries contained in the Object "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(objectSpec);
		assertEquals(objectSpecName, objectSpec.getName());
		assertEquals(objectSpecDesc, objectSpec.getDescription());
		assertEquals(14, objectSpec.getId());

		// Check the output field specification contains 4 non-null Entries
		assertEquals(4, objectSpec.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(objectSpec.retrieveAllEntries().get(i));
		}

		return;
	}

	@Test
	public void checkEddy_uv() {

		// Create a NekReader to test
		NekReader reader = new NekReader();

		// Give it a test factory to use
		reader.setControllerFactory(new TestNekControllerFactory());

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "nek5000Data";
		String filePath = userDir + separator + "eddy_uv.rea";
		File testFile = new File(filePath);

		// Try to read in invalid .rea file
		File fakeFile = null;
		ArrayList<Component> component = null;
		try {
			component = reader.loadREAFile(fakeFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find fake Nek input file");
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from fake Nek input file");
			e.printStackTrace();
		}
		assertTrue(component == null);

		// Load the .rea file and parse the contents into Components
		try {
			component = reader.loadREAFile(testFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find Nek input file: " + testFile.toString());
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from Nek input file: " + testFile.toString());
			e.printStackTrace();
		}

		/* --- Checking PARAMETERS component --- */

		// Specify the Parameter DataComponent
		DataComponent paramComponent = (DataComponent) component.get(0);
		String paramName = "Parameters";
		String paramDescription = "Entries contained in the Parameters "
				+ "section of a Nek5000 reafile";

		// Check default values of the Parameter component
		assertNotNull(paramComponent);
		assertEquals(paramName, paramComponent.getName());
		assertEquals(paramDescription, paramComponent.getDescription());
		assertEquals(2, paramComponent.getId());

		// Check the Parameters component contains 103 non-null entries
		assertEquals(103, paramComponent.retrieveAllEntries().size());
		for (int i = 0; i < 103; i++) {
			assertNotNull(paramComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PASSIVE SCALAR DATA component --- */

		DataComponent passiveScalData = (DataComponent) component.get(1);
		String passiveScalDataName = "Passive Scalar Data";
		String passiveScalDataDesc = "Entries contained in the Passive "
				+ "Scalar Data section of a Nek5000 reafile";

		// Check default values of the Passive Scalar Data component
		assertNotNull(passiveScalData);
		assertEquals(passiveScalDataName, passiveScalData.getName());
		assertEquals(passiveScalDataDesc, passiveScalData.getDescription());
		assertEquals(3, passiveScalData.getId());

		// Check the component is empty for this example
		assertEquals(0, passiveScalData.retrieveAllEntries().size());

		/* --- Checking LOGICAL FLAGS component --- */

		// Specify the Logical Switches DataComponent
		DataComponent switchesComponent = (DataComponent) component.get(2);
		String switchesName = "Logical Switches";
		String switchesDescription = "Entries contained in the Logical Switches "
				+ "section of a Nek5000 reafile";

		// Check default values of the Logical Switches component
		assertNotNull(switchesComponent);
		assertEquals(switchesName, switchesComponent.getName());
		assertEquals(switchesDescription, switchesComponent.getDescription());
		assertEquals(4, switchesComponent.getId());

		// Check the DataComponent contains 13 non-null logical switches
		assertEquals(13, switchesComponent.retrieveAllEntries().size());
		for (int i = 0; i < 13; i++) {
			assertNotNull(switchesComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PRE-NEK AXES component --- */

		DataComponent preNekAxes = (DataComponent) component.get(3);
		String preNekAxesName = "Pre-Nek Axes";
		String preNekAxesDesc = "Entries contained in the Pre-Nek Axes "
				+ "section of a Nek5000 reafile";

		// Check default values of the Pre-Nek Axes component
		assertNotNull(preNekAxes);
		assertEquals(preNekAxesName, preNekAxes.getName());
		assertEquals(preNekAxesDesc, preNekAxes.getDescription());
		assertEquals(5, preNekAxes.getId());

		// Check the Pre-Nek entry is non-null
		assertEquals(1, preNekAxes.retrieveAllEntries().size());
		assertNotNull(preNekAxes.retrieveAllEntries().get(0));

		/* --- Checking MESH component and assigned BOUNDARY CONDITIONS --- */

		MeshComponent meshComponent = (MeshComponent) component.get(4);
		String meshName = "Mesh Data";
		String meshDescription = "Elements contained in the Mesh section of "
				+ "a Nek5000 reafile";

		// Check default values of the mesh component
		assertNotNull(meshComponent);
		assertEquals(meshName, meshComponent.getName());
		assertEquals(meshDescription, meshComponent.getDescription());
		assertEquals(6, meshComponent.getId());

		// Lists of all the edges/vertices for all the polygons
		List<IController> edges = new ArrayList<IController>();
		List<IController> vertices = new ArrayList<IController>();

		// Populate the list by getting the child objects of each polygon
		for (IController polygon : meshComponent.getPolygons()) {
			edges.addAll(polygon.getEntitiesFromCategory(MeshCategory.EDGES));
			vertices.addAll(
					polygon.getEntitiesFromCategory(MeshCategory.VERTICES));
		}

		// Check the mesh contains 64 elements/polygons, 4 edges + 4 vertices
		// each
		assertEquals(256, meshComponent.getPolygons().size());
		assertEquals(1024, edges.size());
		assertEquals(1024, vertices.size());

		// Check the individual polygons, edges and vertices
		NekPolygonController currQuad = new NekPolygonController();
		BoundaryCondition emptyCondition = new BoundaryCondition();
		BoundaryCondition currFluidCondition = new BoundaryCondition();
		BoundaryCondition currThermalCondition = new BoundaryCondition();
		BoundaryCondition currScalarCondition = new BoundaryCondition();
		int currEdgeId;
		for (IController currQuad1 : meshComponent.getPolygons()) {

			/** --- Checking MeshComponent construction --- **/

			// Check current polygon has 4 edges and 4 vertices
			assertEquals(4,
					currQuad1.getEntitiesFromCategory(MeshCategory.EDGES).size());
			assertEquals(4, currQuad1
					.getEntitiesFromCategory(MeshCategory.VERTICES).size());

			// Verify it has non-null vertices and edges
			for (int j = 0; j < 4; j++) {
				assertNotNull(currQuad1
						.getEntitiesFromCategory(MeshCategory.EDGES).get(j));
				assertNotNull(currQuad1
						.getEntitiesFromCategory(MeshCategory.VERTICES).get(j));
			}

			// No need to check if vertices and edges have unique IDs, the
			// construction of Quad does this for us and throws exceptions if
			// there are duplicates

			/** --- Checking BoundaryCondition assignment --- **/

			// Note: BoundaryConditions.type and BoundaryConditions.values don't
			// really need to be throughly tested here as
			// BoundaryConditionsTester
			// ensures they must return valid stuff

			for (int j = 0; j < 4; j++) {

				// Grab the current edge ID
				currEdgeId = Integer.parseInt(
						currQuad1.getEntitiesFromCategory(MeshCategory.EDGES)
								.get(j).getProperty(MeshProperty.ID));

				// Grab the fluid boundary condition associate to this edge
				currFluidCondition = ((NekPolygonController) currQuad1)
						.getFluidBoundaryCondition(currEdgeId);

				// Check the boundary condition, its type and values
				assertNotNull(currFluidCondition);
				assertNotNull(currFluidCondition.getType());
				assertNotNull(currFluidCondition.getValues());
				assertTrue(currFluidCondition.getValues().size() == 5);

				// Grab the thermal boundary condition associated to this edge
				currThermalCondition = ((NekPolygonController) currQuad1)
						.getThermalBoundaryCondition(currEdgeId);

				// Verify it has an empty BC (no thermal BCs for eddy_uv)
				assertTrue(currThermalCondition.equals(emptyCondition));

				// Grab the passive scalar BC and verify it's null (no passive
				// scalars for conj_ht)
				currScalarCondition = ((NekPolygonController) currQuad1)
						.getOtherBoundaryCondition(1, currEdgeId);
				assertTrue(currScalarCondition == null);
			}
		}

		/* --- Checking CURVED SIDE DATA component --- */

		MeshComponent curvedSides = (MeshComponent) component.get(5);
		String curvedSidesName = "Curved Side Data";
		String curvedSidesDesc = "Elements contained in the Curved Side "
				+ " section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(curvedSides);
		assertEquals(curvedSidesName, curvedSides.getName());
		assertEquals(curvedSidesDesc, curvedSides.getDescription());
		assertEquals(7, curvedSides.getId());

		// Check that the curved sides component is empty
		assertEquals(0, curvedSides.getPolygons().size());

		/* --- Checking PRESOLVE/RESTART OPTIONS component --- */

		DataComponent presolveRestartOpts = (DataComponent) component.get(6);
		String presolveRestartOptsName = "Pre-solve/Restart Options";
		String presolveRestartOptsDesc = "Entries contained in the Pre-solve/"
				+ "Restart Options section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(presolveRestartOpts);
		assertEquals(presolveRestartOptsName, presolveRestartOpts.getName());
		assertEquals(presolveRestartOptsDesc,
				presolveRestartOpts.getDescription());
		assertEquals(8, presolveRestartOpts.getId());

		// Check that the presolve/restart options component is empty
		assertEquals(0, presolveRestartOpts.retrieveAllEntries().size());

		/* --- Checking INIITIAL CONDITIONS component --- */

		DataComponent initialConditions = (DataComponent) component.get(7);
		String initialConditionsName = "Initial Conditions";
		String initialConditionsDesc = "Entries contained in the Initial "
				+ "Conditions section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(initialConditions);
		assertEquals(initialConditionsName, initialConditions.getName());
		assertEquals(initialConditionsDesc, initialConditions.getDescription());
		assertEquals(9, initialConditions.getId());

		// Check the initial conditions component contains 7 non-null entries
		assertEquals(7, initialConditions.retrieveAllEntries().size());
		for (int i = 0; i < 7; i++) {
			assertNotNull(initialConditions.retrieveAllEntries().get(i));
		}

		/* --- Checking DRIVE FORCE DATA component --- */

		DataComponent driveForceData = (DataComponent) component.get(8);
		String driveForceDataName = "Drive Force Data";
		String driveForceDataDesc = "Entries contained in the Drive Force "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(driveForceData);
		assertEquals(driveForceDataName, driveForceData.getName());
		assertEquals(driveForceDataDesc, driveForceData.getDescription());
		assertEquals(10, driveForceData.getId());

		// Check the drive force data component contains 4 non-null entries
		assertEquals(4, driveForceData.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(driveForceData.retrieveAllEntries().get(i));
		}

		/* --- Checking VARIABLE PROPERTY DATA component --- */

		DataComponent varPropData = (DataComponent) component.get(9);
		String varPropDataName = "Variable Property Data";
		String varPropDataDesc = "Entries contained in the Variable Property "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(varPropData);
		assertEquals(varPropDataName, varPropData.getName());
		assertEquals(varPropDataDesc, varPropData.getDescription());
		assertEquals(11, varPropData.getId());

		// Check the variable property data component contains 1 non-null entry
		assertEquals(1, varPropData.retrieveAllEntries().size());
		assertNotNull(varPropData.retrieveAllEntries().get(0));

		/* --- Checking HISTORY AND INTEGRAL DATA component --- */

		DataComponent histIntegralData = (DataComponent) component.get(10);
		String histIntegralDataName = "History and Integral Data";
		String histIntegralDataDesc = "Entries contained in the History "
				+ "and Integral Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(histIntegralData);
		assertEquals(histIntegralDataName, histIntegralData.getName());
		assertEquals(histIntegralDataDesc, histIntegralData.getDescription());
		assertEquals(12, histIntegralData.getId());

		// Check the history and integral data component is empty
		assertEquals(0, histIntegralData.retrieveAllEntries().size());

		/* --- Checking OUTPUT FIELD SPECIFICATION component --- */

		DataComponent outputFieldSpec = (DataComponent) component.get(11);
		String outputFieldSpecName = "Output Field Specification";
		String outputFieldSpecDesc = "Entries contained in the Output Field "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(outputFieldSpec);
		assertEquals(outputFieldSpecName, outputFieldSpec.getName());
		assertEquals(outputFieldSpecDesc, outputFieldSpec.getDescription());
		assertEquals(13, outputFieldSpec.getId());

		// Check the output field specification contains 6 non-null Entries
		assertEquals(6, outputFieldSpec.retrieveAllEntries().size());
		for (int i = 0; i < 6; i++) {
			assertNotNull(outputFieldSpec.retrieveAllEntries().get(i));
		}

		/* --- Checking OBJECT SPECIFICATION component --- */

		DataComponent objectSpec = (DataComponent) component.get(12);
		String objectSpecName = "Object Specification";
		String objectSpecDesc = "Entries contained in the Object "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(objectSpec);
		assertEquals(objectSpecName, objectSpec.getName());
		assertEquals(objectSpecDesc, objectSpec.getDescription());
		assertEquals(14, objectSpec.getId());

		// Check the output field specification contains 4 non-null Entries
		assertEquals(4, objectSpec.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(objectSpec.retrieveAllEntries().get(i));
		}

		return;
	}

	@Test
	public void checkKov() {

		// Create a NekReader to test
		NekReader reader = new NekReader();

		// Give it a test factory to use
		reader.setControllerFactory(new TestNekControllerFactory());

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "nek5000Data";
		String filePath = userDir + separator + "kov.rea";
		File testFile = new File(filePath);

		// Try to read in invalid .rea file
		File fakeFile = null;
		ArrayList<Component> component = null;
		try {
			component = reader.loadREAFile(fakeFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find fake Nek input file");
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from fake Nek input file");
			e.printStackTrace();
		}
		assertTrue(component == null);

		// Load the .rea file and parse the contents into Components
		try {
			component = reader.loadREAFile(testFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find Nek input file: " + testFile.toString());
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from Nek input file: " + testFile.toString());
			e.printStackTrace();
		}

		/* --- Checking PARAMETERS component --- */

		// Specify the Parameter DataComponent
		DataComponent paramComponent = (DataComponent) component.get(0);
		String paramName = "Parameters";
		String paramDescription = "Entries contained in the Parameters "
				+ "section of a Nek5000 reafile";

		// Check default values of the Parameter component
		assertNotNull(paramComponent);
		assertEquals(paramName, paramComponent.getName());
		assertEquals(paramDescription, paramComponent.getDescription());
		assertEquals(2, paramComponent.getId());

		// Check the Parameters component contains 103 non-null entries
		assertEquals(118, paramComponent.retrieveAllEntries().size());
		for (int i = 0; i < 118; i++) {
			assertNotNull(paramComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PASSIVE SCALAR DATA component --- */

		DataComponent passiveScalData = (DataComponent) component.get(1);
		String passiveScalDataName = "Passive Scalar Data";
		String passiveScalDataDesc = "Entries contained in the Passive "
				+ "Scalar Data section of a Nek5000 reafile";

		// Check default values of the Passive Scalar Data component
		assertNotNull(passiveScalData);
		assertEquals(passiveScalDataName, passiveScalData.getName());
		assertEquals(passiveScalDataDesc, passiveScalData.getDescription());
		assertEquals(3, passiveScalData.getId());

		// Check the component is empty for this example
		assertEquals(0, passiveScalData.retrieveAllEntries().size());

		/* --- Checking LOGICAL FLAGS component --- */

		// Specify the Logical Switches DataComponent
		DataComponent switchesComponent = (DataComponent) component.get(2);
		String switchesName = "Logical Switches";
		String switchesDescription = "Entries contained in the Logical Switches "
				+ "section of a Nek5000 reafile";

		// Check default values of the Logical Switches component
		assertNotNull(switchesComponent);
		assertEquals(switchesName, switchesComponent.getName());
		assertEquals(switchesDescription, switchesComponent.getDescription());
		assertEquals(4, switchesComponent.getId());

		// Check the DataComponent contains 13 non-null logical switches
		assertEquals(13, switchesComponent.retrieveAllEntries().size());
		for (int i = 0; i < 13; i++) {
			assertNotNull(switchesComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PRE-NEK AXES component --- */

		DataComponent preNekAxes = (DataComponent) component.get(3);
		String preNekAxesName = "Pre-Nek Axes";
		String preNekAxesDesc = "Entries contained in the Pre-Nek Axes "
				+ "section of a Nek5000 reafile";

		// Check default values of the Pre-Nek Axes component
		assertNotNull(preNekAxes);
		assertEquals(preNekAxesName, preNekAxes.getName());
		assertEquals(preNekAxesDesc, preNekAxes.getDescription());
		assertEquals(5, preNekAxes.getId());

		// Check the Pre-Nek entry is non-null
		assertEquals(1, preNekAxes.retrieveAllEntries().size());
		assertNotNull(preNekAxes.retrieveAllEntries().get(0));

		/* --- Checking MESH component and assigned BOUNDARY CONDITIONS --- */

		MeshComponent meshComponent = (MeshComponent) component.get(4);
		String meshName = "Mesh Data";
		String meshDescription = "Elements contained in the Mesh section of "
				+ "a Nek5000 reafile";

		// Check default values of the mesh component
		assertNotNull(meshComponent);
		assertEquals(meshName, meshComponent.getName());
		assertEquals(meshDescription, meshComponent.getDescription());
		assertEquals(6, meshComponent.getId());

		// Lists of all the edges/vertices for all the polygons
		List<IController> edges = new ArrayList<IController>();
		List<IController> vertices = new ArrayList<IController>();

		// Populate the list by getting the child objects of each polygon
		for (IController polygon : meshComponent.getPolygons()) {
			edges.addAll(polygon.getEntitiesFromCategory(MeshCategory.EDGES));
			vertices.addAll(
					polygon.getEntitiesFromCategory(MeshCategory.VERTICES));
		}

		// Check the mesh contains 64 elements/polygons, 4 edges + 4 vertices
		// each
		assertEquals(8, meshComponent.getPolygons().size());
		assertEquals(32, edges.size());
		assertEquals(32, vertices.size());

		// Check the individual polygons, edges and vertices
		BoundaryCondition emptyCondition = new BoundaryCondition();
		BoundaryCondition currFluidCondition = new BoundaryCondition();
		BoundaryCondition currThermalCondition = new BoundaryCondition();
		BoundaryCondition currScalarCondition = new BoundaryCondition();
		int currEdgeId;
		for (IController currQuad : meshComponent.getPolygons()) {

			/** --- Checking MeshComponent construction --- **/

			// Check current polygon has 4 edges and 4 vertices
			assertEquals(4,
					currQuad.getEntitiesFromCategory(MeshCategory.EDGES).size());
			assertEquals(4, currQuad
					.getEntitiesFromCategory(MeshCategory.VERTICES).size());

			// Verify it has non-null vertices and edges
			for (int j = 0; j < 4; j++) {
				assertNotNull(currQuad.getEntitiesFromCategory(MeshCategory.EDGES)
						.get(j));
				assertNotNull(currQuad
						.getEntitiesFromCategory(MeshCategory.VERTICES).get(j));
			}

			// No need to check if vertices and edges have unique IDs, the
			// construction of Quad does this for us and throws exceptions if
			// there are duplicates

			/** --- Checking BoundaryCondition assignment --- **/

			// Note: BoundaryConditions.type and BoundaryConditions.values don't
			// really need to be throughly tested here as
			// BoundaryConditionsTester
			// ensures they must return valid stuff

			for (int j = 0; j < 4; j++) {

				// Grab the current edge ID
				currEdgeId = Integer.parseInt(
						currQuad.getEntitiesFromCategory(MeshCategory.EDGES)
								.get(j).getProperty(MeshProperty.ID));

				// Grab the fluid boundary condition associate to this edge
				currFluidCondition = ((NekPolygonController) currQuad)
						.getFluidBoundaryCondition(currEdgeId);

				// Check the boundary condition, its type and values
				assertNotNull(currFluidCondition);
				assertNotNull(currFluidCondition.getType());
				assertNotNull(currFluidCondition.getValues());
				assertTrue(currFluidCondition.getValues().size() == 5);

				// Grab the thermal boundary condition associate to this edge
				currThermalCondition = ((NekPolygonController) currQuad)
						.getThermalBoundaryCondition(currEdgeId);

				// Check the boundary condition is empty (no thermal BCs for
				// kov)
				assertTrue(currThermalCondition.equals(emptyCondition));

				// Grab the passive scalar BC and verify it's null (no passive
				// scalars for conj_ht)
				currScalarCondition = ((NekPolygonController) currQuad)
						.getOtherBoundaryCondition(1, currEdgeId);
				assertTrue(currScalarCondition == null);
			}
		}

		/* --- Checking CURVED SIDE DATA component --- */

		MeshComponent curvedSides = (MeshComponent) component.get(5);
		String curvedSidesName = "Curved Side Data";
		String curvedSidesDesc = "Elements contained in the Curved Side "
				+ " section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(curvedSides);
		assertEquals(curvedSidesName, curvedSides.getName());
		assertEquals(curvedSidesDesc, curvedSides.getDescription());
		assertEquals(7, curvedSides.getId());

		// Check that the curved sides component is empty
		assertEquals(0, curvedSides.getPolygons().size());

		/* --- Checking PRESOLVE/RESTART OPTIONS component --- */

		DataComponent presolveRestartOpts = (DataComponent) component.get(6);
		String presolveRestartOptsName = "Pre-solve/Restart Options";
		String presolveRestartOptsDesc = "Entries contained in the Pre-solve/"
				+ "Restart Options section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(presolveRestartOpts);
		assertEquals(presolveRestartOptsName, presolveRestartOpts.getName());
		assertEquals(presolveRestartOptsDesc,
				presolveRestartOpts.getDescription());
		assertEquals(8, presolveRestartOpts.getId());

		// Check that the presolve/restart options component is empty
		assertEquals(0, presolveRestartOpts.retrieveAllEntries().size());

		/* --- Checking INIITIAL CONDITIONS component --- */

		DataComponent initialConditions = (DataComponent) component.get(7);
		String initialConditionsName = "Initial Conditions";
		String initialConditionsDesc = "Entries contained in the Initial "
				+ "Conditions section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(initialConditions);
		assertEquals(initialConditionsName, initialConditions.getName());
		assertEquals(initialConditionsDesc, initialConditions.getDescription());
		assertEquals(9, initialConditions.getId());

		// Check the initial conditions component contains 7 non-null entries
		assertEquals(7, initialConditions.retrieveAllEntries().size());
		for (int i = 0; i < 7; i++) {
			assertNotNull(initialConditions.retrieveAllEntries().get(i));
		}

		/* --- Checking DRIVE FORCE DATA component --- */

		DataComponent driveForceData = (DataComponent) component.get(8);
		String driveForceDataName = "Drive Force Data";
		String driveForceDataDesc = "Entries contained in the Drive Force "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(driveForceData);
		assertEquals(driveForceDataName, driveForceData.getName());
		assertEquals(driveForceDataDesc, driveForceData.getDescription());
		assertEquals(10, driveForceData.getId());

		// Check the drive force data component contains 4 non-null entries
		assertEquals(4, driveForceData.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(driveForceData.retrieveAllEntries().get(i));
		}

		/* --- Checking VARIABLE PROPERTY DATA component --- */

		DataComponent varPropData = (DataComponent) component.get(9);
		String varPropDataName = "Variable Property Data";
		String varPropDataDesc = "Entries contained in the Variable Property "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(varPropData);
		assertEquals(varPropDataName, varPropData.getName());
		assertEquals(varPropDataDesc, varPropData.getDescription());
		assertEquals(11, varPropData.getId());

		// Check the variable property data component contains 1 non-null entry
		assertEquals(1, varPropData.retrieveAllEntries().size());
		assertNotNull(varPropData.retrieveAllEntries().get(0));

		/* --- Checking HISTORY AND INTEGRAL DATA component --- */

		DataComponent histIntegralData = (DataComponent) component.get(10);
		String histIntegralDataName = "History and Integral Data";
		String histIntegralDataDesc = "Entries contained in the History "
				+ "and Integral Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(histIntegralData);
		assertEquals(histIntegralDataName, histIntegralData.getName());
		assertEquals(histIntegralDataDesc, histIntegralData.getDescription());
		assertEquals(12, histIntegralData.getId());

		// Check the history and integral data component is empty
		assertEquals(1, histIntegralData.retrieveAllEntries().size());

		/* --- Checking OUTPUT FIELD SPECIFICATION component --- */

		DataComponent outputFieldSpec = (DataComponent) component.get(11);
		String outputFieldSpecName = "Output Field Specification";
		String outputFieldSpecDesc = "Entries contained in the Output Field "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(outputFieldSpec);
		assertEquals(outputFieldSpecName, outputFieldSpec.getName());
		assertEquals(outputFieldSpecDesc, outputFieldSpec.getDescription());
		assertEquals(13, outputFieldSpec.getId());

		// Check the output field specification contains 6 non-null Entries
		assertEquals(6, outputFieldSpec.retrieveAllEntries().size());
		for (int i = 0; i < 6; i++) {
			assertNotNull(outputFieldSpec.retrieveAllEntries().get(i));
		}

		/* --- Checking OBJECT SPECIFICATION component --- */

		DataComponent objectSpec = (DataComponent) component.get(12);
		String objectSpecName = "Object Specification";
		String objectSpecDesc = "Entries contained in the Object "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(objectSpec);
		assertEquals(objectSpecName, objectSpec.getName());
		assertEquals(objectSpecDesc, objectSpec.getDescription());
		assertEquals(14, objectSpec.getId());

		// Check the output field specification contains 4 non-null Entries
		assertEquals(4, objectSpec.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(objectSpec.retrieveAllEntries().get(i));
		}

		return;

	}

	@Test
	public void checkRay_dd() {

		// Create a NekReader to test
		NekReader reader = new NekReader();

		// Give it a test factory to use
		reader.setControllerFactory(new TestNekControllerFactory());

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "nek5000Data";
		String filePath = userDir + separator + "ray_dd.rea";
		File testFile = new File(filePath);

		// Try to read in invalid .rea file
		File fakeFile = null;
		ArrayList<Component> component = null;
		try {
			component = reader.loadREAFile(fakeFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find fake Nek input file");
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from fake Nek input file");
			e.printStackTrace();
		}
		assertTrue(component == null);

		// Load the .rea file and parse the contents into Components
		try {
			component = reader.loadREAFile(testFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find Nek input file: " + testFile.toString());
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from Nek input file: " + testFile.toString());
			e.printStackTrace();
		}

		/* --- Checking PARAMETERS component --- */

		// Specify the Parameter DataComponent
		DataComponent paramComponent = (DataComponent) component.get(0);
		String paramName = "Parameters";
		String paramDescription = "Entries contained in the Parameters "
				+ "section of a Nek5000 reafile";

		// Check default values of the Parameter component
		assertNotNull(paramComponent);
		assertEquals(paramName, paramComponent.getName());
		assertEquals(paramDescription, paramComponent.getDescription());
		assertEquals(2, paramComponent.getId());

		// Check the Parameters component contains 103 non-null entries
		assertEquals(103, paramComponent.retrieveAllEntries().size());
		for (int i = 0; i < 103; i++) {
			assertNotNull(paramComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PASSIVE SCALAR DATA component --- */

		DataComponent passiveScalData = (DataComponent) component.get(1);
		String passiveScalDataName = "Passive Scalar Data";
		String passiveScalDataDesc = "Entries contained in the Passive "
				+ "Scalar Data section of a Nek5000 reafile";

		// Check default values of the Passive Scalar Data component
		assertNotNull(passiveScalData);
		assertEquals(passiveScalDataName, passiveScalData.getName());
		assertEquals(passiveScalDataDesc, passiveScalData.getDescription());
		assertEquals(3, passiveScalData.getId());

		// Check the component is empty for this example
		assertEquals(0, passiveScalData.retrieveAllEntries().size());

		/* --- Checking LOGICAL FLAGS component --- */

		// Specify the Logical Switches DataComponent
		DataComponent switchesComponent = (DataComponent) component.get(2);
		String switchesName = "Logical Switches";
		String switchesDescription = "Entries contained in the Logical Switches "
				+ "section of a Nek5000 reafile";

		// Check default values of the Logical Switches component
		assertNotNull(switchesComponent);
		assertEquals(switchesName, switchesComponent.getName());
		assertEquals(switchesDescription, switchesComponent.getDescription());
		assertEquals(4, switchesComponent.getId());

		// Check the DataComponent contains 13 non-null logical switches
		assertEquals(13, switchesComponent.retrieveAllEntries().size());
		for (int i = 0; i < 13; i++) {
			assertNotNull(switchesComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PRE-NEK AXES component --- */

		DataComponent preNekAxes = (DataComponent) component.get(3);
		String preNekAxesName = "Pre-Nek Axes";
		String preNekAxesDesc = "Entries contained in the Pre-Nek Axes "
				+ "section of a Nek5000 reafile";

		// Check default values of the Pre-Nek Axes component
		assertNotNull(preNekAxes);
		assertEquals(preNekAxesName, preNekAxes.getName());
		assertEquals(preNekAxesDesc, preNekAxes.getDescription());
		assertEquals(5, preNekAxes.getId());

		// Check the Pre-Nek entry is non-null
		assertEquals(1, preNekAxes.retrieveAllEntries().size());
		assertNotNull(preNekAxes.retrieveAllEntries().get(0));

		/* --- Checking MESH component and assigned BOUNDARY CONDITIONS --- */

		MeshComponent meshComponent = (MeshComponent) component.get(4);
		String meshName = "Mesh Data";
		String meshDescription = "Elements contained in the Mesh section of "
				+ "a Nek5000 reafile";

		// Check default values of the mesh component
		assertNotNull(meshComponent);
		assertEquals(meshName, meshComponent.getName());
		assertEquals(meshDescription, meshComponent.getDescription());
		assertEquals(6, meshComponent.getId());

		// Lists of all the edges/vertices for all the polygons
		List<IController> edges = new ArrayList<IController>();
		List<IController> vertices = new ArrayList<IController>();

		// Populate the list by getting the child objects of each polygon
		for (IController polygon : meshComponent.getPolygons()) {
			edges.addAll(polygon.getEntitiesFromCategory(MeshCategory.EDGES));
			vertices.addAll(
					polygon.getEntitiesFromCategory(MeshCategory.VERTICES));
		}

		// Check the mesh contains 64 elements/polygons, 4 edges + 4 vertices
		// each
		assertEquals(3, meshComponent.getPolygons().size());
		assertEquals(12, edges.size());
		assertEquals(12, vertices.size());

		// Check the individual polygons, edges and vertices
		BoundaryCondition currFluidCondition = new BoundaryCondition();
		BoundaryCondition currThermalCondition = new BoundaryCondition();
		BoundaryCondition currScalarCondition = new BoundaryCondition();
		int currEdgeId;
		for (IController currQuad : meshComponent.getPolygons()) {

			/** --- Checking MeshComponent construction --- **/

			// Check current polygon has 4 edges and 4 vertices
			assertEquals(4,
					currQuad.getEntitiesFromCategory(MeshCategory.EDGES).size());
			assertEquals(4, currQuad
					.getEntitiesFromCategory(MeshCategory.VERTICES).size());

			// Verify it has non-null vertices and edges
			for (int j = 0; j < 4; j++) {
				assertNotNull(currQuad.getEntitiesFromCategory(MeshCategory.EDGES)
						.get(j));
				assertNotNull(currQuad
						.getEntitiesFromCategory(MeshCategory.VERTICES).get(j));
			}

			// No need to check if vertices and edges have unique IDs, the
			// construction of Quad does this for us and throws exceptions if
			// there are duplicates

			/** --- Checking BoundaryCondition assignment --- **/

			// Note: BoundaryConditions.type and BoundaryConditions.values don't
			// really need to be throughly tested here as
			// BoundaryConditionsTester
			// ensures they must return valid stuff

			for (int j = 0; j < 4; j++) {

				// Grab the current edge ID
				currEdgeId = Integer.parseInt(
						currQuad.getEntitiesFromCategory(MeshCategory.EDGES)
								.get(j).getProperty(MeshProperty.ID));

				// Grab the fluid boundary condition associate to this edge
				currFluidCondition = ((NekPolygonController) currQuad)
						.getFluidBoundaryCondition(currEdgeId);

				// Check the boundary condition, its type and values
				assertNotNull(currFluidCondition);
				assertNotNull(currFluidCondition.getType());
				assertNotNull(currFluidCondition.getValues());
				assertTrue(currFluidCondition.getValues().size() == 5);

				// Grab the thermal boundary condition associated to this edge
				currThermalCondition = ((NekPolygonController) currQuad)
						.getThermalBoundaryCondition(currEdgeId);

				// Check the boundary condition, its type and values
				assertNotNull(currThermalCondition);
				assertNotNull(currThermalCondition.getType());
				assertNotNull(currThermalCondition.getValues());
				assertTrue(currThermalCondition.getValues().size() == 5);

				// Grab the passive scalar BC and verify it's null (no passive
				// scalars for ray_dd)
				currScalarCondition = ((NekPolygonController) currQuad)
						.getOtherBoundaryCondition(1, currEdgeId);
				assertTrue(currScalarCondition == null);
			}
		}

		/* --- Checking CURVED SIDE DATA component --- */

		MeshComponent curvedSides = (MeshComponent) component.get(5);
		String curvedSidesName = "Curved Side Data";
		String curvedSidesDesc = "Elements contained in the Curved Side "
				+ " section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(curvedSides);
		assertEquals(curvedSidesName, curvedSides.getName());
		assertEquals(curvedSidesDesc, curvedSides.getDescription());
		assertEquals(7, curvedSides.getId());

		// Check that the curved sides component is empty
		assertEquals(0, curvedSides.getPolygons().size());

		/* --- Checking PRESOLVE/RESTART OPTIONS component --- */

		DataComponent presolveRestartOpts = (DataComponent) component.get(6);
		String presolveRestartOptsName = "Pre-solve/Restart Options";
		String presolveRestartOptsDesc = "Entries contained in the Pre-solve/"
				+ "Restart Options section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(presolveRestartOpts);
		assertEquals(presolveRestartOptsName, presolveRestartOpts.getName());
		assertEquals(presolveRestartOptsDesc,
				presolveRestartOpts.getDescription());
		assertEquals(8, presolveRestartOpts.getId());

		// Check that the presolve/restart options component is empty
		assertEquals(0, presolveRestartOpts.retrieveAllEntries().size());

		/* --- Checking INIITIAL CONDITIONS component --- */

		DataComponent initialConditions = (DataComponent) component.get(7);
		String initialConditionsName = "Initial Conditions";
		String initialConditionsDesc = "Entries contained in the Initial "
				+ "Conditions section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(initialConditions);
		assertEquals(initialConditionsName, initialConditions.getName());
		assertEquals(initialConditionsDesc, initialConditions.getDescription());
		assertEquals(9, initialConditions.getId());

		// Check the initial conditions component contains 7 non-null entries
		assertEquals(7, initialConditions.retrieveAllEntries().size());
		for (int i = 0; i < 7; i++) {
			assertNotNull(initialConditions.retrieveAllEntries().get(i));
		}

		/* --- Checking DRIVE FORCE DATA component --- */

		DataComponent driveForceData = (DataComponent) component.get(8);
		String driveForceDataName = "Drive Force Data";
		String driveForceDataDesc = "Entries contained in the Drive Force "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(driveForceData);
		assertEquals(driveForceDataName, driveForceData.getName());
		assertEquals(driveForceDataDesc, driveForceData.getDescription());
		assertEquals(10, driveForceData.getId());

		// Check the drive force data component contains 4 non-null entries
		assertEquals(4, driveForceData.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(driveForceData.retrieveAllEntries().get(i));
		}

		/* --- Checking VARIABLE PROPERTY DATA component --- */

		DataComponent varPropData = (DataComponent) component.get(9);
		String varPropDataName = "Variable Property Data";
		String varPropDataDesc = "Entries contained in the Variable Property "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(varPropData);
		assertEquals(varPropDataName, varPropData.getName());
		assertEquals(varPropDataDesc, varPropData.getDescription());
		assertEquals(11, varPropData.getId());

		// Check the variable property data component contains 1 non-null entry
		assertEquals(1, varPropData.retrieveAllEntries().size());
		assertNotNull(varPropData.retrieveAllEntries().get(0));

		/* --- Checking HISTORY AND INTEGRAL DATA component --- */

		DataComponent histIntegralData = (DataComponent) component.get(10);
		String histIntegralDataName = "History and Integral Data";
		String histIntegralDataDesc = "Entries contained in the History "
				+ "and Integral Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(histIntegralData);
		assertEquals(histIntegralDataName, histIntegralData.getName());
		assertEquals(histIntegralDataDesc, histIntegralData.getDescription());
		assertEquals(12, histIntegralData.getId());

		// Check the history and integral data component is empty
		assertEquals(1, histIntegralData.retrieveAllEntries().size());

		/* --- Checking OUTPUT FIELD SPECIFICATION component --- */

		DataComponent outputFieldSpec = (DataComponent) component.get(11);
		String outputFieldSpecName = "Output Field Specification";
		String outputFieldSpecDesc = "Entries contained in the Output Field "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(outputFieldSpec);
		assertEquals(outputFieldSpecName, outputFieldSpec.getName());
		assertEquals(outputFieldSpecDesc, outputFieldSpec.getDescription());
		assertEquals(13, outputFieldSpec.getId());

		// Check the output field specification contains 6 non-null Entries
		assertEquals(6, outputFieldSpec.retrieveAllEntries().size());
		for (int i = 0; i < 6; i++) {
			assertNotNull(outputFieldSpec.retrieveAllEntries().get(i));
		}

		/* --- Checking OBJECT SPECIFICATION component --- */

		DataComponent objectSpec = (DataComponent) component.get(12);
		String objectSpecName = "Object Specification";
		String objectSpecDesc = "Entries contained in the Object "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(objectSpec);
		assertEquals(objectSpecName, objectSpec.getName());
		assertEquals(objectSpecDesc, objectSpec.getDescription());
		assertEquals(14, objectSpec.getId());

		// Check the output field specification contains 4 non-null Entries
		assertEquals(4, objectSpec.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(objectSpec.retrieveAllEntries().get(i));
		}

		return;
	}

	@Test
	public void checkRay_nn() {

		// Create a NekReader to test
		NekReader reader = new NekReader();

		// Give it a test factory to use
		reader.setControllerFactory(new TestNekControllerFactory());

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "nek5000Data";
		String filePath = userDir + separator + "ray_nn.rea";
		File testFile = new File(filePath);

		// Try to read in invalid .rea file
		File fakeFile = null;
		ArrayList<Component> component = null;
		try {
			component = reader.loadREAFile(fakeFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find fake Nek input file");
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from fake Nek input file");
			e.printStackTrace();
		}
		assertTrue(component == null);

		// Load the .rea file and parse the contents into Components
		try {
			component = reader.loadREAFile(testFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find Nek input file: " + testFile.toString());
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from Nek input file: " + testFile.toString());
			e.printStackTrace();
		}

		/* --- Checking PARAMETERS component --- */

		// Specify the Parameter DataComponent
		DataComponent paramComponent = (DataComponent) component.get(0);
		String paramName = "Parameters";
		String paramDescription = "Entries contained in the Parameters "
				+ "section of a Nek5000 reafile";

		// Check default values of the Parameter component
		assertNotNull(paramComponent);
		assertEquals(paramName, paramComponent.getName());
		assertEquals(paramDescription, paramComponent.getDescription());
		assertEquals(2, paramComponent.getId());

		// Check the Parameters component contains 103 non-null entries
		assertEquals(103, paramComponent.retrieveAllEntries().size());
		for (int i = 0; i < 103; i++) {
			assertNotNull(paramComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PASSIVE SCALAR DATA component --- */

		DataComponent passiveScalData = (DataComponent) component.get(1);
		String passiveScalDataName = "Passive Scalar Data";
		String passiveScalDataDesc = "Entries contained in the Passive "
				+ "Scalar Data section of a Nek5000 reafile";

		// Check default values of the Passive Scalar Data component
		assertNotNull(passiveScalData);
		assertEquals(passiveScalDataName, passiveScalData.getName());
		assertEquals(passiveScalDataDesc, passiveScalData.getDescription());
		assertEquals(3, passiveScalData.getId());

		// Check the component is empty for this example
		assertEquals(0, passiveScalData.retrieveAllEntries().size());

		/* --- Checking LOGICAL FLAGS component --- */

		// Specify the Logical Switches DataComponent
		DataComponent switchesComponent = (DataComponent) component.get(2);
		String switchesName = "Logical Switches";
		String switchesDescription = "Entries contained in the Logical Switches "
				+ "section of a Nek5000 reafile";

		// Check default values of the Logical Switches component
		assertNotNull(switchesComponent);
		assertEquals(switchesName, switchesComponent.getName());
		assertEquals(switchesDescription, switchesComponent.getDescription());
		assertEquals(4, switchesComponent.getId());

		// Check the DataComponent contains 13 non-null logical switches
		assertEquals(13, switchesComponent.retrieveAllEntries().size());
		for (int i = 0; i < 13; i++) {
			assertNotNull(switchesComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PRE-NEK AXES component --- */

		DataComponent preNekAxes = (DataComponent) component.get(3);
		String preNekAxesName = "Pre-Nek Axes";
		String preNekAxesDesc = "Entries contained in the Pre-Nek Axes "
				+ "section of a Nek5000 reafile";

		// Check default values of the Pre-Nek Axes component
		assertNotNull(preNekAxes);
		assertEquals(preNekAxesName, preNekAxes.getName());
		assertEquals(preNekAxesDesc, preNekAxes.getDescription());
		assertEquals(5, preNekAxes.getId());

		// Check the Pre-Nek entry is non-null
		assertEquals(1, preNekAxes.retrieveAllEntries().size());
		assertNotNull(preNekAxes.retrieveAllEntries().get(0));

		/* --- Checking MESH component and assigned BOUNDARY CONDITIONS --- */

		MeshComponent meshComponent = (MeshComponent) component.get(4);
		String meshName = "Mesh Data";
		String meshDescription = "Elements contained in the Mesh section of "
				+ "a Nek5000 reafile";

		// Check default values of the mesh component
		assertNotNull(meshComponent);
		assertEquals(meshName, meshComponent.getName());
		assertEquals(meshDescription, meshComponent.getDescription());
		assertEquals(6, meshComponent.getId());

		// Lists of all the edges/vertices for all the polygons
		List<IController> edges = new ArrayList<IController>();
		List<IController> vertices = new ArrayList<IController>();

		// Populate the list by getting the child objects of each polygon
		for (IController polygon : meshComponent.getPolygons()) {
			edges.addAll(polygon.getEntitiesFromCategory(MeshCategory.EDGES));
			vertices.addAll(
					polygon.getEntitiesFromCategory(MeshCategory.VERTICES));
		}

		// Check the mesh contains 64 elements/polygons, 4 edges + 4 vertices
		// each
		assertEquals(3, meshComponent.getPolygons().size());
		assertEquals(12, edges.size());
		assertEquals(12, vertices.size());

		// Check the individual polygons, edges and vertices
		BoundaryCondition currFluidCondition = new BoundaryCondition();
		BoundaryCondition currThermalCondition = new BoundaryCondition();
		BoundaryCondition currScalarCondition = new BoundaryCondition();
		int currEdgeId;
		for (IController currQuad : meshComponent.getPolygons()) {

			/** --- Checking MeshComponent construction --- **/

			// Check current polygon has 4 edges and 4 vertices
			assertEquals(4,
					currQuad.getEntitiesFromCategory(MeshCategory.EDGES).size());
			assertEquals(4, currQuad
					.getEntitiesFromCategory(MeshCategory.VERTICES).size());

			// Verify it has non-null vertices and edges
			for (int j = 0; j < 4; j++) {
				assertNotNull(currQuad.getEntitiesFromCategory(MeshCategory.EDGES)
						.get(j));
				assertNotNull(currQuad
						.getEntitiesFromCategory(MeshCategory.VERTICES).get(j));
			}

			// No need to check if vertices and edges have unique IDs, the
			// construction of Quad does this for us and throws exceptions if
			// there are duplicates

			/** --- Checking BoundaryCondition assignment --- **/

			// Note: BoundaryConditions.type and BoundaryConditions.values don't
			// really need to be throughly tested here as
			// BoundaryConditionsTester
			// ensures they must return valid stuff

			for (int j = 0; j < 4; j++) {

				// Grab the current edge ID
				currEdgeId = Integer.parseInt(
						currQuad.getEntitiesFromCategory(MeshCategory.EDGES)
								.get(j).getProperty(MeshProperty.ID));

				// Grab the fluid boundary condition associate to this edge
				currFluidCondition = ((NekPolygonController) currQuad)
						.getFluidBoundaryCondition(currEdgeId);

				// Check the boundary condition, its type and values
				assertNotNull(currFluidCondition);
				assertNotNull(currFluidCondition.getType());
				assertNotNull(currFluidCondition.getValues());
				assertTrue(currFluidCondition.getValues().size() == 5);

				// Grab the thermal boundary condition associated to this edge
				currThermalCondition = ((NekPolygonController) currQuad)
						.getThermalBoundaryCondition(currEdgeId);

				// Check the boundary condition, its type and values
				assertNotNull(currThermalCondition);
				assertNotNull(currThermalCondition.getType());
				assertNotNull(currThermalCondition.getValues());
				assertTrue(currThermalCondition.getValues().size() == 5);

				// Grab the passive scalar BC and verify it's null (no passive
				// scalars for ray_nn)
				currScalarCondition = ((NekPolygonController) currQuad)
						.getOtherBoundaryCondition(1, currEdgeId);
				assertTrue(currScalarCondition == null);
			}
		}

		/* --- Checking CURVED SIDE DATA component --- */

		MeshComponent curvedSides = (MeshComponent) component.get(5);
		String curvedSidesName = "Curved Side Data";
		String curvedSidesDesc = "Elements contained in the Curved Side "
				+ " section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(curvedSides);
		assertEquals(curvedSidesName, curvedSides.getName());
		assertEquals(curvedSidesDesc, curvedSides.getDescription());
		assertEquals(7, curvedSides.getId());

		// Check that the curved sides component is empty
		assertEquals(0, curvedSides.getPolygons().size());

		/* --- Checking PRESOLVE/RESTART OPTIONS component --- */

		DataComponent presolveRestartOpts = (DataComponent) component.get(6);
		String presolveRestartOptsName = "Pre-solve/Restart Options";
		String presolveRestartOptsDesc = "Entries contained in the Pre-solve/"
				+ "Restart Options section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(presolveRestartOpts);
		assertEquals(presolveRestartOptsName, presolveRestartOpts.getName());
		assertEquals(presolveRestartOptsDesc,
				presolveRestartOpts.getDescription());
		assertEquals(8, presolveRestartOpts.getId());

		// Check that the presolve/restart options component is empty
		assertEquals(0, presolveRestartOpts.retrieveAllEntries().size());

		/* --- Checking INIITIAL CONDITIONS component --- */

		DataComponent initialConditions = (DataComponent) component.get(7);
		String initialConditionsName = "Initial Conditions";
		String initialConditionsDesc = "Entries contained in the Initial "
				+ "Conditions section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(initialConditions);
		assertEquals(initialConditionsName, initialConditions.getName());
		assertEquals(initialConditionsDesc, initialConditions.getDescription());
		assertEquals(9, initialConditions.getId());

		// Check the initial conditions component contains 7 non-null entries
		assertEquals(7, initialConditions.retrieveAllEntries().size());
		for (int i = 0; i < 7; i++) {
			assertNotNull(initialConditions.retrieveAllEntries().get(i));
		}

		/* --- Checking DRIVE FORCE DATA component --- */

		DataComponent driveForceData = (DataComponent) component.get(8);
		String driveForceDataName = "Drive Force Data";
		String driveForceDataDesc = "Entries contained in the Drive Force "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(driveForceData);
		assertEquals(driveForceDataName, driveForceData.getName());
		assertEquals(driveForceDataDesc, driveForceData.getDescription());
		assertEquals(10, driveForceData.getId());

		// Check the drive force data component contains 4 non-null entries
		assertEquals(4, driveForceData.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(driveForceData.retrieveAllEntries().get(i));
		}

		/* --- Checking VARIABLE PROPERTY DATA component --- */

		DataComponent varPropData = (DataComponent) component.get(9);
		String varPropDataName = "Variable Property Data";
		String varPropDataDesc = "Entries contained in the Variable Property "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(varPropData);
		assertEquals(varPropDataName, varPropData.getName());
		assertEquals(varPropDataDesc, varPropData.getDescription());
		assertEquals(11, varPropData.getId());

		// Check the variable property data component contains 1 non-null entry
		assertEquals(1, varPropData.retrieveAllEntries().size());
		assertNotNull(varPropData.retrieveAllEntries().get(0));

		/* --- Checking HISTORY AND INTEGRAL DATA component --- */

		DataComponent histIntegralData = (DataComponent) component.get(10);
		String histIntegralDataName = "History and Integral Data";
		String histIntegralDataDesc = "Entries contained in the History "
				+ "and Integral Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(histIntegralData);
		assertEquals(histIntegralDataName, histIntegralData.getName());
		assertEquals(histIntegralDataDesc, histIntegralData.getDescription());
		assertEquals(12, histIntegralData.getId());

		// Check the history and integral data component is empty
		assertEquals(1, histIntegralData.retrieveAllEntries().size());

		/* --- Checking OUTPUT FIELD SPECIFICATION component --- */

		DataComponent outputFieldSpec = (DataComponent) component.get(11);
		String outputFieldSpecName = "Output Field Specification";
		String outputFieldSpecDesc = "Entries contained in the Output Field "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(outputFieldSpec);
		assertEquals(outputFieldSpecName, outputFieldSpec.getName());
		assertEquals(outputFieldSpecDesc, outputFieldSpec.getDescription());
		assertEquals(13, outputFieldSpec.getId());

		// Check the output field specification contains 6 non-null Entries
		assertEquals(6, outputFieldSpec.retrieveAllEntries().size());
		for (int i = 0; i < 6; i++) {
			assertNotNull(outputFieldSpec.retrieveAllEntries().get(i));
		}

		/* --- Checking OBJECT SPECIFICATION component --- */

		DataComponent objectSpec = (DataComponent) component.get(12);
		String objectSpecName = "Object Specification";
		String objectSpecDesc = "Entries contained in the Object "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(objectSpec);
		assertEquals(objectSpecName, objectSpec.getName());
		assertEquals(objectSpecDesc, objectSpec.getDescription());
		assertEquals(14, objectSpec.getId());

		// Check the output field specification contains 4 non-null Entries
		assertEquals(4, objectSpec.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(objectSpec.retrieveAllEntries().get(i));
		}

		return;
	}

	@Test
	public void checkV2d() {

		// Create a NekReader to test
		NekReader reader = new NekReader();

		// Give it a test factory to use
		reader.setControllerFactory(new TestNekControllerFactory());

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "nek5000Data";
		String filePath = userDir + separator + "v2d.rea";
		File testFile = new File(filePath);

		// Try to read in invalid .rea file
		File fakeFile = null;
		ArrayList<Component> component = null;
		try {
			component = reader.loadREAFile(fakeFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find fake Nek input file");
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from fake Nek input file");
			e.printStackTrace();
		}
		assertTrue(component == null);

		// Load the .rea file and parse the contents into Components
		try {
			component = reader.loadREAFile(testFile);
		} catch (FileNotFoundException e) {
			fail("Failed to find Nek input file: " + testFile.toString());
			e.printStackTrace();
		} catch (IOException e) {
			fail("Failed to read from Nek input file: " + testFile.toString());
			e.printStackTrace();
		}

		/* --- Checking PARAMETERS component --- */

		// Specify the Parameter DataComponent
		DataComponent paramComponent = (DataComponent) component.get(0);
		String paramName = "Parameters";
		String paramDescription = "Entries contained in the Parameters "
				+ "section of a Nek5000 reafile";

		// Check default values of the Parameter component
		assertNotNull(paramComponent);
		assertEquals(paramName, paramComponent.getName());
		assertEquals(paramDescription, paramComponent.getDescription());
		assertEquals(2, paramComponent.getId());

		// Check the Parameters component contains 103 non-null entries
		assertEquals(103, paramComponent.retrieveAllEntries().size());
		for (int i = 0; i < 103; i++) {
			assertNotNull(paramComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PASSIVE SCALAR DATA component --- */

		DataComponent passiveScalData = (DataComponent) component.get(1);
		String passiveScalDataName = "Passive Scalar Data";
		String passiveScalDataDesc = "Entries contained in the Passive "
				+ "Scalar Data section of a Nek5000 reafile";

		// Check default values of the Passive Scalar Data component
		assertNotNull(passiveScalData);
		assertEquals(passiveScalDataName, passiveScalData.getName());
		assertEquals(passiveScalDataDesc, passiveScalData.getDescription());
		assertEquals(3, passiveScalData.getId());

		// Check the component is empty for this example
		assertEquals(0, passiveScalData.retrieveAllEntries().size());

		/* --- Checking LOGICAL FLAGS component --- */

		// Specify the Logical Switches DataComponent
		DataComponent switchesComponent = (DataComponent) component.get(2);
		String switchesName = "Logical Switches";
		String switchesDescription = "Entries contained in the Logical Switches "
				+ "section of a Nek5000 reafile";

		// Check default values of the Logical Switches component
		assertNotNull(switchesComponent);
		assertEquals(switchesName, switchesComponent.getName());
		assertEquals(switchesDescription, switchesComponent.getDescription());
		assertEquals(4, switchesComponent.getId());

		// Check the DataComponent contains 12 non-null logical switches
		assertEquals(12, switchesComponent.retrieveAllEntries().size());
		for (int i = 0; i < 12; i++) {
			assertNotNull(switchesComponent.retrieveAllEntries().get(i));
		}

		/* --- Checking PRE-NEK AXES component --- */

		DataComponent preNekAxes = (DataComponent) component.get(3);
		String preNekAxesName = "Pre-Nek Axes";
		String preNekAxesDesc = "Entries contained in the Pre-Nek Axes "
				+ "section of a Nek5000 reafile";

		// Check default values of the Pre-Nek Axes component
		assertNotNull(preNekAxes);
		assertEquals(preNekAxesName, preNekAxes.getName());
		assertEquals(preNekAxesDesc, preNekAxes.getDescription());
		assertEquals(5, preNekAxes.getId());

		// Check the Pre-Nek entry is non-null
		assertEquals(1, preNekAxes.retrieveAllEntries().size());
		assertNotNull(preNekAxes.retrieveAllEntries().get(0));

		/* --- Checking MESH component and assigned BOUNDARY CONDITIONS --- */

		MeshComponent meshComponent = (MeshComponent) component.get(4);
		String meshName = "Mesh Data";
		String meshDescription = "Elements contained in the Mesh section of "
				+ "a Nek5000 reafile";

		// Check default values of the mesh component
		assertNotNull(meshComponent);
		assertEquals(meshName, meshComponent.getName());
		assertEquals(meshDescription, meshComponent.getDescription());
		assertEquals(6, meshComponent.getId());

		// Lists of all the edges/vertices for all the polygons
		List<IController> edges = new ArrayList<IController>();
		List<IController> vertices = new ArrayList<IController>();

		// Populate the list by getting the child objects of each polygon
		for (IController polygon : meshComponent.getPolygons()) {
			edges.addAll(polygon.getEntitiesFromCategory(MeshCategory.EDGES));
			vertices.addAll(
					polygon.getEntitiesFromCategory(MeshCategory.VERTICES));
		}

		// Check the mesh contains 64 elements/polygons, 4 edges + 4 vertices
		// each
		assertEquals(20, meshComponent.getPolygons().size());
		assertEquals(80, edges.size());
		assertEquals(80, vertices.size());

		// Check the individual polygons, edges and vertices
		BoundaryCondition currFluidCondition = new BoundaryCondition();
		BoundaryCondition currThermalCondition = new BoundaryCondition();
		BoundaryCondition currScalarCondition = new BoundaryCondition();
		int currEdgeId;
		for (IController currQuad : meshComponent.getPolygons()) {

			/** --- Checking MeshComponent construction --- **/

			// Check current polygon has 4 edges and 4 vertices
			assertEquals(4,
					currQuad.getEntitiesFromCategory(MeshCategory.EDGES).size());
			assertEquals(4, currQuad
					.getEntitiesFromCategory(MeshCategory.VERTICES).size());

			// Verify it has non-null vertices and edges
			for (int j = 0; j < 4; j++) {
				assertNotNull(currQuad.getEntitiesFromCategory(MeshCategory.EDGES)
						.get(j));
				assertNotNull(currQuad
						.getEntitiesFromCategory(MeshCategory.VERTICES).get(j));
			}

			// No need to check if vertices and edges have unique IDs, the
			// construction of Quad does this for us and throws exceptions if
			// there are duplicates

			/** --- Checking BoundaryCondition assignment --- **/

			// Note: BoundaryConditions.type and BoundaryConditions.values don't
			// really need to be throughly tested here as
			// BoundaryConditionsTester
			// ensures they must return valid stuff

			for (int j = 0; j < 4; j++) {

				// Grab the current edge ID
				currEdgeId = Integer.parseInt(
						currQuad.getEntitiesFromCategory(MeshCategory.EDGES)
								.get(j).getProperty(MeshProperty.ID));

				// Grab the fluid boundary condition associate to this edge
				currFluidCondition = ((NekPolygonController) currQuad)
						.getFluidBoundaryCondition(currEdgeId);

				// Check the boundary condition, its type and values
				assertNotNull(currFluidCondition);
				assertNotNull(currFluidCondition.getType());
				assertNotNull(currFluidCondition.getValues());
				assertTrue(currFluidCondition.getValues().size() == 5);

				// Grab the thermal boundary condition associated to this edge
				currThermalCondition = ((NekPolygonController) currQuad)
						.getThermalBoundaryCondition(currEdgeId);

				// Check the boundary condition, its type and values
				assertNotNull(currThermalCondition);
				assertNotNull(currThermalCondition.getType());
				assertNotNull(currThermalCondition.getValues());
				assertTrue(currThermalCondition.getValues().size() == 5);

				// Grab the passive scalar BC and verify it's null (no passive
				// scalars for ray_dd)
				currScalarCondition = ((NekPolygonController) currQuad)
						.getOtherBoundaryCondition(1, currEdgeId);
				assertTrue(currScalarCondition == null);
			}
		}

		/* --- Checking CURVED SIDE DATA component --- */

		MeshComponent curvedSides = (MeshComponent) component.get(5);
		String curvedSidesName = "Curved Side Data";
		String curvedSidesDesc = "Elements contained in the Curved Side "
				+ " section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(curvedSides);
		assertEquals(curvedSidesName, curvedSides.getName());
		assertEquals(curvedSidesDesc, curvedSides.getDescription());
		assertEquals(7, curvedSides.getId());

		// Check that the curved sides component is empty
		assertEquals(0, curvedSides.getPolygons().size());

		/* --- Checking PRESOLVE/RESTART OPTIONS component --- */

		DataComponent presolveRestartOpts = (DataComponent) component.get(6);
		String presolveRestartOptsName = "Pre-solve/Restart Options";
		String presolveRestartOptsDesc = "Entries contained in the Pre-solve/"
				+ "Restart Options section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(presolveRestartOpts);
		assertEquals(presolveRestartOptsName, presolveRestartOpts.getName());
		assertEquals(presolveRestartOptsDesc,
				presolveRestartOpts.getDescription());
		assertEquals(8, presolveRestartOpts.getId());

		// Check that the presolve/restart options component is empty
		assertEquals(0, presolveRestartOpts.retrieveAllEntries().size());

		/* --- Checking INIITIAL CONDITIONS component --- */

		DataComponent initialConditions = (DataComponent) component.get(7);
		String initialConditionsName = "Initial Conditions";
		String initialConditionsDesc = "Entries contained in the Initial "
				+ "Conditions section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(initialConditions);
		assertEquals(initialConditionsName, initialConditions.getName());
		assertEquals(initialConditionsDesc, initialConditions.getDescription());
		assertEquals(9, initialConditions.getId());

		// Check the initial conditions component contains 7 non-null entries
		assertEquals(7, initialConditions.retrieveAllEntries().size());
		for (int i = 0; i < 7; i++) {
			assertNotNull(initialConditions.retrieveAllEntries().get(i));
		}

		/* --- Checking DRIVE FORCE DATA component --- */

		DataComponent driveForceData = (DataComponent) component.get(8);
		String driveForceDataName = "Drive Force Data";
		String driveForceDataDesc = "Entries contained in the Drive Force "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(driveForceData);
		assertEquals(driveForceDataName, driveForceData.getName());
		assertEquals(driveForceDataDesc, driveForceData.getDescription());
		assertEquals(10, driveForceData.getId());

		// Check the drive force data component contains 4 non-null entries
		assertEquals(4, driveForceData.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(driveForceData.retrieveAllEntries().get(i));
		}

		/* --- Checking VARIABLE PROPERTY DATA component --- */

		DataComponent varPropData = (DataComponent) component.get(9);
		String varPropDataName = "Variable Property Data";
		String varPropDataDesc = "Entries contained in the Variable Property "
				+ "Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(varPropData);
		assertEquals(varPropDataName, varPropData.getName());
		assertEquals(varPropDataDesc, varPropData.getDescription());
		assertEquals(11, varPropData.getId());

		// Check the variable property data component contains 1 non-null entry
		assertEquals(1, varPropData.retrieveAllEntries().size());
		assertNotNull(varPropData.retrieveAllEntries().get(0));

		/* --- Checking HISTORY AND INTEGRAL DATA component --- */

		DataComponent histIntegralData = (DataComponent) component.get(10);
		String histIntegralDataName = "History and Integral Data";
		String histIntegralDataDesc = "Entries contained in the History "
				+ "and Integral Data section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(histIntegralData);
		assertEquals(histIntegralDataName, histIntegralData.getName());
		assertEquals(histIntegralDataDesc, histIntegralData.getDescription());
		assertEquals(12, histIntegralData.getId());

		// Check the history and integral data component is empty
		assertEquals(0, histIntegralData.retrieveAllEntries().size());

		/* --- Checking OUTPUT FIELD SPECIFICATION component --- */

		DataComponent outputFieldSpec = (DataComponent) component.get(11);
		String outputFieldSpecName = "Output Field Specification";
		String outputFieldSpecDesc = "Entries contained in the Output Field "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(outputFieldSpec);
		assertEquals(outputFieldSpecName, outputFieldSpec.getName());
		assertEquals(outputFieldSpecDesc, outputFieldSpec.getDescription());
		assertEquals(13, outputFieldSpec.getId());

		// Check the output field specification contains 6 non-null Entries
		assertEquals(6, outputFieldSpec.retrieveAllEntries().size());
		for (int i = 0; i < 6; i++) {
			assertNotNull(outputFieldSpec.retrieveAllEntries().get(i));
		}

		/* --- Checking OBJECT SPECIFICATION component --- */

		DataComponent objectSpec = (DataComponent) component.get(12);
		String objectSpecName = "Object Specification";
		String objectSpecDesc = "Entries contained in the Object "
				+ "Specification section of a Nek5000 reafile";

		// Check the default values
		assertNotNull(objectSpec);
		assertEquals(objectSpecName, objectSpec.getName());
		assertEquals(objectSpecDesc, objectSpec.getDescription());
		assertEquals(14, objectSpec.getId());

		// Check the output field specification contains 4 non-null Entries
		assertEquals(4, objectSpec.retrieveAllEntries().size());
		for (int i = 0; i < 4; i++) {
			assertNotNull(objectSpec.retrieveAllEntries().get(i));
		}

		return;

	}
}
