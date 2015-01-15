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
package org.eclipse.ice.item;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;

import java.io.File;

import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.Identifiable;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TimeDataComponent;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.Entry;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.painfullySimpleForm.PainfullySimpleForm;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.resource.ResourceHandler;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.io.serializable.IReader;
import org.eclipse.ice.io.serializable.IWriter;
import org.eclipse.ice.item.action.Action;
import org.eclipse.ice.item.action.TaggedOutputWriterAction;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;
import org.eclipse.ice.item.messaging.Message;
import org.eclipse.core.resources.IFolder;

import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The Item class is responsible for carrying out activities necessary to
 * perform certain tasks with ICE. The operations process(), setupForm(), and
 * reviewEntries() should be tailored and called by subclasses as needed. The
 * Item stores information in the Eclipse IProject that it is given during
 * construction and subclasses should do the same. The Item will only execute
 * one Action at a time (for now). The Item class starts out in the
 * FormStatus.ReadyToProcess state and all subclasses shall obey the rule that
 * Items are initialized with an acceptable set of default parameters such that
 * they could be processed immediately.
 * </p>
 * <p>
 * Instances of classes that realize the IUpdateable interface can be registered
 * to receive updates from others in the Item with the Registry. Subclasses
 * should override registerUpdateables(). This operation performs parent-child
 * dependency matching by default, so subclasses should always call
 * super.registerUpdateables() too. Subclasses should always override this
 * operation to register their dependencies since dependencies must be
 * re-registered if the Item is copied. IUpdateables should never be registered
 * outside of this operation. (We'll cross the bridge of dynamically updated
 * dependencies when we get to it...)
 * </p>
 * <p>
 * Items may be loaded from a file. This file is either an XML file of the form
 * required by the Item's JAXB annotations or it is in the Painfully Simple Form
 * file format. Items may be initialized using either file type, but they will
 * only persist to the ICE database or to the XML form of an Item. Items must be
 * loaded by calling either loadFromXML() or loadFromPSF() after construction.
 * If it can not read properly either a PSF or XML format, it will fail.
 * </p>
 * <p>
 * By default, the reviewEntries() operation on an Item only performs simple
 * dependency checks to determine if Entries should or should not be notified
 * that their parents have changed or been marked ready. The default
 * implementation of setupForm() will add Entries with parents to the Registry
 * if the Item is loaded from a file, otherwise it will do nothing.
 * </p>
 * <p>
 * The only Actions available to the Item by default are actions to write the
 * Item's Form to an XML file or a "tagged output" where the tags of Entries are
 * used as keys associated with the values of the Entry. If the Entries do not
 * have tags, then the names of the Entries are used.
 * </p>
 * <p>
 * The Item class realizes the IComponent Visitor interface so that it can map
 * the Components in the Form and determine their types.
 * </p>
 * <p>
 * The Item behaves as follows for each state:
 * </p>
 * <table border="1">
 * <col width="50.0%"></col><col width="50.0%"></col>
 * <tr>
 * <td>
 * <p>
 * <b>State</b>
 * </p>
 * </td>
 * <td>
 * <p>
 * <b>Item behavior</b>
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * FormStatus.InfoError
 * </p>
 * </td>
 * <td>
 * <p>
 * The Item returns and accepts Forms as usual, but cannot be processed.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * FormStatus.InReview
 * </p>
 * </td>
 * <td>
 * <p>
 * The Item is reviewing the contents of the Form and will not accept any other
 * requests.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * FormStatus.NeedsInfo
 * </p>
 * </td>
 * <td>
 * <p>
 * The Item returns and accepts a second Form created during the process of
 * executing an action based on the original Form that was submitted to the
 * Item.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * FormStatus.Processed
 * </p>
 * </td>
 * <td>
 * <p>
 * The Item has been completely processed and will retrieve and accept Forms as
 * usual (although that may trigger a state change). ICEResources have been
 * added to the output component, etc.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * FormStatus.Processing
 * </p>
 * </td>
 * <td>
 * <p>
 * The Item is currently processing the Form and perform an Action. It will only
 * accept a request to cancel the Action.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * FormStatus.ReadyToProcess
 * </p>
 * </td>
 * <td>
 * <p>
 * The Item has finished reviewing its Form and can be processed. It will accept
 * all requests.
 * </p>
 * </td>
 * </tr>
 * <tr>
 * <td>
 * <p>
 * FormStatus.Unacceptable
 * </p>
 * </td>
 * <td>
 * <p>
 * The Item has been disabled and will not process the Form or accept another
 * Form in place of the current Form.
 * </p>
 * </td>
 * </tr>
 * </table>
 * <p>
 * Items can be stored to JPA databases. See the *Database() operations for more
 * information.
 * </p>
 * <p>
 * Items can be disabled and put in to a "read-only" mode where their forms can
 * be read, but the Item will not accept updated Forms or process actions.
 * Attempts to process the Item or submit a form will return
 * FormStatus.Unacceptable if the Item is disabled. Checking the Item's status
 * with getStatus() will also return FormStatus.Unacceptable. Items are enabled
 * by default.
 * </p>
 * <p>
 * The Item class also logs output to an output file. This file includes any
 * information that the author of the subclass of the Item wants to persist when
 * the Item is processed. It may or may not include, at the discretion of the
 * subclass author, output generated from external processes launched on the
 * system or outside of the Item. This file handle may be retrieved in by
 * clients from the Item.getOutputFile() operation. The base class does not use
 * the output file, but it initializes it to a file in the project space with
 * the name &lt;itemName&gt;_&lt;itemId&gt;_processOutput.txt and it is up to
 * subclasses to write to the file and close any output streams. This file
 * handle returned is the <i>real</i> file handle and can be written, but
 * clients should be careful to only read from the file. Unfortunately there is
 * no good way to pass a read-only file in Java because that is an OS dependent
 * operation.
 * </p>
 * <p>
 * If the project space for the Item has not be set upon construction, the
 * output file will not be configured and getOutputFile() will be null.
 * </p>
 * <p>
 * Items can also be observed by ItemListeners. Subclasses must implement the
 * calls to update the listeners on their own, although some protected utility
 * operations like notifyListenersOfProjectChange() exist.
 * </p>
 * <p>
 * Every Item has a directory at its disposal for storing preferences or scratch
 * data. This directory can be retrieved by subclasses by calling
 * getPreferencesDirectory(). This directory should only include preferences,
 * configuration information, scratch data or other types of data that are not
 * directly consumed by users.
 * </p>
 * <p>
 * Subclasses may override the loadInput() operation to handle input data
 * import. This operation is passed an inputstream that should contain data of
 * the proper format when a client tries to import data.
 * </p>
 * <p>
 * Items can also receive updates from other ICE subsystems, remote ICE
 * subsystems or external third-party processes through via the update()
 * operation. In practice, these messages are filtered by classes higher up the
 * call stack and acted upon by the Item.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings, Anna Wojtowicz
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "Item")
public class Item implements IComponentVisitor, Identifiable,
		IUpdateableListener {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ItemType of the Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute()
	protected ItemType itemType;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient()
	protected Registry registry;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Item's Form.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAnyElement()
	@XmlElementRefs(value = {
			@XmlElementRef(name = "Form", type = Form.class),
			@XmlElementRef(name = "JobLauncherForm", type = JobLauncherForm.class) })
	protected Form form;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The action that is currently being performed by the Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient()
	protected Action action;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of Actions that the Item can perform. It must be specified by a
	 * subclass during construction.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlElement(name = "AllowedActions")
	protected ArrayList<String> allowedActions;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Eclipse Project where the ICEResources created by this project should
	 * be stored.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient()
	protected IProject project;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The most recent status of the Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute()
	protected FormStatus status;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of Entries from the Form. This list is maintained to improve the
	 * speed of reviewEntries() and is created in setupForm() when it is called
	 * by loadFromXML() or loadFromPSF().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<Entry> entryList;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The string that is used to describe the process by which the Item class
	 * writes the Form in ICE's native XML format.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String nativeExportActionString = "Export to ICE Native Format";
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The string that is used to describe the process by which the Item class
	 * writes the values of the Entries in the Form to a file using their tags,
	 * or their names if no tags are available, as keys.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String taggedExportActionString = "Export to key-value pair output";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A map that contains all of the Components of the Form with each Component
	 * type as a key (data, output or table) and an arraylist of the Components
	 * as the value.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	protected HashMap<String, ArrayList<Component>> componentMap;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The unique identification number of the Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected int uniqueId;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name of the Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String itemName;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The description of the Item. This description should be different than
	 * the name of the Item and should contain information that would be useful
	 * to a human user.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String itemDescription;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ICEJAXBHandler used to marshal Items to and from XML.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	protected ICEJAXBHandler jaxbManipulator;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A specifically attribute designed to be utilized by the JPA database.
	 * This variable should not be accessed normally by ICE, only by JPA. DO NOT
	 * OVERRIDE THIS VARIABLE!
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected int DB_ID;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The itemBuilder's name. The default value is null and can only be set
	 * once.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute(name = "builderName")
	protected String builderName;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the Item is enabled, false if the Item is disabled.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	private boolean enabled = true;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The last status of the Item before it was process or modified. This is
	 * used, for example, when killing processes or disabling the Item so that
	 * the it can be reverted to the last state.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	private FormStatus lastStatus;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The file handle to the output file that stores output generated by this
	 * Item during processing. The data in this file is information provided by
	 * the Item and may or may not include, at the discretion of the author of
	 * the subclass, output collected from externally launched programs.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient
	protected File outputFile;

	/**
	 * The ResourceHandler for this item that discovers and creates 
	 * {@link ICEResource} items.
	 */
	private static ResourceHandler resourceHandler = new ResourceHandler();
	
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of listeners observing this Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlTransient()
	protected ArrayList<ItemListener> listeners;

	/**
	 * A flag that is true if debug mode is enabled, false otherwise.
	 */
	@XmlTransient()
	protected boolean debuggingEnabled = false;

	/**
	 * Reference to the IOService that provides IReaders and IWriters for the
	 * Item.
	 */
	@XmlTransient()
	private static IOService ioService;

	/**
	 * The IActionFactory that provides the set of Actions that can be used by
	 * this Item in processing.
	 */
	@XmlTransient()
	private IActionFactory actionFactory;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor. Subclasses of Item should implement their own
	 * constructor, but creating the Form should be done in the setupForm()
	 * operation, which is called by this constructor. Creating the Form
	 * includes creating the list of Actions that can be performed. The Eclipse
	 * project where the Item should store files is passed as an argument. Since
	 * there is no guarantee that the Item will actually need this argument, it
	 * may be null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project where files should be stored for this
	 *            Item.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Item(IProject projectSpace) {
		// begin-user-code

		// Determine whether or not ICE is in debug mode
		if (System.getProperty("DebugICE") != null) {
			debuggingEnabled = true;
		}

		// Set the default information
		uniqueId = 1;
		itemName = "ICE Item";
		itemDescription = "This is an ICE Item";

		// set builderName to empty string
		builderName = "";

		// Set the type
		itemType = ItemType.Basic;

		// Create the Registry
		registry = new Registry();

		// Setup the listener list
		listeners = new ArrayList<ItemListener>();

		// Set the status
		status = FormStatus.ReadyToProcess;

		// Set the project. It does not matter if it is null.
		project = projectSpace;

		// Initialize allowed Actions. For a default Item it should only handle
		// exports to files in the project space. Subclasses may choose to
		// override this, of course.
		allowedActions = new ArrayList<String>();
		allowedActions.add(nativeExportActionString);
		allowedActions.add(taggedExportActionString);

		// Call the override method for setting up custom information
		setupItemInfo();

		// Setup the Form
		setupForm();

		// Setup the Entry list and the component map and register dependencies
		setupEntryList();

		// Enable the list of actions if the Form is not already setup to list a
		// set of available actions
		if (form.getActionList() == null) {
			form.setActionList(allowedActions);
		}

		// Set the Form's name, description and it's reflexive Item id
		if (form != null) {
			form.setName(getName());
			form.setDescription(getDescription());
			form.setItemID(getId());
			form.setId(getId());
			form.markReady(true);
		} else {
			throw new RuntimeException(
					"Form cannot be null in constructor for "
							+ this.getClass().getName());
		}

		// Setup the output file handle.
		setupOutputFile();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A nullary constructor. This constructor should only be used by JAXB or
	 * JPA for loading the Item from a serialized or transactional form
	 * respectively. If this constructor is used, setProject() must be called
	 * immediately after or an Item will not function.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Item() {
		// begin-user-code

		// Just call the other constructor with a null argument.
		this(null);

		// end-user-code
	}

	/**
	 * This method should be used by subclasses to get a reference to the
	 * desired IReader. To get the desired IReader, subclasses must specify the
	 * IO type String by implementing the Item.getIOType() method.
	 * 
	 * @return
	 */
	protected IReader getReader() {
		if (ioService != null) {
			return ioService.getReader(getIOType());
		}

		return null;
	}

	/**
	 * This method should be used by subclasses to get a reference to the
	 * desired IWriter. To get the desired IWriter, subclasses must specify the
	 * IO type String by implementing the Item.getIOType() method.
	 * 
	 * @return
	 */
	protected IWriter getWriter() {
		if (ioService != null) {
			return ioService.getWriter(getIOType());
		}

		return null;
	}

	/**
	 * Return the IO Type string. This method is to be used by subclasses to
	 * indicate which IReader and IWriter the Item subclass needs to use.
	 * 
	 * @return
	 */
	protected String getIOType() {
		return null;
	}

	/**
	 * This operation is used by the underlying OSGi framework to set the
	 * IOService that has been exposed as a Declarative Service.
	 * 
	 * @param service
	 */
	public void setIOService(IOService service) {
		if (service != null) {
			ioService = service;
		}
	}

	/**
	 * This operation sets the service reference for the IActionFactory that
	 * should be used by Items to find and execute Items during their process
	 * phase.
	 * 
	 * @param factory
	 *            The IActionFactory, which should never be null when this is
	 *            called.
	 */
	public void setActionFactory(IActionFactory factory) {
		if (factory != null) {
			actionFactory = factory;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setId(int id)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setId(int id) {
		// begin-user-code

		if (id >= 0) {
			// Set the unique id
			uniqueId = id;
			// Set the Form Id and ItemId
			form.setItemID(id);
			form.setId(id);
		}

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getDescription()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute()
	public String getDescription() {
		// begin-user-code
		return itemDescription;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getId()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute()
	public int getId() {
		// begin-user-code
		return uniqueId;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setName(String name)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setName(String name) {
		// begin-user-code

		if (name != null) {
			itemName = name;
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#getName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@XmlAttribute
	public String getName() {
		// begin-user-code
		return itemName;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#setDescription(String description)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setDescription(String description) {
		// begin-user-code

		if (description != null) {
			this.itemDescription = description;
		}

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Identifiable#equals(Object otherObject)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

		// Local Declarations
		boolean retVal = false;
		Item otherItem = null;

		// Check if they are the same reference in memory
		if (this == otherObject) {
			return true;
		}

		// Check that the object is not null, and that it is an Item
		if (otherObject == null || !(otherObject instanceof Item)) {
			return false;
		} else {
			otherItem = (Item) otherObject;
		}
		// Check names, ids, descriptions and types
		retVal = (uniqueId == otherItem.uniqueId)
				&& (itemName.equals(otherItem.itemName))
				&& (itemDescription.equals(otherItem.itemDescription))
				// && (this.DB_ID == otherItem.DB_ID)
				&& (itemType.equals(otherItem.itemType))
				&& (allowedActions.equals(otherItem.allowedActions))
				&& (form.equals(otherItem.form))
				&& (project == otherItem.project)
				&& (this.status.equals(otherItem.status))
				&& (this.builderName.equals(otherItem.builderName));

		return retVal;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the type of the Item. The type of the Item is
	 * determined by the Item class or a subclass and can not be set.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The type of the Item.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ItemType getItemType() {
		// begin-user-code
		return this.itemType;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a Form for the Item.
	 * </p>
	 * <p>
	 * If this operation is called immediately after processItem() with the same
	 * Item id and the call to processItem() returns FormStatus.NeedsInfo, then
	 * this operation will return a simple Form composed of a single
	 * DataComponent with Entries for all of the additional required
	 * information. The smaller Form is created by the Action that is executed
	 * during the call to processItem().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The Form that Eclipse User must prepare.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Form getForm() {
		// begin-user-code

		// Local Declarations
		Form actionForm = null;

		// Check the state to determine the proper Form to return
		if (!status.equals(FormStatus.NeedsInfo)) {
			// Return the Item's Form
			return form;
		} else {
			// Return the current Action's Form
			actionForm = action.getForm();
			actionForm.setItemID(uniqueId);
			return actionForm;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation submits a Form to the Item for processing.
	 * </p>
	 * <p>
	 * This operation only reviews the contractual obligations of the Form and
	 * the Item class, such as matching Item and Form.getItemId() values.
	 * Business concerns are reviewed in Item.reviewEntries, which is called by
	 * this class. This class also handles overwriting or discarding Forms as
	 * required.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param preparedForm
	 *            <p>
	 *            A Form that has been prepared by the Eclipse User with
	 *            information that is required by the Item.
	 *            </p>
	 * @return <p>
	 *         The ItemStatus value that specifies whether or not the Form was
	 *         accepted by the Item.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus submitForm(Form preparedForm) {
		// begin-user-code

		// Local Declarations
		FormStatus retVal = FormStatus.InfoError;
		boolean idsMatch = false, namesMatch = false, descMatch = false, itemIdsMatch = false;
		Form actionForm = null;

		// Only accept the submission if the Item is enabled
		if (!enabled) {
			return FormStatus.Unacceptable;
		}
		// Check the biographical information. FIXME - Needs to be more
		// rigorous! ~JJB 20121209 21:00
		// This check needs to be performed on the appropriate Form depending on
		// the mode of the Item. First, look to see if the Item's Form should be
		// checked.
		if (!status.equals(FormStatus.NeedsInfo)) {
			idsMatch = preparedForm.getId() == form.getId();
			namesMatch = preparedForm.getName().equals(form.getName());
			descMatch = preparedForm.getDescription().equals(
					form.getDescription());
			itemIdsMatch = preparedForm.getItemID() == form.getItemID();
		} else {
			// Otherwise check the Action's Form
			actionForm = action.getForm();
			idsMatch = preparedForm.getId() == actionForm.getId();
			namesMatch = preparedForm.getName().equals(actionForm.getName());
			descMatch = preparedForm.getDescription().equals(
					actionForm.getDescription());
			itemIdsMatch = preparedForm.getItemID() == actionForm.getItemID();
		}

		System.out.println("Item Message: Form submitted for review.");

		// Check the Form Entries only if the Form represents this Item. First
		// make sure the Item ids match and then make sure the Forms share
		// biographical information.
		if (idsMatch && namesMatch && descMatch && itemIdsMatch) {
			// Figure out whether to submit it to the Action
			if (!status.equals(FormStatus.NeedsInfo)) {
				// Mark the Item's status as "In Review"
				status = FormStatus.InReview;
				// And do the review! - Review the Entries
				retVal = reviewEntries(preparedForm);
				// Overwrite the current Form if the review passed
				if (retVal != FormStatus.InfoError) {
					form = preparedForm;
				}
				// If the review passed and the Form is ready to process, mark
				// it as such
				if (retVal == FormStatus.ReadyToProcess) {
					form.markReady(true);
				}
			} else {
				// Submit the Form to the Action
				retVal = action.submitForm(preparedForm);
			}
		} else {
			System.out.println("Item " + getId() + " Message: Something is "
					+ "wrong with the submitted form.");
			System.out.println("Item " + getId() + " Message: Matching Ids... "
					+ idsMatch);
			System.out.println("Item " + getId()
					+ " Message: Matching Names..." + namesMatch);
			System.out.println("Item " + getId()
					+ " Message: Matching Descriptions..." + descMatch);
			System.out.println("Item " + getId()
					+ " Message: Matching Item Ids..." + itemIdsMatch);
		}

		// Set the status
		status = retVal;

		return retVal;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The process operation processes the data in the Form to perform a certain
	 * action. The action name must be one of the set of actions from the Form
	 * that represents this Item.
	 * </p>
	 * <p>
	 * It is possible that ICE may require information in addition to that which
	 * was requested in the original Form, such as for a username and password
	 * for a remote machine. If this is the case, process will return
	 * FormStatus.NeedsInfo and a new, temporary Form will be available for the
	 * Item by calling getItem(). Once this new Form is submitted (by calling
	 * Item.submitForm() with the completed Form), the Item will finish
	 * processing.
	 * </p>
	 * <p>
	 * This operation must be tailored by subclasses to initiate specific
	 * Actions. The only Actions available to the Item by default are actions to
	 * write the Item's Form to an XML file or a "tagged output" where the tags
	 * of Entries are used as keys associated with the values of the Entry. If
	 * the Entries do not have tags, then the names of the Entries are used. The
	 * Form is persisted to a file in the project associated with the Item with
	 * a name equal to Form.getName()+"_"+Form.getId() and either a .xml or .dat
	 * extension. If the name of the Form contains spaces or other whitespace,
	 * they are converted to a single underscore. The names of the available
	 * actions are be "Export to ICE Native Format" and
	 * "Export to key-value pair output." Subclasses may choose to add
	 * additional actions or to remove the default actions.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param actionName
	 *            <p>
	 *            The name of action that should be performed using the
	 *            processed Form data.
	 *            </p>
	 * @return <p>
	 *         The status of the Item after processing the Form and executing
	 *         the action. It returns FormStatus.InfoError if it is unable to
	 *         run for any reason, including being asked to run actions that are
	 *         not in the list of available actions.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus process(String actionName) {
		// begin-user-code

		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;
		IFile outputFile = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Hashtable<String, String> propsDictionary = null;
		String filename = (form.getName() + "_" + form.getId()).replaceAll(
				"\\s+", "_");

		// Make sure the action is allowed and that the Item is enabled
		if (allowedActions.contains(actionName) && enabled) {
			// Write the file to XML if requested
			if (actionName.equals(nativeExportActionString)) {
				// Setup the IFile handle
				outputFile = project.getFile(filename + ".xml");
				// Get the XML io service
				IWriter xmlWriter = ioService.getWriter("xml");
				// Write the file
				xmlWriter.write(form, outputFile);
				// Set the status
				retStatus = FormStatus.Processed;
			} else if (actionName.equals(taggedExportActionString)) {
				// Otherwise write the file to a tagged output if requested -
				// first create the action
				action = new TaggedOutputWriterAction();
				// Setup the IFile handle
				outputFile = project.getFile(filename + ".dat");
				try {
					// Setup the dictionary
					propsDictionary = new Hashtable<String, String>();
					// Set the output file name
					propsDictionary.put("iceTaggedOutputFileName", outputFile
							.getLocationURI().getPath());
					// Add the key-value pairs
					System.out.println(entryList.size());
					for (Entry i : entryList) {
						// Use tags if they are available
						if (i.getTag() != null) {
							propsDictionary.put(i.getTag(), i.getValue());
						} else {
							// Otherwise just use the Entry's name
							propsDictionary.put(i.getName(), i.getValue());
							System.out.println("Processing value " + i.getTag()
									+ " = " + i.getValue());
						}
					}
					// Write the file. This will always overwrite an existing
					// file
					retStatus = action.execute(propsDictionary);
					// Refresh the project space so that the file is added or
					// updated
					project.refreshLocal(IProject.DEPTH_ONE, null);
					// Notify any observers of the change
					notifyListenersOfProjectChange();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		// Reset the status and return. It should only be updated if the Item is
		// enabled.
		if (enabled) {
			status = retStatus;
			return retStatus;
		} else {
			return FormStatus.Unacceptable;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation cancels all processes with the specified name.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param actionName
	 *            <p>
	 *            The name of action that should be canceled.
	 *            </p>
	 * @return <p>
	 *         The status of the Item after canceling or trying to cancel an
	 *         action.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus cancelProcess(String actionName) {
		// begin-user-code
		// TODO Auto-generated method stub
		return FormStatus.InfoError;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation cancels the last process request sent to the Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The status of the Item after canceling or trying to cancel an
	 *         action.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus cancelProcess() {
		// begin-user-code

		// Only cancel if the Item is actuallly processing
		if (action != null && status.equals(FormStatus.Processing)) {
			// Try to cancel the action
			action.cancel();
			// Reset the state to "ready" since it was clearly able to process.
			status = FormStatus.ReadyToProcess;
		}

		return status;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the list of available actions for an Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The list of actions available in the Item.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getAvailableActions() {
		// begin-user-code
		return (ArrayList<String>) allowedActions.clone();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the status of the Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FormStatus getStatus() {
		// begin-user-code

		// If the Item is disabled, do not do any further checks. Just report
		// it.
		if (!enabled) {
			return FormStatus.Unacceptable;
		} else if (status.equals(FormStatus.NeedsInfo)
				|| status.equals(FormStatus.Processing) && action != null) {
			// Determine if the status is currently dictated by the Action. If
			// the Action is currently running, then the Item will be in either
			// one of the FormStatus.NeedsInfo or FormStatus.Processing states.
			status = action.getStatus();
		}

		return status;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This protected operation setups the Entries, DataComponents and Form for
	 * a subclass of Item. The default implementation of setupForm() will add
	 * Entries with parents to the Registry if the Item is loaded from a file,
	 * otherwise it will do nothing. Subclasses should tailor this operation as
	 * needed. The list of allowed Actions may also be specified here.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupForm() {
		// begin-user-code

		// Initialize the Form it should be a PSF in case the Item is loaded
		// from a PSF file.
		form = new PainfullySimpleForm();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is used to setup the name and description of an Item. This
	 * information can be provided by the ItemBuilder responsible for this Item
	 * or any client, but it is convenient to define it on the class. It is also
	 * convenient to define it separately of the work in setupForm(). The
	 * default implementation of this operation does nothing and subclasses must
	 * override it.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupItemInfo() {
		// begin-user-code

		// Do nothing by default

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The reviewEntries operations reviews and updates Entry values as needed.
	 * This is an abstract operation that must be implemented by a subclass.
	 * </p>
	 * <p>
	 * This operation is only concerned with the business issues of the Form and
	 * not the contractual obligations that it must fulfill to satisfy ICE. For
	 * example, reviewEntries() should make sure that the Form has acceptable
	 * Entries for the particular business problem instead of worrying about
	 * unique identifiers. This operation should also return the status of the
	 * Form as a literal from the FormStatus enumeration.
	 * </p>
	 * <p>
	 * By default, the reviewEntries() operation on an Item only performs simple
	 * dependency checks to determine if Entries should or should not be
	 * notified that their parents have changed or been marked ready.<b></b>
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param preparedForm
	 * @return <p>
	 *         True if the Entries are completely specified and the Item can be
	 *         processed, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected FormStatus reviewEntries(Form preparedForm) {
		// begin-user-code

		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;
		boolean updateStatus = true;

		registerUpdateables();

		// Update the values of the Entries in the Registry
		for (Entry entry : entryList) {
			if (registry.containsKey(entry.getName())) {
				updateStatus = registry.updateValue(entry.getName(),
						entry.getValue());
			}
		}

		// Dispatch the updates
		registry.dispatch();

		System.out.println("Item Message: Entries reviewed.");

		// Set the status
		if (updateStatus) {
			retStatus = FormStatus.ReadyToProcess;
		}
		return retStatus;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation performs dependency matching by registering IUpdateable
	 * objects with the registry based on values and child names. Subclasses
	 * should always override this operation to register their dependencies
	 * since dependencies must be re-registered if the Item is copied. This
	 * operation does a non-trivial task, so subclasses should always call
	 * super.registerUpdateables() too.
	 * </p>
	 * <p>
	 * The default implementation pulls all Entries from all Data, Table and
	 * Output components and registers parent-child dependencies for those
	 * Entries.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void registerUpdateables() {
		// begin-user-code

		// Create the registry
		registry = new Registry();

		// Clear the Entry list and Component map. Not doing so will result in
		// huge numbers of copies being created! (2*n, in fact, for n calls to
		// this function.)
		entryList.clear();
		for (ArrayList<Component> list : componentMap.values()) {
			list.clear();
		}

		// Map the components
		for (Component component : form.getComponents()) {
			component.accept(this);
		}
		// Setup the Entry list based on the Entries in the
		// DataComponent
		for (Component component : componentMap.get("data")) {
			entryList.addAll(((DataComponent) component).retrieveAllEntries());
		}
		// Register the Entries
		for (Entry entry : entryList) {
			// Register the Entry name and its default value
			registry.setValue(entry.getName(), entry.getDefaultValue());
			// Register parent dependencies so that they can be notified
			// when the parent changes
			if (entry.getParent() != null) {
				registry.register(entry, entry.getParent());
			}
		}
		// Dispatch the values the first time around so that children
		// can mark themselves ready.
		registry.dispatch();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the hashcode value of the Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hashcode
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code
		// Local Declaration
		int hash = 9;

		// Compute hash code from Item data
		hash = 31 * hash + this.uniqueId;
		// If objectName is null, add 0, otherwise add String.hashcode()
		hash = 31 * hash
				+ (null == this.itemName ? 0 : this.itemName.hashCode());
		hash = 31
				* hash
				+ (null == this.itemDescription ? 0 : this.itemDescription
						.hashCode());

		if (this.allowedActions != null) {
			hash += 31 * this.allowedActions.hashCode();
		}
		hash += 31 * this.form.hashCode();

		hash += 31 * this.itemType.hashCode();
		hash += 31 * this.status.hashCode();
		hash += 31 * this.builderName.hashCode();

		return hash;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation performs a deep copy of the attributes of another Item
	 * into the current Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherItem
	 *            <p>
	 *            The Item from which information should be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(Item otherItem) {
		// begin-user-code

		// Return if otherItem is null
		if (otherItem == null) {
			return;
		}

		// Copy contents into super and current object
		// Do the copy
		this.uniqueId = otherItem.uniqueId;
		this.itemName = otherItem.itemName;
		this.itemDescription = otherItem.itemDescription;
		this.action = otherItem.action;
		this.allowedActions = (ArrayList<String>) otherItem.allowedActions
				.clone();
		this.form.copy(otherItem.form); // Deep copy form.
		this.itemType = otherItem.itemType;
		this.project = otherItem.project;
		this.status = otherItem.status;
		this.builderName = otherItem.builderName;

		// Re-run setupEntries list for Item. Everything must be re-registered
		// in a new Registry so that IUpdateable objects are not updated from
		// multiple Items.
		setupEntryList();

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation provides a deep copy of the Item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A clone of the Item.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code
		// Create a new instance, copy contents, and return it

		// create a new instance of Item and copy contents
		Item item = new Item();
		item.copy(this);
		return item;

		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads the SerializedItem from a Painfully Simple Form file
	 * format. If it is unable to load the InputStream or determines that the
	 * contents of the stream are not consistent with the PSF format, then it
	 * will throw an IOException. It delegates the actual work to a
	 * PainfullySimpleForm.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param inputStream
	 *            <p>
	 *            The InputStream that contains a PSF file.
	 *            </p>
	 * @throws IOException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadFromPSF(InputStream inputStream) throws IOException {
		// begin-user-code

		// Local Declarations
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		ArrayList<String> inputLines = new ArrayList<String>();
		String line = null;

		// Read the contents of the stream so long as it is not null
		if (inputStream != null) {
			// Instantiate the Readers
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			// Load the lines
			while ((line = bufferedReader.readLine()) != null) {
				inputLines.add(line);
			}
			// Just forward the load to the Form, which is always a
			// PainfullySimpleForm in this class.
			((PainfullySimpleForm) form).loadFromPSF(inputLines);
			// Setup the Entry list and register dependencies
			setupEntryList();
			// Set the Item name and description
			setName(form.getName());
			System.out.println("Form name = " + form.getName());
			setDescription(form.getDescription());
			// Get the ItemType - just a linear search for now
			for (String i : inputLines) {
				if (i.startsWith("formType=")) {
					// Clear out comments
					String[] typeStringWithoutComments = i.split("\\s+");
					// Split on equals and parse the type
					itemType = ItemType.valueOf(typeStringWithoutComments[0]
							.split("=")[1].trim());
					break;
				}
			}
		} else {
			throw new IOException(
					"PSF cannot be loaded from a null InputStream!");
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the Item is already associated with a
	 * project space that it can use for storing and retrieving files. That is,
	 * it returns true if the non-nullary constructor was called or setProject()
	 * has been called.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if the Item has a project, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean hasProject() {
		// begin-user-code

		// Return true if the Project is set
		return (project != null);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the project for the Item. It should only be called
	 * after the Item is constructed with its nullary constructor. Calling it
	 * after the Item has been running for a time could lead to unintended
	 * consequences.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse Platform IProject that should be referenced for
	 *            project space information by this Item.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setProject(IProject projectSpace) {
		// begin-user-code

		// Set the project so long as it is not null
		if (projectSpace != null) {
			project = projectSpace;
			// Setup the output file
			setupOutputFile();
		}

		// FIXME - SHOULD THIS ONLY BE ALLOWED TO BE CALLED ONCE??? ~JJB
		// 20120502 14:01

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up a master list of Entries that are managed by the
	 * Item base class to handle dependencies and accelerate dependency
	 * checking. It is called by the non-nullary constructor and the loadFrom*
	 * operations. It also add the components to the Item's component map.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void setupEntryList() {
		// begin-user-code

		// Local Declarations
		int numComps = 0;

		// Setup the list of Entries. This list is recreated every time this
		// operation is called.
		entryList = new ArrayList<Entry>();

		// Setup the map of components
		componentMap = new HashMap<String, ArrayList<Component>>();

		// If the Form is not null, then we need to add the Entries to the
		// master list and configure the dependences
		if (form != null) {
			// Setup the component map
			componentMap.put("data", new ArrayList<Component>());
			componentMap.put("output", new ArrayList<Component>());
			componentMap.put("table", new ArrayList<Component>());
			// Get the number of components
			numComps = form.getNumberOfComponents();
			// Only register dependencies if there is work to be done.
			if (numComps > 0) {
				registerUpdateables();
			}
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation adds a Component to the component map with the specified
	 * key (data, output or table). It is called by the visit() operations that
	 * the Item realizes to satisfy the IComponentVisitor interface.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 *            <p>
	 *            The Component to insert into the map of Components.
	 *            </p>
	 * @param key
	 *            <p>
	 *            The key that identifies the type of the Component, equal to
	 *            one of "data," "output" or "table."
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void addComponentToMap(Component component, String key) {
		// begin-user-code

		// Local Declarations
		ArrayList<Component> components = null;

		// Add the DataComponent to the map
		if (component != null) {
			// Grab the list of DataComponents
			components = this.componentMap.get(key);
			// Add the Component
			components.add(component);
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation instantiates the output file.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void setupOutputFile() {
		// begin-user-code

		// Setup the output file handle name
		String outputFilename = form.getName().replaceAll("\\s+", "_") + "_"
				+ getId() + "_processOutput.txt";
		// Get the file handle from the project space. Note that it may not
		// actually exist.
		if (project != null) {
			// Get the file
			IFile outputFileHandle = project.getFile(outputFilename);
			outputFile = outputFileHandle.getLocation().toFile();
			// Create a new file if it does not already exist
			try {
				outputFile.createNewFile();
			} catch (Exception fileFailException) {
				System.out.println("Item Message: Unable to create output "
						+ "file in workspace. Aborting.");
				fileFailException.printStackTrace();
				return;
			}
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a list of files in the current project space with
	 * the given type or all of the files in the project space if no type is
	 * selected (type = null) if and only if the project space is available. If
	 * the project space is not available, it will return null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param type
	 *            <p>
	 *            The file extension or type of the file that should be
	 *            discovered in the project or null if all files should be
	 *            returned by this operation.
	 *            </p>
	 * @return <p>
	 *         The names of the files in the project space with the given type
	 *         or names of all of the files in the project space if no type is
	 *         specified.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<String> getProjectFileNames(String type) {
		// begin-user-code

		ArrayList<String> files = null, allFiles = null;

		// Make sure that the project is available
		if (project != null && project.isAccessible()) {
			allFiles = new ArrayList<String>();
			// Get the files from the project space
			try {
				// Load the file names into the list
				IResource[] resources = project.members();
				for (int i = 0; i < resources.length; i++) {
					// Only load files, not directories
					if (resources[i].getType() == IResource.FILE) {
						allFiles.add(resources[i].getName());
					}
				}
				// Remove the files that are of the wrong type if and only if a
				// type has been selected
				if (type != null && !type.isEmpty()) {
					files = new ArrayList<String>();
					int size = allFiles.size();
					// Check all the files
					for (int i = 0; i < size; i++) {
						String fileName = allFiles.get(i);
						if (fileName.endsWith(type)) {
							// Add the correct ones to the list
							files.add(fileName);
						}
					}
				} else {
					// Return all the files
					files = allFiles;
				}
			} catch (CoreException e) {
				// Complain
				System.out.println("Item Message: "
						+ "Unable to load project files!");
				e.printStackTrace();
			}
		}

		return files;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation directs the Item to reload data that it has read from its
	 * project. Calling this operation signifies to the Item that new files have
	 * been added or old files have been updated in the project.
	 * </p>
	 * <p>
	 * Calling this operation does not refresh the IProject.
	 * </p>
	 * <p>
	 * This operation is meant to be overridden by subclasses and customized. It
	 * does nothing on the base class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reloadProjectData() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation adds a listeners to the Item's set of listeners.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param listener
	 *            <p>
	 *            The new listener that is subscribing to the Item for updates.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addListener(ItemListener listener) {
		// begin-user-code

		if (listener != null) {
			listeners.add(listener);
		}
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation notifies the listeners of a change in the IProject,
	 * normally do to a newly created or deleted resource.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void notifyListenersOfProjectChange() {
		// begin-user-code

		// Notify all of the listeners that they should reload their project
		// data because the project has been changed somehow.
		for (ItemListener listener : listeners) {
			listener.reloadProjectData();
		}

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the Eclipse IFolder that points to the preferences
	 * directory for this Item. This operation will try to create the directory
	 * in the project if the project exists. It will return null if the project
	 * space doesn't exist or it can't create the directory.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The directory where preferences should be stored.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected IFolder getPreferencesDirectory() {
		// begin-user-code

		// Local Declarations
		String folderName = getName().replaceAll("\\s+", "_");
		IFolder folder = null;

		// Only try to folder if the project exists
		if (project != null) {
			folder = project.getFolder(folderName);
			// Create the folder if it doesn't exist
			if (!folder.exists()) {
				try {
					folder.create(true, true, null);
				} catch (CoreException e) {
					// Complain
					e.printStackTrace();
				}
			}
		}

		return folder;

		// end-user-code
	}

	/**
	 * This utility method can be used by subclasses to refresh the project
	 * space after the addition or removal of files and folders.
	 */
	protected void refreshProjectSpace() {
		// Refresh the Project just in case
		if (project != null) {
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return;
	}

	/**
	 * <p>This method is intended to discover and create {@link ICEResource} 
	 * objects (and the {@link VizResource} subclass) that are associated to
	 * the Item in some way. For example, a CSV post-processing file that can
	 * be plotted.</p>
	 * 
	 * <p>This method takes in a file path, and then delegates its work to the 
	 * Item's {@link ResourceHandler}.</p>
	 * 
	 * @param filePath		The file path of the Item's resource.
	 * @return				Returns an {@link ICEResource} or 
	 * 						{@link VizResource} depending on the file extension
	 * 						of the file path. If the file path was invalid, 
	 * 						returns null.
	 * @throws IOException
	 */
	public ICEResource getResource(String filePath) throws IOException {
	
		// Local declarations
		ICEResource resource = null;
		
		// Call the ResourceHandler method to get the resource
		resource = resourceHandler.getResource(filePath);
		
		return resource;
		
	}
	
	/**
	 * <p>This method is similar to {@link #getResource(String)}, except that
	 * it takes in an {@link Entry} instead. This is a special case where
	 * a resource might be stored on the Item's Form (for example, a FileEntry
	 * for a mesh file).</p>
	 * 
	 * <p>This method simply calls {@link #getResource(String)}. If the Entry's
	 * associated file (obtained by {@link Entry#getValue()}) is found in the
	 * default ICE workspace, then it will pass the fully-qualified path name
	 * into {@link #getResource(String)}. Otherwise, it will pass just the file
	 * name (without a path), which will result in a null resource.</p>
	 * 
	 * @param file		The file path of the Item's resource.
	 * @return			Returns an {@link ICEResource} or 
	 * 					{@link VizResource} depending on the file extension
	 * 					of the file path. If the file path was invalid, 
	 * 					returns null.		
	 * @throws IOException
	 */
	public ICEResource getResource(Entry file) throws IOException {
		
		// Local declarations
		ICEResource resource = null;
		String filePath = file.getValue();
		String defaultFilePath = "";
		
		// Check if the file is in the default workspace. If it is, get the 
		// fully qualified path
		if (project != null) {
			defaultFilePath = project.getLocation().toOSString() 
					+ System.getProperty("file.separator") + file.getValue();
		}
		File defaultFile = new File(defaultFilePath);
		if (defaultFile != null && defaultFile.exists()) {
			filePath = defaultFilePath;
		}
		
		// Call the other getResource method
		resource = getResource(filePath);
		
		// Register the resource
		registry.register(resource, "resource");

		return resource;
	}
	
	
	/**
	 * Return a list of files with the provided fileExtension String. The files
	 * are returned as a list of 'file names'. For example, the file
	 * /path/to/file.txt is returned as file.txt.
	 * 
	 * @param directory
	 *            The directory where the Item should search for files with the
	 *            given type
	 * @param fileExtension
	 *            The file extension that the Item should search for.
	 * @return A list of file names found with the given fileExtension. For a
	 *         file at /path/to/file.txt, this list contains the element
	 *         file.txt
	 */
	protected ArrayList<String> getFiles(String directory, String fileExtension) {

		// Local Declarations
		ArrayList<String> files = new ArrayList<String>();

		// Refresh the Project just in case
		refreshProjectSpace();

		// Make sure we were given a valid directory
		if (Files.isDirectory(Paths.get(directory))) {
			// Read through the directory searching for files with the
			// given file extension.
			try (DirectoryStream<Path> directoryStream = Files
					.newDirectoryStream(Paths.get(directory))) {
				for (Path path : directoryStream) {
					if (path.toString().endsWith(fileExtension)) {
						files.add(path.toFile().getName());
					}
				}

				// Refresh the Project just in case
				refreshProjectSpace();

			} catch (IOException ex) {
				ex.printStackTrace();
				files.clear();
			}
		}

		return files;
	}

	/**
	 * Copy the file with the name 'fileName' from the source directory given by
	 * the sourceDir absolute path String to the destination directory given by
	 * the absolute path String. This method creates an exact copy of the file
	 * in the destination directory, leaving the source file intact.
	 * 
	 * @param sourceDir
	 *            The absolute path for the source directory.
	 * @param destinationDir
	 *            The absolute path for the destination directory
	 * @param fileName
	 *            The name of the file to be copied.
	 */
	protected void copyFile(String sourceDir, String destinationDir,
			String fileName) {

		// Local Declarations
		String separator = System.getProperty("file.separator");

		// Make sure this file exists...
		if (Files.exists(Paths.get(sourceDir + separator + fileName))) {
			try {
				// Try to copy the file from the source directory to the target
				// directory. This leaves the source file intact.
				Files.copy(Paths.get(sourceDir + separator + fileName),
						Paths.get(destinationDir + separator + fileName),
						StandardCopyOption.REPLACE_EXISTING);
				// Refresh the Project just in case
				refreshProjectSpace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return;
	}

	/**
	 * Move the file with the name 'fileName' from the source directory given by
	 * the sourceDir absolute path String to the destination directory given by
	 * the absolute path String. This method creates an exact copy of the file
	 * in the destination directory, but removes the file from the source
	 * directory.
	 * 
	 * @param sourceDir
	 *            The absolute path for the source directory.
	 * @param destinationDir
	 *            The absolute path for the destination directory.
	 * @param fileName
	 *            The name of the file to be moved.
	 */
	protected void moveFile(String sourceDir, String destinationDir,
			String fileName) {

		// Local Declarations
		String separator = System.getProperty("file.separator");

		// Make sure the file to be moved is valid.
		if (Files.exists(Paths.get(sourceDir + separator + fileName))) {
			try {
				// Move the file, this deletes the file in sourceDir.
				Files.move(Paths.get(sourceDir + separator + fileName),
						Paths.get(destinationDir + separator + fileName),
						StandardCopyOption.REPLACE_EXISTING);
				// Refresh the Project just in case
				refreshProjectSpace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
	}

	/**
	 * This method deletes the directory, and its contents, corresponding to the
	 * given absolute path String.
	 * 
	 * @param directory
	 *            The absolute path of the directory to be deleted.
	 */
	protected void deleteDirectory(String directory) {

		// Make sure the given absolute path is a directory
		if (Files.isDirectory(Paths.get(directory))) {
			try {

				// Walk the directory tree, deleting all the files it contains.
				Files.walkFileTree(Paths.get(directory),
						new SimpleFileVisitor<Path>() {
							@Override
							public FileVisitResult visitFile(
									Path file,
									java.nio.file.attribute.BasicFileAttributes attrs)
									throws IOException {
								Files.delete(file);
								return FileVisitResult.CONTINUE;
							}

							@Override
							public FileVisitResult postVisitDirectory(Path dir,
									IOException exc) throws IOException {
								Files.delete(dir);
								return FileVisitResult.CONTINUE;
							}

						});

				// Refresh the Project just in case
				refreshProjectSpace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method serves as a utility for moving multiple files with the same
	 * file extension from one directory to another.
	 * 
	 * @param sourceDir
	 *            The directory where the Item should search for files with the
	 *            given type
	 * @param destinationDir
	 *            The directory where the Item should move the found files
	 * @param fileExtension
	 *            The file extension that the Item should search for.
	 */
	protected void moveFiles(String sourceDir, String destinationDir,
			String fileExtension) {
		for (String fileName : getFiles(sourceDir, fileExtension)) {
			moveFile(sourceDir, destinationDir, fileName);
		}

		return;
	}

	/**
	 * This method serves as a utility for copying multiple files with the same
	 * file extension from one directory to another.
	 * 
	 * @param sourceDir
	 *            The directory where the Item should search for files with the
	 *            given type
	 * @param destinationDir
	 *            The directory where the Item should move the found files
	 * @param fileExtension
	 *            The file extension that the Item should search for.
	 */
	protected void copyFiles(String sourceDir, String destinationDir,
			String fileExtension) {
		for (String fileName : getFiles(sourceDir, fileExtension)) {
			copyFile(sourceDir, destinationDir, fileName);
		}
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation loads data into the Item from an input file. This
	 * operation should be overridden by subclasses and specialized for the
	 * correct behavior. The implementation on the base class does nothing.
	 * </p>
	 * <p>
	 * Subclasses that override this operation should make sure that a failed
	 * load does not result in a partially initialized or incorrect Form and an
	 * erroneous Item state.
	 * </p>
	 * <p>
	 * This operation expects that the file is in the workspace and only needs
	 * its name to find it.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param input
	 *            <p>
	 *            The file containing the input that should be loaded. It should
	 *            be a file in the project space.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void loadInput(String input) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation updates the Item to let it know that a particular event
	 * has occurred in an ICE subsystem, remote ICE subsystem or external
	 * third-party process.
	 * </p>
	 * <p>
	 * The base class takes care of a small amount of worked related to
	 * messages, namely writing them to the process log, and subclasses should
	 * override this operation to specialize the behavior. Subclasses should
	 * still call the operation on the base class (via super.update(msg)) from
	 * their overridden operation so that the message can be properly logged.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param msg
	 *            <p>
	 *            The incoming Message.
	 *            </p>
	 * @return <p>
	 *         True if the Item was able to respond to the Message, false
	 *         otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean update(Message msg) {
		// begin-user-code

		// Dump the text to stdout if we are in debugging mode.
		if (debuggingEnabled) {
			System.out.println("Item Message: Received update message!");
			System.out.println("Item Message: Id = " + msg.getId());
			System.out.println("Item Message: Item Id = " + msg.getItemId());
			System.out.println("Item Message: Content = " + msg.getMessage());
			System.out.println("Item Message: Type = " + msg.getType());
		}

		// Just return true for now until the logging functionality can be moved
		// from JobLauncher to Item.
		return true;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the Item's builderName. This operation can only be
	 * called once. Although this operation is public, it should only be called
	 * in the respective ItemBuilder class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param builderName
	 *            <p>
	 *            The builderName to be set. Can not be null or the empty
	 *            string.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setItemBuilderName(String builderName) {
		// begin-user-code

		// If the passed parameter is not null, not an empty string, and the
		// builder name has not been set.
		if (builderName != null && !(builderName.trim().isEmpty())
				&& (this.builderName.isEmpty())) {

			// Set the name
			this.builderName = builderName;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the Item's builderName. This operation can only be
	 * called once. Although this operation is public, it should only be called
	 * in the respective ItemBuilder class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         Returns the builder name. This can be null.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getItemBuilderName() {
		// begin-user-code
		return this.builderName;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation disables the Item. Disabled Items will not accept changes
	 * to their Forms and they cannot be processed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param state
	 *            <p>
	 *            True if the Item is disabled, false if not.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disable(boolean state) {
		// begin-user-code

		// Enable is the opposite of disabled.
		enabled = !state;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the Item is enabled, false if it is disabled.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if the Item is enabled, false if not.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isEnabled() {
		// begin-user-code
		return enabled;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns a file handle to the output file for the Item. It
	 * returns a handle to the file whether or not it actually exists and
	 * clients should check the File.exists() operation before attempting to
	 * manipulate the file. The description of the output file can be found
	 * elsewhere in the class documentation. This file handle returned is the
	 * <i>real</i> file handle and can be written, but clients should be careful
	 * to only read from the file. Nullerizing the file handle will not
	 * nullerize it in the Item. If the output file for the Item has not been
	 * configured, this operation will return null.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The output file for this Item, thoroughly documented elsewhere.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public File getOutputFile() {
		// begin-user-code

		// Local Declarations
		File copiedFileHandle = null;

		// Return another handle to the file
		if (outputFile != null) {
			copiedFileHandle = new File(outputFile.toURI());
		}
		return copiedFileHandle;
		// end-user-code
	}

	/**
	 * This operation returns the IO service for subclasses without giving them
	 * access to the private handle.
	 * 
	 * @return The IOService instance
	 */
	protected IOService getIOService() {
		return ioService;
	}

	/**
	 * This operation returns the ActionFactory for subclasses without giving
	 * them access to the private handle.
	 * 
	 * @return The ActionFactory
	 */
	protected IActionFactory getActionFactory() {
		return actionFactory;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(DataComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(DataComponent component) {
		// begin-user-code

		// Add the Component to the map of components
		addComponentToMap(component, "data");

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(ResourceComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(ResourceComponent component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TableComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(TableComponent component) {
		// begin-user-code

		// Add the Component to the map of components
		addComponentToMap(component, "table");

		return;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MatrixComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(MatrixComponent component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(IShape component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(IShape component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(GeometryComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(GeometryComponent component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MasterDetailsComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(MasterDetailsComponent component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(TreeComposite component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(TreeComposite component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(IReactorComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(IReactorComponent component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	@Override
	public void visit(TimeDataComponent component) {

		// Visit the DataComponent
		this.visit((DataComponent) component);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IComponentVisitor#visit(MeshComponent component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void visit(MeshComponent component) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	@Override
	public void visit(AdaptiveTreeComposite component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EMFComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(IUpdateable component) {
		// Leave this for subclasses.
	}

	@Override
	public void visit(ListComponent component) {
		// TODO Auto-generated method stub

	}
}
