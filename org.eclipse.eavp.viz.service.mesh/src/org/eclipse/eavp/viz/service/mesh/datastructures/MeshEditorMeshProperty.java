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
package org.eclipse.eavp.viz.service.mesh.datastructures;

import org.eclipse.eavp.viz.service.modeling.IMeshProperty;

/**
 * Additional properties for parts from the Mesh Editor
 * 
 * @author Robert Smith
 *
 */
public enum MeshEditorMeshProperty implements IMeshProperty {

	/**
	 * A property which marks a part as currently being created and not yet
	 * finalized. Expected values are "True" and "False".
	 */
	UNDER_CONSTRUCTION
}
