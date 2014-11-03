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

#include "LWRGridManager.h"
#include "LWRDataProvider.h"
#include <H5Cpp.h>
#include <memory>
#include "GridLocation.h"
#include "HDF5LWRTagType.h"
#include <stdexcept>
#include <map>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <sstream>
#include <stddef.h>
#include <stdio.h>
#include <string.h>
#include <iostream>
#include <algorithm>

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
LWRGridManager::LWRGridManager(LWRGridManager & arg) :
	LWRComponent(arg) {

	//Setup size
	this->size = arg.size;

	//Deep copy tree
	this->lWRComponents.clear();
	std::map<std::shared_ptr<GridLocation>, std::string>::iterator mapIter;
	for (mapIter = arg.lWRComponents.begin(); mapIter
			!= arg.lWRComponents.end(); ++mapIter) {

		//Create a new location
		std::shared_ptr<GridLocation> location(
				new GridLocation(*mapIter->first));

		//Add it to the tree
		this->lWRComponents.insert(
				std::pair<std::shared_ptr<GridLocation>, std::string>(
						location, mapIter->second));

	}

	//Setup HDF5 stuff
	this->hdf5GridTableSuffix = "'s Grid Table";
	this->dataH5GroupName = "Positions";
	this->headTableString = " headTable";
	this->dataTableString = " dataTable";
	this->HDF5LWRTag = LWRGRIDMANAGER;

}

/**
 * The Destructor
 */
LWRGridManager::~LWRGridManager() {
	//TODO Auto-generated method stub
}

/**
 * A parameterized Constructor.
 *
 * @param the size
 */
LWRGridManager::LWRGridManager(int size) {

	// begin-user-code

	//Setup LWRComponent Attributes
	this->name = "LWRGridManager 1";
	this->description = "LWRGridManager 1's Description";
	this->id = 1;
	this->HDF5LWRTag = LWRGRIDMANAGER;

	this->size = 1;

	//Setup size if it is at least 1 or greater.  Otherwise use defaults
	if (size > 0) {
		this->size = size;
	}
	this->hdf5GridTableSuffix = "'s Grid Table";
	this->dataH5GroupName = "Positions";
	this->headTableString = " headTable";
	this->dataTableString = " dataTable";

	//end-user-code

}

/**
 * Returns the maximum number of rows or columns.
 *
 * @return the size
 */
int LWRGridManager::getSize() {

	// begin-user-code

	return this->size;

	//end-user-code
}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool LWRGridManager::operator==(const LWRGridManager& otherObject) const {

	//Local declaration
	bool valueFound = false;

	//If the super or sizes dont match, return false
	if (LWRComponent::operator==(otherObject) == false
			|| this->lWRComponents.size() != otherObject.lWRComponents.size()) {
		return false;
	}

	//Iterate and compare
	std::map<std::shared_ptr<GridLocation>, std::string>::const_iterator
			mapIter;
	std::map<std::shared_ptr<GridLocation>, std::string>::const_iterator
			otherIter = otherObject.lWRComponents.begin();
	for (mapIter = this->lWRComponents.begin(); mapIter
			!= this->lWRComponents.end(); ++mapIter) {

		//If the first or last key are not equal, return false
		try {
			//Iterate over the second list to see if the second value exists.  If it does not, fail out.
			for (otherIter = otherObject.lWRComponents.begin(); otherIter
					!= otherObject.lWRComponents.end(); ++otherIter) {
				//If the names compare correctly AND they have the same location, then the value was located and correctly found
				if (otherIter->second.compare(mapIter->second) == 0
						&& mapIter->first.get()->operator ==(
								*otherIter->first.get()) == true) {
					valueFound = true;
					break;
				}
			}

			//If the value is not found or the locations are not equal, return false
			if (valueFound != true) {
				return false;
			}
			//Reset value found
			valueFound = false;
		} catch (const std::out_of_range& oor) {
			return false;
		}

	}

	//Everything good.  Return true!
	return true;

}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> LWRGridManager::clone() {

	// begin-user-code

	// Local Declarations
	std::shared_ptr<LWRGridManager> component(new LWRGridManager(*this));

	// Return the newly instantiated object
	return component;

	// end-user-code

}

/**
 * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
 *
 * @param The File
 * @param The Group
 *
 * @return True if successful, false otherwise
 */
bool LWRGridManager::writeAttributes(std::shared_ptr<H5::H5File> h5File,
		std::shared_ptr<H5::Group> h5Group) {

	// begin-user-code

	bool flag = true;

	flag &= LWRComponent::writeAttributes(h5File, h5Group);
	flag &= ICE_IO::HdfWriterFactory::writeIntegerAttribute(h5File, h5Group,
			"size", this->size);

	return flag;

	// end-user-code

}

/**
 * This operation writes HDF5 Datasets to the h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Datasets, then false is returned. Otherwise, true is returned.
 *
 * @param The File
 * @param The Group
 *
 * @return True if successful, false otherwise
 */
bool LWRGridManager::writeDatasets(std::shared_ptr<H5::H5File> h5File,
		std::shared_ptr<H5::Group> h5Group) {

	// begin-user-code

	bool flag = true;

	// Return if the file or group is null
	if (h5File.get() == NULL || h5Group.get() == NULL)
		return false;

	// Call super's method
	flag &= LWRComponent::writeDatasets(h5File, h5Group);

	// Return true if there are no operations to write
	if (this->lWRComponents.size() == 0)
		return true;

	return LWRGridManager::writeFeatureSets(h5File, h5Group);
	// end-user-code


	return flag;

	//end-user-code
}

bool LWRGridManager::writeFeatureSets(std::shared_ptr<H5::H5File> h5File,
		std::shared_ptr<H5::Group> h5Group) {

	//begin-user-code

	//IF the passed parameters are NULL, return false;
	if (h5File.get() == NULL || h5Group.get() == NULL) {
		return false;
	}

	//Local Declarations
	// Gather all units and put into a table later.
	std::shared_ptr<std::vector<std::string> >unitsList (new std::vector<std::string>());
	std::vector<std::string> positionNames;
	std::shared_ptr<H5::Group> mainH5Group;
	// Setup the size of the String array Dataset
	int maxLength = 0;
	int maxPositionLength = 0;

	hsize_t adims[] = { 3 };

	//Return true if there are no datasets to write on the grid.
	if (this->lWRComponents.size() == 0) {
		return true;
	}

	//Make the main group
	mainH5Group = ICE_IO::HdfWriterFactory::createH5Group(h5File, this->dataH5GroupName,
			h5Group);

	// Iterate over the GridLocations and add them to the list
	// Iterate over a map
	std::map<std::shared_ptr<GridLocation>, std::string>::const_iterator
			mapIter;

	//Iterate over the map
	for (mapIter = this->lWRComponents.begin(); mapIter != this->lWRComponents.end(); ++mapIter) {

		//Holds information regarding the LWRDataProvider on each grid location
		int counter = 0;
		double previousTime = 0;

		//Get the location
		std::shared_ptr<GridLocation> location = mapIter->first;

		//Get the name of the position
		//Create the name of the subgroup
		std::stringstream ss1;
		std::stringstream ss2;
		ss1 << location.get()->getRow();
		ss2 << location.get()->getColumn();

		std::string nameOfPositionsGroup = "Position " + ss1.str() + " "
				+ ss2.str();

		//Get the HDF5Group for data
		std::shared_ptr<H5::Group> dataH5Group =
				ICE_IO::HdfWriterFactory::createH5Group(h5File,
						nameOfPositionsGroup, mainH5Group);


		int positionData[3];

		//Get the position

		bool addName = true;

		//Add positionNames if necessary
		for(int units = 0; units < positionNames.size(); units++) {

			//If the position name is in there, dont add it
			if(positionNames.at(units).compare(mapIter->second) == 0) {
				addName = false;
				break;

			}
		}

		//If you need to add units, add them
		if(addName == true) {

			positionNames.push_back(mapIter->second);

			//Setup the maxLength
			if (mapIter->second.length() > maxPositionLength) {
				maxPositionLength = (int)mapIter->second.size();
			}
		}

		//setup positionDataset - row, column, name index
		positionData[0] = location.get()->getRow();
		positionData[1] = location.get()->getColumn();

		for (int nameListIter = 0; nameListIter < positionNames.size(); nameListIter++) {

			//Find the name and location accordingly.
			if (positionNames.at(nameListIter).compare(mapIter->second) == 0) {

				//Set the iterator
				positionData[2] = nameListIter;
				break;
			}
		}

		//Create dataset and add
		try {

			//Setup dataspace
			hsize_t dimsPosition[] = { 3 };
			H5::DataSpace dataSpacePos(1, dimsPosition);

			// Create the simple dataset - dataList
			std::string positionDatasetName = "Position Dataset";
			H5::DataSet dataSetPosition = dataH5Group.get()->createDataSet(positionDatasetName.c_str(), ICE_IO::HdfWriterFactory::createIntegerH5Datatype(h5File), dataSpacePos);

			//Write datasets
			dataSetPosition.write(positionData, ICE_IO::HdfWriterFactory::createIntegerH5Datatype(h5File));

			//Close stuff
			dataSetPosition.close();
			dataSpacePos.close();

		} catch(...) {
			std::cout<< "LWRGridManager:  Error when creating dataset.  Returning false." << std::endl;
			return false;
		}

		//Get the previously set time.  This is important to store for when iterating over the list later.
		previousTime = location.get()->getLWRDataProvider().get()->getCurrentTime();

		//Get the provider
		std::shared_ptr<LWRDataProvider> provider = location.get()->getLWRDataProvider();

		//If the operation fails, return false
		if(LWRGridManager::writeTimeAtFeatureSet(dataH5Group, h5File, provider, unitsList) == false) {

			// Reset time
			location.get()->getLWRDataProvider().get()->setTime(previousTime);
			return false;

		}

		// Reset time
		location.get()->getLWRDataProvider().get()->setTime(previousTime);

	}

	try {

		//Only write if there are units to write!
		if(positionNames.size() > 0) {
			// Externalize the units
			char arrayUnits[positionNames.size()][maxPositionLength];

			for (int w = 0; w < positionNames.size(); w++) {
				strcpy(arrayUnits[w], positionNames.at(w).c_str());
			}

			// Setup dimensions
			hsize_t dimsStrings[] = { positionNames.size() };
			//Setup space
			hid_t dataspace_id = H5Screate_simple(1, dimsStrings, NULL);

			//Setup string
			hid_t dataTypeString = H5Tcopy(H5T_C_S1);
			H5Tset_size(dataTypeString, maxPositionLength);

			//Create the dataset
			hid_t dataset_id = H5Dcreate(mainH5Group.get()->getId(), "Simple Position Names Table", dataTypeString, dataspace_id, H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT );

			//Write the dataset
			H5Dwrite(dataset_id, dataTypeString, H5S_ALL, H5S_ALL, H5P_DEFAULT, arrayUnits);
		}

		//Only write if there are units to write!
		if(unitsList.get()->size() > 0) {

			//Get the max length first
			for (int w = 0; w < unitsList.get()->size(); w++) {
				if (unitsList.get()->at(w).length() > maxLength) {
					maxLength = unitsList.get()->at(w).size();
				}
			}

			// Externalize the units
			char arrayUnits[unitsList.get()->size()][maxLength];

			for (int w = 0; w < unitsList.get()->size(); w++) {
				strcpy(arrayUnits[w], unitsList.get()->at(w).c_str());

			}

			// Setup dimensions
			hsize_t dimsStrings[] = { unitsList.get()->size() };
			//Setup space
			hid_t dataspace_id = H5Screate_simple(1, dimsStrings, NULL);

			//Setup string
			hid_t dataTypeString = H5Tcopy(H5T_C_S1);
			H5Tset_size(dataTypeString, maxLength);

			//Create the dataset
			hid_t dataset_id = H5Dcreate(mainH5Group.get()->getId(), "Units Table", dataTypeString, dataspace_id, H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT );

			//Write the dataset
			H5Dwrite(dataset_id, dataTypeString, H5S_ALL, H5S_ALL, H5P_DEFAULT, arrayUnits);
		}

	} catch (...) {
		// Break and return
		return false;
	}

	return true;

	//end-user-code
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
bool LWRGridManager::writeTimeAtFeatureSet(std::shared_ptr<H5::Group> dataH5Group, std::shared_ptr<H5::H5File> h5File,
		std::shared_ptr<LWRDataProvider> provider, std::shared_ptr< std::vector< std::string > > unitsList) {

	//begin-user-code

	// Iterate over the dataTree and create timesteps for each key in
	// the tree
	for (int w = 0; w < provider.get()->getTimes().size(); w++) {

		//Get the time
		double time = provider.get()->getTimes().at(w);

		//Set the time in order to get the FeatureSet
		provider.get()->setTime(time);

		//Create a new timeStep group based on the prefix and the timeStep
		std::stringstream ss;
		ss << w;
		std::string timeName = "TimeStep: " + ss.str();
		std::shared_ptr<H5::Group> timeStepH5Group =
						ICE_IO::HdfWriterFactory::createH5Group(h5File, timeName,
								dataH5Group);

		//Add attributes to the timeStep group
		//Create attribute: time as Double
		ICE_IO::HdfWriterFactory::writeDoubleAttribute(h5File,
				timeStepH5Group, "time", time);

		// Create a Compound Dataset for each timeStep to represent the
		// collection of FeatureSets. This contains the list of Feature
		// Sets
		for (int i = 0; i < provider.get()->getFeaturesAtCurrentTime().size(); i++) {

			// Get the FeatureSet
			std::vector<std::shared_ptr<IData> >
			set = provider.get()->getDataAtCurrentTime( provider.get()->getFeatureList().at(i));

			// Get the number of IDatas stored on the FeatureSet
			int iDataSize = set.size();

			// Create a 2D array n x 5
			double dataList[iDataSize][5];

			// Create a head data table
			long headData[iDataSize][2];

			// Iterate over the IDatas to fill out the arrays listed
			// above
			for (int j = 0; j < set.size(); j++) {
				// Get the iData at the location
				std::shared_ptr<IData> iData = set.at(j);

				// Set value and uncertainty
				dataList[j][0] = iData.get()->getValue();
				dataList[j][1] = iData.get()->getUncertainty();

				// Get the position
				std::vector<double> pos = iData.get()->getPosition();
				dataList[j][2] = pos.at(0);
				dataList[j][3] = pos.at(1);
				dataList[j][4] = pos.at(2);

				//Check to see if you need to add units
				bool addUnits = true;

				//Iterate over the units and compare. If it does not need to be added ,set to false and break from loop
				for(int units = 0; units < unitsList.get()->size(); units++) {

					//If the units is in there, dont add it
					if(unitsList.get()->at(units).compare(iData.get()->getUnits()) == 0) {
						addUnits = false;
						break;

					}
				}

				//If you need to add units, add them
				if(addUnits == true) {
					unitsList.get()->push_back(iData.get()->getUnits());
				}

				// Once this is done, append to the headList. Note that
				// this is to be externalized out of this loop in the
				// future
				// Set the dataList id and the unitsList id
				headData[j][0] = j;
				headData[j][1] = -1; //Default -1
				for (int unitsListIter = 0; unitsListIter < unitsList.get()->size(); unitsListIter++) {

					//Find the name and location accordingly.
					if (unitsList.get()->at(unitsListIter).compare(iData.get()->getUnits()) == 0) {

						//Set the iterator
						headData[j][1] = long(unitsListIter);

						break;
					}
				}
			}

			try {

				// Calculate the dimensions of the length of each
				// dataSet by
				// the number of IDatas in the FeatureSet
				hsize_t dimsData[] = { iDataSize, 5 };
				hsize_t dimsHead[] = { iDataSize, 2 };
				H5::DataSpace dataSpace(2, dimsData);
				H5::DataSpace headSpace(2, dimsHead);

				// Get the datatype for integer and LONG (int)
				H5::PredType dataTypeDouble = ICE_IO::HdfWriterFactory::createFloatH5Datatype(h5File);
				H5::PredType dataTypeLong = H5::PredType::NATIVE_LONG;

				// Create the simple dataset - dataList
				H5::DataSet dataSet1 = timeStepH5Group.get()->createDataSet((set.at(0).get()->getFeature()+ this->dataTableString).c_str(), dataTypeDouble, dataSpace);

				// Create the simple dataset - headData
				H5::DataSet dataSet2 = timeStepH5Group.get()->createDataSet((set.at(0).get()->getFeature()+ this->headTableString).c_str(), dataTypeLong, headSpace);

				//Write datasets
				dataSet1.write(dataList, dataTypeDouble);
				dataSet2.write(headData, dataTypeLong);

				//Close stuff
				dataSet1.close();
				dataSet2.close();
				dataSpace.close();
				headSpace.close();
			} catch (...) {

				//Return false if an error is caught
				return false;
			}

		}


	}

	//Operation successful!  Return true
	return true;

	//end-user-code

}

/**
 * This operation reads Datasets from h5Group and assigns their values to class variables. If h5Group is null or an Exception is thrown, false is returned. If the Otherwise, true is returned.
 *
 * @param The Group
 *
 * @return True if successful, false otherwise
 */
bool LWRGridManager::readDatasets(std::shared_ptr<H5::Group> h5Group) {

	//check nullaries and running readDatasets on super group
	if (h5Group.get() == NULL || LWRComponent::readDatasets(h5Group) == false)
		return false;

	// Open the Positions dataSet
	// Return true if this is hit. Means that no positions were added at
	// this time.
	std::shared_ptr<H5::Group> dataH5Group;
	try {
		dataH5Group = ICE_IO::HdfReaderFactory::getChildH5Group(h5Group, this->dataH5GroupName);
	} catch (...){
		return true;
	}

	std::shared_ptr<H5::Group> positionsGroup;
	std::vector < std::string > arrayStrings;
	std::vector <std::string> arrayPositionNames;

	// Clear the tree
	this->lWRComponents.clear();

	//If the dataGroup is 0, return
	if(dataH5Group.get()->getNumObjs() == 0) return true;

	try {

		//Get the units and see if it exists.  If it does, continue.  If it throws an exception, then it does not exist
		std::shared_ptr<H5::DataSet> dset = ICE_IO::HdfReaderFactory::getDataset(dataH5Group, "Units Table");

		if(dset.get() != NULL) {
			//Get the info in order to read the data correctly
			hid_t space = H5Dget_space(dset.get()->getId());
			hsize_t ndims[1];
			H5Sget_simple_extent_dims(space, ndims, NULL);

			int rowSize1 = H5Tget_size(H5Dget_type(dset.get()->getId()));
			char rData[ndims[0]][rowSize1];
			//Read the data and convert to array
			H5Dread(dset.get()->getId(), H5Dget_type(dset.get()->getId()), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData);



			//Convert data to string
			for(int i = 0; i < ndims[0]; i++) {

				//Allocate on stack
				char indexData[rowSize1+1];

				//Take data and copy into indexData
				strncpy(indexData, rData[i], rowSize1);
				indexData[rowSize1] = '\0'; //Add null terminator

				//convert to string
				std::string str (indexData);

				//Add to vector
				arrayStrings.push_back(str);
			}

			//Close pieces if they are not needed anymore
			//H5Dclose(dset);
			H5Sclose(space);
		}


	} catch (...) {
		//Do nothing

	}

	try {
		//Get the Positions names
		std::shared_ptr<H5::DataSet> dset2 = ICE_IO::HdfReaderFactory::getDataset(dataH5Group, "Simple Position Names Table");

		//Return false if the dset2 does not exist.
		if(dset2.get() == NULL) return false;

		//Get the info in order to read the data correctly
		hid_t spacePos = H5Dget_space(dset2.get()->getId());
		hsize_t ndimsPos[1];
		H5Sget_simple_extent_dims(spacePos, ndimsPos, NULL);

		int rowSizePos = H5Tget_size(H5Dget_type(dset2.get()->getId()));
		char rDataPos[ndimsPos[0]][rowSizePos];
		//Read the data and convert to array
		H5Dread(dset2.get()->getId(), H5Dget_type(dset2.get()->getId()), H5S_ALL, H5S_ALL, H5P_DEFAULT, rDataPos);

		//Convert data to string
		for(int i = 0; i < ndimsPos[0]; i++) {
			//Allocate on stack
			char indexData[rowSizePos+1];

			//Take data and copy into indexData
			strncpy(indexData, rDataPos[i], rowSizePos);
			indexData[rowSizePos] = '\0'; //Add null terminator

			//convert to string
			std::string str (indexData);

			//Add to vector
			arrayPositionNames.push_back(str);
		}
	} catch (...) {
		std::cout << "LWRGridManager:  Unable to process position names dataset.  Returning" << std::endl;
		return false;
	}

	//Get the list of groups
	std::vector<std::shared_ptr<H5::Group> > positions = ICE_IO::HdfReaderFactory::getChildH5Groups(dataH5Group);

	for (int i = 0; i < positions.size(); i++) {

		// Get the positon
		std::shared_ptr<H5::Group> position = positions.at(i);

		//Setup Position data
		int positionData[3];

		//Open the dataset to get the position information
		try {

			//Get dataset
			std::shared_ptr<H5::DataSet> positionDataSet(new H5::DataSet(position.get()->openDataSet("Position Dataset")));

			//Read data
			hid_t spacePos = H5Dget_space(positionDataSet.get()->getId());

			//Get the Dimensions
			hsize_t ndimsPos[1];
			H5Sget_simple_extent_dims(spacePos, ndimsPos, NULL);

		    //Retrieve data over the position
			H5Dread(positionDataSet.get()->getId(), H5Dget_type(positionDataSet.get()->getId()), H5S_ALL, H5S_ALL, H5P_DEFAULT, positionData);

		} catch(...) {
			std::cout << "LWRGridManager:  Error when opening up dataset for positions group.  Returning" << std::endl;
			return false;
		}


		// Create the location and add it to the tree
		std::shared_ptr<GridLocation> location  ( new GridLocation(positionData[0], positionData[1]));
		std::string name = arrayPositionNames.at(positionData[2]);
		// Put it in the tree
		//Add the location
		this->lWRComponents.insert(std::pair<std::shared_ptr<GridLocation>, std::string>(location, name));

		//This is important:  If there is a units table with no data in it, then there should be no positions.  Flag error and exit
		if(arrayStrings.size() == 0 && position.get()->getNumObjs() > 1) return false;

		// If it has Members, then it contains LWRData. Iterate over and add
		// appropriately

		// Iterate over the time groups
		//Get the time groups
		std::vector<std::shared_ptr<H5::Group> > timeGroups = ICE_IO::HdfReaderFactory::getChildH5Groups(position);

		//Get the LWRDataProvider off the location
		std::shared_ptr<LWRDataProvider> provider = location.get()->getLWRDataProvider();

		//Pass the LWRDataProvider, the groups, and the array of units to read the time steps at the feature.
		//Return if the operation returns false
		if(LWRGridManager::readTimeStepsAtFeature(provider, timeGroups, arrayStrings) == false) {
			return false;
		}

	}


	return true;
}

/**
 * Reads the time steps at a feature and adds the time steps to the GridLocation.  Returns true if the operation was successful, false otherwise.
 *
 * @param provider The LWRDataProvider
 * @param timeStepsMemberList The time steps on that group
 * @param arrayStrings  An array of strings used to specify the unit list.
 * @return
 */
bool LWRGridManager::readTimeStepsAtFeature(std::shared_ptr<LWRDataProvider> provider, std::vector<std::shared_ptr< H5::Group> > timeGroups, std::vector< std::string> arrayStrings) {

	//begin-user-code

	for (int j = 0; j < timeGroups.size(); j++) {

		std::shared_ptr<H5::Group> timeGroup  = timeGroups.at(j);
		double time = ICE_IO::HdfReaderFactory::readDoubleAttribute(timeGroup, "time");

		// Iterate over the available datasets.  Note there will be two dataSets per feature.
		//NOTE K+=2!!!!!
		for (int k = 0; k < timeGroup.get()->getNumObjs(); k+=2) {

			hid_t dset1Data, dset1Head;

			// Get the FeatureGroupHead
			dset1Data = H5Dopen(timeGroup.get()->getId(), timeGroup.get()->getObjnameByIdx(k).c_str(), H5P_DEFAULT);
			dset1Head = H5Dopen(timeGroup.get()->getId(), timeGroup.get()->getObjnameByIdx(k+1).c_str(), H5P_DEFAULT);

			//Get space
			hid_t space1Data = H5Dget_space(dset1Data);
			hid_t space1Head = H5Dget_space(dset1Head);

			//Get the ndims
			hsize_t ndims1Data[2], ndims1Head[2];
			int rank1Data = H5Sget_simple_extent_dims(space1Data, ndims1Data, NULL);
			int rank1Head = H5Sget_simple_extent_dims(space1Head, ndims1Head, NULL);

			//Get data - prepare on stack
			double dataArray [ndims1Data[0]][ndims1Data[1]];
			long headArray [ndims1Head[0]][ndims1Head[1]];

			//Retrieve data
			H5Dread(dset1Data, H5Dget_type(dset1Data), H5S_ALL, H5S_ALL, H5P_DEFAULT, dataArray);
			H5Dread(dset1Head, H5Dget_type(dset1Head), H5S_ALL, H5S_ALL, H5P_DEFAULT, headArray);

			// Get the name of the feature - note replace the dataTableString
			std::string featureNameFull = timeGroup.get()->getObjnameByIdx(k);
			std::string featureName = timeGroup.get()->getObjnameByIdx(k);

			//Strip the this->dataTableString from the name
			size_t start_pos = featureNameFull.find(this->dataTableString);

			if(start_pos != std::string::npos) {
				featureName = featureNameFull.replace(start_pos, this->dataTableString.length(), "");
			}

			//Iterate over the list and grab values as necessary

			//Counts the iterations in the following value grabber
			int counter = 0;

			// Iterate over the values and create the correct LWRData
			//Iterate by columnsize
			for (int l = 0; l < ndims1Data[0]; l++) {

				// Create the data and setup basic attributes
				std::shared_ptr<LWRData> lwrdata (new LWRData(featureName));
				lwrdata.get()->setValue(dataArray[l][0]);
				lwrdata.get()->setUncertainty(dataArray[l][1]);

				//This states:  At position X in the array of string units, give me the headArray's second (or last) column value for each row.
				//The last column value should represent the unitsID, or the id to represent the units
				lwrdata.get()->setUnits(arrayStrings.at((int) headArray[l][1]));

				// Setup position
				std::vector<double> positionss;
				positionss.push_back(dataArray[l][2]);
				positionss.push_back(dataArray[l][3]);
				positionss.push_back(dataArray[l][4]);

				// Set position
				lwrdata.get()->setPosition(positionss);

				// Add the lwrdata to the location
				provider.get()->addData(lwrdata, time);

				counter++;
			}

		}

	}

	return true;

	//end-user-code

}

/**
 * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
 *
 * @param  The Group
 *
 * @return True if successful, false otherwise
 */
bool LWRGridManager::readAttributes(std::shared_ptr<H5::Group> h5Group) {

	//begin-user-code

	//Local attributes (so we only call read ONCE)
	//These will clear out by the garbage collector
	int size;

	//Check super
	if (h5Group.get() == NULL) {
		return false;
	}

	//Get the information.  If any fail out, return false and do not change data.
	try {
		if (LWRComponent::readAttributes(h5Group) == false)
			return false;
		size = ICE_IO::HdfReaderFactory::readIntegerAttribute(h5Group, "size");
	} catch (...) {
		return false;
	}

	//Set the primitive data
	this->size = size;

	return true;

	//end-user-code

}

/**
 * Overridden from IGridManager
 */
const std::string LWRGridManager::getComponentName(
		std::shared_ptr<GridLocation> location) {

	//Local declarations
	std::string nullString;
	std::map<std::shared_ptr<GridLocation>, std::string>::iterator mapIter;

	if (location.get() != NULL) {

		//Iterate the map
		for (mapIter = this->lWRComponents.begin(); mapIter
				!= this->lWRComponents.end(); ++mapIter) {

			//If the location is found, return the name
			if (mapIter->first.get()->operator ==(*location.get())) {
				return mapIter->second;
			}
		}

	}

	//Either no string found or location is null
	return nullString;

}

/**
 * Overridden from IGridManager
 */
void LWRGridManager::addComponent(
		std::shared_ptr<ICE_DS::Component> component,
		std::shared_ptr<GridLocation> location) {

	//Local declarations
	std::map<std::shared_ptr<GridLocation>, std::string>::iterator mapIter;

	if (location.get() != NULL && component.get() != NULL
			&& location.get()->getColumn() < this->size
			&& location.get()->getRow() < this->size) {

		//Iterate the map
		for (mapIter = this->lWRComponents.begin(); mapIter
				!= this->lWRComponents.end(); ++mapIter) {

			//If the location is found, bail out
			if (mapIter->first.get()->getRow() == location.get()->getRow() && mapIter->first.get()->getColumn() == location.get()->getColumn()) {
				return;
			}
		}

		//Location is not found, then add it
		this->lWRComponents.insert(
				std::pair<std::shared_ptr<GridLocation>, std::string>(
						location, component.get()->getName()));

	}

	//Either location was null or component was null
	return;
}

/**
 * Overridden from IGridManager
 */
void LWRGridManager::removeComponent(std::shared_ptr<GridLocation> location) {

	//Local declarations
	std::map<std::shared_ptr<GridLocation>, std::string>::iterator mapIter;

	if (location.get() != NULL) {

		//Iterate the map
		for (mapIter = this->lWRComponents.begin(); mapIter
				!= this->lWRComponents.end(); ++mapIter) {

			//If the location is found, remove and return
			if (mapIter->first.get()->operator ==(*location.get())) {

				//Erase the item
				this->lWRComponents.erase(mapIter->first);
				return;
			}
		}

	}

	//Either no string found or location is null
	return;

}

/**
 * Overridden from IGridManager
 */
void LWRGridManager::removeComponent(
		std::shared_ptr<ICE_DS::Component> component) {

	//Local declarations
	std::map<std::shared_ptr<GridLocation>, std::string>::iterator mapIter;

	if (component.get() != NULL) {

		//Iterate the map
		for (mapIter = this->lWRComponents.begin(); mapIter
				!= this->lWRComponents.end(); ++mapIter) {

			//If the name is found, remove and return
			if (mapIter->second.compare(component.get()->getName()) == 0) {

				//Erase the item
				this->lWRComponents.erase(mapIter->first);
				return;
			}
		}

	}

	//Either no string found or location is null
	return;

}

/**
 * Gets the data provider at location or null
 *
 * @param the location
 *
 * @return the provider @ location
 */
std::shared_ptr<LWRDataProvider> LWRGridManager::getDataProviderAtLocation(
		std::shared_ptr<GridLocation> location) {

	//begin-user-code

	//Local declarations
	std::map<std::shared_ptr<GridLocation>, std::string>::iterator mapIter;
	std::shared_ptr<LWRDataProvider> provider;

	//If the location is not null
	if (location.get() != NULL) {

		//Iterate the map
		for (mapIter = this->lWRComponents.begin(); mapIter
				!= this->lWRComponents.end(); ++mapIter) {

			//If the location is found, return the provider
			if (mapIter->first.get()->getRow() == location.get()->getRow()
					&& mapIter->first.get()->getColumn()
							== location.get()->getColumn()) {
				return mapIter->first.get()->getLWRDataProvider();

			}
		}

	}

	//Return the provider.  Can be null
	return provider;

	//end-user-code
}

/**
 * Gets the GridLocations at the name
 *
 * @param the name
 *
 * @return the GridLocations at the name
 */
std::vector<std::shared_ptr<GridLocation> > LWRGridManager::getGridLocationsAtName(
		const std::string name) {

	//begin-user-code

	//Local declarations
	std::map<std::shared_ptr<GridLocation>, std::string>::iterator mapIter;
	std::vector<std::shared_ptr<GridLocation> > list;

	//Iterate the map
	for (mapIter = this->lWRComponents.begin(); mapIter
			!= this->lWRComponents.end(); ++mapIter) {

		//If the name is found, add to list
		if (mapIter->second.compare(name) == 0) {
			list.push_back(mapIter->first);

		}
	}

	//return the list
	return list;

}
