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
package org.eclipse.ice.proteus.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.jaxbclassprovider.ICEJAXBClassProvider;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.persistence.xml.XMLPersistenceProvider;
import org.eclipse.ice.proteus.PROTEUSModel;
import org.eclipse.ice.proteus.PROTEUSModelBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Class that tests PROTEUSModel methods.
 * 
 * @author w5q
 * 
 */
public class PROTEUSModelTester {

	/**
	 * A fake project and workspace root for tester classes to use when a null
	 * IProject will not work. Setup using the setupFakeProject() method.
	 */
	private static IProject project;
	private IWorkspaceRoot workspaceRoot;

	/**
	 * <p>
	 * This operation sets up the workspace.
	 * </p>
	 */
	@BeforeClass
	public static void beforeTests() {
		// begin-user-code

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "proteusTesterWorkspace";
		// Enable Debugging
		System.setProperty("DebugICE", "");

		// Setup the project
		try {
			// Get the project handle
			IPath projectPath = new Path(userDir + separator + ".project");
			// Create the project description
			IProjectDescription desc = ResourcesPlugin.getWorkspace()
					.loadProjectDescription(projectPath);
			// Get the project handle and create it
			project = workspaceRoot.getProject(desc.getName());
			// Create the project if it doesn't exist
			if (!project.exists()) {
				project.create(desc, new NullProgressMonitor());
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(new NullProgressMonitor());
			}
			// Refresh the workspace
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// Catch exception for creating the project
			e.printStackTrace();
			fail("PROTEUS Model Tester: Error!  Could not set up project space");
		}

		// Set the global project reference.

		return;
		// end-user-code
	}

	/**
	 * Tests the PROTEUSModel constructors. Only tests that objects inherit
	 * correctly from the parent class (Item). All other relevant construction
	 * tests are in the checkItemInfoSetup() method, as the method it tests
	 * (setupItemInfo()) is always called by the parent class during
	 * construction.
	 * 
	 * @author w5q
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Call nullary constructor
		PROTEUSModel model = new PROTEUSModel();

		// Verify it inherits correctly
		assertTrue(model instanceof Item);

		// Call parameterized constructor
		model = new PROTEUSModel(project);

		// Verify it inherits correctly
		assertTrue(model instanceof Item);
	}

	/**
	 * Tests setupItemInfo() method of the PROTEUSModel class. Is somewhat like
	 * an extension of the construction tester. Only tests PROTEUS-specific
	 * attributes, as all other values will have been set and tested by the
	 * parent class's tester (ItemTester).
	 * 
	 * @author w5q
	 * 
	 */
	@Test
	public void checkItemInfoSetup() {

		// Create default values to check against
		int defaultId = 1;
		String defaultName = "PROTEUS Model";
		String defaultDesc = "Generate input files for the PROTEUS-SN neutron transport simulator";
		ItemType defaultType = ItemType.Model;
		ArrayList<String> defaultActions = new ArrayList<String>();
		defaultActions.add("Write PROTEUS Input File");
		defaultActions.add("Export to ICE Native Format");

		// Call nullary constructor to test
		PROTEUSModel model = new PROTEUSModel();

		// Test default PROTEUS-specific values
		assertEquals(defaultId, model.getId());
		assertEquals(defaultName, model.getName());
		assertEquals(defaultDesc, model.getDescription());
		assertEquals(defaultType, model.getItemType());
		assertEquals(defaultActions, model.getAvailableActions());

		// Call parameterized constructor
		model = new PROTEUSModel(project);

		// Test default PROTEUS-specific values
		assertEquals(defaultId, model.getId());
		assertEquals(defaultName, model.getName());
		assertEquals(defaultDesc, model.getDescription());
		assertEquals(defaultType, model.getItemType());
		assertEquals(defaultActions, model.getAvailableActions());
	}

	/**
	 * Tests the creation and setting of the PROTEUSModel Form, DataComponent
	 * and corresponding Entries. This test assumes the use of the
	 * PROTEUS_Model_Builder/ICEProteusInput.xml input scheme, verifying the
	 * model's Form contains 6 DataComponents, each containing 5 fake entries.
	 * 
	 * @author w5q
	 * @throws JAXBException
	 * 
	 */
	@Test
	public void checkFormSetup() throws JAXBException {

		// Create a PROTEUS model to test
		PROTEUSModel model = new PROTEUSModel(project);
		Form form = model.getForm();

		// Check the form's Component (should have 6 components, which
		// corresponds to the to ICEProteusInput.xml file
		assertEquals(form.getNumberOfComponents(), 4);
	}

	/**
	 * Tests the creation and writing of a PROTEUS-specific input file (.inp),
	 * based upon parameters set in PROTEUSModel's setupForm() method (which is
	 * always executed first in the super constructor).
	 * 
	 * @author w5q
	 * 
	 */
	@Test
	public void checkProcess() {

		String[] fileLines = { "!Required Options ", "option=invalue",
				"option2=4", "tabbedOption=tabbedValue",
				"spacedOption=spacedValue", "section2val=value",
				"anotherVar=anotherValue", "", "!First Section ",
				"section1var=value", "newvariable=newvalue",
				"newTabbedOption=tabbedValue", "newSpacedOption=spacedValue",
				"", "!Second Section ", "section2var=nothing", "",
				"!Third Section ", "section3var=nope", ""};

		// Create a model to test
		PROTEUSModel model = new PROTEUSModel(project);

		// Try to process it with an invalid action
		assertEquals(FormStatus.InfoError, model.process("FIRE ZE MISSILES!"));

		// Process the model with a valid action
		model.process("Write PROTEUS Input File");

		// Verify the PROTEUS output file was created
		IFile outputFile;
		outputFile = project.getFile("PROTEUS_Model_" + model.getId() + ".inp");
		assertTrue(outputFile.exists());

		// Read in the contents of the output file, and verify it's not null
		InputStream fileContents = null;
		try {
			fileContents = outputFile.getContents();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		assertNotNull(fileContents);

		// Convert file contents from InputStream to String
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				fileContents));
		String line;
		int numberLines = 0;
		try {
			while ((line = reader.readLine()) != null) {
				assertEquals(line, fileLines[numberLines]);
				++numberLines;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertEquals(20, numberLines);

		// Verify there are as many lines in the PROTEUS file as there are
		// entries
		Form form = model.getForm();
		assertEquals(4, form.getNumberOfComponents());

	}

	/**
	 * Clean up after ourselves
	 */
	@AfterClass
	public static void cleanup() {
		try {
			project.close(null);
			project.delete(true, null);
		} catch (CoreException e) {

			e.printStackTrace();
			fail("PROTEUS Model Tester: Error!  Could not clean up project space");
		}
	}

}
