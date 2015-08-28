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

#include "UtilityOperations.h"
#include <string>
#include <iostream>
#include "HDF5LWRTagType.h"
#include <map>
#include <stdexcept>
#include <algorithm>
#include "MaterialType.h"
#include "TubeType.h"

using namespace ICE_Reactor;

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
    std::string nullString;

    //String only with spaces
    int counter = 0;
    int i = 0;
    while(i < s.size()) {
        if(s[i] == ' ')  counter++;
        i++;
    }

    //If the string is null, empty, or the same size as the number of spaces, return empty
    if(s == nullString || s=="" || s.size() == counter) return "";

    //Return a string trim.
    return trim_left_copy( trim_right_copy( s ), delimiters);
}

/**
 * Converts a HDF5LWRTagType to a string
 *
 * @param the type
 *
 * @return string representation of the type
 */
std::string UtilityOperations::toStringHDF5Tag(HDF5LWRTagType tag) {

    //Initialize the map
    std::map<std::string, HDF5LWRTagType> tagMap;
    //Add pieces
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("BWReactor", BWREACTOR) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Control Bank", CONTROL_BANK) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Fuel Assembly", FUEL_ASSEMBLY) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Grid Label Provider", GRID_LABEL_PROVIDER) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Incore Instrument", INCORE_INSTRUMENT) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("LWRComponent", LWRCOMPONENT) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("LWRComposite", LWRCOMPOSITE) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("LWReactor", LWREACTOR) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("LWRGridManager", LWRGRIDMANAGER) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("LWRRod", LWRROD) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Material", MATERIAL) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("PWRAssembly", PWRASSEMBLY) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("PWReactor", PWREACTOR) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Ring", RING) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Rod Cluster Assembly", ROD_CLUSTER_ASSEMBLY) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("MaterialBlock", MATERIALBLOCK) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Tube", TUBE) );

    std::map<std::string, HDF5LWRTagType>::iterator iter;
    for(iter = tagMap.begin(); iter != tagMap.end(); ++iter) {
        if(iter->second == tag) {
            return iter->first;
        }
    }
}

/**
 * Converts a string to a HDF5LWRTagType
 *
 * @param the string type
 *
 * @return the returned type
 */
HDF5LWRTagType UtilityOperations::fromStringHDF5Tag(std::string tag) {

    //Initialize the map
    std::map<std::string, HDF5LWRTagType> tagMap;

    //Add pieces
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("BWReactor", BWREACTOR) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Control Bank", CONTROL_BANK) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Fuel Assembly", FUEL_ASSEMBLY) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Grid Label Provider", GRID_LABEL_PROVIDER) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Incore Instrument", INCORE_INSTRUMENT) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("LWRComponent", LWRCOMPONENT) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("LWRComposite", LWRCOMPOSITE) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("LWReactor", LWREACTOR) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("LWRGridManager", LWRGRIDMANAGER) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("LWRRod", LWRROD) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Material", MATERIAL) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("PWRAssembly", PWRASSEMBLY) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("PWReactor", PWREACTOR) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Ring", RING) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Rod Cluster Assembly", ROD_CLUSTER_ASSEMBLY) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("MaterialBlock", MATERIALBLOCK) );
    tagMap.insert( std::pair<std::string, HDF5LWRTagType>("Tube", TUBE) );

    //Find based on tag
    try {
        return tagMap.at(tag);
    } catch (const std::out_of_range& oor) {
        throw -1;
    }
}


/**
 * Converts a material type to a string
 *
 * @param the type
 *
 * @return string representation of the type
 */
MaterialType UtilityOperations::fromStringMaterialType(std::string tag) {

    //Initialize the map
    std::map<std::string, MaterialType> tagMap;

    //Add pieces
    tagMap.insert( std::pair<std::string, MaterialType>("Solid", SOLID) );
    tagMap.insert( std::pair<std::string, MaterialType>("Liquid", LIQUID) );
    tagMap.insert( std::pair<std::string, MaterialType>("Gas", GAS) );

    //Find based on tag
    try {
        return tagMap.at(tag);
    } catch (const std::out_of_range& oor) {
        throw -1;
    }
}

/**
 * Converts a string to a MaterialType
 *
 * @param the string type
 *
 * @return the returned type
 */
std::string UtilityOperations::toStringMaterialType(MaterialType tag) {

    //Initialize the map
    std::map<std::string, MaterialType> tagMap;

    //Add pieces
    tagMap.insert( std::pair<std::string, MaterialType>("Solid", SOLID) );
    tagMap.insert( std::pair<std::string, MaterialType>("Liquid", LIQUID) );
    tagMap.insert( std::pair<std::string, MaterialType>("Gas", GAS) );

    std::map<std::string, MaterialType>::iterator iter;
    for(iter = tagMap.begin(); iter != tagMap.end(); ++iter) {
        if(iter->second == tag) {
            return iter->first;
        }
    }
}

/**
 * Converts a TubeType to a string.
 *
 * @param the type
 *
 * @return string representation of the type
 */
TubeType UtilityOperations::fromStringTubeType(std::string tag) {

    //Initialize the map
    std::map<std::string, TubeType> tagMap;

    //Add pieces
    tagMap.insert( std::pair<std::string, TubeType>("Instrument", INSTRUMENT) );
    tagMap.insert( std::pair<std::string, TubeType>("Guide", GUIDE) );

    //Find based on tag
    try {
        return tagMap.at(tag);
    } catch (const std::out_of_range& oor) {
        throw -1;
    }
}


/**
 * Converts a string to a TubeType
 *
 * @param the string type
 *
 * @return the returned type
 */
std::string UtilityOperations::toStringTubeType(TubeType tag) {

    //Initialize the map
    std::map<std::string, TubeType> tagMap;

    //Add pieces
    tagMap.insert( std::pair<std::string, TubeType>("Instrument", INSTRUMENT) );
    tagMap.insert( std::pair<std::string, TubeType>("Guide", GUIDE) );

    std::map<std::string, TubeType>::iterator iter;
    for(iter = tagMap.begin(); iter != tagMap.end(); ++iter) {
        if(iter->second == tag) {
            return iter->first;
        }
    }
}
