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

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * This class extends Dialog to present the user with a means to provide frame
 * rates beyond few pre-set selections.
 * 
 * @author tnp
 */
public class CustomFrameRateDialog extends Dialog {

	/**
	 * <p>
	 * Structure for the collection of frame rates.
	 * </p>
	 */
	private ArrayList<Integer> rates = new ArrayList<Integer>();

	/**
	 * <p>
	 * The text field in the dialog in which to enter a new frame rate value.
	 * </p>
	 */
	private Text addRateText;

	/**
	 * <p>
	 * List area in the dialog to display the rates ArrayList.
	 * </p>
	 */
	private List rateList;

	/**
	 * <p>
	 * Field for storing the frame rate.
	 * </p>
	 */
	private int frameRate;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 * @param parentShell
	 *            <p>
	 *            The parent structure of this dialog
	 *            </p>
	 * 
	 * @param inputRates
	 *            <p>
	 *            Input point for rates structure maintained by the calling
	 *            class
	 *            </p>
	 */
	public CustomFrameRateDialog(Shell parentShell,
			ArrayList<Integer> inputRates) {

		super(parentShell);
		// Copy to the local structure
		rates = inputRates;

		return;
	}

	/**
	 * <p>
	 * Function used to add text to the dialog window frame.
	 * </p>
	 * 
	 * @param shell
	 *            <p>
	 *            The Shell containing the dialog
	 *            </p>
	 */
	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText("Select frame rate");

		return;
	}

	/**
	 * <p>
	 * Function to configure the dialog layout and components.
	 * </p>
	 * 
	 * @param parent
	 *            <p>
	 *            The composite to whom this dialog belongs
	 *            </p>
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		// Initialize the layout
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(3, false);
		composite.setLayout(layout);

		// The first part of the instructions
		Label addInstr = new Label(composite, SWT.NONE);
		addInstr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));
		addInstr.setText("Enter a new frame rate:");

		// Create the text field for entering a new frame rate
		addRateText = new Text(composite, SWT.BORDER);
		addRateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		// Frames per second label to follow the text field
		Label fpsLabel = new Label(composite, SWT.NONE);
		fpsLabel.setText("fps");

		// The second part of the instructions
		Label selInstr = new Label(composite, SWT.NONE);
		selInstr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));
		selInstr.setText("Or select a frame rate:");

		// Area to display and select previously used frame rates
		rateList = new List(composite, SWT.BORDER);
		for (int rate : rates) {
			rateList.add(rate + "fps");
		}
		rateList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));

		return composite;
	}

	/**
	 * <p>
	 * Function called when "OK" is pressed in the dialog. Store and use a new
	 * rate when a valid value is entered, use a selected rate, or present an
	 * error message for failing to follow instructions.
	 * </p>
	 */
	@Override
	protected void okPressed() {

		try {
			// Get the entered frame rate if available
			String newRateStr = addRateText.getText();
			int newRate = Integer.parseInt(newRateStr);
			// See if the new rate is unique to the stored list. If so, add the
			// new rate to the list and sort the list.
			if (!rates.contains(newRate)) {
				rates.add(newRate);
				Collections.sort(rates);
			}
			// Set the frame rate
			frameRate = newRate;
			super.okPressed();
		} catch (NumberFormatException e) {
			// If no rate is entered, see if one is selected.
			int selectionIndex = rateList.getSelectionIndex();
			if (selectionIndex > -1) {
				// Set the selected frame rate
				frameRate = rates.get(selectionIndex);
				super.okPressed();
			} else {
				// If no rate is selected, present an error message.
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				MessageDialog.openError(shell, "Invalid Entry",
						"Please enter an integer value frame rate or select a"
								+ "frame rate from the list.");
			}
		}

		return;
	}

	/**
	 * <p>
	 * Public access to the frameRate field
	 * </p>
	 * 
	 * @return <p>
	 *         The value stored in the frameRate field
	 *         </p>
	 */
	public int getFrameRate() {
		return frameRate;
	}

}