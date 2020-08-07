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

import java.util.EnumSet;

import org.eclipse.ice.data.IDataElement;
import org.eclipse.ice.tasks.spring.statemachine.ExecutingEventAction;
import org.eclipse.ice.tasks.spring.statemachine.ExecutingStateAction;
import org.eclipse.ice.tasks.spring.statemachine.FinishedEventAction;
import org.eclipse.ice.tasks.spring.statemachine.InitializedStateAction;
import org.eclipse.ice.tasks.spring.statemachine.ReadyEventAction;
import org.eclipse.ice.tasks.spring.statemachine.WaitingEventAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;

/**
 * The basic implementation of ITask.
 * 
 * @author Jay Jay Billings
 *
 */
public class Task<T extends IDataElement<T>> implements ITask<T> {

	/**
	 * Error message for erroneous construction
	 */
	public static final String CONSTRUCTION_ERR = "Task state data cannot be null";

	/**
	 * Error message for erroneous action data set
	 */
	public static final String ACTION_DATA_SET_ERR = "Action data cannot be null.";

	/**
	 * Error message for erroneous action set
	 */
	public static final String ACTION_SET_ERR = "The action cannot be null.";

	/**
	 * The state machine used to manage the task's state and transitions.
	 */
	protected StateMachine<TaskState, TaskTransitionEvents> stateMachine;

	/**
	 * Logging tool
	 */
	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	/**
	 * State data stored about this task
	 */
	protected TaskStateData stateData;

	/**
	 * Data used by the action
	 */
	protected T actionData;

	/**
	 * The action
	 */
	protected org.eclipse.ice.tasks.Action<T> action;

	/**
	 * Utility function for throwing exceptions
	 * 
	 * @param msg error message to go with the exception and in the log
	 * @throws TaskException
	 * @throws Exception     the exception
	 */
	private void throwErrorException(String msg) throws TaskException {
		TaskException exception = new TaskException(msg);
		logger.error(msg, exception);
		throw (exception);
	}

	/**
	 * Constructor
	 * 
	 * @param stateData the task state data must be provided on initialization
	 *                  because tasks do not manage any data. See
	 *                  {@link org.eclipse.ice.tasks.ITask}. Construction will fail
	 *                  without a valid state data structure.
	 * @throws TaskException
	 */
	public Task(TaskStateData taskStateData) throws TaskException {

		// Check state data
		if (taskStateData != null) {
			stateData = taskStateData;
		} else {
			throwErrorException(CONSTRUCTION_ERR);
		}

		// Initialize the state machine
		buildStateMachine();
		stateMachine.start();

		logger.info("State data set and state machine initialized. " + "Ready to rock and roll.");

	}

	@Override
	public void setActionData(T taskActionData) throws TaskException {

		if (taskActionData != null) {

			// Store the data
			actionData = taskActionData;

			// The state machine can only get into the waiting state if the action or the
			// action data are set, so check for that and if so move on to the ready state.
			TaskTransitionEvents event;
			if (getState().equals(TaskState.WAITING)) {
				event = TaskTransitionEvents.ALL_INFO_SET;
			} else {
				// Otherwise wait. Note that event here is different than in setAction().
				event = TaskTransitionEvents.ACTION_DATA_SET;
			}

			// Once the action data is set, the task should wait
			stateMachine.sendEvent(event);
		} else {
			throwErrorException(ACTION_DATA_SET_ERR);
		}

	}

	@Override
	public T getActionData() {
		return actionData;
	}

	@Override
	public void setAction(org.eclipse.ice.tasks.Action<T> taskAction) throws TaskException {

		// Make sure the error is not null before updating the states
		if (taskAction != null) {

			// Store the action
			action = taskAction;
			TaskTransitionEvents event;
			// The state machine can only get into the waiting state if the action or the
			// action data are set, so check for that and if so move on to the ready state.
			if (getState().equals(TaskState.WAITING)) {
				event = TaskTransitionEvents.ALL_INFO_SET;
			} else {
				// Otherwise wait. Note that the event here is different than in
				// setActionData().
				event = TaskTransitionEvents.ACTION_SET;
			}

			// Throw the event to wait or get ready.
			stateMachine.sendEvent(event);
		} else {
			throwErrorException(ACTION_SET_ERR);
		}

	}

	@Override
	public void addHook(Hook<T> hook) {
		// TODO Auto-generated method stub

	}

	@Override
	public TaskState execute() {
		// Execute the action. Note that there is a transition event tied to the state
		// event for execution. The transition event changes the state to EXECUTING
		// while the state event makes some more informed decisions.
		stateMachine.sendEvent(TaskTransitionEvents.EXECUTION_TRIGGERED);
		return getState();
	}

	@Override
	public TaskState cancel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskState getState() {
		return stateData.getTaskState();
	}

	@Override
	public TaskStateData getTaskStateData() {
		return (TaskStateData) stateData.clone();
	}

	/**
	 * This operation builds the state machine used to manage states and transitions
	 * for Tasks.
	 * 
	 * @throws TaskException
	 * 
	 * @throws Exception     thrown in the state machine can not be correctly
	 *                       assembled.
	 */
	private void buildStateMachine() throws TaskException {

		try {
			// Setup the state machine builder
			Builder<TaskState, TaskTransitionEvents> builder = new StateMachineBuilder.Builder<>();

			// Go into the intial state and add the others states to the set
			builder.configureStates().withStates().initial(TaskState.INITIALIZED, new InitializedStateAction(stateData))
					.states(EnumSet.allOf(TaskState.class));

			// Configure the transitions for action data and actions.
			builder.configureTransitions().withExternal().source(TaskState.INITIALIZED).target(TaskState.WAITING)
					.event(TaskTransitionEvents.ACTION_DATA_SET).action(new WaitingEventAction(stateData));
			builder.configureTransitions().withExternal().source(TaskState.INITIALIZED).target(TaskState.WAITING)
					.event(TaskTransitionEvents.ACTION_SET).action(new WaitingEventAction(stateData));

			// Once all the info is set, the state machine can transition to READY
			builder.configureTransitions().withExternal().source(TaskState.WAITING).target(TaskState.READY)
					.event(TaskTransitionEvents.ALL_INFO_SET).action(new ReadyEventAction(stateData));

			// A call to execute() triggers execution. This requires a transition action...
			builder.configureTransitions().withExternal().source(TaskState.READY).target(TaskState.EXECUTING)
					.event(TaskTransitionEvents.EXECUTION_TRIGGERED).action(new ExecutingEventAction(stateData));
			// ...and a state action
			builder.configureStates().withStates().state(TaskState.EXECUTING, new ExecutingStateAction(stateData));

			// FIXME! CONFIRM THAT THIS IS WORKING!---^
			
			// If the execution finished as expected, there is a transition event for that
			// to finalize the state.
			builder.configureTransitions().withExternal().source(TaskState.EXECUTING).target(TaskState.FINISHED)
					.event(TaskTransitionEvents.EXECUTION_FINISHED).action(new FinishedEventAction(stateData));

			// Pack it up and go on home
			stateMachine = builder.build();

		} catch (Exception e) {
			throwErrorException("Unable to build state machine!");
		}
	}

}
