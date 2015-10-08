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
package org.eclipse.ice.viz.service.geometry.viewer.factory;

import org.eclipse.ice.viz.service.geometry.viewer.GeometryViewer;
import org.eclipse.ice.viz.service.geometry.viewer.IViewerFactory;
import org.eclipse.ice.viz.service.javafx.internal.FXGeometryViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * <p>
 * Generates JavaFX GeometryViewer instances.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class GeometryViewerFactory implements IViewerFactory {

    /**
     * <p>
     * Creates a JavaFX geometry viewer instance on the supplied parent
     * composite.
     * </p>
     */
    public GeometryViewer createViewer(Composite parent) {
        return new FXGeometryViewer(parent);
    }

}
