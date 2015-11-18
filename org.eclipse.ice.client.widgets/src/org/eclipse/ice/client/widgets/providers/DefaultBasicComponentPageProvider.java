/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Nick Stanish
 *******************************************************************************/



package org.eclipse.ice.client.widgets.providers;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This is the default implementation of IBasicComponentPageProvider and it is responsible for
 * generating the default set of basic component pages for ICEFormEditor.
 * 
 * @author 
 *
 */

public class DefaultBasicComponentPageProvider extends DefaultPageProvider 
	implements IBasicComponentPageProvider {
	
	@Override
	public ArrayList<IFormPage> getPages(FormEditor formEditor,
			ArrayList<Component> components) {
		
		// TODO: this method in ICEFormEditor makes heavy use of component map
		return null;
	}

}
