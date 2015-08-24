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

#include "LWRComposite.h"
#include <IHdfReadable.h>
#include <IHdfWriteable.h>
#include <ICEObject/Identifiable.h>
#include <stdexcept>
#include <map>
#include <vector>
#include <string>

using namespace ICE_Reactor;

/**
 * The Copy constructor.
 *
 * @param The object to be copied.
 */
LWRComposite::LWRComposite(LWRComposite & arg) : LWRComponent(arg) {

    //Copy contents

    //Deep copy tree
    this->lWRComponents.clear();

    std::map<std::string, std::shared_ptr <LWRComponent> >::iterator mapIter;

    //Iterate the map and add to list
    for(mapIter = arg.lWRComponents.begin(); mapIter != arg.lWRComponents.end(); ++mapIter) {
        std::shared_ptr<LWRComponent> component ( new LWRComponent(*mapIter->second.get()));
        this->lWRComponents.insert( std::pair<std::string, std::shared_ptr <LWRComponent> >(mapIter->first, component) );
    }
}

/**
 * The Destructor
 */
LWRComposite::~LWRComposite() {
    //TODO Auto-generated method stub
}

/**
 * The Constructor.
 */
LWRComposite::LWRComposite() : LWRComponent() {

    //Setup default values
    this->name = "Composite 1";
    this->id = 1;
    this->description = "Composite 1's Description";
    this->HDF5LWRTag = LWRCOMPOSITE;

}

/**
 * Returns the LWRComponent corresponding to the provided name or null if the name is not found.
 *
 * @param The name
 *
 * @return the component
 */
std::shared_ptr<ICE_DS::Component> LWRComposite::getComponent(const std::string name) {

    //Local declarations
    std::shared_ptr<ICE_DS::Component> nullComponent;
    std::map<std::string, std::shared_ptr <LWRComponent> >::iterator mapIter;

    //Iterate the map and add to list
    for(mapIter = this->lWRComponents.begin(); mapIter != this->lWRComponents.end(); ++mapIter) {

        if(mapIter->second.get()->getName().compare(name) == 0) {
            return mapIter->second;
        }

    }

    //Id did not exist
    return nullComponent;
}

/**
 * Returns a list of std::strings containing the names of all LWRComponents contained in this LWRComposite.
 *
 * @return the list of strings
 */
std::vector<std::string> LWRComposite::getComponentNames() {

    // begin-user-code

    //Local declarations
    std::vector<std::string> list;
    std::map<std::string, std::shared_ptr <LWRComponent> >::iterator mapIter;

    //Iterate the map and add to list
    for(mapIter = this->lWRComponents.begin(); mapIter != this->lWRComponents.end(); ++mapIter) {
        list.push_back(mapIter->first);
    }

    //Return the list
    return list;

    // end-user-code
}

/**
 * Removes a LWRComponent with the provided name from this LWRComposite.
 *
 * @param the name
 */
void LWRComposite::removeComponent(const std::string name) {
    // begin-user-code

    //Local declarations
    std::vector<std::string> list;
    std::map<std::string, std::shared_ptr <LWRComponent> >::iterator mapIter;

    //Iterate the map and remove item if equal
    for(mapIter = this->lWRComponents.begin(); mapIter != this->lWRComponents.end(); ++mapIter) {

        //If the name exists, remove it
        if(name.compare(mapIter->first) == 0) {
            this->lWRComponents.erase(mapIter->first);
            break;
        }
    }

    //Return
    return;
}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool LWRComposite::operator ==(const LWRComposite& otherObject) const {

    // begin-user-code

    //Check map values
    std::map<std::string, std::shared_ptr <LWRComponent> >::const_iterator mapIter;

    //Check super equality and the map sizes
    if(LWRComponent::operator ==(otherObject) == false || this->lWRComponents.size() != otherObject.lWRComponents.size() ) return false;

    //Iterate the map and check values
    for(mapIter = this->lWRComponents.begin(); mapIter != this->lWRComponents.end(); ++mapIter) {

        //IF the values do not compare correctly, return false
        try {
            if(this->lWRComponents.at(mapIter->first).get()->operator ==(*otherObject.lWRComponents.at(mapIter->first).get()) == false) {
                return false;
            }
        } catch (const std::out_of_range& oor) {
            return false;
        }

    }


    //everything true, return true
    return true;

    // end-user-code

}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return The newly instantiated object.
 */
std::shared_ptr<ICE_DS::Identifiable> LWRComposite::clone() {
    // begin-user-code

    //Local Declarations
    std::shared_ptr<LWRComponent> component (new LWRComposite(*this));

    //Return the component
    return component;

    // end-user-code
}

/**
 * Returns the list of writeable childen.
 *
 * @return List of writeable chidlren
 */
std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > LWRComposite::getWriteableChildren() {


    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > writeables;
    std::map<std::string, std::shared_ptr <LWRComponent> >::const_iterator mapIter;

    //Loop over the map and get the values and then add it to a vector
    for(mapIter = this->lWRComponents.begin(); mapIter != this->lWRComponents.end(); ++mapIter) {

        writeables.push_back(mapIter->second);
    }

    return writeables;


}

/**
 * This operation reads IHdfReadable child objects. If this IHdfReadable has no IHdfReadable child objects, false is returned.
 *
 * @param the readable child
 *
 * @return true if successful, false otherwise
 */
bool LWRComposite::readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable) {

    // begin-user-code

    //If the child is null, return false
    if(iHdfReadable.get() == NULL) {
        return false;
    }

    //Cast the child into a LWRComponent
    std::shared_ptr<LWRComponent> childComponent = std::dynamic_pointer_cast<LWRComponent> (iHdfReadable);

    //Remove any child with the new child's name from this LWRComposite
    LWRComposite::removeComponent(childComponent.get()->getName());

    //Add the new child LWRComponent
    LWRComposite::addComponent(childComponent);

    //Return true for success
    return true;

    // end-user-code
}

/**
 * This operation adds a child Component to a class that realizes the Composite interface. This operation should notify listeners that components have been added.
 *
 * @param the child to add
 */
void LWRComposite::addComponent(std::shared_ptr<ICE_DS::Component> child) {

    if(child.get() == NULL) {
        return;
    }

    //If found, return null
    std::map<std::string, std::shared_ptr <LWRComponent> >::iterator mapIter;

    //Iterate the map and remove item if equal
    for(mapIter = this->lWRComponents.begin(); mapIter != this->lWRComponents.end(); ++mapIter) {

        //If the name exists, return
        if(child.get()->getName().compare(mapIter->first) == 0) {
            return;
        }
    }

    //Downcast to a shared pointer of LWRComponent type

    //Cast up
    std::shared_ptr<LWRComponent> castedComponent = std::dynamic_pointer_cast <LWRComponent> (child);

    //Name doesnt exist, add it!
    this->lWRComponents.insert( std::pair<std::string, std::shared_ptr <LWRComponent> >(child.get()->getName(), castedComponent) );

}

/**
 * This operation removes a child Component to a class that realizes the Composite interface.
 *
 * @param the child id
 */

void LWRComposite::removeComponent(int childId) {
    // begin-user-code

    //Local declarations
    std::vector<std::string> list;
    std::map<std::string, std::shared_ptr <LWRComponent> >::iterator mapIter;

    //Iterate the map and remove item if equal
    for(mapIter = this->lWRComponents.begin(); mapIter != this->lWRComponents.end(); ++mapIter) {

        //If the name exists, remove it
        if(mapIter->second.get()->getId() == childId) {
            this->lWRComponents.erase(mapIter->first);
            break;
        }
    }

    //Return
    return;
}

/**
 * This operation retrieves a child Component to a class that realizes the Composite interface.
 *
 * @param the child id
 *
 * @return the component
 */
std::shared_ptr<ICE_DS::Component> LWRComposite::getComponent(int childId) {

    //begin-user-code

    //Local declarations
    std::shared_ptr<ICE_DS::Component> nullComponent;

    std::map<std::string, std::shared_ptr <LWRComponent> >::iterator mapIter;

    //Iterate the map and add to list
    for(mapIter = this->lWRComponents.begin(); mapIter != this->lWRComponents.end(); ++mapIter) {
        if(mapIter->second.get()->getId() == childId) {
            //remove, break
            return this->lWRComponents.at(mapIter->first);
            break;
        }

    }

    //Id did not exist
    return nullComponent;

}

/**
 * This operations retrieves the number of child Components stored in an instance of a class that realizes the Composite interface.
 *
 * @return the number of components
 */
int LWRComposite::getNumberOfComponents() {
    return this->lWRComponents.size();
}


/**
 * This operation returns all of the Components stored in the Composite.
 *
 * @return the list of components
 */
std::vector< std::shared_ptr<ICE_DS::Component> > LWRComposite::getComponents() {

    //begin-user-code

    //Local declarations
    std::vector< std::shared_ptr<ICE_DS::Component> > components;

    std::map<std::string, std::shared_ptr <LWRComponent> >::iterator mapIter;

    //Iterate the map and add to list
    for(mapIter = this->lWRComponents.begin(); mapIter != this->lWRComponents.end(); ++mapIter) {

        //Push to the list
        components.push_back(mapIter->second);

    }

    //return list
    return components;

}

