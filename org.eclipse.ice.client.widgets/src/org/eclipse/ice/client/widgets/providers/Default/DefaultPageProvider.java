/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.client.widgets.providers.Default;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.ice.client.widgets.providers.IPageProvider;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This is the default implementation of IPageProvider and it is responsible for
 * generating the default set of pages for ICEFormEditor.
 * 
 * @author Jay Jay Billings
 *
 */
public class DefaultPageProvider implements IPageProvider {

	/**
	 * Default provider name
	 */
	public static final String PROVIDER_NAME = "default";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IPageProvider#getName()
	 */
	@Override
	public String getName() {
		return PROVIDER_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.providers.IPageProvider#getPages(org.
	 * eclipse.ui.forms.editor.FormEditor, java.util.Map)
	 */
	@Override
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components) {
		// TODO Auto-generated method stub
		return null;
	}

}
