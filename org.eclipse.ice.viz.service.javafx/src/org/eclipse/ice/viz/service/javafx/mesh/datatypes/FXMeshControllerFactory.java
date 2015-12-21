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

import org.eclipse.ice.viz.service.mesh.datastructures.NekPolygon;
import org.eclipse.ice.viz.service.mesh.datastructures.NekPolygonComponent;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMeshComponent;
import org.eclipse.ice.viz.service.modeling.EdgeComponent;
import org.eclipse.ice.viz.service.modeling.FaceComponent;
import org.eclipse.ice.viz.service.modeling.IControllerFactory;
import org.eclipse.ice.viz.service.modeling.VertexComponent;

/**
 * A factory which creates JavaFX specific AbstractViews and AbstractControllers
 * for an AbstractMeshComponent.
 * 
 * @author Robert Smith
 *
 */
public class FXMeshControllerFactory implements IControllerFactory {

	@Override
	public AbstractController createController(AbstractMeshComponent model) {

		// If the model is an edge component, create an edge with a linear
		// edge view
		if (model instanceof EdgeComponent) {
			FXLinearEdgeView view = new FXLinearEdgeView((EdgeComponent) model);
			return new FXEdge((EdgeComponent) model, view);
		}

		// If it is a vertex component, create a vertex
		else if (model instanceof VertexComponent) {
			FXVertexView view = new FXVertexView((VertexComponent) model);
			return new FXVertex((VertexComponent) model, view);
		}

		// If it is a face component, create a face
		else if (model instanceof NekPolygonComponent) {
			FXFaceView view = new FXFaceView(model);
			return new NekPolygon((FaceComponent) model, view);
		}

		// If the component type is not recognized, return null
		return null;
	}

}
