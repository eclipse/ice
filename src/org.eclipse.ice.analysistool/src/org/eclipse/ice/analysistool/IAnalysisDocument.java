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
 * <p>IAnalysisDocuments are essentially collections of IAnalysisAssets. They create IAnalysisAssets based on input data provided to a document. They create a set of assets for each "slice" of data. A slice of data would be, for example, all of the data at a time t in a time series or all of the data at a position x in spatial data.</p><p>The types of assets that are created are determined in a two step process. First, the IAnalysisDocument publishes what, if any, assets it can create for the given data. A client then examines the set of assets - a set of strings that describe the assets - that can be created for the given data and chooses which ones should be created in the document. Specifying which assets will be created is done one time and applies to all data slices.</p><p>Reference data may be provided for use by IAnalysisDocuments that can make comparisons or perform other operations that use the reference.</p><p>Data may be added to an IAnalysisDocument through either a URI or an IDataProvider, but it may not be retrieved in the form of an IDataProvider because their is no guarantee that the data stored at a given URI can fit within the available memory and there is no current implementation of IDataProvider that works with serialized data. Realizations of this interface that primarily use data from an IDataProvider should write the data to a file and return the URI instead of returning null. Reference data is treated the same way.</p>
 * @author Eric J. Lingerfelt
 */
public interface IAnalysisDocument {
	/** 
	 * <p>This operation directs the IAnalysisDocument to load or otherwise use data from a folder or file with the specified URI. All IAnalysisAssets will be created from this data.</p>
	 * @param data <p>A URI to the data folder or file used to create assets for this document.</p>
	 * @return <p>True if the IAnalysisDocument can create assets from the data, false if not. If this operation returns false, this IAnalysisDocument should not be used.</p>
	 */
	public boolean loadData(URI data);

	/** 
	 * <p>This operation directs the IAnalysisDocument to load or otherwise use data from an IDataProvider. All IAnalysisAssets will be created from this data.</p>
	 * @param data <p>An IDataProvider used to create assets for this document.</p>
	 * @return <p>True if the IAnalysisDocument can create assets from the data, false if not. If this operation returns false, this IAnalysisDocument should not be used.</p>
	 */
	public boolean loadData(IDataProvider data);

	/** 
	 * <p>This operation directs the IAnalysisDocument to load or otherwise use data from a folder or file with the specified URI. All IAnalysisAssets will be created from this data.</p>
	 * @param data <p>A URI to the data folder or file used to retrieve the reference data.</p>
	 * @return <p>True if the IAnalysisDocument can create assets from the data, false if not. If this operation returns false, this IAnalysisDocument should not be used.</p>
	 */
	public boolean loadReferenceData(URI data);

	/** 
	 * <p>This operation directs the IAnalysisDocument to load or otherwise use data from an IDataProvider as a reference for comparison with the data loaded by loadData().</p>
	 * @param data <p>An IDataProvider used to retrieve the reference data.</p>
	 * @return <p>True if the IAnalysisDocument can create assets from the data, false if not. If this operation returns false, this IAnalysisDocument should not be used.</p>
	 */
	public boolean loadReferenceData(IDataProvider data);

	/** 
	 * <p>This operation returns a URI specifying the location of the data from which this IAnalysisDocument is creating IAnalysisAssets.</p>
	 * @return <p>A URI to the data folder or file used to create assets for this document.</p>
	 */
	public URI getData();

	/** 
	 * <p>This operation provides a list of all of the assets that can be created for the data under consideration by this IAnalysisDocument.</p>
	 * @return <p>The list of assets that can be created for this IAnalysisDocument or null if the document can not create any assets.</p>
	 */
	public ArrayList<String> getAvailableAssets();

	/** 
	 * <p>This operation returns the list of all of assets that should be or was created for the data at each slice for this IAnalysisDocument.</p>
	 * @return <p>The list of assets.</p>
	 */
	public ArrayList<String> getSelectedAssets();

	/** 
	 * <p>This operation specifies the list of all of assets that should be created for the data at each slice for this IAnalysisDocument. Setting the list of desired assets sets it for all data slices. In general, this operation should be called after getAvailableAssets() unless it is known for certain that the list of desired assets can be created in the document.</p>
	 * @param assets <p>The lists of assets that should be created for each slice of data in this IAnalysisDocument.</p>
	 */
	public void setSelectedAssets(ArrayList<String> assets);

	/** 
	 * <p>This operation creates the selected assets in the IAnalysisDocument. This is a potentially long-running operation depending on the data and the analysis operations.</p>
	 */
	public void createSelectedAssets();

	/** 
	 * <p>This operation returns the total number of data slices that exist in the document. This could be, for example, the total number of time steps in the data or the total number of cells. This operation will return 0 if the IAnalysisDocument can not manipulate the data.</p>
	 * @return <p>The total number of data slices in this IAnalysisDocument.</p>
	 */
	public int getTotalSlices();

	/** 
	 * <p>This operation returns a string that identifies the data slice in way that is more descriptive than just its slice id. This could be, for example, a number identifying the time step or the cell id.</p>
	 * @param sliceNumber
	 * @return <p>The identifier with more information about the slice.</p>
	 */
	public String getSliceIdentifier(int sliceNumber);

	/** 
	 * <p>This operation returns the set of assets created for the specified data slice.</p>
	 * @param sliceNumber <p>The slice of data for which assets should be retrieved.</p>
	 * @return <p>All of the assets for the specified slice of data in the document or null if no assets have been created or otherwise just don't exist.</p>
	 */
	public ArrayList<IAnalysisAsset> getAssetsAtSlice(int sliceNumber);

	/** 
	 * <p>This operation returns all assets created for all data slices in the IAnalysisDocument.</p>
	 * @return <p>All of the assets for all of the data slices in the document or null if no assets have been created or otherwise just don't exist.</p>
	 */
	public ArrayList<IAnalysisAsset> getAllAssets();

	/** 
	 * <p>This operation returns a URI specifying the location of the reference data used by this IAnalysisDocument.</p>
	 * @return <p>A URI to the data folder or file used to create assets for this document.</p>
	 */
	public URI getReferenceData();
}