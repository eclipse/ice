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

#ifndef IDATAPROVIDER_H
#define IDATAPROVIDER_H

#include "IData.h"
#include <vector>
#include <string>

namespace ICE_DS {

/**
 * An interface for determining what features are available  for a particular object(pin-power, temperature, etc).  IDataProviders are anything that have information to share regardless of their positions in any particular hierarchy.
 */
class IDataProvider {

public:

    /**
     * This operation returns the list of features available across all time steps (pin-power, temperature, etc).
     *
     * @return The feature list names
     */
    virtual std::vector<std::string> getFeatureList() = 0;

    /**
     * This operation returns the total number of time steps.
     *
     * @return The number of time steps
     */
    virtual int getNumberOfTimeSteps() = 0;

    /**
     * This operation sets the current time step for which data should be retrieved.  It is 0-indexed such that time step 0 is the initial state and time step 1 is the state after the first time step.  This operation should be called to set the current time step before data is retrieved from the provider.  The provider will always default to the initial state.
     *
     * @param the time to set
     */
    virtual void setTime(double step) = 0;

    /**
     * This operation returns all of the data (as IData[*]) related to a particular feature for this provider at a specific time step.  This operation will return null if no data is available and such a situation will most likely signify an error.
     *
     * @param The feature
     *
     * @return the list of IData at that feature, or null if feature does not exist
     */
    virtual std::vector<std::shared_ptr<IData> > getDataAtCurrentTime(const std::string feature) = 0;

    /**
     * This operation is a description of the source of information for this provider and its data.
     *
     * @return The source info
     */
    virtual std::string getSourceInfo() = 0;

    /**
     * Returns the list of features at the current time.
     *
     * @return The list of features at current time step
     */
    virtual std::vector<std::string> getFeaturesAtCurrentTime() = 0;

    /**
     * Returns all the times in ascending order.
     *
     * @param The list of time steps
     */
    virtual std::vector<double> getTimes() = 0;

    /**
     * Returns the integer time based upon the time step.  Returns -1 if the time does not exist.
     *
     * @param the time
     *
     * @return the time step or -1 if it does not exist
     */
    virtual int getTimeStep(double time) = 0;

    /**
     * Returns the time units.
     *
     * @return the timeunits
     */
    virtual std::string getTimeUnits() = 0;

};

}

#endif
