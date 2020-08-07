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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.awaitility.Awaitility.*;
import java.util.concurrent.TimeUnit;
import org.eclipse.ice.tasks.Task;
import org.eclipse.ice.tasks.TaskException;
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
	 * This test verifies that an exception is thrown when the state data is not
	 * initialized.
	 */
	@Test
	void testConstructionExceptionFromNullData() {

		assertThrows(Exception.class, () -> {
			new Task<TestData>(null);
		});

	}

	/**
	 * This operation verifies that an exception is thrown when the action data is
	 * improperly set.
	 */
	@Test
	void testSetActionDataException() {
		assertThrows(Exception.class, () -> {
			TaskStateData testInputData = new TaskStateDataImplementation();
			Task<TestData> task = new Task<TestData>(testInputData);
			task.setActionData(null);
		});
	}

	/**
	 * This operation verifies that an exception is thrown when the action is
	 * improperly set.
	 */
	@Test
	void testSetActionException() {
		assertThrows(Exception.class, () -> {
			TaskStateData testInputData = new TaskStateDataImplementation();
			Task<TestData> task = new Task<TestData>(testInputData);
			task.setAction(null);
		});
	}

	/**
	 * This function tests basic initialization and state assignment up to the point
	 * of execution, but it does not execute the task.
	 */
	@Test
	void testConstruction() {

		try {
			TaskStateData testInputData = new TaskStateDataImplementation();
			// Set the state to something other than initialized to make sure that the Task
			// initializes into the correct state.
			testInputData.setTaskState(TaskState.FINISHED);

			// Create the task.
			Task<TestData> testTask = new Task<TestData>(testInputData);

			// Check the initial state
			assertEquals(TaskState.INITIALIZED, testTask.getState());

			// Check the initial task state data
			TaskStateData stateData = testTask.getTaskStateData();
			assertNotNull(stateData);
			assertEquals(TaskState.INITIALIZED, stateData.getTaskState());
			// Make sure that the returned state data is a clone
			assert (stateData != testInputData);
			// And otherwise equal
			assert (testInputData.equals(stateData));

			// Initialize it with an action
			TestAction<TestData> action = new TestAction<TestData>();
			testTask.setAction(action);

			// At this point, it should be in the waiting state
			assertEquals(TaskState.WAITING, testTask.getState());

			// Set some test data
			TestData data = new TestDataImplementation();
			testTask.setActionData(data);

			// Check the state once data is set
			assertEquals(TaskState.READY, testTask.getState());

			// Check the action data
			TestData returnedActionData = testTask.getActionData();
			// Make sure the data is equal
			assert (returnedActionData.equals(data));

		} catch (TaskException e1) {
			// In this case, it should fail if an exception is caught.
			e1.printStackTrace();
			fail();
		}

		return;
	}

	/**
	 * This tests the execution of the task in a normal mode of operation.
	 */
	@Test
	void testExecution() {

		try {
			// Create the task. Initialized to null to remove compiler warnings.
			TaskStateData testInputData = new TaskStateDataImplementation();
			Task<TestData> testTask = new Task<TestData>(testInputData);
			TestData data = new TestDataImplementation();
			testTask.setActionData(data);
			TestAction<TestData> action = new TestAction<TestData>();
			testTask.setAction(action);

			action.getType();

			// Execute the task and check the return state. If there was some sort of
			// problem, then it will not be in the EXECUTING state.
			TaskState state = testTask.execute();
			assertEquals(TaskState.EXECUTING,state);

			// The test action is on a thread, so this test needs to wait a bit until the
			// timer runs down and the task enters its FINISHED state. It will hold in
			// EXECUTING, not WAITING, until the thread dies.
			await().atMost(10, TimeUnit.SECONDS).until(() -> (testTask.getState() == TaskState.FINISHED));

			// When execution is complete, it will be in FINISHED in this scenario.
			assertEquals(TaskState.FINISHED, testTask.getState());

			/**
			 * So the SM action needs to do at least three things: 1) Fire an event to go to
			 * the finished state if all does well 2) Fire an error event if it fails 3)
			 * Execute the Task Action.
			 * 
			 * Should the SM action be threaded?
			 * 
			 * The SM action should take the SM as an argument so that it can fire events.
			 */

		} catch (TaskException e1) {
			// In this case, it should fail if an exception is caught.
			e1.printStackTrace();
			fail();
		}

		fail("Not yet implemented");
	}

	/**
	 * This tests the execution of the task with some hooks.
	 */
	@Test
	void testExecutionWithHooks() {

		try {
			TaskStateData testInputData = new TaskStateDataImplementation();
			// Set the state to something other than initialized to make sure that the Task
			// initializes into the correct state.
			testInputData.setTaskState(TaskState.FINISHED);

			// Create the task. Initialized to null to remove compiler warnings.
			Task<TestData> testTask = null;
			testTask = new Task<TestData>(testInputData);

			TestHook<TestData> hook1 = new TestHook<TestData>();
			TestHook<TestData> hook2 = new TestHook<TestData>();

			// Currently adding a hook does nothing and isn't tested. How to test? Move to
			// execution test?

			testTask.addHook(hook1);
			testTask.addHook(hook2);

		} catch (TaskException e1) {
			// In this case, it should fail if an exception is caught.
			e1.printStackTrace();
			fail();
		}

		fail("Not yet implemented");
	}

}
