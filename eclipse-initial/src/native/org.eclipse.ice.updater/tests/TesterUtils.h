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

#ifndef TESTERUTILS_H
#define TESTERUTILS_H

#include <string>
#include <map>
#include <boost/shared_ptr.hpp>
#include <PropertyType.h>
#include "TesterPropertyType.h"

using namespace std;
using namespace boost;

/**
 * A TesterPropertyMap is a map template associating with TesterPropertyType keys and string values.
 */
typedef map<TesterPropertyType, string> TesterPropertyMap;

/**
 * TesterUtils is a utility class containing static operations used by the Updater tester classes.
 */
class TesterUtils {

public:

    /**
     * Reads the contents of a local file located in the TesterUtilsConfig.h variable TESTS_BUILD_PATH
     * and returns them as a string.
     *
     * @param fileName The name of the local file.
     * @return The contents of a local file as a string.
     */
    static string getLocalFileContents(string fileName);

    /**
     * Creates and returns a TesterPropertyMap type associating a TesterPropertyType with a string value
     * from a string formatted as a Java properties file.
     *
     * @param testerPropertyString A updatertests.properties formatted string.
     * @return A TesterPropertyMap comprised of the name/value pairs in the testerPropertyString.
     */
    static TesterPropertyMap getTesterPropertyMap(string testerPropertyString);

};

#endif
