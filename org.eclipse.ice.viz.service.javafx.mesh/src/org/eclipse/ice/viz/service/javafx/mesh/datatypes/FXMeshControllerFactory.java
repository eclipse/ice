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
package org.eclipse.ice.viz.service.javafx.mesh.datatypes;

import org.eclipse.ice.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.ice.viz.service.mesh.datastructures.NekPolygonMesh;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.EdgeMesh;
import org.eclipse.ice.viz.service.modeling.FaceMesh;
import org.eclipse.ice.viz.service.modeling.IControllerFactory;
import org.eclipse.ice.viz.service.modeling.VertexMesh;

/**
 * A factory which creates JavaFX specific AbstractViews and AbstractControllers
 * for an AbstractMeshComponent.
 * 
 * @author Robert Smith
 *
 */
public class FXMeshControllerFactory implements IControllerFactory {

	@Override
	public AbstractController createController(AbstractMesh model) {

		// If the model is an edge component, create an edge with a linear
		// edge view
		if (model instanceof EdgeMesh) {
			FXLinearEdgeView view = new FXLinearEdgeView((EdgeMesh) model);
			return new FXEdgeController((EdgeMesh) model, view);
		}

		// If it is a vertex component, create a vertex
		else if (model instanceof VertexMesh) {
			FXVertexView view = new FXVertexView((VertexMesh) model);
			return new FXVertexController((VertexMesh) model, view);
		}

		// If it is a face component, create a face
		else if (model instanceof NekPolygonMesh) {
			FXFaceView view = new FXFaceView(model);
			return new NekPolygonController((FaceMesh) model, view);
		}

		// If the component type is not recognized, return null
		return null;
	}

}
