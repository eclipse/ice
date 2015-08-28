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

#ifndef MATERIALBLOCK_H
#define MATERIALBLOCK_H

#include "LWRComponent.h"
#include "Ring.h"
#include <H5Cpp.h>
#include <memory>
#include <IHdfReadable.h>
#include <IHdfWriteable.h>

namespace ICE_Reactor {

/**
 * <p>The MaterialBlock class is a generalized class containing a set of
 * concentric and/or radial collection of Rings that constitute the inner
 * core of an LWRRod.</p>
 */
class MaterialBlock : public LWRComponent {

private:

    /**
     * A list of rings
     */
    std::vector<std::shared_ptr< Ring > > rings;

    /**
     * <p>The position from the bottom of the rod.</p>
     */
    double pos;

public:

    /**
     * The Copy Constructor
     */
    MaterialBlock(MaterialBlock & arg);

    /**
     * The Destructor
     */
    virtual ~MaterialBlock();

    /**
     * The nullary Constructor.
     */
    MaterialBlock();
    /**
     * <p>Adds a Ring to this MaterialBlock's ring collection. If the ring
     * could not be successfully added, then false is returned. This could
     * be due to a ring existing at within the inner and outer radius of
     * an existing Ring object in the MaterialBlock.</p>
   	 *
   	 * @param ring <p>The ring to add to this MaterialBlock's Ring collection.</p>
   	 * @return <p>True, if the Ring was successfully added.</p>
   	 */
    bool addRing(std::shared_ptr<Ring> ring);

	/**
	 * <p>Returns the Ring located at the provided radius value or null if one could not be found.</p>
	 *
	 * @param radius <p>A radius value.</p>
	 * @return <p>The Ring located at the provided radius value or null if one could not be found.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    std::shared_ptr<Ring> getRing(double radius);

	/**
	 * <p>Returns the Ring with the provided name or null if one could not be found.</p>
	 *
	 * @param ringName <p>A Ring name.</p>
	 * @return <p>The Ring with the provided name or null if one could not be found.</p>
	 */
    std::shared_ptr<Ring> getRing(std::string ringName);

	/**
	 * <p>Returns an ArrayList of Rings ordered by ascending radii.</p>
	 *
	 * @return <p>An ArrayList of Rings ordered by ascending radii.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    std::vector< std::shared_ptr< Ring > > getRings();

	/**
	 * <p>Removes the Ring from this MaterialBlock's ring collection that has
	 * the provided name. Returns true, if the Ring was successfully
	 * removed.</p>
	 *
	 * @param ringName <p>The name of the Ring to remove.</p>
	 * @return <p>True, if the Ring was successfully removed from
	 * this MaterialBlock's Ring collection.</p>
	 */
    bool removeRing(std::string ringName);

	/**
	 * <p>Overrides the equals operation to check the attributes on this
	 * object with another object of the same type.  Returns
	 * true if the objects are equal.  False otherwise.</p>
	 *
	 * @param otherObject <p>The object to be compared.</p>
	 * @return <p>True if otherObject is equal.  False otherwise.</p>
	 */
    bool operator==(const MaterialBlock& otherObject) const;

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
	 * <p>writes the hdf5 attributes.</p>
	 *
	 * @param h5File
	 * @param h5Group
	 * @return
	 */
	bool writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

	/**
	 * <p>Reads the hdf5 attributes.</p>
	 *
	 * @param h5Group
	 * @return
	 */
	bool readAttributes(std::shared_ptr<H5::Group> h5Group);

	/**
	 * <p>Sets the position.</p>
	 *
	 * @param pos <p>the position to set</p>
	 */
	void setPosition(double pos);

	/**
	 * <p>Gets the position</p>
	 *
	 * @return <p>The position set.</p>
	 */
	double getPosition();

};

}

#endif
