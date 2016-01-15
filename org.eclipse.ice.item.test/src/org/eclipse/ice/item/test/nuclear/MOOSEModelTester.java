/*******************************************************************************
 * Copyright (c) 2013, 2014- UT-Battelle, LLC.
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
package org.eclipse.ice.item.test.nuclear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.item.nuclear.MOOSE;
import org.eclipse.ice.item.nuclear.MOOSEModel;
import org.eclipse.ice.item.test.FakeIOService;
import org.eclipse.ice.item.utilities.moose.MOOSEFileHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the MOOSEModel Item to make sure that it can correctly
 * create its Form and process a modified Form.
 * 
 * @author Jay Jay Billings
 */
public class MOOSEModelTester {

	/**
	 * The project space used to create the workspace for the tests.
	 */
	private static IProject projectSpace;

	/**
	 * The IO Service used to read/write via MOOSEFileHandler.
	 */
	private static IIOService service;

	/**
	 * This operation sets up the workspace. It copies the necessary MOOSE data
	 * files into ${workspace}/MOOSE.
	 */
	@BeforeClass
	public static void beforeTests() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String projectName = "MOOSEModelTesterWorkspace";
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "itemData";
		String yamlFile = userDir + separator + "bison.yaml";
		String filePath = userDir + separator + "input_coarse10.i";

		// Debug information
		System.out.println("MOOSE Test Data File: " + filePath);

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as
				// ${workspace_loc}/MOOSEModelTesterWorkspace
				defaultProjectLocation = (new File(userDir + separator
						+ projectName)).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription(projectName);
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}

			// Create the File handle and input stream for the Bison YAML
			// file
			IPath bisonPath = new Path(yamlFile);
			File bisonFile = bisonPath.toFile();
			FileInputStream bisonStream = new FileInputStream(bisonFile);
			// Create the file in the workspace for the Bison YAML file
			IFile bisonYAMLFile = project.getFile("bison.yaml");
			if (!bisonYAMLFile.exists()) {
				bisonYAMLFile.create(bisonStream, true, null);
			}

			// Create the File handle and input stream for the Bison input
			// file
			IPath moosePath = new Path(filePath);
			File mooseFile = moosePath.toFile();
			FileInputStream mooseStream = new FileInputStream(mooseFile);
			// Create the file in the workspace for the Bison input file
			IFile bisonInputFile = project.getFile("input_coarse10.i");
			if (!bisonInputFile.exists()) {
				bisonInputFile.create(mooseStream, true, null);
			}

			// Refresh the workspace
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// Catch exception for creating the project
			e.printStackTrace();
			fail();
		} catch (FileNotFoundException e) {
			// Catch exception for failing to load the file
			e.printStackTrace();
			fail();
		}

		// Set the global project reference.
		projectSpace = project;

		// Set up an IO service and add a reader and writer
		service = new FakeIOService();
		service.addWriter(new MOOSEFileHandler());
		service.addReader(new MOOSEFileHandler());

		return;
	}

	/**
	 * This operation checks the MOOSEModel and makes sure that it can properly
	 * construct its Form.
	 */
	@Test
	public void checkConstruction() {

		// Create a MOOSEModel to test
		MOOSEModel model = new MOOSEModel(projectSpace);

		// Check the form
		Form form = model.getForm();
		assertNotNull(form);
		assertEquals(3, form.getComponents().size());

		// Check the data component
		assertTrue(form.getComponent(MOOSEModel.fileDataComponentId) instanceof DataComponent);

		// Check the MOOSE app entry
		IEntry mooseAppEntry = ((DataComponent) form
				.getComponent(MOOSEModel.fileDataComponentId))
				.retrieveEntry("MOOSE-Based Application");
		assertNotNull(mooseAppEntry);
		assertEquals(1, mooseAppEntry.getId());
		assertEquals("Import Application", mooseAppEntry.getDefaultValue());
		assertEquals(mooseAppEntry.getDefaultValue(), mooseAppEntry.getValue());

		// Check the output file Entry
		IEntry outputFileEntry = ((DataComponent) form
				.getComponent(MOOSEModel.fileDataComponentId))
				.retrieveEntry("Output File Name");
		assertNotNull(outputFileEntry);
		assertEquals(2, outputFileEntry.getId());
		assertEquals("mooseModel.i", outputFileEntry.getValue());

		// Check the TreeComposite
		assertTrue(form.getComponent(MOOSEModel.mooseTreeCompositeId) instanceof TreeComposite);

		// Check the ResourceComponent
		assertTrue(form.getComponent(MOOSEModel.resourceComponentId) instanceof ResourceComponent);

		return;
	}

	/**
	 * This operation checks the MOOSEModel to make sure that it can correctly
	 * process its Form and generate a MOOSE input file.
	 */
	@Test
	public void checkProcessing() {

		// Local Declarations
		String testFilename = "bison_test_file.inp";

		// Create a MOOSEModel to test
		MOOSEModel model = new MOOSEModel(projectSpace);

		// Set the IOService on the model so we can write out
		model.setIOService(service);

		// Check the form
		Form form = model.getForm();
		assertNotNull(form);

		// Check the action list
		assertEquals(2, form.getActionList().size());
		assertTrue(form.getActionList().contains("Write MOOSE File"));

		// FIXME REPLACE WITH PATH TO ICETESTS...
//		Entry appName = ((DataComponent) form.getComponent(1))
//				.retrieveEntry("MOOSE-Based Application");
//		appName.setValue("file:/Users/aqw/ICEFiles_prebuiltMoose/moose/test/moose_test-opt");

		// Change the output file name to make sure that it is possible
		IEntry outputFileEntry = ((DataComponent) form.getComponent(1))
				.retrieveEntry("Output File Name");
		outputFileEntry.setValue(testFilename);

		// Resubmit the form
		assertEquals(FormStatus.ReadyToProcess, model.submitForm(form));

		// Direct the model to write the output file
		assertEquals(FormStatus.Processed, model.process("Write MOOSE File"));

		// Check that the file exists
		assertTrue(projectSpace.getFile(testFilename).exists());

		return;
	}

	/**
	 * This operation is responsible for ensuring that the MOOSEModel can load
	 * input. This operation checks this by passing it an input file for Bison.
	 */
	@Test
	public void checkLoadingInput() {

		// Local Declarations
		int numMooseBlocks = 20;

		// Create a MOOSE Item
		MOOSEModel mooseItem = new MOOSEModel(projectSpace);

		// Set the IO service on the item so we can read/load data in
		mooseItem.setIOService(service);

		// Load the input
		mooseItem.loadInput("input_coarse10.i");

		// Pull the Form
		Form form = mooseItem.getForm();

		// Get the TreeComposite to search it for known blocks and parameters
		TreeComposite mooseTree = (TreeComposite) form
				.getComponent(MOOSEModel.mooseTreeCompositeId);

		// Get its children
		ArrayList<Component> subMooseTrees = new ArrayList<Component>();
		int loopSize = mooseTree.getNumberOfChildren();
		for (int i = 0; i < loopSize; i++) {
			subMooseTrees.add(mooseTree.getChildAtIndex(i));
		}

		// Make sure we have the correct number of sub trees
		assertEquals(numMooseBlocks, subMooseTrees.size());

		// Get the names of the blocks
		ArrayList<String> blockNames = new ArrayList<String>();
		loopSize = subMooseTrees.size();
		int outputsBlockIndex = 0;
		for (int i = 0; i < loopSize; i++) {
			// System.out.println("Blocknames: " +
			// subMooseTrees.get(i).getName());
			blockNames.add(subMooseTrees.get(i).getName());

			// Determine the index of the "Outputs" block (used in the next set
			// of tests)
			if ("Outputs".equals(subMooseTrees.get(i).getName())) {
				outputsBlockIndex = i;
			}
		}

		// Check all of the block names. They should all be in the tree.
		assertTrue(blockNames.contains("GlobalParams"));
		assertTrue(blockNames.contains("Problem"));
		assertTrue(blockNames.contains("Mesh"));
		assertTrue(blockNames.contains("Variables"));
		assertTrue(blockNames.contains("AuxVariables"));
		assertTrue(blockNames.contains("Functions"));
		assertTrue(blockNames.contains("SolidMechanics"));
		assertTrue(blockNames.contains("Kernels"));
		assertTrue(blockNames.contains("Burnup"));
		assertTrue(blockNames.contains("AuxKernels"));
		assertTrue(blockNames.contains("AuxBCs"));
		assertTrue(blockNames.contains("Contact"));
		assertTrue(blockNames.contains("ThermalContact"));
		assertTrue(blockNames.contains("BCs"));
		assertTrue(blockNames.contains("CoolantChannel"));
		assertTrue(blockNames.contains("Materials"));
		assertTrue(blockNames.contains("Dampers"));
		assertTrue(blockNames.contains("Executioner"));
		assertTrue(blockNames.contains("Postprocessors"));
		assertTrue(blockNames.contains("Outputs"));

		// Get the "Outputs" block and its data node
		TreeComposite outputsTree = (TreeComposite) subMooseTrees
				.get(outputsBlockIndex);
		DataComponent dataNode = (DataComponent) outputsTree
				.getActiveDataNode();
		assertNotNull(dataNode);

		// Get the parameters off the data node
		ArrayList<IEntry> parameters = dataNode.retrieveAllEntries();
		assertNotNull(parameters);
		assertEquals(3, parameters.size());
		// Check the first one
		IEntry param = parameters.get(0);
		assertEquals("interval", param.getName());
		assertEquals("1", param.getValue());
		// Check the second one
		param = parameters.get(1);
		assertEquals("output_initial", param.getName());
		assertEquals("true", param.getValue());
		// Check the third one
		param = parameters.get(2);
		assertEquals("exodus", param.getName());
		assertEquals("true", param.getValue());

		// Get the console block off the output block
		TreeComposite consoleTree = outputsTree.getChildAtIndex(0);
		assertNotNull(consoleTree);
		assertEquals("console", consoleTree.getName());

		// Get the parameters from the console block
		dataNode = (DataComponent) consoleTree.getActiveDataNode();
		parameters = dataNode.retrieveAllEntries();
		assertNotNull(parameters);
		assertEquals(4, parameters.size());
		// Check the first one
		param = parameters.get(0);
		assertEquals("type", param.getName());
		assertEquals("Console", param.getValue());
		// Check the second one
		param = parameters.get(1);
		assertEquals("perf_log", param.getName());
		assertEquals("true", param.getValue());
		// Check the third one
		param = parameters.get(2);
		assertEquals("linear_residuals", param.getName());
		assertEquals("true", param.getValue());
		// Check the fourth one
		param = parameters.get(3);
		assertEquals("max_rows", param.getName());
		assertEquals("25", param.getValue());

		return;
	}

	/**
	 * <p>
	 * This operation checks the MOOSE Item to ensure that its equals()
	 * operation works.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Create JobLauncherItems to test
		MOOSEModel equalItem = new MOOSEModel(projectSpace);
		MOOSEModel unEqualItem = new MOOSEModel();
		MOOSEModel transitiveItem = new MOOSEModel(projectSpace);

		// Create a MOOSE Item
		MOOSEModel item = new MOOSEModel(projectSpace);

		// Set the IO service on the item so we can read/load data in
		item.setIOService(service);
		equalItem.setIOService(service);
		transitiveItem.setIOService(service);
		
		// Load the input
		item.loadInput("input_coarse10.i");
		equalItem.loadInput("input_coarse10.i");
		transitiveItem.loadInput("input_coarse10.i");
		
		// Set ICEObject data
		equalItem.setId(item.getId());
		transitiveItem.setId(item.getId());
		unEqualItem.setId(2);

		// Set names
		equalItem.setName(item.getName());
		transitiveItem.setName(item.getName());
		unEqualItem.setName("DC UnEqual");

		// Assert two equal Items return true
		assertTrue(item.equals(equalItem));

		// Assert two unequal Items return false
		assertFalse(item.equals(unEqualItem));

		// Assert equals() is reflexive
		assertTrue(item.equals(item));

		// Assert the equals() is Symmetric
		assertTrue(item.equals(equalItem) && equalItem.equals(item));

		// Assert equals() is transitive
		if (item.equals(equalItem) && equalItem.equals(transitiveItem)) {
			assertTrue(item.equals(transitiveItem));
		} else {
			fail();
		}

		// Assert equals is consistent
		assertTrue(item.equals(equalItem) && item.equals(equalItem)
				&& item.equals(equalItem));
		assertTrue(!item.equals(unEqualItem) && !item.equals(unEqualItem)
				&& !item.equals(unEqualItem));

		// Assert checking equality with null is false
		assertFalse(item == null);

		// Assert that two equal objects return same hashcode
		assertTrue(item.equals(equalItem)
				&& item.hashCode() == equalItem.hashCode());

		// Assert that hashcode is consistent
		assertTrue(item.hashCode() == item.hashCode());

		// Assert that hashcodes from unequal objects are different
		assertTrue(item.hashCode() != unEqualItem.hashCode());

	}

	/**
	 * <p>
	 * This operation checks the MOOSE to ensure that its copy() and clone()
	 * operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Local Declarations
		MOOSE cloneItem = new MOOSE(null), copyItem = new MOOSE(null);
		MOOSE mooseItem = new MOOSE(projectSpace);

		mooseItem.setIOService(service);
		mooseItem.loadInput("input_coarse10.i");
		
		mooseItem.setDescription("I am a job!");
		mooseItem.setProject(null);

		// run clone operations
		cloneItem = (MOOSE) mooseItem.clone();

		// check contents
		assertEquals(mooseItem.getAvailableActions(),
				cloneItem.getAvailableActions());
		assertEquals(mooseItem.getDescription(), cloneItem.getDescription());
		assertTrue(mooseItem.getForm().equals(cloneItem.getForm()));
		assertEquals(mooseItem.getId(), cloneItem.getId());
		assertEquals(mooseItem.getItemType(), cloneItem.getItemType());
		assertEquals(mooseItem.getName(), cloneItem.getName());
		assertEquals(mooseItem.getStatus(), cloneItem.getStatus());

		// run copy operation
		copyItem.copy(mooseItem);

		// check contents
		assertEquals(mooseItem.getAvailableActions(),
				copyItem.getAvailableActions());
		assertEquals(mooseItem.getDescription(), copyItem.getDescription());
		assertTrue(mooseItem.getForm().equals(copyItem.getForm()));
		assertEquals(mooseItem.getId(), copyItem.getId());
		assertEquals(mooseItem.getItemType(), copyItem.getItemType());
		assertEquals(mooseItem.getName(), copyItem.getName());
		assertEquals(mooseItem.getStatus(), copyItem.getStatus());

		// run copy operation by passing null
		copyItem.copy(null);

		// check contents - nothing has changed
		assertEquals(mooseItem.getAvailableActions(),
				copyItem.getAvailableActions());
		assertEquals(mooseItem.getDescription(), copyItem.getDescription());
		assertTrue(mooseItem.getForm().equals(copyItem.getForm()));
		assertEquals(mooseItem.getId(), copyItem.getId());
		assertEquals(mooseItem.getItemType(), copyItem.getItemType());
		assertEquals(mooseItem.getName(), copyItem.getName());
		assertEquals(mooseItem.getStatus(), copyItem.getStatus());

		return;
	}

	/**
	 * Closes the MOOSE tester workspace created in the BeforeClass method.
	 */
	@AfterClass
	public static void afterTests() {
		try {
			// Close and delete the fake workspace created
			projectSpace.close(null);

			// Nullify the IO service
			service = null;
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return;
	}

}
