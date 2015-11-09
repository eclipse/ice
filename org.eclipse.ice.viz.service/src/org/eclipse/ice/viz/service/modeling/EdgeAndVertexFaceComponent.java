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
package org.eclipse.ice.viz.service.modeling;

import java.util.List;

/**
 * A Face component which keeps both its Edges and Vertices as its child
 * entities.
 *
 * @author Robert Smith
 */
public class EdgeAndVertexFaceComponent extends FaceComponent {

	/**
	 * The default constructor
	 */
	public EdgeAndVertexFaceComponent() {
		super();
	}

	/**
	 * A constructor taking a list of entities.
	 * 
	 * @param entities
	 */
	public EdgeAndVertexFaceComponent(List<AbstractController> entities) {
		super(entities);
	}

	/**
	 * An implementation of addEntity that adds both the Edge and its Vertices.
	 * 
	 */
	@Override
	public void addEntity(AbstractController newEntity) {

		// If the new entity is an edge, add it and its vertices under the
		// appropriate categories.
		if (newEntity instanceof Edge) {

			// Add the edge under the Edges category
			Edge edge = (Edge) newEntity;
			addEntityByCategory(edge, "Edges");

			// Add each of the edge's vertices under the "Vertices" category
			for (AbstractController vertex : edge
					.getEntitiesByCategory("Vertices")) {
				addEntityByCategory(vertex, "Vertices");
			}
		}

		// Otherwise, add the entity normally
		super.addEntity(newEntity);

	}
}
