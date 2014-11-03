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
package org.eclipse.ice.iclient.uiwidgets;

import org.eclipse.ice.datastructures.resource.ICEResource;

/** 
 * <!-- begin-UML-doc -->
 * <p>The IErrorBox interface describes the operations that ICE expects from a Widget that can display a text editor to a user.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface ITextEditor {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation sets the ICEResource that editor should display.</p>
	 * <!-- end-UML-doc -->
	 * @param resource <p>The ICEResource</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setResource(ICEResource resource);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation retrieves the ICEResource that editor is displaying or null if it has not been set.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The ICEResource</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEResource getResource();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the ITextEditor to display its text. It must be implemented by subclasses that code to a specific UI API (SWT, Swing).</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void display();
}