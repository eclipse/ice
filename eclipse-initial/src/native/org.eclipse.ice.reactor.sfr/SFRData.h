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
#ifndef SFRDATA_H
#define SFRDATA_H
#include <IData.h>
#include <string>
#include <memory>
#include <vector>

using namespace ICE_DS;

namespace ICE_SFReactor {

// A class that implements the IData interface; provides setters for the
// particular sets of IData associated with this class.
class SFRData: virtual public ICE_DS::IData {

private:

	// Value of the SFRData.
	double value;

	// Uncertainty of the SFRData value.
	double uncertainty;

	// Units of the SFRData value.
	std::string units;

	// Feature the SFRData value describes.
	std::string feature;

	// Representation of the SFRData's physical position in the reactor as a
	// vector of (x, y, z) coordinates.
	std::vector<double> position;

public:

	// Nullary constructor.
	SFRData();

	// Parameterized constructor with feature specified.
	SFRData(std::string feature);

	// Copy constructor. Deep copies the contents of the object.
	SFRData(const SFRData & otherSFRData);

	// Sets the value of the data.
	void setValue(double value);

	// Sets the uncertainty of the data.
	void setUncertainty(double uncertainty);

	// Sets the units of the data.
	void setUnits(std::string units);

	// Sets the feature type of the data.
	void setFeature(std::string feature);

	// Sets the position of the SFRData object. Represented in (x, y, z)
	// coordinates.
	void setPosition(std::vector<double> position);

	// Deep copies and returns a newly instantiated object.
	std::shared_ptr<IData> clone();

	// Overrides the equals operation to check the attributes on this object
	// with another object of the same type. Returns true if the objects are
	// equal. False otherwise.
	bool operator==(const IData & otherData) const;

	// Returns the position of the SFRData.
	std::vector<double> getPosition() const;

	// Returns the value of the SFRData.
	double getValue() const;

	// Returns the uncertainty of the SFRData.
	double getUncertainty() const;

	// Returns the units of SFRData.
	std::string getUnits() const;

	// Returns the feature of the SFRData.
	std::string getFeature() const;

};
//end class SFRData

}// end namespace
#endif
