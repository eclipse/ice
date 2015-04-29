/*******************************************************************************
 * Copyright (c) 2012, 2014- UT-Battelle, LLC.
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ice.client.common.internal.ClientHolder;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IMessageManager;

/**
 * <p>
 * This is an subclass of SWT's Composite class made specifically to work with
 * ICE Entries.
 * </p>
 * <p>
 * Changes to this class are broadcasted using SWT's event system. Marking the
 * FormEditor as dirty, for example, should be handled by registering an event
 * listener with instances of this class and catching the signal.
 * </p>
 * <p>
 * The EntryComposite can post messages about its work with an Entry to a
 * IMessageManager if it is set by calling setMessageManager().
 * </p>
 * 
 * @author gqx, Anna Wojtowicz
 */
public class EntryComposite extends Composite implements IUpdateableListener {
	
	/**
	 * A label that describes the Entry.
	 */
	private Label label;
	
	/**
	 * A text field that is used if the Entry type is unspecified.
	 */
	private Text text;
	
	/**
	 * A drop-down menu for the Entry.
	 */
	private Combo dropDown;
	
	/**
	 * A set of buttons for the Entry.
	 */
	protected final List<Button> buttons;
	
	/**
	 * The Entry that is displayed by the EntryComposite.
	 */
	protected Entry entry;

	/**
	 * The message manager to which message about the success or failure of
	 * manipulating the Entry should be posted.
	 */
	private IMessageManager messageManager;
	
	/**
	 * The name of the message posted to the message manager.
	 */
	private String messageName;

	/**
	 * Entry map of binary/boolean-type allowed values
	 */
	private final List<String> allowedBinaryValues = new ArrayList<String>();

	/**
	 * List of allowed values for the entry in lowercase text
	 */
	private final List<String> lowercaseAllowedValues = new ArrayList<String>();

	/**
	 * This listens to the {@code EntryComposite}'s resize events and adjusts
	 * the size of the dropdown if necessary. This is currently only used for
	 * file entries.
	 */
	private ControlListener resizeListener = null;

	/**
	 * The Constructor
	 * 
	 * @param parent
	 *            The parent Composite.
	 * @param style
	 *            The style of the EntryComposite.
	 * @param refEntry
	 *            An Entry that should be used to create the widget, to update
	 *            when changed by the user and to be updated from when changed
	 *            internally by ICE.
	 */
	public EntryComposite(Composite parent, int style, Entry refEntry) {

		// Call the super constructor
		super(parent, style);

		// Set the Entry reference
		if (refEntry != null) {
			entry = refEntry;
		} else {
			throw new RuntimeException("Entry passed to EntryComposite "
					+ "constructor cannot be null!");
		}
		// Create the Buttons array
		buttons = new ArrayList<Button>();

		// Create the MessageName String
		messageName = new String();

		// Setup the allowedBinaryValues for check boxes
		// Setup the list of values that are equivalent to "ready"
		allowedBinaryValues.add("ready");
		allowedBinaryValues.add("yes");
		allowedBinaryValues.add("y");
		allowedBinaryValues.add("true");
		allowedBinaryValues.add("enabled");
		allowedBinaryValues.add("on");
		// Setup the list of values that are equivalent to "not ready"
		allowedBinaryValues.add("not ready");
		allowedBinaryValues.add("no");
		allowedBinaryValues.add("n");
		allowedBinaryValues.add("false");
		allowedBinaryValues.add("disabled");
		allowedBinaryValues.add("off");

		// Register for updates from the Entry
		entry.register(this);

		// Add a listener to the Entry that unregisters this composite as a
		// listener upon disposal.
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				entry.unregister(EntryComposite.this);
			}
		});

		// Render the entry
		render();

		return;
	}

	/**
	 * Returns the entry stored on this composite
	 * 
	 * @return
	 */
	public Entry getEntry() {
		return entry;
	}

	/**
	 * Creates a label for the EntryComposite.
	 */
	private void createLabel() {

		// Create the Label
		label = new Label(this, SWT.WRAP);
		label.setText(entry.getName() + ":");
		label.setToolTipText(entry.getDescription());
		label.setBackground(getBackground());

		return;
	}

	/**
	 * Creates a checkbox for the EntryComposite.
	 */
	private void createCheckbox() {

		// Local Declarations
		Button tmpButton = null;

		// Create the check box
		tmpButton = new Button(this, SWT.CHECK);
		tmpButton.setText(entry.getName());
		tmpButton.setToolTipText(entry.getDescription());

		// Set the box to be checked if the current entry value is one of the
		// "positive" answers from the allowed values
		if (allowedBinaryValues.subList(0, 5).contains(
				entry.getValue().toLowerCase())) {
			tmpButton.setSelection(true);
		} else {
			// Otherwise unchecked
			tmpButton.setSelection(false);
		}
		// Add the listeners
		tmpButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Notify any listeners that the selection has changed
				notifyListeners(SWT.Selection, new Event());
				// Get the index of the value
				int index = lowercaseAllowedValues.indexOf(entry.getValue()
						.toLowerCase());
				// Set the correct value
				String value = null;
				value = (index == 0) ? entry.getAllowedValues().get(1) : entry
						.getAllowedValues().get(0);
				setEntryValue(value);
				System.out.println("EntryComposite Message: Updated Entry "
						+ entry.getName() + " with value=" + entry.getValue());

				return;
			}
		});

		// Add the button to the list
		tmpButton.setBackground(getBackground());
		buttons.add(tmpButton);
	}

	/**
	 * This operation creates buttons on the Composite.
	 */
	private void createButtons() {

		// Local Declarations
		Button tmpButton = null;

		// Create the radio buttons
		for (String i : entry.getAllowedValues()) {
			// Create the button and set its text fields
			tmpButton = new Button(this, SWT.RADIO);
			tmpButton.setText(i);
			tmpButton.setToolTipText(entry.getDescription());
			// Select the default value if it is this one
			if (i.equals(entry.getValue())) {
				tmpButton.setSelection(true);
			}
			// Add the listeners
			tmpButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Notify any listeners that the selection has changed
					notifyListeners(SWT.Selection, new Event());
					// Set the value of the Entry
					setEntryValue(((Button) e.getSource()).getText());
				}
			});
			// Fix the color
			tmpButton.setBackground(getBackground());
			// Add the button to the list
			buttons.add(tmpButton);
		}

		return;
	}

	/**
	 * This operation creates a drop-down menu on the Composite.
	 */
	private void createDropdown() {

		boolean redraw = (dropDown == null || dropDown.isDisposed());
		
		if (redraw) {
			// Create a drop-down menu
			dropDown = new Combo(this, SWT.DROP_DOWN | SWT.SINGLE | SWT.V_SCROLL
					| SWT.H_SCROLL | SWT.READ_ONLY);
			dropDown.setBackground(getBackground());
	
			// Determine the current value of the entry.
			String currentValue = entry.getValue();
			
			// Add the allowed values to the dropdown menu. If the allowed value
			// matches the current value, select it.
			List<String> allowedValues = entry.getAllowedValues();
			for (int i = 0; i < allowedValues.size(); i++) {
				String allowedValue = allowedValues.get(i);
				dropDown.add(allowedValue);
				if (allowedValue.equals(currentValue)) {
					dropDown.select(i);
				}
			}
			
			// Add a selection listener
			dropDown.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Set the value of the Entry
					setEntryValue(((Combo) e.widget).getText());
					// Notify any listeners that the selection has changed
					notifyListeners(SWT.Selection, new Event());
				}
			});

		} else {
			// If the dropDown hasn't been disposed, check if a new AllowedValue
			// has been added to the Entry
			List<String> allowedValues = entry.getAllowedValues();
			List<String> comboValues = Arrays.asList(dropDown.getItems());
			for (int i = 0; i < allowedValues.size(); i++) {
				String allowedValue = allowedValues.get(i);
				// Add any new AllowedValues to the dropDown
				if (!comboValues.contains(allowedValue)) {
					dropDown.add(allowedValue);
				}
			}
		}

		return;
	}

	/**
	 * This operation creates a textfield on the Composite.
	 */
	private void createTextfield() {

		// Create a textfield
		if (!entry.isSecret()) {
			text = new Text(this, SWT.LEFT | SWT.BORDER);
		} else {
			text = new Text(this, SWT.LEFT | SWT.BORDER | SWT.PASSWORD);
		}
		text.setToolTipText(entry.getDescription());
		text.setText(entry.getValue());
		text.setBackground(getBackground());

		// Add the Focus Listeners
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				// Notify any listeners that the selection has changed
				notifyListeners(SWT.Selection, new Event());
				// Set the value of the Entry
				setEntryValue(text.getText());
			};
		});

		// Add a listener for the "DefaultSelection" key (return/enter). It
		// needs to be registered with the EntryComposite and the Text widget,
		// in case it is being used by JFace, which doesn't always post standard
		// SWT events.
		Listener enterListener = new Listener() {
			public void handleEvent(Event e) {
				// Notify any listeners that the selection has changed
				notifyListeners(SWT.Selection, new Event());
				// Set the value of the Entry
				setEntryValue(text.getText());
			}
		};
		this.addListener(SWT.DefaultSelection, enterListener);
		text.addListener(SWT.DefaultSelection, enterListener);

		return;
	}

	/**
	 * This method creates a browse button on the EntryComposite. Clicking the
	 * button opens a file browser, and once a file is selected, the file is
	 * imported into the default workspace.
	 */
	private void createBrowseButton() {

		boolean redraw = buttons.isEmpty();
		
		if (redraw) {
			// Create a new button, set the text
			Button browseButton = new Button(this, SWT.PUSH);
			browseButton.setText("Browse...");
	
			// Add an event listener that displays a Directory Dialog prompt
			browseButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Notify any listeners that the selection has changed
					notifyListeners(SWT.Selection, new Event());
					// Get the Client
					IClient client = ClientHolder.getClient();
	
					// Create the dialog and get the files
					FileDialog fileDialog = new FileDialog(getShell());
					fileDialog.setText("Select a file to import into ICE");
					String filePath = fileDialog.open();
					if (filePath != null) {
						// Import the files
						File importedFile = new File(filePath);
						client.importFile(importedFile.toURI());
						setEntryValue(importedFile.getName());
					}
	
					return;
				}
			});
	
			// Add the browse button
			buttons.add(browseButton);
		}

		return;
	}

	/**
	 * This operation renders the SWT widgets for the Entry.
	 */
	protected void render() {

		// Local Declarations
		int numAllowedValues = 0, maxValueLength = 12, maxShortValues = 8;
		boolean shortValues = true;
		AllowedValueType valueType = null;

		// Make all of the allowed values lowercase
		for (String value : entry.getAllowedValues()) {
			lowercaseAllowedValues.add(value.toLowerCase());
		}

		// Set the allowed value type
		valueType = entry.getValueType();
		// Get the number of allowed values
		numAllowedValues = entry.getAllowedValues().size();
		// Figure out if the allowed values are small in length. This
		// will let us draw it in a more intuitive way, but it only
		// takes one value that is not short to ruin the view.
		for (String i : entry.getAllowedValues()) {
			if (i.length() > maxValueLength) {
				shortValues = false;
				break;
			}
		}

		// Set the default layout to a vertical FillLayout.
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 3;
		fillLayout.spacing = 5;
		Layout layout = fillLayout;

		// If the valueType is Discrete and there are some allowed values,
		// figure out how to draw it
		if (valueType == AllowedValueType.Discrete && numAllowedValues > 0) {
			// We can use Radio buttons if the allowed values are few
			if (numAllowedValues <= maxShortValues && shortValues) {
				// Check to see if this is something that should use a check box
				if (numAllowedValues == 2
						&& allowedBinaryValues
								.containsAll(lowercaseAllowedValues)) {
					createCheckbox();
				} else {
					// Otherwise create the regular button set
					createLabel();
					createButtons();
				}
			} else {
				// Otherwise we can use a drop-down menu
				createLabel();
				createDropdown();
			}
		} else if (valueType == AllowedValueType.Discrete) {
			// If no values were found, throw an error
			throwMissingValuesError();
		} else if (valueType == AllowedValueType.File) {
			// If this is a File entry, draw dropdown (if applicable)
			// and browse button
			createLabel();
			if (numAllowedValues > 0) {
				createDropdown();
			}
			createBrowseButton();

			// FIXME We should use either this GridLayout or the RowLayout below
			// // Instead of the default FillLayout, use a 3-column GridLayout.
			// GridLayout gridLayout = new GridLayout(3, false);
			//
			// layout = gridLayout;
			// // Since we use a GridLayout, set the GridData on the new
			// widgets.
			// label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
			// false));
			//
			// GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true,
			// false);
			// gridData.minimumWidth = 50;
			// dropDown.setLayoutData(gridData);
			//
			// for (Button button : buttons) {
			// button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
			// false));
			// }

			// The dropdown Combo should get all excess space, but we must also
			// wrap the widgets when the EntryComposite is too small. Thus, we
			// must use a RowLayout and manually resize the dropdown.

			// Use a RowLayout so we can wrap widgets.
			final RowLayout rowLayout = new RowLayout();
			rowLayout.wrap = true;
			rowLayout.fill = false;
			rowLayout.center = true;
			layout = rowLayout;

			// If the file list Combo is rendered, we need to give it RowData so
			// it will grab excess horizontal space. Otherwise, the default
			// RowLayout above will suffice.
			if (numAllowedValues > 0) {
				// Use a RowData for the dropdown Combo so it can get excess
				// space.
				final RowData rowData = new RowData();
				dropDown.setLayoutData(rowData);
				// Set a minimum width of 50 for the dropdown.
				final int minWidth = 50;

				// Compute the space taken up by the label and browse button.
				final int unwrappedWidth;
				Button button = buttons.get(0);
				int labelWidth = label.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
				int buttonWidth = button.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
				int padding = 2 * rowLayout.spacing + rowLayout.marginLeft
						+ rowLayout.marginWidth * 2 + rowLayout.marginRight
						+ 30;
				unwrappedWidth = labelWidth + buttonWidth + padding;

				// Size the dropdown based on the currently available space.
				int availableWidth = getClientArea().width - unwrappedWidth;
				rowData.width = (availableWidth > minWidth ? availableWidth
						: minWidth);

				// If necessary, remove the old resize listener.
				if (resizeListener != null) {
					removeControlListener(resizeListener);
				}

				// Add a resize listener to the EntryComposite to update the
				// size of the dropdown.
				resizeListener = new ControlAdapter() {
					@Override
					public void controlResized(ControlEvent e) {
						int availableWidth = getClientArea().width
								- unwrappedWidth;
						rowData.width = (availableWidth > minWidth ? availableWidth
								: minWidth);
						layout();
					}
				};
				addControlListener(resizeListener);
			}
		} else {
			// Otherwise create a text field
			createLabel();
			createTextfield();
		}

		// Apply the layout.
		setLayout(layout);

		return;
	}

	/**
	 * This operation will post a message to the message manager (if one exists)
	 * if the Entry has no value, but requires a value from a discrete set.
	 */
	private void throwMissingValuesError() {

		if (messageManager != null) {
			// Get the message
			String errorMessage = entry.getErrorMessage();
			// Post it if it exists
			if (errorMessage != null) {
				// Display the error at the top of the screen
				if (messageManager != null) {
					messageManager.addMessage(messageName, errorMessage, null,
							IMessageProvider.ERROR);
				}
				// Highlight the text if it is in a text box
				if (text != null) {
					Color color = new Color(Display.getCurrent(), 200, 0, 0);
					text.setForeground(color);
					FontData fontData = new FontData();
					fontData.setStyle(SWT.BOLD);
					Font font = new Font(getDisplay(), fontData);
					text.setFont(font);
				}
			}
		}

		return;
	}

	/**
	 * This operation directs the EntryComposite to refresh its view of the
	 * Entry. This should be called in the event that the Entry has changed on
	 * the file system and the view needs to be updated.
	 */
	public void refresh() {

		// Dispose of the old widgets
		if (dropDown != null) {
			dropDown.dispose();
			dropDown = null;
		}
		if (label != null) {
			label.dispose();
			label = null;
		}
		if (text != null) {
			text.dispose();
			text = null;
		}
		for (Button button : buttons) {
			if (!button.isDisposed()) {
				button.dispose();
			}
		}
		// Remove all of the previous buttons.
		buttons.clear();

		// Print an error if this Entry has been prematurely disposed.
		if (isDisposed()) {
			System.out.println("EntryComposite Message: "
					+ "This composite has been prematurely disposed!");
			return;
		}
		
		// Remove the resize listener.
		if (resizeListener != null) {
			removeControlListener(resizeListener);
			resizeListener = null;
		}
		
		// Re-render the Composite
		render();

		// Re-draw the Composite
		layout();
		
		return;
	}

	/**
	 * This operation sets the Message Manager that should be used by the
	 * EntryComposite to post messages about the Entry. If the Message Manager
	 * is not set, the EntryComposite will not attempt to post messages.
	 * 
	 * @param manager
	 *            The Message Manager that the EntryComposite should use.
	 */
	public void setMessageManager(IMessageManager manager) {

		// Set the messageManager
		messageManager = manager;

		return;
	}

	/**
	 * This operation sets the value of the Entry and, if possible and
	 * necessary, reports to the message manager.
	 * 
	 * @param value
	 */
	protected void setEntryValue(String value) {

		// Set the value and post a message if necessary
		if (!entry.setValue(value) && messageManager != null) {
			// Get the message
			String errorMessage = entry.getErrorMessage();
			// Post it if it exists
			if (errorMessage != null) {
				// Display the error at the top of the screen
				if (messageManager != null) {
					messageManager.addMessage(messageName, errorMessage, null,
							IMessageProvider.ERROR);
				}
				// Highlight the text if it is in a text box
				if (text != null) {
					Color color = new Color(Display.getCurrent(), 200, 0, 0);
					text.setForeground(color);
					FontData fontData = new FontData();
					fontData.setStyle(SWT.BOLD);
					Font font = new Font(getDisplay(), fontData);
					text.setFont(font);
				}
			}

		} else if (value == null) {
			// Set the Entry to the first AllowedValue
			String allowedValue = entry.getAllowedValues().get(1);
			if (allowedValue != null) {
				entry.setValue(allowedValue);
			}
			
		} else {
			// Remove a posted message if necessary
			if (messageManager != null) {
				messageManager.removeMessage(messageName);
			}
			// Remove the text box highlight if it is in a text box
			if (text != null) {
				Color color = new Color(Display.getCurrent(), 0, 0, 0);
				text.setForeground(color);
				FontData fontData = new FontData();
				fontData.setStyle(SWT.NORMAL);
				fontData.setHeight(10);
				Font font = new Font(getDisplay(), fontData);
				text.setFont(font);
			}
		}

		return;
	}

	int hitCounter = 0;

	/**
	 * Listen for updates from the Entry and redraw if needed.
	 */
	public void update(IUpdateable component) {
		// When the Entry has updated, refresh on the Eclipse UI thread.
		if (component == entry) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (!EntryComposite.this.isDisposed()) {
						refresh();
					} else {
						entry.unregister(EntryComposite.this);
					}
				}
			});
		}
		++hitCounter;
		return;
	}
}
