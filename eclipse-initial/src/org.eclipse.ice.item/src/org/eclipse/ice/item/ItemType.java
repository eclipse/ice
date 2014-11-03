/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * An enumeration of all of the types of Items that can be created by the
 * ItemManager.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public enum ItemType {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A simple, basic Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Basic, /**
	 * <!-- begin-UML-doc --> Modules are the primary units of
	 * functionality including but not limited to: physics and math solvers,
	 * time integrators, and optimization routines. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Module,
	/**
	 * <!-- begin-UML-doc --> A module operation is a publicly exposed operation
	 * available on a module. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	ModuleOperation,
	/**
	 * <!-- begin-UML-doc --> A module connection defines a connection between
	 * two modules where one or both of the modules require the functionality of
	 * the other. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	ModuleConnection,
	/**
	 * <!-- begin-UML-doc --> An application is the composition of one or more
	 * modules into an integrated capability with all necessary connections
	 * between modules defined. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Application,
	/**
	 * <!-- begin-UML-doc --> An application specification describes all of the
	 * information related to an application including information about all of
	 * the modules in the application and the target computing platform. <!--
	 * end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	ApplicationSpecification,
	/**
	 * <!-- begin-UML-doc --> A compiled application is an application where the
	 * application source has been compiled into executable code. <!--
	 * end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	CompiledApplication,
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Model Item type specifies all of the information related to the
	 * particular science problem under consideration including computing
	 * parameters (solver information, etc.) and information about the physical
	 * objects in the system (geometry, mesh, material types, etc.).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Model, /**
	 * <!-- begin-UML-doc --> The model output package specifies all of
	 * the information related to the particular science problem under
	 * consideration including parameters for modules and information about the
	 * physical objects in the system (material types, etc.). The model output
	 * package is created during a simulation. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	ModelOutputPackage,
	/**
	 * <!-- begin-UML-doc -->
	 * "Physical objects have a geometric representation, are composed of materials, and can have possibly many different discretizations."
	 * 
	 * PhysicalObject is an abstract type for storing geometric, mesh, and
	 * materials information about the object that is being modeled. Physical
	 * object exposes three different interfaces, one each for geometry, mesh,
	 * and materials. Other quantities, such as fields, can be managed and
	 * stored on the object through the get, set, and delete methods. <!--
	 * end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	PhysicalObject,
	/**
	 * <!-- begin-UML-doc --> The material item type specifies that an item
	 * handle if a material. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Material,
	/**
	 * <!-- begin-UML-doc --> The geometry item type specifies that an item
	 * handle is a geometry element. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Geometry,
	/**
	 * <!-- begin-UML-doc --> The mesh item type specifies that an item handle
	 * is a mesh element. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Mesh,
	/**
	 * <!-- begin-UML-doc --> A simulation is the combination of a compiled
	 * application and a model input package and is executed on a target
	 * platform. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Simulation,
	/**
	 * <!-- begin-UML-doc --> An analysis session is used to perform data
	 * analysis within ICE. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	AnalysisSession,
	/**
	 * <!-- begin-UML-doc --> An analyzed data set is a dataset on which a user
	 * is performing analysis. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	AnalyzedDataSet,
	/**
	 * <!-- begin-UML-doc --> The data viewer session is a session use for
	 * viewing data while performing analysis. <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	DataViewerSession
}