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
package org.eclipse.ice.analysistool;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import org.eclipse.ice.datastructures.form.Entry;
import java.net.URI;

/** 
 * <!-- begin-UML-doc -->
 * <p>This interface represents an asset created by an IAnalysisDocument. Each IAnalysisAsset has a name, a type and a URI from which the asset can be retrieved. IAnalysisAssets also have a select number of properties whose values can be set to manipulate the asset. New properties can not be added.</p><p>An example of an IAnalysisAsset is a plot that is stored as a Portable Network Graphic (PNG) file. It has a name, is a picture, is stored somewhere on the disk and has properties such as the x axis label and the title.</p>
 * <!-- end-UML-doc -->
 * @author els
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IAnalysisAsset {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the name of the asset.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The name</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the type of the IAnalysisAsset.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The type of the asset</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public AnalysisAssetType getType();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the value of one of the assets properties, requested by a key. If that property does not exist, it returns null.</p>
	 * <!-- end-UML-doc -->
	 * @param key <p>The key of the property that should be retrieved.</p>
	 * @return <p>The value or null if the key does not exist.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getProperty(String key);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation sets the value of a property with the given key. It returns true if the key is in the properties list and false if not. Calling this operation will never add new properties to the list.</p>
	 * <!-- end-UML-doc -->
	 * @param key <p>The key whose value should be set.</p>
	 * @param value <p>The value for the specified key. This value will only be set if the key exists.</p>
	 * @return <p>True if the key is in the list, false otherwise.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setProperty(String key, String value);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operations resets the properties of the asset to their default state.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void resetProperties();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the entire list of properties for this IAnalysisAsset as an instance of Java's Properties class.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The properties of this asset or null if no properties exist.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Properties getProperties();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the asset's properties as a list of Entry objects.</p>
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Entry> getPropertiesAsEntryList();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the URI of the asset.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The URI</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public URI getURI();
}