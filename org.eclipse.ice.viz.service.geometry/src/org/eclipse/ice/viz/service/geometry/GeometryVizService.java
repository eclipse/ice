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

import java.util.Collections;
import java.util.Set;

import org.eclipse.ice.viz.service.AbstractVizService;
import org.eclipse.ice.viz.service.IVizCanvas;
import org.eclipse.ice.viz.service.IVizService;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizObject;
import org.eclipse.ice.viz.service.geometry.shapes.Geometry;
import org.eclipse.ice.viz.service.geometry.viewer.GeometryViewer;
import org.eclipse.ice.viz.service.geometry.widgets.TransformationView;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

/**
 * <p>
 * Implements an ICE viz service for geometry modeling.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class GeometryVizService extends AbstractVizService {

    /** The name used to lookup this service. */
    private static final String VIZ_SERVICE_NAME = "ICE Geometry Editor"; //$NON-NLS-1$

    /** The version of the service. */
    private static final String CURRENT_VERSION = "1.0";

    /**
     * <p>
     * Creates a GeometryCanvas.
     * </p>
     * 
     * @see IVizService#createCanvas(IVizObject)
     */
    public IVizCanvas createCanvas(IVizObject geometry) throws Exception {
        if (geometry instanceof Geometry) {
            GeometryCanvas canvas = new GeometryCanvas((Geometry) geometry);

            return canvas;
        } else {
            throw new IllegalArgumentException(Messages.GeometryVizService_InvalidInput);
        }
    }

    /**
     * @see IVizService#getName()
     */
    public String getName() {
        return VIZ_SERVICE_NAME;
    }

    /**
     * @see IVizService#getVersion()
     */
    public String getVersion() {
        return CURRENT_VERSION; // $NON-NLS-1$
    }

    /**
     * @see AbstractVizService#findSupportedExtensions()
     */
    @Override
    protected Set<String> findSupportedExtensions() {
        return Collections.emptySet();
    }

}
