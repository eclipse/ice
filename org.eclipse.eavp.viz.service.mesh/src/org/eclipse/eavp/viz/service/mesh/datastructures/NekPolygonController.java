/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.mesh.datastructures;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.FaceController;
import org.eclipse.eavp.viz.service.modeling.FaceMesh;

/**
 * A Face which maintains the information needed for a Polygon in a Nek5000
 * mesh.
 * 
 * @author Jordan H. Deyton
 * @author Robert Smith
 *
 */
public class NekPolygonController extends FaceController
		implements IVizUpdateableListener {
	/**
	 * <p>
	 * The properties for each polygon defined in the MESH DATA section of a Nek
	 * reafile. Contains a String representing the material ID, and an integer
	 * representing the group number of the polygon.
	 * </p>
	 * 
	 */
	@XmlElement(name = "PolygonProperties")
	protected PolygonProperties polygonProperties;

	/**
	 * <p>
	 * A map of edge properties for each edge, keyed on the edge IDs.
	 * </p>
	 * 
	 */
	@XmlElementWrapper(name = "EdgeProperties")
	private HashMap<Integer, EdgeProperties> edgeProperties;

	/**
	 * The nullary constructor
	 */
	public NekPolygonController() {
		super();

		// Initialize the boundary condition containers.
		edgeProperties = new HashMap<Integer, EdgeProperties>();

		// Initialize the polygon properties
		polygonProperties = new PolygonProperties();
	}

	/**
	 * The default constructor
	 */
	public NekPolygonController(FaceMesh model, AbstractView view) {
		super(model, view);

		// Initialize the boundary condition containers.
		edgeProperties = new HashMap<Integer, EdgeProperties>();

		// Initialize the polygon properties
		polygonProperties = new PolygonProperties();

		// Set the default name, id, and description.
		model.setProperty("Name", "Polygon");
		model.setProperty("Description", "");

		// Initialize the polygon's relationship to each edge property and
		// boundary condition
		for (AbstractController edge : model.getEntitiesByCategory("Edges")) {
			initializeBoundaryConditions(edge);
		}

	}

	/**
	 * Create a set of edge properties for the given edge and place them in the
	 * properties, keyed by that edge's id.
	 * 
	 * @param edge
	 *            The edge for which boundary conditions are being created
	 */
	public void initializeBoundaryConditions(AbstractController edge) {
		// Create an entry for the edge in the map of edge properties.
		EdgeProperties properties = new EdgeProperties();
		edgeProperties.put(Integer.valueOf(edge.getProperty("Id")), properties);

		// Register with all of the boundary conditions in the properties.
		properties.getFluidBoundaryCondition().register(this);
		properties.getThermalBoundaryCondition().register(this);
		for (BoundaryCondition condition : properties
				.getOtherBoundaryConditions()) {
			condition.register(this);
		}
	}

	/**
	 * <p>
	 * Sets a fluid boundary condition for an edge of the polygon.
	 * </p>
	 * 
	 * @param edgeId
	 *            The ID of the edge that will have the new BoundaryCondition.
	 * @param condition
	 *            The new BoundaryCondition.
	 */
	public void setFluidBoundaryCondition(int edgeId,
			BoundaryCondition condition) {

		// First, check that the edgeId is valid by performing a map lookup.
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (condition != null && properties != null) {
			// If the edgeId is valid, try to set the new condition. If the new
			// condition is set, we need to register with the new condition and
			// notify listeners of the change.
			BoundaryCondition oldCondition = properties
					.getFluidBoundaryCondition();
			if (properties.setFluidBoundaryCondition(condition)) {
				// Unregister from the old condition and register with the new.
				oldCondition.unregister(this);
				condition.register(this);

				// Notify listeners of the change.
				SubscriptionType[] eventType = new SubscriptionType[1];
				eventType[0] = SubscriptionType.PROPERTY;
				updateManager.notifyListeners(eventType);
			}
		}

		return;
	}

	/**
	 * <p>
	 * Gets the fluid boundary condition for an edge of the polygon.
	 * </p>
	 * 
	 * @param edgeId
	 *            The ID of the edge that has a BoundaryCondition.
	 * @return The edge's BoundaryCondition for this polygon, or null if the
	 *         edge ID is invalid.
	 */
	public BoundaryCondition getFluidBoundaryCondition(int edgeId) {

		// If the edgeId is valid, we can pull the property from the
		// EdgeProperty instance.
		BoundaryCondition condition = null;
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (properties != null) {
			condition = properties.getFluidBoundaryCondition();
		}
		return condition;
	}

	/**
	 * <p>
	 * Sets a thermal boundary condition for an edge of the polygon.
	 * </p>
	 * 
	 * @param edgeId
	 *            The ID of the edge that will have the new BoundaryCondition.
	 * @param condition
	 *            The new BoundaryCondition.
	 */
	public void setThermalBoundaryCondition(int edgeId,
			BoundaryCondition condition) {

		// First, check that the edgeId is valid by performing a map lookup.
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (condition != null && properties != null) {
			// If the edgeId is valid, try to set the new condition. If the new
			// condition is set, we need to register with the new condition and
			// notify listeners of the change.
			BoundaryCondition oldCondition = properties
					.getThermalBoundaryCondition();
			if (properties.setThermalBoundaryCondition(condition)) {
				// Unregister from the old condition and register with the new.
				oldCondition.unregister(this);
				condition.register(this);

				// Notify listeners of the change.
				SubscriptionType[] eventType = new SubscriptionType[1];
				eventType[0] = SubscriptionType.PROPERTY;
				updateManager.notifyListeners(eventType);
			}
		}

		return;
	}

	/**
	 * <p>
	 * Gets the thermal boundary condition for an edge of the polygon.
	 * </p>
	 * 
	 * @param edgeId
	 *            The ID of the edge that has a BoundaryCondition.
	 * @return The edge's BoundaryCondition for this polygon, or null if the
	 *         edge ID is invalid.
	 */
	public BoundaryCondition getThermalBoundaryCondition(int edgeId) {

		// If the edgeId is valid, we can pull the property from the
		// EdgeProperty instance.
		BoundaryCondition condition = null;
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (properties != null) {
			condition = properties.getThermalBoundaryCondition();
		}
		return condition;
	}

	/**
	 * <p>
	 * Sets a passive scalar boundary condition for an edge of the polygon.
	 * </p>
	 * 
	 * @param edgeId
	 *            The ID of the edge that will have the new BoundaryCondition.
	 * @param otherId
	 *            The ID or index of the set of passive scalar boundary
	 *            conditions.
	 * @param condition
	 *            The new BoundaryCondition.
	 */
	public void setOtherBoundaryCondition(int edgeId, int otherId,
			BoundaryCondition condition) {

		// First, check that the edgeId is valid by performing a map lookup.
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (condition != null && properties != null) {
			// If the edgeId is valid, try to set the new condition. If the new
			// condition is set, we need to register with the new condition and
			// notify listeners of the change.
			BoundaryCondition oldCondition = properties
					.getOtherBoundaryCondition(otherId);
			if (properties.setOtherBoundaryCondition(otherId, condition)) {
				// Unregister from the old condition and register with the new.
				// We need a null check because the scalar index ID may be new.
				if (oldCondition != null) {
					oldCondition.unregister(this);
				}
				condition.register(this);

				// Notify listeners of the change.
				SubscriptionType[] eventType = new SubscriptionType[1];
				eventType[0] = SubscriptionType.PROPERTY;
				updateManager.notifyListeners(eventType);
			}
		}

		return;
	}

	/**
	 * <p>
	 * Gets a passive scalar boundary condition for an edge of the polygon.
	 * </p>
	 * 
	 * @param edgeId
	 *            The ID of the edge that has a BoundaryCondition.
	 * @param otherId
	 *            The ID or index of the set of passive scalar boundary
	 *            conditions.
	 * @return The edge's BoundaryCondition for this polygon, or null if the
	 *         edge ID is invalid.
	 */
	public BoundaryCondition getOtherBoundaryCondition(int edgeId,
			int otherId) {

		// If the edgeId is valid, we can pull the property from the
		// EdgeProperty instance.
		BoundaryCondition condition = null;
		EdgeProperties properties = edgeProperties.get(edgeId);
		if (properties != null) {
			condition = properties.getOtherBoundaryCondition(otherId);
		}
		return condition;
	}

	/**
	 * <p>
	 * Sets the properties for the current polygon.
	 * </p>
	 * 
	 * @param materialId
	 *            The material ID of the current polygon.
	 * @param group
	 *            The group number of the current polygon.
	 */
	public void setPolygonProperties(String materialId, int group) {
		polygonProperties = new PolygonProperties(materialId, group);

		return;
	}

	/**
	 * <p>
	 * Returns the properties for the current polygon.
	 * </p>
	 * 
	 * @return The properties for the current polygon.
	 */
	public PolygonProperties getPolygonProperties() {
		return polygonProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractController#setProperty(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public void setProperty(String property, String value) {

		// If the Edge's constructing or selected properties are being changed,
		// propagate that change to its vertices
		if ("Constructing".equals(property) || "Selected".equals(property)) {

			// Queue notifications from changing own edges
			updateManager.enqueue();

			for (AbstractController vertex : model
					.getEntitiesByCategory("Edges")) {
				vertex.setProperty(property, value);
			}

			// Send all notifications from setting selection or construction
			// states
			updateManager.flushQueue();
		}

		super.setProperty(property, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener#update(org.eclipse.eavp.viz.service.datastructures.
	 * VizObject.IVizUpdateable)
	 */
	@Override
	public void update(IVizUpdateable component) {

		// This is the IVizUpdateable update method, which will only be
		// triggered by boundary conditions. Thus, this should trigger a
		// Property type update for the part's own listeners
		SubscriptionType[] eventType = new SubscriptionType[1];
		eventType[0] = SubscriptionType.PROPERTY;
		updateManager.notifyListeners(eventType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#
	 * addEntityByCategory(org.eclipse.eavp.viz.service.modeling.
	 * AbstractController, java.lang.String)
	 */
	@Override
	public void addEntityByCategory(AbstractController entity,
			String category) {

		// When edges are added, create boundary conditions for them.
		if ("Edges".equals(category)) {
			initializeBoundaryConditions(entity);
		}

		super.addEntityByCategory(entity, category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.modeling.AbstractController#clone()
	 */
	@Override
	public Object clone() {

		// Create a copy of the model
		NekPolygonController clone = new NekPolygonController();
		clone.copy(this);

		// Refresh the view to be in sync with the model
		clone.refresh();

		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.modeling.AbstractController#copy(org.eclipse.
	 * ice.viz.service.modeling.AbstractController)
	 */
	@Override
	public void copy(AbstractController otherObject) {
		// Create the model and give it a reference to this
		model = new NekPolygonMesh();
		model.setController(this);

		// Copy the other object's data members
		model.copy(otherObject.getModel());
		view = (AbstractView) otherObject.getView().clone();

		// Register as a listener to the model and view
		model.register(this);
		view.register(this);
	}
}
