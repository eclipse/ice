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
#define BOOST_TEST_DYN_LINK
#define BOOST_TEST_MODULE Regression
#include <boost/test/included/unit_test.hpp>
#include <assembly/SFRPin.h>
#include <assembly/Ring.h>
#include <MaterialBlock.h>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(SFRPinTester_testSuite)


BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code

	// A pin to test.
	SFRPin pin;

	// Invalid, default and valid values to use in the constructor.
	std::string defaultName = "SFR Pin 1";
	std::string name = "Trillian";

	std::shared_ptr<Ring> defaultCladding;
	std::shared_ptr<Ring> cladding = std::make_shared<Ring>("Hollow tube", std::make_shared<Material>("Something Solid"), 42.0, 15, 16);

	std::shared_ptr<Material> defaultFillGas;
	std::shared_ptr<Material> fillGas = std::make_shared<Material>("Oxygen-based mixture");

	std::shared_ptr<Material> defaultFuel;

	std::string defaultDescription = "SFR Pin 1's Description";
	int defaultId = 1;

	std::set< std::shared_ptr<MaterialBlock> > defaultMaterialBlocks;
	std::set< std::shared_ptr<MaterialBlock> > materialBlocks;

	/* ---- Update the default values. ---- */

	// Create helium gas
	Material helium("He");
	helium.setDescription("Helium");

	// Create a stainless steel material.
	Material steel("SS-316");
	steel.setDescription("Stainless Steel");

	// Create a fuel material.
	Material fuel("UO2");
	fuel.setDescription("Uranium Oxide");

	// Define default gas, fuel and cladding
	defaultCladding = std::make_shared<Ring>("Cladding", std::make_shared<Material>(steel), -1, 16.25, 17.5);
	defaultFillGas = std::make_shared<Material>(helium);
	defaultFuel = std::make_shared<Material>(fuel);


	// Create the material block used in the default set
	MaterialBlock defaultBlock;
	defaultBlock.setVertPosition(0);

	// Add the default cladding, fill gas, and fuel to the block.
	defaultBlock.addRing(defaultCladding);
	defaultBlock.addRing(std::make_shared<Ring>("Fill Gas", defaultFillGas, -1, 13.3333, 16.25));
	defaultBlock.addRing(std::make_shared<Ring>("Fuel", defaultFuel, -1, 0, 13.3333));

	// Insert the block into default set for comparison.
	defaultMaterialBlocks.insert(std::make_shared<MaterialBlock>(defaultBlock));

	// Create the material block used in the non-default set
	MaterialBlock block;
	block.setVertPosition(0);

	// Add the cladding, fill gas, and fuel to the block.
	block.addRing(cladding);
	block.addRing(std::make_shared<Ring>("Fill Gas", fillGas, -1, 13.3333, 16.25));
	block.addRing(std::make_shared<Ring>("Fuel", defaultFuel, -1, 0, 13.3333));

	// Insert the block into non-default set for comparison.
	materialBlocks.insert(std::make_shared<MaterialBlock>(block));

	/* ------------------------------------ */

	/* ---- Test SFRPin() ---- */

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(defaultName, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*defaultFillGas == *pin.getFillGas());
	BOOST_REQUIRE(*defaultCladding == *pin.getCladding());

	// Compare the sizes of the MaterialBlock sets
	std::set< std::shared_ptr<MaterialBlock> > set1 = defaultMaterialBlocks;
	std::set< std::shared_ptr<MaterialBlock> > set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Create iterators to compare elements of the sets
	std::set< std::shared_ptr<MaterialBlock> >::iterator iter1 = set1.begin();
	std::set< std::shared_ptr<MaterialBlock> >::iterator iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}
	/* ----------------------- */

	/* ---- Test SFRPin(std::string) ---- */

	// Null name string.
	std::string nullStr;
	pin = SFRPin(nullStr);

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(defaultName, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*defaultFillGas == *pin.getFillGas());
	BOOST_REQUIRE(*defaultCladding == *pin.getCladding());

	// Compare the sizes of the MaterialBlock sets
	set1 = defaultMaterialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Set iterators to compare elements of the sets
	iter1 = set1.begin();
	iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	/* ----------------------- */

	// Empty name string.
	pin = SFRPin("");

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(defaultName, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*defaultFillGas == *pin.getFillGas());
	BOOST_REQUIRE(*defaultCladding == *pin.getCladding());

	// Compare the sizes of the MaterialBlock sets
	set1 = defaultMaterialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Set iterators to compare elements of the sets
	iter1 = set1.begin();
	iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	/* ----------------------- */

	// Valid name string.
	pin = SFRPin(name);

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(name, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*defaultFillGas == *pin.getFillGas());
	BOOST_REQUIRE(*defaultCladding == *pin.getCladding());

	// Compare the sizes of the MaterialBlock sets
	set1 = defaultMaterialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Set iterators to compare elements of the sets
	iter1 = set1.begin();
	iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	/* ----------------------------- */

	/* ---- Test SFRPin(string, Ring, Material, set<MaterialBlock>) ---- */

	// All parameters invalid.
	std::shared_ptr<Ring> nullClad;
	std::shared_ptr<Material> nullGas;
	std::set< std::shared_ptr<MaterialBlock> > emptyBlock;
	pin = SFRPin(nullStr, nullGas, emptyBlock, nullClad);

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(defaultName, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*defaultFillGas == *pin.getFillGas());
	BOOST_REQUIRE(*defaultCladding == *pin.getCladding());

	// Compare the sizes of the MaterialBlock sets
	set1 = defaultMaterialBlocks;
	set2 = pin.getMaterialBlocks();

	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Set iterators to compare elements of the sets
	iter1 = set1.begin();
	iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	/* ----------------------------- */

	// Null name string
	pin = SFRPin(nullStr, fillGas, materialBlocks, cladding);

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(defaultName, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*fillGas == *pin.getFillGas());
	BOOST_REQUIRE(*cladding == *pin.getCladding());

	// Compare the sizes of the MaterialBlock sets
	set1 = materialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Set iterators to compare elements of the sets
	iter1 = set1.begin();
	iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	/* ----------------------------- */

	// Empty name string
	pin = SFRPin(" ", fillGas, materialBlocks, cladding);

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(defaultName, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*fillGas == *pin.getFillGas());
	BOOST_REQUIRE(*cladding == *pin.getCladding());

	// Compare the sizes of the MaterialBlock sets
	set1 = materialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Set iterators to compare elements of the sets
	iter1 = set1.begin();
	iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	/* ----------------------------- */

	// Null cladding.
	pin = SFRPin(name, fillGas, materialBlocks, nullClad);

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(name, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*fillGas == *pin.getFillGas());
	BOOST_REQUIRE(*defaultCladding == *pin.getCladding());

	// Compare the sizes of the MaterialBlock sets
	set1 = materialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Set iterators to compare elements of the sets
	iter1 = set1.begin();
	iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	/* ----------------------------- */

	// Null fill gas.
	pin = SFRPin(name, nullGas, materialBlocks, cladding);

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(name, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*defaultFillGas == *pin.getFillGas());
	BOOST_REQUIRE(*cladding == *pin.getCladding());

	// Compare the sizes of the MaterialBlock sets
	set1 = materialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Set iterators to compare elements of the sets
	iter1 = set1.begin();
	iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	/* ----------------------------- */

	// Empty material block
	pin = SFRPin(name, fillGas, emptyBlock, cladding);

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(name, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*fillGas == *pin.getFillGas());
	BOOST_REQUIRE(*cladding == *pin.getCladding());

	// We need to update the default MaterialBlocks because it will use the
	// fillGas and cladding that we provided.
	defaultMaterialBlocks.clear();
	block = MaterialBlock();
	block.setVertPosition(0);
	block.addRing(cladding);
	block.addRing(std::make_shared<Ring>("Fill Gas", fillGas, -1, cladding->getInnerRadius() - 2.9167, cladding->getInnerRadius()));
	block.addRing(std::make_shared<Ring>("Fuel", defaultFuel, -1, 0, cladding->getInnerRadius() - 2.9167));
	defaultMaterialBlocks.insert(std::make_shared<MaterialBlock>(block));

	// Compare the sizes of the MaterialBlock sets
	set1 = defaultMaterialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Set iterators to compare elements of the sets
	iter1 = set1.begin();
	iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}


	/* ----------------------------- */

	// All parameters valid.
	pin = SFRPin(name, fillGas, materialBlocks, cladding);

	BOOST_REQUIRE_EQUAL(defaultId, pin.getId());
	BOOST_REQUIRE_EQUAL(name, pin.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, pin.getDescription());
	BOOST_REQUIRE(*fillGas == *pin.getFillGas());
	BOOST_REQUIRE(*cladding == *pin.getCladding());

	// Compare the sizes of the MaterialBlock sets
	set1 = materialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());

	// Set iterators to compare elements of the sets
	iter1 = set1.begin();
	iter2 = set2.begin();

	// Compare the contents of the MaterialBlock sets
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}
	/* --------------------------------------------------------------- */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkAttributes) {
	// begin-user-code

	SFRPin pin;

	// Invalids, defaults, and non-defaults.
	std::shared_ptr<Material> defaultFillGas;
	std::shared_ptr<Material> fillGas = std::make_shared<Material>("Martian atmosphere");
	std::shared_ptr<Ring> defaultCladding;
	std::shared_ptr<Ring> cladding = std::make_shared<Ring>("Hollow tube", std::make_shared<Material>("Something Solid"), 42.0, 15, 16);

	std::set< std::shared_ptr<MaterialBlock> > defaultMaterialBlocks;
	std::set< std::shared_ptr<MaterialBlock> > materialBlocks;

	// Used to test equivalence of MaterialBlock sets.
	std::set< std::shared_ptr<MaterialBlock> > set1;
	std::set< std::shared_ptr<MaterialBlock> > set2;
	std::set< std::shared_ptr<MaterialBlock> >::iterator iter1;
	std::set< std::shared_ptr<MaterialBlock> >::iterator iter2;

	/* ---- Update the default values. ---- */
	// Create a helium material.
	std::shared_ptr<Material> helium = std::make_shared<Material>("He");
	helium->setDescription("Helium");

	// Create a stainless steel material.
	std::shared_ptr<Material> steel = std::make_shared<Material>("SS-316");
	steel->setDescription("Stainless Steel");

	// Create a fuel material.
	std::shared_ptr<Material> fuel = std::make_shared<Material>("UO2");
	fuel->setDescription("Uranium Oxide");

	defaultFillGas = helium;
	defaultCladding = std::make_shared<Ring>("Cladding", steel, -1, 16.25, 17.5);

	std::shared_ptr<MaterialBlock> defaultBlock = std::make_shared<MaterialBlock>();
	defaultBlock->setVertPosition(0);

	// Add the cladding, fill gas, and fuel to the default block.
	defaultBlock->addRing(defaultCladding);
	defaultBlock->addRing(std::make_shared<Ring>("Fill Gas", helium, -1, 13.3333, 16.25));
	defaultBlock->addRing(std::make_shared<Ring>("Fuel", fuel, -1, 0, 13.3333));

	defaultMaterialBlocks.insert(defaultBlock);

	std::shared_ptr<MaterialBlock> block = std::make_shared<MaterialBlock>();
	block->setVertPosition(0);

	// Add the cladding, fill gas, and fuel to the non-default block.
	block->addRing(cladding);
	block->addRing(std::make_shared<Ring>("Fill Gas", helium, -1, 14.0, 16.25));
	block->addRing(std::make_shared<Ring>("Fuel", steel, -1, 0, 14.0));

	materialBlocks.insert(block);
	/* ------------------------------------ */

	/* ---- Test fill gas. ---- */
	// Check the default.
	BOOST_REQUIRE(*defaultFillGas == *pin.getFillGas());

	// Update the value.
	pin.setFillGas(fillGas);

	// Verify the value.
	BOOST_REQUIRE(*fillGas == *pin.getFillGas());

	// Try to set an invalid value.
	std::shared_ptr<Material> nullGas;
	pin.setFillGas(nullGas);

	// Verify the value remains unchanged
	BOOST_REQUIRE(*fillGas == *pin.getFillGas());
	/* ------------------------ */

	/* ---- Test cladding. ---- */
	// Check the default.
	BOOST_REQUIRE(*defaultCladding == *pin.getCladding());

	// Update the value.
	pin.setCladding(cladding);

	// Verify the value.
	BOOST_REQUIRE(*cladding == *pin.getCladding());

	// Try to set an invalid value.
	std::shared_ptr<Ring> nullClad;
	pin.setCladding(nullClad);

	// Verify the value remains unchanged
	BOOST_REQUIRE(*cladding == *pin.getCladding());
	/* ------------------------ */

	/* ---- Test material blocks. ---- */
	// Check the default.
	// Compare the contents of the MaterialBlock sets.
	set1 = defaultMaterialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());
	iter1 = set1.begin();
	iter2 = set2.begin();
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	// Update the value.
	pin.setMaterialBlocks(materialBlocks);

	// Verify the value.
	// Compare the contents of the MaterialBlock sets.
	set1 = materialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());
	iter1 = set1.begin();
	iter2 = set2.begin();
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	// Try to set an invalid value.
	std::set< std::shared_ptr<MaterialBlock> > emptySet;
	pin.setMaterialBlocks(emptySet);

	// Verify the value remains unchanged
	// Compare the contents of the MaterialBlock sets.
	set1 = materialBlocks;
	set2 = pin.getMaterialBlocks();
	BOOST_REQUIRE_EQUAL(set1.size(), set2.size());
	iter1 = set1.begin();
	iter2 = set2.begin();
	while (iter1 != set1.end() && iter2 != set2.end()) {
		BOOST_REQUIRE(**iter1 == **iter2);
		iter1++, iter2++;
	}

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	// Initialize objects for testing.
	SFRPin pin;
	SFRPin equalPin;
	SFRPin unequalPin;

	// Set up the pin and equalPin.
	pin.setCladding(std::make_shared<Ring>("Cladding!!!"));
	pin.setFillGas(std::make_shared<Material>("Fill gas!!!"));

	for (int i = 0; i < 10; i++)
		pin.addData(std::make_shared<SFRData>("Data" + std::to_string((long long int) i)), 0);

	equalPin.setCladding(std::make_shared<Ring>("Cladding!!!"));
	equalPin.setFillGas(std::make_shared<Material>("Fill gas!!!"));

	for (int i = 0; i < 10; i++)
		equalPin.addData(std::make_shared<SFRData>("Data" + std::to_string((long long int) i)), 0);

	// Set up the unequalPin.
	unequalPin.setCladding(std::make_shared<Ring>("Cladding!!!"));
	unequalPin.setFillGas(std::make_shared<Material>("He makes your voice squeaky."));
	for (int i = 0; i < 10; i++)
		unequalPin.addData(std::make_shared<SFRData>("Data" + std::to_string((long long int)i)), 0);

	// Check that equality is reflexive and symmetric.
	BOOST_REQUIRE(pin == pin);
	BOOST_REQUIRE(pin == equalPin);
	BOOST_REQUIRE(equalPin == pin);

	// Check that equals will fail when it should.
	BOOST_REQUIRE(!(pin == unequalPin));
	BOOST_REQUIRE(!(unequalPin == pin));

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkCopying) {
	// begin-user-code

	// Initialize pins for testing.
	SFRPin pin;

	// Set up the pin.
	pin.setCladding(std::make_shared<Ring>("Cladding!!!"));
	pin.setFillGas(std::make_shared<Material>("Fill gas!!!"));

	for (int i = 0; i < 10; i++)
		pin.addData(std::make_shared<SFRData>("Data" + std::to_string((long long int) i)), 0);

	// Copy the pin.
	SFRPin copyPin(pin);

	// Make sure the contents are the same.
	BOOST_REQUIRE(pin == copyPin);

	// Do the same for the clone operation.

	// Copy the pin.
	auto clonePin = pin.clone();

	// Make sure it's not null
	BOOST_REQUIRE(clonePin);

	// Make sure the contents are the same.
	BOOST_REQUIRE(pin == *(std::dynamic_pointer_cast<SFRPin>(clonePin)));
	BOOST_REQUIRE(copyPin == *(std::dynamic_pointer_cast<SFRPin>(clonePin)));

	return;
	// end-user-code
}
BOOST_AUTO_TEST_SUITE_END()
