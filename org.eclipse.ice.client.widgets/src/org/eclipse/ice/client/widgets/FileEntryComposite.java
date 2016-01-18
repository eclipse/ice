package org.eclipse.ice.client.widgets;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.iclient.IClient;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Layout;

public class FileEntryComposite extends DiscreteEntryComposite {

	public FileEntryComposite(Composite parent, IEntry refEntry, int style) {
		super(parent, refEntry, style);
	}

	@Override
	public void render() {

		// Get the number of allowed values
		int numAllowedValues = entry.getAllowedValues().size();

		// If this is a File entry, draw dropdown (if applicable)
		// and browse button
		createLabel();
		if (numAllowedValues > 0) {
			createDropdown();
		}
		createBrowseButton();
		setLayout(setupDropDownLayout(numAllowedValues));
	}

	/**
	 * This method creates a browse button on the EntryComposite. Clicking the
	 * button opens a file browser, and once a file is selected, the file is
	 * imported into the default workspace.
	 */
	private void createBrowseButton() {

		// Create a new button, set the text
		Button browseButton = new Button(this, SWT.PUSH);
		browseButton.setText("Browse...");

		System.out.println("CREATED A BROWSE BUTTON");
		// Add an event listener that displays a Directory Dialog prompt
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Get the Client
				IClient client = null;
				try {
					client = IClient.getClient();
				} catch (CoreException e1) {
					logger.error("Could not get reference to IClient instance.", e1);
				}

				if (client != null) {
					// Open up a file browser
					FileDialog fileDialog = new FileDialog(getShell());
					fileDialog.setText("Select a file to import into ICE");
					String filePath = fileDialog.open();
					if (filePath != null) {
						// Import the files
						File importedFile = new File(filePath);
						client.importFile(importedFile.toURI());
						// Create a new content provider with the new file
						// in the allowed values list
						List<String> valueList = entry.getAllowedValues();
						if (!valueList.contains(importedFile.getName())) {
							valueList.add(importedFile.getName());
						}

						// Finish setting the allowed values and default
						// value
						entry.setAllowedValues(valueList);

						// Set the entry's value to the new file
						entry.setValue(importedFile.getName());
					}

					// Notify any listeners of the selection event
					notifyListeners(SWT.Selection, new Event());

				}
				return;
			}

		});

		// Add the browse button
		buttons.add(browseButton);

		return;
	}

	/**
	 *
	 * @param layout
	 * @param numAllowedValues
	 */
	protected Layout setupDropDownLayout(int numAllowedValues) {
		// Use a RowLayout so we can wrap widgets.
		final RowLayout rowLayout = new RowLayout();
		rowLayout.wrap = true;
		rowLayout.fill = false;
		rowLayout.center = true;
		// Layout layout = rowLayout;

		// If the file list Combo is rendered, we need to give it RowData so
		// it will grab excess horizontal space. Otherwise, the default
		// RowLayout above will suffice.
		if (numAllowedValues > 0) {
			// Use a RowData for the dropdown Combo so it can get excess
			// space.
			final RowData rowData = new RowData();
			widget.setLayoutData(rowData);
			// Set a minimum width of 50 for the dropdown.
			final int minWidth = 50;

			// Compute the space taken up by the label and browse button.
			final int unwrappedWidth;
			Button button = buttons.get(0);
			int labelWidth = label.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
			int buttonWidth = button.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
			int padding = 2 * rowLayout.spacing + rowLayout.marginLeft + rowLayout.marginWidth * 2
					+ rowLayout.marginRight + 30;
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
		}

		return rowLayout;
	}

}
