/*******************************************************************************
 * Copyright (c) 2017 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alex McCaskey - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package apps;

import apps.impl.EnvironmentCommandLineParser;

/**
 * 
 * @author Alex McCaskey
 *
 */
public class ICEAppStore {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Local Declarations
		EnvironmentCommandLineParser cliParser = new EnvironmentCommandLineParser(args);
		cliParser.execute();
	}

}
