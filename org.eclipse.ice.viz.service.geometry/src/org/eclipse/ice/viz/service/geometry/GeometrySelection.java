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
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class GeometrySelection extends StructuredSelection {

    /** */
    private final IShape shape;

    /**
     * 
     * @param modelShape
     */
    public GeometrySelection(IShape modelShape) {
        super(modelShape);

        this.shape = modelShape;
    }

    public IShape getShape() {
        return shape;
    }

}
