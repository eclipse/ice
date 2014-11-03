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
#ifndef GRIDMANAGER_H
#define GRIDMANAGER_H
#include "IGridManager.h"

#include <map>
#include <memory>
#include <set>
#include <string>
#include <vector>

namespace ICE_SFReactor {

// A GridManager manages the locations of ICE Components in a set of possible
// locations. Classes that employ this GridManager are expected to translate
// between their own geometric coordinate system and 0-based indices used within
// this class.
class GridManager : public IGridManager
{

    private:

        // The size of the list of possible locations. This is considered the
		// maximum index plus 1.
        int size;

        // A Map of locations keyed on their index. Each entry contains the
        // name of the Component at that location. If the location is not in the
        // Map, the value should be null.
        std::map<int, std::string> locations;

        // A Map of Components keyed on their names. Each entry contains a Set
        // of locations for quickly looking up all the locations for a
        // particular Component.
        std::map<std::string, std::set<int> > components;

    public:

        // The default constructor. 
        GridManager(int size);

        // Deep copies the contents of the object from another object.
        GridManager(const GridManager & otherGM);

        // Gets the name of the Component in the specified grid location. 
        std::string getComponentName(int location);

        // Gets all the locations occupied by a Component in the grid. 
        std::vector<int> getComponentLocations(std::string name);

        // Adds a Component to the specified location in the grid. If the
        // parameters are valid and the Component did not already occupy that
        // location, this will return true.
        virtual bool addComponent(std::string name, int location);

        // Dissociates the Component at a specified location with that location.
        // If the location has a corresponding Component, this will return true.
        virtual bool removeComponent(int location);

        // Dissociates the Component with all locations that it currently
        // occupies. If the Component had corresponding locations, this will
        // return true.
        virtual bool removeComponent(std::string name);

        // Deep copies and returns a newly instantiated object. 
        std::shared_ptr<IGridManager> clone();

        // Compares the contents of objects and returns true if they are
        // identical, otherwise returns false.
        bool operator==(const GridManager & otherGM);

};  //end class GridManager

} // end namespace
#endif
