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
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
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
	private AtomicReference<ConnectionConfiguration> configuration = new AtomicReference<ConnectionConfiguration>(null);;

	/**
	 * The secure channel provided by com.jcraft.jsch
	 */
	private AtomicReference<JSch> jShell = new AtomicReference<JSch>(null);

	/**
	 * The JShell session
	 */
	private AtomicReference<Session> session = new AtomicReference<Session>(null);

	/**
	 * The ssh channel for the JSch ssh connection to execute over
	 */
	private AtomicReference<ChannelExec> execChannel = new AtomicReference<ChannelExec>(null);

	/**
	 * The ssh channel for the JSch ssh connection to perform sftp transfers over
	 */
	private AtomicReference<ChannelSftp> sftpChannel = new AtomicReference<ChannelSftp>(null);

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
	}

	/**
	 * Constructor which actually sets the connection configuration to a passed
	 * argument
	 * 
	 * @param config - the configuration to set the connection information
	 */
	public Connection(ConnectionConfiguration config) {
		configuration = new AtomicReference<ConnectionConfiguration>(config);
	}

	/**
	 * Get and return the connection configuration,
	 * see {@link org.eclipse.ice.commands.Connection#configuration}
	 * 
	 * @return - the Connection's ConnectionConfiguration
	 */
	public ConnectionConfiguration getConfiguration() {
		return configuration.get();
	}

	/**
	 * Set the configuration, see {@link org.eclipse.ice.commands.Connection#configuration}
	 * 
	 * @param configuration - ConnectionConfiguration to set for this connection
	 */
	public void setConfiguration(ConnectionConfiguration configuration) {
		this.configuration.set(configuration);
	}
	/**
	 * Set the JShell session {@link org.eclipse.ice.commands.Connection#jShell}
	 * 
	 * @param jsch - JSch session to be set for this Connection
	 */
	public void setJShellSession(JSch jShell) {
		this.jShell = new AtomicReference<JSch>(jShell);
	}

	/**
	 * Get the JShellSession {@link org.eclipse.ice.commands.Connection#jShell}
	 * 
	 * @return - this Connection's JSch session
	 */
	public JSch getJShellSession() {
		return jShell.get();
	}

	/**
	 * Set the execution channel
	 * {@link org.eclipse.ice.commands.Connection#execChannel}
	 * 
	 * @param execChannel - execution channel to be set for this Connection
	 */
	public void setExecChannel(Channel execChannel) {
		this.execChannel = new AtomicReference<ChannelExec>((ChannelExec) execChannel);
	}

	/**
	 * Get the sftp channel {@link org.eclipse.ice.commands.Connection#sftpChannel}
	 * 
	 * @return - this Connection's sftp channel
	 */
	public ChannelSftp getSftpChannel() {
		return sftpChannel.get();
	}

	/**
	 * Set the sftp channel {@link org.eclipse.ice.commands.Connection#sftpChannel}
	 * 
	 * @param sftpChannel - sftp channel to be set for this Connection
	 */
	public void setSftpChannel(Channel sftpChannel) {
		this.sftpChannel = new AtomicReference<ChannelSftp>((ChannelSftp) sftpChannel);
	}

	/**
	 * Get the execution channel
	 * {@link org.eclipse.ice.commands.Connection#execChannel}
	 * 
	 * @return - this Connection's execution channel
	 */
	public ChannelExec getExecChannel() {
		return execChannel.get();
	}

	/**
	 * Getter for the session associated to this connection
	 * 
	 * @return {@link org.eclipse.ice.commands.Connection#session}
	 */
	public Session getSession() {
		return session.get();
	}

	/**
	 * Set the session {@link org.eclipse.ice.commands.Connection#session}
	 * 
	 * @param session - this Connection's session
	 */
	public void setSession(Session session) {
		this.session = new AtomicReference<Session>(session);
	}

	/**
	 * Set the input stream {@link org.eclipse.ice.commands.Connection#inputStream}
	 * 
	 * @param inputStream - this Connection's input stream for logging
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * Get the input stream {@link org.eclipse.ice.commands.Connection#inputStream}
	 * 
	 * @return - this Connection's input stream for logging
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Set the output stream
	 * {@link org.eclipse.ice.commands.Connection#outputStream}
	 * 
	 * @param outputStream - this Connection's output stream for logging
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * Get the output stream
	 * {@link org.eclipse.ice.commands.Connection#outputStream}
	 * 
	 * @return - this Connection's output stream for logging
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}
}
