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
package org.eclipse.ice.viz.service.mesh.datastructures;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Face;
import org.eclipse.ice.viz.service.modeling.FaceComponent;

/**
 * A Face which maintains the information needed for a Polygon in a Nek5000
 * mesh.
 * 
 * @author Jordan H. Deyton, Robert Smith
 *
 */
public class NekPolygon extends Face {
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
	public NekPolygon() {
		super();

		// Initialize the boundary condition containers.
		edgeProperties = new HashMap<Integer, EdgeProperties>();

		// Initialize the polygon properties
		polygonProperties = new PolygonProperties();

		// Set the default name, id, and description.
		model.setProperty("Name", "Polygon");
		model.setProperty("Description", "");
	}

	/**
	 * The default constructor
	 */
	public NekPolygon(FaceComponent model, AbstractView view) {
		super();

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

			// Create an entry for the edge in the map of edge properties.
			EdgeProperties properties = new EdgeProperties();
			edgeProperties.put(Integer.valueOf(edge.getProperty("Id")),
					properties);

			// Register with all of the boundary conditions in the properties.
			properties.getFluidBoundaryCondition().register(this);
			properties.getThermalBoundaryCondition().register(this);
			for (BoundaryCondition condition : properties
					.getOtherBoundaryConditions()) {
				condition.register(this);
			}
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
				notifyListeners();
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
				notifyListeners();
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
				notifyListeners();
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
}
