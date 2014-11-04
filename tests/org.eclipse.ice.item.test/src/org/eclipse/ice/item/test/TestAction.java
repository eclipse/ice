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
package org.eclipse.ice.item.test;

import java.util.Dictionary;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.action.Action;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The TestAction is used for testing Actions and for faking actions in ICE
 * Items. It does not override Action's Form accessors.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TestAction extends Action {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor. It initializes the Action's Form to a basic Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TestAction() {
		// begin-user-code

		// Initialize the Form
		actionForm = new Form();

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Action#execute(Dictionary<Object> dictionary)
	 */
	public FormStatus execute(Dictionary<String, String> dictionary) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Action#cancel()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus cancel() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}
}