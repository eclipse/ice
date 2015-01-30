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
#include "SFRPin.h"
#include "Ring.h"
#include <Material.h>
#include <MaterialBlock.h>

using namespace ICE_SFReactor;

SFRPin::SFRPin() : SFRComponent("SFR Pin 1") {
	// begin-user-code

	// Set the other defaults.
	setDescription("SFR Pin 1's Description");
	setId(1);

	// Numbers below are in mm.

	// Create a stainless steel material.
	std::shared_ptr<Material> steel = std::make_shared<Material>("SS-316");
	steel->setDescription("Stainless Steel");

	// Set the default cladding.
	this->cladding = std::make_shared<Ring>("Cladding", steel, -1, 16.25, 17.5);

	// Create a helium material.
	std::shared_ptr<Material> helium = std::make_shared<Material>("He");
	helium->setDescription("Helium");

	// Set the default fill gas.
	this->fillGas = helium;

	// Create a fuel material.
	std::shared_ptr<Material> fuel = std::make_shared<Material>("UO2");
	fuel->setDescription("Uranium Oxide");

	// Create the default MaterialBlock set (one block).
	this->materialBlocks;

	std::shared_ptr<MaterialBlock> block = std::make_shared<MaterialBlock>();
	block->setVertPosition(0);

	// Add the cladding, fill gas, and fuel to the block.
	block->addRing(this->cladding);

	// Try to add fill gas and fuel if the cladding does not take up too
	// much space.
	double outerRadius = this->cladding->getInnerRadius();
	double innerRadius = outerRadius - 2.9167;
	if (innerRadius > 0.0) {
		block->addRing(std::make_shared<Ring>("Fill Gas", this->fillGas, -1, innerRadius, outerRadius));
		block->addRing(std::make_shared<Ring>("Fuel", fuel, -1, 0, innerRadius));
	}

	// Add the block to the set.
	this->materialBlocks.insert(block);

	return;
	// end-user-code
}
SFRPin::SFRPin(std::string name) : SFRComponent("SFR Pin 1") {
	// begin-user-code

	// Try to set the name
	setName(name);

	// Set the other defaults.
	setDescription("SFR Pin 1's Description");
	setId(1);

	// Numbers below are in mm.

	// Create a stainless steel material.
	std::shared_ptr<Material> steel = std::make_shared<Material>("SS-316");
	steel->setDescription("Stainless Steel");

	// Set the default cladding.
	this->cladding = std::make_shared<Ring>("Cladding", steel, -1, 16.25, 17.5);

	// Create a helium material.
	std::shared_ptr<Material> helium = std::make_shared<Material>("He");
	helium->setDescription("Helium");

	// Set the default fill gas.
	this->fillGas = helium;

	// Create a fuel material.
	std::shared_ptr<Material> fuel = std::make_shared<Material>("UO2");
	fuel->setDescription("Uranium Oxide");

	// Create the default MaterialBlock set (one block).
	this->materialBlocks;

	std::shared_ptr<MaterialBlock> block = std::make_shared<MaterialBlock>();
	block->setVertPosition(0);

	// Add the cladding, fill gas, and fuel to the block.
	block->addRing(this->cladding);

	// Try to add fill gas and fuel if the cladding does not take up too
	// much space.
	double outerRadius = this->cladding->getInnerRadius();
	double innerRadius = outerRadius - 2.9167;
	if (innerRadius > 0.0) {
		block->addRing(std::make_shared<Ring>("Fill Gas", this->fillGas, -1, innerRadius, outerRadius));
		block->addRing(std::make_shared<Ring>("Fuel", fuel, -1, 0, innerRadius));
	}

	// Add the block to the set.
	this->materialBlocks.insert(block);

	return;
	// end-user-code
}
SFRPin::SFRPin(std::string name, std::shared_ptr<Material> fillGas,
		std::set<std::shared_ptr<MaterialBlock>> materialBlocks,
		std::shared_ptr<Ring> cladding) : SFRComponent("SFR Pin 1") {
	// begin-user-code

	// Set the other defaults.
	setDescription("SFR Pin 1's Description");
	setId(1);

	// Try to set the name.
	setName(name);

	// Numbers below are in mm.

	// Try to set the cladding.
	setCladding(cladding);

	// If the cladding did not set properly, set the default value.
	if (!cladding || !(*cladding == *this->cladding)) {
		// Create a stainless steel material.
		std::shared_ptr<Material> steel = std::make_shared<Material>("SS-316");
		steel->setDescription("Stainless Steel");

		// Set the default cladding.
		this->cladding = std::make_shared<Ring>("Cladding", steel, -1, 16.25, 17.5);
	}

	// Try to set the fill gas.
	setFillGas(fillGas);

	// If the fill gas did not set properly, set the default value.
	if (!fillGas || !(*fillGas == *this->fillGas)) {
		// Create a helium material.
		std::shared_ptr<Material> helium = std::make_shared<Material>("He");
		helium->setDescription("Helium");

		// Set the default fill gas.
		this->fillGas = helium;
	}

	// Try to set the material blocks.
	setMaterialBlocks(materialBlocks);

	// If the material blocks did not set properly, set the default value.
	if (this->materialBlocks.empty()) {
		// Create a fuel material.
		std::shared_ptr<Material> fuel = std::make_shared<Material>("UO2");
		fuel->setDescription("Uranium Oxide");

		// Create the default MaterialBlock set (one block).
		this->materialBlocks;

		std::shared_ptr<MaterialBlock> block = std::make_shared<MaterialBlock>();
		block->setVertPosition(0);

		// Add the cladding, fill gas, and fuel to the block.
		block->addRing(this->cladding);

		// Try to add fill gas and fuel if the cladding does not take up too
		// much space.
		double outerRadius = this->cladding->getInnerRadius();
		double innerRadius = outerRadius - 2.9167;
		if (innerRadius > 0.0) {
			block->addRing(std::make_shared<Ring>("Fill Gas", this->fillGas, -1, innerRadius, outerRadius));
			block->addRing(std::make_shared<Ring>("Fuel", fuel, -1, 0, innerRadius));
		}

		// Add the block to the set.
		this->materialBlocks.insert(block);
	}

	return;
	// end-user-code
}
SFRPin::SFRPin(const SFRPin & otherPin) : SFRComponent(otherPin) {
	// begin-user-code

	// Copy the local values.
	fillGas = std::make_shared<Material>(*otherPin.fillGas);
	cladding = std::make_shared<Ring>(*otherPin.cladding);
	materialBlocks = otherPin.materialBlocks;

	return;
	// end-user-code
}
void SFRPin::setFillGas(std::shared_ptr<Material> gas) {
	// begin-user-code

	// Set the fill gas if the parameter is not null.
	if (gas)
		fillGas = gas;

	return;
	// end-user-code
}
std::shared_ptr<Material> SFRPin::getFillGas() {
	// begin-user-code
	return fillGas;
	// end-user-code
}
void SFRPin::setMaterialBlocks(std::set<std::shared_ptr<MaterialBlock>> materialBlocks) {
	// begin-user-code

	// Set the material blocks if the incoming TreeSet is not null or empty.
	if (!materialBlocks.empty())
		this->materialBlocks = materialBlocks;

	return;
	// end-user-code
}
std::set<std::shared_ptr<MaterialBlock>> SFRPin::getMaterialBlocks() {
	// begin-user-code
	return materialBlocks;
	// end-user-code
}
void SFRPin::setCladding(std::shared_ptr<Ring> cladding) {
	// begin-user-code

	// Set the cladding if the parameter is not null.
	if (cladding)
		this->cladding = cladding;

	return;
	// end-user-code
}
std::shared_ptr<Ring> SFRPin::getCladding() {
	// begin-user-code
	return cladding;
	// end-user-code
}
std::shared_ptr<Identifiable> SFRPin::clone() {
	// begin-user-code

	// Initialize a new SFRPin.
	std::shared_ptr<SFRPin> pin = std::make_shared < SFRPin > (*this);

	// Return the newly instantiated object.
	return std::dynamic_pointer_cast < Identifiable > (pin);

	// end-user-code
}
bool SFRPin::operator==(const SFRPin & otherPin) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare the values between the two objects.
	equals = (SFRComponent::operator==(otherPin)
			&& *fillGas == *otherPin.fillGas
			&& *cladding == *otherPin.cladding
			&& materialBlocks.size() == otherPin.materialBlocks.size());

	// Manually check the contents of MaterialBlocks
	std::set<std::shared_ptr<MaterialBlock> >::iterator iter1;
	std::set<std::shared_ptr<MaterialBlock> >::const_iterator iter2;

	// Loop until we have checked every block or we have a mismatch.
	for (iter1 = materialBlocks.begin(), iter2 = otherPin.materialBlocks.begin();
			equals && iter1 != materialBlocks.end();
			iter1++, iter2++) {
		equals = (**iter1 == **iter2);
	}

	return equals;
	// end-user-code
}
