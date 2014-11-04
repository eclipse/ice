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
 * <p>This interface simplifies the loading and retrieval ICEResources. It should be used to retrieve an ICEResource to gather metadata and to direct ICE to load the resource. In general, the ICEResource returned from getResource() should not be loaded by clients but should be used only to gather metadata about the resource. The provider will properly load the resource in a way that is consistent with its inner workings if clients call loadResource().</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface ISimpleResourceProvider {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the simple provider to load the resource in a way that is consistent with its inner workings.</p>
	 * <!-- end-UML-doc -->
	 * @param resource <p>The ICEResource that should be loaded by the provider.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadResource(ICEResource resource);
}