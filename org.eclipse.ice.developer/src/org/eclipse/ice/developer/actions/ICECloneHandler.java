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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.IParameterValues;
import org.eclipse.core.commands.ParameterValuesException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.internal.util.ProjectUtil;
import org.eclipse.egit.core.op.CloneOperation.PostCloneTask;
import org.eclipse.egit.ui.Activator;
import org.eclipse.egit.ui.internal.UIText;
import org.eclipse.egit.ui.internal.clone.ProjectRecord;
import org.eclipse.egit.ui.internal.clone.ProjectUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * The ICECloneHandler clones the ICE repository and adds PostCloneTasks that
 * clone the VisIt Java Client and the ICE Dependencies plugin. It also imports
 * all resulting Eclipse projects.
 * 
 * @author Alex McCaskey
 *
 */
@SuppressWarnings("restriction")
public class ICECloneHandler extends GitCloneHandler {

	/**
	 * This implementation of addPostCloneTask adds an action for importing all
	 * cloned projects.
	 */
	protected void addPostCloneTasks() {

		// Add a post clone task that clones the visit java client
		cloneOperation.addPostCloneTask(new PostCloneTask() {
			@Override
			public void execute(Repository repository, IProgressMonitor monitor) throws CoreException {
				cloneICEDependency("https://github.com/visit-vis/visit_java_client");
			}
		});

		// Add a post clone task that clones the dependencies bundle
		cloneOperation.addPostCloneTask(new PostCloneTask() {
			@Override
			public void execute(Repository repository, IProgressMonitor monitor) throws CoreException {
				cloneICEDependency("https://github.com/jayjaybillings/ICEDeps");
			}
		});

		// Add a post clone task that clones the ICETests bundle
		/*cloneOperation.addPostCloneTask(new PostCloneTask() {
			@Override
			public void execute(Repository repository, IProgressMonitor monitor) throws CoreException {
				cloneICEDependency("https://github.com/jayjaybillings/ICETests");
			}
		});*/

		// Import all ICE projects
		cloneOperation.addPostCloneTask(new PostCloneTask() {
			@Override
			public void execute(Repository repository, IProgressMonitor monitor) throws CoreException {
				importProjects(repository, new IWorkingSet[0]);
			}

		});
	}

	/**
	 * This private method is used to import existing projects into the project
	 * explorer.
	 * 
	 * @param repository
	 * @param sets
	 */
	private void importProjects(final Repository repository, final IWorkingSet[] sets) {
		String repoName = Activator.getDefault().getRepositoryUtil().getRepositoryName(repository);
		Job importJob = new WorkspaceJob(MessageFormat.format(UIText.GitCloneWizard_jobImportProjects, repoName)) {

			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				List<File> files = new ArrayList<File>();
				ProjectUtil.findProjectFiles(files, repository.getWorkTree(), true, monitor);
				if (files.isEmpty()) {
					return Status.OK_STATUS;
				}

				Set<ProjectRecord> projectRecords = new LinkedHashSet<ProjectRecord>();
				for (File file : files) {
					projectRecords.add(new ProjectRecord(file));
				}
				try {
					ProjectUtils.createProjects(projectRecords, sets, monitor);
				} catch (InvocationTargetException | InterruptedException e) {
					Activator.logError(e.getLocalizedMessage(), e);
				}

				return Status.OK_STATUS;
			}
		};
		importJob.schedule();
	}

	/**
	 * This private method is used by ICECloneHandler to execute further repo
	 * clones for ICE dependencies after the main ICE clone has finished.
	 * 
	 * @param repo
	 */
	@SuppressWarnings("unchecked")
	private void cloneICEDependency(String repo) {
		// Get the services we need
		ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
		IHandlerService handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);

		// Clear the parameters map and add the repoURLID key-value
		// required by the GitCloneHandler
		handlerParameters.clear();
		handlerParameters.put("repoURLID", repo);
		
		// Create a Parameters list that matches the map
		ArrayList<IParameter> parameters = new ArrayList<IParameter>();
		
		// We need ICETests to be cloned to the home directory
		if (repo.contains("ICETests")) {
			handlerParameters.put("cloneDirectory", System.getProperty("user.home"));
			parameters.add(new IParameter() {
				@Override
				public String getId() {
					return "cloneDirectory";
				}

				@Override
				public String getName() {
					return repo;
				}

				@Override
				public IParameterValues getValues() throws ParameterValuesException {
					return new IParameterValues() {
						@Override
						public Map getParameterValues() {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(getId(), getName());
							return map;
						};
					};
				}

				@Override
				public boolean isOptional() {
					return false;
				}
			});
		}
		
		// Add the Repo IParameter
		parameters.add(new IParameter() {
			@Override
			public String getId() {
				return "repoURLID";
			}

			@Override
			public String getName() {
				return repo;
			}

			@Override
			public IParameterValues getValues() throws ParameterValuesException {
				return new IParameterValues() {
					@Override
					public Map getParameterValues() {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(getId(), getName());
						return map;
					};
				};
			}

			@Override
			public boolean isOptional() {
				return false;
			}
		});

		// Create a new undefined command and then define it
		Command command = commandService.getCommand("org.eclipse.ice.developer.actions.GitCloneHandler." + repo);
		command.define("Clone ICE Dependency", "This is created Programatically!",
				commandService.getCategory("org.eclipse.ice.developer.command.category"),
				parameters.toArray(new IParameter[parameters.size()]));

		// Create the ParameterizedCommand to be executed
		ParameterizedCommand parameterizedCommand = ParameterizedCommand.generateCommand(command, handlerParameters);

		// Create a new GitCloneHandler to clone the given repo
		// Make sure it imports its projects afterwards.
		GitCloneHandler handler = new GitCloneHandler() {
			@Override
			protected void addPostCloneTasks() {
				cloneOperation.addPostCloneTask(new PostCloneTask() {
					@Override
					public void execute(Repository repository, IProgressMonitor monitor) throws CoreException {
						importProjects(repository, new IWorkingSet[0]);
					}
				});
			}
		};

		// Activate and execute the handler
		handlerService.activateHandler(parameterizedCommand.getId(), handler);
		try {
			handler.execute(handlerService.createExecutionEvent(parameterizedCommand, null));
		} catch (ExecutionException e) {
			e.printStackTrace();
			logger.error("Failed to clone " + repo, e);
		}

	}
}
