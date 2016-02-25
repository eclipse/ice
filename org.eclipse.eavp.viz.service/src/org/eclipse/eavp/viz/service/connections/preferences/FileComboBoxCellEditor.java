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
package org.eclipse.eavp.viz.service.connections.preferences;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.eavp.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizAllowedValueType;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Layout;

/**
 * The FileComboBoxCellEditor is a ComboBoxCellEditor that also adds a file
 * browse button to the combo widget. It lists available file items but also
 * let's a user browse to another and import it into the list.
 * 
 * @author Alex McCaskey
 *
 */
public class FileComboBoxCellEditor extends ComboBoxCellEditor {

	/**
	 * Listens for 'focusLost' events and fires the 'apply' event as long as the
	 * focus wasn't lost because the dialog was opened.
	 */
	private FocusListener buttonFocusListener;

	/**
	 * The editor control.
	 */
	private Composite editor;

	/**
	 * The current contents.
	 */
	private CCombo contents;

	/**
	 * The button.
	 */
	private Button button;

	/**
	 * Reference to the editing support using this cell editor
	 */
	private EntryCellEditingSupport support;

	/**
	 * Reference to the property this editor corresponds to.
	 */
	private VizEntry element;

	/**
	 * The Constructor
	 */
	public FileComboBoxCellEditor() {
		super();
	}

	/**
	 * The Constructor, takes the list of items to display
	 */
	public FileComboBoxCellEditor(Composite parent, String[] items) {
		super(parent, items, SWT.NONE);
	}

	/**
	 * The Constructor, takes the items, editing support, and property
	 */
	public FileComboBoxCellEditor(Composite parent, String[] items, int style, EntryCellEditingSupport supp,
			VizEntry element) {
		super(parent, items, style);
		support = supp;
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ComboBoxCellEditor#createControl(org.eclipse.
	 * swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		Font font = parent.getFont();
		Color bg = parent.getBackground();

		// Create the editor composite
		editor = new Composite(parent, getStyle());
		editor.setFont(font);
		editor.setBackground(bg);
		editor.setLayout(new DialogCellLayout());

		// Create the contents
		contents = (CCombo) super.createControl(editor);

		// Create the Browse Button
		button = createButton(editor);
		button.setFont(font);

		// Add a escape key listener
		button.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == '\u001b') { // Escape
					fireCancelEditor();
				}
			}
		});

		// Add the focus listener
		button.addFocusListener(getButtonFocusListener());

		// Add the button listener to throw up the file browser
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				// Remove the button's focus listener since it's guaranteed
				// to lose focus when the dialog opens
				button.removeFocusListener(getButtonFocusListener());

				// Get the IClient so we can do the import
				// IClient client = ClientHolder.getClient();

				// Open the file browser and get the selection
				String newValue = (String) openDialogBox(editor);

				// Re-add the listener once the dialog closes
				button.addFocusListener(getButtonFocusListener());

				if (newValue != null) {

					String entryValue = "";
					if (newValue != null) {
						// Import the files
						entryValue = newValue;

						String[] currentItems = FileComboBoxCellEditor.this.getItems();
						String[] newItems = new String[currentItems.length + 1];
						for (int i = 0; i < currentItems.length; i++) {
							newItems[i] = currentItems[i];
						}
						newItems[currentItems.length] = entryValue;// .getName();

						FileComboBoxCellEditor.this.setItems(newItems);
						doSetValue(new Integer(currentItems.length));

						IVizEntryContentProvider prov = new BasicVizEntryContentProvider();
						ArrayList<String> valueList = new ArrayList<String>(Arrays.asList(newItems));
						prov.setAllowedValueType(VizAllowedValueType.Executable);

						// Finish setting the allowed values and default
						// value
						prov.setAllowedValues(valueList);

						// Set the new provider
						element.setContentProvider(prov);

						support.setValue(element, currentItems.length);
						markDirty();
						setValueValid(true);
						fireApplyEditorValue();
						deactivate();
					}
				}
			}
		});

		setValueValid(true);

		return editor;
	}

	/**
	 * Open the dialog box for the button.
	 * 
	 * @param cellEditorWindow
	 * @return
	 */
	protected Object openDialogBox(Control cellEditorWindow) {

		FileDialog fileDialog = new FileDialog(cellEditorWindow.getShell());
		fileDialog.setText("Select a file to import into ICE workspace");
		return fileDialog.open();

	}

	/**
	 * Creates the button for this cell editor under the given parent control.
	 * <p>
	 * The default implementation of this framework method creates the button
	 * display on the right hand side of the dialog cell editor. Subclasses may
	 * extend or reimplement.
	 * </p>
	 *
	 * @param parent
	 *            the parent control
	 * @return the new button control
	 */
	protected Button createButton(Composite parent) {
		Button result = new Button(parent, SWT.DOWN);
		result.setText("..."); //$NON-NLS-1$
		return result;
	}

	/**
	 * Return a listener for button focus.
	 * 
	 * @return FocusListener
	 */
	private FocusListener getButtonFocusListener() {
		if (buttonFocusListener == null) {
			buttonFocusListener = new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					// Do nothing
				}

				@Override
				public void focusLost(FocusEvent e) {
					FileComboBoxCellEditor.this.focusLost();
				}
			};
		}

		return buttonFocusListener;
	}

	/**
	 * Internal class for laying out the dialog.
	 */
	private class DialogCellLayout extends Layout {
		@Override
		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			if (contents != null) {
				contents.setBounds(0, 0, bounds.width - size.x, bounds.height);
			}
			button.setBounds(bounds.width - size.x, 0, size.x, bounds.height);
		}

		@Override
		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point contentsSize = contents.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			Point buttonSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			// Just return the button width to ensure the button is not clipped
			// if the label is long.
			// The label will just use whatever extra width there is
			Point result = new Point(buttonSize.x, Math.max(contentsSize.y, buttonSize.y));
			return result;
		}
	}
}
