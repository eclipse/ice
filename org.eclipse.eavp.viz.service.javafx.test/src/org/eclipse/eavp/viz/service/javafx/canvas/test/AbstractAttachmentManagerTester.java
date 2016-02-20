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

import static org.junit.Assert.*;

import org.eclipse.eavp.viz.service.javafx.canvas.AbstractAttachment;
import org.eclipse.eavp.viz.service.javafx.canvas.AbstractAttachmentManager;
import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.junit.Test;

/**
 * A class for testing the functionality of the AbstractAttachmentManager.
 * 
 * @author Robert Smith
 *
 */
public class AbstractAttachmentManagerTester {
	
	/**
	 * Test the manager's ability to properly handle its attachments.
	 */
	@Test
	public void checkAttachments(){
		
		//Create a manager that creates AbstractAttachments 
		AbstractAttachmentManager manager = new AbstractAttachmentManager(){

			@Override
			public IAttachment allocate() {

				//Create a new attachment
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
				
				//Add it to the list of active attachments and return it
				active.add(attachment);
				return attachment;
			}
			
		};
		
		//The manager should start off without attachments
		assertTrue(manager.getAttachments().isEmpty());
		
		//Allocate a new attachment and check that it is there
		manager.allocate();
		assertFalse(manager.getAttachments().isEmpty());
		
		//Allocate a second attachment and check that both are contained by the manager
		IAttachment attachment = manager.allocate();
		assertEquals(2, manager.getAttachments().size());
		
		//Order the manager to destroy the attachment. It should not be removed yet
		manager.destroy(attachment);
		assertEquals(2, manager.getAttachments().size());
		
		//Update the manager, which should cause it to delete the attachment
		manager.update();
		assertEquals(1, manager.getAttachments().size());
		
		//Try destroying a missing attachment and updating with nothing to delete. Nothing should happen.
		manager.destroy(attachment);
		manager.update();
		assertEquals(1, manager.getAttachments().size());
		
	}
}
