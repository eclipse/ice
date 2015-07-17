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
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ice.viz.service.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IGeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.jaxbclassprovider.ICEJAXBClassProvider;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;
import org.junit.Test;

/**
 * <p>
 * The JobLauncherFormTester is responsible for testing the JobLauncherForm. It
 * is primarily focused on testing the number and composition of the Components
 * in the JobLauncherForm. It realizes the IComponentVisitor interface to find
 * the Parallel Execution Data Component from the JobLauncherForm.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class JobLauncherFormTester implements IComponentVisitor {
	/**
	 * <p>
	 * The JobLauncherForm that is under test.
	 * </p>
	 * 
	 */
	private JobLauncherForm jobLauncherForm;

	/**
	 * <p>
	 * A reference to store the threadingComponent used in the checkParallelism
	 * test.
	 * </p>
	 * 
	 */
	private DataComponent threadingComponent = null;

	/**
	 * <p>
	 * This operation checks the Components of the JobLauncherForm to make sure
	 * that they are correct in their number and configuration. It also checks
	 * the list of input files and makes sure that it can be changed properly.
	 * </p>
	 * 
	 */
	@Test
	public void checkJobLauncherFormComponents() {

		// Local Declarations
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> retFiles = new ArrayList<String>();

		// Setup some files for the Form
		files.add("testFile1");
		files.add("testFile2");
		files.add("testFile3");
		files.add("testFile4");
		files.add("testFile5");

		// Create the JobLauncherForm
		jobLauncherForm = new JobLauncherForm();
		jobLauncherForm.setInputFiles("Input file", "The input file", files);

		// Check the number of Components in the JobLauncherForm. It should have
		// two, a DataComponent and an OutputComponent.
		assertEquals(2, jobLauncherForm.getNumberOfComponents());

		// Get the Entries in the input files component
		ArrayList<Entry> entries = ((DataComponent) jobLauncherForm
				.getComponent(1)).retrieveAllEntries();
		// Check the number of Entries in the input files component
		assertEquals(1, entries.size());
		// Check the Entries. The Input File Entry should have files.size()
		// allowed values.
		assertEquals("Input file", entries.get(0).getName());
		retFiles = entries.get(0).getAllowedValues();
		// Check the list of files
		assertNotNull(retFiles);
		assertEquals(files, retFiles);

		return;
	}

	/**
	 * <p>
	 * This operation makes sure that it is possible to enable and disable
	 * support for OpenMP, TBB, and MPI in the JobLauncherForm.
	 * </p>
	 * 
	 */
	@Test
	public void checkParallelism() {

		// Local Declarations
		Entry openMPEntry = null;
		Entry mpiEntry = null;
		Entry tBBEntry = null;

		// Create the JobLauncherForm
		jobLauncherForm = new JobLauncherForm();

		// Make sure there is no threading component in the Form
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNull(threadingComponent);

		// Enable OpenMP
		jobLauncherForm.enableOpenMP(1, 5, 2);

		// Get the parallel component
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNotNull(threadingComponent);

		// Get the OpenMP Entry and check it
		openMPEntry = threadingComponent
				.retrieveEntry("Number of OpenMP Threads");
		assertNotNull(openMPEntry);
		assertEquals("2", openMPEntry.getDefaultValue());
		assertTrue(openMPEntry.getAllowedValues().contains("1"));
		assertTrue(openMPEntry.getAllowedValues().contains("5"));

		// Enable MPI
		jobLauncherForm.enableMPI(1, 1024, 512);
		
		// Check the account code 
		Entry accountEntry = threadingComponent.retrieveEntry("Account Code/Project Code");
		assertNotNull(accountEntry);
		assertEquals("none",accountEntry.getValue());
		assertEquals(accountEntry.getValueType(),AllowedValueType.Undefined);

		// Get the MPI Entry and check it
		mpiEntry = threadingComponent.retrieveEntry("Number of MPI Processes");
		assertNotNull(mpiEntry);
		assertEquals("512", mpiEntry.getDefaultValue());
		assertTrue(mpiEntry.getAllowedValues().contains("1"));
		assertTrue(mpiEntry.getAllowedValues().contains("1024"));

		// Enable TBB
		jobLauncherForm.enableTBB(1, 5, 3);
		System.err.println(threadingComponent.retrieveAllEntries().size());
		// Get the TBB Entry and check it
		tBBEntry = threadingComponent.retrieveEntry("Number of TBB Threads");
		assertNotNull(tBBEntry);
		assertEquals("3", tBBEntry.getDefaultValue());
		assertTrue(tBBEntry.getAllowedValues().contains("1"));
		assertTrue(tBBEntry.getAllowedValues().contains("5"));

		// Disable TBB, OpenMP and MPI
		threadingComponent = null;
		jobLauncherForm.disableMPI();
		jobLauncherForm.disableOpenMP();
		jobLauncherForm.disableTBB();

		// Make sure the "Parallel Execution" component is disabled
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNull(threadingComponent);

		// Re-enable the parallelism
		jobLauncherForm.enableOpenMP(1, 5, 2);
		jobLauncherForm.enableMPI(1, 1024, 512);
		jobLauncherForm.enableTBB(1, 5, 3);

		// Disable OpenMP and make sure the component is still available
		jobLauncherForm.disableOpenMP();
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNotNull(threadingComponent);
		threadingComponent = null;

		// Re-enable OpenMP and TBB, disable MPI
		jobLauncherForm.enableOpenMP(1, 5, 2);
		jobLauncherForm.enableTBB(1, 5, 3);
		jobLauncherForm.disableMPI();

		// Make sure the component is still available
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNotNull(threadingComponent);
		threadingComponent = null;

		// Re-enable MPI and TBB, disable OpenMP
		jobLauncherForm.enableMPI(1, 1024, 512);
		jobLauncherForm.enableTBB(1, 5, 3);
		jobLauncherForm.disableOpenMP();

		// Make sure the component is still available
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNotNull(threadingComponent);

		// Re-enable MPI and OpenMP, disable TBB
		jobLauncherForm.enableMPI(1, 1024, 512);
		jobLauncherForm.enableOpenMP(1, 5, 3);
		jobLauncherForm.disableTBB();

		// Make sure the component is still available
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNotNull(threadingComponent);

		// Re-enable MPI , disable TBB, OpenMP
		jobLauncherForm.enableMPI(1, 1024, 512);
		jobLauncherForm.disableOpenMP();
		jobLauncherForm.disableTBB();

		// Make sure the component is still available
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNotNull(threadingComponent);

		// Re-enable OpenMP , disable TBB, MPI
		jobLauncherForm.disableMPI();
		jobLauncherForm.enableOpenMP(1, 5, 3);
		jobLauncherForm.disableTBB();

		// Make sure the component is still available
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNotNull(threadingComponent);

		// Re-enable TBB , disable OpenMP, MPI
		jobLauncherForm.disableMPI();
		jobLauncherForm.disableOpenMP();
		jobLauncherForm.enableTBB(1, 5, 2);

		// Make sure the component is still available
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNotNull(threadingComponent);

		// Disable OpenMP and MPI and TBB
		jobLauncherForm.disableMPI();
		jobLauncherForm.disableOpenMP();
		jobLauncherForm.disableTBB();
		threadingComponent = null;

		// Make sure the "Parallel Execution" component is disabled
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNull(threadingComponent);

		// Enable OpenMP with bunk values
		jobLauncherForm.enableOpenMP(-1, -2, -3);

		// Retrieve the Component
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNotNull(threadingComponent);

		// Get the OpenMP Entry and check it - Everything should be equal to 1
		// since the values were negative
		openMPEntry = threadingComponent
				.retrieveEntry("Number of OpenMP Threads");
		assertNotNull(openMPEntry);
		assertEquals("1", openMPEntry.getDefaultValue());
		assertEquals(1, openMPEntry.getAllowedValues().size());
		assertEquals("1", openMPEntry.getAllowedValues().get(0));

		// Enable MPI with bunk values
		jobLauncherForm.enableMPI(-1, -2, -3);

		// Get the MPI Entry and check it - Everything should be equal to 1
		// since the values were negative
		mpiEntry = threadingComponent.retrieveEntry("Number of MPI Processes");
		assertNotNull(mpiEntry);
		assertEquals("1", mpiEntry.getDefaultValue());
		assertEquals(1, mpiEntry.getAllowedValues().size());
		assertEquals("1", mpiEntry.getAllowedValues().get(0));

		// Enable TBB with bunk values
		jobLauncherForm.enableTBB(-1, -2, -3);

		// Retrieve the Component
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNotNull(threadingComponent);

		// Get the TBBEntry and check it - Everything should be equal to 1
		// since the values were negative
		tBBEntry = threadingComponent.retrieveEntry("Number of TBB Threads");
		assertNotNull(tBBEntry);
		assertEquals("1", tBBEntry.getDefaultValue());
		assertEquals(1, tBBEntry.getAllowedValues().size());
		assertEquals("1", tBBEntry.getAllowedValues().get(0));

		// Disable TBB, OpenMP and MPI
		threadingComponent = null;
		jobLauncherForm.disableTBB();
		jobLauncherForm.disableMPI();
		jobLauncherForm.disableOpenMP();

		// Enable OpenMP with more bunk values - Out of order
		jobLauncherForm.enableOpenMP(54, 2, 1);

		// The parallel component should not be enabled because those values are
		// bad in some way
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNull(threadingComponent);

		// Enable OpenMP with more bunk values - Bad default
		jobLauncherForm.enableOpenMP(54, 56, 1);

		// The parallel component should not be enabled because those values are
		// bad in some way
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNull(threadingComponent);

		// Enable MPI with more bunk values - Bad default
		jobLauncherForm.enableMPI(54, 56, 1);

		// The parallel component should not be enabled because those values are
		// bad in some way
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNull(threadingComponent);

		// Enable TBB with more bunk values - Bad default
		jobLauncherForm.enableTBB(54, 56, 1);

		// The parallel component should not be enabled because those values are
		// bad in some way
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNull(threadingComponent);

		// Enable TBB with more bunk values - Out of Order
		jobLauncherForm.enableTBB(58, 56, 1);

		// The parallel component should not be enabled because those values are
		// bad in some way
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNull(threadingComponent);

		// Enable MPI with more bunk values - Out of order
		jobLauncherForm.enableOpenMP(54, 2, 1);

		// The parallel component should not be enabled because those values are
		// bad in some way
		for (Component i : jobLauncherForm.getComponents()) {
			i.accept(this);
		}
		assertNull(threadingComponent);

		return;

	}

	/**
	 * <p>
	 * An operation that checks the equals and hashCode operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {
		// Create JobLauncherForms to test
		JobLauncherForm form = new JobLauncherForm();
		JobLauncherForm equalForm = new JobLauncherForm();
		JobLauncherForm unEqualForm = new JobLauncherForm();
		JobLauncherForm transitiveForm = new JobLauncherForm();

		// Set ICEObject data
		equalForm.setId(form.getId());
		transitiveForm.setId(form.getId());
		unEqualForm.setId(2);

		// Set names
		equalForm.setName(form.getName());
		transitiveForm.setName(form.getName());
		unEqualForm.setName("DC UnEqual");

		// Assert two equal Forms return true
		assertTrue(form.equals(equalForm));

		// Assert two unequal Forms return false
		assertFalse(form.equals(unEqualForm));

		// Assert equals() is reflexive
		assertTrue(form.equals(form));

		// Assert the equals() is Symmetric
		assertTrue(form.equals(equalForm) && equalForm.equals(form));

		// Assert equals() is transitive
		if (form.equals(equalForm) && equalForm.equals(transitiveForm)) {
			assertTrue(form.equals(transitiveForm));
		} else {
			fail();
		}

		// Assert equals is consistent
		assertTrue(form.equals(equalForm) && form.equals(equalForm)
				&& form.equals(equalForm));
		assertTrue(!form.equals(unEqualForm) && !form.equals(unEqualForm)
				&& !form.equals(unEqualForm));

		// Assert checking equality with null is false
		assertFalse(form==null);

		// Assert that two equal objects return same hashcode
		assertTrue(form.equals(equalForm)
				&& form.hashCode() == equalForm.hashCode());

		// Assert that hashcode is consistent
		assertTrue(form.hashCode() == form.hashCode());

		// Assert that hashcodes from unequal objects are different
		assertTrue(form.hashCode() != unEqualForm.hashCode());

	}

	/**
	 * An operation that tests xml persistence on JobLauncherForm.
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 */
	@Test
	public void checkXMLPersistence() throws NullPointerException, JAXBException, IOException {

		/*
		 * The following sets of operations will be used to test the
		 * "read and write" portion of the JobLauncherForm Form. It will
		 * demonstrate the behavior of reading and writing from an
		 * "XML (inputStream and outputStream)" file. It will use an annotated
		 * Form to demonstrate basic behavior.
		 */

		// Local Declarations
		JobLauncherForm jobForm = new JobLauncherForm();
		JobLauncherForm loadedForm = new JobLauncherForm();
		ArrayList<String> actions = new ArrayList<String>();
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(JobLauncherForm.class);
		classList.addAll(new ICEJAXBClassProvider().getClasses());
		
		// Setup a JobLauncherForm
		jobForm.setName("I AM NEW!");
		jobForm.setDescription("Oh Somewhat NEW?!");
		jobForm.setId(3);
		jobForm.enableMPI(2, 4, 2);
		jobForm.enableOpenMP(2, 4, 3);

		// Add an action to the Form
		actions.add("I am an action!");
		jobForm.setActionList(actions);
		jobForm.setInputFiles("Input file", "The input file",
				new ArrayList<String>());

		// persist to an output stream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(jobForm, classList, outputStream);

		// Load an Item from the first
		loadedForm = (JobLauncherForm) xmlHandler.read(classList, new ByteArrayInputStream(outputStream
				.toByteArray()));
		// Make sure they match
		assertTrue(jobForm.equals(loadedForm));

	}

	/**
	 * <p>
	 * Checks the copy and clone routines on JobLauncherForm.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {
		/*
		 * The following sets of operations will be used to test the
		 * "clone and copy" portion of JobLauncherForm.
		 */
		// Local Declarations
		Form cloneForm = null, copyForm = new JobLauncherForm();
		JobLauncherForm jobForm = new JobLauncherForm();
		ArrayList<String> actions = new ArrayList<String>();

		jobForm.setName("I AM NEW!");
		jobForm.setDescription("Oh Somewhat NEW?!");
		jobForm.setId(3);

		actions.add("I am an action!");
		jobForm.setActionList(actions);
		jobForm.setInputFiles("Input file", "The input file",
				new ArrayList<String>());
		jobForm.enableMPI(2, 4, 2);
		jobForm.enableOpenMP(2, 4, 3);

		// Copy contents
		copyForm.copy(jobForm);

		// Check that it is equal
		assertTrue(jobForm.equals(copyForm));

		// Clone it
		cloneForm = (Form) jobForm.clone();

		// check that it is equal
		assertTrue(jobForm.equals(cloneForm));

		// pass null - Does not effect copy
		copyForm.copy(null);

		// Nothing has changed
		assertTrue(jobForm.equals(copyForm));

	}

	/**
	 * <p>
	 * This operation checks the JobLauncherForm to ensure that it can manage
	 * multiple input files.
	 * </p>
	 * 
	 */
	@Test
	public void checkMultipleInputFiles() {

		// Local Declarations
		ArrayList<String> files1 = new ArrayList<String>(), files2 = null, files3 = null;
		ArrayList<String> retFiles = new ArrayList<String>();
		String filename1 = "Input file", filename2 = "Other input file", filename3 = "Final input file";
		String desc1 = filename1 + "description", desc2 = filename2
				+ "description", desc3 = filename3 + "description";

		// Setup some files for the Form
		files1.add("testFile1");
		files1.add("testFile2");
		files1.add("testFile3");
		files1.add("testFile4");
		files1.add("testFile5");
		files2 = new ArrayList<String>(files1.subList(0, 3));
		files3 = new ArrayList<String>(files1.subList(0, 2));

		// Create the JobLauncherForm
		jobLauncherForm = new JobLauncherForm();
		jobLauncherForm.setInputFiles(filename1, desc1, files1);
		jobLauncherForm.setInputFiles(filename2, desc2, files2);
		jobLauncherForm.setInputFiles(filename3, desc3, files3);

		// Check the number of Components in the JobLauncherForm. It should have
		// two, a DataComponent and an OutputComponent.
		assertEquals(2, jobLauncherForm.getNumberOfComponents());

		// Get the Entries
		ArrayList<Entry> entries = ((DataComponent) jobLauncherForm
				.getComponent(1)).retrieveAllEntries();

		// Check the number of Entries in the Platform component. There should
		// be one entry each for the three input files.
		assertEquals(3, entries.size());

		// Check the first file set. The Input File Entry should have
		// files.size()
		// allowed values.
		assertEquals(filename1, entries.get(0).getName());
		retFiles = entries.get(0).getAllowedValues();
		assertNotNull(retFiles);
		assertEquals(files1.size(), retFiles.size());
		assertEquals(files1, retFiles);

		// Check the second file set
		assertEquals(filename2, entries.get(1).getName());
		retFiles = entries.get(1).getAllowedValues();
		assertNotNull(retFiles);
		assertEquals(files2.size(), retFiles.size());
		assertEquals(files2, retFiles);

		// Check the third file set
		assertEquals(filename3, entries.get(2).getName());
		retFiles = entries.get(2).getAllowedValues();
		assertNotNull(retFiles);
		assertEquals(files3.size(), retFiles.size());
		assertEquals(files3, retFiles);

		// Check the fourth file set
		assertEquals(filename3, entries.get(2).getName());
		retFiles = entries.get(2).getAllowedValues();
		assertNotNull(retFiles);
		assertEquals(files3.size(), retFiles.size());
		assertEquals(files3, retFiles);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(DataComponent component)
	 */

	@Override
	public void visit(DataComponent component) {

		// We only care about the "Parallel Exectuion" Data component in this
		// test
		if ("Parallel Execution".equals(((DataComponent) component).getName())) {
			threadingComponent = (DataComponent) component;
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(ResourceComponent component)
	 */
	@Override
	public void visit(ResourceComponent component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TableComponent component)
	 */
	@Override
	public void visit(TableComponent component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MatrixComponent component)
	 */
	@Override
	public void visit(MatrixComponent component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(IShape component)
	 */
	@Override
	public void visit(IShape component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(GeometryComponent component)
	 */
	@Override
	public void visit(IGeometryComponent component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MasterDetailsComponent component)
	 */
	@Override
	public void visit(MasterDetailsComponent component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TreeComposite component)
	 */
	@Override
	public void visit(TreeComposite component) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(IReactorComponent component)
	 */
	@Override
	public void visit(IReactorComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TimeDataComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MeshComponent component) {
		// TODO Auto-generated method stub

	}


	@Override
	public void visit(AdaptiveTreeComposite component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EMFComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ListComponent component) {
		// TODO Auto-generated method stub
		
	}
}