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
package org.eclipse.eavp.viz.service.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.eavp.viz.service.datastructures.VizObject.VizObject;

/**
 * <p>
 * A class that implements the IEntryContentProvider interface.
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "BasicEntryContentProvider")
public class BasicVizEntryContentProvider extends VizObject implements
		IVizEntryContentProvider {
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
	private VizAllowedValueType allowedValueType;

	/**
	 * <p>
	 * The constructor. Sets the default values accordingly.
	 * </p>
	 * 
	 */
	public BasicVizEntryContentProvider() {
		this.defaultValue = "";
		this.allowedValues = new ArrayList<String>();
		this.allowedValueType = VizAllowedValueType.Undefined;
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
	@Override
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
	 * @see IVizEntryContentProvider#getAllowedValues()
	 */
	@Override
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
	@Override
	public void setAllowedValues(ArrayList<String> allowedValues) {
		if (allowedValues != null) {
			this.allowedValues = allowedValues;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IVizEntryContentProvider#getDefaultValue()
	 */
	@Override
	@XmlAttribute(name = "DefaultValue")
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IVizEntryContentProvider#getAllowedValueType()
	 */
	@Override
	@XmlElement(name = "AllowedValueType")
	public VizAllowedValueType getAllowedValueType() {
		return this.allowedValueType;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IVizEntryContentProvider#setAllowedValueType(AllowedValueType
	 *      allowedValueType)
	 */
	@Override
	public void setAllowedValueType(VizAllowedValueType allowedValueType) {
		if (allowedValueType != null) {
			this.allowedValueType = allowedValueType;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IVizEntryContentProvider#setParent(String parentName)
	 */
	@Override
	public void setParent(String parentName) {
		if (parentName != null) {
			parent = parentName;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IVizEntryContentProvider#getParent()
	 */
	@Override
	@XmlAttribute(name = "Parent")
	public String getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.VizObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherProvider) {

		// Local Declarations
		boolean retVal = false;

		// Check the provider, null and base type check first.
		if (otherProvider != null && otherProvider instanceof IVizEntryContentProvider) {
			IVizEntryContentProvider provider = (IVizEntryContentProvider) otherProvider;
			// See if they are the same reference on the heap
			if (this == otherProvider) {
				retVal = true;
			} else {
				// Check each member value
				retVal = super.equals(provider)
						&& (this.defaultValue.equals(provider
								.getDefaultValue()))
						&& (this.allowedValues.equals(provider
								.getAllowedValues()))
						&& (this.parent.equals(provider.getParent()))
						&& (this.allowedValueType.equals(provider
								.getAllowedValueType()));
				// Check the tag if it is already set
				if (this.tag != null) {
					retVal = retVal
							&& (this.tag.equals(provider.getTag()));
				} else {
					if (provider.getTag() != null) {
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
	public void copy(BasicVizEntryContentProvider otherProvider) {
		// if otherProvider is null, return
		if (otherProvider == null) {
			return;
		}

		// copy from super class
		super.copy(otherProvider);

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
	@Override
	public Object clone() {

		// Create a new instance of the object, copy contents, and return it
		BasicVizEntryContentProvider contentProvider = new BasicVizEntryContentProvider();
		contentProvider.copy(this);

		return contentProvider;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IVizEntryContentProvider#getTag()
	 */
	@Override
	@XmlAttribute(name = "Tag")
	public String getTag() {
		return tag;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IVizEntryContentProvider#setTag(String tagValue)
	 */
	@Override
	public void setTag(String tagValue) {
		tag = tagValue;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IVizEntryContentProvider#setDefaultValue(String defaultValue)
	 */
	@Override
	public void setDefaultValue(String defaultValue) {
		if (defaultValue != null) {
			this.defaultValue = defaultValue;
		}

	}
}