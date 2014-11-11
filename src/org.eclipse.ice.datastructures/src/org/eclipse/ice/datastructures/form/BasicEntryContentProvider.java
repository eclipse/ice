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

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBManipulator;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A class that implements the IEntryContentProvider interface.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "BasicEntryContentProvider")
public class BasicEntryContentProvider extends ICEObject implements
		IEntryContentProvider {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The default value of the entry.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String defaultValue;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A list of allowed values for entry. If the allowedValueType is discrete,
	 * there can be one to many allowed values. If the allowedValueType is
	 * continuous, the allowedvalues should be 2 in size and the first value to
	 * be smaller than the second. If the allowedValueType is undefined, then no
	 * allowedvalues (empty list) should be declared.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> allowedValues;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the Entry that is the parent of this Entry. Parent Entries
	 * are used, for example, to create dependencies among Entries such that
	 * some subset of the Entries will not become available to update until the
	 * parent notifies them that they should be ready.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String parent;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The tag of an Entry is a secondary descriptive value that may be used to
	 * "tag" an Entry with a small note or additional value. This information
	 * should not be used in the UI! Another way to thing of the tag of an Entry
	 * is to consider it as a second name that could be used, for example, when
	 * writing to a file or stream where human readability is less of a factor
	 * than the ability to parse the stream, (such as key-value pairs).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String tag;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AllowedValueType allowedValueType;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor. Sets the default values accordingly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public BasicEntryContentProvider() {
		// begin-user-code
		this.defaultValue = "";
		this.allowedValues = new ArrayList<String>();
		this.allowedValueType = AllowedValueType.Undefined;
		parent = "orphan";
		tag = null;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hash.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code
		int hashCode = 0;

		// Get ICEObject's hashCode
		hashCode = super.hashCode();

		// Get this object's hashCode
		hashCode += 31 * this.allowedValues.hashCode();
		hashCode += 31 * this.allowedValueType.hashCode();
		hashCode += 31 * this.defaultValue.hashCode();
		hashCode += 31 * this.parent.hashCode();
		hashCode += 31 * (null == this.tag ? 0 : this.tag.hashCode());

		return hashCode;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#getAllowedValues()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "AllowedValues")
	public ArrayList<String> getAllowedValues() {
		// begin-user-code

		return this.allowedValues;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#setAllowedValues(ArrayList<String>
	 *      allowedValues)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setAllowedValues(ArrayList<String> allowedValues) {
		// begin-user-code
		if (allowedValues != null) {
			this.allowedValues = allowedValues;
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#getDefaultValue()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute(name = "DefaultValue")
	public String getDefaultValue() {
		// begin-user-code
		return this.defaultValue;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#getAllowedValueType()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "AllowedValueType")
	public AllowedValueType getAllowedValueType() {
		// begin-user-code
		return this.allowedValueType;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#setAllowedValueType(AllowedValueType
	 *      allowedValueType)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setAllowedValueType(AllowedValueType allowedValueType) {
		// begin-user-code
		if (allowedValueType != null) {
			this.allowedValueType = allowedValueType;
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#setParent(String parentName)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setParent(String parentName) {
		// begin-user-code
		if (parentName != null) {
			parent = parentName;
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#getParent()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute(name = "Parent")
	public String getParent() {
		// begin-user-code
		return parent;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#equals(IEntryContentProvider otherProvider)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(IEntryContentProvider otherProvider) {
		// begin-user-code

		// Local Declarations
		boolean retVal = false;

		// Check the provider, null and base type check first.
		if (otherProvider != null) {
			// See if they are the same reference on the heap
			if (this == otherProvider) {
				retVal = true;
			} else {
				// Check each member value
				retVal = super.equals((ICEObject) otherProvider)
						&& (this.defaultValue.equals(otherProvider
								.getDefaultValue()))
						&& (this.allowedValues.equals(otherProvider
								.getAllowedValues()))
						&& (this.parent.equals(otherProvider.getParent()))
						&& (this.allowedValueType.equals(otherProvider
								.getAllowedValueType()));
				// Check the tag if it is already set
				if (this.tag != null) {
					retVal = retVal
							&& (this.tag.equals(otherProvider.getTag()));
				} else {
					if (otherProvider.getTag() != null) {
						return false;
					}
				}
			}
		}
		return retVal;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies another BasicEntryContentProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherProvider
	 *            <p>
	 *            The provider to be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(BasicEntryContentProvider otherProvider) {
		// begin-user-code
		// if otherProvider is null, return
		if (otherProvider == null) {
			return;
		}

		// copy from super class
		super.copy((ICEObject) otherProvider);

		// Copy current values
		// Deep copy of allowed Values
		this.allowedValues.clear();
		for (int i = 0; i < otherProvider.getAllowedValues().size(); i++) {
			this.allowedValues.add(otherProvider.getAllowedValues().get(i));
		}

		this.allowedValueType = otherProvider.getAllowedValueType();
		this.defaultValue = otherProvider.getDefaultValue();
		this.parent = otherProvider.getParent();
		this.tag = otherProvider.getTag();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation provides a deep copy of the BasicEntryContentProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A clone of the BasicEntryContentProvider.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Create a new instance of the object, copy contents, and return it
		BasicEntryContentProvider contentProvider = new BasicEntryContentProvider();
		contentProvider.copy(this);

		return contentProvider;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Loads the data from xml stream.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStream
	 *            <p>
	 *            The inputStream.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromXML(InputStream inputStream) {
		// begin-user-code
		// Initialize JAXBManipulator
		jaxbManipulator = new ICEJAXBManipulator();

		// Call the read() on jaxbManipulator to create a new Object instance
		// from the inputStream
		Object dataObject;
		try {
			dataObject = jaxbManipulator.read(this.getClass(), inputStream);
			// Copy contents of new object into current data structure
			this.copy((BasicEntryContentProvider) dataObject);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Nullerize jaxbManipilator
		jaxbManipulator = null;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#getTag()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute(name = "Tag")
	public String getTag() {
		// begin-user-code
		return tag;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#setTag(String tagValue)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTag(String tagValue) {
		// begin-user-code
		tag = tagValue;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#setDefaultValue(String defaultValue)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setDefaultValue(String defaultValue) {
		// begin-user-code
		if (defaultValue != null) {
			this.defaultValue = defaultValue;
		}

		// end-user-code
	}
}