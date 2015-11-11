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
import java.util.Map;

import org.eclipse.ice.client.widgets.ErrorMessageFormPage;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * This class is a default extension for providing error pages
 * 
 * @author Nick Stanish
 *
 */
public class DefaultErrorPageProvider implements IErrorPageProvider {

	public static final String PROVIDER_NAME = "default";

	@Override
	public String getName() {
		return PROVIDER_NAME;
	}
	
	@Override
	public IFormPage getPage(FormEditor formEditor,
			Map<String, ArrayList<Component>> componentMap) {
		
		return new ErrorMessageFormPage(formEditor, "Error Page", "Error Page");
	}

}
