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
package org.eclipse.ice.tasks;

/**
 * A dedicated exception for Tasks.
 * @author Jay Jay Billings
 *
 */
public class TaskException extends Exception {

	/**
	 * Constructor
	 * @param msg error message from the task
	 */
	public TaskException(String msg) {
		super(msg);
	}
	
	/**
	 * Exception id
	 */
	private static final long serialVersionUID = -7680227861625984735L;

}
