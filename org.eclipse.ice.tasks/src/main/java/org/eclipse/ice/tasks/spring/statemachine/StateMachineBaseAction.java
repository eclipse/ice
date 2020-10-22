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
package org.eclipse.ice.tasks.spring.statemachine;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.tasks.TaskState;
import org.eclipse.ice.tasks.TaskStateData;
import org.eclipse.ice.tasks.TaskTransitionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.action.Action;

/**
 * This is an abstract base class for State Machine Actions used by Tasks. It
 * stores members in thread safe AtomicReferences.
 * 
 * Note that there is a difference between state machine actions and Task
 * Actions. The former is a construct used by the Spring State Machine library
 * and describes events attached to events or states in a state machine. The
 * latter specifically refers to Actions executed as part of Tasks in the
 * workflow model of Billings, 2019.
 * 
 * @author Jay Jay Billings
 *
 */
public abstract class StateMachineBaseAction<T> implements Action<TaskState, TaskTransitionEvents> {

	/**
	 * Logging tool
	 */
	protected final AtomicReference<Logger> logger;

	/**
	 * Task state data for subclasses stored in an atomic reference for thread
	 * safety.
	 */
	protected AtomicReference<TaskStateData> stateData;

	/**
	 * The ICE action executed by the Task. This is an optional member that may be
	 * supplied to subclasses that require it, so see their documentation.
	 */
	protected AtomicReference<org.eclipse.ice.tasks.Action<T>> taskAction;

	/**
	 * The ICE action data needed to execute the task. This is an optional member
	 * that may be supplied to subclasses that require it, so see their
	 * documentation.
	 */
	protected AtomicReference<T> actionData;

	/**
	 * Constructor
	 * 
	 * @param the state data from the Task
	 */
	public StateMachineBaseAction(TaskStateData taskStateData) {
		logger = new AtomicReference<>(LoggerFactory.getLogger(this.getClass()));
		stateData = new AtomicReference<>(taskStateData);
		taskAction = new AtomicReference<>();
		actionData = new AtomicReference<>();
	}

	/**
	 * This operation sets the action data used to execution the Task's action
	 * 
	 * @param actionData
	 */
	public void setActionData(T actionData) {
		this.actionData.set(actionData);
	}

	public void setTaskAction(org.eclipse.ice.tasks.Action<T> action) {
		taskAction.set(action);
	}

}
