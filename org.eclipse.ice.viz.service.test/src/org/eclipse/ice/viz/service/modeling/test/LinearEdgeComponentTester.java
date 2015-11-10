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
package org.eclipse.ice.viz.service.modeling.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.EdgeComponent;
import org.eclipse.ice.viz.service.modeling.Vertex;
import org.eclipse.ice.viz.service.modeling.VertexComponent;
import org.junit.Test;

/**
 * A class to test LinearEdge's functionality
 * 
 * @author Robert Smith
 *
 */
public class LinearEdgeComponentTester {

	/**
	 * Tests the line's length as the vertices are changed
	 */
	@Test
	public void checkLength() {
		// Create the edge
		EdgeComponent edge = new EdgeComponent();

		// The edge should initially have length 0
		assertEquals(0, Double.compare(edge.getLength(), 0d));

		// Create some vertices
		Vertex vertex1 = new Vertex(new VertexComponent(0, 0, 0),
				new AbstractView());
		Vertex vertex2 = new Vertex(new VertexComponent(1, 1, 1),
				new AbstractView());
		Vertex vertex3 = new Vertex(new VertexComponent(2, 2, 2),
				new AbstractView());

		// Add the first two vertices to the edge.
		edge.addEntity(vertex1);
		edge.addEntity(vertex2);

		// Check that the edge has the correct length
		assertTrue(Double.compare(1.73, edge.getLength()) <= .1d);

		// Replace the second vertex with the third
		edge.removeEntity(vertex2);
		edge.addEntity(vertex3);

		// Check that the edge's length has been updated
		assertTrue(Double.compare(3.46, edge.getLength()) <= .1d);

	}
}
