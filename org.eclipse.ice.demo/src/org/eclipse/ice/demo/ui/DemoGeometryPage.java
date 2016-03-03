/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.demo.ui;

import org.eclipse.ice.client.widgets.ICEFormPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * @author Jay Jay Billings
 *
 */
public class DemoGeometryPage extends ICEFormPage {

	public DemoGeometryPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>
	 * Provides the page with the geometryApplication's information to display
	 * geometry.
	 * </p>
	 * 
	 * @param managedForm
	 *            the managed form that handles the page
	 */
	@Override
	public void createFormContent(IManagedForm managedForm) {

		// Local Declarations
		final ScrolledForm form = managedForm.getForm();
		GridLayout layout = new GridLayout();

		// Setup the layout and layout data
		layout.numColumns = 1;
		form.getBody().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		form.getBody().setLayout(new FillLayout());

		// Just create some text and say hello
		Label geometryText = new Label(form.getBody(), SWT.FLAT);
		geometryText.setText("Draw something based on the geometry.");

		return;

	}

}
