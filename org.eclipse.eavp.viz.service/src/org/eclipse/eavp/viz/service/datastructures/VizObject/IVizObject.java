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
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.eavp.viz.service.datastructures.VizObject;

/**
 * <p>
 * VizObject is the base class for all common, shared data structures in Viz
 * geometry and mesh bundles. ICEObjects are uniquely identifiable by their
 * identification numbers and are persistent. VizObject implements clone() for
 * creating deep copies and also provides a public copy operation to copy into
 * an existing VizObject. VizObjects can be marshaled and unmarshaled to XML
 * using the loadFromXML() and persistToXML().
 * </p>
 * <p>
 * Operations are defined for most of the attributes and capabilities of the
 * VizObject class, but some work is required by subclasses. Subclasses must
 * override clone() if they extend VizObject by adding attributes or the deep
 * copy will fail. They should provide a custom implementation of copy() that is
 * specific to their own type to do a deep copy (i.e. copy(a:myType) instead of
 * copy(a:VizObject)) since VizObject.copy() will only copy the attributes of
 * VizObjects. They must also override the loadFromXML() operation to copy the
 * XML data properly from the XMLLoader (because Viz uses JAXB to bind XML to
 * VizObjects and its subclasses).
 * </p>
 * <p>
 * VizObjects implement IVizUpdateable. The base class manages registering,
 * unregistering and notifications. Subclasses are expected to override
 * update().
 * </p>
 * 
 * @author Jay Jay Billings
 */
public interface IVizObject {

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setId(int id)
	 */
	void setId(int id);

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getId()
	 */
	int getId();

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setName(String name)
	 */
	void setName(String name);

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getName()
	 */
	String getName();

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setDescription(String description)
	 */
	void setDescription(String description);

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getDescription()
	 */
	String getDescription();

	/**
	 * This operation copies the contents of an ICEObject into the current
	 * object using a deep copy.
	 * 
	 * @param entity
	 *            The Identifiable entity from which the values should be
	 *            copied.
	 */
	void copy(VizObject entity);

	/**
	 * <p>
	 * This operation returns a clone of the ICEObject using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone.
	 *         </p>
	 */
	Object clone();

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#equals(Object otherObject)
	 */
	boolean equals(Object otherObject);

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#hashCode()
	 */
	int hashCode();

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#update(String updatedKey, String newValue)
	 */
	void update(String updatedKey, String newValue);

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#register(IUpdateableListener listener)
	 */
	void register(IVizUpdateableListener listener);

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateable#unregister(IUpdateableListener listener)
	 */
	void unregister(IVizUpdateableListener listener);

}