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

/**
 * Provides a section for the tabbed properties view for inspecting the
 * currently selected cell in the reflectivity model. This provides a text field
 * to put constraints on a certain cell selection, or just another editor for
 * the table.
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

		// Create the label for the selection text
		selectionLabel = new Label(body, SWT.NONE);
		String labelText;
		if (selection != null) {
			labelText = selection.getMaterial().getName() + " "
					+ selection.getSelectedProperty() + ": ";
		} else {
			labelText = "Select A Cell in the Table to edit";
		}
		selectionLabel.setText(labelText);

		selectionText = new Text(body, SWT.SEARCH);
		selectionText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR) {
					System.out.println("Enter pressed");
					String text = selectionText.getText();
					try {
						double newVal = Double.parseDouble(text);
						selection.getMaterial().setProperty(
								selection.getSelectedProperty(), newVal);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// Complain

			}

		});

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#setInput(
	 * org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		// Call super
		super.setInput(part, selection);

		// If the selection is a proper selection, then try to set the new
		// values
		if (selection instanceof IStructuredSelection
				&& ((IStructuredSelection) selection).size() >= 2) {
			// Get the second object from the selection, should be the material
			// selection
			Object second = ((IStructuredSelection) selection).toArray()[1];
			// Make sure that the object is a material selection
			if (second instanceof MaterialSelection && second != null) {
				// Set the selection
				this.selection = (MaterialSelection) second;
				// Set the selection label to the new material and property
				selectionLabel.setText(this.selection.getMaterial().getName()
						+ " " + this.selection.getSelectedProperty() + ": ");
				// Set the text of the text field to be the current value for
				// the selected property
				if (this.selection.getSelectedProperty().equals("Name")) {
					this.selectionText
							.setText(this.selection.getMaterial().getName());
					this.selectionText.setEnabled(false);
				} else {
					this.selectionText.setText(Double
							.toString(this.selection.getMaterial().getProperty(
									this.selection.getSelectedProperty())));
					this.selectionText.setEnabled(true);
				}
			}
		}

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
