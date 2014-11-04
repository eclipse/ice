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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import org.eclipse.ice.datastructures.form.Form;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class is a wrapper around the Form class in ICE that realizes the appropriate Eclipse interface and can be used by Eclipse Forms.</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEFormInput implements IEditorInput {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>An internal reference for storing the ICE Form.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Form form;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The constructor for this Eclipse editor input.</p>
	 * <!-- end-UML-doc -->
	 * @param inputForm <p>The Form that should be supplied as input to the Editor.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEFormInput(Form inputForm) {
		// begin-user-code
		this.form = inputForm;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the Form that is represented by the ICEFormInput. It should never return null.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The Form from ICE.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Form getForm() {
		// begin-user-code

		return form;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAdaptable#getAdapter(Class adapter)
	 */
	public Object getAdapter(Class adapter) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IEditorInput#exists()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean exists() {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
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