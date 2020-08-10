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

import org.eclipse.ice.tasks.Action;
import org.eclipse.ice.tasks.TaskState;
import org.eclipse.ice.tasks.TaskStateData;
import org.eclipse.ice.tasks.TaskTransitionEvents;
import org.springframework.statemachine.StateContext;

/**
 * This is a state machine action that executes after the task enters the
 * EXECUTING state. The task executes this action *AFTER* the task enters the
 * state.
 * 
 * This class requires that the state data, action data, and action be set
 * before the execute() operation is called.
 * 
 * @author Jay Jay Billings
 *
 */
public class ExecutingStateAction<T> extends StateMachineBaseAction<T> implements Runnable {

	/**
	 * Spring state machine context used during execution
	 */
	protected AtomicReference<StateContext<TaskState, TaskTransitionEvents>> stateContext;

	/**
	 * Constructor
	 * 
	 * @param taskStateData state data
	 */
	public ExecutingStateAction(TaskStateData taskStateData) {
		super(taskStateData);
		stateContext = new AtomicReference<>(null);
	}

	/**
	 * This operation executes the ICE action for the parent task. The action is
	 * launched on it's own thread.
	 */
	@Override
	public void execute(StateContext<TaskState, TaskTransitionEvents> context) {
		// Update the state context
		stateContext.set(context);
		// Create and launch the thread
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * This is the main operation for executing the Task's Action and is a Runnable
	 * meant to be called on a thread. In principle, this can be called directly by
	 * a client using a Thread, but that misses additional activities preformed by
	 * execute() and that operation is the preferred entry point for the execution
	 * of this work.
	 */
	@Override
	public void run() {
		
		// Default to failure
		TaskState state = TaskState.FAILED;
		TaskTransitionEvents event = TaskTransitionEvents.ERROR_CAUGHT;
		
		// Try to execute the action
		T data = actionData.get();
		if (taskAction.get().run(data)) {
			state = TaskState.FINISHED;
			event = TaskTransitionEvents.EXECUTION_FINISHED;
		}
		
		// Trigger the state transition and update the state and log
		stateContext.get().getStateMachine().sendEvent(event);
		stateData.get().setTaskState(state);
		logger.get().info("Task executed. State = {}", state);
	}

}
