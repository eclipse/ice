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
package org.eclipse.eavp.viz.visit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.lbnl.visit.swt.VisItSwtWidget;
import visit.java.client.AttributeSubject;
import visit.java.client.AttributeSubject.AttributeSubjectCallback;

/**
 * This class provides a dialog for executing Python scripts on the active VisIt
 * plot.
 * 
 * @author Taylor Patterson
 * 
 */
public class VisitPythonDialog extends TitleAreaDialog {
	
	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(VisitPythonDialog.class);

	/**
	 * The active VisItSwtWidget
	 */
	private VisItSwtWidget widget;

	/**
	 * The button to load and execute a Python script from a file.
	 */
	private Button loadButton;

	/**
	 * The button to execute the Python command in the text box.
	 */
	private Button execButton;

	/**
	 * The AttributeSubjectCallback used by the VisIt widget.
	 */
	private AttributeSubjectCallback attSubCallback;

	/**
	 * The StyledText area that will act as the console to accept keyboard input
	 * and display output
	 */
	private StyledText console;

	/**
	 * The constructor
	 * 
	 * @param parentShell
	 *            The parent Shell for this dialog
	 * @param inWidget
	 */
	public VisitPythonDialog(Shell parentShell, VisItSwtWidget inWidget) {
		super(parentShell);

		// Remove the help button
		super.setHelpAvailable(false);

		// Get the active VisIt widget
		widget = inWidget;
	}

	/**
	 * Set the title and message to display near the top of this dialog window.
	 */
	@Override
	public void create() {
		super.create();

		// Set the title and help message for this TitleAreaDialog
		setTitle("VisIt Python Interface");
		setMessage("Use this console to input VisIt Python commands",
				IMessageProvider.INFORMATION);

		return;
	}

	/**
	 * Create the buttons in the button bar area. Override the parent function
	 * to add the 'Execute' and 'Load from File' buttons and remove the 'Ok'
	 * button.
	 */
	@Override
	public void createButtonsForButtonBar(Composite parent) {

		// Create the button to load a script from a file
		loadButton = createButton(parent, IDialogConstants.CLIENT_ID + 1,
				"Load from File", false);

		// Create the button to close the dialog
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);

		// Create the button to execute the last line in the text area
		execButton = createButton(parent, IDialogConstants.CLIENT_ID + 2,
				"Execute", true);

		// Add selection listeners to these buttons
		addButtonListeners();

		return;
	}

	/**
	 * Add listeners to the 'Execute' and 'Load from File' buttons. Add some
	 * additional functionality to the 'Cancel' button.
	 */
	private void addButtonListeners() {

		// Close the AttributeSubjectCallback when the 'Cancel' button in
		// pressed.
		getButton(IDialogConstants.CANCEL_ID).addSelectionListener(
				new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						logger.info("unregistering...");
						widget.getViewerState().unregisterCallback(
								"ClientMethod", attSubCallback);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// Not used
					}
				});

		// Implement the functionality for the 'Load from File' button. Present
		// a FileDialog to select a python script.
		loadButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				// Create an SWT FileDialog with the option to select multiple
				// files.
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN
						| SWT.MULTI);

				// Create a filter to limit the displayed files to those with a
				// .py extension.
				String[] filterNames = new String[] { ".py Files" };
				String[] filterExtensions = new String[] { "*.py" };

				// Set the dialog's file filters.
				dialog.setFilterNames(filterNames);
				dialog.setFilterExtensions(filterExtensions);

				// Get the OS file separator character.
				String separator = System.getProperty("file.separator");

				// Set the default location.
				String filterPath = System.getProperty("user.home") + separator
						+ "ICEFiles" + separator + "default" + separator;
				dialog.setFilterPath(filterPath);

				// If a file was selected in the dialog, process it.
				if (dialog.open() != null) {

					// Get the selected file(s)
					String[] files = dialog.getFileNames();

					if (files != null) {
						// Loop through the selected files
						for (String file : files) {
							try {
								// Get the full file path
								String path = dialog.getFilterPath()
										+ separator + file;
								// Initialize the readers and buffers
								FileReader fReader = new FileReader(path);
								BufferedReader bReader = new BufferedReader(
										fReader);
								StringBuffer strBuffer = new StringBuffer();
								// Loop through the file (via the
								// BufferedReader) to create a single String.
								String line = bReader.readLine();
								while (line != null) {
									strBuffer.append(line + "\n");
									line = bReader.readLine();
								}

								// Close the reader
								bReader.close();
								// Convert the StringBuffer to a String
								String fullFileStr = strBuffer.toString()
										.trim() + "\n";
								// Format the String of commands
								String text = ">>> " + fullFileStr;
								text = text.replace("\n", "\n>>> ");
								text = text.replace("\n>>>  ", "\n... ");
								console.append(text.trim() + "\n");
								// Call the VisIt widget method to process these
								// commands
								widget.getViewerMethods().processCommands(
										fullFileStr);
							} catch (Exception e1) {
								// Complain if needed
								logger.error(getClass().getName() + " Exception!",e);
							}
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Not used
			}
		});

		// Implement the functionality of the 'Execute' button.
		execButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Call the method to execute the last command from the console
				executeCommand();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Not used
			}
		});

		return;
	}

	/**
	 * Create the dialog area above the button bar area. This will include the
	 * text area to input VisIt Python commands and display previously entered
	 * commands with their resulting output.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		// Get the Composite for this dialog
		Composite composite = (Composite) super.createDialogArea(parent);

		// Create the text area
		console = new StyledText(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		console.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Reserve space for the '>>> ' at the start of input lines in the
		// console
		console.addLineStyleListener(new LineStyleListener() {

			@Override
			public void lineGetStyle(LineStyleEvent event) {
				// Get the line where '>>>' should be placed
				event.bulletIndex = console.getLineAtOffset(event.lineOffset);

				// Create the StyleRange to reserve space for the preceeding
				// '>>> '
				StyleRange styleRange = new StyleRange();
				styleRange.metrics = new GlyphMetrics(0, 0,
						">>> ".length() * 12);

				// Create and set the reserved space
				event.bullet = new Bullet(ST.BULLET_CUSTOM, styleRange);
			}
		});

		// Actually generate the preceeding '>>> '
		console.addPaintObjectListener(new PaintObjectListener() {

			@Override
			public void paintObject(PaintObjectEvent event) {
				// Define the TextLayout parameters
				TextLayout textLayout = new TextLayout(event.display);
				textLayout.setAscent(event.ascent);
				textLayout.setDescent(event.descent);
				textLayout.setFont(event.style.font);
				// Create the String (">>> ") at the beginning of each input
				// line
				textLayout.setText(">>> ");
				// Draw the text
				textLayout.draw(event.gc, event.x, event.y);
				textLayout.dispose();
			}
		});

		// Keep track of the index of the top line displayed in the console
		console.addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				console.setTopIndex(console.getLineCount() - 1);
			}
		});

		console.addListener(SWT.Traverse, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_RETURN) {
					executeCommand();
				}
			}
		});

		// Create the AttributeSubjectCallback
		attSubCallback = new AttributeSubjectCallback() {

			@Override
			public boolean update(AttributeSubject subject) {
				String name = subject.getAsString("methodName");

				if (!"AcceptRecordedMacro".equals(name)) {
					return false;
				}

				final List<String> vec = subject
						.getAsStringVector("stringArgs");

				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < vec.size(); ++i) {
							console.append(vec.get(i) + "\n");
						}

						// Move the cursor to the end of the console
						console.setSelection(console.getText().length());
					}
				});

				return true;
			}
		};

		// Register the AttributeSubjectCallback with the VisIt widget
		widget.getViewerState()
				.registerCallback("ClientMethod", attSubCallback);

		return composite;
	}

	/**
	 * Execute the most recent user input in the the {@link console}. Parse the
	 * text in the console to grab the most recent command and send it off to
	 * the VisIt client.
	 */
	private void executeCommand() {

		// Get the last command from the console Text
		String command = console.getText().trim();
		int start = Math.max(0, command.lastIndexOf("\n"));
		command = command.substring(start);
		// Call the VisIt widget method to process the command(s)
		if (!command.isEmpty()) {
			widget.getViewerMethods().processCommands(command);
		}

		return;
	}

}
