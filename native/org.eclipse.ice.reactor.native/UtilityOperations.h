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

#ifndef UTILITYOPERATIONS_H
#define UTILITYOPERATIONS_H

#include <string>
#include <map>
#include "HDF5LWRTagType.h"
#include "MaterialType.h"
#include "TubeType.h"

namespace ICE_Reactor {

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

    /**
     * Converts a HDF5LWRTagType to a string
     *
     * @param the type
     *
     * @return string representation of the type
     */
    static std::string toStringHDF5Tag(HDF5LWRTagType tag);

    /**
     * Converts a string to a HDF5LWRTagType
     *
     * @param the string type
     *
     * @return the returned type
     */
    static HDF5LWRTagType fromStringHDF5Tag(std::string tag);

    /**
     * Converts a material type to a string
     *
     * @param the type
     *
     * @return string representation of the type
     */
    static std::string toStringMaterialType(MaterialType tag);

    /**
     * Converts a string to a MaterialType
     *
     * @param the string type
     *
     * @return the returned type
     */
    static MaterialType fromStringMaterialType(std::string tag);

    /**
     * Converts a TubeType to a string.
     *
     * @param the type
     *
     * @return string representation of the type
     */
    static std::string toStringTubeType(TubeType tag);

    /**
     * Converts a string to a TubeType
     *
     * @param the string type
     *
     * @return the returned type
     */
    static TubeType fromStringTubeType(std::string tag);

};

}

#endif
