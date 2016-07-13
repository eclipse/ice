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
import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.RepositoryCache;
import org.eclipse.egit.core.RepositoryUtil;
import org.eclipse.egit.ui.Activator;
import org.eclipse.egit.ui.UIUtils;
import org.eclipse.egit.ui.internal.CommonUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.progress.IProgressConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ImportLocalICEHandler is a subclass of the ICECloneHandler that 
 * allows users to navigate to an existing, locally cloned ICE repository and 
 * import it into the Git Repositories view. It also imports all projects into the 
 * Project Explorer. 
 * 
 * @author Alex McCaskey
 *
 */
@SuppressWarnings("restriction")
public class ImportLocalICEHandler extends ICECloneHandler {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(ImportLocalICEHandler.class);

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.developer.actions.GitCloneHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Grab the Util and Cache for repositories
		RepositoryUtil repositoryUtil = Activator.getDefault().getRepositoryUtil();
		RepositoryCache cache = org.eclipse.egit.core.Activator.getDefault().getRepositoryCache();
		for (String dir : repositoryUtil.getConfiguredRepositories()) {
			File repoDir = new File(dir);
			try {
				Repository repo = cache.lookupRepository(repoDir);
				
				// FIXME Find a better way to find the recently added
				// ICE repo
				if (repo.getDirectory().getParentFile().getName().equals("ice")) {
					Job badJob = new Job("Import Local ICE") {
						@Override
						protected IStatus run(IProgressMonitor monitor) {
							return new Status(IStatus.ERROR, "org.eclipse.ice.developer", 1, "ICE is already cloned and imported into the Project Explorer.", null);
						}
					};
					// Start the job
					badJob.schedule();
					return null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Fix the maven build errors. 
		fixMavenLifecycleFile();
		
		// Execute the Add Repository EGit action
		IHandlerService service = CommonUtils.getService(PlatformUI.getWorkbench().getActiveWorkbenchWindow(),
				IHandlerService.class);
		UIUtils.executeCommand(service,
				"org.eclipse.egit.ui.RepositoriesViewAddRepository");
		
		// Find the ICE repository.
		for (String dir : repositoryUtil.getConfiguredRepositories()) {
			File repoDir = new File(dir);
			try {
				Repository repo = cache.lookupRepository(repoDir);
				
				// FIXME Find a better way to find the recently added
				// ICE repo
				if (repo.getDirectory().getParentFile().getName().equals("ice")) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							try {
								HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
										.showView(IProgressConstants.PROGRESS_VIEW_ID);
							} catch (PartInitException e) {
								e.printStackTrace();
							}
						}
					});
					importProjects(repo, new IWorkingSet[0]);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
