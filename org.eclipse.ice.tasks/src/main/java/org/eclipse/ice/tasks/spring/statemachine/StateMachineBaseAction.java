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

import org.eclipse.ice.tasks.TaskState;
import org.eclipse.ice.tasks.TaskStateData;
import org.eclipse.ice.tasks.TaskTransitionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.action.Action;

/**
 * This is an abstract base class for State Machine Actions used by Tasks.
 * 
 * @author Jay Jay Billings
 *
 */
public abstract class StateMachineBaseAction implements Action<TaskState, TaskTransitionEvents> {

	/**
	 * Logging tool
	 */
	protected final Logger logger;
	
	/**
	 * Task state data for subclasses
	 */
	protected TaskStateData stateData;
	
	/**
	 * Constructor
	 * @param the state data from the Task
	 */
	public StateMachineBaseAction(TaskStateData taskStateData) {
		logger = LoggerFactory.getLogger(this.getClass());
		stateData = taskStateData;
	}
	
}
