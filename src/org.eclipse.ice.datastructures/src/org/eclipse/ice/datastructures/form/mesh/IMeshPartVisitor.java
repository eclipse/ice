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
package org.eclipse.ice.datastructures.form.mesh;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This interface is used for visiting commonly-used mesh classes.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 * 
 */
public interface IMeshPartVisitor {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The visit operation for a {@link MeshComponent}.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param mesh
	 *            <p>
	 *            The MeshComponent that is being visited.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(MeshComponent mesh);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The visit operation for a generic Polygon. This operation might be called
	 * even if the number of sides is 4 (quad) or 6 (hex). This can happen if
	 * the visited Polygon was initialized as a Polygon.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param polygon
	 *            <p>
	 *            The Polygon that is being visited.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Polygon polygon);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The visit operation for a {@link Quad}, a Polygon that is restricted to
	 * four sides.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param quad
	 *            <p>
	 *            The Quad that is being visited.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Quad quad);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The visit operation for a {@link Hex}, a Polygon that is restricted to
	 * six sides.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param hex
	 *            <p>
	 *            The Hex that is being visited.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Hex hex);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The visit operation for an {@link Edge}.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edge
	 *            <p>
	 *            The Edge that is being visited.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Edge edge);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The visit operation for a {@link BezierEdge}.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edge
	 *            <p>
	 *            The BezierEdge that is being visited.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(BezierEdge edge);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The visit operation for a {@link PolynomialEdge}.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param edge
	 *            <p>
	 *            The PolynomialEdge that is being visited.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(PolynomialEdge edge);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The visit operation for a {@link Vertex}.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param vertex
	 *            <p>
	 *            The Vertex that is being visited.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Vertex vertex);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is a safety operation. This should only be called if the
	 * visited object is not supported.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param object
	 *            <p>
	 *            A visited object that is not supported by this interface.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(Object object);
}
