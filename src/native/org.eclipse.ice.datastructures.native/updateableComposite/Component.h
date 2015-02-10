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

#ifndef COMPONENT_H
#define COMPONENT_H

#include "IUpdateable.h"
#include <memory>
#include "IComponentListener.h"
#include "../componentVisitor/IComponentVisitor.h"

namespace ICE_DS {

/**
 * The Component interface is the base for all shared operations in the UpdateableComposite package's classes. These operations must be implemented by all classes that realize either Component or Composite. Components are also observable and realizations of IComponentListener can be registered with Components to receive updates when the state of the Component changes.
 */
class Component : virtual public IUpdateable {


private:

    /*    std::vector< std::shared_ptr<IComponentListener> > iComponentListener;

        std::vector< std::shared_ptr<IComponentVisitor> > iComponentVisitor;*/

public:

    /**
     * This operation registers a listener that realizes the IComponentListener interface with the Component so that it can receive notifications of changes to the Component if the Component publishes such information.
     *
     * @param the listener to register
     */
    virtual void registerListener(std::shared_ptr<IComponentListener> listener) = 0;

    /**
     * This operation directs the Component to call back to an IComponentVisitor so that the visitor can perform its required actions for the exact type of the Component.
     *
     * @param the visitor to accept
     */
    virtual void accept(std::shared_ptr<IComponentVisitor> visitor) = 0;

};
}
#endif
