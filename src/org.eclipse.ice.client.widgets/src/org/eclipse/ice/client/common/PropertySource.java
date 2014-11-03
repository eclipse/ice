/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.common;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * <p>
 * This class serves as an implementation of {@link IPropertySource} with the
 * added functionality of wrapping some additional object. This acts to utilize
 * the properties view.
 * </p>
 * 
 * @author tnp
 */
public class PropertySource implements IPropertySource {

	/**
	 * <p>
	 * The additional object wrapped by this class.
	 * </p>
	 */
	private Object wrappedData;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 * @param obj
	 *            <p>
	 *            The object to be wrapped by this class
	 *            </p>
	 */
	public PropertySource(Object obj) {
		wrappedData = obj;

		return;
	}

	/**
	 * <p>
	 * This function simply returns the object wrapped by an object of this
	 * class.
	 * </p>
	 * 
	 * @return <p>
	 *         The object wrapped by an instance of this class
	 *         </p>
	 */
	public Object getWrappedData() {
		return wrappedData;
	}

	/**
	 * <p>
	 * The default implementation that provides one property descriptor.
	 * </p>
	 * 
	 * @return <p>
	 *         The array of descriptors
	 *         </p>
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] { new PropertyDescriptor(
				"NONE", "No property") };
		return descriptors;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	@Override
	public Object getEditableValue() {
		// Do nothing
		return null;
	}

	/**
	 * <p>
	 * This function returns "Unknown value" for the one default property.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            The object to identify this property
	 *            </p>
	 * 
	 * @return <p>
	 *         The object value for the input property id
	 *         </p>
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 *      .lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		if ("NONE".equals(id)) {
			return new String("Unknown value");
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang
	 *      .Object)
	 */
	@Override
	public boolean isPropertySet(Object id) {
		// Do nothing
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java
	 *      .lang.Object)
	 */
	@Override
	public void resetPropertyValue(Object id) {
		// Do nothing
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 *      .lang.Object, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object id, Object value) {
		// Do nothing
		return;
	}

}
