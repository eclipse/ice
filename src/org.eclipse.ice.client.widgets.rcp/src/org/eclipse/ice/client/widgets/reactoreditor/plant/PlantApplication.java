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

import org.eclipse.ice.client.widgets.mesh.AbstractApplication;

import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.IPlantCompositeListener;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.reactor.plant.Reactor;

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.FlyCamAppState;
import com.jme3.input.FlyByCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

/**
 * This jME3-based SimpleApplication provides a 3D view of a reactor plant.
 * 
 * @author djg
 * 
 */
public class PlantApplication extends AbstractApplication implements
		IUpdateableListener, IPlantCompositeListener {

	public static void main(String[] args) {

		// FIXME - I've currently disabled the jME3 info output because it's
		// getting annoying to debug with all the red output.
		// Logger.getLogger("com.jme3").setLevel(Level.SEVERE);

		// Set the JME3 application settings (standard).
		AppSettings settings = new AppSettings(true);
		settings.setFrameRate(60);
		settings.setRenderer(AppSettings.LWJGL_OPENGL_ANY);
		settings.setResolution(1024, 768);

		PlantApplication app = new PlantApplication();
		app.setSettings(settings);
		app.setShowSettings(false);
		app.setPauseOnLostFocus(false);
		app.start();

		PlantComposite plant = new PlantComposite();

		Junction j1, j2;
		Pipe p1, p2;
		HeatExchanger h;

		p1 = new Pipe(3.0, 0.5);
		p1.setId(1);
		p1.setPosition(new double[] { 0, 0, 0 });
		p1.setOrientation(new double[] { 0, 1, 0 });

		p2 = new Pipe(5.0, 0.3);
		p2.setId(2);
		p2.setPosition(new double[] { 3.5, 7, -1 });
		p2.setOrientation(new double[] { 1, 1, 1 });

		h = new HeatExchanger();
		h.setId(3);
		h.setPosition(new double[] { 3, 0, -1 });
		h.setOrientation(new double[] { 0, 1, 0 });
		h.setLength(7);
		h.setInnerRadius(0.5);

		j1 = new Junction();
		j1.setId(4);
		j1.addInput(p1);
		j1.addOutput(h);

		j2 = new Junction();
		j2.setId(5);
		j2.addInput(h.getPrimaryPipe());
		j2.addOutput(p2);

		// Wait until the app has initialized.
		while (!app.isInitialized()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// Thread interrupted!
			}
		}

		// Set the plant.
		app.setPlant(plant);

		// Add several plant components.

		CoreChannel c1 = new CoreChannel();
		c1.setId(7);
		c1.setLength(5);
		c1.setPosition(new double[] { 5, 0, 0 });
		c1.setOrientation(new double[] { 0, 1, 0 });
		c1.setRadius(0.5f);
		plant.addPlantComponent(c1);

		Reactor r = new Reactor();
		r.setId(6);
		plant.addPlantComponent(r);

		ArrayList<CoreChannel> coreChannels = new ArrayList<CoreChannel>(3);
		coreChannels.add(c1);
		r.setCoreChannels(coreChannels);

		return;
	}

	// ---- Application Initialization ---- //
	// ------------------------------------ //

	// ---- Application Update/Synchronization ---- //
	// -------------------------------------------- //

	// ---- Materials ---- //
	/**
	 * The factory that is used to look up Materials for {@link PlantComponent}s
	 * in the PlantApplication.
	 */
	private final PlantMaterialFactory plantMaterialFactory;
	// ------------------- //

	// ---- Camera ---- //
	// ---------------- //

	// ---- Scene ---- //
	// TODO We need a root node for collidables. We should also consider using
	// separate AppStates and the application's stateManager to switch between
	// just browsing and manipulating the map.

	/**
	 * A manager for all {@link AbstractPlantController}s. This manager is used
	 * to create controllers and {@link AbstractPlantView}s for reactor plant
	 * objects.
	 */
	private final PlantControllerManager plantControllerManager;

	/**
	 * The Node that should contain all plant views/controllers.
	 */
	private final Node plantNode;
	// --------------- //

	// ---- Plant MVC ---- //
	/**
	 * The root PlantComposite that contains the pipes, junctions, reactors,
	 * etc.
	 */
	private PlantComposite plant;

	// ------------------- //

	/**
	 * The default constructor.
	 */
	public PlantApplication() {
		super(new FlyCamAppState());

		// ---- Application Initialization ---- //
		// ------------------------------------ //

		// ---- Application Update/Synchronization ---- //
		// -------------------------------------------- //

		// ---- Materials ---- //
		// Initialize the factory that is used to create materials. The
		// materials are created in initMaterials().
		plantMaterialFactory = new PlantMaterialFactory(this);
		// ------------------- //

		// ---- Camera ---- //
		// ---------------- //

		// ---- Scene ---- //
		// Create the plant node.
		plantNode = new Node("plantComponents");

		// Initialize the manager for all AbstractPlantControllers.
		plantControllerManager = new PlantControllerManager(this, syncActions);
		// --------------- //

		// Create an initially empty PlantComposite for containing
		// PlantComponents. This prevents us from having to null-check plant.
		plant = new PlantComposite();
		plant.register(this);
		plant.registerPlantCompositeListener(this);

		return;
	}

	// ---- Application Initialization ---- //
	/**
	 * Initializes the materials used by the application. These should be stored
	 * in {@link #materials}. The default behavior provides the materials used
	 * for the axes.
	 */
	public void initMaterials() {
		// Create the default materials for the axes.
		super.initMaterials();

		// Create the materials. These should be retrieved by getting a key from
		// the factory and getting the material from the map of materials.
		plantMaterialFactory.createMaterials();

		return;
	}

	/**
	 * Initializes the HUD that displays useful information to the user.
	 */
	public void initHUD() {
		// TODO Auto-generated method stub

		return;
	}

	/**
	 * Initializes the default scene.
	 */
	public void initScene() {

		// ---- Set up the scene lighting. ---- //
		// Add ambient light.
		AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.LightGray.mult(0.5f));
		rootNode.addLight(ambientLight);

		// Create some directional lights.
		DirectionalLight light;
		Vector3f direction = new Vector3f();
		// The color for the lights.
		ColorRGBA lightColor = ColorRGBA.Gray;

		// Add three lights from above in a triangular pattern.

		// Add a directional light from above at the unit XYZ vector.
		light = new DirectionalLight();
		direction.set(1f, 1f, 1f);
		light.setDirection(direction);
		light.setColor(lightColor);
		rootNode.addLight(light);

		// Now move across the z axis by setting the x to -1.
		light = new DirectionalLight();
		direction.set(-1f, 1f, 1f);
		light.setDirection(direction);
		light.setColor(lightColor);
		rootNode.addLight(light);

		// Now move behind the scene along the z axis.
		light = new DirectionalLight();
		direction.set(0f, 1f, -1f);
		light.setDirection(direction);
		light.setColor(lightColor);
		rootNode.addLight(light);

		// Now add a single light from below.
		light = new DirectionalLight();
		direction.set(1f, -1f, 1f);
		light.setDirection(direction);
		light.setColor(lightColor);
		rootNode.addLight(light);
		// ------------------------------------ //

		// Attach the plant node to the scene.
		rootNode.attachChild(plantNode);

		// If necessary, populate the scene with the plant's components. This
		// might be the case if the plant was set before the application
		// finished initializing.
		if (plant.getNumberOfComponents() > 0) {
			addedComponents(plant, plant.getPlantComponents());
		}
		return;
	}

	/**
	 * Initializes the camera.
	 */
	public void initCamera() {

		// Speed up the fly cam and enable drag rotation.
		FlyByCamera cam = getFlyByCamera();
		cam.setDragToRotate(true);
		cam.setMoveSpeed(15.0f);

		return;
	}

	/**
	 * Initializes all controls associated with the 3D view.
	 */
	public void initControls() {

		// Let space and left control raise and lower the fly cam.
		inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(
				KeyInput.KEY_SPACE));
		inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_C));

		return;
	}

	// ------------------------------------ //

	// ---- Application Update/Synchronization ---- //
	/**
	 * All updates to the scene are processed in this method only.
	 */
	@Override
	public void simpleUpdate(float tpf) {
		// Perform the standard update operation from AbstractApplication.
		super.simpleUpdate(tpf);

		// TODO Any additional update operations should be handled here.

		return;
	}

	// -------------------------------------------- //

	// ---- Scene ---- //
	// --------------- //

	/**
	 * Sets the root PlantComponent that contains the pipes, junctions,
	 * reactors, etc. This is the plant that is rendered by this application.
	 * 
	 * @param plant
	 *            The new plant.
	 */
	public void setPlant(final PlantComposite plant) {

		if (plant != null && plant != this.plant) {

			// Unregister from the old plant.
			this.plant.unregister(this);
			this.plant.unregisterPlantCompositeListener(this);

			// Clear the previous scene by removing all of the controllers.
			plantControllerManager.clearControllers();

			// Update the reference to the plant and register with it.
			this.plant = plant;
			plant.register(this);
			plant.registerPlantCompositeListener(this);

			// Perform the usual update operations to pull in all of the new
			// plant's components.
			update(plant);
			addedComponents(plant, plant.getPlantComponents());
		}

		return;
	}

	// ---- Implements IUpdateableListener ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener
	 * #update(org.eclipse.ice.datastructures.updateableComposite.IUpdateable)
	 */
	public void update(IUpdateable component) {

		if (component == plant) {
			// TODO Any updates to the base properties of the PlantComposite
			// should be applied to the application here.
		}

		return;
	}

	// ---------------------------------------- //

	// ---- Implements IPlantCompositeListener ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantCompositeListener#addedComponents
	 * (org.eclipse.ice.reactor.plant.PlantComposite, java.util.List)
	 */
	public void addedComponents(PlantComposite composite,
			List<PlantComponent> added) {

		if (composite == plant && added != null) {
			// Create controllers for all added components and attach them to
			// the scene.

			AbstractPlantController controller;
			Material material;

			// Loop over the added components. If a controller can be created,
			// add it to the scene.
			for (PlantComponent component : added) {
				material = getMaterial(plantMaterialFactory.getKey(component));
				controller = plantControllerManager.createController(component,
						material);

				if (controller != null) {
					controller.setParentNode(plantNode);
				}
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantCompositeListener#removedComponents
	 * (org.eclipse.ice.reactor.plant.PlantComposite, java.util.List)
	 */
	public void removedComponents(PlantComposite composite,
			List<PlantComponent> removed) {

		if (composite == plant && removed != null) {
			// Remove the controllers for all removed components.
			for (PlantComponent component : removed) {
				plantControllerManager.removeController(component);
			}
		}

		return;
	}

	// -------------------------------------------- //

	/**
	 * Sets all rendered plant components to be viewed as wireframes or as solid
	 * objects.
	 * 
	 * @param wireframe
	 *            If true, plant components will be rendered with wireframes. If
	 *            false, they will be rendered solid.
	 */
	public void setWireframe(boolean wireframe) {
		// Loop over the PlantComponents. If one has a controller, then set the
		// wireframe property for the controller.
		for (PlantComponent component : plant.getPlantComponents()) {
			AbstractPlantController controller = plantControllerManager
					.getController(component);
			if (controller != null) {
				controller.setWireFrame(wireframe);
			}
		}
		return;
	}
}
