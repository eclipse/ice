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

#ifndef LWRROD_H
#define LWRROD_H

#include "LWRComponent.h"
#include "Material.h"
#include "MaterialBlock.h"
#include "Ring.h"
#include <H5Cpp.h>
#include "MaterialType.h"
#include <memory>

namespace ICE_Reactor {

/**
 * <p>The LWRRod class is a generalized class representing a basic rod as a
 * stack contained by a ring (aka the clad).  A LWRRod should be considered the
 * basis for all "rod-tyoes" implemented within a reactor.  Details concering
 * material compositions should be taken care of on the stacks and collections
 * of rings on the stack.
 *
 * Please keep in mind when dealing with the positions on the MaterialBlock,
 * they need to be unique.  If they are not unique, then Java linking side will
 * overwrite the material blocks and cause issues.  </p>
 */
class LWRRod : public LWRComponent {

private:

    /**
     * An annular Ring which surrounds this LWRRod's Stack object.
     *
     */
    std::shared_ptr<Ring> clad;

    /**
     * A Material of MaterialType.GAS that fills the voids within this LWRRod.
     */
    std::shared_ptr< Material> fillGas;

    /**
     * The pressure of the fillGas Material.  Can not be less than or equal to 0.
     */
    double pressure;

    /**
     * The Stack object within this LWRRod.
     */
    std::shared_ptr< std::vector< std::shared_ptr<MaterialBlock> > > materialBlocks;

public:

    /**
     * The Copy Constructor
     */
    LWRRod(LWRRod & arg);

    /**
     * The Destructor
     */
    virtual ~LWRRod();

    /**
     * The Nullary Constructor
     */
    LWRRod();

    /**
     * A parameterized Constructor.
     *
     * @param the name
     */
    LWRRod(const std::string name);

    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param fillGas
     * @param pressure
     * @param stack
     */
    LWRRod(const std::string name, std::shared_ptr<Material> fillGas, double pressure, std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > materialBlocks);

    /**
     * Returns a Material of MaterialType.GAS that fills the voids within this LWRRod.
     *
     * @return the fillGas
     */
    std::shared_ptr<Material> getFillGas();

    /**
     * Sets the Material of MaterialType.GAS that fills the voids within this LWRRod.  Can not be set to null.
     *
     * @param the fillGas
     */
    void setFillGas(std::shared_ptr<Material> fillGas);

    /**
     * Sets the pressure of the fillGas Material.  Can not be set less than or equal to 0.
     *
     * @param the pressure
     */
    void setPressure(double pressure);

    /**
     * Returns the pressure of the fillGas Material.
     *
     * @param the pressure
     */
    double getPressure();

    /**
     * Returns the Stack object within this LWRRod.
     *
     * @return the stack
     */
    std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > getMaterialBlocks();

    /**
     * Sets the Stack object within this LWRRod.  Can not be set null.
     *
     * @param the stack to set
     */
    void setMaterialBlocks(std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > >  materialBlocks);

    /**
     * Returns the clad object of this LWRRod object, if set or null.
     *
     * @return the clad
     */
    std::shared_ptr<Ring> getClad();

    /**
     * Sets the clad object for this LWRRod. It can not be set to null.
     *
     * @param sets the Clad
     */
    void setClad(std::shared_ptr<Ring> clad);

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const LWRRod& otherObject) const;

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
     * Returns the list of writeable childen.
     *
     * @return List of writeable chidlren
     */
    std::vector< std::shared_ptr< ICE_IO::IHdfWriteable > > getWriteableChildren();

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
