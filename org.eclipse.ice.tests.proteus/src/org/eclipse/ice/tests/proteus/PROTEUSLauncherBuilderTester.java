/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.tests.proteus;

import static org.junit.Assert.*;

import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.proteus.PROTEUSLauncher;
import org.eclipse.ice.proteus.PROTEUSLauncherBuilder;
import org.junit.Test;

/**
 * Class that tests PROTEUSLauncherBuilder methods.
 * @author Anna Wojtowicz
 *
 */
public class PROTEUSLauncherBuilderTester {
	
	/**
	 * Tests the getItemName() and getItemType() methods.
	 * @author Anna Wojtowicz
	 * 
	 */
	@Test
	public void checkGetters() {
		
		// Set up default values to test against
		String defaultName = "PROTEUS Launcher";
		ItemType defaultType = ItemType.Simulation;
		
		// Create a PROTEUS launcher builder to test
		PROTEUSLauncherBuilder builder = new PROTEUSLauncherBuilder();
		
		// Test the default values
		assertEquals(defaultName, builder.getItemName());
		assertEquals(defaultType, builder.getItemType());
	}
	
	/**
	 * Tests the build() method that actually constructs a PROTEUSLauncher.
	 * @author Anna Wojtowicz
	 * 
	 */	
	@Test
	public void checkBuild() {
		
		// Default Launcher and LauncherBuilders to test
		PROTEUSLauncherBuilder launcherBuilder = new PROTEUSLauncherBuilder();
		PROTEUSLauncher launcher = new PROTEUSLauncher();
		
		// Build the launcher
		launcher = (PROTEUSLauncher) launcherBuilder.build(null);
		
		// Check their builder names match
		assertEquals(launcher.getItemBuilderName(), launcherBuilder.getItemName());
	}
}
