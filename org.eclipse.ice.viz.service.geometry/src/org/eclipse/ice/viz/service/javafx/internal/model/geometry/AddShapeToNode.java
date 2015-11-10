// package org.eclipse.ice.viz.service.javafx.internal.model.geometry;
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
// import org.eclipse.ice.viz.service.javafx.internal.model.FXShape;
//
// import javafx.scene.Group;
// import javafx.scene.Node;
// import javafx.scene.shape.Mesh;
//
/// **
// * Adds all visited shapes to the JME3 scene graph node
// *
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
// /**
// * Initializes the instance with the given JME3 node
// *
// * @param node
// */
// public AddShapeToNode(Group node) {
// this.node = node;
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
// complexShapeNode.getProperties().put(IShape.class, complexShape);
// complexShapeNode.getTransforms().setAll(Util.convertTransformation(complexShape.getTransformation()));
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
//
// FXShape shape = new FXShape(shapeType);
//
// shape.getProperties().put(IShape.class, primitiveShape);
// shape.getTransforms().setAll(Util.convertTransformation(primitiveShape.getTransformation()));
//
// // Attach the Geometry to a parent
// this.node.getChildren().add(shape);
//
// return;
// }
// }