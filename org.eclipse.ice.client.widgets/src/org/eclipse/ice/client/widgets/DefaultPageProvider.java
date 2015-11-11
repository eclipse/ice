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
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.ice.client.widgets.providers.IPageProvider;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This is the default implementation of IPageProvider and it is responsible for
 * generating the default set of pages for ICEFormEditor.
 * 
 * @author Jay Jay Billings
 *
 */
public class DefaultPageProvider implements IPageProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.IPageProvider#getPages(java.util.Map)
	 */
	@Override
	public IFormPage[] getPages(Map<String, ArrayList<Component>> componentMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
