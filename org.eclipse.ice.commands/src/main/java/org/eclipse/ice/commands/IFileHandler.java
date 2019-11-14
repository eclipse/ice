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
 * This interface defines and lays out the design for the FileHandler structures
 * responsible for moving and/or copying files to and from destinations.
 * 
 * @author Joe Osborn
 *
 */
public interface IFileHandler {

	/**
	 * This method is responsible for moving a file from a source to a destination
	 * path If the operation fails, an exception is thrown
	 * 
	 * @return - CommandStatus - a CommandStatus indicating whether or not the move
	 *         was successful
	 * @throws IOException
	 */
	public abstract CommandStatus move(final String source, final String destination) throws IOException;

	/**
	 * This method is responsible for copying a file from a source to a destination
	 * path If the operation fails, an exception is thrown
	 * 
	 * @return - CommandStatus - a CommandStatus indicating whether or not the copy
	 *         was successful
	 * @throws IOException
	 */
	public abstract CommandStatus copy(final String source, final String destination) throws IOException;

	/**
	 * This method is responsible for determining whether or not a file or directory
	 * already exists for a given path.
	 * 
	 * @param - String - a string with the path for the method to check its
	 *          existence
	 * @return - boolean indicating whether or not the file exists (returns true) or
	 *         does not exist (returns false)
	 * @throws IOException
	 */
	public abstract boolean exists(final String file) throws IOException;

	/**
	 * This method checks the existence of the source and destination files. If the
	 * destination doesn't exist, it tries to make it. If the destination can't be
	 * made, or the source doesn't exist, the method throws an exception.
	 * 
	 * @param source
	 * @param destination
	 * @return
	 * @throws IOException
	 */
	public abstract void checkExistence(final String source, final String destination) throws IOException;

	/**
	 * Method that returns a FileBrowser instance
	 * 
	 * @return
	 */
	public abstract FileBrowser getFileBrowser();

}
