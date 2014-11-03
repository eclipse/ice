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
#include <SFRComponent.h>

#include <limits>
#include <map>
#include <memory>
#include <string>
#include <vector>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(SFRComponentTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code

	// We have two constructors to test:
	// new SFRComponent();
	// new SFRComponent(String name);

	std::string nullString;

	// Component variable used for testing the constructor.
	SFRComponent component;

	/* ---- Test the nullary constructor. ---- */
	// Test all of the getters for default values.
	BOOST_REQUIRE_EQUAL("Component 1", component.getName());
	BOOST_REQUIRE_EQUAL(1, component.getId());
	BOOST_REQUIRE_EQUAL("Component 1's Description", component.getDescription());
	BOOST_REQUIRE_EQUAL(0.0, component.getCurrentTime());
	BOOST_REQUIRE_EQUAL(0, component.getNumberOfTimeSteps());
	BOOST_REQUIRE_EQUAL("No Source Information", component.getSourceInfo());
	BOOST_REQUIRE_EQUAL(0, component.getTimes().size());
	BOOST_REQUIRE_EQUAL("seconds", component.getTimeUnits());
	BOOST_REQUIRE_EQUAL(-1, component.getTimeStep(42));
	/* --------------------------------------- */

	/* ---- Test a constructor with a String parameter. ---- */
	component = SFRComponent("Arthur");

	// Test all of the getters for default values.
	BOOST_REQUIRE_EQUAL("Arthur", component.getName());
	BOOST_REQUIRE_EQUAL(1, component.getId());
	BOOST_REQUIRE_EQUAL("Component 1's Description", component.getDescription());
	BOOST_REQUIRE_EQUAL(0.0, component.getCurrentTime());
	BOOST_REQUIRE_EQUAL(0, component.getNumberOfTimeSteps());
	BOOST_REQUIRE_EQUAL("No Source Information", component.getSourceInfo());
	BOOST_REQUIRE_EQUAL(0, component.getTimes().size());
	BOOST_REQUIRE_EQUAL("seconds", component.getTimeUnits());
	BOOST_REQUIRE_EQUAL(-1, component.getTimeStep(3.14));
	/* ----------------------------------------------------- */

	/* ---- Test a constructor with a bad (null) String parameter. ---- */
	component = SFRComponent(nullString);

	// Test all of the getters for default values.
	BOOST_REQUIRE_EQUAL("Component 1", component.getName());
	BOOST_REQUIRE_EQUAL(1, component.getId());
	BOOST_REQUIRE_EQUAL("Component 1's Description", component.getDescription());
	BOOST_REQUIRE_EQUAL(0.0, component.getCurrentTime());
	BOOST_REQUIRE_EQUAL(0, component.getNumberOfTimeSteps());
	BOOST_REQUIRE_EQUAL("No Source Information", component.getSourceInfo());
	BOOST_REQUIRE_EQUAL(0, component.getTimes().size());
	BOOST_REQUIRE_EQUAL("seconds", component.getTimeUnits());
	BOOST_REQUIRE_EQUAL(-1, component.getTimeStep(std::numeric_limits<double>::max()));
	/* ---------------------------------------------------------------- */

	/* ---- Test a constructor with a bad (empty) String parameter. ---- */
	component = SFRComponent("");

	// Test all of the getters for default values.
	BOOST_REQUIRE_EQUAL("Component 1", component.getName());
	BOOST_REQUIRE_EQUAL(1, component.getId());
	BOOST_REQUIRE_EQUAL("Component 1's Description", component.getDescription());
	BOOST_REQUIRE_EQUAL(0.0, component.getCurrentTime());
	BOOST_REQUIRE_EQUAL(0, component.getNumberOfTimeSteps());
	BOOST_REQUIRE_EQUAL("No Source Information", component.getSourceInfo());
	BOOST_REQUIRE_EQUAL(0, component.getTimes().size());
	BOOST_REQUIRE_EQUAL("seconds", component.getTimeUnits());
	BOOST_REQUIRE_EQUAL(-1, component.getTimeStep(std::numeric_limits<double>::min()));
	/* ----------------------------------------------------------------- */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkAttributes) {
	// begin-user-code

	SFRComponent component;

	std::string null;

	// Set name, description, id, sourceInfo and timeUnits attributes
	component.setName("Zaphod");
	component.setDescription("fooDescription");
	component.setId(1);
	component.setSourceInfo("fooSource");
	component.setTimeUnits("fooSeconds");

	// Check the name, description, id, sourceInfo and timeUnits attributes
	BOOST_REQUIRE_EQUAL("Zaphod", component.getName());
	BOOST_REQUIRE_EQUAL("fooDescription", component.getDescription());
	BOOST_REQUIRE_EQUAL(1, component.getId());
	BOOST_REQUIRE_EQUAL("fooSource", component.getSourceInfo());
	BOOST_REQUIRE_EQUAL("fooSeconds", component.getTimeUnits());

	// Set attributes to illegal values
	component.setName(null);
	component.setDescription(null);
	component.setId(-1);
	component.setSourceInfo(null);
	component.setTimeUnits(null);

	// Check attributes, should remain unchanged
	BOOST_REQUIRE_EQUAL("Zaphod", component.getName());
	BOOST_REQUIRE_EQUAL("fooDescription", component.getDescription());
	BOOST_REQUIRE_EQUAL(1, component.getId());
	BOOST_REQUIRE_EQUAL("fooSource", component.getSourceInfo());
	BOOST_REQUIRE_EQUAL("fooSeconds", component.getTimeUnits());

	// Set string attributes to empty strings
	component.setName("");
	component.setDescription("	");
	component.setSourceInfo("   ");
	component.setTimeUnits("");

	// Check string attributes, should remain unchanged
	BOOST_REQUIRE_EQUAL("Zaphod", component.getName());
	BOOST_REQUIRE_EQUAL("fooDescription", component.getDescription());
	BOOST_REQUIRE_EQUAL("fooSource", component.getSourceInfo());
	BOOST_REQUIRE_EQUAL("fooSeconds", component.getTimeUnits());

	// Set string attributes with leading/trailing whitespaces
	component.setName("  Zaphod ");
	component.setDescription(" fooDescription ");
	component.setSourceInfo(" fooSource  ");
	component.setTimeUnits("  fooSeconds  ");

	// Check string attributes for trimmed leading/trailing whitespaces
	BOOST_REQUIRE_EQUAL("Zaphod", component.getName());
	BOOST_REQUIRE_EQUAL("fooDescription", component.getDescription());
	BOOST_REQUIRE_EQUAL("fooSource", component.getSourceInfo());
	BOOST_REQUIRE_EQUAL("fooSeconds", component.getTimeUnits());

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkDataAddRem) {
	// begin-user-code

	// The component that we will be updating for this test.
	SFRComponent component;

	// Some pre-defined features, times, and IData objects.
	std::string featOne = "Two heads";
	std::string featTwo = "Three arms";
	std::string featThree = "Ex-Galactic president";
	double timeOne = 1.5;
	double timeTwo = 3.0;
	double timeThree = 4.5;
	std::shared_ptr<SFRData> dataOne = std::make_shared<SFRData>(featOne);
	std::shared_ptr<SFRData> dataTwo = std::make_shared<SFRData>(featOne);
	std::shared_ptr<SFRData> dataThree = std::make_shared<SFRData>(featTwo);
	std::shared_ptr<SFRData> dataFour = std::make_shared<SFRData>(featThree);

	// Overall data information (these change when we add/remove
	// timesteps/features).
	int numberOfTimeSteps;
	std::vector<double> times;
	std::vector<std::string> featureList;

	// Time-/feature-specific information.
	double currentTime;
	int timeStep;
	std::vector<std::string> featuresAtCurrentTime;
	std::vector<std::shared_ptr<IData>> dataAtCurrentTime;
	std::vector<std::shared_ptr<IData>> emptyData;

	/* ---- Check the overall information. ---- */
	numberOfTimeSteps = 0;

	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList == component.getFeatureList());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	currentTime = 0.0;
	timeStep = -1;

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep.
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Add IData to the component at t=1.5
	component.addData(dataOne, timeOne);

	/* ---- Check the overall information. ---- */
	numberOfTimeSteps++;
	times.push_back(timeOne);
	featureList.push_back(featOne);

	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	std::vector<std::string> v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	BOOST_REQUIRE(currentTime ==component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep.
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Update the current time.
	component.setTime(timeOne);

	/* ---- Check the overall information. ---- */
	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	currentTime = timeOne;
	timeStep = 0;
	featuresAtCurrentTime.push_back(featOne);
	dataAtCurrentTime.push_back(dataOne);

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep.
	BOOST_REQUIRE(dataAtCurrentTime == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Add another piece of IData to the component at t=1.5
	component.addData(dataTwo, timeOne);

	/* ---- Check the overall information. ---- */
	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	dataAtCurrentTime.push_back(dataTwo);

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep.
	BOOST_REQUIRE(dataAtCurrentTime == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Change current time to t=3.0
	component.setTime(timeTwo);

	/* ---- Check the overall information. ---- */
	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	currentTime = timeTwo;
	timeStep = -1;
	featuresAtCurrentTime.clear();

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep.
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Add third piece of IData to component, at current time (t=3.0)
	component.addData(dataThree, timeTwo);

	/* ---- Check the overall information. ---- */
	numberOfTimeSteps++;
	times.push_back(timeTwo);
	featureList.push_back(featTwo);

	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	timeStep = 1;
	featuresAtCurrentTime.push_back(featTwo);
	dataAtCurrentTime.clear(); dataAtCurrentTime.push_back(dataThree);

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep.
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(dataAtCurrentTime == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Add fourth piece of IData to component, at t=4.5
	component.addData(dataFour, timeThree);

	/* ---- Check the overall information. ---- */
	numberOfTimeSteps++;
	times.push_back(timeThree);
	featureList.push_back(featThree);

	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep.
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(dataAtCurrentTime == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Change current time to t=4.5
	component.setTime(timeThree);

	/* ---- Check the overall information. ---- */
	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	currentTime = timeThree;
	timeStep = 2;
	featuresAtCurrentTime.clear(); featuresAtCurrentTime.push_back(featThree);
	dataAtCurrentTime.clear(); dataAtCurrentTime.push_back(dataFour);

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep.
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(dataAtCurrentTime == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Set the time to 1.5 and add dataFour.
	component.setTime(timeOne);
	component.addData(dataFour, timeOne);

	/* ---- Check the overall information. ---- */
	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	currentTime = timeOne;
	timeStep = 0;
	featuresAtCurrentTime.clear();
	featuresAtCurrentTime.push_back(featOne);
	featuresAtCurrentTime.push_back(featThree);

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime.size() == component.getFeaturesAtCurrentTime().size());

	// Make sure the features at the current time are correct.
	v = component.getFeaturesAtCurrentTime();
	for (int i = 0; i < featuresAtCurrentTime.size(); i++)
			BOOST_REQUIRE(std::find(v.begin(), v.end(), featuresAtCurrentTime[i]) != v.end());

	// Check the data for the features at this timestep. (dataOne and
	// dataTwo for featOne, dataFour for featThree)
	dataAtCurrentTime.clear();
	dataAtCurrentTime.push_back(dataOne);
	dataAtCurrentTime.push_back(dataTwo);
	BOOST_REQUIRE(dataAtCurrentTime == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	dataAtCurrentTime.clear();
	dataAtCurrentTime.push_back(dataFour);
	BOOST_REQUIRE(dataAtCurrentTime == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Try to set invalid time
	component.setTime(-1.0);

	// The time should not have changed.
	BOOST_REQUIRE(currentTime == component.getCurrentTime());

	// Remove featThree IData from component
	component.removeDataFromFeature(featThree);

	/* ---- Check the overall information. ---- */
	featureList.pop_back(); // Removes featTree.

	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	featuresAtCurrentTime.clear();
	featuresAtCurrentTime.push_back(featOne);

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep. (dataOne and
	// dataTwo for featOne, now empty for featThree)
	dataAtCurrentTime.clear();
	dataAtCurrentTime.push_back(dataOne);
	dataAtCurrentTime.push_back(dataTwo);
	BOOST_REQUIRE(dataAtCurrentTime == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));

	// Update the current time so we can check timeThree's data.
	component.setTime(timeThree);

	currentTime = timeThree;
	timeStep = 2;
	featuresAtCurrentTime.clear();

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Remove featTwo IData from component
	component.removeDataFromFeature(featTwo);

	/* ---- Check the overall information. ---- */
	featureList.pop_back(); // Removes featTwo

	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	// Change time to t=3.0
	component.setTime(timeTwo);

	/* ---- Check the time-/feature-specific information. ---- */
	currentTime = timeTwo;
	timeStep = 1;
	featuresAtCurrentTime.clear();

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep.
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	// Remove featOne IData from component
	component.removeDataFromFeature(featOne);

	/* ---- Check the overall information. ---- */
	featureList.pop_back(); // Removes featOne

	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	// Change time to t=1.5
	component.setTime(timeOne);

	/* ---- Check the overall information. ---- */
	BOOST_REQUIRE(numberOfTimeSteps == component.getNumberOfTimeSteps());
	BOOST_REQUIRE(times == component.getTimes());
	BOOST_REQUIRE(featureList.size() == component.getFeatureList().size());

	// Make sure everything in our vector of features is in the vector returned
	// by component.getFeatureList();
	v = component.getFeatureList();
	for (int i = 0; i < featureList.size(); i++)
		BOOST_REQUIRE(std::find(v.begin(), v.end(), featureList[i]) != v.end());
	/* ---------------------------------------- */

	/* ---- Check the time-/feature-specific information. ---- */
	currentTime = timeOne;
	timeStep = 0;
	featuresAtCurrentTime.clear();

	BOOST_REQUIRE(currentTime == component.getCurrentTime());
	BOOST_REQUIRE(timeStep == component.getTimeStep(currentTime));
	BOOST_REQUIRE(featuresAtCurrentTime == component.getFeaturesAtCurrentTime());

	// Check the data for the features at this timestep.
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featOne));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featTwo));
	BOOST_REQUIRE(emptyData == component.getDataAtCurrentTime(featThree));
	/* ------------------------------------------------------- */

	/* ---- Check adding data in a different order. ---- */

	// Initialize three components: one component, one which will have data
	// added in the same order, and one with data added in another order.
	component = SFRComponent();
	SFRComponent sameOrder = SFRComponent();
	SFRComponent diffOrder = SFRComponent();

	// At this point, they are all the same.
	BOOST_REQUIRE(component == sameOrder);
	BOOST_REQUIRE(component == diffOrder);

	// Create some data with different features to add.
	std::shared_ptr<SFRData> data1 = std::make_shared<SFRData>("feature1");
	std::shared_ptr<SFRData> data2 = std::make_shared<SFRData>("feature2");
	std::shared_ptr<SFRData> data3 = std::make_shared<SFRData>("feature3");
	std::shared_ptr<SFRData> data4 = std::make_shared<SFRData>("feature4");

	// Add the data to the component.
	component.addData(data1, 0.0);
	component.addData(data2, 0.0);
	component.addData(data3, 0.0);
	component.addData(data4, 0.0);

	// Add the data in the same order to another component.
	sameOrder.addData(data1, 0.0);
	sameOrder.addData(data2, 0.0);
	sameOrder.addData(data3, 0.0);
	sameOrder.addData(data4, 0.0);

	// Add the data in a different order to another component.
	diffOrder.addData(data1, 0.0);
	diffOrder.addData(data4, 0.0);
	diffOrder.addData(data2, 0.0);
	diffOrder.addData(data3, 0.0);

	// Check the list of feature names.
	BOOST_REQUIRE(component.getFeatureList() == sameOrder.getFeatureList());
	BOOST_REQUIRE(component.getFeatureList() == diffOrder.getFeatureList());

	// Check the list of feature names at the current time.
	BOOST_REQUIRE(component.getFeaturesAtCurrentTime() == sameOrder.getFeaturesAtCurrentTime());
	BOOST_REQUIRE(component.getFeaturesAtCurrentTime() == diffOrder.getFeaturesAtCurrentTime());

	// Check the contents of the feature at the current time.
	for (int i = 1; i <= 4; i++) {
		std::string feature = "feature" + i;

		// Get the data in component.
		std::vector<std::shared_ptr<IData> > d = component.getDataAtCurrentTime(feature);

		// Check the size and contents.

		// Make sure all of the data pointers point the same thing with sameOrder.
		BOOST_REQUIRE(component.getDataAtCurrentTime(feature).size() == sameOrder.getDataAtCurrentTime(feature).size());
		std::vector<std::shared_ptr<IData> > d2 = sameOrder.getDataAtCurrentTime(feature);
		for (int i = 0; i < d2.size(); i++)
			BOOST_REQUIRE(std::find(d.begin(), d.end(), d2[i]) != d.end());

		// Make sure all of the data pointers point the same thing with diffOrder.
		BOOST_REQUIRE(component.getDataAtCurrentTime(feature).size() == diffOrder.getDataAtCurrentTime(feature).size());
		d2 = diffOrder.getDataAtCurrentTime(feature);
		for (int i = 0; i < d2.size(); i++)
			BOOST_REQUIRE(std::find(d.begin(), d.end(), d2[i]) != d.end());
	}

	// The components should all still be equal!
	BOOST_REQUIRE(component == sameOrder);
	BOOST_REQUIRE(component == diffOrder);
	/* ------------------------------------------------- */

	return;
	// end-user-code
}

// Visitation routines are not used in the C++ implementation, however this is
// left here just in case.

//BOOST_AUTO_TEST_CASE(checkVisitation) {
//	// begin-user-code
//
//
//
//	// Create a new SFRComponent to visit.
//	SFRComponent component = new SFRComponent("Marvin");
//
//	// Create a fake visitor that stores a reference to the visited
//	// IReactorComponent.
//	FakeComponentVisitor visitor = null;
//
//	// Try accepting an invalid visitor.
//	component.accept(visitor);
//
//	// Initialize the visitor and get its current component (null).
//	visitor = new FakeComponentVisitor();
//	Object visitedObject = visitor.getComponent();
//
//	// Make sure the component and the [un]visited object are not equal.
//	assertFalse(component.equals(visitedObject));
//
//	// Try accepting the fake component visitor.
//	component.accept(visitor);
//	visitedObject = visitor.getComponent();
//
//	// See if the visited component from the visitor is the same component
//	// that we initially created.
//	assertTrue(component == visitedObject);
//	assertTrue(component.equals(visitedObject));
//
//	return;
//	// end-user-code
//}

BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	// Construct a component to test against
	SFRComponent component("Earth");
	component.setDescription("Mostly Harmless");
	component.setId(10);

	// Construct a component equal to the first
	SFRComponent equalComponent("Earth");
	equalComponent.setDescription("Mostly Harmless");
	equalComponent.setId(10);

	// Construct a component not equal to the first
	SFRComponent unequalComponent("Betelgeuse 5");
	unequalComponent.setDescription("Suspiciously Shifty");
	unequalComponent.setId(5);

	// Check that component and unequalComponet are not the same
	BOOST_REQUIRE_EQUAL(false, component == unequalComponent);
	BOOST_REQUIRE_EQUAL(false, unequalComponent == component);

	// Check is equals() is reflexive and symmetric
	BOOST_REQUIRE_EQUAL(true, component == component);
	BOOST_REQUIRE_EQUAL(true, component == equalComponent && equalComponent == component);

	// Construct a component equal to the first, for testing transitivity
	SFRComponent transComponent("Earth");
	transComponent.setDescription("Mostly Harmless");
	transComponent.setId(10);

	// Check equals is transitive()
	if (component == transComponent && transComponent == equalComponent)
		BOOST_REQUIRE_EQUAL(true, component == equalComponent);
	else
		BOOST_FAIL("SFRComponentTester: == is not transitive");

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkCopying) {
	// begin-user-code

	// Define an object, a copy, and a clone.
	SFRComponent object("Ford Prefect");
	SFRComponent objectCopy;
	std::shared_ptr<SFRComponent> objectClone;

	// Set up the object.
	object.setDescription("Out-of-work actor from Guildford");
	object.setId(30);

	/* ---- Check copying. ---- */
	// Make sure the object is not the same as its copy.
	BOOST_REQUIRE_EQUAL(false, object == objectCopy);

	// Copy the object.
	objectCopy = SFRComponent(object);

	// Make sure the object is now the same as its copy.
	BOOST_REQUIRE_EQUAL(true, object == objectCopy);
	/* ------------------------ */

	/* ---- Check cloning. ---- */
	// Clone the object.
	objectClone = std::dynamic_pointer_cast<SFRComponent>(object.clone());

	// Make sure the object is now the same as its clone.
	BOOST_REQUIRE_EQUAL(true, object == *objectClone);
	/* ------------------------ */

	return;
	// end-user-code
}

// Notification routines are not used in the C++ implementation, however this is
// left here just in case.

//BOOST_AUTO_TEST_CASE(checkNotifications) {
//	// begin-user-code
//
//	// Create a new component to listen to.
//	SFRComponent component = new SFRComponent("Tricia McMillan");
//	SFRComponent component2 = new SFRComponent();
//
//	// All methods that should notify listeners:
//	// component.addData(SFRData, double)
//	// component.copy(SFRComponent)
//	// component.removeDataFromFeature(String)
//	// component.setDescription(String)
//	// component.setId(int)
//	// component.setName(String)
//	// component.setSourceInfo(String)
//	// component.setTime(double)
//	// component.setTimeUnits(String)
//
//	// All of these methods are tested at some point below.
//
//	// Create a test listener and register it with the component.
//	TestComponentListener listener1 = new TestComponentListener();
//	component.register(listener1);
//
//	// The setter should notify the only listener.
//	component.addData(new SFRData("Heart of Gold"), 1.5);
//	assertTrue(listener1.wasNotified());
//	listener1.reset();
//
//	// The setter should notify the only listener.
//	component.setDescription("Arthur's love interest?");
//	assertTrue(listener1.wasNotified());
//	listener1.reset();
//
//	// The setter should notify the only listener.
//	component.setId(42);
//	assertTrue(listener1.wasNotified());
//	listener1.reset();
//
//	// The setter should notify the only listener.
//	component.setName("Trillian");
//	assertTrue(listener1.wasNotified());
//	listener1.reset();
//
//	// Create a new listener and register it with the original component.
//	TestComponentListener listener2 = new TestComponentListener();
//	component.register(listener2);
//
//	// The setter should notify both listeners.
//	component.setSourceInfo("Earth");
//	assertTrue(listener1.wasNotified());
//	listener1.reset();
//	assertTrue(listener2.wasNotified());
//	listener2.reset();
//
//	// The setter should notify both listeners.
//	component.setTime(1.5);
//	assertTrue(listener1.wasNotified());
//	listener1.reset();
//	assertTrue(listener2.wasNotified());
//	listener2.reset();
//
//	// The copy operation should notify both listeners.
//	component2.copy(component);
//	assertTrue(listener1.wasNotified());
//	listener1.reset();
//	assertTrue(listener2.wasNotified());
//	listener2.reset();
//
//	// Create a new listener and register it with the component copy.
//	TestComponentListener listener3 = new TestComponentListener();
//	component2.register(listener3);
//
//	// The component copy should notify listeners 1, 2, and 3.
//	component2.setTimeUnits("tea");
//	assertTrue(listener1.wasNotified());
//	listener1.reset();
//	assertTrue(listener2.wasNotified());
//	listener2.reset();
//	assertTrue(listener3.wasNotified());
//	listener3.reset();
//
//	// Test removeDataFromFeature. The original component should only notify
//	// listeners 1 and 2.
//	component.removeDataFromFeature("Heart of Gold");
//	assertTrue(listener1.wasNotified());
//	listener1.reset();
//	assertTrue(listener2.wasNotified());
//	listener2.reset();
//
//	// Listener3 is NOT supposed to be notified. However, attempts to test
//	// this behavior have proved unsuccessful: the CountDownLatch.await()
//	// operation in listener.wasNotified() does not always wait, so we can
//	// never tell if it succeeded or if JUnit decided it should not run.
////		assertFalse(listener3.wasNotified());
////		listener3.reset();
//
//	return;
//	// end-user-code
//}
BOOST_AUTO_TEST_SUITE_END()
