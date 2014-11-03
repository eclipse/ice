/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets.reactoreditor.plant;

import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.Reactor;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

/**
 * This factory is used to create the basic {@link Material}s for
 * {@link PlantComponent}s. It also stores the keys for said Materials when
 * being stored in the PlantApplication.
 * 
 * @author djg
 * 
 */
public class PlantMaterialFactory {

	/**
	 * The PlantApplication that employs this factory.
	 */
	private final PlantApplication app;

	/**
	 * The default constructor.
	 * 
	 * @param application
	 *            The PlantApplication that employs this factory. This cannot be
	 *            changed.
	 */
	public PlantMaterialFactory(PlantApplication application) {
		app = (application != null ? application : new PlantApplication());
	}

	/**
	 * Get the Material key for a particular type of PlantComponent.
	 * 
	 * @param component
	 *            The PlantComponent that needs a Material.
	 * @return A key string if the component is supported, null otherwise.
	 */
	public String getKey(PlantComponent component) {

		// Use a wrapper to avoid having a class variable that requires thread
		// synchronization.
		final KeyWrapper wrapper = new KeyWrapper();

		// Visit the component with a visitor that sets the key string based on
		// the component type.
		if (component != null) {

			component.accept(new PlantControllerVisitor() {
				public void visit(Junction plantComp) {
					wrapper.key = "Junction";
				}

				public void visit(Reactor plantComp) {
					wrapper.key = "Reactor";
				}

				public void visit(HeatExchanger plantComp) {
					wrapper.key = "HeatExchanger";
				}

				public void visit(Pipe plantComp) {
					wrapper.key = "Pipe";
				}

				@Override
				public void visit(CoreChannel plantComp) {
					wrapper.key = "CoreChannel";
				}
			});
		}
		return wrapper.key;
	}

	/**
	 * Creates the materials for supported {@link PlantComponent}s and adds them
	 * to the {@link PlantApplication} associated with this factory.
	 */
	public void createMaterials() {
		app.addMaterial("Pipe", app.createLitMaterial(ColorRGBA.Cyan));
		app.addMaterial("Junction", app.createLitMaterial(ColorRGBA.Gray));
		app.addMaterial("HeatExchanger", app.createLitMaterial(ColorRGBA.Blue));
		app.addMaterial("Reactor", app.createLitMaterial(ColorRGBA.White));
		app.addMaterial("CoreChannel", app.createLitMaterial(ColorRGBA.Red));
	}

	/**
	 * A wrapper for an key strings. This avoids having to use a class variable
	 * for visit operations (which would require synchronization in
	 * multi-threaded environments).
	 * 
	 * @author djg
	 * 
	 */
	private class KeyWrapper {
		public String key = null;
	}

}
