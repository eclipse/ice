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
package org.eclipse.ice.tests.nek5000;

import org.eclipse.eavp.viz.modeling.base.BasicView;
import org.eclipse.eavp.viz.modeling.EdgeController;
import org.eclipse.eavp.viz.modeling.Edge;
import org.eclipse.eavp.viz.modeling.factory.IControllerProvider;
import org.eclipse.eavp.viz.modeling.factory.IControllerProviderFactory;
import org.eclipse.eavp.viz.modeling.base.IMesh;
import org.eclipse.eavp.viz.modeling.VertexController;
import org.eclipse.eavp.viz.modeling.Vertex;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygon;

/**
 * A factory which produces controllers with dummy views for meshes read from
 * files for testing the NekReader.
 * 
 * @author Robert Smith
 *
 */
public class TestNekControllerFactory implements IControllerProviderFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.modeling.IControllerFactory#
	 * createController( org.eclipse.eavp.viz.modeling.AbstractMesh)
	 */
	@Override
	public IControllerProvider createProvider(IMesh model) {

		// If the model is an edge component, create an IControllerProvider that
		// creates EdgeControllers
		if (model instanceof Edge) {

			return new IControllerProvider<EdgeController>() {
				@Override
				public EdgeController createController(IMesh model) {

					// Create an FXShapeView for the model, then wrap them
					// both in a
					// shape controller
					BasicView view = new BasicView();
					return new EdgeController((Edge) model, view);
				}
			};
		}

		// If the model is an vertex component, create an IControllerProvider
		// that creates VertexControllers
		else if (model instanceof Vertex) {

			return new IControllerProvider<VertexController>() {
				@Override
				public VertexController createController(IMesh model) {

					// Create an FXShapeView for the model, then wrap them
					// both in a
					// shape controller
					BasicView view = new BasicView();
					return new VertexController((Vertex) model, view);
				}
			};
		}

		// If the model is a face component, create an IControllerProvider that
		// creates NekPolygonControllers
		else if (model instanceof NekPolygon) {
			return new IControllerProvider<NekPolygonController>() {
				@Override
				public NekPolygonController createController(IMesh model) {

					// Create an FXShapeView for the model, then wrap them
					// both in a
					// shape controller
					BasicView view = new BasicView();
					return new NekPolygonController((NekPolygon) model,
							view);
				}
			};
		}

		// If the component type is not recognized, return null
		return null;
	}

}
