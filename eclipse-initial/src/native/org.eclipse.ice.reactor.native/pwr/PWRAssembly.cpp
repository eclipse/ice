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

#include "PWRAssembly.h"
#include "../LWRComposite.h"
#include "../LWRGridManager.h"
#include "../LWRRod.h"
#include "../LWRComponent.h"
#include <memory>
#include <updateableComposite/Component.h>
#include <H5Cpp.h>
#include <stdio.h>
#include <iostream>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
PWRAssembly::PWRAssembly(PWRAssembly & arg) : LWRComposite(arg) {

    //begin-user-code

    this->size = arg.size;

    std::shared_ptr <LWRGridManager> manager = std::dynamic_pointer_cast<LWRGridManager>(arg.lWRRodGridManager.get()->clone());
    std::shared_ptr <LWRComposite> rods = std::dynamic_pointer_cast<LWRComposite>(arg.lWRRodComposite.get()->clone());

    this->lWRRodGridManager = manager;
    this->lWRRodComposite = rods;
    this->rodPitch = arg.rodPitch;

    this->lWRComponents.clear();
    LWRComposite::addComponent(lWRRodComposite);

    //Default values for strings
    this->LWRROD_COMPOSITE_NAME = "LWRRods";
    this->LWRROD_GRID_MANAGER_NAME = "LWRRod Grid";

    //end-user-code


}

/**
 * The Destructor
 */
PWRAssembly::~PWRAssembly() {
    //TODO Auto-generated method stub
}

/**
 * A parameterized Constructor.
 *
 * @param the size
 */
PWRAssembly::PWRAssembly(int size) {

    // begin-user-code

    //Setup default values - LWRComponent
    this->name = "PWRAssembly";
    this->description = "PWRAssembly's Description";
    this->id = 1;

    //Default values for strings
    this->LWRROD_COMPOSITE_NAME = "LWRRods";
    this->LWRROD_GRID_MANAGER_NAME = "LWRRod Grid";

    //Setup default values for size and map
    this->size = 1;

    //Set the size if it is greater than 0
    if (size > 0) {
        this->size = size;
    }

    //Setup the LWRComposite and associated values.
    this->lWRRodComposite = std::shared_ptr<LWRComposite> (new LWRComposite());
    this->lWRRodComposite.get()->setName(this->LWRROD_COMPOSITE_NAME);
    this->lWRRodComposite.get()->setDescription("A Composite that contains many LWRRods.");
    this->lWRRodComposite.get()->setId(1);

    //Setup GridManager
    this->lWRRodGridManager = std::shared_ptr<LWRGridManager> (new LWRGridManager(this->size));
    this->lWRRodGridManager.get()->setName(this->LWRROD_GRID_MANAGER_NAME);

    //Add the component to the LWRComposite list
    LWRComposite::addComponent(this->lWRRodComposite);

    //Default size for rod pitch
    this->rodPitch = 1.0;

    //Setup the LWRComponentType to the correct type
    this->HDF5LWRTag = PWRASSEMBLY;

    // end-user-code

}

/**
 * A parameterized Constructor.
 *
 * @param the name
 * @param the size
 */
PWRAssembly::PWRAssembly(const std::string name, int size) {

    // begin-user-code

    //Setup default values - LWRComponent
    this->name = "PWRAssembly";
    this->description = "PWRAssembly's Description";
    this->id = 1;

    //Default values for strings
    this->LWRROD_COMPOSITE_NAME = "LWRRods";
    this->LWRROD_GRID_MANAGER_NAME = "LWRRod Grid";

    //Setup default values for size and map
    this->size = 1;

    //Set the size if it is greater than 0
    if (size > 0) {
        this->size = size;
    }

    //Setup the LWRComposite and associated values.
    this->lWRRodComposite = std::shared_ptr<LWRComposite> (new LWRComposite());
    this->lWRRodComposite.get()->setName(this->LWRROD_COMPOSITE_NAME);
    this->lWRRodComposite.get()->setDescription("A Composite that contains many LWRRods.");
    this->lWRRodComposite.get()->setId(1);

    //Setup GridManager
    this->lWRRodGridManager = std::shared_ptr<LWRGridManager> (new LWRGridManager(this->size));
    this->lWRRodGridManager.get()->setName(this->LWRROD_GRID_MANAGER_NAME);

    //Add the component to the LWRComposite list
    this->lWRComponents.insert(std::pair<std::string, std::shared_ptr <LWRComponent> > (this->lWRRodComposite.get()->getName(), this->lWRRodComposite));

    //Default size for rod pitch
    this->rodPitch = 1.0;

    //Setup the LWRComponentType to the correct type
    this->HDF5LWRTag = PWRASSEMBLY;

    //Call setters
    PWRAssembly::setName(name);

    // end-user-code

}

/**
 * Returns the size of either dimension of this PWRAssembly.
 *
 * @return the size
 */
int PWRAssembly::getSize() {

    // begin-user-code

    return this->size;

    // end-user-code
}

/**
 * Adds a LWRRod to the collection of LWRRods. If a LWRRod with the same name exists in the collection or the passed parameter is null, then the LWRRod will not be added and a value of false will be returned.
 *
 * @param the rod
 *
 * @return true if successful, false otherwise
 */
bool PWRAssembly::addLWRRod(std::shared_ptr<LWRRod> lWRRod) {

    // begin-user-code

    //Get size of components
    int sizeChanged = this->lWRRodComposite.get()->getComponentNames().size();

    //Add the component to the composite
    this->lWRRodComposite.get()->addComponent(lWRRod);

    //If the component is not contained, return false
    if (sizeChanged == this->lWRRodComposite.get()->getComponentNames().size() || lWRRod.get() == NULL || this->lWRRodComposite.get()->getComponent(lWRRod.get()->getName()).get() == NULL) {
        return false;
    }

    //The component was added to the composite, return true!
    return true;

    // end-user-code

}

/**
 * Removes a LWRRod from the collection of LWRRods.  The passed string can not be null.
 *
 * @param the rod name
 *
 * @return true if successful, false otherwise
 */
bool PWRAssembly::removeLWRRod(const std::string lWRRodName) {

    // begin-user-code
    //If the name does not exist, return
    if (this->lWRRodComposite.get()->getComponent(lWRRodName).get() == NULL)
        return false;

    //Remove it from the grid as well
    this->lWRRodGridManager.get()->removeComponent(this->lWRRodComposite.get()->getComponent(lWRRodName));

    //Remove the component from the composite with the given name
    this->lWRRodComposite.get()->removeComponent(lWRRodName);

    //Remove it from the grid as well
    this->lWRRodGridManager.get()->removeComponent(this->lWRRodComposite.get()->getComponent(lWRRodName));

    //If name does not exist, return true.  Else false
    if (this->lWRRodComposite.get()->getComponent(lWRRodName).get() != NULL) {
        return false;
    }

    //The component was deleted from the composite, return true!

    return true;

    // end-user-code

}

/**
 * Returns a list of names for each element of the collection of LWRRods.
 *
 * @return list of rod names
 */
std::vector<std::string> PWRAssembly::getLWRRodNames() {

    // begin-user-code

    //Return the Component's names
    return this->lWRRodComposite.get()->getComponentNames();

    // end-user-code
}

/**
 * Returns the LWRRod corresponding to the provided name or null if the name is not found.
 *
 * @param the name
 *
 * @return the rod at the name
 */
std::shared_ptr<LWRRod> PWRAssembly::getLWRRodByName(const std::string name) {

    // begin-user-code

    std::shared_ptr<Component> component = this->lWRRodComposite.get()->getComponent(name);
    if(component.get() != NULL) {
        //Cast it back to a rod
        std::shared_ptr<LWRRod> rod = std::dynamic_pointer_cast<LWRRod> (component);

        return rod;
    }

    //Return null component if name not found
    std::shared_ptr<LWRRod> nullRod;
    return nullRod;

    // end-user-code
}

/**
 * Returns the LWRRod corresponding to the provided column and row or null if one is not found at the provided location.
 *
 * @param the row
 * @param the column
 *
 * @return the rod at the location
 */
std::shared_ptr<LWRRod> PWRAssembly::getLWRRodByLocation(int row, int column) {

    // begin-user-code

    //Local Declarations
    std::string name;

    //Get the name
    name = this->lWRRodGridManager.get()->getComponentName(std::shared_ptr<GridLocation> (new GridLocation(row, column)));

    //Return the component
    return PWRAssembly::getLWRRodByName(name);

    // end-user-code
}

/**
 * Returns the number of LWRRods in the collection of LWRRods.
 *
 * @return the number of rods
 */
int PWRAssembly::getNumberOfLWRRods() {

    // begin-user-code

    //Return the number of components
    return this->lWRRodComposite.get()->getNumberOfComponents();

    // end-user-code

}

/**
 * Sets the location for the provided name.  Overrides the location of another component name as required.  Returns true if this operation was successful, false otherwise.  Note it will return true if the same name is overridden.
 *
 * @param the rod name
 * @param the row
 * @param the column
 *
 * @return true if successful, false otherwise
 */
bool PWRAssembly::setLWRRodLocation(const std::string lWRRodName, int row, int column) {

    // begin-user-code

    //Local declarations
    std::shared_ptr<GridLocation> location (new GridLocation(row, column));

    //If the rows and columns dont match the location, return false
    if (location.get()->getColumn() != column || location.get()->getRow() != row)
        return false;

    //Set the location
    this->lWRRodGridManager.get()->addComponent(this->lWRRodComposite.get()->getComponent(lWRRodName), location);

    //If the name changed, then return true
    if (this->lWRRodGridManager.get()->getComponentName(location).empty() != true
            && this->lWRRodGridManager.get()->getComponentName(location).compare(
                lWRRodName) == 0) {
        return true;
    }

    //Otherwise, name did not change
    return false;

    // end-user-code
}

/**
 * Removes the LWRRod at the provided location. Returns true if the removal was successful.
 *
 * @param the row
 * @param the column
 *
 * @return true if successful, false otherwise
 */
bool PWRAssembly::removeLWRRodFromLocation(int row, int column) {

    // begin-user-code

    //Local Declarations
    std::shared_ptr<GridLocation> location (new GridLocation(row, column));

    //If the rows and columns dont match the location, return false
    if (location.get()->getColumn() != column || location.get()->getRow() != row)
        return false;

    //Check to make sure that a name exists there.
    if (this->lWRRodGridManager.get()->getComponentName(location).empty() == true) {
        return false;
    }

    //Remove the component from the location
    this->lWRRodGridManager.get()->removeComponent(location);

    //If the composite at that location is null, then return true
    if (this->lWRRodGridManager.get()->getComponentName(location).empty() == true) {
        return true;
    }

    //Nothing was changed, return false;
    return false;

    // end-user-code
}

/**
 * Returns the distance between centers of adjacent fuel rods.
 *
 * @return the rodPitch
 */
double PWRAssembly::getRodPitch() {

    // begin-user-code

    return this->rodPitch;

    // end-user-code

}

/**
 * Sets the distance between centers of adjacent fuel rods in the fuel lattice. The rodPitch value must be greater than zero.
 *
 * @param the rodPitch to set
 */
void PWRAssembly::setRodPitch(double rodPitch) {

    // begin-user-code

    //If the rod pitch is less than zero, do not set to new value.
    if (rodPitch > 0) {
        this->rodPitch = rodPitch;
    }

    return;

    // end-user-code

}

/**
 * This operation overrides the LWRComposite's operation.  This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
 *
 * @param Component to set
 */
void PWRAssembly::addComponent(std::shared_ptr<ICE_DS::Component> component) {

    // begin-user-code

    //Does nothing

    // end-user-code

}

/**
 * An operation that overrides the LWRComposite's operation. This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
 *
 * @param the child id
 */
void PWRAssembly::removeComponent(int childId) {

    // begin-user-code

    //Does nothing

    // end-user-code

}

/**
 * An operation that overrides the LWRComposite's operation. This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
 *
 * @param the name
 */
void PWRAssembly::removeComponent(const std::string name) {

    // begin-user-code

    //Does nothing

    // end-user-code

}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool PWRAssembly::operator==(const PWRAssembly& otherObject) const {

    //begin-user-code

    return LWRComposite::operator ==(otherObject) && this->lWRRodComposite.get()->operator ==(*otherObject.lWRRodComposite.get()) && this->lWRRodGridManager.get()->operator==(*otherObject.lWRRodGridManager.get()) && this->size == otherObject.size && this->rodPitch == otherObject.rodPitch;

    //end-user-code

}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> PWRAssembly::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<PWRAssembly> component(new PWRAssembly (*this));

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
bool PWRAssembly::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {
    // begin-user-code

    bool flag = true;

    flag &= LWRComposite::writeAttributes(h5File, h5Group);
    flag &= ICE_IO::HdfWriterFactory::writeDoubleAttribute(h5File, h5Group, "rodPitch", this->rodPitch);
    flag &= ICE_IO::HdfWriterFactory::writeIntegerAttribute(h5File, h5Group, "size", this->size);

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
std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > PWRAssembly::getWriteableChildren() {

    //begin-user-code

    //Get the children in super
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > children = LWRComposite::getWriteableChildren();


    //Add the gridmanager to children
    children.push_back(this->lWRRodGridManager);

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
bool PWRAssembly::readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable) {
    // begin-user-code

    //If the child is null, return false
    if(iHdfReadable.get() == NULL) {
        return false;
    }

    //Cast the child into a LWRComponent - to get the tag
    try {

        //Dynamic cast to material
        LWRComponent *newType = dynamic_cast<LWRComponent *> (iHdfReadable.get());

        //If the type is not material, return false
        if(newType == 0) {
            return false;
        }

        //Determine the tag type

        //If its a stack
        if(newType->getHDF5LWRTag() == LWRCOMPOSITE && newType->getName().compare(this->LWRROD_COMPOSITE_NAME) == 0) {
            //Dynamic cast up to a LWRComposite (which will preserve the clone operation on implemented dervied classes without calling new!
            //Remove the previous object
            LWRComposite::removeComponent(this->lWRRodComposite.get()->getName());
            this->lWRRodComposite = std::dynamic_pointer_cast<LWRComposite> (iHdfReadable);
            //Add object back
            LWRComposite::addComponent(this->lWRRodComposite);

        }

        //If its a Ring/Tube
        else if(newType->getHDF5LWRTag() == LWRGRIDMANAGER && newType->getName().compare(this->LWRROD_GRID_MANAGER_NAME) == 0 ) {
            //Dynamic cast up to a LWRComposite (which will preserve the clone operation on implemented dervied classes without calling new!
            this->lWRRodGridManager = std::dynamic_pointer_cast<LWRGridManager> (iHdfReadable);
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

/**
 * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
 *
 * @param  The Group
 *
 * @return True if successful, false otherwise
 */
bool PWRAssembly::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    //begin-user-code

    //Local attributes (so we only call read ONCE)
    //These will clear out by the garbage collector
    int size;
    double rodPitch;

    //Check super
    if(h5Group.get() == NULL) {
        return false;
    }

    //Get the information.  If any fail out, return false and do not change data.
    try {
        if (LWRComponent::readAttributes(h5Group) == false) return false;
        size = ICE_IO::HdfReaderFactory::readIntegerAttribute(h5Group, "size");
        rodPitch = ICE_IO::HdfReaderFactory::readDoubleAttribute(h5Group, "rodPitch");
    } catch (...) {
        return false;
    }

    //Set the primitive data
    this->size = size;
    this->rodPitch = rodPitch;
    return true;

    //end-user-code

}

/**
 * <!-- begin-UML-doc -->
 * <p>Returns the data provider for specific group at location or null if it does not exist.</p>
 * <!-- end-UML-doc -->
 * @param row <p>the row</p>
 * @param column <p>The column</p>
 * @return <p>the provider</p>
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::shared_ptr<LWRDataProvider> PWRAssembly::getLWRRodDataProviderAtLocation(int row, int column) {

	// begin-user-code

	return this->lWRRodGridManager.get()->getDataProviderAtLocation(std::shared_ptr<GridLocation> (new GridLocation(row, column)));

	// end-user-code

}
