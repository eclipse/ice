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
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The command factory simplifies the creation of Commands without revealing how
 * they are constructed. It is primarily used for creating generic commands,
 * whereas other factories are use for creating specific commands such as file
 * transfer commands.
 * 
 * @author Jay Jay Billings, Joe Osborn
 */
public class CommandFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);

	/**
	 * Constructor
	 */
	public CommandFactory() {
	}

	/**
	 * Method which gets a command and subsequently executes it on a host See also
	 * Command class {@link org.eclipse.ice.commands.Command}.
	 * 
	 * @param CommandConfiguration    - the CommandConfiguration which holds the
	 *                                particular details of a given command.
	 * @param ConnectionConfiguration - the ConnectionConfiguration which holds the
	 *                                details on the connection (i.e. local vs.
	 *                                remote)
	 * @return Command
	 */
	public Command getCommand(final CommandConfiguration commandConfig,
			final ConnectionConfiguration connectionConfig) throws IOException {

		Command command = null;

		// If no host was provided, we don't know where to run the job
		if (connectionConfig.hostname == null) {
			logger.error(
					"You didn't provide a hostname in the ConnectionConfiguration for the job to run on! Exiting.");
			throw new IOException();
		}

		// If the host is local, get a LocalCommand. Otherwise, RemoteCommand
		if (isLocal(connectionConfig.hostname)) {
			command = new LocalCommand(connectionConfig, commandConfig);
		} else {
			command = new RemoteCommand(connectionConfig, commandConfig);
		}

		return command;

	}

	/**
	 * A function to check whether or not the provided hostname by the user in
	 * CommandFactory is a local hostname or remote hostname.
	 * 
	 * @param host - String of the hostname to be checked
	 * @return boolean - returns true if the hostname matches that of the local
	 *         hostname, false otherwise.
	 */
	private boolean isLocal(String host) {

		// Get the local hostname address
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String hostname = addr.getHostName();

		// If the local hostname is the same as the hostname provided, then it is local
		if (hostname == host)
			return true;
		else
			return false;

	}

}
