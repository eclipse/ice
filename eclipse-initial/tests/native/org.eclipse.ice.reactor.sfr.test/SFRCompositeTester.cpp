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
#include <SFRComposite.h>

#include <memory>
#include <string>
#include <vector>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(SFRCompositeTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code

	SFRComposite composite;

	// SFRComposite extends SFRComponent, so we need to check the defaults
	// for SFRComponent AND the features that SFRComposite adds.

	// SFRComponent default check.
	BOOST_REQUIRE_EQUAL("Composite 1's Description", composite.getDescription());
	BOOST_REQUIRE_EQUAL(1, composite.getId());
	BOOST_REQUIRE_EQUAL("Composite 1", composite.getName());

	std::vector<std::string> emptyStrings;
	std::vector<std::shared_ptr<Component> > emptyComponents;

	// SFRComposite default check. (We initialize the list of components!)
	BOOST_REQUIRE(emptyStrings == composite.getComponentNames());
	BOOST_REQUIRE(emptyComponents == composite.getComponents());
	BOOST_REQUIRE_EQUAL(0, composite.getNumberOfComponents());

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	// Initialize objects for testing.
	SFRComposite object;
	SFRComposite equalObject;
	SFRComposite unequalObject;

	// Set up the object and equalObject.
	object.setName("Earth");
	object.setDescription("Mostly Harmless");
	object.setSourceInfo("Mice made it");
	object.addComponent(std::make_shared<SFRComponent>("Arthur"));
	object.addComponent(std::make_shared<SFRComponent>("Trillian"));

	equalObject.setName("Earth");
	equalObject.setDescription("Mostly Harmless");
	equalObject.setSourceInfo("Mice made it");
	equalObject.addComponent(std::make_shared<SFRComponent>("Arthur"));
	equalObject.addComponent(std::make_shared<SFRComponent>("Trillian"));

	// Set up the unequalObject.
	unequalObject.setName("Earth");
	unequalObject.setDescription("Mostly Harmless");
	unequalObject.setSourceInfo("Mice made it");
	unequalObject.addComponent(std::make_shared<SFRComponent>("Arthur"));
	// No Trillian. Different!

	// Check that equality is reflexive and symmetric.
	BOOST_REQUIRE_EQUAL(true, object == object);
	BOOST_REQUIRE_EQUAL(true, object == equalObject);
	BOOST_REQUIRE_EQUAL(true, equalObject == object);

	// Check that equals will fail when it should.
	BOOST_REQUIRE_EQUAL(false, object == unequalObject);
	BOOST_REQUIRE_EQUAL(false, unequalObject == object);

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkCopying) {
	// begin-user-code

	// Define an object, a copy, and a clone.
	SFRComposite object;
	SFRComposite objectCopy;
	std::shared_ptr<SFRComposite> objectClone;

	// Set up the object.
	object.setName("Earth");
	object.setDescription("Mostly Harmless");
	object.setSourceInfo("Mice made it");
	object.addComponent(std::make_shared<SFRComponent>("Arthur"));
	object.addComponent(std::make_shared<SFRComponent>("Trillian"));

	/* ---- Check copying. ---- */
	// Make sure the object is not the same as its copy.
	BOOST_REQUIRE_EQUAL(false, object == objectCopy);

	// Copy the object.
	objectCopy = SFRComposite(object);

	// Make sure the object is now the same as its copy.
	BOOST_REQUIRE_EQUAL(true, object == objectCopy);
	/* ------------------------ */

	/* ---- Check cloning. ---- */
	// Clone the object.
	objectClone = std::dynamic_pointer_cast<SFRComposite>(object.clone());

	// Make sure the object is now the same as its clone.
	BOOST_REQUIRE_EQUAL(true, object == *objectClone);
	/* ------------------------ */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkComponentAddRem) {
	// begin-user-code

	// Initialize a Composite for testing.
	SFRComposite composite;

	std::vector<std::shared_ptr<Component>> components;
	std::vector<std::string> componentNames;

	std::shared_ptr<Component> nullComponent;
	std::string nullString;


	/* ---- Make sure the list of components is empty. ---- */
	// This method should return zero.
	BOOST_REQUIRE_EQUAL(0, composite.getNumberOfComponents());

	// The component list should be empty.
	BOOST_REQUIRE(components == composite.getComponents());

	// The component name list should also be empty.
	BOOST_REQUIRE(componentNames == composite.getComponentNames());
	/* ---------------------------------------------------- */



	/* ---- Add a single component and check getting and removing. ---- */
	std::shared_ptr<SFRComponent> defaultComponent = std::make_shared<SFRComponent>();
	std::string defaultName = defaultComponent->getName();
	int defaultId = defaultComponent->getId();

	// Make sure defaultComponent is not in the list yet.
	BOOST_REQUIRE_EQUAL(nullComponent, composite.getComponent(defaultName));

	// Add defaultComponent to the list.
	composite.addComponent(defaultComponent);

	/* -- Make sure defaultComponent is the only one in the list. -- */
	// Check getComponent(int childId)
	BOOST_REQUIRE(defaultComponent == composite.getComponent(defaultId));

	// Check getComponent(std::string name)
	BOOST_REQUIRE(defaultComponent == composite.getComponent(defaultName));

	// Check the component list.
	components.push_back(defaultComponent);
	BOOST_REQUIRE(components == composite.getComponents());

	// Check the component names list.
	componentNames.push_back(defaultName);
	BOOST_REQUIRE(componentNames == composite.getComponentNames());
	/* -- ------------------------------------------------------- -- */

	// Remove the component with removeComponent(int childId).
	composite.removeComponent(defaultId);

	// The component list should be empty.
	components.clear();
	BOOST_REQUIRE(components == composite.getComponents());

	// The component name list should also be empty.
	componentNames.clear();
	BOOST_REQUIRE(componentNames == composite.getComponentNames());

	// We shouldn't be able to get defaultComponent.
	BOOST_REQUIRE_EQUAL(nullComponent, composite.getComponent(defaultId));
	BOOST_REQUIRE_EQUAL(nullComponent, composite.getComponent(defaultName));

	// Re-add the component.
	composite.addComponent(defaultComponent);

	/* -- Make sure defaultComponent is the only one in the list. -- */
	// Check getComponent(int childId)
	BOOST_REQUIRE_EQUAL(defaultComponent, composite.getComponent(defaultId));

	// Check getComponent(std::string name)
	BOOST_REQUIRE_EQUAL(defaultComponent, composite.getComponent(defaultName));

	// Check the component list.
	components.push_back(defaultComponent);
	BOOST_REQUIRE(components == composite.getComponents());

	// Check the component names list.
	componentNames.push_back(defaultName);
	BOOST_REQUIRE(componentNames == composite.getComponentNames());
	/* -- ------------------------------------------------------- -- */

	// Remove the component with removeComponent(std::string name).
	composite.removeComponent(defaultName);

	// The component list should be empty.
	components.clear();
	BOOST_REQUIRE(components == composite.getComponents());

	// The component name list should also be empty.
	componentNames.clear();
	BOOST_REQUIRE(componentNames == composite.getComponentNames());

	// We shouldn't be able to get defaultComponent.
	BOOST_REQUIRE_EQUAL(nullComponent, composite.getComponent(defaultId));
	BOOST_REQUIRE_EQUAL(nullComponent, composite.getComponent(defaultName));
	/* ---------------------------------------------------------------- */



	/* ---- Make sure the list of components is empty. ---- */
	// This method should return zero.
	BOOST_REQUIRE_EQUAL(0, composite.getNumberOfComponents());

	// The component list should be empty.
	components.clear();
	BOOST_REQUIRE(components == composite.getComponents());

	// The component name list should also be empty.
	componentNames.clear();
	BOOST_REQUIRE(componentNames == composite.getComponentNames());
	/* ---------------------------------------------------- */



	/* ---- Add multiple components and check getting and removing. ---- */
	std::shared_ptr<SFRComponent> component1 = std::make_shared<SFRComponent>("Leonardo");
	std::shared_ptr<SFRComponent> component2 = std::make_shared<SFRComponent>("Raphael");
	std::shared_ptr<SFRComponent> component3 = std::make_shared<SFRComponent>("Donatello");
	std::shared_ptr<SFRComponent> component4 = std::make_shared<SFRComponent>("Michelangelo");

	// Set their IDs to non-default IDs, otherwise who knows what
	// composite.getComponent(int childId) will return.
	component1->setId(10);
	component2->setId(20);
	component3->setId(30);
	component4->setId(40);

	// Add all four components.
	composite.addComponent(component1);
	composite.addComponent(component2);
	composite.addComponent(component3);
	composite.addComponent(component4);

	/* -- Make sure all four components are in the list. -- */
	// Check getComponent(int childId)
	BOOST_REQUIRE(component1 == composite.getComponent(component1->getId()));

	// Check getComponent(std::string name)
	BOOST_REQUIRE(component2 == composite.getComponent(component2->getName()));

	// Check getComponent(int childId)
	BOOST_REQUIRE(component3 == composite.getComponent(component3->getId()));

	// Check getComponent(std::string name)
	BOOST_REQUIRE(component4 == composite.getComponent(component4->getName()));

	// Check the component list.
	components = composite.getComponents();
	BOOST_REQUIRE_EQUAL(4, components.size());
	BOOST_REQUIRE(std::find(components.begin(), components.end(), component1) != components.end());
	BOOST_REQUIRE(std::find(components.begin(), components.end(), component2) != components.end());
	BOOST_REQUIRE(std::find(components.begin(), components.end(), component3) != components.end());
	BOOST_REQUIRE(std::find(components.begin(), components.end(), component4) != components.end());

	// Check the component names list.
	componentNames = composite.getComponentNames();
	BOOST_REQUIRE_EQUAL(4, componentNames.size());
	BOOST_REQUIRE(std::find(componentNames.begin(), componentNames.end(), "Leonardo") != componentNames.end());
	BOOST_REQUIRE(std::find(componentNames.begin(), componentNames.end(), "Raphael") != componentNames.end());
	BOOST_REQUIRE(std::find(componentNames.begin(), componentNames.end(), "Donatello") != componentNames.end());
	BOOST_REQUIRE(std::find(componentNames.begin(), componentNames.end(), "Michelangelo") != componentNames.end());
	/* -- ---------------------------------------------- -- */
	/* ----------------------------------------------------------------- */



	/* ---- Check adding/getting/removing nulls. ---- */
	composite.addComponent(nullComponent);

	// Make sure the components haven't changed.
	components = composite.getComponents();
	BOOST_REQUIRE_EQUAL(4, components.size());
	BOOST_REQUIRE(std::find(components.begin(), components.end(), component1) != components.end());
	BOOST_REQUIRE(std::find(components.begin(), components.end(), component2) != components.end());
	BOOST_REQUIRE(std::find(components.begin(), components.end(), component3) != components.end());
	BOOST_REQUIRE(std::find(components.begin(), components.end(), component4) != components.end());

	BOOST_REQUIRE(nullComponent == composite.getComponent(nullString));
	composite.removeComponent(nullString);

	// Make sure the components haven't changed.
	componentNames = composite.getComponentNames();
	BOOST_REQUIRE_EQUAL(4, componentNames.size());
	BOOST_REQUIRE(std::find(componentNames.begin(), componentNames.end(), "Leonardo") != componentNames.end());
	BOOST_REQUIRE(std::find(componentNames.begin(), componentNames.end(), "Raphael") != componentNames.end());
	BOOST_REQUIRE(std::find(componentNames.begin(), componentNames.end(), "Donatello") != componentNames.end());
	BOOST_REQUIRE(std::find(componentNames.begin(), componentNames.end(), "Michelangelo") != componentNames.end());
	/* ---------------------------------------------- */



	/* ---- Check getting/removing invalid IDs. ---- */
	BOOST_REQUIRE(nullComponent == composite.getComponent(1984));
	composite.removeComponent(1984);

	// Make sure the components haven't changed.
	BOOST_REQUIRE_EQUAL(4, composite.getNumberOfComponents());
	BOOST_REQUIRE(component1 == composite.getComponent("Leonardo"));
	BOOST_REQUIRE(component2 == composite.getComponent("Raphael"));
	BOOST_REQUIRE(component3 == composite.getComponent("Donatello"));
	BOOST_REQUIRE(component4 == composite.getComponent("Michelangelo"));
	/* --------------------------------------------- */



	/* ---- Check getting/removing invalid strings. ---- */
	BOOST_REQUIRE(nullComponent == composite.getComponent("Shredder"));
	composite.removeComponent("Shredder");

	// Make sure the components haven't changed.
	BOOST_REQUIRE_EQUAL(4, composite.getNumberOfComponents());
	BOOST_REQUIRE(component1 == composite.getComponent("Leonardo"));
	BOOST_REQUIRE(component2 == composite.getComponent("Raphael"));
	BOOST_REQUIRE(component3 == composite.getComponent("Donatello"));
	BOOST_REQUIRE(component4 == composite.getComponent("Michelangelo"));
	/* ------------------------------------------------- */



	/* ---- Check adding a component with a used name. ---- */
	std::shared_ptr<SFRComponent> component = std::make_shared<SFRComponent>("Donatello");

	// Make absolutely sure there are two "Donatello" components.
	BOOST_REQUIRE_EQUAL(false, component == component3);
	BOOST_REQUIRE_EQUAL(false, *component == *component3);

	// Add the component.
	composite.addComponent(component);

	// We shouldn't get a result because there's already a component with
	// the name "Donatello".
	BOOST_REQUIRE(nullComponent == composite.getComponent(component->getId()));
	BOOST_REQUIRE_EQUAL(false, component == composite.getComponent("Donatello"));
	BOOST_REQUIRE_EQUAL(false, *component == *(composite.getComponent("Donatello")));

	// Make sure the components haven't changed.
	BOOST_REQUIRE_EQUAL(4, composite.getNumberOfComponents());
	BOOST_REQUIRE(component1 == composite.getComponent("Leonardo"));
	BOOST_REQUIRE(component2 == composite.getComponent("Raphael"));
	BOOST_REQUIRE(component3 == composite.getComponent("Donatello"));
	BOOST_REQUIRE(component4 == composite.getComponent("Michelangelo"));
	/* ---------------------------------------------------- */

	return;
	// end-user-code
}
BOOST_AUTO_TEST_SUITE_END()
