package org.eclipse.ice.client.widgets.reactoreditor.plant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.eclipse.ice.client.widgets.jme.EmbeddedView;
import org.eclipse.ice.client.widgets.jme.FlightCamera;
import org.eclipse.ice.client.widgets.jme.ViewAppState;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.reactor.plant.IPlantCompositeListener;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.PlantComposite;

import com.jme3.app.state.AppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * This jME3-based {@link AppState} provides a 3D view of a reactor plant.
 * 
 * @author Jordan Deyton
 * 
 */
public class PlantAppState extends ViewAppState implements IUpdateableListener,
		IPlantCompositeListener {

	/**
	 * The factory that is used to look up Materials for {@link PlantComponent}s
	 * in the PlantApplication.
	 */
	private final PlantMaterialFactory plantMaterialFactory;

	/**
	 * A manager for all {@link AbstractPlantController}s. This manager is used
	 * to create controllers and {@link AbstractPlantView}s for reactor plant
	 * objects.
	 */
	private final PlantControllerManager plantControllerManager;

	/**
	 * The root PlantComposite that contains the pipes, junctions, reactors,
	 * etc.
	 */
	private PlantComposite plant;

	// ---- Scene components ---- //
	/**
	 * The Node that should contain all plant views/controllers. This is useful
	 * if we want to detach all geometries from the scene at once and reattach
	 * them later.
	 */
	private final Node plantNode;

	/**
	 * The jME {@link Light}s that are attached to the root <code>Node</code>.
	 */
	private final List<Light> lights;
	// -------------------------- //

	/**
	 * The default position of the view's camera.
	 */
	private final Vector3f defaultPosition = new Vector3f(10f, 0f, 0f);
	/**
	 * The default direction in which the view's camera points.
	 */
	private final Vector3f defaultDirection = new Vector3f(-1f, 0f, 0f);
	/**
	 * The default up direction for the view's camera. This should always be
	 * orthogonal to the default direction vector.
	 */
	private final Vector3f defaultUp = new Vector3f(Vector3f.UNIT_Z);

	/**
	 * The default constructor.
	 */
	public PlantAppState() {

		// Initialize the factory that is used to create materials. The
		// materials are created in initMaterials().
		plantMaterialFactory = new PlantMaterialFactory(this);

		// Initialize the manager for all AbstractPlantControllers.
		plantControllerManager = new PlantControllerManager(this);

		// Create an initially empty PlantComposite for containing
		// PlantComponents. This prevents us from having to null-check plant.
		plant = new PlantComposite();
		plant.register(this);
		plant.registerPlantCompositeListener(this);

		// ---- Scene components ---- //
		// Create the plant node.
		plantNode = new Node("plantComponents");
		// Create the list of lights. Make the list just big enough since the
		// construction of lights is hard-coded (no point in wasting the space).
		lights = new ArrayList<Light>(5);
		// -------------------------- //

		return;
	}

	// ---- Initialization ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#initMaterials()
	 */
	@Override
	protected void initMaterials() {

		// Create the materials used for PlantComponents.
		plantMaterialFactory.createMaterials();

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#initScene()
	 */
	@Override
	protected void initScene() {

		// ---- Set up the scene lighting. ---- //
		// Create the lights if they are not already available.
		if (lights.isEmpty()) {
			// Add ambient light.
			AmbientLight ambientLight = new AmbientLight();
			ambientLight.setColor(ColorRGBA.LightGray.mult(0.5f));
			lights.add(ambientLight);

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
			lights.add(light);

			// Now move across the z axis by setting the x to -1.
			light = new DirectionalLight();
			direction.set(-1f, 1f, 1f);
			light.setDirection(direction);
			light.setColor(lightColor);
			lights.add(light);

			// Now move behind the scene along the z axis.
			light = new DirectionalLight();
			direction.set(0f, 1f, -1f);
			light.setDirection(direction);
			light.setColor(lightColor);
			lights.add(light);

			// Now add a single light from below.
			light = new DirectionalLight();
			direction.set(1f, -1f, 1f);
			light.setDirection(direction);
			light.setColor(lightColor);
			lights.add(light);
		}

		// Add all of the lights to the root Node.
		for (Light light : lights) {
			rootNode.addLight(light);
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#initControls()
	 */
	@Override
	protected void initControls() {
		// Nothing to do yet.
	}
	// ------------------------ //

	// ---- Enable/Disable ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#registerControls()
	 */
	@Override
	public void registerControls() {
		// Nothing to do yet.
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.SimpleAppState#unregisterControls()
	 */
	@Override
	public void unregisterControls() {
		// Nothing to do yet.
	}
	// ------------------------ //
	
	// ---- Cleanup ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#clearControls()
	 */
	@Override
	protected void clearControls() {
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#clearScene()
	 */
	@Override
	protected void clearScene() {

		// Clear the scene lighting.
		for (Light light : lights) {
			rootNode.removeLight(light);
		}
		lights.clear();

		// Remove the rendered plant components.
		rootNode.detachChild(plantNode);

		// If necessary, remove all rendered plant components from the plant
		// Node.
		if (plant.getNumberOfComponents() > 0) {
			removedComponents(plant, plant.getPlantComponents());
		}
		// FIXME We may just be able to directly use the controller manager.

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#clearMaterials()
	 */
	@Override
	protected void clearMaterials() {

		// Dispose of all the materials created by the factory.
		plantMaterialFactory.disposeMaterials();

		return;
	}
	// ----------------- //

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#update(float)
	 */
	@Override
	public void update(float tpf) {
		// Nothing to do yet. If we need to perform any updates to the scene on
		// a regular basis, do that here.
	}

	// ---- Implements IPlantCompositeListener ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.plant.IPlantCompositeListener#addedComponents
	 * (org.eclipse.ice.reactor.plant.PlantComposite, java.util.List)
	 */
	@Override
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
	@Override
	public void removedComponents(PlantComposite composite,
			List<PlantComponent> removed) {

		if (composite == plant && removed != null) {
			// Remove the controllers for all removed components.
			for (PlantComponent component : removed)
				plantControllerManager.removeController(component);
		}

		return;
	}
	// -------------------------------------------- //

	// ---- Implements IUpdateableListener ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener
	 * #update(org.eclipse.ice.datastructures.updateableComposite.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {

		if (component == plant) {
			// TODO Any updates to the base properties of the PlantComposite
			// should be applied to the view here.
		}

		return;
	}
	// ---------------------------------------- //

	/**
	 * Overrides the default view camera to set its initial location based on
	 * the default position and orientation.
	 */
	@Override
	public Object createViewCamera(EmbeddedView view) {
		Object cam = super.createViewCamera(view);

		if (cam != null) {
			FlightCamera flyCam = (FlightCamera) cam;

			// Set the default position and orientation.
			flyCam.setPosition(defaultPosition);
			flyCam.setOrientation(defaultDirection, defaultUp);
			// The camera should be enabled initially.
			flyCam.setEnabled(true);
		}

		return cam;
	}

	/**
	 * Sets the default position of the view's camera.
	 * 
	 * @param position
	 *            The new default position. If null, an exception is thrown.
	 */
	public void setDefaultCameraPosition(Vector3f position) {
		// Check for nulls first.
		if (position == null) {
			throw new IllegalArgumentException(
					"PlantAppState error: "
							+ "Null arguments not accepted for setting the default camera position.");
		}

		// Update the default position.
		defaultPosition.set(position);

		return;
	}

	/**
	 * Sets the default orientation of the view's camera.
	 * 
	 * @param direction
	 *            The new default direction in which the camera will point. If
	 *            null, an exception is thrown.
	 * @param up
	 *            The new default up direction. If null or if it is not
	 *            orthogonal to the camera direction, an exception is thrown.
	 */
	public void setDefaultCameraOrientation(Vector3f direction, Vector3f up) {
		// Check for nulls first.
		if (direction == null || up == null) {
			throw new IllegalArgumentException("FlightCamera error: "
					+ "Null arguments not accepted for orienting the camera.");
		}
		// Make sure the direction and up vectors are orthogonal.
		else if (FastMath.abs(direction.dot(up)) > 1e-5f
				|| direction.equals(up)) {
			throw new IllegalArgumentException("FlightCamera error: "
					+ "Direction and up vector are not orthogonal.");
		}

		// Update the default orientation.
		defaultDirection.set(direction);
		defaultUp.set(up);

		return;
	}

	/**
	 * Resets the plant view's camera to its default position and orientation.
	 * 
	 * @see #setDefaultCameraPosition(Vector3f)
	 * @see #setDefaultCameraOrientation(Vector3f, Vector3f)
	 */
	public void resetCamera() {
		// Get the camera if it exists.
		final EmbeddedView view = getEmbeddedView();
		final FlightCamera flyCam = (view != null ? (FlightCamera) view
				.getViewCamera() : null);

		// If the camera exists, reset its position and orientation.
		if (flyCam != null) {
			enqueue(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					flyCam.setPosition(defaultPosition);
					flyCam.setOrientation(defaultDirection, defaultUp);
					return true;
				}
			});
		}

		return;
	}

	/**
	 * Gets the flying camera associated with the plant view.
	 * 
	 * @return The plant view's fly cam.
	 */
	public FlightCamera getFlightCamera() {
		FlightCamera cam = null;
		EmbeddedView view = getEmbeddedView();
		if (view != null) {
			cam = (FlightCamera) view.getViewCamera();
		}
		return cam;
	}

	// ---- Getters and Setters ---- //
	/**
	 * Sets the root PlantComponent that contains the pipes, junctions,
	 * reactors, etc. This is the plant that is rendered by this view.
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
	// ----------------------------- //
}
