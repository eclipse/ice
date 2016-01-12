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
// import java.util.concurrent.Callable;
// import java.util.concurrent.atomic.AtomicBoolean;
//
// import org.eclipse.ice.client.widgets.jme.AbstractController;
// import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
// import org.eclipse.ice.viz.service.jme3.application.IRenderQueue;
//
// import com.jme3.math.ColorRGBA;
//
/// **
// * This class provides a base implementation for a controller that manages a
// * model for a Plant-level component and synchronizes an
// * {@link AbstractPlantView} with any changes to the model or features
/// provided
// * by this controller.<br>
// * <br>
// * The features exposed by this class should be similar to the features
/// provided
// * for each plant component in Peacock.
// *
// * @author Jordan H. Deyton
// *
// */
// public abstract class AbstractPlantController extends AbstractController {
//
// /**
// * The {@link AbstractPlantView} associated with this controller.
// */
// private final AbstractPlantView view;
//
// // ---- Additional Features ---- //
// /**
// * Whether or not to render the view with a wire frame.
// */
// private final AtomicBoolean wireFrame;
//
// /**
// * The base color used with the view.
// */
// protected ColorRGBA baseColor;
// // ----------------------------- //
//
// /**
// * The default constructor.
// *
// * @param model
// * The model for which this controller provides a view.
// * @param view
// * The view associated with this controller. This needs to be
// * instantiated by the sub-class.
// * @param renderQueue
// * The queue responsible for tasks that need to be performed on
// * the jME rendering thread.
// */
// public AbstractPlantController(IUpdateable model, AbstractPlantView view,
// IRenderQueue renderQueue) {
// super(model, view, renderQueue);
//
// // Set the view. If it is null, create a new invalid view.
// this.view = (view != null ? view : new AbstractPlantView(
// "Invalid View", null) {
// });
//
// // ---- Initialize any additional features. ---- //
// wireFrame = new AtomicBoolean(false);
// baseColor = new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);
// // --------------------------------------------- //
//
// return;
// }
//
// /**
// * Configures the view to either be solid or render as a wire frame.
// *
// * @param wireFrame
// * Whether or not to use a wire frame.
// */
// public void setWireFrame(final boolean wireFrame) {
//
// // If the value has changed from the current value, update wireFrame and
// // update the view.
// if (this.wireFrame.compareAndSet(!wireFrame, wireFrame)) {
//
// // If the controller is not disposed, we should try to update the
// // view's render state.
// if (!disposed.get()) {
// renderQueue.enqueue(new Callable<Boolean>() {
// @Override
// public Boolean call() {
// view.setWireFrame(wireFrame);
// return true;
// }
// });
// }
// }
//
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
// // If the value has changed from the current value, update baseColor and
// // update the view.
// if (color != null && !color.equals(baseColor)) {
// baseColor = color;
//
// // If the controller is not disposed, we should try to update the
// // view's render state.
// if (!disposed.get()) {
// renderQueue.enqueue(new Callable<Boolean>() {
// @Override
// public Boolean call() {
// view.setBaseColor(baseColor);
// return true;
// }
// });
// }
// }
//
// return;
// }
// }
