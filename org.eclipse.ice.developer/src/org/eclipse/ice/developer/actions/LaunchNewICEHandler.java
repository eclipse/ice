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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extends the Eclipse AbstractHandler to provide an action to 
 * the ICE Developer Menu that let's users launch a new instance of ICE with a 
 * the addition of new plugins and without having to go through the usual 
 * Eclipse Run Configuration wizard. 
 * 
 * @author Alex McCaskey
 *
 */
public class LaunchNewICEHandler extends AbstractHandler {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(GitCloneHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the Launch Manager
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();

		if (manager != null) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject productProject = root.getProject("org.eclipse.ice.product");

			if (productProject != null) {

				// Local Declarations
				ArrayList<String> currentPlugins = new ArrayList<String>();
				ArrayList<String> notIncludedProjects = new ArrayList<String>();
				String fileName = "", currentPluginsStr = "", key = "selected_workspace_plugins";
				String vmArgs = "", vmArgsKey = "org.eclipse.jdt.launching.VM_ARGUMENTS";
				String[] plugins;

				// Get the proper file name
				if (SystemUtils.IS_OS_WINDOWS) {
					fileName = "ice.product_WINDOWS.launch";
				} else if (SystemUtils.IS_OS_MAC_OSX) {
					fileName = "ice.macosx_product.launch";
				} else if (SystemUtils.IS_OS_LINUX) {
					fileName = "ice.product_linux.launch";
				} else {
					throw new ExecutionException("Could not get the current OS. Cannot launch new version of ICE.");
				}

				// Get the Launch Configurations files
				IFile launchICE = productProject.getFile("modified_" + fileName);
				if (!launchICE.exists()) {
					IFile tempCopy = productProject.getFile(fileName);
					try {
						launchICE.create(tempCopy.getContents(), true, null);
					} catch (CoreException e1) {
						e1.printStackTrace();
						logger.error("Could not create file withe name modified_" + fileName, e1);
					}
				}

				// Get the Launch Configuration from those files
				ILaunchConfiguration launchConfig = manager.getLaunchConfiguration(launchICE);
				try {
					currentPluginsStr = launchConfig.getAttribute(key, "");
					vmArgs = launchConfig.getAttribute(vmArgsKey, "");
					vmArgs = vmArgs.replace("org.eclipse.equinox.http.jetty.http.port=8081", "org.eclipse.equinox.http.jetty.http.port=8082");
					
					// Put all workspace plugins in the launch config into a
					// list
					plugins = currentPluginsStr.split(",");
					for (String s : plugins) {
						currentPlugins.add(s.split("@")[0]);
					}

					// Get any project name that is not currently in the run
					// config
					List<String> pluginsToIgnore = Arrays.asList("org.eclipse.ice.aggregator", "reflectivity", "xsede",
							"ICEDocCleaner", "all", "default", "developerMenu", "dynamicUI", "fileFormats",
							"installation", "introToPTP", "itemDB", "moose-tutorial", "newItemGeneration",
							"org.eclipse.ice.examples.reflectivity", "org.eclipse.ice.installer",
							"org.eclipse.ice.parent", "org.eclipse.ice.product", "org.eclipse.ice.repository");
					for (IProject p : root.getProjects()) {
						String name = p.getName();
						if (!currentPlugins.contains(p.getName()) && !name.endsWith(".test")
								&& !name.contains("Tutorial")
								&& !name.contains("target.") && !name.contains("feature") 
								&& !pluginsToIgnore.contains(name)) {
							notIncludedProjects.add(p.getName());
						}
					}

					// Display a list selection for the user to select which
					// plugins to add.
					ListSelectionDialog dialog = new ListSelectionDialog(
							PlatformUI.getWorkbench().getDisplay().getActiveShell(), notIncludedProjects.toArray(),
							ArrayContentProvider.getInstance(), new LabelProvider(),
							"Select new plugins to include in the new ICE instance.");
					dialog.setTitle("Plugin Addition Selection");

					// Open the dialog
					int ok = dialog.open();

					if (ok == Window.OK) {
						// Get the results
						Object[] results = dialog.getResult();
						for (Object s : results) {
							currentPluginsStr = s.toString() + "@default:true," + currentPluginsStr;
						}

						ILaunchConfigurationWorkingCopy wc = launchConfig.getWorkingCopy();
						
						// Set the launch configs list of plugins
						wc.setAttribute(key, currentPluginsStr);
						wc.setAttribute(vmArgsKey, vmArgs);
						
						// Save the configuration
						ILaunchConfiguration config = wc.doSave();
						
						// Create and launch the Job.
						Job job = new WorkspaceJob("Launching New Instance of ICE") {

							@Override
							public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
								config.launch(ILaunchManager.RUN_MODE, monitor);
								return Status.OK_STATUS;
							}

						};

						// Launch the job
						job.schedule();
					}
				} catch (CoreException e) {
					e.printStackTrace();
					logger.error("Could not create or launch new run configuration.", e);
				}

			}

		} 

		return null;
	}

}
