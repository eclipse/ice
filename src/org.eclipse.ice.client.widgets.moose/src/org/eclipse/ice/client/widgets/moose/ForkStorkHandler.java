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
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 
 * @author Alex McCaskey
 *
 */
public class ForkStorkHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Create a new Generate YAML Wizard and Dialog
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		ForkStorkWizard wizard = new ForkStorkWizard();
		WizardDialog dialog = new WizardDialog(shell, wizard);
		Git result = null;
		String sep = System.getProperty("file.separator");

		// Open the dialog
		if (dialog.open() != 0) {
			return null;
		}

		String appName = wizard.getMooseAppName();
		String gitHubUser = wizard.getGitUsername();
		String password = wizard.getGitPassword();
		String remoteURI = "https://github.com/" + gitHubUser + "/" + appName;
		File workspace = new File(ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().toOSString() + sep + appName);
		
		System.out.println(appName + ", " + gitHubUser + ", ");
		
		RepositoryService service = new RepositoryService();
		service.getClient().setCredentials(gitHubUser, password);
		RepositoryId id = new RepositoryId("idaholab", "stork");
		try {
			Repository repo = service.forkRepository(id);
			//GitHubClient client = service.getClient();
			System.out.println(repo.getCloneUrl() + ", " + repo.getName());
			
			Map<String, Object> fields = new HashMap<String, Object>();
			fields.put("name", appName);
			
			service.editRepository(repo, fields);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			result = Git.cloneRepository().setURI(remoteURI)
					.setDirectory(workspace).call();
		} catch (InvalidRemoteException e1) {
			e1.printStackTrace();
		} catch (TransportException e1) {
			e1.printStackTrace();
		} catch (GitAPIException e1) {
			e1.printStackTrace();
		}

		try {
			System.out.println("HELLO: " + result.status().call().toString());
		} catch (NoWorkTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result.close();

		return null;

	}
}
