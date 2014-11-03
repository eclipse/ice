/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.reactoreditor.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.eclipse.ice.client.widgets.reactoreditor.SelectionProvider;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Test;

/**
 * Tests the SelectionProvider class from the ReactorEditor.
 * 
 * @author djg
 * 
 */
public class SelectionProviderTester {

	/**
	 * Checks the ISelectionProvider implementation in SelectionProvider.
	 */
	@Test
	public void checkISelectionProvider() {

		// Create a SelectionProvider to test.
		SelectionProvider provider = new SelectionProvider();

		// Create some listeners.
		SelectionChangedListener listener1 = new SelectionChangedListener();
		SelectionChangedListener listener2 = new SelectionChangedListener();

		// The initial selection should be null.
		assertNull(provider.getSelection());

		// Add a listener.
		provider.addSelectionChangedListener(listener1);

		// Set a selection.
		ISelection selection = new StructuredSelection(0);
		provider.setSelection(selection);

		// The listener should have been notified.
		assertTrue(listener1.selectionChanged());
		assertFalse(listener2.selectionChanged());

		// Check the current selection.
		assertSame(selection, provider.getSelection());

		// Try it again with a new selection.
		selection = new StructuredSelection(2);
		provider.setSelection(selection);

		// The listener should have been notified.
		assertTrue(listener1.selectionChanged());
		assertFalse(listener2.selectionChanged());

		// Check the current selection.
		assertSame(selection, provider.getSelection());

		// Now add a second listener.
		provider.addSelectionChangedListener(listener2);

		// Set a selection.
		selection = new ISelection() {
			public boolean isEmpty() {
				return false;
			}

		};
		provider.setSelection(selection);

		// Both listeners should have been notified.
		assertTrue(listener1.selectionChanged());
		assertTrue(listener2.selectionChanged());

		// Check the current selection.
		assertSame(selection, provider.getSelection());

		// Try setting the same selection. No notification should occur.
		provider.setSelection(selection);

		// Neither listeners should have been notified.
		assertFalse(listener1.selectionChanged());
		assertFalse(listener2.selectionChanged());

		// Check the current selection.
		assertSame(selection, provider.getSelection());

		// Now remove a listener.
		provider.removeSelectionChangedListener(listener1);

		// Set a selection.
		selection = new ISelection() {
			public boolean isEmpty() {
				return false;
			}
		};
		provider.setSelection(selection);

		// Only one listener should have been notified.
		assertFalse(listener1.selectionChanged());
		assertTrue(listener2.selectionChanged());

		// Check the current selection.
		assertSame(selection, provider.getSelection());

		return;
	}

	/**
	 * This class provides a very basic implementation of
	 * ISelectionChangedListener. It allows the test class to see if the
	 * listener was updated.
	 * 
	 * @author djg
	 * 
	 */
	private class SelectionChangedListener implements ISelectionChangedListener {

		/**
		 * Whether or not the selection was changed.
		 */
		private boolean changed = false;

		/**
		 * Gets whether or not the selection was changed. This listener
		 * automatically resets after being called.
		 * 
		 * @return Whether or not the selection was changed.
		 */
		public boolean selectionChanged() {
			// Reset the boolean and return true if it was in fact changed.
			if (changed) {
				changed = false;
				return true;
			}
			return false;
		}

		/* ---- Implements ISelectionChangedListener. ---- */
		public void selectionChanged(SelectionChangedEvent event) {

			// Make sure the selection event isn't null!
			assertNotNull(event);

			// Update the boolean.
			changed = true;

			return;
		}
		/* ----------------------------------------------- */
	}
}
