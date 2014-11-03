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

#include "LWRComponentReader.h"
#include "UtilityOperations.h"
#include <HdfReaderFactory.h>
#include <HdfFileFactory.h>
#include <map>
#include "pwr/ControlBank.h"
#include "pwr/FuelAssembly.h"
#include "pwr/IncoreInstrument.h"
#include "pwr/PWRAssembly.h"
#include "pwr/PressurizedWaterReactor.h"
#include "pwr/RodClusterAssembly.h"
#include "bwr/BWReactor.h"
#include "GridLabelProvider.h"
#include "LWRRod.h"
#include "Material.h"
#include "MaterialBlock.h"
#include "Tube.h"
#include "LWRComponent.h"
#include "LWRComposite.h"
#include "LWReactor.h"
#include "LWRGridManager.h"
#include <H5Exception.h>

using namespace ICE_Reactor;

/**
 * Copy constructor.  Not used
 */
LWRComponentReader::LWRComponentReader(LWRComponentReader & arg) {


}

/**
 * Destructor
 */
LWRComponentReader::~LWRComponentReader() {
    //TODO Auto-generated method stub
}

/**
 * Constructor
 */
LWRComponentReader::LWRComponentReader() {
    // begin-user-code

    //Create and assign default instances to the map
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( CONTROL_BANK, std::shared_ptr<LWRComponent> ( new ControlBank())));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( FUEL_ASSEMBLY, std::shared_ptr<LWRComponent> ( new FuelAssembly(-1))));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( GRID_LABEL_PROVIDER, std::shared_ptr<LWRComponent> ( new GridLabelProvider(-1))));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( INCORE_INSTRUMENT, std::shared_ptr<LWRComponent> ( new IncoreInstrument())));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( LWRROD, std::shared_ptr<LWRComponent> ( new LWRRod())));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( MATERIAL, std::shared_ptr<LWRComponent> ( new Material())));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( PWREACTOR, std::shared_ptr<LWRComponent> ( new PressurizedWaterReactor(-1))));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( RING, std::shared_ptr<LWRComponent> ( new Ring())));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( ROD_CLUSTER_ASSEMBLY, std::shared_ptr<LWRComponent> ( new RodClusterAssembly(-1))));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( MATERIALBLOCK, std::shared_ptr<LWRComponent> ( new MaterialBlock())));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( TUBE, std::shared_ptr<LWRComponent> ( new Tube())));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( LWRCOMPONENT, std::shared_ptr<LWRComponent> ( new LWRComponent())));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( LWRCOMPOSITE, std::shared_ptr<LWRComponent> ( new LWRComposite())));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( LWREACTOR, std::shared_ptr<LWRComponent> ( new LWReactor(-1))));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( BWREACTOR, std::shared_ptr<LWRComponent> ( new BWReactor(-1))));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( PWRASSEMBLY, std::shared_ptr<LWRComponent> ( new PWRAssembly(-1))));
    this->lWRComponentInstanceMap.insert( std::pair<HDF5LWRTagType, std::shared_ptr <LWRComponent> >( LWRGRIDMANAGER, std::shared_ptr<LWRComponent> ( new LWRGridManager(-1))));

    // end-user-code

}

/**
 * Parses a file given a path from HDF5 to a IHdfReadable object.
 *
 * @param the path
 *
 * @return the IHDF5Readalbe.
 */
std::shared_ptr<ICE_IO::IHdfReadable> LWRComponentReader::read(const std::string path) {
    // begin-user-code

    //Local Declarations
    std::shared_ptr<ICE_IO::IHdfReadable> nullReadable;
    std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable;

    try {
        //Open the file at the provided uri
        std::shared_ptr<H5::H5File> h5File = ICE_IO::HdfFileFactory::openH5File(path);

        //If the file is null, then return false
        if (h5File.get()== NULL) return nullReadable;


        //Get the root group from the file
        std::shared_ptr<H5::Group> rootH5Group ( new H5::Group( h5File.get()->openGroup("/")));

        //If the root group is null, then return null
        if (rootH5Group.get() == NULL) return nullReadable;


        //Get the first child group from the root group
        std::shared_ptr<H5::Group> h5Group = ICE_IO::HdfReaderFactory::getChildH5Group(rootH5Group, 0);

        //Read the group into the iHdfReadable
        iHdfReadable = LWRComponentReader::read(h5Group);

        //Close the file
        ICE_IO::HdfFileFactory::closeH5File(h5File);

    } catch (...) {
        //If error caught, return null readable
        return nullReadable;
    }

    //Return the iHdfReadable
    return iHdfReadable;

    // end-user-code

}

/**
	 * Returns a new instance of the HDF5LWRTagType or null
	 *
	 * @param  The tag
	 *
	 * @return LWRComponent
	 */
std::shared_ptr<LWRComponent> LWRComponentReader::getLWRComponentInstance(HDF5LWRTagType HDF5LWRTag) {

    //Get the LWRComponent instance corresponding to the provided tag
    std::shared_ptr<ICE_DS::Identifiable> identifiable (LWRComponentReader::lWRComponentInstanceMap.at(HDF5LWRTag).get()->clone());

    //Cast as LWRComponent
    std::shared_ptr<LWRComponent> lWRComponent = std::dynamic_pointer_cast <LWRComponent> (identifiable);

    //Return the instance
    return lWRComponent;
}

/**
 * Reads the H5Group and returns the list of IHDF5Readables
 *
 * @param The Group
 *
 * @return the IHdfReadable
 */
std::shared_ptr<ICE_IO::IHdfReadable> LWRComponentReader::read(std::shared_ptr<H5::Group> h5Group) {

    //begin-user-code

    //Local Declarations
    std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable;
    std::shared_ptr<ICE_IO::IHdfReadable> nullReadable;
    HDF5LWRTagType HDF5LWRTag;

    //Check for a null h5Group
    if (h5Group.get() == NULL) return nullReadable;


    try {
        //Turn off errors for finding the tag.  If the tag does not exist, then in this try block do not worry about it.
        H5::Exception::dontPrint();
        //Get the tag from the group
        HDF5LWRTag = UtilityOperations::fromStringHDF5Tag(ICE_IO::HdfReaderFactory::readStringAttribute(h5Group, "HDF5LWRTag"));
    } catch (...) {
        //Tag does not exist, then return
        return nullReadable;
    }

    try {
        //If the tag does not exist, skip it
        if(HDF5LWRTag) {
            //Get an instance from the tag
            iHdfReadable = LWRComponentReader::getLWRComponentInstance(HDF5LWRTag);

            //Read in the attributes
            iHdfReadable.get()->readAttributes(h5Group);

            //Read in the datasets
            iHdfReadable.get()->readDatasets(h5Group);

            //Get a list of child groups
            std::vector<std::shared_ptr<H5::Group> > childGroupList = ICE_IO::HdfReaderFactory::getChildH5Groups(h5Group);

            //If the group has entries
            if (childGroupList.size() > 0) {

                //Cycle over these entries
                for (int i = 0; i < childGroupList.size(); i++ ) {

                    //Create and populate a child from the child group
                    std::shared_ptr<ICE_IO::IHdfReadable> child = LWRComponentReader::read(childGroupList.at(i));

                    //Read the child into the readable
                    if(child.get() != NULL) {
                        iHdfReadable.get()->readChild(child);
                    }


                }

            }
        }

        //Return the top level readable
        return iHdfReadable;
    } catch (...) {
        //Throw it up if error
        throw -1;
    }

    //end-user-code

}
