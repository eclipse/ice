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
package org.eclipse.ice.datastructures.test;

import org.eclipse.ice.datastructures.form.MeshComponent;
import org.eclipse.ice.viz.service.mesh.datastructures.BezierEdge;
import org.eclipse.ice.viz.service.mesh.datastructures.Edge;
import org.eclipse.ice.viz.service.mesh.datastructures.Hex;
import org.eclipse.ice.viz.service.mesh.datastructures.IMeshPart;
import org.eclipse.ice.viz.service.mesh.datastructures.IMeshPartVisitor;
import org.eclipse.ice.viz.service.mesh.datastructures.Polygon;
import org.eclipse.ice.viz.service.mesh.datastructures.PolynomialEdge;
import org.eclipse.ice.viz.service.mesh.datastructures.Quad;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;

/**
 * <p>
 * Tool for testing whether an {@link IMeshPartVisitor} is visited by an
 * {@link IMeshPart}.
 * </p>
 * <p>
 * By default, a TestMeshVisitor does nothing special with its visit operations.
 * The idea is that a test for a particular IMeshPart's visit operation should
 * override the appropriate visit method to ensure that that particular visit is
 * called from the "visitee's" accept method. For instance, if a Vertex is being
 * visited, only {@link #visit(Vertex)} should change the value of the flag
 * {@link #visited}.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
public class TestMeshVisitor implements IMeshPartVisitor {

	/**
	 * <p>
	 * This variable should be set to true if one of the visit operations was
	 * called and false otherwise.
	 * </p>
	 * 
	 */
	protected boolean visited = false;

	/**
	 * <p>
	 * Gets whether or not the visitor successfully visited the correct
	 * {@link IMeshPart}.
	 * </p>
	 * 
	 * @return <p>
	 *         True if the visitor successfully visited the correct IMeshPart,
	 *         false otherwise.
	 *         </p>
	 */
	public boolean wasVisited() {
		return visited;
	}

	/**
	 * <p>
	 * Resets the {@link #visited} flag.
	 * </p>
	 * 
	 */
	public void reset() {
		visited = false;
	}

	@Override
	public void visit(MeshComponent mesh) {
		// Do nothing by default.
	}

	@Override
	public void visit(Polygon polygon) {
		// Do nothing by default.
	}

	@Override
	public void visit(Quad quad) {
		// Do nothing by default.
	}

	@Override
	public void visit(Hex hex) {
		// Do nothing by default.
	}

	@Override
	public void visit(Edge edge) {
		// Do nothing by default.
	}

	@Override
	public void visit(BezierEdge edge) {
		// Do nothing by default.
	}

	@Override
	public void visit(PolynomialEdge edge) {
		// Do nothing by default.
	}

	@Override
	public void visit(Vertex vertex) {
		// Do nothing by default.
	}

	@Override
	public void visit(Object object) {
		// Do nothing by default.
	}

}
