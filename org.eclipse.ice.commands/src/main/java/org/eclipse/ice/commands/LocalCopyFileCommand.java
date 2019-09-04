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
import java.nio.file.Paths;
/**
 * Child class for locally copying a file without a remote connection
 * @author Joe Osborn
 *
 */
public class LocalCopyFileCommand extends CopyFileCommand {

	/**
	 * Default constructor
	 */
	public LocalCopyFileCommand() {}
	
	/**
	 * Constructor which sets the two paths, source and destination, to those given
	 * by the arguments of the constructor. See
	 * {@link org.eclipse.ice.tests.commands.CopyFileCommand} for member variable
	 * descriptions.
	 * 
	 * @param src
	 * @param dest
	 */
	public LocalCopyFileCommand(final String src, final String dest) {
		source = Paths.get(src);
		destination = Paths.get(dest);
	}
	
	/**
	 * This function actually executes the copy file command. It checks that
	 * the copy was completed successfully. It returns a 
	 * CommandStatus indicating whether or not the move was successful.
	 * @return CommandStatus
	 */
	@Override
	public CommandStatus execute() {
		
		// Run the copying
		status = run();
				
		return status;
	}


	/**
	 * This function contains the command to actually copy the file. Returns
	 * a CommandStatus indicating that the command is currently running and
	 * needs to be checked that it completed correctly.
	 * @return CommandStatus
	 */
	@Override
	protected CommandStatus run() {
		// Try to copy from source to destination, overwriting if the file already
		// exists at the destination. If it can't, complain.
		try {
			Files.copy(source,  destination.resolve(source.getFileName()), REPLACE_EXISTING);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return CommandStatus.RUNNING;
	}

	/**
	 * This function cancels the command when called. See also
	 * {@link org.eclipse.ice.commands.Command#cancel()}
	 */
	@Override
	public CommandStatus cancel() {
		status = CommandStatus.CANCELED;
		return CommandStatus.CANCELED;
	}

	
}
