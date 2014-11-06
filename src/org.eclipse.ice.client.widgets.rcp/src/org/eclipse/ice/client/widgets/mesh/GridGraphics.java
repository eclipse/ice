package org.eclipse.ice.client.widgets.mesh;

import java.util.concurrent.Callable;

import org.eclipse.ice.client.widgets.jme.IRenderQueue;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

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
	 * The {@code IRenderQueue} used to update the scene. This is required so
	 * changes to the grid's {@link #dimensions} will be reflected in the scene
	 * graphics.
	 */
	private final IRenderQueue renderQueue;

	/**
	 * The {@link Geometry} representing the surface of the grid.
	 */
	private final Geometry surface;

	/**
	 * The dimensions of the grid. Apply any changes directly to this variable
	 * via {@link GridDimensions#setDimensions(float, float, float)}. Any valid
	 * changes will result in a call to {@link #refresh()} to update the
	 * graphics.
	 */
	public final GridDimensions dimensions = new GridDimensions() {
		@Override
		public void dimensionsChanged() {
			renderQueue.enqueue(new Callable<Boolean>() {
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
	public GridGraphics(IRenderQueue renderQueue) {
		this.renderQueue = renderQueue;

		// TODO Initialize the surface geometry.
		surface = null;
	}

	/**
	 * Refreshes the grid graphics, assuming they have already been initialized.
	 */
	private void refresh() {
		// TODO
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
