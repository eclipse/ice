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
package org.eclipse.ice.reactorAnalyzer.test;

import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.AnalysisAssetType;
import org.eclipse.ice.datastructures.form.Entry;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.net.URI;

/**
 * <p>
 * A fake implementation of IAnalysisAsset that does not require any external
 * capabilities to run. It is used to test the ReactorAnalyzerBuilder and the
 * ReactorAnalyzer in a lightweight fashion.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class FakeAnalysisAsset implements IAnalysisAsset {
	/**
	 * <p>
	 * True if the properties of the asset were updated, false otherwise.
	 * </p>
	 * 
	 */
	private boolean propertiesUpdated;

	/**
	 * <p>
	 * This operation resets the FakeAnalysisAsset to its initial state.
	 * </p>
	 * 
	 */
	public void reset() {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>
	 * True if the properties of this asset were updated, false otherwise.
	 * </p>
	 * 
	 * @return <p>
	 *         True if the properties were updated, false otherwise.
	 *         </p>
	 */
	public boolean propertiesUpdated() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getType()
	 */
	public AnalysisAssetType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getProperty(String key)
	 */
	public String getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#setProperty(String key, String value)
	 */
	public boolean setProperty(String key, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#resetProperties()
	 */
	public void resetProperties() {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getProperties()
	 */
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getURI()
	 */
	public URI getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisAsset#getPropertiesAsEntryList()
	 */
	public ArrayList<Entry> getPropertiesAsEntryList() {
		// TODO Auto-generated method stub
		return null;
	}
}