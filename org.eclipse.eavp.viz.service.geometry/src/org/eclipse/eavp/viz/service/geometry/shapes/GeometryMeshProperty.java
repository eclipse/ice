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
package org.eclipse.eavp.viz.service.geometry.shapes;

import org.eclipse.eavp.viz.service.modeling.IMeshProperty;

/**
 * Addional IMesh properties for the geometry editor's classes.
 * 
 * @author Robert Smith
 *
 */
public enum GeometryMeshProperty implements IMeshProperty {

	/**
	 * A property defining the operator used to combine a node's children
	 * according to the rules of constructive solid geometry. (ie "Union",
	 * "Intersection", etc)
	 */
	OPERATOR
}
