/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.tests.renderer;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Launcher {

	public static void main(String[] args) {
		
	    Injector injector = Guice.createInjector(new BasicModule());
	    RendererRunner runner  = injector.getInstance(RendererRunner.class);
		
		try {
			runner.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
