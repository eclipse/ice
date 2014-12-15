/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor;

import org.eclipse.ice.datastructures.ICEObject.Component;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * An interface for managing Components on a grid.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IGridManager {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the Component at the provided GridLocation or null if one does
	 * not exist at the provided location.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param location
	 *            <p>
	 *            A GridLocation.
	 *            </p>
	 * @return <p>
	 *         A Component object to return.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getComponentName(GridLocation location);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Adds a Component and its GridLocation to this GridManager. If a Component
	 * already exists at that location, then this operation does nothing.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 *            <p>
	 *            A Component object to add.
	 *            </p>
	 * @param location
	 *            <p>
	 *            A GridLocation.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addComponent(Component component, GridLocation location);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the Component at the provided GridLocation from this GridManager.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param location
	 *            <p>
	 *            A GridLocation.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeComponent(GridLocation location);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the provided Component from this GridManager.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 *            <p>
	 *            A Component object to remove.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeComponent(Component component);
}