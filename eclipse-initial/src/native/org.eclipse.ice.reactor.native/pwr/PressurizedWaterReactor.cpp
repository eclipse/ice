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

#include "PressurizedWaterReactor.h"
#include <H5Cpp.h>
#include <vector>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include "FuelAssembly.h"
#include "ControlBank.h"
#include "RodClusterAssembly.h"
#include "IncoreInstrument.h"
#include <memory>
#include <string>
#include "../GridLocation.h"
#include "../GridLabelProvider.h"
#include "../AssemblyType.h"
#include <map>
#include <stdexcept>
#include <algorithm>

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
PressurizedWaterReactor::PressurizedWaterReactor(PressurizedWaterReactor & arg) : LWReactor(arg) {

	//begin-user-code

    //Copy contents (Deep as necessary)

	//Local Declarations
	std::map<AssemblyType, std::shared_ptr <LWRGridManager> >::const_iterator gridIter;
	std::map<AssemblyType, std::shared_ptr <LWRComposite> >::const_iterator compIter;

    this->size == arg.size;
    this->fuelAssemblyPitch = arg.fuelAssemblyPitch;
    this->gridLabelProvider = std::dynamic_pointer_cast<GridLabelProvider> (arg.gridLabelProvider.get()->clone());

    //Initialize maps
    assemblyComposites = std::shared_ptr< std::map<AssemblyType, std::shared_ptr< LWRComposite > > > (new std::map<AssemblyType, std::shared_ptr< LWRComposite > >());
    managers = std::shared_ptr< std::map<AssemblyType, std::shared_ptr< LWRGridManager > > > (new std::map<AssemblyType, std::shared_ptr< LWRGridManager > >());


    //Deep copy the GridManagers and AssemblyComposites

    //Iterate over the managers and copy them
    for(gridIter = arg.managers.get()->begin(); gridIter != arg.managers.get()->end(); ++gridIter) {
    	//Clone the Grid Manager
    	std::shared_ptr<LWRGridManager> gridManager = std::dynamic_pointer_cast<LWRGridManager>(gridIter->second.get()->clone());



    	//Add it to the list
    	this->managers.get()->insert(std::pair<AssemblyType, std::shared_ptr <LWRGridManager> > (gridIter->first, gridManager));
     }

    //Iterate over the assemblies again, but add them back - saves references between the map and the lwrcomponents.  *** IMPORTANT ***
    for(compIter = arg.assemblyComposites.get()->begin(); compIter != arg.assemblyComposites.get()->end(); ++compIter) {

    	//Clone the composite
    	std::shared_ptr<LWRComposite> composite = std::dynamic_pointer_cast<LWRComposite>(compIter->second.get()->clone());

    	//Add it to the list
    	this->assemblyComposites.get()->insert(std::pair<AssemblyType, std::shared_ptr <LWRComposite> > (compIter->first, composite));

    	//Add it to the components
    	this->lWRComponents.insert(std::pair< std::string, std::shared_ptr< LWRComponent > >(composite.get()->getName(), composite));

    }

    //Setup row and column information

    this->CONTROL_BANK_COMPOSITE_NAME = "Control Banks";
    this->FUEL_ASSEMBLY_COMPOSITE_NAME = "Fuel Assemblies";
    this->INCORE_INSTRUMENT_COMPOSITE_NAME = "Incore Instruments";
    this->ROD_CLUSTER_ASSEMBLY_COMPOSITE_NAME = "Rod Cluster Assemblies";

    this->CONTROL_BANK_GRID_MANAGER_NAME = "Control Bank Grid";
    this->FUEL_ASSEMBLY_GRID_MANAGER_NAME = "Fuel Assembly Grid";
    this->INCORE_INSTRUMENT_GRID_MANAGER_NAME = "Incore Instrument Grid";
    this->ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME = "Rod Cluster Assembly Grid";

    this->GRID_LABEL_PROVIDER_NAME = "Grid Labels";

    //end-user-code

}

/**
 * The Destructor
 */
PressurizedWaterReactor::~PressurizedWaterReactor() {
    //TODO Auto-generated method stub
}

/**
 * A parameterized Constructor.
 *
 * @param the size
 */
PressurizedWaterReactor::PressurizedWaterReactor(int size) : LWReactor(size) {

    // begin-user-code

    //Set the default value of size
    if (size > 0) {
        this->size = size;
    } else {
        this->size = 17; //Auto default size!
    }

    //Initialize everything
    //Setup row and column information

    this->CONTROL_BANK_COMPOSITE_NAME = "Control Banks";
    this->FUEL_ASSEMBLY_COMPOSITE_NAME = "Fuel Assemblies";
    this->INCORE_INSTRUMENT_COMPOSITE_NAME = "Incore Instruments";
    this->ROD_CLUSTER_ASSEMBLY_COMPOSITE_NAME = "Rod Cluster Assemblies";

    this->CONTROL_BANK_GRID_MANAGER_NAME = "Control Bank Grid";
    this->FUEL_ASSEMBLY_GRID_MANAGER_NAME = "Fuel Assembly Grid";
    this->INCORE_INSTRUMENT_GRID_MANAGER_NAME = "Incore Instrument Grid";
    this->ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME = "Rod Cluster Assembly Grid";

    this->GRID_LABEL_PROVIDER_NAME = "Grid Labels";

    //Setup Control Banks information
    std::shared_ptr<LWRComposite> controlBankComposite = std::shared_ptr<LWRComposite> (new LWRComposite());
    controlBankComposite.get()->setName(this->CONTROL_BANK_COMPOSITE_NAME);
    controlBankComposite.get()->setDescription("A Composite that contains many ControlBank Components.");
    controlBankComposite.get()->setId(1);
    //Put into the lWRComponents
    lWRComponents.insert(std::pair<std::string, std::shared_ptr <LWRComponent> > (controlBankComposite.get()->getName(), controlBankComposite));

    //Setup Grid Manager
    std::shared_ptr< LWRGridManager > controlBankGridManager = std::shared_ptr<LWRGridManager> (new LWRGridManager(this->size));
    controlBankGridManager.get()->setName(this->CONTROL_BANK_GRID_MANAGER_NAME);

    //Setup Fuel Assemblies information
    std::shared_ptr<LWRComposite> fuelAssemblyComposite = std::shared_ptr<LWRComposite> (new LWRComposite());
    fuelAssemblyComposite.get()->setName(this->FUEL_ASSEMBLY_COMPOSITE_NAME);
    fuelAssemblyComposite.get()->setDescription("A Composite that contains many FuelAssembly Components.");
    fuelAssemblyComposite.get()->setId(2);
    //Put into the lWRComponents
    this->lWRComponents.insert(std::pair<std::string, std::shared_ptr <LWRComponent> > (fuelAssemblyComposite.get()->getName(), fuelAssemblyComposite));

    //Setup Grid Manager
    std::shared_ptr< LWRGridManager > fuelAssemblyGridManager =std::shared_ptr<LWRGridManager> ( new LWRGridManager(this->size));
    fuelAssemblyGridManager.get()->setName(this->FUEL_ASSEMBLY_GRID_MANAGER_NAME);

    this->fuelAssemblyPitch = 0.0;

    //Setup InCoreInstruments
    std::shared_ptr<LWRComposite> incoreInstrumentComposite = std::shared_ptr<LWRComposite> (new LWRComposite());
    incoreInstrumentComposite.get()->setName(this->INCORE_INSTRUMENT_COMPOSITE_NAME);
    incoreInstrumentComposite.get()->setDescription("A Composite that contains many IncoreInstrument Components.");
    incoreInstrumentComposite.get()->setId(3);
    //Put into the lWRComponents
    this->lWRComponents.insert(std::pair<std::string, std::shared_ptr <LWRComponent> > (incoreInstrumentComposite.get()->getName(), incoreInstrumentComposite));

    //Setup Grid Manager
    std::shared_ptr< LWRGridManager > incoreInstrumentGridManager = std::shared_ptr<LWRGridManager> (new LWRGridManager(this->size));
    incoreInstrumentGridManager.get()->setName(this->INCORE_INSTRUMENT_GRID_MANAGER_NAME);

    //Setup RodClusterAssemblies
    std::shared_ptr<LWRComposite> rodClusterAssemblyComposite = std::shared_ptr<LWRComposite> (new LWRComposite());
    rodClusterAssemblyComposite.get()->setName(this->ROD_CLUSTER_ASSEMBLY_COMPOSITE_NAME);
    rodClusterAssemblyComposite.get()->setDescription("A Composite that contains many RodClusterAssembly Components.");
    rodClusterAssemblyComposite.get()->setId(4);
    //Put into the lWRComponents
    this->lWRComponents.insert(std::pair<std::string, std::shared_ptr <LWRComponent> > (rodClusterAssemblyComposite.get()->getName(), rodClusterAssemblyComposite));

    //Setup Grid Manager
    std::shared_ptr< LWRGridManager > rodClusterAssemblyGridManager = std::shared_ptr<LWRGridManager> (new LWRGridManager(this->size));
    rodClusterAssemblyGridManager.get()->setName(this->ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME);

    //Add a default GridLabelProvider
    this->gridLabelProvider = std::shared_ptr<GridLabelProvider> (new GridLabelProvider(this->size));
    this->gridLabelProvider.get()->setName(this->GRID_LABEL_PROVIDER_NAME);

    //Setup the Name, Id, Description
    this->name = "PressurizedWaterReactor 1";
    this->id = 1;
    this->description = "PressurizedWaterReactor 1's Description";

    //Setup the LWRComponentType to the correct Type
    this->HDF5LWRTag = PWREACTOR;

	// Setup the composite map
    assemblyComposites = std::shared_ptr< std::map<AssemblyType, std::shared_ptr< LWRComposite > > > (new std::map<AssemblyType, std::shared_ptr< LWRComposite > >());
	assemblyComposites.get()->insert(std::pair<AssemblyType, std::shared_ptr <LWRComposite> > (Control_Bank, controlBankComposite));
	assemblyComposites.get()->insert(std::pair<AssemblyType, std::shared_ptr <LWRComposite> > (Fuel, fuelAssemblyComposite));
	assemblyComposites.get()->insert(std::pair<AssemblyType, std::shared_ptr <LWRComposite> > (Incore_Instrument,
			incoreInstrumentComposite));
	assemblyComposites.get()->insert(std::pair<AssemblyType, std::shared_ptr <LWRComposite> > (RodCluster,
			rodClusterAssemblyComposite));

	// Setup the grid map
	managers = std::shared_ptr< std::map<AssemblyType, std::shared_ptr< LWRGridManager > > > (new std::map<AssemblyType, std::shared_ptr< LWRGridManager > >());
	managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (Control_Bank, controlBankGridManager));
	managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (Fuel, fuelAssemblyGridManager));
	managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (Incore_Instrument, incoreInstrumentGridManager));
	managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (RodCluster, rodClusterAssemblyGridManager));

    // end-user-code
}

/**
 * Returns the number of fuel assemblies across the core.
 *
 * @return the size
 */
int PressurizedWaterReactor::getSize() {

    // begin-user-code

    return this->size;

    // end-user-code

}

/**
 * Returns the GridLabelProvider for this PressurizedWaterReactor.
 *
 * @return the grid label provider
 */
std::shared_ptr<GridLabelProvider> PressurizedWaterReactor::getGridLabelProvider() {

    // begin-user-code

    return this->gridLabelProvider;

    // end-user-code

}

/**
 * Sets the GridLabelProvider for this PressurizedWaterReactor.
 *
 * @param the grid label provider to set
 */
void PressurizedWaterReactor::setGridLabelProvider(std::shared_ptr<GridLabelProvider> gridLabelProvider) {

    // begin-user-code

    //Set the grid label provider if it is not null and the same size
    if (gridLabelProvider.get() != NULL && gridLabelProvider.get()->getSize() == this->size) {
        this->gridLabelProvider = gridLabelProvider;
    }

    return;

    // end-user-code

}

/**
 * Returns the distance between assemblies in the core, determined by the seating location in the core plates.
 *
 * @return the fuelAssemblyPitch
 */
double PressurizedWaterReactor::getFuelAssemblyPitch() {

    // begin-user-code

    return this->fuelAssemblyPitch;

    // end-user-code

}

/**
 * Sets the distance between assemblies in the core, determined by the seating location in the core plates.
 *
 * @param the fuelAssemblyPitch to set
 */
void PressurizedWaterReactor::setFuelAssemblyPitch(double fuelAssemblyPitch) {

    // begin-user-code

    if (fuelAssemblyPitch >= 0) {
        this->fuelAssemblyPitch = fuelAssemblyPitch;
    }

    return;

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
bool PressurizedWaterReactor::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {
    // begin-user-code

    bool flag = true;

    flag &= LWReactor::writeAttributes(h5File, h5Group);
    flag &= ICE_IO::HdfWriterFactory::writeDoubleAttribute(h5File, h5Group, "fuelAssemblyPitch", this->fuelAssemblyPitch);

    return flag;

    // end-user-code
}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool PressurizedWaterReactor::operator==(const PressurizedWaterReactor& otherObject) const {

    // begin-user-code

	//Check map values
	std::map<AssemblyType, std::shared_ptr <LWRGridManager> >::const_iterator gridIter;
	std::map<AssemblyType, std::shared_ptr <LWRComposite> >::const_iterator compIter;

    //Compare values
    bool retVal = LWReactor::operator ==(otherObject)
                  && this->size == otherObject.size
                  && this->fuelAssemblyPitch == otherObject.fuelAssemblyPitch
                  && this->gridLabelProvider.get()->operator==(*otherObject.gridLabelProvider.get())
                  && this->managers.get()->size() == otherObject.managers.get()->size() //Check size only here
                  && this->assemblyComposites.get()->size() == otherObject.assemblyComposites.get()->size(); //Check only size here

    //Check GridManagers and assemblycomposites.  We can SAFELY assume they are the same size

    //Since the composites are linked in the LWRComponents tree, we dont have to check those AGAIN (as long as they are the same size, and verified, should be okay).

    //Check Managers
    for(gridIter = this->managers.get()->begin(); gridIter != this->managers.get()->end(); ++gridIter) {

    	//IF the values do not compare correctly, return false
        try {
        	if(this->managers.get()->at(gridIter->first).get()->operator ==(*otherObject.managers.get()->at(gridIter->first).get()) == false) {
        			return false;
        	}
        } catch (const std::out_of_range& oor) {
        	return false;
        }

    }

    //Return retVal
    return retVal;

    // end-user-code
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> PressurizedWaterReactor::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<PressurizedWaterReactor> component(new PressurizedWaterReactor (*this));

    // Return the newly instantiated object
    return component;

    // end-user-code
}

/**
 * Returns the list of writeable childen.
 *
 * @return List of writeable chidlren
 */
std::vector<std::shared_ptr<ICE_IO::IHdfWriteable> > PressurizedWaterReactor::getWriteableChildren() {

    //begin-user-code

    //Get the children in super
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > children = LWReactor::getWriteableChildren();


    //Add  children - grid labels and providers
    children.push_back(this->gridLabelProvider);
    children.push_back(this->managers.get()->at(Control_Bank));
    children.push_back(this->managers.get()->at(Fuel));
    children.push_back(this->managers.get()->at(Incore_Instrument));
    children.push_back(this->managers.get()->at(RodCluster));

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
bool PressurizedWaterReactor::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    //begin-user-code

    //Local attributes (so we only call read ONCE)
    //These will clear out by the garbage collector
    int size;
    double fuelAssemblyPitch;

    //Check super
    if(h5Group.get() == NULL) {
        return false;
    }

    //Get the information.  If any fail out, return false and do not change data.
    try {
        if (LWReactor::readAttributes(h5Group) == false) return false;
        fuelAssemblyPitch = ICE_IO::HdfReaderFactory::readDoubleAttribute(h5Group, "fuelAssemblyPitch");
    } catch (...) {
        return false;
    }

    //Set the primitive data
    this->fuelAssemblyPitch = fuelAssemblyPitch;

    //Reset info
    this->gridLabelProvider = std::shared_ptr<GridLabelProvider> (new GridLabelProvider(this->size));
    this->gridLabelProvider.get()->setName(this->GRID_LABEL_PROVIDER_NAME);

    std::shared_ptr<LWRGridManager> controlBankGridManager = std::shared_ptr<LWRGridManager> (new LWRGridManager(this->size));
    controlBankGridManager.get()->setName(this->CONTROL_BANK_GRID_MANAGER_NAME);
    std::shared_ptr<LWRGridManager> fuelAssemblyGridManager= std::shared_ptr<LWRGridManager> (new LWRGridManager(this->size));
    fuelAssemblyGridManager.get()->setName(FUEL_ASSEMBLY_GRID_MANAGER_NAME);
    std::shared_ptr<LWRGridManager> incoreInstrumentGridManager = std::shared_ptr<LWRGridManager> (new LWRGridManager(this->size));
    incoreInstrumentGridManager.get()->setName(INCORE_INSTRUMENT_GRID_MANAGER_NAME);
    std::shared_ptr<LWRGridManager> rodClusterAssemblyGridManager = std::shared_ptr<LWRGridManager> (new LWRGridManager(this->size));
    rodClusterAssemblyGridManager.get()->setName(ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME);

    // Setup the grid map
    managers = std::shared_ptr< std::map<AssemblyType, std::shared_ptr< LWRGridManager > > > (new std::map<AssemblyType, std::shared_ptr< LWRGridManager > >());
    managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (Control_Bank, controlBankGridManager));
    managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (Fuel, fuelAssemblyGridManager));
    managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (Incore_Instrument, incoreInstrumentGridManager));
    managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (RodCluster, rodClusterAssemblyGridManager));

    return true;

    //end-user-code

}

/**
 * This operation reads IHdfReadable child objects. If this IHdfReadable has no IHdfReadable child objects, false is returned.
 *
 * @param the readable child
 *
 * @return true if successful, false otherwise
 */
bool PressurizedWaterReactor::readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable) {

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

        //If its a ControlBankComposite
        if(newType->getHDF5LWRTag() == LWRCOMPOSITE && newType->getName().compare(this->CONTROL_BANK_COMPOSITE_NAME) == 0) {
            //Dynamic cast up to a LWRComposite (which will preserve the clone operation on implemented dervied classes without calling new!
            //Remove the previous object
        	std::shared_ptr<LWRComposite> composite = std::dynamic_pointer_cast<LWRComposite> (iHdfReadable);
        	LWRComposite::removeComponent(composite.get()->getName());
            //Add object back
            LWRComposite::addComponent(composite);
            this->assemblyComposites.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRComposite > > (Control_Bank, composite));
        }

        //If it is a fuelAssembly
        else if(newType->getHDF5LWRTag() == LWRCOMPOSITE && newType->getName().compare(this->FUEL_ASSEMBLY_COMPOSITE_NAME) == 0) {
            //Dynamic cast up to a LWRComposite (which will preserve the clone operation on implemented dervied classes without calling new!
            //Remove the previous object
        	std::shared_ptr<LWRComposite> composite = std::dynamic_pointer_cast<LWRComposite> (iHdfReadable);
        	LWRComposite::removeComponent(composite.get()->getName());
            //Add object back
            LWRComposite::addComponent(composite);
            this->assemblyComposites.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRComposite > > (Fuel, composite));
        }

        //If its a incoreInstrument
        else if(newType->getHDF5LWRTag() == LWRCOMPOSITE && newType->getName().compare(this->INCORE_INSTRUMENT_COMPOSITE_NAME) == 0) {
            //Dynamic cast up to a LWRComposite (which will preserve the clone operation on implemented dervied classes without calling new!
            //Remove the previous object
        	std::shared_ptr<LWRComposite> composite = std::dynamic_pointer_cast<LWRComposite> (iHdfReadable);
            LWRComposite::removeComponent(composite.get()->getName());
            //Add object back
            LWRComposite::addComponent(composite);
            this->assemblyComposites.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRComposite > > (Incore_Instrument, composite));
        }

        //If its a RCA
        else if(newType->getHDF5LWRTag() == LWRCOMPOSITE && newType->getName().compare(this->ROD_CLUSTER_ASSEMBLY_COMPOSITE_NAME) == 0) {
            //Dynamic cast up to a LWRComposite (which will preserve the clone operation on implemented dervied classes without calling new!
            //Remove the previous object
        	std::shared_ptr<LWRComposite> composite = std::dynamic_pointer_cast<LWRComposite> (iHdfReadable);
        	LWRComposite::removeComponent(composite.get()->getName());
        	//Add object back
        	LWRComposite::addComponent(composite);
        	this->assemblyComposites.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRComposite > > (RodCluster, composite));
        }

        //If its a ControlBank GridManager
        else if(newType->getHDF5LWRTag() == LWRGRIDMANAGER && newType->getName().compare(this->CONTROL_BANK_GRID_MANAGER_NAME) == 0) {
            //Dynamic cast up to a LWRGridManager (which will preserve the clone operation on implemented dervied classes without calling new!
        	this->managers.get()->erase(Control_Bank);
        	std::shared_ptr<LWRGridManager> gridManager =  std::dynamic_pointer_cast<LWRGridManager> (iHdfReadable);
        	this->managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (Control_Bank, gridManager));

        }

        //If it is a fuelAssembly GridManager
        else if(newType->getHDF5LWRTag() == LWRGRIDMANAGER && newType->getName().compare(this->FUEL_ASSEMBLY_GRID_MANAGER_NAME) == 0) {
            //Dynamic cast up to a LWRGridManager (which will preserve the clone operation on implemented dervied classes without calling new!
        	this->managers.get()->erase(Fuel);
        	std::shared_ptr<LWRGridManager> gridManager =  std::dynamic_pointer_cast<LWRGridManager> (iHdfReadable);
        	this->managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (Fuel, gridManager));
        }

        //If its a incoreInstrument GridManager
        else if(newType->getHDF5LWRTag() == LWRGRIDMANAGER && newType->getName().compare(this->INCORE_INSTRUMENT_GRID_MANAGER_NAME) == 0) {
            //Dynamic cast up to a LWRGridManager (which will preserve the clone operation on implemented dervied classes without calling new!
        	this->managers.get()->erase(Incore_Instrument);
        	std::shared_ptr<LWRGridManager> gridManager =  std::dynamic_pointer_cast<LWRGridManager> (iHdfReadable);
        	this->managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (Incore_Instrument, gridManager));
        }

        //If its a RCA GridManager
        else if(newType->getHDF5LWRTag() == LWRGRIDMANAGER && newType->getName().compare(this->ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME) == 0) {
            //Dynamic cast up to a LWRGridManager (which will preserve the clone operation on implemented dervied classes without calling new!
        	this->managers.get()->erase(RodCluster);
        	std::shared_ptr<LWRGridManager> gridManager =  std::dynamic_pointer_cast<LWRGridManager> (iHdfReadable);
        	this->managers.get()->insert(std::pair<AssemblyType, std::shared_ptr< LWRGridManager > > (RodCluster, gridManager));
        }

        //if its GridLabels
        else if(newType->getHDF5LWRTag() == GRID_LABEL_PROVIDER && newType->getName().compare(this->GRID_LABEL_PROVIDER_NAME) == 0) {
            //Dynamic cast up to a GridLabelProvider (which will preserve the clone operation on implemented dervied classes without calling new!
            this->gridLabelProvider = std::dynamic_pointer_cast<GridLabelProvider> (iHdfReadable);
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
 * <!-- begin-UML-doc -->
 * <p>
 * This operation adds an assembly of the specified AssemblyType to the
 * reactor and return true. If an assembly of the same name and type already
 * exists in the reactor, then the new assembly will not be added and this
 * operation will return false.
 * </p>
 * <!-- end-UML-doc -->
 *
 * @param type
 *            <p>
 *            The type of the assembly.
 *            </p>
 * @param assembly
 *            <p>
 *            The assembly to add to the collection of FuelAssemblies.
 *            </p>
 * @return <p>
 *         True, if the assembly was added successfully.
 *         </p>
 * @generated
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
bool PressurizedWaterReactor::addAssembly(AssemblyType type, std::shared_ptr<LWRComponent> assembly) {

	//begin-user-code

	// Local Declarations
	std::shared_ptr<LWRComposite> composite = assemblyComposites.get()->find(type)->second;

	//Add Component
	composite.get()->addComponent(assembly);

	//Check to see if the data was added
	std::vector< std::shared_ptr<ICE_DS::Component> > comps = composite.get()->getComponents();

	//If the component does exist, return true. Else false
	if(find(comps.begin(), comps.end(),assembly) != comps.end()) return true;
	else return false;

	//end-user-code

}

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Removes an assembly of the specified type from the collection of
 * assemblies. Returns true if the operation was successful, false
 * otherwise.
 * </p>
 * <!-- end-UML-doc -->
 *
 * @param type
 *            <p>
 *            The type of the assembly.
 *            </p>
 * @param assemblyName
 *            <p>
 *            The name of the assembly to be removed.
 *            </p>
 * @return <p>
 *         True, if the assembly was removed successfully.
 *         </p>
 * @generated
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
bool PressurizedWaterReactor::removeAssembly(AssemblyType type, std::string assemblyName) {

	// begin-user-code

	// Local Declarations
	std::shared_ptr<LWRComposite> composite = assemblyComposites.get()->find(type)->second;
	std::shared_ptr<ICE_DS::Component> component = composite.get()->getComponent(assemblyName);
	std::shared_ptr<LWRGridManager> manager = managers.get()->find(type)->second;
	bool status = true;

	// Only do this if the component exists
	if (component.get() != NULL) {
		// Remove the assembly from the composite and grid
		manager.get()->removeComponent(component);
		composite.get()->removeComponent(assemblyName);
	} else {
		status = false;
	}

	return status;

	// end-user-code

}

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Returns an list of names for each assembly in the reactor of the
 * specified type.
 * </p>
 * <!-- end-UML-doc -->
 *
 * @param type
 *            <p>
 *            The type of the assembly.
 *            </p>
 * @return <p>
 *         An ArrayList of names for each element of the collection of
 *         assemblies.
 *         </p>
 * @generated
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::vector<std::string> PressurizedWaterReactor::getAssemblyNames(AssemblyType type) {

	// begin-user-code

	// Local Declarations
	std::shared_ptr<LWRComposite> composite = assemblyComposites.get()->find(type)->second;

	return composite.get()->getComponentNames();
	// end-user-code

}

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Returns the assembly of the specified type with the provided name or null
 * if an assembly of that type and name does not exist.
 * </p>
 * <!-- end-UML-doc -->
 *
 * @param type
 *            <p>
 *            The type of the assembly.
 *            </p>
 * @param name
 *            <p>
 *            The name of the assembly to find.
 *            </p>
 * @return <p>
 *         The assembly
 *         </p>
 * @generated
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::shared_ptr<LWRComponent> PressurizedWaterReactor::getAssemblyByName(AssemblyType type, std::string name) {

	// begin-user-code

	// Local Declarations
	std::shared_ptr<LWRComponent> lwrComponent;
	std::shared_ptr<LWRComposite> composite = assemblyComposites.get()->find(type)->second;

	std::shared_ptr<ICE_DS::Component> ds_Component = composite.get()->getComponent(name);

	//If exists, cast it to a LWRComponent
	if(ds_Component.get() != NULL) {
		lwrComponent = std::dynamic_pointer_cast<LWRComponent>(ds_Component);
	}

	// Return the composite
	return lwrComponent;

	// end-user-code

}

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Returns the assembly of the specified type at the specified column and
 * row in the reactor or null if one is not found at the provided location.
 * </p>
 * <!-- end-UML-doc -->
 *
 * @param type
 *            <p>
 *            The type of the assembly.
 *            </p>
 * @param row
 *            <p>
 *            The row id.
 *            </p>
 * @param column
 *            <p>
 *            The column id.
 *            </p>
 * @return <p>
 *         The assembly corresponding to the provided type, column and row
 *         or null if one is not found at the provided location.
 *         </p>
 * @generated
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::shared_ptr<LWRComponent> PressurizedWaterReactor::getAssemblyByLocation(AssemblyType type, int row,
		int column) {

	// begin-user-code

	// Local Declarations
	std::shared_ptr<LWRGridManager> manager = managers.get()->find(type)->second;
	std::string name = "";

	//Create GridLocation
	std::shared_ptr<GridLocation> location (new GridLocation (row, column));

	// Get the name
	name = manager.get()->getComponentName(location);

	// Return the component
	return getAssemblyByName(type, name);

	// end-user-code

}

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Sets the location for the assembly of the specified type with the
 * provided name. Overrides the location of another assembly as required.
 * Returns true if this operation was successful, false otherwise. Note it
 * will return true if the same name is overridden.
 * </p>
 * <!-- end-UML-doc -->
 *
 * @param type
 *            <p>
 *            The type of the assembly.
 *            </p>
 * @param assemblyName
 *            <p>
 *            The name of the assembly.
 *            </p>
 * @param row
 *            <p>
 *            The row id.
 *            </p>
 * @param column
 *            <p>
 *            The column id.
 *            </p>
 * @return <p>
 *         True, if the location of the assembly was set successfully.
 *         </p>
 * @generated
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
bool PressurizedWaterReactor::setAssemblyLocation(AssemblyType type, std::string assemblyName,
		int row, int column) {

	// begin-user-code

	// Local declarations
	std::shared_ptr<LWRComposite> composite = assemblyComposites.get()->find(type)->second;
	std::shared_ptr<ICE_DS::Component> component = composite.get()->getComponent(assemblyName);
	std::shared_ptr<LWRGridManager> manager = managers.get()->find(type)->second;
	std::shared_ptr<GridLocation> location  (new GridLocation(row, column));
	bool status = false;
	std::string nullString;

	// Set the location, but only if it is valid. This makes sure that
	// negative row numbers and other nasty things were not passed to this
	// operation.
	if (location.get()->getColumn() == column && location.get()->getRow() == row ) {

		// Set the location
		manager.get()->addComponent(component, location);

		// Update the status
		status = (manager.get()->getComponentName(location).compare(nullString) != 0 && manager.get()->
				getComponentName(location).compare(assemblyName) == 0);
	}

	return status;
	// end-user-code

}

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Removes the assembly at the provided location and of the specified if it
 * exists. Returns true if the removal was successful, false otherwise.
 * </p>
 * <!-- end-UML-doc -->
 *
 * @param type
 *            <p>
 *            The type of the assembly.
 *            </p>
 * @param row
 *            <p>
 *            The row id.
 *            </p>
 * @param column
 *            <p>
 *            The column id.
 *            </p>
 * @return <p>
 *         True, if the assembly removal was successful.
 *         </p>
 * @generated
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
bool PressurizedWaterReactor::removeAssemblyFromLocation(AssemblyType type, int row,
		int column) {

	// begin-user-code

	// Local Declarations
	std::shared_ptr<LWRGridManager> manager = managers.get()->find(type)->second;
	bool status = true;
	std::shared_ptr<GridLocation> location (new GridLocation(row, column));
	std::string name = manager.get()->getComponentName(location);
	std::string nullString;

	// Only do this if there is a component at the specified location
	if (name.compare(nullString) == 0) {
		// Remove the component from the location
		manager.get()->removeComponent(location);
	} else {
		status = false;
	}

	return status;
	// end-user-code

}

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Returns the data provider for the assembly of the specified type at the
 * given location or null if it does not exist.
 * </p>
 * <!-- end-UML-doc -->
 *
 * @param type
 *            <p>
 *            The type of the assembly.
 *            </p>
 * @param row
 *            <p>
 *            The row id.
 *            </p>
 * @param column
 *            <p>
 *            The column id.
 *            </p>
 * @return <p>
 *         The DataProvider that manages state point data for the specified
 *         assembly.
 *         </p>
 * @generated
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::shared_ptr<LWRDataProvider> PressurizedWaterReactor::getAssemblyDataProviderAtLocation(AssemblyType type,
		int row, int column) {

	// begin-user-code

	// Local Declarations
	std::shared_ptr<LWRGridManager> manager = managers.get()->find(type)->second;

	//Setup the location
	std::shared_ptr<GridLocation> location (new GridLocation(row, column));

	// Return the provider
	return manager.get()->getDataProviderAtLocation(location);

	// end-user-code

}

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This operation returns the number of assemblies of the specified type.
 * </p>
 * <!-- end-UML-doc -->
 *
 * @param type
 *            <p>
 *            The type of the assembly.
 *            </p>
 * @return <p>
 *         The number of assemblies of the specified type.
 *         </p>
 * @generated
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
int PressurizedWaterReactor::getNumberOfAssemblies(AssemblyType type) {

	// begin-user-code
	return assemblyComposites.get()->find(type)->second.get()->getNumberOfComponents();
	// end-user-code

}
