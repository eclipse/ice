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
 * <p>
 * This interface is used for visiting commonly-used mesh classes.
 * </p>
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IMeshPartVisitor {

	/**
	 * <p>
	 * The visit operation for a {@link MeshComponent}.
	 * </p>
	 * 
	 * @param mesh
	 *            <p>
	 *            The MeshComponent that is being visited.
	 *            </p>
	 */
	public void visit(MeshComponent mesh);

	/**
	 * <p>
	 * The visit operation for a generic Polygon. This operation might be called
	 * even if the number of sides is 4 (quad) or 6 (hex). This can happen if
	 * the visited Polygon was initialized as a Polygon.
	 * </p>
	 * 
	 * @param polygon
	 *            <p>
	 *            The Polygon that is being visited.
	 *            </p>
	 */
	public void visit(Polygon polygon);

	/**
	 * <p>
	 * The visit operation for a {@link Quad}, a Polygon that is restricted to
	 * four sides.
	 * </p>
	 * 
	 * @param quad
	 *            <p>
	 *            The Quad that is being visited.
	 *            </p>
	 */
	public void visit(Quad quad);

	/**
	 * <p>
	 * The visit operation for a {@link Hex}, a Polygon that is restricted to
	 * six sides.
	 * </p>
	 * 
	 * @param hex
	 *            <p>
	 *            The Hex that is being visited.
	 *            </p>
	 */
	public void visit(Hex hex);

	/**
	 * <p>
	 * The visit operation for an {@link Edge}.
	 * </p>
	 * 
	 * @param edge
	 *            <p>
	 *            The Edge that is being visited.
	 *            </p>
	 */
	public void visit(Edge edge);

	/**
	 * <p>
	 * The visit operation for a {@link BezierEdge}.
	 * </p>
	 * 
	 * @param edge
	 *            <p>
	 *            The BezierEdge that is being visited.
	 *            </p>
	 */
	public void visit(BezierEdge edge);

	/**
	 * <p>
	 * The visit operation for a {@link PolynomialEdge}.
	 * </p>
	 * 
	 * @param edge
	 *            <p>
	 *            The PolynomialEdge that is being visited.
	 *            </p>
	 */
	public void visit(PolynomialEdge edge);

	/**
	 * <p>
	 * The visit operation for a {@link Vertex}.
	 * </p>
	 * 
	 * @param vertex
	 *            <p>
	 *            The Vertex that is being visited.
	 *            </p>
	 */
	public void visit(Vertex vertex);

	/**
	 * <p>
	 * This operation is a safety operation. This should only be called if the
	 * visited object is not supported.
	 * </p>
	 * 
	 * @param object
	 *            <p>
	 *            A visited object that is not supported by this interface.
	 *            </p>
	 */
	public void visit(Object object);
}
