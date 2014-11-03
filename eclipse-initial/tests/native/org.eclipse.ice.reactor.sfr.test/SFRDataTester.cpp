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
#include <SFRData.h>
#include <string>
#include <vector>
#include <float.h>
#include <memory>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE (SFRDataTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code

	// Initialize a new SFRData
	SFRData data;

	// Default values:
	std::string feature = "Feature 1";

	std::vector<double> position = { 0.0, 0.0, 0.0 };

	double uncertainty = 0.0;
	std::string units = "seconds";
	double value = 0.0;

	/* ---- Check nullary constructor. ---- */

	BOOST_REQUIRE_EQUAL(feature, data.getFeature());
	BOOST_REQUIRE(position == data.getPosition());
	BOOST_REQUIRE_EQUAL(uncertainty, data.getUncertainty());
	BOOST_REQUIRE_EQUAL(units, data.getUnits());
	BOOST_REQUIRE_EQUAL(value, data.getValue());

	/* ------------------------------------ */

	/* ---- Check feature-based constructor. ---- */

	data = SFRData("Infinite Improbability Device");

	BOOST_REQUIRE_EQUAL("Infinite Improbability Device", data.getFeature());
	BOOST_REQUIRE(position == data.getPosition());
	BOOST_REQUIRE_EQUAL(uncertainty, data.getUncertainty());
	BOOST_REQUIRE_EQUAL(units, data.getUnits());
	BOOST_REQUIRE_EQUAL(value, data.getValue());
	/* ------------------------------------------ */

	/* ---- Check invalid constructor (empty string). ---- */
	data = SFRData("");

	BOOST_REQUIRE_EQUAL(feature, data.getFeature());
	BOOST_REQUIRE(position == data.getPosition());
	BOOST_REQUIRE_EQUAL(uncertainty, data.getUncertainty());
	BOOST_REQUIRE_EQUAL(units, data.getUnits());
	BOOST_REQUIRE_EQUAL(value, data.getValue());

	/* ------------------------------------------- */

	/* ---- Check invalid constructor (blank). ---- */
	data = SFRData("   ");

	BOOST_REQUIRE_EQUAL(feature, data.getFeature());
	BOOST_REQUIRE(position == data.getPosition());
	BOOST_REQUIRE_EQUAL(uncertainty, data.getUncertainty());
	BOOST_REQUIRE_EQUAL(units, data.getUnits());
	BOOST_REQUIRE_EQUAL(value, data.getValue());
	/* -------------------------------------------- */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkValue) {
	// begin-user-code

	// Initialize a new SFRData.
	SFRData data;

	// Check the default value.
	double value = 0.0;
	BOOST_REQUIRE_EQUAL(value, data.getValue());
	data.setValue(0.0);
	BOOST_REQUIRE_EQUAL(value, data.getValue());

	// Update the value.
	value = 42.0;
	data.setValue(42.0);
	BOOST_REQUIRE_EQUAL(value, data.getValue());

	// Try setting it to some big values.
	value = DBL_MAX;
	data.setValue(DBL_MAX);
	BOOST_REQUIRE_EQUAL(value, data.getValue());

	// Try setting it to some really small values.
	value = DBL_MIN;
	data.setValue(DBL_MIN);
	BOOST_REQUIRE_EQUAL(value, data.getValue());

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkUncertainty) {
	// begin-user-code

	// Initialize a new SFRData.
	SFRData data;

	// Check the default uncertainty.
	double uncertainty = 0.0;
	BOOST_REQUIRE_EQUAL(uncertainty, data.getUncertainty());
	data.setUncertainty(0.0);
	BOOST_REQUIRE_EQUAL(uncertainty, data.getUncertainty());

	// Update the value.
	uncertainty = 42.0;
	data.setUncertainty(42.0);
	BOOST_REQUIRE_EQUAL(uncertainty, data.getUncertainty());

	// Try setting it to some big values.
	uncertainty = DBL_MAX;
	data.setUncertainty(DBL_MAX);
	BOOST_REQUIRE_EQUAL(uncertainty, data.getUncertainty());

	// Try setting it to some really small values.
	uncertainty = DBL_MIN;
	data.setUncertainty(DBL_MIN);
	BOOST_REQUIRE_EQUAL(uncertainty, data.getUncertainty());

	return;
// end-user-code
}

BOOST_AUTO_TEST_CASE(checkUnits) {
	// begin-user-code

	// Initialize a new SFRData.
	SFRData data;

	std::string defaultstring = "seconds";
	std::string normalstring = "minutes";
	std::string emptystring = "";
	std::string whitespacestring = "  	";
	std::string normalstringPlus = " minutes 	";

	// Check the default.
	BOOST_REQUIRE_EQUAL(defaultstring, data.getUnits());

	// Check setting it to an empty string.
	data.setUnits(emptystring);
	BOOST_REQUIRE_EQUAL(defaultstring, data.getUnits());

	// Check setting it to a string with whitespace only.
	data.setUnits(whitespacestring);
	BOOST_REQUIRE_EQUAL(defaultstring, data.getUnits());

	// Check setting it to a normal string.
	data.setUnits(normalstring);
	BOOST_REQUIRE_EQUAL(normalstring, data.getUnits());

	// Revert it to default temporarily.
	data.setUnits(defaultstring);
	BOOST_REQUIRE_EQUAL(defaultstring, data.getUnits());

	// Check setting it to a normal string with whitespace.
	data.setUnits(normalstringPlus);
	BOOST_REQUIRE_EQUAL(normalstring, data.getUnits());

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkFeature) {
	// begin-user-code

	// Initialize a new SFRdata.
	SFRData data;

	std::string defaultstring = "Feature 1";
	std::string normalstring = "Dimensions Traversed";
	std::string emptystring = "";
	std::string whitespacestring = "  	";
	std::string normalstringPlus = " Dimensions Traversed 	";

	// Check the default.
	BOOST_REQUIRE_EQUAL(defaultstring, data.getFeature());

	// Check setting it to an empty string.
	data.setFeature(emptystring);
	BOOST_REQUIRE_EQUAL(defaultstring, data.getFeature());

	// Check setting it to a string with whitespace only.
	data.setFeature(whitespacestring);
	BOOST_REQUIRE_EQUAL(defaultstring, data.getFeature());

	// Check setting it to a normal string.
	data.setFeature(normalstring);
	BOOST_REQUIRE_EQUAL(normalstring, data.getFeature());

	// Revert it to default temporarily.
	data.setFeature(defaultstring);
	BOOST_REQUIRE_EQUAL(defaultstring, data.getFeature());

	// Check setting it to a normal string with whitespace.
	data.setFeature(normalstringPlus);
	BOOST_REQUIRE_EQUAL(normalstring, data.getFeature());

	return;
// end-user-code
}
BOOST_AUTO_TEST_CASE(checkPosition) {

	// begin-user-code

	// Initialize a new SFRdata
	std::unique_ptr<SFRData> data (new SFRData());

	// Initialize the default position.
	std::vector<double> defaultPosition;
	defaultPosition.push_back(0.0);
	defaultPosition.push_back(0.0);
	defaultPosition.push_back(0.0);

	// Make sure the default position is set.
	BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
	BOOST_REQUIRE_EQUAL(defaultPosition.at(0), data.get()->getPosition().at(0));
	BOOST_REQUIRE_EQUAL(defaultPosition.at(1), data.get()->getPosition().at(1));
	BOOST_REQUIRE_EQUAL(defaultPosition.at(2), data.get()->getPosition().at(2));

	// Initialize a non-default position.
	std::vector<double> position;

	// Add a position value. The list size is 1 and should not change the
	// data's position.
	position.push_back(1.0);
	data.get()->setPosition(position);
	BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
	BOOST_REQUIRE_EQUAL(defaultPosition.at(0), data.get()->getPosition().at(0));
	BOOST_REQUIRE_EQUAL(defaultPosition.at(1), data.get()->getPosition().at(1));
	BOOST_REQUIRE_EQUAL(defaultPosition.at(2), data.get()->getPosition().at(2));

	// Add a position value. The list size is 2 and should not change the
	// data's position.
	position.push_back(-1.0);
	data.get()->setPosition(position);
	BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
	BOOST_REQUIRE_EQUAL(defaultPosition.at(0), data.get()->getPosition().at(0));
	BOOST_REQUIRE_EQUAL(defaultPosition.at(1), data.get()->getPosition().at(1));
	BOOST_REQUIRE_EQUAL(defaultPosition.at(2), data.get()->getPosition().at(2));

	// Add a position value. The list size is 3 and should change the data's
	// position.
	position.push_back(-1.0);
	data.get()->setPosition(position);
	BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
	BOOST_REQUIRE_EQUAL(position.at(0), data.get()->getPosition().at(0));
	BOOST_REQUIRE_EQUAL(position.at(1), data.get()->getPosition().at(1));
	BOOST_REQUIRE_EQUAL(position.at(2), data.get()->getPosition().at(2));

	// Add a fourth position value. The list size is 4 and should not change
	// the data's position.
	position.push_back(-1.0);
	data.get()->setPosition(position);
	BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
	BOOST_REQUIRE_EQUAL(position.at(0), data.get()->getPosition().at(0));
	BOOST_REQUIRE_EQUAL(position.at(1), data.get()->getPosition().at(1));
	BOOST_REQUIRE_EQUAL(position.at(2), data.get()->getPosition().at(2));

	// Remove the fourth element from our position.
	position.pop_back();
	BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
	BOOST_REQUIRE_EQUAL(position.at(0), data.get()->getPosition().at(0));
	BOOST_REQUIRE_EQUAL(position.at(1), data.get()->getPosition().at(1));
	BOOST_REQUIRE_EQUAL(position.at(2), data.get()->getPosition().at(2));

	// Set it to an empty vector. This should not change the position.
	std::vector<double> emptyVector;
	data.get()->setPosition(emptyVector);
	BOOST_REQUIRE_EQUAL(3, data.get()->getPosition().size());
	BOOST_REQUIRE_EQUAL(position.at(0), data.get()->getPosition().at(0));
	BOOST_REQUIRE_EQUAL(position.at(1), data.get()->getPosition().at(1));
	BOOST_REQUIRE_EQUAL(position.at(2), data.get()->getPosition().at(2));

	return;
// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	SFRData object, equalObject, unequalObject, invalidObject;

	// Some values for loading into the data.
	std::string feature = "intelligence";
	std::string nullFeature;
	std::string units = "mouse brain";
	double value = 1.0;
	double uncertainty = 0.0;
	std::vector<double> position;
	position.push_back(1.0);
	position.push_back(2.0);
	position.push_back(3.0);

	// Set the information for the initial data.
	object = SFRData("Mouse");
	object.setFeature(feature);
	object.setUnits(units);
	object.setValue(value);
	object.setUncertainty(uncertainty);
	object.setPosition(position);

	// Set the information for the equal data.
	equalObject = SFRData("Mouse");
	equalObject.setFeature(feature);
	equalObject.setUnits(units);
	equalObject.setValue(value);
	equalObject.setUncertainty(uncertainty);
	equalObject.setPosition(position);

	// Set the information for the unequal data.
	unequalObject = SFRData("Human");
	unequalObject.setFeature(feature);
	unequalObject.setUnits(units);
	unequalObject.setValue(0.5);
	unequalObject.setUncertainty(0.25);
	unequalObject.setPosition(position);

	// Test reflexivity and symmetry.
	BOOST_REQUIRE(object == object);
	BOOST_REQUIRE(object == equalObject);
	BOOST_REQUIRE(equalObject == object);

	// Test inequality.
	BOOST_REQUIRE(!(object == unequalObject));
	BOOST_REQUIRE(!(unequalObject == object));

	// Test inequality with invalid objects.
	BOOST_REQUIRE(!(object == invalidObject));
	BOOST_REQUIRE(!(invalidObject == object));

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkCopying) {
	// begin-user-code

	// Some defaults to use.
	std::string feature = "Acceleration";
	std::string units = "ft/s/s";
	double value = 32.0;

	// Create a new SFRData with some special features and data
	SFRData object("Bowl of petunias");
	object.setFeature(feature);
	object.setUnits(units);
	object.setValue(value);

	std::vector<double> position;
	position.push_back(42.0);
	position.push_back(42000.0);
	position.push_back(84.0);
	object.setPosition(position);

	// Create objects for copying/cloning.
	SFRData copiedObject;
	std::shared_ptr<IData> clonedObject;

	/* ---- Test copying. ---- */
	// Make sure these are different objects beforehand!
	BOOST_REQUIRE(!(object == copiedObject));

	// Copy the contents of object into copiedObject.
	copiedObject = SFRData(object);

	// Check that they are the same now
	BOOST_REQUIRE(object == copiedObject);

	// Make sure we didn't clobber the old contents of object.
	BOOST_REQUIRE_EQUAL(object.getFeature(), feature);
	BOOST_REQUIRE_EQUAL(object.getUnits(), units);
	BOOST_REQUIRE_EQUAL(object.getValue(), value);
	BOOST_REQUIRE(object.getPosition() == position);
	/* ----------------------- */

	/* ---- Test cloning. ---- */

	// Clone the object.
	clonedObject = object.clone();

	// Check that the clone isn't null
	BOOST_REQUIRE(clonedObject);

	// Check that they are the same
	BOOST_REQUIRE(object == *clonedObject);

	// Make sure we didn't clobber the old contents of object.
	BOOST_REQUIRE_EQUAL(object.getFeature(), feature);
	BOOST_REQUIRE_EQUAL(object.getUnits(), units);
	BOOST_REQUIRE_EQUAL(object.getValue(), value);
	BOOST_REQUIRE(object.getPosition() == position);
	/* ----------------------- */

	return;
// end-user-code
}
BOOST_AUTO_TEST_SUITE_END()
