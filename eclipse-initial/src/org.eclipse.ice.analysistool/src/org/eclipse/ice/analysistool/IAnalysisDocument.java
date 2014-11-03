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

import java.net.URI;
import java.util.ArrayList;

/** 
 * <!-- begin-UML-doc -->
 * <p>IAnalysisDocuments are essentially collections of IAnalysisAssets. They create IAnalysisAssets based on input data provided to a document. They create a set of assets for each "slice" of data. A slice of data would be, for example, all of the data at a time t in a time series or all of the data at a position x in spatial data.</p><p>The types of assets that are created are determined in a two step process. First, the IAnalysisDocument publishes what, if any, assets it can create for the given data. A client then examines the set of assets - a set of strings that describe the assets - that can be created for the given data and chooses which ones should be created in the document. Specifying which assets will be created is done one time and applies to all data slices.</p><p>Reference data may be provided for use by IAnalysisDocuments that can make comparisons or perform other operations that use the reference.</p><p>Data may be added to an IAnalysisDocument through either a URI or an IDataProvider, but it may not be retrieved in the form of an IDataProvider because their is no guarantee that the data stored at a given URI can fit within the available memory and there is no current implementation of IDataProvider that works with serialized data. Realizations of this interface that primarily use data from an IDataProvider should write the data to a file and return the URI instead of returning null. Reference data is treated the same way.</p>
 * <!-- end-UML-doc -->
 * @author els
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IAnalysisDocument {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the IAnalysisDocument to load or otherwise use data from a folder or file with the specified URI. All IAnalysisAssets will be created from this data.</p>
	 * <!-- end-UML-doc -->
	 * @param data <p>A URI to the data folder or file used to create assets for this document.</p>
	 * @return <p>True if the IAnalysisDocument can create assets from the data, false if not. If this operation returns false, this IAnalysisDocument should not be used.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean loadData(URI data);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the IAnalysisDocument to load or otherwise use data from an IDataProvider. All IAnalysisAssets will be created from this data.</p>
	 * <!-- end-UML-doc -->
	 * @param data <p>An IDataProvider used to create assets for this document.</p>
	 * @return <p>True if the IAnalysisDocument can create assets from the data, false if not. If this operation returns false, this IAnalysisDocument should not be used.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean loadData(IDataProvider data);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the IAnalysisDocument to load or otherwise use data from a folder or file with the specified URI. All IAnalysisAssets will be created from this data.</p>
	 * <!-- end-UML-doc -->
	 * @param data <p>A URI to the data folder or file used to retrieve the reference data.</p>
	 * @return <p>True if the IAnalysisDocument can create assets from the data, false if not. If this operation returns false, this IAnalysisDocument should not be used.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean loadReferenceData(URI data);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the IAnalysisDocument to load or otherwise use data from an IDataProvider as a reference for comparison with the data loaded by loadData().</p>
	 * <!-- end-UML-doc -->
	 * @param data <p>An IDataProvider used to retrieve the reference data.</p>
	 * @return <p>True if the IAnalysisDocument can create assets from the data, false if not. If this operation returns false, this IAnalysisDocument should not be used.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean loadReferenceData(IDataProvider data);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns a URI specifying the location of the data from which this IAnalysisDocument is creating IAnalysisAssets.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>A URI to the data folder or file used to create assets for this document.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public URI getData();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation provides a list of all of the assets that can be created for the data under consideration by this IAnalysisDocument.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The list of assets that can be created for this IAnalysisDocument or null if the document can not create any assets.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getAvailableAssets();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the list of all of assets that should be or was created for the data at each slice for this IAnalysisDocument.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The list of assets.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getSelectedAssets();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation specifies the list of all of assets that should be created for the data at each slice for this IAnalysisDocument. Setting the list of desired assets sets it for all data slices. In general, this operation should be called after getAvailableAssets() unless it is known for certain that the list of desired assets can be created in the document.</p>
	 * <!-- end-UML-doc -->
	 * @param assets <p>The lists of assets that should be created for each slice of data in this IAnalysisDocument.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSelectedAssets(ArrayList<String> assets);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation creates the selected assets in the IAnalysisDocument. This is a potentially long-running operation depending on the data and the analysis operations.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void createSelectedAssets();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the total number of data slices that exist in the document. This could be, for example, the total number of time steps in the data or the total number of cells. This operation will return 0 if the IAnalysisDocument can not manipulate the data.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The total number of data slices in this IAnalysisDocument.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getTotalSlices();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns a string that identifies the data slice in way that is more descriptive than just its slice id. This could be, for example, a number identifying the time step or the cell id.</p>
	 * <!-- end-UML-doc -->
	 * @param sliceNumber
	 * @return <p>The identifier with more information about the slice.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getSliceIdentifier(int sliceNumber);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the set of assets created for the specified data slice.</p>
	 * <!-- end-UML-doc -->
	 * @param sliceNumber <p>The slice of data for which assets should be retrieved.</p>
	 * @return <p>All of the assets for the specified slice of data in the document or null if no assets have been created or otherwise just don't exist.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IAnalysisAsset> getAssetsAtSlice(int sliceNumber);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns all assets created for all data slices in the IAnalysisDocument.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>All of the assets for all of the data slices in the document or null if no assets have been created or otherwise just don't exist.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IAnalysisAsset> getAllAssets();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns a URI specifying the location of the reference data used by this IAnalysisDocument.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>A URI to the data folder or file used to create assets for this document.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public URI getReferenceData();
}