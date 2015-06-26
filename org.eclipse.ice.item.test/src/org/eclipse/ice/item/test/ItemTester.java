/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.jaxbclassprovider.ICEJAXBClassProvider;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemListener;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.messaging.Message;
import org.eclipse.ice.persistence.xml.XMLPersistenceProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * The ItemTester is responsible for testing the Item class.
 * </p>
 * 
 * @author Jay Jay Billings, Anna Wojtowicz
 */
public class ItemTester implements ItemListener {
	/**
	 * An Item used for testing.
	 */
	private static Item item;

	/**
	 * This field is used to check the Item's ability to update its listeners.
	 * It is part of the checkUpdates() test.
	 */
	private boolean updated = false;

	/**
	 * <p>
	 * A string initialized to hold the entire contents of an Item in the
	 * Painfully Simple Form format. It is used for testing across multiple
	 * tests and is first converted to an InputStream. It contains two Entries
	 * who have a third Entry as a parent.
	 * </p>
	 * 
	 */
	private static String psfItemString;

	/**
	 * Sets up the psfItemString.
	 */
	@BeforeClass
	public static void beforeClass() {

		ArrayList<String> PSFForm = new ArrayList<String>();

		/**
		 * Setup the string containing the Form in PSF format. This was taken
		 * from the Painfully Simple Form article at
		 * 
		 * https://wiki.eclipse.org/Painfully_Simple_Form
		 * 
		 * and is a good example because it is complete, contains lots of
		 * whitespace and comments and, of course, interesting! I have added
		 * whitespaces and comments in some places to make the test more
		 * rigorous and changed some of the comment statements from "//" to "#"
		 * to cover all the possibilities.
		 */
		PSFForm.add("\t  \n");
		PSFForm.add("#Form name and type\n");
		PSFForm.add("formName=PSF Wiki Article Form\n");
		PSFForm.add("formDescription=A PSF Wiki Article Sample\n");
		PSFForm.add("formType=Model\n");
		PSFForm.add(" \n");
		PSFForm.add("#The DataComponents block - it must come first!\n");
		PSFForm.add("group=Assembly\n");
		PSFForm.add("groupDescription=Relevant quantities for modeling a full assembly\n");
		PSFForm.add("\n");
		PSFForm.add("#The Entry blocks will appear below this line");
		PSFForm.add("\n");
		PSFForm.add("\t  \n");
		PSFForm.add("#Some comments to ignore at the top\n");
		PSFForm.add("//More comments to ignore at the top\n");
		PSFForm.add("name=Coolant Temperature                        "
				+ "                                #The name that a user "
				+ "will see\n");
		PSFForm.add("description=The temperature of the coolant that surrounds "
				+ "the assembly and pins //A description that will help the user\n");
		PSFForm.add("defaultValue=550                                          "
				+ "                      //The default value\n");
		PSFForm.add("allowedValueType=Continuous                               "
				+ "                      //Indicates that the value can be "
				+ "anything between 550 and 650 K.\n");
		PSFForm.add("allowedValue=550                                          "
				+ "                      //The lower bound of the range\n");
		PSFForm.add("allowedValue=650                                          "
				+ "                      //The upper bound of the range\n");
		PSFForm.add("tag=coolantTemperature                                    "
				+ "                      //A tag to mark it\n");
		PSFForm.add("parent=Full Assembly Flag                         "
				+ "                              //The parent\n");
		PSFForm.add("group=Assembly                                       "
				+ "                      //The group\n");
		PSFForm.add("  \t  \n");
		PSFForm.add("name=Number of Pins\n");
		PSFForm.add("description=The number of pins in an assembly\n");
		PSFForm.add("defaultValue=289\n");
		PSFForm.add("allowedValueType=Discrete\n");
		PSFForm.add("allowedValue=196\n");
		PSFForm.add("allowedValue=289\n");
		PSFForm.add("tag=numberOfPins\n");
		PSFForm.add("parent=Full Assembly Flag\n");
		PSFForm.add("group=Assembly\n");
		PSFForm.add("  \t  \n");
		PSFForm.add("name=Full Assembly Flag\n");
		PSFForm.add("description=True if a full assembly should be modeled, false if not\n");
		PSFForm.add("defaultValue=false\n");
		PSFForm.add("allowedValueType=Discrete\n");
		PSFForm.add("allowedValue=true\n");
		PSFForm.add("allowedValue=false\n");
		PSFForm.add("tag=fullAssemblyFlag\n");
		PSFForm.add("group=Assembly\n");

		// Convert the ArrayList to a string
		psfItemString = "";
		for (String i : PSFForm) {
			psfItemString += i;
		}

		return;
	}

	/**
	 * <p>
	 * This operation sets up test Items for the Item tester.
	 * </p>
	 * 
	 */
	@Before
	public void Before() {

		// Create the Item. We actually need to test a real Item, so implement
		// the abstract operations below for a simple test.
		item = new TestItem(null);

		return;
	}

	/**
	 * <p>
	 * This operation tests the Item class by creating an Item and checking that
	 * all of the attributes are either set to the proper default values or to
	 * the values specified in the constructor.
	 * </p>
	 */
	@Test
	public void createItem() {

		// Check the ID
		assertEquals(15, item.getId());
		// Check the type
		assertEquals(ItemType.Basic, item.getItemType());

		// check nullary constructor
		Item loadedItem = new Item();
		loadedItem = new Item();

		// Check the ID
		assertEquals(1, loadedItem.getId());
		// Check the type
		assertEquals(ItemType.Basic, loadedItem.getItemType());

		// Set the Item ID and make sure that it updates the Form id
		loadedItem.setId(5);
		assertEquals(loadedItem.getId(), loadedItem.getForm().getId());

		// Check default of item's builder. This should be the empty string!
		assertEquals("", loadedItem.getItemBuilderName());
		// Check null and empty string
		loadedItem.setItemBuilderName(null);
		loadedItem.setItemBuilderName("");

		// Although we check empty string, empty string should not allowed to be
		// set on it. Should be null for now.
		// But it can still be set again!
		assertEquals("", loadedItem.getItemBuilderName());

		// Set it once
		loadedItem.setItemBuilderName("Item's Builder");
		// Check that it is that name
		assertEquals("Item's Builder", loadedItem.getItemBuilderName());
		// Try to set again, notice how it does not change
		loadedItem.setItemBuilderName("Item's Builder2");
		// Check that it is the name from before
		assertEquals("Item's Builder", loadedItem.getItemBuilderName());

	}

	/**
	 * <p>
	 * This operation tests the Item to see if its name can be changed properly.
	 * It first sets the name and then checks the value returned by
	 * Item.getName().
	 * </p>
	 */
	@Test
	public void checkName() {

		// Set the name
		item.setName("Ozzy");
		// Check the name
		assertEquals("Ozzy", item.getName());

	}

	/**
	 * <p>
	 * This operation checks the ability of Item to return a Form that is well
	 * formed for a particular set of Entries. It creates an Item with an
	 * overloaded Item.fillEntryList() method and checks the Form for these
	 * Entries.
	 * </p>
	 */
	@Test
	public void checkForm() {

		// Local Declarations
		int i = 0;
		ArrayList<DataComponent> components = new ArrayList<DataComponent>();
		Form testForm = item.getForm();

		// Make sure the form is not null
		assertNotNull(testForm);

		// Grab the DataComponents
		for (i = 0; i < testForm.getNumberOfComponents(); i++) {
			components.add((DataComponent) testForm.getComponent(i + 1));
		}

		// Check the DataComponent names to make sure the Form is real
		assertEquals(components.get(0).getName(), "Jay");
		assertEquals(components.get(1).getName(), "David");
		assertEquals(components.get(2).getName(), "Alex");
		assertEquals(components.get(3).getName(), "Bobo the Drunken Clown");

		// FIXME - It would be preferable to have something like this:
		// assertTrue(testForm.contains("Jay"));
		// assertTrue(testForm.contains("David"));
		// assertTrue(testForm.contains("Alex"));
		// assertTrue(testForm.contains("Bobo the Drunken Clown"));
		// assertFalse(testForm.contains("Kurt Russell"));

	}

	/**
	 * <p>
	 * This operation tests the Item class by insuring that Forms can be
	 * properly submitted. It also checks that the status of the Item after the
	 * Form is submitted is appropriate.
	 * </p>
	 */
	@Test
	public void checkSubmission() {

		// Local Declarations
		Form testForm = null;
		DataComponent dc1 = null;
		Entry dc1Entry = null;

		// Get the Form and make sure it isn't null
		testForm = item.getForm();
		assertNotNull(testForm);

		// Grab the DataComponent
		dc1 = (DataComponent) testForm.getComponent(2);
		assertNotNull(dc1);

		// Get the Entry
		dc1Entry = dc1.retrieveEntry("David's Entry");
		assertNotNull(dc1Entry);

		// Set the value on the Entry
		dc1Entry.setValue("ORNL Employee");

		// Disable the Item
		item.disable(true);
		assertFalse(item.isEnabled());
		// Make sure that the Form can not be submitted
		assertEquals(FormStatus.Unacceptable, item.submitForm(testForm));
		assertEquals(FormStatus.Unacceptable, item.getStatus());
		// Re-enable the Item
		item.disable(false);
		assertTrue(item.isEnabled());

		// Submit the updated Form and check its status in two ways
		assertEquals(FormStatus.ReadyToProcess, item.submitForm(testForm));
		assertEquals(FormStatus.ReadyToProcess, item.getStatus());

		// Get the Form and check first DataComponent
		dc1 = (DataComponent) testForm.getComponent(1);
		assertEquals(((FakeDataComponent) dc1).getUpdatedValue(),
				"ORNL Employee");

		// FIXME - Need to make Action Form submission testable!

		return;

	}

	/**
	 * <p>
	 * This operation checks the Item to insure that its equals() operation
	 * works.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Create DataComponents to test
		Item item = new Item(null);
		Item equalItem = new Item(null);
		Item unEqualItem = new Item(null);
		Item transitiveItem = new Item(null);
		item.setItemBuilderName("Builder");

		// Set ICEObject data
		equalItem.setId(item.getId());
		transitiveItem.setId(item.getId());
		unEqualItem.setId(2);

		// Set builders
		equalItem.setItemBuilderName("Builder");
		unEqualItem.setItemBuilderName("Builder");
		transitiveItem.setItemBuilderName("Builder");

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
		assertTrue(item.hashCode() == equalItem.hashCode());

		// Assert that hashcode is consistent
		assertTrue(item.hashCode() == item.hashCode());

		// Assert that hashcodes from unequal objects are different
		assertTrue(item.hashCode() != unEqualItem.hashCode());

	}

	/**
	 * <p>
	 * This operation checks the Item to ensure that its copy() and clone()
	 * operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		/*
		 * The following sets of operations will be used to test the
		 * "clone and copy" portion of Item.
		 */
		// Local Declarations
		Item cloneItem = new Item(null), copyItem = new Item(null);

		// run clone operations
		cloneItem = (Item) item.clone();

		// check contents
		assertEquals(item.getAvailableActions(),
				cloneItem.getAvailableActions());
		assertEquals(item.getDescription(), cloneItem.getDescription());
		assertTrue(item.getForm().equals(cloneItem.getForm()));
		assertEquals(item.getId(), cloneItem.getId());
		assertEquals(item.getItemType(), cloneItem.getItemType());
		assertEquals(item.getName(), cloneItem.getName());
		assertEquals(item.getStatus(), cloneItem.getStatus());

		// run copy operation
		copyItem.copy(item);

		// check contents
		assertEquals(item.getAvailableActions(), copyItem.getAvailableActions());
		assertEquals(item.getDescription(), copyItem.getDescription());
		assertTrue(item.getForm().equals(copyItem.getForm()));
		assertEquals(item.getId(), copyItem.getId());
		assertEquals(item.getItemType(), copyItem.getItemType());
		assertEquals(item.getName(), copyItem.getName());
		assertEquals(item.getStatus(), copyItem.getStatus());

		// run copy operation by passing null
		copyItem.copy(null);

		// check contents - nothing has changed
		assertEquals(item.getAvailableActions(), copyItem.getAvailableActions());
		assertEquals(item.getDescription(), copyItem.getDescription());
		assertTrue(item.getForm().equals(copyItem.getForm()));
		assertEquals(item.getId(), copyItem.getId());
		assertEquals(item.getItemType(), copyItem.getItemType());
		assertEquals(item.getName(), copyItem.getName());
		assertEquals(item.getStatus(), copyItem.getStatus());

	}

	/**
	 * This operation checks the ability of the Item to persist itself to XML
	 * and to load itself from an XML input stream.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 * @throws NullPointerException
	 */
	@Test
	public void checkXMLPersistence() throws NullPointerException,
			JAXBException, IOException {
		/*
		 * The following sets of operations will be used to test the
		 * "read and write" portion of the Item. It will demonstrate the
		 * behavior of reading and writing from an
		 * "XML (inputStream and outputStream)" file. It will use an annotated
		 * Item to demonstrate basic behavior.
		 */

		// Local declarations
		Item loadedItem = new Item();
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(Item.class);

		// Set up item
		Item persistedItem = new Item();
		persistedItem.setDescription("I am an item description");
		persistedItem.setId(5);
		persistedItem.setName("I am a name!");
		persistedItem.getForm().setItemID(5);

		// persist to an output stream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(persistedItem, classList, outputStream);

		// Load an Item from the first
		loadedItem = (Item) xmlHandler.read(classList,
				new ByteArrayInputStream(outputStream.toByteArray()));
		// Make sure they match
		assertEquals(persistedItem, loadedItem);

		// Check the contents more closely to make sure that Item.
		assertEquals(persistedItem.getAvailableActions(),
				loadedItem.getAvailableActions());
		assertEquals(persistedItem.getDescription(),
				loadedItem.getDescription());
		assertEquals(persistedItem.getForm(), loadedItem.getForm());
		assertEquals(persistedItem.getId(), loadedItem.getId());
		assertEquals(persistedItem.getItemType(), loadedItem.getItemType());
		assertEquals(persistedItem.getName(), loadedItem.getName());
		assertEquals(persistedItem.getStatus(), loadedItem.getStatus());

	}

	/**
	 * <p>
	 * This operation checks the SerializedItem using a Painfully Simple Form
	 * (PSF) file. It checks the name and type of the Item and tries to create
	 * and check a SerializedItem initialized from the PSF.
	 * </p>
	 * 
	 */
	@Test
	public void checkLoadingFromPSF() {

		// Local Declarations
		ByteArrayInputStream stream = null;
		Form form = null;
		DataComponent dataComp1 = null, dataComp2 = null;
		Entry entry = null;

		// Create the InputStream
		stream = new ByteArrayInputStream(psfItemString.getBytes());

		// Load the Item
		Item testItem = new Item(null);
		try {
			testItem.loadFromPSF(stream);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		// Check the Item's name and description
		assertEquals("PSF Wiki Article Form", testItem.getName());
		assertEquals("A PSF Wiki Article Sample", testItem.getDescription());

		// Get the Form
		form = testItem.getForm();
		assertNotNull(form);
		// Check the Form in some random spots - FIXME: Use Form.equals() once
		// AJM implements it!
		assertEquals("PSF Wiki Article Form", form.getName());
		assertEquals("A PSF Wiki Article Sample", form.getDescription());
		assertEquals(1, form.getNumberOfComponents());
		dataComp1 = (DataComponent) form.getComponent(1);
		dataComp2 = (DataComponent) form.getComponent(2);
		assertNotNull(dataComp1);
		assertEquals("Assembly", dataComp1.getName());
		assertEquals(null, dataComp2);// There should only be one component!
		entry = dataComp1.retrieveEntry("Number of Pins");
		assertNotNull(entry);
		assertEquals("289", entry.getDefaultValue());
		assertEquals("numberOfPins", entry.getTag());

		// Try loading the Item from a null stream, which should result in an
		// exception
		testItem = new Item(null);
		try {
			testItem.loadFromPSF(null);
		} catch (IOException e) {
			// Don't do anything because it was supposed to fail!
		}

		return;

	}

	/**
	 * <p>
	 * This operation checks the SerializedItem to make sure that it properly
	 * reviews the Entries. It uses the psfItemString to create a SerializedItem
	 * with two Entries who have a third as a parent. It makes sure that
	 * updating the SerializedItem results in the child Entries being marked as
	 * ready.
	 * </p>
	 * 
	 */
	@Test
	public void checkEntryReviews() {

		Form form = null;
		DataComponent dataComp1 = null, dataComp2 = null;
		Entry entry = null;

		System.out.println("PSFItemString = " + psfItemString);

		// Load the SerializedItem from the PSF string
		Item testItem = new Item(null);
		try {
			testItem.loadFromPSF(new ByteArrayInputStream(psfItemString
					.getBytes()));
		} catch (IOException e) {
			// Fail if it can't load
			fail();
		}

		// Get the Form
		form = testItem.getForm();
		assertNotNull(form);
		assertEquals(1, form.getNumberOfComponents());

		// Get the parent
		dataComp1 = (DataComponent) form.getComponent(1);
		assertNotNull(dataComp1);
		assertEquals("Assembly", dataComp1.getName());
		entry = dataComp1.retrieveEntry("Full Assembly Flag");
		assertNotNull(entry);
		assertEquals("Full Assembly Flag", entry.getName());
		assertEquals(null, entry.getParent());

		// Change its value. By default it is false.
		entry.setValue("true");

		// Submit the Form back to the Item for review
		assertEquals(FormStatus.ReadyToProcess, testItem.submitForm(form));

		// Get the parent
		dataComp1 = (DataComponent) form.getComponent(1);
		assertNotNull(dataComp1);
		assertEquals("Assembly", dataComp1.getName());
		entry = dataComp1.retrieveEntry("Full Assembly Flag");
		assertNotNull(entry);
		assertEquals("true", entry.getValue());

		// Make sure that the children are now marked as ready
		assertEquals(3, dataComp1.retrieveReadyEntries().size());

		return;

	}

	/**
	 * This operation checks the Item to make sure that by default it offers two
	 * actions, one for writing the Form to XML and another for writing the
	 * Entries in the Form as a set of key-value pairs. It also makes sure that
	 * the Form is written to these two file types if the Item is so directed.
	 * It also makes sure that processing a disabled Item fails. Finally, it
	 * pulls the output file handle from the Item and makes sure that the
	 * default name of the file is set according to the default in the class
	 * documentation.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 * @throws NullPointerException
	 * @throws CoreException
	 */
	@Test
	public void checkProcessing() throws NullPointerException, JAXBException,
			IOException, CoreException {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String separator = System.getProperty("file.separator");
		String filename = null;
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(Item.class);
		classList.addAll(new ICEJAXBClassProvider().getClasses());
		
		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject("itemTesterWorkspace");
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ "itemTesterWorkspace")).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("itemTesterWorkspace");
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
		} catch (CoreException e) {
			// Catch for creating the project
			e.printStackTrace();
			fail();
		}

		// Create the Item
		item = new TestItem(project);
		// Configure the IOService for the Item
		IOService ioService = new IOService();
		XMLPersistenceProvider xmlpp = new XMLPersistenceProvider(project);
		xmlpp.addBuilder(new TestItemBuilder());
		xmlpp.registerClassProvider(new ICEJAXBClassProvider());
		xmlpp.start();
		ioService.addWriter(xmlpp);
		item.setIOService(ioService);

		// Get the available actions. They should be
		// "Export to ICE Native Format" and "Export to key-value pair output"
		// according to the API.
		Form form = item.getForm();
		ArrayList<String> actions = form.getActionList();
		assertNotNull(actions);
		assertEquals(2, actions.size());
		assertTrue(actions.contains("Export to ICE Native Format"));
		assertTrue(actions.contains("Export to key-value pair output"));

		// Try writing to an action not in the list and make sure it fails
		assertEquals(
				FormStatus.InfoError,
				item.process("Go to the gym because "
						+ "you've been working too long and you need to exercise "
						+ "your shoulder."));

		// Disable the Item
		item.disable(true);
		// Make sure that the Form can not be submitted
		assertEquals(FormStatus.Unacceptable,
				item.process("Export to ICE Native Format"));
		// Re-enable the Item
		item.disable(false);

		// Direct the Item to write the Form to XML
		assertEquals(FormStatus.Processed,
				item.process("Export to ICE Native Format"));
		// Pause the thread so it can have some time to write the file
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// Get the file that was written when the Item was processed, make sure
		// it exists, load it and compare to the original form.
		filename = (form.getName() + "_" + form.getId() + ".xml").replaceAll(
				"\\s+", "_");
		IFile formXMLFile = project.getFile(filename);
		assertTrue(formXMLFile.exists());
		Form loadedForm = (Form) xmlHandler.read(classList,
				formXMLFile.getContents());
		assertEquals(form, loadedForm);

		// Delete the file
		try {
			formXMLFile.delete(true, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

		// Direct the Item to write the Form to a key-value pair output
		assertEquals(FormStatus.Processed,
				item.process("Export to key-value pair output"));

		// Get the file, make sure it exists, load it and check it out
		filename = (form.getName() + "_" + form.getId() + ".dat").replaceAll(
				"\\s+", "_");
		IFile formDatFile = project.getFile(filename);
		assertTrue(formDatFile.exists());
		Properties formDatProps = new Properties();
		try {
			formDatProps.load(formDatFile.getContents());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail();
		}
		assertEquals("Awesome", formDatProps.get("jayjaybillings"));
		assertEquals("The boss", formDatProps.get("David'sEntry"));

		// Delete the file
		try {
			formXMLFile.delete(true, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

		// Setup the name of the output file. According to the documentation it
		// should be at
		// <itemName>_<itemId>_processOutput.txt.
		String outputFilename = item.getName().replaceAll("\\s+", "_") + "_"
				+ item.getId() + "_processOutput.txt";
		System.out
				.println("ItemTester message: Looking for (shortened) output file name \""
						+ outputFilename + "\"");
		// Get the output file handle
		File outputFile = item.getOutputFile();
		// Make sure it is not null
		assertNotNull(outputFile);
		// Make sure it contains our short name. It doesn't exactly matter where
		// the file is stored, as long as the name is properly set for now. That
		// means that the Item has created the file handle per the spec.
		String retOutputName = outputFile.getAbsolutePath();
		System.out.println("ItemTester message: Returned Output File Name = "
				+ retOutputName);
		assertTrue(outputFile.getAbsolutePath().contains(outputFilename));

		// Shut down the project resource
		project.close(null);
		project.delete(true, null);

		return;

	}

	/**
	 * <p>
	 * This operation checks the project setup of the Item to ensure that
	 * calling the constructor with an IProject and Item.setProject() setup the
	 * project reference such that Item.hasProject() returns true.
	 * </p>
	 * 
	 */
	@Test
	public void checkProjectSetup() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String separator = System.getProperty("file.separator");

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject("itemTesterWorkspace");
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = new File(
						System.getProperty("user.dir") + separator
								+ "itemTesterWorkspace").toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("itemTesterWorkspace");
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
		} catch (CoreException e) {
			// Catch for creating the project
			e.printStackTrace();
			fail();
		}

		// Setup a project using the constructor
		item = new TestItem(project);
		assertTrue(item.hasProject());

		// Make sure that the Item created a preferences directory. The
		// setPreferences() operation is a fake that forces the Item to create
		// the directory.
		((TestItem) item).setPreferences();
		IFolder preferencesFolder = project.getFolder("Test_Item");
		assertTrue(preferencesFolder.exists());

		// Setup a project using the setter
		item = new TestItem();
		// Check that the output file is not configured yet.
		assertNull(item.getOutputFile());
		item.setProject(project);
		assertTrue(item.hasProject());

		// Make sure that calling the setter with null does not re-set or
		// overwrite the project (Item.hasProject() should still return true)
		item.setProject(null);
		assertTrue(item.hasProject());

		// Make sure that calling the constructor with null does not set the
		// project
		item = new TestItem(null);
		assertEquals(item.hasProject(), false);

		// Make sure that calling the setter with null does not set the project
		item = new TestItem();
		item.setProject(null);
		assertEquals(item.hasProject(), false);

		// Shut down the project resource
		try {
			project.close(null);
			project.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * <p>
	 * This operation checks the ability of the Item to accept subscribers and
	 * notify them of changes by forcing an update using the TestItem. It also
	 * checks the ability of the Item to receive updates from ICE subsystems,
	 * remote ICE subsystems and external third-party processes.
	 * </p>
	 * 
	 */
	@Test
	public void checkUpdates() {

		// Local Declarations
		int id = 1, itemId = 2;
		String type = "FILE_UPDATED", content = "Starfleet Academy";

		// Create the Item
		TestItem fakeItem = new TestItem();
		fakeItem.addListener(this);

		// Force the notification
		fakeItem.notifyListeners();

		// Make sure the message was heard
		assertTrue(updated);

		// Create a message to test external updates (posts)
		Message msg = new Message();
		msg.setId(id);
		msg.setItemId(itemId);
		msg.setMessage(content);
		msg.setType(type);

		// Push the update and check the response
		assertTrue(fakeItem.update(msg));
		assertTrue(fakeItem.wasUpdated());

		return;
	}

	/**
	 * This method tests the {@link Item#getResource(String)} and
	 * {@link Item#getResource(Entry)} methods.
	 */
	@Test
	public void checkGettingResources() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String projectName = "itemTesterWorkspace";
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "itemData";

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/itemTesterWorkspace
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

			// Refresh the workspace
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// Catch exception for creating the project
			e.printStackTrace();
			fail();
		}

		// Local Declarations
		Item item = new Item(project);
		ICEResource iceResource = null;
		VizResource vizResource = null;
		String txtFilePath = project.getLocation().toOSString() + separator
				+ "txtResource.txt";
		String csvFilePath = project.getLocation().toOSString() + separator
				+ "csvResource.csv";

		// Try getting an ICEResource based on a String file path
		try {
			iceResource = item.getResource(txtFilePath);
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}

		// Verify all is well
		assertNotNull(iceResource);
		assertTrue(iceResource instanceof ICEResource);

		// Try getting a VizResource based on a String file path
		try {
			vizResource = (VizResource) item.getResource(csvFilePath);
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}

		// Verify all is well
		assertNotNull(vizResource);
		assertTrue(vizResource instanceof VizResource);

		// Now construct a file Entry based on a .e file
		Entry entry = new Entry();
		entry.setValue("mesh.e");
		vizResource = null;

		// Try getting a VizResource based on the Entry
		try {
			vizResource = (VizResource) item.getResource(entry);
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}

		// Check the VizResource
		assertNotNull(vizResource);
		assertTrue(vizResource instanceof VizResource);

		// Shut down the project resource
		try {
			project.close(null);
			project.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method tests the file copy/move methods on the Item.
	 */
	@Test
	public void checkFileCapabilities() {
		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String separator = System.getProperty("file.separator");

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject("itemData");
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = new File(
						System.getProperty("user.home") + separator
								+ "ICETests" + separator + "itemData").toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("itemData");
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
		} catch (CoreException e) {
			// Catch for creating the project
			e.printStackTrace();
			fail();
		}

		// Setup a project using the constructor
		TestItem testItem = new TestItem(project);
		assertTrue(testItem.hasProject());

		// Verify that Item.getFiles() works as expected
		ArrayList<String> files = testItem.getYAMLFiles(project.getLocation()
				.toOSString());
		assertNotNull(files);
		assertEquals(3, files.size());
		assertTrue(files.contains("bison.yaml"));
		assertTrue(files.contains("bison_short.yaml"));
		assertTrue(files.contains("bison_medium.yaml"));

		// Give it something that is not a directory and make sure we
		// get no files
		files = testItem.getYAMLFiles(project.getLocation().toOSString()
				+ separator + "bison.yaml");
		assertTrue(files.isEmpty());

		// Create a new temp directory
		IFolder tempDir = project.getFolder("tempDir");
		if (!tempDir.exists()) {
			try {
				tempDir.create(true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		// Now let's check that we can move files from one place to another
		// The test here is that the move operation makes a new file in the
		// target and deletes the file in the source
		testItem.moveTestFile(project.getLocation().toOSString(), tempDir
				.getLocation().toOSString(), "bison.yaml");
		assertFalse(project.getFile("bison.yaml").exists());
		assertTrue(tempDir.getFile("bison.yaml").exists());

		// Move it back to keep our workspace pristine for other item tests
		testItem.moveTestFile(tempDir.getLocation().toOSString(), project
				.getLocation().toOSString(), "bison.yaml");
		assertTrue(project.getFile("bison.yaml").exists());
		assertFalse(tempDir.getFile("bison.yaml").exists());

		// Check that we can copy, that is a new copied file is created
		// in the target and the source file is left intact
		testItem.copyTestFile(project.getLocation().toOSString(), tempDir
				.getLocation().toOSString(), "bison.yaml");
		assertTrue(project.getFile("bison.yaml").exists());
		assertTrue(tempDir.getFile("bison.yaml").exists());

		// Make sure we can move multiple files at time
		testItem.moveMultipleFiles(project.getLocation().toOSString(), tempDir
				.getLocation().toOSString(), ".yaml");
		assertTrue(tempDir.getFile("bison.yaml").exists());
		assertTrue(tempDir.getFile("bison_short.yaml").exists());
		assertTrue(tempDir.getFile("bison_medium.yaml").exists());

		// Let's check copying multiple files
		testItem.copyMultipleFiles(tempDir.getLocation().toOSString(), project
				.getLocation().toOSString(), ".yaml");
		assertTrue(project.getFile("bison.yaml").exists());
		assertTrue(project.getFile("bison_short.yaml").exists());
		assertTrue(project.getFile("bison_medium.yaml").exists());

		// Check we can delete directories
		testItem.deleteTestDirectory(tempDir.getLocation().toOSString());
		assertFalse(project.getFolder("tempDir").exists());

		// Test the directory copy
		IFolder limbo = project.getFolder("Directory");
		IFolder newLimbo = project.getFolder("newDirectory");
		if (!newLimbo.exists()) {
			try {
				newLimbo.create(true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		String srcPath = project.getLocation().toOSString() + separator
				+ project.getFolder("Directory").getName();
		String destPath = project.getLocation().toOSString() + separator
				+ project.getFolder("newDirectory").getName();
		testItem.	copyTestDirectory(srcPath, destPath);

		// Make sure all of the files were copied, then delete them all
		ArrayList<File> copiedFiles = new ArrayList<File>();
		copiedFiles.add(new File(destPath + separator + "DeepDirectory"
				+ separator + "DeeperThanDeep" + separator + "file"));
		copiedFiles.add(new File(destPath + separator + "DeepDirectory"
				+ separator + "DeeperThanDeep"));
		copiedFiles.add(new File(destPath + separator + "DeepDirectory"
				+ separator + "deepFile"));
		copiedFiles.add(new File(destPath + separator + "DeepDirectory"));
		copiedFiles.add(new File(destPath + separator + "shallowFile"));
		copiedFiles.add(new File(destPath));

		for (File f : copiedFiles) {
			assertTrue(f.exists());
			f.delete();
		}

		// Shut down the project resource
		try {
			project.close(null);
			project.delete(true, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	/**
	 * This method checks the Item's ability to get and use an IOService.
	 */
	@Test
	public void checkIOService() {
		TestItem testItem = new TestItem(null);
		IOService service = new IOService();

		// Create a fake IReader realization
		IReader fakeReader = new IReader() {

			@Override
			public Form read(IFile file) {
				return new Form();
			}

			@Override
			public ArrayList<Entry> findAll(IFile file, String regex) {
				ArrayList<Entry> fakeEntry = new ArrayList<Entry>();
				return fakeEntry;
			}

			@Override
			public String getReaderType() {
				return "fake";
			}

		};

		// Create a fake IWriter realization
		IWriter fakeWriter = new IWriter() {

			@Override
			public void write(Form formToWrite, IFile file) {
				return;
			}

			@Override
			public void replace(IFile file, String regex, String value) {
				return;
			}

			@Override
			public String getWriterType() {
				return "fake";
			}

		};

		// Test the case that the Item is given a bad IOService
		testItem.setIOService(null);
		assertNull(testItem.getTestReader());
		assertNull(testItem.getTestWriter());

		// Add the fakes
		service.addReader(fakeReader);
		service.addWriter(fakeWriter);

		// Set the service on the Item
		testItem.setIOService(service);

		// Test that if the Item specifies the IO type correctly
		// they should get valid reader and writer.
		assertNotNull(testItem.getTestReader());
		assertNotNull(testItem.getTestWriter());
		assertTrue(fakeReader == testItem.getTestReader());
		assertTrue(fakeWriter == testItem.getTestWriter());

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemListener#reloadProjectData()
	 */
	public void reloadProjectData() {

		updated = true;

	}
}
