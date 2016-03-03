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
package org.eclipse.eavp.viz.service.javafx.canvas.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.canvas.BasicAttachment;
import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachmentManager;
import org.eclipse.eavp.viz.service.javafx.canvas.ModelUtil;
import org.eclipse.eavp.viz.service.javafx.scene.base.GNode;
import org.junit.Test;

/**
 * A class to test the functionality of ModelUtil functions.
 * 
 * @author Robert Smith
 *
 */
public class ModelUtilTester {

	/**
	 * Check that the ModelUtil functions work correctly.
	 */
	@Test
	public void checkFunctions() {

		// Create a node and attachment
		GNode node = new GNode();
		BasicAttachment attachment = new FXAttachment(
				new FXAttachmentManager());

		// Check that null values are invalid
		assertFalse(ModelUtil.isAttachment(null));
		assertFalse(ModelUtil.isNode(null));

		// Check that the correct classes are valid
		assertTrue(ModelUtil.isAttachment(attachment));
		assertTrue(ModelUtil.isNode(node));

		// Check that the incorrect classes are valid
		assertFalse(ModelUtil.isAttachment(node));
		assertFalse(ModelUtil.isNode(attachment));

	}
}
