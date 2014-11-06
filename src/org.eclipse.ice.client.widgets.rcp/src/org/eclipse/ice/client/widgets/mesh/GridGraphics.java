package org.eclipse.ice.client.widgets.mesh;

import java.util.concurrent.Callable;

import org.eclipse.ice.client.widgets.jme.SimpleAppState;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Quad;
import com.jme3.util.BufferUtils;

/**
 * This class is designed to manage the graphics representing the Mesh Editor's
 * workspace or environment. Currently, it consists of a bounded grid. To change
 * the size or scale of the grid, update the grid's {@link #dimensions}.
 * 
 * @author Jordan
 * 
 */
public class GridGraphics {

	/**
	 * The {@code SimpleAppState} hosting this {@code GridGraphics}. This is
	 * required so changes to the grid's {@link #dimensions} will be reflected
	 * in the scene graphics.
	 */
	private final SimpleAppState app;

	/**
	 * The root {@code Node} for the grid graphics. This is for convenience.
	 */
	private Node node;

	/**
	 * The backround behind the surface of the grid.
	 */
	private Geometry background;

	/**
	 * The {@link Geometry} representing the surface of the grid.
	 */
	private Geometry surface;

	/**
	 * The border of the grid. This is the black region that signifies the edge
	 * of the grid. If the background and border are the same color, then this
	 * is no longer necessary.
	 */
	private Geometry border;

	/**
	 * The major grid lines.
	 */
	private Geometry majorGrid;

	/**
	 * The minor grid lines.
	 */
	private Geometry minorGrid;

	/**
	 * The dimensions of the grid. Apply any changes directly to this variable
	 * via {@link GridDimensions#setDimensions(float, float, float)}. Any valid
	 * changes will result in a call to {@link #refresh()} to update the
	 * graphics.
	 */
	public final GridDimensions dimensions = new GridDimensions() {
		@Override
		public void dimensionsChanged() {
			app.enqueue(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					refresh();
					return true;
				}
			});
		}
	};

	/**
	 * Creates a new {@code GridGraphics} object.
	 * 
	 * @param renderQueue
	 *            The {@code IRenderQueue} used to update the scene. This is
	 *            required so changes to the grid's {@link #dimensions} will be
	 *            reflected in the scene graphics.
	 */
	public GridGraphics(SimpleAppState app) {
		this.app = app;
	}

	/**
	 * Initializes the grid graphics.
	 */
	public void init() {

		Geometry geometry;
		Node node;
		Quad quad;
		Grid wireGrid;
		Material material;

		Quaternion rotation = new Quaternion(new float[] { FastMath.HALF_PI,
				0f, 0f });

		float width = dimensions.getXWorldLength();
		float length = dimensions.getYWorldLength();
		float minX = dimensions.getXWorldMin();
		float minY = dimensions.getYWorldMin();

		// Create the GridGraphics' own "root" Node.
		node = new Node("gridGraphics");
		this.node = node;

		// Create a background that lies beneath the blue grid.
		quad = new Quad(width * 4f, length * 4f);
		geometry = new Geometry("gridBackground", quad);
		geometry.setMaterial(app.createBasicMaterial(ColorRGBA.Gray));
		// Center the background on the origin.
		geometry.setLocalTranslation(width * 4f * -0.5f, length * 4f * -0.5f,
				-5f);
		node.attachChild(geometry);
		background = geometry;

		// Create the blue (major) grid.
		wireGrid = new Grid((int) length + 1, (int) width + 1, 1f);
		wireGrid.setLineWidth(2f);
		geometry = new Geometry("gridMajor", wireGrid);
		material = app.createBasicMaterial(ColorRGBA.Blue);
		material.getAdditionalRenderState().setWireframe(true);
		geometry.setMaterial(material);
		// Rotate the grid and center it on the origin.
		geometry.setLocalRotation(rotation);
		geometry.setLocalTranslation(width * -0.5f, length * 0.5f, 0f);
		node.attachChild(geometry);
		majorGrid = geometry;

		// Create the minor grid.
		wireGrid = new Grid((int) length * 4 + 1, (int) width * 4 + 1, 0.25f);
		wireGrid.setLineWidth(1f);
		geometry = new Geometry("gridMinor", wireGrid);
		material = app.createBasicMaterial(ColorRGBA.Blue);
		material.getAdditionalRenderState().setWireframe(true);
		geometry.setMaterial(material);
		// Rotate the grid and center it on the origin.
		geometry.setLocalRotation(rotation);
		geometry.setLocalTranslation(width * -0.5f, length * 0.5f, 0f);
		node.attachChild(geometry);
		minorGrid = geometry;

		// Create the invisible surface of the grid that will register ray hits.
		quad = new Quad(width * 4f, length * 4f);
		geometry = new Geometry("gridSurface", quad);
		material = app.createBasicMaterial(ColorRGBA.BlackNoAlpha);
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		geometry.setMaterial(material);
		geometry.setQueueBucket(Bucket.Transparent);
		// Center the surface on the origin.
		geometry.setLocalTranslation(width * 4f * -0.5f, length * 4f * -0.5f,
				0f);
		node.attachChild(geometry);
		surface = geometry;

		// Create the mesh surrounding the grid. This is basically a large Quad
		// with a hole in the middle for the grid.
		Mesh mesh = new Mesh();
		// Set the distance from the grid over which the mesh should extend to
		// hide the gray background.
		float d = 50f;
		float w = width, l = length;
		Vector3f vertices[] = { new Vector3f(-d, -d, 0f),
				new Vector3f(0f, -d, 0f), new Vector3f(w + d, -d, 0f),
				new Vector3f(0f, 0f, 0f), new Vector3f(w, 0f, 0f),
				new Vector3f(w + d, 0f, 0f), new Vector3f(-d, l, 0f),
				new Vector3f(0f, l, 0f), new Vector3f(w, l, 0f),
				new Vector3f(-d, l + d, 0f), new Vector3f(w, l + d, 0f),
				new Vector3f(w + d, l + d, 0f) };
		for (Vector3f vertex : vertices) {
			vertex.addLocal(minX, minY, 0f);
		}
		int indices[] = { 0, 7, 6, 0, 1, 7, 1, 2, 3, 2, 5, 3, 4, 5, 11, 4, 11,
				10, 8, 10, 9, 6, 8, 9 };
		mesh.setBuffer(Type.Position, 3,
				BufferUtils.createFloatBuffer(vertices));
		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));

		// Create a Geometry from the mesh and set its material.
		geometry = new Geometry("background", mesh);
		geometry.setMaterial(app.createBasicMaterial(ColorRGBA.Black));
		// This call is necessary so that the mesh updates its bounds properly.
		geometry.updateModelBound();

		// Add the cover mesh to the scene.
		node.attachChild(geometry);
		border = geometry;

		return;
	}

	/**
	 * Refreshes the grid graphics, assuming they have already been initialized.
	 */
	private void refresh() {
		// TODO
	}

	/**
	 * Gets the root {@code Node} for the grid graphics. This is for
	 * convenience.
	 * 
	 * @return The {@code GridGraphics}' "root" {@code Node}.
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Gets the {@link Geometry} representing the surface of the grid.
	 * 
	 * @return The grid surface.
	 */
	public Geometry getSurface() {
		return surface;
	}

	/**
	 * Gets a valid point on the grid closest to a particular vector.
	 * 
	 * @param point
	 *            The point for which we need the nearest grid point.
	 * @return A new vector for the point on the grid closest to the specified
	 *         vector. <b>Note:</b> This is in jME world units, not model units.
	 */
	protected Vector3f getClosestGridPoint(Vector3f point) {
		Vector3f newPoint = new Vector3f(FastMath.clamp(point.x,
				dimensions.getXWorldMin(), dimensions.getXWorldMax()),
				FastMath.clamp(point.y, dimensions.getYWorldMin(),
						dimensions.getYWorldMax()), 0f);
		return newPoint;
	}
}
