/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.reflectivity.ui;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.FrameworkUtil;

/**
 * This is a custom JFace dialog that shows the Task Launcher for the
 * Reflectivity tools.
 *
 * @author Jay Jay Billings
 *
 */
public class TaskLaunchDialog extends Dialog {

	/**
	 * The ICE IClient for creating the Items.
	 */
	@Inject
	private IClient client;

	/**
	 * The E4 context for manipulating the workbench. This is primarily used for
	 * opening the materials database editor.
	 */
	@Inject
	private IEclipseContext context;

	/**
	 * The constructor
	 *
	 * @param parentShell
	 *            The shell in which this dialog is drawn and which is also the
	 *            shell of the parent application.
	 */
	@Inject
	public TaskLaunchDialog(
			@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		super(parentShell);
	}

	/**
	 * This operation overrides Dialog.createButton so that no buttons are
	 * created.
	 */
	@Override
	public Button createButton(Composite parent, int id, String label,
			boolean defaultButton) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		// Local Declarations
		Composite swtComposite = (Composite) super.createDialogArea(parent);
		// Set the layout to one so that everything will stack
		((GridLayout) swtComposite.getLayout()).numColumns = 2;

		// Set the title of the dialog
		getShell().setText("Reflectivity Quick Launch Utility");

		// Create simulation launch button
		Button modelRefButton = new Button(swtComposite, SWT.PUSH);
		modelRefButton.setToolTipText("Create New Reflectivity Simulation");
		GridData modelRefData = new GridData(GridData.CENTER, GridData.CENTER,
				true, false);
		modelRefButton.setLayoutData(modelRefData);
		// Load the image if it is available
		String imageString = null;
		try {
			// The URL for the file must be properly resolved by the file
			// locator class because FrameworUtil only returns the path relative
			// to the bundle running in the OSGi instance.
			imageString = FileLocator
					.toFileURL(FrameworkUtil.getBundle(getClass())
							.getEntry("icons/reflectivityCalc.png"))
					.getPath();
			// Create the image
			Image image = new Image(swtComposite.getDisplay(), imageString);
			// Set the button
			modelRefButton.setImage(image);
		} catch (IOException | SWTException e) {
			// Just set the text instead
			modelRefButton.setText("Launch New Simulation");
		}
		// Add a listener that creates the Reflectivity Model Item
		modelRefButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// Nothing TODO
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.
			 * events.MouseEvent)
			 */
			@Override
			public void mouseDown(MouseEvent e) {
				// Create the Reflectivity Model Item
				client.createItem("Reflectivity Model");
				// Close the dialog since our work is done here
				TaskLaunchDialog.this.close();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// Nothing TODO
			}
		});

		// Create edit materials button
		Button editMatsButton = new Button(swtComposite, SWT.PUSH);
		editMatsButton.setToolTipText("Create or Edit Materials");
		GridData editMatsData = new GridData(GridData.CENTER, GridData.CENTER,
				true, false);
		editMatsButton.setLayoutData(editMatsData);
		// Load the image if it is available
		imageString = null;
		try {
			// Exact same process as above
			imageString = FileLocator
					.toFileURL(FrameworkUtil.getBundle(getClass())
							.getEntry("icons/materialProperties.png"))
					.getPath();
			Image image = new Image(swtComposite.getDisplay(), imageString);
			editMatsButton.setImage(image);
		} catch (IOException | SWTException e) {
			// Just set the text instead
			editMatsButton.setText("Edit Materials");
		}
		// Add the mouse listener to open the materials database editor
		editMatsButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// Nothing TODO
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.
			 * events.MouseEvent)
			 */
			@Override
			public void mouseDown(MouseEvent e) {
				// Get the services that will make it possible to
				// programmatically look up commands and associated handlers.
				ECommandService commandService = context
						.get(ECommandService.class);
				EHandlerService handlerService = context
						.get(EHandlerService.class);
				// Get the command that can launch the handler that will open
				// the materials editor.
				ParameterizedCommand myCommand = commandService.createCommand(
						"org.eclipse.ice.materials.ui.EditDatabaseCommand",
						null);
				// Open the database
				handlerService.executeHandler(myCommand);
				// Close the dialog since our work is done here
				TaskLaunchDialog.this.close();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// Nothing TODO
			}
		});

		return swtComposite;
	}

}
