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

#ifndef TESTDRIVER_H
#define TESTDRIVER_H
#include <pwr/PressurizedWaterReactor.h>

using namespace ICE_Reactor;

class TestDriver {

public:
	std::shared_ptr<PressurizedWaterReactor> newMethodSetupData(int numOfAssemblies, int numOfFeatures, int numOfDatas, int numOfTimesteps, int reactorSize, int assemblySize);
	std::shared_ptr<PressurizedWaterReactor> oldMethodSetupData(int numOfAssemblies, int numOfFeatures, int numOfDatas, int numOfTimesteps, int reactorSize, int assemblySize);

};
#endif

