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

import org.eclipse.ice.dev.annotations.DataElement;
import org.eclipse.ice.dev.annotations.DataField;
import org.eclipse.ice.tasks.TaskState;

/**
 * Test data for the Task tests
 * @author Jay Jay Billiings
 *
 */
@DataElement(name="TestData")
public class TestDataSpec {

	@DataField.Default(value = "org.eclipse.ice.tasks.TaskState.INITIALIZED")
	@DataField
	private TaskState state;
	
}
