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

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.subsystem.sftp.SftpClient;

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
	 * The client entry point
	 */
	private AtomicReference<SshClient> client = new AtomicReference<>(null);

	/**
	 * The session for this connection
	 */
	private AtomicReference<ClientSession> clientSession = new AtomicReference<>(null);

	/**
	 * The connection to execute over
	 */
	private AtomicReference<ChannelExec> execChannel = new AtomicReference<>(null);

	/**
	 * The sftp client
	 */
	private AtomicReference<SftpClient> sftpClient = new AtomicReference<>(null);

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
	 * @param config
	 */
	public Connection(ConnectionConfiguration config) {
		configuration = new AtomicReference<ConnectionConfiguration>(config);
	}

	/**
	 * Get and return the connection configuration
	 * 
	 * @return
	 */
	public ConnectionConfiguration getConfiguration() {
		return configuration.get();
	}

	public void setConfiguration(ConnectionConfiguration configuration) {
		this.configuration.set(configuration);
	}
	
	public SshClient getClient() {
		return client.get();
	}
	
	public void setClient(SshClient client) {
		this.client.set(client);
	}

	/**
	 * Get the sftp channel {@link org.eclipse.ice.commands.Connection#sftpChannel}
	 * 
	 * @return
	 */
	public SftpClient getSftpChannel() {
		return sftpClient.get();
	}
	
	/**
	 * Set the sftp channel {@link org.eclipse.ice.commands.Connection#sftpChannel}
	 * 
	 * @param sftpChannel
	 */
	public void setSftpChannel(SftpClient sftpClient) {
		this.sftpClient.set(sftpClient);
	}

	/**
	 * Get the execution channel
	 * {@link org.eclipse.ice.commands.Connection#execChannel}
	 * 
	 * @return
	 */
	public ChannelExec getExecChannel() {
		return execChannel.get();
	}

	/**
	 * Set the execution channel
	 * {@link org.eclipse.ice.commands.Connection#execChannel}
	 * 
	 * @param execChannel
	 */
	public void setExecChannel(ChannelExec execChannel) {
		this.execChannel.set(execChannel);
	}

	/**
	 * Getter for the session associated to this connection
	 * 
	 * @return {@link org.eclipse.ice.commands.Connection#session}
	 */
	public ClientSession getSession() {
		return clientSession.get();
	}

	/**
	 * Set the session {@link org.eclipse.ice.commands.Connection#session}
	 * 
	 * @param _session
	 */
	public void setSession(ClientSession session) {
		clientSession.set(session);
	}

	/**
	 * Set the input stream {@link org.eclipse.ice.commands.Connection#inputStream}
	 * 
	 * @param _stream
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * Get the input stream {@link org.eclipse.ice.commands.Connection#inputStream}
	 * 
	 * @return
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Set the output stream
	 * {@link org.eclipse.ice.commands.Connection#outputStream}
	 * 
	 * @param _stream
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * Get the output stream
	 * {@link org.eclipse.ice.commands.Connection#outputStream}
	 * 
	 * @return
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}
}
