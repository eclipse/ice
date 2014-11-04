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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.Entry;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A LoginInfoForm is a Form with a single DataComponent and two Entries. One
 * Entry is for a username and the second Entry is for a password. The text of
 * the second Entry should be obscured from view. This class provides several
 * convenience methods for the setting properties on its Entries such as the
 * password and username prompt phrases and the description of the request. The
 * name of this Form is "Login Information."
 * </p>
 * <p>
 * This Form is only meant to be used with Actions and satisfies the criteria
 * for such Forms (only one DataComponent and no other components). It does not
 * accept a list of Actions in its constructor because it used by an Action, not
 * an Item.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LoginInfoForm extends Form {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An Entry for the username.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Entry username;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An Entry for the password.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Entry password;

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
	public LoginInfoForm() {
		// begin-user-code

		// Call the super constructor on Form
		super();

		// Create an Entry with the filenames
		username = new Entry() {

			// Setup the filenames
			public void setup() {

				// Set the default values
				allowedValueType = AllowedValueType.Undefined;
				setId(1);
				setName("Username");
				setDescription("Log-in Username");

				return;
			}

		};

		// Create an Entry with the filenames
		password = new Entry() {

			// Simple pass through for updates
			public void update(String key, String value) {
			}

			// Setup the filenames
			public void setup() {

				// Set the default values
				allowedValueType = AllowedValueType.Undefined;
				setId(2);
				setName("Password");
				setDescription("Log-in Password");
				secretFlag = true;

				return;
			}

		};

		// Set the name and description of the Form
		setName("Log-in Information Form");
		setDescription("Information required to log-in to a remote machine.");

		// Make a DataComponent for the application
		DataComponent loginComponent = new DataComponent();
		loginComponent.setId(1);
		loginComponent.setDescription("Please provide your credentials");
		loginComponent.setName("Log-in Information");

		// Add the Entries to the DataComponent
		loginComponent.addEntry(username);
		loginComponent.addEntry(password);

		// Add the data components
		addComponent(loginComponent);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the description of the LoginInfoForm and is the same
	 * as calling the setDescription() operation. It is provided here as a
	 * semantically appealing convenience method.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param promptDesc
	 *            <p>
	 *            The description of the prompt and, in particularl for this
	 *            case, the description of the LoginInfoForm.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPromptDescription(String promptDesc) {
		// begin-user-code

		setDescription(promptDesc);

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the name of the username Entry to something other
	 * than the default, which is "Username."
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param promptName
	 *            <p>
	 *            The name of the prompt.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setUsernamePrompt(String promptName) {
		// begin-user-code

		// Make sure the name is not null
		if (promptName != null) {
			username.setName(promptName);
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the name of the password Entry to something other
	 * than the default, which is "Password."
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param promptName
	 *            <p>
	 *            The name of the prompt.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPasswordPrompt(String promptName) {
		// begin-user-code

		// Make sure the name is not null
		if (promptName != null) {
			password.setName(promptName);
		}
		return;

		// end-user-code
	}
}