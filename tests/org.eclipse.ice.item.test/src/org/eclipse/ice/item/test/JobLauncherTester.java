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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;
import org.junit.Ignore;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.item.action.LoginInfoForm;
import org.eclipse.ice.item.jobLauncher.JobLauncher;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;

import java.util.ArrayList;
import java.util.Random;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the JobLauncher class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class JobLauncherTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TestJobLauncher jobLauncher;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations attempts to put the JobLauncher in the
	 * FormStatus.NeedsInfo state by setting the hostname to something other
	 * than localhost. It retrieves the Form from the Item after than and makes
	 * sure that it is actually a LoginInfoForm and not a JobLauncherForm.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkLoginInfoForm() {
		// begin-user-code

		// Local Declarations
		String localHostname = null, remoteHost = null;
		Form form = null;
		TableComponent hostsTable = null;
		Entry platform = null;
		ArrayList<String> hostnames = null;
		FormStatus status = FormStatus.InfoError;
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String separator = System.getProperty("file.separator");
		// Enable debugging
		System.setProperty("DebugICE", "");

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

		// Instantiate the JobLauncher
		jobLauncher = new TestJobLauncher(project);

		// Get the Form
		form = jobLauncher.getForm();
		assertNotNull(form);

		// Get the hosts table
		hostsTable = (TableComponent) form
				.getComponent(JobLauncherForm.parallelId + 1);
		assertNotNull(hostsTable);
		// Select the first row in the table to denote the selected host
		ArrayList<Integer> selectedRowList = new ArrayList<Integer>();
		selectedRowList.add(0);
		hostsTable.setSelectedRows(selectedRowList);

		// Submit the Form
		status = jobLauncher.submitForm(form);
		assertEquals(FormStatus.ReadyToProcess, status);

		// Process the Item
		status = jobLauncher.process("Launch the Job");

		// Check the status
		assertEquals(FormStatus.NeedsInfo, status);

		// Get the Form and check it
		form = jobLauncher.getForm();
		assertNotNull(form);
		assertTrue(form instanceof LoginInfoForm);

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations makes sure that it is possible to add and remove hosts
	 * from the JobLauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkHosts() {
		// begin-user-code
		// Local Declarations
		ArrayList<String> list = new ArrayList<String>();

		// Setup Conditions
		jobLauncher = new TestJobLauncher(null);

		// Test getHost to make sure it is initialized when constructor is used
		assertNotNull(jobLauncher.getAllHosts());
		// It should have one host in there at the moment (notlocalhost)
		assertEquals(1, jobLauncher.getAllHosts().size());

		// Try to add hosts
		jobLauncher.addHost("blah.blah.gov", "Windows 7 x64", "C:\\myProgram");
		jobLauncher.addHost("one.two.com", "Linux x86_x64", "home\\usr");
		jobLauncher.addHost("Hay.org", "MacOSX 10.4", "SomeDirectory");
		jobLauncher.addHost("tokyo.jp", "Playstation 3 OS", "home\\usr");

		// Get the hosts - check
		list = jobLauncher.getAllHosts();
		assertEquals(5, list.size());
		assertEquals("notlocalhost", list.get(0));
		assertEquals("blah.blah.gov", list.get(1));
		assertEquals("one.two.com", list.get(2));
		assertEquals("Hay.org", list.get(3));
		assertEquals("tokyo.jp", list.get(4));

		// add a duplicate host - check contents
		jobLauncher.addHost("blah.blah.gov", "Windows 7 x64", "C:\\myProgram");

		// Get the hosts - check
		list = jobLauncher.getAllHosts();
		assertFalse(list.isEmpty());
		assertEquals(list.size(), 5);

		// Add for null - hostname
		jobLauncher.addHost(null, "Windows 7 x64", "C:\\myProgram");

		// Get the hosts - check
		list = jobLauncher.getAllHosts();
		assertFalse(list.isEmpty());
		assertEquals(list.size(), 5);

		// Add for null - os
		jobLauncher.addHost("blah.blah.gov", null, "C:\\myProgram");

		// Get the hosts - check
		list = jobLauncher.getAllHosts();
		assertFalse(list.isEmpty());
		assertEquals(list.size(), 5);

		// Add for null - path
		jobLauncher.addHost("blah.blah.gov", "Windows 7 x64", null);

		// Get the hosts - check
		list = jobLauncher.getAllHosts();
		assertFalse(list.isEmpty());
		assertEquals(list.size(), 5);

		// Delete items with hostname
		jobLauncher.deleteHost("blah.blah.gov");

		// Get the hosts - check
		list = jobLauncher.getAllHosts();
		assertFalse(list.isEmpty());
		assertEquals(list.size(), 4);

		// Delete something that does not exist
		jobLauncher.deleteHost("blah.blah.gov");

		// Get the hosts - check
		list = jobLauncher.getAllHosts();
		assertFalse(list.isEmpty());
		assertEquals(list.size(), 4);

		// Delete null
		jobLauncher.deleteHost(null);

		// Get the hosts - check
		list = jobLauncher.getAllHosts();
		assertFalse(list.isEmpty());
		assertEquals(list.size(), 4);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation makes sure that it is possible to enable and disable
	 * support for TBB, OpenMP and MPI in the JobLauncher.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkEnablingParallelism() {
		// begin-user-code
		// Local Declarations:
		JobLauncherForm form;
		DataComponent dataC;
		Entry entry;

		// check MPI
		jobLauncher = new TestJobLauncher(null);
		jobLauncher.setupForm();

		// Legit use of enable
		jobLauncher.enableMPI(1, 5, 2);

		// Get the MPI data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);

		// Check values
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// 0 - set to 1
		jobLauncher.enableMPI(0, 5, 2);

		// Get the MPI data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);

		// Check values
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// negative minimum
		jobLauncher.enableMPI(-1, 5, 2);

		// Get the MPI data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);

		// Check values - nothing changed
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// Max less than minimum
		jobLauncher.enableMPI(5, 2, 1);

		// Get the MPI data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);

		// Check values - nothing changed
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// disable
		jobLauncher.disableMPI();
		// Get the MPI data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();

		// Should not have MPI - total number of components should be 3
		assertEquals(form.getComponents().size(), 3);

		// disable again, see if it breaks
		jobLauncher.disableMPI();
		// Get the MPI data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();

		// Should not have MPI - total number of components should be 3
		assertEquals(form.getComponents().size(), 3);

		// check OpenMP
		// Just see if it crashes or not
		// Legit use of enable
		jobLauncher.enableOpenMP(1, 5, 2);

		// Get the OpenMP data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);

		// Check values
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// 0 - set to 1
		jobLauncher.enableOpenMP(0, 5, 2);

		// Get the OpenMP data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);

		// Check values - nothing changed
		assertEquals("2",entry.getValue());
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// negative minimum
		jobLauncher.enableOpenMP(-1, 5, 2);

		// Get the OpenMP data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);

		// Check values - nothing changed
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// Max less than minimum
		jobLauncher.enableOpenMP(5, 2, 1);

		// Get the OpenMP data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);

		// Check values - nothing changed
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// disable
		jobLauncher.disableOpenMP();

		// Get the OpenMP data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();

		entry = (Entry) dataC.retrieveAllEntries().get(0);
		// Should not have MPI - total number of components should be 3
		assertEquals(form.getComponents().size(), 3);

		// disable again, see if it breaks
		jobLauncher.disableOpenMP();
		// Get the OpenMP data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();

		// Should not have MPI - total number of components should be 3
		assertEquals(form.getComponents().size(), 3);

		jobLauncher = new TestJobLauncher(null);
		jobLauncher.setupForm();

		// Enable them both
		jobLauncher.enableMPI(1, 5, 2);
		jobLauncher.enableOpenMP(1, 5, 2);

		// Check the dataComponent, see if they are both there
		// Get the OpenMP data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);
		assertNotNull(entry);

		// Check values - nothing changed
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// Get the MPI data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);

		// Check values - nothing changed
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		jobLauncher = new TestJobLauncher(null);
		jobLauncher.setupForm();

		// Try to enable when the MPI or openMP is not created
		jobLauncher.enableMPI(-1, 2, 3);
		jobLauncher.enableOpenMP(-1, 2, 3);

		// Get the OpenMP data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();

		// Should not have MPI - total number of components should be 3
		assertEquals(form.getComponents().size(), 3);

		// Tests for TBB
		jobLauncher.enableTBB(1, 5, 3);

		// Get the data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveAllEntries().get(1);

		// Check values
		assertEquals(entry.getValue(), "3");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// Enable TBB with OpenMP and MPI
		jobLauncher.enableTBB(1, 5, 3);
		jobLauncher.enableMPI(1, 5, 4);
		jobLauncher.enableOpenMP(1, 5, 2);

		// Get the data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveEntry("Number of TBB Threads");

		// Check values - TBB
		assertEquals(entry.getValue(), "3");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		entry = (Entry) dataC.retrieveEntry("Number of MPI Processes");
		// Check values - MPI
		assertEquals(entry.getValue(), "4");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		entry = (Entry) dataC.retrieveEntry("Number of OpenMP Threads");
		// Check values - OpenMP
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// Disable TBB
		jobLauncher.disableTBB();

		// Get the data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveEntry("Number of TBB Threads");

		// Check values - TBB
		assertNull(entry);

		// Check values - MPI
		entry = (Entry) dataC.retrieveEntry("Number of MPI Processes");
		assertEquals(entry.getValue(), "4");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// Check values - OpenMP
		entry = (Entry) dataC.retrieveEntry("Number of OpenMP Threads");
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// Try to enable a bad TBB - out of order
		jobLauncher.enableTBB(20, 10, 5);
		// Get the data component and the respective values
		form = (JobLauncherForm) jobLauncher.getForm();
		dataC = (DataComponent) form.getComponents().get(3);
		entry = (Entry) dataC.retrieveEntry("Number of TBB Threads");

		// Check values - TBB
		assertNull(entry);

		// Check values - MPI
		entry = (Entry) dataC.retrieveEntry("Number of MPI Processes");
		assertEquals(entry.getValue(), "4");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// Check values - OpenMP
		entry = (Entry) dataC.retrieveEntry("Number of OpenMP Threads");
		assertEquals(entry.getValue(), "2");
		assertEquals(entry.getAllowedValues().get(0), "1");
		assertEquals(entry.getAllowedValues().get(1), "5");

		// Enable TBB, disable the rest, show that only TBB exists
		jobLauncher.enableTBB(3, 20, 5);
		jobLauncher.disableMPI();
		jobLauncher.disableOpenMP();

		// Check values - TBB
		entry = (Entry) dataC.retrieveEntry("Number of TBB Threads");
		assertEquals(entry.getValue(), "5");
		assertEquals(entry.getAllowedValues().get(0), "3");
		assertEquals(entry.getAllowedValues().get(1), "20");

		// Check values - MPI
		entry = (Entry) dataC.retrieveEntry("Number of MPI Processes");
		assertNull(entry);

		// Check values - OpenMP
		entry = (Entry) dataC.retrieveEntry("Number of OpenMP Threads");
		assertNull(entry);

		// Disable TBB thread, show that component disappears
		jobLauncher.disableTBB();
		assertNull(form.getComponent(dataC.getId()));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the JobLauncher to insure that its equals()
	 * operation works.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		// Create JobLauncherItems to test
		JobLauncher item = new JobLauncher();
		JobLauncher equalItem = new JobLauncher();
		JobLauncher unEqualItem = new JobLauncher();
		JobLauncher transitiveItem = new JobLauncher();

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
		assertFalse(item==null);

		// Assert that two equal objects return same hashcode
		assertTrue(item.equals(equalItem)
				&& item.hashCode() == equalItem.hashCode());

		// Assert that hashcode is consistent
		assertTrue(item.hashCode() == item.hashCode());

		// Assert that hashcodes from unequal objects are different
		assertTrue(item.hashCode() != unEqualItem.hashCode());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the JobLauncher to ensure that its copy() and
	 * clone() operations work as specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		// Local Declarations
		JobLauncher cloneItem = new JobLauncher(null), copyItem = new JobLauncher(
				null);
		JobLauncher jobItem = new JobLauncher();

		jobItem.setDescription("I am a job!");
		jobItem.setExecutable("LS", "/opt/bin", "DOIT!!!!");
		jobItem.setProject(null);

		// run clone operations
		cloneItem = (JobLauncher) jobItem.clone();

		// check contents
		assertEquals(jobItem.getAvailableActions(),
				cloneItem.getAvailableActions());
		assertEquals(jobItem.getDescription(), cloneItem.getDescription());
		assertTrue(jobItem.getForm().equals(cloneItem.getForm()));
		assertEquals(jobItem.getId(), cloneItem.getId());
		assertEquals(jobItem.getItemType(), cloneItem.getItemType());
		assertEquals(jobItem.getName(), cloneItem.getName());
		assertEquals(jobItem.getStatus(), cloneItem.getStatus());
		assertEquals(jobItem.getAllHosts(), cloneItem.getAllHosts());

		// run copy operation
		copyItem.copy(jobItem);

		// check contents
		assertEquals(jobItem.getAvailableActions(),
				copyItem.getAvailableActions());
		assertEquals(jobItem.getDescription(), copyItem.getDescription());
		assertTrue(jobItem.getForm().equals(copyItem.getForm()));
		assertEquals(jobItem.getId(), copyItem.getId());
		assertEquals(jobItem.getItemType(), copyItem.getItemType());
		assertEquals(jobItem.getName(), copyItem.getName());
		assertEquals(jobItem.getStatus(), copyItem.getStatus());
		assertEquals(jobItem.getAllHosts(), copyItem.getAllHosts());

		// run copy operation by passing null
		copyItem.copy(null);

		// check contents - nothing has changed
		assertEquals(jobItem.getAvailableActions(),
				copyItem.getAvailableActions());
		assertEquals(jobItem.getDescription(), copyItem.getDescription());
		assertTrue(jobItem.getForm().equals(copyItem.getForm()));
		assertEquals(jobItem.getId(), copyItem.getId());
		assertEquals(jobItem.getItemType(), copyItem.getItemType());
		assertEquals(jobItem.getName(), copyItem.getName());
		assertEquals(jobItem.getStatus(), copyItem.getStatus());
		assertEquals(jobItem.getAllHosts(), copyItem.getAllHosts());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the JobLauncher to persist itself to
	 * XML and to load itself from an XML input stream.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkXMLPersistence() {
		// begin-user-code
		/*
		 * The following sets of operations will be used to test the
		 * "read and write" portion of the JobLauncher Item. It will demonstrate
		 * the behavior of reading and writing from an
		 * "XML (inputStream and outputStream)" file. It will use an annotated
		 * Item to demonstrate basic behavior.
		 */

		// Local declarations
		JobLauncher loadedItem = new JobLauncher();

		// Set up item
		JobLauncher persistedItem = new JobLauncher();
		persistedItem.setDescription("JobLauncher item description");
		persistedItem.setId(5);
		persistedItem.setName("Name!");
		persistedItem.getForm().setItemID(6);

		// persist to an output stream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		persistedItem.persistToXML(outputStream);

		// Load an Item from the first
		loadedItem.loadFromXML(new ByteArrayInputStream(outputStream
				.toByteArray()));
		// Dump the XML so that it can be inspected
		persistedItem.persistToXML(System.out);
		loadedItem.persistToXML(System.out);
		// Make sure they match
		assertEquals(persistedItem, loadedItem);

		// Check the contents more closely to make sure that JobLauncher Item.
		assertEquals(persistedItem.getAvailableActions(),
				loadedItem.getAvailableActions());
		assertEquals(persistedItem.getDescription(),
				loadedItem.getDescription());
		assertEquals(persistedItem.getForm(), loadedItem.getForm());
		assertEquals(persistedItem.getId(), loadedItem.getId());
		assertEquals(persistedItem.getItemType(), loadedItem.getItemType());
		assertEquals(persistedItem.getName(), loadedItem.getName());
		assertEquals(persistedItem.getStatus(), loadedItem.getStatus());
		assertTrue(persistedItem.getAllHosts().equals(loadedItem.getAllHosts()));

		// The next following tests demonstrate behavior for when you pass null
		// args for read()

		// test for read - null args
		loadedItem.loadFromXML(null);

		// check contents - nothing has changed
		assertEquals(persistedItem.getAvailableActions(),
				loadedItem.getAvailableActions());
		assertEquals(persistedItem.getDescription(),
				loadedItem.getDescription());
		assertTrue(persistedItem.getForm().equals(loadedItem.getForm()));
		assertEquals(persistedItem.getId(), loadedItem.getId());
		assertEquals(persistedItem.getItemType(), loadedItem.getItemType());
		assertEquals(persistedItem.getName(), loadedItem.getName());
		assertEquals(persistedItem.getStatus(), loadedItem.getStatus());
		assertTrue(persistedItem.getAllHosts().equals(loadedItem.getAllHosts()));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the JobLauncher to ensure that it can manage
	 * multiple input files.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkMultipleInputFiles() {
		// begin-user-code

		// Local Declarations
		String name = "JLwMF"; // JobLauncher with Multiple Files
		String desc = "Duritanium Alloy";
		int numFiles = 0, numExtraFiles = 0, totalFiles = 0;
		Form jobForm = null;
		DataComponent fileComponent = null;
		ArrayList<Entry> fileEntries = null;
		Random rng = new Random(2013052111);
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
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ "itemTesterWorkspace")).toURI();
				// Create the project description
				IProjectDescription projectDesc = ResourcesPlugin
						.getWorkspace().newProjectDescription(
								"itemTesterWorkspace");
				// Set the location of the project
				projectDesc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(projectDesc, null);
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

		// Set up the launcher
		JobLauncher launcher = new JobLauncher(project);
		launcher.setDescription("JobLauncher with multiple files");
		launcher.setId(5);
		launcher.setName(name);

		// Get the form and make sure that there is only one file to start
		jobForm = launcher.getForm();
		assertNotNull(jobForm);
		fileComponent = (DataComponent) jobForm.getComponent(1);
		assertNotNull(fileComponent);
		fileEntries = fileComponent.retrieveAllEntries();
		numFiles = fileEntries.size();
		assertEquals(1, numFiles);

		// Add a few files
		numExtraFiles = rng.nextInt(50) + 1; // +1 such that at least one added
		totalFiles = 1 + numExtraFiles; // 1 by default + the extras
		for (int i = 0; i < numExtraFiles; i++) {
			launcher.addInputType(name + " " + i, name + i + "File", desc + " "
					+ i, null);
		}

		// Check that the files were added
		numFiles = fileEntries.size();
		assertEquals(totalFiles, numFiles);

		// Check the names and descriptions
		for (int i = 1; i < totalFiles; i++) {
			assertEquals(name + " " + (i - 1), fileEntries.get(i).getName());
			assertEquals(desc + " " + (i - 1), fileEntries.get(i)
					.getDescription());
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation insures that the JobLauncher properly refreshes after
	 * being issued a reload command.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
//	@Test
	public void checkDataReload() {
		// begin-user-code

		// Local Declarations
		String name = "JLwMF&R.test"; // JobLauncher with Multiple Files
		String desc = "Duritanium Alloy";
		int numFileTypes = 0, numFiles = 0;
		Form jobForm = null;
		DataComponent fileComponent = null;
		ArrayList<Entry> fileEntries = null;
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
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ "itemTesterWorkspace")).toURI();
				// Create the project description
				IProjectDescription projectDesc = ResourcesPlugin
						.getWorkspace().newProjectDescription(
								"itemTesterWorkspace");
				// Set the location of the project
				projectDesc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(projectDesc, null);
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

		try {
			// Remove any old test files
			for (IResource resource : project.members()) {
				if (resource.getType() == IResource.FILE
						&& !(".project").equals(resource.getName())) {

					resource.delete(true, null);
				}
			}
			// Refresh the project space
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e1) {
			// Complain
			e1.printStackTrace();
			fail();
		}

		// Get the file handle
		IFile file = project.getFile(name);

		// Set up the launcher. The launcher uses both the full list of files
		// for the input files (fileEntries[0]) and a filtered list of input
		// files for the test (fileEntries[1]).
		JobLauncher launcher = new JobLauncher(project);
		launcher.setDescription("JobLauncher with multiple files");
		launcher.setId(5);
		launcher.setName(name);
		// Add a second input file that only works with test files so that we
		// can test updating filtered input files.
		launcher.addInputType(name, name + "File", desc + " ", ".test");

		// Get the form and get the current number of input files.
		jobForm = launcher.getForm();
		assertNotNull(jobForm);
		fileComponent = (DataComponent) jobForm.getComponent(1);
		assertNotNull(fileComponent);
		fileEntries = fileComponent.retrieveAllEntries();
		numFileTypes = fileEntries.size();
		assertEquals(2, numFileTypes);
		numFiles = fileEntries.get(0).getAllowedValues().size();
		// Also get the number of test files and make sure it is 0.
		assertEquals(0, fileEntries.get(1).getAllowedValues().size());

		// Create the test file in the workspace
		String contents = "contents";
		ByteArrayInputStream testStream = new ByteArrayInputStream(
				contents.getBytes());
		try {
			file.create(testStream, true, null);
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
			fail();
		}

		// Refresh the launcher
		launcher.reloadProjectData();

		// Check the number of input files and test files. Make sure the number
		// of types didn't change!
		assertEquals(numFileTypes, fileEntries.size());
		// The number of files should now be 1 (.project) + 1 (Process output) +
		// 1 (the test file)
		numFiles = 3;
		// Make sure that the number of input files is correct
		System.out.println("JobLauncherTester Message: " + "NumFiles = "
				+ (numFiles + 1));
		System.out.println("JobLauncherTester Message: " + "File Entries = "
				+ fileEntries.get(0).getAllowedValues().size());
		for (String value : fileEntries.get(0).getAllowedValues()) {
			System.out.println(value);
		}
		assertEquals(numFiles, fileEntries.get(0).getAllowedValues().size());
		// Also get the number of test files and make sure it is 0.
		assertEquals(1, fileEntries.get(1).getAllowedValues().size());

		// Delete the test file
		try {
			file.delete(true, null);
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// Complain
			e.printStackTrace();
			fail();
		}

		return;
		// end-user-code
	}
}