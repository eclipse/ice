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
package org.eclipse.eavp.viz.service.javafx.internal.scene.camera;

/**
 * CameraControllers manage an ICamera, handling the process of moving the
 * camera and registering with the scene to detect user interaction.
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public interface ICameraController {

	/**
	 * Change the camera's pitch, its rotation about the axis perpendicular to
	 * its heading to the right, controlling how it is pointed in an up/down
	 * direction.
	 * 
	 * @param radians
	 *            The number of radians by which to rotate the camera.
	 */
	public void pitchCamera(double radians);

	/**
	 * Set the default angle for the camera. This will be used when reset() is
	 * called.
	 * 
	 * @param x
	 *            The number of degrees of X axis rotation.
	 * @param y
	 *            The number of degrees of Y axis rotation.
	 * @param z
	 *            The number of degrees of Z axis rotation.
	 */
	public void setDefaultAngle(double x, double y, double z);

	/**
	 * Change the camera's roll, it's rotation about the axis it is pointing.
	 * 
	 * @param radians
	 *            The number of radians by which to rotate the camera.
	 */
	public void rollCamera(double radians);

	/**
	 * Move the camera to the up or down, orthogonal to the direction it is
	 * pointing.
	 * 
	 * @param distance
	 *            The amount of space to move the camera. Positive distances
	 *            move the camera up, negative distances move it down.
	 */
	public void raiseCamera(double distance);

	/**
	 * Move the camera to the left or right, orthogonal to the direction it is
	 * pointing.
	 * 
	 * @param distance
	 *            The amount of space to move the camera. Positive distances
	 *            move the camera right, negative distances move it left.
	 */
	public void strafeCamera(double distance);

	/**
	 * Move the camera forward or backwards in the direction it is pointing.
	 * 
	 * @param distance
	 *            The amount of space to move the camera. Positive distances
	 *            move the camera forward, negative distances move it backwards.
	 */
	public void thrustCamera(double distance);

	/**
	 * Change the camera's yaw, its rotation about the axis perpendicular to its
	 * heading in to the above, controlling how it is pointed in a left/right
	 * direction.
	 * 
	 * @param radians
	 *            The number of radians by which to rotate the camera.
	 */
	public void yawCamera(double radians);

	/**
	 * Resets the camera to its default position.
	 */
	public void reset();

}
