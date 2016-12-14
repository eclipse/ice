/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.vibe.kvPair;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.entry.StringEntry;

/**
 * This class represents one of the rows of the TableComponent comprising the
 * majority of the VIBE Key-Value Pair Item. Each VibeKVPairRow consists of two
 * StringEntries, one each for the key and the value. The VibeKVPairRow is
 * responsible for listening to other rows and updating its own value to keep it
 * valid, according to the values of other rows on which its value depends.
 * 
 * @author Robert Smith
 *
 */
public class VibeKVPairRow implements IUpdateable, IUpdateableListener {

	// The Identifiable context
	private String context;

	// The Identifiable description
	private String description;

	// The Identifiable ID
	private int ID;

	// The Entry containing the key's name
	private StringEntry key;

	private ArrayList<IUpdateableListener> listeners;

	// The Identifiable name
	private String name;

	// The Entry containing the value associated with key
	private StringEntry value;

	/**
	 * The nullary constructor.
	 */
	private VibeKVPairRow() {

		// Initialize the Identifiable information
		context = "";
		description = "";
		ID = 0;
		name = "";

		// Initialize the listener list
		listeners = new ArrayList<IUpdateableListener>();
	}

	/**
	 * The default constructor.
	 * 
	 * @param key
	 *            The entry for the row's key.
	 * @param value
	 *            The entry for the row's value.
	 */
	public VibeKVPairRow(StringEntry key, StringEntry value) {

		// Initialize the class with the nullary constructor
		this();

		// Set the data members
		this.key = key;
		this.value = value;

		// Listen to changes in the value
		value.register(this);

	}

	/**
	 * Getter method for the key.
	 * 
	 * @return The row's entry for the key
	 */
	public StringEntry getKey() {
		return key;
	}

	/**
	 * Getter method for the value.
	 * 
	 * @return The row's entry for the value
	 */
	public StringEntry getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#setId(int)
	 */
	@Override
	public void setId(int id) {
		ID = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.Identifiable#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getId()
	 */
	@Override
	public int getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.Identifiable#setName(java.lang.
	 * String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.Identifiable#setDescription(java
	 * .lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.ICEObject.Identifiable#getContext()
	 */
	@Override
	public String getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.Identifiable#setContext(java.
	 * lang.String)
	 */
	@Override
	public void setContext(String context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org.
	 * eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {

		// Notify own listeners of a change in the value
		update(null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateable#update(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public void update(String updatedKey, String newValue) {

		// Iterate through the listeners, updating each
		for (IUpdateableListener listener : listeners) {
			listener.update(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateable#register(org.eclipse
	 * .ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void register(IUpdateableListener listener) {
		listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.ICEObject.IUpdateable#unregister(org.
	 * eclipse.ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void unregister(IUpdateableListener listener) {
		listeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Make a new row and copy this object's data into it
		VibeKVPairRow clone = new VibeKVPairRow();
		clone.copy(this);
		return clone;
	}

	/**
	 * Make the receiver into a copy of the given object.
	 * 
	 * @param source
	 */
	public void copy(Object source) {

		// If the source is not a VibeKVPairRow, fail silently
		if (source instanceof VibeKVPairRow) {

			// Cast the source object
			VibeKVPairRow castSource = (VibeKVPairRow) source;

			// Copy the key and value
			key = (StringEntry) castSource.key.clone();
			value = (StringEntry) castSource.value.clone();

			// Copy the other data members
			context = castSource.context;
			description = castSource.description;
			ID = castSource.ID;
			key = castSource.key;
			name = castSource.name;
		}
	}

}
