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

import java.util.ArrayList;

import org.eclipse.ice.client.widgets.providers.Default.DefaultPageFactory;
import org.eclipse.january.form.Component;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * @author Jay Jay Billings
 *
 */
public class DemoGeometryPageFactory extends DefaultPageFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IPageFactory#
	 * getGeometryComponentPages()
	 */
	@Override
	public ArrayList<IFormPage> getGeometryComponentPages(FormEditor editor,
			ArrayList<Component> components) {
		DemoGeometryPageProvider pageProvider = new DemoGeometryPageProvider();
		return pageProvider.getPages(editor, components);
	}
}
