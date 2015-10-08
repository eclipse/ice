/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.internal.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import org.eclipse.ice.viz.service.geometry.shapes.ComplexShape;
import org.eclipse.ice.viz.service.geometry.shapes.IShape;
import org.eclipse.ice.viz.service.geometry.shapes.IShapeVisitor;
import org.eclipse.ice.viz.service.geometry.shapes.PrimitiveShape;
import org.eclipse.ice.viz.service.geometry.shapes.ShapeType;
import org.eclipse.ice.viz.service.geometry.widgets.ShapeMaterial;
import org.eclipse.ice.viz.service.javafx.internal.Util;

import javafx.scene.Group;
import javafx.scene.paint.Material;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

/**
 * Synchronizes the given JavaFX node with each visited IShape child
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 * @author Andrew P. Belt
 * 
 */
public class SyncShapes implements IShapeVisitor {

	/**
	 * The JavaFX scene graph node to update
	 */
	private Group parentNode;

	/**
	 * The temporary working list of PrimitiveShapes
	 */
	private ArrayList<PrimitiveShape> primitiveShapes = new ArrayList<PrimitiveShape>();

	/**
	 * The temporary working list of ComplexShapes
	 */
	private ArrayList<ComplexShape> complexShapes = new ArrayList<ComplexShape>();

    /** */
    private Material baseMaterial;

    /** */
    private Material highlightedMaterial;
    
	/**
	 * Initializes an instance with a JavaFX scene graph node
	 * 
	 * @param parentNode
	 *            The parent of the nodes associated with the visited
	 *            IShapes
	 */
	public SyncShapes(Group parentNode) {
		this.parentNode = parentNode;
		
        baseMaterial = Util.DEFAULT_MATERIAL;
        highlightedMaterial = Util.DEFAULT_HIGHLIGHTED_MATERIAL;
	}

	/**
	 * Adds a ComplexShape to the temporary working list
	 */
	@Override
	public void visit(ComplexShape complexShape) {
		complexShapes.add(complexShape);
	}

	/**
	 * Adds a PrimitiveShape to the temporary working list
	 */
	@Override
	public void visit(PrimitiveShape primitiveShape) {
		primitiveShapes.add(primitiveShape);
	}

	/**
	 * Performs the synchronization of the node and the list of shapes
	 */
	public void commit() {

		// Local Declarations
		IShape nodeShape = null;
		int primitiveShapeIndex = -1;
		int complexShapeIndex = -1;

		// Create a list of Node children indices to remove at a later time
		Vector<Integer> removeNodeIndices = new Vector<Integer>();

		int numNodeChildren = this.parentNode.getChildren().size();
		for (int nodeIndex = 0; nodeIndex < numNodeChildren; nodeIndex++) {

			// Get the child node
		    MeshView childSpatial = (MeshView) this.parentNode.getChildren().get(nodeIndex);
			Group childNode = null;

			/*try {
				childNode = (Node) childSpatial;
			} catch (ClassCastException e) {
			}*/

			// Extract the reference to the child shape if it exists
			IShape shapeProperty = Util.getShapeProperty(childSpatial);
			
			if (shapeProperty != null) {
				primitiveShapeIndex = primitiveShapes.indexOf(shapeProperty);
				complexShapeIndex = complexShapes.indexOf(shapeProperty);
			}

			// Check if nodeShape is in one of the IShape lists

			if (primitiveShapeIndex >= 0) {
				// nodeShape is an existing PrimitiveShape

				PrimitiveShape primitiveShape = primitiveShapes
						.get(primitiveShapeIndex);

				// Reset the transformation

				childSpatial
						.getTransforms().add(Util.convertTransformation(primitiveShape
								.getTransformation()));

				// Rotate 90 degrees if it's a cylinder

				if (primitiveShape.getType() == ShapeType.Cylinder) {
		            Rotate rotate = Transform.rotate(90.0f, 0.0f, 0.0f);
		            //meshGroup.getTransforms().add(rotate);
				}

				// Reset the material - remember to use the baseMaterial and
				// highlightedMaterial!
				ShapeMaterial shapeMaterial = new ShapeMaterial(primitiveShape);
				
				shapeMaterial.setMaterial(baseMaterial);
				// Set the base material that is used for all selected
				// shapes
				shapeMaterial.setHighlightedMaterial(highlightedMaterial);
				// Get the proper material from the shapeMaterial. It will
				// change depending on whether or not it is selected.
				childSpatial.setMaterial(shapeMaterial.getMaterial());

				// Remove the shape from the PrimitiveShapes list

				this.primitiveShapes.remove(primitiveShapeIndex);
			}

			else if (complexShapeIndex >= 0) {
				// nodeShape is an existing ComplexShape

				ComplexShape complexShape = complexShapes
						.get(complexShapeIndex);

				// Reset the transform

				childSpatial
						.getTransforms().setAll(Util.convertTransformation(complexShape
								.getTransformation()));

				// Synchronize each individual child in the ComplexShape

				SyncShapes syncComplexShapes = new SyncShapes(childNode);
				ArrayList<IShape> complexShapeChildren = complexShape
						.getShapes();

				for (IShape complexShapeChild : complexShapeChildren) {
					complexShapeChild.acceptShapeVisitor(syncComplexShapes);
				}

				// Perform the updating on the scene graph

				syncComplexShapes.commit();

				// Remove the shape from the ComplexShapes list

				this.complexShapes.remove(complexShapeIndex);
			}

			else {
				// nodeShape does not exist in one of the IShape lists

				removeNodeIndices.add(new Integer(nodeIndex));
			}
		}

		// Remove the child Spatials by the given node indices

		Collections.reverse(removeNodeIndices);

		for (Integer nodeIndex : removeNodeIndices) {
			//this.parentNode.detachChildAt(nodeIndex);
		}

		// Add the new shapes to the node using the AddShapeToNode shape
		// visitor class

		AddShapeToNode addShape = new AddShapeToNode(this.parentNode);

		for (IShape primitiveShape : primitiveShapes) {
			primitiveShape.acceptShapeVisitor(addShape);
		}

		for (IShape complexShape : complexShapes) {
			complexShape.acceptShapeVisitor(addShape);
		}
	}
}
