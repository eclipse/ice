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

#ifndef LWRGRIDMANAGER_H
#define LWRGRIDMANAGER_H

#include "IGridManager.h"
#include "LWRComponent.h"
#include "LWRDataProvider.h"
#include "GridLocation.h"
#include <H5Cpp.h>
#include <map>
#include <vector>
#include <string>

namespace ICE_Reactor {

/**
 * <p>The LWRGridManager class manages LWRComponents and their GridLocations on
 * a Cartesian grid with an equal number of rows and columns. This class
 * implements the ICE IGridManager interface.</p>
 *
 * <p>This class also allows a
 * "pass through" for LWRDataProviders, which are used to store state point data.
 * This is a preferred method for storing data over time instead of using
 * LWRComponent's IDataProvider directly.  Please see GridLocation for more
 * details on the usage of this delegation class.</p>
 */
class LWRGridManager : public IGridManager, public LWRComponent {

private:

    /**
     * A TreeMap keyed on GridLocation with unique LWRComponent values.
     */
    std::map<std::shared_ptr<GridLocation>, std::string> lWRComponents;

    /**
     * The size of the rows and columns.
     */
    int size;

protected:

    /**
     * A grid table suffix for reading the dataset.
     */
    std::string hdf5GridTableSuffix;
    std::string dataH5GroupName;
    std::string headTableString;
    std::string dataTableString;

public:

    typedef struct {
        int x, y;
        char *locationName;
    } grid;

    /**
     * The Copy Constructor
     */
    LWRGridManager(LWRGridManager & arg);

    /**
     * The Destructor
     */
    virtual ~LWRGridManager();

    /**
     * A parameterized Constructor.
     *
     * @param the size
     */
    LWRGridManager(int size);

    /**
     * Returns the maximum number of rows or columns.
     *
     * @return the size
     */
    int getSize();

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const LWRGridManager& otherObject) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return newly instantiated object
     */
    std::shared_ptr<ICE_DS::Identifiable> clone();

    /**
     * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
     *
     * @param The File
     * @param The Group
     *
     * @return True if successful, false otherwise
     */
    bool writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

    /**
     * This operation writes HDF5 Datasets to the h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Datasets, then false is returned. Otherwise, true is returned.
     *
     * @param The File
     * @param The Group
     *
     * @return True if successful, false otherwise
     */
    bool writeDatasets(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

    /**
     * This operation reads Datasets from h5Group and assigns their values to class variables. If h5Group is null or an Exception is thrown, false is returned. If the Otherwise, true is returned.
     *
     * @param The Group
     *
     * @return True if successful, false otherwise
     */
    bool readDatasets(std::shared_ptr<H5::Group> h5Group);

    /**
     * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
     *
     * @param  The Group
     *
     * @return True if successful, false otherwise
     */
    bool readAttributes(std::shared_ptr<H5::Group> h5Group);

    /**
     * Overridden from IGridManager
     */
    const std::string getComponentName(std::shared_ptr<GridLocation> location);

    /**
     * Overridden from IGridManager
     */
    void addComponent(std::shared_ptr<ICE_DS::Component> component, std::shared_ptr<GridLocation> location);

    /**
     * Overridden from IGridManager
     */
    void removeComponent(std::shared_ptr<GridLocation> location) ;

    /**
     * Overridden from IGridManager
     */
    void removeComponent(std::shared_ptr<ICE_DS::Component> component);

    /**
     * Gets the data provider at location or null
     *
     * @param the location
     *
     * @return the provider @ location
     */
    std::shared_ptr<LWRDataProvider> getDataProviderAtLocation(std::shared_ptr<GridLocation> location);

    /**
     * Gets the GridLocations at the name
     *
     * @param the name
     *
     * @return the GridLocations at the name
     */
    std::vector < std::shared_ptr<GridLocation> > getGridLocationsAtName(const std::string name);

private:
    /**
     * A private operation for handling group creation
     *
     * @param H5 File
     * @param the h5Group
     */
    bool writeFeatureSets(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

    /**
     * Writes the Datasets for all the time steps to the group. Returns true if
     * the operation was successful, false otherwise.
     *
     * @param dataH5Group
     *            The main data group
     * @param h5File
     *            The h5file
     * @param provider
     *            The LWRGridDataProvider @ Location
     * @param unitsList
     *            The list of units. Must be passed to keep the list maintained!
     * @return True if successful, false otherwise.
     */
    bool writeTimeAtFeatureSet(std::shared_ptr<H5::Group> dataH5Group, std::shared_ptr<H5::H5File> h5File,
    		std::shared_ptr<LWRDataProvider> provider, std::shared_ptr< std::vector< std::string > > unitsList);

	/**
	 * Reads the time steps at a feature and adds the time steps to the GridLocation.  Returns true if the operation was successful, false otherwise.
	 *
	 * @param provider The LWRDataProvider
	 * @param timeStepsMemberList The time steps on that group
	 * @param arrayStrings  An array of strings used to specify the unit list.
	 * @return
	 */
	bool readTimeStepsAtFeature(std::shared_ptr<LWRDataProvider> provider, std::vector<std::shared_ptr < H5::Group > > timeGroups, std::vector< std::string > arrayStrings);

};

}

#endif
