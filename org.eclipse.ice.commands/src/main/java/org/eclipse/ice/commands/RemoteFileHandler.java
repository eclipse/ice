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
package org.eclipse.ice.commands;

import java.io.IOException;

/**
 * This class inherits from FileHandler and handles the processing of remote file
 * transfer commands. Remote file transfers can be from a local-to-remote (or vice versa) or 
 * remote-to-remote system.
 * @author Joe Osborn
 *
 */
public class RemoteFileHandler extends FileHandler{

	/**
	 * Default constructor
	 */
	public RemoteFileHandler() {
	}
	
	public RemoteFileHandler(String _source, String _destination, ConnectionConfiguration _sourceConfiguration,
			ConnectionConfiguration _destinationConfiguration) {
		source = _source;
		destination = _destination;
		sourceConfiguration = _sourceConfiguration;
		destinationConfiguration = _destinationConfiguration;

	}
	
	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#move()}
	 */
	@Override
	public CommandStatus move() throws IOException {
		return null;
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#copy()}
	 */
	@Override
	public CommandStatus copy() throws IOException {
		return null;
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#exists(String)}
	 */
	@Override
	public boolean exists(String file) throws IOException {
		return false;
	}

}
