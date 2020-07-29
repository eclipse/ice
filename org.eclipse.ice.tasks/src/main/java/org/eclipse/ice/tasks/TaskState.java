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
 * This enumeration describes the set of states for ITasks.
 *
 * @author Jay Jay Billings
 *
 */
public enum TaskState {
	
	/**
	 * This pseudostate indicates that the state machine has fully been 
	 * initialized. This is a short-lived state that while notable primarily
	 * only exists in transition to READY.
	 */
	INITIALIZED,
	
	/**
	 * This state indicates that the task can be executed and that all
     * initialization has completed. It is ready to be executed.
     * 
     * Idempotent tasks may re-enter the ready state when complete, using the
     * FINISHED state as a transition back to readiness. 
	 */
	READY,
	
    /**
     * The state is entered when a task needs to spend a large amount
     * of time to review information received for pre-, post-, or in situ 
     * processing that is required to execute the task. Once the review is 
     * complete, the task will transition into the EXECUTING state in the ideal 
     * primary flow, or wait or fail in alternative flows. The difference
     * between this state and the waiting states is that this state indicates
     * actual work is being performed, i.e. - a review is in progress.
     */
	REVIEWING,
	
	/**
	 * This state indicates that the task is presently executing the work 
	 * assigned to it.
	 */
	EXECUTING,
	
	/**
	 * This state indicates that the task is waiting on internal resources or
	 * events, such as the allocation of compute or data resources or the
	 * completion of other tasks.
	 */
	WAITING,
	
	/**
	 * Tasks in the WaitingForInfo state are waiting on information from an 
	 * external agent. External agents are defined as actors that exist outside
	 * the workflow system, such as humans, file systems that generate events,
	 * or automated systems that provide information to the task.
	 */
	WAITINGFORAGENT,
	
	/**
	 * This is the ideal terminal state for the task and indicates that all
	 * work has completed successfully. It may be possible to re-execute
	 * finished tasks and some idempotent tasks may use FINISHED as a
	 * transitional pseudostate pending a reset to READY.
	 */
	FINISHED,
	
    /**
     * This state indicates that an unexpected failure happened while executing
     * the task.
     */
	FAILED

}
