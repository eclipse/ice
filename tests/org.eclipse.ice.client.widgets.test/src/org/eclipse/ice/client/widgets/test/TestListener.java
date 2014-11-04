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
package org.eclipse.ice.client.widgets.test;

import org.eclipse.ice.iclient.uiwidgets.IProcessEventListener;
import org.eclipse.ice.iclient.uiwidgets.IUpdateEventListener;
import org.eclipse.ice.datastructures.form.Form;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class implements both the IUpdateEventListener and IProcessEventListener
 * interfaces and is used to test the FormWidget class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TestListener implements IProcessEventListener,
		IUpdateEventListener {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the listener has received an update, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean updated;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the listener has received a process request, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean processed;

	/**
	 * This flag is set to true if the test listener received a request to
	 * cancel a process.
	 */
	private boolean cancelled = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the listener was updated and false
	 * otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasUpdated() {
		// begin-user-code
		return updated;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the listener received a process request
	 * and false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if a process request was received, false otherwise.
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
	 * This operation returns true if the listener was notified of a process
	 * cancellation request.
	 * 
	 * @return True if the request was made, false otherwise.
	 */
	public boolean wasCancelled() {
		// begin-user-code
		return cancelled;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation resets the TestListener by setting the update and process
	 * states to false.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reset() {
		// begin-user-code

		// Reset the flags
		updated = false;
		processed = false;
		cancelled = false;

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IProcessEventListener#processSelected(Form form, String process)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void processSelected(Form form, String process) {
		// begin-user-code

		// Set the process flag
		processed = true;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateEventListener#formUpdated(Form form)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void formUpdated(Form form) {
		// begin-user-code

		// Set the update flag
		updated = true;

		// end-user-code
	}

	@Override
	public void cancelRequested(Form form, String process) {
		cancelled = true;
		return;
	}
}