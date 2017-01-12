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
package org.eclipse.tests.ice.item;

import org.eclipse.ice.datastructures.form.DataComponent;

/**
 * <p>
 * The FakeDataComponent class is a subclass of DataComponent that is used for
 * testing.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class FakeDataComponent extends DataComponent {

	// Updated value
	String value;

	@Override
	public void update(String key, String newValue) {
		this.value = newValue;
	}

	/**
	 * <p>
	 * This operation returns the value that was passed to the update operation
	 * inherited from DataComponent.
	 * </p>
	 * 
	 * @return <p>
	 *         The value submitted to the update operation.
	 *         </p>
	 */
	public String getUpdatedValue() {
		return value;
	}

}