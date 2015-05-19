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
package org.eclipse.ice.vibe.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.io.ips.IPSReader;
import org.eclipse.ice.io.ips.IPSWriter;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.item.Item;

/**
 * <p>
 * This class is the model representation of the VIBE model. It inherits from
 * the Item Class. It will load INI conf files into a form that can be written
 * to create new input for VIBE Simulations. If no conf file is given to the
 * loadInput method the VibeModel will automatically load the case 6 example.
 * </p>
 * 
 * @author Scott Forest Hull II, Andrew Bennett
 */
@XmlRootElement(name = "VibeModel")
public class VibeModel extends Item {

	/**
	 * <p>
	 * A custom tag for ini files operation. Set in the constructor.
	 * </p>
	 */
	private String customTaggedExportString = "Export to VIBE INI format";

	// The name of the example chosen
	protected String exampleName; // Default for now
	
	private ArrayList<String> actionItems;

	private IOService ioService;
	
	/**
	 * A nullary constructor that delegates to the project constructor.
	 */
	public VibeModel() {
		this(null);
	}

	/**
	 * <p>
	 * The constructor for the VibeModel. Calls the constructor for Item by
	 * passing the IProject. It should call setupForm() in the super
	 * constructor.
	 * </p>
	 * 
	 * @param project
	 *            The passed IProject for the workspace.
	 */
	public VibeModel(IProject project) {
		// Setup the form and everything
		super(project);
	}

	/**
	 * <p>
	 * This operation overrides the Item.setupForm() operation.
	 * </p>
	 */
	public void setupForm() {
		// Create a fresh form to start with
		form = new Form();

		// If loading from the new item button we should just
		// load up the default case 6 file by passing in null
		if (project != null) {
			loadInput(null);
		}
	}
	
	/**
	 * <p>
	 * This operation overrides the Item.setupItemInfo() operation.
	 * </p>
	 */
	protected void setupItemInfo() {
		// Setup Item information
		setName("VIBE Model");
		setDescription("Specify the components and set up a battery model for VIBE.");

		// Add an action to the list to allow for the INI exports
		customTaggedExportString = "Export to VIBE INI format";
		allowedActions.add(0, customTaggedExportString);
		actionItems = getAvailableActions();
		
		// Set up the necessary io services if they aren't already done.
		ioService = getIOService();
		if (ioService == null) {
			setIOService(new IOService());
			ioService = getIOService();
		}
		if (ioService.getReader("IPSReader") == null) {
			ioService.addReader(new IPSReader());
		}
		if (ioService.getWriter("IPSWriter") == null) {
			ioService.addWriter(new IPSWriter());
		}
	}

	/**
	 * <p>
	 * Overrides the reviewEntries operation. This will still call
	 * super.reviewEntries, but will handle the dependencies after all other dep
	 * handing is finished.
	 * </p>
	 * 
	 * @return the status of the form
	 */
	protected FormStatus reviewEntries(Form preparedForm) {
		FormStatus retStatus = FormStatus.ReadyToProcess;
		Component dataComp = null;

		// Grab the data component from the Form and only proceed if it exists
		ArrayList<Component> components = preparedForm.getComponents();

		// Make sure the form has the right amount of data
		if (components.size() != 4) {
			System.out.println("VibeModel Message: Could not find enough data to write a complete input format." +
					" 4 Components are required, but " + components.size() + " were found.");
			retStatus = FormStatus.InfoError;
		}
		return retStatus;
	}

	/**
	 * <p>
	 * Overrides item's process by adding a customTaggedExportString (ini).
	 * Still utilizes Item's process functionality for all other calls.
	 * </p>
	 */
	public FormStatus process(String actionName) {
		FormStatus retStatus;

		// If it is the custom operation, call this here.
		if (this.customTaggedExportString.equals(actionName)) {

			// Get the file from the project space to create the output
			String filename = getName().replaceAll("\\s+", "_") + "_" + getId()
					+ ".conf";
			// Get the file path and build the URI that will be used to write
			IFile outputFile = project.getFile(filename);

			// Get the data from the form
			ArrayList<Component> components = form.getComponents();

			// A valid VibeModel needs 4 components
			if (components.size() > 3) {

				// create a new IPSWriter with the output file
				IPSWriter writer = (IPSWriter) ioService.getWriter("IPSWriter"); //new IPSWriter();
				try {
					// Write the output file
					writer.write(form, outputFile);
					// Refresh the project space
					project.refreshLocal(IResource.DEPTH_ONE, null);
				} catch (CoreException e) {
					// Complain
					System.err.println("VibeModel Message: "
							+ "Failed to refresh the project space.");
					e.printStackTrace();
				}
				// return a success
				retStatus = FormStatus.Processed;
			} else {
				// return an error
				System.err.println("Not enough components to write new file!");
				retStatus = FormStatus.InfoError;
			}
		} else {
			// Otherwise let item deal with the process
			retStatus = super.process(actionName);
		}
		return retStatus;
	}

	/**
	 * <p>
	 * This operation loads the given example into the Form.
	 * </p>
	 * 
	 * @param name
	 *            The path name of the example file name to load.
	 */
	public void loadInput(String name) {
		// If nothing is specified, load case 6 from inside the plugin
		IFile inputFile = null;
		File temp = null;
		if (name == null) {
			try {
				// Create a filepath for the default file
				String defaultFilePath = project.getLocation().toOSString()
							+ System.getProperty("file.separator")
							+ "case_6.conf";			
				// Create a temporary location to load the default file
				temp = new File(defaultFilePath);
				if (!temp.exists()) {
					temp.createNewFile();
				}
				
				// Pull the default file from inside the plugin
				URI uri = new URI(
						"platform:/plugin/org.eclipse.ice.vibe/data/case_6.conf");
				InputStream reader = uri.toURL().openStream();
				FileOutputStream outStream = new FileOutputStream(temp);

				// Write out the default file from the plugin to the temp location
				int fileByte;
				while ((fileByte = reader.read()) != -1) {
					outStream.write(fileByte);
				}
				outStream.close();
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
				inputFile = project.getFile("case_6.conf");

			} catch (URISyntaxException e) {
				e.printStackTrace();
				System.err.println("VibeModel Message: Error!  Could not load the default"
								+ " VIBE case data!");
			} catch (MalformedURLException e) {
				e.printStackTrace();
				System.err.println("VibeModel Message: Error!  Could not load the default"
						+ " VIBE case data!");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("VibeModel Message: Error!  Could not load the default"
						+ " VIBE case data!");
			} catch (CoreException e) {
				e.printStackTrace();
				System.err.println("VibeModel Message: Error!  Could not load the default"
						+ " VIBE case data!");
			}
		} else {
			// Get the file
			inputFile = project.getFile(name);
		}
		
		// Load the components from the file and setup the form
		System.out.println("VibeModel Message: Loading " + inputFile.getName());		
		IPSReader reader = (IPSReader) ioService.getReader("IPSReader"); //new IPSReader();
		form = reader.read(inputFile);
		form.setName(getName());
		form.setDescription(getDescription());
		form.setId(getId());
		form.setItemID(getId());
		form.setActionList(actionItems);
		
		// Delete default file if it was copied into the workspace
		if (temp != null) {
			temp.delete();
		}
	}
}
