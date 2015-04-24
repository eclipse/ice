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
package org.eclipse.ice.client.widgets.moose.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ice.client.widgets.EclipseStreamingTextWidget;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.IEntryContentProvider;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.item.nuclear.MOOSELauncher;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This class executes the steps needed to generate MOOSE YAML and 
 * Action Syntax files by using the MOOSELauncher. 
 * 
 * @author Alex McCaskey
 *
 */
public class GenerateYAMLHandler extends AbstractHandler implements Runnable {

	/**
	 * Reference to the MOOSELauncher that we will use to 
	 * generate the YAML and Action Syntax files. 
	 */
	private MOOSELauncher launcher;

	/**
	 * Reference to the wizard that will get the required info 
	 * we need to generate yaml and syntax files. 
	 */
	private GenerateYAMLWizard wizard;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Create a new Generate YAML Wizard and Dialog
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		wizard = new GenerateYAMLWizard();
		WizardDialog dialog = new WizardDialog(shell, wizard);

		// The MOOSELauncher has the functionality needed to generate 
		// yaml and syntax files, so lets create one. 
		// FIXME NOT SURE HOW TO GET THE CORRECT PROJECT
		launcher = new MOOSELauncher(ResourcesPlugin.getWorkspace().getRoot()
				.getProject("default"));

		// Open the dialog
		if (dialog.open() != 0) {
			return null;
		}

		// Since this action now takes care of the generate yaml stuff, 
		// we don't need to explicitly show the generate yaml executable on the 
		// Moose Launcher form. So I've removed it, but we need to set it here so 
		// we can use that action. So here I'm creating a new IEntryContentProvider 
		// that has that executable in the Entry. 
		IEntryContentProvider execContentProvider = new BasicEntryContentProvider();
		ArrayList<String> execList = ((DataComponent) launcher.getForm()
				.getComponent(5)).retrieveEntry("Executable")
				.getAllowedValues();
		execList.add("Generate YAML/action syntax");

		// Finish setting the allowed values and default value
		execContentProvider.setAllowedValues(execList);
		execContentProvider.setAllowedValueType(AllowedValueType.Discrete);
		execContentProvider.setDefaultValue("Generate YAML/action syntax");

		// Give the Launcher form's executable Entry a new IEntryContentProvider
		// so that we can execute the YAML generation stuff
		((DataComponent) launcher.getForm().getComponent(5)).retrieveEntry(
				"Executable").setContentProvider(execContentProvider);
		
		// Set the executable to Generate YAML
		((DataComponent) launcher.getForm().getComponent(5)).retrieveEntry(
				"Executable").setValue("Generate YAML/action syntax");
		
		// Set the Host and Exec path information
		((TableComponent) launcher.getForm().getComponent(4)).getRow(0).get(0)
				.setValue(wizard.getHostName());
		((TableComponent) launcher.getForm().getComponent(4)).getRow(0).get(2)
				.setValue(wizard.getExecPath());

		// Submit the Form
		launcher.submitForm(launcher.getForm());

		// Launch the execution thread. 
		Thread thread = new Thread(this);
		thread.start();

		return null;
	}

	@Override
	public void run() {

		// Local Declarations
		String nextLine = null;
		File outputFile = null;
		FileReader outputFileReader = null;
		BufferedReader outputFileBufferedReader = null;
		EclipseStreamingTextWidget streamingTextWidget = new EclipseStreamingTextWidget();

		// Grab the output file handle
		outputFile = launcher.getOutputFile();
		// Open the file if it is available
		if (outputFile != null && outputFile.exists()
				&& streamingTextWidget != null) {
			try {
				// Create the readers
				outputFileReader = new FileReader(outputFile);
				outputFileBufferedReader = new BufferedReader(outputFileReader);
				// Set the widget label
				streamingTextWidget
						.setLabel("Generate YAML/Action Syntax Live Output");
				// Open the widget
				streamingTextWidget.display();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		// Launch the generate yaml job
		FormStatus status = launcher.process("Launch the Job");

		// If it is remote, then we should have a NeedsInfo status
		// which is asking the user to provide a username and password
		// We've already got this, so just give it to it
		if (status.equals(FormStatus.NeedsInfo)) {
			Form actionForm = launcher.getForm();
			((DataComponent) actionForm.getComponent(1)).retrieveEntry(
					"Username").setValue(wizard.getUsername());
			((DataComponent) actionForm.getComponent(1)).retrieveEntry(
					"Password").setValue(wizard.getPassword());

			// Notify the user that we are connecting
			streamingTextWidget
					.postText("\nICE establishing remote connection for MOOSE YAML/Action syntax generation\n");

			// Submit the UserName/Password Form back to the action
			launcher.submitForm(actionForm);
		}

		// Event loop for processing
		while (!launcher.getStatus().equals(FormStatus.Processed)) {
			// Read the file if it was opened correctly
			if (outputFileBufferedReader != null && streamingTextWidget != null) {
				// Get everything currently there
				try {
					while ((nextLine = outputFileBufferedReader.readLine()) != null) {
						// Write it to the streaming text widget
						streamingTextWidget.postText(nextLine);
					}
				} catch (IOException e) {
					// Complain because the next line could not be read
					e.printStackTrace();
				}
			}

		}

		try {
			outputFileBufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		streamingTextWidget
				.postText("\nYAML/Action Syntax Generation complete.\n");
		return;
	}
}
