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

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.proteus.PROTEUSModel;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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
	private IProject project;
	private IWorkspaceRoot workspaceRoot;

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

		// Setup a fake project
		setupFakeProject();

		// Call parameterized constructor
		model = new PROTEUSModel(project);

		// Verify it inherits correctly
		assertTrue(model instanceof Item);

		// Close and delete the fake workspace created
		try {
			project.close(null);
			workspaceRoot.delete(true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
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
		String defaultName = "PROTEUS Model Builder";
		String defaultDesc = "This item builds models for "
				+ "PROTEUS-based applications for simulating "
				+ "sodium-cooled fast reactors.";
		ItemType defaultType = ItemType.Model;
		ArrayList<String> defaultActions = new ArrayList<String>();
		defaultActions.add("Export to ICE Native Format");
		defaultActions.add("Write PROTEUS File");

		// Call nullary constructor to test
		PROTEUSModel model = new PROTEUSModel();

		// Test default PROTEUS-specific values
		assertEquals(defaultId, model.getId());
		assertEquals(defaultName, model.getName());
		assertEquals(defaultDesc, model.getDescription());
		assertEquals(defaultType, model.getItemType());
		assertEquals(defaultActions, model.getAvailableActions());

		// Setup a fake project
		setupFakeProject();

		// Call parameterized constructor
		model = new PROTEUSModel(project);

		// Test default PROTEUS-specific values
		assertEquals(defaultId, model.getId());
		assertEquals(defaultName, model.getName());
		assertEquals(defaultDesc, model.getDescription());
		assertEquals(defaultType, model.getItemType());
		assertEquals(defaultActions, model.getAvailableActions());

		// Close and delete the fake workspace created
		try {
			project.close(null);
			workspaceRoot.delete(true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tests the creation and setting of the PROTEUSModel Form, DataComponent
	 * and corresponding Entries. This test assumes the use of the
	 * PROTEUS_Model_Builder/ICEProteusInput.xml input scheme, verifying the
	 * model's Form contains 6 DataComponents, each containing 5 fake entries.
	 * 
	 * @author w5q
	 * 
	 */
	@Test
	public void checkFormSetup() {

		// Setup the fake project and generate a test XML input file
		setupFakeProject();
		createXMLInput();

		// Create a PROTEUS model to test
		PROTEUSModel model = new PROTEUSModel(project);
		Form form = model.getForm();

		// Check the form's Component (should have 6 components, which
		// corresponds to the to ICEProteusInput.xml file
		assertEquals(form.getNumberOfComponents(), 6);

		// Initialize a DataComponent to use in tests
		DataComponent modelDataComponent = null;

		for (int i = 0; i < form.getNumberOfComponents(); i++) {

			// Cast the Component to DataComponent to access entries, verify
			// entries exist
			modelDataComponent = (DataComponent) form.getComponents().get(i);
			assertEquals(5, modelDataComponent.retrieveAllEntries().size());

			// Verify entries of each DataComponent are not null
			for (int j = 0; j < modelDataComponent.retrieveAllEntries().size(); j++) {
				Entry entry = modelDataComponent.retrieveAllEntries().get(j);
				assertNotNull(entry);
			}
		}

		// FIXME This approach is rather simplistic and doesn't actually verify
		// the
		// contents of the Entries. If we wanted to be thorough, we could
		// construct
		// a fake DataComponent that contains the Entries of ICEProteusInput.xml
		// As of right now though, createXMLFile() just creates a form with 6
		// DataComponents and 5 arbitrary entries each.

		// Close and delete the fake workspace created
		try {
			project.close(null);
			workspaceRoot.delete(true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
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

		// Setup the fake project and XML input file
		setupFakeProject();
		createXMLInput();

		// Create a model to test
		PROTEUSModel model = new PROTEUSModel(project);

		// Try to process it with an invalid action
		assertEquals(FormStatus.InfoError, model.process("FIRE ZE MISSILES!"));

		// Process the model with a valid action
		model.process("Write PROTEUS File");

		// Verify the PROTEUS output file was created
		IFile outputFile;
		outputFile = project.getFile("proteus_neutronics_" + model.getId()
				+ ".inp");
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
		StringBuilder builder = new StringBuilder();
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Split the file contents at every instance of "\t!" to represent one
		// line. It would be more logical to use every instance of a "\n" to
		// represent 1 line, but newline characters seem to get fudged up in the
		// InputStream. Using "\t!" instead will result in a String array with
		// (n + 1) elements, where n is the number of lines in the file.
		String[] splitLines = (builder.toString()).split("\t!");

		// Verify there are as many lines in the PROTEUS file as there are
		// entries
		// in the model's 6 DataComponents (6 components x 5 entries each = 30)
		Form form = model.getForm();
		assertEquals(6, form.getNumberOfComponents());

		DataComponent component;
		int entriesCounter = 0;
		for (int i = 0; i < form.getNumberOfComponents(); i++) {
			component = (DataComponent) form.getComponents().get(i);
			entriesCounter += component.retrieveAllEntries().size();
		}
		assertEquals(splitLines.length - 1, entriesCounter);

		// Close and delete the fake workspace created
		try {
			project.close(null);
			workspaceRoot.delete(true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates a fake workspace in the bundle for all PROTEUSModel unit tests to
	 * utilize throughout their execution. Once the workspace resource is no
	 * longer needed, it is left to each individual test to close and delete the
	 * project workspace.
	 * 
	 * @author w5q
	 * 
	 */
	public void setupFakeProject() {
		// Setup a dummy project workspace
		URI projectLocation = null;
		String separator = System.getProperty("file.separator");

		try {
			// Get the project handle
			workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			project = workspaceRoot.getProject("PROTEUSTesterWorkspace");

			// If the project does not exist, create it
			if (!project.exists()) {

				// Define the location as
				// ${workspace_loc}/PROTEUSTesterWorkspace
				projectLocation = (new File(System.getProperty("user.dir")
						+ separator + "PROTEUSTesterWorkspace")).toURI();

				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("PROTEUSTesterWorkspace");

				// Set the location of the project
				desc.setLocationURI(projectLocation);

				// Create the project
				project.create(desc, null);
			}

			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}

		} catch (CoreException e) {
			e.printStackTrace();
			fail("Project workspace could not be created.");
		}

	}

	/**
	 * Creates a DataComponent with 32 entries, to mimic the contents of
	 * ICEProteusInput.xml. Writes the DataComponent into an XML file in the
	 * tester project workspace.
	 * 
	 * @author w5q
	 * 
	 */
	public void createXMLInput() {

		// Local declarations
		DataComponent dataComponent;
		Entry entry;
		Form form = new Form();
		ArrayList<String> actionList = new ArrayList<String>();
		actionList.add("Export to key-value pair output");
		actionList.add("Write PROTEUS File");
		form.setActionList(actionList);

		// Create 6 DataComponents and append them to the form
		for (int i = 1; i <= 6; i++) {

			// Define the data component and add it to the form
			dataComponent = new DataComponent();
			dataComponent.setName("Neutronics Parameters");
			dataComponent
					.setDescription("SHARP Parameters for the PROTEUS Neutronics Module");
			dataComponent.setId(i);

			// Generate 5 fake entries and append them to the DataComponent
			for (int j = 1; j <= 5; j++) {

				entry = new Entry() {
					protected void setup() {
						this.setName("fakeName");
						this.tag = "";
						this.setDescription("fakeDescription");
						this.allowedValues = new ArrayList<String>();
						this.defaultValue = "fakeValue";
						this.value = this.defaultValue;
						this.allowedValueType = AllowedValueType.Undefined;
					}
				};

				// Set a unique ID and add the entry to the DataComponent
				entry.setId(j);
				dataComponent.addEntry(entry);
			}

			// Add the component to the form
			form.addComponent(dataComponent);
		}

		// Set the folder in which the XML file will be located
		IFolder folder = project.getFolder("PROTEUS_Model_Builder");

		// If it already exists, delete it
		if (folder.exists()) {
			try {
				folder.delete(true, null);
			} catch (CoreException e) {
				e.printStackTrace();
				fail("Could not delete PROTEUS_Model_Builder folder");
			}
		}

		// If it doesn't exist, create it
		if (!folder.exists()) {
			try {
				folder.create(true, true, null);
			} catch (CoreException e1) {
				e1.printStackTrace();
				fail("Could not create PROTEUS_Model_Builder folder");
			}
		}

		// Check to see if the ICEProteusInput.xml file already exists
		IFile file = folder.getFile("ICEProteusInput.xml");

		// If it already exists, delete it
		if (file != null) {
			try {
				file.delete(true, null);
			} catch (CoreException e) {
				e.printStackTrace();
				fail("Could not delete ICEProteusInput.xml");
			}
		}

		// Create the OutputStream to copy information
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Persist the form to XML
		form.persistToXML(outputStream);

		// Create the file
		ByteArrayInputStream source;

		// Convert the outputStream to inputStream
		source = new ByteArrayInputStream(outputStream.toByteArray());

		IFile inputFile = folder.getFile("ICEProteusInput.xml");

		// Try to create the file
		try {
			inputFile.create(source, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
			fail("Could not create ICEProteusInput.xml");
		}

	}
}
