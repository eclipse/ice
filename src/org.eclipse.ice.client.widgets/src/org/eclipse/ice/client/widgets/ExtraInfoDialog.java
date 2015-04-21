/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ice.datastructures.form.DataComponent;
import java.util.ArrayList;
import org.eclipse.ice.iclient.uiwidgets.IWidgetClosedListener;
import org.eclipse.ice.datastructures.form.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This class is a subclass of JFace's Dialog that renders a DataComponent to
 * the screen using the EntryComposite. It also maintains a collection of
 * IWidgetClosedListeners which it notifies when the widget is closed. It does
 * this by overriding okPressed() and cancelPressed() from the Dialog base
 * class.
 * 
 * @author Jay Jay Billings
 */
public class ExtraInfoDialog extends Dialog {
	
	/**
	 * The DataComponent displayed for editing by this dialog.
	 */
	private DataComponent dataComp;

	/**
	 * The list of IWidgetClosedListeners that should be notified when the
	 * widget is closed.
	 */
	private ArrayList<IWidgetClosedListener> listeners;

	/**
	 * The set of EntryComposites used to display Entries.
	 */
	private ArrayList<EntryComposite> entryComposites;

	/**
	 * The DataComponentComposite that renders the data
	 */
	DataComponentComposite dataComposite = null;

	/**
	 * This operation retrieves the DataComponent that is currently rendered by
	 * the ExtraInfoDialog.
	 * 
	 * @return
	 *         The DataComponent
	 */
	public DataComponent getDataComponent() {
		return dataComp;
	}

	/**
	 * This operation sets the DataComponent that the dialog should render.
	 * 
	 * @param comp
	 *            The DataComponent
	 */
	public void setDataComponent(DataComponent comp) {
		dataComp = comp;
		return;
	}

	/**
	 * The Constructor
	 * 
	 * @param parentShell
	 *            The shell into which the dialog should be rendered.
	 */
	public ExtraInfoDialog(Shell parentShell) {

		// Call the Dialog constructor
		super(parentShell);

		// Initialize the list of listeners
		listeners = new ArrayList<IWidgetClosedListener>();

		// Initialize the list of Composites
		entryComposites = new ArrayList<EntryComposite>();

		return;
	}

	/**
	 * This operation sets a listener, an IWidgetClosedListener, that is waiting
	 * to be notified with the widget is closed and whether it was closed OK or
	 * if it was cancelled.
	 * 
	 * @param listener
	 *            An IWidgetClosedListener
	 */
	public void setCloseListener(IWidgetClosedListener listener) {
		// Add the widget to the list so long as it is not null
		if (listener != null) {
			listeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(dataComp.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		// Local Declarations
		Composite swtComposite = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout) swtComposite.getLayout();
		Color backgroundColor = getParentShell().getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND);

		// Set the column layout to one so that everything will stack
		layout.numColumns = 1;

		// Add the description as text
		Text text = new Text(swtComposite, SWT.FLAT);
		text.setToolTipText(dataComp.getDescription());
		text.setText(dataComp.getDescription());
		text.setLayoutData(new GridData(255, SWT.DEFAULT));
		text.setEditable(false);
		text.setBackground(backgroundColor);

		// Create the DataComponentComposite that will render the Entries
		dataComposite = new DataComponentComposite(dataComp, swtComposite,
				SWT.FLAT);

		// Set the data composite's layout. This arranges the composite to be a
		// tight column.
		GridLayout dataLayout = new GridLayout(1, true);
		GridData dataGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		dataComposite.setLayout(dataLayout);
		dataComposite.setLayoutData(dataGridData);

		return swtComposite;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {

		// Local Declarations
		Event defaultSelectionEvent = new Event();

		// Notify the listeners
		for (IWidgetClosedListener i : listeners) {
			i.closedOK();
		}
		// Setup the default selection event
		defaultSelectionEvent.type = SWT.DefaultSelection;
		defaultSelectionEvent.widget = this.buttonBar;

		// Notify the Composites that OK was pressed
		dataComposite.notifyListeners(SWT.DefaultSelection,
				defaultSelectionEvent);

		// Call the operation on Dialog
		super.okPressed();

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
	 */
	@Override
	protected void cancelPressed() {

		// Notify the listeners
		for (IWidgetClosedListener i : listeners) {
			i.cancelled();
		}
		// Call the operation on Dialog
		super.cancelPressed();

	}

}