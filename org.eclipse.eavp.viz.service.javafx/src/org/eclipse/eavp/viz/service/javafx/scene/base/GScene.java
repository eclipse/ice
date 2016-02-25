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
package org.eclipse.eavp.viz.service.javafx.scene.base;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.eclipse.eavp.viz.service.javafx.scene.model.IScene;
import org.eclipse.eavp.viz.service.modeling.ShapeController;

/**
 * <p>
 * Base impl for IScene.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public abstract class GScene implements IScene {

	/** */
	private INode root;

	/** */
	private Map<String, ICamera> cameras;

	/** */
	private Map<Integer, INode> nodes;

	/** */
	private List<ShapeController> geometry;

	/**
	 * @see IScene#getRoot()
	 */
	@Override
	public INode getRoot() {
		return root;
	}

	/**
	 * @see IScene#getCameras()
	 */
	@Override
	public Map<String, ICamera> getCameras() {
		if (cameras == null) {
			return Collections.emptyMap();
		}

		return cameras;
	}

	/**
	 * @see IScene#getNodes()
	 */
	@Override
	public Map<Integer, INode> getNodes() {
		if (nodes == null) {
			return Collections.emptyMap();
		}

		return nodes;
	}

	/**
	 * @see IScene#getGeometry()
	 */
	@Override
	public List<ShapeController> getGeometry() {
		if (geometry == null) {
			return Collections.emptyList();
		}

		return geometry;
	}

	public void setRoot(INode root) {
		this.root = root;
	}

}
