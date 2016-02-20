/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.scene.model;

import java.util.List;
import java.util.Map;

import org.eclipse.eavp.viz.service.javafx.scene.base.ICamera;
import org.eclipse.eavp.viz.service.modeling.ShapeController;

/**
 * <p>
 * Interface for a global scene representation. A scene has a root {@link INode}
 * and also provides other data for easy access (camera, geometry, nodes, etc.)
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public interface IScene {

	/**
	 * <p>
	 * Returns the scene's root node.
	 * </p>
	 * 
	 * @return an INode that is the top most node in the scene hierarchy.
	 */
	public INode getRoot();

	/**
	 * <p>
	 * Returns the scene's cameras.
	 * </p>
	 * 
	 * @return a Map of the scene's cameras
	 */
	public Map<String, ICamera> getCameras();

	/**
	 * <p>
	 * Returns a Map of all the INode instances in the scene.
	 * </p>
	 * 
	 * @return a Map of all the INode instances in the scene.
	 */
	public Map<Integer, INode> getNodes();

	/**
	 * <p>
	 * Returns a list of all the {@link Geometry} used in the scene.
	 * </p>
	 * 
	 * @return a List of all the geometry used in the scene.
	 */
	public List<ShapeController> getGeometry();

}
