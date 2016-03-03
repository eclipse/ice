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
package org.eclipse.eavp.viz.service.javafx.canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.jface.util.Geometry;

/**
 * <p>
 * Base for GeometryAttachment implementations, which provide nodes with the
 * ability to display ICE Geometry elements and their constituent IShapes.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public abstract class BasicAttachment extends Attachment
		implements IModelPart {

	/**
	 * Geometry that has been added but has not been integrated as the node
	 * hasn't been attached yet.
	 */
	private List<IController> queuedGeometry;

	/** List of shapes that have been added via Geometry instances. */
	private List<IController> shapes;

	/** */
	private boolean visible;

	/** */
	private boolean enabled;

	/** */
	private boolean immutable;

	/** */
	protected IController currentGeom = null;

	/**
	 * 
	 * @param node
	 */
	protected void checkNode(INode node) {
	}

	/**
	 * 
	 * @param shape
	 */
	protected void checkMesh(IController shape) {
	}

	/**
	 * @see IModelPart#addGeometry(Geometry)
	 */
	@Override
	public void addGeometry(IController geom) {
		if (geom == null) {
			return;
		}

		if (currentGeom == geom) {
			return;
		} else {
			currentGeom = geom;
		}

		if (owner == null) {
			if (queuedGeometry == null) {
				queuedGeometry = new ArrayList<>();
			}

			queuedGeometry.add(geom);

			return;
		}

	}

	/**
	 * @see IModelPart#addShape(Geometry)
	 */
	@Override
	public void addShape(IController shape) {
		checkMesh(shape);

		if (shapes == null) {
			shapes = new ArrayList<>();
		}

		shapes.add(shape);

		processShape(shape);
	}

	/**
	 * @see IAttachment#attach(INode)
	 */
	@Override
	public void attach(INode owner) {
		super.attach(owner);

		if (queuedGeometry == null) {
			return;
		}

		for (IController geom : queuedGeometry) {
			for (IController shape : geom.getEntities()) {
				addShape(shape);
			}
		}

		queuedGeometry.clear();
	}

	/**
	 * @see IAttachment#detach(INode)
	 */
	@Override
	public void detach(INode owner) {
		super.detach(owner);

		if (shapes != null) {
			shapes.clear();
		}

		if (queuedGeometry != null) {
			queuedGeometry.clear();
		}
	}

	/**
	 * <p>
	 * Handles generating renderer specific data from the input model shape.
	 * </p>
	 * 
	 * @param shape
	 *            ICE shape to visualize
	 */
	protected abstract void processShape(IController shape);

	/**
	 * @see IModelPart#addShape(Geometry)
	 */
	@Override
	public void removeShape(IController shape) {
		if (shapes == null) {
			return;
		}

		if (!shapes.contains(shape)) {
			return;
		}

		shapes.remove(shape);

		disposeShape(shape);
	}

	/**
	 * 
	 * @param shape
	 */
	protected abstract void disposeShape(IController shape);

	/**
	 * 
	 */
	@Override
	public boolean hasShape(IController shape) {
		if (shapes == null) {
			return false;
		}

		return shapes.contains(shape);
	}

	/**
	 * 
	 */
	@Override
	public IController getShape(int index) {
		if (shapes == null || shapes.size() < index) {
			return null;
		}

		return shapes.get(index);
	}

	/**
	 * 
	 * @param copy
	 * @return
	 */
	@Override
	public List<IController> getShapes(boolean copy) {
		if (shapes == null) {
			return Collections.emptyList();
		}

		if (copy) {
			return new ArrayList<IController>(shapes);
		} else {
			return shapes;
		}
	}

	/**
	 * 
	 */
	@Override
	public void clearShapes() {
		if (shapes == null) {
			return;
		}

		shapes.clear();
	}

	/**
	 */
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * 
	 */
	@Override
	public boolean isVisible() {
		return visible;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public boolean isImmutable() {
		return immutable;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public void setImmutable(boolean immutable) {
		this.immutable = immutable;
	}

}
