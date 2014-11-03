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

#include "LWRRod.h"
#include <H5Cpp.h>
#include "MaterialType.h"
#include "UtilityOperations.h"
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include "HDF5LWRTagType.h"
#include "UtilityOperations.h"
#include <iostream>

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
LWRRod::LWRRod(LWRRod & arg) : LWRComponent(arg) {

    //begin-user-code

    this->pressure = arg.pressure;

    //Clone - fillGas
    std::shared_ptr<ICE_DS::Identifiable> identMaterial = arg.fillGas.get()->clone();
    //Dynamic cast up (which will preserve the clone operation without calling new!
    std::shared_ptr<Material> fillGas = std::dynamic_pointer_cast<Material> (identMaterial);
    this->fillGas = fillGas;

    //Clone - MaterialBlock
    this->materialBlocks = std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > (new std::vector < std::shared_ptr < MaterialBlock > >());

    for(int i = 0; i < arg.materialBlocks.get()->size(); i++) {

    	std::shared_ptr<ICE_DS::Identifiable> identMaterialBlock = arg.materialBlocks.get()->at(i).get()->clone();
    	this->materialBlocks.get()->push_back(std::dynamic_pointer_cast<MaterialBlock> (identMaterialBlock));
    }

    //Clone - clad
    std::shared_ptr<ICE_DS::Identifiable> identRing = arg.clad.get()->clone();
    //Dynamic cast up (which will preserve the clone operation without calling new!
    std::shared_ptr<Ring> ring = std::dynamic_pointer_cast<Ring> (identRing);
    this->clad = ring;

    return;

    //end-user-code

}

/**
 * The Destructor
 */
LWRRod::~LWRRod() {
    //TODO Auto-generated method stub
}

/**
 * The Nullary Constructor
 */
LWRRod::LWRRod() {
    // begin-user-code

    //Set default LWRComponent Values
    this->name = "LWRRod";
    this->description = "LWRRod's Description";
    this->id = 1;

    //Set default values for privates;
    this->pressure = 2200.00;
    std::shared_ptr<Material> cladMaterial (new Material("Zirc", SOLID));
    this->clad = std::shared_ptr<Ring> (new Ring("Clad", cladMaterial, -1.0, -1.0));
    this->fillGas = std::shared_ptr<Material> (new Material("Void", GAS));

    //Setup MaterialBlocks
    this->materialBlocks = std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > (new std::vector < std::shared_ptr < MaterialBlock > >());

    //Setup LWRComponentType to correct type
    this->HDF5LWRTag = LWRROD;

    // end-user-code
}

/**
 * A parameterized Constructor.
 *
 * @param the name
 */
LWRRod::LWRRod(const std::string name) {

    // begin-user-code

    //Set default LWRComponent Values
    this->name = "LWRRod";
    this->description = "LWRRod's Description";
    this->id = 1;

    //Set default values for privates;
    this->pressure = 2200.00;
    std::shared_ptr<Material> cladMaterial (new Material("Zirc", SOLID));
    this->clad = std::shared_ptr<Ring> (new Ring("Clad", cladMaterial, -1.0, -1.0));
    this->fillGas = std::shared_ptr<Material> (new Material("Void", GAS));
    
    //Setup MaterialBlocks
    this->materialBlocks = std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > (new std::vector < std::shared_ptr < MaterialBlock > > () );

    //Setup LWRComponentType to correct type
    this->HDF5LWRTag = LWRROD;

    //Call setter
    LWRRod::setName(name);

    // end-user-code

}

/**
 * A parameterized Constructor.
 *
 * @param the name
 * @param fillGas
 * @param pressure
 * @param stack
 */
LWRRod::LWRRod(const std::string name, std::shared_ptr<Material> fillGas, double pressure, std::shared_ptr < std::vector < std::shared_ptr< MaterialBlock > > > materialBlocks) {

    // begin-user-code

    //Set default LWRComponent Values
    this->name = "LWRRod";
    this->description = "LWRRod's Description";
    this->id = 1;

    //Set default values for privates;
    this->pressure = 2200.00;
    std::shared_ptr<Material> cladMaterial (new Material("Zirc", SOLID));
    this->clad = std::shared_ptr<Ring> (new Ring("Clad", cladMaterial, -1.0, -1.0));
    this->fillGas = std::shared_ptr<Material> (new Material("Void", GAS));

    //Setup LWRComponentType to correct type
    this->HDF5LWRTag = LWRROD;
    
    //Setup MaterialBlocks
    this->materialBlocks = std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > (new std::vector < std::shared_ptr < MaterialBlock > > () );

    //Call setter
    LWRRod::setName(name);
    LWRRod::setFillGas(fillGas);
    LWRRod::setPressure(pressure);
    LWRRod::setMaterialBlocks(materialBlocks);
    // end-user-code

}

/**
 * Returns a Material of MaterialType.GAS that fills the voids within this LWRRod.
 *
 * @return the fillGas
 */
std::shared_ptr<Material> LWRRod::getFillGas() {

    // begin-user-code

    return this->fillGas;

    // end-user-code

}

/**
 * Sets the Material of MaterialType.GAS that fills the voids within this LWRRod.  Can not be set to null.
 *
 * @param the fillGas
 */
void LWRRod::setFillGas(std::shared_ptr<Material> fillGas) {

    // begin-user-code

    // if fillGas is not null
    if (fillGas.get() != NULL) {
        this->fillGas = fillGas;
    }

    return;

    // end-user-code

}

/**
 * Sets the pressure of the fillGas Material.  Can not be set less than or equal to 0.
 *
 * @param the pressure
 */
void LWRRod::setPressure(double pressure) {

    // begin-user-code

    //if pressure is not 0 or negative
    if (pressure > 0.0) {
        this->pressure = pressure;
    }

    return;

    // end-user-code

}

/**
 * Returns the pressure of the fillGas Material.
 *
 * @param the pressure
 */
double LWRRod::getPressure() {

    // begin-user-code

    return this->pressure;

    // end-user-code

}

/**
 * Returns the Stack object within this LWRRod.
 *
 * @return the stack
 */
std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > LWRRod::getMaterialBlocks() {

    // begin-user-code

    return this->materialBlocks;

    // end-user-code
}

/**
 * Sets the Stack object within this LWRRod.  Can not be set null.
 *
 * @param the stack to set
 */
void LWRRod::setMaterialBlocks(std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > materialBlocks) {

    // begin-user-code

    //if MaterialBlocks are not empty
    if (materialBlocks.get()->size()  != 0) {
        this->materialBlocks = materialBlocks;

    }

    return;

    // end-user-code

}

/**
 * Returns the clad object of this LWRRod object, if set or null.
 *
 * @return the clad
 */
std::shared_ptr<Ring> LWRRod::getClad() {

    // begin-user-code

    return this->clad;

    // end-user-code

}

/**
 * Sets the clad object for this LWRRod. It can not be set to null.
 *
 * @param sets the Clad
 */
void LWRRod::setClad(std::shared_ptr<Ring> clad) {

    // begin-user-code

    // if clad is not null
    if (clad.get() != NULL) {
        this->clad = clad;
    }

    // end-user-code

}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool LWRRod::operator==(const LWRRod& otherObject) const {
    
    //begin-user-code
    
    bool retVal = LWRComponent::operator==(otherObject) &&
           this->pressure == otherObject.pressure &&
           this->fillGas.get()->operator==(*otherObject.fillGas.get()) &&
           this->clad.get()->operator==(*otherObject.clad.get()) &&
	   this->materialBlocks.get()->size() == otherObject.materialBlocks.get()->size();
    
    if(retVal == false) return false;

    //Check MaterialBlocks
    for(int i = 0; i < this->materialBlocks.get()->size(); i++) {
        if(this->materialBlocks.get()->at(i).get()->operator==(*otherObject.materialBlocks.get()->at(i).get()) == false) return false;
    }
	
    //Everything is valid, return true
    return true;
    //end-user-code
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> LWRRod::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<LWRRod> component(new LWRRod (*this));

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
bool LWRRod::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code
    bool flag = true;

    flag &= LWRComponent::writeAttributes(h5File, h5Group);
    flag &= ICE_IO::HdfWriterFactory::writeDoubleAttribute(h5File, h5Group,
            "pressure", this->pressure);

    return flag;
    // end-user-code

}

/**
 * Returns the list of writeable childen.
 *
 * @return List of writeable chidlren
 */
std::vector< std::shared_ptr< ICE_IO::IHdfWriteable > > LWRRod::getWriteableChildren() {
    // begin-user-code

    //Get the children in super
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > children = LWRComponent::getWriteableChildren();


    //Add the values to children
    children.push_back(this->clad);
    children.push_back(this->fillGas);
    children.insert(children.end(), this->materialBlocks.get()->begin(), this->materialBlocks.get()->end());

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
bool LWRRod::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    //Local Declarations
    double pressure;


    //If group is null or the super read attributes is false, return false
    if(h5Group.get() == NULL || LWRComponent::readAttributes(h5Group) == false) return false;

    //Try to get values.  If failure, return false;
    try {
        pressure = ICE_IO::HdfReaderFactory::readDoubleAttribute(h5Group, "pressure");
    } catch (...) {
        return false;
    }

    //Setup the values
    this->pressure = pressure;

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
bool LWRRod::readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable) {

    // begin-user-code

    //If the child is null, return false
    if(iHdfReadable.get() == NULL) {
        return false;
    }

    //Cast the child into a LWRComponent - to get the tag
    try {

        //Dynamic cast to LWRComponent
        LWRComponent *newType = dynamic_cast<LWRComponent *> (iHdfReadable.get());

        //If the type is not LWRComponent, return false
        if(newType == 0) {
            return false;
        }

        //Determine the tag type

        //If its a MaterialBlock
        if(newType->getHDF5LWRTag() == MATERIALBLOCK) {
            //Dynamic cast up to a MaterialBlock (which will preserve the clone operation on implemented dervied classes without calling new!
            std::shared_ptr<MaterialBlock> obj = std::dynamic_pointer_cast<MaterialBlock> (iHdfReadable);
            this->materialBlocks.get()->push_back(obj);
        }

        //If its a Ring/Tube
        else if(newType->getHDF5LWRTag() == RING || newType->getHDF5LWRTag() == TUBE ) {
            //Dynamic cast up to a stack (which will preserve the clone operation on implemented dervied classes without calling new!
            std::shared_ptr<Ring> obj = std::dynamic_pointer_cast<Ring> (iHdfReadable);
            LWRRod::setClad(obj);
        }

        //If its a Material
        else if (newType->getHDF5LWRTag() == MATERIAL ) {
            //Dynamic cast up to a stack (which will preserve the clone operation on implemented dervied classes without calling new!
            std::shared_ptr<Material> obj = std::dynamic_pointer_cast<Material> (iHdfReadable);
            LWRRod::setFillGas(obj);
        } else {
            //Else, return false!
            return false;
        }

    } catch (...) {
        return false;
    }

    //Return true for success
    return true;

    // end-user-code

}
