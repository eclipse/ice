/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.internal.model.geometry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.ice.viz.service.geometry.scene.base.GeometryAttachment;
import org.eclipse.ice.viz.service.geometry.scene.base.IGeometry;
import org.eclipse.ice.viz.service.geometry.scene.model.IAttachment;
import org.eclipse.ice.viz.service.geometry.scene.model.INode;
import org.eclipse.ice.viz.service.javafx.internal.Util;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.Shape;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 * <p>
 * JavaFX implementation of GeometryAttachment.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public class FXGeometryAttachment extends GeometryAttachment {

	/**
	 * Node used to attach geometry to (instead of the root, to keep things
	 * easier to manipulate).
	 */
	private Group fxAttachmentNode;

	/** The manager that owns this attachment. */
	private final FXGeometryAttachmentManager manager;

	/** */
	private List<AbstractController> knownGeometry;

	/**
	 * <p>
	 * Creates an FXGeometryAttachment instance.
	 * </p>
	 * 
	 * @param manager
	 *            the manager that created this instance.
	 */
	public FXGeometryAttachment(FXGeometryAttachmentManager manager) {
		this.manager = manager;
	}

	/**
	 * @see GeometryAttachment#attach(INode)
	 */
	@Override
	public void attach(INode owner) {
		super.attach(owner);

		if (fxAttachmentNode == null) {
			fxAttachmentNode = new Group();
		}

		Group fxNode = Util.getFxGroup(owner);
		fxNode.getChildren().add(fxAttachmentNode);
	}

	/**
	 * @see IAttachment#detach(INode)
	 */
	@Override
	public void detach(INode owner) {
		Group fxNode = Util.getFxGroup(owner);

		if (fxAttachmentNode != null) {
			fxNode.getChildren().remove(fxAttachmentNode);
		}

		super.detach(owner);
	}

	/**
	 * @see IAttachment#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return false;
	}

	/**
	 * @see IGeometry#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		fxAttachmentNode.setVisible(visible);
	}

	@Override
	public void addGeometry(AbstractController geom) {
		super.addGeometry(geom);

		if (fxAttachmentNode == null) {
			fxAttachmentNode = new Group();
		}

		if (knownGeometry == null) {
			knownGeometry = new ArrayList<>();
		}

		if (!knownGeometry.contains(geom)) {
			final AbstractController finalGeom = geom;

			geom.register(new IVizUpdateableListener() {
				@Override
				public void update(IVizUpdateable component) {

					javafx.application.Platform.runLater(new Runnable() {
						@Override
						public void run() {

							// On update, refresh the list of top level nodes
							fxAttachmentNode.getChildren().clear();

							for (AbstractController child : geom
									.getEntities()) {
								fxAttachmentNode.getChildren()
										.add((Group) child.getRepresentation());

							}
						}
					});
				}
			});

			knownGeometry.add(geom);
		}
	}

	/**
	 * <p>
	 * Generates JavaFX shapes from the input IShape.
	 * </p>
	 * 
	 * @param shape
	 *            an ICE Geometry IShape
	 */
	@Override
	public void processShape(Shape shape) {
		// Nothing to do.
	}

	/**
	 * 
	 */
	@Override
	protected void disposeShape(Shape shape) {
		Node node = (Group) shape.getRepresentation();

		if (node == null) {
			return;
		}

		fxAttachmentNode.getChildren().remove(node);
	}

	/**
	 *
	 * @param copy
	 * @return
	 */
	@Override
	public List<Shape> getShapes(boolean copy) {
		return super.getShapes(copy);

	}

	/**
	 *
	 */
	@Override
	public void clearShapes() {
		super.clearShapes();
	}

	/**
	 * 
	 * @return
	 */
	public FXGeometryAttachmentManager getManager() {
		return manager;
	}

	/**
	 *
	 * @return
	 */
	public javafx.scene.Node getFxParent() {
		return fxAttachmentNode;
	}

	/**
	 *
	 * @return
	 */
	public Group getFxNode() {
		return fxAttachmentNode;
	}

	/**
	 * 
	 */
	@Override
	public Class<?> getType() {
		return GeometryAttachment.class;
	}

	/**
	 * 
	 */
	public String getName() {
		if (fxAttachmentNode == null) {
			return "UNNAMED";
		}

		return fxAttachmentNode.getId();
	}

}
