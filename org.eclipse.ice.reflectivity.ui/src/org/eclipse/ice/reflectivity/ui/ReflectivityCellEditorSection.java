/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Kasper Gammeltoft
 *******************************************************************************/

package org.eclipse.ice.reflectivity.ui;

import org.eclipse.ice.reflectivity.MaterialSelection;
import org.eclipse.january.form.ListComponent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a section for the tabbed properties view for inspecting the
 * currently selected cell in the reflectivity model. This provides a text field
 * to put constraints on a certain cell selection, or just another editor for
 * the table.
 * 
 * FIXME- Still needs to have constraints implemented!
 * 
 * @author Kasper Gammeltoft
 *
 */
public class ReflectivityCellEditorSection extends AbstractPropertySection {

	/**
	 * The selection in the table that this section is editing
	 */
	MaterialSelection selection;

	/**
	 * The text object in this section
	 */
	Text selectionText;

	/**
	 * A label for the selection text
	 */
	Label selectionLabel;

	/**
	 * The list component holding the data for the table
	 */
	ListComponent list;

	/**
	 * Provides the logging service for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ReflectivityCellEditorSection.class);

	/**
	 * Constructor
	 */
	public ReflectivityCellEditorSection() {
		super();
	}

	/**
	 * This operation draws the (initial) controls in the properties view based
	 * on the input.
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		// Get the default background color.
		Color backgroundColor = parent.getBackground();

		GridLayout clientLayout = new GridLayout(2, false);

		// Set the margins and spacing based on the tabbed property
		// constants.
		clientLayout.marginLeft = ITabbedPropertyConstants.HMARGIN;
		clientLayout.marginRight = ITabbedPropertyConstants.HMARGIN;
		clientLayout.marginTop = ITabbedPropertyConstants.VMARGIN;
		clientLayout.marginBottom = ITabbedPropertyConstants.VMARGIN;
		clientLayout.horizontalSpacing = ITabbedPropertyConstants.HSPACE;
		clientLayout.verticalSpacing = ITabbedPropertyConstants.VSPACE;

		// Create the body component
		Composite body = new Composite(parent, SWT.NONE);
		body.setLayout(clientLayout);
		body.setBackground(backgroundColor);

		// Create the label's text for the selection text
		String labelText;
		if (selection != null) {
			labelText = selection.getMaterial().getName() + " "
					+ selection.getSelectedProperty() + ": ";
		} else {
			labelText = "Select A Cell in the Table to edit";
		}
		// Create the label using the default styling
		selectionLabel = getWidgetFactory().createLabel(body, labelText);

		// Create the text field using the default styling
		selectionText = getWidgetFactory().createText(body, "");
		// Add a listener to see when the user presses enter to update the table
		// cell
		selectionText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// Do nothing
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// If the character is enter
				if (e.character == SWT.CR) {
					// Get the text
					String text = selectionText.getText();
					// See if it is a double value. If so, set the value
					try {
						double newVal = Double.parseDouble(text);
						selection.getMaterial().setProperty(
								selection.getSelectedProperty(), newVal);

					} catch (Exception e1) {
						// See if this is a function we are reading
						if (text.startsWith("=")) {
							// Get the value and set the current selection
							getFunctionValue(text);
						} else {
							// Complain
							logger.debug("Invalid entry value: " + text, e1);
						}
					}
				} else if (e.character == '='
						&& selectionText.getText().length() == 0) {
					// TODO implement some sort of function reading or setup-
					// let the user know that we are ready to read a function
				}
			}

		});

		return;
	}

	/**
	 * Gets the value form the provided function and fits the proper constraints
	 * on the list elements specified.
	 * 
	 * FIXME- Needs implementation!
	 * 
	 * @param func
	 *            The string from the text field that should be converted into a
	 *            mathematical function involving the variables from the table.
	 */
	private void getFunctionValue(String func) {
		// Calculate the value of the function provided
	}

	/**
	 * Sets the input for this section. This changes the
	 * {@link MaterialSelection} that is currently selected, and updates the
	 * text field and label appropriately.
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#setInput(
	 *      org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		// Call super
		super.setInput(part, selection);

		// If the selection is a proper selection, then try to set the new
		// values
		if (selection instanceof IStructuredSelection
				&& ((IStructuredSelection) selection).size() >= 3) {
			// Get the second object from the selection, should be the material
			// selection
			Object third = ((IStructuredSelection) selection).toArray()[2];
			// Make sure that the object is a material selection
			if (third instanceof MaterialSelection && third != null) {
				// Set the selection
				this.selection = (MaterialSelection) third;
				// Set the selection label to the new material and property
				selectionLabel.setText(this.selection.getMaterial().getName()
						+ " " + this.selection.getSelectedProperty() + ": ");
				// Set the text of the text field to be the current value for
				// the selected property
				if (this.selection.getSelectedProperty().equals("Name")) {
					// Set the text to the name and disable the text field to
					// show that this value cannot be edited
					this.selectionText
							.setText(this.selection.getMaterial().getName());
					this.selectionText.setEnabled(false);
				} else {
					// Enable the field and set the default value
					this.selectionText.setText(Double
							.toString(this.selection.getMaterial().getProperty(
									this.selection.getSelectedProperty())));
					this.selectionText.setEnabled(true);
				}
			}
		}

	}

	/**
	 * Sets the list component to be used when editing the table values and
	 * setting constraints
	 * 
	 * @param listComp
	 *            The list component
	 */
	public void setListComponent(ListComponent listComp) {
		list = listComp;
	}

	/**
	 * Sets the selection for this section.
	 * 
	 * @param selection
	 *            The material selection for this section
	 */
	public void setMaterialSelection(MaterialSelection selection) {
		this.selection = selection;
	}

}
