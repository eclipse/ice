/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit;

import org.eclipse.ice.viz.service.AbstractVizPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;

/**
 * This class provides a preferences page for the VisIt visualization service.
 * 
 * @author Jay Jay Billings
 * 
 */
public class VisItPreferencePage extends AbstractVizPreferencePage {

	/**
	 * The default constructor.
	 */
	public VisItPreferencePage() {
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
		setDescription("VisIt 2.8.2 Connection Preferences");
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

		// TODO These preferences will each need to be custom tailored.
		for (ConnectionPreference p : ConnectionPreference.values()) {
			addField(new StringFieldEditor(p.toString(), p.getName(),
					getFieldEditorParent()));
		}

		return;
	}
}