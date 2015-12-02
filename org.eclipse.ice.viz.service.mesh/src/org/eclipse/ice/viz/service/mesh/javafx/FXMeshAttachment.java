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
package org.eclipse.ice.viz.service.mesh.javafx;

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
import org.eclipse.ice.viz.service.modeling.Face;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * <p>
 * JavaFX implementation of GeometryAttachment.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public class FXMeshAttachment extends GeometryAttachment {

	/**
	 * Node used to attach geometry to (instead of the root, to keep things
	 * easier to manipulate).
	 */
	private Group fxAttachmentNode;

	/** The manager that owns this attachment. */
	private final FXMeshAttachmentManager manager;

	/** */
	private List<AbstractController> knownGeometry;

	/**
	 * A box displayed behind the rest of the scene.
	 */
	private Box background;

	/**
	 * <p>
	 * Creates an FXGeometryAttachment instance.
	 * </p>
	 * 
	 * @param manager
	 *            the manager that created this instance.
	 */
	public FXMeshAttachment(FXMeshAttachmentManager manager) {
		this.manager = manager;

		// Create a grey background box
		background = new Box(96, 48, 1);
		PhongMaterial backgroundMaterial = new PhongMaterial();
		backgroundMaterial.setDiffuseColor(Color.GRAY);
		background.setMaterial(backgroundMaterial);

		// Move the background back slightly so it will not overlap with the
		// objects on the z plane
		// background.setTranslateZ(-1);
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

		// Add the background
		fxAttachmentNode.getChildren().add(background);

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

			fxAttachmentNode.getChildren().clear();

			geom.register(new IVizUpdateableListener() {
				@Override
				public void update(IVizUpdateable component) {

					javafx.application.Platform.runLater(new Runnable() {
						@Override
						public void run() {

							// On update, refresh the scene
							refresh();
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
	public void processShape(AbstractController shape) {
		// Nothing to do.
	}

	/**
	 * 
	 */
	@Override
	protected void disposeShape(AbstractController shape) {
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
	public List<AbstractController> getShapes(boolean copy) {
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
	public FXMeshAttachmentManager getManager() {
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

	/**
	 * Returns the root AbstractController under which all permanent parts are
	 * children.
	 * 
	 * @return The AbstractController which is an ancestor to all permanent
	 *         parts for this attachment's editor.
	 */
	public AbstractController getRoot() {
		return knownGeometry.get(0);
	}

	/**
	 * Redraw all shapes in the scene.
	 */
	private void refresh() {

		// On update, refresh the list of top level nodes
		fxAttachmentNode.getChildren().clear();

		// For each group which has been added to the attachment
		for (AbstractController group : knownGeometry) {

			// Get each part which is managed by that controller
			for (AbstractController entity : group.getEntities()) {

				// Add each child of a polygon to the scene, without repeats
				if (entity instanceof Face) {

					for (AbstractController child : entity.getEntities()) {

						Group render = (Group) child.getRepresentation();

						if (!fxAttachmentNode.getChildren().contains(render)) {
							// Add the representation to the scene's node
							fxAttachmentNode.getChildren().add(render);
						}
					}
				} else {

					// Simply add the representations of other objects
					fxAttachmentNode.getChildren()
							.add((Group) entity.getRepresentation());
				}
			}
		}

		// Add the background
		fxAttachmentNode.getChildren().add(background);
	}

}
