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

/** 
 * <!-- begin-UML-doc -->
 * <p>This is an enumeration of all properties that can be modified for a VisItAnalysisAsset with an AnalysisAssetType of PICTURE.</p>
 * <!-- end-UML-doc -->
 * @author els
 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public enum VisItAnalysisPictureProperty {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the image's colortable by name. The available color table names are retrieved by calling the getColorTables() operation in VisItAnalysisAsset.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	COLORTABLE("Colortable"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the type of database label. Available types are "File", "Directory" and "Full".</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	DATABASE_LABEL_TYPE("Database Label Type"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the image's width in pixels.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	IMAGE_WIDTH("Image Width (pixels)"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the image's height in pixels.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	IMAGE_HEIGHT("Image Height (pixels)"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates whether or not to invert the selected color table.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	INVERT_COLORTABLE("Invert Colortable"),  
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the name of the mesh to be displayed. 
	 * All available mesh names can be accessed by calling the getAvailableMeshes() VisItAnalysisAsset operation. 
	 * A value of "true" must be set of the SHOW_MESH property for this to be applied to the plot.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	MESH_NAME("Mesh Name"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the image's pan x value.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	PAN_X("Pan X"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the image's pan y value.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	PAN_Y("Pan Y"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the scale's minimum value. VisIt's default value will be used if an empty string is passed as the value of this property. </p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SCALE_MIN("Scale Minimum"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the scale's maximum value. VisIt's default value will be used if an empty string is passed as the value of this property. </p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SCALE_MAX("Scale Maximum"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the scale's skew factor.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SCALE_SKEW_FACTOR("Scale Skew Factor"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the scale's type. Available types are "Lin", "Log" and "Skew". If "Skew" is selected, then a value of SCALE_SKEW_FACTOR should be set.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SCALE_TYPE("Scale Type"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates whether to display the x, y, and z axes.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SHOW_AXES("Show Axes"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates whether to display a bounding box.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SHOW_BOUNDING_BOX("Show Bounding Box"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates whether to display the database label.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SHOW_DATABASE_LABEL("Show Database Label"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates whether to display the image's creation date and user. Note that in the current version of VisIt these properties can not be decoupled. </p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SHOW_DATE_AND_USER("Show Date and User"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates whether to display the legend.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SHOW_LEGEND("Show Legend"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates whether to display the mesh. A value for MESH_NAME must also be set for this to be applied to the plot.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SHOW_MESH("Show Mesh"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates whether to display the triad.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	SHOW_TRIAD("Show Triad"), 
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the angle of view in degrees.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	VIEW_ANGLE("Angle of View (degrees)"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the x component of the unit vector normal to the view.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	VIEW_NORMAL_X("View Normal X"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the y component of the unit vector normal to the view.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	VIEW_NORMAL_Y("View Normal Y"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the z component of the unit vector normal to the view.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	VIEW_NORMAL_Z("View Normal Z"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the x component of the unit vector pointing up.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	VIEW_UP_X("View Up X"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the y component of the unit vector pointing up.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	VIEW_UP_Y("View Up Y"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the z component of the unit vector pointing up.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	VIEW_UP_Z("View Up Z"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the x axis label.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	X_AXIS_LABEL("X Axis Label"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the y axis label.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Y_AXIS_LABEL("Y Axis Label"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the z axis label.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Z_AXIS_LABEL("Z Axis Label"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the x axis units.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	X_AXIS_UNITS("X Axis Units"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the y axis units.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Y_AXIS_UNITS("Y Axis Units"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the z axis units.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Z_AXIS_UNITS("Z Axis Units"),
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This literal indicates the zoom level in percent.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	ZOOM_LEVEL("Zoom Level");
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The assigned human consumable string for this enumeration value.</p>
	 * <!-- end-UML-doc -->
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String name;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Returns the assigned human consumable string for the enumeration value.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The assigned human consumable string for the enumeration value.</p>
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String toString() {
		// begin-user-code
		return name;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>Returns the VisItAnalysisPictureProperty with the provided name.</p>
	 * <!-- end-UML-doc -->
	 * @param name <p>The provided human consumable string for the enumeration value.</p>
	 * @return <p>The VisItAnalysisPictureProperty with the provided name.</p>
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static VisItAnalysisPictureProperty toProperty(String name) {
		// begin-user-code

		//Cycle over all properties
		for (VisItAnalysisPictureProperty p : values()) {

			//If this property's name equals name
			if (p.name.equals(name)) {

				//Return the property
				return p;
			}
		}

		//If not found return null
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The Constructor.</p>
	 * <!-- end-UML-doc -->
	 * @param name <p>The assigned human consumable string for the enumeration value.</p>
	 * @!generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	VisItAnalysisPictureProperty(String name) {
		// begin-user-code
		this.name = name;
		// end-user-code
	}
}