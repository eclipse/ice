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
#define BOOST_TEST_DYN_LINK
#define BOOST_TEST_MODULE GridLabelProviderTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <GridLabelProvider.h>
#include <LWRComponent.h>
#include <HDF5LWRTagType.h>
#include <string>
#include <vector>
#include <algorithm>
#include <HdfFileFactory.h>
#include <UtilityOperations.h>
#include <HdfReaderFactory.h>
#include <HdfWriterFactory.h>
#include <H5Cpp.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(GridLabelProviderTester_testSuite)

BOOST_AUTO_TEST_CASE(beforeClass) {

    //Create folder testPath in local directory - relative!
    std::string testFolder = "ICEIOTestsDir/";
    mkdir(testFolder.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    //Delete file
    std::string testFile = testFolder + "test.h5";
    remove(testFile.c_str());

}

BOOST_AUTO_TEST_CASE(checkConstruction) {

    // begin-user-code

    //Local declarations
    int defaultSize = 1;
    std::string defaultName = "GridLabelProvider 1";
    std::string defaultDescription = "GridLabelProvider 1's Description";
    int defaultId = 1;
    HDF5LWRTagType type = GRID_LABEL_PROVIDER;

    //new declarations
    int newSize = 10;

    //Show default value for constructor
    GridLabelProvider provider(defaultSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultSize, provider.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, provider.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, provider.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, provider.getId());
    BOOST_REQUIRE_EQUAL(type, provider.getHDF5LWRTag());

    //Show a new size set
    GridLabelProvider provider2(newSize);
    //Check values
    BOOST_REQUIRE_EQUAL(newSize, provider2.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, provider2.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, provider2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, provider2.getId());
    BOOST_REQUIRE_EQUAL(type, provider2.getHDF5LWRTag());

    //Show an illegal size, and notice the default value
    GridLabelProvider provider3(-1);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultSize, provider3.getSize()); //Defaults!
    BOOST_REQUIRE_EQUAL(defaultName, provider3.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, provider3.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, provider3.getId());
    BOOST_REQUIRE_EQUAL(type, provider3.getHDF5LWRTag());

    //Show an illegal size, and notice the default value
    GridLabelProvider provider4(0);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultSize, provider4.getSize()); //Defaults!
    BOOST_REQUIRE_EQUAL(defaultName, provider4.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, provider4.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, provider4.getId());
    BOOST_REQUIRE_EQUAL(type, provider4.getHDF5LWRTag());

    // end-user-code
    return;
}
BOOST_AUTO_TEST_CASE(checkRows) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    std::vector<std::string> validList;
    std::vector<std::string> invalidList;
    std::vector<std::string> emptyList;

    std::string nullString;

    //Setup list
    validList.push_back("A");
    validList.push_back("B");
    validList.push_back("C");
    validList.push_back("D");
    validList.push_back("E");

    invalidList.push_back("A");
    invalidList.push_back("B");

    //Check the default row size
    GridLabelProvider provider(size);
    BOOST_REQUIRE_EQUAL(size, provider.getSize());

    //Get the default values for rows - should all be null
    BOOST_REQUIRE_EQUAL(provider.getLabelFromRow(1), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromRow(0), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromRow(-1), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromRow(6), nullString); //Even though out of range, shouldn't break it

    //Check the default values for strings - should return null
    BOOST_REQUIRE_EQUAL(-1, provider.getRowFromLabel(nullString));
    BOOST_REQUIRE_EQUAL(-1, provider.getRowFromLabel("Not in there string191248015"));

    //Set invalid arraylist size
    provider.setRowLabels(invalidList);

    //Get the default values for rows - should all be null
    BOOST_REQUIRE_EQUAL(provider.getLabelFromRow(1), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromRow(0), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromRow(-1), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromRow(6), nullString); //Even though out of range, shouldn't break it

    //Check the default values for strings - should return null
    BOOST_REQUIRE_EQUAL(-1, provider.getRowFromLabel(nullString));
    BOOST_REQUIRE_EQUAL(-1, provider.getRowFromLabel("Not in there string191248015"));

    //Set a valid list
    provider.setRowLabels(validList);

    //Check list size
    BOOST_REQUIRE_EQUAL(validList.size(), provider.getSize());

    //Check each label to see it is in there
    for (int i = 0; i < validList.size(); i++) {
        //Check row count and label identification
        BOOST_REQUIRE_EQUAL(validList.at(i), provider.getLabelFromRow(i));
        BOOST_REQUIRE_EQUAL(i, provider.getRowFromLabel(validList.at(i)));
    }

    //Show that the rows can be set again and that the data is separated from the list
    //Set a valid list
    validList.erase(validList.begin());
    validList.insert(validList.begin(), "DD");
    provider.setRowLabels(validList);

    //Check list size
    BOOST_REQUIRE_EQUAL(validList.size(), provider.getSize());

    //Check each label to see it is in there
    for (int i = 0; i < validList.size(); i++) {
        //Check row count and label identification
        BOOST_REQUIRE_EQUAL(validList.at(i), provider.getLabelFromRow(i));
        BOOST_REQUIRE_EQUAL(i, provider.getRowFromLabel(validList.at(i)));
    }

    //Check ArrayList separation
    std::vector<std::string> newList = validList;
    validList.erase(validList.begin());
    validList.insert(validList.begin(), "BOBBY!");

    //Check list size
    BOOST_REQUIRE_EQUAL(newList.size(), provider.getSize());

    //Check each label to see it is in there
    for (int i = 0; i < newList.size(); i++) {
        //Check row count and label identification
        BOOST_REQUIRE_EQUAL(newList.at(i), provider.getLabelFromRow(i));
        BOOST_REQUIRE_EQUAL(i, provider.getRowFromLabel(newList.at(i)));
    }

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkColumns) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    std::vector<std::string> validList;
    std::vector<std::string> invalidList;
    std::vector<std::string> emptyList;

    std::string nullString;

    //Setup list
    validList.push_back("A");
    validList.push_back("B");
    validList.push_back("C");
    validList.push_back("D");
    validList.push_back("E");

    invalidList.push_back("A");
    invalidList.push_back("B");

    //Check the default column size
    GridLabelProvider provider(size);
    BOOST_REQUIRE_EQUAL(size, provider.getSize());

    //Get the default values for columns - should all be null
    BOOST_REQUIRE_EQUAL(provider.getLabelFromColumn(1), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromColumn(0), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromColumn(-1), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromColumn(6), nullString); //Even though out of range, shouldn't break it

    //Check the default values for strings - should return null
    BOOST_REQUIRE_EQUAL(-1, provider.getColumnFromLabel(nullString));
    BOOST_REQUIRE_EQUAL(-1, provider.getColumnFromLabel("Not in there string191248015"));

    //Set invalid vector size
    provider.setColumnLabels(invalidList);

    //Get the default values for rows - should all be null
    BOOST_REQUIRE_EQUAL(provider.getLabelFromColumn(1), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromColumn(0), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromColumn(-1), nullString);
    BOOST_REQUIRE_EQUAL(provider.getLabelFromColumn(6), nullString); //Even though out of range, shouldn't break it

    //Check the default values for strings - should return null
    BOOST_REQUIRE_EQUAL(-1, provider.getColumnFromLabel(nullString));
    BOOST_REQUIRE_EQUAL(-1, provider.getColumnFromLabel("Not in there string191248015"));

    //Set a valid list
    provider.setColumnLabels(validList);

    //Check list size
    BOOST_REQUIRE_EQUAL(validList.size(), provider.getSize());

    //Check each label to see it is in there
    for (int i = 0; i < validList.size(); i++) {
        //Check column count and label identification
        BOOST_REQUIRE_EQUAL(validList.at(i), provider.getLabelFromColumn(i));
        BOOST_REQUIRE_EQUAL(i, provider.getColumnFromLabel(validList.at(i)));
    }

    //Show that the columns can be set again and that the data is separated from the list
    //Set a valid list
    validList.erase(validList.begin());
    validList.insert(validList.begin(), "DD");
    provider.setColumnLabels(validList);

    //Check list size
    BOOST_REQUIRE_EQUAL(validList.size(), provider.getSize());

    //Check each label to see it is in there
    for (int i = 0; i < validList.size(); i++) {
        //Check column count and label identification
        BOOST_REQUIRE_EQUAL(validList.at(i), provider.getLabelFromColumn(i));
        BOOST_REQUIRE_EQUAL(i, provider.getColumnFromLabel(validList.at(i)));
    }

    //Check ArrayList separation
    std::vector<std::string> newList = validList;
    validList.erase(validList.begin());
    validList.insert(validList.begin(), "BOBBY!");

    //Check list size
    BOOST_REQUIRE_EQUAL(newList.size(), provider.getSize());

    //Check each label to see it is in there
    for (int i = 0; i < newList.size(); i++) {
        //Check column count and label identification
        BOOST_REQUIRE_EQUAL(newList.at(i), provider.getLabelFromColumn(i));
        BOOST_REQUIRE_EQUAL(i, provider.getColumnFromLabel(newList.at(i)));
    }

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    std::vector<std::string> rowLabels;
    std::vector<std::string> colLabels;
    int size = 3;
    std::string name = "Name of awesome";
    std::string description = "Description of awesome";
    int id = 12;

    //Add rows and columns
    rowLabels.push_back("Iffy");
    rowLabels.push_back("Biffy");
    rowLabels.push_back("Sissy");

    colLabels.push_back("Silly");
    colLabels.push_back("Billy");
    colLabels.push_back("Milling");

    //Setup root object
    GridLabelProvider object(size);
    object.setColumnLabels(colLabels);
    object.setRowLabels(rowLabels);
    object.setName(name);
    object.setDescription(description);
    object.setId(id);

    //Setup equalObject equal to object
    GridLabelProvider equalObject(size);
    equalObject.setColumnLabels(colLabels);
    equalObject.setRowLabels(rowLabels);
    equalObject.setName(name);
    equalObject.setDescription(description);
    equalObject.setId(id);

    //Setup transitiveObject equal to object
    GridLabelProvider transitiveObject(size);
    transitiveObject.setColumnLabels(colLabels);
    transitiveObject.setRowLabels(rowLabels);
    transitiveObject.setName(name);
    transitiveObject.setDescription(description);
    transitiveObject.setId(id);

    // Set its data, not equal to object
    GridLabelProvider unEqualObject(size);
    unEqualObject.setRowLabels(rowLabels);
    unEqualObject.setName(name);
    unEqualObject.setDescription(description);
    unEqualObject.setId(id);

    // Assert that these two objects are equal
    BOOST_REQUIRE_EQUAL(object==(equalObject), true);

    // Assert that two unequal objects returns false
    BOOST_REQUIRE_EQUAL(object==(unEqualObject), false);

    // Check that equals() is Reflexive
    // x==(x) = true
    BOOST_REQUIRE_EQUAL(object==(object), true);

    // Check that equals() is Symmetric
    // x==(y) = true iff y==(x) = true
    BOOST_REQUIRE_EQUAL(object==(equalObject) && equalObject==(object), true);

    // Check that equals() is Transitive
    // x==(y) = true, y==(z) = true => x==(z) = true
    if (object==(equalObject) && equalObject==(transitiveObject)) {
        BOOST_REQUIRE_EQUAL(object==(transitiveObject), true);
    } else {
        BOOST_FAIL("EQUALITY CHECK FAILURE!");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(object==(equalObject) && object==(equalObject)
                        && object==(equalObject), true);
    BOOST_REQUIRE_EQUAL(!(object==(unEqualObject))
                        && !(object==(unEqualObject))
                        && !(object==(unEqualObject)), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local Declarations
    std::vector<std::string> rowLabels;
    std::vector<std::string> colLabels;
    int size = 3;
    std::string name = "Name of awesome";
    std::string description = "Description of awesome";
    int id = 1;

    //Add rows and columns
    rowLabels.push_back("Iffy");
    rowLabels.push_back("Biffy");
    rowLabels.push_back("Sissy");

    colLabels.push_back("Silly");
    colLabels.push_back("Billy");
    colLabels.push_back("Milling");

    //Setup root object
    GridLabelProvider object(size);
    object.setColumnLabels(colLabels);
    object.setRowLabels(rowLabels);
    object.setName(name);
    object.setDescription(description);
    object.setId(id);

    //Run the copy routine
    GridLabelProvider copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);
    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<GridLabelProvider> castedObject (new GridLabelProvider(*(dynamic_cast<GridLabelProvider *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);


    return;

    // end-user-code

}

BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    GridLabelProvider provider(size);
    std::string name = "GRID!";
    std::string description = "LABELS!";
    int id = 4;
    HDF5LWRTagType tag = provider.getHDF5LWRTag();
    std::vector<std::string> columnLabels;
    std::vector<std::string> rowLabels;

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Setup column Labels
    columnLabels.push_back("Aa");
    columnLabels.push_back("B");
    columnLabels.push_back("C");
    columnLabels.push_back("D");
    columnLabels.push_back("E");

    //Setup row Labels
    rowLabels.push_back("1");
    rowLabels.push_back("2");
    rowLabels.push_back("3");
    rowLabels.push_back("4");
    rowLabels.push_back("5");

    //Setup Provider
    provider.setName(name);
    provider.setId(id);
    provider.setDescription(description);
    provider.setColumnLabels(columnLabels);
    provider.setRowLabels(rowLabels);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL((provider.getWriteableChildren().size() == 0), true);

    //Check writing attributes
    std::shared_ptr<H5::Group> h5Group1 (new H5::Group(h5File.get()->createGroup("/Group1")));

    //Pass the group and file to the writer for attributes
    //See that it passes
    BOOST_REQUIRE_EQUAL(provider.writeAttributes(h5File, h5Group1), true);

    //Bad pointer checks
    std::shared_ptr<H5::Group> badGroup;
    std::shared_ptr<H5::H5File> badFile;

    //Check dataSet.  Pass null to show it return false
    BOOST_REQUIRE_EQUAL(provider.writeDatasets(badFile, badGroup), false);
    BOOST_REQUIRE_EQUAL(provider.writeDatasets(badFile, h5Group1), false);
    BOOST_REQUIRE_EQUAL(provider.writeDatasets(h5File, badGroup), false);

    //Check dataset
    BOOST_REQUIRE_EQUAL(provider.writeDatasets(h5File, h5Group1), true);

    //Close group and then reopen
    try {
        h5File.get()->close();
        //free resources
        h5Group1.get()->close();

        //Open file
        h5File =  HdfFileFactory::openH5File(dataFile);

    } catch (...) {
        BOOST_FAIL("FAILED IN checkHdfWriteables!  Exiting");
    }

    //Get the group again
    std::shared_ptr<H5::Group> h5Group (new H5::Group(h5File.get()->openGroup("/Group1")));

    //Check attributes

    try {
        //Show that there is one group made at this time
        BOOST_REQUIRE_EQUAL(2, h5Group.get()->getNumObjs());


        //Check the meta data
        BOOST_REQUIRE_EQUAL(5, h5Group.get()->getNumAttrs());

        //Values to check

        //Get attributes by reader factory and check values
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "id"), id);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "name"), name);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "description"), description);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "HDF5LWRTag"), UtilityOperations::toStringHDF5Tag(tag));
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "size"), size);

        //Check the Group names and contents
        BOOST_REQUIRE_EQUAL("Labels", h5Group.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", h5Group.get()->getObjnameByIdx(1));

    } catch (...) {
        BOOST_FAIL("GRIDLABELPROVIDER FAILURE IN CHECKHDF5WRITEABLES");
    }

    //Make sure the writeAttributes fail for invalid stuff
    BOOST_REQUIRE_EQUAL(provider.writeAttributes(badFile, h5Group), false);
    BOOST_REQUIRE_EQUAL(provider.writeAttributes(h5File, badGroup), false);

    //Check dataSet.  Pass null to show it will fail.
    BOOST_REQUIRE_EQUAL(provider.writeDatasets(badFile, badGroup), false);
    BOOST_REQUIRE_EQUAL(provider.writeDatasets(badFile, h5Group), false);
    BOOST_REQUIRE_EQUAL(provider.writeDatasets(h5File, badGroup), false);

    //Get the Labels Group
    std::shared_ptr<H5::Group> labelsGroup  = HdfReaderFactory::getChildH5Group(h5Group, "Labels");

    //Check the values of that dataset
    BOOST_REQUIRE_EQUAL(labelsGroup.get()->getNumObjs(), 2);
    BOOST_REQUIRE_EQUAL("Column Labels", labelsGroup.get()->getObjnameByIdx(0));
    BOOST_REQUIRE_EQUAL("Row Labels", labelsGroup.get()->getObjnameByIdx(1));

    //Check DataSet information
    try {
        //Get the dataset
        hid_t dset1 =  H5Dopen(labelsGroup.get()->getId(), labelsGroup.get()->getObjnameByIdx(0).c_str(), H5P_DEFAULT);
        hid_t dset2 =  H5Dopen(labelsGroup.get()->getId(), labelsGroup.get()->getObjnameByIdx(1).c_str(), H5P_DEFAULT);

        //Get the space
        hid_t space1 = H5Dget_space(dset1);
        hid_t space2 = H5Dget_space(dset2);
        //Get the Dimensions
        hsize_t ndims1[1];
        hsize_t ndims2[1];
        H5Sget_simple_extent_dims(space1, ndims1, NULL);
        H5Sget_simple_extent_dims(space2, ndims2, NULL);


        //Get data - prepare on heap
        int rowSize1 = H5Tget_size(H5Dget_type(dset2));
        int colSize1 = H5Tget_size(H5Dget_type(dset1));
        std::cout<< "Col size: " << colSize1 << std::endl;
        char rData1[size][colSize1];
        char rData2[size][rowSize1];

        //Retrieve data over the features
        H5Dread(dset1, H5Dget_type(dset1), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData1);
        H5Dread(dset2, H5Dget_type(dset2), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData2);

        //Check data
        BOOST_REQUIRE_EQUAL(ndims1[0], ndims2[0]);
        for(int i = 0; i < ndims1[0]; i++) {

        	//Convert to char * array
        	char colArray[colSize1+1];
        	char rowArray[rowSize1+1];
        	memcpy(colArray, rData1[i], colSize1);
        	memcpy(rowArray, rData2[i], rowSize1);

        	//add nullary
        	colArray[colSize1] = 0;
        	rowArray[rowSize1] = 0;

            //Convert values
            /*std::string colLabel (rData1[i]);
            std::string rowLabel (rData2[i]);*/

            std::string colLabel (colArray);
            std::string rowLabel (rowArray);

            //check values
            BOOST_REQUIRE_EQUAL(columnLabels[i].c_str(), colLabel);
            BOOST_REQUIRE_EQUAL(rowLabels[i].c_str(), rowLabel);

        }

        //Close resources
        H5Dclose(dset1);
        H5Sclose(space1);
        H5Dclose(dset2);
        H5Sclose(space2);
    } catch (...) {
        BOOST_FAIL("FAILURE IN READING DATASET FOR CHECK WRITING.  EXITING");
    }

    //Check Group Creation
    std::shared_ptr<H5::Group> h5Group3 = provider.createGroup(h5File, h5Group);
    //See that the previous group has a group
    BOOST_REQUIRE_EQUAL(3, h5Group.get()->getNumObjs());
    //Check that it has the same name as the root provider
    BOOST_REQUIRE_EQUAL(provider.getName(), h5Group.get()->getObjnameByIdx(0));
    //Check that the returned group is a Group but no members
    BOOST_REQUIRE_EQUAL(0, h5Group3.get()->getNumObjs());

    //Delete file
    remove(dataFile.c_str());

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Readables) {

    // begin-user-code

    //Local Declarations
    GridLabelProvider newComponent(-1);
    int size = 5;
    GridLabelProvider provider(size);
    std::string name = "GRID!";
    std::string description = "LABELS!";
    int id = 4;
    HDF5LWRTagType tag = provider.getHDF5LWRTag();
    std::vector<std::string> columnLabels;
    std::vector<std::string> rowLabels;
    std::shared_ptr <GridLabelProvider> nulledComponent (new GridLabelProvider(-1));

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Setup column Labels
    columnLabels.push_back("Aasdasdasdasdasdasd");
    columnLabels.push_back("B");
    columnLabels.push_back("C");
    columnLabels.push_back("D");
    columnLabels.push_back("E");

    //Setup row Labels
    rowLabels.push_back("1");
    rowLabels.push_back("2");
    rowLabels.push_back("3");
    rowLabels.push_back("4");
    rowLabels.push_back("5");

    //Setup Provider
    provider.setName(name);
    provider.setId(id);
    provider.setDescription(description);
    provider.setColumnLabels(columnLabels);
    provider.setRowLabels(rowLabels);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);
    //h5File.get()->close();

    //Check writing attributes
    std::shared_ptr<H5::Group> h5Group1 (new H5::Group(h5File.get()->createGroup("/Group1")));

    try {

        //Write everything from component
        BOOST_REQUIRE_EQUAL(provider.writeAttributes(h5File, h5Group1), true);
        BOOST_REQUIRE_EQUAL(provider.writeDatasets(h5File, h5Group1), true);

        //Persist data
        h5File.get()->close();
        h5Group1.get()->close();

        //Reopen file
        h5File =  HdfFileFactory::openH5File(dataFile);

        //Call creator
        std::shared_ptr<H5::Group> h5Group2 (new H5::Group (h5File.get()->openGroup("/Group1")));

        //Read information
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(h5Group2), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(nulledComponent), true);
        BOOST_REQUIRE_EQUAL(newComponent.readDatasets(h5Group2), true);

        //Check with setup component
        BOOST_REQUIRE_EQUAL(provider==newComponent, true);

        //Now, lets try to set an erroneous H5Group with missing data
        h5Group2.get()->removeAttr("name");

        //Save, close, reopen
        //Persist data
        h5File.get()->close();
        h5Group1.get()->close();

        //Reopen file
        h5File =  HdfFileFactory::openH5File(dataFile);

        //Call creator
        std::shared_ptr<H5::Group> h5Group3 (new H5::Group (h5File.get()->openGroup("/Group1")));

        //Run it through
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(h5Group3), false);
        //Check it does not change
        BOOST_REQUIRE_EQUAL(provider==newComponent, true);

        //Check for nullaries
        std::shared_ptr<H5::Group> badGroup;
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(badGroup), false);

        //Doesn't change anything
        BOOST_REQUIRE_EQUAL(provider==newComponent, true);

        //Close file and group
        h5File.get()->close();
        h5Group3.get()->close();


    } catch (...) {
        BOOST_FAIL("FAILURE IN READABLE TEST.");
    }

    //Delete file
    remove(dataFile.c_str());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(afterClass) {

    //relative!
    std::string testFolder = "ICEIOTestsDir/";
    std::string testFile = testFolder + "test.h5";

    //Delete file
    remove(testFile.c_str());

    //Delete folder
    remove(testFolder.c_str());

}
BOOST_AUTO_TEST_SUITE_END()
