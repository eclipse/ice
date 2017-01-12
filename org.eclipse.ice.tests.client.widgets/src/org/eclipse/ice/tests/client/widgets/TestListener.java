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
package org.eclipse.ice.tests.client.widgets;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.iclient.uiwidgets.IProcessEventListener;
import org.eclipse.ice.iclient.uiwidgets.IUpdateEventListener;

/**
 * <p>
 * This class implements both the IUpdateEventListener and IProcessEventListener
 * interfaces and is used to test the FormWidget class.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class TestListener implements IProcessEventListener,
		IUpdateEventListener {
	/**
	 * <p>
	 * True if the listener has received an update, false otherwise.
	 * </p>
	 * 
	 */
	private boolean updated;

	/**
	 * <p>
	 * True if the listener has received a process request, false otherwise.
	 * </p>
	 * 
	 */
	private boolean processed;

	/**
	 * This flag is set to true if the test listener received a request to
	 * cancel a process.
	 */
	private boolean cancelled = false;

	/**
	 * <p>
	 * This operation returns true if the listener was updated and false
	 * otherwise.
	 * </p>
	 * 
	 * @return
	 */
	public boolean wasUpdated() {
		return updated;
	}

	/**
	 * <p>
	 * This operation returns true if the listener received a process request
	 * and false otherwise.
	 * </p>
	 * 
	 * @return <p>
	 *         True if a process request was received, false otherwise.
	 *         </p>
	 */
	public boolean wasProcessed() {
		return processed;
	}

	/**
	 * This operation returns true if the listener was notified of a process
	 * cancellation request.
	 * 
	 * @return True if the request was made, false otherwise.
	 */
	public boolean wasCancelled() {
		return cancelled;
	}

	/**
	 * <p>
	 * This operation resets the TestListener by setting the update and process
	 * states to false.
	 * </p>
	 * 
	 */
	public void reset() {

		// Reset the flags
		updated = false;
		processed = false;
		cancelled = false;

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IProcessEventListener#processSelected(Form form, String process)
	 */
	@Override
	public void processSelected(Form form, String process) {

		// Set the process flag
		processed = true;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateEventListener#formUpdated(Form form)
	 */
	@Override
	public void formUpdated(Form form) {

		// Set the update flag
		updated = true;

	}

	@Override
	public void cancelRequested(Form form, String process) {
		cancelled = true;
		return;
	}
}