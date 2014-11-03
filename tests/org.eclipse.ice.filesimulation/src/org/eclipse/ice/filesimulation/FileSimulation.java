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
package org.eclipse.ice.filesimulation;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.jobLauncher.JobLauncher;

/** 
 * <!-- begin-UML-doc -->
 * <p>The FileSimulation class launches the "File" command a Linux system to determine the type of a specified file. It requires the name of a file and, when launched with ICE, the identity of a local or remote machine. The only actions available for this class are local and remote job launch.</p><p>It defines two entries, one for the filename and one for the computing platform, and one data component in its Form.</p>
 * <!-- end-UML-doc -->
 * @author jaybilly
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@XmlRootElement(name = "FileSimulation")
public class FileSimulation extends JobLauncher {
	
	/**
	 * Default constructor that delegates to the project constructor with a null argument.
	 */
	public FileSimulation() {
		this(null);
	}
	
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The constructor.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FileSimulation(IProject projectSpace) {
		// begin-user-code

		// Call the super class constructor
		super(projectSpace);

		// end-user-code
	}

	/**
	 * This operation creates a Form for the FileSimulation with two Entries,
	 * one for the file name and one for the computing platform, and one
	 * DataComponent.
	 * 
	 * @see Item#setupForm()
	 */
	protected void setupForm() {
		// begin-user-code
		
		// Set the name and description of the Item
		setName("File Command Launcher");
		setDescription("A launcher for the "
				+ "\"File\" system command on Linux. "
				+ "This command is used to determine "
				+ "the type and other properties of a file.");
				
		// Setup the Form
		super.setupForm();
		
		// Setup the executable information
		setExecutable(getName(),getDescription(),"file ${inputFile} " +
				"${secondFile} ${thirdFile}");
		
		// Add a couple of hosts
		addHost("antecessor.ornl.gov","linux x86_64","/usr/bin");
		addHost("habilis.ornl.gov","linux x86_64","/usr/bin");
		addHost("merovech.ornl.gov","linux x86_64","/usr/bin");
		addHost("neanderthalensis.ornl.gov","linux x86_64","/usr/bin");
		addHost("localhost.localdomain","linux x86_64","/usr/bin");

		// Add a second and third input file type
		addInputType("Second Input File", "secondFile", "The Second File", null);
		addInputType("Third Input File", "thirdFile", "The Third File", null);
		
		return;
		// end-user-code
	}

}