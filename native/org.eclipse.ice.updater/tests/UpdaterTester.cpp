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
#include <fstream>
#include <sstream>
#include <boost/lexical_cast.hpp>
#include <boost/random/uniform_int.hpp>
#include <boost/filesystem/operations.hpp>
#include <boost/filesystem/path.hpp>
#include <LibcurlUtils.h>
#include <Updater.h>
#include <Post.h>
#include <PostType.h>
#include "TesterUtils.h"
#include "TesterUtilsConfig.h"
#include <boost/test/included/unit_test.hpp>
#include <boost/shared_ptr.hpp>
#include <boost/random/mersenne_twister.hpp>
#include <PostType.h>
#include "TesterUtils.h"

using namespace std;
using namespace boost;

/**
 * A LibcurlUtilsPtr is a shared pointer object referring to a LibcurlUtils instance.
 */
typedef shared_ptr<LibcurlUtils> LibcurlUtilsPtr;

/**
 * A UpdaterPtr is a shared pointer object referring to a Updater instance.
 */
typedef shared_ptr<Updater> UpdaterPtr;

using namespace boost::filesystem;

BOOST_AUTO_TEST_SUITE(UpdaterTester_testSuite)

/**
 * Deletes all error log files dumped by the Updater's ErrorLogger class.
 */
void deleteAllErrorLogFiles() {

	//Create a boost path object set to the current working directory.
	path cwd(TESTS_BUILD_PATH);

	//Create a directory_iterator for the end of the iterator.
	//We can do this since default construction yields past-the-end itr.
	directory_iterator end_itr;

	//Loop over the files in the directory.
	for (directory_iterator itr(cwd); itr != end_itr; ++itr) {

		//Get the next path in the itr.
		path p = *itr;

		//Check if this is a regular file and not a directory
		//Then check if the filename contains "updatererrors"
		if (is_regular_file(p)
				&& p.string().find("updatererrors") != string::npos) {

			//Delete the error file.
			remove(p);

		}

	}

}

/**
 * Returns a flag indicating whether an error log file exists.
 *
 * @return A flag indicating whether an error log file exists.
 */
bool errorLogFileExists() {

	//Create a boost path object set to the current working directory.
	path cwd(TESTS_BUILD_PATH);

	//Create a directory_iterator for the end of the iterator.
	//We can do this since default construction yields past-the-end itr.
	directory_iterator end_itr;

	//Loop over the files in the directory.
	for (directory_iterator itr(cwd); itr != end_itr; ++itr) {

		//Get the next path in the itr.
		path p = *itr;

		//Check if this is a regular file and not a directory
		//Then check if the filename contains "updatererrors"
		if (is_regular_file(p)
				&& p.string().find("updatererrors") != string::npos) {

			return true;

		}

	}

	return false;

}


/**
 * Creates a name/value pair in the following format: &lt;name&gt;=&lt;value&gt;newline.
 *
 * @param postType A PostType value used to generate the name.
 * @param post A string post used to generate the value.
 * @return A string in the form of name=value;
 */
string getNameValuePair(PostType postType, string post) {

	//Return a name/value pair.
	return Post::getPostTypeString(postType) + "=" + post + "\n";
}

/**
 * Executes a post for the given PostType and returns a name/value pair.
 *
 * @param postType The Post's PostType value.
 * @param niCEUpdaterPtr The niCEUpdaterPtr used to transmit the Post.
 * @return A name value pair comprised of the PostType and its value.
 */
string executePost(PostType postType,
		UpdaterPtr niCEUpdaterPtr) {

	//Create temp string variables for testing
	string filepath = "/tmp/work/data/dataFile";
	string message = "This is message number ";

	//Initialize static variables for progress and convergence.
	//These will be randomly incremented over the
	//course of the test.
	static int progressCounter = 1;
	static int convergenceCounter = 1;
	static int messageCounter = 1;
	static int fileCounter = 1;

	//Append the counters to the text variables.
	filepath += boost::lexical_cast < string > (fileCounter);
	message += boost::lexical_cast < string > (messageCounter);

	//Create and initialize return parameter
	string nameValuePair = "";

	//Switch on postType
	switch (postType) {

	case FILE_CREATED:

		//Post a file created message
		niCEUpdaterPtr->postFileCreated(filepath);

		//Create a name/value pair from the post
		nameValuePair = getNameValuePair(FILE_CREATED, filepath);

		//Increment file counter
		fileCounter++;

		break;

	case FILE_MODIFIED:

		//Post a file modified message
		niCEUpdaterPtr->postFileModified(filepath);

		//Create a name/value pair from the post
		nameValuePair = getNameValuePair(FILE_MODIFIED, filepath);

		//Increment file counter
		fileCounter++;

		break;

	case FILE_DELETED:

		//Post a file deleted message
		niCEUpdaterPtr->postFileDeleted(filepath);

		//Create a name/value pair from the post
		nameValuePair = getNameValuePair(FILE_DELETED, filepath);

		//Increment file counter
		fileCounter++;

		break;

	case MESSAGE_POSTED:

		//Post a plain text message
		niCEUpdaterPtr->postMessage(message);

		//Create a name/value pair from the post
		nameValuePair = getNameValuePair(MESSAGE_POSTED, message);

		//Increment the message counter
		messageCounter++;

		break;

	case PROGRESS_UPDATED:

		//Post a progress update status
		niCEUpdaterPtr->updateProgress(progressCounter);

		//Create a name/value pair from the post
		nameValuePair = getNameValuePair(PROGRESS_UPDATED,
				boost::lexical_cast < string > (progressCounter));

		//Increment progress
		progressCounter++;

		//If the test value of progress is greater than 100
		//then set the value to 100 to match post
		if (progressCounter > 100) {
			progressCounter = 100;
		}

		break;

	case CONVERGENCE_UPDATED:

		//Post a convergence update status
		niCEUpdaterPtr->updateConvergence(convergenceCounter);

		//Create a name/value pair from the post
		nameValuePair = getNameValuePair(CONVERGENCE_UPDATED,
				boost::lexical_cast < string > (convergenceCounter));

		//Increment convergence
		convergenceCounter++;

		//If the test value of is greater than 100
		//then set the value to 100 to match post
		if (convergenceCounter > 100) {
			convergenceCounter = 100;
		}

		break;

	default:

		return "";

	}

	return nameValuePair;

}

/**
 * Returns a random integer between min and max inclusively.
 *
 * @param generator A Boost mt19937 random number generator.
 * @param min The inclusive lower bound for the random integer.
 * @param max The inclusive upper bound for the random integer.
 * @return A random integer between min and max inclusively.
 */
int getRandomInteger(boost::mt19937 generator, int min,
		int max) {

	//Create a random number distribution which is uniform
	//in probability for the number of posts
	boost::uniform_int<> distribution(min, max);

	//Generate a random integer using the distribution
	int randomInteger = distribution(generator);

	//Return the random integer.
	return randomInteger;

}

/**
 * Executes tests on each constructor by instantiating them with a correct and a malformed properties file.
 */
BOOST_AUTO_TEST_CASE(checkConstructors) {

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();

	//Construct a new Updater instance with the nullary constructor.
	UpdaterPtr niCEUpdaterPtr = UpdaterPtr(new Updater());

	//Verify that an error log file was not created.
	BOOST_REQUIRE(!errorLogFileExists());

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();

	//Create an input stream
	stringstream stream;

	//Insert data with format into the stream
	stream << TesterUtils::getLocalFileContents("updater.properties");

	//Construct a new Updater instance with the parameterized constructor.
	niCEUpdaterPtr = UpdaterPtr(new Updater(stream));

	//Verify that an error log file was not created.
	BOOST_REQUIRE(!errorLogFileExists());

	//Insert data with format into the stream
	stream << "malformed data";

	//Construct a new Updater instance with the parameterized constructor.
	niCEUpdaterPtr = UpdaterPtr(new Updater(stream));

	//Verify that an error log file was created.
	BOOST_REQUIRE(errorLogFileExists());

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();


}

/**
 * Executes an integration test of the whole system.
 */
BOOST_AUTO_TEST_CASE(checkPostUpdating) {

	//Construct a new LibcurlUtils instance.
	LibcurlUtilsPtr libcurlUtilsPtr = LibcurlUtilsPtr(new LibcurlUtils());

	//Create an input stream
	stringstream stream;

	//Insert data with format into the stream
	stream << TesterUtils::getLocalFileContents("updater.properties");

	//Construct a new Updater instance with the parameterized constructor.
	UpdaterPtr niCEUpdaterPtr = UpdaterPtr(new Updater(stream));

	//Get the contents of updatertests.properties as a string.
	string contents = TesterUtils::getLocalFileContents(
			"updatertests.properties");

	//Create the tester property map.
	TesterPropertyMap testerPropertyMap = TesterUtils::getTesterPropertyMap(
			contents);

	//If the value for IGNORE_SSL_PEER_VERIFICATION exists and is "true"
	if (testerPropertyMap.find(IGNORE_SSL_PEER_VERIFICATION)
			!= testerPropertyMap.end()
			&& testerPropertyMap.at(IGNORE_SSL_PEER_VERIFICATION) == "true") {

		//Then set the ignoreSslPeerVerification flag to true
		libcurlUtilsPtr->setIgnoreSslPeerVerification(true);

		//We need to set the flag in the Updater instances so that they will
		//pass it to the internal instance of LibcurlUtils in the threadProcess operation.
		niCEUpdaterPtr->setIgnoreSslPeerVerification(true);
	}

	//Create the default seed for the random number generator.
	//The time(0) provides the generator with a different starting
	//point each execution. Without this parameter, it will
	//return the same number every execution of the same
	//compilation.
	boost::mt19937 generator = boost::mt19937(time(0));

	//Declare and initialize variable to hold name/value pairs for comparison
	string postContents = "";

	//Call start on niCEUpdater to begin processing thread.
	bool updaterStarted = niCEUpdaterPtr->start();

	//Verify that the updater returned true upon starting.
	BOOST_REQUIRE(updaterStarted);

	//Add start event notification
	postContents += getNameValuePair(UPDATER_STARTED, "");

	//Get a random number of posts
	int numPosts = getRandomInteger(generator, 1,
			boost::lexical_cast<int>(
					testerPropertyMap.at(MAX_NUMBER_OF_POSTS)));

	//Start the randomization loop
	for (int i = 0; i < numPosts; i++) {

		//Get a random integer representing an entry on the
		//PostType enumeration.
		int postTypeInt = getRandomInteger(generator, 0, 5);

		//Execute the post for the random PostType.
		postContents += executePost((PostType) postTypeInt, niCEUpdaterPtr);

		//Get a random integer representing an an
		//amount in seconds to sleep.
		int time = getRandomInteger(generator, 100,
				boost::lexical_cast<int>(
						testerPropertyMap.at(MAX_POST_TIME_INTERVAL)));

		//Create a boost posix_time::milliseconds object for the random time
		boost::posix_time::milliseconds sleepTime(time);

		//Pause this thread for sleepTime milliseconds
		boost::this_thread::sleep(sleepTime);
	}

	//Stop the niCEUpdater
	bool updaterStopped = niCEUpdaterPtr->stop();

	//Verify that the updater returned true upon starting.
	BOOST_REQUIRE(updaterStopped);

	//Add stop event notification
	postContents += getNameValuePair(UPDATER_STOPPED, "");

	// The only thing we can do for the moment is make sure that there was a
	// valid response. We can't check the post contents yet.

	//Verify that an error log file was not created.
	BOOST_REQUIRE(!errorLogFileExists());

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();

	//Create a url path to the file written by the remote server
//	string urlFilePath = testerPropertyMap.at(URL_PATH)
//			+ "UpdaterTester.txt";

	//Use the get function to retrieve the contents of the niCEUpdater posts.
//	string urlContents = libcurlUtilsPtr->get(urlFilePath,"","");

	//Verify that get returns the same string contents as postContents.
	//QCOMPARE(urlContents, postContents);

	return;
}

/**
 * Executes multiple tests on the private validatePropertyMap operation by providing the
 * constructor malformed properties and examining the resulting error log file.
 */
BOOST_AUTO_TEST_CASE(checkPropertyMapValidation) {

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();

	//Create an input stream
	stringstream stream;

	//Insert data with format into the stream
	stream << "malformed data";

	//Construct a new Updater instance with the parameterized constructor.
	UpdaterPtr niCEUpdaterPtr = UpdaterPtr(new Updater(stream));

	//Verify that an error log file was created.
	BOOST_REQUIRE(errorLogFileExists());

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();

	//Insert data with format into the stream
	stream << "item_id=\n";

	//Construct a new Updater instance with the parameterized constructor.
	niCEUpdaterPtr = UpdaterPtr(new Updater(stream));

	//Verify that an error log file was created.
	BOOST_REQUIRE(errorLogFileExists());

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();

	//Insert data with format into the stream
	stream << "item_id=1234\n";

	//Construct a new Updater instance with the parameterized constructor.
	niCEUpdaterPtr = UpdaterPtr(new Updater(stream));

	//Verify that an error log file was created.
	BOOST_REQUIRE(errorLogFileExists());

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();

	//Insert data with format into the stream
	stream << "item_id=1234\nclient_key=\n";

	//Construct a new Updater instance with the parameterized constructor.
	niCEUpdaterPtr = UpdaterPtr(new Updater(stream));

	//Verify that an error log file was created.
	BOOST_REQUIRE(errorLogFileExists());

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();

	//Insert data with format into the stream
	stream
			<< "item_id=1234\n"
			<< "client_key=1234567890ABCDEFGHIJ1234567890ABCDEFGHIJ1234567890ABCDEFGHIJ1234567890ABCDEFGHIJ\n"
			<< "url=http://localhost:8080/ice/update/";

	//Construct a new Updater instance with the parameterized constructor.
	niCEUpdaterPtr = UpdaterPtr(new Updater(stream));

	//Verify that an error log file was created.
	BOOST_REQUIRE(errorLogFileExists());

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();

	//Insert data with format into the stream
	stream
			<< "item_id=1234\nclient_key=1234567890ABCDEFGHIJ1234567890ABCDEFGHIJ\nurl=";

	//Construct a new Updater instance with the parameterized constructor.
	niCEUpdaterPtr = UpdaterPtr(new Updater(stream));

	//Verify that an error log file was created.
	BOOST_REQUIRE(errorLogFileExists());

	//Clear out all of the previous log files.
	deleteAllErrorLogFiles();

	//Insert data with format into the stream
	stream << "item_id=1234\n"
			<< "client_key=1234567890ABCDEFGHIJ1234567890ABCDEFGHIJ\n"
			<< "url=http://localhost:8080/ice/update/"
			<< "username=ice\n" << "password=veryice";

	//Construct a new Updater instance with the parameterized constructor.
	niCEUpdaterPtr = UpdaterPtr(new Updater(stream));

	//Verify that an error log file was created.
	BOOST_REQUIRE(errorLogFileExists());

}

BOOST_AUTO_TEST_SUITE_END()

