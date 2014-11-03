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

#ifndef INCOREINSTRUMENT_H
#define INCOREINSTRUMENT_H

#include "../LWRComponent.h"
#include "../Ring.h"
#include <H5Cpp.h>
#include <string>
#include <vector>
#include <memory>
#include <ICEObject/Identifiable.h>
#include <IDataProvider.h>
#include <IHdfWriteable.h>
#include <IHdfReadable.h>
#include "../HDF5LWRTagType.h"
#include <IHdfWriteable.h>

namespace ICE_Reactor {

/**
 * <p>The IncoreInstrument class represents instruments (or detectors) that are
 * used for power distribution monitoring inside of a PWReactor.  This class
 * contains a ring of data designed to be the "thimble" for material
 * composition on this class.</p>
 */
class IncoreInstrument : public LWRComponent {

private:

    /**
     * An empty thimble tube used a boundary between the detector and the reactor.
     */
    std::shared_ptr<Ring> thimble;

public:

    /**
     * The Copy Constructor
     */
    IncoreInstrument(IncoreInstrument & arg);

    /**
     * The Destructor
     */
    virtual ~IncoreInstrument();

    /**
     * The nullary Constructor.
     */
    IncoreInstrument();

    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param the thimble
     */
    IncoreInstrument(const std::string name, std::shared_ptr<Ring> thimble);

    /**
     * Sets an empty thimble tube used a boundary between the detector and the reactor.
     *
     * @param the thimble to set
     */
    void setThimble(std::shared_ptr<Ring> thimble);

    /**
     * Returns an empty thimble tube used a boundary between the detector and the reactor.
     *
     * @return the thimble
     */
    std::shared_ptr<Ring> getThimble();

    /**
     * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
     *
     * @param The File
     * @param The Group
     *
     * @return True if successful, false otherwise
     */
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > getWriteableChildren();

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const IncoreInstrument& otherObject) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return the newly instantiated object
     */
    std::shared_ptr<ICE_DS::Identifiable> clone();

    /**
     * This operation reads IHdfReadable child objects. If this IHdfReadable has no IHdfReadable child objects, false is returned.
     *
     * @param the readable child
     *
     * @return true if successful, false otherwise
     */
    bool readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable);

};

}

#endif
