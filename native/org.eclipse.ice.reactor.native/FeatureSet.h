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

#ifndef FEATURESET_H
#define FEATURESET_H

#include "IData.h"
#include "LWRData.h"
#include <vector>
#include <memory>
#include <string>

namespace ICE_Reactor {

/**
 * <p>A convience class that holds a IData for the java collection on the
 * LWRComponent.  This is an intermediary class designed to hold the list of
 * LWRData for the same types of features.  The getFeature() operation on LWRData
 * should return the same value as the getName() operation on this class.</p>
 */
class FeatureSet {

private:

    /**
     * List of IData associated with this feature set.
     */
    std::vector< std::shared_ptr<IData> > iData;

    /**
     * The name of the feature.
     */
    std::string name;

public:

    /**
     * Copy constructor
     *
     * @param Object to copy from
     */

    FeatureSet(FeatureSet & arg);

    /**
     * Destructor
     */

    virtual ~FeatureSet();

    /**
     * The constructor. The passed value must be a valid feature set, otherwise it will set the feature name to null and not allow the addition of any IData.
     */
    FeatureSet(const std::string feature);

    /**
     * Returns the name of the feature.
     *
     * @return name of the feature
     */
    const std::string getName();

    /**
     * Returns the IDatas associated with the FeatureSet.
     *
     * @return list of IData.
     */
    std::vector< std::shared_ptr<IData> > getIData();

    /**
     * Adds IData to the list within the feature set.  The name of the feature must match the name set on the FeatureSet, otherwise this operation will fail.  Returns true if operation was successful, false otherwise.
     *
     * @param the IData to add
     */
    bool addIData(std::shared_ptr <IData> iData);

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param the object to check obj with
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const FeatureSet &other) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return a newly instantiated object.
     */
    std::shared_ptr<FeatureSet> clone();

    /**
     * Removes data from the feature based upon index
     *
     * @param the index to remove
     *
     * @return true if removed, false otherwise
     */
    bool removeIDataAtIndex(int index);

};

}

#endif
