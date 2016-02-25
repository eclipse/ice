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
package org.eclipse.eavp.viz.service.geometry.widgets;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

/**
 * <p>
 * Dialog box displaying parameters for the Replication action
 * </p>
 * <p>
 * Replicating a selected shape will clone it a number of times such that the
 * final number of similar shapes is equal to the given Quantity parameter. Each
 * repeated shape will be shifted by the given Translation parameter relative to
 * the previous shape.
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class ReplicateDialog extends Dialog {

    /**
     * <p>
     * The quantity spinner's value
     * </p>
     * 
     */
    private int quantity;

    /**
     * <p>
     * An array of the shift spinner's values
     * </p>
     * 
     */
    private double[] shift = new double[3];

    /**
     * Shift spinners
     */
    private RealSpinner[] shiftSpinners = new RealSpinner[3];

    /**
     * Quantity spinner
     */
    private Spinner quantitySpinner;

    /**
     * Initializes the Dialog with the given shell
     * 
     * @param parentShell
     *            The shell
     */
    public ReplicateDialog(Shell parentShell) {
        super(parentShell);
    }

    /**
     * <p>
     * Returns the translation parameters
     * </p>
     * 
     * @return
     *         <p>
     *         An array of three values representing the translation parameters
     *         </p>
     */
    public double[] getShift() {
        return shift;
    }

    /**
     * <p>
     * Returns the desired number of total similar shapes when cloning
     * </p>
     * 
     * @return
     *         <p>
     *         The quantity
     *         </p>
     */
    public int getQuantity() {
        return quantity;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);

        newShell.setText("Replicate");
    }

    /**
     * Creates the buttons to close the Dialog
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {

        // Local Declarations
        String okLabel = "Ok", cancelLabel = "Cancel";

        // Create der butans
        createButton(parent, IDialogConstants.OK_ID, okLabel, true);
        createButton(parent, IDialogConstants.CANCEL_ID, cancelLabel, false);
    }

    /**
     * Create the widgets for the main composite for the Dialog
     */
    @Override
    protected Control createDialogArea(Composite parent) {

        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(4, false));

        Label totalLabel = new Label(container, SWT.NONE);
        totalLabel.setText("Quantity");

        quantitySpinner = new Spinner(container, SWT.BORDER);
        quantitySpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        quantitySpinner.setValues(1, 1, 10000, 0, 1, 10);
        new Label(container, SWT.NONE);

        // Create the X, Y, Z coordinate labels

        Label labelX = new Label(container, SWT.NONE);
        labelX.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        labelX.setText("X");

        Label labelY = new Label(container, SWT.NONE);
        labelY.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        labelY.setText("Y");

        Label labelZ = new Label(container, SWT.NONE);
        labelZ.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        labelZ.setText("Z");

        // Create the shift label

        Label shiftLabel = new Label(container, SWT.NONE);
        shiftLabel.setText("Shift");

        // Create the X, Y, Z shift spinners

        for (int i = 0; i < 3; i++) {
            shiftSpinners[i] = new RealSpinner(container);
            shiftSpinners[i].setBounds(-1.0e6, 1.0e6);
            shiftSpinners[i].getControl().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }

        return container;
    }

    @Override
    protected void okPressed() {

        // Fire a default selection event on the spinners so they update
        // their state

        Event defaultSelectionEvent = new Event();
        defaultSelectionEvent.type = SWT.DefaultSelection;

        for (RealSpinner spinner : shiftSpinners) {
            spinner.getControl().notifyListeners(SWT.DefaultSelection, defaultSelectionEvent);
        }

        // Save the quantity value

        quantity = quantitySpinner.getSelection();

        // Save the shift values

        for (int i = 0; i < 3; i++) {

            shift[i] = shiftSpinners[i].getValue();
        }

        // Call Dialog's okPressed operation

        super.okPressed();
    }
}