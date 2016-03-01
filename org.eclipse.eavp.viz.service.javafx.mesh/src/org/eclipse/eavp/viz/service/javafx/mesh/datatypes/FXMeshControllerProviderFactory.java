/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.mesh.datatypes;

import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractControllerProviderFactory;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
import org.eclipse.eavp.viz.service.modeling.FaceEdgeMesh;
import org.eclipse.eavp.viz.service.modeling.FaceMesh;
import org.eclipse.eavp.viz.service.modeling.IControllerProvider;
import org.eclipse.eavp.viz.service.modeling.IMesh;
import org.eclipse.eavp.viz.service.modeling.LinearEdgeMesh;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;

/**
 * A factory which creates JavaFX specific AbstractViews and AbstractControllers
 * for an AbstractMeshComponent.
 * 
 * @author Robert Smith
 *
 */
public class FXMeshControllerProviderFactory extends AbstractControllerProviderFactory {

	/**
	 * The default cosntructor.
	 */
	public FXMeshControllerProviderFactory() {
		super();

		// Set the EdgeMesh provider
		typeMap.put(EdgeMesh.class,
				new IControllerProvider<FXEdgeController>() {
					@Override
					public FXEdgeController createController(IMesh model) {

						// If the model is an edge component, create an edge
						// with a
						// linear
						// edge view
						FXLinearEdgeView view = new FXLinearEdgeView(
								(EdgeMesh) model);
						return new FXEdgeController((EdgeMesh) model, view);
					}
				});

		// TODO find a way to avoid enumerating every subclass of EdgeMesh here
		// Copy the EdgeMesh provider to the other EdgeMesh classes
		typeMap.put(FaceEdgeMesh.class, typeMap.get(EdgeMesh.class));
		typeMap.put(LinearEdgeMesh.class, typeMap.get(EdgeMesh.class));

		// Set the NekPolygonMesh provider
		typeMap.put(NekPolygonMesh.class,
				new IControllerProvider<NekPolygonController>() {
					@Override
					public NekPolygonController createController(IMesh model) {

						// Create a NekPolygonController with a face view
						FXFaceView view = new FXFaceView(model);
						return new NekPolygonController((FaceMesh) model, view);
					}
				});

		// Set the VertexMesh provider
		typeMap.put(VertexMesh.class,
				new IControllerProvider<FXVertexController>() {
					@Override
					public FXVertexController createController(IMesh model) {

						// Create a vertex controller
						FXVertexView view = new FXVertexView(
								(VertexMesh) model);
						return new FXVertexController((VertexMesh) model, view);
					}
				});
	}

}
