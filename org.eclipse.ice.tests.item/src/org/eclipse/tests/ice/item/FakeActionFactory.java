/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.tests.ice.item;

import org.eclipse.ice.item.IActionFactory;
import org.eclipse.ice.item.action.Action;

/**
 * This is a fake implementation of the IActionFactory used for testing.
 * 
 * @author Jay Jay Billings
 * 
 */
public class FakeActionFactory implements IActionFactory {

	@Override
	public Action getAction(String actionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAvailableActions() {
		// TODO Auto-generated method stub
		return null;
	}

}
