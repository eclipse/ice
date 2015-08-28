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
package org.eclipse.ice.kdd.test.fakeobjects;

import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;

/**
 * <p>
 * This is a fake KDDStrategy to be used in unit testing the KDD architecture.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class FakeStrategy extends KDDStrategy {

	@Override
	public boolean executeStrategy() {
		return true;
	}

	/**
	 * 
	 */
	public FakeStrategy() {
		super(null, null);
	}

}