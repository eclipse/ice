/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
#include "SFRData.h"
#include <memory>
#include <string>

using namespace ICE_DS;

namespace ICE_SFReactor {

// A convenience class that holds IData for the map on the SFRComponent. This is
// an intermediary class designed to hold the map of SFRData for the same types
// of features. The getFeature() operation on SFRData should return the same
// value as the getName() operation on this class.
class FeatureSet {

    private:

        // Vector of IData associated to the FeatureSet.
        std::vector< std::shared_ptr<IData> > iData;

        // Name of the feature. 
        std::string name;

    public:

        // Copy constructor. Deep copies the contents of the object from another
        // object.
        FeatureSet(const FeatureSet & arg);

        // Parameterized constructor specifying the feature type. The passed
        // value must be a valid feature set, otherwise it will set the feature
        // name to null and not allow the addition of any IData.
        FeatureSet(std::string feature);

        // Returns the name of the feature as a string. This may be null if the
        // name provided during construction was invalid.
        std::string getName();

        // Returns the IDatas associated with the FeatureSet.
        std::vector< std::shared_ptr<IData> > getData();

        // Adds IData to the list within the feature set. The name of the
        // feature must match the name set on the FeatureSet, otherwise this
        // operation will fail. Returns true if operation was successful, false
        // otherwise.
        bool addIData(std::shared_ptr<IData> iData);

        // Deep copies and returns a newly instantiated object. 
        std::shared_ptr<FeatureSet> clone();

        // Compares the contents of objects and returns true if they are
        // identical, otherwise returns false.
        bool operator==(const FeatureSet & otherObject) const;

};  //end class FeatureSet

} //end namespace

#endif
