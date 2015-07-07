/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.client.widgets.moose.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * This class provides the default action that is performed when the button part
 * (not the arrow) of the MOOSE dropdown ToolItem is clicked.
 * <p>
 * Currently, clicking this button does nothing. The meaningful actions are
 * contained in its dropdown menu.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class MOOSEActionHandler extends AbstractHandler {

	/*
	 * Implements abstract method from AbstractHandler.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// There is nothing to do at the moment.
		return null;
	}
}