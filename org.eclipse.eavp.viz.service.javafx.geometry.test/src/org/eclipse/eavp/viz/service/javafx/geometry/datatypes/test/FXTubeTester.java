/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.geometry.datatypes.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXTube;
import org.junit.Test;

import javafx.scene.shape.MeshView;

/**
 * A class to test the functionality of the FXTube.
 * 
 * @author Robert Smith
 *
 */
public class FXTubeTester {

	/**
	 * Check that the FXTube will return null when trying to create a mesh for
	 * invalid parameters.
	 */
	@Test
	public void checkValidity() {

		// First check that a view can be made when valid parameters are used.
		FXTube tube = new FXTube(9d, 6d, 3d, 3, 5);
		MeshView view = new MeshView(tube.getMesh());
		assertNotNull(view);

		// Try a tube with a negative height. Should return null.
		FXTube tube2 = new FXTube(-1d, 6d, 3d, 3, 5);
		assertNull(tube2.getMesh());

		// Try a tube with height zero. Should return null.
		FXTube tube3 = new FXTube(0d, 6d, 3d, 3, 5);
		assertNull(tube3.getMesh());

		// Try a tube with an inner radius that is larger than its outer radius.
		// Should return null.
		FXTube tube4 = new FXTube(9d, 3d, 6d, 3, 5);
		assertNull(tube4.getMesh());

		// Try a tube with identical inner and outer radii. This should be
		// valid, creating a pipe with only sides but no top or bottom.
		FXTube thinTube = new FXTube(9d, 3d, 3d, 3, 5);
		assertNotNull(thinTube.getMesh());

		// Try a tube with only 1 axial sample. This should return null.
		FXTube tube5 = new FXTube(-1d, 6d, 3d, 1, 5);
		assertNull(tube5.getMesh());

		// Try a tube with only 1 radial sample. This should return null.
		FXTube tube6 = new FXTube(-1d, 6d, 3d, 3, 1);
		assertNull(tube6.getMesh());

	}

}
