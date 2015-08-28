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

#ifndef IGRIDMANAGER_H
#define IGRIDMANAGER_H

#include "GridLocation.h"
#include <memory>
#include <updateableComposite/Component.h>

namespace ICE_Reactor {

/**
 * An interface for managing Components on a grid.
 */
class IGridManager {

public:

    /**
     * Returns the Component at the provided GridLocation or null if one does not exist at the provided location.
     *
     * @param The GridLocation
     *
     * @return the name of the component @ grid Location or empty if it does not exist
     */
    virtual const std::string getComponentName(std::shared_ptr<GridLocation> location) = 0;

    /**
     * Adds a Component and its GridLocation to this GridManager. If a Component already exists at that location, then this operation does nothing.
     *
     * @param The component to add
     * @param The location
     */
    virtual void addComponent(std::shared_ptr<ICE_DS::Component> component, std::shared_ptr<GridLocation> location) = 0;



    /**
     * Removes the Component at the provided GridLocation from this GridManager.
     *
     * @param The location
     */
    virtual void removeComponent(std::shared_ptr<GridLocation> location) = 0;



    /**
     * Removes the provided Component from this GridManager.
     *
     * @param The component to remove
     */
    virtual void removeComponent(std::shared_ptr<ICE_DS::Component> component) = 0;



};  //end class IGridManager

}

#endif
