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
 * The default list of IProperties used by IMeshes. It is not intended that
 * every IMesh instance have every one of these properties defined, nor that
 * they all make sense for all subclasses of IMesh. Rather, these define the
 * possible properties in a canonical way so as to standardize them across the
 * code base.
 * 
 * @author Robert Smith
 *
 */
public enum MeshProperty implements IMeshProperty {

	/**
	 * A property holding text which describes the part in a hunman readable
	 * way.
	 */
	DESCRIPTION,

	/**
	 * The numerical ID that identifies this part.
	 */
	ID,

	/**
	 * A numerical property defining an inner radius for a part, such as a tube
	 * or torus.
	 */
	INNER_RADIUS,

	/**
	 * A numerical property defining the part's length.
	 */
	LENGTH,

	/**
	 * The part's name.
	 */
	NAME,

	/**
	 * A numerical property defining the part's radius.
	 */
	RADIUS,

	/**
	 * A property which controls how much detail should be used in displaying
	 * the part.
	 */
	RESOLUTION,

	/**
	 * A property defining whether or not the part is the root of its tree.
	 * Values should be "True" or "False."
	 */
	ROOT,

	/**
	 * A property that defines the "type" of the part in some way appropriate to
	 * that kind of part. (ie A ShapeMesh might have a type of "Cube" or
	 * "Sphere".)
	 */
	TYPE,

	/**
	 * Whether or not the part is currently selected. Values should be "True" or
	 * "False".
	 */
	SELECTED

}
