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

#ifndef LWRDATA_H
#define LWRDATA_H

#include <IData.h>
#include <vector>
#include <memory>
#include <string>

using namespace ICE_DS;

namespace ICE_Reactor {

/**
 * <p>A class that implements the IData interface.  It provides setters for the
 * particular sets of IData associated with this class along with some basic
 * equality and copying routines for convenience.</p>
 */
class LWRData : public IData {

private:

    /**
     * The representation of the x, y, z coordinate.
     */
    std::vector<double> position;

    /**
     * The value.
     */
    double value;

    /**
     * The uncertainty value.
     */
    double uncertainty;

    /**
     * The representation of the type of "Unit" represented by the value (Meters, velocity, etc).
     */
    std::string units;

    /**
     * The feature (unique name) of this object.
     */
    std::string feature;

public:

    /**
     * The Copy Constructor
     */
    LWRData(const LWRData & arg);

    /**
     * The Destructor
     */
    virtual ~LWRData();

    /**
     * The constructor.  Sets up the default values for the LWRData.
     */
    LWRData();

    /**
     * A parameterized Constructor.
     */
    LWRData(const std::string feature);

    /**
     * Sets the position of the LWRData. The passed parameter can not be null and must be equal to three dimensions (x, y, z coordinate plane and in that order for less than 3 dimensions). If working in less than 3 dimensions, the offset values should be set to 0.
     *
     * @param the positions
     */
    void setPosition(std::vector<double> position);

    /**
     * Sets the value.
     *
     * @param Sets the value
     */
    void setValue(double value);

    /**
     * Sets the uncertainty.
     *
     * @param the uncertainty
     */
    void setUncertainty(double uncertainty);

    /**
     * Sets the units. Can not be null or the empty string. std::strings are trimmed accordingly upon being set.
     *
     * @param the units to set
     */
    void setUnits(const std::string units);

    /**
     * Sets the feature.  Can not set to null or the empty string.  std::strings are trimmed accordingly upon being set.
     *
     * @param the feature to set
     */
    void setFeature(const std::string feature);

    /**
     * Equals operation.  Overrides IData's equality
     */
    virtual bool operator==(const IData &other) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return newly instantiated object
     */
    virtual std::shared_ptr<IData> clone();

    /**
     * Overrides the IData implementation
     */
    virtual std::vector<double>  getPosition() const;

    /**
      * Overrides the IData implementation
    */
    virtual double getValue() const;

    /**
     * Overrides the IData implementation
    */
    virtual double getUncertainty() const;

    /**
     * Overrides the IData implementation
     */
    virtual std::string getUnits() const;

    /**
     * Overrides the IData implementation
     */
    virtual std::string getFeature() const;



};  //end class LWRData

}

#endif
