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

import org.eclipse.ice.analysistool.IAnalysisTool;
import org.eclipse.ice.analysistool.IAnalysisDocument;
import org.eclipse.ice.analysistool.IDataProvider;

import java.net.URI;

/**
 * <p>
 * A fake implementation of IAnalysisTool that does not require any external
 * capabilities to run. It is used to test the ReactorAnalyzerBuilder and the
 * ReactorAnalyzer in a lightweight fashion.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class FakeAnalysisTool implements IAnalysisTool {
	/**
	 * <p>
	 * True if the document was created, false otherwise.
	 * </p>
	 * 
	 */
	private boolean documentCreated;
	/**
	 * <p>
	 * A reference to the FakeAnalysisDocument that is fed to clients by this
	 * FakeAnalysisTool.
	 * </p>
	 * 
	 */
	private FakeAnalysisDocument analysisDoc;

	/**
	 * <p>
	 * This operation resets the FakeAnalysisTool to its initial state.
	 * </p>
	 * 
	 */
	public void reset() {

		// Perform a factory reset. ;)
		analysisDoc = null;
		documentCreated = false;

	}

	/**
	 * <p>
	 * True if the IAnalysisDocument was retrieved, false otherwise.
	 * </p>
	 * 
	 * @return
	 */
	public boolean documentRetrieved() {
		return documentCreated;
	}

	/**
	 * <p>
	 * This operation returns the FakeAnalysisDocument that was delivered by the
	 * FakeAnalysisTool.
	 * </p>
	 * 
	 * @return <p>
	 *         The FakeAnalysisDocument used to fake out the ReactorAnalyzer.
	 *         </p>
	 */
	public FakeAnalysisDocument getDocument() {

		return analysisDoc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#close()
	 */
	public Boolean close() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#getName()
	 */
	public String getName() {
		return "Fake Analysis Tool";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#getVersion()
	 */
	public String getVersion() {
		return "1.0.0";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#createDocument(URI data)
	 */
	public IAnalysisDocument createDocument(URI data) {

		// Create the document if necessary
		if (analysisDoc == null) {
			analysisDoc = new FakeAnalysisDocument();
		}

		return analysisDoc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisTool#isReady()
	 */
	public boolean isReady() {
		return true;
	}

	@Override
	public IAnalysisDocument createDocument(IDataProvider data) {
		// TODO Auto-generated method stub
		return null;
	}
}