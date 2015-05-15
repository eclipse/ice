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
package org.eclipse.ice.kdd;

import org.eclipse.ice.analysistool.IAnalysisTool;
import static org.eclipse.ice.kdd.KDDAnalysisDocument.*;
import org.eclipse.ice.analysistool.IAnalysisDocument;

import java.net.URI;

import org.eclipse.ice.kdd.kddstrategy.KDDStrategyFactory;

import org.eclipse.ice.analysistool.IDataProvider;

/**
 * <p>
 * The KDDAnalysisTool is a realization of the ICE IAnalysisTool interface, and
 * is responsible for creating a KDDAnalysisDocument that performs and displays
 * various types of data clustering and anomaly detection for nuclear reactors.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class KDDAnalysisTool implements IAnalysisTool {
	/**
	 * <p>
	 * The name of this AnomalyDetectionTool.
	 * </p>
	 * 
	 */
	private String name;
	/**
	 * <p>
	 * A boolean flag indicating whether or not this tool is ready for use in
	 * ICE
	 * </p>
	 * 
	 */
	private Boolean ready;
	/**
	 * <p>
	 * The version number of this AnomalyDetectionTool.
	 * </p>
	 * 
	 */
	private String version;

	/**
	 * <p>
	 * Reference to the OSGi created strategy factory to be used in the
	 * KDDAnalysisDocument.
	 * </p>
	 * 
	 */
	private KDDStrategyFactory strategyFactory;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param name
	 *            <p>
	 *            The name to be used for this KDDAnalysisTool
	 *            </p>
	 * @param version
	 *            <p>
	 *            The version of this KDDAnalysisTool
	 *            </p>
	 */
	public KDDAnalysisTool(String name, String version) {
		this.name = name;
		this.version = version;
		ready = true;
	}

	/**
	 * <p>
	 * The nullary constructor.
	 * </p>
	 * 
	 */
	public KDDAnalysisTool() {
		name = "Knowledge Discovery and Data Mining ICE Analysis Tool";
		version = "1.0";
		ready = true;
	}

	/**
	 * 
	 * @param factory
	 */
	public void registerStrategyFactory(KDDStrategyFactory factory) {
		if (factory != null) {
			System.out
					.println("KDDAnalysisTool Message: Registering new KDDStrategyFactory.");
			strategyFactory = factory;
		} else {
			System.out
					.println("KDDAnalysisTool Message: Invalid KDDStrategyFactory instance.");
			strategyFactory = null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#close()
	 */
	public Boolean close() {
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#createDocument(URI data)
	 */
	public IAnalysisDocument createDocument(URI data) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#getVersion()
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#isReady()
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#createDocument(IDataProvider data)
	 */
	public IAnalysisDocument createDocument(IDataProvider data) {

		// Create an IAnalysisDocument reference
		IAnalysisDocument document;

		// If the strategy factory is not null, then use
		// it, since it is from the OSGi framework
		// and has been populated with
		// the available strategies
		if (strategyFactory != null) {
			document = new KDDAnalysisDocument(strategyFactory);
		} else {
			document = new KDDAnalysisDocument();
		}

		// Load the given data, return null
		// if the load didn't work.
		if (document.loadData(data)) {
			return document;
		} else {
			return null;
		}

	}
}