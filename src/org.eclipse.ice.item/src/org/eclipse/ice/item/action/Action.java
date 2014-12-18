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
package org.eclipse.ice.item.action;

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import java.util.Dictionary;

/**
 * The Action class performs actions, such as launching a job or script, for
 * ICE. It requires a Form with relevant information to execute property and it
 * may, at its own discretion, request additional information using a secondary,
 * Action-specific Form. This Form should not be created with list of Actions
 * since it is not used by an Item. This is needed to retrieve unexpected
 * information, such as login usernames and passwords, and contains a single
 * DataComponent with Entries for each additional bit of requested information.
 * This additional Form should not be used to retrieve large amounts of
 * additional information. Instead, it should be considered for
 * "emergency use only" and used for those types of information that are truly
 * unpredictable or not time-independent.
 * 
 * Actions may update the dictionary passed to execute() at their discretion, so
 * keep in mind that it may change if you depend on it in the client class.
 * 
 * @author Jay Jay Billings
 */
public abstract class Action {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A Form that is used to by the Action if it requires additional
	 * information, such as a login username and password, that is not available
	 * on the Form submitted by the Item that executed the action. This Form
	 * contains one DataComponent with Entries for each additional piece of
	 * required information.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Form actionForm;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A Form that contains data from the Item that initiated the Action.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Form dataForm;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The current status of the Action.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected FormStatus status;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Action() {
		// begin-user-code
		// TODO Auto-generated constructor stub
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves a Form from the Action that is used to request
	 * additional, unexpected information from the user such as a login username
	 * and password.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The second Form created by the Action for retrieving, for
	 *         example, a username and password.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Form getForm() {
		// begin-user-code
		// TODO Auto-generated method stub
		return actionForm;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation submits a Form to the Action that contains additional,
	 * unexpected information from the user such as a login username and
	 * password. This Form was originally created by the Action and posted by
	 * the calling Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param form
	 *            <p>
	 *            The second Form created by the Action for retrieving, for
	 *            example, a username and password.
	 *            </p>
	 * @return <p>
	 *         The ItemStatus value that specifies whether or not the secondary
	 *         Form was accepted by the Action. By default it is
	 *         FormStatus.Processing for any non-null Form and
	 *         FormStatus.InfoError otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus submitForm(Form form) {
		// begin-user-code

		// Accept or reject the Form
		if (form != null) {
			actionForm = form;
			return FormStatus.Processing;
		} else {
			return FormStatus.InfoError;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation executes the Action based on the information provided in
	 * the dictionary. Subclasses must implement this operation and should
	 * publish the exact keys and values that they require to perform their
	 * function in their documentation.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param dictionary
	 *            <p>
	 *            A dictionary that contains key-value pairs used by the action
	 *            to perform its function.
	 *            </p>
	 * @return <p>
	 *         The status of the Action.
	 *         </p>
	 */
	public abstract FormStatus execute(Dictionary<String, String> dictionary);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation cancels the Action, if possible.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The ItemStatus value that specifies whether or not the Action was
	 *         canceled successfully.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public abstract FormStatus cancel();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the current status of the Action.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The status
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus getStatus() {
		// begin-user-code
		return status;
		// end-user-code
	}
}