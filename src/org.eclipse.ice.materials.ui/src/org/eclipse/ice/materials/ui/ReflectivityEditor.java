/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jay Jay Billings (UT-Battelle, LLC.) - initial API and implementation 
 *      and/or initial documentation
 *******************************************************************************/
 
package org.eclipse.ice.materials.ui;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ReflectivityEditor {
	@Inject
	public ReflectivityEditor() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		Text text = new Text(parent,SWT.None);
		text.setText("Reflectivity Editor");
	}
	
	
	
	
}