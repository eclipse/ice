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
package org.eclipse.eavp.viz.service.javafx.canvas.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.eclipse.eavp.viz.service.javafx.canvas.AbstractAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.IAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
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
		
		//Create some AbstractControllers for the attachment to contain
		AbstractController geometry1 = new AbstractController(new AbstractMesh(), new AbstractView());
		AbstractController geometry2 = new AbstractController(new AbstractMesh(), new AbstractView());
		AbstractController shape1 = new AbstractController(new AbstractMesh(), new AbstractView());
		shape1.setProperty("Name", "shape1");
		AbstractController shape2 = new AbstractController(new AbstractMesh(), new AbstractView());
		shape2.setProperty("Name", "shape2");
		AbstractController shape3 = new AbstractController(new AbstractMesh(), new AbstractView());
		shape3.setProperty("Name", "shape3");
		
		//Geometry 1 has shape1, geometry2 has the other two shapes
		geometry1.addEntity(shape1);
		geometry2.addEntity(shape2);
		geometry2.addEntity(shape3);
		
		//Add the first geometry to the attachment, the shapes should be null since it is not attached to a node
		attachment.addGeometry(geometry1);
		assertTrue(attachment.getShapes(false).isEmpty());
		
		//Attach to a node, which should put the geometry's shapes in the correct list
		TestNode node = new TestNode();
		attachment.attach(node);
		assertTrue(attachment.getShape(0) == shape1);
		
		//Add a geometry with multiple shapes and ensure its shapes weren't added
		attachment.addGeometry(geometry2);
		assertTrue(attachment.getShapes(false).size() == 1);
		
		//Detach, re-add both geometries, attach again, and check that all the shapes are now present
		attachment.detach(node);
		attachment.addGeometry(geometry1);
		attachment.addGeometry(geometry2);
		attachment.attach(node);
		assertTrue(attachment.getShape(0) == shape1);
		assertTrue(attachment.getShape(1) == shape2);
		assertTrue(attachment.getShape(2) == shape3);
		
		//Detach the node and check that the shapes have been cleared
		attachment.detach(node);
		assertTrue(attachment.getShapes(false).isEmpty());
		
		//Attach to a node again. The geometry list should also have been cleared, and so the geometries' shapes should not be added upon the attach
		attachment.attach(node);
		assertTrue(attachment.getShapes(false).isEmpty());
		
	}
	
	/**
	 * Test the getter and setter methods for the class's data members.
	 */
	@Test
	public void checkData(){
		
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

		//Check the immutable field
		attachment.setImmutable(false);
		assertFalse(attachment.isImmutable());
		attachment.setImmutable(true);
		assertTrue(attachment.isImmutable());
		
		//Check the visible field
		attachment.setVisible(false);
		assertFalse(attachment.isVisible());
		attachment.setVisible(true);
		assertTrue(attachment.isVisible());
	}
	
	/**
	 * A basic implementation of INode.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestNode implements INode{

		@Override
		public INode getParent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setParent(INode parent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addChild(INode node) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeChild(INode node) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeAllChildren() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public List<INode> getChildren(boolean copy) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setVisible(boolean visible) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isVisible() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void attach(IAttachment attachment) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void detach(IAttachment attachment) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean hasAttachment(Class<?> attachmentClass) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean supports(IAttachment attachment) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Map<Class<?>, List<IAttachment>> getAttachments() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<IAttachment> getAttachments(Class<?> clazz) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, Object> getProperties() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getProperty(String key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setProperty(String key, Object value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean hasProperty(String key) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
