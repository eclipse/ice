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
#define BOOST_TEST_MODULE LWRGridManagerTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <LWRGridManager.h>
#include <stdio.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <HdfFileFactory.h>
#include <UtilityOperations.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(LWRGridManagerTester_testSuite)

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

    //Local Declarations
    int defaultSize = 1;
    std::string defaultName = "LWRGridManager 1";
    std::string defaultDescription = "LWRGridManager 1's Description";
    int defaultId = 1;
    HDF5LWRTagType type = LWRGRIDMANAGER;

    //New
    int newSize = 5;

    //Check normal construction
    LWRGridManager manager(newSize);
    //Check values
    BOOST_REQUIRE_EQUAL(newSize, manager.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, manager.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, manager.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, manager.getId());
    BOOST_REQUIRE_EQUAL(type, manager.getHDF5LWRTag());

    //Check defaultSize construction
    LWRGridManager manager2(defaultSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultSize, manager2.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, manager2.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, manager2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, manager2.getId());
    BOOST_REQUIRE_EQUAL(type, manager2.getHDF5LWRTag());

    //Check illegal size - zero
    LWRGridManager manager3(0);
    //Check value - defaults
    BOOST_REQUIRE_EQUAL(defaultSize, manager3.getSize()); // Defaults
    BOOST_REQUIRE_EQUAL(defaultName, manager3.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, manager3.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, manager3.getId());
    BOOST_REQUIRE_EQUAL(type, manager3.getHDF5LWRTag());

    //Check illegal size - negative
    LWRGridManager manager4(-1);
    //Check values - defaults
    BOOST_REQUIRE_EQUAL(defaultSize, manager4.getSize()); // Defaults
    BOOST_REQUIRE_EQUAL(defaultName, manager4.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, manager4.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, manager4.getId());
    BOOST_REQUIRE_EQUAL(type, manager4.getHDF5LWRTag());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkComponent) {

    // begin-user-code
    //Local Declarations
    int size = 10;

    //Setup GridLocations values
    int row1 = 1, row2 = 5, row3 = 9, row4 = 22;
    int col1 = 1, col2 = 5, col3 = 8, col4 = 15;

    //Use LWRComponent
    std::string component1Name = "Component 1";
    std::string component2Name = "Component 2";
    std::string component3Name = "Component 3";
    std::string component4Name = "Component 4";

    //Setup the LWRComponents
    std::shared_ptr<LWRComponent> component1 (new LWRComponent(component1Name));
    std::shared_ptr<LWRComponent> component2 (new LWRComponent(component2Name));
    std::shared_ptr<LWRComponent> component3 (new LWRComponent(component3Name));
    std::shared_ptr<LWRComponent> component4 (new LWRComponent(component4Name));


    //Set ids
    component1.get()->setId(1);
    component2.get()->setId(2);
    component3.get()->setId(3);
    component4.get()->setId(4);

    //Null args
    std::shared_ptr<LWRComponent> nullComponent;
    std::shared_ptr<GridLocation> nullLocation;

    //Create a manager
    LWRGridManager manager(size);

    //Create a gridLocation and add it to the list for Component1
    std::shared_ptr<GridLocation> location1( new GridLocation(row1, col1));

    //Add it, and check to see if it exists
    manager.addComponent(component1, location1);
    //See if it exists and is equal
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);

    //add 2 more
    std::shared_ptr<GridLocation> location2(new GridLocation(row2, col2));
    manager.addComponent(component2, location2);

    std::shared_ptr<GridLocation> location3( new GridLocation(row3, col3));
    manager.addComponent(component3, location3);

    //See if they both exist
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(component2.get()->getName().compare(manager.getComponentName(location2)) == 0, true);
    BOOST_REQUIRE_EQUAL(component3.get()->getName().compare(manager.getComponentName(location3)) == 0, true);

    //Try to add something on top of something that already exists
    manager.addComponent(component4, location2);
    //Show that it does not exist, and no other items are messed with
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(component2.get()->getName().compare(manager.getComponentName(location2)) == 0, true);
    BOOST_REQUIRE_EQUAL(component3.get()->getName().compare(manager.getComponentName(location3)) == 0, true);

    //Try to add with a location out of the size range
    std::shared_ptr<GridLocation> location4 (new GridLocation(row4, col4));
    manager.addComponent(component4, location4);
    //Show that it does not exist, and no other items are messed with
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location4).empty(), true);
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(component2.get()->getName().compare(manager.getComponentName(location2)) == 0, true);
    BOOST_REQUIRE_EQUAL(component3.get()->getName().compare(manager.getComponentName(location3)) == 0, true);

    //Try to add with a location of null
    manager.addComponent(component4, nullLocation);
    //Show that it does not exist, and no other items are messed with
    BOOST_REQUIRE_EQUAL(manager.getComponentName(nullLocation).empty(), true);
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(component2.get()->getName().compare(manager.getComponentName(location2)) == 0, true);
    BOOST_REQUIRE_EQUAL(component3.get()->getName().compare(manager.getComponentName(location3)) == 0, true);

    //Try to add with a component of null
    manager.addComponent(nullComponent, nullLocation);
    //Show that it does not exist, and no other items are messed with
    BOOST_REQUIRE_EQUAL(manager.getComponentName(nullLocation).empty(), true);
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(component2.get()->getName().compare(manager.getComponentName(location2)) == 0, true);
    BOOST_REQUIRE_EQUAL(component3.get()->getName().compare(manager.getComponentName(location3)) == 0, true);

    //Try to remove based on location - remove component 3
    manager.removeComponent(location3);
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(component2.get()->getName().compare(manager.getComponentName(location2)) == 0, true);
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location3).empty(), true); // Does not exist anymore!

    //Try to remove based on location - bad location (location does not exist)
    manager.removeComponent(location3);
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(component2.get()->getName().compare(manager.getComponentName(location2)) == 0, true);
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location3).empty(), true); // Does not exist anymore!

    //Try to removed based on location - out of range
    manager.removeComponent(location4);
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(component2.get()->getName().compare(manager.getComponentName(location2)) == 0, true);
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location3).empty(), true); // Does not exist anymore!

    //Try to remove  - null Location
    manager.removeComponent(nullLocation);
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(component2.get()->getName().compare(manager.getComponentName(location2)) == 0, true);
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location3).empty(), true); // Does not exist anymore!

    //Try to remove  - null Component
    manager.removeComponent(std::dynamic_pointer_cast<ICE_DS::Component>(nullComponent));
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(component2.get()->getName().compare(manager.getComponentName(location2)) == 0, true);
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location3).empty(), true); // Does not exist anymore!

    //Remove based on Component
    manager.removeComponent(std::dynamic_pointer_cast<ICE_DS::Component>(component2));
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location2).empty(), true); // Does not exist anymore!
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location3).empty(), true); // Does not exist anymore!

    //Remove a component that does not exist
    manager.removeComponent(std::dynamic_pointer_cast<ICE_DS::Component>(component2));
    BOOST_REQUIRE_EQUAL(component1.get()->getName().compare(manager.getComponentName(location1)) == 0, true);
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location2).empty(), true); // Does not exist anymore!
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location3).empty(), true); // Does not exist anymore!

    //Remove the last component
    manager.removeComponent(std::dynamic_pointer_cast<ICE_DS::Component>(component1));
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location1).empty(), true); // Does not exist anymore!
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location2).empty(), true); // Does not exist anymore!
    BOOST_REQUIRE_EQUAL(manager.getComponentName(location3).empty(), true); // Does not exist anymore!

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    std::string name = "Billy";

    //Setup Components
    std::shared_ptr<Component> component1 (new LWRComponent("Billy Bob"));
    std::shared_ptr<Component> component2 (new LWRComponent("Bobby Bill"));

    //Setup Locations
    std::shared_ptr<GridLocation> location1 (new GridLocation(1, 6));
    std::shared_ptr<GridLocation> location2 (new GridLocation(3, 4));

    //Setup root object
    LWRGridManager object(15);
    object.setName(name);
    object.addComponent(component1, location1);
    object.addComponent(component2, location2);

    //Setup equalObject equal to object
    LWRGridManager equalObject(15);
    equalObject.setName(name);
    equalObject.addComponent(component1, location1);
    equalObject.addComponent(component2, location2);

    //Setup transitiveObject equal to object
    LWRGridManager transitiveObject(15);
    transitiveObject.setName(name);
    transitiveObject.addComponent(component1, location1);
    transitiveObject.addComponent(component2, location2);

    // Set its data, not equal to object
    LWRGridManager unEqualObject(15);
    unEqualObject.setName(name);
    unEqualObject.addComponent(component2, location1);

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
        BOOST_FAIL("FAILURE IN EQUALITY CHECKING!  EXITING!");
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
    std::string name = "Billy";

    //Setup Components
    std::shared_ptr<Component> component1 (new LWRComponent("Billy Bob"));
    std::shared_ptr<Component> component2 (new LWRComponent("Bobby Bill"));

    //Setup Locations
    std::shared_ptr<GridLocation> location1 (new GridLocation(1, 6));
    std::shared_ptr<GridLocation> location2 (new GridLocation(3, 4));

    //Setup root object
    LWRGridManager object(15);
    object.setName(name);
    object.addComponent(component1, location1);
    object.addComponent(component2, location2);

    //Run the copy routine
    LWRGridManager copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<LWRGridManager> castedObject (new LWRGridManager(*(dynamic_cast<LWRGridManager *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code

}

BOOST_AUTO_TEST_CASE(checkDataProvider) {

	// begin-user-code

	//Local Declarations
	int size = 5;
	LWRGridManager manager(size);

	std::string name1 = "Foo1";
	std::string name2 = "Foo2";
	std::shared_ptr<LWRComponent> component1 ( new LWRComponent(name1));
	std::shared_ptr<LWRComponent> component2 ( new LWRComponent(name2));
	std::shared_ptr<GridLocation> location1 ( new GridLocation(2,3));
	std::shared_ptr<GridLocation> location2 ( new GridLocation(3,3));
	std::shared_ptr<GridLocation> location3 ( new GridLocation(1,3));

	//Try to get locations when there are no GridLocations or names
	BOOST_REQUIRE_EQUAL(0, manager.getGridLocationsAtName(name1).size());
	BOOST_REQUIRE_EQUAL(manager.getDataProviderAtLocation(location1).get() == NULL, true);

	//Add to grid
	manager.addComponent(component1, location1);

	//Try to get locations
	BOOST_REQUIRE_EQUAL(1, manager.getGridLocationsAtName(name1).size());
	BOOST_REQUIRE_EQUAL(manager.getGridLocationsAtName(name1).at(0).get()->operator==(*location1.get()), true);
	BOOST_REQUIRE_EQUAL(manager.getDataProviderAtLocation(location1).get() != NULL, true);

	//Add to another location
	manager.addComponent(component1, location2);

	//Try to get locations
	BOOST_REQUIRE_EQUAL(2, manager.getGridLocationsAtName(name1).size());
	BOOST_REQUIRE_EQUAL(manager.getGridLocationsAtName(name1).at(0).get()->operator==(*location1.get()), true);
	BOOST_REQUIRE_EQUAL(manager.getGridLocationsAtName(name1).at(1).get()->operator==(*location2.get()), true);
	BOOST_REQUIRE_EQUAL(manager.getDataProviderAtLocation(location1).get() != NULL, true);
	BOOST_REQUIRE_EQUAL(manager.getDataProviderAtLocation(location2).get() != NULL, true);

	//Add to another location - different component
	manager.addComponent(component2, location3);

	//Try to get locations
	BOOST_REQUIRE_EQUAL(2, manager.getGridLocationsAtName(name1).size());
	BOOST_REQUIRE_EQUAL(manager.getGridLocationsAtName(name1).at(0).get()->operator==(*location1.get()), true);
	BOOST_REQUIRE_EQUAL(manager.getGridLocationsAtName(name1).at(1).get()->operator==(*location2.get()), true);


	//Check second component
	BOOST_REQUIRE_EQUAL(1, manager.getGridLocationsAtName(name2).size());
	BOOST_REQUIRE_EQUAL(manager.getGridLocationsAtName(name2).at(0).get()->operator==(*location3.get()), true);

	//Check all added locations
	BOOST_REQUIRE_EQUAL(manager.getDataProviderAtLocation(location1).get() != NULL, true);
	BOOST_REQUIRE_EQUAL(manager.getDataProviderAtLocation(location2).get() != NULL, true);
	BOOST_REQUIRE_EQUAL(manager.getDataProviderAtLocation(location3).get() != NULL, true);

	// end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    LWRGridManager component(size);
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;

    //Setup Components
    std::shared_ptr<Component> component1 (new LWRComponent("Billy Bob"));
    std::shared_ptr<Component> component2 (new LWRComponent("Bobby Bill"));

    //Setup Locations
    std::shared_ptr<GridLocation> location1 (new GridLocation(1, 3));
    std::shared_ptr<GridLocation> location2 (new GridLocation(2, 4));
    std::shared_ptr<GridLocation> location3 (new GridLocation(3, 4));

    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Exceptions
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);
    component.addComponent(component1, location1);
    component.addComponent(component2, location2);
    component.addComponent(component2, location3);

    //Setup LWRData
    std::string feature1 = "Feature 1";
    std::string feature2 = "Feature 2";
    double time1= 1.0, time2 = 3.0, time3 = 3.5;
    std::vector<double> position1;
    std::vector<double> position2;
    std::vector<double> position3;
    std::vector<double> position4;
    std::vector<double> position5;

    //Setup Positions

    //Setup Position 1
    position1.push_back(0.0);
    position1.push_back(1.0);
    position1.push_back(0.0);

    //Setup Position 2
    position2.push_back(0.0);
    position2.push_back(1.0);
    position2.push_back(4.0);

    //Setup Position 3
    position3.push_back(1.0);
    position3.push_back(1.0);
    position3.push_back(0.0);

    //Setup Position 4
    position4.push_back(0.0);
    position4.push_back(1.0);
    position4.push_back(1.0);

    //Setup Position 5
    position5.push_back(0.0);
    position5.push_back(1.0);
    position5.push_back(3.0);



    //Setup data1
    std::shared_ptr<LWRData> data1( new LWRData(feature1));
    data1.get()->setPosition(position1);
    data1.get()->setValue(1.0);
    data1.get()->setUncertainty(1.5);
    data1.get()->setUnits("Units 12345");

    //Setup data2
    std::shared_ptr<LWRData> data2(new LWRData(feature1));
    data2.get()->setPosition(position2);
    data2.get()->setValue(2.0);
    data2.get()->setUncertainty(2.5);
    data2.get()->setUnits("Units 2");

    //Setup data3
    std::shared_ptr<LWRData> data3(new LWRData(feature1) );
    data3.get()->setPosition(position3);
    data3.get()->setValue(3.0);
    data3.get()->setUncertainty(3.5);
    data3.get()->setUnits("Units 3");

    //Setup data4
    std::shared_ptr<LWRData> data4( new LWRData(feature1) );
    data4.get()->setPosition(position4);
    data4.get()->setValue(4.0);
    data4.get()->setUncertainty(4.5);
    data4.get()->setUnits("Units 4");

    //Setup data5
    std::shared_ptr<LWRData> data5( new LWRData(feature2) );
    data5.get()->setPosition(position5);
    data5.get()->setValue(5.0);
    data5.get()->setUncertainty(5.5);
    data5.get()->setUnits("Units 5");

    //Add data to the component
    location1.get()->getLWRDataProvider().get()->addData(data1, time1);
    location1.get()->getLWRDataProvider().get()->addData(data2, time1);
    location1.get()->getLWRDataProvider().get()->addData(data3, time2);
    location1.get()->getLWRDataProvider().get()->addData(data4, time3);
    location1.get()->getLWRDataProvider().get()->addData(data5, time3);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL((component.getWriteableChildren().size() == 0), true);

    //Check writing attributes
    std::shared_ptr<H5::Group> h5Group1 (new H5::Group(h5File.get()->createGroup("/Group1")));

    //Pass the group and file to the writer for attributes
    //See that it passes
    BOOST_REQUIRE_EQUAL(component.writeAttributes(h5File, h5Group1), true);

    //Bad pointer checks
    std::shared_ptr<H5::Group> badGroup;
    std::shared_ptr<H5::H5File> badFile;

    //Check dataSet.  Pass null to show it return false
    BOOST_REQUIRE_EQUAL(component.writeDatasets(badFile, badGroup), false);
    BOOST_REQUIRE_EQUAL(component.writeDatasets(badFile, h5Group1), false);
    BOOST_REQUIRE_EQUAL(component.writeDatasets(h5File, badGroup), false);

    //Check dataset
    BOOST_REQUIRE_EQUAL(component.writeDatasets(h5File, h5Group1), true);

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

        //Check the dataset
        std::string nameOfGroup = "Positions"; //Get the name

        //Get the dataset
        std::shared_ptr<H5::Group> positionsH5Group (new H5::Group(h5Group.get()->openGroup(nameOfGroup)));

        //Check the size of the positions groups - will contain 4
        BOOST_REQUIRE_EQUAL(5, positionsH5Group.get()->getNumObjs());

        //Get the first position and check data
        std::shared_ptr<H5::Group> dataGroup(new H5::Group(positionsH5Group.get()->openGroup("Position 1 3")));
        std::shared_ptr<H5::Group> dataGroup2(new H5::Group(positionsH5Group.get()->openGroup("Position 2 4")));
        std::shared_ptr<H5::Group> dataGroup3(new H5::Group(positionsH5Group.get()->openGroup("Position 3 4")));
        std::shared_ptr<H5::DataSet> unitsDataSet(new H5::DataSet(positionsH5Group.get()->openDataSet("Units Table")));
        std::shared_ptr<H5::DataSet> namesDataSet(new H5::DataSet(positionsH5Group.get()->openDataSet("Simple Position Names Table")));

        //Check the units and names group
        //Get the space
        hid_t spaceUnits = H5Dget_space(unitsDataSet.get()->getId());
        hid_t spaceNames = H5Dget_space(namesDataSet.get()->getId());


        //Get the Dimensions
        hsize_t ndims[1];
        hsize_t namesdims[1];

        H5Sget_simple_extent_dims(spaceUnits, ndims, NULL);
        H5Sget_simple_extent_dims(spaceNames, namesdims, NULL);


        //Get data - prepare on heap
        int rowSize1 = H5Tget_size(H5Dget_type(unitsDataSet.get()->getId()));
        int rowSize2 = H5Tget_size(H5Dget_type(namesDataSet.get()->getId()));

        char rData[ndims[0]][rowSize1];
        char rDataNames[namesdims[0]][rowSize2];

        //Retrieve data over the features
        H5Dread(namesDataSet.get()->getId(), H5Dget_type(namesDataSet.get()->getId()), H5S_ALL, H5S_ALL, H5P_DEFAULT, rDataNames);
        H5Dread(unitsDataSet.get()->getId(), H5Dget_type(unitsDataSet.get()->getId()), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData);

        //Check units data and names, add 1 for null terminator
        char char1[rowSize1+1], char2[rowSize1+1], char3[rowSize1+1], char4[rowSize1+1], char5[rowSize1+1], char6[rowSize2+1], char7[rowSize2+1];
        strncpy(char1, rData[0], rowSize1);
        strncpy(char2, rData[1], rowSize1);
        strncpy(char3, rData[2], rowSize1);
        strncpy(char4, rData[3], rowSize1);
        strncpy(char5, rData[4], rowSize1);
        strncpy(char6, rDataNames[0], rowSize2);
        strncpy(char7, rDataNames[1], rowSize2);


        //Add null terminator
        char1[rowSize1] = '\0';
        char2[rowSize1] = '\0';
        char3[rowSize1] = '\0';
        char4[rowSize1] = '\0';
        char5[rowSize1] = '\0';
        char6[rowSize2] = '\0';
        char7[rowSize2] = '\0';

        //convert to string
        std::string str1(char1);
        std::string str2(char2);
        std::string str3(char3);
        std::string str4(char4);
        std::string str5(char5);
        std::string str6(char6);
        std::string str7(char7);

        BOOST_REQUIRE_EQUAL(data1.get()->getUnits(), str1);
        BOOST_REQUIRE_EQUAL(data2.get()->getUnits(), str2);
        BOOST_REQUIRE_EQUAL(data3.get()->getUnits(), str3);
        BOOST_REQUIRE_EQUAL(data4.get()->getUnits(), str4);
        BOOST_REQUIRE_EQUAL(data5.get()->getUnits(), str5);


        //Check Position data - ordering is weird here due to map

        int positionName1 = -1;
        int positionName2 = -1;
        bool nameRequired1 = false;
        bool nameRequired2 = false;

        std::vector<std::string> list;
        list.push_back(str6);
        list.push_back(str7);

        for(int listIter = 0; listIter < 2; listIter++) {
        	if(component1.get()->getName().compare(list.at(listIter)) == 0) {
        		nameRequired1 = true;
        		positionName1= listIter;
        	}

        	if(component2.get()->getName().compare(list.at(listIter)) == 0) {
        	    nameRequired2 = true;
        	    positionName2 = listIter;
        	 }
        }
        BOOST_REQUIRE_EQUAL(nameRequired1, true);
        BOOST_REQUIRE_EQUAL(nameRequired2, true);

        //Check the size of the data groups
        BOOST_REQUIRE_EQUAL(4, dataGroup.get()->getNumObjs()); // 3 Time groups and 1 Position Dataset
        BOOST_REQUIRE_EQUAL(1, dataGroup2.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(1, dataGroup3.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(0, dataGroup.get()->getNumAttrs());
        BOOST_REQUIRE_EQUAL(0, dataGroup2.get()->getNumAttrs());
        BOOST_REQUIRE_EQUAL(0, dataGroup3.get()->getNumAttrs());

        //Check Position Dataset on dataGroup1
        std::shared_ptr<H5::DataSet> positionDataSet1(new H5::DataSet(dataGroup.get()->openDataSet("Position Dataset")));
        hid_t spacePosition1 = H5Dget_space(positionDataSet1.get()->getId());

        //Get the Dimensions
        hsize_t ndimsPosition1[1];

        H5Sget_simple_extent_dims(spacePosition1, ndims, NULL);

        int rDataPosition1[3];

        //Retrieve data over the features
        H5Dread(positionDataSet1.get()->getId(), H5Dget_type(positionDataSet1.get()->getId()), H5S_ALL, H5S_ALL, H5P_DEFAULT, rDataPosition1);

        //Check data
        BOOST_REQUIRE_EQUAL(location1.get()->getRow(), rDataPosition1[0]);
        BOOST_REQUIRE_EQUAL(location1.get()->getColumn(), rDataPosition1[1]);
        BOOST_REQUIRE_EQUAL(positionName1, rDataPosition1[2]); // Reflects the name of the position on the position list

        //Check Position Dataset on dataGroup2
        std::shared_ptr<H5::DataSet> positionDataSet2(new H5::DataSet(dataGroup2.get()->openDataSet("Position Dataset")));
        hid_t spacePosition2 = H5Dget_space(positionDataSet2.get()->getId());

        //Get the Dimensions
        hsize_t ndimsPosition2[1];

        H5Sget_simple_extent_dims(spacePosition2, ndims, NULL);

        int rDataPosition2[3];

        //Retrieve data over the features
        H5Dread(positionDataSet2.get()->getId(), H5Dget_type(positionDataSet2.get()->getId()), H5S_ALL, H5S_ALL, H5P_DEFAULT, rDataPosition2);

        //Check data
        BOOST_REQUIRE_EQUAL(location2.get()->getRow(), rDataPosition2[0]);
        BOOST_REQUIRE_EQUAL(location2.get()->getColumn(), rDataPosition2[1]);
        BOOST_REQUIRE_EQUAL(positionName2, rDataPosition2[2]); // Reflects the name on the position names list

        //Check Position Dataset on dataGroup2
        std::shared_ptr<H5::DataSet> positionDataSet3(new H5::DataSet(dataGroup3.get()->openDataSet("Position Dataset")));
        hid_t spacePosition3 = H5Dget_space(positionDataSet3.get()->getId());

        //Get the Dimensions
        hsize_t ndimsPosition3[1];

        H5Sget_simple_extent_dims(spacePosition3, ndims, NULL);

        int rDataPosition3[3];

        //Retrieve data over the features
        H5Dread(positionDataSet3.get()->getId(), H5Dget_type(positionDataSet3.get()->getId()), H5S_ALL, H5S_ALL, H5P_DEFAULT, rDataPosition3);

        //Check data
        BOOST_REQUIRE_EQUAL(location3.get()->getRow(), rDataPosition3[0]);
        BOOST_REQUIRE_EQUAL(location3.get()->getColumn(), rDataPosition3[1]);
        BOOST_REQUIRE_EQUAL(positionName2, rDataPosition3[2]); // Reflects the name on the position names list

        //Check the names of the groups.  These should reflect the number of time steps
        BOOST_REQUIRE_EQUAL("Position Dataset", dataGroup.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("TimeStep: 0", dataGroup.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("TimeStep: 1", dataGroup.get()->getObjnameByIdx(2));
        BOOST_REQUIRE_EQUAL("TimeStep: 2", dataGroup.get()->getObjnameByIdx(3));

        //Get the TimeStep Groups and check contents
        std::shared_ptr<H5::Group> timeGroup1(new H5::Group(dataGroup.get()->openGroup(dataGroup.get()->getObjnameByIdx(1))));
        std::shared_ptr<H5::Group> timeGroup2(new H5::Group(dataGroup.get()->openGroup(dataGroup.get()->getObjnameByIdx(2))));
        std::shared_ptr<H5::Group> timeGroup3(new H5::Group(dataGroup.get()->openGroup(dataGroup.get()->getObjnameByIdx(3))));


        //Check that there is a group and attributes.
        //The breakdown is as follows:  There are 2 Datasets for every feature.  This is why timeGroup3 has 4 nums of objects
        BOOST_REQUIRE_EQUAL(2, timeGroup1.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(2, timeGroup2.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(4, timeGroup3.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(1, timeGroup1.get()->getNumAttrs());
        BOOST_REQUIRE_EQUAL(1, timeGroup2.get()->getNumAttrs());
        BOOST_REQUIRE_EQUAL(1, timeGroup3.get()->getNumAttrs());

        //Check Attributes on timeGroups

        //Check timeGroup1 attributes
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(timeGroup1, "time"), time1);

        //Check timeGroup2 attributes
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(timeGroup2, "time"), time2);

        //Check timeGroup3 attributes
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(timeGroup3, "time"), time3);

        //Open the datasets on the timeGroups

        hid_t dset1Data, dset1Head, dset2Data, dset2Head, dset3aData, dset3aHead, dset3bData, dset3bHead;

        //Get the datasets
        dset1Data = H5Dopen(timeGroup1.get()->getId(), timeGroup1.get()->getObjnameByIdx(0).c_str(), H5P_DEFAULT);
        dset1Head = H5Dopen(timeGroup1.get()->getId(), timeGroup1.get()->getObjnameByIdx(1).c_str(), H5P_DEFAULT);
        dset2Data = H5Dopen(timeGroup2.get()->getId(), timeGroup2.get()->getObjnameByIdx(0).c_str(), H5P_DEFAULT);
        dset2Head = H5Dopen(timeGroup2.get()->getId(), timeGroup2.get()->getObjnameByIdx(1).c_str(), H5P_DEFAULT);
        dset3aData = H5Dopen(timeGroup3.get()->getId(), timeGroup3.get()->getObjnameByIdx(0).c_str(), H5P_DEFAULT);
        dset3aHead = H5Dopen(timeGroup3.get()->getId(), timeGroup3.get()->getObjnameByIdx(1).c_str(), H5P_DEFAULT);
        dset3bData = H5Dopen(timeGroup3.get()->getId(), timeGroup3.get()->getObjnameByIdx(2).c_str(), H5P_DEFAULT);
        dset3bHead = H5Dopen(timeGroup3.get()->getId(), timeGroup3.get()->getObjnameByIdx(3).c_str(), H5P_DEFAULT);

        //Check the names
        BOOST_REQUIRE_EQUAL("Feature 1 dataTable", timeGroup1.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Feature 1 headTable", timeGroup1.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("Feature 1 dataTable", timeGroup2.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Feature 1 headTable", timeGroup2.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("Feature 1 dataTable", timeGroup3.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Feature 1 headTable", timeGroup3.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("Feature 2 dataTable", timeGroup3.get()->getObjnameByIdx(2));
        BOOST_REQUIRE_EQUAL("Feature 2 headTable", timeGroup3.get()->getObjnameByIdx(3));

        //Read the data off each dset

        //Get the space
        hid_t space1Data = H5Dget_space(dset1Data);
        hid_t space1Head = H5Dget_space(dset1Head);
        hid_t space2Data = H5Dget_space(dset2Data);
        hid_t space2Head = H5Dget_space(dset2Head);
        hid_t space3aData = H5Dget_space(dset3aData);
        hid_t space3aHead = H5Dget_space(dset3aHead);
        hid_t space3bData = H5Dget_space(dset3bData);
        hid_t space3bHead = H5Dget_space(dset3bHead);

        //Get the Dimensions
        hsize_t ndims1Data[2], ndims1Head[2], ndims2Data[2], ndims2Head[2], ndims3aData[2], ndims3aHead[2], ndims3bData[2], ndims3bHead[2];

        //Get the ndims
        int rank1Data = H5Sget_simple_extent_dims(space1Data, ndims1Data, NULL);
        int rank1Head = H5Sget_simple_extent_dims(space1Head, ndims1Head, NULL);
        int rank2Data = H5Sget_simple_extent_dims(space2Data, ndims2Data, NULL);
        int rank2Head = H5Sget_simple_extent_dims(space2Head, ndims2Head, NULL);
        int rank3aData = H5Sget_simple_extent_dims(space3aData, ndims3aData, NULL);
        int rank3aHead = H5Sget_simple_extent_dims(space3aHead, ndims3aHead, NULL);
        int rank3bData = H5Sget_simple_extent_dims(space3bData, ndims3bData, NULL);
        int rank3bHead = H5Sget_simple_extent_dims(space3bHead, ndims3bHead, NULL);

        //Make sure they are 2 ranks
        BOOST_REQUIRE_EQUAL(rank1Data, 2);
        BOOST_REQUIRE_EQUAL(rank1Head, 2);
        BOOST_REQUIRE_EQUAL(rank2Data, 2);
        BOOST_REQUIRE_EQUAL(rank2Head, 2);
        BOOST_REQUIRE_EQUAL(rank3aData, 2);
        BOOST_REQUIRE_EQUAL(rank3aHead, 2);
        BOOST_REQUIRE_EQUAL(rank3bData, 2);
        BOOST_REQUIRE_EQUAL(rank3bHead, 2);

        //Check dimension sizes
        BOOST_REQUIRE_EQUAL(ndims1Data[0], 2);
        BOOST_REQUIRE_EQUAL(ndims1Data[1], 5);
        BOOST_REQUIRE_EQUAL(ndims1Head[0], 2);
        BOOST_REQUIRE_EQUAL(ndims1Head[1], 2);
        BOOST_REQUIRE_EQUAL(ndims2Data[0], 1);
        BOOST_REQUIRE_EQUAL(ndims2Data[1], 5);
        BOOST_REQUIRE_EQUAL(ndims2Head[0], 1);
        BOOST_REQUIRE_EQUAL(ndims2Head[1], 2);
        BOOST_REQUIRE_EQUAL(ndims3aData[0], 1);
        BOOST_REQUIRE_EQUAL(ndims3aData[1], 5);
        BOOST_REQUIRE_EQUAL(ndims3aHead[0], 1);
        BOOST_REQUIRE_EQUAL(ndims3aHead[1], 2);
        BOOST_REQUIRE_EQUAL(ndims3bData[0], 1);
        BOOST_REQUIRE_EQUAL(ndims3bData[1], 5);
        BOOST_REQUIRE_EQUAL(ndims3bHead[0], 1);
        BOOST_REQUIRE_EQUAL(ndims3bHead[1], 2);

        //Get data - prepare on stack
        double rData1Data [ndims1Data[0]][ndims1Data[1]];
        long rData1Head [ndims1Head[0]][ndims1Head[1]];
        double rData2Data [ndims2Data[0]][ndims2Data[1]];
        long rData2Head [ndims2Head[0]][ndims2Head[1]];
        double rData3aData [ndims3aData[0]][ndims3aData[1]];
        long rData3aHead [ndims3aHead[0]][ndims3aHead[1]];
        double rData3bData [ndims3bData[0]][ndims3bData[1]];
        long rData3bHead [ndims3bHead[0]][ndims3bHead[1]];

        //Retrieve data
        H5Dread(dset1Data, H5Dget_type(dset1Data), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData1Data);
        H5Dread(dset1Head, H5Dget_type(dset1Head), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData1Head);
        H5Dread(dset2Data, H5Dget_type(dset2Data), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData2Data);
        H5Dread(dset2Head, H5Dget_type(dset2Head), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData2Head);
        H5Dread(dset3aData, H5Dget_type(dset3aData), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData3aData);
        H5Dread(dset3aHead, H5Dget_type(dset3aHead), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData3aHead);
        H5Dread(dset3bData, H5Dget_type(dset3bData), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData3bData);
        H5Dread(dset3bHead, H5Dget_type(dset3bHead), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData3bHead);

        //Check values for timeGroup1
        //Data1 and Data2
        BOOST_REQUIRE_EQUAL(rData1Data[0][0], data1.get()->getValue());
        BOOST_REQUIRE_EQUAL(rData1Data[0][1], data1.get()->getUncertainty());
        BOOST_REQUIRE_EQUAL(rData1Data[0][2], data1.get()->getPosition().at(0));
        BOOST_REQUIRE_EQUAL(rData1Data[0][3], data1.get()->getPosition().at(1));
        BOOST_REQUIRE_EQUAL(rData1Data[0][4], data1.get()->getPosition().at(2));

        BOOST_REQUIRE_EQUAL(rData1Data[1][0], data2.get()->getValue());
        BOOST_REQUIRE_EQUAL(rData1Data[1][1], data2.get()->getUncertainty());
        BOOST_REQUIRE_EQUAL(rData1Data[1][2], data2.get()->getPosition().at(0));
        BOOST_REQUIRE_EQUAL(rData1Data[1][3], data2.get()->getPosition().at(1));
        BOOST_REQUIRE_EQUAL(rData1Data[1][4], data2.get()->getPosition().at(2));

        //Head1 (Data1 and Data2)
        BOOST_REQUIRE_EQUAL(rData1Head[0][0], 0); //Position of data
        BOOST_REQUIRE_EQUAL(rData1Head[0][1], 0); //Position of units
        BOOST_REQUIRE_EQUAL(rData1Head[1][0], 1); //Position of data
        BOOST_REQUIRE_EQUAL(rData1Head[1][1], 1); //Position of units

        //Data3
        BOOST_REQUIRE_EQUAL(rData2Data[0][0], data3.get()->getValue());
        BOOST_REQUIRE_EQUAL(rData2Data[0][1], data3.get()->getUncertainty());
        BOOST_REQUIRE_EQUAL(rData2Data[0][2], data3.get()->getPosition().at(0));
        BOOST_REQUIRE_EQUAL(rData2Data[0][3], data3.get()->getPosition().at(1));
        BOOST_REQUIRE_EQUAL(rData2Data[0][4], data3.get()->getPosition().at(2));

        //Head2 (Data3)
        BOOST_REQUIRE_EQUAL(rData2Head[0][0], 0);
        BOOST_REQUIRE_EQUAL(rData2Head[0][1], 2);

        //Data4 and Data5
        BOOST_REQUIRE_EQUAL(rData3aData[0][0], data4.get()->getValue());
        BOOST_REQUIRE_EQUAL(rData3aData[0][1], data4.get()->getUncertainty());
        BOOST_REQUIRE_EQUAL(rData3aData[0][2], data4.get()->getPosition().at(0));
        BOOST_REQUIRE_EQUAL(rData3aData[0][3], data4.get()->getPosition().at(1));
        BOOST_REQUIRE_EQUAL(rData3aData[0][4], data4.get()->getPosition().at(2));

        BOOST_REQUIRE_EQUAL(rData3bData[0][0], data5.get()->getValue());
        BOOST_REQUIRE_EQUAL(rData3bData[0][1], data5.get()->getUncertainty());
        BOOST_REQUIRE_EQUAL(rData3bData[0][2], data5.get()->getPosition().at(0));
        BOOST_REQUIRE_EQUAL(rData3bData[0][3], data5.get()->getPosition().at(1));
        BOOST_REQUIRE_EQUAL(rData3bData[0][4], data5.get()->getPosition().at(2));

        //Head (Data4 and 5)
        BOOST_REQUIRE_EQUAL(rData3aHead[0][0], 0);
        BOOST_REQUIRE_EQUAL(rData3aHead[0][1], 3);
        BOOST_REQUIRE_EQUAL(rData3bHead[0][0], 0);
        BOOST_REQUIRE_EQUAL(rData3bHead[0][1], 4);

        //Close resources
        H5Dclose(dset1Data);
        H5Dclose(dset1Head);
        H5Dclose(dset2Data);
        H5Dclose(dset2Head);
        H5Dclose(dset3aData);
        H5Dclose(dset3aHead);
        H5Dclose(dset3bData);
        H5Dclose(dset3bHead);

        H5Sclose(space1Data);
        H5Sclose(space1Head);
        H5Sclose(space2Data);
        H5Sclose(space2Head);
        H5Sclose(space3aData);
        H5Sclose(space3aHead);
        H5Sclose(space3bData);
        H5Sclose(space3bHead);

        dataGroup.get()->close();
        timeGroup1.get()->close();
        timeGroup2.get()->close();
        timeGroup3.get()->close();

    } catch (...) {
        BOOST_FAIL("Failure 2 in checkIHdfWritables!  Exiting");
    }

    //Make sure the writeAttributes fail for invalid stuff
    try {
        component.createGroup(h5File, badGroup);
    } catch(...) {
        exceptionHit0 = true;
    }
    BOOST_REQUIRE_EQUAL(exceptionHit0, true);

    try {
        component.createGroup(badFile, h5Group);
    } catch(...) {
        exceptionHit1 = true;
    }
    BOOST_REQUIRE_EQUAL(exceptionHit1, true);

    //Check Group Creation
    std::shared_ptr<H5::Group> group2 = component.createGroup(h5File, h5Group);
    //See that the previous group has a group
    BOOST_REQUIRE_EQUAL(3, h5Group.get()->getNumObjs());
    //Check that it has the same name as the root component
    BOOST_REQUIRE_EQUAL(component.getName(), h5Group.get()->getObjnameByIdx(0));
    //Check that the returned group is a Group but no members
    BOOST_REQUIRE_EQUAL(0, group2.get()->getNumObjs());
    BOOST_REQUIRE_EQUAL(0, group2.get()->getNumAttrs());

    //Close resources
    h5Group.get()->close();
    group2.get()->close();
    h5File.get()->close();

    //Delete the file

    //Delete file
    remove(dataFile.c_str());

    return;

    // end-user-code

}


BOOST_AUTO_TEST_CASE(checkHDF5Readables) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    LWRGridManager component(size);
    LWRGridManager newComponent(-1);
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;

    //Setup Components
    std::shared_ptr<Component> component1 (new LWRComponent("Billy Bob"));
    std::shared_ptr<Component> component2 (new LWRComponent("Bobby Bill"));

    //Setup Locations
    std::shared_ptr<GridLocation> location1 (new GridLocation(1, 3));
    std::shared_ptr<GridLocation> location2 (new GridLocation(3, 4));
    std::shared_ptr<GridLocation> location3 (new GridLocation(1, 1));

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Exceptions
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);
    component.addComponent(component1, location1);
    component.addComponent(component2, location2);
    component.addComponent(component2, location3);

    //Setup LWRData
    std::string feature1 = "Feature 1";
    std::string feature2 = "Feature 2";
    double time1= 1.0, time2 = 3.0, time3 = 3.5;
    std::vector<double> position1;
    std::vector<double> position2;
    std::vector<double> position3;
    std::vector<double> position4;
    std::vector<double> position5;

    //Setup Positions

    //Setup Position 1
    position1.push_back(0.0);
    position1.push_back(1.0);
    position1.push_back(0.0);

    //Setup Position 2
    position2.push_back(0.0);
    position2.push_back(1.0);
    position2.push_back(4.0);

    //Setup Position 3
    position3.push_back(1.0);
    position3.push_back(1.0);
    position3.push_back(0.0);

    //Setup Position 4
    position4.push_back(0.0);
    position4.push_back(1.0);
    position4.push_back(1.0);

    //Setup Position 5
    position5.push_back(0.0);
    position5.push_back(1.0);
    position5.push_back(3.0);


    //Setup data1
    std::shared_ptr<LWRData> data1( new LWRData(feature1));
    data1.get()->setPosition(position1);
    data1.get()->setValue(1.0);
    data1.get()->setUncertainty(1.5);
    data1.get()->setUnits("Units 1");

    //Setup data2
    std::shared_ptr<LWRData> data2(new LWRData(feature1));
    data2.get()->setPosition(position2);
    data2.get()->setValue(2.0);
    data2.get()->setUncertainty(2.5);
    data2.get()->setUnits("Units 2");

    //Setup data3
    std::shared_ptr<LWRData> data3(new LWRData(feature1) );
    data3.get()->setPosition(position3);
    data3.get()->setValue(3.0);
    data3.get()->setUncertainty(3.5);
    data3.get()->setUnits("Units 3");

    //Setup data4
    std::shared_ptr<LWRData> data4( new LWRData(feature1) );
    data4.get()->setPosition(position4);
    data4.get()->setValue(4.0);
    data4.get()->setUncertainty(4.5);
    data4.get()->setUnits("Units 4");

    //Setup data5
    std::shared_ptr<LWRData> data5( new LWRData(feature2) );
    data5.get()->setPosition(position5);
    data5.get()->setValue(5.0);
    data5.get()->setUncertainty(5.5);
    data5.get()->setUnits("Units 5");

    //Add data to the component
    location1.get()->getLWRDataProvider().get()->addData(data1, time1);
    location1.get()->getLWRDataProvider().get()->addData(data2, time1);
    location1.get()->getLWRDataProvider().get()->addData(data3, time2);
    location1.get()->getLWRDataProvider().get()->addData(data4, time3);
    location1.get()->getLWRDataProvider().get()->addData(data5, time3);

    //Do readChild checks here. For LWRComponent this method always returns true
    std::shared_ptr<LWRComponent> nulledComponent;
    BOOST_REQUIRE_EQUAL(component.readChild(nulledComponent), true);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check reading attributes

    //Create a group
    std::shared_ptr<H5::Group> h5Group1 (new H5::Group (h5File.get()->createGroup("/Group1")));

    //Use the writers.
    //Yes, this creates a dependency on the writers to work before the readers, but for testing scalability concerns, this class needs to be edited as little as possible.
    //Since the java code will change drastically, this is the way I have chosen to do it.  "Don't re-invent the wheel" - SFH @ 02122012@1223 hrs

    try {
        //Write everything from component
        BOOST_REQUIRE_EQUAL(component.writeAttributes(h5File, h5Group1), true);
        BOOST_REQUIRE_EQUAL(component.writeDatasets(h5File, h5Group1), true);

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
        BOOST_REQUIRE_EQUAL(component==newComponent, true);

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
        BOOST_REQUIRE_EQUAL(component==newComponent, true);

        //Check for nullaries
        std::shared_ptr<H5::Group> badGroup;
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(badGroup), false);

        //Doesn't change anything
        BOOST_REQUIRE_EQUAL(component==newComponent, true);

        //Close resources
        h5File.get()->close();
        h5Group1.get()->close();

    } catch (...) {
        BOOST_FAIL("FAILURE IN LWRCOMPONENTTESTER::checkReadables.  Exiting");
    }

    //Delete the file

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
