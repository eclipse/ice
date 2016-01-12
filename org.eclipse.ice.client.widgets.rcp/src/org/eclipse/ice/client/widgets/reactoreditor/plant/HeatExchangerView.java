/// *******************************************************************************
// * Copyright (c) 2014 UT-Battelle, LLC.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// * Initial API and implementation and/or initial documentation - Jay Jay
/// Billings,
// * Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
// * Claire Saunders, Matthew Wang, Anna Wojtowicz
// *******************************************************************************/
// package org.eclipse.ice.client.widgets.reactoreditor.plant;
//
// import org.eclipse.ice.reactor.plant.HeatExchanger;
//
// import com.jme3.bounding.BoundingBox;
// import com.jme3.material.Material;
// import com.jme3.math.FastMath;
// import com.jme3.math.Quaternion;
// import com.jme3.math.Vector3f;
// import com.jme3.scene.Geometry;
// import com.jme3.scene.shape.Box;
//
/// **
// * This class provides a view for a {@link HeatExchanger}. This view provides
/// a
// * standard pipe as its primary pipe, but it also provides a {@link
/// #container}
// * around the pipe and secondary input and output pipes that extend from the
// * container to their respective junctions.<br>
// * <br>
// * <b>Operations in this class (not including the constructor) should be
/// called
// * from a SimpleApplication's simpleUpdate() thread.</b>
// *
// * @author Jordan H. Deyton
// *
// */
// public class HeatExchangerView extends PipeView {
//
// /**
// * The mesh for the container that envelops the primary pipe mesh.
// * Currently, this is a {@link Box} that extends 1/10th the length of the
// * pipe from the inlet to 1/10th the length of the pipe from the outlet.
// */
// private final Box container;
// /**
// * The {@link Geometry} that contains the {@link #container} mesh.
// */
// private final Geometry containerGeometry;
//
// /*
// * Both of the secondary pipes managed by this view are facing outward. That
// * is, their translation is set to a central point along the center of the
// * container. Their rotations are set to point at the center of their
// * attached junction. The pipe is long enough to connect the central point
// * in the container to the junction's center.
// *
// * Because their bottom vertices (inlet) are positioned within the
// * container, only their top vertices (outlets) will be used for the
// * secondary input and output bounds.
// *
// * For each of the secondary pipes, we need a Geometry to contain the mesh.
// * Translations and rotations should be applied to this Geometry.
// */
// // ---- Secondary Input ---- //
// /**
// * The tube mesh for the secondary input (inlet).
// */
// private final TubeMesh secondaryInput;
// /**
// * The {@link Geometry} that contains the {@link #secondaryInput} tube mesh.
// */
// private final Geometry secondaryInputGeometry;
// /**
// * The location of the center of the {@link #secondaryInput} mesh's inlet.
// */
// private final Vector3f secondaryInputOffset;
// /**
// * The orientation of the {@link #secondaryInput} pipe mesh.
// */
// private final Quaternion secondaryInputRotation;
// // ------------------------- //
//
// // ---- Secondary Output ---- //
// /**
// * The tube mesh for the secondary output (outlet).
// */
// private final TubeMesh secondaryOutput;
// /**
// * The {@link Geometry} that contains the {@link #secondaryOutput} tube
// * mesh.
// */
// private final Geometry secondaryOutputGeometry;
// /**
// * The location of the center of the {@link #secondaryOutput} mesh's inlet.
// */
// private final Vector3f secondaryOutputOffset;
// /**
// * The orientation of the {@link #secondaryOutput} pipe mesh.
// */
// private final Quaternion secondaryOutputRotation;
//
// // -------------------------- //
//
// /**
// * The default constructor. It creates a mesh for the view's
// * {@link AbstractPlantView#geometry}.
// *
// * @param name
// * The name of the view's root node.
// * @param material
// * The jME3 Material that should be used for the view's geometry.
// * Must not be null.
// */
// public HeatExchangerView(String name, Material material) {
// super(name, material);
//
// // Add the box containing the external fluids that cool the primary
// // input/output pipes.
// container = new Box(Vector3f.ZERO, Vector3f.UNIT_XYZ);
// containerGeometry = new Geometry("container", container);
// containerGeometry.setMaterial(material);
// viewNode.attachChild(containerGeometry);
//
// // Add the secondary input pipe.
// secondaryInputOffset = new Vector3f();
// secondaryInputRotation = new Quaternion();
// secondaryInput = new TubeMesh(defaultLength, defaultRadius,
// axialSamples, radialSamples);
// secondaryInputGeometry = new Geometry("secondaryInput", secondaryInput);
// secondaryInputGeometry.setMaterial(material);
// viewNode.attachChild(secondaryInputGeometry);
//
// // Add the secondary output pipe.
// secondaryOutputOffset = new Vector3f();
// secondaryOutputRotation = new Quaternion();
// secondaryOutput = new TubeMesh(defaultLength, defaultRadius,
// axialSamples, radialSamples);
// secondaryOutputGeometry = new Geometry("secondaryOutput",
// secondaryOutput);
// secondaryOutputGeometry.setMaterial(material);
// viewNode.attachChild(secondaryOutputGeometry);
//
// // If any of the arguments were invalid, we should throw an exception
// // now after all class variables have been initialized.
// if (material == null) {
// throw new IllegalArgumentException(
// "HeatExchangerView: Material is null!");
// }
// return;
// }
//
// /**
// * Overrides the default behavior, which returns the same vertices for
// * primary and secondary pipes, to return different vertices for the
// * secondary pipe.
// */
// @Override
// public Vector3f[] getBottomVertices(boolean primary) {
// // The array that we will be returning.
// Vector3f[] vertices = null;
//
// // For primary pipes, get the default value.
// if (primary) {
// vertices = super.getBottomVertices(true);
// }
// // For the secondary pipe, return the top edge of the secondary input
// // pipe.
// else {
// readLock.lock();
// try {
// // To get the proper coordinates with respect to the root view
// // node, we need to apply the secondary geometry offsets and its
// // node offsets.
// Vector3f[] source = secondaryInput.getTopEdgeVertices();
// vertices = new Vector3f[source.length];
//
// // Apply the rotation and location offsets to the list of
// // vertices.
// Vector3f tmp;
// for (int i = 0; i < source.length; i++) {
// tmp = new Vector3f(source[i]);
// secondaryInputRotation.multLocal(tmp);
// tmp.addLocal(secondaryInputOffset);
// vertices[i] = tmp;
// }
// } finally {
// readLock.unlock();
// }
// }
// return vertices;
// }
//
// /**
// * Overrides the default behavior, which returns the same vertices for
// * primary and secondary pipes, to return different vertices for the
// * secondary pipe.
// */
// @Override
// public Vector3f[] getTopVertices(boolean primary) {
// Vector3f[] vertices;
// if (primary) {
// vertices = super.getTopVertices(true);
// } else {
// readLock.lock();
// try {
// // To get the proper coordinates with respect to the root view
// // node, we need to apply the secondary geometry offsets and its
// // node offsets.
// Vector3f[] source = secondaryOutput.getTopEdgeVertices();
// vertices = new Vector3f[source.length];
//
// // Apply the rotation and location offsets to the list of
// // vertices.
// Vector3f tmp;
// for (int i = 0; i < source.length; i++) {
// tmp = new Vector3f(source[i]);
// secondaryOutputRotation.multLocal(tmp);
// tmp.addLocal(secondaryOutputOffset);
// vertices[i] = tmp;
// }
// } finally {
// readLock.unlock();
// }
// }
// return vertices;
// }
//
// /**
// * Refreshes the primary pipe based on the specified values. Also refreshes
// * the container for the heat transfer.
// */
// @Override
// public void updateMesh(float radius, float length, int axialSamples) {
//
// // Update the mesh for the primary pipe.
// super.updateMesh(radius, length, axialSamples);
//
// // Update the mesh for the container.
// float halfWidth = radius * 2f;
// float joinerLength = length * 0.1f;
//
// Vector3f min = new Vector3f(-halfWidth, joinerLength, -halfWidth);
// Vector3f max = new Vector3f(halfWidth, length * 0.9f, halfWidth);
//
// writeLock.lock();
// try {
// container.updateGeometry(min, max);
// } finally {
// writeLock.unlock();
// }
//
// return;
// }
//
// /**
// * Applies any changes to the underlying mesh to
// * {@link AbstractPlantView#geometry}. Augments the default behavior to
// * update the container that wraps the primary pipe.
// */
// @Override
// public void refreshMesh() {
// // Synchronize the primary pipe mesh and geometry.
// super.refreshMesh();
// // Synchronize the container mesh and geometry.
// containerGeometry.updateModelBound();
// }
//
// /**
// * Updates the secondary pipe's mesh with the required parameters. <b>The
// * center <i>must</i> have the inverses of this view's translation and
// * rotation applied before calling this method.</b>
// *
// * @param input
// * Whether the secondary pipe is the input pipe or the output
// * pipe.
// * @param radius
// * The radius of the secondary pipe.
// * @param center
// * The center of the junction <b>with the inverses of this view's
// * translation and rotation applied before calling this
// * method.</b>
// */
// public void updateSecondaryMesh(boolean input, float radius, Vector3f center)
/// {
// // ---- Get the tube mesh, translation, and rotation. ---- //
// // Computations done after this block do not depend on whether the
// // secondary pipe is input or output for the heat exchanger. Determine
// // which tube mesh, translation, and rotation will be modified.
// TubeMesh tube;
// Vector3f translation;
// Quaternion rotation;
// if (input) {
// tube = secondaryInput;
// translation = secondaryInputOffset;
// rotation = secondaryInputRotation;
// } else {
// tube = secondaryOutput;
// translation = secondaryOutputOffset;
// rotation = secondaryOutputRotation;
// }
// // ------------------------------------------------------- //
//
// // Get the bounds of the container mesh.
// BoundingBox containerBounds = null;
// readLock.lock();
// try {
// containerBounds = (BoundingBox) container.getBound();
// } finally {
// readLock.unlock();
// }
//
// // ---- Determine the pipe's source and length. ---- //
// // We need to draw the pipe from the center of the junction to the
// // central axis or vector of the heat exchanger. To do this,
// // we determine the point on the line segment (from the top of the
// // container to the bottom) closest to the center of the junction.
//
// // We have a point C and a line segment AB. To determine the nearest
// // point D on the line segment AB, we need to do the following:
// // Any point on AB can be defined by P(t) = A + t (B - A)
// // Compute the orientation n for AB. n = (B - A) normalized.
// // Compute t = ((C - A) dot n) / ||B - A||
// // t must be clamped to the range 0 <= t <= 1
// // Compute D = A + t (B - A)
// // Compute the vector between D and C to get the orientation (C - D)
// // Compute the length of the orientation vector to get the pipe length.
//
// // Set C, A, and B. To save on computations, compute B - A.
// Vector3f C = center;
// // A is the vector (location) for the bottom of the container.
// Vector3f A = new Vector3f(0f, containerBounds.getMin(null).y, 0f);
// // B is the vector (location) for the top of the container.
// Vector3f B = new Vector3f(0f, containerBounds.getMax(null).y, 0f);
// Vector3f BsubtractA = B.subtract(A);
//
// // Determine t and D.
// float t = FastMath.clamp(C.subtract(A).dot(BsubtractA.normalize())
// / BsubtractA.length(), 0f, 1f);
// Vector3f D = A.addLocal(BsubtractA.multLocal(t));
//
// // Set the pipe's location, orientation, and length.
// Vector3f location = D;
// Vector3f orientation = C.subtract(D);
// float length = orientation.length();
// // ------------------------------------------------- //
//
// // Update the pipe mesh, the translation, and the rotation.
// writeLock.lock();
// try {
// tube.setLength(length);
// tube.setRadius(radius);
// tube.refresh(false);
// translation.set(location);
// rotation.set(PipeController
// .getQuaternionFromOrientation(orientation));
// } finally {
// writeLock.unlock();
// }
//
// return;
// }
//
// /**
// * Applies any changes to the specified secondary pipe mesh to its geometry
// * and/or node.
// *
// * @param input
// * Whether the secondary pipe is the input pipe or the output
// * pipe.
// */
// public void refreshSecondaryMesh(boolean input) {
// if (input) {
// secondaryInputGeometry.setLocalTranslation(secondaryInputOffset);
// secondaryInputGeometry.setLocalRotation(secondaryInputRotation);
// secondaryInputGeometry.updateModelBound();
// } else {
// secondaryOutputGeometry.setLocalTranslation(secondaryOutputOffset);
// secondaryOutputGeometry.setLocalRotation(secondaryOutputRotation);
// secondaryOutputGeometry.updateModelBound();
// }
// return;
// }
//
// }
