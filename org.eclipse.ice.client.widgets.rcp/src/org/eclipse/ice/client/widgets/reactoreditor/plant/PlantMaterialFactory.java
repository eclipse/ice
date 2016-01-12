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
// import org.eclipse.ice.reactor.plant.CoreChannel;
// import org.eclipse.ice.reactor.plant.HeatExchanger;
// import org.eclipse.ice.reactor.plant.Junction;
// import org.eclipse.ice.reactor.plant.Pipe;
// import org.eclipse.ice.reactor.plant.PlantComponent;
// import org.eclipse.ice.reactor.plant.Reactor;
//
// import com.jme3.material.Material;
// import com.jme3.math.ColorRGBA;
//
/// **
// * This factory is used to create the basic {@link Material}s for
// * {@link PlantComponent}s. It also stores the keys for said Materials when
// * being stored in the PlantAppState.
// *
// * @author Jordan H. Deyton
// *
// */
// public class PlantMaterialFactory {
//
// /**
// * The PlantAppState that employs this factory.
// */
// private final PlantAppState app;
//
// /**
// * The default constructor.
// *
// * @param app
// * The PlantAppState that employs this factory. This cannot be
// * changed.
// */
// public PlantMaterialFactory(PlantAppState app) {
// this.app = (app != null ? app : new PlantAppState());
// }
//
// /**
// * Get the Material key for a particular type of PlantComponent.
// *
// * @param component
// * The PlantComponent that needs a Material.
// * @return A key string if the component is supported, null otherwise.
// */
// public String getKey(PlantComponent component) {
//
// // Use a wrapper to avoid having a class variable that requires thread
// // synchronization.
// final KeyWrapper wrapper = new KeyWrapper();
//
// // Visit the component with a visitor that sets the key string based on
// // the component type.
// if (component != null) {
//
// component.accept(new PlantControllerVisitor() {
// @Override
// public void visit(Junction plantComp) {
// wrapper.key = "Junction";
// }
//
// @Override
// public void visit(Reactor plantComp) {
// wrapper.key = "Reactor";
// }
//
// @Override
// public void visit(HeatExchanger plantComp) {
// wrapper.key = "HeatExchanger";
// }
//
// @Override
// public void visit(Pipe plantComp) {
// wrapper.key = "Pipe";
// }
//
// @Override
// public void visit(CoreChannel plantComp) {
// wrapper.key = "CoreChannel";
// }
// });
// }
// return wrapper.key;
// }
//
// /**
// * Creates the materials for supported {@link PlantComponent}s and adds them
// * to the {@link PlantAppState} associated with this factory.
// */
// public void createMaterials() {
// app.setMaterial("Pipe", app.createLitMaterial(ColorRGBA.Cyan));
// app.setMaterial("Junction", app.createLitMaterial(ColorRGBA.Gray));
// app.setMaterial("HeatExchanger", app.createLitMaterial(ColorRGBA.Blue));
// app.setMaterial("Reactor", app.createLitMaterial(ColorRGBA.White));
// app.setMaterial("CoreChannel", app.createLitMaterial(ColorRGBA.Red));
// }
//
// /**
// * Disposes of all materials for supported {@link PlantComponent}s and
// * removes them from the associated <code>PlantAppState</code>.
// */
// public void disposeMaterials() {
// app.removeMaterial("Pipe");
// app.removeMaterial("Junction");
// app.removeMaterial("HeatExchanger");
// app.removeMaterial("Reactor");
// app.removeMaterial("CoreChannel");
// }
//
// /**
// * A wrapper for an key strings. This avoids having to use a class variable
// * for visit operations (which would require synchronization in
// * multi-threaded environments).
// *
// * @author Jordan H. Deyton
// *
// */
// private class KeyWrapper {
// public String key = null;
// }
//
// }
