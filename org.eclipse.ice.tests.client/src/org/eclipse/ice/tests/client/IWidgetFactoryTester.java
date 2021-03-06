/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Menghan Li
 *******************************************************************************/
package org.eclipse.ice.tests.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.iclient.uiwidgets.IWidgetFactory;
import org.junit.Test;

/**
 * This class tests the static interface operation
 * IWidgetFactory.getWidgetFactories().
 *
 * @author Jay Jay Billings
 *
 */
public class IWidgetFactoryTester {
	/**
	 * Test for {@link org.eclipse.ice.client.uiwidgets.IWidgetFactory}.
	 *
	 * @throws CoreException
	 */
	@Test
	public void test() throws CoreException {
		// Simply get the factories from the registry and make sure they are
		// actually there.
		IWidgetFactory[] factories = IWidgetFactory.getIWidgetFactories();
		assertNotNull(factories);
		assertTrue(factories.length > 0);
		return;
	}

}
