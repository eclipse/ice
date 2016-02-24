/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.modeling;

/**
 * The
 * 
 * @author Robert Smith
 *
 */
public enum MeshCategory implements IMeshCategory {

	/**
	 * A category for parts which are lower than the parent part in some
	 * hierarchy, such as a constructive solid geometry tree.
	 */
	CHILDREN,

	/**
	 * A catch-all category for entities which have been added without a
	 * specific category added.
	 */
	DEFAULT,

	/**
	 * A category for related edges.
	 */
	EDGES,

	/**
	 * A category for related faces.
	 */
	FACES,

	/**
	 * A category for a single part which is a direct superior in some
	 * hierarchy, such as a constructive solid geometry tree.
	 */
	PARENT,

	/**
	 * A category for related vertices.
	 */
	VERTICES
}
