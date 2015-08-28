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
#define BOOST_TEST_DYN_LINK
#define BOOST_TEST_MODULE Regression
#include <boost/test/included/unit_test.hpp>
#include <HDF5LWRTagType.h>
#include <UtilityOperations.h>

using namespace ICE_Reactor;

BOOST_AUTO_TEST_SUITE(HDF5LWRTagTypeTester_testSuite)

BOOST_AUTO_TEST_CASE(checkTyping) {


    // begin-user-code

    //Local Declarations
    MaterialType type;
    bool exceptionHit0 = false;

    //Check the toString implementations of the hdf5Type enum
    BOOST_REQUIRE_EQUAL("BWReactor", UtilityOperations::toStringHDF5Tag(BWREACTOR));
    BOOST_REQUIRE_EQUAL("Control Bank", UtilityOperations::toStringHDF5Tag(CONTROL_BANK));
    BOOST_REQUIRE_EQUAL("Fuel Assembly", UtilityOperations::toStringHDF5Tag(FUEL_ASSEMBLY));
    BOOST_REQUIRE_EQUAL("Incore Instrument", UtilityOperations::toStringHDF5Tag(INCORE_INSTRUMENT));
    BOOST_REQUIRE_EQUAL("LWRComponent", UtilityOperations::toStringHDF5Tag(LWRCOMPONENT));
    BOOST_REQUIRE_EQUAL("LWRComposite", UtilityOperations::toStringHDF5Tag(LWRCOMPOSITE));
    BOOST_REQUIRE_EQUAL("LWRRod", UtilityOperations::toStringHDF5Tag(LWRROD));
    BOOST_REQUIRE_EQUAL("Material", UtilityOperations::toStringHDF5Tag(MATERIAL));
    BOOST_REQUIRE_EQUAL("PWRAssembly", UtilityOperations::toStringHDF5Tag(PWRASSEMBLY));
    BOOST_REQUIRE_EQUAL("PWReactor", UtilityOperations::toStringHDF5Tag(PWREACTOR));
    BOOST_REQUIRE_EQUAL("Ring", UtilityOperations::toStringHDF5Tag(RING));
    BOOST_REQUIRE_EQUAL("Rod Cluster Assembly", UtilityOperations::toStringHDF5Tag(ROD_CLUSTER_ASSEMBLY));
    BOOST_REQUIRE_EQUAL("MaterialBlock", UtilityOperations::toStringHDF5Tag(MATERIALBLOCK));
    BOOST_REQUIRE_EQUAL("Tube", UtilityOperations::toStringHDF5Tag(TUBE));
    BOOST_REQUIRE_EQUAL("Grid Label Provider", UtilityOperations::toStringHDF5Tag(GRID_LABEL_PROVIDER));
    BOOST_REQUIRE_EQUAL("LWRGridManager", UtilityOperations::toStringHDF5Tag(LWRGRIDMANAGER));

    //Check the toType implementations of the HDf5 enum

    //Check the type
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("BWReactor"), BWREACTOR);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("Control Bank"), CONTROL_BANK);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("Fuel Assembly"), FUEL_ASSEMBLY);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("Incore Instrument"), INCORE_INSTRUMENT);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("LWRComponent"), LWRCOMPONENT);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("LWRComposite"), LWRCOMPOSITE);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("LWRRod"), LWRROD);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("Material"), MATERIAL);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("PWRAssembly"), PWRASSEMBLY);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("PWReactor"), PWREACTOR);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("Ring"), RING);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("Rod Cluster Assembly"), ROD_CLUSTER_ASSEMBLY);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("MaterialBlock"), MATERIALBLOCK);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("Tube"), TUBE);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("Grid Label Provider"), GRID_LABEL_PROVIDER);
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringHDF5Tag("LWRGridManager"), LWRGRIDMANAGER);


    //Try to return a type that does not exist
    try {
        UtilityOperations::fromStringHDF5Tag("asdasdasdasdasd");
    } catch (...) {
        exceptionHit0 = true;
    }

    BOOST_REQUIRE_EQUAL(exceptionHit0, true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_SUITE_END()
