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
package org.eclipse.ice.visitanalysistool.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.imageio.ImageIO;

import llnl.visit.ViewerProxy;
import llnl.visit.avtDatabaseMetaData;
import llnl.visit.avtMeshMetaData;

import org.eclipse.ice.analysistool.AnalysisAssetType;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.visitanalysistool.VisItAnalysisAsset;
import org.eclipse.ice.visitanalysistool.VisItAnalysisPictureProperty;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class is responsible for testing the VisitAnalysisAsset class.</p>
 * <!-- end-UML-doc -->
 * @author els
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisItAnalysisAssetTester {

	/**
	 * The visit port number to use
	 */
	private static final int visitPortNumber = 12000;
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ViewerProxy viewerProxy;
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Initializes System properties used by all test operations.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void beforeClass(){
		
		//Create a bin path from the user's home directory.
		String binPath = System.getProperty("user.home")
							+ System.getProperty("file.separator")
							+ "visit"
							+ System.getProperty("file.separator")
							+ "bin";
		
		//Auto set bin for windows.
		if(System.getProperty("os.name").contains("Windows")) {
			binPath = System.getProperty("user.home")
			+ System.getProperty("file.separator")
			+ "visit";
		}

		//Assign System Property value for visit.binpath
		System.setProperty("visit.binpath", binPath);
		
		//Assign System Property value for visit.port
		System.setProperty("visit.port", String.valueOf(visitPortNumber));
		
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation checks the VisItAnalysisAsset getURI operation. It will also check properties of the file located at the returned URI.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAssetCreation() {
		// begin-user-code
		
		//Tell the thread to sleep in order to give time in between tests.
		try {
			Thread.sleep(100);
		} catch(Exception e) {
			
		}
		
		//Local declarations
		String name = "rank_14/Displacements_mesh_magnitude";
		String binPath = System.getProperty("visit.binpath");
		int port = Integer.valueOf(System.getProperty("visit.port"));;
		
		// Create a URI from the test data file
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator + "data" + separator + "AMPData.silo");
		URI data = dataFile.toURI();
		
		// Attempt to create a URI from the test image file
		File assetFile = new File(System.getProperty("user.dir") + separator + "data" + separator + "AMPData-rank_14-Displacements_mesh_magnitude-0.png");
		URI assetUri = assetFile.toURI();
		
		//Create a new ViewerProxy
		viewerProxy = new ViewerProxy();
		
		//Add headless arguments
		viewerProxy.AddArgument("-nowin");
		viewerProxy.AddArgument("-nosplash");
		viewerProxy.AddArgument("-noconfig");

		//Set the bin path
		viewerProxy.SetBinPath(binPath);
		
		//Create the viewerProxy on a port
		if(viewerProxy.Create(port)){

			//Set to use synchronization
			viewerProxy.SetSynchronous(true);

			//Try to open the database
			if (viewerProxy.GetViewerMethods().OpenDatabase(dataFile.getPath(), 0)) {
			
				//Request that the metadata object for slice 0 be filled.
				viewerProxy.GetViewerMethods().RequestMetaData(dataFile.getPath(), 0);

				//Get the database metadata object and assign to local variable
				avtDatabaseMetaData databaseMetaData = viewerProxy.GetViewerState().GetDatabaseMetaData();
				
				//Create the test asset
				VisItAnalysisAsset visItAnalysisAsset = new VisItAnalysisAsset(data, databaseMetaData, name, 0, viewerProxy);
				
				//Run some tests
				assertNotNull(visItAnalysisAsset.getURI());
				assertEquals(visItAnalysisAsset.getURI(), assetUri);
				assertTrue(new File(visItAnalysisAsset.getURI()).exists());
	
				//Check the width and height of the image saved to disk
				String width = visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.IMAGE_WIDTH.toString());
				String height = visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.IMAGE_HEIGHT.toString());
				BufferedImage bufferedImage;
				try {
					bufferedImage = ImageIO.read(new File(visItAnalysisAsset.getURI()));
					assertEquals(bufferedImage.getWidth(), Integer.valueOf(width).intValue());
					assertEquals(bufferedImage.getHeight(), Integer.valueOf(height).intValue());
				} catch (IOException e) {
					fail("The asset's image could not read into a buffered image.");
					e.printStackTrace();
				}

			}else{
				
				fail("The test database could not be opened by VisIt.");
				
			}
			
		}else{
	
			fail("A fully initialized ViewerProxy instance could not be created.");
		}

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation checks the VisItAnalysisAsset getType, getName, getAvailableColorTables and getAvailableMeshes operations.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAccessors() {
		// begin-user-code
		
		//Local declarations
		AnalysisAssetType type  = AnalysisAssetType.PICTURE;
		String name = "rank_14/Displacements_mesh_magnitude";
		String binPath = System.getProperty("visit.binpath");
		int port = Integer.valueOf(System.getProperty("visit.port"));
		
		// Create a URI from the test data file
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator + "data" + separator + "AMPData.silo");
		URI data = dataFile.toURI();
		
		//Create a new ViewerProxy
		viewerProxy = new ViewerProxy();
		
		//Add headless arguments
		viewerProxy.AddArgument("-nowin");
		viewerProxy.AddArgument("-nosplash");
		viewerProxy.AddArgument("-noconfig");
		
		//Set the bin path
		viewerProxy.SetBinPath(binPath);
		
		//Create the viewerProxy on a port
		if(viewerProxy.Create(port)){
			
			//Set to use synchronization
			viewerProxy.SetSynchronous(true);
			
			//Try to open the database
			if (viewerProxy.GetViewerMethods().OpenDatabase(dataFile.getPath(), 0)) {
			
				//Request that the metadata object for slice 0 be filled.
				viewerProxy.GetViewerMethods().RequestMetaData(dataFile.getPath(), 0);

				//Get the database metadata object and assign to local variable
				avtDatabaseMetaData databaseMetaData = viewerProxy.GetViewerState().GetDatabaseMetaData();
				
				//Create the test asset
				VisItAnalysisAsset visItAnalysisAsset = new VisItAnalysisAsset(data, databaseMetaData, name, 0, viewerProxy);

				//Run some tests
				assertNotNull(visItAnalysisAsset.getName());
				assertEquals(visItAnalysisAsset.getName(), name);
				assertNotNull(visItAnalysisAsset.getType());
				assertEquals(visItAnalysisAsset.getType(), type);
				
				//Create a list to hold the color table names
				ArrayList<String> localAvailableColorTables = new ArrayList<String>();
				
				//Get the names as a vector from VisIt
				Vector<String> colorTables = viewerProxy.GetViewerState().GetColorTableAttributes().GetNames();
				
				//Iterate over names and add elements to colorTables
				Iterator<String> itr = colorTables.iterator();
				while(itr.hasNext()){
					localAvailableColorTables.add(itr.next());
				}
	
				//Get the color tables from the asset
				ArrayList<String> availableColorTables = visItAnalysisAsset.getAvailableColorTables();
	
				//Run some tests
				assertNotNull(availableColorTables);
				assertEquals(localAvailableColorTables.size(), availableColorTables.size());
				assertTrue(availableColorTables.containsAll(localAvailableColorTables));
				
				//Create a new list to hold the meshes 
				ArrayList<String> localAvailableMeshes = new ArrayList<String>();

				//Get the vector of meshes from the database metadata
				Vector<avtMeshMetaData> meshes = databaseMetaData.GetMeshes();

				//If the vector is not null
				if (meshes != null) {

					//Get an iterator
					Iterator<avtMeshMetaData> itrMesh = meshes.iterator();

					//While the iterator has more elements
					while (itrMesh.hasNext()) {

						//Add the mesh's name to the list
						localAvailableMeshes.add(itrMesh.next().GetName());

					}

				}
				
				//Get the color tables from the asset
				ArrayList<String> availableMeshes = visItAnalysisAsset.getAvailableMeshes();
	
				//Run some tests
				assertNotNull(availableMeshes);
				assertEquals(localAvailableMeshes.size(), availableMeshes.size());
				assertTrue(availableMeshes.containsAll(localAvailableMeshes));
				
			}else{
				
				fail("The test database could not be opened by VisIt.");
				
			}
				
		}else{
	
			fail("A fully initialized ViewerProxy instance could not be created.");
		}
			
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation checks the VisItAnalysisAsset getProperty, setProperty, resetProperties, getProperties, and getPropertiesAsEntryList operations.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkPropertyMutators() {
		// begin-user-code
		
		//Assign System Property value for visit.port
		System.setProperty("visit.port", String.valueOf(visitPortNumber));
		
		//Local declarations
		String name = "rank_14/Displacements_mesh_magnitude";
		String binPath = System.getProperty("visit.binpath");
		int port = Integer.valueOf(System.getProperty("visit.port"));
		
		// Create a URI from the test data file
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator + "data" + separator + "AMPData.silo");
		URI data = dataFile.toURI();
		
		//Create a new ViewerProxy
		viewerProxy = new ViewerProxy();
		
		//Add headless arguments
		viewerProxy.AddArgument("-nowin");
		viewerProxy.AddArgument("-nosplash");
		viewerProxy.AddArgument("-noconfig");
		
		//Set the bin path
		viewerProxy.SetBinPath(binPath);
		
		//Create the viewerProxy on a port
		if(viewerProxy.Create(port)){
			
			//Set to use synchronization
			viewerProxy.SetSynchronous(true);
			
			//Try to open the database
			if (viewerProxy.GetViewerMethods().OpenDatabase(dataFile.getPath(), 0)) {
			
				//Request that the metadata object for slice 0 be filled.
				viewerProxy.GetViewerMethods().RequestMetaData(dataFile.getPath(), 0);

				//Get the database metadata object and assign to local variable
				avtDatabaseMetaData databaseMetaData = viewerProxy.GetViewerState().GetDatabaseMetaData();
				
				//Create the test asset
				VisItAnalysisAsset visItAnalysisAsset = new VisItAnalysisAsset(data, databaseMetaData, name, 0, viewerProxy);
				
				//Save current properties
				String colortableDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.COLORTABLE.toString());
				String databaseLabelTypeDefault = 	visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.DATABASE_LABEL_TYPE.toString());
				String imageWidthDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.IMAGE_WIDTH.toString());
				String imageHeightDefault = 		visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.IMAGE_HEIGHT.toString());
				String invertColortableDefault = 	visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.INVERT_COLORTABLE.toString());
				String meshNameDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.MESH_NAME.toString());
				String panXDefault = 				visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.PAN_X.toString());
				String panYDefault = 				visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.PAN_Y.toString());
				String scaleMaxDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_MAX.toString());
				String scaleMinDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_MIN.toString());
				String scaleSkewFactorDefault = 	visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_SKEW_FACTOR.toString());
				String scaleTypeDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_TYPE.toString());
				String showAxesDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_AXES.toString());
				String showBoundingBoxDefault = 	visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_BOUNDING_BOX.toString());
				String showDatabaseLabelDefault = 	visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_DATABASE_LABEL.toString());
				String showDateAndUserDefault = 	visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_DATE_AND_USER.toString());
				String showLegendDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_LEGEND.toString());
				String showMeshDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_MESH.toString());
				String showTriadDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_TRIAD.toString());
				String viewAngleDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_ANGLE.toString());
				String viewNormalXDefault = 		visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_X.toString());
				String viewNormalYDefault = 		visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_Y.toString());
				String viewNormalZDefault = 		visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_Z.toString());
				String viewUpXDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_UP_X.toString());
				String viewUpYDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_UP_Y.toString());
				String viewUpZDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_UP_Z.toString());
				String xAxisLabelDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.X_AXIS_LABEL.toString());
				String xAxisUnitsDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.X_AXIS_UNITS.toString());
				String yAxisLabelDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Y_AXIS_LABEL.toString());
				String yAxisUnitsDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Y_AXIS_UNITS.toString());
				String zAxisLabelDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Z_AXIS_LABEL.toString());
				String zAxisUnitsDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Z_AXIS_UNITS.toString());
				String zoomLevelDefault = 			visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.ZOOM_LEVEL.toString());
				
				//Local declarations
				String colortable = "rainbow";
				String databaseLabelType = "Full";
				String imageWidth = "1050";
				String imageHeight = "800";
				String invertColortable = "true";
				String meshName = "rank14/mesh";
				String panX = "0.011";
				String panY = "-0.002";
				String scaleMax = "600";
				String scaleMin = "580";
				String scaleSkewFactor = "0.1";
				String scaleType = "Skew";
				String showAxes = "true";
				String showBoundingBox = "true";
				String showDatabaseLabel = "true";
				String showDateAndUser = "true";
				String showLegend = "false";
				String showMesh = "true";
				String showTriad = "false";
				String viewAngle = "34";
				String viewNormalX = "0.39026";
				String viewNormalY = "0.44108";
				String viewNormalZ = "0.808174";
				String viewUpX = "-0.153449";
				String viewUpY = "0.896661";
				String viewUpZ = "-0.415275";
				String xAxisLabel = "Eric";
				String xAxisUnits = "Rocks";
				String yAxisLabel = "Metallica";
				String yAxisUnits = "Rocks Better";
				String zAxisLabel = "AC/DC";
				String zAxisUnits = "Rocks Best";
				String zoomLevel = "1.2";
				
				//Set properties
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.COLORTABLE.toString(), 			colortable);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.DATABASE_LABEL_TYPE.toString(), databaseLabelType);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.IMAGE_WIDTH.toString(), 		imageWidth);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.IMAGE_HEIGHT.toString(), 		imageHeight);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.INVERT_COLORTABLE.toString(), 	invertColortable);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.MESH_NAME.toString(), 			meshName);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.PAN_X.toString(), 				panX);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.PAN_Y.toString(), 				panY);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SCALE_MAX.toString(), 			scaleMax);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SCALE_MIN.toString(), 			scaleMin);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SCALE_SKEW_FACTOR.toString(), 	scaleSkewFactor);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SCALE_TYPE.toString(), 			scaleType);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SHOW_AXES.toString(), 			showAxes);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SHOW_BOUNDING_BOX.toString(), 	showBoundingBox);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SHOW_DATABASE_LABEL.toString(), showDatabaseLabel);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SHOW_DATE_AND_USER.toString(), 	showDateAndUser);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SHOW_LEGEND.toString(), 		showLegend);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SHOW_MESH.toString(), 			showMesh);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.SHOW_TRIAD.toString(), 			showTriad);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.VIEW_ANGLE.toString(), 			viewAngle);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_X.toString(), 		viewNormalX);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_Y.toString(), 		viewNormalY);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_Z.toString(), 		viewNormalZ);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.VIEW_UP_X.toString(), 			viewUpX);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.VIEW_UP_Y.toString(), 			viewUpY);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.VIEW_UP_Z.toString(), 			viewUpZ);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.X_AXIS_LABEL.toString(), 		xAxisLabel);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.X_AXIS_UNITS.toString(), 		xAxisUnits);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.Y_AXIS_LABEL.toString(), 		yAxisLabel);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.Y_AXIS_UNITS.toString(), 		yAxisUnits);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.Z_AXIS_LABEL.toString(), 		zAxisLabel);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.Z_AXIS_UNITS.toString(), 		zAxisUnits);
				visItAnalysisAsset.setProperty(VisItAnalysisPictureProperty.ZOOM_LEVEL.toString(), 			zoomLevel);
				
				//Run some tests
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.COLORTABLE.toString()), 			colortable);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.DATABASE_LABEL_TYPE.toString()),	databaseLabelType);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.IMAGE_WIDTH.toString()), 			imageWidth);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.IMAGE_HEIGHT.toString()),			imageHeight);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.INVERT_COLORTABLE.toString()),		invertColortable);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.MESH_NAME.toString()),				meshName);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.PAN_X.toString()), 				panX);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.PAN_Y.toString()), 				panY);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_MAX.toString()), 			scaleMax);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_MIN.toString()), 			scaleMin);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_SKEW_FACTOR.toString()), 	scaleSkewFactor);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_TYPE.toString()), 			scaleType);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_AXES.toString()), 			showAxes);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_BOUNDING_BOX.toString()), 	showBoundingBox);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_DATABASE_LABEL.toString()), 	showDatabaseLabel);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_DATE_AND_USER.toString()), 	showDateAndUser);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_LEGEND.toString()), 			showLegend);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_MESH.toString()), 			showMesh);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_TRIAD.toString()), 			showTriad);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_ANGLE.toString()), 			viewAngle);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_X.toString()), 		viewNormalX);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_Y.toString()), 		viewNormalY);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_Z.toString()), 		viewNormalZ);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_UP_X.toString()), 			viewUpX);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_UP_Y.toString()), 			viewUpY);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_UP_Z.toString()), 			viewUpZ);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.X_AXIS_LABEL.toString()), 			xAxisLabel);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.X_AXIS_UNITS.toString()), 			xAxisUnits);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Y_AXIS_LABEL.toString()), 			yAxisLabel);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Y_AXIS_UNITS.toString()), 			yAxisUnits);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Z_AXIS_LABEL.toString()), 			zAxisLabel);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Z_AXIS_UNITS.toString()), 			zAxisUnits);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.ZOOM_LEVEL.toString()), 			zoomLevel);
		
				//Get the Properties object from the asset
				Properties properties = visItAnalysisAsset.getProperties();
				
				//Run some tests
				assertEquals(properties.get(VisItAnalysisPictureProperty.COLORTABLE.toString()), 			colortable);
				assertEquals(properties.get(VisItAnalysisPictureProperty.DATABASE_LABEL_TYPE.toString()),	databaseLabelType);
				assertEquals(properties.get(VisItAnalysisPictureProperty.IMAGE_WIDTH.toString()), 			imageWidth);
				assertEquals(properties.get(VisItAnalysisPictureProperty.IMAGE_HEIGHT.toString()),			imageHeight);
				assertEquals(properties.get(VisItAnalysisPictureProperty.INVERT_COLORTABLE.toString()),		invertColortable);
				assertEquals(properties.get(VisItAnalysisPictureProperty.MESH_NAME.toString()),				meshName);
				assertEquals(properties.get(VisItAnalysisPictureProperty.PAN_X.toString()), 				panX);
				assertEquals(properties.get(VisItAnalysisPictureProperty.PAN_Y.toString()), 				panY);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SCALE_MAX.toString()), 			scaleMax);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SCALE_MIN.toString()), 			scaleMin);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SCALE_SKEW_FACTOR.toString()), 	scaleSkewFactor);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SCALE_TYPE.toString()), 			scaleType);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SHOW_AXES.toString()), 			showAxes);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SHOW_BOUNDING_BOX.toString()), 	showBoundingBox);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SHOW_DATABASE_LABEL.toString()), 	showDatabaseLabel);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SHOW_DATE_AND_USER.toString()), 	showDateAndUser);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SHOW_LEGEND.toString()), 			showLegend);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SHOW_MESH.toString()), 			showMesh);
				assertEquals(properties.get(VisItAnalysisPictureProperty.SHOW_TRIAD.toString()), 			showTriad);
				assertEquals(properties.get(VisItAnalysisPictureProperty.VIEW_ANGLE.toString()), 			viewAngle);
				assertEquals(properties.get(VisItAnalysisPictureProperty.VIEW_NORMAL_X.toString()), 		viewNormalX);
				assertEquals(properties.get(VisItAnalysisPictureProperty.VIEW_NORMAL_Y.toString()), 		viewNormalY);
				assertEquals(properties.get(VisItAnalysisPictureProperty.VIEW_NORMAL_Z.toString()), 		viewNormalZ);
				assertEquals(properties.get(VisItAnalysisPictureProperty.VIEW_UP_X.toString()), 			viewUpX);
				assertEquals(properties.get(VisItAnalysisPictureProperty.VIEW_UP_Y.toString()), 			viewUpY);
				assertEquals(properties.get(VisItAnalysisPictureProperty.VIEW_UP_Z.toString()), 			viewUpZ);
				assertEquals(properties.get(VisItAnalysisPictureProperty.X_AXIS_LABEL.toString()), 			xAxisLabel);
				assertEquals(properties.get(VisItAnalysisPictureProperty.X_AXIS_UNITS.toString()), 			xAxisUnits);
				assertEquals(properties.get(VisItAnalysisPictureProperty.Y_AXIS_LABEL.toString()), 			yAxisLabel);
				assertEquals(properties.get(VisItAnalysisPictureProperty.Y_AXIS_UNITS.toString()), 			yAxisUnits);
				assertEquals(properties.get(VisItAnalysisPictureProperty.Z_AXIS_LABEL.toString()), 			zAxisLabel);
				assertEquals(properties.get(VisItAnalysisPictureProperty.Z_AXIS_UNITS.toString()), 			zAxisUnits);
				assertEquals(properties.get(VisItAnalysisPictureProperty.ZOOM_LEVEL.toString()), 			zoomLevel);
				
				//Get an array list of Entries
				ArrayList<Entry> list = visItAnalysisAsset.getPropertiesAsEntryList();
				
				//Run some tests on the list
				assertNotNull(list);
				assertEquals(list.size(), VisItAnalysisPictureProperty.values().length);
				
				//Loop over entries and run some tests
				for(Entry entry: list){
					
					//Check that the entry is associated with a VisItAnalysisPictureProperty value
					assertNotNull(VisItAnalysisPictureProperty.toProperty(entry.getName()));
					assertNotNull(VisItAnalysisPictureProperty.toProperty(entry.getDescription()));
					
					//Get the entry's property
					VisItAnalysisPictureProperty p = VisItAnalysisPictureProperty.toProperty(entry.getName());
					
					//Run some property type dependent tests 
					switch(p){
						case COLORTABLE:
							assertEquals(entry.getDefaultValue(), colortableDefault);
							assertEquals(entry.getValue(), colortable);
							assertEquals(entry.getAllowedValues().size(), visItAnalysisAsset.getAvailableColorTables().size());
							assertTrue(entry.getAllowedValues().containsAll(visItAnalysisAsset.getAvailableColorTables()));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case DATABASE_LABEL_TYPE:
							assertEquals(entry.getDefaultValue(), databaseLabelTypeDefault);
							assertEquals(entry.getValue(), databaseLabelType);
							assertEquals(entry.getAllowedValues().size(), 3);
							assertTrue(entry.getAllowedValues().contains("File"));
							assertTrue(entry.getAllowedValues().contains("Directory"));
							assertTrue(entry.getAllowedValues().contains("Full"));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case IMAGE_WIDTH:
							assertEquals(entry.getDefaultValue(), imageWidthDefault);
							assertEquals(entry.getValue(), imageWidth);
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case IMAGE_HEIGHT:
							assertEquals(entry.getDefaultValue(), imageHeightDefault);
							assertEquals(entry.getValue(), imageHeight);
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case INVERT_COLORTABLE:
							assertEquals(entry.getDefaultValue(), invertColortableDefault);
							assertEquals(entry.getValue(), invertColortable);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("true"));
							assertTrue(entry.getAllowedValues().contains("false"));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case MESH_NAME:
							assertEquals(entry.getDefaultValue(), visItAnalysisAsset.getAvailableMeshes().get(0));
							assertEquals(entry.getValue(), visItAnalysisAsset.getAvailableMeshes().get(0));
							assertEquals(entry.getAllowedValues().size(), visItAnalysisAsset.getAvailableMeshes().size());
							assertTrue(entry.getAllowedValues().containsAll(visItAnalysisAsset.getAvailableMeshes()));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case PAN_X:
							assertEquals(entry.getDefaultValue(), panXDefault);
							assertEquals(entry.getValue(), panX);
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case PAN_Y:
							assertEquals(entry.getDefaultValue(), panYDefault);
							assertEquals(entry.getValue(), panY);
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case SCALE_MIN:
							assertEquals(entry.getDefaultValue(), String.valueOf(Integer.MIN_VALUE));
							assertEquals(entry.getValue(), scaleMin);
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case SCALE_MAX:
							assertEquals(entry.getDefaultValue(), String.valueOf(Integer.MIN_VALUE));
							assertEquals(entry.getValue(), scaleMax);
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case SCALE_SKEW_FACTOR:
							assertEquals(entry.getDefaultValue(), scaleSkewFactorDefault);
							assertEquals(entry.getValue(), scaleSkewFactor);
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case SCALE_TYPE:
							assertEquals(entry.getDefaultValue(), scaleTypeDefault);
							assertEquals(entry.getValue(), scaleType);
							assertEquals(entry.getAllowedValues().size(), 3);
							assertTrue(entry.getAllowedValues().contains("Log"));
							assertTrue(entry.getAllowedValues().contains("Lin"));
							assertTrue(entry.getAllowedValues().contains("Skew"));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case SHOW_AXES:
							assertEquals(entry.getDefaultValue(), showAxesDefault);
							assertEquals(entry.getValue(), showAxes);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("true"));
							assertTrue(entry.getAllowedValues().contains("false"));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case SHOW_BOUNDING_BOX:
							assertEquals(entry.getDefaultValue(), showBoundingBoxDefault);
							assertEquals(entry.getValue(), showBoundingBox);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("true"));
							assertTrue(entry.getAllowedValues().contains("false"));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case SHOW_DATABASE_LABEL:
							assertEquals(entry.getDefaultValue(), showDatabaseLabelDefault);
							assertEquals(entry.getValue(), showDatabaseLabel);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("true"));
							assertTrue(entry.getAllowedValues().contains("false"));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case SHOW_DATE_AND_USER:
							assertEquals(entry.getDefaultValue(), showDateAndUserDefault);
							assertEquals(entry.getValue(), showDateAndUser);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("true"));
							assertTrue(entry.getAllowedValues().contains("false"));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case SHOW_LEGEND:
							assertEquals(entry.getDefaultValue(), showLegendDefault);
							assertEquals(entry.getValue(), showLegend);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("true"));
							assertTrue(entry.getAllowedValues().contains("false"));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case SHOW_MESH:
							assertEquals(entry.getDefaultValue(), showMeshDefault);
							assertEquals(entry.getValue(), showMesh);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("true"));
							assertTrue(entry.getAllowedValues().contains("false"));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case SHOW_TRIAD:
							assertEquals(entry.getDefaultValue(), showTriadDefault);
							assertEquals(entry.getValue(), showTriad);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("true"));
							assertTrue(entry.getAllowedValues().contains("false"));
							assertEquals(entry.getValueType(), AllowedValueType.Discrete);
							break;
						case VIEW_ANGLE:
							assertEquals(entry.getDefaultValue(), viewAngleDefault);
							assertEquals(entry.getValue(), viewAngle);
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case VIEW_NORMAL_X:
							assertEquals(entry.getDefaultValue(), viewNormalXDefault);
							assertEquals(entry.getValue(), viewNormalX);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("-1"));
							assertTrue(entry.getAllowedValues().contains("1"));
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case VIEW_NORMAL_Y:
							assertEquals(entry.getDefaultValue(), viewNormalYDefault);
							assertEquals(entry.getValue(), viewNormalY);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("-1"));
							assertTrue(entry.getAllowedValues().contains("1"));
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case VIEW_NORMAL_Z:
							assertEquals(entry.getDefaultValue(), viewNormalZDefault);
							assertEquals(entry.getValue(), viewNormalZ);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("-1"));
							assertTrue(entry.getAllowedValues().contains("1"));
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case VIEW_UP_X:
							assertEquals(entry.getDefaultValue(), viewUpXDefault);
							assertEquals(entry.getValue(), viewUpX);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("-1"));
							assertTrue(entry.getAllowedValues().contains("1"));
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case VIEW_UP_Y:
							assertEquals(entry.getDefaultValue(), viewUpYDefault);
							assertEquals(entry.getValue(), viewUpY);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("-1"));
							assertTrue(entry.getAllowedValues().contains("1"));
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case VIEW_UP_Z:
							assertEquals(entry.getDefaultValue(), viewUpZDefault);
							assertEquals(entry.getValue(), viewUpZ);
							assertEquals(entry.getAllowedValues().size(), 2);
							assertTrue(entry.getAllowedValues().contains("-1"));
							assertTrue(entry.getAllowedValues().contains("1"));
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
						case X_AXIS_LABEL:
							assertEquals(entry.getDefaultValue(), xAxisLabelDefault);
							assertEquals(entry.getValue(), xAxisLabel);
							assertEquals(entry.getValueType(), AllowedValueType.Undefined);
							break;
						case Y_AXIS_LABEL:
							assertEquals(entry.getDefaultValue(), yAxisLabelDefault);
							assertEquals(entry.getValue(), yAxisLabel);
							assertEquals(entry.getValueType(), AllowedValueType.Undefined);
							break;
						case Z_AXIS_LABEL:
							assertEquals(entry.getDefaultValue(), zAxisLabelDefault);
							assertEquals(entry.getValue(), zAxisLabel);
							assertEquals(entry.getValueType(), AllowedValueType.Undefined);
							break;
						case X_AXIS_UNITS:
							assertEquals(entry.getDefaultValue(), xAxisUnitsDefault);
							assertEquals(entry.getValue(), xAxisUnits);
							assertEquals(entry.getValueType(), AllowedValueType.Undefined);
							break;
						case Y_AXIS_UNITS:
							assertEquals(entry.getDefaultValue(), yAxisUnitsDefault);
							assertEquals(entry.getValue(), yAxisUnits);
							assertEquals(entry.getValueType(), AllowedValueType.Undefined);
							break;
						case Z_AXIS_UNITS:
							assertEquals(entry.getDefaultValue(), zAxisUnitsDefault);
							assertEquals(entry.getValue(), zAxisUnits);
							assertEquals(entry.getValueType(), AllowedValueType.Undefined);
							break;
						case ZOOM_LEVEL:
							assertEquals(entry.getDefaultValue(), zoomLevelDefault);
							assertEquals(entry.getValue(), zoomLevel);
							assertEquals(entry.getValueType(), AllowedValueType.Continuous);
							break;
							
					}
					
				}
				
				//Reset the properties to the defaults
				visItAnalysisAsset.resetProperties();
				
				//Run some tests
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.COLORTABLE.toString()), 			colortableDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.DATABASE_LABEL_TYPE.toString()),	databaseLabelTypeDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.IMAGE_WIDTH.toString()), 			imageWidthDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.IMAGE_HEIGHT.toString()),			imageHeightDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.INVERT_COLORTABLE.toString()),		invertColortableDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.MESH_NAME.toString()),				meshNameDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.PAN_X.toString()), 				panXDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.PAN_Y.toString()), 				panYDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_MAX.toString()), 			scaleMaxDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_MIN.toString()), 			scaleMinDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_SKEW_FACTOR.toString()), 	scaleSkewFactorDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SCALE_TYPE.toString()), 			scaleTypeDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_AXES.toString()), 			showAxesDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_BOUNDING_BOX.toString()), 	showBoundingBoxDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_DATABASE_LABEL.toString()), 	showDatabaseLabelDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_DATE_AND_USER.toString()), 	showDateAndUserDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_LEGEND.toString()), 			showLegendDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_MESH.toString()), 			showMeshDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.SHOW_TRIAD.toString()), 			showTriadDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_ANGLE.toString()), 			viewAngleDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_X.toString()), 		viewNormalXDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_Y.toString()), 		viewNormalYDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_NORMAL_Z.toString()), 		viewNormalZDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_UP_X.toString()), 			viewUpXDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_UP_Y.toString()), 			viewUpYDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.VIEW_UP_Z.toString()), 			viewUpZDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.X_AXIS_LABEL.toString()), 			xAxisLabelDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.X_AXIS_UNITS.toString()), 			xAxisUnitsDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Y_AXIS_LABEL.toString()), 			yAxisLabelDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Y_AXIS_UNITS.toString()), 			yAxisUnitsDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Z_AXIS_LABEL.toString()), 			zAxisLabelDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.Z_AXIS_UNITS.toString()), 			zAxisUnitsDefault);
				assertEquals(visItAnalysisAsset.getProperty(VisItAnalysisPictureProperty.ZOOM_LEVEL.toString()), 			zoomLevelDefault);
				
 			}else{
				
				fail("The test database could not be opened by VisIt.");
				
			}
			
		}else{
	
			fail("A fully initialized ViewerProxy instance could not be created.");
		}

		// end-user-code
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Closes the VisitAnalysisTool instance on the selected port.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@After
	public void after() {
		// begin-user-code
		viewerProxy.Close();
		// end-user-code
	}
	
}