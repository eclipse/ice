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
package org.eclipse.ice.kdd.kddstrategy;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.ice.analysistool.AnalysisAssetType;
import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.datastructures.form.Entry;

/**
 * <p>
 * KDDStrategy is the top-level abstraction for a Strategy design pattern used
 * for running various families of clustering and anomaly detection algorithms.
 * Subclasses simply implement the executeStrategy method with their specific
 * clustering or anomaly detection algorithm. Additionally, KDDStrategy is a
 * realization of the IAnalysisAsset interface, and as such, must produce a URI
 * of the data it represents to be displayed to the user.
 * </p>
 * 
 * @author Alex McCaskey
 */
public abstract class KDDStrategy implements IAnalysisAsset {
	/**
	 * <p>
	 * The reference to the data this KDDStrategy has to work with.
	 * </p>
	 * 
	 */
	protected ArrayList<IDataProvider> dataForAnalysis;

	/**
	 * <p>
	 * The name of this IAnalysisAsset.
	 * </p>
	 * 
	 */
	protected String assetName;

	/**
	 * <p>
	 * This IAnalysisAsset's list of properties.
	 * </p>
	 * 
	 */
	protected HashMap<String, String> properties;

	/**
	 * <p>
	 * The URI of this IAnalysisAsset..
	 * </p>
	 * 
	 */
	protected URI uri;

	/**
	 * <p>
	 * The constructor, takes an array of IDataProviders. By convention, the
	 * first IDataProvider of that array will be the loaded data to be analyzed.
	 * Any other IDataProviders will be reference or extra data.
	 * </p>
	 * 
	 * @param name
	 * @param data
	 * @throws IllegalArgumentException
	 */
	public KDDStrategy(String name, ArrayList<IDataProvider> data)
			throws IllegalArgumentException {
		assetName = name;
		dataForAnalysis = data;
		properties = new HashMap<String, String>();
	}

	/**
	 * <p>
	 * This method allows subclasses to implement a unique clustering or anomaly
	 * detection algorithm and produce a KDDAnalysisAsset for clients to display
	 * and manipulate.
	 * </p>
	 * 
	 * @return
	 */
	public abstract boolean executeStrategy();

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 */
	public KDDStrategy() {
		assetName = "None";
		dataForAnalysis = new ArrayList<IDataProvider>();
		properties = new HashMap<String, String>();
	}

	/**
	 * <p>
	 * The constructor, with name injection.
	 * </p>
	 * 
	 * @param name
	 */
	public KDDStrategy(String name) {
		assetName = name;
		dataForAnalysis = new ArrayList<IDataProvider>();
		properties = new HashMap<String, String>();
	}

	/**
	 * <p>
	 * This operation returns the name of the asset.
	 * </p>
	 * 
	 * @return <p>
	 *         The name
	 *         </p>
	 */
	@Override
	public String getName() {
		return assetName;
	}

	/**
	 * <p>
	 * This operation returns the type of the IAnalysisAsset.
	 * </p>
	 * 
	 * @return <p>
	 *         The type of the asset
	 *         </p>
	 */
	@Override
	public AnalysisAssetType getType() {
		return AnalysisAssetType.TABLE;
	}

	/**
	 * <p>
	 * This operation returns the value of one of the assets properties,
	 * requested by a key. If that property does not exist, it returns null.
	 * </p>
	 * 
	 * @param key
	 *            <p>
	 *            The key of the property that should be retrieved.
	 *            </p>
	 * @return <p>
	 *         The value or null if the key does not exist.
	 *         </p>
	 */
	@Override
	public String getProperty(String key) {
		if (properties != null) {
			return properties.get(key);
		} else {
			return null;
		}
	}

	/**
	 * <p>
	 * This operation sets the value of a property with the given key. It
	 * returns true if the key is in the properties list and false if not.
	 * Calling this operation will never add new properties to the list.
	 * </p>
	 * 
	 * @param key
	 *            <p>
	 *            The key whose value should be set.
	 *            </p>
	 * @param value
	 *            <p>
	 *            The value for the specified key. This value will only be set
	 *            if the key exists.
	 *            </p>
	 * @return <p>
	 *         True if the key is in the list, false otherwise.
	 *         </p>
	 */
	@Override
	public boolean setProperty(String key, String value) {
		// Make sure the properties attribute is valid
		if (properties != null && !properties.isEmpty()) {
			// We want the Strategies to specify which properties they have
			// so if someone tries to set a property the Strategy doesn't
			// already contain, we want to fail here
			if (properties.containsKey(key)) {
				// Since that key exists, we can set it and
				// re-run the strategy. First set the new value and
				// get a copy of the old value just in case
				String oldValue = properties.put(key, value);

				// Now execute the strategy with the new data
				if (!executeStrategy()) {
					// If it didn't work, restore the old value
					// and return false
					properties.put(key, oldValue);
					return false;
				} else {
					// It did work, so return true
					return true;
				}
			} else {
				// Invalid property from user
				System.err
						.println("This IAnalysisAsset's properties does not contain "
								+ key + ". Set Property failed.");
				return false;
			}
		} else {
			// If we made it here, the Strategy subclass didn't initialize
			// properties, or doesn't take any
			System.err
					.println("This IAnalysisAsset does not take any new properties.");
			return false;
		}
	}

	/**
	 * <p>
	 * This operations resets the properties of the asset to their default
	 * state.
	 * </p>
	 * 
	 */
	@Override
	public void resetProperties() {
		properties.clear();
	}

	/**
	 * <p>
	 * This operation returns the entire list of properties for this
	 * IAnalysisAsset as an instance of Java's Properties class.
	 * </p>
	 * 
	 * @return <p>
	 *         The properties of this asset or null if no properties exist.
	 *         </p>
	 */
	@Override
	public Properties getProperties() {
		// Create a new Properties object to return
		Properties retProps = new Properties();

		// Fill it with the HashMap properties
		retProps.putAll(properties);

		// Return it
		return retProps;
	}

	/**
	 * <p>
	 * This operation returns the asset's properties as a list of Entry objects.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	public ArrayList<Entry> getPropertiesAsEntryList() {
		ArrayList<Entry> retList = new ArrayList<Entry>();
		Entry temp;
		int i = 1;
		for (String key : properties.keySet()) {
			temp = new Entry();
			temp.setName(key);
			temp.setValue(properties.get(key));
			temp.setId(i);
			i++;
			retList.add(temp);
		}

		return retList;
	}

	/**
	 * <p>
	 * This operation returns the URI of the asset.
	 * </p>
	 * 
	 * @return <p>
	 *         The URI
	 *         </p>
	 */
	@Override
	public URI getURI() {
		return uri;
	}
}