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

import org.eclipse.ice.datastructures.form.Form;

/** 
 * <!-- begin-UML-doc -->
 * <p>Implementations of the IUpdateEventListener are registered with a FormWidget and notified when the Form is updated.</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IUpdateEventListener {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation notifies the listener that the included Form has been updated.</p>
	 * <!-- end-UML-doc -->
	 * @param form <p>The widget that was changed.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void formUpdated(Form form);
}