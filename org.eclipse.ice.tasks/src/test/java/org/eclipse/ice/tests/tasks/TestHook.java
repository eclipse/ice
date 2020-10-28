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

import org.eclipse.ice.tasks.ActionType;
import org.eclipse.ice.tasks.Hook;

/**
 * @author Jay Jay Billings
 *
 */
public class TestHook<T> implements Hook<T> {

	@Override
	public ActionType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean run(T data) {
		// TODO Auto-generated method stub
		return false;
	}

}
