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
package org.eclipse.ice.client.widgets.analysis;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * This class implements methods commonly shared between {@link IColorFactory}
 * implementations.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractColorFactory implements IColorFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.reactoreditor.IColorFactory#createColor(
	 * org.eclipse.swt.widgets.Display, int)
	 */
	@Override
	public Color createColor(Display display, int hex) {
		// Get the default display if necessary.
		if (display == null) {
			display = Display.getCurrent();
			if (display == null) {
				display = Display.getDefault();
			}
		}
		/*-
		 * To create a color, we must convert its hex values into its
		 * corresponding RGB values. This is calculated using the following:
		 * 
		 * red = hex value shifted right 2 bytes
		 * green = hex value shifted right 1, then remove red byte with &
		 * blue = hex value left as is, then remove red and green bytes with &
		 */
		hex &= 0xffffff; // Remove any extraneous bits or any sign.
		return new Color(display, hex >> 16, hex >> 8 & 0x00ff, hex & 0x0000ff);
	}

}
