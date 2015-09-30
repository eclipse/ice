/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alex McCaskey - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.developer.moose.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This handler triggers the "Fork MOOSE" action.
 * 
 * TODO Update this documentation with more appropriate details.
 * 
 * @author Alex McCaskey
 *
 */
public class ForkMOOSEHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Get the current shell from the HandlerUtil.
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

		// TODO Implement. Note: This is using the UI thread.

		return null;
	}
}