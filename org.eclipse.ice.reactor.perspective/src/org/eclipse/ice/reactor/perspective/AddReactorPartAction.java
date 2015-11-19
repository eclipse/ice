/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor.perspective;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisWidgetFactory;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisWidgetRegistry;
import org.eclipse.ice.reactor.perspective.internal.ReactorEditorRegistry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * 
 * @author Taylor Patterson
 */
public class AddReactorPartAction extends Action {

	/**
	 * The ViewPart that owns an object of this class.
	 */
	private final ViewPart viewer;

	/**
	 * The constructor
	 * 
	 * @param parent
	 *            The ViewPart to whom the object of this class belongs.
	 */
	public AddReactorPartAction(ViewPart parent) {

		viewer = parent;

		// Set the action's tool tip text.
		setText("Add reactor component");
		setToolTipText("Add a reactor component");

		// Set the action's image (the green plus button for adding).
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "add.png");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		setImageDescriptor(imageDescriptor);

		return;
	}

	/**
	 * The function called whenever the action is clicked.
	 */
	@Override
	public void run() {
		// Get the selection in the Reactor Viewer.
		Object selection = ((ReactorViewer) viewer).getSelectedElement();

		if (selection != null) {

			// Get the registry
			IAnalysisWidgetRegistry registry = ReactorEditorRegistry
					.getAnalysisWidgetRegistry();

			// Get the factory
			IAnalysisWidgetFactory factory = registry
					.getAnalysisWidgetFactory(registry.getClass());

			// Get the wizard
			IWizard wizard = factory.createWizard(selection);

			// Get the Shell of the workbench
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();

			// Create a WizardDialog with an AddReactorPartWizard constructed
			// with the selection in the Reactor Viewer.
			WizardDialog dialog = new WizardDialog(shell, wizard);

			// Open the dialog
			dialog.open();
		}

		return;
	}

}
