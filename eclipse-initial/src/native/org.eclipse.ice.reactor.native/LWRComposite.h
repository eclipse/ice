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

#ifndef LWRCOMPOSITE_H
#define LWRCOMPOSITE_H

#include "LWRComponent.h"
#include <updateableComposite/Composite.h>
#include <updateableComposite/Component.h>
#include <ICEObject/Identifiable.h>
#include <IHdfReadable.h>
#include <IHdfWriteable.h>
#include <vector>
#include <string>
#include <memory>
#include <map>

namespace ICE_Reactor {

/**
 * <p>The LWRComposite class represents all reactor components that can store and manage
 * LWRComponents. This class implements the ICE Composite interface.  This class was
 * designed as a "branch" within the Reactor package to hold references to other
 * LWRComponents.  Although this class implements the Composite interface, classes that
 * extend LWRComposite should consider if they will need to override the Composite
 * Interface's operations to provide specific utility as needed.</p>
 */
class LWRComposite : public LWRComponent, virtual public ICE_DS::Composite {

protected:

    /**
     * A Hashtable keyed on LWRComponent name storing unique LWRComponents.
     */
    std::map< std::string, std::shared_ptr<LWRComponent> > lWRComponents;

public:

    /**
     * The Copy constructor.
     *
     * @param The object to be copied.
     */
    LWRComposite(LWRComposite & arg);

    /**
     * The Destructor
     */
    virtual ~LWRComposite();

    /**
     * The Constructor.
     */
    LWRComposite();

    /**
     * Returns the LWRComponent corresponding to the provided name or null if the name is not found.
     *
     * @param The name
     *
     * @return the component
     */
    std::shared_ptr<ICE_DS::Component> getComponent(const std::string name);

    /**
     * Returns a list of std::strings containing the names of all LWRComponents contained in this LWRComposite.
     *
     * @return the list of strings
     */
    std::vector<std::string> getComponentNames();

    /**
     * Removes a LWRComponent with the provided name from this LWRComposite.
     *
     * @param the name
     */
    void removeComponent(const std::string name);

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const LWRComposite& otherObject) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return The newly instantiated object.
     */
    std::shared_ptr<ICE_DS::Identifiable> clone();

    /**
     * Returns the list of writeable childen.
     *
     * @return List of writeable chidlren
     */
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > getWriteableChildren();

    /**
     * This operation reads IHdfReadable child objects. If this IHdfReadable has no IHdfReadable child objects, false is returned.
     *
     * @param the readable child
     *
     * @return true if successful, false otherwise
     */
    bool readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable);

    /**
     * This operation adds a child Component to a class that realizes the Composite interface. This operation should notify listeners that components have been added.
     *
     * @param the child to add
     */
    void addComponent(std::shared_ptr<ICE_DS::Component> child);

    /**
     * This operation removes a child Component to a class that realizes the Composite interface.
     *
     * @param the child id
     */
    void removeComponent(int childId);

    /**
     * This operation retrieves a child Component to a class that realizes the Composite interface.
     *
     * @param the child id
     *
     * @return the component
     */
    std::shared_ptr<ICE_DS::Component> getComponent(int childId);

    /**
     * This operations retrieves the number of child Components stored in an instance of a class that realizes the Composite interface.
     *
     * @return the number of components
     */
    int getNumberOfComponents();

    /**
     * This operation returns all of the Components stored in the Composite.
     *
     * @return the list of components
     */
    std::vector< std::shared_ptr<ICE_DS::Component> > getComponents();

};

}

#endif
