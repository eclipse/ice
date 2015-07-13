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
package org.eclipse.ice.analysistool;

import java.net.URI;

/**
 * <p>
 * IAnalysisTools are tools that can be used to analyze data. There is one
 * instance of each IAnalysisTool in ICE and they are responsible for creating
 * IAnalysisDocument for their respective tool. Each IAnalysisTool should have a
 * name and a version number that are unique so that they can be distinguished
 * from each other.
 * </p>
 * 
 * @author Alex McCaskey
 */
public interface IAnalysisTool {
	/**
	 * <p>
	 * This operation allows subclasses to perform shutdown operations when they
	 * are required by a back-end analysis service (such as VisIt) to close the
	 * running processes.
	 * </p>
	 * 
	 * @return
	 */
	public Boolean close();

	/**
	 * <p>
	 * This operation returns the name of the analysis tool.
	 * </p>
	 * 
	 * @return <p>
	 *         The name of the analysis tool.
	 *         </p>
	 */
	public String getName();

	/**
	 * <p>
	 * This operation returns the version number of the analysis tool.
	 * </p>
	 * 
	 * @return <p>
	 *         The version number, as a string, for this analysis tool.
	 *         </p>
	 */
	public String getVersion();

	/**
	 * <p>
	 * This operation create a new IAnalysisDocument for the data at the given
	 * URI.
	 * </p>
	 * 
	 * @param data
	 *            <p>
	 *            A URI to a folder or file that contains data which should be
	 *            analyzed in the IAnalysisDocument.
	 *            </p>
	 * @return <p>
	 *         A new analysis document for the data provided by this analysis
	 *         tool. This IAnalysisDocument is made to work with this analysis
	 *         tool only.
	 *         </p>
	 */
	public IAnalysisDocument createDocument(URI data);

	/**
	 * <p>
	 * This operation notifies a client if the IAnalysisTool is ready to be
	 * used.
	 * </p>
	 * 
	 * @return <p>
	 *         True if the tool is ready to be used, false otherwise.
	 *         </p>
	 */
	public boolean isReady();

	/**
	 * <p>
	 * This operation creates a new IAnalysisDocument for the data within the
	 * given IDataProvider realization.
	 * </p>
	 * 
	 * @param data
	 *            <p>
	 *            A concrete realization of IDataProvider that contains data
	 *            which should be analyzed in the IAnalysisDocument.
	 *            </p>
	 * @return <p>
	 *         A new analysis document for the data provided by this analysis
	 *         tool. This IAnalysisDocument is made to work with this analysis
	 *         tool only.
	 *         </p>
	 */
	public IAnalysisDocument createDocument(IDataProvider data);
}