package org.eclipse.ice.client.widgets.mesh.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.ice.client.widgets.mesh.GridDimensions;
import org.junit.Test;

public class GridDimensionsTester {

	/**
	 * A delta value required for comparing floats and doubles.
	 */
	private static final float delta = 1e-7f;

	/**
	 * Checks the default values for a {@link GridDimensions}.
	 */
	@Test
	public void checkDefaults() {
		GridDimensions dims = new GridDimensions() {
			@Override
			public void dimensionsChanged() {
				// Nothing to do.
			}
		};

		// Set the default values.
		float scale = 1f;
		float x = 32f;
		float y = 16f;
		float xWorld = x;
		float yWorld = y;

		compareValues(dims, scale, x, y, xWorld, yWorld);

		return;
	}

	/**
	 * Checks the ability to set the dimensions for a {@link GridDimensions}.
	 */
	@Test
	public void checkDimensions() {
		GridDimensions dims = new GridDimensions() {
			@Override
			public void dimensionsChanged() {
				// Nothing to do.
			}
		};

		// Set the desired values. This scales the dimensions by 50%,
		// effectively making the mesh half as big.
		float scale = 0.5f;
		float x = 10f;
		float y = 10f;
		float xWorld = 5f;
		float yWorld = 5f;

		// Set the dimensions and compare.
		dims.set(scale, x, y);
		compareValues(dims, scale, x, y, xWorld, yWorld);

		// Set the desired values. This scales the dimensions by 100%,
		// effectively making the mesh twice as big.
		scale = 2f;
		x = 12f;
		y = 11f;
		xWorld = 24f;
		yWorld = 22f;

		// Set the dimensions and compare.
		dims.set(scale, x, y);
		compareValues(dims, scale, x, y, xWorld, yWorld);

		// TODO Test the upper and lower bounds on the scale and the min/max
		// world units for both x and y. For now, let's focus on getting this
		// working with the MeshEditor. When we start getting funky dimensions,
		// we should test this class further.

		return;
	}

	private void compareValues(GridDimensions dims, float scale, float x,
			float y, float xWorld, float yWorld) {

		// Check the scale.
		assertEquals(scale, dims.getScale(), delta);

		// Check the x units, min, and max.
		assertEquals(x, dims.getXLength(), delta);
		assertEquals(x / 2f, dims.getXMax(), delta);
		assertEquals(-x / 2f, dims.getXMin(), delta);

		// Check the y units, min, and max.
		assertEquals(y, dims.getYLength(), delta);
		assertEquals(y / 2f, dims.getYMax(), delta);
		assertEquals(-y / 2f, dims.getYMin(), delta);

		// Check the x world units, min, and max.
		assertEquals(xWorld, dims.getXWorldLength(), delta);
		assertEquals(xWorld / 2f, dims.getXWorldMax(), delta);
		assertEquals(-xWorld / 2f, dims.getXWorldMin(), delta);

		// Check the y world units, min, and max.
		assertEquals(yWorld, dims.getYWorldLength(), delta);
		assertEquals(yWorld / 2f, dims.getYWorldMax(), delta);
		assertEquals(-yWorld / 2f, dims.getYWorldMin(), delta);

		return;
	}

}
