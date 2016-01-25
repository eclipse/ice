/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Alex McCaskey
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Alex McCaskey
 *
 */
public class MultiValueEntryComposite extends AbstractEntryComposite {

	private ListViewer viewer;

	private ArrayList<String> values;

	private ArrayList<String> selectedValues;

	/**
	 * The Constructor.
	 * 
	 * @param parent
	 * @param refEntry
	 * @param style
	 */
	public MultiValueEntryComposite(Composite parent, IEntry refEntry, int style) {
		super(parent, refEntry, style);
		values = new ArrayList<String>();
		selectedValues = new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.AbstractEntryComposite#render()
	 */
	@Override
	public void render() {

		createLabel();

		viewer = new ListViewer(this, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);

		// Add the label provider for the list viewer
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return (String) element;
			}
		});
		// Add the content provider for the list viewer
		viewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// Nothing to do
			}

			@Override
			public void dispose() {
				// Nothing to do
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return ((List<?>) inputElement).toArray();
			}
		});
		// Create the selection listener
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// Get and store the selection
				Iterator iter = ((IStructuredSelection) event.getSelection()).iterator();
				selectedValues.clear();

				while (iter.hasNext()) {
					String value = (String) iter.next();
					selectedValues.add(value);
				}

			}
		});

		Composite buttonComp = new Composite(this, SWT.NONE);
		// Set the default layout to a vertical FillLayout.
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 3;
		fillLayout.spacing = 5;
		buttonComp.setLayout(fillLayout);

		// Create a new button, set the text
		Button addButton = new Button(buttonComp, SWT.PUSH);
		addButton.setText("Add");
		buttons.add(addButton);

		Button remove = new Button(buttonComp, SWT.PUSH);
		remove.setText("Remove");
		buttons.add(remove);
		// Add an event listener that displays a Directory Dialog prompt
		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (String s : selectedValues) {
					values.remove(s);
				}
				viewer.setInput(values);
				entry.setValue(values.toArray(new String[values.size()]));
				viewer.refresh();

			}
		});

		// Add an event listener that displays a Directory Dialog prompt
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				InputDialog dialog = new InputDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						"Add new MultiValue Entry value", "Please provide a new value.", "", null);
				if (dialog.open() == Window.OK) {
					System.out.println(dialog.getValue());
					values.add(dialog.getValue());
					viewer.setInput(values);
					entry.setValue(values.toArray(new String[values.size()]));
					viewer.refresh();
				}

			}
		});

		// Set the input to the list from the client
		viewer.setInput(null);

		setLayout(setupLayout());

	}

	/**
	 * 
	 * @return
	 */
	protected Layout setupLayout() {
		// Use a RowLayout so we can wrap widgets.
		final RowLayout rowLayout = new RowLayout();
		rowLayout.wrap = false;
		rowLayout.fill = false;
		rowLayout.center = true;
		// Layout layout = rowLayout;

		// If the file list Combo is rendered, we need to give it RowData so
		// it will grab excess horizontal space. Otherwise, the default
		// RowLayout above will suffice.
		// Use a RowData for the dropdown Combo so it can get excess
		// space.
		final RowData rowData = new RowData();
		viewer.getControl().setLayoutData(rowData);
		// Set a minimum width of 50 for the dropdown.
		final int minWidth = 50;

		// Compute the space taken up by the label and browse button.
		final int unwrappedWidth;
		Button button = buttons.get(0);
		int labelWidth = label.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		int buttonWidth = button.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		int padding = 2 * rowLayout.spacing + rowLayout.marginLeft + rowLayout.marginWidth * 2 + rowLayout.marginRight
				+ 30;
		unwrappedWidth = labelWidth + buttonWidth + padding;

		// Size the dropdown based on the currently available space.
		int availableWidth = getClientArea().width - unwrappedWidth;
		rowData.width = (availableWidth > minWidth ? availableWidth : minWidth);

		// If necessary, remove the old resize listener.
		if (resizeListener != null) {
			removeControlListener(resizeListener);
		}

		// Add a resize listener to the EntryComposite to update the
		// size of the dropdown.
		resizeListener = new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				int availableWidth = getClientArea().width - unwrappedWidth;
				rowData.width = (availableWidth > minWidth ? availableWidth : minWidth);
				layout();
			}
		};
		addControlListener(resizeListener);

		return rowLayout;
	}
}
//