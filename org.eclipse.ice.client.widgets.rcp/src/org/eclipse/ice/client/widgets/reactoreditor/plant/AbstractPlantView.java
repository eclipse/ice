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
// import org.eclipse.ice.viz.service.jme3.widgets.AbstractView;
//
// import com.jme3.material.Material;
// import com.jme3.math.ColorRGBA;
// import com.jme3.scene.Geometry;
//
/// **
// * This class provides a base implementation for Plant-level jME3 views, like
/// a
// * Pipe or a Reactor.<br>
// * <br>
// * <b>Operations in this class (not including the constructor) should be
/// called
// * from a SimpleApplication's simpleUpdate() thread.</b>
// *
// * @author Jordan H. Deyton
// *
// */
// public abstract class AbstractPlantView extends AbstractView {
//
// /**
// * The Geometry representing this view in the scene. This may correspond to
// * a cylinder, sphere, or other custom mesh.
// */
// protected final Geometry geometry;
//
// /**
// * The default constructor. It constructs a new {@link #geometry} for the
// * view.
// *
// * @param name
// * The name of the view's root node.
// * @param material
// * The jME3 Material that should be used for the view's geometry.
// * Must not be null.
// */
// public AbstractPlantView(String name, Material material) {
// super(name);
//
// // Create the Geometry with the same name as the view's root node.
// geometry = new Geometry(viewNode.getName());
// viewNode.attachChild(geometry);
//
// // If possible, set the material for the geometry.
// if (material != null) {
// geometry.setMaterial(material);
// }
//
// return;
// }
//
// /**
// * Configure's the view to be rendered with a wire frame or not.
// *
// * @param wireFrame
// * Whether or not to render the view as a wire frame.
// */
// public void setWireFrame(boolean wireFrame) {
//
// // If possible, get the geometry and its material. Set the material to
// // either use or not use the wire frame.
// Material material = geometry.getMaterial();
// if (material != null) {
// material.getAdditionalRenderState().setWireframe(wireFrame);
// }
// return;
// }
//
// /**
// * Sets the base or primary color used when rendering the controller's view.
// *
// * @param color
// * The new base color of the view.
// */
// public void setBaseColor(ColorRGBA color) {
//
// // If possible, get the geometry and its material. Set the base color of
// // the material.
// Material material = geometry.getMaterial();
// if (material != null) {
// material.setColor("color", color);
// }
// return;
// }
//
// }
