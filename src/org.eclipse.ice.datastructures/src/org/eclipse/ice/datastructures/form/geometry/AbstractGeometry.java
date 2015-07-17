/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Robert Smith
 *******************************************************************************/

package org.eclipse.ice.datastructures.form.geometry;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;

/**
 * An abstract implementation of IGeometry.
 * 
 * @author Robert Smith
 *
 */
@XmlRootElement(name = "AbstractGeometry")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractGeometry implements IGeometry {
	
	/**
	 * The list of held shapes
	 */
	@XmlAnyElement()
	private ArrayList<IShape> shapes;
	
	/**
	 * The list of objects registered as listeners to this one.
	 */
	@XmlTransient
	private ArrayList<IUpdateableListener> listeners;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateable#update(java.lang.String, java.lang.String)
	 */
	@Override
	public void update(String updatedKey, String newValue) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateable#register(org.eclipse.ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void register(IUpdateableListener listener) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateable#unregister(org.eclipse.ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void unregister(IUpdateableListener listener) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setId(int)
	 */
	@Override
	public void setId(int id) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getDescription()
	 */
	@Override
	public String getDescription() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getId()
	 */
	@Override
	public int getId() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getName()
	 */
	@Override
	public String getName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org.eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.form.geometry.IGeometry#addShape(org.eclipse.ice.datastructures.form.geometry.IShape)
	 */
	@Override
	public void addShape(IShape shape) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.form.geometry.IGeometry#getShapes()
	 */
	@Override
	public ArrayList<IShape> getShapes() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.form.geometry.IGeometry#removeShape(org.eclipse.ice.datastructures.form.geometry.IShape)
	 */
	@Override
	public void removeShape(IShape shape) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.form.geometry.IGeometry#setShapes(java.util.ArrayList)
	 */
	@Override
	public void setShapes(ArrayList<IShape> shapes) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.datastructures.form.geometry.IGeometry#copy(org.eclipse.ice.datastructures.form.geometry.IGeometry)
	 */
	@Override
	public void copy(IGeometry iceObject) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		return null;
	}

}
