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

#include "GridLabelProvider.h"
#include <memory>
#include <string>
#include <vector>
#include <ICEObject/Identifiable.h>
#include "IDataProvider.h"
#include <IHdfWriteable.h>
#include <IHdfReadable.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <stdio.h>
#include <string.h>
#include <iostream>
#include <algorithm>

using namespace ICE_Reactor;

/**
 * The copy constructor.
 */
GridLabelProvider::GridLabelProvider(GridLabelProvider & arg) : LWRComponent(arg) {

    //copy contents - deep
    this->size = arg.size;
    this->rowLabels.clear();
    this->columnLabels.clear();


    //Row and column labels
    for(int i = 0; i < arg.rowLabels.size(); i++) {
        this->rowLabels.push_back(arg.rowLabels[i]);
    }

    for(int i = 0; i < arg.columnLabels.size(); i++) {
        this->columnLabels.push_back(arg.columnLabels[i]);
    }

    this->ROW_LABELS_NAME = "Row Labels";
    this->COLUMN_LABELS_NAME = "Column Labels";
    this->LABELS_GROUP_NAME="Labels";
}

/**
 * The Destructor.
 */
GridLabelProvider::~GridLabelProvider() {
    //TODO Auto-generated method stub
}

/**
 * A parameterized constructor.
 *
 * @param The size
 */
GridLabelProvider::GridLabelProvider(int size) {

    // begin-user-code

    // Setup LWRComponent info
    this->name = "GridLabelProvider 1";
    this->description = "GridLabelProvider 1's Description";
    this->id = 1;
    this->HDF5LWRTag = GRID_LABEL_PROVIDER;

    // Default values
    this->size = 1;
    this->columnLabels;
    this->rowLabels;

    this->ROW_LABELS_NAME = "Row Labels";
    this->COLUMN_LABELS_NAME = "Column Labels";
    this->LABELS_GROUP_NAME="Labels";

    // Check size - can't be less than or equal to 0.
    if (size > 0) {
        this->size = size;
    }

    // end-user-code

}

/**
 * Returns the column position from a label.  Returns -1 if the label is not found or if the label is null.
 *
 * @param the column label
 *
 * @return The index of column label
 */
int GridLabelProvider::getColumnFromLabel(const std::string columnLabel) {

    // begin-user-code
    // If the column label is in there, or -1 if it does not exist
    std::vector<std::string>::iterator iter;
    iter = find(this->columnLabels.begin(), this->columnLabels.end(), columnLabel);

    if(iter == this->columnLabels.end()) {
        return -1;
    } else {
        return distance(this->columnLabels.begin(), iter);
    }


    // end-user-code
}

/**
 * Returns the row position from a label. Returns -1 if the label is not found or if the label is null.
 *
 * @param the row label
 *
 * @return The index of the row label
 */
int GridLabelProvider::getRowFromLabel(const std::string rowLabel) {
    // begin-user-code

    // If the row label is in there, or -1 if it does not exist
    std::vector<std::string>::iterator iter;
    iter = find(this->rowLabels.begin(), this->rowLabels.end(), rowLabel);

    if(iter == this->rowLabels.end()) {
        return -1;
    } else {
        return distance(this->rowLabels.begin(), iter);
    }


    // end-user-code
}

/**
 * Returns the label at position column.
 *
 * @param The index of column
 *
 * @return The label of the column at index
 */
const std::string GridLabelProvider::getLabelFromColumn(int column) {

    // begin-user-code
    std::string nullString;

    // Return the column label or null if it does not exist
    // Make sure its within the size
    // Make sure the column labels also have stuff in the arraylist
    if (column >= 0 && column < this->size && !(this->columnLabels.empty())) {
        return this->columnLabels[column];
    }

    // Return null if not bound correctly
    return nullString;

    // end-user-code

}

/**
 * Returns the label at position row.
 *
 * @param The index of row
 *
 * @return The label of the row at index
 */
const std::string GridLabelProvider::getLabelFromRow(int row) {

    // begin-user-code
    std::string nullString;

    // Return the row label or null if it does not exist
    // Make sure its within the size
    // Make sure the row labels also have stuff in the vector
    if (row >= 0 && row < this->size && !(this->rowLabels.empty())) {
        return this->rowLabels[row];
    }

    // Return null if not bound correctly
    return nullString;

    // end-user-code

}

/**
 * Sets the array of row labels ordered from top to bottom.
 *
 * @param The list of rowLabels
 */
void GridLabelProvider::setRowLabels(const std::vector<std::string> rowLabels) {

    // begin-user-code

    // If the rowLabels passed are not null and equal in size, then add them
    if (!rowLabels.empty() && rowLabels.size() == this->size) {
        this->rowLabels.clear(); // Clear out the current list
        for (int i = 0; i < rowLabels.size(); i++) {
            this->rowLabels.push_back(rowLabels.at(i));
        }
    }

    return;
    // end-user-code

}

/**
 * Sets the array of column labels ordered from left to right.
 *
 * @param The list of columnLabels
 */
void GridLabelProvider::setColumnLabels(const std::vector<std::string> columnLabels) {

    // begin-user-code

    // If the columnLabels passed are not null and equal in size, then add
    // them
    if (!(columnLabels.empty()) && columnLabels.size() == this->size) {
        this->columnLabels.clear(); // Clear out the current list
        for (int i = 0; i < columnLabels.size(); i++) {
            this->columnLabels.push_back(columnLabels.at(i));
        }
    }

    return;

    // end-user-code

}

/**
 * Returns the size for the row and column label ArrayLists.
 *
 * @return the size
 */
int GridLabelProvider::getSize() {

    return this->size;

}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param The object to compare
 *
 * @return true if equal, false otherwise
 */
bool GridLabelProvider::operator==(const GridLabelProvider &other) const {

    // begin-user-code

    // Get the equality of the values
    return LWRComponent::operator ==(other) && this->size == other.size && this->rowLabels == other.rowLabels && this->columnLabels == other.columnLabels;

    // end-user-code

}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return The newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> GridLabelProvider::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<GridLabelProvider> provider(new GridLabelProvider (*this));

    // Return the newly instantiated object
    return provider;

    // end-user-code

}

/**
 * Overrides writeAttributes operation on LWRComponent
 *
 * @param File
 * @param Group
 *
 * @return true if operation successful, false otherwise
 */
bool GridLabelProvider::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    //Write size and super attributes
    try {
        return LWRComponent::writeAttributes(h5File, h5Group) && ICE_IO::HdfWriterFactory::writeIntegerAttribute(h5File, h5Group, "size", this->size);
    } catch(...) {
        return false;
    }


    // end-user-code
}

/**
 * Overrides writeDatasets operation on LWRComponent
 *
 * @param File
 * @param Group
 *
 * @return true if operation successful, false otherwise
 */
bool GridLabelProvider::writeDatasets(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code
    bool flag = true;

    // Return if the file or group is null
    if (h5File == NULL || h5Group == NULL)
        return false;

    flag &= LWRComponent::writeDatasets(h5File, h5Group);

    if(this->rowLabels.size() == 0 && this->columnLabels.size() == 0) return true;

    std::shared_ptr<H5::Group> labelsGroup = ICE_IO::HdfWriterFactory::createH5Group(h5File, this->LABELS_GROUP_NAME, h5Group);

    // Check to see if we have any labels and
    // that they are of the right size
    if (this->rowLabels.size() != this->size || this->columnLabels.size() != this->size) {

        // If not return false
        return false;
    }

    int maxSizeRow = 0;
    int maxSizeCol = 0;

    try {

        //Get the largest sized string
        for(int i = 0; i < this->size; i++) {

            if(this->rowLabels[i].length() > maxSizeRow) {
                maxSizeRow = this->rowLabels[i].size();
            }

            if(this->columnLabels[i].length() > maxSizeCol) {
                maxSizeCol = this->columnLabels[i].size();
            }
        }

        // Initialize row and column String arrays
        char rowValues[this->size][maxSizeRow];
        char columnValues[this->size][maxSizeCol];

        // Loop over the labels
        for (int i = 0; i < this->size; i++) {

            // Assign the current label
            std::string rowLabel = this->rowLabels[i];
            std::string columnLabel = this->columnLabels[i];

            // Assign the label to the String array
            strcpy(rowValues[i], rowLabel.c_str());
            strcpy(columnValues[i], columnLabel.c_str());

        }

        hsize_t dims[] = { this->size };
        int rank = 1;
        hid_t space1 = H5Screate_simple(rank, dims, NULL);
        hid_t space2 = H5Screate_simple(rank, dims, NULL);

        //Get the string variable taken care of - Row
        hid_t strTypeRow = H5Tcopy(H5T_C_S1);
        H5Tset_size(strTypeRow, maxSizeRow);

        //Get the string variable taken care of - Col
        hid_t strTypeCol = H5Tcopy(H5T_C_S1);
        H5Tset_size(strTypeCol, maxSizeCol);

        //Create the dataset and then write it

        //Column labels
        hid_t datasetCol = H5Dcreate(labelsGroup.get()->getId(), this->COLUMN_LABELS_NAME.c_str(), strTypeCol, space1, H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        H5Dwrite(datasetCol, strTypeCol, H5S_ALL, H5S_ALL, H5P_DEFAULT, columnValues);

        //Row labels
        hid_t datasetRow = H5Dcreate(labelsGroup.get()->getId(), this->ROW_LABELS_NAME.c_str(), strTypeRow, space2, H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        H5Dwrite(datasetRow, strTypeRow, H5S_ALL, H5S_ALL, H5P_DEFAULT, rowValues);


        //Close values
        H5Dclose(datasetCol);
        H5Dclose(datasetRow);
        H5Sclose(space1);
        H5Sclose(space2);
        labelsGroup.get()->close();


    } catch (...) {

        return false;
    }

    //Close group
    labelsGroup.get()->close();

    return flag;

    // end-user-code

}

/**
 * Overrides readDatasets operation on LWRComponent
 *
 * @param File
 * @param Group
 *
 * @return true if operation successful, false otherwise
 */
bool GridLabelProvider::readDatasets(std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    // Call super
    bool flag = LWRComponent::readDatasets(h5Group);


    if(h5Group.get() == NULL || flag == false) return false;


    std::shared_ptr<H5::Group> labelGroup = ICE_IO::HdfReaderFactory::getChildH5Group(h5Group, LABELS_GROUP_NAME);

    //Group doesnt exist, return true
    if(labelGroup.get() == NULL) return true;

    //Check DataSet information
    try {
        //Get the dataset
        hid_t dset1 =  H5Dopen(labelGroup.get()->getId(), this->COLUMN_LABELS_NAME.c_str(), H5P_DEFAULT);
        hid_t dset2 =  H5Dopen(labelGroup.get()->getId(), this->ROW_LABELS_NAME.c_str(), H5P_DEFAULT);

        //Get the space
        hid_t space1 = H5Dget_space(dset1);
        hid_t space2 = H5Dget_space(dset2);
        //Get the Dimensions
        hsize_t ndims1[1];
        hsize_t ndims2[1];
        H5Sget_simple_extent_dims(space1, ndims1, NULL);
        H5Sget_simple_extent_dims(space2, ndims2, NULL);


        //Get data - prepare on stack
        int rowSize1 = H5Tget_size(H5Dget_type(dset2));
        int colSize1 = H5Tget_size(H5Dget_type(dset1));
        char rData1[size][colSize1];
        char rData2[size][rowSize1];

        //Retrieve data over the features
        H5Dread(dset1, H5Dget_type(dset1), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData1);
        H5Dread(dset2, H5Dget_type(dset2), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData2);

        //Check data - return false if array sizes are not equal!
        if(ndims1[0]!= ndims2[0]) {
            return false;
        }

        //Clear the vectors
        this->columnLabels.clear();
        this->rowLabels.clear();

        //Iterate over list and set up data
        for(int i = 0; i < ndims1[0]; i++) {

        	//Convert to char  array
        	char colArray[colSize1+1];
        	char rowArray[rowSize1+1];
        	memcpy(colArray, rData1[i], colSize1);
        	memcpy(rowArray, rData2[i], rowSize1);

        	//add nullary
        	colArray[colSize1] = 0;
        	rowArray[rowSize1] = 0;

        	//Convert values
        	std::string columnLabel (colArray);
        	std::string rowLabel (rowArray);

            //insert values
            this->columnLabels.push_back(columnLabel);
            this->rowLabels.push_back(rowLabel);

        }

        //Close resources
        H5Dclose(dset1);
        H5Sclose(space1);
        H5Dclose(dset2);
        H5Sclose(space2);
    } catch (...) {
        return false;
    }

    //Everything went well!  Return true
    return true;
}

/**
 * Overrides readAttributes operation on LWRComponent
 *
 * @param File
 * @param Group
 *
 * @return true if operation successful, false otherwise
 */
bool GridLabelProvider::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    //Local attributes (so we only call read ONCE)
    //These will clear out by the garbage collector
    int size;

    //Check super
    if(h5Group.get() == NULL) {
        return false;
    }

    //Get the information.  If any fail out, return false and do not change data.
    try {
        if (LWRComponent::readAttributes(h5Group) == false) return false;
        size = ICE_IO::HdfReaderFactory::readIntegerAttribute(h5Group, "size");
    } catch (...) {
        return false;
    }

    //Set the primitive data
    this->size = size;

    return true;
}
