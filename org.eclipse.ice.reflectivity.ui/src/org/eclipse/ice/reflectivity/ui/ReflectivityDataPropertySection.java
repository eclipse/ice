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

import org.eclipse.ice.client.widgets.DataComponentComposite;
import org.eclipse.ice.client.widgets.IEntryComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * Provides the custom section for the data component in the reflectivity model.
 * This section is rendered in the properties view for the data component tab,
 * labeled info. Puts the {@link DataComponent} into a
 * {@link DataComponentComposite} and adds that composite to the properties
 * view. If the data that is added should be hidden, use the
 * {@link ReflectivityDataPropertySection#setIsEnabled(boolean)} method to
 * change the state of the entries to the value provided. The entries are set to
 * be enabled by default.
 *
 * @author Kasper Gammeltoft
 *
 */
public class ReflectivityDataPropertySection extends AbstractPropertySection {

	/**
	 * The data component that will hold the values for the user's input
	 */
	private DataComponent data;

	/**
	 * The data composite that displays and renders the data component entries
	 */
	private DataComponentComposite dataComposite;

	/**
	 * True if the EntryComposites are enabled for viewing.
	 */
	private boolean isEnabled = true;

	/**
	 * The <code>Section</code> that holds the data component or list component
	 * property information.
	 */
	private Section section;

	/**
	 * The constructor
	 */
	public ReflectivityDataPropertySection() {
		super();

		return;
	}

	/**
	 * This operation draws the (initial) controls in the properties view based
	 * on the input. Adds the <code>DataComponent</code> to a
	 * <code>DataComponentComposite</code> to be used in the properties view.
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		// Get the default background color.
		Color backgroundColor = parent.getBackground();

		// Create a section for the data composites.
		section = getWidgetFactory().createSection(parent,
				Section.SHORT_TITLE_BAR | Section.DESCRIPTION);
		section.setText(data.getName());
		section.setDescription(data.getDescription());
		section.setBackground(backgroundColor);

		dataComposite = new DataComponentComposite(data, section, SWT.NONE);

		// Sets the entries to be enabled to the state of the isEnabled flag
		Control[] children = dataComposite.getChildren();
		for (Control child : children) {
			if (child instanceof IEntryComposite) {
				IEntryComposite entry = (IEntryComposite) child;
				entry.getComposite().setEnabled(isEnabled);
			}
		}

		GridLayout clientLayout = new GridLayout(2, true);

		// Set the margins and spacing based on the tabbed property
		// constants.
		clientLayout.marginLeft = ITabbedPropertyConstants.HMARGIN;
		clientLayout.marginRight = ITabbedPropertyConstants.HMARGIN;
		clientLayout.marginTop = ITabbedPropertyConstants.VMARGIN;
		clientLayout.marginBottom = ITabbedPropertyConstants.VMARGIN;
		clientLayout.horizontalSpacing = ITabbedPropertyConstants.HSPACE;
		clientLayout.verticalSpacing = ITabbedPropertyConstants.VSPACE;
		dataComposite.setLayout(clientLayout);

		// Make the background of the section client white unless ICE is in
		// debug mode.
		if (System.getProperty("DebugICE") == null) {
			dataComposite.setBackground(backgroundColor);
		} else {
			dataComposite.setBackground(
					Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		}

		// Adapt the composite to be rendered with the default widget factory
		// toolkit
		getWidgetFactory().adapt(dataComposite);

		// Set the client area for the section.
		section.setClient(dataComposite);

		return;
	}

	/**
	 * Sets the input, or the data component, for this property section. If
	 * multiple selections, assumes the first one is the data component to be
	 * selected. Will discard anything else that is the first element in the
	 * selection (not a data component).
	 *
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#setInput(
	 *      org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj instanceof DataComponent) {
				data = (DataComponent) obj;
			}
		}

		return;
	}

	/**
	 * This operation manually sets the data component to be displayed by the
	 * properties view. Set this property before the section is graphically
	 * constructed.
	 *
	 * @param dataComp
	 *            The data component to display
	 */
	public void setDataComponent(DataComponent dataComp) {
		data = dataComp;
	}

	/**
	 * Sets the enabled state of the entry composites in this section. This is
	 * set to true by default.
	 *
	 * @param enabled
	 *            True if the entries are enabled, false if otherwise.
	 */
	public void setIsEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#
	 * aboutToBeHidden()
	 */
	@Override
	public void aboutToBeHidden() {
		super.aboutToBeHidden();
		return;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#
	 * aboutToBeShown()
	 */
	@Override
	public void aboutToBeShown() {
		super.aboutToBeShown();
		return;
	}

}
