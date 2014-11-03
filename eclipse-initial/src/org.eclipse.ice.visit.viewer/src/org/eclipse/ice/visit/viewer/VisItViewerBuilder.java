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
package org.eclipse.ice.visit.viewer;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IAnalysisTool;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class is the ItemBuilder for the VisitViewer. It constructs a VisitViewer when build() is called if and only if at least one IAnalysisTool is available to do the analysis work for the analyzer, otherwise it will return null when build() is called.</p><p>The name and type of the Item created by this builder are set as final, static variables on this class. Just something I'm trying out for convenience...</p>
 * <!-- end-UML-doc -->
 * @author s4h
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisItViewerBuilder implements ItemBuilder {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The set of IAnalysisTools available for the VisitViewer.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IAnalysisTool> analysisTools;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The name of the VisitViewer.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static final String name = "VisIt Viewer";

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The type of the Item that this builder will create.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static final ItemType type = ItemType.AnalysisSession;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The constructor.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public VisItViewerBuilder() {
		// begin-user-code

		// Allocate the list of tools
		analysisTools = new ArrayList<IAnalysisTool>();

		return;

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation adds an IAnalysisTool to the list of analysis tools that is available to the VisitViewer.</p>
	 * <!-- end-UML-doc -->
	 * @param tool <p>The IAnalysisTool to add to the set.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addAnalysisTool(IAnalysisTool tool) {
		// begin-user-code

		// Add the tool
		if (tool != null) {
			System.out.println("VisitViewerBuilder: Adding Tool " + tool.getName());
			analysisTools.add(tool);
		}

		return;

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation is called by the OSGI to close the associated IAnalysisTools.  </p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void stop() {
		// begin-user-code
		
		System.out.println("VisitViewerBuilder Message: Closing Analysis Tools!");
		for(int i = 0; i < this.analysisTools.size(); i++) {
			this.analysisTools.get(i).close();
		}
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemName()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getItemName() {
		// begin-user-code
		return name;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemType()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ItemType getItemType() {
		// begin-user-code
		return type;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#build(IProject projectSpace)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Item build(IProject projectSpace) {
		// begin-user-code

		// Local Declarations
		VisItViewer analyzer = null;

		// Only build the Item if the project space is not null and there are
		// analysis tools
		if (projectSpace != null && analysisTools.size() > 0) {
			// Make the analyzer
			analyzer = new VisItViewer(projectSpace);
			
			//Set the item builder name
			analyzer.setItemBuilderName(this.getItemName());
			
			// Set the name
			analyzer.setName(name);
			// Set the analysis tools
			analyzer.setAnalysisTools(analysisTools);
		}
		
		
		
		return analyzer;

		// end-user-code
	}

}