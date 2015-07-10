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
package org.eclipse.ice.viz.service.paraview.proxy;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.viz.service.paraview.connections.ParaViewConnection;

/**
 * This class provides a basic implementation of an {@link IProxyProperty}.
 * Sub-classes will only need to worry about interactions with the associated
 * {@link IParaViewProxy}'s connection to ParaView.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractProxyProperty implements IProxyProperty {

	/**
	 * The name of the property.
	 */
	private final String name;
	/**
	 * The proxy associated with the property. In other words, this property
	 * belongs to the proxy.
	 */
	protected final IParaViewProxy proxy;

	/**
	 * The current value of the property.
	 */
	private final AtomicReference<String> value;

	/**
	 * The allowed values for the property.
	 */
	private final Set<String> allowedValues;

	/**
	 * The connection to ParaView. This should be the same one used by the
	 * {@link #proxy}.
	 */
	private ParaViewConnection connection;

	/**
	 * The default constructor.
	 * 
	 * @param name
	 *            The name of the property.
	 * @param proxy
	 *            The proxy that owns this property.
	 * @param connection
	 *            The connection used by the proxy.
	 */
	public AbstractProxyProperty(String name, IParaViewProxy proxy,
			ParaViewConnection connection) {
		this.name = name;
		this.proxy = proxy;
		this.connection = connection;

		// Initialize the value and allowed value references.
		value = new AtomicReference<String>();
		allowedValues = new TreeSet<String>();

		// Attempt to load the allowed values.
		value.set(findValue(connection));
		allowedValues.addAll(findAllowedValues(connection));

		return;
	}

	/*
	 * Implements a method from IProxyProperty.
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * Implements a method from IProxyProperty.
	 */
	@Override
	public String getValue() {
		return value.get();
	}

	/*
	 * Implements a method from IProxyProperty.
	 */
	@Override
	public Set<String> getAllowedValues() {
		return new TreeSet<String>(allowedValues);
	}

	/*
	 * Implements a method from IProxyProperty.
	 */
	@Override
	public boolean setValue(String value) throws NullPointerException,
			IllegalArgumentException, UnsupportedOperationException {

		// Check the value against the allowed values.
		if (!allowedValues.contains(value)) {
			// Throw an NPE if the value is null.
			if (value == null) {
				throw new NullPointerException("ParaView error: "
						+ "Cannot set \"" + getName() + "\" to a null value.");
			}
			// Throw an IllegalArgumentException if the value is not allowed.
			else {
				throw new IllegalArgumentException("ParaView error: "
						+ "Cannot set \"" + getName() + "\" to the value \""
						+ value + "\".");
			}
		}

		// Attempt to set the value via the client. If successful, update the
		// atomic reference to the value. Don't bother if the value is already
		// set.
		boolean changed = false;
		String currentValue = getValue();
		if (value == null && currentValue != null
				|| !value.equals(currentValue)) {
			if (setValueOnClient(value, connection)) {
				changed = true;
				this.value.set(value);
			}
		}

		return changed;
	}

	/**
	 * Finds the property's current value via the connection to ParaView.
	 * 
	 * @param connection
	 *            The connection to ParaView.
	 * @return The property's current value, or {@code null} if it could not be
	 *         found.
	 */
	protected abstract String findValue(ParaViewConnection connection);

	/**
	 * Finds the property's allowed values via the connection to ParaView.
	 * 
	 * @param connection
	 *            The connection to ParaView.
	 * @return The property's allowed values. This may be empty if the values
	 *         could not be loaded.
	 */
	protected abstract Set<String> findAllowedValues(
			ParaViewConnection connection);

	/**
	 * Sets the property's value via the connection to ParaView.
	 * 
	 * @param value
	 *            The new value for the property. This will have already been
	 *            vetted against the current value and the allowed values.
	 * @param connection
	 *            The connection to ParaView.
	 * @return True if the property changed to a <i>new</i> value, false
	 *         otherwise.
	 */
	protected abstract boolean setValueOnClient(String value,
			ParaViewConnection connection);
}
