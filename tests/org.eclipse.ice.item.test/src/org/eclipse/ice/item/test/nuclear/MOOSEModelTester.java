/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.item.nuclear.MOOSEModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the MOOSEModel Item to make sure that it can correctly
 * create its Form and process a modified Form.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@Ignore
public class MOOSEModelTester {

	/**
	 * The project space used to create the workspace for the tests.
	 */
	private static IProject projectSpace;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the workspace. It copies the necessary MOOSE data
	 * files into ${workspace}/MOOSE.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void beforeTests() {
		// begin-user-code

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
				defaultProjectLocation = (new File(
						userDir + separator
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
			bisonYAMLFile.create(bisonStream, true, null);

			// Create the File handle and input stream for the Bison input
			// file
			IPath moosePath = new Path(filePath);
			File mooseFile = moosePath.toFile();
			FileInputStream mooseStream = new FileInputStream(mooseFile);
			// Create the file in the workspace for the Bison input file
			IFile bisonInputFile = project.getFile("input_coarse10.i");
			bisonInputFile.create(mooseStream, true, null);

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

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the MOOSEModel and makes sure that it can properly
	 * construct its Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// Create a MOOSEModel to test
		MOOSEModel model = setupMOOSEItem();

		// Check the form
		Form form = model.getForm();
		assertNotNull(form);
		assertEquals(3, form.getComponents().size());

		// Check the data component
		assertTrue(form.getComponent(MOOSEModel.fileDataComponentId) instanceof DataComponent);

		// Check the MOOSE app entry
		Entry mooseAppEntry = ((DataComponent) form
				.getComponent(MOOSEModel.fileDataComponentId))
				.retrieveEntry("MOOSE-Based Application");
		assertNotNull(mooseAppEntry);
		assertEquals(1, mooseAppEntry.getId());
		assertEquals("MOOSE app...", mooseAppEntry.getValue());

		// Check the output file Entry
		Entry outputFileEntry = ((DataComponent) form
				.getComponent(MOOSEModel.fileDataComponentId))
				.retrieveEntry("Output File Name");
		assertNotNull(outputFileEntry);
		assertEquals(2, outputFileEntry.getId());
		assertEquals("mooseModel.i", outputFileEntry.getValue());

		// Check the input tree composite.
		assertTrue(form.getComponent(2) instanceof TreeComposite);
		
		// Check the YAML tree composite
		assertTrue(form.getComponent(3) instanceof TreeComposite);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the MOOSEModel to make sure that it can correctly
	 * process its Form and generate a MOOSE input file.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkProcessing() {
		// begin-user-code

		// Local Declarations
		String testFilename = "bison_test_file.inp";

		// Create a MOOSEModel to test
		MOOSEModel model = setupMOOSEItem();

		// Check the form
		Form form = model.getForm();
		assertNotNull(form);

		// Check the action list
		assertEquals(2, form.getActionList().size());
		assertTrue(form.getActionList().contains("Write MOOSE File"));

		// Change the output file name to make sure that it is possible
		Entry outputFileEntry = ((DataComponent) form.getComponent(1))
				.retrieveEntry("Output File Name");
		outputFileEntry.setValue(testFilename);

		// Resubmit the form
		assertEquals(FormStatus.ReadyToProcess, model.submitForm(form));

		// Direct the model to write the output file
		assertEquals(FormStatus.Processed, model.process("Write MOOSE File"));

		// Check that the file exists
		assertTrue(projectSpace.getFile(testFilename).exists());

		return;
		// end-user-code
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
		MOOSEModel mooseItem = setupMOOSEItem();

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
		ArrayList<Entry> parameters = dataNode.retrieveAllEntries();
		assertNotNull(parameters);
		assertEquals(3, parameters.size());
		// Check the first one
		Entry param = parameters.get(0);
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation configures a MOOSEModel. It is used by both test
	 * operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A newly instantiated MOOSEModel.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private MOOSEModel setupMOOSEItem() {
		// begin-user-code

		// Local Declarations
		MOOSEModel model = new MOOSEModel(projectSpace);

		return model;
		// end-user-code
	}

	/**
	 * Closes the MOOSE tester workspace created in the BeforeClass method.
	 */
	@AfterClass
	public static void afterTests() {
		// Close and delete the fake workspace created
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		try {
			projectSpace.close(null);
			//workspaceRoot.delete(true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return;
	}

}