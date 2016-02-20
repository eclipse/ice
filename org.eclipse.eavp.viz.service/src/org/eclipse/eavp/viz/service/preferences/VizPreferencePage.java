/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * This class manages the main page of visualization service preferences in the
 * Eclipse Window > Preferences dialog, specifically under the node
 * "Visualization".
 * <p>
 * In addition to the code here, the extension point
 * {@code org.eclipse.ui.preferencePage} must be used. In this case, the
 * extension point in {@code plugin.xml} looks like:
 * </p>
 * 
 * <pre>
 * <code>
 * {@literal <plugin>}
 *    {@literal <extension}
 *          id="org.eclipse.eavp.viz.service.preferencePage"
 *          name="Visualization Preferences"
 *          point="org.eclipse.ui.preferencePages"{@literal >}
 *       {@literal <page}
 *             class="org.eclipse.eavp.viz.service.VizPreferencePage"
 *             id="org.eclipse.eavp.viz.service.preferences"
 *             name="Visualization"{@literal >}
 *       {@literal </page>}
 *    {@literal </extension>}
 * {@literal </plugin>}
 * </code>
 * </pre>
 * <p>
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

}
