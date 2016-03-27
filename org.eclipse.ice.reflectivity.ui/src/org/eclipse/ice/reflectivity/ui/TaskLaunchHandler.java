/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/

package org.eclipse.ice.reflectivity.ui;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

/**
 * This class handles launch requests for the Reflectivity Task Launcher, which
 * is just a little composite that draw stuff to the screen to make it easier to
 * use the reflectivity tools.
 * 
 * @author Jay Jay Billings
 *
 */
public class TaskLaunchHandler {

	/**
	 * Constructor
	 */
	public TaskLaunchHandler() throws CoreException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Execute
	public void execute(Shell shell) throws ExecutionException {
		TaskLaunchDialog dialog = new TaskLaunchDialog(shell);
		dialog.open();
	}

}
