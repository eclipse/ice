/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets.moose;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.IPDOMManager;
import org.eclipse.cdt.core.index.IIndexManager;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
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
import org.eclipse.cdt.core.settings.model.WriteAccessException;
import org.eclipse.cdt.core.settings.model.extension.CConfigurationData;
import org.eclipse.cdt.make.core.IMakeCommonBuildInfo;
import org.eclipse.cdt.make.core.IMakeTarget;
import org.eclipse.cdt.make.core.IMakeTargetManager;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.cdt.make.core.MakeProjectNature;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IProjectType;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.core.ManagedCProjectNature;
import org.eclipse.cdt.managedbuilder.internal.core.Configuration;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedProject;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.ice.client.widgets.EclipseStreamingTextWidget;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.item.nuclear.MOOSELauncher;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * The ForkStorkHandler displays a Wizard to the user to gather a new MOOSE
 * application name and the users GitHub credentials, and then forks
 * idaholab/stork and renames the repository to the provided application name.
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
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		String sep = System.getProperty("file.separator"), appName = "", gitHubUser = "", password = "", remoteURI = "";
		ArrayList<String> cmdList = new ArrayList<String>();
		ProcessBuilder jobBuilder = null;

		// Set the pyton command
		cmdList.add("/bin/bash");
		cmdList.add("-c");
		cmdList.add("python make_new_application.py");

		// Create a new ForkStorkWizard and Dialog
		ForkStorkWizard wizard = new ForkStorkWizard();
		WizardDialog dialog = new WizardDialog(shell, wizard);

		// Open the dialog
		if (dialog.open() != 0) {
			return null;
		}

		// Get the User Input Data
		appName = wizard.getMooseAppName();
		gitHubUser = wizard.getGitUsername();
		password = wizard.getGitPassword();

		// Construct the Remote URI for the repo
		remoteURI = "https://github.com/" + gitHubUser + "/" + appName;

		// Create a File reference to the repo in the Eclipse workspace
		File workspaceFile = new File(ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().toOSString()
				+ sep + appName);

		// Create a EGit-GitHub RepositoryService and Id to
		// connect and create our Fork
		RepositoryService service = new RepositoryService();
		RepositoryId id = new RepositoryId("idaholab", "stork");

		// Set the user's GitHub credentials
		service.getClient().setCredentials(gitHubUser, password);

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
		}

		// Now that it is all set on the GitHub end,
		// Let's pull it down into our workspace
		try {
			Git result = Git.cloneRepository().setURI(remoteURI)
					.setDirectory(workspaceFile).call();
		} catch (InvalidRemoteException e1) {
			e1.printStackTrace();
		} catch (TransportException e1) {
			e1.printStackTrace();
		} catch (GitAPIException e1) {
			e1.printStackTrace();
		}

		// Create the ProcessBuilder and change to the project dir
		jobBuilder = new ProcessBuilder(cmdList);
		jobBuilder.directory(new File(workspaceFile.getAbsolutePath()));

		// Do not direct the error to stdout. Catch it separately.
		jobBuilder.redirectErrorStream(false);
		try {
			// Execute the python script!
			jobBuilder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Pull this into the Project Explorer as CDT project!
		IProject appProject = root.getProject(appName), cProject = null;
		IWorkspaceDescription workspaceDesc = workspace.getDescription();
		workspaceDesc.setAutoBuilding(false);
		try {
			workspace.setDescription(workspaceDesc);

			IProjectDescription description = workspace
					.newProjectDescription(appProject.getName());

			cProject = CCorePlugin.getDefault().createCDTProject(description,
					appProject, null);

			if (!cProject.isOpen()) {
				cProject.open(null);
			}
			cProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}

		String projTypeId = "cdt.managedbuild.target.macosx.exe";

		// ICProjectDescription des =
		// CCorePlugin.getDefault().getProjectDescription(cProject, true);
		IManagedBuildInfo info = ManagedBuildManager.createBuildInfo(cProject);

		for (IProjectType t : ManagedBuildManager.getDefinedProjectTypes()) {
			System.out.println("PROJECT TYPE: " + t.getId());
		}

		try {
			IProjectType type = ManagedBuildManager.getProjectType(projTypeId);
			IManagedProject mProj = ManagedBuildManager.createManagedProject(
					cProject, type);
			// info.setManagedProject(mProj);

			IConfiguration cfgs[] = type.getConfigurations();
			IConfiguration config = mProj
					.createConfiguration(
							cfgs[0],
							ManagedBuildManager.calculateChildId(
									cfgs[0].getId(), null));

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
			ICSourceEntry srcFolderEntry = new CSourceEntry(srcFolder, null, ICSettingEntry.RESOLVED);
			ICSourceEntry includeFolderEntry = new CSourceEntry(includeFolder, null, ICSettingEntry.RESOLVED);

			cConfigDescription.setSourceEntries(new ICSourceEntry[]{srcFolderEntry, includeFolderEntry});
			
			info.setManagedProject(mProj);

			cDescription.setCdtProjectCreated();

			//IIndexManager indexMgr = CCorePlugin.getIndexManager();
			//ICProject proj = CoreModel.getDefault().getCModel()
			//		.getCProject(cProject.getName());
			//indexMgr.setIndexerId(proj, IPDOMManager.ID_FAST_INDEXER);
			CoreModel.getDefault()
					.setProjectDescription(cProject, cDescription);
			ManagedBuildManager.setDefaultConfiguration(cProject, config);
			ManagedBuildManager.setSelectedConfiguration(cProject, config);
			ManagedBuildManager.setNewProjectVersion(cProject);
			ManagedBuildManager.saveBuildInfo(cProject, true);

		} catch (CoreException e) {
			e.printStackTrace();
		} catch (BuildException b) {
			b.printStackTrace();
		}
		// IProjectDescription desc = ResourcesPlugin.getWorkspace()
		// .newProjectDescription(appName);
		// IProject cProject = null;
		// try {
		// cProject = CCorePlugin.getDefault().createCDTProject(desc,
		// appProject, null);
		// MakeProjectNature.addNature(cProject, null);
		// cProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		// } catch (OperationCanceledException e) {
		// e.printStackTrace();
		// } catch (CoreException e) {
		// e.printStackTrace();
		// }

		// Now create a default Make Target for the Moose user to use to
		// build the new app
		try {
			IMakeTargetManager manager = MakeCorePlugin.getDefault()
					.getTargetManager();
			String[] ids = manager.getTargetBuilders(cProject);
			IMakeTarget target = manager.createTarget(cProject, "make all",
					ids[0]);
			target.setStopOnError(false);
			target.setRunAllBuilders(false);
			target.setUseDefaultBuildCmd(true);
			target.setBuildAttribute(IMakeCommonBuildInfo.BUILD_COMMAND, "make");
			target.setBuildAttribute(IMakeTarget.BUILD_LOCATION, "");
			target.setBuildAttribute(IMakeTarget.BUILD_ARGUMENTS, "");
			target.setBuildAttribute(IMakeTarget.BUILD_TARGET, "all");
			manager.addTarget(cProject, target);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		String workspacePath = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().toOSString();
		ICProjectDescription projectDescription = CoreModel.getDefault()
				.getProjectDescription(cProject, true);
		ICConfigurationDescription configDecriptions[] = projectDescription
				.getConfigurations();
		for (ICConfigurationDescription configDescription : configDecriptions) {
			ICFolderDescription projectRoot = configDescription
					.getRootFolderDescription();
			ICLanguageSetting[] settings = projectRoot.getLanguageSettings();
			for (ICLanguageSetting setting : settings) {
				List<ICLanguageSettingEntry> includes = new ArrayList<ICLanguageSettingEntry>();
				includes.addAll(setting
						.getSettingEntriesList(ICSettingEntry.INCLUDE_PATH));

				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/actions",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/auxkernels",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/base", ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/bcs", ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/contraints",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/dampers",
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
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/ics",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/indicators",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/kernels",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/markers",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/materials",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/mesh",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/meshmodifiers",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/multiapps",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/outputs",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/parser",
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
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/restart",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/splits",
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
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/utils",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/framework/include/vectorpostprocessors",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				includes.add(new CIncludePathEntry(
						 "/moose/libmesh/installed/include",
						ICSettingEntry.VALUE_WORKSPACE_PATH));
				//includes.add(new CIncludePathEntry(appName + "/include/base",
				//		ICSettingEntry.VALUE_WORKSPACE_PATH));
				
				setting.setSettingEntries(ICSettingEntry.INCLUDE_PATH, includes);
			}
		}
		
		try {
			CoreModel.getDefault().setProjectDescription(cProject,
					projectDescription);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}