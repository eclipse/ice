/*******************************************************************************
 * Copyright (c) 2012, 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview;

import org.eclipse.ice.viz.service.AbstractVizPreferencePage;
import org.eclipse.ui.IWorkbench;

/**
 * This class provides a preference page for the ParaView visualization service.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewPreferencePage extends AbstractVizPreferencePage {

	/**
	 * The default constructor.
	 */
	public ParaViewPreferencePage() {
		super(GRID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors
	 * ()
	 */
	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub

	}

}
