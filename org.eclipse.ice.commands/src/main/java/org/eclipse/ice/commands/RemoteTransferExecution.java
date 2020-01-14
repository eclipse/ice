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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClient.Attributes;
import org.apache.sshd.client.subsystem.sftp.SftpClient.OpenMode;
import org.apache.sshd.common.subsystem.sftp.SftpConstants;
import org.apache.sshd.common.subsystem.sftp.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class performs the remote transfer execution for a remote copy or move
 * command It is called from RemoteCopyFileCommand or RemoteMoveFileCommand,
 * after the RemoteFileTransfer configures this command.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteTransferExecution {
	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(RemoteTransferExecution.class);

	/**
	 * A status to track the status of the execution
	 */
	private CommandStatus status;

	/**
	 * A boolean indicating whether or not the transfer is a move or a copy. True
	 * for move, false for copy
	 */
	private boolean isMove;

	/**
	 * Default constructor
	 */
	public RemoteTransferExecution() {
	}

	protected CommandStatus executeTransfer(Connection connection, String source, String destination, int permissions,
			HandleType transferType) {
		try {
			// Determine how to proceed given what kind of copy it is
			if (transferType == HandleType.localRemote) {
				transferLocalToRemote(connection, source, destination);
			} else if (transferType == HandleType.remoteLocal) {
				transferRemoteToLocal(connection, source, destination);
			} else if (transferType == HandleType.remoteRemote) {
				transferRemoteToRemote(connection, source, destination);
			} else {
				logger.info("Unknown handle type...");
				status = CommandStatus.FAILED;
				return status;
			}

			// If permissions was actually instantiated and isn't the default, then perform
			// a chmod
			if (permissions != -999) {
				SftpClient channel = connection.getSftpChannel();
				Attributes attrs = channel.stat(destination);
				attrs.setPermissions(permissions);
				channel.setStat(destination, attrs);
			}
		} catch (IOException e) {
			logger.error("Failed to connect or obtain file to/from remote host. Returning failed.", e);
			status = CommandStatus.FAILED;
			return status;
		}

		// Set the status to success and return
		status = CommandStatus.SUCCESS;
		return status;
	}

	/**
	 * This is a function that executes a transfer command on the remote host to
	 * copy a file from one location on the remote host to another location on the
	 * remote host.
	 * 
	 * @throws JSchException
	 */
	private void transferRemoteToRemote(Connection connection, String source, String destination) throws IOException {
		// Make a transfer command to execute
		// First check if it is a move or copy
		String moveType = "cp ";
		if (isMove)
			moveType = "mv ";

		// Build the command to execute
		String command = moveType + source + " " + destination;
		// Set the command for the JSch connection
		connection.getSession().executeRemoteCommand(command);
	}
	
	/**
	 * Transfers a local file to the remote host. If the remote destination is a directory, a file with the
	 * same name as the local file will be created in the directory.
	 * 
	 * @param connection
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	private void transferLocalToRemote(Connection connection, String src, String dest) throws IOException {
		SftpClient client = connection.getSftpChannel();
		try {
			if (client.stat(dest).isDirectory()) {
				Path path = FileSystems.getDefault().getPath(src);
				String sep = "";
				if (!dest.endsWith("/")) {
					sep = "/";
				}
				dest += sep + path.getFileName();
			}	
		} catch (SftpException e) {
            if (!(e.getStatus() == SftpConstants.SSH_FX_NO_SUCH_FILE)) {
                throw e;
            }
        }
		try (OutputStream dstStream = client.write(dest, OpenMode.Create, OpenMode.Write, OpenMode.Truncate)) {
			try (InputStream srcStream = new FileInputStream(src)) {
				byte[] buf = new byte[32*1024];
				while (srcStream.read(buf) > 0) {
					dstStream.write(buf);
				}
			}
		}
	}
	
	
	/**
	 * Transfers a remote file to the local host. If the destination is a directory, a file with the
	 * same name as the remote file will be created in the directory.
	 * 
	 * @param connection
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	private void transferRemoteToLocal(Connection connection, String src, String dest) throws IOException {
		Path dstPath = FileSystems.getDefault().getPath(dest);
		if (dstPath.toFile().isDirectory()) {
			String[] tokens = src.split("/");
			dstPath = dstPath.resolve(tokens[tokens.length - 1]);
		}
		try (OutputStream dstStream = Files.newOutputStream(dstPath)) {
			try (InputStream srcStream = connection.getSftpChannel().read(src, OpenMode.Read)) {
				byte[] buf = new byte[32*1024];
				while (srcStream.read(buf) > 0) {
					dstStream.write(buf);
				}
			}
		}
	}

	/**
	 * Setter function to tell the class whether or not the transfer is a move or a
	 * copy
	 * 
	 * @param isMove
	 */
	protected void isMove(boolean isMove) {
		this.isMove = isMove;
	}

}
