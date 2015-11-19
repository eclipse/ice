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

#ifndef HDFFILEFACTORY_H
#define HDFFILEFACTORY_H

#include <H5Cpp.h>
#include <memory>

namespace ICE_IO {

/**
 * The HDFFileFactory class contains static methods used to create, open, and close HDF5 files.
 */
class HdfFileFactory {

public:

    /**
     * Creates, opens, and returns an H5File from the provided uri. If the uri is null, the null is returned. If the FID of resulting h5File is -1, then null is returned. If any Exception is thrown, then null is returned.
     *
     * @param the file path
     *
     * @return H5 file
     */
    static std::shared_ptr<H5::H5File> createH5File(const std::string filePath);

    /**
     *  Closes an h5File.
     *
     *  @param the file to close
     */
    static void closeH5File(std::shared_ptr<H5::H5File> h5File);

    /**
     * Opens and returns an h5File from the provided URI. If the uri is null, the null is returned. If the File for uri does not exist, then null is returned. If the resulting h5File is null or does not exist, then null is returned. If any Exception is thrown, then null is returned.
     *
     * @param the file path
     *
     * @return the H5File that is opened
     */
    static std::shared_ptr<H5::H5File> openH5File(const std::string filePath);

};
}
#endif
