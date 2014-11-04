/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.messaging;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The Message class is a simple Java Bean that contains information from an ICE
 * subsystem, a remote ICE subsystem or an external third process about the
 * progress of worked performed by ICE.
 * </p>
 * <p>
 * Messages cannot be copied or cloned.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Message {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An optional id for this message.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int id;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The id of the Item which which this Message should be associated.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int itemId;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The type of the message. One of FILE_CREATED, FILE_MODIFIED,
	 * FILE_DELETED, MESSAGE_POSTED, PROGRESS_UPDATED, CONVERGENCE_UPDATED,
	 * UPDATER_STARTED, or UPDATER_STOPPED.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String type;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The contents of the Message.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String message;

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
	public Message() {
		// begin-user-code

		id = 0;
		itemId = 0;
		type = "";
		message = "";

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the id of the Message.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The id
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getId() {
		// begin-user-code
		return id;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the id of the Item with which the Message is
	 * associated.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The ItemId
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getItemId() {
		// begin-user-code
		return itemId;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the type of the Message.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The type
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getType() {
		// begin-user-code
		return type;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the content of the Message.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The content
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getMessage() {
		// begin-user-code
		return message;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the id of the Message.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param id
	 *            <p>
	 *            The id
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setId(int id) {
		// begin-user-code
		this.id = id;
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the id of the Item with which the Message is
	 * associated.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemId
	 *            <p>
	 *            The ItemId
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setItemId(int itemId) {
		// begin-user-code
		this.itemId = itemId;
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the type of the Message.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param type
	 *            <p>
	 *            The type
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setType(String type) {
		// begin-user-code
		this.type = type;
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the content of the Message.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param content
	 *            <p>
	 *            The content
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setMessage(String content) {
		// begin-user-code
		message = content;
		return;
		// end-user-code
	}
}