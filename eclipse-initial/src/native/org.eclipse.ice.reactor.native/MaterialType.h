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

#ifndef MATERIALTYPE_H
#define MATERIALTYPE_H

namespace ICE_Reactor {

/**
 * The MaterialType enumeration describes each type of material phase.
 */

enum MaterialType {

    /**
     * This literal indicates a gas material phase.
     */
    GAS,

    /**
     * This literal indicates a liquid material phase.
     */
    LIQUID,

    /**
     * This literal indicates a solid material phase.
     */
    SOLID

};

}

#endif
