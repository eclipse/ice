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

#include "UtilityOperations.h"
#include <string>
#include <iostream>
//#include "HDF5LWRTagType.h"
#include <map>
#include <stdexcept>
#include <algorithm>
//#include "MaterialType.h"
//#include "TubeType.h"

using namespace ICE_SFReactor;

/**
 * The Copy Constructor.  Does nothing
 */
UtilityOperations::UtilityOperations(UtilityOperations & arg) {

}

/**
 * The Destructor.  Does nothing
 */
UtilityOperations::~UtilityOperations() {

}

/**
 * Trims a line and returns a copy of the string.
 *
 * @param the string to trim
 * @param delimiters to trim from
 */
std::string UtilityOperations::trim_right_copy(const std::string &s, const std::string& delimiters = " \f\n\r\t\v") {
    return s.substr( 0, s.find_last_not_of( delimiters ) + 1 );
}

/**
 * Trims left of a line and returns a copy of the string.
 *
 * @param the string to trim
 * @param delimiters to trim from
 */
std::string UtilityOperations::trim_left_copy(const std::string &s, const std::string& delimiters = " \f\n\r\t\v") {
    return s.substr( s.find_first_not_of( delimiters ) );
}

/**
 * Trims right of a line and returns a copy of the string.
 *
 * @param the string to trim
 * @param delimiters to trim from
 */
std::string UtilityOperations::trim_copy(const std::string &s, const std::string& delimiters = " \f\n\r\t\v") {
	// Start with an empty string.
	std::string trimmed;

	// If the string has characters not in delimiters, then trim it.
    if (s.find_last_not_of(delimiters) != std::string::npos)
    	trimmed = trim_left_copy( trim_right_copy( s ), delimiters);

    // Return the trimmed string.
    return trimmed;
}
