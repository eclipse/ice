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
package org.eclipse.ice.client.widgets;

import org.eclipse.ui.forms.IDetailsPageProvider;

import static org.eclipse.ice.client.widgets.ICEDataComponentDetailsPage.*;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ui.forms.IDetailsPage;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class implements the IDetailsPageProvider interface to provide a Details pages for a MasterDetailsBlock. The only way to provide the MasterDetailsComponent handle for this class is through the constructor.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEDetailsPageProvider implements IDetailsPageProvider {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The MasterDetailsComponent which should be queried for DataComponents to add to DetailsPages.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private MasterDetailsComponent component;

	/**
	 * The id of the master whose details need specification.
	 */
	private int masterId;

	/**
	 * The FormEditor that should be notified when it is time to save.
	 */
	private ICEFormEditor formEditor;

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The constructor. If the is null, this class will be unable to render anything to the screen.</p>
	 * <!-- end-UML-doc -->
	 * @param masterDetailsComponent <p>The MasterDetailsComponent that has the DataComponents that will be used as Details blocks in the MasterDetails table.</p>
	 * @param editor <p>The ICEFormEditor in which the pages will exist. This editor is marked as dirty when the page changes.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEDetailsPageProvider(
			MasterDetailsComponent masterDetailsComponent, ICEFormEditor editor) {
		// begin-user-code

		component = masterDetailsComponent;
		formEditor = editor;

		// end-user-code
	};

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the component that is used to gather details information or null if it was not set in the constructor.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The MasterDetailsComponent used in this provider or null if it was not set.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public MasterDetailsComponent getComponent() {
		// begin-user-code
		return component;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IDetailsPageProvider#getPageKey(Object object)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object getPageKey(Object object) {
		// begin-user-code
		return (String) object;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IDetailsPageProvider#getPage(Object key)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IDetailsPage getPage(Object key) {
		// begin-user-code

		// Local declarations
		ICEDataComponentDetailsPage ICEDataComponentDetailsPage = null;

		String keyId = key.toString().split(" ")[0];

		if (keyId != null) {
			// Implement switch to get the corresponding page to any key.
			ICEDataComponentDetailsPage = new ICEDataComponentDetailsPage(
					component.getDetails(Integer.parseInt(keyId)), formEditor);
		}

		return ICEDataComponentDetailsPage;

		// end-user-code
	}
}