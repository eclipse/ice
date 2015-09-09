package org.eclipse.ice.client.common.properties;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

public class FileComboBoxCellEditor extends ComboBoxCellEditor {

	/**
	 * Image registry key for three dot image (value
	 * <code>"cell_editor_dots_button_image"</code>).
	 */
	public static final String CELL_EDITOR_IMG_DOTS_BUTTON = "cell_editor_dots_button_image";//$NON-NLS-1$

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
	 * The label that gets reused by <code>updateLabel</code>.
	 */
	private Label defaultLabel;

	/**
	 * The value of this cell editor; initially <code>null</code>.
	 */
	private Object value = null;

	/**
	 * The button.
	 */
	private Button button;

	private ComboCellEditingSupport support;

	private TreeProperty element;

	/**
	 */
	public FileComboBoxCellEditor() {
		super();
	}

	/**
	 */
	public FileComboBoxCellEditor(Composite parent, String[] items) {
		super(parent, items, SWT.NONE);
	}

	/**
	 */
	public FileComboBoxCellEditor(Composite parent, String[] items, int style, ComboCellEditingSupport supp,
			TreeProperty element) {
		super(parent, items, style);
		support = supp;
		this.element = element;
	}

	@Override
	protected Control createControl(Composite parent) {
		Font font = parent.getFont();
		Color bg = parent.getBackground();

		editor = new Composite(parent, getStyle());
		editor.setFont(font);
		editor.setBackground(bg);
		editor.setLayout(new DialogCellLayout());

		contents = (CCombo) createContents(editor);
		// updateContents(value);

		button = createButton(editor);
		button.setFont(font);

		button.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == '\u001b') { // Escape
					fireCancelEditor();
				}
			}
		});

		button.addFocusListener(getButtonFocusListener());

		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				// Remove the button's focus listener since it's guaranteed
				// to lose focus when the dialog opens
				button.removeFocusListener(getButtonFocusListener());

				IClient client = ClientHolder.getClient();
				String newValue = (String) openDialogBox(editor);

				System.out.println("THE NEW FILE: " + newValue);

				// Re-add the listener once the dialog closes
				button.addFocusListener(getButtonFocusListener());

				if (newValue != null) {

					// Import the files
					File importedFile = new File(newValue);
					client.importFile(importedFile.toURI());

					String[] currentItems = FileComboBoxCellEditor.this.getItems();
					String[] newItems = new String[currentItems.length + 1];
					for (int i = 0; i < currentItems.length; i++) {
						newItems[i] = currentItems[i];
					}
					newItems[currentItems.length] = importedFile.getName();

					FileComboBoxCellEditor.this.setItems(newItems);
					doSetValue(new Integer(currentItems.length));

					IEntryContentProvider prov = new BasicEntryContentProvider();
					ArrayList<String> valueList = new ArrayList<String>(Arrays.asList(newItems));
					prov.setAllowedValueType(AllowedValueType.File);

					// Finish setting the allowed values and default
					// value
					prov.setAllowedValues(valueList);

					// Set the new provider
					element.getEntry().setContentProvider(prov);

					support.setValue(element, currentItems.length);
					markDirty();
					setValueValid(true);
					fireApplyEditorValue();
					deactivate();
				}
			}
		});

		setValueValid(true);

		return editor;
	}

	protected Object openDialogBox(Control cellEditorWindow) {

		FileDialog fileDialog = new FileDialog(cellEditorWindow.getShell());
		fileDialog.setText("Select a file to import into ICE workspace");
		return fileDialog.open();

		// InputDialog dialog = new InputDialog(cellEditorWindow.getShell(), "",
		// "", "", null);
		// if (dialog.open() == Window.OK) {
		// return dialog.getValue();
		// } else {
		// return null;
		// }
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
	 * Creates the controls used to show the value of this cell editor.
	 * <p>
	 * The default implementation of this framework method creates a label
	 * widget, using the same font and background color as the parent control.
	 * </p>
	 * <p>
	 * Subclasses may reimplement. If you reimplement this method, you should
	 * also reimplement <code>updateContents</code>.
	 * </p>
	 *
	 * @param cell
	 *            the control for this cell editor
	 * @return the underlying control
	 */
	protected Control createContents(Composite cell) {
		return super.createControl(cell);
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
