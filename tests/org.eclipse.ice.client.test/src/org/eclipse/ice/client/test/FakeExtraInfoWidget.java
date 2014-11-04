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
package org.eclipse.ice.client.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.iclient.uiwidgets.IExtraInfoWidget;
import org.eclipse.ice.iclient.uiwidgets.IWidgetClosedListener;

import java.util.ArrayList;
import org.eclipse.ice.datastructures.form.Form;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The FakeInfoWidget is a realization of IExtraInfoWidget that is used for
 * testing. It provides several methods in addition to the IExtraInfoWidget
 * interface that are used for testing and introspection.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeExtraInfoWidget implements IExtraInfoWidget {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if display() was called, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	private boolean displayed = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of IWidgetClosedListeners listening to this widget.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IWidgetClosedListener> listeners;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	private Form widgetForm = null;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if display() was called, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean widgetDisplayed() {
		// begin-user-code
		return displayed;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#display()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void display() {
		// begin-user-code

		// Set the displayed flag
		displayed = true;
		System.out.println("FakeExtraInfoWidget Message: Displayed.");

		// Immediately notify the listeners that the widget was closed ok so
		// that the test can proceed.
		// Thread updateThread = new Thread(new Runnable() {
		// public void run() {
		if (listeners != null) {
			for (IWidgetClosedListener i : listeners) {
				i.closedOK();
			}
		} else {
			System.out.println("No listeners registered "
					+ "with the FakeExtraInfoWidget!");
			// }
			// });
			// updateThread.start();
		}
		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#setForm(Form form)
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
	 * @see IExtraInfoWidget#getForm()
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
	 * @see IExtraInfoWidget#setCloseListener(IWidgetClosedListener listener)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setCloseListener(IWidgetClosedListener listener) {
		// begin-user-code

		// Add the listener
		if (listeners == null) {
			listeners = new ArrayList<IWidgetClosedListener>();
		}
		listeners.add(listener);

		// end-user-code
	}
}