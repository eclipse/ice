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
#ifndef IGRIDMANAGER_H
#define IGRIDMANAGER_H

#include <string>
#include <vector>

namespace ICE_SFReactor {

// Classes that implement this interface should keep track of the locations
// of each Component added. It should offer fast look-up capabilities based on
// both the location and the name of a component.
class IGridManager {

    public:

        // Gets the name of the Component in the specified grid location. 
        virtual std::string getComponentName(int location) = 0;

        // Gets all the locations occupied by a Component in the grid. 
        virtual std::vector<int> getComponentLocations(std::string name) = 0;

        // Adds a Component to the specified location in the grid. If the
        // parameters are valid and the Component did not already occupy that
        // location, this will return true.
        virtual bool addComponent(std::string name, int location) = 0;

        // Dissociates the Component at a specified location with that location.
        // If the location has a corresponding Component, this will return true.
        virtual bool removeComponent(int location) = 0;

        // Dissociates the Component with all locations that it currently
        // occupies. If the Component had corresponding locations, this will
        // return true.
        virtual bool removeComponent(std::string name) = 0;

};  //end class IGridManager

} //end namespace

#endif
