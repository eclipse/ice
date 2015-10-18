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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.ice.core.internal.itemmanager.ItemManager;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.messaging.Message;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * The ItemManagerTester class is responsible for testing the ItemManager class.
 * It does not explicitly test the ability of the ItemManager to persist and
 * retrieve Items from an IPersistenceProvider, but, instead, it weaves tests of
 * the persistence among the other tests as needed to accurately cover the
 * ability of the ItemManager to load, persist and update Items from its
 * provider. This is true but with an exception for mass loading and persisting
 * that is performed before and after all work in the Core is performed. Those
 * tests are performed in the checkMassItemManagement() operation.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ItemManagerTester {
	/**
	 * 
	 */
	private ItemManager itemManager;

	/**
	 * 
	 */
	private FakeGeometryBuilder fakeGeometryBuilder;

	/**
	 * 
	 */
	private FakeItem fakeItem;

	/**
	 * 
	 */
	private FakeModuleBuilder fakeModuleBuilder;

	/**
	 * 
	 */
	private FakeCompositeItemBuilder fakeCompositeItemBuilder;

	/**
	 * 
	 */
	private FakePersistenceProvider fakePersistenceProvider;

	/**
	 * <p>
	 * This method is sets up the test ItemManagerTester.
	 * </p>
	 * 
	 */
	@Before
	public void setup() {

		// Setup the ItemManager to test
		itemManager = new ItemManager();

		// Setup the Fakes
		fakeItem = new FakeItem(null);
		fakeGeometryBuilder = new FakeGeometryBuilder();
		fakeModuleBuilder = new FakeModuleBuilder();
		fakePersistenceProvider = new FakePersistenceProvider();

		// Register the FakeGeometryBuilder and the FakeModuleBuilder
		itemManager.registerBuilder(fakeGeometryBuilder);
		itemManager.registerBuilder(fakeModuleBuilder);

		// Register the fake persistence provider
		itemManager.setPersistenceProvider(fakePersistenceProvider);

	}

	/**
	 * <p>
	 * This operation checks the ItemManager by insuring that instances of
	 * ItemBuilder can be registered with the class. It registers both the
	 * FakeModuleBuilder and the FakeGeometryBuilder to make sure that the
	 * ItemManager can handle multiple ItemBuilders. It also registers a
	 * FakeCompositeItemBuilder and makes sure that the ItemManager sets the
	 * list of ItemBuilders during registration.
	 * </p>
	 * 
	 */
	@Test
	public void checkItemRegistration() {

		// Local Declarations
		String testItemName;
		boolean found;

		// Make sure the list of builders has exactly 2 items.
		List<String> availableBuilders = itemManager.getAvailableBuilders();
		assertNotNull(availableBuilders);
		assertEquals(2, availableBuilders.size());

		// NOTE: The below code has to loop over the list of available builders
		// because the underlying list is NOT ordered.

		// Make sure the available builders includes the fake module builder.
		found = false;
		testItemName = fakeModuleBuilder.getItemName();
		for (int i = 0; !found && i < availableBuilders.size(); i++) {
			found = testItemName.equals(availableBuilders.get(i));
		}
		assertTrue(
				"ItemManagerTester: " + "FakeModuleBuilder with name "
						+ testItemName + " not found in available builders!",
				found);

		// Make sure the available builders includes the fake geometry builder.
		found = false;
		testItemName = fakeGeometryBuilder.getItemName();
		for (int i = 0; !found && i < availableBuilders.size(); i++) {
			found = testItemName.equals(availableBuilders.get(i));
		}
		assertTrue(
				"ItemManagerTester: " + "FakeGeometryBuilder with name "
						+ testItemName + " not found in available builders!",
				found);

		// Get the builders by type, Geometry first
		assertNotNull(itemManager.getAvailableBuilders(ItemType.Geometry));
		assertEquals(1,
				itemManager.getAvailableBuilders(ItemType.Geometry).size());
		testItemName = itemManager.getAvailableBuilders(ItemType.Geometry)
				.get(0);
		assertEquals(testItemName, fakeGeometryBuilder.getItemName());
		// Now Modules
		availableBuilders = itemManager.getAvailableBuilders(ItemType.Module);
		assertNotNull(availableBuilders);
		assertEquals(1, availableBuilders.size());
		testItemName = availableBuilders.get(0);
		assertEquals(testItemName, fakeModuleBuilder.getItemName());

		// Unregister the FakeItemBuilder
		itemManager.unregisterBuilder(fakeGeometryBuilder);
		itemManager.unregisterBuilder(fakeModuleBuilder);

		// Make sure that the list of available Items is null now.
		assertNull(itemManager.getAvailableBuilders());

		// Register the FakeCompositeItemBuilder
		FakeCompositeItemBuilder compositeBuilder = new FakeCompositeItemBuilder();
		itemManager.registerCompositeBuilder(compositeBuilder);
		assertTrue(compositeBuilder.itemBuildersRegistered());

		// Reset the composite builder and add another Item to make sure the
		// composite is updated when the set of builders changes
		compositeBuilder.reset();
		itemManager.registerBuilder(new FakeModuleBuilder());
		assertTrue(compositeBuilder.itemBuildersRegistered());

		// Unregister the FakeCompositeItemBuilder and check it. There should
		// only be the one Item that we added above, not two.
		itemManager.unregisterBuilder(compositeBuilder);
		assertEquals(1, itemManager.getAvailableBuilders().size());

		return;
	}

	/**
	 * <p>
	 * This operation checks the ItemManager by creating an Item and then
	 * retrieving it.
	 * </p>
	 * 
	 */
	@Test
	public void checkItemCreation() {

		// Local Declarations
		int itemId = 99999;
		Form testForm = null;

		// Reset the fake persistence provider
		fakePersistenceProvider.reset();

		// Print the test directory for reference.
		System.out.println(
				"Current test directory = " + System.getProperty("user.dir"));

		// Create an Item
		itemId = itemManager.createItem(fakeGeometryBuilder.getItemName(),
				null);

		// Make sure the id changed - it shouldn't be 99999 anymore!
		assertTrue(itemId < 99999 && itemId > 0);

		// Make sure the persistence provider was called to persist the Item.
		assertTrue(fakePersistenceProvider.itemPersisted());

		// Try to retrieve it - which should return a Form
		testForm = itemManager.retrieveItem(itemId);

		// Make sure that the Form is not null
		assertNotNull(testForm);

		// Test input file loading support
		itemId = 0; // Zero the id to make sure it changed
		itemId = itemManager.createItem("test.input",
				fakeGeometryBuilder.getItemName(), null);

		// Make sure the id changed
		assertTrue(itemId > 0);
		FakeItem testItem = fakeGeometryBuilder.getLastFakeItem();
		assertTrue(testItem.wasLoaded());

		return;
	}

	/**
	 * This operation checks the ability of the ItemManager to load a single
	 * Item.
	 */
	@Test
	public void checkSingleItemLoad() {
		// Reset the fake provider
		fakePersistenceProvider.reset();
		// Create a fake file and direct the manager to load it
		IFile fakeFile = new FakeIFile();
		Form form = itemManager.loadItem(fakeFile);
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
	 * <p>
	 * This operation tests the ItemManager by making sure that it can delete
	 * Items. It creates a set of Items, deletes one or more of the Items and
	 * then tries to retrieve the deleted Item or Items.
	 * </p>
	 * 
	 */
	@Test
	public void checkItemDeletion() {

		// Local Declarations
		int itemIds[] = { 99998, 99999 };
		Form forms[] = { new Form(), new Form() };

		// Create a couple of Items
		itemIds[0] = itemManager.createItem(fakeGeometryBuilder.getItemName(),
				null);
		itemIds[1] = itemManager.createItem(fakeModuleBuilder.getItemName(),
				null);
		assertTrue(itemIds[0] != 99998 && itemIds[0] > 0);
		assertTrue(itemIds[0] != 99999 && itemIds[1] > 0);

		// Retrieve the Forms to make sure that the Items were created
		forms[0] = itemManager.retrieveItem(itemIds[0]);
		forms[1] = itemManager.retrieveItem(itemIds[1]);
		assertNotNull(forms[0]);
		assertNotNull(forms[1]);

		// Delete the Items and make sure the persistence provider was called.
		fakePersistenceProvider.reset();
		assertTrue(itemManager.deleteItem(itemIds[0]));
		assertTrue(fakePersistenceProvider.itemDeleted());
		fakePersistenceProvider.reset();
		assertTrue(itemManager.deleteItem(itemIds[1]));
		assertTrue(fakePersistenceProvider.itemDeleted());

		// Try to retrieve them again and make sure that they are null
		forms[0] = itemManager.retrieveItem(itemIds[0]);
		forms[1] = itemManager.retrieveItem(itemIds[1]);
		assertTrue(forms[0] == null);
		assertNotNull(forms[1] == null);

		return;

	}

	/**
	 * <p>
	 * This operation tests the Item's ability to return an appropriate list of
	 * Items. It creates several Items and requests the list of Items to do
	 * this.
	 * </p>
	 * 
	 */
	@Test
	public void checkItemList() {

		// Local Declarations
		int itemIds[] = { 99998, 99999 };
		ArrayList<Identifiable> allItems = new ArrayList<Identifiable>();

		// Add a couple extra Items
		itemIds[0] = this.itemManager
				.createItem(this.fakeGeometryBuilder.getItemName(), null);
		itemIds[1] = this.itemManager
				.createItem(this.fakeModuleBuilder.getItemName(), null);

		// Grab the list of Items
		allItems = this.itemManager.retrieveItemList();
		assertNotNull(allItems);
		assertTrue(allItems.size() > 0);

		// Do a sanity check on the ICEObjects
		assertEquals(2, allItems.size());
		assertEquals(itemIds[0], allItems.get(0).getId());
		assertEquals(itemIds[0], allItems.get(0).getId());

		return;

	}

	/**
	 * <p>
	 * This operation checks the ItemManager by ensuring that Items can be
	 * updated. This operation also insures that Items can be updated from other
	 * ICE subsystems, remote ICE subsystems and third-party libraries via the
	 * update() operation.
	 * </p>
	 * 
	 */
	@Test
	public void checkItemUpdates() {

		// Local Declarations
		int itemId = -1;
		Form testForm = null;
		FormStatus status = null;
		int id = 1;
		String type = "FILE_UPDATED", content = "Starfleet Academy";

		// Create a FakeGeometry. This will return an instance of FakeItem.
		itemId = itemManager.createItem(fakeGeometryBuilder.getItemName(),
				null);

		// Get the Form and add two new DataComponents.
		testForm = itemManager.retrieveItem(itemId);
		testForm.addComponent(new DataComponent());
		testForm.addComponent(new DataComponent());

		// Reset the fake persistence provider
		fakePersistenceProvider.reset();

		// Post the update.
		status = itemManager.updateItem(testForm);

		// Make sure the persistence provider was called
		assertTrue(fakePersistenceProvider.itemUpdated());

		// Send a signal for the Item to update/reload its data
		itemManager.reloadItemData();
		assertTrue(fakeGeometryBuilder.getLastFakeItem().wasRefreshed());

		// Check the update status. It should not be null and the ItemManager
		// should have reported that it is Ready To Process.
		assertTrue(status != null);
		assertEquals(FormStatus.ReadyToProcess, status);

		// Add another data component and reprocess the update. The update
		// should fail because FakeItem sets a limit on the number of
		// DataComponents.
		testForm = itemManager.retrieveItem(itemId);
		testForm.addComponent(new DataComponent());
		status = itemManager.updateItem(testForm);
		assertTrue(status != null);
		assertEquals(FormStatus.InfoError, status);

		// Reset the last Item and force it to post an update
		FakeItem lastItem = fakeGeometryBuilder.getLastFakeItem();
		lastItem.reset();
		lastItem.notifyListeners();

		// Make sure that it was updated
		assertTrue(lastItem.wasRefreshed());

		// Create a message
		Message msg = new Message();
		msg.setId(id);
		msg.setItemId(lastItem.getId());
		msg.setMessage(content);
		msg.setType(type);

		// Push the update
		assertTrue(itemManager.postUpdateMessage(msg));
		assertTrue(lastItem.wasUpdated());

		return;

	}

	/**
	 * This operation checks the ItemManager to make sure that it can process
	 * Items. It creates a FakeItem with the ItemManager and then directs the
	 * ItemManager to process it. It checks both the returned status and the
	 * FakeItem class to make sure that the call was completed properly. It also
	 * checks the status using getItemStatus(). Finally, it pulls the output
	 * file handle from the Item by id and confirms that the default name of the
	 * file is set according to the default in the class documentation.
	 */
	@Test
	public void checkItemProcessing() {

		// Local Declarations
		int itemId = -1;
		FormStatus status = null;
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String separator = System.getProperty("file.separator");

		// Setup the project space so that the output file can be checked.
		try {
			// Get the project handle
			project = workspaceRoot.getProject("itemManagerTesterWorkspace");
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ "itemManagerTesterWorkspace")).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("itemManagerTesterWorkspace");
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

		// Create a FakeGeometry. This will return an instance of FakeItem.
		itemId = itemManager.createItem(fakeGeometryBuilder.getItemName(),
				null);

		// Get the FakeItem
		fakeItem = fakeGeometryBuilder.getLastFakeItem();

		// Process the Item and check its status
		status = itemManager.processItem(itemId, "blend");
		assertEquals(FormStatus.Processed, status);
		assertEquals(FormStatus.Processed, itemManager.getItemStatus(itemId));

		// Make sure the Item was processed
		assertTrue(fakeItem.wasProcessed());

		// Reset the FakeItem
		fakeItem.reset();

		// Process the Item with bad values and make sure it was not processed.
		// Bad Item id first.
		status = itemManager.processItem(-1, "blend");
		assertEquals(FormStatus.InfoError, status);
		// null ActionName second.
		status = itemManager.processItem(itemId, null);
		assertEquals(FormStatus.InfoError, status);

		// Make sure the Item was NOT processed
		assertFalse(fakeItem.wasProcessed());

		// Setup the project space for the output file test
		fakeItem.setProject(project);

		// Setup the name of the output file. According to the documentation it
		// should be at <itemName>_<itemId>_processOutput.txt.
		String outputFilename = fakeItem.getName().replaceAll("\\s+", "_") + "_"
				+ fakeItem.getId() + "_processOutput.txt";
		System.out.println(
				"ItemManagerTester message: Looking for (shortened) output file name \""
						+ outputFilename + "\"");
		// Get the output file handle
		File outputFile = itemManager.getOutputFile(itemId);
		// Make sure it is not null
		assertNotNull(outputFile);
		// Make sure it contains our short name. It doesn't exactly matter where
		// the file is stored, as long as the name is properly set for now. That
		// means that the Item has created the file handle per the spec.
		String retOutputName = outputFile.getAbsolutePath();
		System.out.println(
				"ItemManagerTester message: Returned Output File Name = "
						+ retOutputName);
		assertTrue(outputFile.getAbsolutePath().contains(outputFilename));

		// Check canceling by putting the fake item into a persistent
		// "Processing" state and shutting it down.
		fakeItem.process("setProcessing");
		status = fakeItem.cancelProcess();
		assertTrue(fakeItem.wasCancelled());
		assertEquals(FormStatus.ReadyToProcess, status);

		return;
	}

	/**
	 * This operation checks that the ItemManager loads and persists all Items
	 * from and to the persistence provider when requested.
	 */
	@Test
	public void checkMassItemManagement() {

		// Reset the fake persistence provider
		fakePersistenceProvider.reset();

		// Tell the ItemManager to load everything from the persistence provider
		itemManager.loadItems(null);

		// Create an Item
		int itemId = itemManager.createItem(fakeGeometryBuilder.getItemName(),
				null);
		// Make sure the Item id is 2 since the FakePersistenceProvider provides
		// Items with ids 1 and 3
		assertEquals(2, itemId);
		// Create an Item
		itemId = itemManager.createItem(fakeGeometryBuilder.getItemName(),
				null);
		// Make sure the Item id is 4 since the FakePersistenceProvider provides
		// Items with ids 1 and 3 and we just created one with id 2.
		assertEquals(4, itemId);

		// See if everything was loaded
		assertTrue(fakePersistenceProvider.allLoaded());

		// Reset the fake persistence provider
		fakePersistenceProvider.reset();

		// Tell the ItemManager to store everything in the persistence provider
		itemManager.persistItems();

		// See if everything was updated
		assertTrue(fakePersistenceProvider.itemUpdated());

		return;

	}

}