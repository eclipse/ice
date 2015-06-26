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

import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This is the base class for visualization service preference pages added to
 * the Eclipse IDE's Window > Preferences dialog.
 * <p>
 * For an example of how to create a preference page using this class, see
 * {@link VizPreferencePage} and its class documentation.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractVizPreferencePage extends
		FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * The default constructor. Other constructors are available that are passed
	 * to the super class, {@link FieldEditorPreferencePage}.
	 */
	public AbstractVizPreferencePage() {
		super();
	}

	/**
	 * Creates a new field editor preference page with the given style, an empty
	 * title, and no image.
	 *
	 * @param style
	 *            either <code>GRID</code> or <code>FLAT</code>
	 */
	public AbstractVizPreferencePage(int style) {
		super(style);
	}

	/**
	 * Creates a new field editor preference page with the given title and
	 * style, but no image.
	 *
	 * @param title
	 *            the title of this preference page
	 * @param style
	 *            either <code>GRID</code> or <code>FLAT</code>
	 */
	public AbstractVizPreferencePage(String title, int style) {
		super(title, style);
	}

	/**
	 * Creates a new field editor preference page with the given title, image,
	 * and style.
	 *
	 * @param title
	 *            the title of this preference page
	 * @param image
	 *            the image for this preference page, or <code>null</code> if
	 *            none
	 * @param style
	 *            either <code>GRID</code> or <code>FLAT</code>
	 */
	public AbstractVizPreferencePage(String title, ImageDescriptor image,
			int style) {
		super(title, image, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setDescription(getVizService().getName() + " Preferences");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
	 */
	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		// This method is used to get the page's preference store. This method
		// will be called by the super class only when the store is unset and
		// queried.

		// If we want to maintain a separate PreferenceStore for multiple
		// PreferencePages in the same bundle, we will need to design an
		// alternative mechanism rather than using the bundle's store,
		// especially if multiple preference pages in the same bundle have
		// identical preference names/keys. NOTE: If we do design a mechanism
		// for managing multiple PreferenceStores in the same bundle,
		// AbstractVizPreferenceInitializer's getPreferenceStore() method may
		// also need to be updated.
		return new CustomScopedPreferenceStore(getClass());
	}

	/**
	 * Gets the IVizService whose preferences are displayed here.
	 */
	protected abstract IVizService getVizService();
}
