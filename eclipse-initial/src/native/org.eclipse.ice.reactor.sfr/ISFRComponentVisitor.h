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
#ifndef ISFRCOMPONENTVISITOR_H
#define ISFRCOMPONENTVISITOR_H

namespace ICE_SFReactor {

// Forward Declarations
class SFReactor;
class SFRAssembly;
class PinAssembly;
class ReflectorAssembly;
class SFRPin;
class SFRRod;
class MaterialBlock;
class Material;
class Ring;

// This interface defines the "visitation" routines that SFRComponents and
// subclasses may use to reveal their types to visitors.
class ISFRComponentVisitor {

public:

	// This operation directs a visitor to perform its actions on the SFRComponent as a SFReactor.
	virtual int visit(SFReactor sfrComp) = 0;

	// This operation directs a visitor to perform its actions on the SFRComponent as a SFRAssembly.
	virtual int visit(SFRAssembly sfrComp) = 0;

	// This operation directs a visitor to perform its actions on the SFRComponent as a PinAssembly.
	virtual int visit(PinAssembly sfrComp) = 0;

	// This operation directs a visitor to perform its actions on the SFRComponent as a ReflectorAssembly.
	virtual int visit(ReflectorAssembly sfrComp) = 0;

	// This operation directs a visitor to perform its actions on the SFRComponent as a SFRPin.
	virtual int visit(SFRPin sfrComp) = 0;

	// This operation directs a visitor to perform its actions on the SFRComponent as a SFRRod.
	virtual int visit(SFRRod sfrComp) = 0;

	// This operation directs a visitor to perform its actions on the SFRComponent as a MaterialBlock.
	virtual int visit(MaterialBlock sfrComp) = 0;

	// This operation directs a visitor to perform its actions on the SFRComponent as a Material.
	virtual int visit(Material sfrComp) = 0;

	// This operation directs a visitor to perform its actions on the SFRComponent as a Ring.
	virtual int visit(Ring sfrComp) = 0;

};
//end class ISFRComponentVisitor

}// end namespace
#endif
