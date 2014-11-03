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
#define BOOST_TEST_DYN_LINK
#define BOOST_TEST_MODULE Regression
#include <boost/test/included/unit_test.hpp>
#include <FeatureSet.h>
#include <SFRData.h>
#include <memory>
#include <string>
#include <vector>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(FeatureSetTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code

	// Default, invalid, and valid arguments.
	std::string defaultFeature;
	std::string emptyFeature = " ";
	std::string nullFeature;
	std::string feature = "Infinite Improbability Drive";

	// Check the defaults for the constructor with an invalid String.
	FeatureSet featureSet(nullFeature);
	BOOST_REQUIRE_EQUAL(defaultFeature, featureSet.getName());
	BOOST_REQUIRE_EQUAL(0, featureSet.getData().size());

	// Check the values for a constructor with an invalid whitespace String.
	featureSet = FeatureSet(emptyFeature);
	BOOST_REQUIRE_EQUAL(defaultFeature, featureSet.getName());
	BOOST_REQUIRE_EQUAL(0, featureSet.getData().size());

	// Check the values for a constructor with a valid name.
	featureSet = FeatureSet(feature);
	BOOST_REQUIRE_EQUAL(feature, featureSet.getName());
	BOOST_REQUIRE_EQUAL(0, featureSet.getData().size());

	return;
	// end-user-code

}

BOOST_AUTO_TEST_CASE(checkData) {
	// begin-user-code

	// Some features.
	std::string sameFeature = "Slartibartfast";
	std::string diffFeature = "Almighty Bob";
	std::string nullFeature;

	// Some data using the features.
	std::shared_ptr < SFRData > sameData1 = std::make_shared<SFRData>(sameFeature);
	std::shared_ptr < SFRData > sameData2 = std::make_shared<SFRData>(sameFeature);
	std::shared_ptr < SFRData > diffData = std::make_shared<SFRData>(diffFeature);
	std::shared_ptr < SFRData > nullData;

	sameData1->setValue(3.1415926);
	diffData->setUncertainty(1.41421);

	/* ---- Test a normal FeatureSet. ---- */
	// Construct a normal FeatureSet.
	std::unique_ptr < FeatureSet > featureSet(new FeatureSet(sameFeature));

	// Add some data for that feature.
	BOOST_REQUIRE(featureSet->addIData(sameData1));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(1, featureSet->getData().size());
	BOOST_REQUIRE(*sameData1 == *featureSet->getData()[0]);

	// Add some more data.
	BOOST_REQUIRE(featureSet->addIData(sameData2));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(2, featureSet->getData().size());
	BOOST_REQUIRE(*sameData1 == *featureSet->getData()[0]);
	BOOST_REQUIRE(*sameData2 == *featureSet->getData()[1]);

	// Try adding the same data again.
	BOOST_REQUIRE(featureSet->addIData(sameData1));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(3, featureSet->getData().size());
	BOOST_REQUIRE(*sameData1 == *featureSet->getData()[0]);
	BOOST_REQUIRE(*sameData2 == *featureSet->getData()[1]);
	BOOST_REQUIRE(*sameData1 == *featureSet->getData()[2]);

	// Add some data for a different feature.
	BOOST_REQUIRE(!featureSet->addIData(diffData));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(3, featureSet->getData().size());
	BOOST_REQUIRE(*sameData1 == *featureSet->getData()[0]);
	BOOST_REQUIRE(*sameData2 == *featureSet->getData()[1]);
	BOOST_REQUIRE(*sameData1 == *featureSet->getData()[2]);

	// Try adding a null IData.
	BOOST_REQUIRE(!featureSet->addIData(nullData));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(3, featureSet->getData().size());
	BOOST_REQUIRE(*sameData1 == *featureSet->getData()[0]);
	BOOST_REQUIRE(*sameData2 == *featureSet->getData()[1]);
	BOOST_REQUIRE(*sameData1 == *featureSet->getData()[2]);
	/* ----------------------------------- */

	/* ---- Test a FeatureSet with an invalid name. ---- */
	// Construct an invalid FeatureSet.
	featureSet.reset(new FeatureSet(nullFeature));

	// Add some data for that feature.
	BOOST_REQUIRE(!featureSet->addIData(sameData1));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(0, featureSet->getData().size());

	// Add some more data.
	BOOST_REQUIRE(!featureSet->addIData(sameData2));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(0, featureSet->getData().size());

	// Try adding the same data again.
	BOOST_REQUIRE(!featureSet->addIData(sameData1));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(0, featureSet->getData().size());

	// Add some data for a different feature.
	BOOST_REQUIRE(!featureSet->addIData(diffData));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(0, featureSet->getData().size());

	// Try adding a null IData.
	BOOST_REQUIRE(!featureSet->addIData(nullData));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(0, featureSet->getData().size());

	// Try adding a new SFRData based off the FeatureSet's name.
	std::shared_ptr<SFRData> sameData3 = std::make_shared<SFRData>(featureSet->getName());
	BOOST_REQUIRE(!featureSet->addIData(sameData3));
	// Check the contents.
	BOOST_REQUIRE_EQUAL(0, featureSet->getData().size());
	/* ------------------------------------------------- */

	return;
	// end-user-code

}

BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	// Feature names to use.
	std::string feature = "Deep Thought";
	std::string equalFeature = "   Deep Thought  ";
	std::string unequalFeature =
			"Answer to the Ultimate Question of Life, the Universe, and Everything";
	std::string nullFeature;

	// FeatureSets to test.
	std::unique_ptr<FeatureSet> object(new FeatureSet(feature));
	std::unique_ptr<FeatureSet> equalObject(new FeatureSet(equalFeature));
	std::unique_ptr<FeatureSet> unequalObject(new FeatureSet(unequalFeature));
	std::unique_ptr<FeatureSet> invalidObject(new FeatureSet(nullFeature));
	std::unique_ptr<FeatureSet> invalidObject2(new FeatureSet(nullFeature));

	// Populate the FeatureSets with some IData.
	for (int i = 0; i < 10; i++) {
		// Data added to the object FeatureSet.
		std::shared_ptr < SFRData > iData(new SFRData(feature));
		iData->setValue((double) i);
		iData->setUncertainty(i / 10.0);
		object->addIData(iData);

		// Also add the data to the invalid FeatureSet.
		invalidObject->addIData(iData);

		// Data added to the equal FeatureSet.
		iData.reset(new SFRData(equalFeature));
		iData->setValue((double) i);
		iData->setUncertainty(i / 10.0);
		equalObject->addIData(iData);

		// Data added to the unequal FeatureSet.
		iData.reset(new SFRData(unequalFeature));
		iData->setValue((double) i);
		iData->setUncertainty(i / 10.0);
		unequalObject->addIData(iData);
	}

	// Test reflexivity and symmetry.
	BOOST_REQUIRE_EQUAL(true, *object == *object);
	BOOST_REQUIRE_EQUAL(true, *object == *equalObject);
	BOOST_REQUIRE_EQUAL(true, *equalObject == *object);

	// Test inequality.
	BOOST_REQUIRE_EQUAL(false, *object == *unequalObject);
	BOOST_REQUIRE_EQUAL(false, *unequalObject == *object);

	// Test inequality with invalid objects.
	BOOST_REQUIRE_EQUAL(false, *object == *invalidObject);
	BOOST_REQUIRE_EQUAL(false, *invalidObject == *object);

	// Two invalid objects should be equal!
	BOOST_REQUIRE_EQUAL(true, *invalidObject == *invalidObject);
	BOOST_REQUIRE_EQUAL(true, *invalidObject == *invalidObject2);
	BOOST_REQUIRE_EQUAL(true, *invalidObject2 == *invalidObject);

	return;
	// end-user-code

}

BOOST_AUTO_TEST_CASE(checkCopying) {
	// begin-user-code

	// Some feature names.
	std::string feature = "Blart Versenwald III";
	std::string otherFeature = "Not Blart Versenwald III";

	// Some objects to test copying/cloning.
	FeatureSet object(feature);
	FeatureSet copiedObject(otherFeature);
	std::shared_ptr<FeatureSet> clonedObject;

	// A vector to keep track of the original object's data.
	std::vector<std::shared_ptr<IData> > data;

	// Populate the FeatureSets with some IData.
	for (int i = 0; i < 10; i++) {
		// Data added to the object FeatureSet.
		std::shared_ptr < SFRData > iData = std::make_shared<SFRData>(feature);
		iData->setValue((double) i);
		iData->setUncertainty(i / 10.0);
		object.addIData(iData);
		data.push_back(iData);

		// Data added to the copiedObject FeatureSet.
		iData.reset(new SFRData(otherFeature));
		iData->setValue((double) i / 10.0);
		iData->setUncertainty(i);
		copiedObject.addIData(iData);
	}

	/* ---- Test copying. ---- */
	// Make sure these are different objects beforehand!
	BOOST_REQUIRE_EQUAL(false, object == copiedObject);

	// Copy the contents of object into copiedObject.
	copiedObject = FeatureSet(object);

	// Make sure these are the same.
	BOOST_REQUIRE_EQUAL(true, object == copiedObject);

	// Make sure we didn't clobber the old contents of object.
	BOOST_REQUIRE_EQUAL(true, feature == object.getName());
	BOOST_REQUIRE_EQUAL(true, data == object.getData());
	/* ----------------------- */

	/* ---- Test cloning. ---- */
	// Clone the object.
	clonedObject = object.clone();

	// Make sure they are now the same object.
	BOOST_REQUIRE_EQUAL(true, object == *clonedObject);

	// Make sure we didn't clobber the old contents of object.
	BOOST_REQUIRE_EQUAL(true, feature == object.getName());
	BOOST_REQUIRE_EQUAL(true, data == object.getData());
	/* ----------------------- */
	return;
	// end-user-code
}
BOOST_AUTO_TEST_SUITE_END()
