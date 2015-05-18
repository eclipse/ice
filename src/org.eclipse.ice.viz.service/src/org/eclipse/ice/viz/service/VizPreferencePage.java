/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;

/**
 * This class manages the main page of visualization service preferences in the
 * Eclipse Window -> Preferences dialog, specifically under the node
 * "Visualization".
 * <p>
 * In addition to the code here, the extension point
 * {@code org.eclipse.ui.preferencePage} must be used. In this case, the
 * extension point in {@code plugin.xml} looks like:
 * 
 * <pre>
 * <code>
 * {@literal<plugin>}
 *    {@literal<extension}
 *          id="org.eclipse.ice.viz.service.preferencePage"
 *          name="Visualization Preferences"
 *          point="org.eclipse.ui.preferencePages"{@literal>}
 *       {@literal<page}
 *             class="org.eclipse.ice.viz.service.VizPreferencePage"
 *             id="org.eclipse.ice.viz.service.preferences"
 *             name="Visualization"{@literal>}
 *       {@literal</page>}
 *    {@literal</extension>}
 * {@literal</plugin>}
 * </code>
 * </pre>
 * 
 * The same method should be used for other preference pages.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class VizPreferencePage extends AbstractVizPreferencePage {

	/**
	 * The default constructor.
	 */
	public VizPreferencePage() {
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
		setDescription("Visualization Preferences");
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
		Composite parent = getFieldEditorParent();

		// Add a field to automatically connect to the default viz service
		// connections when they start.
		addField(new BooleanFieldEditor(
				"autoConnectToDefaults",
				"Automatically establish default connections for vizualization services",
				parent));

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.AbstractVizPreferencePage#getVizService()
	 */
	@Override
	protected IVizService getVizService() {
		return null;
	}

}
