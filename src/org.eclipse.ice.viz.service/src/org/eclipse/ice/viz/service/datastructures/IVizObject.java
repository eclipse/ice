package org.eclipse.ice.viz.service.datastructures;

import javax.xml.bind.annotation.XmlAttribute;

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