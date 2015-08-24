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

#ifndef MATERIAL_H
#define MATERIAL_H

#include "LWRComponent.h"
#include "MaterialType.h"
#include <H5Cpp.h>

namespace ICE_Reactor {

/**
 * <p>The Material class is a representation of any material property of
 * any class within an LWReactor.  The setName for this class should represent
 * the material name.  The Material Class can also indicate what type of
 * material is defined (solid, liquid, gas).  </p>
 */
class Material : public LWRComponent {

private:

    /**
     * The phase of this Material. Must be one of the enumeration values listed in MaterialType.
     */
    MaterialType materialType;

public:

    /**
     * The Copy Constructor
     */

    Material(Material & arg);

    /**
     * The Destructor
     */
    virtual ~Material();

    /**
     * The nullary Constructor.
     */
    Material();

    /**
     * A parameterized Constructor.
     *
     * @param the name
     */
    Material(const std::string name);

    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param the material type
     */
    Material(const std::string name, MaterialType materialType);

    /**
     * Sets the phase of this Material. Must be one of the enumeration values listed in MaterialType.
     *
     * @param The materialType to set
     */
    void setMaterialType(MaterialType materialType);

    /**
     * Returns the phase of this Material. Must be one of the enumeration values listed in MaterialType.
     *
     * @return the material type
     */
    MaterialType getMaterialType();

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const Material & otherObject) const;

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
