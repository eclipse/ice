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
package org.eclipse.ice.viz.service.connections.test;

import org.eclipse.ice.datastructures.form.IEntryContentProvider;
import org.eclipse.ice.viz.service.connections.PortEntryContentProvider;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class verifies the behavior of the {@link PortEntryContentProvider}.
 * 
 * @author Jordan Deyton
 *
 */
@Ignore
public class PortEntryContentProviderTester {

	// TODO Implement

	/**
	 * Verifies that the default values for a {@link PortEntryContentProvider}
	 * are properly set when instantiated.
	 */
	@Test
	public void checkConstruction() {

	}

	/**
	 * Tests that the default value can only be set to a valid port number
	 * within the allowed values, and that if the allowed values change, the
	 * default value is clamped to the range.
	 */
	@Test
	public void checkDefaultValue() {

	}

	/**
	 * Tests that the allowed values are based off the set range and not the
	 * default {@link IEntryContentProvider} methods for setting the allowed
	 * values.
	 */
	@Test
	public void checkAllowedValues() {

	}

}
