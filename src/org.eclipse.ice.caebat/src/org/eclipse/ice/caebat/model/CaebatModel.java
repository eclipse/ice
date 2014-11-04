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
package org.eclipse.ice.caebat.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.item.Item;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is the model representation of the CAEBAT model. It inherits from
 * the Item Class. It will obtain it's specified entries for each DataComponent
 * from XML files stored in the plugin's directory under a folder titled
 * caebatModel. The files are hard coded in the loadDataComponent's operation,
 * and should be adjusted as DataComponent files are added or deleted. Once all
 * the data is correctly specified, an xml file will be generated to be used by
 * the launcher or a dat file (to be deprecated) for Caebat's runtime. All of
 * the required DataComponents need to exist inside the project's workspace
 * under Caebat's folder with the correct extension, otherwise it will refuse to
 * process the item.
 * 
 * This class will also grab complete forms that store entire "use cases" under
 * a folder called "caebatCases".
 * 
 * All files must be specified within their operations in order to be picked up,
 * otherwise they will be completely ignored.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 */
@XmlRootElement(name = "CaebatModel")
public class CaebatModel extends Item {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A custom tag for ini files operation. Set in the constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	private String customTaggedExportString;

	/**
	 * The current case selected by the user.
	 */
	private String currentCaseName;

	// The name of the CAEBAT info component
	private static String caebatInfoDCName = "Caebat Information";

	// The name of the time loop variable
	private static String timeLoopVariableName = "TIME_LOOP";

	// The name of the ports table
	private static String portsTableName = "PORTS";

	// The name of the master-details component with detailed port information
	private static String portsMasterDetails = "Component Details";

	// The name of the case selection component
	private static String caseSelectionName = "Case Selection";

	/**
	 * The data component that stores the descriptive and zone information about
	 * the selected case.
	 */
	private DataComponent caebatInfoComp;

	/**
	 * A map of cases.
	 */
	private HashMap<String, Form> caseMap;

	/**
	 * A nullary constructor that delegates to the project constructor.
	 */
	public CaebatModel() {
		this(null);
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor for the CaebatModel. Calls the constructor for Item by
	 * passing the IProject. It should call setupForm() in the super
	 * constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param project
	 *            <p>
	 *            The passed IProject for the workspace.
	 *            </p>
	 */
	public CaebatModel(IProject project) {

		// begin-user-code

		// Setup the form and everything
		super(project);

		return;
		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the reviewEntries operation. This will still call
	 * super.reviewEntries, but will handle the dependencies after all other dep
	 * handing is finished.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return form status
	 */
	protected FormStatus reviewEntries(Form preparedForm) {

		// begin-user-code

		// Local Declarations
		TableComponent portsTable = null;
		MasterDetailsComponent portsMaster = null;
		TimeDataComponent timeComponent = null;
		DataComponent casesComponent = null;
		DataComponent caebatComponent = null;

		// Call super submitForm
		FormStatus retStatus = super.reviewEntries(preparedForm);

		// This portion of the dependency handling deals with the relationship
		// between the ports table and the master details table.
		for (int i = 0; i < preparedForm.getComponents().size(); i++) {
			// Get the component
			Component comp = preparedForm.getComponents().get(i);
			String name = comp.getName();
			// get the portsTable
			if (name.equals(portsTableName)) {
				portsTable = (TableComponent) comp;
			}
			// get the portsMaster
			else if (name.equals(portsMasterDetails)) {
				portsMaster = (MasterDetailsComponent) comp;
			}
			// Get TimeDataComponent
			else if (name.equals(timeLoopVariableName)) {
				timeComponent = (TimeDataComponent) comp;
			}
			// Get case selection component
			else if (name.equals(caseSelectionName)) {
				casesComponent = (DataComponent) comp;
			}
			// Get caebat information component
			else if (name.equals(caebatInfoDCName)) {
				caebatComponent = (DataComponent) comp;
			}
		}

		// If both components not found, return InfoError
		// Check to make sure the portsTable is correctly set to a K/V table and
		// the masterDetails contains at least one template
		if (portsTable == null || portsMaster == null || timeComponent == null
				|| casesComponent == null
				|| portsTable.getColumnNames().size() != 2
				|| portsMaster.getAllowedMasterValues() == null) {
			System.err
					.println("CAEBATModel Message: "
							+ "Found invalid component "
							+ "configuration upon review.");
			retStatus = FormStatus.InfoError;
			return retStatus;
		}

		// If a user specifies a case, then load that specific case
		if (!casesComponent.retrieveEntry("Caebat Case").getValue()
				.equals(currentCaseName)) {
			Entry caseSelected = casesComponent.retrieveEntry("Caebat Case");
			String selectedCaseName = caseSelected.getValue();
			// Load the case
			loadSpecificCase(selectedCaseName, preparedForm);
			// Fix the table to show correct K/Vs for rows.
		}

		return retStatus;

	}

	/**
	 * Fixes the TimeDataComponent's values and handles dependencies between
	 * true and false.
	 * 
	 * @param timeComponent
	 */
	private void fixTimeLoop(TimeDataComponent timeComponent) {

		// begin-user-code

		// Local declarations
		Entry valuesEntry = timeComponent.retrieveEntry("VALUES");

		// Hide information correctly within time loop
		if ("True".equals(timeComponent.retrieveEntry("Enable Regular Mode")
				.getValue())) {
			valuesEntry.setReady(false);
		} else {

			valuesEntry.setReady(true);

			// Force order
			String values = valuesEntry.getValue().trim();

			// Split by space
			String[] parsedValues = values.split("\\s+");

			TreeMap<Double, String> mappedValues = new TreeMap<Double, String>();

			// Verify the numerical values exist
			for (int i = 0; i < parsedValues.length; i++) {
				try {

					// Convert to double and then put into list
					double value = Double.parseDouble(parsedValues[i]);

					mappedValues.put(value, parsedValues[i]);
				} catch (Exception e) {
					// Do nothing
				}
			}

			// Reset the value - empty first
			String stringOfDoubles = "";

			// Add numerical values in order
			for (int i = 0; i < mappedValues.size(); i++) {
				stringOfDoubles += mappedValues.values().toArray()[i] + " ";
			}
			stringOfDoubles = stringOfDoubles.trim();
			valuesEntry.setValue(stringOfDoubles);

		}

		// end-user-code
	}

	/**
	 * This operation fixes the portsTable in order for it to show the desired
	 * K/V pairs on the table. This assumes the passed TableComponent is a 2
	 * column table. The first column represents keys. The second column
	 * represents values. The values are set based upon the KEY.
	 * 
	 * This file assumes it knows all of the keys and must be modified if a new
	 * type of component is added.
	 * 
	 * s4h: This contains a branch, but it is the only way to handle the
	 * comparisons between K/V pairs. //FIXME
	 * 
	 * @param portsTable
	 *            The ports table to adjust.
	 */
	private void fixPortsTableComponent(TableComponent portsTable) {

		// begin-user-code
		System.out.println("Caebat Model:  Performing portsTable conversion.");
		// FIXME these are hardcoded values to represent the key/value pairs
		ArrayList<String> keys = new ArrayList<String>();
		HashMap<String, ArrayList<String>> keyValuesPairs;

		keyValuesPairs = new HashMap<String, ArrayList<String>>();

		// Add to key value pairs
		for (int i = 0; i < portsTable.numberOfRows(); i++) {

			// Get the keys
			keys.add(portsTable.getRow(i).get(0).getValue());
		}

		// FIXME BRANCH STATEMENT!!!! SFH
		// Add values to map based on key
		for (int i = 0; i < keys.size(); i++) {

			// Create the values list
			ArrayList<String> values = new ArrayList<String>();
			if ("DRIVER".equals(keys.get(i))) {

				// Set the values
				values.add("CHARTRAN_ELECTRICAL_THERMAL_DRIVER");

				// Add the key and value
				keyValuesPairs.put(keys.get(i), values);

			} else if ("THERMAL".equals(keys.get(i))) {

				// Set the values
				values.add("AMPERES_THERMAL");

				// Add the key and value
				keyValuesPairs.put(keys.get(i), values);

			} else if ("ELECTRICAL".equals(keys.get(i))) {

				// Set the values
				values.add("AMPERES_ELECTRICAL");

				// Add the key and value
				keyValuesPairs.put(keys.get(i), values);

			} else if ("CHARTRAN".equals(keys.get(i))) {

				// Set the values
				values.add("DUALFOIL");
				values.add("NTG");

				// Add the key and value
				keyValuesPairs.put(keys.get(i), values);

			} else {
				// Do nothing
			}
		}

		final HashMap<String, ArrayList<String>> finalKeyValuesPairs = new HashMap<String, ArrayList<String>>();
		finalKeyValuesPairs.putAll(keyValuesPairs);

		// Iterate over the rows
		for (int i = 0; i < portsTable.numberOfRows(); i++) {
			// Get the row
			ArrayList<Entry> row = portsTable.getRow(i);

			// Get the key's value
			final String key = row.get(0).getValue();

			// Get the second entry
			Entry valueEntry = row.get(1);

			System.err.println("Info on entry for key: " + key + " values: "
					+ finalKeyValuesPairs.get(key));

			// Determine what value to put in it based upon the key
			// Make a new Entry
			Entry entry = new Entry() {
				protected void setup() {
					setName("Component Value");
					tag = "Column2";
					setDescription("The type of the components.");
					allowedValues = new ArrayList<String>();
					if (finalKeyValuesPairs.get(key) != null
							&& !(finalKeyValuesPairs.get(key).isEmpty())) {
						defaultValue = finalKeyValuesPairs.get(key).get(0);
						allowedValues.addAll(finalKeyValuesPairs.get(key));
					}
					value = defaultValue;
					allowedValueType = AllowedValueType.Discrete;
				}
			};

			// Set the name, id, value, and description correctly
			entry.setId(valueEntry.getId());
			entry.setName(valueEntry.getName());
			entry.setDescription(valueEntry.getDescription());
			entry.setValue(valueEntry.getValue());

			// Copy contents
			valueEntry.copy(entry);

		}

		// end-user-code
	}

	/**
	 * This operation fixes the master details component based on the inputted
	 * values in the ports table.
	 * 
	 * @param portsTable
	 * @param portsMaster
	 */
	private void fixMasterDetailsComponent(TableComponent portsTable,
			MasterDetailsComponent portsMaster) {

		// begin-user-code

		// Local Declarations
		ArrayList<String> portsTableValues = new ArrayList<String>();

		if (portsTable.getRowIds().isEmpty()) {

			// Add a row to the portsTable
			portsTable.addRow();

		}

		// Get the names of the ports - these correspond to the values of the
		// second column
		for (int i = 0; i < portsTable.getRowIds().size(); i++) {
			portsTableValues.add(portsTable.getRow(i).get(1).getValue());
		}

		// If the portsTable is empty, then add a row and reset the template

		// create the template list.

		// Get the names of the ports
		boolean needsFixing = false;

		// First verify if the number of values are equal to the number of
		// templates AND the number of ports are correct.
		// This is important in order to verify that if they are correct or
		// correct. If they are not, trash and restart.

		// They are the same templated values. Check to make sure the
		// portTableValues are the same size and correct templates
		if (portsTableValues.size() == portsMaster.numberOfMasters()) {

			// Verify that the names are correct
			for (int i = 0; i < portsMaster.numberOfMasters(); i++) {

				// If the name does not exist, then the table must be fixed
				if (!portsTableValues.contains(portsMaster.getMasterAtIndex(i))) {
					needsFixing = true;
					break;

				}
			}

		} else {
			// Then the sizes are not correct, needs to be adjusted
			needsFixing = true;
		}

		// If it needs fixing, then fix the table
		if (needsFixing) {
			System.out.println("CaebatModel: "
					+ "Adjusting MasterDetailsComponent because of change "
					+ "in table.");

			// Delete all the current master details in the table.
			while (portsMaster.numberOfMasters() > 0) {
				portsMaster.deleteMasterAtIndex(0);
			}

			// Add masters back in relation to the ports specified on the table
			for (int i = 0; i < portsTableValues.size(); i++) {
				// Add masters to the corresponding port value
				portsMaster.setMasterInstanceValue(portsMaster.addMaster(),
						portsTableValues.get(i));
			}

		}

		// end-user-code

	}

	/**
	 * This first section deals with cases. If the case selection is enabled, it
	 * will reset the entire form to consist of the current selected case.
	 * 
	 * @param caseName
	 * @param preparedForm
	 */
	private void loadSpecificCase(String caseName, Form preparedForm) {
		// begin-user-code

		// Local Declarations
		Form selectedCase;

		if (debuggingEnabled) {
			System.out.println("CAEBAT Model: Selected case value = "
					+ caseName + ". Updating model.");
		}

		// Get the current case
		selectedCase = caseMap.get(caseName);

		// Load the form completely if there is nothing in it
		if (preparedForm.getNumberOfComponents() != 4) {
			preparedForm.copy(selectedCase);
			preparedForm.removeComponent(2);
		} else {
			// If it already has components in it, copy the components from the
			// new Form into the old one. This is just written out in the raw
			// right now because it is the simplest way. I need to figure out a
			// better way to do it. Note that the indices are off by one because
			// the info component is still in the selected case from the map!
			ArrayList<Component> updatedComponents = selectedCase
					.getComponents();
			ArrayList<Component> currentComponents = preparedForm
					.getComponents();
			// Copy the case component
			((DataComponent) currentComponents.get(0))
					.copy((DataComponent) updatedComponents.get(0));
			// Copy the ports component
			((TableComponent) currentComponents.get(1))
					.copy((TableComponent) updatedComponents.get(2));
			// Copy the port details component
			((MasterDetailsComponent) currentComponents.get(2))
					.copy((MasterDetailsComponent) updatedComponents.get(3));
			// Copy the time component
			((TimeDataComponent) currentComponents.get(3))
					.copy((TimeDataComponent) updatedComponents.get(4));
		}

		// Update its item id
		preparedForm.setItemID(getId());

		// Store the information component and then remove it
		caebatInfoComp = (DataComponent) selectedCase.getComponents().get(1);

		// Update the case information
		currentCaseName = caseName;

		return;
		// end-user-code
	}

	/**
	 * Auto configures number of zones based upon the inputs given by other
	 * entries.
	 * 
	 * @param component
	 *            The component that contains the zones.
	 */
	private void fixNumOfZones(DataComponent component) {

		// begin-user-code

		// Check passed parameter
		if (component == null) {
			return;
		}
		// Local Declarations

		// Name of entries. If names are changed, these are the only that should
		// be modified!
		String ccpZones = "Number of CCP Zones";
		String ccnZones = "Number of CCN Zones";
		String pouchZones = "Number of Pouch Zones";
		String chartranZones = "Number of Chartran Zones";
		String numZones = "Number of Zones";

		// Retrieve the entries based upon the name
		Entry ccpZonesEntry = component.retrieveEntry(ccpZones);
		Entry ccnZonesEntry = component.retrieveEntry(ccnZones);
		Entry pouchZonesEntry = component.retrieveEntry(pouchZones);
		Entry chartranZonesEntry = component.retrieveEntry(chartranZones);
		Entry numZonesEntry = component.retrieveEntry(numZones);

		// Verify that all pieces are not null. If one piece is missing, return.
		if (ccpZonesEntry == null || ccnZonesEntry == null
				|| pouchZonesEntry == null || chartranZonesEntry == null
				|| numZonesEntry == null) {
			System.err.println("Caebat Model:  "
					+ "Not all of the zones were found.  "
					+ "Number of Zones calculation not performed.");
			return;
		}

		// Perform the calculation
		double value = Double.parseDouble(ccpZonesEntry.getValue())
				+ Double.parseDouble(ccnZonesEntry.getValue())
				+ Double.parseDouble(pouchZonesEntry.getValue())
				+ Double.parseDouble(chartranZonesEntry.getValue());
		String valueString = Double.toString(value);

		// Set the value
		numZonesEntry.setValue(valueString);

		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation overrides the Item.setupForm() operation.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	public void setupForm() {
		// begin-user-code

		// Local declarations
		boolean localStatus;

		// This method will create a new Form and add all the dataComponents to
		// the form. These dataComponents will be accessed later in
		// loadDataComponents.
		form = new Form();

		// Setup Item information
		setName("Caebat Model");
		setDescription("This model creates input for CAEBAT.");

		// Load from the DataComponents.
		localStatus = loadCases();

		if (!localStatus) {
			System.err.println("There was an error loading the item. "
					+ "There may be missing pieces. "
					+ "Please investigate the CaebatFiles for errors.");
			status = FormStatus.InfoError;
			return;
		}

		// Add an action to the list to allow for the INI exports
		customTaggedExportString = "Export to Caebat INI format";
		allowedActions.add(customTaggedExportString);

		// ----- Finish setting up the Form so that it can be immediately
		// launched

		// Load the case
		loadSpecificCase("Prismatic Cell", form);

		return;
		// end-user-code

	}

	/**
	 * This operation loads the caebat cases. Returns true if the operation was
	 * successful, false otherwise.
	 * 
	 * This operation assumes it knows all of the cases provided under the
	 * bundle and that they are named correctly. These must be complete forms
	 * and must contain all of the data provided within the default "template"
	 * of the caebatModel. If there are new components to add, then these must
	 * be updated accordingly.
	 * 
	 * @return
	 */

	private boolean loadCases() {

		// local declarations
		String folderDirectory = "caebatCases";
		ArrayList<String> fileNames = new ArrayList<String>();

		// Originally, one would use the proper file separator for the OS.
		// Nevertheless, OSGI specification for the bundle utility converts the
		// slashes to the correct format. So, in other words...
		String separator = "/";

		// If there is no form, or if there are components return false
		if (form == null) {
			return false;
		}
		if (debuggingEnabled) {
			System.out.println("CaebatModel Message: Loading Caebat Cases");
		}
		// This is a list of the files the loader will iterate over.
		// This is separated out cleanly for editing later.

		// These files represent the general selection list for the GUI
//		fileNames.add("Case3CAEBATModel.xml");
		fileNames.add("Case6CAEBATModel.xml");
//		fileNames.add("Case7aCAEBATModel.xml");
//		fileNames.add("Case7bCAEBATModel.xml");

		// Need to have at least one filename
		if (fileNames.isEmpty()) {
			return false;
		}

		// Use the first filename to get the path
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		if (bundle == null) {
			System.err.println("CaebatModel Message (Cases): "
					+ "OSGI bundle FrameUtil operation failed.  Returning;");
			return false;
		}

		URL file = bundle.getEntry(separator + folderDirectory + separator
				+ fileNames.get(0));

		if (file != null) {
			System.out.println("CaebatModel Message (Cases): "
					+ "The first file exists in the directory. "
					+ "Starting iteraton");
		} else {
			System.err.println("CaebatModel Message (Cases): "
					+ "Error, first file does not exist in directory for "
					+ separator + folderDirectory + separator
					+ fileNames.get(0));
			return false;
		}

		// Create the case map.
		caseMap = new HashMap<String, Form>();

		// Iterate over the list of files, make sure they are instances of
		// CaebatModel Components. Then load the data from the workspace.
		for (int i = 0; i < fileNames.size(); i++) {
			Form openedForm;

			// Get the file from the project's full path
			file = null;
			bundle = null;
			bundle = FrameworkUtil.getBundle(this.getClass());
			file = bundle.getEntry(separator + folderDirectory + separator
					+ fileNames.get(i));

			// Create the component, and load it to XML.
			openedForm = new Form();

			try {
				System.out.println("CaebatModel Message (Cases): "
						+ "Trying to load file: " + fileNames.get(i));
				// Load to xml
				((ICEObject) openedForm).loadFromXML(file.openStream());
			} catch (FileNotFoundException e) {
				System.err.println("CaebatModel Message (Cases): "
						+ "File inputstream conversion error.  Returning.");
				return false;
			} catch (IOException e) {
				System.err.println("CaebatModel Message (Cases): "
						+ "Error opening file.  Returning");
				return false;
			}

			// Get the case name
			DataComponent casesComponent = (DataComponent) openedForm
					.getComponent(1);
			Entry caseSelected = casesComponent.retrieveEntry("Caebat Case");
			String selectedCaseName = caseSelected.getValue();

			// Load the case into the map
			caseMap.put(selectedCaseName, openedForm);
		}

		return true;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation takes a Caebat form and converts it to a INI format. It
	 * will ignore certain components that are erroneous or problematic.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return the ini formatted string of all the objects on this form, or null
	 *         if error.
	 */
	public String writeCAEBATINIFile() {

		// begin-user-code

		// Local Declarations
		String iniStream = ""; // Set it to empty string so string addition
								// works correctly
		Date currentDate = new Date();
		SimpleDateFormat shortDate = new SimpleDateFormat("yyyyMMddhhmmss");
		Form writtenForm = new Form();
		String baseID = "caebatRun";

		// Check to see the form is not null and the components list is greater
		// than 0
		if (form == null || form.getComponents().isEmpty()) {
			return null;
		}

		// Update the CAEBAT Information component so that the run id can be
		// properly formatted
		if (caebatInfoComp != null) {
			// Get the RUN_ID entry
			Entry runID = caebatInfoComp.retrieveEntry("Run ID");
			baseID = runID.getValue();
			runID.setValue(baseID + "_" + shortDate.format(currentDate));

			// Create a copy of the Form that will be written to disk and add
			// the CAEBAT information to it.
			writtenForm.copy(form);
			writtenForm.addComponent(caebatInfoComp);

			// Use the custom visitor within the package to allow the components
			// to write themselves. This is much more robust and scalable in the
			// long run.
			for (int i = 1; i < 6; i++) {
				// Grab the component
				Component comp = writtenForm.getComponent(i);
				// Skip the case Selection name
				if (!caseSelectionName.equals(comp.getName())) {
					// Create the visitor
					VisitorINI visitor = new VisitorINI();
					// Have each component accept the visitor
					comp.accept(visitor);
					// If the visitor was visited and the ini string was set,
					// add it
					if (visitor.wasVisited() && visitor.getINIString() != null) {
						// Add new line!
						iniStream += "\n" + visitor.getINIString();
					} else {
						// Otherwise it was not visited or the string was not
						// parsed
						// correctly. Return null
						return null;
					}
				}
			}

			// Get the RUN_ID entry
			runID = caebatInfoComp.retrieveEntry("Run ID");
			runID.setValue(baseID);
		}

		// Return the iniFormat. Should be complete
		return iniStream;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides item's process by adding a customTaggedExportString (ini).
	 * Still utilizes Item's process functionality for all other calls.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	public FormStatus process(String actionName) {
		// begin-user-code

		// If it is the custom operation, call this here.
		if (this.customTaggedExportString.equals(actionName)) {

			// Get the custom string
			String stream = writeCAEBATINIFile();

			// If the stream is not null, try to write the file
			if (stream != null) {
				// Convert string to outputStream
				try {
					// Create a byteArray stream
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

					// Write to output stream
					outputStream
							.write(stream.getBytes(Charset.forName("UTF-8")));
					IFile outputFile = null;

					// Get the file name.
					String filename = (form.getName() + "_" + form.getId())
							.replaceAll("\\s+", "_");
					outputFile = project.getFile(filename + ".conf");

					// If the output file already exists, delete it
					if (outputFile.exists()) {
						outputFile.delete(false, null);
					}
					// Create the contents of the IFile from the output stream
					outputFile
							.create(new ByteArrayInputStream(outputStream
									.toByteArray()), false, null);
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
					// Notify any observers of the change
					notifyListenersOfProjectChange();
					// Print some debug information
					if (debuggingEnabled) {
						System.out.println("CaebatModel Message: "
								+ "Output file created at "
								+ outputFile.getLocation().toOSString());
					}
				} catch (Exception e) {
					// Any exception caught, return info error
					e.printStackTrace();
					return FormStatus.InfoError;
				}

				// Everything went according to plan! Returning processed
				return FormStatus.Processed;
			}

			// The stream was null, return info error
			return FormStatus.InfoError;
		}

		// Otherwise let item deal with the process
		else {
			return super.process(actionName);
		}
		// end-user-code

	}

}
