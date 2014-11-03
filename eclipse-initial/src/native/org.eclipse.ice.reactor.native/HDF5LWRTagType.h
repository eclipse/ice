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

#ifndef HDF5LWRTAGTYPE_H
#define HDF5LWRTAGTYPE_H

namespace ICE_Reactor {

/**
 *  Represents the LWRComponentTypes (stack, material, etc) for the object.
 */
enum HDF5LWRTagType {

    /**
     * Represents a BWReactor.
     */
    BWREACTOR,


    /**
     * Represents the ControlBank.
     */
    CONTROL_BANK,


    /**
     * Represents the FuelAssembly.
     */
    FUEL_ASSEMBLY,

    /**
     * Represents the Grid Label Provider
     */
    GRID_LABEL_PROVIDER,


    /**
     *  Represents the IncoreInstrument.
     */
    INCORE_INSTRUMENT,

    /**
     * Represents the LWRRod.
     */
    LWRROD,

    /**
     * Represents the Material.
     */
    MATERIAL,

    /**
     * Represents a generic LWRComponent.
     */
    LWRCOMPONENT,


    /**
     * Represents a generic LWRComposite.
     */
    LWRCOMPOSITE,

    /**
     * Represents the LWR Grid Manager.
     */
    LWRGRIDMANAGER,

    /**
     * Represents a LWReactor.
     */
    LWREACTOR,

    /**
     * Represents a PWRAssembly.
     */
    PWRASSEMBLY,

    /**
     * Represents the PWReactor.
     */
    PWREACTOR,

    /**
     * Represents the Ring.
     */
    RING,

    /**
     * Represents the Rod Cluster Assembly.
     */
    ROD_CLUSTER_ASSEMBLY,

    /**
     * Represents the MaterialBlock.
     */
    MATERIALBLOCK,

    /**
     * Represents the Tube.
     */
    TUBE
};

}

#endif
