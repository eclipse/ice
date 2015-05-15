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
 * <p>
 * The FakeInfoWidget is a realization of IExtraInfoWidget that is used for
 * testing. It provides several methods in addition to the IExtraInfoWidget
 * interface that are used for testing and introspection.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class FakeExtraInfoWidget implements IExtraInfoWidget {
	/**
	 * <p>
	 * True if display() was called, false otherwise.
	 * </p>
	 */
	private boolean displayed = false;

	/**
	 * <p>
	 * The list of IWidgetClosedListeners listening to this widget.
	 * </p>
	 * 
	 */
	private ArrayList<IWidgetClosedListener> listeners;

	/**
	 * <p>
	 * The Form.
	 * </p>
	 */
	private Form widgetForm = null;

	/**
	 * <p>
	 * This operation returns true if display() was called, false otherwise.
	 * </p>
	 * 
	 * @return
	 */
	public boolean widgetDisplayed() {
		return displayed;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#display()
	 */
	public void display() {

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

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#setForm(Form form)
	 */
	public void setForm(Form form) {

		widgetForm = form;

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#getForm()
	 */
	public Form getForm() {

		return widgetForm;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IExtraInfoWidget#setCloseListener(IWidgetClosedListener listener)
	 */
	public void setCloseListener(IWidgetClosedListener listener) {

		// Add the listener
		if (listeners == null) {
			listeners = new ArrayList<IWidgetClosedListener>();
		}
		listeners.add(listener);

	}
}