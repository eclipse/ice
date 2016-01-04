/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Andrew P. Belt, Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.geometry;

import java.util.List;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.ShapeController;
import org.eclipse.ice.viz.service.modeling.ShapeMesh;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 * A view containing the JME3 representation of a ShapeComponent.
 * 
 * @author Andrew P. Belt, Robert Smith
 *
 */
public class JME3ShapeView extends AbstractView {

	/**
	 * The JME3 mesh created and managed by this view.
	 */
	Mesh mesh;

	/**
	 * The Material used for this shape.
	 */
	private Material material;

	/**
	 * The Material used for this shape when it is selected/highlighted.
	 */
	private Material highlightedMaterial;

	/**
	 * <p>
	 * The selected state of the shape
	 * </p>
	 * 
	 */
	private boolean selected;
	
	/**
	 * The nullary constructor.
	 */
	public JME3ShapeView(){	
	}

	/**
	 * The default constructor.
	 * 
	 * @param model
	 *            The model which this view will graphically represent.
	 */
	public JME3ShapeView(ShapeMesh model) {

		// Create the view's mesh
		setMesh(model);

		// Set the view's selected state
		selected = ("True".equals(model.getProperty("Selected")) ? true : false);

	}

	/**
	 * <p>
	 * Returns the material associated with the IShape properties
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The material to render the shape
	 *         </p>
	 */
	public Material getMaterial() {

		// Highlight the shape if it is selected. The if statement is ordered
		// backwards because it is most likely that the shape is not selected
		// and in this case it will execute this function quicker because it
		// only has to do the first check.
		if (!selected) {
			return material;
		} else {
			return highlightedMaterial;
		}
	}

	/**
	 * This operation sets the material that should be used for this shape.
	 * 
	 * @param mat
	 *            The JME3 material.
	 */
	public void setMaterial(Material mat) {

		// Set the material that should be used for this shape
		material = mat;

		return;
	}

	/**
	 * This operation sets the material that should be used for the shape when
	 * it is selected.
	 * 
	 * @param mat
	 *            The JME3 material used when this shape is selected.
	 */
	public void setHighlightedMaterial(Material mat) {

		// Set the material
		highlightedMaterial = mat;

		return;
	}

	/**
	 * Create a JME3 mesh object appropriate to this part's type.
	 * 
	 * @param model
	 *            The view' model
	 */
	public void setMesh(AbstractMesh model) {
		// The shape's type
		String type = model.getProperty("Type");

		// Depending on the type of shape, create a matching type of mesh
		if (type != null) {
			switch (type) {
			case "Cube":
				mesh = new Box(Vector3f.ZERO, 0.5f, 0.5f, 0.5f);
				break;
			case "Cylinder":
				mesh = new Cylinder(5, 30, 0.5f, 1.0f, true, false);
				break;
			case "Sphere":
				mesh = new Sphere(15, 30, 0.5f);
				break;
			case "Tube":
				mesh = new Tube(0.5f, 0.4f, 1.0f, 5, 30);
				break;
			// TODO Implement Cone.
			// "None" or invalid input creates a view with no mesh.
			default:
				mesh = null;
				break;
			}
		} else {
			mesh = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractView#getRepresentation()
	 */
	@Override
	public Object getRepresentation() {
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.AbstractView#refresh(org.eclipse.ice
	 * .viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public void refresh(AbstractMesh model) {

		// Update both the mesh and selected state
		setMesh(model);
		selected = ("True".equals(model.getProperty("Selected")) ? true : false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.modeling.AbstractView#clone()
	 */
	public Object clone(){
		
		//Create a new view and copy this object's data into it
		JME3ShapeView clone = new JME3ShapeView();
		clone.copy(this);
		return clone;
	}
	
	/**
	 * Make the receiver a copy of the source object
	 * 
	 * @param source The object to be copied.
	 */
	public void copy(JME3ShapeView source){
		super.copy(source);
		
		//Copy the member variables
		mesh = source.mesh;
		material = source.material;
		highlightedMaterial = source.highlightedMaterial;
		selected = source.selected;
	}
}
