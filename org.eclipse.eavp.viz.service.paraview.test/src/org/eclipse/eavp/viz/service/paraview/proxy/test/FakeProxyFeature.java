/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.paraview.proxy.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.paraview.proxy.ProxyFeature;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyProperty;
import org.eclipse.eavp.viz.service.paraview.test.FakeParaViewWebClient;

/**
 * This class provides a fake that can be used to test both
 * {@link ProxyProperty}s and {@link ProxyFeature}s. These should be passed to a
 * {@link FakeParaViewWebClient} so that, when queried, it can populate a
 * response based on each added fake property.
 * 
 * @author Jordan Deyton
 *
 */
public class FakeProxyFeature extends ProxyFeature {

	public final List<String> allowedValues = new ArrayList<String>();
	public String initialValue;
	public String propertyName;

	public FakeProxyFeature(String name, int index) {
		super(name, index);
	}

	public FakeProxyFeature(String name, int index, PropertyType type) {
		super(name, index, type, null, null);
	}

	public FakeProxyFeature(String name, int index, PropertyType type,
			ColorByMode mode, ColorByLocation location) {
		super(name, index, type, mode, location);
	}

	/**
	 * Sets the allowed values. These can be read by a
	 * {@link FakeParaViewWebClient} when generating a response to a query.
	 * 
	 * @param values
	 *            The allowed values.
	 */
	public void setAllowedValues(String... values) {
		allowedValues.clear();
		for (String value : values) {
			allowedValues.add(value);
		}
	}

	/**
	 * Exposes the property type for testing purposes.
	 * 
	 * @return The property type set at construction.
	 */
	public PropertyType getPropertyType() {
		return type;
	}

	@Override
	protected int getProxyId() {
		return 0;
	}
}
