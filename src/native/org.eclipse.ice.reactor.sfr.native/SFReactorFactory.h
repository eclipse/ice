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

#ifndef ORG_ECLIPSE_ICE_REACTOR_SFR_NATIVE_SFREACTORFACTORY_H_
#define ORG_ECLIPSE_ICE_REACTOR_SFR_NATIVE_SFREACTORFACTORY_H_

#include <map>
#include <memory>
//#include <random>
#include <string>
#include <vector>

#include "SFReactor.h"
#include "SFRComponent.h"

namespace ICE_SFReactor {

class SFReactorFactory {

private :

	// The first version here uses C++11-style random number generation. It has been commented out for g++ 4.4.6 support.
//	void addDataToComponent(std::shared_ptr<SFRComponent> component, std::map<std::string, int> features, std::vector<double> times, bool randomData, std::default_random_engine & generator, std::uniform_real_distribution<double> & distribution);

	// Adds randomized data to an SFRComponent. This version uses rand() and srand().
	void addDataToComponent(std::shared_ptr<SFRComponent> component, std::map<std::string, int> features, std::vector<double> times, bool randomData);

public :

	// Generates a populated full-core reactor. The locations of assemblies and pins can be randomly generated, otherwise they will go into the first available positon.
	std::shared_ptr<SFReactor> generatePopulatedFullCoreReactor(int reactorSize, int assemblySize, int nAssemblies, int nAssemblyComponents, int nAxialLevels, long seed, bool randomData, bool randomLocations);

}; // end SFReactorFactory class

} // end namespace

#endif /* ORG_ECLIPSE_ICE_REACTOR_SFR_NATIVE_SFREACTORFACTORY_H_ */
