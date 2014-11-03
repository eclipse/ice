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

#ifndef LWREACTOR_H
#define LWREACTOR_H

#include "LWRComposite.h"
#include <H5Cpp.h>
#include <updateableComposite/Component.h>
#include <vector>
#include <memory>
#include <ICEObject/Identifiable.h>


namespace ICE_Reactor {

/**
 * The LWReactor class represents any Light Water Nuclear Reactor.
 */
class LWReactor : public LWRComposite {

protected:

    /**
     * The size.  Defaults to 1 if not set correctly in the constructor.
     */
    int size;

public:

    /**
     * Copy constructor
     */
    LWReactor(LWReactor & arg);

    /**
     * Destructor
     */
    virtual ~LWReactor();

    /**
     * A parameterized constructor.
     */
    LWReactor(int size);

    /**
     * An operation that overrides the LWRComposite's operation. This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
     *
     * @param The component to add
     */
    void addComponent(std::shared_ptr<ICE_DS::Component> component);

    /**
     * An operation that overrides the LWRComposite's operation. This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
     *
     * @param the child Id
     */
    void removeComponent(int childId);

    /**
     * An operation that overrides the LWRComposite's operation. This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
     *
     * @param the name
     */
    void removeComponent(const std::string name);

    /**
     * Returns the size.
     *
     * @return the size
     */
    int getSize();

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const LWReactor& otherObject) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return The newly instantiated object.
     */
    std::shared_ptr<ICE_DS::Identifiable> clone();

    /**
     * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
     *
     * @param The File
     * @param The Group
     *
     * @return True if successful, false otherwise
     */
    bool writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

    /**
     * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
     *
     * @param  The Group
     *
     * @return True if successful, false otherwise
     */
    bool readAttributes(std::shared_ptr<H5::Group> h5Group);

};  //end class LWReactor

}

#endif
