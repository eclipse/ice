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
#ifndef GRIDDATAMANAGER_H_
#define GRIDDATAMANAGER_H_

#include "GridManager.h"
#include "SFRComponent.h"

namespace ICE_SFReactor {

// A GridManager manages the locations of ICE Components in a set of possible
// locations. Classes that employ this GridManager are expected to translate
// between their own geometric coordinate system and 0-based indices used within
// this class.
class GridDataManager : public GridManager {

    private:

        // A Map of IDataProviders keyed on the locations. If it has a
        // Component, each location should also have a separate IDataProvider to
        // store data.
        std::map<int, std::shared_ptr<SFRComponent>> dataProviders;

    public:

        // The default constructor.
        GridDataManager(int size);

        // Deep copies the contents of the object from another object.
        GridDataManager(const GridDataManager & otherGM);

        // Gets the IDataProvider at a specified location or a null pointer if
        // none exists.
        std::shared_ptr<SFRComponent> getDataProvider(int location);

        // Overrides the GridData method. This also initializes an IDataProvider
        // for the location.
        bool addComponent(std::string name, int location);

        // Overrides the GridData method. This also removes the IDataProvider
        // from the location.
        bool removeComponent(int location);

        // Overrides the GridData method. This also removes the IDataProviders
        // from locations inhabited by the specified Component.
        bool removeComponent(std::string name);

        // Deep copies and returns a newly instantiated object.
        std::shared_ptr<IGridManager> clone();

        // Compares the contents of objects and returns true if they are
        // identical, otherwise returns false.
        bool operator==(const GridDataManager & otherGM);

};  //end class GridManager

} // end namespace
#endif
