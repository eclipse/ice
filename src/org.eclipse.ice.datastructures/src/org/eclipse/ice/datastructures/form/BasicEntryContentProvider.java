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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.ICEObject.ICEObject;

/**
 * <p>
 * A class that implements the IEntryContentProvider interface.
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "BasicEntryContentProvider")
public class BasicEntryContentProvider extends ICEObject implements
		IEntryContentProvider {
	/**
	 * <p>
	 * The default value of the entry.
	 * </p>
	 * 
	 */
	private String defaultValue;
	/**
	 * <p>
	 * A list of allowed values for entry. If the allowedValueType is discrete,
	 * there can be one to many allowed values. If the allowedValueType is
	 * continuous, the allowedvalues should be 2 in size and the first value to
	 * be smaller than the second. If the allowedValueType is undefined, then no
	 * allowedvalues (empty list) should be declared.
	 * </p>
	 * 
	 */
	private ArrayList<String> allowedValues;
	/**
	 * <p>
	 * The name of the Entry that is the parent of this Entry. Parent Entries
	 * are used, for example, to create dependencies among Entries such that
	 * some subset of the Entries will not become available to update until the
	 * parent notifies them that they should be ready.
	 * </p>
	 * 
	 */
	private String parent;
	/**
	 * <p>
	 * The tag of an Entry is a secondary descriptive value that may be used to
	 * "tag" an Entry with a small note or additional value. This information
	 * should not be used in the UI! Another way to thing of the tag of an Entry
	 * is to consider it as a second name that could be used, for example, when
	 * writing to a file or stream where human readability is less of a factor
	 * than the ability to parse the stream, (such as key-value pairs).
	 * </p>
	 * 
	 */
	private String tag;
	/**
	 * 
	 */
	private AllowedValueType allowedValueType;

	/**
	 * <p>
	 * The constructor. Sets the default values accordingly.
	 * </p>
	 * 
	 */
	public BasicEntryContentProvider() {
		this.defaultValue = "";
		this.allowedValues = new ArrayList<String>();
		this.allowedValueType = AllowedValueType.Undefined;
		parent = "orphan";
		tag = null;
	}

	/**
	 * <p>
	 * Returns the hashCode.
	 * </p>
	 * 
	 * @return <p>
	 *         The hash.
	 *         </p>
	 */
	public int hashCode() {
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

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#getAllowedValues()
	 */
	@XmlElement(name = "AllowedValues")
	public ArrayList<String> getAllowedValues() {

		return this.allowedValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.form.IEntryContentProvider#setAllowedValues
	 * (java.util.ArrayList)
	 */
	public void setAllowedValues(ArrayList<String> allowedValues) {
		if (allowedValues != null) {
			this.allowedValues = allowedValues;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#getDefaultValue()
	 */
	@XmlAttribute(name = "DefaultValue")
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#getAllowedValueType()
	 */
	@XmlElement(name = "AllowedValueType")
	public AllowedValueType getAllowedValueType() {
		return this.allowedValueType;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#setAllowedValueType(AllowedValueType
	 *      allowedValueType)
	 */
	public void setAllowedValueType(AllowedValueType allowedValueType) {
		if (allowedValueType != null) {
			this.allowedValueType = allowedValueType;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#setParent(String parentName)
	 */
	public void setParent(String parentName) {
		if (parentName != null) {
			parent = parentName;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#getParent()
	 */
	@XmlAttribute(name = "Parent")
	public String getParent() {
		return parent;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#equals(IEntryContentProvider otherProvider)
	 */
	public boolean equals(IEntryContentProvider otherProvider) {

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

	}

	/**
	 * <p>
	 * Deep copies another BasicEntryContentProvider.
	 * </p>
	 * 
	 * @param otherProvider
	 *            <p>
	 *            The provider to be copied.
	 *            </p>
	 */
	public void copy(BasicEntryContentProvider otherProvider) {
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

	}

	/**
	 * <p>
	 * This operation provides a deep copy of the BasicEntryContentProvider.
	 * </p>
	 * 
	 * @return <p>
	 *         A clone of the BasicEntryContentProvider.
	 *         </p>
	 */
	public Object clone() {

		// Create a new instance of the object, copy contents, and return it
		BasicEntryContentProvider contentProvider = new BasicEntryContentProvider();
		contentProvider.copy(this);

		return contentProvider;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#getTag()
	 */
	@XmlAttribute(name = "Tag")
	public String getTag() {
		return tag;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#setTag(String tagValue)
	 */
	public void setTag(String tagValue) {
		tag = tagValue;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IEntryContentProvider#setDefaultValue(String defaultValue)
	 */
	public void setDefaultValue(String defaultValue) {
		if (defaultValue != null) {
			this.defaultValue = defaultValue;
		}

	}
}