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
package org.eclipse.ice.item;

import java.util.ArrayList;

/**
 * <p>
 * This class builds Items that depend on other Items. These "composite Items"
 * are themselves subclasses of Item, but they require functionality implemented
 * by other Items to function properly. ICompositeItemBuilder.build() must
 * return null if the list of other ItemBuilders has not be set by calling
 * ICompositeItemBuilder.addBuilders().
 * </p>
 * 
 * @author Jay Jay Billings
 */
public interface ICompositeItemBuilder extends ItemBuilder {

	/**
	 * <p>
	 * This operation sets the list of ItemBuilders that may be used by the
	 * composite Item to construct its children. This operation should be called
	 * before ICompositeItemBuilder.build().
	 * </p>
	 * 
	 * @param itemBuilders
	 *            <p>
	 *            The list of ItemBuilders. This list should contain one or more
	 *            ItemBuilders. If it does not, then building a composite Item
	 *            with this builder must fail.
	 *            </p>
	 */
	public void addBuilders(ArrayList<ItemBuilder> itemBuilders);
}