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

#ifndef GRIDLABELPROVIDER_H
#define GRIDLABELPROVIDER_H

#include "LWRComponent.h"
#include <H5Cpp.h>
#include <string>
#include <vector>
#include <memory>
#include <ICEObject/Identifiable.h>
#include <IHdfWriteable.h>
#include <IHdfReadable.h>


namespace ICE_Reactor {

/**
 * <p>This is a utility class that provides labels on a 2D grid for rows and columns.
 * This class should be considered as a piece designed specifically for interactions
 * with the GUI and should not be considered as a means to override the ability to set
 * rows and column indicie types.</p>
 *
 * <p>The constructor takes a size that is N squared,
 * and defaults to a positive number if the size is non-positive or zero. </p>
 */
class GridLabelProvider : public LWRComponent {

private:


    /**
     * An ArrayList of std::strings of length size containing the label for each column position from left to right.
     */
    std::vector<std::string> columnLabels;



    /**
     *  An ArrayList of std::strings of length size containing the label for each row position from top to bottom.
     */
    std::vector<std::string> rowLabels;



    /**
     * The size for the row and column label ArrayLists.
     */
    int size;

    /**
     * Attributes for hdf5
     */
    std::string ROW_LABELS_NAME;
    std::string COLUMN_LABELS_NAME;
    std::string LABELS_GROUP_NAME;


public:

    /**
     * The copy constructor.
     */
    GridLabelProvider(GridLabelProvider & arg);

    /**
     * The Destructor.
     */
    virtual ~GridLabelProvider();

    /**
     * A parameterized constructor.
     *
     * @param The size
     */
    GridLabelProvider(int size);

    /**
     * Returns the column position from a label.  Returns -1 if the label is not found or if the label is null.
     *
     * @param the column label
     *
     * @return The index of column label
     */
    int getColumnFromLabel(const std::string columnLabel);

    /**
     * Returns the row position from a label. Returns -1 if the label is not found or if the label is null.
     *
     * @param the row label
     *
     * @return The index of the row label
     */
    int getRowFromLabel(const std::string rowLabel);

    /**
     * Returns the label at position column.
     *
     * @param The index of column
     *
     * @return The label of the column at index
     */
    const std::string getLabelFromColumn(int column);

    /**
     * Returns the label at position row.
     *
     * @param The index of row
     *
     * @return The label of the row at index
     */
    const std::string getLabelFromRow(int row);

    /**
     * Sets the array of row labels ordered from top to bottom.
     *
     * @param The list of rowLabels
     */
    void setRowLabels(const std::vector<std::string> rowLabels);

    /**
     * Sets the array of column labels ordered from left to right.
     *
     * @param The list of columnLabels
     */
    void setColumnLabels(const std::vector<std::string> columnLabels);

    /**
     * Returns the size for the row and column label ArrayLists.
     *
     * @return the size
     */
    int getSize();

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param The object to compare
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const GridLabelProvider &otherObject) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return The newly instantiated object
     */
    std::shared_ptr<ICE_DS::Identifiable> clone();

    /**
     * Overrides writeAttributes operation on LWRComponent
     *
     * @param File
     * @param Group
     *
     * @return true if operation successful, false otherwise
     */
    bool writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

    /**
     * Overrides writeDatasets operation on LWRComponent
     *
     * @param File
     * @param Group
     *
     * @return true if operation successful, false otherwise
     */
    bool writeDatasets(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group);

    /**
     * Overrides readDatasets operation on LWRComponent
     *
     * @param File
     * @param Group
     *
     * @return true if operation successful, false otherwise
     */
    bool readDatasets(std::shared_ptr<H5::Group> h5Group);

    /**
     * Overrides readAttributes operation on LWRComponent
     *
     * @param File
     * @param Group
     *
     * @return true if operation successful, false otherwise
     */
    bool readAttributes(std::shared_ptr<H5::Group> h5Group);

};

}

#endif
