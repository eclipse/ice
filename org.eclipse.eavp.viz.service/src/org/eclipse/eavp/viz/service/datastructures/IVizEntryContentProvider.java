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

/**
 * <p>
 * An interface that contains the basic getters and setters required to pull
 * data from the Entry.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public interface IVizEntryContentProvider {
	/**
	 * <p>
	 * Returns the allowedValues.
	 * </p>
	 * 
	 * @return <p>
	 *         The allowedValues.
	 *         </p>
	 */
	public ArrayList<String> getAllowedValues();

	/**
	 * 
	 * @param allowedValues
	 *            <p>
	 *            The allowedValues. Can not be null.
	 *            </p>
	 */
	public void setAllowedValues(ArrayList<String> allowedValues);

	/**
	 * <p>
	 * Returns the defaultValue.
	 * </p>
	 * 
	 * @return <p>
	 *         The returned value.
	 *         </p>
	 */
	public String getDefaultValue();

	/**
	 * <p>
	 * Returns the AllowedValueType.
	 * </p>
	 * 
	 * @return <p>
	 *         The returned value.
	 *         </p>
	 */
	public VizAllowedValueType getAllowedValueType();

	/**
	 * <p>
	 * Sets the AllowedValueType.
	 * </p>
	 * 
	 * @param allowedValueType
	 *            <p>
	 *            The allowedValueType to set.
	 *            </p>
	 */
	public void setAllowedValueType(VizAllowedValueType allowedValueType);

	/**
	 * <p>
	 * Sets the name of the parent. Can not be null.
	 * </p>
	 * 
	 * @param parentName
	 *            <p>
	 *            The name of the parent.
	 *            </p>
	 */
	public void setParent(String parentName);

	/**
	 * <p>
	 * Returns the name of the parent.
	 * </p>
	 * 
	 * @return <p>
	 *         The returned value.
	 *         </p>
	 */
	public String getParent();

	/**
	 * <p>
	 * Returns true if equal. False otherwise.
	 * </p>
	 * 
	 * @param otherProvider
	 *            <p>
	 *            The other provider to compare against.
	 *            </p>
	 * @return True if equivalent, false otherwise.
	 */
	@Override
	public boolean equals(Object otherProvider);

	/**
	 * <p>
	 * Returns the hashCode.
	 * </p>
	 * 
	 * @return <p>
	 *         The returned value.
	 *         </p>
	 */
	@Override
	public int hashCode();

	/**
	 * <p>
	 * This operation provides a deep copy of the IEntryContentProvider.
	 * </p>
	 * 
	 * @return <p>
	 *         A clone of the IEntryContentProvider.
	 *         </p>
	 */
	public Object clone();

	/**
	 * <p>
	 * Returns the tag.
	 * </p>
	 * 
	 * @return <p>
	 *         The returned value.
	 *         </p>
	 */
	public String getTag();

	/**
	 * <p>
	 * Sets the tag.Can be set to null.
	 * </p>
	 * 
	 * @param tagValue
	 *            <p>
	 *            The tag value to set.
	 *            </p>
	 */
	public void setTag(String tagValue);

	/**
	 * <p>
	 * Sets the default value.Can not be null! (Can be the empty string!)
	 * </p>
	 * 
	 * @param defaultValue
	 *            <p>
	 *            The default value to set.
	 *            </p>
	 */
	public void setDefaultValue(String defaultValue);
}