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

#include "HdfFileFactory.h"
#include <fstream>
#include <iostream>
#include <H5Cpp.h>
#include <memory>

using namespace ICE_IO;

std::shared_ptr<H5::H5File> HdfFileFactory::createH5File(const std::string filePath) {
    // begin-user-code

    try {

        //Open the file and return it
        std::shared_ptr<H5::H5File> h5File(new H5::H5File(filePath, H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT));

        return h5File;
    } catch(...) {
        throw -1;
    }

    // end-user-code
}
void HdfFileFactory::closeH5File(std::shared_ptr<H5::H5File> h5File) {
    // begin-user-code

    //If the file is null, return
    if(h5File == NULL) return;

    //Close the file
    h5File.get()->close();

    //Return
    return;

    // end-user-code
}
std::shared_ptr<H5::H5File> HdfFileFactory::openH5File(const std::string filePath) {
    // begin-user-code

    try {

        //Open the file and return it
        std::shared_ptr<H5::H5File> h5File (new H5::H5File(filePath, H5F_ACC_RDWR, H5P_DEFAULT, H5P_DEFAULT));
        return h5File;

    } catch(...) {
        throw -1;
    }


    // end-user-code
}
