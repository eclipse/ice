/*******************************************************************************
 * Copyright (c) 2013, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.reflectivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.ice.io.csv.CSVReader;
import org.eclipse.ice.item.model.Model;
import org.eclipse.ice.materials.IMaterialsDatabase;
import org.eclipse.ice.materials.MaterialWritableTableFormat;

/**
 * This classes calculates the reflectivity profile of a set of materials
 * layered on top of each other. It has three components. The first displays
 * entries for the user to specify a file for the wave vector (expects .csv) and
 * fields for the angles, layers of roughness, and RQ4. The second component
 * displays a table for entering the materials and reordering the layers.
 * Finally, the last component gives access to the computed data as both
 * editable graphs (see CSVPlotEditor) and .csv files.
 * 
 * @author Jay Jay Billings, Alex McCaskey, Kasper Gammeltoft
 */
@XmlRootElement(name = "ReflectivityModel")
public class ReflectivityModel extends Model {

	/**
	 * The process action name for calculating the reflectivity.
	 */
	private final String processActionName = "Calculate Reflectivity";

	/**
	 * The name for the wave vector entry.
	 */
	private static final String WaveEntryName = "Wave Vector (Q) file";

	/**
	 * The roughness entry name.
	 */
	private static final String RoughnessEntryName = "Num Layers of Roughness";

	/**
	 * The delta q0 entry name.
	 */
	private static final String deltaQ0EntryName = "deltaQ0";

	/**
	 * The delta q1 by q entry name.
	 */
	private static final String deltaQ1ByQEntryName = "deltaQ1ByQ";

	/**
	 * The wave length entry name.
	 */
	private static final String WaveLengthEntryName = "Wave Length";

	/**
	 * The entry name for the chi squared analysis entry
	 */
	private static final String ChiSquaredEntryName = "Chi Squared";

	/**
	 * The entry name for the chi squared analysis for the rq4 profile
	 */
	private static final String ChiSquaredRQ4EntryName = "RQ^4 Chi Squared";

	/**
	 * Identification number for the component that contains the parameters.
	 */
	public static final int paramsCompId = 1;

	/**
	 * Identification number for the list component that contains all of the
	 * materials.
	 */
	public static final int matListId = 2;

	/**
	 * Identification number for the resource component that contains the output
	 * files.
	 */
	public static final int resourceCompId = 3;

	/**
	 * Identification number for the output data component that displays the chi
	 * squared analysis
	 */
	public static final int outputCompId = 4;

	/**
	 * The constructor.
	 */
	public ReflectivityModel() {
		this(null);
	}

	/**
	 * The constructor with a project space in which files should be handled.
	 * 
	 * @param projectSpace
	 *            The Eclipse project where files should be stored and from
	 *            which they should be retrieved.
	 */
	public ReflectivityModel(IProject projectSpace) {
		// Call super
		super(projectSpace);
	}

	/**
	 * If the action name is ReflectivityModel.processActionName, then
	 * calculates the reflectivity and scattering density profiles for the
	 * material layers and input fields.
	 * 
	 * @see {@link org.eclipse.ice.item.Item#process(String)}
	 */
	@Override
	public FormStatus process(String actionName) {

		// Local Declarations. Return this value to display the process status
		// on the form.
		FormStatus retVal = null;

		if (actionName.equals(processActionName)) {

			// Get the material list from the form.
			ListComponent<Material> matList = (ListComponent<Material>) form
					.getComponent(matListId);
			ArrayList<Slab> slabs = new ArrayList<Slab>();

			// Create the slabs from the materials
			for (Material mat : matList) {
				Slab slab = new Slab();
				slab.thickness = mat.getProperty("Thickness (A)");
				slab.interfaceWidth = mat.getProperty("Roughness (A)");
				slab.scatteringLength = mat
						.getProperty(Material.SCAT_LENGTH_DENSITY);
				slab.trueAbsLength = mat
						.getProperty(Material.MASS_ABS_COHERENT);
				slab.incAbsLength = mat
						.getProperty(Material.MASS_ABS_INCOHERENT);
				slabs.add(slab);
			}

			// Get the roughness from the form.
			int numRough = Integer
					.parseInt(((DataComponent) form.getComponent(paramsCompId))
							.retrieveEntry(RoughnessEntryName).getValue());

			// Get the deltaQ0 from the form.
			double deltaQ0 = Double.parseDouble(
					((DataComponent) form.getComponent(paramsCompId))
							.retrieveEntry(deltaQ0EntryName).getValue());

			// Get the deltaQ0 from the form.
			double deltaQ1ByQ = Double.parseDouble(
					((DataComponent) form.getComponent(paramsCompId))
							.retrieveEntry(deltaQ1ByQEntryName).getValue());

			// Get the wave length from the form.
			double wavelength = Double.parseDouble(
					((DataComponent) form.getComponent(paramsCompId))
							.retrieveEntry(WaveLengthEntryName).getValue());

			// Get the wave vector, r data, and error bars from the file picker
			// in the paramters
			// component.
			double[] waveVector;
			double[] rData;
			double[] error;
			String fileName = ((DataComponent) form.getComponent(paramsCompId))
					.retrieveEntry(WaveEntryName).getValue();

			// Get the file that should have been pulled into the local project.
			IFile userDataFile = project.getFile(fileName);

			// Get the reader and read in the values.
			Form dataForm = new CSVReader().read(userDataFile);
			ListComponent<String[]> userData = (ListComponent<String[]>) dataForm
					.getComponent(1);

			// Pull the data from the form into an array.
			waveVector = new double[userData.size()];
			rData = new double[userData.size()];
			error = new double[userData.size()];
			for (int i = 0; i < userData.size(); i++) {
				String[] dataLine = userData.get(i);
				double waveDataPoint = Double.parseDouble(dataLine[0]);
				waveVector[i] = waveDataPoint;
				double rDataPoint = Double.parseDouble(dataLine[1]);
				rData[i] = rDataPoint;
				double errorDataPoint = Double.parseDouble(dataLine[2]);
				error[i] = errorDataPoint;
			}

			// Calculate the reflectivity - first is regular R calculation
			ReflectivityCalculator calculator = new ReflectivityCalculator();
			ReflectivityProfile profile = calculator.getReflectivityProfile(
					slabs.toArray(new Slab[slabs.size()]), numRough, deltaQ0,
					deltaQ1ByQ, wavelength, waveVector, false);

			// The second profile is for the QR^4 data model
			ReflectivityProfile rq4Profile = calculator.getReflectivityProfile(
					slabs.toArray(new Slab[slabs.size()]), numRough, deltaQ0,
					deltaQ1ByQ, wavelength, waveVector, true);

			// Get the data from the profile
			double[] reflectivity = profile.reflectivity;
			double[] scatDensity = profile.scatteringDensity;
			double[] depth = profile.depth;
			double[] rq4 = rq4Profile.reflectivity;
			double[] rq4Data = new double[rq4.length];

			// Get the chi squared analysis from the data and calculate rq4
			// data.
			double rChiSquare = 0;
			double rq4ChiSquare = 0;
			for (int i = 0; i < reflectivity.length; i++) {

				// Get the data points to compare
				double rPoint = reflectivity[i];
				double rDataPoint = rData[i];
				double rq4Point = rq4[i];
				double rq4DataPoint = rDataPoint * (Math.pow(waveVector[i], 4));

				// Set the rq4 data
				rq4Data[i] = rq4DataPoint;

				// Calculate chi squared which equals sum((o-e)^2/e)
				rChiSquare += (rPoint - rDataPoint) * (rPoint - rDataPoint)
						/ rPoint;
				rq4ChiSquare += (rq4Point - rq4DataPoint)
						* (rq4Point - rq4DataPoint) / rq4Point;
			}

			// Sets the chi squared value in the entry on the output data
			// component
			((DataComponent) form.getComponent(outputCompId))
					.retrieveEntry(ChiSquaredEntryName)
					.setValue(Double.toString(rChiSquare));
			// Sets the rq4 chi squared value in the entry on the output data
			// component
			((DataComponent) form.getComponent(outputCompId))
					.retrieveEntry(ChiSquaredRQ4EntryName)
					.setValue(Double.toString(rq4ChiSquare));

			// Create the csv data for the reflectivity file
			String reflectData = "Q,R,RData,RData_error\n#units,A-1,R,R,R\n";
			for (int i = 0; i < reflectivity.length; i++) {
				reflectData += Double.toString(waveVector[i]) + ", "
						+ Double.toString(reflectivity[i]) + ","
						+ Double.toString(rData[i]) + ","
						+ Double.toString(error[i]) + "\n";
			}

			// Create the stream
			ByteArrayInputStream reflectStream = new ByteArrayInputStream(
					reflectData.getBytes());

			// Create the data for the scattering density profile
			String scatData = "Z,b/V\n#units,A,A-2\n";
			for (int i = 0; i < depth.length; i++) {
				scatData += Double.toString(depth[i]) + ","
						+ Double.toString(scatDensity[i]) + "\n";
			}

			// Create the stream
			ByteArrayInputStream scatStream = new ByteArrayInputStream(
					scatData.getBytes());

			// Create the csv data for the rq4 file
			String rq4DataStr = "Q,R,RData,RData_error\n#units,A-1,R,R,R\n";

			for (int i = 0; i < rq4.length; i++) {
				rq4DataStr += Double.toString(waveVector[i]) + ","
						+ Double.toString(rq4[i]) + ","
						+ Double.toString(rData[i]) + ","
						+ Double.toString(error[i]) + "\n";
			}

			// Create the stream
			ByteArrayInputStream rq4Stream = new ByteArrayInputStream(
					rq4DataStr.getBytes());

			// Get the resource component from the form
			ResourceComponent resources = (ResourceComponent) form
					.getComponent(resourceCompId);

			// Create the new resources to output the data to!
			if (resources.isEmpty()) {
				// Create names with id from the form (should be unique)
				String basename = "reflectivityModel_" + form.getId() + "_";
				// Create the output file for the reflectivity data
				IFile reflectivityFile = project.getFile(basename + "rfd.csv");
				// Create the output file for the scattering density data
				IFile scatteringFile = project.getFile(basename + "scdens.csv");
				// Create the output file for the QR^4 reflectivity data
				IFile rq4File = project.getFile(basename + "rq4.csv");
				try {
					// Reflectivity first
					if (!reflectivityFile.exists()) {
						reflectivityFile.create(reflectStream, true, null);
					}

					// Then the scattering file
					if (!scatteringFile.exists()) {
						scatteringFile.create(scatStream, true, null);
					}

					if (!rq4File.exists()) {
						rq4File.create(rq4Stream, true, null);
					}

					// Create the VizResource to hold the reflectivity data
					VizResource reflectivitySource = new VizResource(
							reflectivityFile.getLocation().toFile());
					reflectivitySource.setName("Reflectivity Data File");
					reflectivitySource.setId(1);
					reflectivitySource.setDescription(
							"Data from reflectivity calculation");

					// Create the VizResource to hold the scatDensity data
					VizResource scatDensitySource = new VizResource(
							scatteringFile.getLocation().toFile());
					scatDensitySource.setName("Scattering Density Data File");
					scatDensitySource.setId(2);
					scatDensitySource.setDescription(
							"Data from Stattering " + "Density calculation");

					// Create the VizResource to hold the RQ4 data
					VizResource rq4Source = new VizResource(
							rq4File.getLocation().toFile());
					rq4Source.setName("RQ^4 Data File");
					rq4Source.setId(3);
					rq4Source.setDescription(
							"Data from RQ^4 reflectivity calculation");

					// Add the resources to the component
					resources.addResource(reflectivitySource);
					resources.addResource(scatDensitySource);
					resources.addResource(rq4Source);
				} catch (CoreException | IOException e) {
					// Complain
					System.err.println("ReflectivityModel Error: "
							+ "Problem creating reflectivity files!");
					logger.error(getClass().getName() + " Exception!", e);
				}

				// Just override the existing files.
			} else {

				// Write the data to the files.
				try {
					// First the reflectivity file
					VizResource reflectSource = (VizResource) resources.get(0);
					IFile reflectivityFile = project
							.getFile(reflectSource.getContents().getName());
					reflectivityFile.setContents(
							new BufferedInputStream(reflectStream), true, false,
							null);

					// Updates the reflectivity viz resource
					reflectSource.setName(reflectSource.getName());

					// Then the scattering density file
					VizResource scatSource = (VizResource) resources.get(1);
					IFile scatteringFile = project
							.getFile(scatSource.getContents().getName());
					scatteringFile.setContents(
							new BufferedInputStream(scatStream), true, false,
							null);

					// Updates the scattering density viz resource
					scatSource.setName(scatSource.getName());

					// Finally the rq4 data file
					VizResource rq4Source = (VizResource) resources.get(2);
					IFile rq4File = project
							.getFile(rq4Source.getContents().getName());
					rq4File.setContents(new BufferedInputStream(rq4Stream),
							true, false, null);

					// Update the rq4 viz resource
					rq4Source.setName(rq4Source.getName());

					// Catch exceptions, should return an error.
				} catch (CoreException | NullPointerException e) {
					System.err.println("Reflectivity Model Error: "
							+ "Problem writing to reflectivity files.");
					logger.error(getClass().getName() + " Exception!", e);
					retVal = FormStatus.InfoError;
				}

			}

			// Return processed if the value has not already been set.
			if (retVal == null) {
				retVal = FormStatus.Processed;
			}

			// Some other process action.
		} else {
			retVal = super.process(actionName);
		}

		// Finally return retVal.
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.Item#setupForm()
	 */
	@Override
	protected void setupForm() {

		// Let the parent setup the Form
		super.setupForm();

		// The data component for the number of rough layers and the
		// input file, along with other user inputs.
		DataComponent paramComponent = new DataComponent();
		paramComponent.setDescription(
				"Give a wave vector, a number of layers of roughness "
						+ "between interfaces, and the angles. ");
		paramComponent.setName("Parameters and Files");
		paramComponent.setId(paramsCompId);
		form.addComponent(paramComponent);

		// Add a file entry for the wave vector file
		Entry fileEntry = new Entry() {
			@Override
			protected void setup() {
				// Only set the allowed value type for this. No other work
				// required.
				allowedValueType = AllowedValueType.File;
				defaultValue = "waveVector.csv";
				return;
			}
		};
		fileEntry.setId(1);
		fileEntry.setName(WaveEntryName);
		fileEntry.setDescription("Wave vector information for this problem.");
		paramComponent.addEntry(fileEntry);

		// Add an entry for the number of layers
		Entry numLayersEntry = new Entry() {
			@Override
			protected void setup() {
				// The number of layers should never be less than one and I
				// imagine that 100 is a good upper limit.
				allowedValueType = AllowedValueType.Continuous;
				allowedValues.add("1");
				allowedValues.add("101");
				defaultValue = "41";
				return;
			}
		};
		numLayersEntry.setId(2);
		numLayersEntry.setName(RoughnessEntryName);
		numLayersEntry.setDescription(
				"Number of layers of roughness per material layer.");
		paramComponent.addEntry(numLayersEntry);

		// Add an entry for the deltaQ0
		Entry deltaQ0Entry = new Entry() {
			@Override
			protected void setup() {
				allowedValueType = AllowedValueType.Continuous;
				allowedValues.add(".000001");
				allowedValues.add("15");
				defaultValue = ".0002";
				return;
			}
		};
		deltaQ0Entry.setId(3);
		deltaQ0Entry.setName(deltaQ0EntryName);
		deltaQ0Entry.setDescription("The incident angle of the neutron beam.");
		paramComponent.addEntry(deltaQ0Entry);

		// Add an entry for the deltaQ1ByQ
		Entry deltaQ1Entry = new Entry() {
			@Override
			protected void setup() {
				allowedValueType = AllowedValueType.Continuous;
				allowedValues.add(".000001");
				allowedValues.add("15");
				defaultValue = ".03";
				return;
			}
		};
		deltaQ1Entry.setId(4);
		deltaQ1Entry.setName(deltaQ1ByQEntryName);
		deltaQ1Entry
				.setDescription("The angle of refraction on the neutron beam.");
		paramComponent.addEntry(deltaQ1Entry);

		// Add an entry for the wavelength
		Entry waveEntry = new Entry() {
			@Override
			protected void setup() {
				allowedValueType = AllowedValueType.Continuous;
				allowedValues.add(".000001");
				allowedValues.add("1000");
				defaultValue = "4.25";
				return;
			}
		};
		waveEntry.setId(5);
		waveEntry.setName(WaveLengthEntryName);
		waveEntry.setDescription("The wavelength of the neutron beam.");
		paramComponent.addEntry(waveEntry);

		// Configure a list of property names for the materials
		ArrayList<String> names = new ArrayList<String>();
		names.add("Material ID");
		names.add("Thickness (A)");
		names.add("Roughness (A)");
		names.add(Material.SCAT_LENGTH_DENSITY);
		names.add(Material.MASS_ABS_COHERENT);
		names.add(Material.MASS_ABS_INCOHERENT);
		// Create the writable format to be used by the list
		MaterialWritableTableFormat format = new MaterialWritableTableFormat(
				names);

		// Create the list that will contain all of the material information
		ListComponent<Material> matList = new ListComponent<Material>();
		matList.setId(matListId);
		matList.setName("Reflectivity Input Data");
		matList.setDescription("Reflectivity Input Data");
		matList.setTableFormat(format);

		// Create a default list of materials for now. This is TEMPORARY, I
		// imagine.
		fillMaterialList(matList);

		// Make sure to put it in the form!
		form.addComponent(matList);

		// Create a component to hold the output
		ResourceComponent resources = new ResourceComponent();
		resources.setName("Results");
		resources.setDescription("Results and Output");
		resources.setId(resourceCompId);
		form.addComponent(resources);

		// Create the output data component to hold the chi squared analysis
		DataComponent output = new DataComponent();
		output.setDescription("Chi squared analysis:");
		output.setName("Output");
		output.setId(outputCompId);

		// Add an entry for the wavelength
		Entry chiSquared = new Entry();
		chiSquared.setId(1);
		chiSquared.setName(ChiSquaredEntryName);
		chiSquared.setDescription(
				"The chi squared analysis for the reflectivity profile.");
		output.addEntry(chiSquared);

		// Add an entry for the wavelength
		Entry chiSquaredrq4 = new Entry();
		chiSquaredrq4.setId(2);
		chiSquaredrq4.setName(ChiSquaredRQ4EntryName);
		chiSquaredrq4.setDescription(
				"The chi squared analysis for the rq^4 reflectivity profile.");
		output.addEntry(chiSquaredrq4);

		form.addComponent(output);

		// Put the action name in the form so that the reflectivity can be
		// calculated.
		allowedActions.add(0, processActionName);

		return;
	}

	/**
	 * This operation fills the material list with a default set of materials so
	 * that the Item is immediately valid and can be processed.
	 * 
	 * @param matList
	 *            the list of Materials that represents the system. One material
	 *            per layer.
	 */
	private void fillMaterialList(ListComponent<Material> matList) {

		// Local Declarations
		Material material = null;

		// Create the slabs that define the system, starting with air
		Slab air = new Slab();
		air.thickness = 200.0;
		matList.add(convertSlabToMaterial(air, "Air", 1));

		// NiOx
		Slab niOx = new Slab();
		niOx.scatteringLength = (0.00000686 + 0.00000715) / 2.0;
		niOx.trueAbsLength = 2.27931868269305E-09;
		niOx.incAbsLength = 4.74626235093697E-09;
		niOx.thickness = 22.0;
		niOx.interfaceWidth = 4.0 * 2.35;
		matList.add(convertSlabToMaterial(niOx, "NiOx", 1));

		// Ni
		Slab ni = new Slab();
		ni.scatteringLength = 9.31e-6;
		ni.trueAbsLength = 2.27931868269305E-09;
		ni.incAbsLength = 4.74626235093697E-09;
		ni.thickness = 551.0;
		ni.interfaceWidth = 4.3 * 2.35;
		matList.add(convertSlabToMaterial(ni, "Ni", 1));

		// SiNiOx
		Slab siNiOx = new Slab();
		siNiOx.scatteringLength = (0.00000554 + 0.00000585) / 2.0;
		siNiOx.trueAbsLength = 2.27931868269305E-09;
		siNiOx.incAbsLength = 4.74626235093697E-09;
		siNiOx.thickness = 42.0;
		siNiOx.interfaceWidth = 7.0 * 2.35;
		matList.add(convertSlabToMaterial(siNiOx, "SiNiOx", 1));

		// SiOx
		Slab si = new Slab();
		si.scatteringLength = 2.070e-6;
		si.trueAbsLength = 4.74981478870069E-11;
		si.incAbsLength = 1.99769988072137E-12;
		si.thickness = 100.0;
		si.interfaceWidth = 17.5;
		matList.add(convertSlabToMaterial(si, "Si", 1));

		return;
	}

	/**
	 * This operation create a Material based on a Slab
	 * 
	 * @param slab
	 *            the slab
	 * @param name
	 *            the name of the material
	 * @param id
	 *            the material id
	 * @return the new material created from the slab
	 */
	private Material convertSlabToMaterial(Slab slab, String name, int id) {
		// Just add each property of the slab to the list of properties of the
		// material.
		Material material = new Material();
		material.setName(name);
		material.setProperty("Material ID", id);
		material.setProperty("Thickness (A)", slab.thickness);
		material.setProperty("Roughness (A)", slab.interfaceWidth);
		material.setProperty(Material.SCAT_LENGTH_DENSITY,
				slab.scatteringLength);
		material.setProperty(Material.MASS_ABS_COHERENT, slab.trueAbsLength);
		material.setProperty(Material.MASS_ABS_INCOHERENT, slab.incAbsLength);
		return material;
	}

	/**
	 * Gives this item a name, description, and item type. Also sets the builder
	 * name. The name should be the same as the ReflectivityModelBuilder name.
	 * 
	 * @see org.eclipse.ice.item.Item#setupItemInfo()
	 */
	@Override
	protected void setupItemInfo() {

		// Local Declarations
		String desc = "This item builds models for " + "Reflectivity.";

		// Describe the Item
		setName(ReflectivityModelBuilder.name);
		setItemBuilderName(ReflectivityModelBuilder.name);
		setDescription(desc);
		itemType = ReflectivityModelBuilder.type;
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.item.Item#submitForm(org.eclipse.ice.datastructures.form.
	 * Form)
	 */
	@Override
	public FormStatus submitForm(Form preparedForm) {
		// Get the form status from the model
		FormStatus status = super.submitForm(preparedForm);
		// If it is good, process
		if (status.equals(FormStatus.ReadyToProcess)) {
			// Get the status from processing and process the item
			status = process(processActionName);
		}
		// Return the status
		return status;
	}

	/**
	 * Sets up the form with the basic services needed for the reflectivity
	 * model. Namely, sets the materials database and the table format for the
	 * list component in the model.
	 * 
	 * @see org.eclipse.ice.item.Item#setupFormWithServices()
	 */
	@Override
	public void setupFormWithServices() {

		// Grab the component
		ListComponent<Material> matList = (ListComponent<Material>) form
				.getComponent(matListId);

		// Configure a list of property names for the materials
		ArrayList<String> names = new ArrayList<String>();
		names.add("Material ID");
		names.add("Thickness (A)");
		names.add("Roughness (A)");
		names.add(Material.SCAT_LENGTH_DENSITY);
		names.add(Material.MASS_ABS_COHERENT);
		names.add(Material.MASS_ABS_INCOHERENT);
		// Create the writable format to be used by the list
		MaterialWritableTableFormat format = new MaterialWritableTableFormat(
				names);

		// Set the table format
		matList.setTableFormat(format);

		// If the materials database is available, register it as the element
		// provider for the list component of materials on the Form.
		IMaterialsDatabase database = getMaterialsDatabase();
		if (database != null) {
			// Set the database as an element source
			matList.setElementSource(database);
		}

		return;
	}

}
