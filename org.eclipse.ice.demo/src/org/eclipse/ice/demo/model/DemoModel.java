/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.demo.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.eavp.viz.modeling.ShapeController;
import org.eclipse.eavp.viz.modeling.Shape;
import org.eclipse.eavp.viz.modeling.base.BasicView;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.model.Model;

@XmlRootElement(name = "DemoModel")
public class DemoModel extends Model {

	// TODO:
	// These need to be filled in before using this item
	private String writerName = "DemoDefaultWriterName";
	private String readerName = "DemoDefaultReaderName";
	private String outputName = "DemoDefaultOutputName";
	// End required variables

	private String exportString;
	private IIOService ioService;
	private IReader reader;
	private IWriter writer;

	public DemoModel() {
		this(null);
	}

	public DemoModel(IProject project) {
		super(project);
		exportString = "Export to Demo input format";

		// TODO: (optional) Add User Code Here
	}

	/**
	 * Adds relevant information that specify the ui provided to the user when
	 * they create the Demo Model Item in ICE.
	 */
	@Override
	public void setupForm() {
		form = new Form();

		ioService = getIOService();

		// Create a geometry component
		GeometryComponent geomComp = new GeometryComponent();
		geomComp.setName("Geometry");
		geomComp.setDescription("A geometry");
		geomComp.setContext("demo-geometry");
		geomComp.setGeometry(
				new ShapeController(new Shape(), new BasicView()));

		// Create a data component
		DataComponent dataComp = new DataComponent();
		dataComp.setName("Data");
		dataComp.setDescription("Some Data");
		dataComp.setContext("demo");
		// Need to set the id since geomComp is number 1
		dataComp.setId(2);

		// Create an Entry for the data component
		IEntry entry = new StringEntry();
		entry.setName("Data Entry");
		entry.setDescription("An Entry with Important Data");
		entry.setContext("demo-entry");
		// Add the Entry to the data component
		dataComp.addEntry(entry);

		// Add both components to the Form, showing the data component first.
		form.addComponent(dataComp);
		form.addComponent(geomComp);

		// Set the context on the Form
		form.setContext("demo");

		return;
	}

	/**
	 * Sets the name, description, and custom action name for the item.
	 */
	@Override
	protected void setupItemInfo() {
		setName("Demo Model");
		setDescription("Specify information about Demo");
		exportString = "Export to Demo input format";
		allowedActions.add(0, exportString);
	}

	/**
	 * The reviewEntries method is used to ensure that the form is in an
	 * acceptable state before processing the information it contains. If the
	 * form is not ready to process it is advisable to have this method return
	 * FormStatus.InfoError.
	 * 
	 * @param preparedForm
	 * 
	 * @return
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {
		FormStatus retStatus = FormStatus.ReadyToProcess;

		// TODO: Add User Code Here

		return retStatus;
	}

	/**
	 * Use this method to process the data that has been specified in the form.
	 * Data being processed should be written usint
	 * 
	 * @param actionName
	 * @return
	 */
	@Override
	public FormStatus process(String actionName) {
		FormStatus retStatus = FormStatus.ReadyToProcess;

		// Check to make sure that the item code has been filled in properly
		// Before going further make sure that the top three variables are
		// customized to the appropriate values for your new item.
		if (writerName == "DemoDefaultWriterName"
				|| outputName == "DemoDefaultOutputName") {
			return FormStatus.InfoError;
		}

		// This action occurs only when the default processing option is chosen
		// The default processing option is defined in the last line of the
		// setupItemInfo() method defined above.
		if (actionName == exportString) {
			IFile outputFile = project.getFile(outputName);
			writer = ioService.getWriter(writerName);
			try {
				retStatus = FormStatus.Processing;
				writer.write(form, outputFile);
				project.refreshLocal(1, null);
				retStatus = FormStatus.Processed;
			} catch (CoreException e) {
				logger.error(getClass().getName() + " CoreException!", e);
			}
		} else {
			retStatus = super.process(actionName);
		}

		// TODO: Add User Code Here

		return retStatus;
	}

	/**
	 * This method is called when loading a new item either via the item
	 * creation button or through importing a file associated with this item. It
	 * is responsible for setting up the form for user interaction.
	 * 
	 * @param fileName
	 */
	@Override
	public void loadInput(String fileName) {

		// Check to make sure that the item code has been filled in properly
		// Before going further make sure that the top three variables are
		// customized to the appropriate values for your new item.
		if (readerName == "DemoDefaultReaderName") {
			return;
		}

		// Read in the file and set up the form
		IFile inputFile = project.getFile(fileName);
		reader = ioService.getReader(readerName);
		form = reader.read(inputFile);
		form.setName(getName());
		form.setDescription(getDescription());
		form.setId(getId());
		form.setItemID(getId());
	}
}
