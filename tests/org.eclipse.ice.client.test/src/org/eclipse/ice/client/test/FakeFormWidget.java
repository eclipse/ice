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
package org.eclipse.ice.client.test;

import org.eclipse.ice.iclient.uiwidgets.IFormWidget;
import org.eclipse.ice.iclient.uiwidgets.IProcessEventListener;
import org.eclipse.ice.iclient.uiwidgets.ISimpleResourceProvider;
import org.eclipse.ice.iclient.uiwidgets.IUpdateEventListener;
import org.eclipse.ice.datastructures.form.Form;
import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The FakeFormWidget is a realization of IFormWidget that is used for testing.
 * It provides several methods in addition to the IFormWidget interface that are
 * used for testing and introspection.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeFormWidget implements IFormWidget {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Boolean to signify if a listener was registered.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean observed;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Boolean to store the display state.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean displayed;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Form widgetForm;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of IUpdateEventListeners
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IUpdateEventListener> updateListeners;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of IProcessEventListeners
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IProcessEventListener> processListeners;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if setItem() was called on the FakeFormWidget
	 * with a non-null Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean formRegistered() {
		// begin-user-code

		// Return true if the Form is not null, otherwise false
		if (this.widgetForm != null) {
			return true;
		}
		return false;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if a UIWidgetListener is registered for the
	 * FakeFormWidget.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean listenerRegistered() {
		// begin-user-code
		return this.observed;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the display operation was previously
	 * called for the FakeFormWidget.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean widgetDisplayed() {
		// begin-user-code
		return this.displayed;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation implements display() from UIWidget with a simple pass
	 * through that makes whether or not the method was called. Nothing is drawn
	 * on the screen.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void display() {
		// begin-user-code

		this.displayed = true;

		return;

		// end-user-code
	}

	/**
	 * Override the registerListener method to properly set the update flag
	 */
	@Override
	public void registerUpdateListener(IUpdateEventListener listener) {

		// Store the listener
		if (updateListeners == null) {
			updateListeners = new ArrayList<IUpdateEventListener>();
		}
		updateListeners.add(listener);

		// Set the flag if the listener is not null
		if (listener != null) {
			this.observed = true;
		}
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#registerProcessListener(IProcessEventListener
	 *      listener)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerProcessListener(IProcessEventListener listener) {
		// begin-user-code

		// Store the listener
		if (processListeners == null) {
			processListeners = new ArrayList<IProcessEventListener>();
		}
		processListeners.add(listener);

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#registerResourceProvider(ISimpleResourceProvider
	 *      provider)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerResourceProvider(ISimpleResourceProvider provider) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#notifyUpdateListeners()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyUpdateListeners() {
		// begin-user-code

		for (IUpdateEventListener i : updateListeners) {
			i.formUpdated(widgetForm);
		}
		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#notifyProcessListeners(String process)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyProcessListeners(String process) {
		// begin-user-code

		for (IUpdateEventListener i : updateListeners) {
			i.formUpdated(widgetForm);
		}
		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#notifyCancelListeners(String process)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyCancelListeners(String process) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormWidget#setForm(Form form)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setForm(Form form) {
		// begin-user-code

		widgetForm = form;

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormWidget#getForm()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Form getForm() {
		// begin-user-code
		return widgetForm;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormWidget#updateStatus(String statusMessage)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void updateStatus(String statusMessage) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormWidget#disable(boolean state)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disable(boolean state) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

}