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
#define BOOST_TEST_MODULE FeatureSetTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <FeatureSet.h>
#include <vector>
#include <memory>
#include <string>
#include <IData.h>

using namespace ICE_Reactor;

BOOST_AUTO_TEST_SUITE(FeatureSetTester_testSuite)



BOOST_AUTO_TEST_CASE(checkConstruction) {

    // begin-user-code

    //Local Declarations
    std::string feature = "pin power";
    std::string nullFeature;
    std::string spaceFeature = " ";
    std::string emptyFeature;

    //Test normal construction
    std::unique_ptr<FeatureSet> featureSet1 (new FeatureSet(feature));
    BOOST_REQUIRE_EQUAL(feature, featureSet1.get()->getName());

    //Invalid construction - null
    std::unique_ptr<FeatureSet> featureSet2 (new FeatureSet(nullFeature));
    BOOST_REQUIRE_EQUAL(emptyFeature, featureSet2.get()->getName()); //Defaults to empty

    //Invalid construction - space
    std::unique_ptr<FeatureSet> featureSet3 (new FeatureSet(spaceFeature));
    BOOST_REQUIRE_EQUAL(emptyFeature, featureSet3.get()->getName()); //Set as empty!

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkData) {

    // begin-user-code

    //Local Declarations
    std::string feature1 = "Feature 1";
    std::string feature2 = "Feature 2";
    std::string nullFeature;
    std::string emptyFeature = " ";

    //Setup Features
    std::shared_ptr<FeatureSet> set1 (new FeatureSet(feature1));
    std::shared_ptr<FeatureSet> set2 (new FeatureSet(nullFeature));
    std::shared_ptr<FeatureSet> set3 (new FeatureSet(emptyFeature));

    //Setup data
    std::shared_ptr<LWRData> data1 (new LWRData(feature1));
    std::shared_ptr<LWRData> data2 (new LWRData(feature1));
    std::shared_ptr<LWRData> data3 (new LWRData(feature2));

    //Try to add data to the feature set
    BOOST_REQUIRE_EQUAL(set1.get()->addIData(data1), true);
    //Check contents
    BOOST_REQUIRE_EQUAL(1, set1.get()->getIData().size());
    BOOST_REQUIRE_EQUAL(data1.get()->operator==(*(set1->getIData().at(0).get())), true);

    //Try to add data to the feature set
    BOOST_REQUIRE_EQUAL(set1.get()->addIData(data2), true);
    //Check contents
    BOOST_REQUIRE_EQUAL(2, set1.get()->getIData().size());
    BOOST_REQUIRE_EQUAL(data1.get()->operator==(*(set1.get()->getIData().at(0).get())), true);
    BOOST_REQUIRE_EQUAL(data2.get()->operator==(*(set1.get()->getIData().at(1).get())), true);

    //Try to add data to the feature set - invalid data
    BOOST_REQUIRE_EQUAL(set1.get()->addIData(data3), false);
    //Check contents - show that only two pieces remain
    BOOST_REQUIRE_EQUAL(2, set1.get()->getIData().size());
    BOOST_REQUIRE_EQUAL(data1.get()->operator==(*(set1.get()->getIData().at(0).get())), true);
    BOOST_REQUIRE_EQUAL(data2.get()->operator==(*(set1.get()->getIData().at(0).get())), true);


    //Check removal
    BOOST_REQUIRE_EQUAL(set1.get()->removeIDataAtIndex(5), false);
    BOOST_REQUIRE_EQUAL(set1.get()->removeIDataAtIndex(-1), false);
    BOOST_REQUIRE_EQUAL(set1.get()->removeIDataAtIndex(1), true);

    //Check size
    BOOST_REQUIRE_EQUAL(1, set1.get()->getIData().size());

    //Add it back
    BOOST_REQUIRE_EQUAL(true, set1.get()->addIData(data2));


    //Show that data can only be removed from the feature set by manipulating the Feature calls directly
    std::vector< std::shared_ptr<IData> > dataList = set1.get()->getIData();
    dataList.erase(dataList.begin());
    //Check contents - show that the item was not removed
    BOOST_REQUIRE_EQUAL(2, set1.get()->getIData().size());
    BOOST_REQUIRE_EQUAL(data1.get()->operator==(*(set1.get()->getIData().at(0).get())), true);

    //Try to setup bad FeatureSets and show that data can not be added

    //Show the behavior of a null feature set
    BOOST_REQUIRE_EQUAL(set2.get()->addIData(data1), false);
    BOOST_REQUIRE_EQUAL(0, set2.get()->getIData().size());
    dataList = set2.get()->getIData();
    dataList.push_back(data1);

    //Show the list can not be manipulated
    BOOST_REQUIRE_EQUAL(0, set2.get()->getIData().size());

    //Show the behavior of a empty string feature set
    BOOST_REQUIRE_EQUAL(set3.get()->addIData(data1), false);
    BOOST_REQUIRE_EQUAL(0, set3.get()->getIData().size());
    dataList = set3.get()->getIData();
    dataList.push_back(data1);

    //Show the list can not be manipulated
    BOOST_REQUIRE_EQUAL(0, set3.get()->getIData().size());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    // Create an Object
    FeatureSet object("Feature 1");

    // Set its data

    std::shared_ptr<LWRData> data1(new LWRData("Feature 1"));
    std::shared_ptr<LWRData> data2(new LWRData("Feature 1"));

    object.addIData(data1);
    object.addIData(data2);

    // Create another FeatureSet to assert Equality with the last
    FeatureSet equalObject("Feature 1");

    // Set its data, equal to object
    for(int i = 0; i < object.getIData().size(); i++) {
        equalObject.addIData(object.getIData().at(i));
    }

    // Create a unEqualObject that is not equal to Object
    FeatureSet unEqualObject("Feature 1");

    // Set its data, not equal to testLWRComponent
    unEqualObject.addIData(equalObject.getIData().at(0));

    // Create a third object to test Transitivity
    FeatureSet transitiveObject("Feature 1");

    //Set it equal
    // Set its data, equal to object
    for(int i = 0; i < object.getIData().size(); i++) {
        transitiveObject.addIData(object.getIData().at(i));
    }

    // Assert that these two LWRComponents are equal
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
        BOOST_FAIL("Equality check failed.  Exiting.");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(object==(equalObject)
                        && object==(equalObject)
                        && object==(equalObject), true);
    BOOST_REQUIRE_EQUAL(!(object==(unEqualObject))
                        && !(object==(unEqualObject))
                        && !(object==(unEqualObject)), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    // Create an Object
    FeatureSet object("Feature 2");
    std::shared_ptr<FeatureSet> clonedObject;

    // Set its data
    std::shared_ptr<LWRData> data1(new LWRData("Feature 2"));
    std::shared_ptr<LWRData> data2(new LWRData("Feature 2"));

    //Add data
    object.addIData(data1);
    object.addIData(data2);

    //Run the copy routine
    FeatureSet copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    clonedObject = object.clone();

    //Remove reference
    FeatureSet clonedObjectRef = *clonedObject.get();

    //Check contents
    BOOST_REQUIRE_EQUAL(object==clonedObjectRef, true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_SUITE_END()
