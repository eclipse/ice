/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.visitanalysistool;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.analysistool.IAnalysisAsset;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import llnl.visit.SaveWindowAttributes;

import java.io.File;
import java.net.URI;

import llnl.visit.AnnotationAttributes;
import llnl.visit.View3DAttributes;
import llnl.visit.ViewerMethods;
import llnl.visit.ViewerState;
import llnl.visit.ViewerProxy;
import llnl.visit.avtDatabaseMetaData;
import llnl.visit.avtMeshMetaData;
import llnl.visit.plots.PseudocolorAttributes;
import org.eclipse.ice.analysistool.AnalysisAssetType;
import java.util.Properties;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class implements the IAnalysisAsset interface for use with VisIt.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author els
 * @!generated 
 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisItAnalysisAsset implements IAnalysisAsset {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A list of available VisIt color table names.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> availableColorTables;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A list of available mesh names.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> availableMeshes;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An instance of the VisIt Java API's avtDatabaseMetaData class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private avtDatabaseMetaData databaseMetaData;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A HashTable keyed on VisItAnalysisPictureProperty literals and their
	 * associated values as Strings.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Hashtable<VisItAnalysisPictureProperty, String> defaultPropertyTable;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the asset.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String name;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A HashTable of properties and their values as Strings. If this asset's
	 * type is AnalysisAssetType.PICTURE, then this HashTable is keyed on
	 * VisItAnalysisPictureProperty literals.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Hashtable<VisItAnalysisPictureProperty, String> propertyTable;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The AnalysisAssetType literal for this asset.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private AnalysisAssetType type;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The absolute path to this asset's output.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private URI uri;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An instance of the VisIt Java API's ViewerMethods class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ViewerMethods viewerMethods;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An instance of the VisIt Java API's ViewerProxy class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ViewerProxy viewerProxy;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An instance of the VisIt Java API's ViewerState class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ViewerState viewerState;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 *            <p>
	 *            The data URI of the parent VisItAnalysisDocument.
	 *            </p>
	 * @param databaseMetaData
	 *            <p>
	 *            An instance of the VisIt Java API's avtDatabaseMetaData class.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of this asset.
	 *            </p>
	 * @param sliceNumber
	 *            <p>
	 *            The slice number of this asset.
	 *            </p>
	 * @param viewerProxy
	 *            <p>
	 *            The ViewerProxy used by this asset to generate output.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public VisItAnalysisAsset(URI data, avtDatabaseMetaData databaseMetaData,
			String name, Integer sliceNumber, ViewerProxy viewerProxy) {
		// begin-user-code

		// Assign values to instance variables
		this.databaseMetaData = databaseMetaData;
		this.name = name;
		this.viewerProxy = viewerProxy;
		this.viewerState = viewerProxy.GetViewerState();
		this.viewerMethods = viewerProxy.GetViewerMethods();

		// Set the type to PICTURE
		type = AnalysisAssetType.PICTURE;

		// Initialize the available color table list
		initializeAvailableColorTables();

		// Initialize the available mesh list
		initializeAvailableMeshes();

		// Initialize the propertyTable to default values queried from VisIt
		initializeDefaultProperties();

		// Create a new property table
		propertyTable = new Hashtable<VisItAnalysisPictureProperty, String>();

		// Place all of the keys and values from the default table to the real
		// one
		propertyTable.putAll(defaultPropertyTable);

		// Create and assign the uri for this asset
		createUri(data, sliceNumber);

		// Create this asset and save it to disk
		createAsset();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns a list of available color tables. These names are used as values
	 * for the VisItPictureProperty COLORTABLE literal.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A list of available mesh names.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getAvailableColorTables() {
		// begin-user-code
		return availableColorTables;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns a list of available mesh names. These names are used as values
	 * for the VisItPictureProperty MESH_NAME literal.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A list of available mesh names.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getAvailableMeshes() {
		// begin-user-code
		return availableMeshes;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Creates this asset and writes it to a file.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void createAsset() {
		// begin-user-code

		// Delete all other active plots
		viewerMethods.DeleteActivePlots();

		// Create a new plot
		viewerMethods.AddPlot("Pseudocolor", name);

		// Get an iterator to the property table
		Iterator<VisItAnalysisPictureProperty> itr = propertyTable.keySet()
				.iterator();

		// Local declarations
		SaveWindowAttributes saveWindowAttributes = viewerState
				.GetSaveWindowAttributes();
		View3DAttributes view3DAttributes = viewerState.GetView3DAttributes();
		AnnotationAttributes annotationAttributes = viewerState
				.GetAnnotationAttributes();
		PseudocolorAttributes pseudocolorAttributes = (PseudocolorAttributes) viewerProxy
				.GetPlotAttributes("Pseudocolor");

		// Loop through iterator until finished
		while (itr.hasNext()) {

			// Get the current property
			VisItAnalysisPictureProperty property = itr.next();

			// Get the current property's value
			String value = propertyTable.get(property);

			// Cycle over all properties and set them in visit
			switch (property) {

			case COLORTABLE:
				if (availableColorTables.contains(value)) {
					pseudocolorAttributes.SetColorTableName(value);
				}
				break;
			case DATABASE_LABEL_TYPE:
				if (value.equalsIgnoreCase("File")) {
					annotationAttributes
							.SetDatabaseInfoExpansionMode(AnnotationAttributes.PATHEXPANSIONMODE_FILE);
				} else if (value.equalsIgnoreCase("Directory")) {
					annotationAttributes
							.SetDatabaseInfoExpansionMode(AnnotationAttributes.PATHEXPANSIONMODE_DIRECTORY);
				} else if (value.equalsIgnoreCase("Full")) {
					annotationAttributes
							.SetDatabaseInfoExpansionMode(AnnotationAttributes.PATHEXPANSIONMODE_FULL);
				}
				break;
			case IMAGE_WIDTH:
				saveWindowAttributes.SetWidth(Integer.valueOf(value));
				break;
			case IMAGE_HEIGHT:
				saveWindowAttributes.SetHeight(Integer.valueOf(value));
				break;
			case INVERT_COLORTABLE:
				if (value.equalsIgnoreCase("true")
						|| value.equalsIgnoreCase("false")) {
					pseudocolorAttributes.SetInvertColorTable(Boolean
							.valueOf(value));
				}
				break;
			case PAN_X:
				double[] imagePanX = new double[2];
				imagePanX[0] = Double.valueOf(value);
				imagePanX[1] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.PAN_Y));
				view3DAttributes.SetImagePan(imagePanX);
				break;
			case PAN_Y:
				double[] imagePanY = new double[2];
				imagePanY[0] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.PAN_X));
				imagePanY[1] = Double.valueOf(value);
				view3DAttributes.SetImagePan(imagePanY);
				break;
			case SCALE_MIN:
				if ("".equals(value)) {
					pseudocolorAttributes.SetMinFlag(false);
				} else {
					pseudocolorAttributes.SetMinFlag(true);
					pseudocolorAttributes.SetMin(Double.valueOf(value));
				}
				break;
			case SCALE_MAX:
				if ("".equals(value)) {
					pseudocolorAttributes.SetMaxFlag(false);
				} else {
					pseudocolorAttributes.SetMaxFlag(true);
					pseudocolorAttributes.SetMax(Double.valueOf(value));
				}
				break;
			case SCALE_SKEW_FACTOR:
				pseudocolorAttributes.SetSkewFactor(Double.valueOf(value));
				break;
			case SCALE_TYPE:
				if (value.equalsIgnoreCase("Lin")) {
					pseudocolorAttributes
							.SetScaling(PseudocolorAttributes.SCALING_LINEAR);
				} else if (value.equalsIgnoreCase("Log")) {
					pseudocolorAttributes
							.SetScaling(PseudocolorAttributes.SCALING_LOG);
				} else if (value.equalsIgnoreCase("Skew")) {
					pseudocolorAttributes
							.SetScaling(PseudocolorAttributes.SCALING_SKEW);
				}
				break;
			case SHOW_AXES:
				if (value.equalsIgnoreCase("true")
						|| value.equalsIgnoreCase("false")) {
					annotationAttributes.GetAxes3D().SetVisible(
							Boolean.valueOf(value));
				}
				break;
			case SHOW_BOUNDING_BOX:
				if (value.equalsIgnoreCase("true")
						|| value.equalsIgnoreCase("false")) {
					annotationAttributes.GetAxes3D().SetBboxFlag(
							Boolean.valueOf(value));
				}
				break;
			case SHOW_DATABASE_LABEL:
				if (value.equalsIgnoreCase("true")
						|| value.equalsIgnoreCase("false")) {
					annotationAttributes.SetDatabaseInfoFlag(Boolean
							.valueOf(value));
				}
				break;
			case SHOW_DATE_AND_USER:
				if (value.equalsIgnoreCase("true")
						|| value.equalsIgnoreCase("false")) {
					annotationAttributes
							.SetUserInfoFlag(Boolean.valueOf(value));
				}
				break;
			case SHOW_LEGEND:
				if (value.equalsIgnoreCase("true")
						|| value.equalsIgnoreCase("false")) {
					annotationAttributes.SetLegendInfoFlag(Boolean
							.valueOf(value));
				}
				break;
			case SHOW_MESH:
				if (value.equalsIgnoreCase("true")
						&& !propertyTable.get(
								VisItAnalysisPictureProperty.MESH_NAME).equals(
								"")
						&& availableMeshes.contains(propertyTable
								.get(VisItAnalysisPictureProperty.MESH_NAME))) {
					viewerMethods.AddPlot("Mesh", propertyTable
							.get(VisItAnalysisPictureProperty.MESH_NAME));
				}
				break;
			case SHOW_TRIAD:
				if (value.equalsIgnoreCase("true")
						|| value.equalsIgnoreCase("false")) {
					annotationAttributes.GetAxes3D().SetTriadFlag(
							Boolean.valueOf(value));
				}
				break;
			case VIEW_ANGLE:
				view3DAttributes.SetViewAngle(Double.valueOf(value));
				break;
			case VIEW_NORMAL_X:
				double[] viewNormalX = new double[3];
				viewNormalX[0] = Double.valueOf(value);
				viewNormalX[1] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_NORMAL_Y));
				viewNormalX[2] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_NORMAL_Z));
				view3DAttributes.SetViewNormal(viewNormalX);
				break;
			case VIEW_NORMAL_Y:
				double[] viewNormalY = new double[3];
				viewNormalY[0] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_NORMAL_X));
				viewNormalY[1] = Double.valueOf(value);
				viewNormalY[2] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_NORMAL_Z));
				view3DAttributes.SetViewNormal(viewNormalY);
				break;
			case VIEW_NORMAL_Z:
				double[] viewNormalZ = new double[3];
				viewNormalZ[0] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_NORMAL_X));
				viewNormalZ[1] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_NORMAL_Y));
				viewNormalZ[2] = Double.valueOf(value);
				view3DAttributes.SetViewNormal(viewNormalZ);
				break;
			case VIEW_UP_X:
				double[] viewUpX = new double[3];
				viewUpX[0] = Double.valueOf(value);
				viewUpX[1] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_UP_Y));
				viewUpX[2] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_UP_Z));
				view3DAttributes.SetViewUp(viewUpX);
				break;
			case VIEW_UP_Y:
				double[] viewUpY = new double[3];
				viewUpY[0] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_UP_X));
				viewUpY[1] = Double.valueOf(value);
				viewUpY[2] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_UP_Z));
				view3DAttributes.SetViewUp(viewUpY);
				break;
			case VIEW_UP_Z:
				double[] viewUpZ = new double[3];
				viewUpZ[0] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_UP_X));
				viewUpZ[1] = Double.valueOf(propertyTable
						.get(VisItAnalysisPictureProperty.VIEW_UP_Y));
				viewUpZ[2] = Double.valueOf(value);
				view3DAttributes.SetViewUp(viewUpZ);
				break;
			case X_AXIS_LABEL:
				annotationAttributes.GetAxes3D().GetXAxis().GetTitle()
						.SetUserTitle(true);
				annotationAttributes.GetAxes3D().GetXAxis().GetTitle()
						.SetTitle(value);
				break;
			case Y_AXIS_LABEL:
				annotationAttributes.GetAxes3D().GetYAxis().GetTitle()
						.SetUserTitle(true);
				annotationAttributes.GetAxes3D().GetYAxis().GetTitle()
						.SetTitle(value);
				break;
			case Z_AXIS_LABEL:
				annotationAttributes.GetAxes3D().GetZAxis().GetTitle()
						.SetUserTitle(true);
				annotationAttributes.GetAxes3D().GetZAxis().GetTitle()
						.SetTitle(value);
				break;
			case X_AXIS_UNITS:
				annotationAttributes.GetAxes3D().GetXAxis().GetTitle()
						.SetUserUnits(true);
				annotationAttributes.GetAxes3D().GetXAxis().GetTitle()
						.SetUnits(value);
				break;
			case Y_AXIS_UNITS:
				annotationAttributes.GetAxes3D().GetYAxis().GetTitle()
						.SetUserUnits(true);
				annotationAttributes.GetAxes3D().GetYAxis().GetTitle()
						.SetUnits(value);
				break;
			case Z_AXIS_UNITS:
				annotationAttributes.GetAxes3D().GetZAxis().GetTitle()
						.SetUserUnits(true);
				annotationAttributes.GetAxes3D().GetZAxis().GetTitle()
						.SetUnits(value);
				break;
			case ZOOM_LEVEL:
				view3DAttributes.SetImageZoom(Double.valueOf(value));
				break;

			}

		}

		// We must set the size of the window to the size of the image
		viewerMethods.ResizeWindow(1, saveWindowAttributes.GetWidth(),
				saveWindowAttributes.GetHeight());

		// Set family to false in order to keep VisIt from assigning the
		// filename automatically
		saveWindowAttributes.SetFamily(false);

		// Set screen capture to true in order maintain the correct lighting and
		// shading
		saveWindowAttributes.SetScreenCapture(true);

		// Set the format to PNG
		saveWindowAttributes.SetFormat(SaveWindowAttributes.FILEFORMAT_PNG);

		// Disable output to the current working directory so we can set the
		// output directory ourselves
		saveWindowAttributes.SetOutputToCurrentDirectory(false);

		// Set the output directory to the path of the URI
		saveWindowAttributes.SetOutputDirectory(new File(uri.getPath())
				.getParent());

		// Set the filename to the uri's filename
		saveWindowAttributes.SetFileName(new File(uri.getPath()).getName());

		// Notify the viewerProxy that these settings have been changed
		saveWindowAttributes.Notify();
		view3DAttributes.Notify();
		annotationAttributes.Notify();
		pseudocolorAttributes.Notify();

		// Apply the changes to the view
		viewerMethods.SetView3D();
		viewerMethods.SetAnnotationAttributes();
		viewerMethods.SetPlotOptions("Pseudocolor");

		// We are done manipulating the image so lets redraw it
		viewerMethods.DrawPlots();

		// Save the image to disk
		viewerMethods.SaveWindow();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Creates the URI of the asset's output.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 *            <p>
	 *            The data URI of the parent VisItAnalysisDocument.
	 *            </p>
	 * @param sliceNumber
	 *            <p>
	 *            The slice number of this asset.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void createUri(URI data, int sliceNumber) {
		// begin-user-code

		// Get the parentUri's directory
		String parentDir = new File(data.getPath()).getParent();

		// Get the parent's filename
		String parentFilename = new File(data.getPath()).getName();

		// Strip off the extension if there is one
		String parentFilenameWithoutExtension = parentFilename;
		if (parentFilename.indexOf(".") != -1) {
			parentFilenameWithoutExtension = parentFilename.substring(0,
					parentFilename.lastIndexOf("."));
		}

		// Convert characters in name
		String convertedName = name.replaceAll("/", "-").replaceAll(" ", "_");

		// Create the asset's filename
		String assetFilename = parentFilenameWithoutExtension + "-"
				+ convertedName + "-" + sliceNumber + ".png";

		// Create the asset's uri
		String separator = System.getProperty("file.separator");
		File assetFile = new File(parentDir + separator + assetFilename);
		uri = assetFile.toURI();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Queries VisIt for the default value for the provided property.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param property
	 *            <p>
	 *            A String representing a property.
	 *            </p>
	 * @return <p>
	 *         The default value for this property.
	 *         </p>
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String getDefaultPropertyValue(VisItAnalysisPictureProperty property) {
		// begin-user-code

		// Local declarations
		SaveWindowAttributes saveWindowAttributes = viewerState
				.GetSaveWindowAttributes();
		View3DAttributes view3DAttributes = viewerState.GetView3DAttributes();
		AnnotationAttributes annotationAttributes = viewerState
				.GetAnnotationAttributes();
		PseudocolorAttributes pseudocolorAttributes = (PseudocolorAttributes) viewerProxy
				.GetPlotAttributes("Pseudocolor");

		// Declare a string to hold the value
		String value = "";

		// Switch on the provided property and extract the default value
		switch (property) {

		case COLORTABLE:
			value = pseudocolorAttributes.GetColorTableName();
			break;
		case DATABASE_LABEL_TYPE:
			switch (annotationAttributes.GetDatabaseInfoExpansionMode()) {
			case AnnotationAttributes.PATHEXPANSIONMODE_FILE:
				value = "File";
				break;
			case AnnotationAttributes.PATHEXPANSIONMODE_DIRECTORY:
				value = "Directory";
				break;
			case AnnotationAttributes.PATHEXPANSIONMODE_FULL:
				value = "Full";
				break;
			}
			break;
		case IMAGE_WIDTH:
			value = String.valueOf(saveWindowAttributes.GetWidth());
			break;
		case IMAGE_HEIGHT:
			value = String.valueOf(saveWindowAttributes.GetHeight());
			break;
		case INVERT_COLORTABLE:
			value = pseudocolorAttributes.GetInvertColorTable() ? "true"
					: "false";
			break;
		case MESH_NAME:
			value = "";
			break;
		case PAN_X:
			value = String.valueOf(view3DAttributes.GetImagePan()[0]);
			break;
		case PAN_Y:
			value = String.valueOf(view3DAttributes.GetImagePan()[1]);
			break;
		case SCALE_MIN:
			if (pseudocolorAttributes.GetMinFlag()) {
				value = String.valueOf(pseudocolorAttributes.GetMin());
			} else {
				value = "";
			}
			break;
		case SCALE_MAX:
			if (pseudocolorAttributes.GetMaxFlag()) {
				value = String.valueOf(pseudocolorAttributes.GetMax());
			} else {
				value = "";
			}
			break;
		case SCALE_SKEW_FACTOR:
			value = String.valueOf(pseudocolorAttributes.GetSkewFactor());
			break;
		case SCALE_TYPE:
			switch (pseudocolorAttributes.GetScaling()) {
			case PseudocolorAttributes.SCALING_LINEAR:
				value = "Lin";
				break;
			case PseudocolorAttributes.SCALING_LOG:
				value = "Log";
				break;
			case PseudocolorAttributes.SCALING_SKEW:
				value = "Skew";
				break;
			}
			break;
		case SHOW_AXES:
			value = annotationAttributes.GetAxes3D().GetVisible() ? "true"
					: "false";
			break;
		case SHOW_BOUNDING_BOX:
			value = annotationAttributes.GetAxes3D().GetBboxFlag() ? "true"
					: "false";
			break;
		case SHOW_DATABASE_LABEL:
			value = annotationAttributes.GetDatabaseInfoFlag() ? "true"
					: "false";
			break;
		case SHOW_DATE_AND_USER:
			value = annotationAttributes.GetUserInfoFlag() ? "true" : "false";
			break;
		case SHOW_LEGEND:
			value = annotationAttributes.GetLegendInfoFlag() ? "true" : "false";
			break;
		case SHOW_MESH:
			value = "false";
			break;
		case SHOW_TRIAD:
			value = annotationAttributes.GetAxes3D().GetTriadFlag() ? "true"
					: "false";
			break;
		case VIEW_ANGLE:
			value = String.valueOf(view3DAttributes.GetViewAngle());
			break;
		case VIEW_NORMAL_X:
			value = String.valueOf(view3DAttributes.GetViewNormal()[0]);
			break;
		case VIEW_NORMAL_Y:
			value = String.valueOf(view3DAttributes.GetViewNormal()[1]);
			break;
		case VIEW_NORMAL_Z:
			value = String.valueOf(view3DAttributes.GetViewNormal()[2]);
			break;
		case VIEW_UP_X:
			value = String.valueOf(view3DAttributes.GetViewUp()[0]);
			break;
		case VIEW_UP_Y:
			value = String.valueOf(view3DAttributes.GetViewUp()[1]);
			break;
		case VIEW_UP_Z:
			value = String.valueOf(view3DAttributes.GetViewUp()[2]);
			break;
		case X_AXIS_LABEL:
			value = annotationAttributes.GetAxes3D().GetXAxis().GetTitle()
					.GetTitle();
			break;
		case Y_AXIS_LABEL:
			value = annotationAttributes.GetAxes3D().GetYAxis().GetTitle()
					.GetTitle();
			break;
		case Z_AXIS_LABEL:
			value = annotationAttributes.GetAxes3D().GetZAxis().GetTitle()
					.GetTitle();
			break;
		case X_AXIS_UNITS:
			value = annotationAttributes.GetAxes3D().GetXAxis().GetTitle()
					.GetUnits();
			break;
		case Y_AXIS_UNITS:
			value = annotationAttributes.GetAxes3D().GetYAxis().GetTitle()
					.GetUnits();
			break;
		case Z_AXIS_UNITS:
			value = annotationAttributes.GetAxes3D().GetZAxis().GetTitle()
					.GetUnits();
			break;
		case ZOOM_LEVEL:
			value = String.valueOf(view3DAttributes.GetImageZoom());
			break;

		}

		// Return the default value
		return value;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns a list of available VisIt color table names. These names are used
	 * as values for the VisItPictureProperty COLORTABLE literal.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A list of available VisIt color table names.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void initializeAvailableColorTables() {
		// begin-user-code

		// Create a list to hold the color table names
		availableColorTables = new ArrayList<String>();

		// Get the names as a vector from VisIt
		Vector<String> names = viewerState.GetColorTableAttributes().GetNames();

		// Iterate over names and add elements to colorTables
		Iterator<String> itr = names.iterator();
		while (itr.hasNext()) {
			availableColorTables.add(itr.next());
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes the available meshes ArrayList. These names are used as
	 * values for the VisItPictureProperty MESH_NAME literal.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void initializeAvailableMeshes() {
		// begin-user-code

		// Create a new list to hold the meshes
		availableMeshes = new ArrayList<String>();

		// Get the vector of meshes from the database metadata
		Vector<avtMeshMetaData> meshVector = databaseMetaData.GetMeshes();

		// If the vector is not null
		if (meshVector != null) {

			// Get an iterator
			Iterator<avtMeshMetaData> itr = meshVector.iterator();

			// While the iterator has more elements
			while (itr.hasNext()) {

				// Add the mesh's name to the list
				availableMeshes.add(itr.next().GetName());

			}

		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Creates and initializes the propertyTable for this asset with default
	 * values.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void initializeDefaultProperties() {
		// begin-user-code

		// Delete all other active plots
		viewerMethods.DeleteActivePlots();

		// Create a new plot to extract default properties
		viewerMethods.AddPlot("Pseudocolor", name);

		// Create a new property table
		defaultPropertyTable = new Hashtable<VisItAnalysisPictureProperty, String>();

		// Cycle over all VisItAnalysisPictureProperty properties
		for (VisItAnalysisPictureProperty p : VisItAnalysisPictureProperty
				.values()) {

			// Get the default value for this property
			String value = getDefaultPropertyValue(p);

			// Put the key/value pair into the table
			defaultPropertyTable.put(p, value);

		}

		// Delete all other active plots
		viewerMethods.DeleteActivePlots();

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getName()
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName() {
		// begin-user-code
		return name;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getType()
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public AnalysisAssetType getType() {
		// begin-user-code
		return type;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getProperty(String key)
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getProperty(String key) {
		// begin-user-code

		// Validate key and check for keys in propertyMap
		if (VisItAnalysisPictureProperty.toProperty(key) == null
				|| !propertyTable.containsKey(VisItAnalysisPictureProperty
						.toProperty(key))) {
			return null;
		}

		// Return the property
		return propertyTable.get(VisItAnalysisPictureProperty.toProperty(key));
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#setProperty(String key, String value)
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setProperty(String key, String value) {
		// begin-user-code

		// Validate key and value
		if (key == null || "".equals(key) || value == null
				|| VisItAnalysisPictureProperty.toProperty(key) == null) {
			return false;
		}

		// Put the value into the property table
		propertyTable.put(VisItAnalysisPictureProperty.toProperty(key), value);

		// Create this asset
		createAsset();

		// Return true
		return true;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#resetProperties()
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void resetProperties() {
		// begin-user-code

		// Place all of the keys and values from the default table to the real
		// one
		propertyTable.putAll(defaultPropertyTable);

		// Create this asset and save it to disk
		createAsset();

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getProperties()
	 * @!generated 
	 *             "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Properties getProperties() {
		// begin-user-code

		// Create a Properties instance
		Properties properties = new Properties();

		// If the current property table is null
		if (propertyTable == null) {

			// Then return null
			return null;
		}

		// Get an iterator from the table
		Iterator<VisItAnalysisPictureProperty> itr = propertyTable.keySet()
				.iterator();

		// Loop over all values in the table
		while (itr.hasNext()) {

			// Get the next property
			VisItAnalysisPictureProperty property = itr.next();

			// Get the property's value
			String value = propertyTable.get(property);

			// Put the property's string value and the property value into the
			// properties instance
			properties.put(property.toString(), value);

		}

		// Return the properties object
		return properties;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getURI()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public URI getURI() {
		// begin-user-code
		return uri;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getPropertiesAsEntryList()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Entry> getPropertiesAsEntryList() {

		// Create a new properties list
		ArrayList<Entry> properties = new ArrayList<Entry>();

		// Set up an id counter
		int id = 0;

		// Loop over all enumerations
		for (final VisItAnalysisPictureProperty p : VisItAnalysisPictureProperty
				.values()) {

			// Create a new Entry
			Entry e = new Entry() {

				// Override setup
				@Override
				protected void setup() {

					// Set the default value
					defaultValue = defaultPropertyTable.get(p);

					// Set the allowed values and allowed value type where
					// appropriate
					switch (p) {

					case COLORTABLE:
						allowedValues = VisItAnalysisAsset.this
								.getAvailableColorTables();
						allowedValueType = AllowedValueType.Discrete;
						break;
					case DATABASE_LABEL_TYPE:
						allowedValues = new ArrayList<String>();
						allowedValues.add("File");
						allowedValues.add("Directory");
						allowedValues.add("Full");
						allowedValueType = AllowedValueType.Discrete;
						break;
					case IMAGE_WIDTH:
						allowedValueType = AllowedValueType.Continuous;
						allowedValues = new ArrayList<String>();
						allowedValues.add("0");
						allowedValues.add(String.valueOf(Integer.MAX_VALUE));
						break;
					case IMAGE_HEIGHT:
						allowedValueType = AllowedValueType.Continuous;
						allowedValues = new ArrayList<String>();
						allowedValues.add("0");
						allowedValues.add(String.valueOf(Integer.MAX_VALUE));
						break;
					case INVERT_COLORTABLE:
						allowedValues = new ArrayList<String>();
						allowedValues.add("true");
						allowedValues.add("false");
						allowedValueType = AllowedValueType.Discrete;
						break;
					case MESH_NAME:
						allowedValues = VisItAnalysisAsset.this
								.getAvailableMeshes();
						allowedValueType = AllowedValueType.Discrete;
						break;
					case PAN_X:
						allowedValues = new ArrayList<String>();
						allowedValues.add(String.valueOf(Integer.MIN_VALUE));
						allowedValues.add(String.valueOf(Integer.MAX_VALUE));
						allowedValueType = AllowedValueType.Continuous;
						break;
					case PAN_Y:
						allowedValues = new ArrayList<String>();
						allowedValues.add(String.valueOf(Integer.MIN_VALUE));
						allowedValues.add(String.valueOf(Integer.MAX_VALUE));
						allowedValueType = AllowedValueType.Continuous;
						break;
					case SCALE_MIN:
						allowedValues = new ArrayList<String>();
						allowedValues.add(String.valueOf(Integer.MIN_VALUE));
						allowedValues.add(String.valueOf(Integer.MAX_VALUE));
						allowedValueType = AllowedValueType.Continuous;
						break;
					case SCALE_MAX:
						allowedValues = new ArrayList<String>();
						allowedValues.add(String.valueOf(Integer.MIN_VALUE));
						allowedValues.add(String.valueOf(Integer.MAX_VALUE));
						allowedValueType = AllowedValueType.Continuous;
						break;
					case SCALE_SKEW_FACTOR:
						allowedValues = new ArrayList<String>();
						allowedValues.add(String.valueOf(Integer.MIN_VALUE));
						allowedValues.add(String.valueOf(Integer.MAX_VALUE));
						allowedValueType = AllowedValueType.Continuous;
						break;
					case SCALE_TYPE:
						allowedValues = new ArrayList<String>();
						allowedValues.add("Log");
						allowedValues.add("Lin");
						allowedValues.add("Skew");
						allowedValueType = AllowedValueType.Discrete;
						break;
					case SHOW_AXES:
						allowedValues = new ArrayList<String>();
						allowedValues.add("true");
						allowedValues.add("false");
						allowedValueType = AllowedValueType.Discrete;
						break;
					case SHOW_BOUNDING_BOX:
						allowedValues = new ArrayList<String>();
						allowedValues.add("true");
						allowedValues.add("false");
						allowedValueType = AllowedValueType.Discrete;
						break;
					case SHOW_DATABASE_LABEL:
						allowedValues = new ArrayList<String>();
						allowedValues.add("true");
						allowedValues.add("false");
						allowedValueType = AllowedValueType.Discrete;
						break;
					case SHOW_DATE_AND_USER:
						allowedValues = new ArrayList<String>();
						allowedValues.add("true");
						allowedValues.add("false");
						allowedValueType = AllowedValueType.Discrete;
						break;
					case SHOW_LEGEND:
						allowedValues = new ArrayList<String>();
						allowedValues.add("true");
						allowedValues.add("false");
						allowedValueType = AllowedValueType.Discrete;
						break;
					case SHOW_MESH:
						allowedValues = new ArrayList<String>();
						allowedValues.add("true");
						allowedValues.add("false");
						allowedValueType = AllowedValueType.Discrete;
						break;
					case SHOW_TRIAD:
						allowedValues = new ArrayList<String>();
						allowedValues.add("true");
						allowedValues.add("false");
						allowedValueType = AllowedValueType.Discrete;
						break;
					case VIEW_ANGLE:
						allowedValues = new ArrayList<String>();
						allowedValues.add(String.valueOf(Integer.MIN_VALUE));
						allowedValues.add(String.valueOf(Integer.MAX_VALUE));
						allowedValueType = AllowedValueType.Continuous;
						break;
					case VIEW_NORMAL_X:
						allowedValues = new ArrayList<String>();
						allowedValues.add("-1");
						allowedValues.add("1");
						allowedValueType = AllowedValueType.Continuous;
						break;
					case VIEW_NORMAL_Y:
						allowedValues = new ArrayList<String>();
						allowedValues.add("-1");
						allowedValues.add("1");
						allowedValueType = AllowedValueType.Continuous;
						break;
					case VIEW_NORMAL_Z:
						allowedValues = new ArrayList<String>();
						allowedValues.add("-1");
						allowedValues.add("1");
						allowedValueType = AllowedValueType.Continuous;
						break;
					case VIEW_UP_X:
						allowedValues = new ArrayList<String>();
						allowedValues.add("-1");
						allowedValues.add("1");
						allowedValueType = AllowedValueType.Continuous;
						break;
					case VIEW_UP_Y:
						allowedValues = new ArrayList<String>();
						allowedValues.add("-1");
						allowedValues.add("1");
						allowedValueType = AllowedValueType.Continuous;
						break;
					case VIEW_UP_Z:
						allowedValues = new ArrayList<String>();
						allowedValues.add("-1");
						allowedValues.add("1");
						allowedValueType = AllowedValueType.Continuous;
						break;
					case X_AXIS_LABEL:
						allowedValueType = AllowedValueType.Undefined;
						break;
					case Y_AXIS_LABEL:
						allowedValueType = AllowedValueType.Undefined;
						break;
					case Z_AXIS_LABEL:
						allowedValueType = AllowedValueType.Undefined;
						break;
					case X_AXIS_UNITS:
						allowedValueType = AllowedValueType.Undefined;
						break;
					case Y_AXIS_UNITS:
						allowedValueType = AllowedValueType.Undefined;
						break;
					case Z_AXIS_UNITS:
						allowedValueType = AllowedValueType.Undefined;
						break;
					case ZOOM_LEVEL:
						allowedValues = new ArrayList<String>();
						allowedValues.add(String.valueOf(Integer.MIN_VALUE));
						allowedValues.add(String.valueOf(Integer.MAX_VALUE));
						allowedValueType = AllowedValueType.Continuous;
						break;

					}

				}

			};

			// Add the new Entry to the list
			properties.add(e);

			// Set the Entry's name, value, description and id
			e.setName(p.toString());
			e.setValue(propertyTable.get(p));
			e.setDescription(p.toString());
			e.setId(id);

			// Increment the id
			id++;
		}

		// Return the list of entries
		return properties;
	}
}