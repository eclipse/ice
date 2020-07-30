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

/**
 * Actions can be executed by Tasks and represent fundamental functional
 * units.
 * 
 * Actions have distinct types described by the {@link ActionType} enumeration.
 * 
 * Actions store state data externally and do not control their own storage. 
 * Thus the must be configured when built to store state to the proper 
 * location. State controllers should be provided by Tasks executing an action,
 * but Actions will attempt to store state locally by default if no controller
 * is provided. State storage is separate from data storage, which is tracked by
 * individual data models.
 * 
 * @author Jay Jay Billings
 *
 */
public interface Action<T> {

	/**
	 * This operation returns the type of the Action.
	 * @return the type of the Action
	 */
	public ActionType getType();
	
	/**
	 * This operation executes the Action on the provided input data.
	 * @param data the input data used by the action
	 * @return true if the Action executes successfully, false otherwise.
	 */
	public boolean run(T data);
	
}
