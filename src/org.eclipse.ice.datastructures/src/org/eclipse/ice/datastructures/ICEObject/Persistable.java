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
package org.eclipse.ice.datastructures.ICEObject;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class defines the operations that ICE expects for units that are
 * persisted to disc outside of a database.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface Persistable {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads the ICEObject from persistent storage as XML. This
	 * operation will throw an IOException if it fails.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStream
	 *            <p>
	 *            An input stream from which the ICEObject should be loaded.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromXML(InputStream inputStream);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation saves the ICEObject in persistent storage as XML. This
	 * operation will throw an IOException if it fails.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param outputStream
	 *            <p>
	 *            An output stream to which the ICEObject should be stored.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void persistToXML(OutputStream outputStream);
}