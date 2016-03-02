/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.mesh;

import java.util.Collections;
import java.util.Set;

import org.eclipse.eavp.viz.service.AbstractVizService;
import org.eclipse.eavp.viz.service.IVizCanvas;
import org.eclipse.eavp.viz.service.IVizService;
import org.eclipse.eavp.viz.service.javafx.mesh.datatypes.FXMeshControllerProviderFactory;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.IControllerProviderFactory;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * <p>
 * Implements an ICE viz service for geometry modeling.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public class FXMeshVizService extends AbstractVizService {

	/** The name used to lookup this service. */
	private static final String VIZ_SERVICE_NAME = "ICE JavaFX Mesh Editor"; //$NON-NLS-1$

	/** The version of the service. */
	private static final String CURRENT_VERSION = "1.0";

	/**
	 * <p>
	 * Creates a GeometryCanvas.
	 * </p>
	 */
	@Override
	public IVizCanvas createCanvas(IController mesh) throws Exception {
		FXMeshCanvas canvas = new FXMeshCanvas(mesh);

		return canvas;
	}

	/**
	 * @see IVizService#getName()
	 */
	@Override
	public String getName() {
		return VIZ_SERVICE_NAME;
	}

	/**
	 * @see IVizService#getVersion()
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizService#getFactory()
	 */
	@Override
	public IControllerProviderFactory getFactory() {
		return new FXMeshControllerProviderFactory();
	}

}
