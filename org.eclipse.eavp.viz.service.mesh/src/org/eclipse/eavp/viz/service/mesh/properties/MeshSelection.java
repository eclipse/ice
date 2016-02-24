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
package org.eclipse.eavp.viz.service.mesh.properties;

import org.eclipse.eavp.viz.service.modeling.IController;

public class MeshSelection {

	public final IController mesh;

	public final IController selectedMeshPart;

	public MeshSelection(IController mesh, IController selectedMeshPart) {
		this.mesh = mesh;
		this.selectedMeshPart = selectedMeshPart;
	}

}
