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
package org.eclipse.ice.client.common.properties;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;

/**
 * This class provides an alternative {@link CellLabelProvider} that extends
 * {@link OwnerDrawLabelProvider} so that painting the cell can be customized.
 * 
 * @see CellColumnLabelProvider
 * 
 * @author Jordan H. Deyton
 * 
 */
public abstract class CellOwnerDrawLabelProvider extends
		OwnerDrawLabelProvider implements ILabelProvider {

	/**
	 * The content provider. The methods required as a
	 * <code>CellLabelProvider</code> and <code>ILabelProvider</code> are passed
	 * to this content provider.
	 */
	protected final ICellContentProvider contentProvider;

	/**
	 * The default constructor.
	 * 
	 * @param contentProvider
	 *            The content provider. The methods required as a
	 *            <code>CellLabelProvider</code> and <code>ILabelProvider</code>
	 *            are passed to this content provider.
	 */
	public CellOwnerDrawLabelProvider(
			ICellContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return contentProvider.getImage(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		return contentProvider.getText(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.CellLabelProvider#getToolTipText(java.lang.
	 * Object)
	 */
	@Override
	public String getToolTipText(Object element) {
		return contentProvider.getToolTipText(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.OwnerDrawLabelProvider#measure(org.eclipse.
	 * swt.widgets.Event, java.lang.Object)
	 */
	@Override
	protected abstract void measure(Event event, Object element);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.OwnerDrawLabelProvider#paint(org.eclipse.swt
	 * .widgets.Event, java.lang.Object)
	 */
	@Override
	protected abstract void paint(Event event, Object element);

}
