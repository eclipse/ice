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

#ifndef PWRASSEMBLY_H
#define PWRASSEMBLY_H

#include "../LWRComposite.h"
#include "../LWRGridManager.h"
#include "../LWRRod.h"
#include "../LWRComponent.h"
#include <memory>
#include <updateableComposite/Component.h>
#include <H5Cpp.h>
#include <string>

namespace ICE_Reactor {

/**
 * <p>The PWRAssembly class contains a collection of LWRRods mapped to
 *  locations on a grid. When the addLWRRod() operation is used,
 *  if a LWRRod with the same name exists in the collection, then the
 *  LWRRod will not be added and a value of false will be returned.
 *  When using the setLWRRodLocation() operation, if a LWRRod with the same
 *  name exists at the provided location, then the current LWRRod name at the
 *   provided location will be overwritten.</p>
 *
 *   <p>StatePointData for
 *   LWRRods should be stored by position and accessed by the
 *   getLWRRodDataProviderAtLocation operation.</p>
 */
class PWRAssembly : public LWRComposite {

protected:

    /**
     * The distance between centers of adjacent fuel rods in the fuel grid.  Must be greater than zero.
     */
    double rodPitch;

    /**
     * The size of either dimension of this PWRAssembly.
     */
    int size;

    /**
     * A LWRComposite for LWRRods.
     */
    std::shared_ptr<LWRComposite> lWRRodComposite;


    /**
     * Grid Manager
     */
    std::shared_ptr<LWRGridManager> lWRRodGridManager;

    /**
     * Name of HDF5 Pieces
     */
    std::string LWRROD_COMPOSITE_NAME;

    /**
     * Name of HDF5 Pieces
     */
    std::string LWRROD_GRID_MANAGER_NAME;

public:

    /**
     * The Copy Constructor
     */
    PWRAssembly(PWRAssembly & arg);

    /**
     * The Destructor
     */
    virtual ~PWRAssembly();

    /**
     * A parameterized Constructor.
     *
     * @param the size
     */
    PWRAssembly(int size);

    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param the size
     */
    PWRAssembly(const std::string name, int size);

    /**
     * Returns the size of either dimension of this PWRAssembly.
     *
     * @return the size
     */
    int getSize();

    /**
     * Adds a LWRRod to the collection of LWRRods. If a LWRRod with the same name exists in the collection or the passed parameter is null, then the LWRRod will not be added and a value of false will be returned.
     *
     * @param the rod
     *
     * @return true if successful, false otherwise
     */
    bool addLWRRod(std::shared_ptr<LWRRod> lWRRod);

    /**
     * Removes a LWRRod from the collection of LWRRods.  The passed string can not be null.
     *
     * @param the rod name
     *
     * @return true if successful, false otherwise
     */
    bool removeLWRRod(const std::string lWRRodName);

    /**
     * Returns a list of names for each element of the collection of LWRRods.
     *
     * @return list of rod names
     */
    std::vector<std::string> getLWRRodNames();

    /**
     * Returns the LWRRod corresponding to the provided name or null if the name is not found.
     *
     * @param the name
     *
     * @return the rod at the name
     */
    std::shared_ptr<LWRRod> getLWRRodByName(const std::string name);

    /**
     * Returns the LWRRod corresponding to the provided column and row or null if one is not found at the provided location.
     *
     * @param the row
     * @param the column
     *
     * @return the rod at the location
     */
    std::shared_ptr<LWRRod> getLWRRodByLocation(int row, int column);

    /**
     * Returns the number of LWRRods in the collection of LWRRods.
     *
     * @return the number of rods
     */
    int getNumberOfLWRRods();

    /**
     * Sets the location for the provided name.  Overrides the location of another component name as required.  Returns true if this operation was successful, false otherwise.  Note it will return true if the same name is overridden.
     *
     * @param the rod name
     * @param the row
     * @param the column
     *
     * @return true if successful, false otherwise
     */
    bool setLWRRodLocation(const std::string lWRRodName, int row, int column);

    /**
     * Removes the LWRRod at the provided location. Returns true if the removal was successful.
     *
     * @param the row
     * @param the column
     *
     * @return true if successful, false otherwise
     */
    bool removeLWRRodFromLocation(int row, int column);

    /**
     * Returns the distance between centers of adjacent fuel rods.
     *
     * @return the rodPitch
     */
    double getRodPitch();

    /**
     * Sets the distance between centers of adjacent fuel rods in the fuel lattice. The rodPitch value must be greater than zero.
     *
     * @param the rodPitch to set
     */
    void setRodPitch(double rodPitch);

    /**
     * This operation overrides the LWRComposite's operation.  This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
     *
     * @param Component to set
     */
    void addComponent(std::shared_ptr<ICE_DS::Component> component);

    /**
     * An operation that overrides the LWRComposite's operation. This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
     *
     * @param the child id
     */
    void removeComponent(int childId);

    /**
     * An operation that overrides the LWRComposite's operation. This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
     *
     * @param the name
     */
    void removeComponent(const std::string name);

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const PWRAssembly& otherObject) const;

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
    bool writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

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
    std::shared_ptr<LWRDataProvider> getLWRRodDataProviderAtLocation(int row, int column);


    //THIS IS ERRONEOUS!  WILL NEED TO REMOVE WHEN FIXED! FIXME SFH
    std::shared_ptr<ICE_DS::Component> getComponent(int childId) { return LWRComposite::getComponent(childId);};
     std::shared_ptr<ICE_DS::Component> getComponent(const std::string name){ return LWRComposite::getComponent(name);};


};

}

#endif
