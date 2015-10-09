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
package org.eclipse.ice.client.widgets;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

/**
 * This class extends the FilteredItemsSelectionDialog for a basic list 
 * dialog for Tree Node Child Exemplars with a search bar that allows for 
 * filtering the list. 
 * 
 * @author Alex McCaskey
 *
 */
public class TreeNodeFilteredItemsSelectionDialog extends FilteredItemsSelectionDialog {

	/**
	 * The set of elements that this dialog displays. 
	 */
	private Set<String> elements;

	/**
	 * The constructor. 
	 * 
	 * @param shell
	 * @param list
	 */
	public TreeNodeFilteredItemsSelectionDialog(Shell shell, Set<String> list) {
		super(shell);
		elements = list;
	}

	/**
	 * The constructor for selecting multiple elements at once. 
	 * 
	 * @param shell
	 * @param multi
	 * @param list
	 */
	public TreeNodeFilteredItemsSelectionDialog(Shell shell, boolean multi, Set<String> list) {
		super(shell, multi);
		elements = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#
	 * createExtendedContentArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createExtendedContentArea(Composite parent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getDialogSettings()
	 */
	protected IDialogSettings getDialogSettings() {
		return new DialogSettings("root");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#validateItem(java.
	 * lang.Object)
	 */
	@Override
	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#createFilter()
	 */
	@Override
	protected ItemsFilter createFilter() {
		return new ItemsFilter() {
			public boolean matchItem(Object item) {
				return matches(item.toString());
			}

			public boolean isConsistentItem(Object item) {
				return true;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getItemsComparator()
	 */
	@Override
	protected Comparator getItemsComparator() {
		return new Comparator() {
	         public int compare(Object arg0, Object arg1) {
	            return arg0.toString().compareTo(arg1.toString());
	         }
	      };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#fillContentProvider(
	 * org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.
	 * AbstractContentProvider,
	 * org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
			IProgressMonitor progressMonitor) throws CoreException {
		progressMonitor.beginTask("Searching", elements.size()); //$NON-NLS-1$
		for (Iterator iter = elements.iterator(); iter.hasNext();) {
			contentProvider.add(iter.next(), itemsFilter);
			progressMonitor.worked(1);
		}
		progressMonitor.done();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getElementName(java.
	 * lang.Object)
	 */
	@Override
	public String getElementName(Object item) {
		 return item.toString();
	}

}
