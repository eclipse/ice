/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.vibe.kvPair;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * The ItemBuilder for the VibeKVPair Item.
 * @author Jay Jay Billings, Andrew Bennett
 *
 */
public class VibeKVPairBuilder extends AbstractItemBuilder {

	// The Item name
	public static final String name = "VIBE Key-Value Pair";
	
	// The type
	public static final ItemType type = ItemType.Model;

	/**
	 * The Constructor
	 */
	public VibeKVPairBuilder() {
		setName(name);
		setType(type);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject projectSpace) {
		return new VibeKVPair(projectSpace);
	}

}
