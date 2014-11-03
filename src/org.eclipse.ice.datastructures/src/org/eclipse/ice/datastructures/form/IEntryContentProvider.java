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
package org.eclipse.ice.datastructures.form;

import java.util.ArrayList;
import java.io.InputStream;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * An interface that contains the basic getters and setters required to pull
 * data from the Entry.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author jaybilly
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IEntryContentProvider {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the allowedValues.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The allowedValues.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getAllowedValues();

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param allowedValues
	 *            <p>
	 *            The allowedValues. Can not be null.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setAllowedValues(ArrayList<String> allowedValues);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the defaultValue.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The returned value.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getDefaultValue();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the AllowedValueType.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The returned value.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public AllowedValueType getAllowedValueType();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the AllowedValueType.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param allowedValueType
	 *            <p>
	 *            The allowedValueType to set.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setAllowedValueType(AllowedValueType allowedValueType);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the name of the parent. Can not be null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param parentName
	 *            <p>
	 *            The name of the parent.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setParent(String parentName);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the name of the parent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The returned value.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getParent();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns true if equal. False otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherProvider
	 *            <p>
	 *            The return value.
	 *            </p>
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(IEntryContentProvider otherProvider);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The returned value.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation provides a deep copy of the IEntryContentProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A clone of the IEntryContentProvider.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the tag.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The returned value.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getTag();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the tag.Can be set to null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param tagValue
	 *            <p>
	 *            The tag value to set.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTag(String tagValue);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the default value.Can not be null! (Can be the empty string!)
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param defaultValue
	 *            <p>
	 *            The default value to set.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setDefaultValue(String defaultValue);
}