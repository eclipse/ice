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
package org.eclipse.eavp.viz.service.javafx.mesh.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.mesh.FXMeshAttachment;
import org.eclipse.eavp.viz.service.javafx.mesh.FXMeshAttachmentManager;
import org.junit.Test;

/**
 * A class to test the functionality of the FXMeshAttachmentManager
 * 
 * @author Robert Smith
 *
 */
public class FXMeshAttachmentManagerTester {

	/**
	 * Check that the manager is creating FXMeshAttachments
	 */
	@Test
	public void checkAttachments() {

		// Check that the manager allocate
		FXMeshAttachmentManager manager = new FXMeshAttachmentManager();
		assertTrue(manager.allocate() instanceof FXMeshAttachment);
	}

}
