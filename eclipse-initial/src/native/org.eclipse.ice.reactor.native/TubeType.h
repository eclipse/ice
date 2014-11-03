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

#ifndef TUBETYPE_H
#define TUBETYPE_H

namespace ICE_Reactor {

/**
 * The TubeType enumeration describes the types of Tubes that can be created.
 */

enum TubeType {

    /**
     * This literal indicates a guide tube.
     */
    GUIDE,

    /**
     * This literal indicates an instrument tube.
     */
    INSTRUMENT

};

}

#endif
