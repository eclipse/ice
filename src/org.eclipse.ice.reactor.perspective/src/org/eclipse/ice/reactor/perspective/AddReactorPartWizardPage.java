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

import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * This class creates wizard pages to provide the user with SWT widgets to
 * create and add reactor parts to the selection in the Reactor Viewer. The
 * content of the page is determined by the type of selection input to the
 * constructor.
 * 
 * @author Taylor Patterson
 */
public class AddReactorPartWizardPage extends WizardPage {

	/**
	 * The constructor for wizard pages for ICEResource selections in the
	 * Reactor Viewer.
	 * 
	 * @param resource
	 *            The ICEResource selection in the Reactor Viewer
	 */
	public AddReactorPartWizardPage(ICEResource resource) {

		// Call WizardPage's constructor
		// TODO Consider better String input - resource.getName() was just a
		// quick placeholder
		super(resource.getName());

		// Set the page's title and description
		setTitle("Add a Reactor");
		setDescription("Use this page to add a reactor to the selected .h5 "
				+ "file");

		return;
	}

	/**
	 * The constructor for wizard pages for IReactorComponent selections in the
	 * Reactor Viewer.
	 * 
	 * @param component
	 *            The IReactorComponent selection in the Reactor Viewer
	 */
	public AddReactorPartWizardPage(IReactorComponent component) {

		// Call WizardPage's constructor
		// TODO Consider better String input - component.getName() was just a
		// quick placeholder
		super(component.getName());

		// TODO Use an IReactorComponent visitor to set these?
		// Set the page's title and description
		setTitle("Add a <insert-appropriate-reactor-part>");
		setDescription("Use this page to add a <insert-appropriate-reactor"
				+ "-part> to the <insert-appropriate-reactor-part-selection"
				+ "-in-the-Reactor-Viewer>.");

		return;
	}

	/**
	 * Create and layout the widgets to be included in the WizardPage.
	 * 
	 * @param parent
	 *            The parent Composite of the control to be created
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		Label testLabel = new Label(composite, SWT.NONE);
		testLabel.setText("Composite area for this page");

		setControl(composite);
	}

}
