/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ice.client.widgets.properties.ICellContentProvider;
import org.eclipse.ice.client.widgets.properties.CellOwnerDrawLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;

/**
 * This class implements the necessary methods to support drawing the
 * platform-specific <code>Image</code>s checkbox
 * <code>Button<code> in the proper location in the cells.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CheckboxCellLabelProvider extends
		CellOwnerDrawLabelProvider {

	/**
	 * The default constructor.
	 * 
	 * @param contentProvider
	 *            The content provider. The methods required as a
	 *            <code>CellLabelProvider</code> and <code>ILabelProvider</code>
	 *            are passed to this content provider.
	 */
	public CheckboxCellLabelProvider(ICellContentProvider contentProvider) {
		super(contentProvider);
	}

	// TODO Paint the Images in the middle of the cell.

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.properties.
	 * TreePropertyCellOwnerDrawLabelProvider
	 * #measure(org.eclipse.swt.widgets.Event, java.lang.Object)
	 */
	@Override
	protected void measure(Event event, Object element) {
		// Get the corresponding Image and its bounds.
		Image image = getImage(element);
		Rectangle rect = image.getBounds();
		// The content of the cell should have the width/height of the Image.
		event.width = rect.width;
		event.height = rect.height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.properties.
	 * TreePropertyCellOwnerDrawLabelProvider
	 * #paint(org.eclipse.swt.widgets.Event, java.lang.Object)
	 */
	@Override
	protected void paint(Event event, Object element) {
		// Get the corresponding Image and its bounds.
		Image image = getImage(element);
		// Rectangle rect = image.getBounds();
		event.gc.drawImage(image, event.x, event.y);
	}

}
