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

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ice.client.common.ComponentPropertySection;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * This class extends the {@link ComponentPropertySection}, which displays
 * tabbed properties for Components inside a TreeComposite, to provide
 * additional widgets in support of {@link AdaptiveTreeComposite}s.<br>
 * <br>
 * Currently, this class adds a {@link Combo} widget that lets the user set the
 * AdaptiveTreeComposite's type.
 * 
 * @author djg
 * 
 */
public class MOOSETypePropertySection extends ComponentPropertySection {

	/**
	 * The Composite that contains the widgets used to select the type of
	 * AdaptiveTreeComposite.
	 */
	private Composite typeComposite;

	/**
	 * A Combo representing the available types for an AdaptiveTreeComposite.
	 */
	private Combo typeCombo;

	/**
	 * The AdaptiveTreeComposite managed by this property section if it exists.
	 */
	private AdaptiveTreeComposite adaptiveTree;

	/**
	 * The default constructor.
	 */
	public MOOSETypePropertySection() {

		// Initialize the widgets and initial tree to null.
		typeComposite = null;
		typeCombo = null;
		adaptiveTree = null;

		return;
	}

	/**
	 * This operation draws the (initial) controls in the properties view based
	 * on the input. In this case, there are initially no widgets to prepare.
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		// Get the current Section.
		Section section = getSection();

		// Update the section title and description.
		section.setText("Data Parameters");
		section.setDescription(section.getDescription()
				+ " If the block's type is changed, additional properties are exposed.");

		return;
	}

	/**
	 * This operation sets the current input displayed in the MOOSE tabbed
	 * properties. Specifically, it sets {@link #adaptiveTree} if the input is
	 * an {@link AdaptiveTreeComposite}.
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		// Since the tree's DataComponent will still have its properties shown,
		// continue with the default setInput() operation.
		super.setInput(part, selection);

		// Get and check the selection.
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();

		// Try to get the input AdaptiveTreeComposite from the selection.
		if (input instanceof AdaptiveTreeComposite) {
			// Update the reference to the AdaptiveTreeComposite.
			adaptiveTree = (AdaptiveTreeComposite) input;

			System.out.println("MOOSETypePropertySection message: "
					+ "Setting the input to adaptive tree "
					+ adaptiveTree.getName() + ".");
		} else {
			// Otherwise, nullify the reference to the AdaptiveTreeComposite.
			adaptiveTree = null;

			System.out.println("MOOSETypePropertySection message: "
					+ "The input is not an adaptive tree.");
		}

		return;
	}

	/**
	 * Refreshes any widgets required for the current input for this property
	 * section.
	 */
	@Override
	public void refresh() {

		if (adaptiveTree != null) {
			// If they have not been rendered, create the widgets used to set
			// the adaptive tree's type.
			if (typeComposite == null) {

				// Make sure the typeCombo is at the top of the section by
				// removing all DataComponentComposites.
				for (Control control : sectionControls) {
					control.dispose();
				}
				sectionControls.clear();

				// Get the section's client for rendering.
				Composite client = (Composite) getSection().getClient();

				// Get the client's background color.
				Color backgroundColor = client.getBackground();

				// Create the type sub-section Composite that will contain the
				// type label and combo (dropdown).
				typeComposite = new Composite(client, SWT.NONE);
				typeComposite.setLayoutData(new GridData(SWT.BEGINNING,
						SWT.BEGINNING, false, true));
				typeComposite.setBackground(backgroundColor);

				// Set the layout of the Composite to a vertical fill layout.
				// This ensures the type Label is above the Combo and that both
				// consume all available space.
				FillLayout typeCompositeLayout = new FillLayout(SWT.VERTICAL);
				typeCompositeLayout.marginHeight = 5;
				typeCompositeLayout.marginWidth = 3;
				typeCompositeLayout.spacing = 5;
				typeComposite.setLayout(typeCompositeLayout);

				// Add the type Label and an empty Combo.
				Label typeLabel = new Label(typeComposite, SWT.NONE);
				typeLabel.setText("Type:");
				typeLabel.setBackground(backgroundColor);
				typeCombo = new Combo(typeComposite, SWT.DROP_DOWN
						| SWT.READ_ONLY);
				typeCombo.setBackground(backgroundColor);

				// Hook up a listener to the Combo to set the current type.
				typeCombo.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						setType(typeCombo.getText());
					}
				});
			}

			// Replace the available types in the type Combo widget.
			List<String> types = adaptiveTree.getTypes();
			typeCombo.removeAll();
			for (String allowedType : types) {
				typeCombo.add(allowedType);
			}

			// Select the tree's current type in the Combo if possible.
			String type = adaptiveTree.getType();
			if (type != null) {
				typeCombo.setText(type);
			}
		}
		// If there is no adaptive tree but the type widgets exist, we need to
		// dispose them.
		else if (typeComposite != null) {

			// Dispose of the typeComposite and its Controls (this includes
			// typeCombo).
			for (Control control : typeComposite.getChildren()) {
				control.dispose();
			}
			typeComposite.dispose();
			// Nullify the references to the Combo and its Composite.
			typeCombo = null;
			typeComposite = null;
		}

		// Now perform the usual refresh operation.
		super.refresh();

		return;
	}

	/**
	 * Sets the type of the {@link AdaptiveTreeComposite}.
	 * 
	 * @param type
	 *            A String value from the {@link #typeCombo} representing an
	 *            allowed type for the {@link #adaptiveTree}.
	 */
	private void setType(String type) {
		System.out.println("MOOSETypePropertySection message: "
				+ "Setting the type for " + adaptiveTree.getName() + " to "
				+ type + ".");
		adaptiveTree.setType(typeCombo.getText());

		// Refresh the widgets now that the type has changed. This should update
		// the properties because the DataComponent changes.
		refresh();

		return;
	}

}
