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
 * <p>
 * The FakeFormWidget is a realization of IFormWidget that is used for testing.
 * It provides several methods in addition to the IFormWidget interface that are
 * used for testing and introspection.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class FakeFormWidget implements IFormWidget {
	/**
	 * <p>
	 * Boolean to signify if a listener was registered.
	 * </p>
	 * 
	 */
	private boolean observed;
	/**
	 * <p>
	 * Boolean to store the display state.
	 * </p>
	 * 
	 */
	private boolean displayed;

	/**
	 * <p>
	 * The Form.
	 * </p>
	 * 
	 */
	private Form widgetForm;

	/**
	 * <p>
	 * The list of IUpdateEventListeners
	 * </p>
	 * 
	 */
	private ArrayList<IUpdateEventListener> updateListeners;
	/**
	 * <p>
	 * The list of IProcessEventListeners
	 * </p>
	 * 
	 */
	private ArrayList<IProcessEventListener> processListeners;

	/**
	 * <p>
	 * This operation returns true if setItem() was called on the FakeFormWidget
	 * with a non-null Form.
	 * </p>
	 * 
	 * @return
	 */
	public boolean formRegistered() {

		// Return true if the Form is not null, otherwise false
		if (this.widgetForm != null) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * This operation returns true if a UIWidgetListener is registered for the
	 * FakeFormWidget.
	 * </p>
	 * 
	 * @return
	 */
	public boolean listenerRegistered() {
		return this.observed;
	}

	/**
	 * <p>
	 * This operation returns true if the display operation was previously
	 * called for the FakeFormWidget.
	 * </p>
	 * 
	 * @return
	 */
	public boolean widgetDisplayed() {
		return this.displayed;
	}

	/**
	 * <p>
	 * This operation implements display() from UIWidget with a simple pass
	 * through that makes whether or not the method was called. Nothing is drawn
	 * on the screen.
	 * </p>
	 * 
	 */
	public void display() {

		this.displayed = true;

		return;

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
	 */
	public void registerProcessListener(IProcessEventListener listener) {

		// Store the listener
		if (processListeners == null) {
			processListeners = new ArrayList<IProcessEventListener>();
		}
		processListeners.add(listener);

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#registerResourceProvider(ISimpleResourceProvider
	 *      provider)
	 */
	public void registerResourceProvider(ISimpleResourceProvider provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#notifyUpdateListeners()
	 */
	public void notifyUpdateListeners() {

		for (IUpdateEventListener i : updateListeners) {
			i.formUpdated(widgetForm);
		}
		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#notifyProcessListeners(String process)
	 */
	public void notifyProcessListeners(String process) {

		for (IUpdateEventListener i : updateListeners) {
			i.formUpdated(widgetForm);
		}
		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IObservableWidget#notifyCancelListeners(String process)
	 */
	public void notifyCancelListeners(String process) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormWidget#setForm(Form form)
	 */
	public void setForm(Form form) {

		widgetForm = form;

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormWidget#getForm()
	 */
	public Form getForm() {
		return widgetForm;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormWidget#updateStatus(String statusMessage)
	 */
	public void updateStatus(String statusMessage) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormWidget#disable(boolean state)
	 */
	public void disable(boolean state) {
		// TODO Auto-generated method stub

	}

}