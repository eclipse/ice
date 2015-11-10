/// *******************************************************************************
// * Copyright (c) 2015 UT-Battelle, LLC.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// * Initial implmentation - Andrew P. Belt
// * Tony McCrary (tmccrary@l33tlabs.com)
// *******************************************************************************/
// package org.eclipse.ice.viz.service.javafx.internal.scene;
//
// import java.util.ArrayList;
//
// import org.eclipse.ice.viz.service.geometry.shapes.ComplexShape;
// import org.eclipse.ice.viz.service.geometry.shapes.IShape;
// import org.eclipse.ice.viz.service.geometry.shapes.IShapeVisitor;
// import org.eclipse.ice.viz.service.geometry.shapes.OperatorType;
// import org.eclipse.ice.viz.service.geometry.shapes.PrimitiveShape;
// import org.eclipse.ice.viz.service.geometry.shapes.ShapeType;
// import org.eclipse.ice.viz.service.geometry.widgets.ShapeMaterial;
// import org.eclipse.ice.viz.service.javafx.internal.Util;
//
// import javafx.scene.Group;
// import javafx.scene.paint.Material;
// import javafx.scene.shape.Mesh;
// import javafx.scene.shape.MeshView;
// import javafx.scene.shape.TriangleMesh;
// import javafx.scene.shape.VertexFormat;
// import javafx.scene.transform.Rotate;
// import javafx.scene.transform.Transform;
//
/// **
// * Adds all visited shapes to the JavaFX scene graph node
// *
// * @author Tony McCrary (tmccrary@l33tlabs.com)
// * @author Andrew P. Belt
// *
// */
// class AddShapeToNode implements IShapeVisitor {
//
// /**
// * The node whose children will be added for each visited IShape
// */
// private Group node;
//
// /** */
// private Mesh[] primitiveMeshes;
//
// /** */
// private Material baseMaterial;
//
// /** */
// private Material highlightedMaterial;
//
// /**
// * Initializes the instance with the given JavaFX node
// *
// * @param node
// */
// public AddShapeToNode(Group node) {
// this.node = node;
//
// baseMaterial = Util.DEFAULT_MATERIAL;
// highlightedMaterial = Util.DEFAULT_HIGHLIGHTED_MATERIAL;
// }
//
// /**
// * Prepares the ComplexShape to be synchronized
// */
// @Override
// public void visit(ComplexShape complexShape) {
//
// // Only unions are currently supported until the mesh renderer is
// // implemented
//
// if (complexShape.getType() == OperatorType.Union) {
//
// // Create a new Node
//
// Group complexShapeNode = new Group();
// complexShapeNode.setId(complexShape.getName());
//
// complexShapeNode.getProperties().put(Util.SHAPE_PROP_KEY, complexShape);
//
// complexShapeNode.getTransforms().add(Util.convertTransformation(complexShape.getTransformation()));
//
// // Loop through the shapes in the ComplexShape
//
// ArrayList<IShape> shapes = complexShape.getShapes();
//
// AddShapeToNode addShapeToNode = new AddShapeToNode(complexShapeNode);
//
// for (IShape shape : shapes) {
//
// // Visit the shape to add it to the current spatial
//
// shape.acceptShapeVisitor(addShapeToNode);
// }
//
// // Attach the Node to a parent
//
// this.node.getChildren().add(complexShapeNode);
// }
// }
//
// /**
// * Prepares the PrimitiveShape to be synchronized
// */
// @Override
// public void visit(PrimitiveShape primitiveShape) {
//
// // Local Declarations
// ShapeType shapeType = ShapeType.None;
// Mesh mesh = null;
//
// // Get the mesh. Since we are only using primitive shapes, we can
// // reuse the meshes.
// shapeType = primitiveShape.getType();
// if (shapeType != ShapeType.None && shapeType != ShapeType.Cone) {
// mesh = primitiveMeshes[shapeType.ordinal()];
// } else {
// return;
// }
// // Create geometry and store the reference to the shape data
// // structure
//
// Group meshGroup = new Group();
// TriangleMesh geom = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
//
// MeshView geomMeshView = new MeshView(geom);
// meshGroup.getChildren().add(geomMeshView);
//
// meshGroup.getProperties().put(Util.SHAPE_PROP_KEY, primitiveShape);
// meshGroup.getTransforms().setAll(Util.convertTransformation(primitiveShape.getTransformation()));
//
// // Cylinders need to be rotated 90 degrees on the x-axis
// if (shapeType == ShapeType.Cylinder) {
// Rotate rotate = Transform.rotate(90.0f, 0.0f, 0.0f);
// meshGroup.getTransforms().add(rotate);
// }
//
// // Give it a material
// ShapeMaterial shapeMaterial = new ShapeMaterial(primitiveShape);
// // Set the base material that is used for all the shapes
// shapeMaterial.setMaterial(baseMaterial);
// // Set the base material that is used for all selected shapes
// shapeMaterial.setHighlightedMaterial(highlightedMaterial);
// // Get the proper material from the shapeMaterial. It will change
// // depending on whether or not it is selected.
// geomMeshView.setMaterial(shapeMaterial.getMaterial());
//
// // Attach the Geometry to a parent
// this.node.getChildren().add(geomMeshView);
// }
// }