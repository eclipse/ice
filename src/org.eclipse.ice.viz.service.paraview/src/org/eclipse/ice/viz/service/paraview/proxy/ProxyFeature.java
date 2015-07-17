/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.proxy;

public class ProxyFeature extends ProxyProperty {

	protected final boolean canColorBy;
	protected final ColorByMode colorByMode;
	protected final ColorByLocation colorByLocation;

	public enum ColorByMode {
		SOLID, ARRAY;
	}

	public enum ColorByLocation {
		POINTS, CELLS;
	}

	public ProxyFeature(String name, int index) {
		this(name, index, null, null);
	}

	public ProxyFeature(String name, int index, ColorByMode mode,
			ColorByLocation location) {
		super(name, index, PropertyType.DISCRETE_MULTI);

		canColorBy = (mode != null && location != null);
		colorByMode = (mode != null ? mode : ColorByMode.SOLID);
		colorByLocation = (location != null ? location
				: ColorByLocation.POINTS);
	}

	@Override
	protected int getProxyId() {
		return proxy != null ? proxy.getFileId() : -1;
	}

}
