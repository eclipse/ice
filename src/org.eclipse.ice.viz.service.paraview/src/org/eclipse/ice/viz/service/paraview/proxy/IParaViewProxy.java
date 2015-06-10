/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan H. Deyton (UT-Battelle, LLC.) - Initial API and implementation 
 *   and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.proxy;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;

/**
 * A proxy serves as an intermediary between client code and a particular file
 * loaded into a view in ParaView. Each proxy is responsible for <i>exactly</i>
 * one particular file for a given view. If another file needs to be
 * manipulated, another proxy should be created for that file.
 * 
 * @author Jordan Deyton
 *
 */
public interface IParaViewProxy {

	/**
	 * Opens the designated URI, if possible, using the specified ParaView
	 * connection.
	 * 
	 * @param connection
	 *            The connection to use when opening the URI. Should not be
	 *            {@code null}.
	 * @return True if the proxy could be opened using the connection (also when
	 *         it is already open), false otherwise.
	 * @throws NullPointerException
	 *             If the specified connection is {@code null}.
	 */
	public boolean open(ParaViewConnectionAdapter connection)
			throws NullPointerException;

	/**
	 * Gets the current URI associated with the proxy.
	 * 
	 * @return The URI pointing to the proxy's file.
	 */
	public URI getURI();

	/**
	 * Gets the categories of "features" in the file that can be rendered. This
	 * may include categories such as "point arrays" or "cell arrays".
	 * 
	 * @return A set of categories of "features" in the file.
	 */
	public Set<String> getFeatureCategories();

	/**
	 * Gets the file's "features" that can be rendered for the specified
	 * category.
	 * 
	 * @param category
	 *            The category whose features will be returned. This should be a
	 *            value retrieved from {@link #getFeatureCategories()}.
	 * @return A set of features that can be rendered. This set may be unique to
	 *         the specified category.
	 * @throws NullPointerException
	 *             If the specified category is {@code null}.
	 * @throws IllegalArgumentException
	 *             If the specified category is an invalid category.
	 */
	public Set<String> getFeatures(String category)
			throws NullPointerException, IllegalArgumentException;

	/**
	 * Sets the current feature that is rendered via ParaView.
	 * 
	 * @param category
	 *            The category for the feature. This should be one of the keys
	 *            in the map returned by {@link #getFeatures()}.
	 * @param feature
	 *            The feature or "type" to be rendered. This should be in the
	 *            set (from the feature map) associated with the category key.
	 * @return True if the feature was changed, false otherwise.
	 * @throws NullPointerException
	 *             If either of the specified arguments are {@code null}.
	 * @throws IllegalArgumentException
	 *             If either the category is invalid or the feature is not valid
	 *             for the category.
	 */
	public boolean setFeature(String category, String feature)
			throws NullPointerException, IllegalArgumentException;

	/**
	 * Gets the set of allowed properties for the proxy.
	 * 
	 * @return A set of properties that can be updated and may or may not affect
	 *         the rendered view.
	 */
	public Set<String> getProperties();

	/**
	 * Gets the set of allowed values for the specified property.
	 * 
	 * @param property
	 *            The property whose allowed values will be returned.
	 * @return A set containing all allowed values for the specified property.
	 * @throws NullPointerException
	 *             If the specified property is {@code null}.
	 * @throws IllegalArgumentException
	 *             If the specified property is an invalid property.
	 */
	public Set<String> getPropertyValues(String property)
			throws NullPointerException, IllegalArgumentException;

	/**
	 * Sets the specified property to the new value.
	 * 
	 * @param property
	 *            The property to updated. Allowed values can be retrieved via
	 *            {@link #getProperties()}.
	 * @param value
	 *            The new value of the property. Allowed values can be retrieved
	 *            via {@link #getPropertyValues(String)}.
	 * @return True if the property was changed, false otherwise.
	 * @throws NullPointerException
	 *             If either of the specified arguments are null.
	 * @throws IllegalArgumentException
	 *             If either the property name is invalid or the value is not
	 *             valid for the property.
	 */
	public boolean setProperty(String property, String value)
			throws NullPointerException, IllegalArgumentException;

	/**
	 * Updates the proxy's current properties to include all properties listed
	 * in the specified map. This can be used for a bulk update of properties
	 * that triggers only a single refresh of the rendered view.
	 * 
	 * @param properties
	 *            The new properties to set.
	 * @return The number of properties that were set.
	 * @throws NullPointerException
	 *             If the arguments are null or the map contains either null
	 *             properties or values.
	 * @throws IllegalArgumentException
	 *             If the map contains invalid properties or values.
	 */
	public int setProperties(Map<String, String> properties)
			throws NullPointerException, IllegalArgumentException;

	/**
	 * Gets the ID of the underlying view.
	 * 
	 * @return The ID of the underlying view, or {@code -1} if the proxy was not
	 *         opened.
	 */
	public int getViewId();
}
