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
package org.eclipse.ice.client.widgets.moose.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CIncludePathEntry;
import org.eclipse.cdt.core.settings.model.CSourceEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICFolderDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSetting;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.ICSourceEntry;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.make.core.IMakeCommonBuildInfo;
import org.eclipse.cdt.make.core.IMakeTarget;
import org.eclipse.cdt.make.core.IMakeTargetManager;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.cdt.managedbuilder.core.IBuilder;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedProject;
import org.eclipse.cdt.managedbuilder.ui.wizards.CfgHolder;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.ice.client.widgets.moose.nature.MooseNature;
import org.eclipse.ice.client.widgets.moose.wizards.ForkStorkWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * The ForkStorkHandler displays a Wizard to the user to gather a new MOOSE
 * application name and the users GitHub credentials, and then forks
 * idaholab/stork and renames the repository to the provided application name.
 * Additionally, it imports the project as a CDT Makefile project with existing
 * code, creates a new Make Target, and adds the appropriate MOOSE include files
 * to the Paths and Symbols preference page.
 * 
 * @author Alex McCaskey
 *
 */
public class ForkStorkHandler extends AbstractHandler {

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Local Declarations
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

		// Create a new ForkStorkWizard and Dialog
		final ForkStorkWizard wizard = new ForkStorkWizard();
		WizardDialog dialog = new WizardDialog(shell, wizard);

		// Open the dialog
		if (dialog.open() != 0) {
			return null;
		}

		// Create a File reference to the repo in the Eclipse workspace
		final Job job = new Job("Forking the MOOSE Stork!") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(
						"Attempting to Fork the Stork with supplied credentials",
						100);

				// Local Declarations
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				ArrayList<String> cmdList = new ArrayList<String>();
				ProcessBuilder jobBuilder = null;
				String os = System.getProperty("os.name");
				String sep = System.getProperty("file.separator");

				// Get the user specified data
				String appName = wizard.getMooseAppName();
				String gitHubUser = wizard.getGitUsername();
				String password = wizard.getGitPassword();

				// Construct the Remote URI for the repo
				String remoteURI = "https://github.com/" + gitHubUser + "/"
						+ appName;

				// Create the workspace file
				File workspaceFile = new File(ResourcesPlugin.getWorkspace()
						.getRoot().getLocation().toOSString()
						+ sep + appName);

				// Set the pyton command
				cmdList.add("/bin/bash");
				cmdList.add("-c");
				cmdList.add("python make_new_application.py");

				// Create a EGit-GitHub RepositoryService and Id to
				// connect and create our Fork
				RepositoryService service = new RepositoryService();
				RepositoryId id = new RepositoryId("idaholab", "stork");

				// Set the user's GitHub credentials
				service.getClient().setCredentials(gitHubUser, password);
				monitor.subTask("Connecting to GitHub...");
				monitor.worked(20);

				// Fork the Repository!!!
				try {
					// Fork and get the repo
					Repository repo = service.forkRepository(id);

					// Reset the project name to the provided app name
					Map<String, Object> fields = new HashMap<String, Object>();
					fields.put("name", appName);

					// Edit the name
					service.editRepository(repo, fields);
				} catch (IOException e1) {
					e1.printStackTrace();
					String errorMessage = "ICE failed in forking the new stork.";
					return new Status(Status.ERROR,
							"org.eclipse.ice.client.widgets.moose", 1,
							errorMessage, null);
				}

				monitor.subTask("Stork Forked, cloning to local machine...");
				monitor.worked(40);

				// Now that it is all set on the GitHub end,
				// Let's pull it down into our workspace
				try {
					Git result = Git.cloneRepository().setURI(remoteURI)
							.setDirectory(workspaceFile).call();
				} catch (GitAPIException e1) {
					e1.printStackTrace();
					String errorMessage = "ICE failed in cloning the new application.";
					return new Status(Status.ERROR,
							"org.eclipse.ice.client.widgets.moose", 1,
							errorMessage, null);
				}

				monitor.subTask("Executing make_new_application.py...");
				monitor.worked(60);

				// We can only run the python script on Linux or Mac
				// And Moose devs only use Linux or Macs to build their apps
				if (os.contains("Linux") || os.contains("Mac")) {
					// Create the ProcessBuilder and change to the project dir
					jobBuilder = new ProcessBuilder(cmdList);
					jobBuilder.directory(new File(workspaceFile
							.getAbsolutePath()));

					// Do not direct the error to stdout. Catch it separately.
					jobBuilder.redirectErrorStream(false);
					try {
						// Execute the python script!
						jobBuilder.start();
					} catch (IOException e) {
						e.printStackTrace();
						String errorMessage = "ICE could not execute the make_new_application python script.";
						return new Status(Status.ERROR,
								"org.eclipse.ice.client.widgets.moose", 1,
								errorMessage, null);
					}
				}

				/*------------ The rest is about importing the C++ project correctly ---------*/

				// Get the project and project description handles
				IProject project = workspace.getRoot().getProject(appName);
				IProjectDescription description = workspace
						.newProjectDescription(appName);

				monitor.subTask("Converting application to CDT C++ Project...");
				monitor.worked(80);

				try {
					// Create the CDT Project
					CCorePlugin.getDefault().createCDTProject(description,
							project, new NullProgressMonitor());

					// Add the CPP nature
					CCProjectNature.addCCNature(project,
							new NullProgressMonitor());

					// Set up build information
					ICProjectDescriptionManager pdMgr = CoreModel.getDefault()
							.getProjectDescriptionManager();
					ICProjectDescription projDesc = pdMgr
							.createProjectDescription(project, false);
					ManagedBuildInfo info = ManagedBuildManager
							.createBuildInfo(project);
					ManagedProject mProj = new ManagedProject(projDesc);
					info.setManagedProject(mProj);

					// Grab the correct toolchain
					// FIXME this should be better...
					IToolChain toolChain = null;
					for (IToolChain tool : ManagedBuildManager
							.getRealToolChains()) {
						if (os.contains("Mac")
								&& tool.getName().contains("Mac")
								&& tool.getName().contains("GCC")) {
							toolChain = tool;
							break;
						} else if (os.contains("Linux")
								&& tool.getName().contains("Linux")
								&& tool.getName().contains("GCC")) {
							toolChain = tool;
							break;
						} else if (os.contains("Windows")
								&& tool.getName().contains("Cygwin")) {
							toolChain = tool;
							break;
						} else {
							toolChain = null;
						}
					}

					// Set up the Build configuratino
					CfgHolder cfgHolder = new CfgHolder(toolChain, null);
					String s = toolChain == null ? "0" : toolChain.getId(); //$NON-NLS-1$
					IConfiguration config = new Configuration(
							mProj,
							(org.eclipse.cdt.managedbuilder.internal.core.ToolChain) toolChain,
							ManagedBuildManager.calculateChildId(s, null),
							cfgHolder.getName());
					IBuilder builder = config.getEditableBuilder();
					builder.setManagedBuildOn(false);
					CConfigurationData data = config.getConfigurationData();
					projDesc.createConfiguration(
							ManagedBuildManager.CFG_DATA_PROVIDER_ID, data);
					pdMgr.setProjectDescription(project, projDesc);

					// Now create a default Make Target for the Moose user to
					// use to
					// build the new app
					IProject cProject = projDesc.getProject();
					IMakeTargetManager manager = MakeCorePlugin.getDefault()
							.getTargetManager();
					String[] ids = manager.getTargetBuilders(cProject);
					IMakeTarget target = manager.createTarget(cProject,
							"make all", ids[0]);
					target.setStopOnError(false);
					target.setRunAllBuilders(false);
					target.setUseDefaultBuildCmd(false);
					target.setBuildAttribute(
							IMakeCommonBuildInfo.BUILD_COMMAND, "make");
					target.setBuildAttribute(IMakeTarget.BUILD_LOCATION,
							cProject.getLocation().toOSString());
					target.setBuildAttribute(IMakeTarget.BUILD_ARGUMENTS, "");
					target.setBuildAttribute(IMakeTarget.BUILD_TARGET, "all");
					manager.addTarget(cProject, target);

					// Set the include and src folders as actual CDT source
					// folders
					ICProjectDescription cDescription = CoreModel.getDefault()
							.getProjectDescriptionManager()
							.createProjectDescription(cProject, false);
					ICConfigurationDescription cConfigDescription = cDescription
							.createConfiguration(
									ManagedBuildManager.CFG_DATA_PROVIDER_ID,
									config.getConfigurationData());
					cDescription.setActiveConfiguration(cConfigDescription);
					cConfigDescription.setSourceEntries(null);
					IFolder srcFolder = cProject.getFolder("src");
					IFolder includeFolder = cProject.getFolder("include");
					ICSourceEntry srcFolderEntry = new CSourceEntry(srcFolder,
							null, ICSettingEntry.RESOLVED);
					ICSourceEntry includeFolderEntry = new CSourceEntry(
							includeFolder, null, ICSettingEntry.RESOLVED);
					cConfigDescription.setSourceEntries(new ICSourceEntry[] {
							srcFolderEntry, includeFolderEntry });

					// Add the Moose include paths
					ICProjectDescription projectDescription = CoreModel
							.getDefault().getProjectDescription(cProject, true);
					ICConfigurationDescription configDecriptions[] = projectDescription
							.getConfigurations();
					for (ICConfigurationDescription configDescription : configDecriptions) {
						ICFolderDescription projectRoot = configDescription
								.getRootFolderDescription();
						ICLanguageSetting[] settings = projectRoot
								.getLanguageSettings();
						for (ICLanguageSetting setting : settings) {
							List<ICLanguageSettingEntry> includes = getIncludePaths();
							includes.addAll(setting
									.getSettingEntriesList(ICSettingEntry.INCLUDE_PATH));
							setting.setSettingEntries(
									ICSettingEntry.INCLUDE_PATH, includes);
						}
					}
					CoreModel.getDefault().setProjectDescription(cProject,
							projectDescription);

					// Add a MOOSE Project Nature
					IProjectDescription desc = project.getDescription();
					String[] prevNatures = desc.getNatureIds();
					String[] newNatures = new String[prevNatures.length + 1];
					System.arraycopy(prevNatures, 0, newNatures, 0,
							prevNatures.length);
					newNatures[prevNatures.length] = MooseNature.NATURE_ID;
					desc.setNatureIds(newNatures);
					project.setDescription(desc, new NullProgressMonitor());

				} catch (CoreException e) {
					e.printStackTrace();
					String errorMessage = "ICE could not import the new MOOSE application as a C++ project.";
					return new Status(Status.ERROR,
							"org.eclipse.ice.client.widgets.moose", 1,
							errorMessage, null);
				}

				monitor.subTask("Importing into ICE.");
				monitor.worked(100);
				monitor.done();
				return Status.OK_STATUS;
			}

		};

		job.schedule();

		// FIXME SHOULD WE CHECK IF MOOSE IS IN THE WORKSPACE
		// AND CLONE IT IF ITS NOT

		return null;
	}

	/**
	 * Private method used basically to just compartimentalize all the include
	 * additions for the MOOSE build system.
	 * 
	 * @return
	 */
	private List<ICLanguageSettingEntry> getIncludePaths() {
		List<ICLanguageSettingEntry> includes = new ArrayList<ICLanguageSettingEntry>();

		includes.add(new CIncludePathEntry("/moose/framework/include/actions",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/auxkernels",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/base",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/bcs",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/constraints",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/dampers",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/dgkernels",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/dirackernels",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/executioners",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/functions",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/geomsearch",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/ics",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/indicators",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/kernels",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/markers",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/materials",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/mesh",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/meshmodifiers",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/multiapps",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/outputs",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/parser",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/postprocessors",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/preconditioners",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/predictors",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/restart",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/splits",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/timeintegrators",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/timesteppers",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/userobject",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/framework/include/utils",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry(
				"/moose/framework/include/vectorpostprocessors",
				ICSettingEntry.VALUE_WORKSPACE_PATH));
		includes.add(new CIncludePathEntry("/moose/libmesh/installed/include",
				ICSettingEntry.VALUE_WORKSPACE_PATH));

		return includes;
	}
}