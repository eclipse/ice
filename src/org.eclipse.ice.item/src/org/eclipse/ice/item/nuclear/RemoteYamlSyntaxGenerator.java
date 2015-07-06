/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Alexander J. McCaskey
 *******************************************************************************/
package org.eclipse.ice.item.nuclear;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteProcess;
import org.eclipse.remote.core.IRemoteProcessBuilder;
import org.eclipse.remote.core.IRemoteProcessService;
import org.eclipse.remote.core.exception.RemoteConnectionException;

/**
 * This class serves as a utility for generating MOOSE YAML and Action Syntax
 * files on a remote machine and using them to construct the MOOSE input tree
 * locally in ICE.
 * 
 * @author Alex McCaskey
 *
 */
public class RemoteYamlSyntaxGenerator {

	/**
	 * This method generates the YAML and Action syntax files in 
	 * the local projectSpace/MOOSE directory from a remotely hosted 
	 * MOOSE-based application. 
	 * 
	 * @param connection The remote connection for the machine with the application
	 * @param mooseFolder The reference to the MOOSE folder in the project space
	 * @param appPath The path for the remote MOOSE-based application
	 */
	public void generate(IRemoteConnection connection, IFolder mooseFolder, String appPath) {

		// Local Declarations
		IRemoteProcessService processService = null;
		IRemoteProcess yamlRemoteJob = null, syntaxRemoteJob = null;

		// Try to open the connection and fail if it will not open
		try {
			connection.open(null);
		} catch (RemoteConnectionException e) {
			// Print diagnostic information and fail
			e.printStackTrace();
			return;
		}

		// Do the upload(s) and launch the job if the connection is open
		if (connection.isOpen()) {
			// Diagnostic info
			System.out.println("RemoteYamlSyntaxGenerator Message:" + " PTP connection established.");

			// Get the IRemoteProcessService
			processService = connection.getService(IRemoteProcessService.class);

			// Create the process builder for the remote job
			IRemoteProcessBuilder yamlProcessBuilder = processService.getProcessBuilder("sh", "-c",
					appPath + " --yaml");
			IRemoteProcessBuilder syntaxProcessBuilder = processService.getProcessBuilder("sh", "-c",
					appPath + " --syntax");

			// Do not redirect the streams
			yamlProcessBuilder.redirectErrorStream(false);
			syntaxProcessBuilder.redirectErrorStream(false);

			try {
				System.out.println("RemoteYamlSyntaxGenerator Message: " + "Attempting to launch with PTP...");
				System.out.println("RemoteYamlSyntaxGenerator Message: Command sent to PTP = "
						+ yamlProcessBuilder.command().toString());
				yamlRemoteJob = yamlProcessBuilder.start(IRemoteProcessBuilder.FORWARD_X11);
				syntaxRemoteJob = syntaxProcessBuilder.start(IRemoteProcessBuilder.FORWARD_X11);

			} catch (IOException e) {
				// Print diagnostic information and fail
				e.printStackTrace();
				return;
			}

			// Monitor the job
			while (!yamlRemoteJob.isCompleted() && !syntaxRemoteJob.isCompleted()) {
				// Give it a second
				try {
					Thread.currentThread();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// Complain
					e.printStackTrace();
				}
			}

			// Get the YAML and Syntax output
			InputStream yamlStream = yamlRemoteJob.getInputStream();
			InputStream syntaxStream = syntaxRemoteJob.getInputStream();

			// Write it to File.
			try {
				String yamlString = IOUtils.toString(yamlStream);
				String syntaxString = IOUtils.toString(syntaxStream);

				// Get the app name
				String animal = Paths.get(appPath).getFileName().toString();

				// Get the path to the Yaml/Syntax files to be created
				// in the MOOSE folder
				Path yamlPath = Paths.get(mooseFolder.getLocation().toOSString() + "/" + animal + ".yaml");
				Path syntaxPath = Paths.get(mooseFolder.getLocation().toOSString() + "/" + animal + ".syntax");

				// Delete existing files 
				if (Files.exists(yamlPath)) {
					Files.delete(yamlPath);
				}
				if (Files.exists(syntaxPath)) {
					Files.delete(syntaxPath);
				}
				
				// Write the new files. 
				Files.write(yamlPath, yamlString.getBytes());
				Files.write(syntaxPath, syntaxString.getBytes());

			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}
}
