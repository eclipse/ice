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

import org.eclipse.eavp.viz.service.javafx.scene.base.ICamera;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.eclipse.eavp.viz.service.javafx.viewer.IRenderer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

import javafx.embed.swt.FXCanvas;
import javafx.scene.Group;

/**
 * <p>
 * JFace Viewer base abstract implementation.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public abstract class AbstractViewer extends Viewer {

	/** The parent the viewer is created on. */
	protected final Composite parent;

	/** The current input model node. */
	protected INode input;

	/** The active scene selection. */
	private ISelection selection;

	/** The active renderer. */
	protected IRenderer renderer;

	/** The active camera. */
	protected ICamera activeCamera;

	/** The root JavaFX widget that displays content. */
	protected FXCanvas fxCanvas;

	/**
	 * <p>
	 * Creates a new GeometryViewer.
	 * </p>
	 *
	 * @param parent
	 *            the parent widget to create the viewer in
	 */
	public AbstractViewer(Composite parent) {
		this.parent = parent;

		createControl(parent);
	}

	/**
	 * <p>
	 * Returns the renderer for this viewer.
	 * </p>
	 * 
	 * @return the renderer for this viewer.
	 */
	public IRenderer getRenderer() {
		return renderer;
	}

	/**
	 * <p>
	 * Creates the control that backs this viewer, to be implemented by
	 * subclasses.
	 * </p>
	 * 
	 * @param parent
	 *            the parent widget that owns the viewer control.
	 */
	protected abstract void createControl(Composite parent);

	/**
	 * <p>
	 * Returns the active input scene model object.
	 * </p>
	 * 
	 * @see Viewer#getInput()
	 */
	@Override
	public Object getInput() {
		return input;
	}

	/**
	 * <p>
	 * Sets the active scene, supports either INode or IScene.
	 * </p>
	 */
	@Override
	public void setInput(Object newInput) {
		if (newInput == input) {
			return;
		}

		if (newInput == null) {
			newInput = null;
		} else if (!ModelUtil.isNode(newInput)) {
			throw new IllegalArgumentException(
					Messages.GeometryViewer_InvalidInput);
		}

		Object oldInput = input;
		this.input = (INode) newInput;

		// Fire the inputChanged event
		inputChanged(oldInput, this.input);
	}

	/**
	 * <p>
	 * Returns the currently active selection in the scene.
	 * </p>
	 */
	@Override
	public ISelection getSelection() {
		return selection;
	}

	/**
	 * <p>
	 * Sets the selection in the scene.
	 * </p>
	 */
	@Override
	public void setSelection(ISelection selection, boolean makeVisible) {
		this.selection = selection;

		fireSelectionChanged(new SelectionChangedEvent(this, selection));
	}

	/**
	 * <p>
	 * Sets the active camera displayed by the viewer.
	 * </p>
	 * 
	 * @param camera
	 *            the camera to use to display the viewer
	 */
	public void setCamera(ICamera camera) {
		this.activeCamera = camera;

		updateCamera(camera);
	}

	/**
	 * <p>
	 * Updates the active camera.
	 * </p>
	 * 
	 * @param camera
	 *            the camera to use in the scene.
	 */
	protected abstract void updateCamera(ICamera camera);

	/**
	 * <p>
	 * Return the active camera.
	 * </p>
	 * 
	 * @return the active camera
	 */
	public ICamera getCamera() {
		return this.activeCamera;
	}

	/**
	 * Getter method for the canvas in which the viewer is drawing its data.
	 * 
	 * @return The FXCanvas displaying the viewer
	 */
	public FXCanvas getCanvas() {
		return fxCanvas;
	}

	/**
	 * Gets the topmost node in the scene graph.
	 * 
	 * @return The scene graph's root node
	 */
	public abstract Group getRoot();

}
