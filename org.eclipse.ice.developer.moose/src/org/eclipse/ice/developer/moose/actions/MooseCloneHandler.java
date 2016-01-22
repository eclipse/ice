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
package org.eclipse.ice.developer.moose.actions;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.egit.core.op.CloneOperation.PostCloneTask;
import org.eclipse.ice.developer.actions.GitCloneHandler;
import org.eclipse.ice.developer.moose.nature.MooseNature;
import org.eclipse.jgit.lib.Repository;

/**
 * This handler triggers the "Clone MOOSE" action.
 * 
 * @author Alex McCaskey
 *
 */
@SuppressWarnings("restriction")
public class MooseCloneHandler extends GitCloneHandler {

	/**
	 * This implementation of addPostCloneTasks attempts to show a Existing
	 * Project Import Wizard.
	 */
	protected void addPostCloneTasks() {
		cloneOperation.addPostCloneTask(new ImportMooseTask());
	}

	/**
	 * 
	 * @author Alex McCaskey
	 *
	 */
	private class ImportMooseTask implements PostCloneTask {

		@Override
		public void execute(Repository repository, IProgressMonitor monitor) throws CoreException {

			// Get the project and project description handles
			String os = System.getProperty("os.name");
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("moose");
			IProjectDescription description = workspace.newProjectDescription("moose");

			monitor.subTask("Converting application to CDT C++ Project...");

			try {
				// Create the CDT Project
				CCorePlugin.getDefault().createCDTProject(description, project, new NullProgressMonitor());

				// Add the CPP nature
				CCProjectNature.addCCNature(project, new NullProgressMonitor());

				// Set up build information
				ICProjectDescriptionManager pdMgr = CoreModel.getDefault().getProjectDescriptionManager();
				ICProjectDescription projDesc = pdMgr.createProjectDescription(project, false);
				ManagedBuildInfo info = ManagedBuildManager.createBuildInfo(project);
				ManagedProject mProj = new ManagedProject(projDesc);
				info.setManagedProject(mProj);

				// Grab the correct toolchain
				// FIXME this should be better...
				IToolChain toolChain = null;
				for (IToolChain tool : ManagedBuildManager.getRealToolChains()) {
					if (os.contains("Mac") && tool.getName().contains("Mac") && tool.getName().contains("GCC")) {
						toolChain = tool;
						break;
					} else if (os.contains("Linux") && tool.getName().contains("Linux")
							&& tool.getName().contains("GCC")) {
						toolChain = tool;
						break;
					} else if (os.contains("Windows") && tool.getName().contains("Cygwin")) {
						toolChain = tool;
						break;
					} else {
						toolChain = null;
					}
				}

				// Set up the Build configuratino
				CfgHolder cfgHolder = new CfgHolder(toolChain, null);
				String s = toolChain == null ? "0" : toolChain.getId(); //$NON-NLS-1$
				IConfiguration config = new Configuration(mProj,
						(org.eclipse.cdt.managedbuilder.internal.core.ToolChain) toolChain,
						ManagedBuildManager.calculateChildId(s, null), cfgHolder.getName());
				IBuilder builder = config.getEditableBuilder();
				builder.setManagedBuildOn(false);
				CConfigurationData data = config.getConfigurationData();
				projDesc.createConfiguration(ManagedBuildManager.CFG_DATA_PROVIDER_ID, data);
				pdMgr.setProjectDescription(project, projDesc);

				// Now create a default Make Target for the Moose user to
				// use to
				// build the new app
				IProject cProject = projDesc.getProject();
				IMakeTargetManager manager = MakeCorePlugin.getDefault().getTargetManager();
				String[] ids = manager.getTargetBuilders(cProject);
				IMakeTarget mooseTarget = manager.createTarget(cProject, "Build Moose", ids[0]);
				mooseTarget.setStopOnError(false);
				mooseTarget.setRunAllBuilders(false);
				mooseTarget.setUseDefaultBuildCmd(false);
				mooseTarget.setBuildAttribute(IMakeCommonBuildInfo.BUILD_COMMAND, "make -j $MOOSE_JOBS -C framework");
				mooseTarget.setBuildAttribute(IMakeCommonBuildInfo.BUILD_LOCATION, cProject.getLocation().toOSString());
				mooseTarget.setBuildAttribute(IMakeCommonBuildInfo.BUILD_ARGUMENTS, "");
				mooseTarget.setBuildAttribute(IMakeTarget.BUILD_TARGET, "");
				manager.addTarget(cProject, mooseTarget);

				IMakeTarget libmeshTarget = manager.createTarget(cProject, "Build LibMesh", ids[0]);
				libmeshTarget.setStopOnError(false);
				libmeshTarget.setRunAllBuilders(false);
				libmeshTarget.setUseDefaultBuildCmd(false);
				libmeshTarget.setBuildAttribute(IMakeCommonBuildInfo.BUILD_COMMAND,
						"sh scripts/update_and_rebuild_libmesh.sh");
				libmeshTarget.setBuildAttribute(IMakeCommonBuildInfo.BUILD_LOCATION,
						cProject.getLocation().toOSString());
				libmeshTarget.setBuildAttribute(IMakeCommonBuildInfo.BUILD_ARGUMENTS, "");
				libmeshTarget.setBuildAttribute(IMakeTarget.BUILD_TARGET, "");
				manager.addTarget(cProject, libmeshTarget);

				// Set the include and src folders as actual CDT source
				// folders
				ICProjectDescription cDescription = CoreModel.getDefault().getProjectDescriptionManager()
						.createProjectDescription(cProject, false);
				ICConfigurationDescription cConfigDescription = cDescription
						.createConfiguration(ManagedBuildManager.CFG_DATA_PROVIDER_ID, config.getConfigurationData());
				cDescription.setActiveConfiguration(cConfigDescription);
				cConfigDescription.setSourceEntries(null);
				IFolder frameworkFolder = cProject.getFolder("framework");
				IFolder srcFolder = frameworkFolder.getFolder("src");
				IFolder includeFolder = frameworkFolder.getFolder("include");
				ICSourceEntry srcFolderEntry = new CSourceEntry(srcFolder, null, ICSettingEntry.RESOLVED);
				ICSourceEntry includeFolderEntry = new CSourceEntry(includeFolder, null, ICSettingEntry.RESOLVED);
				cConfigDescription.setSourceEntries(new ICSourceEntry[] { srcFolderEntry, includeFolderEntry });

				// Add the Moose include paths
				ICProjectDescription projectDescription = CoreModel.getDefault().getProjectDescription(cProject, true);
				ICConfigurationDescription configDecriptions[] = projectDescription.getConfigurations();
				for (ICConfigurationDescription configDescription : configDecriptions) {
					ICFolderDescription projectRoot = configDescription.getRootFolderDescription();
					ICLanguageSetting[] settings = projectRoot.getLanguageSettings();
					for (ICLanguageSetting setting : settings) {
						List<ICLanguageSettingEntry> includes = getIncludePaths(project);
						includes.addAll(setting.getSettingEntriesList(ICSettingEntry.INCLUDE_PATH));
						setting.setSettingEntries(ICSettingEntry.INCLUDE_PATH, includes);
					}
				}
				CoreModel.getDefault().setProjectDescription(cProject, projectDescription);

				// Add a MOOSE Project Nature
				IProjectDescription desc = project.getDescription();
				String[] prevNatures = desc.getNatureIds();
				String[] newNatures = new String[prevNatures.length + 1];
				System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
				newNatures[prevNatures.length] = MooseNature.NATURE_ID;
				desc.setNatureIds(newNatures);
				project.setDescription(desc, new NullProgressMonitor());

			} catch (CoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
				String errorMessage = "ICE could not import the new MOOSE application as a C++ project.";
				// return new Status(IStatus.ERROR,
				// "org.eclipse.ice.client.widgets.moose", 1,
				// errorMessage, null);
			}
		}

		/**
		 * This private method configures the include paths for the CDT project.
		 * 
		 * @param mooseProject
		 * @return
		 */
		private List<ICLanguageSettingEntry> getIncludePaths(IProject mooseProject) {

			// Create a list of ICLanguageSettingEntry to hold the include paths. 
			List<ICLanguageSettingEntry> includes = new ArrayList<ICLanguageSettingEntry>();

			try {
				mooseProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}

			// Get the header file directory
			IFolder includeFolder = mooseProject.getFolder("framework").getFolder("include");

			// Create a File from that IFolder and search for all its
			// sub directories
			File includeDirFile = includeFolder.getLocation().toFile();
			String[] directories = includeDirFile.list(new FilenameFilter() {
				@Override
				public boolean accept(File current, String name) {
					return new File(current, name).isDirectory();
				}
			});

			// Loop over all directories and add them to the include paths
			for (String directory : directories) {
				includes.add(new CIncludePathEntry("/moose/framework/include/" + directory,
						ICSettingEntry.VALUE_WORKSPACE_PATH));
			}
			
			// Add libmesh too
			includes.add(new CIncludePathEntry("/moose/libmesh/installed/include",
					ICSettingEntry.VALUE_WORKSPACE_PATH));

			return includes;

		}

	}

}