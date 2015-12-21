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
package org.eclipse.ice.item.jobLauncher;

import java.util.Dictionary;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ICEJob is a subclass of the Eclipse Job class that provides a run
 * implementation that executes a list of ICE Actions.
 * 
 * @author Alex McCaskey
 *
 */
public class ICEJob extends Job {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(ICEJob.class);

	/**
	 * The list of Actions to execute
	 */
	protected List<Action> actions;

	/**
	 * The key-value pairs to be used as input to each Action.
	 */
	protected Dictionary<String, String> actionDataMap;

	/**
	 * The Form Status produced by the Action executions.
	 */
	protected FormStatus status;

	/**
	 * Reference to the currently executing Action.
	 */
	protected Action currentlyRunningAction;

	/**
	 * The Constructor.
	 * 
	 * @param actionsToExecute
	 *            The list of Actions to execute in this Job
	 * @param map
	 *            The map of input parameters for each Action
	 * @param jobStatus
	 *            The reference to the FormStatus to update.
	 */
	public ICEJob(List<Action> actionsToExecute, Dictionary<String, String> map) {
		super("ICE Job Launch");
		actions = actionsToExecute;
		actionDataMap = map;
		status = FormStatus.Processing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		final int ticks = 100;
		int worked = 0;
		monitor.beginTask("Executing the Job Launch Action...", ticks);
		status = FormStatus.Processing;

		// Clear any existing Eclipse Console content
		Action.clearConsole();
		
		// Execute the Actions
		for (Action action : actions) {
			// Get a reference to the Current Action
			currentlyRunningAction = action;
			monitor.subTask("Executing " + currentlyRunningAction.getActionName() + "...");

			// Execute the Action
			FormStatus tempStatus = currentlyRunningAction.execute(actionDataMap);

			// If the Action returned with an InfoError status,
			// then we need to report that and throw an Error
			// Eclipse Status
			if (tempStatus.equals(FormStatus.InfoError)) {
				return error("Error in executing the " + action.getActionName() + " Action.", null);
			}

			// Check for Eclipse Job cancellations.
			if (monitor.isCanceled()) {
				currentlyRunningAction.cancel();
				status = FormStatus.Processed;
				return Status.CANCEL_STATUS;
			}

			// If the Action is still processing, then
			// lets just wait until its done.
			while (tempStatus.equals(FormStatus.Processing)) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// Complain
					return error("Error in executing the " + action.getActionName() + " Action.", e1);
				}
				tempStatus = currentlyRunningAction.getStatus();
			}

			// Increment the worked ticker
			worked += ticks / actions.size();
			monitor.worked(worked);
		}

		// Once done executing all Actions, indicate
		// so by setting the status flag.
		status = FormStatus.Processed;
		monitor.worked(ticks);
		monitor.done();
		return Status.OK_STATUS;
	}

	/**
	 * Return the current Form Status
	 * 
	 * @return
	 */
	public FormStatus getStatus() {
		return status;
	}

	/**
	 * Return the currently executing Action.
	 * 
	 * @return
	 */
	public Action getCurrentAction() {
		return currentlyRunningAction;
	}

	/**
	 * This operation cancels this Job from the Eclipse and ICE perspectives.
	 * 
	 * @return
	 */
	public FormStatus cancelICEJob() {
		// Cancel this Job
		cancel();

		// Cancel the Action.
		return currentlyRunningAction.cancel();
	}

	/**
	 * This protected utility method is used to report errors in the 
	 * Job execution. 
	 * 
	 * @param message
	 * @param e
	 * @return
	 */
	protected IStatus error(String message, Exception e) {
		if (e == null) {
			logger.error(message);
		} else {
			logger.error(message, e);
		}
		status = FormStatus.InfoError;
		return new Status(Status.ERROR, "org.eclipse.ice.item.jobLauncher.ICEJob", 1, message, null);
	}
}
