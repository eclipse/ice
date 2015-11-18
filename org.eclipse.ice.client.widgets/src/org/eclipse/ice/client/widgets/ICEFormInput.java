/*******************************************************************************
* Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/** 
 * <p>This class is a wrapper around the Form class in ICE that realizes the appropriate Eclipse interface and can be used by Eclipse Forms.</p>
 * @author Jay Jay Billings
 */
public class ICEFormInput implements IEditorInput {
	/** 
	 * <p>An internal reference for storing the ICE Form.</p>
	 */
	private Form form;

	/** 
	 * <p>The constructor for this Eclipse editor input.</p>
	 * @param inputForm <p>The Form that should be supplied as input to the Editor.</p>
	 */
	public ICEFormInput(Form inputForm) {
		this.form = inputForm;
	}

	/** 
	 * <p>This operation returns the Form that is represented by the ICEFormInput. It should never return null.</p>
	 * @return <p>The Form from ICE.</p>
	 */
	public Form getForm() {

		return form;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * (non-Javadoc)
	 * @see IEditorInput#exists()
	 */
	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// Return the name of the Form
		return this.form.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// Return the description of the Form
		return this.form.getDescription();
	}

}