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
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * Provides the custom section for the data component in the reflectivity model.
 * This section is rendered in the properties view for the data component tab.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class ReflectivityPropertySection extends AbstractPropertySection {

	/**
	 * The data component that will hold the values for the user's input
	 */
	private DataComponent data;

	/**
	 * The data composite that displays and renders the data component entries
	 */
	private DataComponentComposite dataComposite;

	/**
	 * The <code>Section</code> that holds the data component or list component
	 * property information.
	 */
	private Section section;

	/**
	 * The constructor
	 */
	public ReflectivityPropertySection() {
		super();

		return;
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

		// Create a section for the data composites.
		section = getWidgetFactory().createSection(parent,
				Section.SHORT_TITLE_BAR | Section.DESCRIPTION);
		section.setText("Reflectivity Inputs:");
		section.setDescription(
				"Give a wave vector, a number of layers of roughness "
						+ "between interfaces, and the angles. ");
		section.setBackground(backgroundColor);

		dataComposite = new DataComponentComposite(data, section, SWT.NONE);
		GridLayout clientLayout = new GridLayout(2, false);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#setInput(
	 * org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
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
	 * This operator manually sets the data component to be displayed by the
	 * properties view
	 * 
	 * @param dataComp
	 *            The data component to display
	 */
	public void setDataComponent(DataComponent dataComp) {
		data = dataComp;
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
