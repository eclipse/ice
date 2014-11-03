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

/** 
 * <!-- begin-UML-doc -->
 * <p>The IObservableWidget interface describes the operations that must be realized by Widgets that are observable vian ICE event listeners (i.e. - IProcessEventListener, IUpdateEventListener, etc.).</p>
 * <!-- end-UML-doc -->
 * @author jaybilly
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IObservableWidget {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation registers an IUpdateEventListener with the FormWidget.</p>
	 * <!-- end-UML-doc -->
	 * @param listener <p>The listener.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerUpdateListener(IUpdateEventListener listener);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation registers an IProcessEventListener with the FormWidget.</p>
	 * <!-- end-UML-doc -->
	 * @param listener <p>The listener.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerProcessListener(IProcessEventListener listener);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation registers an ISimpleResourceProvider with the observable widget so that it can notify the provider when a resource should be loaded.</p>
	 * <!-- end-UML-doc -->
	 * @param provider <p>The ICEResource provider.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerResourceProvider(ISimpleResourceProvider provider);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This protected operation notifies the IUpdateEventListeners of a change.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyUpdateListeners();

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This protected operation notifies the IProcessEventListeners of a change.</p>
	 * <!-- end-UML-doc -->
	 * @param process <p>The process that should be performed for the Form.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyProcessListeners(String process);

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This  operation notifies the IProcessEventListeners of a cancellation request..</p>
	 * <!-- end-UML-doc -->
	 * @param process <p>The process that should be performed for the Form.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void notifyCancelListeners(String process);
}