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
package org.eclipse.eavp.viz.service.connections.preferences;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * This class provides a basic {@link ColumnLabelProvider} for
 * {@link ICellContentProvider}s. Its methods are simply hooked up to the
 * corresponding methods in the <code>ICellContentProvider</code>.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CellColumnLabelProvider extends ColumnLabelProvider {

	/**
	 * The content provider. The methods required as a
	 * <code>ColumnLabelProvider</code> are passed to this content provider.
	 */
	protected final IVizCellContentProvider contentProvider;

	/**
	 * The default constructor.
	 * 
	 * @param contentProvider
	 *            The content provider. The methods required as a
	 *            <code>ColumnLabelProvider</code> are passed to this content
	 *            provider.
	 */
	public CellColumnLabelProvider(IVizCellContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ColumnLabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return contentProvider.getImage(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
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

}
