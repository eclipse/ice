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
#define BOOST_TEST_MODULE LWRDataProviderTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <LWRDataProvider.h>
#include <string>
#include <vector>
#include <algorithm>
#include <UtilityOperations.h>
#include <LWRData.h>

using namespace ICE_Reactor;

BOOST_AUTO_TEST_SUITE(LWRDataProviderTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {

	//begin-user-code

	//Local Declarations
	LWRDataProvider provider;

	//Check default values
	BOOST_REQUIRE_EQUAL(0.0, provider.getCurrentTime());
	BOOST_REQUIRE_EQUAL(0, provider.getFeatureList().size());
	BOOST_REQUIRE_EQUAL(0, provider.getFeaturesAtCurrentTime().size());
	BOOST_REQUIRE_EQUAL(0, provider.getNumberOfTimeSteps());
	BOOST_REQUIRE_EQUAL("No Source Available", provider.getSourceInfo());
	BOOST_REQUIRE_EQUAL("seconds", provider.getTimeUnits());
	BOOST_REQUIRE_EQUAL(0, provider.getTimes().size());

	//end-user-code

}

BOOST_AUTO_TEST_CASE(checkDataProvider) {

    // begin-user-code

    //Local Declarations
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
    LWRDataProvider component;

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
    LWRDataProvider component2;

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

    return;

    // end-user-code

}
BOOST_AUTO_TEST_SUITE_END()
