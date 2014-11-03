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

#ifndef IDATA_H
#define IDATA_H

#include <string>
#include <vector>
#include <memory>

namespace ICE_DS {

/**
 *  An interface that provides the position and value of a data entry as well as a descriptive tag about the featureof the entry that the data represents.
 */
class IData {

public:

    /**
     * The position of the data relative to the position of the containing object.
     *
     * @return vector of positions
     */
    virtual std::vector<double> getPosition() const = 0;

    /**
     * The value of the particular feature (pin-power, temperature, etc).
     *
     * @return the value
     */
    virtual double getValue() const = 0;

    /**
     *  The amount of uncertainty in the value.
     *
     *  @return the uncertainty
     */
    virtual double getUncertainty() const = 0;

    /**
     *  A string describing the units of the value and its uncertainty.
     *
     *  @return the units
     */
    virtual std::string getUnits() const = 0;

    /**
     * The name of the feature that this data represents (pin-power, temperature, etc).
     *
     * @return the feature
     */
    virtual std::string getFeature() const = 0;

    /**
     * The equality operation.  Returns true if objects are equal, false otherwise.
     *
     * @param The object to compare to
     *
     * @return true if equal, false otherwise
     */
    virtual bool operator==(const IData &other) const = 0;

    /**
     * Returns a newly instantiated clone of the object.
     *
     * @param newly instantiated object.
     */
    virtual std::shared_ptr<IData> clone() = 0;

};

}

#endif
