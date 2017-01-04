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
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.january.form.AdaptiveTreeComposite;
import org.eclipse.january.form.Component;
import org.eclipse.january.form.DataComponent;
import org.eclipse.january.form.GeometryComponent;
import org.eclipse.january.form.IComponentVisitor;
import org.eclipse.january.form.IReactorComponent;
import org.eclipse.january.form.ListComponent;
import org.eclipse.january.form.MasterDetailsComponent;
import org.eclipse.january.form.MatrixComponent;
import org.eclipse.january.form.MeshComponent;
import org.eclipse.january.form.ResourceComponent;
import org.eclipse.january.form.TableComponent;
import org.eclipse.january.form.TimeDataComponent;
import org.eclipse.january.form.TreeComposite;
import org.eclipse.january.form.emf.EMFComponent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for ICE Components in a TabbedPropertiesWindow.
 * 
 * @author Jay Jay Billings, Jordan H. Deyton
 * 
 */
public class ComponentPropertySection extends AbstractPropertySection
		implements IComponentVisitor {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ComponentPropertySection.class);

	/**
	 * The tree composite provided to this section as input.
	 */
	private TreeComposite treeComp;

	/**
	 * The section that contains the properties of the TreeComposite.
	 */
	private Section section;

	/**
	 * A list of Controls rendered in the section. When new input is set, all
	 * Controls in this list should be disposed, and all new
	 * DataComponentComposites (or any Controls) should be added to this list
	 * for later cleanup.
	 */
	protected final List<Control> sectionControls = new ArrayList<Control>();

	/**
	 * The index of the Component in its parent TreeComposite. This is used in
	 * the visit operations.
	 */
	private int index;

	/**
	 * The ScrolledComposite that occupies the Property Viewer. Its minimum size
	 * (the size at which it starts drawing scroll bars) should be updated
	 * because of GridLayouts in this Section.
	 */
	private ScrolledComposite scrollComposite;

	/**
	 * The client Composite for the {@link #scrollComposite}.
	 */
	private Composite scrollCompositeClient;

	/**
	 * This operation draws the controls in the properties view based on the
	 * input.
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		// Get the default background color (white).
		Color backgroundColor = parent.getBackground();

		// Create a section for the data composites.
		section = getWidgetFactory().createSection(parent,
				ExpandableComposite.SHORT_TITLE_BAR | Section.DESCRIPTION);
		section.setText("Node properties");
		section.setDescription("All properties available for "
				+ "this node can be modified here.");
		section.setBackground(backgroundColor);

		// Create the Composite that contains all DataComponentComposites.
		final Composite client = new Composite(section, SWT.NONE);
		// Set the layout of the client area so that all DataComponentComposites
		// are stacked on top of each other.
		GridLayout clientLayout = new GridLayout();
		// Set the margins and spacing based on the tabbed property constants.
		clientLayout.marginLeft = ITabbedPropertyConstants.HMARGIN;
		clientLayout.marginRight = ITabbedPropertyConstants.HMARGIN;
		clientLayout.marginTop = ITabbedPropertyConstants.VMARGIN;
		clientLayout.marginBottom = ITabbedPropertyConstants.VMARGIN;
		clientLayout.horizontalSpacing = ITabbedPropertyConstants.HSPACE;
		clientLayout.verticalSpacing = ITabbedPropertyConstants.VSPACE;
		client.setLayout(clientLayout);

		// Make the background of the section client white unless ICE is in
		// debug mode.
		if (System.getProperty("DebugICE") == null) {
			client.setBackground(backgroundColor);
		} else {
			client.setBackground(
					Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		}

		// Set the client area for the section.
		section.setClient(client);

		// Get the property viewer's ScrolledComposite and its first Composite
		// (its "client" Composite).
		scrollCompositeClient = section.getParent().getParent().getParent()
				.getParent();
		scrollComposite = (ScrolledComposite) scrollCompositeClient.getParent();

		// Add a listener to resize the Section's properties and update the
		// ScrollComposite's minimum bounds correctly based on the displayed
		// properties.
		scrollComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				resizeProperties();
			}
		});

		return;
	}

	/**
	 * This operation accepts input updates from the workbench. This may be
	 * called multiple times. It should only be used to store information about
	 * the current input.
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);

		logger.info("ComponentPropertySection message: " + "Setting input.");

		// Get and check the selection.
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();

		// Try to get the input TreeComposite from the selection.
		if (input != treeComp && input instanceof TreeComposite) {
			// Update the reference to the input TreeComposite.
			treeComp = (TreeComposite) input;

			logger.info("ComponentPropertySection message: "
					+ "Input changed to " + treeComp.getName());
		}

		return;
	}

	/**
	 * This operation refreshes widgets in the tabbed properties section based
	 * on the current input. It may be called multiple times.
	 */
	@Override
	public void refresh() {
		super.refresh();

		logger.info("ComponentPropertySection message: "
				+ "Refreshing properties.");

		// Disable redrawing until the properties are completely redrawn. This
		// reduces flicker that occurs when several widgets are added to the
		// section.
		section.setRedraw(false);

		// Get the list of data nodes for the TreeComposite if possible
		ArrayList<Component> dataNodes = treeComp.getDataNodes();
		int size = dataNodes.size();

		// Remove extra controls.
		for (int i = sectionControls.size() - 1; i >= size; i--) {
			sectionControls.get(i).dispose();
			sectionControls.remove(i);
		}

		// Visit all of the data nodes and draw them
		for (index = 0; index < size; index++) {
			dataNodes.get(index).accept(this);
		}

		// Adjust the size of the section client and update the Properties
		// Viewer's ScrolledComposite.
		resizeProperties();

		// Re-enable redrawing the section.
		section.setRedraw(true);

		return;
	}

	/**
	 * Gets the section that contains the properties of the TreeComposite.
	 * 
	 * @return The decorated section. To draw in the section, get its client
	 *         Composite.
	 */
	protected Section getSection() {
		return section;
	}

	/**
	 * Resizes the Section's client and updates the Property Viewer's
	 * {@link ScrolledComposite} to account for GridLayouts in the client.
	 */
	private void resizeProperties() {
		// Disable re-drawing for the ScrolledComposite.
		scrollComposite.setRedraw(false);

		// FIXME - Remove this if the second fix below works better.
		// Get the width of the ScrolledComposite's client area.
		// int width = scrollComposite.getClientArea().width;

		// Recompute the size of the first Composite in the ScrolledComposite
		// based on the width of the ScrolledComposite's client area.
		Point size = scrollCompositeClient.computeSize(
				scrollComposite.getClientArea().width, SWT.DEFAULT);

		// Update the size of the ScrolledComposite's client.
		scrollCompositeClient.setSize(size);
		// Set the minimum size at which the ScrolledComposite will start
		// drawing scroll bars.
		scrollComposite.setMinSize(size);
		// We need to call layout() so the ScrolledComposite will update.
		scrollComposite.layout();

		// FIXME - Remove this is the second fix works better.
		// The parent of the Section has a FillLayout. This code forces it to
		// adjust its size. This fixes a bug in certain instances where it does
		// not lay out within its parent.
		// Composite parent = section.getParent();
		// parent.setSize(parent.computeSize(width, SWT.DEFAULT));

		// FIXME - Remove this if the previous fix works better.
		// The parent of the Section has a FillLayout. Its parent has a
		// GridLayout, but sometimes it does not update. Tell it to layout so
		// the Section's parent will update its size.
		section.getParent().getParent().layout();

		// Enable re-drawing for the ScrolledComposite.
		scrollComposite.setRedraw(true);

		return;
	}

	/**
	 * This operation will draw a data component in the properties section.
	 */
	@Override
	public void visit(DataComponent component) {

		// Get the client area of the section
		Composite sectionClient = (Composite) section.getClient();

		DataComponentComposite dataComposite;

		// If a DataComponentComposite already exists, reuse it.
		if (index < sectionControls.size()) {
			dataComposite = (DataComponentComposite) sectionControls.get(index);
			dataComposite.setDataComponent(component);
		}
		// Otherwise, create a new DataComponentComposite.
		else {
			// Instantiate the dialog, set the DataComponent and set the
			// listeners
			dataComposite = new DataComponentComposite(component, sectionClient,
					SWT.FLAT);

			// Set the LayoutData for the DataComponentComposite. It should
			// occupy all available horizontal space.
			dataComposite.setLayoutData(
					new GridData(SWT.FILL, SWT.BEGINNING, true, true));

			// Keep track of the added DataComponentComposites.
			sectionControls.add(dataComposite);
		}

		return;
	}

	@Override
	public void visit(ResourceComponent component) {
		// Do nothing.
	}

	@Override
	public void visit(TableComponent component) {
		// Do nothing.
	}

	@Override
	public void visit(MatrixComponent component) {
		// Do nothing.
	}

	@Override
	public void visit(GeometryComponent component) {
		// Do nothing.
	}

	@Override
	public void visit(MasterDetailsComponent component) {
		// Do nothing.
	}

	@Override
	public void visit(TreeComposite component) {
		// Do nothing.
	}

	@Override
	public void visit(IReactorComponent component) {
		// Do nothing.
	}

	@Override
	public void visit(TimeDataComponent component) {
		// Do nothing.
	}

	@Override
	public void visit(MeshComponent component) {
		// Do nothing.
	}

	@Override
	public void visit(AdaptiveTreeComposite component) {
		// Do nothing.
	}

	@Override
	public void visit(EMFComponent component) {
		// Do nothing.
	}

	@Override
	public void visit(ListComponent component) {
		// Do nothing.

	}
}
