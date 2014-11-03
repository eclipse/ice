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
package org.eclipse.ice.visitanalysistool;

import org.eclipse.ice.analysistool.IAnalysisDocument;
import org.eclipse.ice.analysistool.IDataProvider;

import java.util.Iterator;
import java.util.Vector;

import llnl.visit.avtDatabaseMetaData;
import java.util.Hashtable;
import java.io.File;
import java.net.URI;
import llnl.visit.ViewerMethods;
import llnl.visit.ViewerProxy;
import llnl.visit.ViewerState;
import llnl.visit.avtScalarMetaData;
import llnl.visit.avtTensorMetaData;
import llnl.visit.avtVectorMetaData;

import java.util.ArrayList;
import org.eclipse.ice.analysistool.IAnalysisAsset;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class implements the IAnalysisDocument interface for use with VisIt.</p>
 * <!-- end-UML-doc -->
 * @author els
 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisItAnalysisDocument implements IAnalysisDocument {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>A list of all of the assets that can be created for the data under consideration by this VisItAnalysisDocument.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> availableAssets;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>An instance of the VisIt Java API's avtDatabaseMetaData class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private avtDatabaseMetaData databaseMetaData;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>A list of selected assets that can be created for the data under consideration by this VisItAnalysisDocument.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> selectedAssets;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>A HashTable keyed on slice number associated with values that are sets of VisItAnalysisAssets.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Hashtable<Integer, ArrayList<IAnalysisAsset>> sliceTable;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The total number of slices in this database. </p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int totalSlices;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The URI pointing to the directory containing the database.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private URI data;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>An instance of the VisIt Java API's ViewerMethods class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ViewerMethods viewerMethods;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>An instance of the VisIt Java API's ViewerProxy class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ViewerProxy viewerProxy;
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>An instance of the VisIt Java API's ViewerState class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ViewerState viewerState;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The Constructor.</p>
	 * <!-- end-UML-doc -->
	 * @param viewerProxy <p>A fully initialized instance of a ViewerProxy class.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public VisItAnalysisDocument(ViewerProxy viewerProxy) {
		// begin-user-code

		//Assign local value
		this.viewerProxy = viewerProxy;

		//Create local ViewerState and ViewerMethods objects
		viewerMethods = viewerProxy.GetViewerMethods();
		viewerState = viewerProxy.GetViewerState();

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Initializes the available assets ArrayList.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void initializeAvailableAssets() {
		// begin-user-code

		//Create a new list to hold the assets 
		availableAssets = new ArrayList<String>();

		//Get the vector of scalars from the database metadata
		Vector<avtScalarMetaData> scalarVector = databaseMetaData.GetScalars();

		//If the vector is not null
		if (scalarVector != null) {

			//Get an iterator
			Iterator<avtScalarMetaData> itr = scalarVector.iterator();

			//While the iterator has more elements
			while (itr.hasNext()) {

				//Add the asset's name to the list
				availableAssets.add(itr.next().GetName());

			}

		}

		// end-user-code
	}
	
	/** 
	 * (non-Javadoc)
	 * @see IAnalysisDocument#loadData(URI data)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean loadData(URI data) {
		// begin-user-code

		//Assign the value of data to a local variable 
		this.data = data;
		
		//Get the file path from "File".  - SFH ~20121127@3:22PM
		File file = new File(data);
		System.err.println("File path: " + file.getAbsolutePath());
		System.err.println("ViewerMethods: " + viewerMethods.hashCode());
		System.err.println("File path: " + file.getPath());
		System.err.println("Exists? : " + file.exists());

		
		//Open the database located at the data URI
		if (viewerMethods.OpenDatabase(file.getAbsolutePath())) {

			//Request that the metadata object for slice 0 be filled.
			viewerMethods.RequestMetaData(file.getPath(), 0);

			//Get the database metadata object and assign to local variable
			databaseMetaData = viewerState.GetDatabaseMetaData();

			//Get the total number of slices (states) and assign to local variable
			totalSlices = databaseMetaData.GetNumStates();

			//Initialize the available assets collection
			initializeAvailableAssets();

			return true;

		}

		return false;

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IAnalysisDocument#getData()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public URI getData() {
		// begin-user-code
		return data;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IAnalysisDocument#getAvailableAssets()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getAvailableAssets() {
		// begin-user-code
		return availableAssets;
		// end-user-code
	}
	
	/** 
	 * (non-Javadoc)
	 * @see IAnalysisDocument#getSelectedAssets()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getSelectedAssets() {
		// begin-user-code
		return selectedAssets;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IAnalysisDocument#setSelectedAssets(ArrayList<String> assets)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSelectedAssets(ArrayList<String> assets) {
		// begin-user-code
		this.selectedAssets = assets;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IAnalysisDocument#createSelectedAssets()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void createSelectedAssets() {
		// begin-user-code

		//Create the slice table
		sliceTable = new Hashtable<Integer, ArrayList<IAnalysisAsset>>();

		//Cycle over all slices
		for (int i = 0; i < totalSlices; i++) {

			//Create a new collection for the assets at this slice
			ArrayList<IAnalysisAsset> list = new ArrayList<IAnalysisAsset>();

			//Put the new list into the slice table at this slice 
			sliceTable.put(i, list);

			//Cycle over all selected assets
			for (String assetName : selectedAssets) {

				//Create a new IAnalysisAsset for this selectedAsset for this slice
				IAnalysisAsset asset = new VisItAnalysisAsset(data, databaseMetaData, assetName, i, viewerProxy);

				//Add the asset to the list
				list.add(asset);

			}

		}

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IAnalysisDocument#getTotalSlices()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getTotalSlices() {
		// begin-user-code
		return totalSlices;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IAnalysisDocument#getSliceIdentifier(int sliceNumber)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getSliceIdentifier(int sliceNumber) {
		// begin-user-code

		//Currently we will just return the sliceNumber as a string
		return String.valueOf(sliceNumber);
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IAnalysisDocument#getAssetsAtSlice(int sliceNumber)
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IAnalysisAsset> getAssetsAtSlice(int sliceNumber) {
		// begin-user-code

		//Check to see if the slice number exists
		if (sliceTable.containsKey(sliceNumber)) {

			//Return the array list at sliceNumber
			return sliceTable.get(sliceNumber);
		}

		//Else return null
		return null;

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IAnalysisDocument#getAllAssets()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IAnalysisAsset> getAllAssets() {
		// begin-user-code

		//If the sliceTable is not null
		if (sliceTable != null) {

			//Create a temp list to hold the assets
			ArrayList<IAnalysisAsset> list = new ArrayList<IAnalysisAsset>();

			//Create an iterator to cycle over the sliceTable
			Iterator<ArrayList<IAnalysisAsset>> itr = sliceTable.values().iterator();

			//Loop over iterator while elements still exist
			while (itr.hasNext()) {

				//Add all of the assets at this slice to the temp list
				list.addAll(itr.next());

			}

			//Return the temp list
			return list;

		}

		//Else return null
		return null;

		// end-user-code
	}

	@Override
	public boolean loadData(IDataProvider data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadReferenceData(URI data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadReferenceData(IDataProvider data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public URI getReferenceData() {
		// TODO Auto-generated method stub
		return null;
	}

}