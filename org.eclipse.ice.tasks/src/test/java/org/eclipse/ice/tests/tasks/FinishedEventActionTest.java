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

import org.eclipse.ice.tasks.TaskState;
import org.eclipse.ice.tasks.TaskStateData;
import org.eclipse.ice.tasks.TaskStateDataImplementation;
import org.eclipse.ice.tasks.spring.statemachine.FinishedEventAction;
import org.junit.jupiter.api.Test;

/**
 * This tests the basic ability of the {@link FinishedEventAction} to set the
 * state correctly.
 * 
 * @author Jay Jay Billings
 *
 */
class FinishedEventActionTest {

	/**
	 * Tests the state setting
	 */
	@Test
	void testStateSet() {
		TaskStateData data = new TaskStateDataImplementation();
		FinishedEventAction action = new FinishedEventAction(data);
		action.execute(null);
		assertEquals(TaskState.FINISHED, data.getTaskState());

		return;
	}

}
