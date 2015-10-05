/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alex McCaskey - Initial API and implementation and/or initial documentation
 *
 *******************************************************************************/
package org.eclipse.ice.developer.actions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.op.CloneOperation;
import org.eclipse.jgit.transport.URIish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The GitCloneHandler is an AbstractHandler that provides an execute
 * implemenation that clones the repository with the given repoURLID found in
 * the ExecutionEvent IParameter map.
 * 
 * @author Alex McCaskey
 *
 */
@SuppressWarnings("restriction")
public class GitCloneHandler extends AbstractHandler {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(GitCloneHandler.class);

	/**
	 * Reference to the EGit CloneOperation to be used in this clone action
	 */
	protected CloneOperation cloneOperation;

	/**
	 * Reference to the Map of Parameters provided by the Extension Registry for
	 * this Handler.
	 */
	protected Map handlerParameters;

	/**
	 * Reference to the location that this handler should 
	 * clone the repo to. 
	 */
	protected File cloneLocation;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the repository
		handlerParameters = event.getParameters();

		// Get the Repo URL
		String repo = (String) handlerParameters.get("repoURLID");

		// Make sure we have a valid URL.
		if (repo == null) {
			logger.error("Error! This GitCloneHandler was not given a valid parameter map "
					+ "from Eclipse. No Repo URL found.");
			return null;
		}

		// Get the name of the repository
		String repoName = repo.substring(repo.lastIndexOf("/") + 1, repo.length());

		// Get the initial branch to check out. 
		String branch = handlerParameters.get("branchID") == null ? "master" : (String) handlerParameters.get("branchID");
		
		// See if we have been given a cloneDirectory parameter. 
		String directory = (String) handlerParameters.get("cloneDirectory");

		// If not, then clone this repo to the current Workspace.
		if (directory == null) {
			// Create the File reference for the clone location
			cloneLocation = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()
					+ System.getProperty("file.separator") + repoName);
		} else {
			cloneLocation = new File(directory + System.getProperty("file.separator") + repoName);
		}

		// Make sure there isn't already a cloned repo with the same name
		if (cloneLocation.exists()) {
			logger.info("This repository already exists at " + cloneLocation.getAbsolutePath()
					+ ". ICE will not perform this clone operation. Perform a Git Pull to update the "
					+ "current repository's contents.");
			return null;
		}

		// Create the Job to be executed
		final Job job = new WorkspaceJob("Cloning " + repo) {

			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {

				try {
					// FIXME MASTER OR NEXT MAKE IT VARIABLE
					cloneOperation = new CloneOperation(new URIish(repo), true, null, cloneLocation, branch, "origin",
							100);
					addPostCloneTasks();
					cloneOperation.run(monitor);
				} catch (URISyntaxException | InvocationTargetException | InterruptedException e) {
					e.printStackTrace();
					String message = "Git clone operation failed with the following stacktrace:\n" + e.getMessage();
					logger.error(message, e);
					new Status(IStatus.ERROR, "org.eclipse.ice.developer.action.GitCloneHandler", 1, message, null);
				}

				return Status.OK_STATUS;
			}
		};

		job.schedule();
		return null;
	}

	/**
	 * This method is for subclasses to implement and add any number of
	 * org.eclipse.egit.core.op.CloneOperation.PostCloneTask objects to be
	 * executed by the EGit CloneOperation after the clone has completed. It is
	 * guaranteed that the protected CloneOperation attribute will be
	 * instantiated before this method is called, so subclasses can just create
	 * new PostCloneTask implementations and invoke addPostCloneTask on this
	 * GitCloneHandler's cloneOperation attribute.
	 */
	protected void addPostCloneTasks() {
		return;
	}

}
