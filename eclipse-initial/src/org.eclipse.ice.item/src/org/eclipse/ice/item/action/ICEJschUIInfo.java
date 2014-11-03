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

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import static org.eclipse.ice.item.action.LoginInfoForm.*;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;

import java.util.ArrayList;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The ICEJschUIInfo class gathers password information from a LoginInfoForm to
 * provide the password to Jsch. It implements both the Jsch UserInfo and
 * UIKeyboardInteractive interfaces. This class is only configured to work with
 * single request keyboard interactive login and (and it fakes the interactive
 * response at that...).
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEJschUIInfo implements UIKeyboardInteractive, UserInfo {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An AtomicReference to the LoginInfoForm from which password information
	 * should be gathered.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	private AtomicReference<LoginInfoForm> loginInfoFormAtomic;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEJschUIInfo() {
		// begin-user-code

		loginInfoFormAtomic = new AtomicReference<LoginInfoForm>();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the LoginInfoForm that should be used by the
	 * ICEJschUIInfo class to gather the password information.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param form
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setForm(LoginInfoForm form) {
		// begin-user-code

		// Check the Form before setting it
		if (form != null) {
			loginInfoFormAtomic.set(form);
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the ICEJschUIInfo is ready and false
	 * otherwise. The decision is based on the presence or absence of the
	 * LoginInfoForm.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if ready, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isReady() {
		// begin-user-code
		if (loginInfoFormAtomic.get() != null) {
			return true;
		} else {
			return false;
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see UIKeyboardInteractive#promptKeyboardInteractive(String destination,
	 *      String name, String instruction, ArrayList<String> prompt,
	 *      boolean... echo)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> promptKeyboardInteractive(String destination,
			String name, String instruction, ArrayList<String> prompt,
			boolean... echo) {
		// begin-user-code

		// Local Declarations
		ArrayList<String> retList = new ArrayList<String>();
		String[] retArray = promptKeyboardInteractive(destination, name,
				instruction, (String[]) prompt.toArray(), echo);

		// Transfer the string array
		for (String i : retArray) {
			retList.add(i);
		}
		return retList;
		// end-user-code
	}

	/**
	 * This operation fakes out Jsch's keyboard interactive check and gives it
	 * the stored password.
	 * 
	 * @see UIKeyboardInteractive#promptKeyboardInteractive(String destination,
	 *      String name, String instruction, ArrayList<String> prompt,
	 *      boolean... echo)
	 */
	public String[] promptKeyboardInteractive(String destination, String name,
			String instruction, String[] prompt, boolean[] echo) {
		// begin-user-code

		// Local Declarations
		DataComponent comp = null;
		ArrayList<Entry> entries = null;
		String password = null;
		String[] response = new String[1];

		// Get the password if possible
		if (loginInfoFormAtomic.get() != null) {
			comp = (DataComponent) loginInfoFormAtomic.get().getComponent(1);
			entries = comp.retrieveAllEntries();
			password = entries.get(1).getValue();
			response[0] = password;
		}

		return response;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see UserInfo#getPassphrase()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getPassphrase() {
		// begin-user-code
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see UserInfo#getPassword()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getPassword() {
		// begin-user-code

		// Local Declarations
		DataComponent comp = null;
		ArrayList<Entry> entries = null;
		String password = null;

		// Get the password if possible
		if (loginInfoFormAtomic.get() != null) {
			comp = (DataComponent) loginInfoFormAtomic.get().getComponent(1);
			entries = comp.retrieveAllEntries();
			password = entries.get(1).getValue();
			System.out.println("Password requested!");
		}

		return password;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see UserInfo#promptPassword(String message)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean promptPassword(String message) {
		// begin-user-code

		// Local Declarations
		DataComponent comp = null;
		ArrayList<Entry> entries = null;
		String password = null;

		// Get the password if possible
		if (loginInfoFormAtomic.get() != null) {
			comp = (DataComponent) loginInfoFormAtomic.get().getComponent(1);
			entries = comp.retrieveAllEntries();
			password = entries.get(1).getValue();
		}

		// Check for the password
		if (password == null) {
			return false;
		} else {
			return true;
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see UserInfo#promptPassphrase(String message)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean promptPassphrase(String message) {
		// begin-user-code
		return true;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see UserInfo#promptYesNo(String message)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean promptYesNo(String message) {
		// begin-user-code
		return false;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see UserInfo#showMessage(String message)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void showMessage(String message) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}
}