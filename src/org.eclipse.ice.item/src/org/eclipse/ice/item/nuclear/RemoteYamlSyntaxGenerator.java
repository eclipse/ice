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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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
public class RemoteYamlSyntaxGenerator {// extends Job {

	/**
	 * Reference to the remote PTP connection to use.
	 */
	private IRemoteConnection connection;

	/**
	 * Reference to the IFolder for MOOSE in the project space.
	 */
	private IFolder mooseFolder;

	/**
	 * Reference to the remote application absolute path. 
	 */
	private String appPath;

	/**
	 * The Constructor
	 * 
	 * @param conn
	 * @param moose
	 * @param app
	 */
	public RemoteYamlSyntaxGenerator(IRemoteConnection conn, IFolder moose, String app) {
		//super("Executing Remote YAML/Syntax Invocation.");
		connection = conn;
		mooseFolder = moose;
		appPath = app;
	}

	/**
	 * This method generates the YAML and Action syntax files in the local
	 * projectSpace/MOOSE directory from a remotely hosted MOOSE-based
	 * application.
	 *
	 * @param monitor
	 */
	public IStatus generate() {

		// Local Declarations
		IRemoteProcessService processService = null;
		IRemoteProcess yamlRemoteJob = null, syntaxRemoteJob = null;

		//monitor.subTask("Opening the Remote Connection.");
		//monitor.worked(40);
		
		// Try to open the connection and fail if it will not open
		try {
			connection.open(null);
		} catch (RemoteConnectionException e) {
			// Print diagnostic information and fail
			e.printStackTrace();
			String errorMessage = "Could not create connection to remote machine.";
			return new Status(
					Status.ERROR,
					"org.eclipse.ice.item.nuclear",
					1, errorMessage, null);
		}

		// Do the upload(s) and launch the job if the connection is open
		if (connection.isOpen()) {
			// Diagnostic info
			System.out.println("RemoteYamlSyntaxGenerator Message:" + " PTP connection established.");

			//monitor.subTask("Getting a reference to the Process Service on remote machine.");
			//monitor.worked(60);

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
				
				//monitor.subTask("Generating YAML and Action Syntax files on remote machine.");
				//monitor.worked(80);

				yamlRemoteJob = yamlProcessBuilder.start(IRemoteProcessBuilder.FORWARD_X11);
				syntaxRemoteJob = syntaxProcessBuilder.start(IRemoteProcessBuilder.FORWARD_X11);

			} catch (IOException e) {
				// Print diagnostic information and fail
				e.printStackTrace();
				String errorMessage = "Could not execute YAML/Syntax generation on remote machine.";
				return new Status(
						Status.ERROR,
						"org.eclipse.ice.item.nuclear",
						1, errorMessage, null);
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

				//monitor.subTask("Writing files locally.");
				//monitor.worked(95);

				// Write the new files.
				Files.write(yamlPath, yamlString.getBytes());
				Files.write(syntaxPath, syntaxString.getBytes());

			} catch (IOException e1) {
				e1.printStackTrace();
				String errorMessage = "Could not create write files locally.";
				return new Status(
						Status.ERROR,
						"org.eclipse.ice.item.nuclear",
						1, errorMessage, null);
			}

		}
		
		return Status.OK_STATUS;
	}

}
