/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation
 *   - Kasper Gammeltoft
 *******************************************************************************/

package org.eclipse.eavp.viz.service.test;

import org.eclipse.eavp.viz.service.styles.AbstractSeriesStyle;

/**
 * This class provides a fake implementation of a series style to use for
 * testing.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class FakeSeriesStyle extends AbstractSeriesStyle {

	public static final String NAME = "name";
	public static final String THING = "thing";
	public static final String COLOR = "color";

	/**
	 * The constructor. Provides the initial properties to the properties map
	 */
	public FakeSeriesStyle() {
		properties.put(NAME, "");
		properties.put(THING, null);
		properties.put(COLOR, null);
	}

}
