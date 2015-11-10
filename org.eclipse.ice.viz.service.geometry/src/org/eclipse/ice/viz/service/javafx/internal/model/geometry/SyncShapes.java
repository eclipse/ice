// package org.eclipse.ice.viz.service.javafx.internal.model.geometry;
//
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
// import java.util.Vector;
//
// import org.eclipse.ice.viz.service.geometry.shapes.ComplexShape;
// import org.eclipse.ice.viz.service.geometry.shapes.IShape;
// import org.eclipse.ice.viz.service.geometry.shapes.IShapeVisitor;
// import org.eclipse.ice.viz.service.geometry.shapes.PrimitiveShape;
// import org.eclipse.ice.viz.service.javafx.internal.Util;
//
// import javafx.scene.Group;
// import javafx.scene.Node;
//
/// **
// * <p>
// * Sync ICE Geometry with the current scene.
// * </p>
// *
// * <p>
// * (Adapted from ICE JME3 impl)
// * </p>
// *
// * @author Andrew P. Belt
// * @author Tony McCrary (tmccrary@l33tlabs.com)
// *
// */
// class SyncShapes implements IShapeVisitor {
//
// /**
// * The JavaFX scene graph node to update
// */
// private Group parentNode;
//
// /**
// * The temporary working list of PrimitiveShapes
// */
// private ArrayList<PrimitiveShape> primitiveShapes = new
// ArrayList<PrimitiveShape>();
//
// /**
// * The temporary working list of ComplexShapes
// */
// private ArrayList<ComplexShape> complexShapes = new
// ArrayList<ComplexShape>();
//
// /**
// * Initializes an instance with a JME3 scene graph node
// *
// * @param parentNode
// * The parent of the nodes associated with the visited IShapes
// */
// public SyncShapes(Group parentNode) {
// this.parentNode = parentNode;
// }
//
// /**
// * Adds a ComplexShape to the temporary working list
// */
// @Override
// public void visit(ComplexShape complexShape) {
// complexShapes.add(complexShape);
// }
//
// /**
// * Adds a PrimitiveShape to the temporary working list
// */
// @Override
// public void visit(PrimitiveShape primitiveShape) {
// primitiveShapes.add(primitiveShape);
// }
//
// /**
// * Performs the synchronization of the node and the list of shapes
// */
// public void commit() {
//
// // Local Declarations
// IShape nodeShape = null;
// int primitiveShapeIndex = -1;
// int complexShapeIndex = -1;
//
// // Create a list of Node children indices to remove at a later time
// List<Node> removeNodeIndices = new ArrayList<Node>();
//
// int numNodeChildren = this.parentNode.getChildren().size();
// for (int nodeIndex = 0; nodeIndex < numNodeChildren; nodeIndex++) {
//
// // Get the child node
// Node childSpatial = this.parentNode.getChildren().get(nodeIndex);
// Group childNode = null;
//
// try {
// childNode = (Group) childSpatial;
// } catch (ClassCastException e) {
// return;
// }
//
// // Extract the reference to the child shape if it exists
// if (childSpatial.getProperties().containsKey(IShape.class)) {
// nodeShape = (IShape) childSpatial.getProperties().get(IShape.class);
// primitiveShapeIndex = primitiveShapes.indexOf(nodeShape);
// complexShapeIndex = complexShapes.indexOf(nodeShape);
// }
//
// // Check if nodeShape is in one of the IShape lists
//
// if (primitiveShapeIndex >= 0) {
// // nodeShape is an existing PrimitiveShape
//
// PrimitiveShape primitiveShape = primitiveShapes.get(primitiveShapeIndex);
//
// // Reset the transformation
// childSpatial.getTransforms().setAll(Util.convertTransformation(primitiveShape.getTransformation()));
//
// // Rotate 90 degrees if it's a cylinder
//
// /*
// * if (primitiveShape.getType() == ShapeType.Cylinder) {
// * childSpatial.getTransforms().(Math.PI / 2, 0.0f, 0.0f); }
// */
//
// // Remove the shape from the PrimitiveShapes list
// this.primitiveShapes.remove(primitiveShapeIndex);
// }
//
// else if (complexShapeIndex >= 0) {
// // nodeShape is an existing ComplexShape
//
// ComplexShape complexShape = complexShapes.get(complexShapeIndex);
//
// // Reset the transform
//
// childSpatial.getTransforms().setAll(Util.convertTransformation(complexShape.getTransformation()));
//
// // Synchronize each individual child in the ComplexShape
//
// SyncShapes syncComplexShapes = new SyncShapes(childNode);
// ArrayList<IShape> complexShapeChildren = complexShape.getShapes();
//
// for (IShape complexShapeChild : complexShapeChildren) {
// complexShapeChild.acceptShapeVisitor(syncComplexShapes);
// }
//
// // Perform the updating on the scene graph
//
// syncComplexShapes.commit();
//
// // Remove the shape from the ComplexShapes list
//
// this.complexShapes.remove(complexShapeIndex);
// }
//
// else {
// // nodeShape does not exist in one of the IShape lists
//
// removeNodeIndices.add(childSpatial);
// }
// }
//
// for (Node nodeIndex : removeNodeIndices) {
// nodeIndex.setVisible(false);
// this.parentNode.getChildren().remove(nodeIndex);
// }
//
// // Add the new shapes to the node using the AddShapeToNode shape
// // visitor class
//
// AddShapeToNode addShape = new AddShapeToNode(this.parentNode);
//
// for (IShape primitiveShape : primitiveShapes) {
// primitiveShape.acceptShapeVisitor(addShape);
// }
//
// for (IShape complexShape : complexShapes) {
// complexShape.acceptShapeVisitor(addShape);
// }
// }
// }