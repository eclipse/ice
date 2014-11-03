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
#ifndef ASSEMBLYTYPE_H
#define ASSEMBLYTYPE_H

// Represents the three possible types of assemblies: fuel, control or reflector. 

enum AssemblyType
{

        // Fuel type includes burnable and blanket assemblies. 
        Fuel,

        // Control type includes primary and secondary (shut down) assemblies. 
        Control,

        // Reflector type contains only reflector assemblies. 
        Reflector,

        // Shield type only contains shield assemblies.
        Shield,

        // Test type includes materials test and fuel test assemblies.
        Test


}; //end enum AssemblyType 

#endif
