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
package org.eclipse.ice.reactor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.*;
import ncsa.hdf.hdflib.*;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.io.hdf.*;
import org.eclipse.ice.datastructures.ICEObject.Component;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The LWRGridManager class manages LWRComponents and their GridLocations on a
 * Cartesian grid with an equal number of rows and columns. This class
 * implements the ICE IGridManager interface.
 * </p>
 * <p>
 * This class also allows a "pass through" for LWRDataProviders, which are used
 * to store state point data. This is a preferred method for storing data over
 * time instead of using LWRComponent's IDataProvider directly. Please see
 * GridLocation for more details on the usage of this delegation class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LWRGridManager extends LWRComponent implements IGridManager {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TreeMap<GridLocation, String> lWRComponents;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The size of the rows and columns.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int size;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A grid table suffix for reading the dataset.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String hdf5GridTableSuffix = "'s Grid Table";
	// Names for groups
	private String dataH5GroupName = "Positions";
	private String timeStepNamePrefix = "TimeStep: ";

	private String headTableString = " headTable";
	private String dataTableString = " dataTable";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param size
	 *            <p>
	 *            The maximum number of rows or columns.
	 *            </p>
	 *            <p>
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */

	public LWRGridManager(int size) {
		// begin-user-code

		// Setup LWRComponent Attributes
		this.name = "LWRGridManager 1";
		this.description = "LWRGridManager 1's Description";
		this.id = 1;

		// Setup defaults for the LWRGridManager
		lWRComponents = new TreeMap<GridLocation, String>();
		this.size = 1;

		// Setup size if it is at least 1 or greater. Otherwise use defaults
		if (size > 0) {
			this.size = size;
		}

		// Setup the HDF5LWRTagType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.LWRGRIDMANAGER;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the maximum number of rows or columns.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         Returns the maximum number of rows or columns.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getSize() {
		// begin-user-code

		return this.size;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		// Local Declarations
		LWRGridManager manager;
		boolean retVal = false;

		// Make sure the object is not null and is an instance of this object
		if (otherObject != null && otherObject instanceof LWRGridManager) {
			manager = (LWRGridManager) otherObject;

			// If they are equal to the same heap, return true
			if (this == otherObject) {
				return true;
			}
			// Check values
			retVal = (super.equals(otherObject)
					&& this.lWRComponents.equals(manager.lWRComponents) && this.size == manager.size);

			// If the size is not equal, return false
			if (this.lWRComponents.size() != manager.lWRComponents.size()) {
				return false;
			}

			// Check the map for comparisons
			for (Map.Entry<GridLocation, String> entry : this.lWRComponents
					.entrySet()) {

				// Grab the location
				GridLocation location = entry.getKey();
				boolean objectFound = false;

				// Iterate over the manager's list to see if the object exists.
				// If it does, mark it as true
				for (Map.Entry<GridLocation, String> managerEntry : manager.lWRComponents
						.entrySet()) {
					if (managerEntry.getKey().equals(location)
							&& managerEntry.getValue().equals(entry.getValue())) {
						objectFound = true;
						break;
					}
				}
				// If the object was not found, then return false.
				if (!objectFound) {
					return false;
				}
			}

		}

		// Return retVal
		return retVal;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hash of the object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Local Declarations
		int hash = super.hashCode();

		// Compute hash of attributes
		hash += 31 * this.lWRComponents.hashCode();
		hash += 31 * this.size;

		// Return the hash
		return hash;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(LWRGridManager otherObject) {
		// begin-user-code

		// Local Declarations
		Iterator<GridLocation> iter;
		GridLocation location;

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}
		// Copy contents- super
		super.copy(otherObject);

		// Copy contents

		this.size = otherObject.size;

		// Perform a deep copy of the tree
		this.lWRComponents.clear();

		// Get the iterator
		iter = otherObject.lWRComponents.keySet().iterator();

		// Iterate over the list, deep copy the lWRComponents and values
		while (iter.hasNext()) {
			location = iter.next();
			this.lWRComponents.put((GridLocation) location.clone(),
					otherObject.lWRComponents.get(location));
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Local Declarations
		LWRGridManager manager = new LWRGridManager(0);

		// Copy the contents of this manager
		manager.copy(this);

		// Return the newly instantiated object
		return manager;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IGridManager#getComponentName(GridLocation location)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getComponentName(GridLocation location) {
		// begin-user-code

		// If the location is not null, return the component
		if (location != null) {
			return this.lWRComponents.get(location);
		}
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IGridManager#addComponent(Component component, GridLocation
	 *      location)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addComponent(Component component, GridLocation location) {
		// begin-user-code

		// If the passed args are not null and if the locations are valid, add
		// to the grid
		// Also, if the location does already exist, do not add component
		if (component != null && location != null
				&& location.getRow() < this.size
				&& location.getColumn() < this.size && location.getRow() >= 0
				&& location.getColumn() >= 0
				&& !this.lWRComponents.containsKey(location)) {
			this.lWRComponents.put(location, component.getName());
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IGridManager#removeComponent(GridLocation location)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeComponent(GridLocation location) {
		// begin-user-code

		// If the location is not null, remove location
		if (location != null) {
			this.lWRComponents.remove(location);
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IGridManager#removeComponent(Component component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeComponent(Component component) {
		// begin-user-code

		// If the component is not null, remove the associated component
		if (component != null) {

			// Iterate over a map
			for (Map.Entry<GridLocation, String> entry : this.lWRComponents
					.entrySet()) {
				String lComponent = entry.getValue();

				// If the components are equal, remove the entry
				if (lComponent.equals(component.getName())) {
					this.lWRComponents.remove(entry.getKey());
					return;
				}
			}

		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 * @param h5Group
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		// begin-user-code
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeIntegerAttribute(h5File, h5Group, "size",
				size);

		return flag;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 * @param h5Group
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean writeDatasets(H5File h5File, H5Group h5Group) {
		// begin-user-code

		boolean flag = true;

		// Return if the file or group is null
		if (h5File == null || h5Group == null) {
			return false;
		}
		// Call super's method
		flag &= super.writeDatasets(h5File, h5Group);

		// Return true if there are no operations to write
		if (this.lWRComponents.isEmpty()) {
			return true;
		}
		return this.writeFeatureSets(h5File, h5Group);
		// end-user-code
	}

	//

	/**
	 * This writes the FeatureSets due to a given location
	 * 
	 * @param The
	 *            H5File
	 * @param The
	 *            Group
	 * 
	 * @return True if successful, false otherwise
	 */
	private boolean writeFeatureSets(H5File h5File, H5Group h5Group) {

		// begin-user-code

		// Return if the file or group is null
		if (h5File == null || h5Group == null) {
			return false;
		}
		// Gather all units and put into a table later.
		ArrayList<String> unitsList = new ArrayList<String>();
		ArrayList<String> positionNames = new ArrayList<String>();
		H5Group mainH5Group = null;

		// Setup the size of the String array Dataset
		int maxLength = 0;
		int maxPositionLength = 0;

		if (this.lWRComponents.isEmpty()) {
			return true;
		}

		// Make the main group
		mainH5Group = HdfWriterFactory.createH5Group(h5File,
				this.dataH5GroupName, h5Group);

		// Iterate over the GridLocations and add them to the list
		// Iterate over a map
		for (Map.Entry<GridLocation, String> entry : this.lWRComponents
				.entrySet()) {

			GridLocation location = entry.getKey();

			// Local Declarations
			H5Group dataH5Group;
			double previousTime = 0;

			// Create a HDF5Group for data
			dataH5Group = HdfWriterFactory.createH5Group(h5File, "Position "
					+ location.getRow() + " " + location.getColumn(),
					mainH5Group);

			// Create a Dataset. This contains Row, Column, and a position in
			// the positionNames table.
			long[] dimsPosition = { 3 };
			int[] positionData = new int[3];

			// Get the position
			if (!positionNames.contains(entry.getValue())) {
				positionNames.add(entry.getValue());
				maxPositionLength = Math.max(entry.getValue().length(),
						maxPositionLength);
			}

			// Store row, col, and the name at that position
			positionData[0] = location.getRow();
			positionData[1] = location.getColumn();
			// Get the index of that that position
			positionData[2] = positionNames.indexOf(entry.getValue());

			// Get the datatype for integer
			Datatype dataTypeInteger = HdfWriterFactory
					.createIntegerH5Datatype(h5File);

			// Create the simple dataset - dataList
			try {
				Dataset positionDataset = h5File.createScalarDS(
						"Position Dataset", dataH5Group, dataTypeInteger,
						dimsPosition, null, null, 0, positionData);

				positionDataset.init();
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}

			previousTime = location.getLWRDataProvider().getCurrentTime();

			// Get the LWRDataProvider
			LWRDataProvider provider = location.getLWRDataProvider();

			// Write the data at the individual time steps. If false, return
			if (!this.writeTimeAtFeatureSet(dataH5Group, h5File, provider,
					unitsList)) {

				// Reset time
				location.getLWRDataProvider().setTime(previousTime);

				return false;
			}

			// Reset time
			location.getLWRDataProvider().setTime(previousTime);
		}
		try {

			// Externalize names
			if (!(positionNames.isEmpty())) {
				// Externalize units
				String[] arrayUnits = new String[positionNames.size()];
				for (int w = 0; w < positionNames.size(); w++) {
					arrayUnits[w] = positionNames.get(w);
				}

				// Setup dimensions
				long[] dimsStrings = { positionNames.size() };

				// Setup string datatype
				H5Datatype datatypeString = (H5Datatype) h5File.createDatatype(
						Datatype.CLASS_STRING, maxPositionLength,
						Datatype.NATIVE, Datatype.NATIVE);
				Dataset dataSet3 = h5File.createScalarDS(
						"Simple Position Names Table", mainH5Group,
						datatypeString, dimsStrings, null, null, 0, null);
				dataSet3.write(arrayUnits);

				dataSet3.init();

			}

			// Only write if there are units to write!
			if (!(unitsList.isEmpty())) {
				// Externalize the units
				String[] arrayUnits = new String[unitsList.size()];
				for (int w = 0; w < unitsList.size(); w++) {

					// Get the length of the units
					maxLength = Math.max(unitsList.get(w).length(), maxLength);
					arrayUnits[w] = unitsList.get(w);
				}

				// Setup dimensions
				long[] dimsStrings = { unitsList.size() };

				// Setup string datatype
				H5Datatype datatypeString = (H5Datatype) h5File.createDatatype(
						Datatype.CLASS_STRING, maxLength, Datatype.NATIVE,
						Datatype.NATIVE);
				Dataset dataSet3 = h5File.createScalarDS("Units Table",
						mainH5Group, datatypeString, dimsStrings, null, null,
						0, null);
				dataSet3.write(arrayUnits);

				dataSet3.init();
			}

		} catch (Exception e) {
			// Break and return
			e.printStackTrace();
			return false;

		}

		return true;

		// end-user-code
	}

	/**
	 * Writes the Datasets for all the time steps to the group. Returns true if
	 * the operation was successful, false otherwise.
	 * 
	 * @param dataH5Group
	 *            The main data group
	 * @param h5File
	 *            The h5file
	 * @param provider
	 *            The LWRGridDataProvider @ Location
	 * @param unitsList
	 *            The list of units. Must be passed to keep the list maintained!
	 * @return True if successful, false otherwise.
	 */
	private boolean writeTimeAtFeatureSet(H5Group dataH5Group, H5File h5File,
			LWRDataProvider provider, ArrayList<String> unitsList) {
		// begin-user-code

		// Iterate over the dataTree and create timesteps for each key in
		// the tree
		for (int w = 0; w < provider.getTimes().size(); w++) {

			// Get the time
			double time = provider.getTimes().get(w);

			// Set the time in order to get the FeatureSet
			provider.setTime(time);

			// Create a new timeStep group based on the prefix and the
			// timeStep
			H5Group timeStepH5Group = HdfWriterFactory.createH5Group(h5File,
					timeStepNamePrefix + "" + w, dataH5Group);

			// Add attributes to the timeStep group

			// Create attribute: time as Double
			HdfWriterFactory.writeDoubleAttribute(h5File, timeStepH5Group,
					"time", time);

			// Create a Compound Dataset for each timeStep to represent the
			// collection of FeatureSets. This contains the list of Feature
			// Sets
			for (int i = 0; i < provider.getFeaturesAtCurrentTime().size(); i++) {

				// Get the FeatureSet
				ArrayList<IData> set = provider.getDataAtCurrentTime(provider
						.getFeatureList().get(i));

				H5Datatype datatype = null;

				// Get the number of IDatas stored on the FeatureSet
				int iDataSize = set.size();

				// Create a 2D array n x 5
				double[][] dataList = new double[iDataSize][5];

				// Create a head data table
				long[][] headData = new long[iDataSize][2];

				// Iterate over the IDatas to fill out the arrays listed
				// above
				for (int j = 0; j < set.size(); j++) {
					// Get the iData at the location
					IData iData = set.get(j);

					// Set value and uncertainty
					dataList[j][0] = iData.getValue();
					dataList[j][1] = iData.getUncertainty();

					// Get the position
					ArrayList<Double> pos = iData.getPosition();
					dataList[j][2] = pos.get(0).doubleValue();
					dataList[j][3] = pos.get(1);
					dataList[j][4] = pos.get(2);

					// If the unitsList does not contain the units, add them!
					if (!unitsList.contains(iData.getUnits())) {
						unitsList.add(iData.getUnits());

					}

					// Once this is done, append to the headList. Note that
					// this is to be externalized out of this loop in the
					// future
					// Set the dataList id and the unitsList id
					headData[j][0] = j;
					headData[j][1] = unitsList.indexOf(iData.getUnits());

				}

				try {

					// Calculate the dimensions of the length of each
					// dataSet by
					// the number of IDatas in the FeatureSet
					long[] dimsData = { iDataSize, 5 };
					long[] dimsHead = { iDataSize, 2 };

					// Get the datatype for integer and LONG (int)
					Datatype dataTypeDouble = HdfWriterFactory
							.createFloatH5Datatype(h5File);
					Datatype dataTypeLong = h5File.createDatatype(
							Datatype.CLASS_INTEGER, 8, Datatype.NATIVE,
							Datatype.NATIVE);

					// Create the simple dataset - dataList
					Dataset dataSet1 = h5File.createScalarDS(set.get(0)
							.getFeature() + this.dataTableString,
							timeStepH5Group, dataTypeDouble, dimsData, null,
							null, 0, dataList);

					// Create the simple dataset - headData
					Dataset dataSet2 = h5File.createScalarDS(set.get(0)
							.getFeature() + this.headTableString,
							timeStepH5Group, dataTypeLong, dimsHead, null,
							null, 0, headData);

					dataSet1.init();
					dataSet2.init();
				} catch (Exception e) {
					// Break and return
					e.printStackTrace();

					return false;
				}
			}

		}

		// Operation successful, return true
		return true;

		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides LWRComponent's readDatasets.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param h5Group
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean readDatasets(H5Group h5Group) {
		// begin-user-code

		// Call super
		boolean flag = super.readDatasets(h5Group);

		if (!flag) {
			return false;
		}
		// Open the Positions dataSet
		H5Group dataH5Group = HdfReaderFactory.getChildH5Group(h5Group,
				this.dataH5GroupName);
		Dataset unitsSet;
		Dataset positionNamesSet;
		String[] arrayStrings = null;
		String[] arrayPositions = null;

		// Return true if this is hit. Means that no positions were added at
		// this time.
		if (dataH5Group == null) {
			return true;
		}
		// Clear the tree
		this.lWRComponents.clear();

		// If the dataGroup is 0, return
		if (dataH5Group.getNumberOfMembersInFile() == 0) {
			return true;
		}
		// Get the last member list and make sure it is correct
		if (HdfReaderFactory.getDataset(dataH5Group, "Units Table") == null) {
			// Do nothing
		} else {
			unitsSet = HdfReaderFactory.getDataset(dataH5Group, "Units Table");

			// Cast to array list
			try {
				// Get data
				Object unitListData = unitsSet.getData();

				// Cast to array of Strings
				arrayStrings = (String[]) unitListData;

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		// Get the Position names table
		if (HdfReaderFactory.getDataset(dataH5Group,
				"Simple Position Names Table") == null) {
			// Return false. This is bad
			System.err
					.println("LWRGridManager: Can't find dataset for reading the positions table.");
			return false;
		} else {

			try {

				// Get the dataset
				positionNamesSet = HdfReaderFactory.getDataset(dataH5Group,
						"Simple Position Names Table");

				// Get data
				Object positionsNamesData = positionNamesSet.getData();

				// Cast to array of strings
				arrayPositions = (String[]) positionsNamesData;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		// Iterate over ONLY the groups
		ArrayList<H5Group> positionsMemberList = HdfReaderFactory
				.getChildH5Groups(dataH5Group);

		// Iterate over the group list
		for (int i = 0; i < positionsMemberList.size(); i++) {

			// Get the position
			H5Group position = positionsMemberList.get(i);

			// Get the position information from dataset
			Dataset positionDataset = HdfReaderFactory.getDataset(position,
					"Position Dataset");

			// Convert dataset to data
			Object positionData;
			try {
				positionDataset.init();
				positionData = positionDataset.getData();
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}

			// Get the positionInformation
			int[] positionInfo = { 3 };
			positionInfo = (int[]) positionData;

			// Create the location and add it to the tree
			GridLocation location = new GridLocation(positionInfo[0],
					positionInfo[1]);
			String name = arrayPositions[positionInfo[2]];

			// Put it in the tree
			this.lWRComponents.put(location, name);

			// This is important: If there is a units table with no data in it,
			// then there should be no positions. Flag error and exit
			if (arrayStrings == null && position.getNumberOfMembersInFile() > 1) {
				return false;
			}
			// Iterate over ONLY the groups
			ArrayList<H5Group> timeStepsMemberList = HdfReaderFactory
					.getChildH5Groups(position);
			LWRDataProvider provider = location.getLWRDataProvider();

			// Pass the LWRDataProvider, the groups, and the array of units to
			// read the time steps at the feature.
			// Return if the operation returns false
			if (!this.readTimeStepsAtFeature(provider, timeStepsMemberList,
					arrayStrings)) {
				return false;
			}

		}

		return true;

		// end-user-code
	}

	/**
	 * Reads the time steps at a feature and adds the time steps to the
	 * GridLocation. Returns true if the operation was successful, false
	 * otherwise.
	 * 
	 * @param provider
	 *            The LWRDataProvider
	 * @param timeStepsMemberList
	 *            The time steps on that group
	 * @param arrayStrings
	 *            An array of strings used to specify the unit list.
	 * @return
	 */
	private boolean readTimeStepsAtFeature(LWRDataProvider provider,
			ArrayList<H5Group> timeStepsMemberList, String[] arrayStrings) {
		// begin-user-code

		// Iterate over the time groups
		for (int j = 0; j < timeStepsMemberList.size(); j++) {

			H5Group timeGroup = timeStepsMemberList.get(j);
			double time = HdfReaderFactory.readDoubleAttribute(timeGroup,
					"time");

			ArrayList<HObject> memberList = HdfReaderFactory
					.getChildH5Members(timeGroup);

			// Iterate over the available datasets. Note there will be two
			// dataSets per feature.
			// NOTE K+=2!!!!!
			int timeListSize = memberList.size();
			for (int k = 0; k < timeListSize; k += 2) {

				// Get the FeatureGroupHead
				Dataset featureGroupData = (Dataset) memberList.get(k);
				Dataset featureGroupHead = (Dataset) memberList.get(k + 1);

				// Get the name of the feature - note replace the
				// dataTableString
				String featureName = featureGroupData.getName().replace(
						this.dataTableString, "");

				// call init
				featureGroupData.init();
				featureGroupHead.init();

				// Get the object data off the dataset
				Object headListData;
				Object dataListData;
				try {
					dataListData = featureGroupData.getData();
					headListData = featureGroupHead.getData();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

				// get the dimensions of data and head
				long dataRowSize = featureGroupData.getDims()[0];
				long dataColSize = featureGroupData.getDims()[1];
				long headRowSize = featureGroupHead.getDims()[0];
				long headColSize = featureGroupHead.getDims()[1];

				// Convert the data to a readable format
				double[] dataArray = (double[]) dataListData;
				long[] headArray = (long[]) headListData;

				// Iterate over the list and grab values as necessary

				// Counts the iterations in the following value grabber
				int counter = 0;

				// Iterate over the values and create the correct LWRData
				// Iterate by columnsize
				for (int l = 0; l < dataArray.length; l += dataColSize) {

					// Create the data and setup basic attributes
					LWRData lwrdata = new LWRData(featureName);
					lwrdata.setValue(dataArray[l]);
					lwrdata.setUncertainty(dataArray[l + 1]);

					// This states: At position X in the array of string
					// units, give me the headArray's second (or last)
					// column value for each row.
					// The last column value should represent the unitsID,
					// or the id to represent the units
					lwrdata.setUnits(arrayStrings[(int) headArray[(int) ((counter) * headColSize) + 1]]);

					// Setup position
					ArrayList<Double> positionss = new ArrayList<Double>();
					positionss.add(dataArray[l + 2]);
					positionss.add(dataArray[l + 3]);
					positionss.add(dataArray[l + 4]);

					// Set position
					lwrdata.setPosition(positionss);

					// Add the lwrdata to the location
					provider.addData(lwrdata, time);

					counter++;
				}

			}

		}

		// Operation successful. Return true
		return true;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param h5Group
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean readAttributes(H5Group h5Group) {
		// begin-user-code

		// Local Declarations
		boolean flag = true;

		// Get values
		Integer size = HdfReaderFactory.readIntegerAttribute(h5Group, "size");

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (size == null || !flag || h5Group == null) {
			return false;
		}
		// If everything is valid, then set data
		this.size = size.intValue();

		// Reset map
		this.lWRComponents.clear();

		return true;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the data provider at the grid location or null if it does not
	 * exist.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param location
	 *            <p>
	 *            The grid location.
	 *            </p>
	 * @return <p>
	 *         The provider at that location
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRDataProvider getDataProviderAtLocation(GridLocation location) {
		// begin-user-code

		if (location == null) {
			return null;
		}
		// Iterate over a map
		for (Map.Entry<GridLocation, String> entry : this.lWRComponents
				.entrySet()) {

			// If the key exists, return
			if (location.getRow() == entry.getKey().getRow()
					&& location.getColumn() == entry.getKey().getColumn()) {
				return entry.getKey().getLWRDataProvider();
			}
		}

		// Not found!
		return null;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the list of grid locations at the given name. If none are found,
	 * returns an empty list.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 *            <p>
	 *            The name
	 *            </p>
	 * @return <p>
	 *         The locations
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<GridLocation> getGridLocationsAtName(String name) {
		// begin-user-code

		// Local Declarations
		ArrayList<GridLocation> locations = new ArrayList<GridLocation>();

		if (name == null) {
			return locations;
		}
		// Iterate over a map
		for (Map.Entry<GridLocation, String> entry : this.lWRComponents
				.entrySet()) {

			// If the key exists, return
			if (name.equals(entry.getValue())) {
				locations.add((GridLocation) entry.getKey().clone());
			}
		}

		// Return list
		return locations;

		// end-user-code
	}

}