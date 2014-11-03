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

#ifndef TUBE_H
#define TUBE_H

#include "Ring.h"
#include "TubeType.h"
#include "Material.h"
#include <H5Cpp.h>
#include <memory>

namespace ICE_Reactor {

/**
 * The Tube class represents the hollow tubes in a FuelAssembly which allow for the insertion of discrete poison rodlets (Guide Tubes) and instrument thimbles (Instrument Tube).
 */
class Tube : public Ring {

private:

    /**
     * One of the TubeType enumeration values.
     */
    TubeType tubeType;

public:

    /**
     * The Copy Constructor
     */
    Tube(Tube & arg);

    /**
     * The Destructor
     */
    virtual ~Tube();

    /**
     * The nullary Constructor.
     */
    Tube();

    /**
     * The parameterized Constructor.
     *
     * @param the name
     */
    Tube(const std::string name);

    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param the tube type
     */
    Tube(const std::string name, TubeType tubeType);

    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param the tube type
     * @param the material
     * @param the height
     * @param the outerRadius
     */
    Tube(const std::string name, TubeType tubeType, std::shared_ptr<Material> material, double height, double outerRadius);

    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param the tube type
     * @param the material
     * @param the height
     * @param the innerRadius
     * @param the outerRadius
     */
    Tube(const std::string name, TubeType tubeType, std::shared_ptr<Material> material, double height, double innerRadius, double outerRadius);

    /**
     * Returns the TubeType enumeration value for this Tube.
     *
     * @return the tube type
     */
    TubeType getTubeType();

    /**
     * Sets the TubeType enumeration value for this Tube. Can not set to null.
     *
     * @param the tube type
     */
    void setTubeType(TubeType tubeType);

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const Tube& otherObject) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return the newly instantiated object
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

};

}

#endif
