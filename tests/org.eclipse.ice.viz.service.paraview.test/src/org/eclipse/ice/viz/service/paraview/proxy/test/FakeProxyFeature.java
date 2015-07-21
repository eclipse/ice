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
package org.eclipse.ice.viz.service.paraview.proxy.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.paraview.proxy.ProxyFeature;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;

public class FakeProxyFeature extends ProxyFeature {

	public final List<String> allowedValues;
	public final String initialValue;
	public final String propertyName;

	public FakeProxyFeature(String name, int index, String propertyName,
			String initialValue, String... allowedValues) {
		super(name, index, PropertyType.DISCRETE, null, null);

		this.allowedValues = new ArrayList<String>(allowedValues.length);
		for (String value : allowedValues) {
			this.allowedValues.add(value);
		}
		this.initialValue = initialValue;
		this.propertyName = propertyName;
	}

	@Override
	protected List<String> findAllowedValues(IParaViewWebClient client) {
		return allowedValues;
	}

	@Override
	protected String findPropertyName(IParaViewWebClient client) {
		return propertyName;
	}

	@Override
	protected String findValue(IParaViewWebClient client) {
		return initialValue;
	}

	@Override
	protected List<String> findValues(IParaViewWebClient client) {
		List<String> values = new ArrayList<String>();
		values.add(initialValue);
		return values;
	}

	@Override
	protected boolean applyChanges() {
		return true;
	}
}
