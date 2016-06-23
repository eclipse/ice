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
package org.eclipse.ice.projectgeneration.templates;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.pde.ui.templates.BaseOptionTemplateSection;
import org.eclipse.pde.ui.templates.StringOption;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The DataFileOption is an extension of the StringOption which has
 * some additional UI components to be used in a specific manner.
 * The main functionality of the DataFileOption comes from having
 * the browse button attached, which allows users to select files
 * from their local machine to be used as the default datasets to
 * be loaded when no other data is given
 * 
 * @author arbennett
 */
public class DataFileOption extends StringOption {

	private Text locationPathField;
	private Button browseButton;
	private Label locationLabel;
	private FileDialog fileChooser;
	private String chosenFile;
	private String description;
	
	/**
	 * Constructor
	 * 
	 * @param section
	 * 			The wizard section to add the option to
	 * @param name
	 * 			The variable replacement string
	 * @param label
	 * 			A description to prompt the user in some way
	 */
	public DataFileOption(BaseOptionTemplateSection section, String name, String label) {
		super(section, name, label);
		description = label;
	}	


	/**
	 * Create the user interface for the option
	 */
	public void createControl(Composite parent, int span) {
		// Create the descriptive layer
		locationLabel = new Label(parent, SWT.NONE);
		locationLabel.setText(description);

		// Create the text entry area where the path to the file will be displayed
		locationPathField = new Text(parent, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		data.horizontalSpan = 2;
		locationPathField.setLayoutData(data);

		// Create the browse button and add a listener to open a file chooser
		browseButton = new Button(parent, SWT.PUSH);
		browseButton.setText("Browse");
		browseButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				fileChooser = new FileDialog(parent.getShell());
				fileChooser.setText("Select File");
				fileChooser.setFilterExtensions(new String[] {"*.*"});
				chosenFile = fileChooser.open();
				locationPathField.setText(chosenFile);
				setValue(chosenFile);
			}

			@Override public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}

	
	/**
	 * Get the path from the location path field
	 * 
	 * @return the path
	 */
	public String getDataFilePath() {
		URI fieldURI;
		try {
			fieldURI = new URI(chosenFile);
		} catch (URISyntaxException e) {
			return chosenFile;
		}
		String path = fieldURI.getPath();
		return path != null ? path : chosenFile;
	}
	

	/**
	 * Check if the value of the option is null
	 */
	@Override
	public boolean isEmpty() {
		return getValue() == null;
	}
	
	/**
	 * Set whether the option is usable
	 */
	@Override
	public void setEnabled(boolean enabled) {
		locationLabel.setEnabled(enabled);
		locationPathField.setEnabled(enabled);
		browseButton.setEnabled(enabled);
	}
}
