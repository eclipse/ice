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
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.eavp.viz.modeling.EdgeController;
import org.eclipse.eavp.viz.modeling.FaceController;
import org.eclipse.eavp.viz.modeling.base.IController;
import org.eclipse.eavp.viz.modeling.properties.MeshCategory;
import org.eclipse.eavp.viz.modeling.properties.MeshProperty;
import org.eclipse.eavp.viz.modeling.VertexController;
import org.eclipse.ice.client.common.PropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * This class extends PropertySource to manage property data provided by entries
 * in the MeshElementTreeView and populate the Properties view.
 * 
 * @author Taylor Patterson
 */
public class MeshElementTreeViewPropertySource extends PropertySource {

	/**
	 * Property IDs
	 */
	private static final String ID_EDGES = "Edges";
	private static final String ID_VERTICES = "Vertices";
	private static final String ID_LENGTH = "Length";
	private static final String ID_STARTLOC = "Start Location";
	private static final String ID_ENDLOC = "End Location";
	private static final String ID_LOCATION = "Location";

	/**
	 * IPropertyDescriptors for each type of PropertySource that will appear in
	 * the MeshElementTreeView
	 */
	private static IPropertyDescriptor[] polygonDescriptors;
	private static IPropertyDescriptor[] edgeDescriptors;
	private static IPropertyDescriptor[] vertexDescriptors;

	/**
	 * Initialize the PropertyDescriptor arrays for polygons, edges, and
	 * vertices
	 */
	static {
		polygonDescriptors = new IPropertyDescriptor[] {
				new PropertyDescriptor(ID_EDGES, "Edges"),
				new PropertyDescriptor(ID_VERTICES, "Vertices") };
		edgeDescriptors = new IPropertyDescriptor[] {
				new PropertyDescriptor(ID_VERTICES, "Vertices"),
				new PropertyDescriptor(ID_LENGTH, "Length"),
				new PropertyDescriptor(ID_STARTLOC, "Start Location"),
				new PropertyDescriptor(ID_ENDLOC, "End Location") };
		vertexDescriptors = new IPropertyDescriptor[] {
				new PropertyDescriptor(ID_LOCATION, "Location") };
	}

	/**
	 * A map of tree element types to property descriptors
	 */
	private static Hashtable<Class<?>, IPropertyDescriptor[]> propDescMap;

	/**
	 * Initialize the map of element types to descriptors
	 */
	static {
		propDescMap = new Hashtable<Class<?>, IPropertyDescriptor[]>();
		propDescMap.put(FaceController.class, polygonDescriptors);
		propDescMap.put(EdgeController.class, edgeDescriptors);
		propDescMap.put(VertexController.class, vertexDescriptors);
	}

	/**
	 * Classes that implement this interface will be used to handle property
	 * value retrieval.
	 * 
	 * @author Taylor Patterson
	 */
	interface IPropertyTypeHandler {

		/**
		 * This function retrieves the property value of the given id from the
		 * given data object.
		 * 
		 * @param data
		 *            The PropertySource to extract the property value from
		 * 
		 * @param id
		 *            The id for the requested value
		 * 
		 * @return The property value requested
		 */
		Object getValue(Object data, Object id);
	}

	/**
	 * A map of tree element types to their respective value handler methods
	 */
	private static Hashtable<Class<?>, IPropertyTypeHandler> propHandlerMap;

	/**
	 * Initialize the IPropertyTypeHandler classes and the map
	 */
	static {

		// Create the property value handler for polygons
		IPropertyTypeHandler polygonPropertyHandler = new IPropertyTypeHandler() {

			/**
			 * @see IPropertyTypeHandler#getValue(Object, Object)
			 */
			@Override
			public Object getValue(Object data, Object id) {

				FaceController polygon = (FaceController) data;
				ArrayList<String> propertySet = new ArrayList<String>();

				// If the caller seeks the edges, get them from the wrapped
				// Polygon.
				if (ID_EDGES.equals(id)) {
					for (IController e : polygon
							.getEntitiesFromCategory(MeshCategory.EDGES)) {
						propertySet.add("Edge " + e.getProperty(MeshProperty.ID));
					}
					return propertySet;
				}
				// If the caller seeks the vertices, get them from the wrapped
				// Polygon.
				else if (ID_VERTICES.equals(id)) {
					for (IController v : polygon
							.getEntitiesFromCategory(MeshCategory.VERTICES)) {
						propertySet.add("Vertex " + v.getProperty(MeshProperty.ID));
					}
					return propertySet;
				}
				// Otherwise, the property is unknown.
				else {
					return "ERROR: Unknown property requested";
				}

			}
		};

		// Create the property value handler for edges
		IPropertyTypeHandler edgePropertyHandler = new IPropertyTypeHandler() {

			/**
			 * @see IPropertyTypeHandler#getValue(Object, Object)
			 */
			@Override
			public Object getValue(Object data, Object id) {

				EdgeController edge = (EdgeController) data;
				ArrayList<String> propertySet = new ArrayList<String>();

				// Collect the given edge's vertices
				if (ID_VERTICES.equals(id)) {
					for (IController vertex : edge
							.getEntitiesFromCategory(MeshCategory.VERTICES)) {
						propertySet.add("Vertex " + Integer
								.valueOf(vertex.getProperty(MeshProperty.ID)));
					}
					return propertySet;
				}
				// Get the edge's length
				else if (ID_LENGTH.equals(id)) {
					return edge.getLength();
				}
				// Get the edge's start location
				else if (ID_STARTLOC.equals(id)) {
					double[] loc = edge.getStartLocation();
					for (int i = 0; i < loc.length; i++) {
						propertySet.add(((Double) loc[i]).toString());
					}
					return propertySet;
				}
				// Get the edge's end location
				else if (ID_ENDLOC.equals(id)) {
					double[] loc = edge.getEndLocation();
					for (int i = 0; i < loc.length; i++) {
						propertySet.add(((Double) loc[i]).toString());
					}
					return propertySet;
				}
				// Otherwise, the property is unknown.
				else {
					return "ERROR: Unknown property requested";
				}

			}
		};

		// Create the property value handler for vertices
		IPropertyTypeHandler vertexPropertyHandler = new IPropertyTypeHandler() {

			/**
			 * @see IPropertyTypeHandler#getValue(Object, Object)
			 */
			@Override
			public Object getValue(Object data, Object id) {

				VertexController vertex = (VertexController) data;
				ArrayList<String> propertySet = new ArrayList<String>();

				// Get the vertex's location
				if (ID_LOCATION.equals(id)) {
					double[] loc = vertex.getTranslation();
					for (int i = 0; i < loc.length; i++) {
						propertySet.add(((Double) loc[i]).toString());
					}
					return propertySet;
				}
				// Otherwise, the property is unknown.
				else {
					return "ERROR: Unknown property requested";
				}

			}
		};

		// Populate the handler map
		propHandlerMap = new Hashtable<Class<?>, IPropertyTypeHandler>();
		propHandlerMap.put(FaceController.class, polygonPropertyHandler);
		propHandlerMap.put(EdgeController.class, edgePropertyHandler);
		propHandlerMap.put(VertexController.class, vertexPropertyHandler);

	}

	/**
	 * The constructor
	 * 
	 * @param part
	 *            The object to be wrapped by PropertySource. For this subclass,
	 *            this will be a Polygon, Edge, or Vertex.
	 */
	public MeshElementTreeViewPropertySource(IController part) {

		// Just call the superclass constructor
		super(part);

		return;
	}

	/**
	 * This function returns the array of descriptors for properties.
	 * 
	 * @return The array of descriptors for properties.
	 * 
	 * @see IPropertySource#getPropertyDescriptors()
	 */
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		// Just get the descriptor array from the map
		return propDescMap.get(this.getWrappedData().getClass());

	}

	/**
	 * This function returns the value for a given property.
	 * 
	 * @param id
	 *            The object used to identify this property.
	 * 
	 * @return The value for the input property
	 * 
	 * @see IPropertySource#getPropertyValue(Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {

		// Get the property source's wrapped object
		Object data = this.getWrappedData();

		// Get the handler method from the map
		IPropertyTypeHandler propHandler = propHandlerMap.get(data.getClass());

		// Call the handler method
		return propHandler.getValue(data, id);

	}
}