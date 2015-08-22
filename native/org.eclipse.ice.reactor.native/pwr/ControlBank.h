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

#ifndef CONTROLBANK_H
#define CONTROLBANK_H

#include "../LWRComponent.h"
#include <H5Cpp.h>
#include <string>
#include <vector>
#include <memory>
#include <ICEObject/Identifiable.h>
#include "../HDF5LWRTagType.h"

namespace ICE_Reactor {

/**
 * <p>The ControlBank class contains properties associated with axially
 * movable RodClusterAssembly objects known as Rod Cluster Control Assemblies.
 *  A Bank is a group of RCCA's that are positioned simultaneously by the plant
 *  operations.  A Bank may have 8 individual RCCAs, for instance,
 *  located symmetrically around the core.  For a core with 50+ RCCAs,
 *  usually only 8 or so banks are controlled independently.</p>
 */
class ControlBank : public LWRComponent {

private:

    /**
     * The distance between an axial step.
     */
    double stepSize;

    /**
     * The maximum number of axial steps.
     */
    int maxNumberOfSteps;

public:

    /**
     * The Copy Constructor
     */
    ControlBank(ControlBank & arg);

    /**
     * The Destructor
     */
    virtual ~ControlBank();

    /**
     * The nullary Constructor.
     */
    ControlBank();

    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param the stepSize
     * @param the maxNumberOfSteps
     */
    ControlBank(const std::string name, double stepSize, int maxNumberOfSteps);

    /**
     * Sets the axial step size.
     *
     * @param the stepSize
     */
    void setStepSize(double stepSize);

    /**
     * Sets the maximum number of axial steps.
     *
     * @param the maxNumberOfSteps to set
     */
    void setMaxNumberOfSteps(int maxNumberOfSteps);

    /**
     * Returns the maximum number of axial steps.
     *
     * @return the maxNumberOfSteps
     */
    int getMaxNumberOfSteps();

    /**
     * Returns the axial step size.
     *
     * @return the stepSize
     */
    double getStepSize();

    /**
     * Calculates and returns the stroke length, which is the axial step size multiplied by the maximum number of axial steps.
     *
     * @return the stroke length
     */
    double getStrokeLength();

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const ControlBank& otherObject) const;

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
     * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
     *
     * @param  The Group
     *
     * @return True if successful, false otherwise
     */
    bool readAttributes(std::shared_ptr<H5::Group> h5Group);

};

}

#endif
