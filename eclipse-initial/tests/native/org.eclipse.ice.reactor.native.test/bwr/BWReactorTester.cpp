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
#define BOOST_TEST_MODULE BWReactorTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <bwr/BWReactor.h>
#include <HDF5LWRTagType.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <HdfFileFactory.h>
#include <UtilityOperations.h>
#include <H5Cpp.h>
#include <memory>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(BWReactorTester_testSuite)



BOOST_AUTO_TEST_CASE(checkConstruction) {

    // begin-user-code

    //Local Declarations
    std::string defaultName = "BWReactor 1";
    std::string defaultDescription = "BWReactor 1's Description";
    int defaultId = 1;
    int defaultSize = 1;
    HDF5LWRTagType type = BWREACTOR;

    // This test is to show the default value for a reactor when it is
    // created with a negative value.
    BWReactor reactor(-1);
    BOOST_REQUIRE_EQUAL(defaultSize, reactor.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, reactor.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, reactor.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, reactor.getId());
    BOOST_REQUIRE_EQUAL(type, reactor.getHDF5LWRTag());

    // This test is to show the default value for a reactor when its created
    // with a zero value
    BWReactor reactor2(0);
    BOOST_REQUIRE_EQUAL(defaultSize, reactor2.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, reactor2.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, reactor2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, reactor2.getId());
    BOOST_REQUIRE_EQUAL(type, reactor2.getHDF5LWRTag());

    // This is a test to show a valid creation of a reactor
    BWReactor reactor3(17);
    BOOST_REQUIRE_EQUAL(17, reactor3.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, reactor3.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, reactor3.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, reactor3.getId());
    BOOST_REQUIRE_EQUAL(type, reactor3.getHDF5LWRTag());

    return;

    // end-user-code
}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations

    int size = 5;
    int unEqualSize = 7;

    //Setup root object
    BWReactor object(size);

    //Setup equalObject equal to object
    BWReactor equalObject(size);

    //Setup transitiveObject equal to object
    BWReactor transitiveObject(size);

    // Set its data, not equal to object
    BWReactor unEqualObject(unEqualSize);

    // Assert that these two objects are equal
    BOOST_REQUIRE_EQUAL(object==(equalObject), true);

    // Assert that two unequal objects returns false
    BOOST_REQUIRE_EQUAL(object==(unEqualObject), false);

    // Check that equals() is Reflexive
    // x.equals(x) = true
    BOOST_REQUIRE_EQUAL(object==(object), true);

    // Check that equals() is Symmetric
    // x.equals(y) = true iff y.equals(x) = true
    BOOST_REQUIRE_EQUAL(object==(equalObject) && equalObject==(object), true);

    // Check that equals() is Transitive
    // x.equals(y) = true, y.equals(z) = true => x.equals(z) = true
    if (object==(equalObject) && equalObject==(transitiveObject)) {
        BOOST_REQUIRE_EQUAL(object==(transitiveObject), true);
    } else {
        BOOST_FAIL("FAILURE IN EQUALITY CHECKING!  EXITING!");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(object==(equalObject) && object==(equalObject)
                        && object==(equalObject), true);
    BOOST_REQUIRE_EQUAL(!(object==(unEqualObject))
                        && !(object==(unEqualObject))
                        && !(object==(unEqualObject)), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local declarations
    int size = 5;

    //Setup root object
    BWReactor object(size);


    //Run the copy routine
    BWReactor copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<BWReactor> castedObject (new BWReactor(*(dynamic_cast<BWReactor *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_SUITE_END()
