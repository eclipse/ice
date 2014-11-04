/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets.mesh.test;

import static org.junit.Assert.fail;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.client.widgets.mesh.AbstractMeshController;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class provides a subclass of AbstractMeshController to test its basic
 * functions that are not required to be implemented by subclasses. For update()
 * and syncView() calls, it marks a flag that can be requested to check that the
 * AbstractMeshController properly registers with the provided IUpdateable.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jordan H. Deyton
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TestMeshController extends AbstractMeshController {

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>A stored reference to the model managed by the controller. Required for the implementation of clone.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private IUpdateable model;
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Whether or not the controller's update method was called.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AtomicBoolean wasUpdated = new AtomicBoolean(false);
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Whether or not the controller's syncView method was called.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AtomicBoolean wasSynced = new AtomicBoolean(false);
	
	/**
	 * <!-- begin-UML-doc -->
	 * <p>The default constructor. Does nothing but call the AbstractMeshController constructor.</p>
	 * <!-- end-UML-doc -->
	 * @param model The model. For testing purposes, this can be an ICEObject.
	 * @param queue The queue used to push updates to the views.
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TestMeshController(IUpdateable model,
			ConcurrentLinkedQueue<AbstractMeshController> queue) {
		// begin-user-code
		super(model, queue);
		
		// Store a reference to the model.
		this.model = model;

		return;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Implements the syncView method of AbstractMeshController to mark a flag when the view is synced.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void syncView() {
		// begin-user-code
		
		// Update the wasSynced flag.
		wasSynced.set(true);
		
		return;
		// end-user-code
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Overrides the update method of AbstractMeshController to mark a flag when the controller is notified.</p>
	 * <!-- end-UML-doc -->
	 * @param component <p>The component that has been updated.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void update(IUpdateable component) {
		// begin-user-code
				
		// Update the wasUpdated flag.
		wasUpdated.set(true);
		// Proceed with the normal update process.
		super.update(component);
		
		return;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Implements the clone method of AbstractMeshController.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// Initialize a new object.
		TestMeshController object = new TestMeshController((IUpdateable) model.clone(), updateQueue);

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Gets whether or not the controller's update method was called. This also resets the wasUpdated value to false after the call.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>True if the controller's update method was called, false otherwise.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasUpdated() {
		// begin-user-code
		
		// Wait a half second so that the thread can work.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// Complain and fail
			e.printStackTrace();
			fail();
		}
		
		// Reset wasUpdated and return its previous value.
		return wasUpdated.getAndSet(false);
		// end-user-code
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Gets whether or not the controller's syncView method was called. This also resets the wasSynced value to false after the call.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>True if the controller's syncView method was called, false otherwise.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean wasSynced() {
		// begin-user-code
		
		// Wait a half second so that the thread can work.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// Complain and fail
			e.printStackTrace();
			fail();
		}
		
		// Reset wasUpdated and return its previous value.
		return wasSynced.getAndSet(false);
		// end-user-code
	}
	
}
