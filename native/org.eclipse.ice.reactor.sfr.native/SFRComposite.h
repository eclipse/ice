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
#ifndef SFRCOMPOSITE_H
#define SFRCOMPOSITE_H
#include <updateableComposite/Composite.h>
#include "SFRComponent.h"
#include <map>

namespace ICE_SFReactor {

// The SFRComposite class represents all reactor components that can store and
// manage SFRComponents. This class implements the ICE Composite interface.
// This class was designed as a "branch" within the Reactor package to hold
// references to other SFRComponents.
class SFRComposite: virtual public ICE_DS::Composite, public SFRComponent {

private:

	// Hashtable of all SFRComponents contained in the SFRComposite, keyed by name.
	std::map<std::string, std::shared_ptr<SFRComponent> > sFRComponents;

public:

	// Nullary constructor.
	SFRComposite();

	// Deep copies the contents of the SFRComposite.
	SFRComposite(const SFRComposite & otherComp);

	// Returns the SFRComponent of the specified name
	std::shared_ptr<SFRComponent> getComponent(std::string name);

	// Returns a string std::vector of all SFRComponent names contained in the SFRComposite.
	std::vector<std::string> getComponentNames();

	// Removes the component with the specified name.
	void removeComponent(std::string name);

	// Adds the specified Component to the SFRComposite.
	void addComponent(std::shared_ptr<Component> child);

	// Removes the component specified by the ID, from the SFRComposite.
	void removeComponent(int childId);

	// Returns the SFRComponent of the specified ID, from the SFRComposite.
	std::shared_ptr<Component> getComponent(int childId);

	// Returns the number of SFRComonents contained in the SFRComposite, as an int.
	int getNumberOfComponents();

	// Returns an std::vector of SFRComponents contained in the SFRComposite.
	std::vector<std::shared_ptr<Component>> getComponents();

    // Deep copies and returns a newly instantiated object.
    std::shared_ptr<Identifiable> clone();

	// Compares the contents of SFRComposites and returns true if they are identical, otherwise returns false.
	bool operator==(const SFRComposite & otherComp);

};
//end class SFRComposite

}// end namespace

#endif
