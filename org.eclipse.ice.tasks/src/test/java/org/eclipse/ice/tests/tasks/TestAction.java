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

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.tasks.Action;
import org.eclipse.ice.tasks.ActionType;

/**
 * This is a test action used to test the Task class.
 * 
 * @author Jay Jay Billings
 *
 */
public class TestAction<T> implements Action<T> {

	/**
	 * True if the run operation is called, false if not.
	 */
	private AtomicBoolean called = new AtomicBoolean(false);

	/**
	 * @return a diagnostic action type since this is a test
	 */
	@Override
	public ActionType getType() {
		return ActionType.BASIC.DIAGNOSTIC;
	}

	@Override
	public boolean run(T data) {
		// Flag that the action was correctly called.
		called.set(true);
		return true;
	}

	/**
	 * This operation returns true if the action was called successfully, false if
	 * not.
	 * 
	 * @return true if called, false otherwise.
	 */
	public boolean wasCalled() {
		return called.get();
	}

}
