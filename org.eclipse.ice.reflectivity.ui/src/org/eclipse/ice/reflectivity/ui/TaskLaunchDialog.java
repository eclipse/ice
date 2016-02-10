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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
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
	 * The constructor
	 * 
	 * @param parentShell
	 *            The shell in which this dialog is drawn and which is also the
	 *            shell of the parent application.
	 */
	public TaskLaunchDialog(Shell parentShell) {
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
			imageString = FileLocator.resolve(FrameworkUtil
					.getBundle(getClass()).getEntry("icons/sns.png")).getPath();
			// Create the image
			Image image = new Image(swtComposite.getDisplay(), imageString);
			// Set the button
			modelRefButton.setImage(image);
		} catch (IOException e) {
			// Just set the text instead
			modelRefButton.setText("Launch New Simulation");
		}

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
			imageString = FileLocator.resolve(FrameworkUtil
					.getBundle(getClass()).getEntry("icons/sns.png")).getPath();
			Image image = new Image(swtComposite.getDisplay(), imageString);
			editMatsButton.setImage(image);
		} catch (IOException e) {
			// Just set the text instead
			editMatsButton.setText("Edit Materials");
		}

		return swtComposite;
	}

}
