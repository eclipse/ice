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

import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ui.forms.editor.FormEditor;

/** 
 * <!-- begin-UML-doc -->
 * <p>The ICEFormPage class is the base class for all FormPages in ICE.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEFormPage extends FormPage {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The Form from ICE that contains the data to be displayed in this on this Page.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Form iceForm;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>A handle to the Editor that is injected in the Constructor.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ICEFormEditor editor;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The Constructor</p>
	 * <!-- end-UML-doc -->
	 * @param editor <p>The FormEditor for which the Page should be constructed.</p>
	 * @param id <p>The id of the page.</p>
	 * @param title <p>The title of the page.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEFormPage(FormEditor editor, String id, String title) {
		// begin-user-code

		// Call the super constructor
		super(editor, id, title);

		// end-user-code
	}
}