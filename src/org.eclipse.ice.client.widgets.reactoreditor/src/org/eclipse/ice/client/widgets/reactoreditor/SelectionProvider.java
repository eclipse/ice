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
package org.eclipse.ice.client.widgets.reactoreditor;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * This is a basic implementation of ISelectionProvider. This class is used in
 * the ReactorEditor to provide a way for widgets, specifically IAnalysisViews,
 * to push properties for the currently selected object to the
 * IWorkbenchPartSite, which in turn will fill the ICE Properties View.<br>
 * <br>
 * To set the current selection that will have its properties displayed, use
 * SelectionProvider.setSelection() and pass in a new StructuredSelection
 * constructed from an IPropertySource instance.<br>
 * <br>
 * For example:<br>
 * <br>
 * <code>
 * SelectionProvider provider = new SelectionProvider();<br>
 * IPropertySource object = new IPropertySourceImplementation();<br>
 * provider.setSelection(new StructuredSelection(object));<br>
 * </code> <br>
 * For the provider to push properties to the ICE Properties View, it must be
 * set for the IWorkbenchPartSite. For the ReactorEditor, this is done in
 * ReactorFormEditor with the call <code>
 * this.getSite().setSelectionProvider(provider);</code>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class SelectionProvider implements ISelectionProvider {

	/**
	 * The current selection. Objects that are selected should implement the
	 * IPropertySource if they would like to provide properties.
	 */
	private ISelection selection;

	/**
	 * A thread-safe list of listeners that holds ISelectionChangedListeners.
	 */
	private ListenerList listeners = new ListenerList();

	/* ---- Implements ISelectionProvider. ---- */
	/**
	 * Implements addSelectionChangedListener for ISelectionProvider. Adds an
	 * ISelectionChangedListener to listen for SelectionChangedEvents.
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		// Add the listener if it is valid.
		if (listener != null) {
			listeners.add(listener);
		}
		return;
	}

	/**
	 * Implements getSelection for ISelectionProvider. Returns the current
	 * ISelection, which, in this implementation, should always be a
	 * StructuredSelection.
	 */
	public ISelection getSelection() {
		// Return the current selection (may be null).
		return selection;
	}

	/**
	 * Implements removeSelectionChangedListener for ISelectionProvider. Removes
	 * the listener from the List of listeners.
	 */
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		// Remove the listener if it is valid.
		if (listener != null) {
			listeners.remove(listener);
		}
		return;
	}

	/**
	 * Implements setSelection for ISelectionProvider. Sets the current
	 * ISelection, which should be a StructuredSelection.
	 */
	public void setSelection(ISelection selection) {

		// Check the parameter.
		if (selection == null || selection.equals(this.selection)) {
			return;
		}

		// Update the selection.
		this.selection = selection;

		// Create a SelectionChangedEvent.
		SelectionChangedEvent event = new SelectionChangedEvent(this, selection);

		// Notify all of the listeners.
		Object[] list = listeners.getListeners();
		for (int i = 0; i < list.length; i++) {
			((ISelectionChangedListener) list[i]).selectionChanged(event);
		}

		return;
	}
	/* ---------------------------------------- */
}
