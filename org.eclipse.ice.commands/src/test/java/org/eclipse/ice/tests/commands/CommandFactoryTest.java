/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.tests.commands;

import org.eclipse.ice.commands.CommandFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.CommandFactory}.
 * 
 * @author Jay Jay Billings
 *
 */
public class CommandFactoryTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.CommandFactory#CommandFactory()}.
	 */
	@Test
	public void testCommandFactory() {
		CommandFactory factory = new CommandFactory();
	}

	/**
	 * Test method for {@link org.eclipse.ice.commands.CommandFactory#get()}.
	 */
	@Test
	public void testGetCommand() {
		CommandFactory factory = new CommandFactory();
		
		//Command theCommand = factory.getCommand(command, host)
	}

}
