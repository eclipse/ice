/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.nek5000.test;

import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
import org.eclipse.eavp.viz.service.modeling.FaceMesh;
import org.eclipse.eavp.viz.service.modeling.IControllerFactory;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;

/**
 * A factory which produces controllers with dummy views for meshes read from
 * files for testing the NekReader.
 * 
 * @author Robert Smith
 *
 */
public class TestNekControllerFactory implements IControllerFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.IControllerFactory#createController(
	 * org.eclipse.eavp.viz.service.modeling.AbstractMesh)
	 */
	@Override
	public AbstractController createController(AbstractMesh model) {

		// If the model is an edge component, create an edge with a linear
		// edge view
		if (model instanceof EdgeMesh) {
			AbstractView view = new AbstractView();
			return new EdgeController((EdgeMesh) model, view);
		}

		// If it is a vertex component, create a vertex
		else if (model instanceof VertexMesh) {
			AbstractView view = new AbstractView();
			return new VertexController((VertexMesh) model, view);
		}

		// If it is a face component, create a face
		else if (model instanceof NekPolygonMesh) {
			AbstractView view = new AbstractView();
			return new NekPolygonController((FaceMesh) model, view);
		}

		// If the component type is not recognized, return null
		return null;
	}

}
