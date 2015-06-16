/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;

/**
 * A {@code SecretEntry} is simply an {@link Entry} whose secret flag is set to
 * true.
 * 
 * @author Jordan Deyton
 *
 */
public class SecretEntry extends Entry {

	// If we add any other properties, then we will need to override the clone
	// operation and provide a copy constructor.

	/**
	 * A constructor that will create an {@code Entry} with only a unique ID and
	 * a name. Default values are set:
	 * <ul>
	 * <li>objectDescription = "Entry" + uniqueId</li>
	 * <li>defaultValue = ""</li>
	 * <li>allowedValues = null</li>
	 * <li>allowedValueType = AllowedValueType.Undefined</li>
	 * </ul>
	 * The constructor will call the setup function after setting the default
	 * values. The setup function can be overridden to tailor the properties of
	 * the {@code Entry} or otherwise overload the behavior of the {@code Entry}
	 * .
	 */
	public SecretEntry() {
		super();

		secretFlag = true;
	}

	/**
	 * A constructor that sets the {@code Entry} to the content provider.
	 * 
	 * @param contentProvider
	 *            The content provider for the {@code Entry}.
	 */
	public SecretEntry(IEntryContentProvider contentProvider) {
		super(contentProvider);

		secretFlag = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.datastructures.form.Entry#clone()
	 */
	@Override
	public Object clone() {
		// Create a new entry and copy the contents. Note that since we already
		// set the secret flag to true in the constructor, we can rely on the
		// default copy operation.
		Entry entry = new SecretEntry();
		entry.copy(this);

		return entry;
	}
}
