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

#ifndef RING_H
#define RING_H

#include "LWRComponent.h"
#include "Material.h"
#include <H5Cpp.h>
#include <string>
#include <IHdfReadable.h>
#include <IHdfWriteable.h>


namespace ICE_Reactor {

/**
 * <p>The ring class represents a single instance of a material at a particular
 *  radial coordinate within a cylindrical location on the rod. The height
 *  variable on this class should uniformly represent the height from the
 *  bottom of the MaterialBlock (or Z coordinate displacement) to help
 *  compensate for varying types of materials across a cylindrical segment
 *  of a rod.</p>
 */
class Ring : public LWRComponent {

protected:


    /**
     * The height of this Ring, which must be greater than zero.
     */
    double height;



    /**
     * The inner radius of this Ring, which must be greater than or equal to zero and less than the outer radius.
     */
    double innerRadius;



    /**
     * The outer radius of this Ring, which must be greater than the innerRadius value and greater than 0.
     */
    double outerRadius;



    /**
     * The Material for this Ring.
     */
    std::shared_ptr<Material> material;



public:

    /**
     * The Copy Constructor
     */
    Ring(Ring & arg);

    /**
     * The Destructor
     */
    virtual ~Ring();

    /**
     * The nullary Constructor.
     */
    Ring();

    /**
     * A parameterized Constructor.
     *
     * @param the name
     */
    Ring(const std::string name);



    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param the material
     * @param the height
     * @param the outerRadius
     */
    Ring(const std::string name, std::shared_ptr<Material> material, double height, double outerRadius);

    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param the material
     * @param the height
     * @param the innerRadius
     * @param the outerRadius
     */
    Ring(const std::string name, std::shared_ptr<Material> material, double height, double innerRadius, double outerRadius);

    /**
     * Returns the height of this Ring.
     *
     * @return the height
     */
    double getHeight();

    /**
     * Sets the height of this Ring, which must be greater than zero.
     *
     * @param sets the height
     */
    void setHeight(double height);

    /**
     * Returns the inner radius of this Ring.
     *
     * @return the inner radius
     */
    double getInnerRadius();

    /**
     * Sets the inner radius of this Ring, which must be greater than or equal to zero and less than the outer radius.
     *
     * @param the inner radius to set
     */
    void setInnerRadius(double innerRadius);

    /**
     * Returns the outer radius for this Ring. Must be greater than 0 and the inner radius.
     *
     * @param the outerRadius
     */
    double getOuterRadius();

    /**
     * Sets the outer radius of this Ring, which must be greater than the innerRadius value.
     *
     * @param the outerRadius
     */
    void setOuterRadius(double outerRadius);

    /**
     * Returns the Material for this ring.
     *
     * @return the material
     */
    std::shared_ptr<Material> getMaterial();

    /*
     * Sets the material.  Can not set to null.
     *
     * @param the material to set
     */
    void setMaterial(std::shared_ptr<Material> material);

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const Ring& otherObject) const;

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
     * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
     *
     * @param The File
     * @param The Group
     *
     * @return True if successful, false otherwise
     */
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > getWriteableChildren();

    /**
     * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
     *
     * @param  The Group
     *
     * @return True if successful, false otherwise
     */
    bool readAttributes(std::shared_ptr<H5::Group> h5Group);

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
