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
	private static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);

	/**
	 * Constructor
	 */
	public CommandFactory() {
	}

	/**
	 * This is a helper function to call the main getCommand function with a null
	 * reference for the second connection. This function is for Local commands or
	 * Remote commands where your local computer is connecting to a remote host. The
	 * more generic getCommand function with three arguments is for the case where
	 * one wants to connect remotely to a host, and then connect from that host to
	 * another host to execute a command. See also
	 * {@link org.eclipse.ice.commands.CommandFactory#getCommand(CommandConfiguration, ConnectionConfiguration, ConnectionConfiguration)
	 * 
	 * @param commandConfig
	 * @param connectConfig
	 * @return
	 * @throws IOException
	 */
	public Command getCommand(final CommandConfiguration commandConfig, final ConnectionConfiguration connectConfig)
			throws IOException {
		// pass null since the connection configuration connects us from the local host
		// to the remote host, and we don't have an intermediary host
		return getCommand(commandConfig, connectConfig, null);
	}

	/**
	 * Method which gets a command and subsequently executes it on a host See also
	 * Command class {@link org.eclipse.ice.commands.Command}.
	 * 
	 * @param commandConfig           - the CommandConfiguration which holds the
	 *                                particular details of a given command.
	 * @param ConnectionConfiguration - the ConnectionConfiguration which holds the
	 *                                details on the connection (i.e. local vs.
	 *                                remote)
	 * @param extraConnection         - An additional connection configuration if
	 *                                the user wants to "hop" connections, i.e. log
	 *                                into one host and then send a job from that
	 *                                host to an additional remote host
	 * @return
	 * @throws IOException
	 */
	public Command getCommand(final CommandConfiguration commandConfig, final ConnectionConfiguration connectionConfig,
			ConnectionConfiguration extraConnection) throws IOException {

		Command command = null;

		// If no host was provided, we don't know where to run the job
		if (connectionConfig.getHostname() == null) {
			logger.error(
					"You didn't provide a hostname in the ConnectionConfiguration for the job to run on! Exiting.");
			throw new IOException();
		}

		// If the host is local, get a LocalCommand. Otherwise, RemoteCommand
		if (isLocal(connectionConfig.getHostname())) {
			command = new LocalCommand(connectionConfig, commandConfig);
		} else if (extraConnection == null) {
			// If there was only one connection, then it is a remote command from
			// the local machine to another host machine
			command = new RemoteCommand(commandConfig, connectionConfig, null);
		} else {
			// Otherwise if there were two connections provided, the intermediate connection
			// is used as the "host" and the final connection is used as the "destination"
			command = new RemoteCommand(commandConfig, connectionConfig, extraConnection);
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
		// Get the local host name from the operating system
		String hostname = addr.getHostName();

		// If the local hostname is the same as the hostname provided, then it is local
		if (hostname == host)
			return true;
		else
			return false;

	}

}
