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
#define BOOST_TEST_MODULE GridLocationTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <GridLocation.h>
#include <HdfFileFactory.h>
#include <UtilityOperations.h>
#include <HdfReaderFactory.h>
#include <HdfWriterFactory.h>

using namespace ICE_Reactor;

BOOST_AUTO_TEST_SUITE(GridLocationTester_testSuite)



BOOST_AUTO_TEST_CASE(checkConstruction) {

    // begin-user-code

    //Local Declarations
    int defaultRow = 1;
    int defaultCol = 1;

    //new values
    int newRow = 5;
    int newCol = 6;

    //Create a normal gridLocation with normal values
    GridLocation location(newRow, newCol);
    //Check values
    BOOST_REQUIRE_EQUAL(newRow, location.getRow());
    BOOST_REQUIRE_EQUAL(newCol, location.getColumn());

    //Create a normal gridLocation with normal values - 0 column and 0 row
    GridLocation location1(0, 0);
    //Check values - defaults
    BOOST_REQUIRE_EQUAL(0, location1.getRow());
    BOOST_REQUIRE_EQUAL(0, location1.getColumn());

    //Create a normal gridLocation with normal values of default
    GridLocation location2(defaultRow, defaultCol);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultRow, location2.getRow());
    BOOST_REQUIRE_EQUAL(defaultCol, location2.getColumn());

    //Create a normal gridLocation with bad values - negative row and column
    GridLocation location3(-1, -1);
    //Check values - defaults
    BOOST_REQUIRE_EQUAL(defaultRow, location3.getRow());
    BOOST_REQUIRE_EQUAL(defaultCol, location3.getColumn());

    // end-user-code
    return;
}
BOOST_AUTO_TEST_CASE(checkComparable) {
    /*    //TODO Auto-generated method stub
    		// begin-user-code

    		//Local declaration
    		GridLocation location1;
    		GridLocation location2;
    		int row1 = 2;
    		int col1 = 9;
    		int row2 = 6;
    		int col2 = 7;

    		//Instantiate a GridLocation with same row and col
    		location1 = new GridLocation(row1, col1);
    		location2 = new GridLocation(row1, col1);

    		//Check compareTo - should be 0
    		assertEquals(0, location1.compareTo(location2));

    		//Instantiate a GridLocation with different row and col
    		location1 = new GridLocation(row1, col1);
    		location2 = new GridLocation(row2, col2);

    		//Check compareTo - should be equal to row1-row2
    		assertEquals(row1 - row2, location1.compareTo(location2));

    		//Instantiate a GridLocation with same row but different col
    		location1 = new GridLocation(row1, col1);
    		location2 = new GridLocation(row1, col2);

    		//Check compareTo - should be equal to col1 - col2
    		assertEquals(col1 - col2, location1.compareTo(location2));

    		//Instantiate a GridLocation with same col but different row
    		location1 = new GridLocation(row1, col1);
    		location2 = new GridLocation(row2, col1);

    		//Check compareTo - should be false
    		assertEquals(row1 - row2, location1.compareTo(location2));

    		// end-user-code
        return;*/
}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    int row1 = 3, col1 = 4;
    int row2 = 3, col2 = 5;

    //Setup root object
    GridLocation object (row1, col1);

    //Setup equalObject equal to object
    GridLocation equalObject(row1, col1);

    //Setup transitiveObject equal to object
    GridLocation transitiveObject(row1, col1);

    // Set its data, not equal to object
    GridLocation unEqualObject(row2, col2);


    // Assert that these two objects are equal
    BOOST_REQUIRE_EQUAL(object==(equalObject), true);

    // Assert that two unequal objects returns false
    BOOST_REQUIRE_EQUAL(object==(unEqualObject), false);

    // Check that equals() is Reflexive
    // x==(x) = true
    BOOST_REQUIRE_EQUAL(object==(object), true);

    // Check that equals() is Symmetric
    // x==(y) = true iff y==(x) = true
    BOOST_REQUIRE_EQUAL(object==(equalObject)
                        && equalObject==(object), true);

    // Check that equals() is Transitive
    // x==(y) = true, y==(z) = true => x==(z) = true
    if (object==(equalObject)
            && equalObject==(transitiveObject)) {
        BOOST_REQUIRE_EQUAL(object==(transitiveObject), true);
    } else {
        BOOST_FAIL("Failed in Equality check");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(object==(equalObject)
                        && object==(equalObject)
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
    int row1 = 3, col1 = 4;

    //Setup root object
    GridLocation object(row1, col1);

    //Run the copy routine
    GridLocation copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<GridLocation >clonedObject = object.clone();

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(*clonedObject.get()), true);

    return;

    // end-user-code

}

BOOST_AUTO_TEST_CASE(checkLWRDataProvider) {

	//begin-user-code

	//Local Declarations
	std::shared_ptr<LWRDataProvider> provider1 (new LWRDataProvider());
	provider1.get()->setSourceInfo("Source Info 3");
	GridLocation location1(0,0);
	std::shared_ptr<LWRDataProvider> nullProvider;

	//Try to set
	location1.setLWRDataProvider(provider1);

	//Check comparison
	BOOST_REQUIRE_EQUAL(location1.getLWRDataProvider().get()->operator==(*provider1.get()), true);

	//Try null
	location1.setLWRDataProvider(nullProvider);

	//Show that it does not change
	BOOST_REQUIRE_EQUAL(location1.getLWRDataProvider().get()->operator==(*provider1.get()), true);

	//end-user-code

}
BOOST_AUTO_TEST_SUITE_END()
