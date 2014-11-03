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

#include "GridLocation.h"
#include "LWRDataProvider.h"
#include<memory>

using namespace ICE_Reactor;

/**
 * The copy constructor
 *
 * @param The object to copy
 */
GridLocation::GridLocation(GridLocation & arg) {

    //Copy contents
    this->col = arg.col;
    this->row = arg.row;

    this->provider = arg.provider.get()->clone();

}

/**
 * The Destructor
 */
GridLocation::~GridLocation() {
    //TODO Auto-generated method stub
}

/**
 * The Constructor.
 *
 * @param The row
 * @param The column
 */
GridLocation::GridLocation(int row, int col) {

    //Default to 0
    this->row = this->col = 1;

    //Setup row and cols
    if(row >= 0) this->row = row;
    if(col >= 0) this->col = col;

    this->provider = std::shared_ptr<LWRDataProvider>(new LWRDataProvider());
}

/**
 * Returns the row position.
 *
 * @param Returns the row
 */
int GridLocation::getRow() {
    return this->row;
}

/**
 * Returns the column position.
 *
 * @param Retruns the column
 */
int GridLocation::getColumn() {

    return this->col;

}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param The object to compare to
 *
 * @return true if equal, false otherwise
 */
bool GridLocation::operator==(const GridLocation &other) const {

    return this->row == other.row && this->col == other.col && this->provider.get()->operator ==(*other.provider.get());
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return The newly instantiated object
 */
std::shared_ptr<GridLocation> GridLocation::clone() {

    return std::shared_ptr<GridLocation> ( new GridLocation(this->row, this->col));

}

/**
  * Sets the LWRDataProvider
  *
  * @param the provider to set
  */
 void GridLocation::setLWRDataProvider(std::shared_ptr<LWRDataProvider> provider) {

	 //begin-user-code

	 //Make sure the provider is not null
	 if(provider.get() != NULL) {
		 this->provider = provider;
	 }

	 //end-user-code
 }

 /**
  * Returns the LWRDataProvider
  *
  * @return The data provider.
  */
 std::shared_ptr<LWRDataProvider> GridLocation::getLWRDataProvider() {

	 //begin-user-code

	 return this->provider;

	 //end-user-code
 }
