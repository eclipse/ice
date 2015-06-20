/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.client.widgets.jme;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * This class provides a base implementation for a view managed by an
 * {@link AbstractController}. This class essentially provides a root
 * {@link Node} to contain jME3 scene objects and operations to add or remove
 * the root node to or from the scene.<br>
 * <br>
 * <b>Operations in this class (not including the constructor) should be called
 * from a SimpleApplication's simpleUpdate() thread.</b>
 * 
 * @author Jordan Deyton
 * 
 */
public abstract class AbstractView {

	/**
	 * The Node containing all jME3 scene objects associated with this view.
	 */
	protected final Node viewNode;

	/**
	 * The default constructor.
	 * 
	 * @param name
	 *            The name of the AbstractView's node, {@link #viewNode}. If
	 *            null, "AbstractView" is used instead.
	 */
	public AbstractView(String name) {

		// Create the root node for the view with the specified name.
		viewNode = new Node((name != null ? name : "AbstractView"));

		return;
	}

	/**
	 * Attaches the view's node to the specified parent node.
	 * 
	 * @param node
	 *            The new parent node of the view. If null, nothing is changed.
	 */
	public void setParentNode(Node node) {

		// Attach the view's node to the new parent node.
		if (node != null) {
			node.attachChild(viewNode);
		}
		return;
	}

	/**
	 * Sets the location of the view.
	 * 
	 * @param location
	 *            The new location of the view in jME3 coordinates.
	 */
	public void setLocation(Vector3f location) {

		// If the location is valid, set the view node's location.
		if (location != null) {
			viewNode.setLocalTranslation(location);
		}
		return;
	}

	/**
	 * Sets the rotation of the view.
	 * 
	 * @param quaternion
	 *            The new rotation of the view in jME3 coordinates.
	 */
	public void setRotation(Quaternion quaternion) {

		// If the rotation is valid, set the view node's rotation.
		if (quaternion != null) {
			viewNode.setLocalRotation(quaternion);
		}
		return;
	}

	/**
	 * Disposes of the AbstractView's spatials attached to the jME3 scene.
	 */
	public void dispose() {

		// Remove the view node from its parent node if possible.
		Node parent = viewNode.getParent();
		if (parent != null) {
			parent.detachChild(viewNode);
		}
		return;
	}
}
