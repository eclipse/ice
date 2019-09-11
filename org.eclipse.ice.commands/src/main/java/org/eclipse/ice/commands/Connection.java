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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * This class represents a connection to a remote system. This could be a system
 * that is physically remote or remote in a process sense.
 * 
 * @author Jay Jay Billings, Joe Osborn
 *
 */
public class Connection {

	/**
	 * An AtomicReference to the ConnectionConfiguration from which connection
	 * information can be gathered
	 */
	private AtomicReference<ConnectionConfiguration> configuration;

	/**
	 * The secure channel provided by com.jcraft.jsch
	 */
	private JSch jShell = null;

	/**
	 * The JShell session
	 */
	private Session session = null;

	/**
	 * The ssh channel for the JSch ssh connection to execute over
	 */
	private Channel channel = null;

	/**
	 * The input stream for the JSch ssh connection
	 */
	private InputStream inputStream = null;

	/**
	 * The output stream for the JSch ssh connection
	 */
	private OutputStream outputStream = null;

	/**
	 * Default constructor
	 */
	public Connection() {
		configuration = new AtomicReference<ConnectionConfiguration>(new ConnectionConfiguration());
	}

	/**
	 * Constructor which actually sets the connection configuration to a passed
	 * argument
	 * 
	 * @param config
	 */
	public Connection(AtomicReference<ConnectionConfiguration> config) {
		configuration = config;
	}

	/**
	 * Get and return the connection configuration
	 * 
	 * @return
	 */
	public ConnectionConfiguration getConfiguration() {
		return configuration.get();
	}

	/**
	 * Set the JShell session {@link org.eclipse.ice.commands.Connection#jShell}
	 * 
	 * @param jsch
	 */
	public void setJShellSession(JSch jsch) {
		jShell = jsch;
	}

	/**
	 * Get the JShellSession {@link org.eclipse.ice.commands.Connection#jShell}
	 * 
	 * @return
	 */
	public JSch getJShellSession() {
		return jShell;
	}

	/**
	 * Set the channel {@link org.eclipse.ice.commands.Connection#channel}
	 * 
	 * @param _channel
	 */
	public void setChannel(Channel _channel) {
		channel = _channel;
	}

	/**
	 * Get the channel {@link org.eclipse.ice.commands.Connection#channel}
	 * 
	 * @return
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Getter for the session associated to this connection
	 * 
	 * @return {@link org.eclipse.ice.commands.Connection#session}
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Set the session {@link org.eclipse.ice.commands.Connection#session}
	 * 
	 * @param _session
	 */
	public void setSession(Session _session) {
		session = _session;
	}

}
