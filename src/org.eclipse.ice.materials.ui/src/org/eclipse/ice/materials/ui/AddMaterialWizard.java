/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.materials.ui;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * This class is a JFace Wizard for creating and adding new materials to the
 * materials database.
 * 
 * @author Jay Jay Billings
 *
 */
public class AddMaterialWizard extends Wizard implements INewWizard {

	/**
	 * The wizard page used to create the Material.
	 */
	private AddMaterialWizardPage page;

	/**
	 * The workbench window used by the wizard.
	 */
	private IWorkbenchWindow workbenchWindow;

	/**
	 * A nullary constructor. This is used by the platform. <b>If called from an
	 * {@link IHandler}, use {@link #AddMaterialWizard(IWorkbenchWindow)} </b>.
	 */
	public AddMaterialWizard() {
		super();
	}

	/**
	 * The default constructor. This is not normally called by the platform but
	 * via handlers.
	 * 
	 * @param window
	 *            The workbench window.
	 */
	public AddMaterialWizard(IWorkbenchWindow window) {
		this();
		// Store a reference to the workbench window.
		workbenchWindow = window;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		if (page == null) {
			page = new AddMaterialWizardPage("Create a New Material");
		}
		addPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		workbenchWindow = workbench.getActiveWorkbenchWindow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
