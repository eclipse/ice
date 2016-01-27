/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.canvas.test;

import org.eclipse.ice.viz.service.javafx.canvas.AbstractAttachment;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.junit.Test;

/**
 * A class to test the functionality of AbstractAttachment.
 * 
 * @author Robert Smith
 *
 */
public class AbstractAttachmentTester {

	/**
	 * Check that the AbstractAttachment's process of attaching/detaching to a node is working correctly.
	 */
	@Test
	public void checkAttachment(){
		
		AbstractAttachment attachment = new AbstractAttachment(){
			
			@Override
			public void removeGeometry(AbstractController geom) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Class<?> getType() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void processShape(AbstractController shape) {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void disposeShape(AbstractController shape) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		
	}
}
