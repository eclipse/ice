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

#include "Ring.h"
#include <H5Cpp.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <iostream>
#include <string>
#include "Material.h"
#include "MaterialType.h"

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
Ring::Ring(Ring & arg) : LWRComponent(arg) {

    // begin-user-code

    //Copy local contents
    this->height = arg.height;
    this->outerRadius = arg.outerRadius;
    this->innerRadius = arg.innerRadius;
    this->material = std::shared_ptr<Material> (new Material (*arg.material.get()));

    // end-user-code
}

/**
 * The Destructor
 */
Ring::~Ring() {
    //TODO Auto-generated method stub
}

/**
 * The nullary Constructor.
 */
Ring::Ring() {

    // begin-user-code

    //Setup Defaults
    this->name = "Ring";
    this->description = "Ring's Description";
    this->id = 1;
    this->height = 1.0;
    this->outerRadius = 1.0;
    this->innerRadius = 0.0;
    this->material = std::shared_ptr<Material> (new Material());
    this->HDF5LWRTag = RING;

    // end-user-code

}

/**
 * A parameterized Constructor.
 *
 * @param the name
 */
Ring::Ring(const std::string name)  {

    // begin-user-code

    //Setup Defaults
    this->name = "Ring";
    this->description = "Ring's Description";
    this->id = 1;
    this->height = 1.0;
    this->outerRadius = 1.0;
    this->innerRadius = 0.0;
    this->material = std::shared_ptr<Material> (new Material());
    this->HDF5LWRTag = RING;

    //Call setter on name
    Ring::setName(name);

    // end-user-code

}

/**
 * A parameterized Constructor.
 *
 * @param the name
 * @param the material
 * @param the height
 * @param the outerRadius
 */
Ring::Ring(const std::string name, std::shared_ptr<Material> material, double height, double outerRadius) {

    // begin-user-code

    //Setup Defaults
    this->name = "Ring";
    this->description = "Ring's Description";
    this->id = 1;
    this->height = 1.0;
    this->outerRadius = 1.0;
    this->innerRadius = 0.0;
    this->material = std::shared_ptr<Material> (new Material());
    this->HDF5LWRTag = RING;

    //Call setters
    Ring::setName(name);
    Ring::setMaterial(material);
    Ring::setHeight(height);
    Ring::setOuterRadius(outerRadius);

    // end-user-code

}

/**
 * A parameterized Constructor.
 *
 * @param the name
 * @param the material
 * @param the height
 * @param the innerRadius
 * @param the outerRadius
 */
Ring::Ring(const std::string name, std::shared_ptr<Material> material, double height, double innerRadius, double outerRadius) {

    // begin-user-code

    //Setup Defaults
    this->name = "Ring";
    this->description = "Ring's Description";
    this->id = 1;
    this->height = 1.0;
    this->outerRadius = 1.0;
    this->innerRadius = 0.0;
    this->material = std::shared_ptr<Material> (new Material());
    this->HDF5LWRTag = RING;

    //Call Setters
    Ring::setName(name);
    Ring::setMaterial(material);
    Ring::setHeight(height);
    Ring::setOuterRadius(outerRadius);
    Ring::setInnerRadius(innerRadius);

    // end-user-code

}

/**
 * Returns the height of this Ring.
 *
 * @return the height
 */
double Ring::getHeight() {

    // begin-user-code

    return this->height;

    // end-user-code

}

/**
 * Sets the height of this Ring, which must be greater than zero.
 *
 * @param sets the height
 */
void Ring::setHeight(double height) {

    // begin-user-code

    //If the height is not less than zero, valid
    if (height > 0.0) {
        this->height = height;
    }

    return;

    // end-user-code

}

/**
 * Returns the inner radius of this Ring.
 *
 * @return the inner radius
 */
double Ring::getInnerRadius() {

    // begin-user-code

    return this->innerRadius;

    // end-user-code

}

/**
 * Sets the inner radius of this Ring, which must be greater than or equal to zero and less than the outer radius.
 *
 * @param the inner radius to set
 */
void Ring::setInnerRadius(double innerRadius) {

    // begin-user-code

    //If the inner Radius is greater than or equal to 0 AND it is less than current outer radius
    if (innerRadius >= 0 && innerRadius < this->outerRadius) {
        this->innerRadius = innerRadius;
    }

    return;

    // end-user-code

}

/**
 * Returns the outer radius for this Ring. Must be greater than 0 and the inner radius.
 *
 * @param the outerRadius
 */
double Ring::getOuterRadius() {

    // begin-user-code

    return this->outerRadius;

    // end-user-code

}

/**
 * Sets the outer radius of this Ring, which must be greater than the innerRadius value.
 *
 * @param the outerRadius
 */
void Ring::setOuterRadius(double outerRadius) {

    // begin-user-code

    //If the outer Radius is greater than  0 AND it is greater than current inner radius
    if (outerRadius > 0 && outerRadius > this->innerRadius) {
        this->outerRadius = outerRadius;
    }

    return;

    // end-user-code
}

/**
 * Returns the Material for this ring.
 *
 * @return the material
 */
std::shared_ptr<Material> Ring::getMaterial() {

    // begin-user-code

    return this->material;

    // end-user-code
}

/*
 * Sets the material.  Can not set to null.
 *
 * @param the material to set
 */
void Ring::setMaterial(std::shared_ptr<Material> material) {

    // begin-user-code

    //If the material is not null
    if (material.get() != NULL) {
        this->material = material;
    }

    return;

    // end-user-code

}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool Ring::operator ==(const Ring& otherObject) const {
    // begin-user-code

    //Check equality
    bool retVal = LWRComponent::operator ==(otherObject) &&
                  this->innerRadius == otherObject.innerRadius &&
                  this->height == otherObject.height &&
                  this->outerRadius == otherObject.outerRadius &&
                  this->material.get()->operator ==(*otherObject.material.get());

    return retVal;

    // end-user-code
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> Ring::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<Ring> component(new Ring (*this));

    // Return the newly instantiated object
    return component;

    // end-user-code

}

/**
 * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
 *
 * @param The File
 * @param The Group
 *
 * @return True if successful, false otherwise
 */
bool Ring::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code
    bool flag = true;

    flag &= LWRComponent::writeAttributes(h5File, h5Group);
    flag &= ICE_IO::HdfWriterFactory::writeDoubleAttribute(h5File, h5Group,
            "height", height);
    flag &= ICE_IO::HdfWriterFactory::writeDoubleAttribute(h5File, h5Group,
            "innerRadius", innerRadius);
    flag &= ICE_IO::HdfWriterFactory::writeDoubleAttribute(h5File, h5Group,
            "outerRadius", outerRadius);

    return flag;
    // end-user-code

}

/**
 * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
 *
 * @param The File
 * @param The Group
 *
 * @return True if successful, false otherwise
 */
std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > Ring::getWriteableChildren() {

    // begin-user-code

    //Get the children in super
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > children = LWRComponent::getWriteableChildren();


    //Add the material to children
    children.push_back(this->material);

    return children;
    // end-user-code

}

/**
 * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
 *
 * @param  The Group
 *
 * @return True if successful, false otherwise
 */
bool Ring::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    //Local Declarations
    double height;
    double innerRadius;
    double outerRadius;

    //If group is null or the super read attributes is false, return false
    if(h5Group.get() == NULL || LWRComponent::readAttributes(h5Group) == false) return false;

    //Try to get values.  If failure, return false;
    try {
        height = ICE_IO::HdfReaderFactory::readDoubleAttribute(h5Group, "height");
        innerRadius = ICE_IO::HdfReaderFactory::readDoubleAttribute(h5Group, "innerRadius");
        outerRadius = ICE_IO::HdfReaderFactory::readDoubleAttribute(h5Group, "outerRadius");
    } catch (...) {
        return false;
    }

    //Setup the values
    this->height = height;
    this->innerRadius = innerRadius;
    this->outerRadius = outerRadius;

    //Operation passed!
    return true;

    // end-user-code

}

/**
 * This operation reads IHdfReadable child objects. If this IHdfReadable has no IHdfReadable child objects, false is returned.
 *
 * @param the readable child
 *
 * @return true if successful, false otherwise
 */
bool Ring::readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable) {

    // begin-user-code

    //If the child is null, return false
    if(iHdfReadable.get() == NULL) {
        return false;
    }

    //Cast the child into a Material - may throw a class cast exception
    try {

        //Dynamic cast to material
        Material *newType = dynamic_cast<Material *> (iHdfReadable.get());

        //If the type is not material, return false
        if(newType == 0) {
            free (newType);
            return false;
        }

        //Cast it as a new material
        std::shared_ptr<Material> childComponent (new Material(*newType));

        //Add the new child
        this->material = childComponent;

    } catch (...) {
        return false;
    }

    //Return true for success
    return true;

    // end-user-code

}
