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
package org.eclipse.ice.viz.service.javafx.canvas.test;

import org.eclipse.ice.viz.service.javafx.canvas.AbstractAttachmentManager;
import org.eclipse.ice.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.ice.viz.service.javafx.scene.base.GNode;
import org.eclipse.ice.viz.service.javafx.scene.model.IAttachment;
import org.junit.Test;

/**
 * A class to test the functionality of the FXAttachment.
 * 
 * @author Robert Smith
 *
 */
public class FXAttachmentTester {

	/**
	 * Test that the Attachment is correctly handling the JavaFX data structures.
	 */
	@Test
	public void checkGeometry(){
		
		FXAttachment attachment = new FXAttachment(new AbstractAttachmentManager(){

			@Override
			public IAttachment allocate() {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		
		GNode node = new GNode();
		
	}
}
