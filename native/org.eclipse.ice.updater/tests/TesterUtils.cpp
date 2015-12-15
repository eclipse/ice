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

#include "TesterUtils.h"
#include <fstream>
#include <sstream>
#include <iostream>
#include <vector>
#include <boost/foreach.hpp>
#include <boost/algorithm/string.hpp>
#include "TesterUtilsConfig.h"

/**
 * Reads the contents of a local file located in the TesterUtilsConfig.h variable TESTS_BUILD_PATH
 * and returns them as a string.
 *
 * @param fileName The name of the local file.
 * @return The contents of a local file as a string.
 */
string TesterUtils::getLocalFileContents(string fileName) {

    //Declare variable to holds contents of local file.
    string contents;

    //Declare input file stream.
    ifstream file;

    //Create a variable to hold the path to the file.
    string filePath = TESTS_BUILD_PATH;

    //Append the filename to the path
    filePath += fileName;

    //Create an input file stream with the local test file.
    file.open(filePath.data());

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
 * Creates and returns a TesterPropertyMap type associating a TesterPropertyType with a string value
 * from a string formatted as a Java properties file.
 *
 * @param testerPropertyString A "updatertests.properties" formatted string.
 * @return A TesterPropertyMap comprised of the name/value pairs in the testerPropertyString.
 */
TesterPropertyMap TesterUtils::getTesterPropertyMap(string testerPropertyString) {

    //Create an empty map associating a TesterPropertyType with a string value
    TesterPropertyMap testerPropertyMap;

    //Create a vector to hold the lines in the properties string
    vector<string> lines;

    //Split propertyString into lines and store in a vector
    split(lines, testerPropertyString, is_any_of("\n"));

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

            if(property=="max_number_of_posts") {

                //Insert TesterPropertyMap MAX_NUMBER_OF_POSTS and the value
                testerPropertyMap.insert(TesterPropertyMap::value_type(MAX_NUMBER_OF_POSTS, value));

            } else if(property=="max_post_time_interval") {

                //Insert TesterPropertyMap MAX_POST_TIME_INTERVAL and the value
                testerPropertyMap.insert(TesterPropertyMap::value_type(MAX_POST_TIME_INTERVAL, value));

            } else if(property=="ignore_ssl_peer_verification") {

                //Insert TesterPropertyMap IGNORE_SSL_PEER_VERIFICATION and the value
                testerPropertyMap.insert(TesterPropertyMap::value_type(IGNORE_SSL_PEER_VERIFICATION, value));

            } else if(property=="url_path") {

                //If the url_path value does not end in "/"
                if(value[value.length()-1] != '/') {

                    //Then add a "/" to the end of the value.
                    value += "/";
                }

                //Insert TesterPropertyMap URL_PATH and the value
                testerPropertyMap.insert(TesterPropertyMap::value_type(URL_PATH, value));

            } else if (property =="username") {
            	// Get the username
                testerPropertyMap.insert(TesterPropertyMap::value_type(USER_NAME, value));
            } else if (property =="password") {
            	// Get the username
                testerPropertyMap.insert(TesterPropertyMap::value_type(PASS_WORD, value));
            }

        }

    }

    //Return the property map
    return testerPropertyMap;
}
