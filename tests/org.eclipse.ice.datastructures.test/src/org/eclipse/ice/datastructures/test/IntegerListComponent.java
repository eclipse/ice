/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay 
 *   Billings
 *******************************************************************************/
package org.eclipse.ice.datastructures.test;

import java.io.InputStream;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.ICEObject.AbstractListComponent;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.Persistable;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;

/**
 * This is a test class that implements the abstract operations on
 * AbstractListComponent so that it can be instantiated and the concerte
 * operations tested.
 * 
 * @author Jay Jay Billings
 *
 */
@XmlRootElement(name = "IntegerListComponent")
public class IntegerListComponent extends AbstractListComponent<Integer> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see Persistable#loadFromXML(InputStream inputStream)
	 */
	@Override
	public void loadFromXML(InputStream inputStream) {

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#accept(IComponentVisitor)
	 */
	@Override
	public void accept(IComponentVisitor visitor) {

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Cloneable#clone()
	 */
	@Override
	public Object clone() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#update(String updatedKey, String newValue)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void update(String updatedKey, String newValue) {

	}

}
