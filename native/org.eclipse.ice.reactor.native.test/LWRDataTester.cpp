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
#define BOOST_TEST_MODULE LWRDataTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <LWRData.h>
#include <vector>
#include <memory>
#include <string>

using namespace ICE_Reactor;

BOOST_AUTO_TEST_SUITE(LWRDataTester_testSuite)



BOOST_AUTO_TEST_CASE(checkPosition) {

    // begin-user-code

    //Local Declarations
    std::unique_ptr<LWRData> data (new LWRData());
    std::vector<double> position;

    //Setup the nullary constructors default position
    std::vector<double> defaultPosition;
    defaultPosition.push_back(0.0);
    defaultPosition.push_back(0.0);
    defaultPosition.push_back(0.0);

    //Add a position
    position.push_back(1.0);

    //Check setter: one position
    data.get()->setPosition(position);
    //Check value - stays the same
    BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
    BOOST_REQUIRE_EQUAL(defaultPosition.at(0), data.get()->getPosition().at(0));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(1), data.get()->getPosition().at(1));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(2), data.get()->getPosition().at(2));

    //Check setter: two positions
    position.push_back(-1.0);
    data.get()->setPosition(position);

    //Check value
    BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
    BOOST_REQUIRE_EQUAL(defaultPosition.at(0), data.get()->getPosition().at(0));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(1), data.get()->getPosition().at(1));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(2), data.get()->getPosition().at(2));

    //Check setter: three positions
    position.push_back(-1.0);
    data.get()->setPosition(position);

    //Check value - actually works!
    BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
    BOOST_REQUIRE_EQUAL(position.at(0), data.get()->getPosition().at(0));
    BOOST_REQUIRE_EQUAL(position.at(1), data.get()->getPosition().at(1));
    BOOST_REQUIRE_EQUAL(position.at(2), data.get()->getPosition().at(2));

    //Check setter: 4 or more positions
    position.push_back(-1.0);
    data.get()->setPosition(position);
    position.pop_back(); // Remove it for checking

    //Check value - same as 3 positions
    BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
    BOOST_REQUIRE_EQUAL(position.at(0), data.get()->getPosition().at(0));
    BOOST_REQUIRE_EQUAL(position.at(1), data.get()->getPosition().at(1));
    BOOST_REQUIRE_EQUAL(position.at(2), data.get()->getPosition().at(2));

    //Set it back to empty - does not work
    std::vector<double> zeroSize(0);
    data.get()->setPosition(zeroSize);
    //Check value
    BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
    BOOST_REQUIRE_EQUAL(position.at(0), data.get()->getPosition().at(0));
    BOOST_REQUIRE_EQUAL(position.at(1), data.get()->getPosition().at(1));
    BOOST_REQUIRE_EQUAL(position.at(2), data.get()->getPosition().at(2));

    return;
    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkValue) {

    // begin-user-code

    //Local Declarations
    std::unique_ptr<LWRData> data (new LWRData());

    double posValue = 30.0;
    double negValue = -1.0;
    double zeroValue = 0.0;

    //Check setter : positive
    data.get()->setValue(posValue);
    //Check value
    BOOST_REQUIRE_EQUAL(posValue, data.get()->getValue());

    //Check setter : negative
    data.get()->setValue(negValue);
    //Check value
    BOOST_REQUIRE_EQUAL(negValue, data.get()->getValue());

    //Check setter : zero
    data.get()->setValue(zeroValue);
    //Check value
    BOOST_REQUIRE_EQUAL(zeroValue, data.get()->getValue());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkUncertainty) {

    // begin-user-code
    //Local Declarations
    std::unique_ptr<LWRData> data (new LWRData());
    double posValue = 30.0;
    double negValue = -1.0;
    double zeroValue = 0.0;

    //Check setter : positive
    data.get()->setUncertainty(posValue);
    //Check value
    BOOST_REQUIRE_EQUAL(posValue, data.get()->getUncertainty());

    //Check setter : negative
    data.get()->setUncertainty(negValue);
    //Check value
    BOOST_REQUIRE_EQUAL(negValue, data.get()->getUncertainty());

    //Check setter : zero
    data.get()->setUncertainty(zeroValue);
    //Check value
    BOOST_REQUIRE_EQUAL(zeroValue, data.get()->getUncertainty());

    return;
    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkUnits) {

    // begin-user-code

    //Local Declarations
    std::unique_ptr<LWRData> data (new LWRData());

    //Values to check
    std::string normalString = "Bob";
    std::string emptyString = "";
    std::string spaceString = " ";
    std::string spaceString2 = "    ";
    std::string trimableString = "Bob  ";
    std::string nullString;

    //Check normal String
    data.get()->setUnits(normalString);
    //Check value
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getUnits());

    //Check empty String
    data.get()->setUnits(emptyString);
    //Check value - nothing changed
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getUnits());

    //Check space String
    data.get()->setUnits(spaceString);
    //Check value - nothing changed
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getUnits());

    //Check space String
    data.get()->setUnits(spaceString2);
    //Check value - nothing changed
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getUnits());

    //Check trimable String
    data.get()->setUnits(trimableString);
    //Check value - nothing changed
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getUnits());

    //Check null
    data.get()->setUnits(nullString);
    //Check value - nothing changed
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getUnits());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkFeature) {

    // begin-user-code

    //Local Declarations
    std::unique_ptr<LWRData> data (new LWRData());

    //Values to check
    std::string normalString = "Bob";
    std::string emptyString = "";
    std::string spaceString = " ";
    std::string trimableString = "Bob  ";
    std::string nullString;

    //Check normal String
    data->setFeature(normalString);
    //Check value
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getFeature());

    //Check empty String
    data->setFeature(emptyString);
    //Check value - nothing changed
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getFeature());

    //Check space String
    data->setFeature(spaceString);
    //Check value - nothing changed
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getFeature());

    //Check trimable String
    data->setFeature(trimableString);
    //Check value - nothing changed
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getFeature());

    //Check null
    data->setFeature(nullString);
    //Check value - nothing changed
    BOOST_REQUIRE_EQUAL(normalString, data.get()->getFeature());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkConstruction) {

    // begin-user-code

    //Local Declarations

    //Default values for LWRData

    //Setup the defaultPosition data
    std::vector<double> defaultPosition(3);
    defaultPosition.at(0) = 0.0;
    defaultPosition.at(1) = 0.0;
    defaultPosition.at(2) = 0.0;

    //Setup the rest
    double defaultValue = 0.0;
    double defaultUncertainty = 0.0;
    int size = 3;
    std::string defaultUnits = "seconds";
    std::string defaultFeature = "Feature 1";

    std::string newFeature = "newFEATURE";

    //Check Construction of a LWRData.
    //Instantiate LWRData
    std::unique_ptr<LWRData> data (new LWRData());
    //Check default values
    BOOST_REQUIRE_EQUAL(size, data.get()->getPosition().size());
    BOOST_REQUIRE_EQUAL(defaultPosition.at(0), data.get()->getPosition().at(0));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(1), data.get()->getPosition().at(1));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(2), data.get()->getPosition().at(2));
    BOOST_REQUIRE_EQUAL(defaultValue, data.get()->getValue());
    BOOST_REQUIRE_EQUAL(defaultUncertainty, data.get()->getUncertainty());
    BOOST_REQUIRE_EQUAL(defaultUnits, data.get()->getUnits());
    BOOST_REQUIRE_EQUAL(defaultFeature, data.get()->getFeature());

    //Instantiate LWRData - feature
    std::unique_ptr<LWRData> data1(new LWRData(newFeature));
    //Check default values
    BOOST_REQUIRE_EQUAL(size, data1.get()->getPosition().size());
    BOOST_REQUIRE_EQUAL(defaultPosition.at(0), data1.get()->getPosition().at(0));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(1), data1.get()->getPosition().at(1));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(2), data1.get()->getPosition().at(2));
    BOOST_CHECK_EQUAL(defaultValue, data1.get()->getValue());
    BOOST_CHECK_EQUAL(defaultUncertainty, data1.get()->getUncertainty());
    BOOST_CHECK_EQUAL(defaultUnits, data1.get()->getUnits());
    BOOST_CHECK_EQUAL(newFeature, data1.get()->getFeature());

    //Instantiate LWRData - erroneous feature (" ")
    std::unique_ptr<LWRData> data2 (new LWRData(" "));
    //Check default values
    BOOST_REQUIRE_EQUAL(size, data2.get()->getPosition().size());
    BOOST_REQUIRE_EQUAL(defaultPosition.at(0), data2.get()->getPosition().at(0));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(1), data2.get()->getPosition().at(1));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(2), data2.get()->getPosition().at(2));
    BOOST_CHECK_EQUAL(defaultValue, data2.get()->getValue());
    BOOST_CHECK_EQUAL(defaultUncertainty, data2.get()->getUncertainty());
    BOOST_CHECK_EQUAL(defaultUnits, data2.get()->getUnits());
    BOOST_CHECK_EQUAL(defaultFeature, data2.get()->getFeature()); // default when erroneous feature passed

    //Instantiate LWRData - erroneous feature (empty string)
    std::unique_ptr<LWRData> data3(new LWRData(""));
    //Check default values
    BOOST_REQUIRE_EQUAL(size, data3.get()->getPosition().size());
    BOOST_REQUIRE_EQUAL(defaultPosition.at(0), data3.get()->getPosition().at(0));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(1), data3.get()->getPosition().at(1));
    BOOST_REQUIRE_EQUAL(defaultPosition.at(2), data3.get()->getPosition().at(2));
    BOOST_CHECK_EQUAL(defaultValue, data3.get()->getValue());
    BOOST_CHECK_EQUAL(defaultUncertainty, data3.get()->getUncertainty());
    BOOST_CHECK_EQUAL(defaultUnits, data3.get()->getUnits());
    BOOST_CHECK_EQUAL(defaultFeature, data3.get()->getFeature()); // default when erroneous feature passed

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    LWRData object, equalObject, unEqualObject, transitiveObject;
    std::string units = "inches";
    std::string features = "Billy";
    double uncertainty = 5.0;
    double value = 4.0;
    std::vector<double> position;
    position.push_back(2.0);
    position.push_back(-3.0);
    position.push_back(44.0);

    //Setup the Object data

    object.setUnits(units);
    object.setFeature(features);
    object.setPosition(position);
    object.setValue(value);
    object.setUncertainty(uncertainty);

    //Setup equalObject equal to object
    equalObject.setUnits(units);
    equalObject.setFeature(features);
    equalObject.setPosition(position);
    equalObject.setValue(value);
    equalObject.setUncertainty(uncertainty);

    //Setup transitiveObject equal to object
    transitiveObject.setUnits(units);
    transitiveObject.setFeature(features);
    transitiveObject.setPosition(position);
    transitiveObject.setValue(value);
    transitiveObject.setUncertainty(uncertainty);

    // Set its data, not equal to object
    //Does not contain components!

    unEqualObject.setUnits(units);
    unEqualObject.setFeature(features + " asdasd"); //Different feature!
    unEqualObject.setPosition(position);
    unEqualObject.setValue(value);
    unEqualObject.setUncertainty(uncertainty);

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
    if (object==(equalObject)&& equalObject==(transitiveObject)) {
        BOOST_REQUIRE_EQUAL(object==(transitiveObject), true);
    } else {
        BOOST_FAIL("Failed Equality check.  Returning.");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(object==(equalObject) && object==(equalObject) && object==(equalObject), true);
    BOOST_REQUIRE_EQUAL(!(object==(unEqualObject)) && !(object==(unEqualObject)) && !(object==(unEqualObject)), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local declarations
    LWRData object;
    std::shared_ptr <IData> clonedObject;

    //Values
    std::string units = "inches";
    std::string features = "Billy";
    double uncertainty = 5.0;
    double value = 4.0;
    std::vector<double> position;
    position.push_back(2.0);
    position.push_back(-3.0);
    position.push_back(44.0);

    //Setup the Object data
    object.setUnits(units);
    object.setFeature(features);
    object.setPosition(position);
    object.setValue(value);
    object.setUncertainty(uncertainty);

    //Run the copy routine
    LWRData copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    clonedObject = object.clone();

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(*clonedObject.get()), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_SUITE_END()
