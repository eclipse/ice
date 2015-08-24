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
package org.eclipse.ice.viz.service.mesh.datastructures;

/**
 * <p>
 * This interface is for any part of a {@link MeshComponent} that can be visited
 * by an {@link IMeshPartVisitor}.
 * </p>
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IMeshPart {

	/**
	 * <p>
	 * This method calls the {@link IMeshPartVisitor}'s visit method.
	 * </p>
	 * 
	 * @param visitor
	 *            <p>
	 *            The {@link IMeshPartVisitor} that is visiting this
	 *            {@link IMeshPart}.
	 *            </p>
	 */
	public void acceptMeshVisitor(IMeshPartVisitor visitor);

}
