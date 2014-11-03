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
#ifndef SFREACTORIOHANDLER_H
#define SFREACTORIOHANDLER_H

#include <memory>
#include <stdexcept>
#include <string>
#include <vector>

#include "SFRComponent.h"
#include "SFReactor.h"
#include "assembly/Ring.h"
#include <hdf5.h>

namespace ICE_SFReactor {

// Class acts as an intermediary between the reactor and HDF5 data. This class
// both reads HDF5 data into the SFReactor, and writes from the SFReactor into
// HDF5 data.
class SFReactorIOHandler {

private:

	// This utility method throws an HDF5LibraryException with a custom message.
	void throwException(std::string message, int status) throw (std::runtime_error);

	// Gets a List of all child Objects of an HDF5 Group with the specified ID
	// and type.
	std::vector<std::string> getChildNames(int parentId, int objectType) throw (std::runtime_error);

	// Writes an integer attribute. The Java equivalent is in writeAttribute.
	void writeIntegerAttribute(int objectId, std::string name, int value) throw (std::runtime_error);

	// Reads an integer attribute. The Java equivalent is in readAttribute.
	int readIntegerAttribute(int objectId, std::string name) throw (std::runtime_error);

	// Writes a double attribute. The Java equivalent is in writeAttribute.
	void writeDoubleAttribute(int objectId, std::string name, double value) throw (std::runtime_error);

	// Reads a double attribute. The Java equivalent is in readAttribute.
	double readDoubleAttribute(int objectId, std::string name) throw (std::runtime_error);

	// Writes a std::string as an Attribute for an HDF5 Object, which is typically a
	// Group. This requires a special method because the std::string must first be
	// converted to a byte array.
	void writeStringAttribute(int objectId, std::string name, std::string value) throw (std::runtime_error);

	// Reads a std::string Attribute from an HDF5 Object, which is typically a Group.
	// This requires a special method because the std::string must first be converted
	// to a byte array.
	std::string readStringAttribute(int objectId, std::string name) throw (std::runtime_error);

	// Writes an integer dataset.
	void writeIntegerDataset(int objectId, std::string name, int rank, hsize_t* dims, int* buffer) throw (std::runtime_error);

	// This method writes an HDF5 Dataset containing the data that is stored in
	// a buffer. All of the data's properties and the buffer must be allocated
	// before calling this method.
	void writeDoubleDataset(int objectId, std::string name, int rank, hsize_t* dims, double* buffer) throw (std::runtime_error);

	// Writes a string dataset. This requires a special datatype be created
	// beforehand and closed after. The ID of the datatype should be provided as
	// the typeId parameter.
	void writeStringDataset(int objectId, std::string name, int rank, hsize_t* dims, int typeId, char* buffer) throw (std::runtime_error);

	// Writes all of the properties and data stored for an SFRComponent.
	void writeSFRComponent(std::shared_ptr<SFRComponent> component, int groupId) throw (std::runtime_error);

	// Reads all of the properties and data into an SFRComponent.
	void readSFRComponent(std::shared_ptr<SFRComponent> component, int groupId) throw (std::runtime_error);

	// Writes the data for a GridDataManager from a pre-constructed List of
	// IDataProviders (SFRComponents).
	void writeGridData(std::vector<std::shared_ptr<SFRComponent>> providers, int groupId) throw (std::runtime_error);

	// Writes all of the data from an IDataProvider (implemented by
	// SFRComponent).
	void writeDataProvider(std::shared_ptr<SFRComponent> provider, int groupId) throw (std::runtime_error);

	// Reads in the data for a GridDataManager into a pre-constructed List of
	// IDataProviders (SFRComponents).
	void readGridData(std::vector<std::shared_ptr<SFRComponent>> providers, int groupId) throw (std::runtime_error);

	// Reads all of the data in for an IDataProvider (implemented by
	// SFRComponent).
	void readDataProvider(std::shared_ptr<SFRComponent> provider, int groupId) throw (std::runtime_error);

	// Writes a List of locations, stored as Integers, as a Dataset.
	void writeLocationData(std::vector<int> locations, int groupId) throw (std::runtime_error);

	// Reads a List of locations, stored as Integers, from a Dataset.
	std::vector<int> readLocationData(int groupId) throw (std::runtime_error);

	// Writes an SFR Ring to an HDF5 Group. This includes its properties and the
	// Material stored in the Ring.
	void writeRing(std::shared_ptr<Ring> ring, int ringGroupId) throw (std::runtime_error);

	// Reads an SFR Ring from an HDF5 Group. This includes its properties and
	// Material stored in the Ring.
	std::shared_ptr<Ring> readRing(int ringGroupId) throw (std::runtime_error);

	// Creates and opens an HDF5 Group.
	int createGroup(int parentId, std::string name) throw (std::runtime_error);

	// Opens an HDF5 Group.
	int openGroup(int parentId, std::string name) throw (std::runtime_error);

	// Closes an HDF5 Group.
	void closeGroup(int groupId) throw (std::runtime_error);

	int createDataset(int parentGroupId, std::string name, int typeId, int dataspaceId) throw (std::runtime_error);

	int openDataset(int parentGroupId, std::string name) throw (std::runtime_error);

	void closeDataset(int datasetId) throw (std::runtime_error);

	int createDataspace(int rank, hsize_t* dims, hsize_t* maxDims) throw (std::runtime_error);

	int openDataspace(int datasetId) throw (std::runtime_error);

	void closeDataspace(int dataspaceId) throw (std::runtime_error);

	int createAttribute(int parentGroupId, std::string name, int typeId, int spaceId) throw(std::runtime_error);

	int openAttribute(int parentGroupId, std::string name) throw(std::runtime_error);

	void closeAttribute(int attributeId) throw(std::runtime_error);

	int createDatatype(H5T_class_t classId, int size) throw(std::runtime_error);

	void closeDatatype(int typeId) throw(std::runtime_error);

public:

	// Constructor.
	SFReactorIOHandler() {};

	// Reads data from an input HDF5 file into a SFReactor.
	std::shared_ptr<SFReactor> readHDF5(std::string path);

	// Writes data from the input SFReactor into a HDF5 file.
	void writeHDF5(std::shared_ptr<SFReactor> reactor, std::string path);

}; //end class SFReactorIOHandler

}// end namespace
#endif
