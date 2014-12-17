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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.messaging.Message;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This is a subclass of Item that implements setupForm(), reviewEntries() and
 * process() so that the other operations of Item can be tested. It creates a
 * relatively simple Form that is used during the tests.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "TestItem")
public class TestItem extends Item {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the TestItem was successfully updated, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean updated = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TestItem() {
		// begin-user-code
		// Punt
		this(null);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param project
	 *            <p>
	 *            The Eclipse IProject that should be used by the TestItem.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TestItem(IProject project) {
		// begin-user-code

		super(project);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides setupForm() to setup a Form for the tests.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupForm() {
		// begin-user-code

		// This operations creates four DataComponents and makes
		// one of them dependent on the value of another. This setups up a Form
		// with the names of the ICE team members circa February 2011.

		// Create the Form
		form = new Form();

		// Create the data components - the first is a Fake for testing
		FakeDataComponent dc1 = new FakeDataComponent();
		dc1.setId(1);
		dc1.setName("Jay");
		Entry dc1Entry = new Entry();
		dc1Entry.setName("Jay");
		dc1Entry.setValue("Awesome");
		dc1Entry.setTag("jayjaybillings");
		dc1.addEntry(dc1Entry);

		DataComponent dc2 = new DataComponent();
		dc2.setId(2);
		dc2.setName("David");
		Entry dc2Entry = new Entry();
		dc2Entry.setId(5);
		dc2Entry.setName("David's Entry");
		dc2Entry.setValue("The boss");
		dc2.addEntry(dc2Entry);

		DataComponent dc3 = new DataComponent();
		dc3.setId(3);
		dc3.setName("Alex");

		DataComponent dc4 = new DataComponent();
		dc4.setId(4);
		dc4.setName("Bobo the Drunken Clown");

		// Add the components to the Form
		form.addComponent(dc1);
		form.addComponent(dc2);
		form.addComponent(dc3);
		form.addComponent(dc4);

		// Set the particulars about the Item
		setName("Test Item");
		setId(15);
		setDescription("An Item used for testing.");

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides reviewEntries to modify the Form that it setup
	 * in some specific ways.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param preparedForm
	 *            <p>
	 *            The Form under review.
	 *            </p>
	 * @return <p>
	 *         The status.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected FormStatus reviewEntries(Form preparedForm) {
		// begin-user-code

		// This implementation of reviewEntries looks for the value
		// of David and passes it to Jay through the Registry.

		// Local Declarations
		int i = 0;
		FormStatus retVal = FormStatus.InfoError;
		ArrayList<DataComponent> components = new ArrayList<DataComponent>();
		DataComponent dc2 = null;
		Entry dc2Entry = null;

		// Grab the DataComponents
		for (i = 0; i < this.form.getNumberOfComponents(); i++) {
			components.add((DataComponent) form.getComponent(i + 1));
		}

		// Get David's data component in the list and get
		// the value from David's entry
		dc2 = components.get(1);
		dc2Entry = ((DataComponent) dc2).retrieveEntry("David's Entry");
		// Set the value in the Registry
		registry.setValue("Boss", dc2Entry.getValue());
		// Set the return value
		if (dc2Entry != null) {
			retVal = FormStatus.ReadyToProcess;
		}
		// Tell the Registry to dispatch any updates that are waiting
		registry.dispatch();

		// Return
		return retVal;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides super class operation to register a data component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void registerUpdateables() {
		// begin-user-code

		// Call the super class' registerUpdateables operation first
		super.registerUpdateables();

		// Register Entries against keys
		registry.register(form.getComponent(1), "Boss");

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs the TestItem to notify its listeners so that the
	 * ItemTester can see that the Item super class correctly handles the
	 * request.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyListeners() {
		// begin-user-code

		notifyListenersOfProjectChange();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation fakes setting some preferences, but calls
	 * getPreferencesDirectory() so that the Item base class tries to create the
	 * preferences directory.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPreferences() {
		// begin-user-code

		// Force the Item to create a preferences directory
		getPreferencesDirectory();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the TestItem was successfully updated,
	 * false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if updated, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasUpdated() {
		// begin-user-code
		return updated;
		// end-user-code
	}

	/**
	 * This operation overrides Item.update() to catch the update and mark the
	 * update flag.
	 */
	@Override
	public boolean update(Message msg) {
		super.update(msg);
		updated = true;
		return true;
	}

	/**
	 * This method is used by ItemTester to test that Item.getFiles functions
	 * appropriately.
	 * 
	 * @param directory
	 * @return
	 */
	public ArrayList<String> getYAMLFiles(String directory) {
		return getFiles(directory, ".yaml");
	}

	/**
	 * This method is used by the ItemTester to test that the Item.moveFile
	 * method functions correctly.
	 * 
	 * @param fromDir
	 * @param toDir
	 * @param fileName
	 */
	public void moveTestFile(String fromDir, String toDir, String fileName) {
		moveFile(fromDir, toDir, fileName);
	}

	/**
	 * This method is used by the ItemTester to test that the
	 * Item.deleteDirectory method functions correctly.
	 * 
	 * @param dir
	 */
	public void deleteTestDirectory(String dir) {
		deleteDirectory(dir);
	}

	/**
	 * This method is used by the ItemTester to test that the Item.copyFile
	 * method functions correctly.
	 * 
	 * @param fromDir
	 * @param toDir
	 * @param newName
	 */
	public void copyTestFile(String fromDir, String toDir, String newName) {
		copyFile(fromDir, toDir, newName);
	}

	/**
	 * This method is used by the ItemTester to test that the Item.copyFiles
	 * method functions correctly.
	 * 
	 * @param fromDir
	 * @param toDir
	 * @param newName
	 */
	public void copyMultipleFiles(String src, String dest,
			String ext) {
		
		copyFiles(src, dest, ext);
		
	}

	/**
	 * This method is used by the ItemTester to test that the Item.moveiles
	 * method functions correctly.
	 * 
	 * @param fromDir
	 * @param toDir
	 * @param newName
	 */
	public void moveMultipleFiles(String src, String dest, String ext) {
		moveFiles(src, dest, ext);
	}

	
}