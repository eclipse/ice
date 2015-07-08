/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog box for querying the user about which visualization service to use
 * during the opening of a PlotEditor.
 * 
 * @author Robert Smith
 *
 */
public class PlotEditorDialog extends Dialog {
	// The index of the visualization service selected by the user in the combo
	// box.
	private int selectedServiceIndex;

	/**
	 * Default constructor.
	 * 
	 * @param parentShell
	 */
	public PlotEditorDialog(Shell parentShell) {
		super(parentShell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		return container;
	}

	/**
	 * Get method for the index of the selected service.
	 * 
	 * @return The index of the service selected by the user form the combo box.
	 */
	protected int getSelection() {
		return selectedServiceIndex;
	}

	/**
	 * Creates the dialog box.
	 * 
	 * @param parent
	 *            The parent composite
	 * @param serviceNamesArray
	 *            An array of names of available services to populate the combo
	 *            box.
	 * @return Returns the dialog window's composite.
	 */
	public Composite createDialogArea(Composite parent,
			String[] serviceNamesArray) {
		// Set up the window.
		//Container to be returned, create by superclass's constructor
		Composite container = (Composite) super.createDialogArea(parent);
		
		//The current display
		Display display = Display.getCurrent();
		
		//The current shell
		final Shell shell = container.getShell();
		
		//The combo box containing the visualization service names
		final Combo combo = new Combo(shell, SWT.NONE);
		
		//Name window and give it a grid layout
		shell.setText("Open a Visualization File");
		shell.setLayout(new GridLayout());
		
		//Populate the combo box and give it a default message
		combo.setItems(serviceNamesArray);
		combo.setText("Select a visualization service.");

		// Update the selected index when the user makes a new selection.
		combo.addListener(SWT.DefaultSelection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				selectedServiceIndex = combo.getSelectionIndex();
			}
		});
		
		//Set the size of the combo box
		combo.setLayoutData(new GridData(300, 100, false, false));
		
		//Create the Ok button
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Ok");

		// Close the window when the ok button is pressed.
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		
		//Set the ok button's size
		button.setLayoutData(new GridData(SWT.CENTER));
		shell.setDefaultButton(button);
		shell.setSize(200, 100);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(250, 100);
	}

}
