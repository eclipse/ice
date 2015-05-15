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
 * <p>
 * This is a fake subclass of Item that is used to test the ItemManager and
 * Core.
 * </p>
 * 
 * @author Jay Jay Billings
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
	 * <p>
	 * The process state of the FakeItem. True if processed, false if not.
	 * </p>
	 * 
	 */
	private boolean processed;

	/**
	 * <p>
	 * True if the FakeItem was asked to reload data, false otherwise.
	 * </p>
	 * 
	 */
	private boolean refreshed;

	/**
	 * <p>
	 * True if an attempt was made to cancel an Item process, false otherwise.
	 * </p>
	 */
	private boolean cancelled = false;

	/**
	 * <p>
	 * True if the FakeItem was asked to load an imported input file, false
	 * otherwise.
	 * </p>
	 * 
	 */
	private boolean loaded;

	/**
	 * <p>
	 * True if the FakeItem was successfully updated, false otherwise.
	 * </p>
	 * 
	 */
	private boolean updated = false;

	/**
	 * <p>
	 * This operation returns the process state of the FakeItem.
	 * </p>
	 * 
	 * @return <p>
	 *         The state - true if the Item was processed, false otherwise.
	 *         </p>
	 */
	public boolean wasProcessed() {
		return processed;
	}

	/**
	 * <p>
	 * This operation resets the FakeItem's process state to false.
	 * </p>
	 * 
	 */
	public void reset() {

		processed = false;
		refreshed = false;
		cancelled = false;
		loaded = false;
		updated = false;

		return;
	}

	/**
	 * <p>
	 * This operation returns true if the FakeItem was refreshed by calling
	 * reloadProjectData and false if not.
	 * </p>
	 * 
	 * @return
	 */
	public boolean wasRefreshed() {
		return refreshed;
	}

	/**
	 * <p>
	 * This operation directs the TestItem to notify its listeners so that the
	 * ItemTester can see that the Item super class correctly handles the
	 * request.
	 * </p>
	 * 
	 */
	public void notifyListeners() {
		notifyListenersOfProjectChange();

		return;
	}

	/**
	 * <p>
	 * This operation returns the load state of the FakeItem.
	 * </p>
	 * 
	 * @return <p>
	 *         The state - true if the Item was loaded, false otherwise.
	 *         </p>
	 */
	public boolean wasLoaded() {
		return loaded;
	}

	/**
	 * <p>
	 * This operation returns true if the FakeItem was successfully updated,
	 * false otherwise.
	 * </p>
	 * 
	 * @return <p>
	 *         True if updated, false otherwise.
	 *         </p>
	 */
	public boolean wasUpdated() {
		return updated;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Item#setupForm()
	 */
	protected void setupForm() {

		form = new Form();
		form.setId(1);
		form.setItemID(2); // Set it to 2 so that we can make sure Item.setId
							// works because it should override this!
		form.setName("Harry Potter");
		form.setDescription("The Boy Who Lived");

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Item#reviewEntries(Form preparedForm)
	 */
	protected FormStatus reviewEntries(Form preparedForm) {

		status = FormStatus.InfoError;

		// Set the status to ReadyToProcess if there are two components and
		// false otherwise.
		if (form.getNumberOfComponents() == 2) {
			status = FormStatus.ReadyToProcess;
		}

		return status;

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
	 * <p>
	 * This operation returns the true an attempt was made to cancel a process
	 * request.
	 * </p>
	 * 
	 * @return <p>
	 *         The status of the last call to process().
	 *         </p>
	 */
	public boolean wasCancelled() {

		return cancelled;
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
