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

#ifndef ICOMPONENTLISTENER_H
#define ICOMPONENTLISTENER_H

#include "Component.h"
#include <memory>

namespace ICE_DS {

class Component; // Forward Declaration

/**
 * The IComponentListener interface specifies the operations that must be realized by classes in order to receive updates from realizations of the Component interface when their state changes.
 */
class IComponentListener {

private:

    //std::vector< std::shared_ptr<Component> > component;

public:

    /**
     * This operation notifies the listener that an update has occurred in the Component.
     *
     * @param the component to update
     */
    virtual void update(std::shared_ptr<Component> component) = 0;

};
}
#endif
