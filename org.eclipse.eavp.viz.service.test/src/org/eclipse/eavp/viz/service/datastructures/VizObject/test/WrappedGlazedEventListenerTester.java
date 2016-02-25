/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay 
 *   Billings
 *******************************************************************************/
package org.eclipse.eavp.viz.service.datastructures.VizObject.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.WrappedGlazedEventListener;
import org.junit.Test;

/**
 * This class is responsible for testing the WrappedGlazedEventListener.
 * 
 * It realizes the Component and IUpdateableListener interfaces, but only
 * provides implementations where needed to check the update.
 * 
 * @author Jay Jay Billings
 *
 */
public class WrappedGlazedEventListenerTester implements IVizUpdateable, IVizUpdateableListener {

	/**
	 * The flag that indicates the test was notified
	 */
	boolean updated = false;

	/**
	 * This indicates that the component that allegedly changed is the test.
	 */
	boolean isThis = false;

	/**
	 * This operation performs the update test. It just creates a
	 * WrappedGlazedEventListener, registers the test as the IUpdateableListener
	 * and Component and then make sure it receives and update when the wrapped
	 * listener is notified.
	 */
	@Test
	public void checkUpdates() {
		// Create the WrappedGlazedEventListener and give have it post updates
		// to the test class.
		WrappedGlazedEventListener listener = new WrappedGlazedEventListener(this, this);

		// Call the update
		listener.listChanged(null);

		// Make sure we received the message
		assertTrue(updated);
		assertTrue(isThis);

		return;
	}

	@Override
	public Object clone() {
		return null;
	}

	@Override
	public void update(String updatedKey, String newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void register(IVizUpdateableListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister(IVizUpdateableListener listener) {
		// TODO Auto-generated method stub

	}

	/**
	 * This operation sets some flags for the test when it receives the update
	 * notification.
	 */
	@Override
	public void update(IVizUpdateable component) {
		updated = true;
		isThis = (this == component);
	}

}
