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
#define BOOST_TEST_MODULE UpdaterTester_testSuite

#include <fstream>
#include <sstream>
#include <iostream>
#include <LibcurlUtils.h>
#include "TesterUtils.h"
#include <boost/test/included/unit_test.hpp>
#include <boost/shared_ptr.hpp>

using namespace std;
using namespace boost;

/**
 * A LibcurlUtilsPtr is a shared pointer object referring to a LibcurlUtils instance.
 */
typedef shared_ptr<LibcurlUtils> LibcurlUtilsPtr;

struct TestStruct {

	/**
	 * An instance of LibcurlUtilsPtr used for testing.
	 */
	LibcurlUtilsPtr libcurlUtilsPtr;

	/**
	 * A standard template library map container keyed on TesterPropertyType.
	 */
	TesterPropertyMap testerPropertyMap;

	TestStruct() {

		//Construct a new LibcurlUtils instance.
		libcurlUtilsPtr = LibcurlUtilsPtr(new LibcurlUtils());

		//Get the contents of updatertests.properties as a string.
		string contents = TesterUtils::getLocalFileContents(
				"updatertests.properties");

		//Create the tester property map.
		testerPropertyMap = TesterUtils::getTesterPropertyMap(contents);

		//If the value for IGNORE_SSL_PEER_VERIFICATION exists and is "true"
		if (testerPropertyMap.find(IGNORE_SSL_PEER_VERIFICATION)
				!= testerPropertyMap.end()
				&& testerPropertyMap.at(IGNORE_SSL_PEER_VERIFICATION) == "true") {

			//Then set the ignoreSslPeerVerification flag to true
			libcurlUtilsPtr->setIgnoreSslPeerVerification(true);
		}

		return;
	}

	~TestStruct() {}

};


BOOST_FIXTURE_TEST_SUITE(LibcurlUtilsTester_testSuite,TestStruct)

/**
 * Checks the LibcurlUtils get() operation.
 */
BOOST_AUTO_TEST_CASE(checkGet) {

	//Create a string to hold the full url to the SampleText.txt file
	string urlFilePath = testerPropertyMap.at(URL_PATH);

	//Use libcurlUtils to get the contents of the file at urlPath.
	string urlContents = libcurlUtilsPtr->get(urlFilePath, testerPropertyMap.at(USER_NAME), testerPropertyMap.at(PASS_WORD));

	//Verify that get returns the same file contents.
	BOOST_REQUIRE_EQUAL(urlContents,"1");

	return;
}

/**
 * Checks the LibcurlUtils post() operation.
 */
BOOST_AUTO_TEST_CASE(checkPost) {

	// Create a test stream
	stringstream stream;
	stream << "{\"item_id\":\"5\", "
		<< "\"client_key\":\"1234567890ABCDEFGHIJ1234567890ABCDEFGHIJ\", "
		<< "\"posts\":[{\"type\":\"UPDATER_STARTED\",\"message\":\"\"},"
		<< "{\"type\":\"FILE_MODIFIED\","
		<< "\"message\":\"/tmp/file\"}]}";

	//Get the actual contents of the getTest.txt file.

	string sampleTextContents = stream.str();

	//Create a string to hold the full url to the PostTester.php file
	string urlFilePath = testerPropertyMap.at(URL_PATH);
	urlFilePath += "update/";

	//Use libcurlUtils to post sampleTextContents to urlFilePath
	string error = libcurlUtilsPtr->post(urlFilePath, sampleTextContents, testerPropertyMap.at(USER_NAME), testerPropertyMap.at(PASS_WORD));

	// All we can do for now is check the error. It should be empty.
	BOOST_REQUIRE_EQUAL(error,"");

//	//Now set url to point to the results of the post call.
//	urlFilePath = testerPropertyMap.at(URL_PATH) + "PostTest.txt";
//
//	//Use libcurlUtils to get the contents of the file at urlFilePath.
//	string postTestFileContents = libcurlUtilsPtr->get(urlFilePath, testerPropertyMap.at(USER_NAME), testerPropertyMap.at(PASS_WORD));
//
//	//Verify that post returns the same file contents.
//	BOOST_REQUIRE_EQUAL(postTestFileContents,"OK");

	return;
}

BOOST_AUTO_TEST_SUITE_END()
