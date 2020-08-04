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
 * This enumeration describes the possible events that cause transitions
 * between task states.
 * 
 * @author Jay Jay Billings
 *
 */
public enum TaskTransitionEvents {

	/**
	 * This event indicatese that the action data has been set for the task.
	 */
	ACTION_DATA_SET,
	
	/**
	 * This event indicates that the action for the task has been set.
	 */
	ACTION_SET,
	
	/**
	 * This event indicates that all required information has been specified.
	 */
	ALL_INFO_SET,
	
	/**
	 * This event indicates that the task has been directed to execute it's
	 * action.
	 */
	EXECUTION_TRIGGERED,
	
	/**
	 * This event indicates that the task should reset to the READY state from
	 * the FINISHED state.
	 */
	RESET,
	
	/**
	 * This event indicates that the task's execution should be canceled.
	 */
	CANCELED,
	
	/**
	 * This event indicates that the cancellation of the task's execution 
	 * failed.
	 */
	CANCEL_FAILED,
	
	/**
	 * This event indicates that the action has requested additional 
	 * information to complete it's execution.
	 */
	INFO_REQUESTED,
	
	/**
	 * This event indicates that additional information previously requested
	 * was received.
	 */
	INFO_RECEIVED,
	
	/**
	 * This event indicates that execution finished without error at least as
	 * far as the Task is concerned. (Deeper issues beyond the Task's control
	 * could cause errors that are masked and reported as successful
	 * execution.)
	 */
	EXECUTION_FINISHED,
	
	/**
	 * This event indicates that an error has been caught during execution.
	 */
	ERROR_CAUGHT,
	
	/**
	 * This event indicates that a timeout occurred while waiting on a process
	 * to complete or a request for more information.
	 */
	TIMEOUT_OCCURRED
	
}
