/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.ice.viz.service.geometry;

import org.eclipse.ice.viz.service.geometry.shapes.IShape;

import javafx.scene.Node;

/**
 * <p>
 * Contains various static utility functions and constants used throughout that
 * can't be refactored into specific classes..
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class Util {

    /** */
    public static final String SHAPE_PROP_KEY = "shape"; //$NON-NLS-1$

    /**
     * <p>
     * Returns the IShape property defined by Util.SHAPE_PROP_KEY from the
     * supplied node, if one exists.
     * </p>
     * 
     * @param node
     * @return
     */
    public static IShape getShapeProperty(Node node) {
        Object shapeProperty = node.getProperties().get(SHAPE_PROP_KEY);

        if (shapeProperty != null && shapeProperty instanceof IShape) {
            return (IShape) shapeProperty;
        }

        return null;
    }

}
