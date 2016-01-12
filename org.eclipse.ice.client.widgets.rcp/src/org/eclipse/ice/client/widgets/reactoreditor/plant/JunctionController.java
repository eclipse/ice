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
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Set;
// import java.util.TreeSet;
// import java.util.concurrent.Callable;
// import java.util.concurrent.atomic.AtomicBoolean;
//
// import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
// import org.eclipse.ice.reactor.plant.HeatExchanger;
// import org.eclipse.ice.reactor.plant.IJunction;
// import org.eclipse.ice.reactor.plant.IJunctionListener;
// import org.eclipse.ice.reactor.plant.Junction;
// import org.eclipse.ice.reactor.plant.Pipe;
// import org.eclipse.ice.reactor.plant.PlantComponent;
// import org.eclipse.ice.reactor.plant.Reactor;
// import org.eclipse.ice.viz.service.jme3.application.IRenderQueue;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import com.jme3.bounding.BoundingBox;
// import com.jme3.math.Vector3f;
//
/// **
// * This class provides a controller for Junctions and links the {@link
/// Junction}
// * model with the {@link JunctionView}. Any updates to the view should be
// * coordinated through this class.
// *
// * @author Jordan H. Deyton
// *
// */
// public class JunctionController extends AbstractPlantController implements
// IJunctionListener, IPlantControllerManagerListener {
//
// /**
// * Logger for handling event messages and other information.
// */
// private static final Logger logger = LoggerFactory
// .getLogger(JunctionController.class);
//
// /**
// * The {@link Junction} model for which this controller provides a view.
// */
// private final Junction model;
// /**
// * The {@link JunctionView} associated with this controller.
// */
// private final JunctionView view;
//
// // ---- Additional Features ---- //
// // TODO Any features that are not a part of the model but are configurable
// // in Peacock should go here.
// // ----------------------------- //
//
// // ---- Model/View synchronization ---- //
// // This class listens to the Junction model as an IUpdateableListener (see
// // AbstractController). The update method from the interface only notifies
// // that the Junction has somehow changed, so we need a way to update the
// // view based on the current pipes without just recomputing all of the
// // bounding boxes. We use these two maps below to contain the current
// // input and output pipes that are displayed in the view.
//
// /**
// * The manager of all controllers for the applications. When the pipes
// * attached to this junction change, we will need to get jME3 bounds for
// * input and output pipes from their controllers. We should query the
// * controller manager to get references to the pipe controllers.
// */
// private final PlantControllerManager controllers;
// // ------------------------------------ //
//
// // TODO Make these structures more thread safe.
// private final HashMap<Integer, PipeInfo> pipes;
// private final Set<Integer> secondaryPipes = new TreeSet<Integer>();
//
// private class PipeInfo {
// public final boolean input;
// public final PlantComponent pipe;
// public PipeController controller;
//
// public PipeInfo(boolean input, PlantComponent pipe,
// PipeController controller) {
// this.input = input;
// this.pipe = pipe;
// this.controller = controller;
// }
// }
//
// /**
// * The default constructor.
// *
// * @param model
// * The model (a {@link Junction}) for which this controller
// * provides a view.
// * @param view
// * The view (a {@link JunctionView}) associated with this
// * controller.
// * @param renderQueue
// * The queue responsible for tasks that need to be performed on
// * the jME rendering thread.
// * @param manager
// * A {@link PlantControllerManager} used for looking up
// * {@link PipeController}s for the current {@link Pipe}s
// * connected to the Junction.
// */
// public JunctionController(Junction model, JunctionView view,
// IRenderQueue renderQueue, PlantControllerManager manager) {
// super(model, view, renderQueue);
//
// // Set the model. If it is null, create a new, default model.
// this.model = (model != null ? model : new Junction());
//
// // Set the view. If it is null, create a new, default view.
// this.view = (view != null ? view : new JunctionView("Invalid View",
// null));
//
// // Set the controller manager used to look up PipeControllers.
// this.controllers = (manager != null ? manager
// : new PlantControllerManager());
//
// // ---- Initialize any additional features. ---- //
// pipes = new HashMap<Integer, PipeInfo>();
// // --------------------------------------------- //
//
// // If any of the arguments were invalid, we should throw an exception
// // now after all class variables have been initialized.
// if (model == null) {
// throw new IllegalArgumentException(
// "JunctionController error: Model is null!");
// } else if (view == null) {
// throw new IllegalArgumentException(
// "JunctionController error: View is null!");
// } else if (renderQueue == null) {
// throw new IllegalArgumentException(
// "JunctionController error: Update queue is null!");
// } else if (controllers == null) {
// throw new IllegalArgumentException(
// "JunctionController error: Controller manager is null!");
// }
//
// // The view should not be attached to the scene yet, so we should be
// // able to synchronize it with the current model.
//
// // Register with the Junction model as an IJunctionListener and with the
// // controller manager as an IPlantControllerManagerListener
// this.model.registerJunctionListener(this);
// this.controllers.registerListener(this);
//
// // ---- Synchronize the initial view with the model. ---- //
// // Create the lists for the primary pipe IDs and BoundingBoxes.
// List<Integer> ids = new ArrayList<Integer>();
// List<BoundingBox> boxes = new ArrayList<BoundingBox>();
//
// // Add all possible pipes from the model's input and output and store
// // any new pipe IDs and BoundingBoxes in the two lists above.
// addPipes(model.getInputs(), model.getOutputs(), ids, boxes);
//
// // Update the view with the primary connections.
// view.putPipes(ids, boxes);
// // Update the view with the secondary connections.
// view.setSecondaryPipes(getSecondaryBoundingBoxes());
// // Refresh the view's mesh.
// view.refreshMesh();
// // ------------------------------------------------------ //
//
// return;
// }
//
// /**
// * Updates the {@link #view} depending on the changes in the {@link #model}.
// * It also updates the view based on changes to pipes connected to this
// * junction.
// */
// @Override
// public void update(IUpdateable component) {
//
// // If the updated component is the junction, then some feature of the
// // junction changed. Adding/Removing pipes are handled in addedPipes()
// // and removedPipes().
// if (component == model && !disposed.get()) {
// // Nothing to do yet...
// }
// // If the updated component is a pipe that this junction controller is
// // listening to, then we should send the pipe's current position to the
// // view.
// else if (!disposed.get()) {
//
// // Get the associated PipeInfo for the component's ID.
// final int id = component.getId();
// final PipeInfo info = pipes.get(id);
// if (info != null) {
//
// final AtomicBoolean boundsChanged = new AtomicBoolean(false);
//
// PlantControllerVisitor visitor = new PlantControllerVisitor() {
// // Junctions do not connect Junctions or Reactors.
// @Override
// public void visit(Junction plantComp) {
// }
//
// @Override
// public void visit(Reactor plantComp) {
// }
//
// @Override
// public void visit(HeatExchanger plantComp) {
// // If a secondary pipe was updated and changing it
// // changed the bounds of the junction, we should update
// // the view's mesh.
// if (view.setSecondaryPipes(getSecondaryBoundingBoxes())) {
// boundsChanged.set(true);
// }
// }
//
// @Override
// public void visit(Pipe plantComp) {
// // If we updated a primary pipe and the bounds change,
// // we need to update the secondary pipes.
//
// // Create the lists to pass to the view.
// List<Integer> ids = new ArrayList<Integer>(1);
// List<BoundingBox> boxes = new ArrayList<BoundingBox>(1);
// ids.add(id);
// if (info.input) {
// boxes.add(info.controller.getOutletBounds());
// } else {
// boxes.add(info.controller.getInletBounds());
// }
// if (view.putPipes(ids, boxes)) {
// boundsChanged.set(true);
// view.setSecondaryPipes(getSecondaryBoundingBoxes());
// }
// }
// };
// info.pipe.accept(visitor);
//
// // If the bounds have changed, synchronize the view's geometry
// // with the mesh.
// if (boundsChanged.get()) {
// renderQueue.enqueue(new Callable<Boolean>() {
// @Override
// public Boolean call() {
// view.refreshMesh();
// return true;
// }
// });
// }
// }
// }
//
// return;
// }
//
// // ---- Implements IJunctionListener ---- //
// /*
// * (non-Javadoc)
// *
// * @see
// * org.eclipse.ice.reactor.plant.IJunctionListener#addedPipes(org.eclipse
// * .ice .reactor.plant.IJunction, java.util.List)
// */
// @Override
// public void addedPipes(IJunction junction, List<PlantComponent> addedPipes) {
// // This method needs to add PipeInfos for the added Pipes and update the
// // view based on the available controllers for the added pipes. It also
// // needs to differentiate between the primary and secondary pipes.
//
// if (junction == model && addedPipes != null && !disposed.get()) {
//
// // Get the input and output pipes from the list of added pipes.
// List<PlantComponent> input = new ArrayList<PlantComponent>();
// List<PlantComponent> output = new ArrayList<PlantComponent>();
// for (PlantComponent pipe : addedPipes) {
// if (model.isInput(pipe)) {
// input.add(pipe);
// } else {
// output.add(pipe);
// }
// }
//
// // Create the lists for the primary pipe IDs and BoundingBoxes.
// List<Integer> ids = new ArrayList<Integer>();
// List<BoundingBox> boxes = new ArrayList<BoundingBox>();
//
// // Add all possible pipes from the model's input and output and
// // store any new pipe IDs and BoundingBoxes in the two lists above.
// addPipes(input, output, ids, boxes);
//
// boolean boundsChanged = false;
//
// // If we added primary pipes, then update the view.
// if (!ids.isEmpty()) {
// boundsChanged = view.putPipes(ids, boxes);
// // If the bounds changed, then we also need to update the
// // secondary pipes to point to the center of the current view.
// if (boundsChanged) {
// view.setSecondaryPipes(getSecondaryBoundingBoxes());
// }
// }
// // If we added secondary pipes, update the view. Mark the bounds as
// // changed if they changed.
// else if (view.setSecondaryPipes(getSecondaryBoundingBoxes())) {
// boundsChanged = true;
// }
//
// // If the bounds have changed, synchronize the view's geometry with
// // the mesh.
// if (boundsChanged) {
// renderQueue.enqueue(new Callable<Boolean>() {
// @Override
// public Boolean call() {
// view.refreshMesh();
// return true;
// }
// });
// }
// }
//
// return;
// }
//
// /*
// * (non-Javadoc)
// *
// * @see
// * org.eclipse.ice.reactor.plant.IJunctionListener#removedPipes(org.eclipse
// * .ice .reactor.plant.IJunction, java.util.List)
// */
// @Override
// public void removedPipes(IJunction junction,
// List<PlantComponent> removedPipes) {
// // This method needs to remove the PipeInfos associated with the removed
// // pipes and update the view as necessary. It also needs to
// // differentiate between the primary and secondary pipes.
//
// if (junction == model && removedPipes != null && !disposed.get()) {
//
// // Create a list to hold IDs for removed primary pipes.
// final List<Integer> ids = new ArrayList<Integer>();
//
// // Create a visitor to add primary pipe IDs to the list of IDs to
// // remove and remove secondary pipe IDs from the set of secondary
// // pipe IDs.
// PlantControllerVisitor visitor = new PlantControllerVisitor() {
// // Junctions do not connect Junctions or Reactors.
// @Override
// public void visit(Junction plantComp) {
// }
//
// @Override
// public void visit(Reactor plantComp) {
// }
//
// @Override
// public void visit(HeatExchanger plantComp) {
// secondaryPipes.remove(plantComp.getId());
// }
//
// @Override
// public void visit(Pipe plantComp) {
// ids.add(plantComp.getId());
// }
// };
//
// // Loop over the removed pipe list. If the pipe is known to the
// // controller, remove it.
// for (PlantComponent pipe : removedPipes) {
// int id = pipe.getId();
// PipeInfo info = pipes.get(id);
//
// if (info != null && pipe == info.pipe) {
// // Unregister from the pipe and remove its PipeInfo.
// pipe.unregister(this);
// pipes.remove(id);
//
// // Visit the pipe to update this controller appropriately.
// pipe.accept(visitor);
// }
// }
//
// boolean boundsChanged = false;
//
// // If we removed primary pipes, then update the view.
// if (!ids.isEmpty()) {
// boundsChanged = view.removePipes(ids);
// // If the bounds changed, then we also need to update the
// // secondary pipes to point to the center of the current view.
// if (boundsChanged) {
// view.setSecondaryPipes(getSecondaryBoundingBoxes());
// }
// }
// // If we removed secondary pipes, update the view. Mark the bounds
// // as changed if they changed.
// else if (view.setSecondaryPipes(getSecondaryBoundingBoxes())) {
// boundsChanged = true;
// }
//
// // If the bounds have changed, synchronize the view's geometry with
// // the mesh.
// if (boundsChanged) {
// renderQueue.enqueue(new Callable<Boolean>() {
// @Override
// public Boolean call() {
// view.refreshMesh();
// return true;
// }
// });
// }
// }
//
// return;
// }
//
// // -------------------------------------- //
//
// /**
// * Sends the {@link #view}'s current center to the {@link PipeController}s
// * for secondary pipes. Once all secondary pipe controllers have re-routed
// * to the new center, their inlet/outlet BoundingBoxes are returned.
// *
// * @return A list of BoundingBoxes for all available secondary pipes that
// * have been connected to the center of the Junction's view.
// */
// private List<BoundingBox> getSecondaryBoundingBoxes() {
// List<BoundingBox> boxes = new ArrayList<BoundingBox>();
//
// Vector3f center = view.getCenter();
//
// // Loop over all secondary pipes and send the view's center to their
// // controllers. Add the resulting inlet/outlet box to the list of boxes.
// for (int id : secondaryPipes) {
// PipeInfo info = pipes.get(id);
// if (info.controller != null) {
// BoundingBox box;
// if (info.input) {
// box = info.controller.setSecondaryOutletPosition(center);
// } else {
// box = info.controller.setSecondaryInletPosition(center);
// }
// boxes.add(box);
// }
// }
//
// return boxes;
// }
//
// /**
// * Adds {@link Pipe}s and {@link HeatExchanger}s to {@link #pipes} and
// * {@link #secondaryPipes} as necessary. <b>This method requires a list of
// * Integers and a list of BoundingBoxes.</b> These will hold any added
// * primary pipe IDs and their input or output BoundingBoxes.
// *
// * @param input
// * A list of input Pipes and/or HeatExchangers.
// * @param output
// * A list of output Pipes and/or HeatExchangers.
// * @param ids
// * A list for storing the ID of any Pipe or HeatExchanger that is
// * added from the input or output list.
// * @param boxes
// * A list for storing the appropriate BoundingBox of any Pipe or
// * HeatExchanger that is added from the input or output list.
// */
// private void addPipes(List<PlantComponent> input,
// List<PlantComponent> output, final List<Integer> ids,
// final List<BoundingBox> boxes) {
//
// // Create a visitor for adding Pipes and HeatExchangers to the inputs.
// PlantControllerVisitor visitor = new PlantControllerVisitor() {
// // Junctions do not connect Junctions or Reactors.
// @Override
// public void visit(Junction plantComp) {
// }
//
// @Override
// public void visit(Reactor plantComp) {
// }
//
// @Override
// public void visit(HeatExchanger plantComp) {
// // Get the ID and controller for the component.
// int id = plantComp.getId();
// HeatExchangerController controller = (HeatExchangerController) controllers
// .getController(plantComp);
//
// // Store the component with its own PipeInfo.
// pipes.put(id, new PipeInfo(true, plantComp, controller));
// // Connected HeatExchangers are assumed to be connected to their
// // secondary pipes. Add their IDs to the secondary ID set.
// secondaryPipes.add(id);
//
// // Register with the component.
// plantComp.register(JunctionController.this);
// }
//
// @Override
// public void visit(Pipe plantComp) {
// // Get the ID and controller for the component.
// int id = plantComp.getId();
// PipeController controller = (PipeController) controllers
// .getController(plantComp);
//
// // Store the component with its own PipeInfo.
// pipes.put(id, new PipeInfo(true, plantComp, controller));
//
// // Get the BoundingBox.
// BoundingBox box = null;
// if (controller != null) {
// box = controller.getOutletBounds();
// }
//
// // Add the primary outlet to the ID and BoundingBox lists.
// ids.add(plantComp.getId());
// boxes.add(box);
//
// // Register with the component.
// plantComp.register(JunctionController.this);
// }
// };
// // Set up all valid inputs with this controller.
// for (PlantComponent pipe : input) {
// if (pipe != null && !pipes.containsKey(pipe.getId())) {
// pipe.accept(visitor);
// }
// }
//
// // Create a visitor for adding Pipes and HeatExchangers to the outputs.
// visitor = new PlantControllerVisitor() {
// // Junctions do not connect Junctions or Reactors.
// @Override
// public void visit(Junction plantComp) {
// }
//
// @Override
// public void visit(Reactor plantComp) {
// }
//
// @Override
// public void visit(HeatExchanger plantComp) {
// // Get the ID and controller for the component.
// int id = plantComp.getId();
// HeatExchangerController controller = (HeatExchangerController) controllers
// .getController(plantComp);
//
// // Store the component with its own PipeInfo.
// pipes.put(id, new PipeInfo(false, plantComp, controller));
// // Connected HeatExchangers are assumed to be connected to their
// // secondary pipes. Add their IDs to the secondary ID set.
// secondaryPipes.add(id);
//
// // Register with the component.
// plantComp.register(JunctionController.this);
// }
//
// @Override
// public void visit(Pipe plantComp) {
// // Get the ID and controller for the component.
// int id = plantComp.getId();
// PipeController controller = (PipeController) controllers
// .getController(plantComp);
//
// // Store the component with its own PipeInfo.
// pipes.put(id, new PipeInfo(false, plantComp, controller));
//
// // Get the BoundingBox.
// BoundingBox box = null;
// if (controller != null) {
// box = controller.getInletBounds();
// }
// // Add the primary outlet to the ID and BoundingBox lists.
// ids.add(plantComp.getId());
// boxes.add(box);
//
// // Register with the component.
// plantComp.register(JunctionController.this);
// }
// };
// // Set up all valid inputs with this controller.
// for (PlantComponent pipe : output) {
// if (pipe != null && !pipes.containsKey(pipe.getId())) {
// pipe.accept(visitor);
// }
// }
// return;
// }
//
// // ---- Implements IPlantControllerManagerListener ---- //
// /*
// * (non-Javadoc)
// *
// * @see org.eclipse.ice.client.widgets.reactoreditor.plant.
// * IPlantControllerManagerListener
// * #addedController(org.eclipse.ice.reactor.plant.PlantComponent,
// * org.eclipse
// * .ice.client.widgets.reactoreditor.plant.AbstractPlantController)
// */
// @Override
// public void addedController(PlantComponent component,
// AbstractPlantController controller) {
// // This method needs to add the controller to its component's PipeInfo.
// // It should also use the controller's inlet or outlet bounds to update
// // the JunctionView as necessary.
//
// if (component != null && controller != null) {
// final int id = component.getId();
// final PipeInfo info = pipes.get(id);
// final ComponentWrapper wrapper = new ComponentWrapper();
// final AtomicBoolean boundsChanged = new AtomicBoolean(false);
//
// // If the component matches one of the model's pipes, update the
// // associated PipeController.
// if (info != null) {
//
// // Get the component stored in the PipeInfo map. This is
// // necessary for HeatExchangers, whose primary pipes are stored
// // in the map instead of the HeatExchanger itself.
// PlantControllerVisitor visitor = new PlantControllerVisitor() {
// // Junctions do not connect Junctions or Reactors.
// @Override
// public void visit(Junction plantComp) {
// }
//
// @Override
// public void visit(Reactor plantComp) {
// }
//
// @Override
// public void visit(HeatExchanger plantComp) {
// if (info.pipe == plantComp) {
// logger.info("HeatExchanger found");
// wrapper.component = plantComp;
// } else if (info.pipe == plantComp.getPrimaryPipe()) {
// logger.info("HeatExchanger primary pipe found");
// wrapper.component = plantComp.getPrimaryPipe();
// }
// }
//
// @Override
// public void visit(Pipe plantComp) {
// if (info.pipe == plantComp) {
// wrapper.component = plantComp;
// }
// }
// };
// component.accept(visitor);
// }
//
// // If the wrapped component is not null, then we have the Pipe or
// // HeatExchanger whose controller will need to be updated.
// if (wrapper.component != null) {
// // Set the info's controller.
// info.controller = (PipeController) controller;
//
// // Create a visitor to update the Junction view's bounds data as
// // is necessary. We only handle Pipes and HeatExchangers.
// PlantControllerVisitor visitor = new PlantControllerVisitor() {
// // Junctions do not connect Junctions or Reactors.
// @Override
// public void visit(Junction plantComp) {
// }
//
// @Override
// public void visit(Reactor plantComp) {
// }
//
// @Override
// public void visit(HeatExchanger plantComp) {
// // HeatExchangers are considered secondary connections.
// // Update the secondary connections for the view.
// List<BoundingBox> boxes = getSecondaryBoundingBoxes();
// boundsChanged.set(view.setSecondaryPipes(boxes));
// }
//
// @Override
// public void visit(Pipe plantComp) {
// // Pipes are considered primary connections.
// List<Integer> ids = new ArrayList<Integer>(1);
// List<BoundingBox> boxes = new ArrayList<BoundingBox>(1);
//
// // Add the ID and the input or output BoundingBox.
// ids.add(id);
// if (info.input) {
// boxes.add(info.controller.getOutletBounds());
// } else {
// boxes.add(info.controller.getInletBounds());
// }
//
// // Send the ID and BoundingBox to the view and set
// // boundsChanged as necessary. If the bounds change, we
// // also need to update the view's secondary connections.
// if (view.putPipes(ids, boxes)) {
// view.setSecondaryPipes(getSecondaryBoundingBoxes());
// boundsChanged.set(true);
// }
//
// return;
// }
// };
// wrapper.component.accept(visitor);
// }
//
// // If the bounds have changed, synchronize the view's geometry with
// // the mesh.
// if (boundsChanged.get()) {
// renderQueue.enqueue(new Callable<Boolean>() {
// @Override
// public Boolean call() {
// view.refreshMesh();
// return true;
// }
// });
// }
// }
//
// return;
// }
//
// /*
// * (non-Javadoc)
// *
// * @see org.eclipse.ice.client.widgets.reactoreditor.plant.
// * IPlantControllerManagerListener
// * #removedController(org.eclipse.ice.reactor.plant.PlantComponent)
// */
// @Override
// public void removedController(PlantComponent component) {
// // This method needs to get the component's PipeInfo and set its
// // controller to be null. It should also update the view by telling it
// // to remove the BoundingBox corresponding to the pipe.
//
// // Make sure the argument is not null.
// if (component != null) {
// // Look up the component's info. If we have a controller set for the
// // component, we need to update the info.
// PipeInfo info = pipes.get(component.getId());
// if (info != null && component == info.pipe
// && info.controller != null) {
//
// // Create a boolean that a visitor can use to determine if the
// // view's mesh needs to be synchronized with its spatial.
// final AtomicBoolean boundsChanged = new AtomicBoolean(false);
//
// // Create a visitor to update the Junction view's bounds data as
// // is necessary. We only handle Pipes and HeatExchangers.
// PlantControllerVisitor visitor = new PlantControllerVisitor() {
// // Junctions do not connect Junctions or Reactors.
// @Override
// public void visit(Junction plantComp) {
// }
//
// @Override
// public void visit(Reactor plantComp) {
// }
//
// @Override
// public void visit(HeatExchanger plantComp) {
// // HeatExchangers are considered secondary connections.
// // Update the secondary connections for the view.
// boolean changed = view
// .setSecondaryPipes(getSecondaryBoundingBoxes());
// boundsChanged.set(changed);
// }
//
// @Override
// public void visit(Pipe plantComp) {
// // Pipes are considered primary connections.
// List<Integer> ids = new ArrayList<Integer>(1);
// ids.add(plantComp.getId());
//
// // Remove the pipe's BoundingBox from the view. If the
// // bounds change, we also need to update the view's
// // secondary connections.
// if (view.removePipes(ids)) {
// view.setSecondaryPipes(getSecondaryBoundingBoxes());
// boundsChanged.set(true);
// }
// }
// };
// component.accept(visitor);
//
// // Remove the controller from the component's info.
// info.controller = null;
//
// // If the bounds have changed, synchronize the view's geometry
// // with the mesh.
// if (boundsChanged.get()) {
// renderQueue.enqueue(new Callable<Boolean>() {
// @Override
// public Boolean call() {
// view.refreshMesh();
// return true;
// }
// });
// }
// }
// }
//
// return;
// }
//
// // ---------------------------------------------------- //
//
// private class ComponentWrapper {
// public PlantComponent component = null;
// }
// }
