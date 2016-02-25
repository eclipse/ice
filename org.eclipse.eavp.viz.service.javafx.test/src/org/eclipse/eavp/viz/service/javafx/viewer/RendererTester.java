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
package org.eclipse.eavp.viz.service.javafx.viewer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachmentManager;
import org.eclipse.eavp.viz.service.javafx.viewer.IAttachmentManager;
import org.eclipse.eavp.viz.service.javafx.viewer.Renderer;
import org.junit.Test;

/**
 * A class to test the functionality of the Renderer.
 * 
 * @author Robert Smith
 *
 */
public class RendererTester {

	/**
	 * Check that the Renderer can correctly create attachments
	 */
	@Test
	public void checkAttachments() {

		// The renderer for testing
		Renderer renderer = new TestRenderer();

		// Create two managers and register them for two classes
		FXAttachmentManager manager1 = new FXAttachmentManager();
		renderer.register(TestAttachment1.class, manager1);
		FXAttachmentManager manager2 = new FXAttachmentManager();
		renderer.register(TestAttachment2.class, manager2);

		// The renderer should not support FXAttachment, as it has not been
		// registered
		assertFalse(renderer.supportsAttachment(FXAttachment.class));

		// It should support TestAttachment1, which was registered
		assertTrue(renderer.supportsAttachment(TestAttachment1.class));

		// The FXAttachmentManagers should create FXAttachments for both classes
		assertTrue(renderer.createAttachment(
				TestAttachment1.class) instanceof FXAttachment);
		assertTrue(renderer.createAttachment(
				TestAttachment2.class) instanceof FXAttachment);

		// Remove TestAttachment1 from the renderer
		renderer.unregister(TestAttachment1.class);

		// The renderer should no longer support the class
		assertFalse(renderer.supportsAttachment(TestAttachment1.class));

		// The renderer should return null for an unsupported class
		assertNull(renderer.createAttachment(TestAttachment1.class));

		// The other manager should not be affected
		assertTrue(renderer.createAttachment(
				TestAttachment2.class) instanceof FXAttachment);
	}

	/**
	 * A concrete version of Renderer.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestRenderer extends Renderer {
		// No code needed, we just want a non-Abstract Renderer class.
	}

	/**
	 * A dummy Attachment for testing purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestAttachment1 extends FXAttachment {

		public TestAttachment1(IAttachmentManager manager) {
			super(manager);
			// TODO Auto-generated constructor stub
		}

	}

	/**
	 * A dummy Attachment for testing purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestAttachment2 extends FXAttachment {

		public TestAttachment2(IAttachmentManager manager) {
			super(manager);
			// TODO Auto-generated constructor stub
		}

	}
}
