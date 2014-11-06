package org.eclipse.ice.client.widgets.mesh;


public abstract class GridDimensions {

	public final static float minScale = 0.0000000001f;
	public final static float maxScale = 10000000f;

	public final static float minWorldUnits = 1f;
	public final static float maxWorldUnits = 1000f;

	private float scale;

	private float xUnits;
	private float xMax;
	private float xMin;

	private float yUnits;
	private float yMax;
	private float yMin;

	private float xWorldUnits;
	private float xWorldMax;
	private float xWorldMin;

	private float yWorldUnits;
	private float yWorldMax;
	private float yWorldMin;

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
	public void setDimensions(float scale, float xUnits, float yUnits) {
		if (validate(scale, xUnits, yUnits)) {
			this.scale = scale;
			this.xUnits = xUnits;
			this.yUnits = yUnits;
			refresh();
		}

		return;
	}

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
	}

	/**
	 * This is called when the dimensions have changed.
	 */
	public abstract void dimensionsChanged();

	public float getScale() {
		return scale;
	}

	public float getXLength() {
		return xUnits;
	}

	public float getXMax() {
		return xMax;
	}

	public float getXMin() {
		return xMin;
	}

	public float getYLength() {
		return yUnits;
	}

	public float getYMax() {
		return yMax;
	}

	public float getYMin() {
		return yMin;
	}

	public float getXWorldLength() {
		return xWorldUnits;
	}

	public float getXWorldMax() {
		return xWorldMax;
	}

	public float getXWorldMin() {
		return xWorldMin;
	}

	public float getYWorldLength() {
		return yWorldUnits;
	}

	public float getYWorldMax() {
		return yWorldMax;
	}

	public float getYWorldMin() {
		return yWorldMin;
	}
}
