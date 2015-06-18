/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.client.widgets.mesh.test;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.client.widgets.ToggleAxesAction;
import org.eclipse.ice.client.widgets.ToggleHUDAction;
import org.eclipse.ice.client.widgets.jme.MasterApplication;
import org.eclipse.ice.client.widgets.jme.ViewAppState;
import org.eclipse.ice.client.widgets.mesh.IMeshSelectionListener;
import org.eclipse.ice.client.widgets.mesh.MeshAppState;
import org.eclipse.ice.client.widgets.mesh.MeshAppStateMode;
import org.eclipse.ice.client.widgets.mesh.MeshAppStateModeFactory;
import org.eclipse.ice.client.widgets.mesh.MeshAppStateModeFactory.Mode;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;

/**
 * This class can be used to test an embedded, custom {@link ViewAppState}. This
 * test in particular tests the <code>MeshEditor</code>'s view.
 * 
 * @author Jordan Deyton
 * 
 */
public class MeshViewLauncher {

	public static void main(String[] args) {
		// Create the display and shell.
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();

		// Create the application and widgets that can create and dispose of jME
		// views.
		final MasterApplication app = MasterApplication.createApplication();

		// Set the shell's Layout and create a SashForm with a left and right
		// Composite.
		shell.setLayout(new GridLayout(1, false));

		// Create a ToolBar.
		ToolBar toolBar = new ToolBar(shell, SWT.NONE);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		ToolBarManager manager = new ToolBarManager(toolBar);

		// Wait until the Application is initialized.
		app.blockUntilInitialized(0);

		// Create a mesh view, start it, and embed it in the shell.
		MeshAppState view = new MeshAppState();
		view.start(app);

		// Fill the ToolBar.
		createActions(manager, view);

		// Render the view in a Composite.
		Composite viewComposite = view.createComposite(shell);
		viewComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Lay out the shell.
		shell.layout();

		// ---- Add a mesh to the view. ---- //
		view.getSelectionManager().addMeshApplicationListener(
				new IMeshSelectionListener() {
					@Override
					public void selectionChanged() {
						System.out
								.println("MeshApplication: Selection changed!");
					}
				});
		view.setMesh(new MeshComponent());
		// --------------------------------- //

		// Close the display and shell.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

		// We *should* dispose the Composite that contains the embedded view
		// first, but it is not required.
		viewComposite.dispose();
		System.out.println("Composite disposed.");
		// Stop the ViewAppState and the Application.
		view.stop();
		System.out.println("View stopped");
		app.stop();
		System.out.println("App stopped");

		return;
	}

	private static void createActions(final ToolBarManager manager,
			final MeshAppState meshView) {

		Action action;
		ActionTree actionTree;

		// Create the drop down for switching between add and modify modes
		actionTree = new ActionTree("Mode");
		// Use a MeshAppStateModeFactory to get the available modes and create
		// ActionTrees for each one to go in the Mode menu.
		MeshAppStateModeFactory factory = meshView.getModeFactory();
		for (Mode type : factory.getAvailableModes()) {
			final MeshAppStateMode mode = factory.getMode(type);
			action = new Action() {
				@Override
				public void run() {
					meshView.setMode(mode);
				}
			};
			// Set the Action's text and tool tip.
			action.setText(mode.getName());
			action.setToolTipText(mode.getDescription());
			actionTree.add(new ActionTree(action));
		}
		manager.add(actionTree.getContributionItem());

		// Create the drop down to reset the camera placement or zoom
		// TODO create the camera reset action

		// Create the toggle switch to show or hide the heads-up display
		action = new ToggleHUDAction(meshView);
		actionTree = new ActionTree(action);
		manager.add(actionTree.getContributionItem());

		// Create the toggle switch to show or hide the axes.
		action = new ToggleAxesAction(meshView);
		actionTree = new ActionTree(action);
		manager.add(actionTree.getContributionItem());

		// Create the button to delete mesh elements.
		action = new Action() {
			@Override
			public void run() {
				meshView.getSelectionManager().deleteSelection();
			}
		};
		action.setText("Delete");
		action.setToolTipText("Remove the selected element from the mesh");
		actionTree = new ActionTree(action);
		manager.add(actionTree.getContributionItem());

		// Force an update for the ToolBarManager.
		manager.update(true);

		return;
	}
}
