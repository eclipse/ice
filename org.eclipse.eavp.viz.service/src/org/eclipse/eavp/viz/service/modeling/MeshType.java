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
package org.eclipse.eavp.viz.service.modeling;

/**
 * An enumeration of the types of meshes usable for modeling.
 * 
 * @author Robert Smith
 *
 */
public enum MeshType {

	/**
	 * A mesh which defines many three dimensional cells.
	 */
	VOLUMETRIC,
	
	/**
	 * A mesh specified by some combination of vertices, edges, and/or faces in three dimensional space.
	 */
	SIMPLE,
	
	/**
	 * A mesh of a specific design, parameterized through the mesh's properties.
	 */
	CUSTOM_PART,
	
	/**
	 * A mesh represented by a constructive solid geometry tree.
	 */
	CONSTRUCTIVE;
}
