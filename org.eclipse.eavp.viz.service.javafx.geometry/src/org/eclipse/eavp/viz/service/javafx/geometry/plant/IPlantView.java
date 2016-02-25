/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.geometry.plant;

import org.eclipse.swt.widgets.Composite;

/**
 * An interface for classes which create the graphical representations for Plant
 * Views and maintain their current state.
 * 
 * @author Jordan Deyton, Robert Smith
 *
 */
public interface IPlantView {

	/**
	 * Creates a new SWT {@link Composite} with this object's associated Plant
	 * View embedded within it.
	 *
	 * @param parent
	 *            The parent <code>Composite</code>.
	 * @return The <code>Composite</code> that has an embedded visualization
	 *         managed by this <code>PlantState</code>. This
	 *         <code>Composite</code>'s layout should be set by the caller.
	 *         <b>This <code>Composite</code> should be disposed when it is no
	 *         longer required</b>.
	 */
	public Composite createComposite(Composite parent);

	/**
	 * Dispose the plant view and its resources.
	 */
	public void dispose();

	/**
	 * Exports the view to an image file. The user is prompted for the image
	 * location.
	 */
	public void exportImage();

	/**
	 * Resets the plant view's camera to its default position and orientation.
	 * 
	 * @see #setDefaultCameraPosition(Vector3f)
	 * @see #setDefaultCameraOrientation(Vector3f, Vector3f)
	 */
	public void resetCamera();

	/**
	 * Sets the camera to face the origin with the Y axis horizontal and the Z
	 * axis vertical. This will also be the new default position reset to when
	 * case resetCamera() is called.
	 */
	public void setDefaultCameraYByZ();

	/**
	 * Sets the camera to face the origin with the X axis horizontal and the Y
	 * axis vertical. This will also be the new default position reset to when
	 * case resetCamera() is called.
	 */
	public void setDefaultCameraXByY();

	/**
	 * Sets the camera to face the origin with the Z axis horizontal and the X
	 * axis vertical. This will also be the new default position reset to when
	 * case resetCamera() is called.
	 */
	public void setDefaultCameraZByX();

	/**
	 * Set the IPlantData object from which the view will render.
	 * 
	 * @param plant
	 *            The new data source for the view.
	 */
	public void setPlant(IPlantData plant);

	/**
	 * Sets all rendered plant components to be viewed as wireframes or as solid
	 * objects.
	 * 
	 * @param wireframe
	 *            If true, plant components will be rendered with wireframes. If
	 *            false, they will be rendered solid.
	 */
	public void setWireframe(boolean wireframe);

	/**
	 * Moves the camera forward or backward.
	 * 
	 * @param distance
	 *            If positive, the camera moves forward. If negative, the camera
	 *            moves backward.
	 */
	public void thrustCamera(float distance);

	/**
	 * Moves the camera right or left.
	 * 
	 * @param distance
	 *            If positive, the camera moves right. If negative, the camera
	 *            moves left.
	 */
	public void strafeCamera(float distance);

	/**
	 * Moves the camera up or down.
	 * 
	 * @param distance
	 *            If positive, the camera moves up. If negative, the camera
	 *            moves down.
	 */
	public void raiseCamera(float distance);

	/**
	 * Rotates (rolls) the camera right or left.
	 * 
	 * @param radians
	 *            If positive, the camera rolls right. If negative, the camera
	 *            rolls left.
	 */
	public void rollCamera(float radians);

	/**
	 * Changes the pitch of the camera (rotates up and down).
	 * 
	 * @param radians
	 *            If positive, the camera pitches up. If negative, the camera
	 *            pitches down.
	 */
	public void pitchCamera(float radians);

	/**
	 * Changes the yaw of the camera right or left.
	 * 
	 * @param radians
	 *            If positive, the camera rotates right. If negative, the camera
	 *            rotates left.
	 */
	public void yawCamera(float radians);

}
