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
#ifndef SFRPIN_H
#define SFRPIN_H
#include <SFRComponent.h>
#include <string>
#include <GridManager.h>

namespace ICE_SFReactor {

class Material;
class MaterialBlock;
class Ring;

// Class representing the SFR pin structure. The pin is the basic unit of the
// FuelAssembly and ControlAssembly lattice, and contains either fissile,
// fertile or absorber pellets, in addition to structural features above and
// below the pellet columns.
class SFRPin : public SFRComponent
{

    private:

        // Type of gas filling the the pellet-clad gap (space between pellet
		// stack and cladding).
        std::shared_ptr<Material> fillGas;

        // Cylindrical tubing structure that houses a pin. 
        std::shared_ptr<Ring> cladding;

        // Collection of material blocks contained in a single SFRPin; can
        // include pellet-clad gap rings, and pellet rings.
        std::set<std::shared_ptr<MaterialBlock>> materialBlocks;

    public:

        // Nullary constructor. 
        SFRPin(); 

        // Parameterized constructor with the name specified. 
        SFRPin(std::string name); 

        // Parameterized constructor specified pin name, location, fill gas and
        // material blocks (if any) specified.
        SFRPin(std::string name, std::shared_ptr<Material> fillGas, std::set<std::shared_ptr<MaterialBlock>> materialBlocks, std::shared_ptr<Ring> cladding);

        // Copy constructor. Deep copies the contents of the object.
        SFRPin(const SFRPin & otherPin);

        // Sets the type of gas present inside the pellet-clad (space between
        // pellet stack and cladding) gap and gas plenum(s).
        void setFillGas(std::shared_ptr<Material> gas);

        // Returns the type of fill gas present inside the pellet-stack gap
        // (space between pellet stack and cladding) and gas plenum(s).
        std::shared_ptr<Material> getFillGas();

        // Sets a collection of material blocks within the SFRPin. 
        void setMaterialBlocks(std::set<std::shared_ptr<MaterialBlock>> materialBlocks);

        // Returns a collection of material blocks within the SFRPin. 
        std::set<std::shared_ptr<MaterialBlock>> getMaterialBlocks();

        // Sets the SFRPin cladding as a Ring. 
        void setCladding(std::shared_ptr<Ring> cladding);

        // Returns the SFRPin's cladding as a Ring. 
        std::shared_ptr<Ring> getCladding();

        // Deep copies and returns a newly instantiated object. 
        std::shared_ptr<Identifiable> clone();

        // Compares the contents of objects and returns true if they are
        // identical, otherwise returns false.
        virtual bool operator==(const SFRPin & otherPin);

};  //end class SFRPin

} // end namespace
#endif
