/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands.notification;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface lays out the format for any Command update class. Command
 * update classes notify users via their implemented methods about the status of
 * jobs, e.g. if they are completed or not.
 * 
 * @author Joe Osborn
 *
 */
public interface ICommandUpdateHandler {

	/**
	 * Logger for handling event messages and other information.
	 */
	static final Logger logger = LoggerFactory.getLogger(ICommandUpdateHandler.class);

	/*
	 * This function processes the logic required to post an update about the job
	 * for the class
	 */
	abstract void postUpdate() throws IOException;
}
