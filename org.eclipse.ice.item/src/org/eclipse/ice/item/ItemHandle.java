/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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

/**
 * <p>
 * The ItemHandle class is used by the ItemManager to store the unique ID and
 * name of an Item.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ItemHandle {
	/**
	 * <p>
	 * The ID of the Item represented by this ItemHandle.
	 * </p>
	 * 
	 */
	private Integer id;
	/**
	 * <p>
	 * The name of the Item represented by this ItemHandle.
	 * </p>
	 * 
	 */
	private String name;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param itemID
	 *            <p>
	 *            The ID of the Item represented by this ItemHandle.
	 *            </p>
	 * @param itemName
	 *            <p>
	 *            The name of the Item represented by this ItemHandle.
	 *            </p>
	 */
	public ItemHandle(Integer itemID, String itemName) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>
	 * This operation returns the ID of the Item represented by this ItemHandle.
	 * </p>
	 * 
	 * @return <p>
	 *         The ID of the Item represented by this ItemHandle.
	 *         </p>
	 */
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 * This operation returns the name of the Item represented by this
	 * ItemHandle.
	 * </p>
	 * 
	 * @return <p>
	 *         The name of the Item represented by this ItemHandle.
	 *         </p>
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}