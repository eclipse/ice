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
package org.eclipse.ice.client.widgets.test;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.widgets.IFormWidgetBuilder;
import org.junit.Test;

/**
 * This class tests the static interface operation
 * IFormWidgetBuilder.getFormWidgetBuilder()
 *
 * @author Menghan Li
 *
 */
public class IFormWidgetBuilderTest {
	/**
	 * Test for {@link org.eclipse.ice.client.widget.IFormWidgetBuilder}.
	 *
	 * @throws CoreException
	 */
	@Test
	public void test() throws CoreException {
		// Simply get the builders from the registry and make sure they are
		// actually there.
		IFormWidgetBuilder [] builders = IFormWidgetBuilder.getFormWidgetBuilders();
		assertNotNull(builders);
		assertTrue(builders.length > 0);
		return;
	}

}
