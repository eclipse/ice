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

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;

/** 
 * <p>This class implements the IDetailsPageProvider interface to provide a Details pages for a MasterDetailsBlock. The only way to provide the MasterDetailsComponent handle for this class is through the constructor.</p>
 * @author Jay Jay Billings
 */
public class ICEDetailsPageProvider implements IDetailsPageProvider {
	/** 
	 * <p>The MasterDetailsComponent which should be queried for DataComponents to add to DetailsPages.</p>
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
	 * <p>The constructor. If the is null, this class will be unable to render anything to the screen.</p>
	 * @param masterDetailsComponent <p>The MasterDetailsComponent that has the DataComponents that will be used as Details blocks in the MasterDetails table.</p>
	 * @param editor <p>The ICEFormEditor in which the pages will exist. This editor is marked as dirty when the page changes.</p>
	 */
	public ICEDetailsPageProvider(
			MasterDetailsComponent masterDetailsComponent, ICEFormEditor editor) {

		component = masterDetailsComponent;
		formEditor = editor;

	};

	/** 
	 * <p>This operation returns the component that is used to gather details information or null if it was not set in the constructor.</p>
	 * @return <p>The MasterDetailsComponent used in this provider or null if it was not set.</p>
	 */
	public MasterDetailsComponent getComponent() {
		return component;
	}

	/** 
	 * (non-Javadoc)
	 * @see IDetailsPageProvider#getPageKey(Object object)
	 */
	@Override
	public Object getPageKey(Object object) {
		return (String) object;
	}

	/** 
	 * (non-Javadoc)
	 * @see IDetailsPageProvider#getPage(Object key)
	 */
	@Override
	public IDetailsPage getPage(Object key) {

		// Local declarations
		ICEDataComponentDetailsPage ICEDataComponentDetailsPage = null;

		String keyId = key.toString().split(" ")[0];

		if (keyId != null) {
			// Grab the right details
			DataComponent detailsComp = component.getDetails(Integer.parseInt(keyId));
			ICEDataComponentDetailsPage = new ICEDataComponentDetailsPage(
					detailsComp, formEditor);
		}

		return ICEDataComponentDetailsPage;

	}
}