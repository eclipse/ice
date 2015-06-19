/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.materials.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * This is an editor for the Materials Database Service.
 * 
 * @author Jay Jay Billings
 * 
 */
public class MaterialsDatabaseEditor extends FormEditor {

	/**
	 * The unique ID of this editor part
	 */
	public static final String ID = "org.eclipse.ice.materials.ui.MaterialsDatabaseEditor";

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		if (input instanceof MaterialsDatabaseEditorInput) {
			setInput(input);
		} else {
			throw new PartInitException("Wrong input type! "
					+ input.getClass().getName() + " is the wrong type!");
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#isDirty()
	 */
	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// Nothing TODO
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		
		// Get the input
		MaterialsDatabaseEditorInput input = (MaterialsDatabaseEditorInput) getEditorInput();
		IMaterialsDatabase database = input.getDatabase();
		
		try {
			// Create the Master-Details page for the Materials
			addPage(new MaterialsDatabaseMasterDetailsPage(this, "MDPage",
					"Materials Database", database));
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
