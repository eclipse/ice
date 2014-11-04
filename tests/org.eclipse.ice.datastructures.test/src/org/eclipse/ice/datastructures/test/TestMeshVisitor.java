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

import org.eclipse.ice.datastructures.form.mesh.BezierEdge;
import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.form.mesh.Hex;
import org.eclipse.ice.datastructures.form.mesh.IMeshPartVisitor;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.mesh.Polygon;
import org.eclipse.ice.datastructures.form.mesh.PolynomialEdge;
import org.eclipse.ice.datastructures.form.mesh.Quad;
import org.eclipse.ice.datastructures.form.mesh.Vertex;

/**
 * <!-- begin-UML-doc -->
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
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TestMeshVisitor implements IMeshPartVisitor {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This variable should be set to true if one of the visit operations was
	 * called and false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected boolean visited = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Gets whether or not the visitor successfully visited the correct
	 * {@link IMeshPart}.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if the visitor successfully visited the correct IMeshPart,
	 *         false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasVisited() {
		return visited;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Resets the {@link #visited} flag.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reset() {
		visited = false;
	}

	public void visit(MeshComponent mesh) {
		// Do nothing by default.
	}

	public void visit(Polygon polygon) {
		// Do nothing by default.
	}

	public void visit(Quad quad) {
		// Do nothing by default.
	}

	public void visit(Hex hex) {
		// Do nothing by default.
	}

	public void visit(Edge edge) {
		// Do nothing by default.
	}

	public void visit(BezierEdge edge) {
		// Do nothing by default.
	}

	public void visit(PolynomialEdge edge) {
		// Do nothing by default.
	}

	public void visit(Vertex vertex) {
		// Do nothing by default.
	}

	public void visit(Object object) {
		// Do nothing by default.
	}

}
