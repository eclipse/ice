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

import org.eclipse.ice.analysistool.IAnalysisAsset;
import static org.eclipse.ice.analysistool.IAnalysisAsset.*;
import static org.eclipse.ice.kdd.kddmath.KDDMatrix.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.analysistool.AnalysisAssetType;

import java.util.Hashtable;
import java.net.URI;
import java.util.Properties;

import org.eclipse.ice.datastructures.form.Entry;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * KDDStrategy is the top-level abstraction for a Strategy design pattern used
 * for running various families of clustering and anomaly detection algorithms.
 * Subclasses simply implement the executeStrategy method with their specific
 * clustering or anomaly detection algorithm. Additionally, KDDStrategy is a
 * realization of the IAnalysisAsset interface, and as such, must produce a URI
 * of the data it represents to be displayed to the user.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public abstract class KDDStrategy implements IAnalysisAsset {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The reference to the data this KDDStrategy has to work with.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<IDataProvider> dataForAnalysis;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of this IAnalysisAsset.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String assetName;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This IAnalysisAsset's list of properties.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected HashMap<String, String> properties;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The URI of this IAnalysisAsset..
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected URI uri;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor, takes an array of IDataProviders. By convention, the
	 * first IDataProvider of that array will be the loaded data to be analyzed.
	 * Any other IDataProviders will be reference or extra data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 * @param data
	 * @throws IllegalArgumentException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDStrategy(String name, ArrayList<IDataProvider> data)
			throws IllegalArgumentException {
		// begin-user-code
		assetName = name;
		dataForAnalysis = data;
		properties = new HashMap<String, String>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method allows subclasses to implement a unique clustering or anomaly
	 * detection algorithm and produce a KDDAnalysisAsset for clients to display
	 * and manipulate.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public abstract boolean executeStrategy();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDStrategy() {
		// begin-user-code
		assetName = "None";
		dataForAnalysis = new ArrayList<IDataProvider>();
		properties = new HashMap<String, String>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor, with name injection.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDStrategy(String name) {
		// begin-user-code
		assetName = name;
		dataForAnalysis = new ArrayList<IDataProvider>();
		properties = new HashMap<String, String>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the name of the asset.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The name
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName() {
		return assetName;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the type of the IAnalysisAsset.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The type of the asset
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public AnalysisAssetType getType() {
		return AnalysisAssetType.TABLE;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the value of one of the assets properties,
	 * requested by a key. If that property does not exist, it returns null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param key
	 *            <p>
	 *            The key of the property that should be retrieved.
	 *            </p>
	 * @return <p>
	 *         The value or null if the key does not exist.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getProperty(String key) {
		if (properties != null) {
			return properties.get(key);
		} else {
			return null;
		}
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the value of a property with the given key. It
	 * returns true if the key is in the properties list and false if not.
	 * Calling this operation will never add new properties to the list.
	 * </p>
	 * <!-- end-UML-doc -->
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
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations resets the properties of the asset to their default
	 * state.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void resetProperties() {
		properties.clear();
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the entire list of properties for this
	 * IAnalysisAsset as an instance of Java's Properties class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The properties of this asset or null if no properties exist.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Properties getProperties() {
		// Create a new Properties object to return
		Properties retProps = new Properties();

		// Fill it with the HashMap properties
		retProps.putAll(properties);

		// Return it
		return retProps;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the asset's properties as a list of Entry objects.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
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
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the URI of the asset.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The URI
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public URI getURI() {
		return uri;
	}
}