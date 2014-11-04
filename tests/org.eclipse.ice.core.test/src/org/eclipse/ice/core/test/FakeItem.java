/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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

import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.messaging.Message;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This is a fake subclass of Item that is used to test the ItemManager and
 * Core.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeItem extends Item {

	/**
	 * A Constructor that merely calls super().
	 * 
	 * @param projectSpace
	 */
	public FakeItem(IProject projectSpace) {
		super(projectSpace);
		refreshed = false;
		loaded = false;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The process state of the FakeItem. True if processed, false if not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean processed;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the FakeItem was asked to reload data, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean refreshed;

	/**
	 * <p>
	 * True if an attempt was made to cancel an Item process, false otherwise.
	 * </p>
	 */
	private boolean cancelled = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the FakeItem was asked to load an imported input file, false
	 * otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean loaded;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the FakeItem was successfully updated, false otherwise.
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
	 * This operation returns the process state of the FakeItem.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The state - true if the Item was processed, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasProcessed() {
		// begin-user-code
		return processed;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation resets the FakeItem's process state to false.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reset() {
		// begin-user-code

		processed = false;
		refreshed = false;
		cancelled = false;
		loaded = false;
		updated = false;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the FakeItem was refreshed by calling
	 * reloadProjectData and false if not.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasRefreshed() {
		// begin-user-code
		return refreshed;
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
	 * This operation returns the load state of the FakeItem.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The state - true if the Item was loaded, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasLoaded() {
		// begin-user-code
		return loaded;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the FakeItem was successfully updated,
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
	 * (non-Javadoc)
	 * 
	 * @see Item#setupForm()
	 */
	protected void setupForm() {
		// begin-user-code

		form = new Form();
		form.setId(1);
		form.setItemID(2); // Set it to 2 so that we can make sure Item.setId
							// works because it should override this!
		form.setName("Harry Potter");
		form.setDescription("The Boy Who Lived");

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Item#reviewEntries(Form preparedForm)
	 */
	protected FormStatus reviewEntries(Form preparedForm) {
		// begin-user-code

		status = FormStatus.InfoError;

		// Set the status to ReadyToProcess if there are two components and
		// false otherwise.
		if (form.getNumberOfComponents() == 2) {
			status = FormStatus.ReadyToProcess;
		}

		return status;

		// end-user-code
	}

	@Override
	public FormStatus cancelProcess() {
		cancelled = true;
		return FormStatus.ReadyToProcess;
	}

	@Override
	public FormStatus process(String actionName) {

		processed = true;

		if (!("setProcessing".equals(actionName))) {
			status = FormStatus.Processed;
		} else {
			status = FormStatus.Processing;
		}
		return status;
	}

	@Override
	public void reloadProjectData() {
		refreshed = true;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the true an attempt was made to cancel a process
	 * request.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The status of the last call to process().
	 *         </p>
	 */
	public boolean wasCancelled() {
		// begin-user-code

		return cancelled;
		// end-user-code
	}

	/**
	 * Override the load operation for the input file import test.
	 */
	@Override
	public void loadInput(String file) {
		loaded = true;
	}

	/**
	 * Override the update operation to log the hit in the update test.
	 */
	@Override
	public boolean update(Message msg) {
		super.update(msg);
		updated = true;
		return true;
	}
}
