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

#include "Updater.h"
#include <fstream>
#include <boost/lexical_cast.hpp>
#include <boost/foreach.hpp>
#include <boost/algorithm/string.hpp>
#include <stdlib.h>
#include "LibcurlUtils.h"

/**
 * The Constructor. Here, the Constructor will search for properties file in the current directory.
 * This file must contain the url, item id and client key used for transmission.
 */
Updater::Updater() {

	//Get the contents of the "updater.properties" file
	string contents = getPropertyFileContents();

	//Initialize the updater
	initialize(contents);

}

/**
 * The Constructor. Here, the Constructor requires an input stream in the form of the
 * updater.properties file format.
 *
 * @param stream An input stream in the form of the updater.properties file format.
 */
Updater::Updater(istream &stream) {

	//Create a string buffer
	stringstream buffer;

	//Read contents of the input stream into string buffer.
	buffer << stream.rdbuf();

	//Create a string to hold contents of buffer
	string contents;

	//Convert buffer into a string and assign to contents.
	contents = buffer.str();

	//Initialize the updater
	initialize(contents);

}

/**
 * The Destructor.
 */
Updater::~Updater() {
	return;
}

/**
 * Adds a Post object to the posts queue indicating the creation of the file located at path.
 *
 * @param path The path of the file.
 */
void Updater::postFileCreated(string path) {

	//Create a new FILE_CREATED post
	addPostToQueue(FILE_CREATED, path);

	return;
}

/**
 * Adds a Post object to the posts queue indicating the deletion of the file located at path.
 *
 * @param path The path of the file.
 */
void Updater::postFileDeleted(string path) {

	//Create a new FILE_DELETED post
	addPostToQueue(FILE_DELETED, path);

	return;
}

/**
 * Adds a Post to the posts queue indicating the modification of the file located at path.
 *
 * @param path The path of the file.
 */
void Updater::postFileModified(string path) {

	//Create a new FILE_MODIFIED post
	addPostToQueue(FILE_MODIFIED, path);

	return;
}

/**
 * Adds a plain text message Post to the posts queue.
 *
 * @param message A plain text message.
 */
void Updater::postMessage(string message) {

	//Create a new MESSAGE_POSTED post
	addPostToQueue(MESSAGE_POSTED, message);

	return;
}

/**
 * Adds a Post object to posts queue containing the convergence status of the user simulation.
 * Status must be &gt;=1 and &lt;=100. If the value of status is less than 0 then the value will be set to 0.
 * If the value of status is greater than 100 then the value will be set to 100.
 *
 * @param status The convergence value to post.
 */
void Updater::updateConvergence(int status) {

	//If status is less than 0, set status to 0
	if (status < 0) {
		status = 0;
		//If status is greater than 100, set status to 100
	} else if (status > 100) {
		status = 100;
	}

	//Create a new CONVERGENCE_UPDATED post
	addPostToQueue(CONVERGENCE_UPDATED,
			boost::lexical_cast < string > (status));

	return;
}

/**
 * Adds a Post object to posts queue containing the progress of the user simulation.
 * Status must be &gt;=1 and &lt;=100. If the value of status is less than 0 then the value will be set to 0.
 * If the value of status is greater than 100 then the value will be set to 100.
 *
 * @param status The progress value to post.
 */
void Updater::updateProgress(int status) {

	//If status is less than 0, set status to 0
	if (status < 0) {
		status = 0;
		//If status is greater than 100, set status to 100
	} else if (status > 100) {
		status = 100;
	}

	//Create a new PROGRESS_UPDATED post
	addPostToQueue(PROGRESS_UPDATED, boost::lexical_cast < string > (status));

	return;
}

/**
 * Adds a Post object to the posts queue.
 *
 * @param type A PostType literal.
 * @param message The string message assigned to the Post.
 */
void Updater::addPostToQueue(PostType type, string message) {

	//Lock the thread using a lock_guard object
	boost::lock_guard < boost::mutex > lock(mutex);

	//Create a new post
	shared_ptr<Post> postPtr(new Post(type, message));

	//Add the post to the posts queue
	posts.push(postPtr);

	return;
}

/**
 * Sets the ignoreSslPeerVerification flag. If ignoreSslPeerVerification flag is
 * set to true then cURL will skip peer certificate verification for HTTPS urls.
 * This flag should only be set to true for testing purposes.
 *
 * @param ignoreSslPeerVerification The value for the ignoreSslPeerVerification flag.
 */
void Updater::setIgnoreSslPeerVerification(bool ignoreSslPeerVerification) {

	//Set the instance value to the value in ignoreSslPeerVerification.
	this->ignoreSslPeerVerification = ignoreSslPeerVerification;

}

/**
 * Creates the thread object initialized with a reference to the threadProcess() operation.
 * Returns whether the thread was created successfully.
 *
 * @return true if the thread was started successfully.
 */
bool Updater::start() {

	//If thread has not been created and the property map is valid
	if (!threadCreated && goodPropertyMap) {

		//Create a boost thread with threadProcess as its function
		thread = boost::thread(&Updater::threadProcess, this);

		//Set flag to true
		threadCreated = true;

		//Create a new UPDATER_STARTED post
		addPostToQueue(UPDATER_STARTED, "");

	}

	//Return the value of threadCreated
	return threadCreated;
}

/**
 * Stops the thread by calling the interrupt operation on thread.
 * Returns whether the thread was interrupted successfully.
 *
 * @return true if the thread was stopped successfully.
 */
bool Updater::stop() {

	//If thread has been created
	if (threadCreated) {

		//Create a new UPDATER_STOPPED post
		addPostToQueue(UPDATER_STOPPED, "");

		//Wait for a few seconds so that all posts will get processed on the server
		//Create a boost posix_time::seconds object for 1 seconds
		boost::posix_time::seconds sleepTime(1);

		//Pause this thread for sleepTime seconds
		boost::this_thread::sleep(sleepTime);

		//Interrupt the thread
		thread.interrupt();

		//Set threadCreated back to false
		threadCreated = false;

		//We have stopped the updater
		//so we dump the errors into an error file.
		errorLoggerPtr->dumpErrors();

	}

	//Return not threadCreated
	return !threadCreated;
}

/**
 * The method passed to the thread constructor which transmits all
 * Posts in the posts queue to url with cURL in JSON format.
 * The thread is then put to sleep for 1000 milliseconds.
 */
void Updater::threadProcess() {

	//Get the item id from the propertyMap
	string itemId = propertyMap.at(ITEM_ID);

	//Get the item id from the propertyMap
	string clientKey = propertyMap.at(CLIENT_KEY);

	//Get the url from the propertyMap
	string url = propertyMap.at(URL);

	//Create a new LibcurlUtils object to transmit posts
	shared_ptr<LibcurlUtils> libcurlUtilsPtr(new LibcurlUtils());

	//Set the ignoreSslPeerVerification flag in libcurlUtilsPtr
	libcurlUtilsPtr->setIgnoreSslPeerVerification(ignoreSslPeerVerification);

	//Start an infinite loop
	while (true) {

		//If we have posts to transmit
		if (!posts.empty()) {

			//Lock the thread using a lock_guard object
			boost::lock_guard < boost::mutex > lock(mutex);

			//Create the transmission string and add initial json string to it
			string transmission = "{\"item_id\":\"" + itemId
					+ "\", \"client_key\":\"" + clientKey + "\", \"posts\":[";

			//Cycle over all posts in posts queue
			while (!posts.empty()) {

				//Get the first post
				PostPtr postPtr = posts.front();

				//Get the JSON from the post
				string json = postPtr->getJSON();

				//Add the json to the posts array in the transmission
				transmission += json;

				//Remove the post from the queue
				posts.pop();

				//If posts is still non empty
				if (!posts.empty()) {

					//Add a comma separator
					transmission += ",";

				}

			}

			//Add the final characters to the transmission
			transmission += "]}";

			//Transmit post to url and return an error if there is one
			string error = libcurlUtilsPtr->post(url, transmission, propertyMap[USERNAME], propertyMap[PASSWORD]);

			//Check to see if there was an error
			if (error != "") {

				//Then log the error
				errorLoggerPtr->logError(error);
			}

		}

		//Create a boost posix_time::seconds object for one second
		boost::posix_time::seconds sleepTime(1);

		//Pause this thread for sleepTime seconds
		boost::this_thread::sleep(sleepTime);

	}

	return;
}

/**
 * Returns the contents of the updater.properties file as a string.
 *
 * @return The string contents of the updater.properties file if it exists in the current directory.
 */
string Updater::getPropertyFileContents() {

	//Create a variable to hold the properties filename
	string propertiesFilename = "updater.properties";

	//Declare variable to holds contents of local file.
	string contents;

	//Declare input file stream.
	ifstream file;

	//Create an input file stream with the local test file.
	file.open(propertiesFilename.data());

	//Create a string buffer to store file contents.
	stringstream buffer;

	//Read contents of file into string buffer.
	buffer << file.rdbuf();

	//Convert buffer into a string and assign to contents.
	contents = buffer.str();

	//Close the file
	file.close();

	//Return contents.
	return contents;
}

/**
 * Creates and returns a PropertyMap type from a string formatted as a Java properties file.
 *
 * @param propertyString A string formatted as a Java properties file containing name/value pairs.
 * @return A PropertyMap comprised of the name/value pairs in the propertyString.
 */
PropertyMap Updater::getPropertyMap(string propertyString) {

	//Create an empty map associating a PropertyType with a string value
	PropertyMap propertyMap;

	//Create a vector to hold the lines in the properties string
	vector < string > lines;

	//Split propertyString into lines and store in a vector
	split(lines, propertyString, is_any_of("\n"));

	//Loop over all tokens
	BOOST_FOREACH(string line, lines) {

		//Create a new vector to hold the name and value
		vector<string> pair;

		//If the "=" character is found in this line
		if(line.find("=") != string::npos) {

			//Split the line into a property/value pair
			split(pair, line, is_any_of("="));

			//Get the property name
			string property = pair[0];

			//Get the property value
			string value = pair[1];

			// FIXME! This switch can be removed with a map. ~JJB 20140404 15:06

			//If the property is "item_id"
			if(property=="item_id") {
				//Insert PropertyType ITEM_ID and the value
				propertyMap.insert(PropertyMap::value_type(ITEM_ID, value));
				//If the property is "url"
			} else if(property=="url") {
				//Insert PropertyType URL and the value
				propertyMap.insert(PropertyMap::value_type(URL, value));
				//If the property is "client_key"
			} else if(property=="client_key") {
				//Insert PropertyType CLIENT_KEY and the value
				propertyMap.insert(PropertyMap::value_type(CLIENT_KEY, value));
			} else if (property=="username") {
				// Insert the username into the map
				propertyMap.insert(PropertyMap::value_type(USERNAME,value));
			} else if (property=="password") {
				// Insert the password into the map
				propertyMap.insert(PropertyMap::value_type(PASSWORD,value));
			}

		}

	}

	//Return the property map
	return propertyMap;
}

/**
 * Validates the propertyMap object and writes any errors to the error logger.
 *
 * @return True if the property map contains good values.
 */
bool Updater::validatePropertyMap() {

	//Declare boolean flag and set value to true.
	bool goodPropertyMap = true;

	//If the map has less than 5 key/value pairs.
	if (propertyMap.size() < 5) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map contains less than the five default values.");
		goodPropertyMap = false;
	}

	//If the map does not have an ITEM_ID key.
	if (propertyMap.find(ITEM_ID) == propertyMap.end()) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map does not contain a value for \"item_id\".");
		goodPropertyMap = false;
	} else if (propertyMap.at(ITEM_ID).empty()) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map contains an empty value for \"item_id\".");
		goodPropertyMap = false;
	}

	//If the map does not have an CLIENT_KEY key.
	if (propertyMap.find(CLIENT_KEY) == propertyMap.end()) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map does not contain a value for \"client_key\".");
		goodPropertyMap = false;
	} else if (propertyMap.at(CLIENT_KEY).empty()) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map contains an empty value for \"client_key\".");
		goodPropertyMap = false;
	} else if (propertyMap.at(CLIENT_KEY).size() != 40) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map contains a value for \"client_key\" that is not exactly 40 characters.");
		goodPropertyMap = false;
	}

	//If the map does not have an URL key.
	if (propertyMap.find(URL) == propertyMap.end()) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map does not contain a value for \"url\".");
		goodPropertyMap = false;
	} else if (propertyMap.at(URL).empty()) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map contains an empty value for \"url\".");
		goodPropertyMap = false;
	}

	//If the map does not have an username key.
	if (propertyMap.find(USERNAME) == propertyMap.end()) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map does not contain a value for \"username\".");
		goodPropertyMap = false;
	} else if (propertyMap.at(USERNAME).empty()) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map contains an empty value for \"username\".");
		goodPropertyMap = false;
	}

	//If the map does not have an password key.
	if (propertyMap.find(PASSWORD) == propertyMap.end()) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map does not contain a value for \"password\".");
		goodPropertyMap = false;
	} else if (propertyMap.at(PASSWORD).empty()) {
		//Then log error and set flag to false.
		errorLoggerPtr->logError(
				"The property map contains an empty value for \"password\".");
		goodPropertyMap = false;
	}

	//Return the flag.
	return goodPropertyMap;
}

/**
 * Initializes ICEupdater by creating the property map and setting threadCreated to false.
 * Called only from the constructors.
 *
 * @param propertyString A string formatted as a Java properties file containing name/value pairs.
 */
void Updater::initialize(string propertyString) {

	//Get the map of configuration properties
	propertyMap = getPropertyMap(propertyString);

	//Set threadCreated to false
	threadCreated = false;

	//Set the ignoreSslPeerVerification flag to false by default
	ignoreSslPeerVerification = false;

	//Create an ErrorLoggerPtr object.
	errorLoggerPtr = ErrorLoggerPtr(new ErrorLogger());

	//Validate the propertyMap and store the results
	goodPropertyMap = validatePropertyMap();

	//If the propertyMap was not successfully validated
	if (!goodPropertyMap) {

		//Dump the errors into an error file.
		errorLoggerPtr->dumpErrors();

	}

}
