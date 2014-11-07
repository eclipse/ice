package org.eclipse.ice.client.widgets.mesh;

/**
 * This class manages the dimensions and scale of the Mesh Editor's grid. When
 * the dimensions are changed via {@link #set(float, float, float)}, the parent
 * class that created this instance will be notified via its implementation of
 * {@link #dimensionsChanged()}.
 * 
 * @author Jordan
 * 
 */
public abstract class GridDimensions {

	/**
	 * The maximum supported scale.
	 */
	public final static float maxScale = 10000000f;
	/**
	 * The minimum supported scale.
	 */
	public final static float minScale = 0.0000000001f;

	/**
	 * The maximum supported jME world units.
	 */
	public final static float maxWorldUnits = 1000f;
	/**
	 * The minimum supported jME world units.
	 */
	public final static float minWorldUnits = 1f;

	/**
	 * The scale factor that is applied to all locations in the Mesh Editor. For
	 * example, a scale of 0.5 will "shrink" the rendered mesh by 50%. A scale
	 * of 10 will "magnify" the rendered mesh by a factor of 10.
	 */
	private float scale;

	/**
	 * The width of the grid in the model's units (not in jME world units).
	 */
	private float xUnits;
	/**
	 * The maximum x value for components displayed on the grid.
	 */
	private float xMax;
	/**
	 * The minimum x value for components displayed on the grid.
	 */
	private float xMin;

	/**
	 * The height of the grid in the model's units (not in jME world units).
	 */
	private float yUnits;
	/**
	 * The maximum y value for components displayed on the grid.
	 */
	private float yMax;
	/**
	 * The minimum y value for components displayed on the grid.
	 */
	private float yMin;

	/**
	 * The total width of the grid in jME world units.
	 */
	private float xWorldUnits;
	/**
	 * The maximum x value for components displayed on the grid.
	 */
	private float xWorldMax;
	/**
	 * The minimum x value for the displayed grid.
	 */
	private float xWorldMin;

	/**
	 * The total height of the grid in jME world units.
	 */
	private float yWorldUnits;
	/**
	 * The maximum y value for components displayed on the grid.
	 */
	private float yWorldMax;
	/**
	 * The minimum y value for the displayed grid.
	 */
	private float yWorldMin;

	/**
	 * The default constructor. Initializes a grid with default scale, width,
	 * and height.
	 */
	public GridDimensions() {
		scale = 1f;
		xUnits = 32f;
		yUnits = 16f;
		refresh();
	}

	/**
	 * Validates the provided units based on the min and max allowed values for
	 * scale, width, and height.
	 * 
	 * @param scale
	 *            The scale factor that is applied to all locations in the Mesh
	 *            Editor. For example, a scale of 0.5 will "shrink" the rendered
	 *            mesh by 50%. A scale of 10 will "magnify" the rendered mesh by
	 *            a factor of 10.
	 * @param xUnits
	 *            The width of the grid in the model's units (not in jME world
	 *            units).
	 * @param yUnits
	 *            The height of the grid in the model's units (not in jME world
	 *            units).
	 */
	public boolean validate(float scale, float xUnits, float yUnits) {
		boolean valid = false;

		if (scale >= minScale && scale <= maxScale) {
			float worldX = scale * xUnits;
			float worldY = scale * yUnits;

			if (worldX >= minWorldUnits && worldX <= maxWorldUnits
					&& worldY >= minWorldUnits && worldY <= maxWorldUnits) {
				valid = true;
			}
		}
		return valid;
	}

	/**
	 * Sets the scale, width, and height of the Mesh Editor grid based on the
	 * model's units.
	 * 
	 * @param scale
	 *            The scale factor that is applied to all locations in the Mesh
	 *            Editor. For example, a scale of 0.5 will "shrink" the rendered
	 *            mesh by 50%. A scale of 10 will "magnify" the rendered mesh by
	 *            a factor of 10.
	 * @param xUnits
	 *            The width of the grid in the model's units (not in jME world
	 *            units).
	 * @param yUnits
	 *            The height of the grid in the model's units (not in jME world
	 *            units).
	 */
	public void set(float scale, float xUnits, float yUnits) {
		if (validate(scale, xUnits, yUnits)) {
			this.scale = scale;
			this.xUnits = xUnits;
			this.yUnits = yUnits;
			refresh();
			dimensionsChanged();
		}

		return;
	}

	/**
	 * This is called when the dimensions have changed.
	 */
	protected abstract void dimensionsChanged();

	/**
	 * Updates all of the values so they will not have to be recalculated later.
	 */
	private void refresh() {
		xMax = xUnits * 0.5f;
		xMin = -xMax;

		yMax = yUnits * 0.5f;
		yMin = -yMax;

		xWorldUnits = scale * xUnits;
		xWorldMax = xWorldUnits * 0.5f;
		xWorldMin = -xWorldMax;

		yWorldUnits = scale * yUnits;
		yWorldMax = yWorldUnits * 0.5f;
		yWorldMin = -yWorldMax;

		return;
	}

	/**
	 * Gets the scale factor that is applied to all locations in the Mesh
	 * Editor. For example, a scale of 0.5 will "shrink" the rendered mesh by
	 * 50%. A scale of 10 will "magnify" the rendered mesh by a factor of 10.
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Gets the width of the grid in the model's units (not in jME world units).
	 */
	public float getXLength() {
		return xUnits;
	}

	/**
	 * Gets the maximum x value for components displayed on the grid.
	 */
	public float getXMax() {
		return xMax;
	}

	/**
	 * Gets the minimum x value for components displayed on the grid.
	 */
	public float getXMin() {
		return xMin;
	}

	/**
	 * Gets the height of the grid in the model's units (not in jME world
	 * units).
	 */
	public float getYLength() {
		return yUnits;
	}

	/**
	 * Gets the maximum y value for components displayed on the grid.
	 */
	public float getYMax() {
		return yMax;
	}

	/**
	 * Gets the minimum y value for components displayed on the grid.
	 */
	public float getYMin() {
		return yMin;
	}

	/**
	 * Gets the total width of the grid in jME world units.
	 */
	public float getXWorldLength() {
		return xWorldUnits;
	}

	/**
	 * Gets the maximum x value for components displayed on the grid.
	 */
	public float getXWorldMax() {
		return xWorldMax;
	}

	/**
	 * Gets the minimum x value for the displayed grid.
	 */
	public float getXWorldMin() {
		return xWorldMin;
	}

	/**
	 * Gets the total height of the grid in jME world units.
	 */
	public float getYWorldLength() {
		return yWorldUnits;
	}

	/**
	 * Gets the maximum y value for components displayed on the grid.
	 */
	public float getYWorldMax() {
		return yWorldMax;
	}

	/**
	 * Gets the minimum y value for the displayed grid.
	 */
	public float getYWorldMin() {
		return yWorldMin;
	}
}
