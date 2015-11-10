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
package org.eclipse.ice.viz.service.javafx.internal.model.geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.ice.viz.service.geometry.scene.base.GeometryAttachment;
import org.eclipse.ice.viz.service.geometry.scene.base.IGeometry;
import org.eclipse.ice.viz.service.geometry.scene.model.IAttachment;
import org.eclipse.ice.viz.service.geometry.scene.model.INode;
import org.eclipse.ice.viz.service.geometry.shapes.IShape;
import org.eclipse.ice.viz.service.geometry.shapes.OperatorType;
import org.eclipse.ice.viz.service.geometry.shapes.ShapeType;
import org.eclipse.ice.viz.service.javafx.internal.Util;
import org.eclipse.ice.viz.service.javafx.internal.model.FXShape;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.Shape;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Mesh;
import javafx.scene.transform.Transform;

/**
 * <p>
 * JavaFX implementation of GeometryAttachment.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class FXGeometryAttachment extends GeometryAttachment {

	/**
	 * Node used to attach geometry to (instead of the root, to keep things
	 * easier to manipulate).
	 */
	private Group fxAttachmentNode;

	/** The manager that owns this attachment. */
	private final FXGeometryAttachmentManager manager;

	/** Maps ICE Geometry shapes to JavaFX shapes. */
	private Map<Shape, Node> fxShapeMap;

	/** */
	private List<Shape> knownGeometry;

	/**
	 * <p>
	 * Creates an FXGeometryAttachment instance.
	 * </p>
	 * 
	 * @param manager
	 *            the manager that created this instance.
	 */
	public FXGeometryAttachment(FXGeometryAttachmentManager manager) {
		this.manager = manager;
	}

	/**
	 * @see GeometryAttachment#attach(INode)
	 */
	@Override
	public void attach(INode owner) {
		super.attach(owner);

		if (fxAttachmentNode == null) {
			fxAttachmentNode = new Group();
		}

		Group fxNode = Util.getFxGroup(owner);
		fxNode.getChildren().add(fxAttachmentNode);
	}

	/**
	 * @see IAttachment#detach(INode)
	 */
	@Override
	public void detach(INode owner) {
		Group fxNode = Util.getFxGroup(owner);

		if (fxAttachmentNode != null) {
			fxNode.getChildren().remove(fxAttachmentNode);
		}

		super.detach(owner);
	}

	/**
	 * @see IAttachment#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return false;
	}

	/**
	 * @see IGeometry#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		fxAttachmentNode.setVisible(visible);
	}

	@Override
	public void addGeometry(Shape geom) {
		super.addGeometry(geom);

		if (fxAttachmentNode == null) {
			fxAttachmentNode = new Group();
		}

		if (knownGeometry == null) {
			knownGeometry = new ArrayList<>();
		}

		if (!knownGeometry.contains(geom)) {
			geom.register(new IVizUpdateableListener() {
				@Override
				public void update(IVizUpdateable component) {
					SyncShapes sync = new SyncShapes(fxAttachmentNode);

					/**
					 * The temporary working list of PrimitiveShapes
					 */
					ArrayList<Shape> primitiveShapes = new ArrayList<Shape>();

					/**
					 * The temporary working list of ComplexShapes
					 */
					ArrayList<Shape> complexShapes = new ArrayList<Shape>();

					// Add each shape to the correct list
					for (AbstractController shape : geom.getEntitiesByCategory("Children")) {
						if (shape.getProperty("Type") != null) {
							primitiveShapes.add((Shape) shape);
						}

						else {
							complexShapes.add((Shape) shape);
						}
					}

					javafx.application.Platform.runLater(new Runnable() {
						@Override
						public void run() {
							sync.commit();
						}
					});
				}
			});

			knownGeometry.add(geom);
		}
	}

	/**
	 * <p>
	 * Generates JavaFX shapes from the input IShape.
	 * </p>
	 * 
	 * @param shape
	 *            an ICE Geometry IShape
	 */
	@Override
	public void processShape(Shape shape) {

		if (fxShapeMap == null) {
			fxShapeMap = new HashMap<>();
		}

		// If the Operator property is not set, add a shape
		if (shape.getProperty("Operator") == null) {
			ShapeType type = ShapeType.valueOf(shape.getProperty("Type"));

			Transform xform = Util.convertTransformation(shape.getTransformation());

			FXShape fxShape = new FXShape(type);

			fxShape.getProperties().put(Shape.class, shape);
			// FIXME??? This used to be a Geometry, may need to make it
			// something else to keep the two separate, or just comment out
			fxShape.getProperties().put(Shape.class, currentGeom);

			fxShape.getTransforms().setAll(xform);

			fxAttachmentNode.getChildren().add(fxShape);

			fxShapeMap.put(shape, fxShape);
		}

		// If there is an Operator property, add the shape's children
		else {
			for (AbstractController subShape : shape.getEntitiesByCategory("Children")) {
				processShape((Shape) subShape);
			}
		}
	}

	/**
	 * 
	 */
	@Override
	protected void disposeShape(Shape shape) {
		Node node = fxShapeMap.get(shape);

		if (node == null) {
			return;
		}

		fxAttachmentNode.getChildren().remove(node);
	}

	public void syncShapes(Group parentNode, List<Shape> primitiveShapes, List<Shape> complexShapes) {

		// Local Declarations
		Shape nodeShape = null;
		int primitiveShapeIndex = -1;
		int complexShapeIndex = -1;

		// Create a list of Node children indices to remove at a later time
		List<Node> removeNodeIndices = new ArrayList<Node>();

		int numNodeChildren = parentNode.getChildren().size();
		for (int nodeIndex = 0; nodeIndex < numNodeChildren; nodeIndex++) {

			// Get the child node
			Node childSpatial = parentNode.getChildren().get(nodeIndex);
			Group childNode = null;

			try {
				childNode = (Group) childSpatial;
			} catch (ClassCastException e) {
				return;
			}

			// Extract the reference to the child shape if it exists
			if (childSpatial.getProperties().containsKey(Shape.class)) {
				nodeShape = (Shape) childSpatial.getProperties().get(Shape.class);
				primitiveShapeIndex = primitiveShapes.indexOf(nodeShape);
				complexShapeIndex = complexShapes.indexOf(nodeShape);
			}

			// Check if nodeShape is in one of the IShape lists

			if (primitiveShapeIndex >= 0) {
				// nodeShape is an existing PrimitiveShape

				Shape primitiveShape = primitiveShapes.get(primitiveShapeIndex);

				// Reset the transformation
				childSpatial.getTransforms().setAll(Util.convertTransformation(primitiveShape.getTransformation()));

				// Rotate 90 degrees if it's a cylinder

				/*
				 * if (primitiveShape.getType() == ShapeType.Cylinder) {
				 * childSpatial.getTransforms().(Math.PI / 2, 0.0f, 0.0f); }
				 */

				// Remove the shape from the PrimitiveShapes list
				primitiveShapes.remove(primitiveShapeIndex);
			}

			else if (complexShapeIndex >= 0) {
				// nodeShape is an existing ComplexShape

				Shape complexShape = complexShapes.get(complexShapeIndex);

				// Reset the transform

				childSpatial.getTransforms().setAll(Util.convertTransformation(complexShape.getTransformation()));

				// Synchronize each individual child in the ComplexShape

				List<AbstractController> complexShapeChildren = complexShape.getEntitiesByCategory("Children");

				// Lists of this shape's children, divided by type
				List<Shape> childPrimitiveShapes = new ArrayList<Shape>();
				List<Shape> childComplexShapes = new ArrayList<Shape>();

				// Add each child shape to the appriopriate list
				for (AbstractController complexShapeChild : complexShapeChildren) {
					if (complexShapeChild.getProperty("Type") != null) {
						childPrimitiveShapes.add((Shape) complexShapeChild);
					} else {
						childComplexShapes.add((Shape) complexShapeChild);
					}
				}

				syncShapes(childNode, childPrimitiveShapes, childComplexShapes);

				// Remove the shape from the ComplexShapes list

				complexShapes.remove(complexShapeIndex);
			}

			else {
				// nodeShape does not exist in one of the IShape lists

				removeNodeIndices.add(childSpatial);
			}
		}

		for (Node nodeIndex : removeNodeIndices) {
			nodeIndex.setVisible(false);
			parentNode.getChildren().remove(nodeIndex);
		}

		// Add the new shapes to the node using the AddShapeToNode shape
		// visitor class

		AddShapeToNode addShape = new AddShapeToNode(parentNode);

		for (Shape primitiveShape : primitiveShapes) {
			// Local Declarations
			ShapeType shapeType = ShapeType.None;
			Mesh mesh = null;

			// Get the mesh. Since we are only using primitive shapes, we can
			// reuse the meshes.
			shapeType = ShapeType.valueOf(primitiveShape.getProperty("Type"));

			FXShape shape = new FXShape(shapeType);

			shape.getProperties().put(Shape.class, primitiveShape);
			shape.getTransforms().setAll(Util.convertTransformation(primitiveShape.getTransformation()));

			// Attach the Geometry to a parent
			parentNode.getChildren().add(shape);
		}

		for (Shape complexShape : complexShapes) {

			// Only union operators are currently supported
			if (OperatorType.valueOf(complexShape.getProperty("Operator")) == OperatorType.Union) {

				// Create a new Node

				Group complexShapeNode = new Group();
				complexShapeNode.setId(complexShape.getProperty("Name"));

				complexShapeNode.getProperties().put(IShape.class, complexShape);
				complexShapeNode.getTransforms().setAll(Util.convertTransformation(complexShape.getTransformation()));

				// Loop through the shapes in the ComplexShape

				List<AbstractController> shapes = complexShape.getEntitiesByCategory("Children");

				addShapesToNode(complexShapeNode, shapes);

				// Attach the Node to a parent

				parentNode.getChildren().add(complexShapeNode);
			}
		}
	}

	public void addShapesToNode(Group parentNode, List<AbstractController> shapes) {
		for (AbstractController nativeShape : shapes) {
			if (nativeShape.getProperty("Type") != null) {
				// Local Declarations
				ShapeType shapeType = ShapeType.None;
				Mesh mesh = null;

				// Get the mesh. Since we are only using primitive shapes, we
				// can
				// reuse the meshes.
				shapeType = ShapeType.valueOf(nativeShape.getProperty("Type"));

				FXShape shape = new FXShape(shapeType);

				shape.getProperties().put(Shape.class, nativeShape);
				shape.getTransforms().setAll(Util.convertTransformation(nativeShape.getTransformation()));

				// Attach the Geometry to a parent
				parentNode.getChildren().add(shape);
			}

			else {

				// Only union operators are currently supported
				if (OperatorType.valueOf(nativeShape.getProperty("Operator")) == OperatorType.Union) {

					// Create a new Node

					Group complexShapeNode = new Group();
					complexShapeNode.setId(nativeShape.getProperty("Name"));

					complexShapeNode.getProperties().put(IShape.class, nativeShape);
					complexShapeNode.getTransforms()
							.setAll(Util.convertTransformation(nativeShape.getTransformation()));

					// Loop through the shapes in the ComplexShape

					List<AbstractController> childShapes = nativeShape.getEntitiesByCategory("Children");

					addShapesToNode(complexShapeNode, childShapes);

					// Attach the Node to a parent

					parentNode.getChildren().add(complexShapeNode);
				}
			}
		}
	}

	/**
	 *
	 * @param copy
	 * @return
	 */
	@Override
	public List<Shape> getShapes(boolean copy) {
		return super.getShapes(copy);

	}

	/**
	 *
	 */
	@Override
	public void clearShapes() {
		super.clearShapes();
	}

	/**
	 * 
	 * @return
	 */
	public FXGeometryAttachmentManager getManager() {
		return manager;
	}

	/**
	 *
	 * @return
	 */
	public javafx.scene.Node getFxParent() {
		return fxAttachmentNode;
	}

	/**
	 *
	 * @return
	 */
	public Group getFxNode() {
		return fxAttachmentNode;
	}

	/**
	 * 
	 */
	@Override
	public Class<?> getType() {
		return GeometryAttachment.class;
	}

	/**
	 * 
	 */
	public String getName() {
		if (fxAttachmentNode == null) {
			return "UNNAMED";
		}

		return fxAttachmentNode.getId();
	}

}
