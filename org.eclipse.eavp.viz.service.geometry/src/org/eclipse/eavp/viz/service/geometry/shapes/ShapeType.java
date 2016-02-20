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
package org.eclipse.eavp.viz.service.geometry.shapes;

/**
 * <p>
 * Represents types of primitive solids
 * </p>
 * 
 * @author Jay Jay Billings
 */
public enum ShapeType {
    /**
     * <p>
     * Default shape type
     * </p>
     * <p>
     * When rendering, None should be taken to mean "invisible". A
     * PrimitiveShape with this type should have no effect on its parent.
     * </p>
     * 
     */
    None, /**
           * <p>
           * A "half-unit" sphere with a radius of 0.5 (diameter of 1) with its
           * origin at its center
           * </p>
           * 
           */
    Sphere, /**
             * <p>
             * A 1x1x1 cube with its origin at its center (0.5, 0.5, 0.5)
             * </p>
             * 
             */
    Cube, /**
           * <p>
           * A cylinder with a radius of 0.5 (diameter of 1), a height of 1, and
           * its origin at its center (0.5, 0.5, 0.5)
           * </p>
           * 
           */
    Cylinder, /**
               * <p>
               * A circular cone with a diameter of 1, height of 1, and its
               * center at (0.5, 0.5, 0.5)
               * </p>
               * 
               */
    Cone, /**
           * <p>
           * A cylinder with an inner and outer radius, an extruded annulus
           * </p>
           * 
           */
    Tube
}