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
#ifndef PINTYPE_H
#define PINTYPE_H

// This enumeration represents allowed types of pins for a PinAssembly. Currently, the PinType corresponds to an attribute of a PinAssembly. 

enum PinType
{

        // A primary control pin. 
        PrimaryControl,

        // A secondary (shutdown) control pin. 
        SecondaryControl,

        // An inner core pin containing burnable fuel.
        InnerFuel,

        // An outer core pin containing burnable fuel.
        OuterFuel,

        // A shielding pin.
        // Note: this enum is called simply "Shield" in the Java implementation,
        // however, C++ won't allow two enums with the same values
        // (AssemblyType::Shield and PinType::Shield in this case), so the name
        // has been changed.
        Shielding,

        // A pin for conducting materials testing.
        MaterialTest,

        // A pin for conducting fuels testing.
        FuelTest,

        // A pin containing fertile blanket fuel. 
        BlanketFuel

}; //end enum PinType 

#endif
