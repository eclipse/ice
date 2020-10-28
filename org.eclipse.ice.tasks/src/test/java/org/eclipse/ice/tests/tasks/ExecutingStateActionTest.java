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

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;
import org.eclipse.ice.tasks.TaskState;
import org.eclipse.ice.tasks.TaskStateData;
import org.eclipse.ice.tasks.TaskStateDataImplementation;
import org.eclipse.ice.tasks.spring.statemachine.ExecutingStateAction;
import org.junit.jupiter.api.Test;

/**
 * This tests the basic ability of the {@link ExecutingStateAction} to set the
 * state correctly. Most of correct execution is tested in {@link TaskTest}.
 * 
 * @author Jay Jay Billings
 *
 */
class ExecutingStateActionTest {

	/**
	 * Tests the state setting and failure modes of the action
	 */
	@Test
	void testStateSet() {
		TaskStateData data = new TaskStateDataImplementation();
		ExecutingStateAction<TestData> action = new ExecutingStateAction<>(data);
		action.execute(null);
		
		// The test action is on a thread, so this test needs to wait a bit until the
		// timer runs down and the execution finishes. It should fail since it does not
		// have the required information.
		await().atMost(10, TimeUnit.SECONDS).until(() -> (data.getTaskState() == TaskState.FAILED));

		return;
	}

}
