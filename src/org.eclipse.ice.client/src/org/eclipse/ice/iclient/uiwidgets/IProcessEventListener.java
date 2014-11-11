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
 * <p>Implementations of the IProcessEventListener are registered with a FormWidget and notified when a process is selected.</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IProcessEventListener {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation notifies the listener that the Form has been marked to be processed.</p>
	 * <!-- end-UML-doc -->
	 * @param form
	 * @param process
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void processSelected(Form form, String process);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the listener to cancel the last process request for the Form. There is no guarantee that it can actually cancel the process (it may finish first!).</p>
	 * <!-- end-UML-doc -->
	 * @param form <p>The form that was previously processed.</p>
	 * @param process <p>The name of the process that was requested for the previous form.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void cancelRequested(Form form, String process);
}