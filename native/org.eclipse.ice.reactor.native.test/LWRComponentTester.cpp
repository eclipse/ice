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
#define BOOST_TEST_MODULE LWRComponentTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <LWRComponent.h>
#include <HDF5LWRTagType.h>
#include <string>
#include <vector>
#include <algorithm>
#include <HdfFileFactory.h>
#include <UtilityOperations.h>
#include <HdfReaderFactory.h>
#include <HdfWriterFactory.h>
#include "LWRData.h"
#include <H5Cpp.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(LWRComponentTester_testSuite)

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

    HDF5LWRTagType type = LWRCOMPONENT;
    std::string timeUnit = "seconds";
    std::string nullString;

    //Check component
    LWRComponent component1;

    //Check default values - name, id, description
    BOOST_REQUIRE_EQUAL("Component 1", component1.getName());
    BOOST_REQUIRE_EQUAL(0, component1.getId());
    BOOST_REQUIRE_EQUAL("Component 1's Description", component1.getDescription());
    BOOST_REQUIRE_EQUAL(type, component1.getHDF5LWRTag());
    BOOST_REQUIRE_EQUAL(timeUnit, component1.getTimeUnits());
    BOOST_REQUIRE_EQUAL(0, component1.getCurrentTime());
    BOOST_REQUIRE_EQUAL(0, component1.getNumberOfTimeSteps());
    BOOST_REQUIRE_EQUAL("No Source Available", component1.getSourceInfo());

    //Check nonnullary constructor - name
    LWRComponent component2("Bob");

    //Check default values - name, id, description
    BOOST_REQUIRE_EQUAL("Bob", component2.getName());
    BOOST_REQUIRE_EQUAL(0, component2.getId());
    BOOST_REQUIRE_EQUAL("Component 1's Description", component2.getDescription());
    BOOST_REQUIRE_EQUAL(type, component2.getHDF5LWRTag());
    BOOST_REQUIRE_EQUAL(timeUnit, component2.getTimeUnits());
    BOOST_REQUIRE_EQUAL(0, component2.getCurrentTime());
    BOOST_REQUIRE_EQUAL(0, component2.getNumberOfTimeSteps());
    BOOST_REQUIRE_EQUAL("No Source Available", component2.getSourceInfo());

    //Check erroneous value for constructor - name=null
    LWRComponent component3(nullString);

    //Check default values - name, id, description
    BOOST_REQUIRE_EQUAL("Component 1", component3.getName()); //Defaults
    BOOST_REQUIRE_EQUAL(0, component3.getId());
    BOOST_REQUIRE_EQUAL("Component 1's Description", component3.getDescription());
    BOOST_REQUIRE_EQUAL(type, component3.getHDF5LWRTag());
    BOOST_REQUIRE_EQUAL(timeUnit, component3.getTimeUnits());
    BOOST_REQUIRE_EQUAL(0, component3.getCurrentTime());
    BOOST_REQUIRE_EQUAL(0, component3.getNumberOfTimeSteps());
    BOOST_REQUIRE_EQUAL("No Source Available", component3.getSourceInfo());

    //Check erroneous value for constructor - empty string
    LWRComponent component4("");

    //Check default values - name, id, description
    BOOST_REQUIRE_EQUAL("Component 1", component4.getName()); //Defaults
    BOOST_REQUIRE_EQUAL(0, component4.getId());
    BOOST_REQUIRE_EQUAL("Component 1's Description", component4.getDescription());
    BOOST_REQUIRE_EQUAL(type, component4.getHDF5LWRTag());
    BOOST_REQUIRE_EQUAL(timeUnit, component4.getTimeUnits());
    BOOST_REQUIRE_EQUAL(0, component4.getCurrentTime());
    BOOST_REQUIRE_EQUAL(0, component4.getNumberOfTimeSteps());
    BOOST_REQUIRE_EQUAL("No Source Available", component4.getSourceInfo());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkAttributes) {

    // begin-user-code

    //Local declarations
    LWRComponent component;
    std::string nullString;

    //Change all values
    component.setName("Component 2");
    component.setId(2);
    component.setDescription("FooRA!");

    //Check values - name, id, description
    BOOST_REQUIRE_EQUAL("Component 2", component.getName());
    BOOST_REQUIRE_EQUAL(2, component.getId());
    BOOST_REQUIRE_EQUAL("FooRA!", component.getDescription());

    //Try to set them illegally
    component.setName(nullString);
    component.setId(-1);
    component.setDescription(nullString);

    //Check values - name, id, description - does not change!
    BOOST_REQUIRE_EQUAL("Component 2", component.getName());
    BOOST_REQUIRE_EQUAL(2, component.getId());
    BOOST_REQUIRE_EQUAL("FooRA!", component.getDescription());

    //Check setting the name to empty string
    component.setName("");
    component.setName(" ");
    component.setName("       ");
    component.setDescription("");
    component.setDescription(" ");
    component.setDescription("       ");

    //See the name will not be the representation of the empty string.
    BOOST_REQUIRE_EQUAL("Component 2", component.getName());
    BOOST_REQUIRE_EQUAL(2, component.getId());
    BOOST_REQUIRE_EQUAL("FooRA!", component.getDescription());

    //Also, see that it will always trim the strings for Name and description
    component.setName("Component  3  ");
    component.setDescription(" FooRA!! ");

    //See the data is trimmed appropriately
    BOOST_REQUIRE_EQUAL("Component  3", component.getName()); //Note double space inbetween Component and 3
    BOOST_REQUIRE_EQUAL(2, component.getId());
    BOOST_REQUIRE_EQUAL("FooRA!!", component.getDescription());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    // Create a LWRComponent
    LWRComponent testLWRComponent("ICE LWRComponent");

    // Set its data
    testLWRComponent.setId(12);
    testLWRComponent.setDescription("This is a LWRComponent that will be used for testing equality with other LWRComponents.");

    // Create another LWRComponent to assert Equality with the last
    LWRComponent equalObject("ICE LWRComponent");

    // Set its data, equal to testLWRComponent
    equalObject.setId(12);
    equalObject.setDescription("This is a LWRComponent that will be used for testing equality with other LWRComponents.");

    // Create a LWRComponent that is not equal to testLWRComponent
    LWRComponent unEqualObject;

    // Set its data, not equal to testLWRComponent
    unEqualObject.setId(52);
    unEqualObject.setName("Bill the LWRComponent");
    unEqualObject
    .setDescription("This is a LWRComponent to verify that LWRComponent.equals() returns false for an object that is not equivalent to testLWRComponent.");

    // Create a third LWRComponent to test Transitivity
    LWRComponent transitiveObject("ICE LWRComponent");

    // Set its data, not equal to testLWRComponent
    transitiveObject.setId(12);
    transitiveObject.setDescription("This is a LWRComponent that will be used for testing equality with other LWRComponents.");

    // Assert that these two LWRComponents are equal
    BOOST_REQUIRE_EQUAL(testLWRComponent==equalObject, true);

    // Assert that two unequal objects returns false
    BOOST_REQUIRE_EQUAL(testLWRComponent==unEqualObject, false);

    // Check that equals() is Reflexive
    // x==(x) = true
    BOOST_REQUIRE_EQUAL(testLWRComponent==testLWRComponent, true);

    // Check that equals() is Symmetric
    // x==(y) = true iff y==(x) = true
    BOOST_REQUIRE_EQUAL(testLWRComponent==equalObject
                        && equalObject==testLWRComponent, true);

    // Check that equals() is Transitive
    // x==(y) = true, y==(z) = true => x==(z) = true
    if (testLWRComponent==equalObject
            && equalObject==transitiveObject, true) {
        BOOST_REQUIRE_EQUAL(testLWRComponent==transitiveObject, true);
    } else {
        BOOST_FAIL("EQUALITY CHECK FAILED.  EXITING");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(testLWRComponent==equalObject
                        && testLWRComponent==equalObject
                        && testLWRComponent==equalObject, true);
    BOOST_REQUIRE_EQUAL(!(testLWRComponent==unEqualObject)
                        && !(testLWRComponent==unEqualObject)
                        && !(testLWRComponent==unEqualObject), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkVisitation) {
    //TODO Auto-generated method stub
    // begin-user-code
    // TODO Auto-generated method stub

    // end-user-code
    return;
}
BOOST_AUTO_TEST_CASE(checkNotifications) {
    /*   //TODO Auto-generated method stub
    	// begin-user-code

    	//Local declarations
    	LWRComponent component = new LWRComponent("Bob");
    	LWRData data = new LWRData();
    	data.setFeature("Billy");

    	//Create the listener
    	this.testComponentListener = new TestComponentListener();

    	//Register the listener
    	component.register(this.testComponentListener);

    	//Change the name
    	component.setName("Bob");

    	//See if the listener was notified
    	BOOST_REQUIRE_EQUAL(testComponentListener.wasNotified());

    	//Reset the listener
    	testComponentListener.reset();

    	//Change the id
    	component.setId(55);

    	//See if the listener was notified
    	BOOST_REQUIRE_EQUAL(testComponentListener.wasNotified());

    	//Reset the listener
    	testComponentListener.reset();

    	//set the SourceInfo
    	component.setSourceInfo("Bob");

    	//See if the listener was notified
    	BOOST_REQUIRE_EQUAL(testComponentListener.wasNotified());

    	//Reset the listener
    	testComponentListener.reset();

    	//set the timeStep
    	component.setTime(5);

    	//See if the listener was notified
    	BOOST_REQUIRE_EQUAL(testComponentListener.wasNotified());

    	//Reset the listener
    	testComponentListener.reset();

    	//Add a feature to the tree
    	component.addData(data);

    	//See if the listener was notified
    	BOOST_REQUIRE_EQUAL(testComponentListener.wasNotified());

    	//Reset the listener
    	testComponentListener.reset();

    	//Remove data
    	component.removeAllDataFromFeature(data.getFeature());

    	//See if the listener was notified
    	BOOST_REQUIRE_EQUAL(testComponentListener.wasNotified());

    	//Reset the listener
    	testComponentListener.reset();

    	// end-user-code
       return;*/
}
BOOST_AUTO_TEST_CASE(checkIDataProvider) {

    // begin-user-code
    //Local Declarations
    std::string name = "Bob";
    std::string data1Feature = "Data1";
    std::string data2Feature = "Data2";
    std::string defaultSource = "No Source Available";
    std::string newSource = "A source";
    double time1 = 1.1;
    double time2 = 1.0;
    double time3 = 1.3;
    std::string timeUnit = "Minutes!";
    std::string nullString;


    //Create a component, set the number of time steps, and show the behaviors of setting the current time step
    LWRComponent component(name);

    //Try to set the time step to 1
    component.setTime(1);

    //Show that it is equal to the set timestep
    BOOST_REQUIRE_EQUAL(1.0, component.getCurrentTime());

    //Check invalid - negative
    component.setTime(-1);

    //Defaults
    BOOST_REQUIRE_EQUAL(1.0, component.getCurrentTime());

    //Check valid - 0
    component.setTime(0);

    //Equal to the set timeStep
    BOOST_REQUIRE_EQUAL(0, component.getCurrentTime());

    //Get/set sourceInfo
    LWRComponent component2(name);

    //Check default
    BOOST_REQUIRE_EQUAL(defaultSource, component2.getSourceInfo());

    //Try to set it to a new std::string
    component2.setSourceInfo(newSource);
    //Check values - should be newSource
    BOOST_REQUIRE_EQUAL(newSource, component2.getSourceInfo());

    //Try to set it to null - invalid
    component2.setSourceInfo(nullString);
    //Check values - should be newSource
    BOOST_REQUIRE_EQUAL(newSource, component2.getSourceInfo()); //Defaults

    //Try to set it to empty string - invalid
    component2.setSourceInfo("");
    //Check values - should be newSource
    BOOST_REQUIRE_EQUAL(newSource, component2.getSourceInfo()); //Defaults

    //Show that the string is trimmed
    component2.setSourceInfo(newSource + " ");
    //Check values - should be newSource
    BOOST_REQUIRE_EQUAL(newSource, component2.getSourceInfo()); //Trimmed

    //Setup timeUnits
    component2.setTimeUnits(timeUnit);
    //Check values
    BOOST_REQUIRE_EQUAL(timeUnit, component2.getTimeUnits());

    //Set timeUnits - nullary
    component2.setTimeUnits(nullString);
    //Check values - nothing has changed
    BOOST_REQUIRE_EQUAL(timeUnit, component2.getTimeUnits());

    //Set timeUnits - "empty string"
    component2.setTimeUnits(" ");
    //Check values - nothing has changed <- auto trims strings
    BOOST_REQUIRE_EQUAL(timeUnit, component2.getTimeUnits());

    //Check getters for IData
    BOOST_REQUIRE_EQUAL(0, component2.getFeatureList().size());
    BOOST_REQUIRE_EQUAL(0, component2.getFeaturesAtCurrentTime().size());
    BOOST_REQUIRE_EQUAL(-1, component2.getTimeStep(0.0));
    BOOST_REQUIRE_EQUAL(0, component2.getDataAtCurrentTime(data1Feature).size());

    //Add a IData
    std::shared_ptr<LWRData> data1(new LWRData(data1Feature));
    component2.addData(data1, time1);
    //Check getters
    BOOST_REQUIRE_EQUAL(1, component2.getFeatureList().size());
    BOOST_REQUIRE_EQUAL(data1Feature, component2.getFeatureList().at(0));
    BOOST_REQUIRE_EQUAL(0, component2.getCurrentTime());
    BOOST_REQUIRE_EQUAL(0, component2.getDataAtCurrentTime(data1Feature).size());
    //Change current time
    component2.setTime(time1);
    //Check values
    BOOST_REQUIRE_EQUAL(1, component2.getDataAtCurrentTime(data1Feature).size());
    BOOST_REQUIRE_EQUAL(data1.get()==component2.getDataAtCurrentTime(data1Feature).at(0).get(), true);
    //Check number of timesteps
    BOOST_REQUIRE_EQUAL(1, component2.getNumberOfTimeSteps());

    //Add more data to current time with same feature
    std::shared_ptr<LWRData> data2 (new LWRData(data1Feature));
    data2.get()->setUncertainty(33.333);
    //Check getters
    component2.addData(data2, time1);
    BOOST_REQUIRE_EQUAL(1, component2.getFeatureList().size());
    BOOST_REQUIRE_EQUAL(data1Feature, component2.getFeatureList().at(0));
    BOOST_REQUIRE_EQUAL(2, component2.getDataAtCurrentTime(data1Feature).size());
    BOOST_REQUIRE_EQUAL(data1.get()==component2.getDataAtCurrentTime(data1Feature).at(0).get(), true);
    BOOST_REQUIRE_EQUAL(data2.get()==component2.getDataAtCurrentTime(data1Feature).at(1).get(), true);
    //Check number of timesteps
    BOOST_REQUIRE_EQUAL(1, component2.getNumberOfTimeSteps());
    //Check times
    BOOST_REQUIRE_EQUAL(1, component2.getTimes().size());
    BOOST_REQUIRE_EQUAL(time1, component2.getTimes().at(0));
    BOOST_REQUIRE_EQUAL(0, component2.getTimeStep(time1));

    //Add a LWRData to a different time location
    std::shared_ptr<LWRData> data3( new LWRData(data1Feature));
    data3.get()->setUncertainty(22.2);
    component2.addData(data3, time2);
    //Check getters
    BOOST_REQUIRE_EQUAL(1, component2.getFeatureList().size());
    BOOST_REQUIRE_EQUAL(data1Feature, component2.getFeatureList().at(0));
    BOOST_REQUIRE_EQUAL(2, component2.getDataAtCurrentTime(data1Feature).size());
    BOOST_REQUIRE_EQUAL(data1.get()==component2.getDataAtCurrentTime(data1Feature).at(0).get(), true);
    BOOST_REQUIRE_EQUAL(data2.get()==component2.getDataAtCurrentTime(data1Feature).at(1).get(), true);
    //Change time frame
    component2.setTime(time2);
    //Check information
    BOOST_REQUIRE_EQUAL(1, component2.getDataAtCurrentTime(data1Feature).size());
    BOOST_REQUIRE_EQUAL(data3.get()==component2.getDataAtCurrentTime(data1Feature).at(0).get(), true);
    //Check number of timesteps
    BOOST_REQUIRE_EQUAL(2, component2.getNumberOfTimeSteps());
    //Check times
    BOOST_REQUIRE_EQUAL(2, component2.getTimes().size());
    BOOST_REQUIRE_EQUAL(time2, component2.getTimes().at(0));
    BOOST_REQUIRE_EQUAL(time1, component2.getTimes().at(1));
    BOOST_REQUIRE_EQUAL(0, component2.getTimeStep(time2));
    BOOST_REQUIRE_EQUAL(1, component2.getTimeStep(time1));


    //Add a LWRData with the same time as time2, but different feature
    std::shared_ptr<LWRData> data4(new LWRData(data2Feature));
    data4.get()->setUncertainty(11.1);
    component2.addData(data4, time2);

    //Check getters
    BOOST_REQUIRE_EQUAL(2, component2.getFeatureList().size());
    BOOST_REQUIRE_EQUAL(std::find(component2.getFeatureList().begin(), component2.getFeatureList().end(), data1Feature) != component2.getFeatureList().end(), true);
    BOOST_REQUIRE_EQUAL(std::find(component2.getFeatureList().begin(), component2.getFeatureList().end(), data2Feature) != component2.getFeatureList().end(), true);
    BOOST_REQUIRE_EQUAL(1, component2.getDataAtCurrentTime(data1Feature).size());
    BOOST_REQUIRE_EQUAL(data3.get()==component2.getDataAtCurrentTime(data1Feature).at(0).get(), true);
    BOOST_REQUIRE_EQUAL(1, component2.getDataAtCurrentTime(data2Feature).size());
    BOOST_REQUIRE_EQUAL(data4.get()==component2.getDataAtCurrentTime(data2Feature).at(0).get(), true);
    //Check number of timesteps
    BOOST_REQUIRE_EQUAL(2, component2.getNumberOfTimeSteps());
    //Check times
    BOOST_REQUIRE_EQUAL(2, component2.getTimes().size());
    BOOST_REQUIRE_EQUAL(time2, component2.getTimes().at(0));
    BOOST_REQUIRE_EQUAL(time1, component2.getTimes().at(1));
    BOOST_REQUIRE_EQUAL(0, component2.getTimeStep(time2));
    BOOST_REQUIRE_EQUAL(1, component2.getTimeStep(time1));

    //Check nullaries and invalid setters/parameters
    BOOST_REQUIRE_EQUAL(-1, component2.getTimeStep(time1+3333));
    BOOST_REQUIRE_EQUAL(-1, component2.getTimeStep(-1.0));
    component2.addData(data4, -1.0);
    BOOST_REQUIRE_EQUAL(0, component2.getDataAtCurrentTime("").size());
    BOOST_REQUIRE_EQUAL(0, component2.getDataAtCurrentTime("FeatureDNE!@#12321321").size());

    //Change to invalid time
    component2.setTime(time3+3333333);
    BOOST_REQUIRE_EQUAL(time3+3333333, component2.getCurrentTime());
    component2.setTime(-1.0); //Try negative
    BOOST_REQUIRE_EQUAL(time3+3333333, component2.getCurrentTime()); //Does not change
    BOOST_REQUIRE_EQUAL(0, component2.getFeaturesAtCurrentTime().size());
    BOOST_REQUIRE_EQUAL(0, component2.getDataAtCurrentTime(data1Feature).size());

    //Check getters - change time back and show data is not changed
    component2.setTime(time2);
    BOOST_REQUIRE_EQUAL(2, component2.getFeatureList().size());
    BOOST_REQUIRE_EQUAL(std::find(component2.getFeatureList().begin(), component2.getFeatureList().end(), data1Feature) != component2.getFeatureList().end(), true);
    BOOST_REQUIRE_EQUAL(std::find(component2.getFeatureList().begin(), component2.getFeatureList().end(), data2Feature) != component2.getFeatureList().end(), true);
    BOOST_REQUIRE_EQUAL(1, component2.getDataAtCurrentTime(data1Feature).size());
    BOOST_REQUIRE_EQUAL(data3.get()==component2.getDataAtCurrentTime(data1Feature).at(0).get(), true);
    BOOST_REQUIRE_EQUAL(1, component2.getDataAtCurrentTime(data2Feature).size());
    BOOST_REQUIRE_EQUAL(data4.get()==component2.getDataAtCurrentTime(data2Feature).at(0).get(), true);
    //Check number of timesteps
    BOOST_REQUIRE_EQUAL(2, component2.getNumberOfTimeSteps());


    // end-user-code
    return;//*/
}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local declarations

    //Values
    std::string name = "A LWRComponent!@!@#!#@56483";
    std::string description = "Description !@#!@#!@#!46546484328";
    int id = 68468431;
    int timeStep = 3;
    std::string sourceInfo = "5465465SOURCEINFO!@#!#!#@!#@";

    //Setup Object to test
    LWRComponent object(name);
    object.setTime(timeStep);
    object.setId(id);
    object.setDescription(description);
    object.setSourceInfo(sourceInfo);
    std::shared_ptr<LWRData> data( new LWRData("Feature 545"));
    object.addData(data, 1.0);

    //Run the copy routine
    LWRComponent copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==copyObject, true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<LWRComponent> castedObject (new LWRComponent(*(dynamic_cast<LWRComponent *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    LWRComponent component;
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    std::string sourceInfo = "ASDASDASD";
    std::string timeUnits = "UNITS OF AWESOME";
    double time = 1.0;

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Setup LWRData
    std::string feature1 = "Feature 1";
    std::string feature2 = "Feature 2";
    double time1= 1.0, time2 = 3.0, time3 = 3.5;
    std::vector<double> position1;
    std::vector<double> position2;
    std::vector<double> position3;
    std::vector<double> position4;
    std::vector<double> position5;

    //Exceptions
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

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
    position4.push_back(0.0);
    position4.push_back(1.0);
    position4.push_back(3.0);


    //Setup data1
    std::shared_ptr<LWRData> data1( new LWRData(feature1));
    data1.get()->setPosition(position1);
    data1.get()->setValue(1.0);
    data1.get()->setUncertainty(1.5);
    data1.get()->setUnits("Units 123456");

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
    component.addData(data1, time1);
    component.addData(data2, time1);
    component.addData(data3, time2);
    component.addData(data4, time3);
    component.addData(data5, time3);

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);
    component.setSourceInfo(sourceInfo);
    component.setTime(time);
    component.setTimeUnits(timeUnits);

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
        BOOST_REQUIRE_EQUAL(1, h5Group.get()->getNumObjs());


        //Check the meta data
        BOOST_REQUIRE_EQUAL(4, h5Group.get()->getNumAttrs());

        //Values to check

        //Get attributes by reader factory and check values
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "id"), id);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "name"), name);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "description"), description);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "HDF5LWRTag"), UtilityOperations::toStringHDF5Tag(tag));

        //Check the Group names and contents
        BOOST_REQUIRE_EQUAL("State Point Data", h5Group.get()->getObjnameByIdx(0));

        //Get the IDataGroup
        std::shared_ptr<H5::Group> dataGroup(new H5::Group(h5Group.get()->openGroup("State Point Data")));

        //Check the size of the data groups
        BOOST_REQUIRE_EQUAL(3, dataGroup.get()->getNumObjs());

        //Check the names of the groups.  These should reflect the number of time steps
        BOOST_REQUIRE_EQUAL("TimeStep: 0", dataGroup.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("TimeStep: 1", dataGroup.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("TimeStep: 2", dataGroup.get()->getObjnameByIdx(2));

        //Get the TimeStep Groups and check contents
        std::shared_ptr<H5::Group> timeGroup1(new H5::Group(dataGroup.get()->openGroup(dataGroup.get()->getObjnameByIdx(0))));
        std::shared_ptr<H5::Group> timeGroup2(new H5::Group(dataGroup.get()->openGroup(dataGroup.get()->getObjnameByIdx(1))));
        std::shared_ptr<H5::Group> timeGroup3(new H5::Group(dataGroup.get()->openGroup(dataGroup.get()->getObjnameByIdx(2))));


        //Check that there is a group and attributes
        BOOST_REQUIRE_EQUAL(1, timeGroup1.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(1, timeGroup2.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(2, timeGroup3.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(2, timeGroup1.get()->getNumAttrs());
        BOOST_REQUIRE_EQUAL(2, timeGroup2.get()->getNumAttrs());
        BOOST_REQUIRE_EQUAL(2, timeGroup3.get()->getNumAttrs());

        //Check Attributes on timeGroups

        //Check timeGroup1 attributes
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(timeGroup1, "time"), time1);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(timeGroup1, "units"), component.getTimeUnits());


        //Check timeGroup2 attributes
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(timeGroup2, "time"), time2);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(timeGroup2, "units"), component.getTimeUnits());

        //Check timeGroup3 attributes
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(timeGroup3, "time"), time3);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(timeGroup3, "units"), component.getTimeUnits());

        //Open the datasets on the timeGroups

        hid_t dset1, dset2, dset3a, dset3b;

        //Get the datasets
        dset1 = H5Dopen(timeGroup1.get()->getId(), timeGroup1.get()->getObjnameByIdx(0).c_str(), H5P_DEFAULT);
        dset2 = H5Dopen(timeGroup2.get()->getId(), timeGroup2.get()->getObjnameByIdx(0).c_str(), H5P_DEFAULT);
        dset3a = H5Dopen(timeGroup3.get()->getId(), timeGroup3.get()->getObjnameByIdx(0).c_str(), H5P_DEFAULT);
        dset3b = H5Dopen(timeGroup3.get()->getId(), timeGroup3.get()->getObjnameByIdx(1).c_str(), H5P_DEFAULT);

        //Check the names
        BOOST_REQUIRE_EQUAL("Feature 1", timeGroup1.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Feature 1", timeGroup2.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Feature 1", timeGroup3.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Feature 2", timeGroup3.get()->getObjnameByIdx(1));

        //Read the data off each dset

        //Open the datasets
        dset1 = H5Dopen(timeGroup1.get()->getId(), timeGroup1.get()->getObjnameByIdx(0).c_str(), H5P_DEFAULT);
        dset2 = H5Dopen(timeGroup2.get()->getId(), timeGroup2.get()->getObjnameByIdx(0).c_str(), H5P_DEFAULT);
        dset3a = H5Dopen(timeGroup3.get()->getId(), timeGroup3.get()->getObjnameByIdx(0).c_str(), H5P_DEFAULT);
        dset3b = H5Dopen(timeGroup3.get()->getId(), timeGroup3.get()->getObjnameByIdx(1).c_str(), H5P_DEFAULT);

        //Get the space
        hid_t space1 = H5Dget_space(dset1);
        hid_t space2 = H5Dget_space(dset2);
        hid_t space3a = H5Dget_space(dset3a);
        hid_t space3b = H5Dget_space(dset3b);

        //Get the Dimensions
        hsize_t ndims1[2], ndims2[2], ndims3a[2], ndims3b[2];

        //Get the ndims
        int rank1 = H5Sget_simple_extent_dims(space1, ndims1, NULL);
        int rank2 = H5Sget_simple_extent_dims(space2, ndims2, NULL);
        int rank3a = H5Sget_simple_extent_dims(space3a, ndims3a, NULL);
        int rank3b = H5Sget_simple_extent_dims(space3b, ndims3b, NULL);

        //Make sure they are 2 ranks
        BOOST_REQUIRE_EQUAL(rank1, 2);
        BOOST_REQUIRE_EQUAL(rank2, 2);
        BOOST_REQUIRE_EQUAL(rank3a, 2);
        BOOST_REQUIRE_EQUAL(rank3b, 2);

        //Check dimension sizes
        BOOST_REQUIRE_EQUAL(ndims1[0], 1);
        BOOST_REQUIRE_EQUAL(ndims1[1], 2);
        BOOST_REQUIRE_EQUAL(ndims2[0], 1);
        BOOST_REQUIRE_EQUAL(ndims2[1], 1);
        BOOST_REQUIRE_EQUAL(ndims3a[0], 1);
        BOOST_REQUIRE_EQUAL(ndims3a[1], 1);
        BOOST_REQUIRE_EQUAL(ndims3b[0], 1);
        BOOST_REQUIRE_EQUAL(ndims3b[1], 1);

        //Get data - prepare on stack
        LWRComponent::dataSet_t rData1 [ndims1[0]][ndims1[1]];
        LWRComponent::dataSet_t rData2 [ndims2[0]][ndims2[1]];
        LWRComponent::dataSet_t rData3a [ndims3a[0]][ndims3a[1]];
        LWRComponent::dataSet_t rData3b [ndims3b[0]][ndims3b[1]];

        //Retrieve data
        H5Dread(dset1, H5Dget_type(dset1), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData1);
        H5Dread(dset2, H5Dget_type(dset2), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData2);
        H5Dread(dset3a, H5Dget_type(dset3a), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData3a);
        H5Dread(dset3b, H5Dget_type(dset3b), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData3b);

        //Check values for timeGroup1
        //Data1
        BOOST_REQUIRE_EQUAL(rData1[0][0].value, data1.get()->getValue());
        BOOST_REQUIRE_EQUAL(rData1[0][0].uncertainty, data1.get()->getUncertainty());
        BOOST_REQUIRE_EQUAL(rData1[0][0].units, data1.get()->getUnits().c_str());
        BOOST_REQUIRE_EQUAL(rData1[0][0].position[0], data1.get()->getPosition().at(0));
        BOOST_REQUIRE_EQUAL(rData1[0][0].position[1], data1.get()->getPosition().at(1));
        BOOST_REQUIRE_EQUAL(rData1[0][0].position[2], data1.get()->getPosition().at(2));
        //Data2
        BOOST_REQUIRE_EQUAL(rData1[0][1].value, data2.get()->getValue());
        BOOST_REQUIRE_EQUAL(rData1[0][1].uncertainty, data2.get()->getUncertainty());
        BOOST_REQUIRE_EQUAL(rData1[0][1].units, data2.get()->getUnits().c_str());
        BOOST_REQUIRE_EQUAL(rData1[0][1].position[0], data2.get()->getPosition().at(0));
        BOOST_REQUIRE_EQUAL(rData1[0][1].position[1], data2.get()->getPosition().at(1));
        BOOST_REQUIRE_EQUAL(rData1[0][1].position[2], data2.get()->getPosition().at(2));

        //Check values for timeGroup2
        //Data3
        BOOST_REQUIRE_EQUAL(rData2[0][0].value, data3.get()->getValue());
        BOOST_REQUIRE_EQUAL(rData2[0][0].uncertainty, data3.get()->getUncertainty());
        BOOST_REQUIRE_EQUAL(rData2[0][0].units, data3.get()->getUnits().c_str());
        BOOST_REQUIRE_EQUAL(rData2[0][0].position[0], data3.get()->getPosition().at(0));
        BOOST_REQUIRE_EQUAL(rData2[0][0].position[1], data3.get()->getPosition().at(1));
        BOOST_REQUIRE_EQUAL(rData2[0][0].position[2], data3.get()->getPosition().at(2));

        //Check values for timeGroup3a
        //Data4
        BOOST_REQUIRE_EQUAL(rData3a[0][0].value, data4.get()->getValue());
        BOOST_REQUIRE_EQUAL(rData3a[0][0].uncertainty, data4.get()->getUncertainty());
        BOOST_REQUIRE_EQUAL(rData3a[0][0].units, data4.get()->getUnits().c_str());
        BOOST_REQUIRE_EQUAL(rData3a[0][0].position[0], data4.get()->getPosition().at(0));
        BOOST_REQUIRE_EQUAL(rData3a[0][0].position[1], data4.get()->getPosition().at(1));
        BOOST_REQUIRE_EQUAL(rData3a[0][0].position[2], data4.get()->getPosition().at(2));
        //Check values for timeGroup3b
        //Data5
        BOOST_REQUIRE_EQUAL(rData3b[0][0].value, data5.get()->getValue());
        BOOST_REQUIRE_EQUAL(rData3b[0][0].uncertainty, data5.get()->getUncertainty());
        BOOST_REQUIRE_EQUAL(rData3b[0][0].units, data5.get()->getUnits().c_str());
        BOOST_REQUIRE_EQUAL(rData3b[0][0].position[0], data5.get()->getPosition().at(0));
        BOOST_REQUIRE_EQUAL(rData3b[0][0].position[1], data5.get()->getPosition().at(1));
        BOOST_REQUIRE_EQUAL(rData3b[0][0].position[2], data5.get()->getPosition().at(2));

        //Close resources
        H5Dclose(dset1);
        H5Dclose(dset2);
        H5Dclose(dset3a);
        H5Dclose(dset3b);

        H5Sclose(space1);
        H5Sclose(space2);
        H5Sclose(space3a);
        H5Sclose(space3b);

        dataGroup.get()->close();
        timeGroup1.get()->close();
        timeGroup2.get()->close();
        timeGroup3.get()->close();

        //Free units
        free(rData1[0][0].units);
        free(rData1[0][1].units);
        free(rData2[0][0].units);
        free(rData3a[0][0].units);
        free(rData3b[0][0].units);

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
    BOOST_REQUIRE_EQUAL(2, h5Group.get()->getNumObjs());
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
    LWRComponent component;
    LWRComponent newComponent;
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Setup LWRData
    std::string feature1 = "Feature 1";
    std::string feature2 = "Feature 2";
    double time1= 1.0, time2 = 3.0, time3 = 3.5;
    std::vector<double> position1;
    std::vector<double> position2;
    std::vector<double> position3;
    std::vector<double> position4;
    std::vector<double> position5;

    //Exceptions
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

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
    position4.push_back(0.0);
    position4.push_back(1.0);
    position4.push_back(3.0);


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
    component.addData(data1, time1);
    component.addData(data2, time1);
    component.addData(data3, time2);
    component.addData(data4, time3);
    component.addData(data5, time3);

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);

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
