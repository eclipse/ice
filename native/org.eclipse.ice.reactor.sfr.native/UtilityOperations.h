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

#ifndef UTILITYOPERATIONS_H
#define UTILITYOPERATIONS_H

#include <string>
#include <map>

namespace ICE_SFReactor {

/**
 * A utility class with static operations.
 */

class UtilityOperations {

public:

    /**
     * The Copy Constructor.  Does nothing
     */
    UtilityOperations(UtilityOperations & arg);

    /**
     * The Destructor.  Does nothing
     */
    virtual ~UtilityOperations();

    /**
     * Trims a line and returns a copy of the string.
     *
     * @param the string to trim
     * @param delimiters to trim from
     */
    static std::string trim_copy(const std::string &s, const std::string& delimiters);

    /**
     * Trims left of a line and returns a copy of the string.
     *
     * @param the string to trim
     * @param delimiters to trim from
     */
    static std::string trim_left_copy(const std::string &s, const std::string& delimiters);

    /**
     * Trims right of a line and returns a copy of the string.
     *
     * @param the string to trim
     * @param delimiters to trim from
     */
    static std::string trim_right_copy(const std::string &s, const std::string& delimiters);

};

} //end namespace

#endif
