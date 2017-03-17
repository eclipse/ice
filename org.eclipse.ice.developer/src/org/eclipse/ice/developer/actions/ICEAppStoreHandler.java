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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import apps.AppsFactory;
import apps.EnvironmentManager;
import apps.IEnvironment;
import apps.impl.EnvironmentManagerImpl;
import eclipseapps.EclipseappsFactory;

/**
 * The GitCloneHandler is an AbstractHandler that provides an execute
 * implemenation that clones the repository with the given repoURLID found in
 * the ExecutionEvent IParameter map.
 * 
 * @author Alex McCaskey
 *
 */
public class ICEAppStoreHandler extends AbstractHandler {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(ICEAppStoreHandler.class);

	private EnvironmentManager manager;

	private IEnvironment environment;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		manager = AppsFactory.eINSTANCE.createEnvironmentManager();
		manager.setEnvironmentStorage(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentStorage());
		manager.setConsole(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentConsole());
		manager.loadEnvironments();

		EList<String> envNames = manager.list();
		String createNewStr = "Create New Environment...";
		String[] envNamesArr = new String[1 + envNames.size()];
		envNamesArr[0] = createNewStr;
		int counter = 1;
		for (String s : envNames) {
			envNamesArr[counter] = s;
			counter++;
		}

		// Local Declarations
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

		ElementListSelectionDialog listDialog = new ElementListSelectionDialog(shell, new LabelProvider());
		listDialog.setElements(envNamesArr);
		listDialog.setTitle("Select existing environment, or create a new one.");
		// user pressed cancel
		if (listDialog.open() != Window.OK) {
			return null;
		}

		Object[] result = listDialog.getResult();
		String strResult = (String) result[0];

		if (strResult.equals(createNewStr)) {

			// Create a new ForkStorkWizard and Dialog
			final AppStoreWizard wizard = new AppStoreWizard();
			WizardDialog dialog = new WizardDialog(shell, wizard);

			// Open the dialog
			if (dialog.open() != 0) {
				return null;
			}

			// Get that Environment... and build and connect
			// FIXME this is a bad way to do this, we need 
			// a new service reference that returns the new Environment
			List<String> list = Arrays.asList(envNamesArr);
			manager.loadEnvironments();
			for (String s : manager.list()) {
				if (!list.contains(s)) {
					// This is our new environment
					environment = manager.get(s);
				}
			}
		} else {
			// If they asked for a current one, just get it
			environment = manager.get(strResult);
		}

		// Create the Job to be executed
		final Job job = new WorkspaceJob("Creating Environment") {

			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {

				environment.setProjectlauncher(EclipseappsFactory.eINSTANCE.createDockerPTPSyncProjectLauncher());
				if (!environment.build() || !environment.connect()) {
					String message = "Could not build or connect to the environment:\n";
					logger.error(message, new ExecutionException("Could not build or connect to the environment."));
					new Status(IStatus.ERROR, "org.eclipse.ice.developer.action.ICEAppStoreHandler", 1, message, null);
				}

				ICEAppStoreHandler.this.manager.persistEnvironments();
				return Status.OK_STATUS;
			}
		};

		job.schedule();

		// // Create the EnvironmentMangaer and set it up to use
		// // the Eclipse IPreferences to store IEnvironments
		// ICEAppStoreHandler.this.manager
		// .setEnvironmentStorage(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentStorage());
		// ICEAppStoreHandler.this.manager
		// .setConsole(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentConsole());
		//
		// // Show view to get Json string
		// String jsonStr = "{\n" + " \"General\": {\n" + " \"name\":
		// \"mccaskey/test_env\",\n"
		// + " \"type\": \"Docker\"\n" + " },\n" + " \"Application\": {\n"
		// + " \"type\": \"Source\",\n" + " \"name\": \"xacc\",\n"
		// + " \"repoURL\": \"https://github.com/ORNL-QCI/xacc\",\n"
		// + " \"buildCommand\": \"cd xacc && mkdir build && cd build && cmake
		// .. && make\"\n"
		// + " },\n" + " \"Dependencies\": [\n" + " {\n" + " \"type\":
		// \"OS\",\n"
		// + " \"name\": \"cmake\"\n" + " },\n" + " {\n"
		// + " \"type\": \"OS\",\n"
		// + " \"name\":
		// \"https://github.com/ORNL-QCI/ScaffCC/releases/download/v2.0/scaffold-2.0-1.fc25.x86_64.rpm\"\n"
		// + " },\n" + " {\n" + " \"type\": \"OS\",\n"
		// + " \"name\": \"boost-mpich-devel\"\n" + " },\n" + " {\n"
		// + " \"type\": \"OS\",\n" + " \"name\": \"mpich-devel\"\n" + " }\n"
		// + " ],\n" + " \"ContainerConfig\": {\n" + " \"name\": \"xaccdev\",\n"
		// + " \"ephemeral\": true\n" + " }\n" + "}";
		//
		// IEnvironment environment =
		// ICEAppStoreHandler.this.manager.create(jsonStr);
		// environment.setProjectlauncher(EclipseappsFactory.eINSTANCE.createDockerPTPSyncProjectLauncher());
		// if (!environment.build() || !environment.connect()) {
		// String message = "Could not build or connect to the environment:\n";
		// logger.error(message, new ExecutionException("Could not build or
		// connect to the environment."));
		// new Status(IStatus.ERROR,
		// "org.eclipse.ice.developer.action.ICEAppStoreHandler", 1, message,
		// null);
		// }
		//
		// ICEAppStoreHandler.this.manager.persistEnvironments();
		// return Status.OK_STATUS;
		// }
		// };
		//
		// job.schedule();

		return null;
	}

}
