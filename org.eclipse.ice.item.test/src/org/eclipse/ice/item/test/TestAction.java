/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.item.test;

import java.util.Dictionary;

import org.eclipse.ice.item.action.Action;
import org.eclipse.january.form.Form;
import org.eclipse.january.form.FormStatus;

/**
 * <p>
 * The TestAction is used for testing Actions and for faking actions in ICE
 * Items. It does not override Action's Form accessors.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class TestAction extends Action {
	/**
	 * <p>
	 * The constructor. It initializes the Action's Form to a basic Form.
	 * </p>
	 * 
	 */
	public TestAction() {

		// Initialize the Form
		actionForm = new Form();

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Action#execute(Dictionary<Object> dictionary)
	 */
	@Override
	public FormStatus execute(Dictionary<String, String> dictionary) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Action#cancel()
	 */
	@Override
	public FormStatus cancel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getActionName() {
		return "Test Action";
	}
}