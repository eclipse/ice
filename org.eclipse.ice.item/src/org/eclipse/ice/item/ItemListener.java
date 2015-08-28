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
package org.eclipse.ice.item;

/**
 * <p>
 * This interface is meant to be realized by other classes that would observe an
 * Item, including its own subclasses, so that they can respond to changes in
 * the system caused by the Item. This includes things like creating or
 * destroying files for example.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public interface ItemListener {
	/**
	 * <p>
	 * This operation informs the listener that it should reload its data in
	 * response to a change that the Item made to the project.
	 * </p>
	 * 
	 */
	public void reloadProjectData();
}