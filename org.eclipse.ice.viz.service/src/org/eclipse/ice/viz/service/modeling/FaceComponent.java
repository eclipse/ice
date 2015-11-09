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
package org.eclipse.ice.viz.service.modeling;

import java.util.List;

/**
 * A mesh component representing a polygon.
 * 
 * @author Robert Smith
 *
 */
public class FaceComponent extends AbstractMeshComponent {

	/**
	 * The default constructore
	 */
	public FaceComponent() {
		super();
	}

	/**
	 * A constructor for specifying the child entities.
	 * 
	 * @param entities
	 *            The child entities comprising the face
	 */
	public FaceComponent(List<AbstractController> entities) {
		super(entities);
	}
}
