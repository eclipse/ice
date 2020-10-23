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
		// Only execute if the information is available
		boolean dataGood = context != null && actionData.get() != null && taskAction.get() != null
				&& stateData.get() != null;
		if (dataGood) {
			// Create and launch the thread
			Thread thread = new Thread(this);
			thread.start();
		} else {
			// Otherwise fail hard
			fail();
		}
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

		logger.get().info("Executing action of type {}", taskAction.get().getType());

		// Try to execute the action
		T data = actionData.get();
		if (taskAction.get().run(data)) {
			finish();
		} else {
			// Fail if there's an error
			fail();
		}

		logger.get().info("Task executed. State = {}", stateData.get().getTaskState());
	}

	/**
	 * Finish successfully
	 */
	private void finish() {
		// Trigger the state transition
		stateData.get().setTaskState(TaskState.FINISHED);
		stateContext.get().getStateMachine().sendEvent(TaskTransitionEvents.EXECUTION_FINISHED);
	}

	/**
	 * Fail miserably. Okay, gracefully...
	 */
	private void fail() {
		// Failure may mean that there is a problem with state data
		if (stateData.get() != null) {
			stateData.get().setTaskState(TaskState.FAILED);
		}
		// Or the state context
		if (stateContext.get() != null) {
			stateContext.get().getStateMachine().sendEvent(TaskTransitionEvents.ERROR_CAUGHT);
		}

	}

}
