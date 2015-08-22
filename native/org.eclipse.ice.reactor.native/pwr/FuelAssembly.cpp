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

#include "FuelAssembly.h"
#include <H5Cpp.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <string>
#include <vector>
#include <memory>
#include "PWRAssembly.h"

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
FuelAssembly::FuelAssembly(FuelAssembly & arg) : PWRAssembly(arg) {
    //begin-user-code

    //Clone and dynamic cast correctly
    std::shared_ptr <LWRGridManager> manager = std::dynamic_pointer_cast<LWRGridManager>(arg.tubeGridManager.get()->clone());
    std::shared_ptr <LWRComposite> tubes = std::dynamic_pointer_cast<LWRComposite>(arg.tubeComposite.get()->clone());
    std::shared_ptr <RodClusterAssembly> rca;
    //If the rodClusterAssembly on the other object is not null, set it
    if(arg.rodClusterAssembly.get() != NULL) rca = std::dynamic_pointer_cast<RodClusterAssembly>(arg.rodClusterAssembly.get()->clone());
    std::shared_ptr <GridLabelProvider> labels = std::dynamic_pointer_cast<GridLabelProvider>(arg.gridLabelProvider.get()->clone());

    //Copy contents
    this->tubeGridManager = manager;
    this->tubeComposite = tubes;
    LWRComposite::addComponent(tubeComposite); //Should be cleared above
    this->rodClusterAssembly = rca;
    this->gridLabelProvider = labels;


    this->TUBE_COMPOSITE_NAME = "Tubes";
    this->TUBE_GRID_MANAGER_NAME = "Tube Grid";
    this->GRID_LABEL_PROVIDER_NAME = "Grid Labels";

    return;

    //end-user-code
}

/**
 * The Destructor
 */
FuelAssembly::~FuelAssembly() {
    //TODO Auto-generated method stub
}

/**
 * A parameterized Constructor.
 *
 * @param the size
 */
FuelAssembly::FuelAssembly(int size) : PWRAssembly(size) {

    // begin-user-code

    //Call super constructor

    //Set defaults for FuelAssembly
    this->name = "FuelAssembly";
    this->description = "FuelAssembly's Description";
    this->id = 1;
    this->TUBE_COMPOSITE_NAME = "Tubes";
    this->TUBE_GRID_MANAGER_NAME = "Tube Grid";
    this->GRID_LABEL_PROVIDER_NAME = "Grid Labels";

    //Setup rows and cols
    this->gridLabelProvider = std::shared_ptr<GridLabelProvider> (new GridLabelProvider(this->size));
    this->gridLabelProvider.get()->setName(this->GRID_LABEL_PROVIDER_NAME);

    //Setup tube map
    this->tubeComposite = std::shared_ptr<LWRComposite> (new LWRComposite());
    this->tubeComposite.get()->setName(this->TUBE_COMPOSITE_NAME);
    this->tubeComposite.get()->setDescription("A Composite that contains many Tubes.");
    this->tubeComposite.get()->setId(2);

    //Setup  tube grid manager
    this->tubeGridManager = std::shared_ptr<LWRGridManager> ( new LWRGridManager(this->size));
    this->tubeGridManager.get()->setName(this->TUBE_GRID_MANAGER_NAME);

    //Add tubeComposite to components

    //this has to be added manually
    this->lWRComponents.insert(std::pair<std::string, std::shared_ptr <LWRComponent> > (this->tubeComposite.get()->getName(), this->tubeComposite));

    //Setup the LWRComponentType to correct type
    this->HDF5LWRTag = FUEL_ASSEMBLY;

    // end-user-code

}
/**
 *  A parameterized Constructor.
 *
 *  @param the name
 *  @param the size
 */
FuelAssembly::FuelAssembly(const std::string name, int size) : PWRAssembly(name, size) {

    // begin-user-code

    //Call super constructor

    //Set defaults for FuelAssembly
    this->name = "FuelAssembly";
    this->description = "FuelAssembly's Description";
    this->id = 1;
    this->TUBE_COMPOSITE_NAME = "Tubes";
    this->TUBE_GRID_MANAGER_NAME = "Tube Grid";
    this->GRID_LABEL_PROVIDER_NAME = "Grid Labels";


    //Setup rows and cols
    this->gridLabelProvider = std::shared_ptr<GridLabelProvider> (new GridLabelProvider(this->size));
    this->gridLabelProvider.get()->setName(this->GRID_LABEL_PROVIDER_NAME);

    //Setup tube map
    this->tubeComposite = std::shared_ptr<LWRComposite> (new LWRComposite());
    this->tubeComposite.get()->setName(this->TUBE_COMPOSITE_NAME);
    this->tubeComposite.get()->setDescription("A Composite that contains many Tubes.");
    this->tubeComposite.get()->setId(2);

    //Setup  tube grid manager
    this->tubeGridManager = std::shared_ptr<LWRGridManager> ( new LWRGridManager(this->size));
    this->tubeGridManager.get()->setName(this->TUBE_GRID_MANAGER_NAME);

    //Add tubeComposite to components

    //this has to be added manually
    this->lWRComponents.insert(std::pair<std::string, std::shared_ptr <LWRComponent> > (this->tubeComposite.get()->getName(), this->tubeComposite));

    //Setup the LWRComponentType to correct type
    this->HDF5LWRTag = FUEL_ASSEMBLY;

    //Call setters
    FuelAssembly::setName(name);

    // end-user-code

}

/**
 * Returns the RodClusterAssembly associated with this FuelAssembly or null if one has not been set.
 *
 * @return the rod cluster assembly
 */
std::shared_ptr<RodClusterAssembly> FuelAssembly::getRodClusterAssembly() {

    // begin-user-code

    return this->rodClusterAssembly;

    // end-user-code

}

/**
 * Sets the RodClusterAssembly associated with this FuelAssembly.
 *
 * @param the rod cluster assembly to set
 */
void FuelAssembly::setRodClusterAssembly(std::shared_ptr<RodClusterAssembly> rodClusterAssembly) {

    // begin-user-code

    //RodClusterAssembly can be null
    this->rodClusterAssembly = rodClusterAssembly;

    return;

    // end-user-code

}

/**
 * Adds a Tube to the collection of Tubes. If a Tube with the same name exists in the collection or if the passed tube is null, then the Tube will not be added and a value of false will be returned.
 *
 * @param the tube to add
 *
 * @return true if successful, false otherwise
 */
bool FuelAssembly::addTube(std::shared_ptr<Tube> tube) {

    // begin-user-code

    //Get size of components
    int sizeChanged = this->tubeComposite.get()->getComponentNames().size();

    //Add the component to the composite
    this->tubeComposite.get()->addComponent(tube);

    //If the component is not contained, return false
    if (sizeChanged == this->tubeComposite.get()->getComponentNames().size() || tube.get() == NULL || this->tubeComposite.get()->getComponent(tube.get()->getName()).get() == NULL) {
        return false;
    }

    //The component was added to the composite, return true!
    return true;

    // end-user-code

}

/**
 * Returns an ArrayList of names for each element of the collection of Tubes.
 *
 * @return  the names of the tubes
 */
std::vector < std::string > FuelAssembly::getTubeNames() {

    // begin-user-code

    //Return the Component's names
    return this->tubeComposite.get()->getComponentNames();

    // end-user-code
}

/**
 * Returns the Tube corresponding to the provided name or null if the name is not found.
 *
 * @param the name of the tube to return
 *
 * @return the tube at that name
 */
std::shared_ptr<Tube> FuelAssembly::getTubeByName(const std::string name) {

    // begin-user-code

    std::shared_ptr<Component> component = this->tubeComposite.get()->getComponent(name);
    if(component.get() != NULL) {
        //Cast it back to a rod
        std::shared_ptr<Tube> rod = std::dynamic_pointer_cast<Tube> (component);

        return rod;
    }

    //Return null component if name not found
    std::shared_ptr<Tube> nullTube;
    return nullTube;

    // end-user-code

}

/**
 * Returns the Tube corresponding to the provided column and row or null if one is not found at the provided location.
 *
 * @param the row
 * @param the column
 *
 * @return the tube at that row/col
 */
std::shared_ptr<Tube> FuelAssembly::getTubeByLocation(int row, int column) {

    // begin-user-code

    //Local Declarations
    std::string name;

    //Get the name
    name = this->tubeGridManager.get()->getComponentName(std::shared_ptr<GridLocation> (new GridLocation(row, column)));

    //Return the component
    return FuelAssembly::getTubeByName(name);

    // end-user-code

}

/**
 * Returns the number of Tubes in the collection of Tubes.
 *
 * @return the number of tubes
 */
int FuelAssembly::getNumberOfTubes() {

    // begin-user-code

    //Return the number of components
    return this->tubeComposite.get()->getNumberOfComponents();

    // end-user-code

}

/**
 * Removes a Tube from the collection of Tubes.  Returns false if the tubeName does not exist or if the string passed is null.
 *
 * @param the name of the tube
 *
 * @return true if successful, false otherwise
 */
bool FuelAssembly::removeTube(const std::string tubeName) {

    // begin-user-code
    //If the name does not exist, return
    if (this->tubeComposite.get()->getComponent(tubeName).get() == NULL)
        return false;

    //Remove it from the grid as well
    this->tubeGridManager.get()->removeComponent(this->tubeComposite.get()->getComponent(tubeName));

    //Remove the component from the composite with the given name
    this->tubeComposite.get()->removeComponent(tubeName);

    //Remove it from the grid as well
    this->tubeGridManager.get()->removeComponent(this->tubeComposite.get()->getComponent(tubeName));

    //If name does not exist, return true.  Else false
    if (this->tubeComposite.get()->getComponent(tubeName).get() != NULL) {
        return false;
    }

    //The component was deleted from the composite, return true!

    return true;

    // end-user-code
}

/**
 * Removes the Tube at the provided location. Returns true if the removal was successful.
 *
 * @param the row
 * @param the column
 *
 * @return true if successful, false otherwise
 */
bool FuelAssembly::removeTubeFromLocation(int row, int column) {

    // begin-user-code

    //Local Declarations
    std::shared_ptr<GridLocation> location (new GridLocation(row, column));

    //If the rows and columns dont match the location, return false
    if (location.get()->getColumn() != column || location.get()->getRow() != row)
        return false;

    //Check to make sure that a name exists there.
    if (this->tubeGridManager.get()->getComponentName(location).empty() == true) {
        return false;
    }

    //Remove the component from the location
    this->tubeGridManager.get()->removeComponent(location);

    //If the composite at that location is null, then return true
    if (this->tubeGridManager.get()->getComponentName(location).empty() == true) {
        return true;
    }

    //Nothing was changed, return false;
    return false;

    // end-user-code
}

/**
 * Sets the location for the provided name.  Overrides the location of another component name as required.  Returns true if this operation was successful, false otherwise.  Note it will return true if the same name is overridden.
 *
 * @param the name of the tube
 * @param the row
 * @param the column
 *
 * @return true if successful, false otherwise
 */
bool FuelAssembly::setTubeLocation(const std::string tubeName, int row, int column) {

    // begin-user-code

    //Local declarations
    std::shared_ptr<GridLocation> location (new GridLocation(row, column));

    //If the rows and columns dont match the location, return false
    if (location.get()->getColumn() != column || location.get()->getRow() != row)
        return false;

    //Set the location
    this->tubeGridManager.get()->addComponent(this->tubeComposite.get()->getComponent(tubeName), location);

    //If the name changed, then return true
    if (this->tubeGridManager.get()->getComponentName(location).empty() != true
            && this->tubeGridManager.get()->getComponentName(location).compare(
                tubeName) == 0) {
        return true;
    }

    //Otherwise, name did not change
    return false;

    // end-user-code

}

/**
 * Returns the GridLabelProvider for this FuelAssembly.
 *
 * @return the gridLabelProvider
 */
std::shared_ptr<GridLabelProvider> FuelAssembly::getGridLabelProvider() {

    // begin-user-code

    return this->gridLabelProvider;

    // end-user-code

}

/**
 * Sets the GridLabelProvider for this FuelAssembly.  Can not be set to null.
 *
 * @param the grid label provider to set
 */
void FuelAssembly::setGridLabelProvider(std::shared_ptr<GridLabelProvider> gridLabelProvider) {

    // begin-user-code

    //If the GridLabelProvider passed is not null and of the same size.
    if (gridLabelProvider.get() != NULL && gridLabelProvider.get()->getSize() == PWRAssembly::getSize()) {
        this->gridLabelProvider = gridLabelProvider;
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
bool FuelAssembly::operator==(const FuelAssembly& otherObject) const {

    //Custom rodclusterassembly check
    if(this->rodClusterAssembly.get() == NULL) {
        if(otherObject.rodClusterAssembly.get() == NULL) {
            //Do nothing
        } else {
            return false;
        }
    }
    if(this->rodClusterAssembly.get() != NULL && otherObject.rodClusterAssembly.get() !=NULL) {
        if(this->rodClusterAssembly.get()->operator ==(*otherObject.rodClusterAssembly.get()) == false) {
            return false;
        }
    }

    return PWRAssembly::operator ==(otherObject)  && this->gridLabelProvider.get()->operator==(*otherObject.gridLabelProvider.get()) && this->tubeComposite.get()->operator==(*otherObject.tubeComposite.get()) && this->tubeGridManager.get()->operator ==(*otherObject.tubeGridManager.get());
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> FuelAssembly::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<FuelAssembly> component(new FuelAssembly (*this));

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
std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > FuelAssembly::getWriteableChildren() {

    //begin-user-code

    //Get the children in super
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > children = PWRAssembly::getWriteableChildren();


    //Add  children
    children.push_back(this->gridLabelProvider);
    children.push_back(this->tubeGridManager);

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
bool FuelAssembly::readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable) {

    // begin-user-code

    //If the child is null, return false
    if(iHdfReadable.get() == NULL) {
        return false;
    }

    //Cast the child into a LWRComponent - to get the tag
    try {

        //Call super first to see if its valid
        if (PWRAssembly::readChild(iHdfReadable) == true) return true;

        //Dynamic cast to material
        LWRComponent *newType = dynamic_cast<LWRComponent *> (iHdfReadable.get());

        //If the type is not material, return false
        if(newType == 0) {
            return false;
        }

        //Determine the tag type

        //If its a tube
        if(newType->getHDF5LWRTag() == LWRCOMPOSITE && newType->getName().compare(this->TUBE_COMPOSITE_NAME) == 0) {
            //Dynamic cast up to a LWRComposite (which will preserve the clone operation on implemented dervied classes without calling new!
            //Remove the previous object
            LWRComposite::removeComponent(this->tubeComposite.get()->getName());
            this->tubeComposite = std::dynamic_pointer_cast<LWRComposite> (iHdfReadable);
            //Add object back
            LWRComposite::addComponent(this->tubeComposite);

        }

        //If its a grid manager
        else if(newType->getHDF5LWRTag() == LWRGRIDMANAGER && newType->getName().compare(this->TUBE_GRID_MANAGER_NAME) == 0 ) {
            //Dynamic cast up to a LWRComposite (which will preserve the clone operation on implemented dervied classes without calling new!
            this->tubeGridManager= std::dynamic_pointer_cast<LWRGridManager> (iHdfReadable);
        }
        //If its a gridLabel
        else if(newType->getHDF5LWRTag() == GRID_LABEL_PROVIDER && newType->getName().compare(this->GRID_LABEL_PROVIDER_NAME) == 0 ) {
            this->gridLabelProvider= std::dynamic_pointer_cast<GridLabelProvider> (iHdfReadable);
        }

        else {
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
bool FuelAssembly::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code


    //Read super groups attributes
    try {
        if(PWRAssembly::readAttributes(h5Group) == false) return false;
    } catch (...) {
        return false;
    }
    //Setup rows and cols
    this->gridLabelProvider = std::shared_ptr<GridLabelProvider> (new GridLabelProvider(this->size));
    this->gridLabelProvider.get()->setName(this->GRID_LABEL_PROVIDER_NAME);
    //Setup  tube grid manager
    this->tubeGridManager = std::shared_ptr<LWRGridManager> ( new LWRGridManager(this->size));
    this->tubeGridManager.get()->setName(this->TUBE_GRID_MANAGER_NAME);


    return true;

    // end-user-code
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
std::shared_ptr<LWRDataProvider> FuelAssembly::getTubeDataProviderAtLocation(int row, int column) {

	// begin-user-code

	//Pass through
	return this->tubeGridManager.get()->getDataProviderAtLocation(std::shared_ptr<GridLocation> (new GridLocation(row, column)));

	// end-user-code
}
