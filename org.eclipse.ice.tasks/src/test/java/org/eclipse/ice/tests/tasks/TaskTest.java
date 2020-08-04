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
	 * This function tests basic initialization and state assignment up to the 
	 * point of execution, but it does not execute the task.
	 */
	@Test
	void testConstruction() {
		
		// Test incorrect construction without state data
		try {
			Task<TestData> errorTask = new Task<TestData>(null);
			// Fail if no exception is caught.
			fail("Expected runtime error for null task state data was not "
					+ "caught.");
		} catch (RuntimeException e) {
			// Nothing do to. The exception was caught as expected.
		}

		// Create the task
		Task<TestData> testTask = new Task<TestData>(new TaskStateDataImplementation());
		// Check the initial state
		assertEquals(TaskState.INITIALIZED,testTask.getState());
		
		// Check the initial task state data
		TaskStateData stateData = testTask.getTaskStateData();
		assertNotNull(stateData);
		// FIXME! - Check some more stuff!
		
		// Initialize it with test hooks and an action
		TestHook<TestData> hook1 = new TestHook<TestData>();
		TestHook<TestData> hook2 = new TestHook<TestData>();
		testTask.addHook(hook1);
		testTask.addHook(hook2);
		TestAction<TestData> action = new TestAction<TestData>();
		testTask.setAction(action);

		// At this point, it should be in the waiting state
		assertEquals(TaskState.WAITING,testTask.getState());
		
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
