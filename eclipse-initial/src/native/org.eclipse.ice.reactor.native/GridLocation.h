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

#ifndef GRIDLOCATION_H
#define GRIDLOCATION_H

#include<memory>
#include "LWRDataProvider.h"

namespace ICE_Reactor {

/**
 * <p>The GridLocation class represents a location on a Cartesian grid.  This
 * class also stores an implementation of the IDataProvider, called LWRDataProvider.
 * This class was designed with two specific roles in mind.</p>
 *
 * <p>1.) To provide a
 * wrapper for Rows and Columns (i, j or x, y) on a cartesean grid.</p>
 *
 * <p>2.) To
 * provide state point data at particular positions within a grid.</p>
 *
 * <p>As 1.) is
 * easy to understand, 2.) is a bit complex.  To optimize memory efficiency, the
 * LWRDataProvider should be used to represent changes in statepointdata at a particular
 * position for a specific type of delegated sub component.  </p>
 *
 * <p>For example, in most
 * reactors there are only about 3-5 different types of "Rods" used within a reaction.
 * On an assembly, these rods are used multiple times for different positions.  These rods
 * also contain materials that decompose over a reaction's lifetime. For a 17x17 assembly,
 * it is a waste of memory to store 17x17 amount of rods for each location along with
 * unique state point data (LWRDataProvider) for each rod (especially if most of them
 * are the same type of rod).  It is preferred to store 5 rods and to set their positions
 * based on name within that assembly and to change the statepoint data on the assembly
 * through this class over the life cycle of the reaction.  This improves memory
 * efficiency and separates the model from the "experimental data".</p>
 *
 * <p>Keep in mind,
 * you can do both.  However, the second method improves IO times in magnitudes and is
 * more optimal.</p>
 */
class GridLocation {

private:

    /**
     * The row position.
     */
    int row;

    /**
     * The column position.
     */
    int col;

    /**
     * The provider
     */
    std::shared_ptr<LWRDataProvider> provider;

public:

    /**
     * The copy constructor
     *
     * @param The object to copy
     */
    GridLocation(GridLocation & arg);

    /**
     * The Destructor
     */
    virtual ~GridLocation();

    /**
     * The Constructor.
     *
     * @param The row
     * @param The column
     */
    GridLocation(int row, int col);

    /**
     * Returns the row position.
     *
     * @return Returns the row
     */
    int getRow();

    /**
     * Returns the column position.
     *
     * @return Returns the column
     */
    int getColumn();

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param The object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const GridLocation &other) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return The newly instantiated object
     */
    std::shared_ptr<GridLocation> clone();

    /**
     * Sets the LWRDataProvider
     *
     * @param the provider to set
     */
    void setLWRDataProvider(std::shared_ptr<LWRDataProvider> provider);

    /**
     * Returns the LWRDataProvider
     *
     * @return The data provider.
     */
    std::shared_ptr<LWRDataProvider> getLWRDataProvider();

};

}

#endif
