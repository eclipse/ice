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
package org.eclipse.ice.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.core.internal.Core;
import org.eclipse.ice.core.internal.itemmanager.ItemManager;
import org.eclipse.ice.core.test.FakeIFile;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * CoreTester tests the functionality of Core. It checks its ability to get a
 * representation of the file system, the ability to set Locations, and the
 * ability to make a client-core connection. It uses an instance FakeItemManager
 * and calls the alternative constructor of Core to inject the dependency.
 * </p>
 *
 * @author Jay Jay Billings
 */
public class CoreTester {
	/**
	 * The core under test
	 */
	private Core iCECore;

	/**
	 * The ItemManager used by the core and queried by the test.
	 */
	private ItemManager fakeItemManager;

	/**
	 * The fake persistence provider used to make sure Items are persisted in
	 * separate projects.
	 */
	private FakePersistenceProvider fakePersistenceProvider;

	/**
	 * This operation configures the core.
	 * 
	 * @throws CoreException
	 */
	@Before
	public void BeforeClass() throws CoreException {

		// Local Declarations
		File testDir = new File(".");
		String location = testDir.toURI().toASCIIString();

		// Enable debug mode
		System.setProperty("DebugICE", "");

		// Setup the ItemManager, fake Persistence Provider and Core
		fakeItemManager = new ItemManager();
		fakePersistenceProvider = new FakePersistenceProvider();
		iCECore = new Core(fakeItemManager);
		iCECore.setPersistenceProvider(fakePersistenceProvider);
		iCECore.start(null);

		return;
	}

	/**
	 * This operation checks the Core by insuring that instances of ItemBuilder
	 * can be registered with the class.
	 */
	@Test
	public void checkItemRegistration() {

		// Local Declarations
		FakeGeometryBuilder fakeGeometryBuilder = new FakeGeometryBuilder();
		FakeModuleBuilder fakeModuleBuilder = new FakeModuleBuilder();
		FakeCompositeItemBuilder fakeCompositeBuilder = new FakeCompositeItemBuilder();
		String testItemName = null;
		boolean found;

		// Register the ItemBuilders
		iCECore.registerItem(fakeModuleBuilder);
		iCECore.registerItem(fakeGeometryBuilder);

		// Check the ItemManager and make sure the builders were forwarded

		// Make sure the list of available builders contains the 2 fake items.
		List<String> availableBuilders = fakeItemManager.getAvailableBuilders();
		assertNotNull(availableBuilders);
		assertTrue(availableBuilders.contains(fakeModuleBuilder.getItemName()));
		assertTrue(availableBuilders.contains(fakeGeometryBuilder.getItemName()));

		// NOTE: The below code has to loop over the list of available builders
		// because the underlying list is NOT ordered.

		// Make sure the available builders includes the fake module builder.
		found = false;
		testItemName = fakeModuleBuilder.getItemName();
		for (int i = 0; !found && i < availableBuilders.size(); i++) {
			found = testItemName.equals(availableBuilders.get(i));
		}
		assertTrue("ItemManagerTester: " + "FakeModuleBuilder with name " + testItemName
				+ " not found in available builders!", found);

		// Make sure the available builders includes the fake geometry builder.
		found = false;
		testItemName = fakeGeometryBuilder.getItemName();
		for (int i = 0; !found && i < availableBuilders.size(); i++) {
			found = testItemName.equals(availableBuilders.get(i));
		}
		assertTrue("ItemManagerTester: " + "FakeGeometryBuilder with name " + testItemName
				+ " not found in available builders!", found);

		// Register the CompositeItemBuilder
		iCECore.registerCompositeItem(fakeCompositeBuilder);

		// Check the ItemManager and make sure the composite item builder was
		// forwarded
		assertTrue(fakeCompositeBuilder.itemBuildersRegistered());

		// FIXME! Need tests for unregistering!

		return;
	}

	/**
	 * This operation checks the Core by creating a new Item and making sure
	 * that it is available from the ItemManager. It also checks this
	 * functionality for ICompositeItemBuilders.
	 * <p>
	 * This operation also checks that the types of Items returned by
	 * getAvailableItemTypes are consistent with those registered with the
	 * ItemManager that is used in the test.
	 * </p>
	 *
	 */
	@Test
	public void checkItemCreation() {

		// Local Declarations
		FakeGeometryBuilder fakeGeometryBuilder = new FakeGeometryBuilder();
		FakeModuleBuilder fakeModuleBuilder = new FakeModuleBuilder();
		String name = fakeModuleBuilder.getItemName();
		ArrayList<String> types = new ArrayList<String>();

		// Register the ItemBuilders
		iCECore.registerItem(fakeModuleBuilder);
		iCECore.registerItem(fakeGeometryBuilder);

		// Clean up any old items hanging around
		ArrayList<Identifiable> items = iCECore.getItemList();
		for (Identifiable iceObj : items) {
			iCECore.deleteItem(String.valueOf(iceObj.getId()));
		}

		// Check the available Item types after registration
		types = iCECore.getAvailableItemTypes().getList();
		assertTrue(types.contains(fakeGeometryBuilder.getItemName()));
		assertTrue(types.contains(fakeModuleBuilder.getItemName()));

		// Create a new Item
		int itemId = Integer.parseInt(iCECore.createItem(name));

		// Check the Item id
		assertNotNull(itemId);
		assertTrue(itemId > 0);

		// Check the ItemManager to see if the Item exists
		Form testForm = fakeItemManager.retrieveItem(itemId);
		assertNotNull(testForm);

		// Create a few more and store the ids
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ids.add(itemId);
		ids.add(Integer.parseInt(iCECore.createItem(name)));
		ids.add(Integer.parseInt(iCECore.createItem(name)));
		ids.add(Integer.parseInt(iCECore.createItem(name)));
		ids.add(Integer.parseInt(iCECore.createItem(name)));

		// Get the list of ICEObjects that represents the Items that have been
		// created and check it
		items = iCECore.getItemList();
		for (Identifiable iceObj : items) {
			assertTrue(ids.contains(iceObj.getId()));
		}

		// Delete all of the Items
		for (Identifiable iceObj : items) {
			iCECore.deleteItem(String.valueOf(iceObj.getId()));
		}
		// Make sure that there are no items
		assertEquals(0, iCECore.getItemList().size());
		System.out.println("Num ITEMS after delete = " + iCECore.getItemList().size());

		return;
	}

	/**
	 * Check that the Core can correctly rename an Item.
	 */
	@Test
	public void checkItemRenaming() {
		// Local Declarations
		FakeGeometryBuilder fakeGeometryBuilder = new FakeGeometryBuilder();
		FakeModuleBuilder fakeModuleBuilder = new FakeModuleBuilder();
		String name = fakeModuleBuilder.getItemName();
		ArrayList<String> types = new ArrayList<String>();

		// Register the ItemBuilders
		iCECore.registerItem(fakeModuleBuilder);
		iCECore.registerItem(fakeGeometryBuilder);

		// Clean up any old items hanging around
		ArrayList<Identifiable> items = iCECore.getItemList();
		for (Identifiable iceObj : items) {
			iCECore.deleteItem(String.valueOf(iceObj.getId()));
		}

		// Check the available Item types after registration
		types = iCECore.getAvailableItemTypes().getList();
		assertTrue(types.contains(fakeGeometryBuilder.getItemName()));
		assertTrue(types.contains(fakeModuleBuilder.getItemName()));

		// Create a new Item
		int itemId = Integer.parseInt(iCECore.createItem(name));

		iCECore.renameItem(itemId, "NewName");
		assertTrue("New Name".equals(iCECore.getItem(itemId).getName()));
		
	}
	
	/**
	 * This operation checks the ability of the core to load a single Item.
	 */
	@Test
	public void checkSingleItemLoad() {
		// Reset the fake provider
		fakePersistenceProvider.reset();
		// Create a fake file and direct the manager to load it
		IFile fakeFile = new FakeIFile();
		Form form = iCECore.loadItem(fakeFile);
		// The form should not be null since the FakePersistenceProvider creates
		// an Item.
		assertNotNull(form);
		// Make sure the name matches the one in the FakePersistenceProvider and
		// that the load operation was called.
		assertEquals("The Doctor", form.getName());
		assertTrue(fakePersistenceProvider.allLoaded());
		// Reset the fake provider one more time, just to be polite.
		fakePersistenceProvider.reset();

		return;
	}

	/**
	 * This operation checks the Core by ensuring that Items can be updated.
	 */
	@Test
	public void checkItemUpdates() {

		// Local Declarations
		ArrayList<String> types = null;
		int testItemId = 0;
		Form testForm = null;
		FakeGeometryBuilder fakeGeometryBuilder = new FakeGeometryBuilder();
		FormStatus status = FormStatus.InfoError;

		// Register the ItemBuilders
		this.iCECore.registerItem(fakeGeometryBuilder);

		// Get the types and make sure some are available
		types = iCECore.getAvailableItemTypes().getList();
		assertNotNull(types);

		// Create a FakeGeometry, which will return an instance of FakeItem.
		// This class has a special implementation of reviewEntries that
		// makes testing easier. Adding two data components will make it pass
		// its review, but adding any more will cause it to fail.
		testItemId = Integer.parseInt(iCECore.createItem(fakeGeometryBuilder.getItemName()));
		assertTrue(testItemId > 0);

		// Get the Form and make sure it is not null
		testForm = iCECore.getItem(testItemId);
		assertNotNull(testForm);

		// Add two components to the Form
		testForm.addComponent(new DataComponent());
		testForm.addComponent(new DataComponent());

		// Update the Item and catch the status
		status = iCECore.updateItem(testForm, 1);

		// Check the status. It should be fine since we only added two
		// components.
		assertEquals(FormStatus.ReadyToProcess, status);

		// Grab the Form from the ItemManager and check it ourselves. This makes
		// sure that it went all the way through.
		testForm = fakeItemManager.retrieveItem(testItemId);
		assertEquals(2, testForm.getNumberOfComponents());

		// Add a third component, resubmit it and make sure it fails.
		testForm.addComponent(new DataComponent());
		status = iCECore.updateItem(testForm, 1);
		assertEquals(FormStatus.InfoError, status);

		// Grab the Form again and check it for ourselves. Again, this makes
		// sure it went all the way through.
		testForm = fakeItemManager.retrieveItem(testItemId);
		assertEquals(3, testForm.getNumberOfComponents());

		return;
	}

	/**
	 * This operation checks the Core to make sure that it can process Items. It
	 * creates a FakeItem through the API and then directs the Core to process
	 * it. It also checks the final status of the Item using the getItemStatus()
	 * operation. Finally, it pulls the output file handle from the Item by id
	 * and confirms that the default name of the file is set according to the
	 * default in the class documentation.
	 */
	@Test
	public void checkItemProcessing() {

		// Local Declarations
		ArrayList<String> types = null;
		int testItemId = 0;
		FakeGeometryBuilder fakeGeometryBuilder = new FakeGeometryBuilder();
		FormStatus status = FormStatus.InfoError;
		IProject project = null;
		String separator = System.getProperty("file.separator");

		// Setup the project space so that the output file can be checked.
		project = getProject("itemManagerTesterWorkspace");

		// Register the ItemBuilders
		iCECore.registerItem(fakeGeometryBuilder);

		// Get the types and make sure some are available
		types = iCECore.getAvailableItemTypes().getList();
		assertNotNull(types);

		// Create a FakeGeometry, which will return an instance of FakeItem.
		// This class has a special implementation of reviewEntries that
		// makes testing easier. Adding two data components will make it pass
		// its review, but adding any more will cause it to fail.
		testItemId = Integer.parseInt(iCECore.createItem(fakeGeometryBuilder.getItemName()));
		assertTrue(testItemId > 0);

		// Direct the Core to process the Item
		status = iCECore.processItem(testItemId, "blend", 1);

		// Check the status
		assertEquals(FormStatus.Processed, status);
		assertEquals(FormStatus.Processed, iCECore.getItemStatus(testItemId));

		// Setup the project space for the output file test
		FakeItem fakeItem = fakeGeometryBuilder.getLastFakeItem();
		fakeItem.setProject(project);

		// Setup the name of the output file. According to the documentation it
		// should be at <itemName>_<itemId>_processOutput.txt.
		String outputFilename = fakeItem.getName().replaceAll("\\s+", "_") + "_" + fakeItem.getId()
				+ "_processOutput.txt";
		System.out.println("CoreTester message: Looking for (shortened) output file name \"" + outputFilename + "\"");
		// Get the output file handle
		File outputFile = iCECore.getItemOutputFile(testItemId);
		// Make sure it is not null
		assertNotNull(outputFile);
		// Make sure it contains our short name. It doesn't exactly matter where
		// the file is stored, as long as the name is properly set for now. That
		// means that the Item has created the file handle per the spec.
		String retOutputName = outputFile.getAbsolutePath();
		System.out.println("CoreTester message: Returned Output File Name = " + retOutputName);
		assertTrue(outputFile.getAbsolutePath().contains(outputFilename));

		// Check canceling by putting the fake item into a persistent
		// "Processing" state and shutting it down.
		fakeItemManager.processItem(fakeItem.getId(), "setProcessing");
		status = fakeItemManager.cancelItemProcess(fakeItem.getId(), "setProcessing");
		assertTrue(fakeItem.wasCancelled());
		assertEquals(FormStatus.ReadyToProcess, status);

		return;
	}

	/**
	 * This operation checks the Core to make sure that it can import files into
	 * the project space.
	 */
	@Test
	public void checkFileImports() {

		// Local Declarations
		ArrayList<String> types = null;
		int testItemId = 0;
		Form testForm = null;
		FakeGeometryBuilder fakeGeometryBuilder = new FakeGeometryBuilder();
		IProject project = null;

		project = getProject("default");

		// Clean out any old test files
		try {
			project.getFile("testFile.test").delete(true, null);
		} catch (CoreException e1) {
			// Complain
			e1.printStackTrace();
			fail();
		}

		// Register the ItemBuilders
		iCECore.registerItem(fakeGeometryBuilder);

		// Get the types and make sure some are available
		types = iCECore.getAvailableItemTypes().getList();
		assertNotNull(types);

		// Create an Item
		testItemId = Integer.parseInt(iCECore.createItem(fakeGeometryBuilder.getItemName()));

		// Create a test file
		File testFile = new File("testFile.test");
		File testFile2 = new File("testFile2.test");
		try {
			testFile.createNewFile();
			testFile2.createNewFile();
		} catch (IOException e) {
			// Complain
			e.printStackTrace();
			fail();
		}

		// Import the file
		iCECore.importFile(testFile.toURI());

		// Check the import in the project
		IFile testProjectFile = project.getFile("testFile.test");
		assertTrue(testProjectFile.exists());

		// Check that we can import a file to a given IProject
		IProject testProject = getProject("testProject");
		iCECore.importFile(testProjectFile.getLocationURI(), testProject);

		IFile anotherTestProjectFile = testProject.getFile("testFile.test");
		assertTrue(anotherTestProjectFile.exists());

		// Check we can use the import method with project string name
		iCECore.importFile(testFile2.toURI(), "testProject");
		IFile test2 = testProject.getFile("testFile2.test");
		assertTrue(test2.exists());

		// Check the import in the fake item
		FakeItem fakeItem = fakeGeometryBuilder.getLastFakeItem();
		assertTrue(fakeItem.wasRefreshed());

		// Delete the file from the project
		try {
			testProjectFile.delete(true, null);
			anotherTestProjectFile.delete(true, null);
			test2.delete(true, null);
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
			fail();
		}

		// Import the file as an input file
		int itemId = Integer.valueOf(iCECore.importFileAsItem(testFile.toURI(), fakeGeometryBuilder.getItemName()));

		// Check the id
		assertTrue(itemId > 0);
		// Check the import in the fake item
		fakeItem = fakeGeometryBuilder.getLastFakeItem();
		assertTrue(fakeItem.wasLoaded());

		// Check the import in the project
		testProjectFile = project.getFile("testFile.test");
		assertTrue(testProjectFile.exists());

		// Check that we can import as Item with an IFile
		iCECore.importFile(testFile.toURI());
		itemId = Integer
				.valueOf(iCECore.importFileAsItem(project.getFile("testFile.test"), fakeGeometryBuilder.getItemName()));
		assertTrue(itemId > 0);

		// Check we can import an Item to a given IProject
		itemId = Integer
				.valueOf(iCECore.importFileAsItem(testFile.toURI(), fakeGeometryBuilder.getItemName(), testProject));
		assertTrue(itemId > 0 && testProject.getFile("testFile.test").exists());

		// Delete the file from the project
		try {
			testProject.getFile("testFile.test").delete(true, null);
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
			fail();
		}

		// Now make sure we can import as Item to a Project with given name
		itemId = Integer
				.valueOf(iCECore.importFileAsItem(testFile.toURI(), fakeGeometryBuilder.getItemName(), "testProject"));
		assertTrue(itemId > 0 && testProject.getFile("testFile.test").exists());

		// Delete the file from the project and the local directory
		testFile.delete();
		testFile2.delete();
		try {
			testProjectFile.delete(true, null);
			testProject.delete(true, null);
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
			fail();
		}

		return;
	}

	/**
	 * This operation is responsible for testing the ability of the Core to post
	 * updates from the ICEUpdater.
	 */
	@Test
	public void checkRealtimeUpdates() {

		// Local Declarations
		FakeGeometryBuilder fakeGeometryBuilder = new FakeGeometryBuilder();

		// Register the ItemBuilders
		iCECore.registerItem(fakeGeometryBuilder);

		// Create an Item
		int id = Integer.parseInt(iCECore.createItem(fakeGeometryBuilder.getItemName()));

		// A message from some of the Updater tests
		String msg = "post={\"item_id\":\"" + id + "\", "
				+ "\"client_key\":\"1234567890ABCDEFGHIJ1234567890ABCDEFGHIJ\", "
				+ "\"posts\":[{\"type\":\"UPDATER_STARTED\",\"message\":\"\"}," + "{\"type\":\"FILE_MODIFIED\","
				+ "\"message\":\"/tmp/file\"}]}";

		// Make sure posting a valid message works
		assertEquals("OK", iCECore.postUpdateMessage(msg));
		// Get the FakeItem and make sure it was updated
		assertTrue(fakeGeometryBuilder.getLastFakeItem().wasUpdated());

		// Make sure posting a null message fails
		assertNull(iCECore.postUpdateMessage(null));

		// Make sure posting a message without json content fails
		assertNull(iCECore.postUpdateMessage("not&realContent"));

		return;
	}

	/**
	 * This operation checks the Core's ability to make a client connection with
	 * a given username, password, and Client ID.
	 */
	@Test
	public void checkConnection() {

		// Make sure valid connection is made
		int uID = Integer.parseInt(iCECore.connect());
		assertTrue(uID > 0);

		return;
	}

	/**
	 * This operation checks that the second create() operation functions
	 * properly.
	 *
	 * @throws CoreException
	 *             Thrown if the project file cannot be deleted.
	 */
	@Test
	public void checkCreateInMultipleProjects() throws CoreException {
		// Create a builder for the test
		FakeGeometryBuilder fakeGeometryBuilder = new FakeGeometryBuilder();
		IProject project = getProject("default2");

		// Register the ItemBuilder
		iCECore.registerItem(fakeGeometryBuilder);

		// Reset the persistence provider
		fakePersistenceProvider.reset();

		// Create the item
		String name = fakeGeometryBuilder.getItemName();

		String idString = iCECore.createItem(name, project);
		int id = Integer.parseInt(idString);
		assertTrue(id > 0);
		assertTrue(fakePersistenceProvider.itemPersisted());

		// Check the project space for the process output file created by the
		// new Item. The actual XML file of the Item will not be stored here
		// because I am using a fake persistence provider. (It is a unit test,
		// after all.) However, if the Item is correctly receiving the new
		// project, the persistence provider should be called and the Item
		// should be storing its process output file there.
		String fileName = "Inigo_Montoya_" + id + "_processOutput.txt";
		// Refresh the project space, just in case.
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
		IFile file = project.getFile(fileName);

		// Clean up the file
		file.delete(true, null);

		// Delete the Item from the Core to give the next test a clean slate.
		iCECore.deleteItem(String.valueOf(id));

		// Reset the persistence provider for the next test
		fakePersistenceProvider.reset();

		return;
	}

	/**
	 * This is a utility operation that returns the project of the given name
	 * for the test. It removes any old test files from the project . It
	 * attempts to create the project if it does not exist.
	 *
	 * @param name
	 *            the name of the project to retrieve from the Workspace
	 * @return the project
	 */
	public IProject getProject(String name) {

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String separator = System.getProperty("file.separator");

		// Setup the project space so that the output file can be checked.
		System.out.println("CoreTester Workspace Root = " + workspaceRoot.getLocation());
		System.out.println("Constructing project " + name);
		try {
			// Get the project handle
			project = workspaceRoot.getProject(name);
			// If the project does not exist, create it
			if (!project.exists()) {
				defaultProjectLocation = (new File(System.getProperty("user.dir") + separator + name)).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace().newProjectDescription(name);
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				System.out.println("CoreTester Message: " + "Project location is " + desc.getLocationURI());
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

		// Clean out any old test files
		try {
			for (IResource resource : project.members()) {
				resource.delete(true, null);
			}
		} catch (CoreException e1) {
			// Complain
			e1.printStackTrace();
			fail();
		}

		return project;
	}
}