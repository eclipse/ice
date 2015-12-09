/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Eric J. Lingerfelt, Alexander J. McCaskey
 *******************************************************************************/

#define BOOST_TEST_DYN_LINK
#define BOOST_TEST_MODULE UpdaterTester_testSuite
#include <iostream>
#include <Post.h>
#include <PostType.h>
#include <boost/property_tree/json_parser.hpp>
#include <boost/property_tree/ptree.hpp>
#include <boost/shared_ptr.hpp>
#include <boost/test/included/unit_test.hpp>
#include <PostType.h>

using namespace std;
using namespace boost;

/**
 * A PostPtr is a shared pointer object referring to a Post instance.
 */
typedef shared_ptr<Post> PostPtr;

/**
 * An instance of PostPtr used for testing.
 */
PostPtr postPtr;

struct TestStruct {

	/**
	 * An string variable for testing.
	 */
	string testString;

	/**
	 * An PostType enum value for testing.
	 */
	PostType testType;

	TestStruct() {

	    //Initialize test message
	    testString = "Many new, high-performance nuclear energy modeling and simulation tools are under ";
	    testString+= "development in the United States and many more existing projects are moving to high-performance computing. ";
	    testString+= "This shift to state-of-the-art modeling and simulation will not only result in an unprecedented use of computing ";
	    testString+= "resources but it will generate extremely large amounts of data in extraordinary detail. Domain scientists will ";
	    testString+= "find themselves simultaneously in two positions: managing the complexity of their own problem domain and managing ";
	    testString+= "the complexity of a nightmarish scenario of high-performance computational complexity. ";

	    //Initialize post type
	    testType = MESSAGE_POSTED;

	    //Construct a new PostPtr instance.
	    postPtr = PostPtr(new Post(testType, testString));

	}

	~TestStruct() {}

};

BOOST_FIXTURE_TEST_SUITE(PostTester_testSuite, TestStruct)

/**
 * Checks the getGetJSON() operation.
 */
BOOST_AUTO_TEST_CASE(checkGetJSON) {

    //Create a buffer to hold JSON.
    stringstream buffer;

    //Pipe JSON string into buffer.
    buffer << postPtr->getJSON();

    //Create a ptree to hold the JSON tree.
    property_tree::ptree tree;

    //Parse the JSON into the ptree.
    property_tree::read_json(buffer, tree);

    //Verify that the post's type is the same as testType.
    BOOST_REQUIRE(tree.get_child("type").data() == "MESSAGE_POSTED");

    //Verify that the message length is less than 256 characters
    BOOST_REQUIRE(tree.get_child("message").data().length() <= 256);

    //Truncate local testString to 256 characters.
    testString = testString.substr(0, 256);

    //Verify that the post's message is the same as testString.
    BOOST_REQUIRE(tree.get_child("message").data() == testString);

    //Create a properly formatted JSON string for testing.
    string s = "{\"type\":\"MESSAGE_POSTED\",\"message\":\"" + testString + "\"}";

    //Verify that post returns the correct JSON string.
    BOOST_REQUIRE(postPtr->getJSON() == s);

    return;
}

/**
 * Checks the getPostTypeString() operation.
 */
BOOST_AUTO_TEST_CASE(checkGetPostTypeString) {

    //Create the correct string representation for PostType MessagePosted enum value.
    string s = "MESSAGE_POSTED";

    //Verify that post returns the correct JSON string.
    BOOST_REQUIRE(postPtr->getPostTypeString(testType) == s);

    return;
}

BOOST_AUTO_TEST_SUITE_END()
