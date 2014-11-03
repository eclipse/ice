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
package org.eclipse.ice.visit.viewer.test;

import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.AnalysisAssetType;
import org.eclipse.ice.datastructures.form.Entry;

import java.util.ArrayList;
import java.util.Properties;
import java.net.URI;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A fake implementation of IAnalysisAsset that does not require any external
 * capabilities to run. It is used to test the VisItViewerBuilder and the
 * VisItViewer in a lightweight fashion.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeAnalysisAsset implements IAnalysisAsset {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the properties of the asset were updated, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean propertiesUpdated;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation resets the FakeAnalysisAsset to its initial state.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reset() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the properties of this asset were updated, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if the properties were updated, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean propertiesUpdated() {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getName() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getType()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public AnalysisAssetType getType() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getProperty(String key)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getProperty(String key) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#setProperty(String key, String value)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setProperty(String key, String value) {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#resetProperties()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void resetProperties() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getProperties()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Properties getProperties() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}
}