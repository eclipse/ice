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
package org.eclipse.ice.tests.tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.ice.data.IDataElement;
import org.eclipse.ice.tasks.Task;
import org.eclipse.ice.tasks.TaskState;
import org.eclipse.ice.tasks.TaskStateData;
import org.eclipse.ice.tasks.TaskStateDataImplementation;
import org.junit.jupiter.api.Test;

/**
 * This class tests {@link org.eclipse.ice.tasks.Task}.
 * 
 * @author Jay Jay Billings
 *
 */
class TaskTest {

	/**
	 * This function tests basic initialization and state assignment up to the point
	 * of execution, but it does not execute the task.
	 */
	@Test
	void testConstruction() {

		// Create the task and initialize it with test hooks and an action
		Task<TestData> testTask = new Task<TestData>(new TaskStateDataImplementation());
		TestHook hook1 = new TestHook();
		TestHook hook2 = new TestHook();
		testTask.addHook(hook1);
		testTask.addHook(hook2);
		TestAction action = new TestAction();
		testTask.setAction(action);

		// Check the initial state
		assertEquals(TaskState.INITIALIZED,testTask.getState());

		// Check the initial task state data
		TaskStateData stateData = testTask.getTaskStateData();
		assertNotNull(stateData);
		// FIXME! - Check some more stuff!
		
		// Set some test data
		TestData data = new TestDataImplementation();
		testTask.setActionData(data);

		// Check the state once data is set
		assertEquals(TaskState.READY,testTask.getState());
		
		return;
	}

	@Test
	void testExecution() {
		fail("Not yet implemented");
	}

}


//package org.eclipse.ice.workflow;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.EnumSet;
//
//import org.eclipse.ice.commands.FileHandlerFactory;
//import org.eclipse.ice.commands.IFileHandler;
//import org.springframework.statemachine.StateContext;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.statemachine.action.Action;
//import org.springframework.statemachine.config.StateMachineBuilder;
//import org.springframework.statemachine.config.StateMachineBuilder.Builder;
//
//public class TaskTest {
//
//	static String name = "ICEIII/ice/org.eclipse.ice.workflow/src/main/resources/test1.txt";
//	static String newName = "ICEIII/ice/org.eclipse.ice.workflow/src/main/resources/test2.txt";
//
//	static public StateMachine<WorkflowEngine.States, WorkflowEngine.Events> buildMachine()
//			throws Exception {
//		Builder<WorkflowEngine.States, WorkflowEngine.Events> builder = new StateMachineBuilder.Builder<>();
//
//		builder.configureStates().withStates()
//				.initial(WorkflowEngine.States.INITIALIZED)
//				.states(EnumSet.allOf(WorkflowEngine.States.class));
//
//		builder.configureTransitions().withExternal()
//				.source(WorkflowEngine.States.INITIALIZED)
//				.target(WorkflowEngine.States.EXECUTING)
//				.event(WorkflowEngine.Events.PARAMETERS_RECEIVED).and()
//				.withExternal().source(WorkflowEngine.States.EXECUTING)
//				.target(WorkflowEngine.States.FINISHED)
//				.action(new Action<WorkflowEngine.States, WorkflowEngine.Events>() {
//
//					@Override
//					public void execute(
//							StateContext<WorkflowEngine.States, WorkflowEngine.Events> context) {
//						try {
//							moveFile(name, newName);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}).event(WorkflowEngine.Events.EXECUTION_COMPLETE);
//
//		return builder.build();
//	}
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
