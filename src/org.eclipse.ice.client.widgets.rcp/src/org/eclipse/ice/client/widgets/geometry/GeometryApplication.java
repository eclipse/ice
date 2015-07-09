/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.geometry.IShapeVisitor;
import org.eclipse.ice.datastructures.form.geometry.OperatorType;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;
import org.eclipse.ice.datastructures.form.geometry.ShapeType;
import org.eclipse.ice.datastructures.form.geometry.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;

/**
 * <p>
 * The JME3 entry point for rendering the geometry editor viewport
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class GeometryApplication extends SimpleApplication implements
		IUpdateableListener {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(GeometryApplication.class);

	/**
	 * Some of the code in this file was adapted from the JME3 tutorials and
	 * some of it was adapted from jmodelview, which can be found here:
	 * https://bitbucket
	 * .org/rherriman/jmodelview/src/5a1078c60879/src/jmodelview/ModelView.java
	 */

	/**
	 * The currently displayed GeometryComponent
	 */
	private GeometryComponent geometry;

	/**
	 * Defines whether the geometry has changed since the last rerender
	 */
	private boolean geometryHasChanged = true;

	/**
	 * The directional light that follows the camera
	 */
	DirectionalLight cameraLight;

	/**
	 * Vectors along X,Y and Z that define the axial direction
	 */
	private static Vector3f xPoint = new Vector3f(3.0f, 0.0f, 0.0f);
	private static Vector3f yPoint = new Vector3f(0.0f, 3.0f, 0.0f);
	private static Vector3f zPoint = new Vector3f(0.0f, 0.0f, 3.0f);

	/**
	 * Leaf nodes in the scene graph that show each axis
	 */
	private static Geometry xAxis = new Geometry("xAxis", new Line(
			xPoint.negate(), xPoint));
	private static Geometry yAxis = new Geometry("yAxis", new Line(
			yPoint.negate(), yPoint));
	private static Geometry zAxis = new Geometry("zAxis", new Line(
			zPoint.negate(), zPoint));

	/**
	 * A material for the axis and labels that define each.
	 */
	private Material axisMaterial;
	private BitmapText xLabel, yLabel, zLabel;

	/**
	 * An array to hold the meshes for the primitive types. This array is
	 * indexed by the integer value of the ShapeType instance, starting at 0 for
	 * "None" and ending at 5 for "Tube."
	 */
	private Mesh[] primitiveMeshes = new Mesh[6];

	/**
	 * A material that will be used as the default for all materials in the
	 * geometry. It is configured upon start and shared when the shapes are
	 * visited.
	 */
	private Material baseMaterial = null;

	/**
	 * A material that will be used as the default for all selected materials in
	 * the geometry. It is configured upon start and shared when the shapes are
	 * visited.
	 */
	private Material highlightedMaterial = null;

	/**
	 * Initializes the GeometryApplication when the display needs to be created
	 */
	@Override
	public void simpleInitApp() {

		logger.info("Opening jME3 geometry renderer...");

		// Turn off near clipping on the geometry
		cam.setFrustumPerspective(45f,
				(float) cam.getWidth() / cam.getHeight(), 0.01f, 1000f);

		// Set up the camera
		FlyByCamera cam = getFlyByCamera();
		cam.setDragToRotate(true);
		cam.setMoveSpeed(13.0f);

		// Set the gui
		setDisplayStatView(false);

		// Add ambient light
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.LightGray.mult(0.5f));
		rootNode.addLight(al);

		ColorRGBA MediumGray = new ColorRGBA((float) .3, (float) .3,
				(float) .3, 1);

		// Setup the light source that is emitted from behind the camera
		cameraLight = new DirectionalLight();
		Vector3f position1 = new Vector3f(1.0f, 0.0f, 0.0f);
		cameraLight.setColor(MediumGray);
		cameraLight.setDirection(position1);
		rootNode.addLight(cameraLight);

		// Setup the light in the upper corner shining orange light from
		// infinity
		DirectionalLight upperCornerLight = new DirectionalLight();
		Vector3f position2 = new Vector3f(1.0f, 1.0f, 1.0f);
		upperCornerLight.setColor(ColorRGBA.Orange);
		upperCornerLight.setDirection(position2);
		rootNode.addLight(upperCornerLight);

		// Setup the light in the lower corner shining blue light from infinity
		// (in the opposite direction of the orange light)
		DirectionalLight lowerCornerLight = new DirectionalLight();
		Vector3f position3 = new Vector3f(-1.0f, -1.0f, -1.0f);
		lowerCornerLight.setColor(ColorRGBA.Blue);
		lowerCornerLight.setDirection(position3);
		rootNode.addLight(lowerCornerLight);

		// Create the x axis
		axisMaterial = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		axisMaterial.setColor("Color", ColorRGBA.Green);
		xAxis.setMaterial(axisMaterial);
		rootNode.attachChild(xAxis);
		xLabel = new BitmapText(guiFont, false);
		xLabel.setSize(guiFont.getCharSet().getRenderedSize());
		xLabel.setColor(ColorRGBA.Green);
		xLabel.setText("+X");
		guiNode.attachChild(xLabel);

		// Create the y axis
		axisMaterial = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		axisMaterial.setColor("Color", ColorRGBA.Red);
		yAxis.setMaterial(axisMaterial);
		rootNode.attachChild(yAxis);
		yLabel = new BitmapText(guiFont, false);
		yLabel.setSize(guiFont.getCharSet().getRenderedSize());
		yLabel.setColor(ColorRGBA.Red);
		yLabel.setText("+Y");
		guiNode.attachChild(yLabel);

		// Create the z axis
		axisMaterial = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		axisMaterial.setColor("Color", ColorRGBA.Blue);
		zAxis.setMaterial(axisMaterial);
		rootNode.attachChild(zAxis);
		zLabel = new BitmapText(guiFont, false);
		zLabel.setSize(guiFont.getCharSet().getRenderedSize());
		zLabel.setColor(ColorRGBA.Blue);
		zLabel.setText("+Z");
		guiNode.attachChild(zLabel);

		// // Add ambient occlusion shadows

		// Setup the array of primitive meshes. Meshes None and Cone have no
		// representation.
		primitiveMeshes[0] = null; // None
		primitiveMeshes[1] = new Sphere(15, 30, 0.5f);
		primitiveMeshes[2] = new Box(Vector3f.ZERO, 0.5f, 0.5f, 0.5f);
		primitiveMeshes[3] = new Cylinder(5, 30, 0.5f, 1.0f, true, false);
		primitiveMeshes[0] = null; // Cone
		primitiveMeshes[5] = new Tube(0.5f, 0.4f, 1.0f, 5, 30);

		// Create the base material
		baseMaterial = new Material(assetManager,
				"Common/MatDefs/Light/Lighting.j3md");
		baseMaterial.setBoolean("UseMaterialColors", true);
		float alpha = 1.0f;
		ColorRGBA color = new ColorRGBA(1.0f, 1.0f, 1.0f, alpha);
		baseMaterial.setColor("Diffuse", color);
		baseMaterial.setFloat("Shininess", 12);
		baseMaterial.setColor("Ambient", ColorRGBA.White);
		baseMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

		// Create the highlighted material used when a shape is selected
		highlightedMaterial = new Material(assetManager,
				"Common/MatDefs/Light/Lighting.j3md");
		highlightedMaterial.setBoolean("UseMaterialColors", true);
		color = new ColorRGBA(1.0f, 0.0f, 0.0f, 1.0f); // Red
		highlightedMaterial.setColor("Diffuse", color);
		highlightedMaterial.setFloat("Shininess", 12);
		highlightedMaterial.setColor("Ambient", ColorRGBA.White);
		highlightedMaterial.getAdditionalRenderState().setBlendMode(
				BlendMode.Alpha);

		return;
	}

	@Override
	public void simpleUpdate(float tpf) {

		// Updates the geometry if it has changed
		if (geometryHasChanged) {
			syncGeometry();
			geometryHasChanged = false;
		}

		// Update the direction of the light
		cameraLight.setDirection(cam.getDirection());

		// Update directions of the axis labels
		xLabel.setLocalTranslation(cam.getScreenCoordinates(xPoint));
		yLabel.setLocalTranslation(cam.getScreenCoordinates(yPoint));
		zLabel.setLocalTranslation(cam.getScreenCoordinates(zPoint));

		return;
	}

	/**
	 * Initializes the CSG tree given a GeometryComponent and prepares for
	 * rendering
	 * 
	 * @param geometry
	 *            The geometry to render
	 */
	public synchronized void loadGeometry(GeometryComponent geometry) {

		if (geometry == null) {
			return;
		}
		this.geometry = geometry;

		// Loop through each shape in the geometry

		AddShapeToNode addShapeToNode = new AddShapeToNode(rootNode);

		for (IShape shape : geometry.getShapes()) {

			// Trigger the visitor pattern to render the IShape

			shape.acceptShapeVisitor(addShapeToNode);
		}

		// Listen to the GeometryComponent

		geometry.register(this);
	}

	/**
	 * Synchronizes the current state of the scene graph with the current
	 * geometry
	 */
	public void syncGeometry() {

		synchronized (geometry) {

			// Get the shapes list from the GeometryComponent

			ArrayList<IShape> shapes = geometry.getShapes();

			SyncShapes syncVisitor = new SyncShapes(rootNode);

			// Add each shape from the GeometryComponent to the SyncShapes

			for (IShape shape : shapes) {
				shape.acceptShapeVisitor(syncVisitor);
			}

			// Commit the synchronization

			syncVisitor.commit();
		}
	}

	/**
	 * Adds all visited shapes to the JME3 scene graph node
	 * 
	 * @author Andrew P. Belt
	 * 
	 */
	class AddShapeToNode implements IShapeVisitor {

		/**
		 * The node whose children will be added for each visited IShape
		 */
		private Node node;

		/**
		 * Initializes the instance with the given JME3 node
		 * 
		 * @param node
		 */
		public AddShapeToNode(Node node) {
			this.node = node;
		}

		/**
		 * Prepares the ComplexShape to be synchronized
		 */
		@Override
		public void visit(ComplexShape complexShape) {

			// Only unions are currently supported until the mesh renderer is
			// implemented

			if (complexShape.getType() == OperatorType.Union) {

				// Create a new Node

				Node complexShapeNode = new Node(complexShape.getName());
				complexShapeNode.setUserData("shape", new ShapeTransient(
						complexShape));
				complexShapeNode
						.setLocalTransform(convertTransformation(complexShape
								.getTransformation()));

				// Loop through the shapes in the ComplexShape

				ArrayList<IShape> shapes = complexShape.getShapes();

				AddShapeToNode addShapeToNode = new AddShapeToNode(
						complexShapeNode);

				for (IShape shape : shapes) {

					// Visit the shape to add it to the current spatial

					shape.acceptShapeVisitor(addShapeToNode);
				}

				// Attach the Node to a parent

				this.node.attachChild(complexShapeNode);
			}
		}

		/**
		 * Prepares the PrimitiveShape to be synchronized
		 */
		@Override
		public void visit(PrimitiveShape primitiveShape) {

			// Local Declarations
			ShapeType shapeType = ShapeType.None;
			Mesh mesh = null;

			// If the assetManager doesn't exist, don't render the shape
			if (assetManager == null) {
				return;
			}
			// Get the mesh. Since we are only using primitive shapes, we can
			// reuse the meshes.
			shapeType = primitiveShape.getType();
			if (shapeType != ShapeType.None && shapeType != ShapeType.Cone) {
				mesh = primitiveMeshes[shapeType.ordinal()];
			} else {
				return;
			}
			// Create geometry and store the reference to the shape data
			// structure
			Geometry geom = new Geometry(primitiveShape.getName(), mesh);
			geom.setUserData("shape", new ShapeTransient(primitiveShape));
			geom.setLocalTransform(convertTransformation(primitiveShape
					.getTransformation()));

			// Cylinders need to be rotated 90 degrees on the x-axis

			if (shapeType == ShapeType.Cylinder) {
				geom.rotate(FastMath.PI / 2, 0.0f, 0.0f);
			}

			// Give it a material
			ShapeMaterial shapeMaterial = new ShapeMaterial(assetManager,
					primitiveShape);
			// Set the base material that is used for all the shapes
			shapeMaterial.setMaterial(baseMaterial);
			// Set the base material that is used for all selected shapes
			shapeMaterial.setHighlightedMaterial(highlightedMaterial);
			// Get the proper material from the shapeMaterial. It will change
			// depending on whether or not it is selected.
			geom.setMaterial(shapeMaterial.getMaterial());

			// FIXME! - Great inside joke, Andrew! However, we actually need to
			// know why it works and document it in here. Please fix it!

			// I have no idea why this works...

			// But it does.
			geom.setQueueBucket(Bucket.Transparent);

			// Attach the Geometry to a parent
			this.node.attachChild(geom);

			return;
		}
	}

	/**
	 * Synchronizes the given JME3 node with each visited IShape child
	 * 
	 * @author Andrew P. Belt
	 * 
	 */
	class SyncShapes implements IShapeVisitor {

		/**
		 * The JME3 scene graph node to update
		 */
		private Node parentNode;

		/**
		 * The temporary working list of PrimitiveShapes
		 */
		private ArrayList<PrimitiveShape> primitiveShapes = new ArrayList<PrimitiveShape>();

		/**
		 * The temporary working list of ComplexShapes
		 */
		private ArrayList<ComplexShape> complexShapes = new ArrayList<ComplexShape>();

		/**
		 * Initializes an instance with a JME3 scene graph node
		 * 
		 * @param parentNode
		 *            The parent of the nodes associated with the visited
		 *            IShapes
		 */
		public SyncShapes(Node parentNode) {
			this.parentNode = parentNode;
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

			int numNodeChildren = this.parentNode.getQuantity();
			for (int nodeIndex = 0; nodeIndex < numNodeChildren; nodeIndex++) {

				// Get the child node
				Spatial childSpatial = this.parentNode.getChild(nodeIndex);
				Node childNode = null;

				try {
					childNode = (Node) childSpatial;
				} catch (ClassCastException e) {
				}

				// Extract the reference to the child shape if it exists
				if (childSpatial.getUserData("shape") != null) {
					nodeShape = ((ShapeTransient) childSpatial
							.getUserData("shape")).getShape();
					primitiveShapeIndex = primitiveShapes.indexOf(nodeShape);
					complexShapeIndex = complexShapes.indexOf(nodeShape);
				}

				// Check if nodeShape is in one of the IShape lists

				if (primitiveShapeIndex >= 0) {
					// nodeShape is an existing PrimitiveShape

					PrimitiveShape primitiveShape = primitiveShapes
							.get(primitiveShapeIndex);

					// Reset the transformation

					childSpatial
							.setLocalTransform(convertTransformation(primitiveShape
									.getTransformation()));

					// Rotate 90 degrees if it's a cylinder

					if (primitiveShape.getType() == ShapeType.Cylinder) {
						childSpatial.rotate(FastMath.PI / 2, 0.0f, 0.0f);
					}

					// Reset the material - remember to use the baseMaterial and
					// highlightedMaterial!
					ShapeMaterial shapeMaterial = new ShapeMaterial(
							assetManager, primitiveShape);
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
							.setLocalTransform(convertTransformation(complexShape
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
				this.parentNode.detachChildAt(nodeIndex);
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

	/**
	 * Triggers the synchronization whenever the GeometryComponent updates
	 */
	@Override
	public void update(IUpdateable component) {

		// Check that the updated component is the correct GeometryComponent

		if (component == geometry) {

			// Mark the geometry as changed

			geometryHasChanged = true;
		}
	}

	/**
	 * Converts a geometry Transformation data structure to a JME3 Transform
	 * 
	 * @param The
	 *            Geometry transformation data structure
	 * @return The JME3 transformation
	 */
	private Transform convertTransformation(Transformation transformation) {

		// Create the JME3 transform

		Transform transform = new Transform();

		double size = transformation.getSize();
		double[] scale = transformation.getScale();
		double[] rotation = transformation.getRotation();
		double[] translation = transformation.getTranslation();

		// Set the transformation values

		transform.setScale((float) (size * scale[0]),
				(float) (size * scale[1]), (float) (size * scale[2]));

		// Create a quaternion from the angle measurements on each axis,
		// and apply it to the Transform

		Quaternion rotationQuat = new Quaternion();
		rotationQuat.fromAngles((float) rotation[0], (float) rotation[1],
				(float) rotation[2]);
		transform.setRotation(rotationQuat);

		// Translate it last

		transform.setTranslation((float) translation[0],
				(float) translation[1], (float) translation[2]);

		return transform;
	}
}
