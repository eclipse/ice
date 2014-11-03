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
package org.eclipse.ice.caebat.model.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.BeforeClass;
import org.junit.Test;
import org.eclipse.ice.caebat.model.CaebatModel;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.updateableComposite.Component;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the operations on CaebatModel. This class is also
 * responsible for building the model files designed to be used in the Caebat
 * Model for production mode. It creates two separate SETS OF FILES. The first
 * set of files are responsible for the production of the form on the model,
 * which contains safety defaults. The second set are a collection of cases
 * which will be used to pre-generate the input data on the forms.
 * </p>
 * <!-- end-UML-doc -->
 */
public class CaebatModelTester {

	/**
	 * The project space used to create the workspace for the tests.
	 */
	private static IProject projectSpace;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the workspace. It copies the necessary CAEBAT data
	 * files into ${workspace}/CAEBAT.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void beforeTests() {
		// begin-user-code

		// Local Declarations
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		URI defaultProjectLocation = null;
		IProject project = null;
		String projectName = "caebatTesterWorkspace";
		String separator = System.getProperty("file.separator");

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject(projectName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as
				// ${workspace_loc}/CAEBATModelTesterWorkspace
				defaultProjectLocation = (new File(
						System.getProperty("user.dir") + separator
								+ projectName)).toURI();
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription(projectName);
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
			// Refresh the workspace
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// Catch exception for creating the project
			e.printStackTrace();
			fail();
		}

		// Set the global project reference.
		projectSpace = project;

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the customINIFormatConverter() for the Caebat
	 * model.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkCustomINIFormatConverter() {

		// begin-user-code

		// Local Declarations
		CaebatModel caebat;
		Form form;

		// Instantiate a new caebat model
		caebat = new CaebatModel(projectSpace);

		// Get the form
		form = caebat.getForm();

		// Now run it to see if it does not generate null!
		String output = caebat.writeCAEBATINIFile();
		assertNotNull(output);
		assertTrue(!output.isEmpty());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks CaebatModel.setupForm operation. This should also
	 * test the loadDataComponents in comparison.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkSetupForm() {

		// begin-user-code

		// Local declarations
		CaebatModel caebatModel;
		Form form = new Form();

		/*
		 * This operation will test the CaebatModel plugin for proper loads with
		 * the setupForm operation. This should work as long as the files exists
		 * inside the plugin directory. Although not unit tested, it does check
		 * for specific file types, if the files exist, and if the Components
		 * exist.
		 */

		// The project space can be null for this operation and it will still
		// work.
		caebatModel = new CaebatModel(null);

		// Check contents - see that there are 5 components
		form = caebatModel.getForm();
		assertNotNull(form.getComponents());
		assertEquals(4, form.getComponents().size());

		return;
		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks CaebatModel.process operation.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkProcess() {

		// begin-user-code

		/*
		 * This test will demonstrate the behavior of the writeToXML" file
		 * process, INI, and dat file creation. It should run through other
		 * process operations as needed, and demonstrate when the wrong process
		 * operation is used.
		 * 
		 * The dat file creation should create a dat file.
		 */

		// Local Declarations
		CaebatModel Caebat;
		Form form = new Form();

		// The projectspace has to be set for this to work.
		Caebat = new CaebatModel(projectSpace);

		// Check default contents
		form = Caebat.getForm();
		assertNotNull(form.getComponents());
		assertEquals(4, form.getComponents().size());

		// Check the name and description of the item and allowedActions
		assertEquals("Caebat Model", Caebat.getName());
		assertEquals("This model creates input for CAEBAT.",
				Caebat.getDescription());

		// Since there are entries and DataComponents, lets run the test!
		// Now try to pass it the wrong information into the process
		// operation on CaebatModel
		assertEquals(FormStatus.InfoError,
				Caebat.process("I AM A VAGUE, INAPPROPRIATE PROCESS!"));

		// This portion will try to get the operation to write the information
		// to xml file
		assertEquals(FormStatus.Processed,
				Caebat.process("Export to ICE Native Format"));

		// Check to see if the file is created an exists. It will create a file
		// in relation to the Id of the item.
		assertTrue(projectSpace.getFile(
				"Caebat_Model_" + Caebat.getId() + ".xml").exists());

		// Create the dat file
		Caebat.process("Export to key-value pair output");

		// check to see if it exists, this is unit tested under item
		assertTrue(projectSpace.getFile(
				"Caebat_Model_" + Caebat.getId() + ".dat").exists());

		/*
		 * This part will verify the custom ini process
		 */
		assertEquals(FormStatus.Processed,
				Caebat.process("Export to Caebat INI format"));
		assertTrue(projectSpace.getFile(
				"Caebat_Model_" + Caebat.getId() + ".conf").exists());

		// end-user-code

	}

	/**
	 * <p>
	 * Generates data for case3. This is based upon the data provided in the
	 * Caebat's Case 3 input model. This operation will first grab the default
	 * form from the this.createPlugins() operation and set the data
	 * accordingly.
	 * </p>
	 * 
	 * @return The generated data for the specified case.
	 */
	private Form generateCase3() {
		// begin-user-code

		// Local Declarations
		Form case3;
		DataComponent caseSelected, caebatComponent;
		MasterDetailsComponent masterDetails;
		DataComponent detailsComponent;
		int master1, master2, master3, master4;
		TableComponent portsTable;

		case3 = this.createPlugins();
		case3.setName("Case3");

		// Modify case3Components to handle the correct values for the specific
		// case

		// CaseSelected Information
		caseSelected = (DataComponent) case3.getComponent(0);
		caseSelected.retrieveEntry("Previously Selected Case")
				.setValue("Case3");

		// Caebat Information
		caebatComponent = (DataComponent) case3.getComponent(1);
		assertTrue(this.setEntryByTag(caebatComponent, "RUN_ID", "case3"));
		assertTrue(this.setEntryByTag(caebatComponent, "TAG",
				"ElectroChemElectricalThermal"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_ZONES", "168"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CHARTRAN_ZONES",
				"56"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CCN_ZONES", "56"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CCP_ZONES", "56"));

		// Ports
		portsTable = (TableComponent) case3.getComponent(2);

		// Clear out table
		while (portsTable.getRowIds().size() > 0) {
			portsTable.deleteRow(0);
		}

		// Add rows - 4 of them
		portsTable.addRow();
		portsTable.addRow();
		portsTable.addRow();
		portsTable.addRow();

		// Setup Row information
		// Driver
		portsTable.getRow(0).get(0).setValue("DRIVER");
		portsTable.getRow(0).get(1)
				.setValue("CHARTRAN_ELECTRICAL_THERMAL_DRIVER");
		// THERMAL
		portsTable.getRow(1).get(0).setValue("THERMAL");
		portsTable.getRow(1).get(1).setValue("AMPERES_THERMAL");
		// ELECTRICAL
		portsTable.getRow(2).get(0).setValue("ELECTRICAL");
		portsTable.getRow(2).get(1).setValue("AMPERES_ELECTRICAL");
		// CHARTRAN
		portsTable.getRow(3).get(0).setValue("CHARTRAN");
		portsTable.getRow(3).get(1).setValue("DUALFOIL");

		// Setup MasterDetailsComponent
		masterDetails = (MasterDetailsComponent) case3.getComponent(3);

		// Delete all masters
		for (int i = 0; i < masterDetails.numberOfMasters(); i++) {
			masterDetails.deleteMasterAtIndex(0);
		}

		// Add masters
		master1 = masterDetails.addMaster();
		master2 = masterDetails.addMaster();
		master3 = masterDetails.addMaster();
		master4 = masterDetails.addMaster();

		// Setup masters
		assertTrue(masterDetails.setMasterInstanceValue(master1,
				"CHARTRAN_ELECTRICAL_THERMAL_DRIVER"));
		assertTrue(masterDetails.setMasterInstanceValue(master2,
				"AMPERES_THERMAL"));
		assertTrue(masterDetails.setMasterInstanceValue(master3,
				"AMPERES_ELECTRICAL"));
		assertTrue(masterDetails.setMasterInstanceValue(master4, "DUALFOIL"));

		// Setup values of masters

		// CHARTRAN_ELECTRICAL_THERMAL_DRIVER
		detailsComponent = masterDetails.getDetails(master1);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "DRIVERS"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Driver"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES", ""));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/thermal_electrical_chartran_driver_n.py"));

		// AMPERES_THERMAL
		detailsComponent = masterDetails.getDetails(master2);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "THERMAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Amperes"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "ELECTRICAL", "True"));
		assertTrue(this.setEntryByTag(detailsComponent, "BCTYPE", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "XCONDUCTIVITY", "3.2"));
		assertTrue(this.setEntryByTag(detailsComponent, "YCONDUCTIVITY", "3.2"));
		assertTrue(this.setEntryByTag(detailsComponent, "ZCONDUCTIVITY", "3.2"));
		assertTrue(this.setEntryByTag(detailsComponent, "HEATCAPACITY", "700"));
		assertTrue(this.setEntryByTag(detailsComponent, "CONVECTIVERATE",
				"25.0"));
		assertTrue(this.setEntryByTag(detailsComponent, "ICSHORT", "False"));
		assertTrue(this.setEntryByTag(detailsComponent, "SIDECOOLING", "False"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/amperes_thermal"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'DualRolledCell3.e'"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/amperes_thermal.py"));

		// AMPERES ELECTRICAL
		detailsComponent = masterDetails.getDetails(master3);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "ELECTRICAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Amperes"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/amperes_electrical"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'DualRolledCell3.e'"));
		assertTrue(this.setEntryByTag(detailsComponent, "CurrentFlux", "21000"));
		assertTrue(this.setEntryByTag(detailsComponent, "ICSHORT", "False"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/amperes_electrical.py"));

		// DUALFOIL
		detailsComponent = masterDetails.getDetails(master4);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "CHARTRAN"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "DualFoil"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/dualfoil"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'dualfoil5.in', 'li-ion-ebar.in'"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"'df_caebat.out'"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/dualfoil_chartran.py"));
		return case3;

		// end-user-code
	}

	/**
	 * <p>
	 * Generates data for case4. This is based upon the data provided in the
	 * Caebat's Case 4 input model. This operation will first grab the default
	 * form from the this.createPlugins() operation and set the data
	 * accordingly.
	 * </p>
	 * 
	 * @return The generated data for the specified case.
	 */
	private Form generateCase4() {
		// begin-user-code

		// Local Declarations
		Form case4;
		DataComponent caseSelected, caebatComponent;
		MasterDetailsComponent masterDetails;
		DataComponent detailsComponent;
		int master1, master2, master3, master4;
		TableComponent portsTable;

		case4 = this.createPlugins();
		case4.setName("Case4");

		// CaseSelected Information
		caseSelected = (DataComponent) case4.getComponent(0);
		caseSelected.retrieveEntry("Previously Selected Case")
				.setValue("Case4");

		// Caebat information
		caebatComponent = (DataComponent) case4.getComponent(1);
		assertTrue(this.setEntryByTag(caebatComponent, "TAG",
				"ElectroChemElectricalThermal"));

		// Ports
		portsTable = (TableComponent) case4.getComponent(2);

		// Clear out table
		while (portsTable.getRowIds().size() > 0) {
			portsTable.deleteRow(0);
		}

		// Add rows - 4 of them
		portsTable.addRow();
		portsTable.addRow();
		portsTable.addRow();
		portsTable.addRow();

		// Setup Row information
		// Driver
		portsTable.getRow(0).get(0).setValue("DRIVER");
		portsTable.getRow(0).get(1)
				.setValue("CHARTRAN_ELECTRICAL_THERMAL_DRIVER");
		// THERMAL
		portsTable.getRow(1).get(0).setValue("THERMAL");
		portsTable.getRow(1).get(1).setValue("AMPERES_THERMAL");
		// ELECTRICAL
		portsTable.getRow(2).get(0).setValue("ELECTRICAL");
		portsTable.getRow(2).get(1).setValue("AMPERES_ELECTRICAL");
		// CHARTRAN
		portsTable.getRow(3).get(0).setValue("CHARTRAN");
		portsTable.getRow(3).get(1).setValue("NTG");

		// Setup MasterDetailsComponent
		masterDetails = (MasterDetailsComponent) case4.getComponent(3);

		// Delete all masters
		for (int i = 0; i < masterDetails.numberOfMasters(); i++) {
			masterDetails.deleteMasterAtIndex(0);
		}

		// Add masters
		master1 = masterDetails.addMaster();
		master2 = masterDetails.addMaster();
		master3 = masterDetails.addMaster();
		master4 = masterDetails.addMaster();

		// Setup masters
		assertTrue(masterDetails.setMasterInstanceValue(master1,
				"CHARTRAN_ELECTRICAL_THERMAL_DRIVER"));
		assertTrue(masterDetails.setMasterInstanceValue(master2,
				"AMPERES_THERMAL"));
		assertTrue(masterDetails.setMasterInstanceValue(master3,
				"AMPERES_ELECTRICAL"));
		assertTrue(masterDetails.setMasterInstanceValue(master4, "NTG"));

		// CHARTRAN_ELECTRICAL_THERMAL_DRIVER
		detailsComponent = masterDetails.getDetails(master1);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "DRIVERS"));
		assertTrue(this.setEntryByTag(detailsComponent, "SUB_CLASS",
				"CHARTRAN_THERMAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Driver"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES", ""));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/thermal_electrical_chartran_driver_n.py"));

		// AMPERES_THERMAL
		detailsComponent = masterDetails.getDetails(master2);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "THERMAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Amperes"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "ELECTRICAL", "True"));
		assertTrue(this.setEntryByTag(detailsComponent, "BCTYPE", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "XCONDUCTIVITY", "3.2"));
		assertTrue(this.setEntryByTag(detailsComponent, "YCONDUCTIVITY", "3.2"));
		assertTrue(this.setEntryByTag(detailsComponent, "ZCONDUCTIVITY", "3.2"));
		assertTrue(this.setEntryByTag(detailsComponent, "HEATCAPACITY", "700"));
		assertTrue(this.setEntryByTag(detailsComponent, "CONVECTIVERATE",
				"25.0"));
		assertTrue(this.setEntryByTag(detailsComponent, "ICSHORT", "False"));
		assertTrue(this.setEntryByTag(detailsComponent, "SIDECOOLING", "False"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/amperes_thermal"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'LGChem2.e'"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/amperes_thermal.py"));

		// AMPERES ELECTRICAL
		detailsComponent = masterDetails.getDetails(master3);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "ELECTRICAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Amperes"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/amperes_electrical"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'LGChem2.e'"));
		assertTrue(this
				.setEntryByTag(detailsComponent, "CurrentFlux", "629310"));
		assertTrue(this.setEntryByTag(detailsComponent, "ICSHORT", "False"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/amperes_electrical.py"));

		// NTG
		detailsComponent = masterDetails.getDetails(master4);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "CHARTRAN"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "NTG"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/ntg"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES", " "));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"'ntg.out'"));
		assertTrue(this.setEntryByTag(detailsComponent, "CRATE", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "CURRDEN", "32.77"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/ntg_chartran.py"));

		return case4;

		// end-user-code
	}

	/**
	 * <p>
	 * Generates data for case6. This is based upon the data provided in the
	 * Caebat's Case 6 input model. This operation will first grab the default
	 * form from the this.createPlugins() operation and set the data
	 * accordingly.
	 * </p>
	 * 
	 * @return The generated data for the specified case.
	 */
	private Form generateCase6() {
		// begin-user-code

		// Local Declarations
		Form case6;
		DataComponent caseSelected, caebatComponent;
		MasterDetailsComponent masterDetails;
		DataComponent detailsComponent;
		int master1, master2, master3, master4;
		TableComponent portsTable;

		// Create the case
		case6 = this.createPlugins();
		case6.setName("Case6");

		// CaseSelected Information
		caseSelected = (DataComponent) case6.getComponent(0);
		caseSelected.retrieveEntry("Previously Selected Case")
				.setValue("Case6");

		// Ports
		portsTable = (TableComponent) case6.getComponent(2);

		// Clear out table
		while (portsTable.getRowIds().size() > 0) {
			portsTable.deleteRow(0);
		}

		// Add rows - 4 of them
		portsTable.addRow();
		portsTable.addRow();
		portsTable.addRow();
		portsTable.addRow();

		// Setup Row information
		// Driver
		portsTable.getRow(0).get(0).setValue("DRIVER");
		portsTable.getRow(0).get(1)
				.setValue("CHARTRAN_ELECTRICAL_THERMAL_DRIVER");
		// THERMAL
		portsTable.getRow(1).get(0).setValue("THERMAL");
		portsTable.getRow(1).get(1).setValue("AMPERES_THERMAL");
		// ELECTRICAL
		portsTable.getRow(2).get(0).setValue("ELECTRICAL");
		portsTable.getRow(2).get(1).setValue("AMPERES_ELECTRICAL");
		// CHARTRAN
		portsTable.getRow(3).get(0).setValue("CHARTRAN");
		portsTable.getRow(3).get(1).setValue("NTG");

		// Caebat Information
		caebatComponent = (DataComponent) case6.getComponent(1);
		assertTrue(this.setEntryByTag(caebatComponent, "RUN_ID", "case6"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_ZONES", "73"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CHARTRAN_ZONES",
				"34"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CCN_ZONES", "18"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CCP_ZONES", "19"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_POUCH_ZONES", "2"));
		assertTrue(this.setEntryByTag(caebatComponent, "TAG",
				"ElectroChemElectricalThermal"));

		// Setup MasterDetailsComponent
		masterDetails = (MasterDetailsComponent) case6.getComponent(3);

		// Delete all masters
		for (int i = 0; i < masterDetails.numberOfMasters(); i++) {
			masterDetails.deleteMasterAtIndex(0);
		}

		// Add masters
		master1 = masterDetails.addMaster();
		master2 = masterDetails.addMaster();
		master3 = masterDetails.addMaster();
		master4 = masterDetails.addMaster();

		// Setup masters
		assertTrue(masterDetails.setMasterInstanceValue(master1,
				"CHARTRAN_ELECTRICAL_THERMAL_DRIVER"));
		assertTrue(masterDetails.setMasterInstanceValue(master2,
				"AMPERES_THERMAL"));
		assertTrue(masterDetails.setMasterInstanceValue(master3,
				"AMPERES_ELECTRICAL"));
		assertTrue(masterDetails.setMasterInstanceValue(master4, "NTG"));

		// Setup values of masters

		// CHARTRAN_ELECTRICAL_THERMAL_DRIVER
		detailsComponent = masterDetails.getDetails(master1);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "DRIVERS"));
		assertTrue(this.setEntryByTag(detailsComponent, "SUB_CLASS",
				"CHARTRAN_THERMAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Driver"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES", ""));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/thermal_electrical_chartran_driver_n.py"));

		// AMPERES_THERMAL
		detailsComponent = masterDetails.getDetails(master2);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "THERMAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Amperes"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "ELECTRICAL", "True"));
		assertTrue(this.setEntryByTag(detailsComponent, "BCTYPE", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "XCONDUCTIVITY",
				"1.7899"));
		assertTrue(this.setEntryByTag(detailsComponent, "YCONDUCTIVITY",
				"1.7899"));
		assertTrue(this.setEntryByTag(detailsComponent, "ZCONDUCTIVITY",
				"1.7899"));
		assertTrue(this.setEntryByTag(detailsComponent, "HEATCAPACITY", "1100"));
		assertTrue(this
				.setEntryByTag(detailsComponent, "CONVECTIVERATE", "5.0"));
		assertTrue(this.setEntryByTag(detailsComponent, "ICSHORT", "False"));
		assertTrue(this.setEntryByTag(detailsComponent, "SIDECOOLING", "False"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/amperes_thermal"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'FarasisWSingleTab.e'"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/amperes_thermal.py"));

		// AMPERES ELECTRICAL
		detailsComponent = masterDetails.getDetails(master3);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "ELECTRICAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Amperes"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/amperes_electrical"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'FarasisWSingleTab.e'"));
		assertTrue(this
				.setEntryByTag(detailsComponent, "CurrentFlux", "790441"));
		assertTrue(this.setEntryByTag(detailsComponent, "ICSHORT", "False"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/amperes_electrical.py"));

		// NTG
		detailsComponent = masterDetails.getDetails(master4);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "CHARTRAN"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "NTG"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/ntg"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES", " "));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"'ntg.out'"));
		assertTrue(this.setEntryByTag(detailsComponent, "CRATE", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "CURRDEN", "15.852"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/ntg_chartran.py"));

		// Time_Loop
		DataComponent timeLoop = (DataComponent) case6.getComponent(4);
		assertTrue(this.setEntryByTag(timeLoop, "FINISH", "55"));
		assertTrue(this.setEntryByTag(timeLoop, "NSTEP", "5"));
		assertTrue(this.setEntryByTag(timeLoop, "VALUES", "3.4 3.5 3.6 3.7"));

		return case6;

		// end-user-code
	}

	/**
	 * <p>
	 * Generates data for case7a. This is based upon the data provided in the
	 * Caebat's Case 7a input model. This operation will first grab the default
	 * form from the this.createPlugins() operation and set the data
	 * accordingly.
	 * </p>
	 * 
	 * @return The generated data for the specified case.
	 */
	private Form generateCase7a() {
		// begin-user-code

		// Local Declarations
		Form case7a;
		DataComponent caseSelected, caebatComponent;
		MasterDetailsComponent masterDetails;
		DataComponent detailsComponent;
		int master1, master2, master3, master4;
		TableComponent portsTable;
		DataComponent timeLoop;

		// Creat the case
		case7a = this.createPlugins();
		case7a.setName("Case7a");

		// CaseSelected Information
		caseSelected = (DataComponent) case7a.getComponent(0);
		caseSelected.retrieveEntry("Previously Selected Case").setValue(
				"Case7a");

		// Ports
		portsTable = (TableComponent) case7a.getComponent(2);

		// Clear out table
		while (portsTable.getRowIds().size() > 0) {
			portsTable.deleteRow(0);
		}

		// Add rows - 4 of them
		portsTable.addRow();
		portsTable.addRow();
		portsTable.addRow();
		portsTable.addRow();

		// Setup Row information
		// Driver
		portsTable.getRow(0).get(0).setValue("DRIVER");
		portsTable.getRow(0).get(1)
				.setValue("CHARTRAN_ELECTRICAL_THERMAL_DRIVER");
		// THERMAL
		portsTable.getRow(1).get(0).setValue("THERMAL");
		portsTable.getRow(1).get(1).setValue("AMPERES_THERMAL");
		// ELECTRICAL
		portsTable.getRow(2).get(0).setValue("ELECTRICAL");
		portsTable.getRow(2).get(1).setValue("AMPERES_ELECTRICAL");
		// CHARTRAN
		portsTable.getRow(3).get(0).setValue("CHARTRAN");
		portsTable.getRow(3).get(1).setValue("NTG");

		// Caebat Information
		caebatComponent = (DataComponent) case7a.getComponent(1);
		assertTrue(this.setEntryByTag(caebatComponent, "RUN_ID", "case7a"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_ZONES", "308"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CHARTRAN_ZONES",
				"136"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CCN_ZONES", "80"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CCP_ZONES", "84"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_POUCH_ZONES", "8"));
		assertTrue(this.setEntryByTag(caebatComponent, "TAG",
				"ElectroChemElectricalThermal"));

		// Setup MasterDetailsComponent
		masterDetails = (MasterDetailsComponent) case7a.getComponent(3);

		// Delete all masters
		for (int i = 0; i < masterDetails.numberOfMasters(); i++) {
			masterDetails.deleteMasterAtIndex(0);
		}

		// Add masters
		master1 = masterDetails.addMaster();
		master2 = masterDetails.addMaster();
		master3 = masterDetails.addMaster();
		master4 = masterDetails.addMaster();

		// Setup masters
		assertTrue(masterDetails.setMasterInstanceValue(master1,
				"CHARTRAN_ELECTRICAL_THERMAL_DRIVER"));
		assertTrue(masterDetails.setMasterInstanceValue(master2,
				"AMPERES_THERMAL"));
		assertTrue(masterDetails.setMasterInstanceValue(master3,
				"AMPERES_ELECTRICAL"));
		assertTrue(masterDetails.setMasterInstanceValue(master4, "NTG"));

		// Setup values of masters

		// CHARTRAN_ELECTRICAL_THERMAL_DRIVER
		detailsComponent = masterDetails.getDetails(master1);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "DRIVERS"));
		assertTrue(this.setEntryByTag(detailsComponent, "SUB_CLASS",
				"CHARTRAN_THERMAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Driver"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES", ""));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/thermal_electrical_chartran_driver_n.py"));

		// AMPERES_THERMAL
		detailsComponent = masterDetails.getDetails(master2);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "THERMAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Amperes"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "ELECTRICAL", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BCTYPE", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "XCONDUCTIVITY",
				"1.7899"));
		assertTrue(this.setEntryByTag(detailsComponent, "YCONDUCTIVITY",
				"1.7899"));
		assertTrue(this.setEntryByTag(detailsComponent, "ZCONDUCTIVITY",
				"1.7899"));
		assertTrue(this.setEntryByTag(detailsComponent, "HEATCAPACITY", "1100"));
		assertTrue(this.setEntryByTag(detailsComponent, "CONVECTIVERATE",
				"15.0"));
		assertTrue(this.setEntryByTag(detailsComponent, "ICSHORT", "0"));
		assertTrue(this.setEntryByTag(detailsComponent, "SIDECOOLING", "0"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/amperes_thermal"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'FarasisModule_4P.e'"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/amperes_thermal.py"));

		// AMPERES ELECTRICAL
		detailsComponent = masterDetails.getDetails(master3);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "ELECTRICAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Amperes"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/amperes_electrical"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'FarasisModule_4P.e'"));
		assertTrue(this.setEntryByTag(detailsComponent, "CurrentFlux",
				"259374996"));
		assertTrue(this.setEntryByTag(detailsComponent, "ICSHORT", "0"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/amperes_electrical.py"));

		// NTG
		detailsComponent = masterDetails.getDetails(master4);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "CHARTRAN"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "NTG"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/ntg"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES", " "));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"'ntg.out'"));
		assertTrue(this.setEntryByTag(detailsComponent, "CRATE", "5"));
		assertTrue(this.setEntryByTag(detailsComponent, "CURRDEN", "79.26"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/ntg_chartran.py"));

		// Time_Loop
		timeLoop = (DataComponent) case7a.getComponent(4);
		assertTrue(this.setEntryByTag(timeLoop, "FINISH", "12"));
		assertTrue(this.setEntryByTag(timeLoop, "NSTEP", "4"));
		assertTrue(this.setEntryByTag(timeLoop, "VALUES", "3.4 3.5 3.6 3.7"));

		return case7a;

		// end-user-code
	}

	/**
	 * <p>
	 * Generates data for case7b. This is based upon the data provided in the
	 * Caebat's Case 7b input model. This operation will first grab the default
	 * form from the this.createPlugins() operation and set the data
	 * accordingly.
	 * </p>
	 * 
	 * @return The generated data for the specified case.
	 */
	private Form generateCase7b() {
		// begin-user-code

		// Local Declarations
		Form case7b;
		DataComponent caseSelected, caebatComponent;
		MasterDetailsComponent masterDetails;
		DataComponent detailsComponent;
		int master1, master2, master3, master4;
		TableComponent portsTable;
		DataComponent timeLoop;

		// Create the case
		case7b = this.createPlugins();
		case7b.setName("Case7b");

		// CaseSelected Information
		caseSelected = (DataComponent) case7b.getComponent(0);
		caseSelected.retrieveEntry("Previously Selected Case").setValue(
				"Case7b");

		// Ports
		portsTable = (TableComponent) case7b.getComponent(2);

		// Clear out table
		while (portsTable.getRowIds().size() > 0) {
			portsTable.deleteRow(0);
		}

		// Add rows - 4 of them
		portsTable.addRow();
		portsTable.addRow();
		portsTable.addRow();
		portsTable.addRow();

		// Setup Row information
		// Driver
		portsTable.getRow(0).get(0).setValue("DRIVER");
		portsTable.getRow(0).get(1)
				.setValue("CHARTRAN_ELECTRICAL_THERMAL_DRIVER");
		// THERMAL
		portsTable.getRow(1).get(0).setValue("THERMAL");
		portsTable.getRow(1).get(1).setValue("AMPERES_THERMAL");
		// ELECTRICAL
		portsTable.getRow(2).get(0).setValue("ELECTRICAL");
		portsTable.getRow(2).get(1).setValue("AMPERES_ELECTRICAL");
		// CHARTRAN
		portsTable.getRow(3).get(0).setValue("CHARTRAN");
		portsTable.getRow(3).get(1).setValue("NTG");

		// Caebat Information
		caebatComponent = (DataComponent) case7b.getComponent(1);
		assertTrue(this.setEntryByTag(caebatComponent, "RUN_ID", "case7b"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_ZONES", "300"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CHARTRAN_ZONES",
				"136"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CCN_ZONES", "76"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_CCP_ZONES", "80"));
		assertTrue(this.setEntryByTag(caebatComponent, "NUM_POUCH_ZONES", "8"));
		assertTrue(this.setEntryByTag(caebatComponent, "TAG",
				"ElectroChemElectricalThermal"));

		// Setup MasterDetailsComponent
		masterDetails = (MasterDetailsComponent) case7b.getComponent(3);

		// Delete all masters
		for (int i = 0; i < masterDetails.numberOfMasters(); i++) {
			masterDetails.deleteMasterAtIndex(0);
		}

		// Add masters
		master1 = masterDetails.addMaster();
		master2 = masterDetails.addMaster();
		master3 = masterDetails.addMaster();
		master4 = masterDetails.addMaster();

		// Setup masters
		assertTrue(masterDetails.setMasterInstanceValue(master1,
				"CHARTRAN_ELECTRICAL_THERMAL_DRIVER"));
		assertTrue(masterDetails.setMasterInstanceValue(master2,
				"AMPERES_THERMAL"));
		assertTrue(masterDetails.setMasterInstanceValue(master3,
				"AMPERES_ELECTRICAL"));
		assertTrue(masterDetails.setMasterInstanceValue(master4, "NTG"));

		// Setup values of masters

		// CHARTRAN_ELECTRICAL_THERMAL_DRIVER
		detailsComponent = masterDetails.getDetails(master1);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "DRIVERS"));
		assertTrue(this.setEntryByTag(detailsComponent, "SUB_CLASS",
				"CHARTRAN_THERMAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Driver"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES", ""));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/thermal_electrical_chartran_driver_n.py"));

		// AMPERES_THERMAL
		detailsComponent = masterDetails.getDetails(master2);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "THERMAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Amperes"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "ELECTRICAL", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BCTYPE", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "XCONDUCTIVITY",
				"1.7899"));
		assertTrue(this.setEntryByTag(detailsComponent, "YCONDUCTIVITY",
				"1.7899"));
		assertTrue(this.setEntryByTag(detailsComponent, "ZCONDUCTIVITY",
				"1.7899"));
		assertTrue(this.setEntryByTag(detailsComponent, "HEATCAPACITY", "1100"));
		assertTrue(this.setEntryByTag(detailsComponent, "CONVECTIVERATE",
				"15.0"));
		assertTrue(this.setEntryByTag(detailsComponent, "ICSHORT", "0"));
		assertTrue(this.setEntryByTag(detailsComponent, "SIDECOOLING", "0"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/amperes_thermal"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'FarasisModule_4S.e'"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/amperes_thermal.py"));

		// AMPERES ELECTRICAL
		detailsComponent = masterDetails.getDetails(master3);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "ELECTRICAL"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "Amperes"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/amperes_electrical"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES",
				"'FarasisModule_4S.e'"));
		assertTrue(this.setEntryByTag(detailsComponent, "CurrentFlux",
				"64843750"));
		assertTrue(this.setEntryByTag(detailsComponent, "ICSHORT", "0"));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"$CURRENT_STATE"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/amperes_electrical.py"));

		// NTG
		detailsComponent = masterDetails.getDetails(master4);
		assertTrue(this.setEntryByTag(detailsComponent, "CLASS", "CHARTRAN"));
		assertTrue(this.setEntryByTag(detailsComponent, "NAME", "NTG"));
		assertTrue(this.setEntryByTag(detailsComponent, "NPROC", "1"));
		assertTrue(this.setEntryByTag(detailsComponent, "BIN_PATH",
				"$CAEBAT_ROOT/bin"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_DIR",
				"$DATA_ROOT/ntg"));
		assertTrue(this.setEntryByTag(detailsComponent, "INPUT_FILES", " "));
		assertTrue(this.setEntryByTag(detailsComponent, "OUTPUT_FILES",
				"'ntg.out'"));
		assertTrue(this.setEntryByTag(detailsComponent, "CRATE", "5"));
		assertTrue(this.setEntryByTag(detailsComponent, "CURRDEN", "79.26"));
		assertTrue(this.setEntryByTag(detailsComponent, "SCRIPT",
				"$BIN_PATH/ntg_chartran.py"));

		// Time_Loop
		timeLoop = (DataComponent) case7b.getComponent(4);
		assertTrue(this.setEntryByTag(timeLoop, "FINISH", "12"));
		assertTrue(this.setEntryByTag(timeLoop, "NSTEP", "20"));
		assertTrue(this.setEntryByTag(timeLoop, "VALUES", "3.4 3.5 3.6 3.7"));

		return case7b;

		// end-user-code
	}

	/**
	 * <p>
	 * This operation generates specific cases. These case files are designed
	 * specifically to be moved into a certain directory on the model and to be
	 * used when generating the designed output specified within Caebat's
	 * default input model for EACH case.
	 * </p>
	 */

	@Test
	public void generateCaseFiles() {

		// begin-user-code

		// Local Declarations
		Form case3, case4, case6, case7a, case7b;

		// Generate the cases
		case3 = this.generateCase3();
		case4 = this.generateCase4();
		case6 = this.generateCase6();
		case7a = this.generateCase7a();
		case7b = this.generateCase7b();

		// Add the forms to the arraylist. These will need to be outputted to
		// xml
		ArrayList<Form> forms = new ArrayList<Form>();
		forms.add(case3);
		forms.add(case4);
		forms.add(case6);
		forms.add(case7a);
		forms.add(case7b);

		// ----- Generate Forms from unique cases

		// Local Declarations
		String separator = System.getProperty("file.separator");

		// This operation will setup the sample workspace with
		// the required xml files.
		// Create the proper folder for the loadDataComponents if it does not
		// exist
		IFolder folder = projectSpace.getFolder("caebatCases");

		// If it exists, delete the folder.
		if (folder.exists()) {
			try {
				folder.delete(true, null);
			} catch (CoreException e) {
				e.printStackTrace();
				fail();
			}
		}

		// If the folder does not exist, create it.
		folder = projectSpace.getFolder("caebatCases");
		if (!folder.exists()) {
			try {
				folder.create(true, true, null);
			} catch (CoreException e1) {
				e1.printStackTrace();
				fail(); // Fail here if exception hit.
			}
		}

		// Now, we need to check to see if the xml files exist before we begin.
		// Use the project space - safer
		// Iterate through each name of the Components on the form, convert
		// their filenames to the correct format
		// and see if they exist in the right directory.
		// If they exist, delete them. Then create them.
		for (int i = 0; i < forms.size(); i++) {
			Form form = forms.get(i);

			// Replace spaces with underscores
			String subFileName = form.getName().replace(" ", "_");
			String fileName = "caebatCases" + separator + subFileName
					+ "CAEBATModel.case";

			// With the file name, check to see if it exists
			IFile file = projectSpace.getFile(fileName);
			// If it exists, delete it.
			if (file != null) {
				try {
					file.delete(true, null);
				} catch (CoreException e) {
					e.printStackTrace();
					fail(); // Fail here if this stacktrace is hit!
				}
			}

			// If it does not exist, then it is time to make the DataComponent
			// xml file.
			// Create the OutputStream to copy information
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			// Persist the dataComponent to XML
			form.persistToXML(outputStream);

			// create the file
			ByteArrayInputStream source;

			// Convert the outputStream to inputStream
			source = new ByteArrayInputStream(outputStream.toByteArray());

			IFile file2 = projectSpace.getFile(fileName);
			// Try to create the file.
			try {
				// Create the file
				file2.create(source, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
				fail();
			}

		}

		// end-user-code

	}

	/**
	 * Sets the entry's value based upon the tag.
	 * 
	 * @param component
	 *            The DataComponent that contains entries and the associated
	 *            Tag.
	 * @param tag
	 *            The name of the entry
	 * @param value
	 *            The value to set for the tag
	 * @return True if the tag was found and value was set, false otherwise.
	 */

	boolean setEntryByTag(DataComponent component, String tag, String value) {

		for (int i = 0; i < component.retrieveAllEntries().size(); i++) {
			if (component.retrieveAllEntries().get(i).getTag().equals(tag)) {
				component.retrieveAllEntries().get(i).setValue(value);
				return true;
			}
		}

		return false;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation generates the Component files. These files are created
	 * based upon their component's name. This class is important since it lays
	 * out the model of the Caebat's Input model and it's default values.
	 * </p>
	 * <!-- begin-UML-doc -->
	 */
	@Test
	public void generatePlugins() {

		// begin-user-code

		// Local Declarations
		Form form;

		// Create the form
		form = this.createPlugins();

		// Create suffix
		ArrayList<String> suffix = new ArrayList<String>();
		suffix.add("dac");
		suffix.add("dac");
		suffix.add("tab");
		suffix.add("mdp");
		suffix.add("tdc");
		this.createXMLComponentFiles(form, suffix);

		// end-user-code

	}

	/**
	 * Creates a list of DataComponents that contains specific dependencies for
	 * the Drivers for K/V pairs. The names used in these operations should NOT
	 * BE CHANGED. These names are protected under the Caebat's execution for
	 * picking up the values. The Tag's values on the entry are what are used to
	 * create the Caebat's input file and should be carefully considered when
	 * altering or removing as well.
	 * 
	 * @return A list of DataComponent that represent the unique drivers (Caebat
	 *         Components).
	 */
	private ArrayList<DataComponent> createTemplatePlugins() {

		// begin-user-code

		DataComponent chartranElecThermDriv = new DataComponent();
		DataComponent amperesThermalComponent = new DataComponent();
		DataComponent amperesElectricalComponent = new DataComponent();
		DataComponent ntgComponent = new DataComponent();
		DataComponent dualFoilComponent = new DataComponent();
		Entry entry;
		int entryCount = 0;

		// A form will contain a selection of DataComponents. Each DataComponent
		// will contain a list of entries, or K/V pair associations.

		// Setup CHARTRAN_ELECTRICAL_THERMAL_DRIVER
		chartranElecThermDriv.setName("CHARTRAN_ELECTRICAL_THERMAL_DRIVER");
		chartranElecThermDriv
				.setDescription("Handles dependencies for templated value.");

		// Setup AMPERES_THERMAL

		amperesThermalComponent.setName("AMPERES_THERMAL");
		amperesThermalComponent
				.setDescription("Handles dependencies for templated value.");

		// Entry:Electrical
		entry = new Entry() {
			protected void setup() {
				this.setName("Electrical");
				this.tag = "ELECTRICAL";
				this.setDescription("Boolean for handling electrical input.");
				this.defaultValue = "True";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("True");
				this.allowedValues.add("False");
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesThermalComponent.addEntry(entry);

		// Entry:BCTYPE
		entry = new Entry() {
			protected void setup() {
				this.setName("BC type");
				this.tag = "BCTYPE";
				this.setDescription("Numerical value for BC type");
				this.defaultValue = "1";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("-9999999");
				this.allowedValues.add("9999999");
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesThermalComponent.addEntry(entry);

		// Entry:XCONDUCTIVITY
		entry = new Entry() {
			protected void setup() {
				this.setName("X Conductivity");
				this.tag = "XCONDUCTIVITY";
				this.setDescription("Conductivity for X coordinate.");
				this.defaultValue = "3.2";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("-9999999");
				this.allowedValues.add("9999999");
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesThermalComponent.addEntry(entry);

		// Entry:YCONDUCTIVITY
		entry = new Entry() {
			protected void setup() {
				this.setName("Y Conductivity");
				this.tag = "YCONDUCTIVITY";
				this.setDescription("Conductivity for Y coordinate.");
				this.defaultValue = "3.2";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("-9999999");
				this.allowedValues.add("9999999");
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesThermalComponent.addEntry(entry);

		// Entry:ZCONDUCTIVITY
		entry = new Entry() {
			protected void setup() {
				this.setName("Z Conductivity");
				this.tag = "ZCONDUCTIVITY";
				this.setDescription("Conductivity for Z coordinate.");
				this.defaultValue = "3.2";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("-9999999");
				this.allowedValues.add("9999999");
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesThermalComponent.addEntry(entry);

		// Entry:HEATCAPACITY
		entry = new Entry() {
			protected void setup() {
				this.setName("Heat Capacity");
				this.tag = "HEATCAPACITY";
				this.setDescription("Handles the heat capacity.");
				this.defaultValue = "700";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("-9999999");
				this.allowedValues.add("9999999");
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesThermalComponent.addEntry(entry);

		// Entry:ICSHORT
		entry = new Entry() {
			protected void setup() {
				this.setName("ICShort");
				this.tag = "ICSHORT";
				this.setDescription("A boolean to represent ic short.");
				this.defaultValue = "False";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("True");
				this.allowedValues.add("False");
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesThermalComponent.addEntry(entry);

		// Entry:CONVECTIVERATE
		entry = new Entry() {
			protected void setup() {
				this.setName("Convection rate");
				this.tag = "CONVECTIVERATE";
				this.setDescription("A numerical value to represent convection rate.");
				this.defaultValue = "25";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("-9999999");
				this.allowedValues.add("9999999");
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesThermalComponent.addEntry(entry);

		// Entry:SIDECOOLING
		entry = new Entry() {
			protected void setup() {
				this.setName("Side Cooling");
				this.tag = "SIDECOOLING";
				this.setDescription("A boolean to represent side cooling.");
				this.defaultValue = "False";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("True");
				this.allowedValues.add("False");
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesThermalComponent.addEntry(entry);

		// Setup Amperes Electrical

		amperesElectricalComponent.setName("AMPERES_ELECTRICAL");
		amperesElectricalComponent
				.setDescription("Handles dependencies for templated value.");
		entryCount = 0;// Reset entry count

		// Entry:Current Flux
		entry = new Entry() {
			protected void setup() {
				this.setName("The current flux");
				this.tag = "CurrentFlux";
				this.setDescription("Handles the heat capacity.");
				this.defaultValue = "629310";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("-9999999");
				this.allowedValues.add("9999999");
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesElectricalComponent.addEntry(entry);

		// Entry:ICSHORT
		entry = new Entry() {
			protected void setup() {
				this.setName("ICShort");
				this.tag = "ICSHORT";
				this.setDescription("A boolean to represent ic short.");
				this.defaultValue = "False";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("True");
				this.allowedValues.add("False");
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		amperesElectricalComponent.addEntry(entry);

		// ntgComponent
		// Reset entryCount
		entryCount = 0;
		ntgComponent.setName("NTG");
		ntgComponent
				.setDescription("Handles dependencies for templated value.");

		// Entry:CRATE
		entry = new Entry() {
			protected void setup() {
				this.setName("The current rate");
				this.tag = "CRATE";
				this.setDescription("Handles the current rate.");
				this.defaultValue = "1";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("-9999999");
				this.allowedValues.add("9999999");
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		ntgComponent.addEntry(entry);

		// Entry:CURRDEN
		entry = new Entry() {
			protected void setup() {
				this.setName("The current density.");
				this.tag = "CURRDEN";
				this.setDescription("Handles the current density.");
				this.defaultValue = "32.77";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("-9999999");
				this.allowedValues.add("9999999");
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		ntgComponent.addEntry(entry);

		dualFoilComponent.setName("DUALFOIL");
		dualFoilComponent
				.setDescription("Handles dependencies for templated value.");

		ArrayList<DataComponent> components = new ArrayList<DataComponent>();

		// Add components to form
		components.add(chartranElecThermDriv);
		components.add(amperesThermalComponent);
		components.add(amperesElectricalComponent);
		components.add(ntgComponent);
		components.add(dualFoilComponent);

		return components;

		// end-user-code

	}

	/**
	 * Create the form that contains the components in order to generate/use
	 * plugins from. The names and tags should be carefully considered when
	 * editing due to the fact that dependencies within the Caebat's Executable
	 * and the Caebat Model use these names (Caebat execution uses the tags)
	 * specifically for dependency and K/V pair reading.
	 * 
	 * @return created form
	 */

	private Form createPlugins() {

		// begin-user-code

		// Local Declarations
		DataComponent caseComponent;
		DataComponent caebatDataComponent;
		TableComponent portsTableComponent;
		TimeDataComponent timeLoopDataComponent;
		MasterDetailsComponent portsMasterDetails;
		Entry entry;
		int entryCount = 0;

		// Create Case Component
		caseComponent = new DataComponent();
		caseComponent.setName("Case Selection");
		caseComponent.setDescription("Select the case to load for Caebat");

		// Enable Case Selection
		entry = new Entry() {
			protected void setup() {
				this.setName("Enable Case Selection");
				this.tag = "";
				this.setDescription("Auto generated cases selection.");
				this.defaultValue = "True";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("True");
				this.allowedValues.add("False");
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		caseComponent.addEntry(entry);
		entry.setId(0);

		// Enable Case Selection Notes
		entry = new Entry() {
			protected void setup() {
				this.setName("Previously Selected Case");
				this.tag = "";
				this.setDescription("Handled by the case selection");
				this.defaultValue = "Do not change";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("Case3");
				this.allowedValues.add("Case4");
				this.allowedValues.add("Case6");
				this.allowedValues.add("Case7a");
				this.allowedValues.add("Case7b");
				this.allowedValues.add("Do not change");
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		caseComponent.addEntry(entry);
		entry.setId(1);

		// Case selection
		entry = new Entry() {
			protected void setup() {
				this.setName("Caebat Case");
				this.tag = "";
				this.setDescription("Auto generated cases.");
				this.defaultValue = "Do not change";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("Case3");
				this.allowedValues.add("Case4");
				this.allowedValues.add("Case6");
				this.allowedValues.add("Case7a");
				this.allowedValues.add("Case7b");
				this.allowedValues.add("Do not change");
				this.allowedValueType = AllowedValueType.Discrete;
				this.parent = "Enable Case Selection";
				this.ready = true;
			}
		};
		caseComponent.addEntry(entry);
		entry.setId(2);

		// Create the caebat DataComponent
		caebatDataComponent = new DataComponent();
		caebatDataComponent.setName("Caebat Information");
		caebatDataComponent.setDescription("Sets up the Caebat's information");

		// Setup the Entries

		// Entry: CAEBAT_ROOT
		entry = new Entry() {
			protected void setup() {
				this.setName("Caebat Root");
				this.tag = "CAEBAT_ROOT";
				this.setDescription("The Caebat root directory");
				this.defaultValue = "/data1/projects/caebat/VIBE/trunk/components/";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: BIN_DIR
		entry = new Entry() {
			protected void setup() {
				this.setName("Binary Directory Path");
				this.tag = "BIN_DIR";
				this.setDescription("Root of IPS component and binary tree.  Use $CAEBAT_ROOT to specify variable path.");
				this.defaultValue = "$CAEBAT_ROOT/bin";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: OUTPUT_PREFIX
		entry = new Entry() {
			protected void setup() {
				this.setName("Output Prefix");
				this.tag = "OUTPUT_PREFIX";
				this.setDescription("The prefix set on the output.");
				this.defaultValue = "";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: RUN_ID
		entry = new Entry() {
			protected void setup() {
				this.setName("Run ID");
				this.tag = "RUN_ID";
				this.setDescription("Identifier for this simulation run.");
				this.defaultValue = "case4";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: SIM_NAME
		entry = new Entry() {
			protected void setup() {
				this.setName("Simulation Name");
				this.tag = "SIM_NAME";
				this.setDescription("Name of the current simulation. Use $RUN_ID to specify the usage of RUN_ID identifier.");
				this.defaultValue = "$RUN_ID";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = true;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: SIM_ROOT
		entry = new Entry() {
			protected void setup() {
				this.setName("Simulation Root");
				this.tag = "SIM_ROOT";
				this.setDescription("Where to put results from this simulation.");
				this.defaultValue = "/data1/projects/caebat/$SIM_NAME${RUN_ID}";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: PLASMA_STATE_WORK_DIRECTORY
		entry = new Entry() {
			protected void setup() {
				this.setName("Battery State Work Directory");
				this.tag = "PLASMA_STATE_WORK_DIR";
				this.setDescription("Where to put sate files as the simulation evolves.");
				this.defaultValue = "$SIM_ROOT/work/battery_state";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: CURRENT_STATE
		entry = new Entry() {
			protected void setup() {
				this.setName("Current State");
				this.tag = "CURRENT_STATE";
				this.setDescription("The current state.");
				this.defaultValue = "cphit.cgns";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: RUN_COMMENT
		entry = new Entry() {
			protected void setup() {
				this.setName("Run Comment");
				this.tag = "RUN_COMMENT";
				this.setDescription("Coupled CAEBAT Module simulation");
				this.defaultValue = "A run comment picked up by the portal.";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: TAG
		entry = new Entry() {
			protected void setup() {
				this.setName("Tag");
				this.tag = "TAG";
				this.setDescription("A tag that enables related runs to be retrieved together.");
				this.defaultValue = "ElectroChemThermal";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: USER
		entry = new Entry() {
			protected void setup() {
				this.setName("User");
				this.tag = "USER";
				this.setDescription("Optional, if missing the unix username is used");
				this.defaultValue = "<username>";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: NUM_CHARTRAN_ZONES
		entry = new Entry() {
			protected void setup() {
				this.setName("Number of Chartran Zones");
				this.tag = "NUM_CHARTRAN_ZONES";
				this.setDescription("The number of chartran zones.");
				this.defaultValue = "10";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("0");
				this.allowedValues.add("2000");
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: NUM_CPP_ZONES
		entry = new Entry() {
			protected void setup() {
				this.setName("Number of CCP Zones");
				this.tag = "NUM_CCP_ZONES";
				this.setDescription("The number of CCP zones.");
				this.defaultValue = "10";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("0");
				this.allowedValues.add("2000");
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: NUM_CNN_ZONES
		entry = new Entry() {
			protected void setup() {
				this.setName("Number of CCN Zones");
				this.tag = "NUM_CCN_ZONES";
				this.setDescription("The number of CCN zones.");
				this.defaultValue = "10";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("0");
				this.allowedValues.add("2000");
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: NUM_ZONES
		entry = new Entry() {
			protected void setup() {
				this.setName("Number of Zones");
				this.tag = "NUM_ZONES";
				this.setDescription("The number of zones.");
				this.defaultValue = "30";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("0");
				this.allowedValues.add("2000");
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Continuous;
				this.ready = false;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: NUM_POUCH_ZONES
		entry = new Entry() {
			protected void setup() {
				this.setName("Number of Pouch Zones");
				this.tag = "NUM_POUCH_ZONES";
				this.setDescription("The number of pouch zones.");
				this.defaultValue = "0";
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("0");
				this.allowedValues.add("2000");
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: PLASMA_STATE_FILES
		entry = new Entry() {
			protected void setup() {
				this.setName("Battery State Files");
				this.tag = "PLASMA_STATE_FILES";
				this.setDescription("The Battery State files.  Can use $CURRENT_STATE tag if necessary.");
				this.defaultValue = "$CURRENT_STATE";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: SIMULATION_MODE
		entry = new Entry() {
			protected void setup() {
				this.setName("Simulation Mode");
				this.tag = "SIMULATION_MODE";
				this.setDescription("The simulation mode.");
				this.defaultValue = "NORMAL";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("NORMAL");
				this.allowedValueType = AllowedValueType.Discrete;
				this.ready = false;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: LOG_LEVEL
		entry = new Entry() {
			protected void setup() {
				this.setName("Logging Level");
				this.tag = "LOG_LEVEL";
				this.setDescription("The logging level.");
				this.defaultValue = "WARNING";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("WARNING");
				this.allowedValues.add("DEBUG");
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Entry: LOG_FILE
		entry = new Entry() {
			protected void setup() {
				this.setName("Logging File Location");
				this.tag = "LOG_FILE";
				this.setDescription("The logging file location path.  Use ${SIM_NAME} to specify the simulation name on this logging file.");
				this.defaultValue = "$SIM_ROOT/${SIM_NAME}.log";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		// Added to make each entry's id unique! Required for dependency
		// handling to work!
		entryCount++;
		entry.setId(entryCount);
		caebatDataComponent.addEntry(entry);

		// Setup Table Component

		// The portsTableComponent is a 2 col, multi-row table with undefined
		// entries;
		portsTableComponent = new TableComponent();
		portsTableComponent.setName("PORTS");
		portsTableComponent.setDescription("The ports on the simulation.");

		Entry tableColumnKey1 = new Entry() {
			protected void setup() {
				this.setName("Component Name");
				this.tag = "Column1";
				this.setDescription("The name of the component.");
				this.defaultValue = "INIT";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Discrete;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("DRIVER");
				this.allowedValues.add("THERMAL");
				this.allowedValues.add("ELECTRICAL");
				this.allowedValues.add("CHARTRAN");
				this.allowedValues.add("INIT");
			}
		};

		Entry tableColumnKey2 = new Entry() {
			protected void setup() {
				this.setName("Component Value");
				this.tag = "Column2";
				this.setDescription("The type of the components.");
				this.defaultValue = "CHARTRAN_ELECTRICAL_THERMAL_DRIVER";
				this.value = this.defaultValue;
				this.allowedValues = new ArrayList<String>();
				this.allowedValues.add("CHARTRAN_ELECTRICAL_THERMAL_DRIVER");
				this.allowedValues.add("AMPERES_THERMAL");
				this.allowedValues.add("AMPERES_ELECTRICAL");
				this.allowedValues.add("NTG");
				this.allowedValues.add("DUALFOIL");
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};

		// Add entries to template list
		ArrayList<Entry> tableList = new ArrayList<Entry>();
		tableList.add(tableColumnKey1);
		tableList.add(tableColumnKey2);

		// Add entries to table
		portsTableComponent.setRowTemplate(tableList);

		// Add as many values to table as allowed values in the second entry
		portsTableComponent.addRow();
		portsTableComponent.addRow();
		portsTableComponent.addRow();
		portsTableComponent.addRow();
		portsTableComponent.addRow();

		// Set the names
		// Column 0
		portsTableComponent.getRow(0).get(0).setValue("DRIVER");
		portsTableComponent.getRow(1).get(0).setValue("THERMAL");
		portsTableComponent.getRow(2).get(0).setValue("ELECTRICAL");
		portsTableComponent.getRow(3).get(0).setValue("CHARTRAN");
		portsTableComponent.getRow(4).get(0).setValue("INIT");

		// Column 1
		portsTableComponent.getRow(0).get(1)
				.setValue("CHARTRAN_ELECTRICAL_THERMAL_DRIVER");
		portsTableComponent.getRow(1).get(1).setValue("AMPERES_THERMAL");
		portsTableComponent.getRow(2).get(1).setValue("AMPERES_ELECTRICAL");
		portsTableComponent.getRow(3).get(1).setValue("NTG");
		portsTableComponent.getRow(4).get(1).setValue("DUALFOIL");

		// Setup portMasterDetailsComponent
		portsMasterDetails = new MasterDetailsComponent();
		portsMasterDetails.setName("Component Details");
		portsMasterDetails
				.setDescription("Lists drivers from the component's table.");

		// Disable the buttons
		portsMasterDetails.toggleAddRemoveButton(false);

		// Get the customized DataComponents.
		ArrayList<DataComponent> dataComponents = this.createTemplatePlugins();

		// This template to be set is the default behavior. This should be
		// edited during instantiation and during updates to the ports table!

		// Set template based on the values of column list.
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<DataComponent> detailList = new ArrayList<DataComponent>();

		// Use port name values from portsTableComponent to create
		// MasterDetailsPage
		for (int i = 0; i < portsTableComponent.getRowIds().size(); i++) {

			// Get the template
			DataComponent masterDetailsDataComponent = this
					.createTemplateDetailsComponent();

			// Check the list to see if the name exists
			for (int j = 0; j < dataComponents.size(); j++) {
				if (dataComponents
						.get(j)
						.getName()
						.equals(portsTableComponent.getRow(i).get(1).getValue())) {
					// Add all remaining entries to list
					masterDetailsDataComponent.retrieveAllEntries().addAll(
							dataComponents.get(j).retrieveAllEntries());
				}
			}

			// Add the name to the list of templates
			list.add(portsTableComponent.getRow(i).get(1).getValue());

			// Add the templated value
			detailList.add(masterDetailsDataComponent);
		}
		// Set the template
		portsMasterDetails.setTemplates(list, detailList);

		// Create timeLoop DataComponent.
		timeLoopDataComponent = new TimeDataComponent();
		timeLoopDataComponent.setName("TIME_LOOP");
		timeLoopDataComponent
				.setDescription("Information and selections between time loops.");

		// Hide information correctly within time loop
		if ("True".equals(timeLoopDataComponent.retrieveEntry(
				"Enable Regular Mode").getValue())) {
			timeLoopDataComponent.retrieveEntry("VALUES").setReady(false);
		} else {
			timeLoopDataComponent.retrieveEntry("VALUES").setReady(true);
		}

		// Setup the TimeLoop's values
		assertTrue(this.setEntryByTag(timeLoopDataComponent, "FINISH", "30"));
		assertTrue(this.setEntryByTag(timeLoopDataComponent, "NSTEP", "1"));
		assertTrue(this.setEntryByTag(timeLoopDataComponent, "VALUES",
				"3.4 3.5 3.6 3.7"));

		// Set ids on components
		caseComponent.setId(0);
		caebatDataComponent.setId(1);
		portsTableComponent.setId(2);
		portsMasterDetails.setId(3);
		timeLoopDataComponent.setId(4);

		// Add the components to a form
		Form form = new Form();
		form.addComponent(caseComponent);
		form.addComponent(caebatDataComponent);
		form.addComponent(portsTableComponent);
		form.addComponent(portsMasterDetails);
		form.addComponent(timeLoopDataComponent);

		return form;

		// end-user-code
	}

	/**
	 * Creates XML files given from the Components passed on the Form. These XML
	 * files will be named based on the Component.getName() operation. The
	 * suffixes passed will represent the extension, which are outlined in
	 * detail within this operation. This is important for figuring out what
	 * type of "component" is stored.
	 * 
	 * Note: The Suffixes should MATCH the number of components!!!!
	 * 
	 * @param form
	 *            The form
	 * @param suffix
	 *            The suffixes.
	 */
	private void createXMLComponentFiles(Form form, ArrayList<String> suffix) {

		// begin-user-code

		// Local Declarations
		String separator = System.getProperty("file.separator");

		// Make sure they are the same!
		assertEquals(suffix.size(), form.getComponents().size());

		// This operation will setup the sample workspace with
		// the required xml files.
		// Create the proper folder for the loadDataComponents if it does not
		// exist
		IFolder folder = projectSpace.getFolder("caebatModel");

		// If it exists, delete it
		if (folder.exists()) {
			try {
				folder.delete(true, null);
			} catch (CoreException e) {
				e.printStackTrace();
				fail();
			}
		}

		// Create the caebatModel directory
		folder = projectSpace.getFolder("caebatModel");
		if (!folder.exists()) {
			try {
				folder.create(true, true, null);
			} catch (CoreException e1) {
				e1.printStackTrace();
				fail(); // Fail here if exception hit.
			}
		}

		// Now, we need to check to see if the xml files exist before we begin.
		// Use the project space - safer
		// Iterate through each name of the Components on the form, convert
		// their filenames to the correct format
		// and see if they exist in the right directory.
		// If they exist, delete them. Then create them.
		for (int i = 0; i < form.getComponents().size(); i++) {
			ICEObject dataComponent = (ICEObject) form.getComponents().get(i);

			// Clone it
			dataComponent = (ICEObject) dataComponent.clone();

			// Replace spaces with underscores
			String subFileName = dataComponent.getName().replace(" ", "_");
			String fileName = "caebatModel" + separator + subFileName
					+ "CAEBATModel." + suffix.get(i);

			// With the file name, check to see if it exists
			IFile file = projectSpace.getFile(fileName);
			// If it exists, delete it.
			if (file != null) {
				try {
					file.delete(true, null);
				} catch (CoreException e) {
					e.printStackTrace();
					fail(); // Fail here if this stacktrace is hit!
				}
			}

			// If it does not exist, then it is time to make the DataComponent
			// xml file.
			// Create the OutputStream to copy information
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			// Persist the dataComponent to XML
			dataComponent.persistToXML(outputStream);

			// create the file
			ByteArrayInputStream source;

			// Convert the outputStream to inputStream
			source = new ByteArrayInputStream(outputStream.toByteArray());

			IFile file2 = projectSpace.getFile(fileName);
			// Try to create the file.
			try {
				// Create the file
				file2.create(source, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
				fail();
			}

		}

		// end-user-code

	}

	/**
	 * This creates a default template for the templated values. The tags should
	 * be put under careful consideration when altering, as these are used
	 * specifically within the Caebat's executable..
	 * 
	 * @return A templated DataComponent
	 */
	private DataComponent createTemplateDetailsComponent() {

		// Returns a DataComponent setup with similiar entries for the template.
		// Create DataComponent
		DataComponent masterDetailsDataComponent = new DataComponent();
		masterDetailsDataComponent.setName("Ports Template");
		masterDetailsDataComponent
				.setDescription("The list of values for the template.");
		int entryCount = 0;
		Entry entry;

		// Add entries to DataComponent

		// Entry: CLASS
		entry = new Entry() {
			protected void setup() {
				this.setName("Class");
				this.tag = "CLASS";
				this.setDescription("The class of the linked association.");
				this.defaultValue = "ClassName";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		entryCount++;
		entry.setId(entryCount);
		masterDetailsDataComponent.addEntry(entry);

		// Entry: SUB_CLASS
		entry = new Entry() {
			protected void setup() {
				this.setName("SubClass");
				this.tag = "SUB_CLASS";
				this.setDescription("The subclass of the linked association.");
				this.defaultValue = "";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		entryCount++;
		entry.setId(entryCount);
		masterDetailsDataComponent.addEntry(entry);

		// Entry: NAME
		entry = new Entry() {
			protected void setup() {
				this.setName("Name");
				this.tag = "NAME";
				this.setDescription("The specific name of the port.");
				this.defaultValue = "name";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
				this.ready = false;
			}
		};
		entryCount++;
		entry.setId(entryCount);
		masterDetailsDataComponent.addEntry(entry);

		// Entry: NPROC
		entry = new Entry() {
			protected void setup() {
				this.setName("Number of processors");
				this.tag = "NPROC";
				this.setDescription("The number of processors");
				this.defaultValue = "1";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		entryCount++;
		entry.setId(entryCount);
		masterDetailsDataComponent.addEntry(entry);

		// Entry: BIN_PATH
		entry = new Entry() {
			protected void setup() {
				this.setName("Binary Path");
				this.tag = "BIN_PATH";
				this.setDescription("The binary path.");
				this.defaultValue = "$CAEBAT_ROOT/bin";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		entryCount++;
		entry.setId(entryCount);
		masterDetailsDataComponent.addEntry(entry);

		// Entry: INPUT_DIR
		entry = new Entry() {
			protected void setup() {
				this.setName("Input Directory");
				this.tag = "INPUT_DIR";
				this.setDescription("The input directory.");
				this.defaultValue = "$DATA_ROOT/filePath";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		entryCount++;
		entry.setId(entryCount);
		masterDetailsDataComponent.addEntry(entry);

		// Entry: INPUT_FILES
		entry = new Entry() {
			protected void setup() {
				this.setName("Input Files");
				this.tag = "INPUT_FILES";
				this.setDescription("The input files.");
				this.defaultValue = "'important.e'";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		entryCount++;
		entry.setId(entryCount);
		masterDetailsDataComponent.addEntry(entry);

		// Entry: OUTPUT_FILES
		entry = new Entry() {
			protected void setup() {
				this.setName("Output Files");
				this.tag = "OUTPUT_FILES";
				this.setDescription("The output_files location.");
				this.defaultValue = "$CURRENT_STATE";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		entryCount++;
		entry.setId(entryCount);
		masterDetailsDataComponent.addEntry(entry);

		// Entry: SCRIPT
		entry = new Entry() {
			protected void setup() {
				this.setName("Script");
				this.tag = "SCRIPT";
				this.setDescription("The python script location.");
				this.defaultValue = "$BIN_PATH/file.py";
				this.value = this.defaultValue;
				this.allowedValueType = AllowedValueType.Undefined;
			}
		};
		entryCount++;
		entry.setId(entryCount);
		masterDetailsDataComponent.addEntry(entry);

		return masterDetailsDataComponent;

	}

}
