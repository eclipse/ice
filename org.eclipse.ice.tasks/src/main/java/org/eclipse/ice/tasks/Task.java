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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.io.File;
//import java.io.IOException;
//import java.util.EnumSet;
//
//import org.eclipse.ice.commands.FileHandlerFactory;
//import org.eclipse.ice.commands.IFileHandler;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
////
//public class TaskTest {
//
//	static String name = "ICEIII/ice/org.eclipse.ice.workflow/src/main/resources/test1.txt";
//	static String newName = "ICEIII/ice/org.eclipse.ice.workflow/src/main/resources/test2.txt";
//
//	static 
//
//	public static void moveFile(final String filePath, final String newFilePath)
//			throws IOException {
//
//		String home = System.getProperty("user.home");
//		String separator = String.valueOf(File.separatorChar);
//		String homePath = home + separator;
//
//		FileHandlerFactory factory = new FileHandlerFactory();
//		IFileHandler fileHandler = factory.getFileHandler();
//		fileHandler.move(homePath + filePath, homePath + newFilePath);
//		return;
//	}
//
//	public static void main(String[] args) throws Exception {
//
//		StateMachine<WorkflowEngine.States, WorkflowEngine.Events> taskProcessor = buildMachine();
//		taskProcessor.start();
//		taskProcessor.sendEvent(WorkflowEngine.Events.PARAMETERS_RECEIVED);
//		taskProcessor.sendEvent(WorkflowEngine.Events.EXECUTION_COMPLETE);
//		System.out.println(taskProcessor.getState());
//
//		return;
//	}
//
//}
//
//

/**
 * The basic implementation of ITask.
 * 
 * @author Jay Jay Billings
 *
 */
public class Task<T extends IDataElement<T>> implements ITask<T> {

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
	 * Constructor
	 * 
	 * @param stateData the task state data must be provided on initialization
	 *                  because tasks do not manage any data. See
	 *                  {@link org.eclipse.ice.tasks.ITask}. Construction will fail
	 *                  without a valid state data structure.
	 */
	public Task(TaskStateData taskStateData) throws RuntimeException {

		if (taskStateData != null) {
			stateData = taskStateData;
		} else {
			String errMsg = "Cannot create task without state data!";
			RuntimeException exception = new RuntimeException(errMsg);
			logger.error(errMsg, exception);
			throw (exception);
		}

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
	public void setAction(org.eclipse.ice.tasks.Action<T> taskAction) {
		// TODO Auto-generated method stub
		org.springframework.statemachine.action.Action smAction;
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
	 * @throws Exception thrown in the state machine can not be correctly assembled.
	 */
	private void buildStateMachine() throws Exception {
		Builder<TaskState, TaskTransitionEvents> builder = new StateMachineBuilder.Builder<>();

//		builder.configureStates().withStates().initial(WorkflowEngine.States.INITIALIZED)
//				.states(EnumSet.allOf(WorkflowEngine.States.class));
//
//		builder.configureTransitions().withExternal().source(WorkflowEngine.States.INITIALIZED)
//				.target(WorkflowEngine.States.EXECUTING).event(WorkflowEngine.Events.PARAMETERS_RECEIVED).and()
//				.withExternal().source(WorkflowEngine.States.EXECUTING).target(WorkflowEngine.States.FINISHED)
//				.action(new Action<WorkflowEngine.States, WorkflowEngine.Events>() {
//
//					@Override
//					public void execute(StateContext<WorkflowEngine.States, WorkflowEngine.Events> context) {
//						try {
//							moveFile(name, newName);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}).event(WorkflowEngine.Events.EXECUTION_COMPLETE);
//
//		stateMachine = builder.build();

		return;
	}

}
