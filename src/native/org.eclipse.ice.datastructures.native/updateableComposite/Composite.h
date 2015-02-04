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

#ifndef COMPOSITE_H
#define COMPOSITE_H

#include "Component.h"
#include <vector>
#include <memory>

namespace ICE_DS {

/**
 * The Composite interface defines behavior for realizations of the Component interface that will also have children.
 */
class Composite : virtual public Component {

public:

    /**
     * This operation adds a child Component to a class that realizes the Composite interface. This operation should notify listeners that components have been added.
     *
     * @param the component to add
     */
    virtual void addComponent(std::shared_ptr<Component> child) = 0;

    /**
     * This operation removes a child Component to a class that realizes the Composite interface.
     *
     * @param the component's id to remove
     */
    virtual void removeComponent(int childId) = 0;

    /**
     * This operation retrieves a child Component to a class that realizes the Composite interface.
     *
     * @param the component's id
     *
     * @return the component with id
     */
    virtual std::shared_ptr<Component> getComponent(int childId) = 0;

    /**
     * This operations retrieves the number of child Components stored in an instance of a class that realizes the Composite interface.
     *
     * @return the number of components
     */
    virtual int getNumberOfComponents() = 0;

    /**
     * This operation returns all of the Components stored in the Composite.
     *
     * @return the components
     */
    virtual std::vector< std::shared_ptr<Component> > getComponents() = 0;

};
}
#endif
