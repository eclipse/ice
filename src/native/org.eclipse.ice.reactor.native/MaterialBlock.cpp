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
#include "MaterialBlock.h"
#include "Ring.h"
#include <H5Cpp.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <iostream>
#include <memory>
#include <vector>
#include <memory>

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
MaterialBlock::MaterialBlock(MaterialBlock & arg) : LWRComponent(arg) {

    // begin-user-code

    //Deep copy rings
    this->rings.clear();

    //Iterate over the rings on the other object and get contents
    for(int i = 0; i < arg.rings.size(); i++) {

        //Clone for getting rings and tubes!
        std::shared_ptr<Identifiable> identRing = arg.rings[i].get()->clone();

        //Dynamic cast up to a ring (which will preserve the clone operation on MaterialBlock without calling new!
        std::shared_ptr<Ring> ring = std::dynamic_pointer_cast<Ring> (identRing);

        //Add to list
        this->rings.push_back(ring);
    }

    //Copy position
    this->pos = arg.pos;

    // end-user-code

}

/**
 * The Destructor
 */
MaterialBlock::~MaterialBlock() {
    //TODO Auto-generated method stub
}

/**
 * The nullary Constructor.
 */
MaterialBlock::MaterialBlock() {

    this->name = "MaterialBlock 1";
    this->description = "MaterialBlock 1's Description";
    this->id = 1;
    this->HDF5LWRTag = MATERIALBLOCK;
    this->pos = 0.0;

}

/**
 * Adds a Ring to this MaterialBlock's ring collection. If the ring could not be successfully added, then false is returned. This could be due to a ring existing at within the inner and outer radius of an existing Ring object in the MaterialBlock.
 *
 * @param ring to add
 */
bool MaterialBlock::addRing(std::shared_ptr<Ring> ring) {

    // begin-user-code

    //Check for a null ring
    if (ring) {
        //Make sure no overlapping rings
        for(int i = 0; i < this->rings.size(); i++) {

            //Do not add ring if another ring overlaps
            if(this->rings[i].get()->getOuterRadius() > ring.get()->getInnerRadius() && this->rings[i].get()->getInnerRadius() < ring.get()->getOuterRadius()) {
                return false;
            }
        }

    } else {
        return false;
    }

    //We could not locate an overlapping ring
    //therefore add the new ring
    this->rings.push_back(ring);

    return true;
    // end-user-code

}

/**
 * Returns the Ring located at the provided radius value or null if one could not be found.
 *
 * @param ring at radius
 */
std::shared_ptr<Ring> MaterialBlock::getRing(double radius) {

    //begin-user-code

    //Iterate over the list
    for(int i = 0; i < this->rings.size(); i++) {

        //If the ring has the same or within bounds of radius, return that ring
        if(radius <= this->rings[i].get()->getOuterRadius() && radius >= this->rings[i].get()->getInnerRadius()) {
            return this->rings[i];
        }
    }

    //Null ring
    std::shared_ptr<Ring> ring;

    //No ring found matching that description.  Returning a null ring
    return ring;

    //end-user-code
}

/**
 * Returns the Ring with the provided name or null if one could not be found.
 *
 * @param the name of the ring
 *
 * @return the ring at the name
 */
std::shared_ptr<Ring> MaterialBlock::getRing(const std::string ringName) {

    //begin-user-code
    //Null ring
    std::shared_ptr<Ring> ring;

    if(ringName.empty() == true) {
        return ring;
    }

    //Iterate over the list
    for(int i = 0; i < this->rings.size(); i++) {

        //If the ring has the same name, return it
        if(this->rings[i].get()->getName().compare(ringName) == 0) {
            return this->rings[i];
        }
    }

    //No ring found matching that description.  Returning a null ring
    return ring;

    //end-user-code

}

/**
 * Returns a list of Rings ordered by ascending radii.
 *
 * @return list of rings
 */
std::vector< std::shared_ptr< Ring > >MaterialBlock::getRings() {

    //begin-user-code

    //Return a shallow copy of the rings

    std::vector< std::shared_ptr < Ring> > list;

    for(int i = 0; i < this->rings.size(); i++) {
        list.push_back(this->rings[i]);
    }

    //Return the list
    return list;

    //end-user-code

}

/**
 * Removes the Ring from this MaterialBlock's ring collection that has the provided name. Returns true, if the Ring was successfully removed.
 *
 * @param name of ring
 *
 * @return true if successful, false otherwise
 */
bool MaterialBlock::removeRing(const std::string ringName) {

    //begin-user-code
    if(ringName.empty() == true) {
        return false;
    }

    //Iterate over the list
    for(int i = 0; i < this->rings.size(); i++) {

        //If the ring has the same name, remove and return true
        if(this->rings[i].get()->getName().compare(ringName) == 0) {
            this->rings.erase(this->rings.begin() + i);
            return true;
        }
    }

    //Nothing found.  Return false

    return false;

    //end-user-code
}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool MaterialBlock::operator ==(const MaterialBlock& otherObject) const {
    //begin-user-code

    if(otherObject.rings.size() != this->rings.size()|| LWRComponent::operator ==(otherObject) == false || this->pos != otherObject.pos) return false;

    //Check the rings
    for(int i = 0; i < this->rings.size(); i++) {
        //If the individual rings are not equal, return false
        if(this->rings[i].get()->operator ==(*otherObject.rings[i].get()) == false) {
            return false;
        }
    }

    //Everything good.  Return true
    return true;

    // end-user-code
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> MaterialBlock::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<MaterialBlock> component(new MaterialBlock (*this));

    // Return the newly instantiated object
    return component;

    // end-user-code

}

/**
 * Returns the list of writeable childen.
 *
 * @return List of writeable chidlren
 */
std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > MaterialBlock::getWriteableChildren() {
    // begin-user-code

    //Get the children in super
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > children = LWRComponent::getWriteableChildren();


    //Add the rings to children
    for(int i = 0; i < this->rings.size(); i++) {
        children.push_back(this->rings[i]);
    }

    return children;
    // end-user-code
}

/**
 * This operation reads IHdfReadable child objects. If this IHdfReadable has no IHdfReadable child objects, false is returned.
 *
 * @param the readable child
 *
 * @return true if successful, false otherwise
 */
bool MaterialBlock::readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable) {

    // begin-user-code

    //If the child is null, return false
    if(iHdfReadable.get() == NULL) {
        return false;
    }

    //Cast the child into a Material - may throw a class cast exception
    try {

        //Dynamic cast to material
        Ring *newType = dynamic_cast<Ring *> (iHdfReadable.get());

        //If the type is not material, return false
        if(newType == 0) {
            return false;
        }

        //Dynamic cast up to a ring (which will preserve the clone operation on implemented dervied classes without calling new!
        std::shared_ptr<Ring> ring = std::dynamic_pointer_cast<Ring> (iHdfReadable);

        //Add ring
        MaterialBlock::addRing(ring);

    } catch (...) {
        return false;
    }

    //Return true for success
    return true;

    // end-user-code

}

/**
 * <p>writes the hdf5 attributes.</p>
 *
 * @param h5File
 * @param h5Group
 * @return
 */
bool MaterialBlock::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code
    bool flag = true;

    flag &= LWRComponent::writeAttributes(h5File, h5Group);
    flag &= ICE_IO::HdfWriterFactory::writeDoubleAttribute(h5File, h5Group,
            "position", this->pos);

    return flag;
    // end-user-code
}

/**
 * <p>Reads the hdf5 attributes.</p>
 *
 * @param h5Group
 * @return
 */
bool MaterialBlock::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    //Local Declarations
    double position;


    //If group is null or the super read attributes is false, return false
    if(h5Group.get() == NULL || LWRComponent::readAttributes(h5Group) == false) return false;

    //Try to get values.  If failure, return false;
    try {
        position = ICE_IO::HdfReaderFactory::readDoubleAttribute(h5Group, "position");
    } catch (...) {
        return false;
    }

    //Setup the values
    this->pos = position;

    //Operation passed!
    return true;

    // end-user-code
}

/**
 * <p>Sets the position.</p>
 *
 * @param pos <p>the position to set</p>
 */
void MaterialBlock::setPosition(double pos) {
	// begin-user-code
	if (pos >= 0)
		this->pos = pos;

	// end-user-code
}

/**
 * <p>Gets the position</p>
 *
 * @return <p>The position set.</p>
 */
double MaterialBlock::getPosition() {
	// begin-user-code
	return this->pos;
	// end-user-code
}
