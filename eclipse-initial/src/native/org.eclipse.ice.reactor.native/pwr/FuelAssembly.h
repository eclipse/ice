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

#ifndef FUELASSEMBLY_H
#define FUELASSEMBLY_H

#include "PWRAssembly.h"
#include "../LWRComposite.h"
#include "../LWRGridManager.h"
#include "RodClusterAssembly.h"
#include "../Tube.h"
#include "../GridLabelProvider.h"
#include <string>
#include <H5Cpp.h>
#include <memory>
#include <vector>

namespace ICE_Reactor {

/**
 * <p>The FuelAssembly class is a PWRAssembly populated with a collection
 * of Tubes positioned on a fixed grid. When the addTube() operation is
 * used, if a Tube with the same name exists in the collection, then the
 * Tube will not be added and a value of false will be returned. When using
 * the setTubeLocation() operation, if a Tube with the same name exists at the
 * provided location, then the current Tube name at the provided location
 * will be overwritten.</p>
 *
 * <p>StatePointData for Tubes should be stored by
 * position and accessed by the getTubeDataProviderAtLocation operation.</p>
 */
class FuelAssembly : public PWRAssembly {

private:

    /**
     * The GridLabelProvider for this FuelAssembly.
     */
    std::shared_ptr<GridLabelProvider> gridLabelProvider;

    /**
     * The RodClusterAssembly associated with this FuelAssembly.
     */
    std::shared_ptr<RodClusterAssembly> rodClusterAssembly;

    /**
     * A LWRComposite for Tubes.
     */
    std::shared_ptr<LWRComposite> tubeComposite;

    /**
     * Tubes grid manager
     */
    std::shared_ptr<LWRGridManager> tubeGridManager;

    /**
     * Private attributes for hdf5 identification
     */
    std::string TUBE_COMPOSITE_NAME;

    /**
     * Private attributes for hdf5 identification
     */
    std::string TUBE_GRID_MANAGER_NAME;

    /**
     * Private attributes for hdf5 identification
     */
    std::string GRID_LABEL_PROVIDER_NAME;

public:

    /**
     * The Copy Constructor
     */
    FuelAssembly(FuelAssembly & arg);

    /**
     * The Destructor
     */
    virtual ~FuelAssembly();

    /**
     * A parameterized Constructor.
     *
     * @param the size
     */
    FuelAssembly(int size);

    /**
     *  A parameterized Constructor.
     *
     *  @param the name
     *  @param the size
     */
    FuelAssembly(const std::string name, int size);

    /**
     * Returns the RodClusterAssembly associated with this FuelAssembly or null if one has not been set.
     *
     * @return the rod cluster assembly
     */
    std::shared_ptr<RodClusterAssembly> getRodClusterAssembly();

    /**
     * Sets the RodClusterAssembly associated with this FuelAssembly.
     *
     * @param the rod cluster assembly to set
     */
    void setRodClusterAssembly(std::shared_ptr<RodClusterAssembly> rodClusterAssembly);

    /**
     * Adds a Tube to the collection of Tubes. If a Tube with the same name exists in the collection or if the passed tube is null, then the Tube will not be added and a value of false will be returned.
     *
     * @param the tube to add
     *
     * @return true if successful, false otherwise
     */
    bool addTube(std::shared_ptr<Tube> tube);

    /**
     * Returns an ArrayList of names for each element of the collection of Tubes.
     *
     * @return  the names of the tubes
     */
    std::vector< std::string > getTubeNames();

    /**
     * Returns the Tube corresponding to the provided name or null if the name is not found.
     *
     * @param the name of the tube to return
     *
     * @return the tube at that name
     */
    std::shared_ptr<Tube> getTubeByName(const std::string name);

    /**
     * Returns the Tube corresponding to the provided column and row or null if one is not found at the provided location.
     *
     * @param the row
     * @param the column
     *
     * @return the tube at that row/col
     */
    std::shared_ptr<Tube> getTubeByLocation(int row, int column);

    /**
     * Returns the number of Tubes in the collection of Tubes.
     *
     * @return the number of tubes
     */
    int getNumberOfTubes();

    /**
     * Removes a Tube from the collection of Tubes.  Returns false if the tubeName does not exist or if the string passed is null.
     *
     * @param the name of the tube
     *
     * @return true if successful, false otherwise
     */
    bool removeTube(const std::string tubeName);

    /**
     * Removes the Tube at the provided location. Returns true if the removal was successful.
     *
     * @param the row
     * @param the column
     *
     * @return true if successful, false otherwise
     */
    bool removeTubeFromLocation(int row, int column);

    /**
     * Sets the location for the provided name.  Overrides the location of another component name as required.  Returns true if this operation was successful, false otherwise.  Note it will return true if the same name is overridden.
     *
     * @param the name of the tube
     * @param the row
     * @param the column
     *
     * @return true if successful, false otherwise
     */
    bool setTubeLocation(const std::string tubeName, int row, int column);

    /**
     * Returns the GridLabelProvider for this FuelAssembly.
     *
     * @return the gridLabelProvider
     */
    std::shared_ptr<GridLabelProvider> getGridLabelProvider();

    /**
     * Sets the GridLabelProvider for this FuelAssembly.  Can not be set to null.
     *
     * @param the grid label provider to set
     */
    void setGridLabelProvider(std::shared_ptr<GridLabelProvider> gridLabelProvider);

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const FuelAssembly& otherObject) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return the newly instantiated object
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
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > getWriteableChildren();

    /**
     * This operation reads IHdfReadable child objects. If this IHdfReadable has no IHdfReadable child objects, false is returned.
     *
     * @param the readable child
     *
     * @return true if successful, false otherwise
     */
    bool readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable);

    /**
     * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
     *
     * @param  The Group
     *
     * @return True if successful, false otherwise
     */
    bool readAttributes(std::shared_ptr<H5::Group> h5Group);

    /**
     * <!-- begin-UML-doc -->
     * <p>Returns the data provider for specific group at location or null if it does not exist.</p>
     * <!-- end-UML-doc -->
     * @param row <p>the row</p>
     * @param column <p>The column</p>
     * @return <p>the provider</p>
     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
     */
    std::shared_ptr<LWRDataProvider> getTubeDataProviderAtLocation(int row, int column);

};

}

#endif
