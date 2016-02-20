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
package org.eclipse.eavp.viz.service.javafx.canvas;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.javafx.internal.Util;
import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.eclipse.eavp.viz.service.javafx.viewer.IAttachmentManager;
import org.eclipse.eavp.viz.service.modeling.AbstractController;

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
public class FXAttachment extends AbstractAttachment {

	/**
	 * Node used to attach geometry to (instead of the root, to keep things
	 * easier to manipulate).
	 */
	protected Group fxAttachmentNode;

	/** The manager that owns this attachment. */
	private final IAttachmentManager manager;

	/** */
	protected List<AbstractController> knownParts;

	/**
	 * <p>
	 * Creates an FXGeometryAttachment instance.
	 * </p>
	 * 
	 * @param manager
	 *            the manager that created this instance.
	 */
	public FXAttachment(IAttachmentManager manager) {
		this.manager = manager;
	}

	/**
	 * A function invoked when the attachment receives an update from its
	 * contained modeling parts. This function does nothing by default and is
	 * intended to be implemented by subclasses
	 * 
	 * @param source
	 *            The controller which triggered the update
	 */
	protected void handleUpdate(AbstractController source) {
		// Nothing to do
	}

	/**
	 * Get the list of all modeling parts which have been added to this
	 * attachment.
	 * 
	 * @return All parts contained in this attachment
	 */
	public List<AbstractController> getKnownParts() {
		return knownParts;
	}

	/**
	 * @see AbstractAttachment#attach(INode)
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
	 * @see IModelPart#setVisible(boolean)
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

		if (knownParts == null) {
			knownParts = new ArrayList<>();
		}

		if (!knownParts.contains(geom)) {

			geom.register(new IManagedUpdateableListener() {
				@Override
				public void update(IManagedUpdateable component,
						SubscriptionType[] type) {

					javafx.application.Platform.runLater(new Runnable() {
						@Override
						public void run() {

							// Invoke the update function
							handleUpdate(geom);
						}
					});
				}

				@Override
				public ArrayList<SubscriptionType> getSubscriptions(
						IManagedUpdateable source) {

					// Register to receive all updates
					ArrayList<SubscriptionType> types = new ArrayList<SubscriptionType>();
					types.add(SubscriptionType.ALL);
					return types;
				}
			});

			// Have the geometry refreshed when it is added
			handleUpdate(geom);

			knownParts.add(geom);
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
	public IAttachmentManager getManager() {
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
		return AbstractAttachment.class;
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

	@Override
	public void removeGeometry(AbstractController geom) {

		// Remove the part from the list of seen parts
		knownParts.remove(geom);

	}

}
