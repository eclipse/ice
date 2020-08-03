/******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *****************************************************************************/
package org.eclipse.ice.tasks;

import org.eclipse.ice.data.IDataElement;

/**
 * The basic implementation of ITask.
 * 
 * @author Jay Jay Billings
 *
 */
public class Task<T extends IDataElement<T>> implements ITask<T> {

	/**
	 * Constructor
	 * 
	 * @param stateData the task state data must be provided on initialization
	 *                  because tasks do not manage any data. See
	 *                  {@link org.eclipse.ice.tasks.ITask}.
	 */
	public Task(TaskStateData stateData) {

	}

	@Override
	public void setActionData(T actionData) {
		// TODO Auto-generated method stub

	}

	@Override
	public T getActionData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAction(Action<T> taskAction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addHook(Hook<T> hook) {
		// TODO Auto-generated method stub

	}

	@Override
	public TaskState execute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskState cancel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskStateData getTaskStateData() {
		// TODO Auto-generated method stub
		return null;
	}

}
