/**
 * /*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Joe Osborn
 *******************************************************************************/

package org.eclipse.ice.commands;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Child class for locally copying a file without a remote connection
 * 
 * @author Joe Osborn
 *
 */
public class LocalCopyFileCommand extends LocalCommand {

	/**
	 * The path to the source file which is to be copied
	 */
	private Path source;

	/**
	 * The path of the destination for which the source file will be copied to
	 */
	private Path destination;

	/**
	 * Default constructor
	 */
	public LocalCopyFileCommand() {
	}

	/**
	 * This function actually executes the copy file command. It checks that the
	 * copy was completed successfully. It returns a CommandStatus indicating
	 * whether or not the move was successful.
	 * See also {@link org.eclipse.ice.commands.Command#execute()}
	 * 
	 * @return CommandStatus - indicating status of job
	 */
	@Override
	public CommandStatus execute() {

		// Run the copying
		status = run();

		return status;
	}

	/**
	 * This function contains the command to actually copy the file. Returns a
	 * CommandStatus indicating that the command is currently running and needs to
	 * be checked that it completed correctly.
	 * See also {@link org.eclipse.ice.commands.Command#run}
	 * 
	 * @return CommandStatus - indicate status of job
	 */
	@Override
	protected CommandStatus run() {
		// Try to copy from source to destination, overwriting if the file already
		// exists at the destination. If it can't, complain.
		logger.info("Copying " + source + " to " + destination);
		try {
			status = CommandStatus.RUNNING;
			Files.copy(source, destination.resolve(source.getFileName()), REPLACE_EXISTING);
		} catch (IOException e) {
			status = CommandStatus.FAILED;
			logger.error("Local copy failed! Returning failed.", e);
			return status;
		}
		return CommandStatus.SUCCESS;
	}

	/**
	 * This function sets the Paths for source and destination to the given strings
	 * 
	 * @param src  - string corresponding to the source file
	 * @param dest - string corresponding to the destination file
	 */
	public void setConfiguration(String src, String dest) {
		source = Paths.get(src);
		destination = Paths.get(dest);
		return;
	}

	/**
	 * A function that returns the source path in string form
	 * 
	 * @return - String of the source path
	 */
	public String getSource() {
		return source.toString();
	}

	/**
	 * A function that returns the destination path in string form
	 * 
	 * @return - String of the destination path
	 */
	public String getDestination() {
		return destination.toString();
	}

}
