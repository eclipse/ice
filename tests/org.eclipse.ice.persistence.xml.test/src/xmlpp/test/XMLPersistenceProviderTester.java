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
package xmlpp.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.caebat.kvPair.CAEBATKVPairBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.nuclear.MOOSEModelBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import xmlpp.XMLPersistenceProvider;

/**
 * This class tests the XMLPersistenceProvider.
 * 
 * @author Jay Jay Billings
 * 
 */
public class XMLPersistenceProviderTester {

	/**
	 * The Eclipse project used in the test.
	 */
	private static IProject project;

	/**
	 * The XMLPersistenceProvider that will be tested.
	 */
	private static XMLPersistenceProvider xmlpp;

	/**
	 * This operation sets up the tester and creates the project space. It also
	 * copies a data file for the MOOSEModel Item into the workspace so that it
	 * can be used. It is the biggest normal Item in ICE and using it makes the
	 * test realistic.
	 * 
	 * The project name is chosen to match the name expected by the
	 * XMLPersistenceProvider.
	 */
	@BeforeClass
	static public void setup() {

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		String projectName = "itemDB";
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "persistenceData";
		String filePath = userDir + separator + "bison.yaml";
		String projectPath = userDir + separator + projectName;

		// Debug information
		System.out.println("MOOSE Test Data File: " + filePath);

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(projectPath).toURI());
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
			// Setup the project directory
			IFolder mooseFolder = project.getFolder("MOOSE");
			if (!mooseFolder.exists()) {
				// Create the directory
				mooseFolder.create(true, true, null);
				// Create the File handle and input stream
				IPath moosePath = new Path(filePath);
				File mooseFile = moosePath.toFile();
				FileInputStream mooseStream = new FileInputStream(mooseFile);
				// Create the Eclipse workspace file
				IFile mooseEFSFile = mooseFolder.getFile("bison.yaml");
				mooseEFSFile.create(mooseStream, true, null);
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

		// Setup the XMLPersistenceProvider
		xmlpp = new XMLPersistenceProvider(project);
		// Register the MOOSE model and the CAEBAT key-value pair builders with
		// it so that it can determine class information for unmarshalling
		// Items.
		xmlpp.addBuilder(new MOOSEModelBuilder());
		xmlpp.addBuilder(new CAEBATKVPairBuilder());
		try {
			// Start the service
			xmlpp.start();
		} catch (JAXBException e) {
			e.printStackTrace();
			fail();
		}

		return;
	}

	/**
	 * This operation cleans up after the test and removes the project space.
	 */
	@AfterClass
	static public void teardown() {

		// Delete the project.
		try {
			project.delete(true, null);
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
		}

		// Stop the provider
		xmlpp.stop();

		return;
	}

	/**
	 * This is a private utility operation used by the tests.
	 * 
	 * @param name
	 *            the name that the resource should have
	 * @return true if the file was found, false if not
	 */
	private boolean checkPersistedFile(String name) {

		System.out.println("XMLPersistenceProviderTester Message: "
				+ "Searching for " + name);

		try {
			// Get the list of resources
			IResource[] resources = project.members();
			// Check the list and make sure the file was stored
			for (IResource resource : resources) {
				System.out.println("XMLPersistenceProviderTester Message: "
						+ "Found resource " + resource.getName());
				if (resource.getName().equals(name)) {
					return true;
				}
			}
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
			fail();
		}

		return false;
	}

	/**
	 * This is a utility operation that just delays the execution of the program
	 * for the specified number of seconds.
	 * 
	 * @param seconds
	 *            The time to delay.
	 */
	private void pause(int seconds) {

		// Just delay the thread
		Thread.currentThread();
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

	/**
	 * This operation checks the ability of the XMLPersistenceProvider to
	 * persist Items to its project space. It also checks update() since that
	 * operation is identical to persist().
	 */
	@Test
	public void checkPersist() {

		// Create a MOOSE item
		MOOSEModelBuilder builder = new MOOSEModelBuilder();
		Item item = builder.build(project);
		String name = item.getName().replace(" ", "_") + "_" + item.getId()
				+ ".xml";

		// Persist it
		assertTrue(xmlpp.persistItem(item));

		// Wait while the file is persisted. The MOOSE Model takes about a half
		// a second, but lets wait two.
		pause(2);

		// Check the project space to make sure it was persisted
		assertTrue(checkPersistedFile(name));

		// Update the file
		assertTrue(xmlpp.updateItem(item));

		// Check the project space to make sure it was not deleted or something
		assertTrue(checkPersistedFile(name));

		// Wait while the file is persisted. The MOOSE Model takes about a half
		// a second, but lets wait two.
		pause(2);

		// Change the id of the item and persist it so that it shows up in the
		// project space as a different item
		item.setId(2);
		assertTrue(xmlpp.persistItem(item));

		// Wait while the file is persisted. The MOOSE Model takes about a half
		// a second, but lets wait two.
		pause(2);

		// Check the project space to make sure it was persisted
		name = item.getName().replace(" ", "_") + "_" + item.getId() + ".xml";
		assertTrue(checkPersistedFile(name));

		return;
	}

	/**
	 * This operation checks the load operation to make sure that the
	 * XMLPersistenceProvider can properly load Items from the workspace. It
	 * also checks the loadAll() and delete() operation since they are closely
	 * related to the ability to load a single Item.
	 */
	@Test
	public void checkLoad() {

		// Create a MOOSE item
		MOOSEModelBuilder builder = new MOOSEModelBuilder();
		CAEBATKVPairBuilder caebatBuilder = new CAEBATKVPairBuilder();
		Item item = builder.build(project);
		String name;
		int passedCount = 0;

		// Persist it
		item.setId(3);
		assertTrue(xmlpp.persistItem(item));

		// Wait while the file is persisted. The MOOSE Model takes about a half
		// a second, but lets wait two.
		pause(2);

		// Load the Item and check it
		Item loadedItem = xmlpp.loadItem(3);
		assertNotNull(loadedItem);
		// Set the project so that the Items will match. Recall that serialized
		// Items do not store their project info!
		loadedItem.setProject(project);
		assertEquals(item, loadedItem);

		// Change the id of the item and persist it so that it shows up in the
		// project space as a different item
		item.setId(4);
		assertTrue(xmlpp.persistItem(item));

		// Wait while the file is persisted. The MOOSE Model takes about a half
		// a second, but lets wait two.
		pause(2);

		// Now load "both" of the Items by calling load all
		ArrayList<Item> items = xmlpp.loadItems();
		assertNotNull(items);

		// Check the list
		for (Item listItem : items) {
			// Look for the correct name and item ids
			if (listItem.getName().equals(MOOSEModelBuilder.name)
					&& (listItem.getId() == 3 || listItem.getId() == 4)) {
				passedCount++;
			}
		}
		assertEquals(2, passedCount);

		// Delete the item with id = 3
		item.setId(3);
		assertTrue(xmlpp.deleteItem(item));

		// Wait while the file is deleted.
		pause(2);

		// Check the project and make sure it is gone
		name = item.getName().replace(" ", "_") + "_" + item.getId() + ".xml";
		assertFalse(checkPersistedFile(name));

		// Add a CAEBAT KVPair item, which has a hyphenated name, to make sure
		// the the provider can handle it.
		Item caebatItem = caebatBuilder.build(project);
		caebatItem.setId(5);
		assertTrue(xmlpp.persistItem(caebatItem));
		pause(2);
		items = xmlpp.loadItems();
		// Check the list
		passedCount = 0;
		for (Item listItem : items) {
			// Look for the correct name and item ids
			if (listItem.getName().equals(CAEBATKVPairBuilder.name)
					&& listItem.getId() == 5) {
				passedCount++;
			}
		}
		assertEquals(1,passedCount);

		return;
	}

}
