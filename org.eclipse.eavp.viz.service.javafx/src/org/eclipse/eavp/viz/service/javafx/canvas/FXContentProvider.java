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

import org.eclipse.eavp.viz.service.javafx.internal.FXSceneGenerator;
import org.eclipse.eavp.viz.service.javafx.internal.Messages;
import org.eclipse.eavp.viz.service.javafx.scene.base.ISceneGenerator;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.Viewer;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 * <p>
 * Handles parsing the scene model input (INode, etc) and generates an internal
 * JavaFX model that can be visualized in the viewer.
 * </p>
 *
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class FXContentProvider implements IContentProvider {

	/** Used to generate the JavaFX scene from the input model. */
	private ISceneGenerator sceneGenerator;

	/**
	 * <p>
	 * Generates a JavaFX scene from the input model.
	 * </p>
	 * 
	 * @see IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object input, Object oldInput) {
		verifyInput(viewer, input, oldInput);

		FXViewer fxViewer = (FXViewer) viewer;

		INode newRoot = (INode) input;

		Group fxRoot = fxViewer.getRoot();

		if (sceneGenerator == null) {
			sceneGenerator = new FXSceneGenerator();
		}

		Node generatedRoot = sceneGenerator.generateScene(newRoot);

		fxRoot.getChildren().setAll(generatedRoot);
	}

	/**
	 * <p>
	 * Verifies the model inputs for sanity.
	 * </p>
	 * 
	 * @param viewer
	 *            the viewer being modified
	 * @param input
	 *            the new input
	 * @param oldInput
	 *            the old input
	 */
	private void verifyInput(Viewer viewer, Object input, Object oldInput) {
		if (!(viewer instanceof FXViewer)) {
			throw new IllegalArgumentException(
					Messages.FXContentProvider_InvalidViewerType);
		}

		if (!(input instanceof INode)) {
			throw new IllegalArgumentException(
					Messages.FXContentProvider_InvalidInputType);
		}

		if (oldInput != null && !(oldInput instanceof INode)) {
			throw new IllegalArgumentException(
					Messages.FXContentProvider_InvalidInputNode);
		}
	}

	/**
	 * <p>
	 * Disposes the Viewer input.
	 * </p>
	 */
	@Override
	public void dispose() {
	}

	/**
	 * 
	 */
	public ISceneGenerator getGenerator() {
		return sceneGenerator;
	}

	/**
	 *
	 */
	public void setGenerator(ISceneGenerator generator) {
		this.sceneGenerator = generator;
	}

}
