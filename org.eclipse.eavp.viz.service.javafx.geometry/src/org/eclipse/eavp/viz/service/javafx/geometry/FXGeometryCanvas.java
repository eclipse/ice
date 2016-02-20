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
package org.eclipse.eavp.viz.service.javafx.geometry;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.eavp.viz.service.javafx.canvas.AbstractAttachment;
import org.eclipse.eavp.viz.service.javafx.canvas.AbstractViewer;
import org.eclipse.eavp.viz.service.javafx.canvas.FXSelection;
import org.eclipse.eavp.viz.service.javafx.canvas.FXViewer;
import org.eclipse.eavp.viz.service.javafx.canvas.FXVizCanvas;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.IPlantData;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.IPlantView;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.IWireFramePart;
import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.eavp.viz.service.geometry.widgets.TransformationView;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

/**
 * An extension for FXVizCanvas that implements functionality for the Geometry
 * Editor.
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public class FXGeometryCanvas extends FXVizCanvas implements IPlantView {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(FXGeometryCanvas.class);

	/**
	 * The default constructor
	 * 
	 * @param source
	 *            The shape under which all parts in the model will be contained
	 *            as children
	 */
	public FXGeometryCanvas(AbstractController source) {
		super(source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.javafx.internal.FXVizCanvas#createAttachment(
	 * )
	 */
	@Override
	protected void createAttachment() {
		rootAtachment = (AbstractAttachment) viewer.getRenderer()
				.createAttachment(FXGeometryAttachment.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.FXVizCanvas#
	 * handleSelectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	protected void handleSelectionChanged(SelectionChangedEvent event) {
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(TransformationView.ID);

		if (view == null || !(view instanceof TransformationView)) {
			return;
		}

		TransformationView transformView = (TransformationView) view;

		FXSelection selection = (FXSelection) event.getSelection();

		transformView.setShape((ShapeController) selection.getShape());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.javafx.internal.FXVizCanvas#materializeViewer
	 * (org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected AbstractViewer materializeViewer(Composite viewerParent)
			throws Exception {

		// Create a geometry viewer, throwing an exception if the operation
		// fails
		return new FXGeometryViewer(viewerParent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#createComposite
	 * (org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite createComposite(Composite parent) {
		try {
			return draw(parent);
		} catch (Exception e) {
			logger.error(
					"JavaFX Geometry Canvas could not instantiate FXGeometryViewer.");
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#exportImage()
	 */
	@Override
	public void exportImage() {

		// Take a snapshot of the current scene
		WritableImage screenshot = viewer.getCanvas().getScene().snapshot(null);

		// Make the array of strings needed to pass to the file dialog.
		String[] extensionStrings = new String[] { ".png" };

		// Create the file save dialog.
		FileDialog fileDialog = new FileDialog(
				Display.getCurrent().getActiveShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(extensionStrings);
		fileDialog.setOverwrite(true);

		// Open the dialog and, if the user inputs a path, write the image to
		// the file
		String path = fileDialog.open();
		if (path != null) {
			File file = new File(path);
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(screenshot, null), "png",
						file);
			} catch (IOException e) {
				logger.error(
						"JavaFX Geometry Canvas encountered an error while "
								+ "attempting to write screenshot to file.");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#resetCamera()
	 */
	@Override
	public void resetCamera() {
		((FXViewer) viewer).resetCamera();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#setWireframe(
	 * boolean)
	 */
	@Override
	public void setWireframe(boolean wireframe) {

		// Set all objects in the tree to wireframe mode
		for (AbstractController entity : root.getEntities()) {
			recursiveSetWireframe(entity, wireframe);
		}
	}

	/**
	 * Set the target object and all of its descendants to display in wireframe
	 * mode or fill mode.
	 * 
	 * @param target
	 *            The object whose descendants (self included) will have their
	 *            modes set.
	 * @param wireframe
	 *            If true, the parts will be set to wireframe mode. Otherwise,
	 *            they will be removed from wireframe mode.
	 */
	private void recursiveSetWireframe(AbstractController target,
			boolean wireframe) {

		// Set this object to the correct mode
		((IWireFramePart) target).setWireFrameMode(wireframe);

		// Iterate over each of its children, setting them to the correct mode
		for (AbstractController child : target
				.getEntitiesByCategory("Children")) {
			((IWireFramePart) child).setWireFrameMode(wireframe);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#thrustCamera(
	 * float)
	 */
	@Override
	public void thrustCamera(float distance) {
		((FXViewer) viewer).thrustCamera(distance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#strafeCamera(
	 * float)
	 */
	@Override
	public void strafeCamera(float distance) {
		((FXViewer) viewer).strafeCamera(distance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#raiseCamera(
	 * float)
	 */
	@Override
	public void raiseCamera(float distance) {
		((FXViewer) viewer).raiseCamera(distance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#rollCamera(
	 * float)
	 */
	@Override
	public void rollCamera(float radians) {
		((FXViewer) viewer).rollCamera(radians);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#pitchCamera(
	 * float)
	 */
	@Override
	public void pitchCamera(float radians) {
		((FXViewer) viewer).pitchCamera(radians);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#yawCamera(
	 * float)
	 */
	@Override
	public void yawCamera(float radians) {
		((FXViewer) viewer).yawCamera(radians);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#
	 * setDefaultCameraYByZ()
	 */
	@Override
	public void setDefaultCameraYByZ() {
		((FXViewer) viewer).setDefaultCameraYByZ();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#
	 * setDefaultCameraXByY()
	 */
	@Override
	public void setDefaultCameraXByY() {
		((FXViewer) viewer).setDefaultCameraXByY();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.geometry.plantView.IPlantView#
	 * setDefaultCameraZByX()
	 */
	@Override
	public void setDefaultCameraZByX() {
		((FXViewer) viewer).setDefaultCameraZByX();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.reactor.plant.IPlantView#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantView#setPlant(org.eclipse.ice.reactor
	 * .plant.PlantComposite)
	 */
	@Override
	public void setPlant(IPlantData plant) {

		// Convert the plant composite into an AbstractController
		AbstractController newRoot = plant.getPlant();

		// Remove everything from the root to ensure it will be
		for (AbstractController entity : root.getEntities()) {
			root.removeEntity(entity);
		}

		root = newRoot;
		loadPart(root);

	}
}
