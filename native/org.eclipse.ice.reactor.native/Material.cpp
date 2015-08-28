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

#include "Material.h"
#include <H5Cpp.h>
#include <string>
#include <H5Cpp.h>
#include "LWRComponent.h"
#include "MaterialType.h"
#include "UtilityOperations.h"
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <iostream>

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
Material::Material(Material & arg) : LWRComponent (arg) {

    // begin-user-code

    this->materialType = arg.materialType;

    // end-user-code
}

/**
 * The Destructor
 */
Material::~Material() {
    //TODO Auto-generated method stub
}

/**
 * The nullary Constructor.
 */
Material::Material() {

    // begin-user-code

    //Setup defaults
    this->name = "Material";
    this->id = 1;
    this->description = "Material's Description";
    this->materialType = SOLID;
    this->HDF5LWRTag = MATERIAL;

    // end-user-code

}

/**
 * A parameterized Constructor.
 *
 * @param the name
 */
Material::Material(const std::string name) {

    // begin-user-code

    //Setup defaults
    this->name = "Material";
    this->id = 1;
    this->description = "Material's Description";
    this->materialType = SOLID;
    this->HDF5LWRTag = MATERIAL;

    //Call setters
    Material::setName(name);

    // end-user-code

}

/**
 * A parameterized Constructor.
 *
 * @param the name
 * @param the material type
 */
Material::Material(const std::string name, MaterialType materialType) {

    // begin-user-code

    //Setup defaults
    this->name = "Material";
    this->id = 1;
    this->description = "Material's Description";
    this->materialType = SOLID;
    this->HDF5LWRTag = MATERIAL;

    //Call setters
    Material::setName(name);
    Material::setMaterialType(materialType);

    // end-user-code

}

/**
 * Sets the phase of this Material. Must be one of the enumeration values listed in MaterialType.
 *
 * @param The materialType to set
 */
void Material::setMaterialType(MaterialType materialType) {

    this->materialType = materialType;

}

/**
 * Returns the phase of this Material. Must be one of the enumeration values listed in MaterialType.
 *
 * @return the material type
 */
MaterialType Material::getMaterialType() {

    // begin-user-code

    return this->materialType;

    // end-user-code

}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool Material::operator==(const Material & otherObject) const {
    return LWRComponent::operator ==(otherObject) && this->materialType == otherObject.materialType;
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> Material::clone() {
    // begin-user-code

    // Local Declarations
    std::shared_ptr<Material> component(new Material (*this));

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
bool Material::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    bool flag = true;

    flag &= LWRComponent::writeAttributes(h5File, h5Group);
    flag &= ICE_IO::HdfWriterFactory::writeStringAttribute(h5File, h5Group, "materialType", UtilityOperations::toStringMaterialType(this->materialType));

    return flag;


    // end-user-code

}

/**
 * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
 *
 * @param  The Group
 *
 * @return True if successful, false otherwise
 */
bool Material::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    //Local Declarations
    MaterialType type;

    //If group is null or the super read attributes is false, return false
    if(h5Group.get() == NULL || LWRComponent::readAttributes(h5Group) == false) return false;

    //Try to get values.  If failure, return false;
    try {
        type  = UtilityOperations::fromStringMaterialType(ICE_IO::HdfReaderFactory::readStringAttribute(h5Group, "materialType"));
    } catch (...) {
        return false;
    }

    //Setup the values
    this->materialType = type;

    //Operation passed!
    return true;

    // end-user-code

}
