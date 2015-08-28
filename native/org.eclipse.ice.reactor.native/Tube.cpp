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
#include "Tube.h"
#include <H5Cpp.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <iostream>
#include <memory>
#include "UtilityOperations.h"
#include <IHdfReadable.h>
#include <IHdfWriteable.h>
#include "Material.h"
#include "HDF5LWRTagType.h"
#include "TubeType.h"

using namespace ICE_Reactor;

Tube::Tube(Tube & arg) : Ring(arg) {

    //begin-user-code

    //Copy contents
    this->tubeType = arg.tubeType;

    //end-user-code

}
Tube::~Tube() {
    //TODO Auto-generated method stub
}
Tube::Tube() {

    //begin-user-code

    //Setup Defaults
    this->name = "Tube";
    this->id= 1;
    this->description= "Tube's Description";
    this->innerRadius = 0;
    this->outerRadius = 1;
    this->height = 1;
    this->tubeType = GUIDE;
    this->material = std::shared_ptr<Material> (new Material());
    this->HDF5LWRTag = TUBE;

    //end-user-code

}
Tube::Tube(const std::string name) {

    //begin-user-code

    //Setup Defaults
    this->name = "Tube";
    this->id= 1;
    this->description= "Tube's Description";
    this->innerRadius = 0;
    this->outerRadius = 1;
    this->height = 1;
    this->tubeType = GUIDE;
    this->material = std::shared_ptr<Material> (new Material());
    this->HDF5LWRTag = TUBE;

    //Run setters
    Tube::setName(name);

    //end-user-code

}
Tube::Tube(const std::string name, TubeType tubeType) {
    //begin-user-code

    //Setup Defaults
    this->name = "Tube";
    this->id= 1;
    this->description= "Tube's Description";
    this->innerRadius = 0;
    this->outerRadius = 1;
    this->height = 1;
    this->tubeType = GUIDE;
    this->material = std::shared_ptr<Material> (new Material());
    this->HDF5LWRTag = TUBE;


    //Run setters
    Tube::setName(name);
    Tube::setTubeType(tubeType);

    //end-user-code
}
Tube::Tube(const std::string name, TubeType tubeType, std::shared_ptr<Material> material, double height, double outerRadius) {
    //begin-user-code

    //Setup Defaults
    this->name = "Tube";
    this->id= 1;
    this->description= "Tube's Description";
    this->innerRadius = 0;
    this->outerRadius = 1;
    this->height = 1;
    this->tubeType = GUIDE;
    this->material = std::shared_ptr<Material> (new Material());
    this->HDF5LWRTag = TUBE;


    //Run setters
    Tube::setName(name);
    Tube::setTubeType(tubeType);
    Tube::setMaterial(material);
    Tube::setHeight(height);
    Tube::setOuterRadius(outerRadius);

    //end-user-code
}
Tube::Tube(const std::string name, TubeType tubeType, std::shared_ptr<Material> material, double height, double innerRadius, double outerRadius) {
    //begin-user-code

    //Setup Defaults
    this->name = "Tube";
    this->id= 1;
    this->description= "Tube's Description";
    this->innerRadius = 0;
    this->outerRadius = 1;
    this->height = 1;
    this->tubeType = GUIDE;
    this->material = std::shared_ptr<Material> (new Material());
    this->HDF5LWRTag = TUBE;


    //Run setters
    Tube::setName(name);
    Tube::setTubeType(tubeType);
    Tube::setMaterial(material);
    Tube::setHeight(height);
    Tube::setOuterRadius(outerRadius);
    Tube::setInnerRadius(innerRadius);

    //end-user-code
}
TubeType Tube::getTubeType() {

    // begin-user-code

    return this->tubeType;

    // end-user-code
}
void Tube::setTubeType(TubeType tubeType) {

    //begin-user-code

    this->tubeType = tubeType;


    return;
    // end-user-code

}
bool Tube::operator==(const Tube& otherObject) const {

    // begin-user-code

    return Ring::operator ==(otherObject) && this->tubeType == otherObject.tubeType;

    // end-user-code
}

std::shared_ptr<ICE_DS::Identifiable> Tube::clone() {
    // begin-user-code

    // Local Declarations
    std::shared_ptr<Tube> component(new Tube (*this));

    // Return the newly instantiated object
    return component;

    // end-user-code
}
bool Tube::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    bool flag = true;

    flag &= Ring::writeAttributes(h5File, h5Group);
    flag &= ICE_IO::HdfWriterFactory::writeStringAttribute(h5File, h5Group, "tubeType", UtilityOperations::toStringTubeType(this->tubeType));

    return flag;


    // end-user-code
}
bool Tube::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    //Local Declarations
    TubeType type;

    //If group is null or the super read attributes is false, return false
    if(h5Group.get() == NULL || Ring::readAttributes(h5Group) == false) return false;

    //Try to get values.  If failure, return false;
    try {
        type  = UtilityOperations::fromStringTubeType(ICE_IO::HdfReaderFactory::readStringAttribute(h5Group, "tubeType"));
    } catch (...) {
        return false;
    }

    //Setup the values
    this->tubeType = type;

    //Operation passed!
    return true;

    // end-user-code

}
