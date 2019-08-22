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


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.CommandFactory}.
 * 
 * @author Jay Jay Billings, Joe Osborn
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
	 * Test method for {@link org.eclipse.ice.commands.CommandFactory#getCommand()}.
	 */
	@Test

	public void testGetLocalCommand() {
		CommandFactory factory = new CommandFactory();
		
		AtomicBoolean localjob = new AtomicBoolean();
		localjob.set( true );
		
		HashMap<String, String> executableDictionary = new HashMap<String, String>();
		executableDictionary.put( "executable" , "someExecutable.sh ${installDir}" );
		executableDictionary.put( "inputFile" , "someInputFile.txt" );
		executableDictionary.put( "stdOutFileName",  "someOutFile.txt" );
		executableDictionary.put( "stdErrFileName",  "someErrFile.txt" );
		executableDictionary.put( "installDir" ,  "~/install");
		executableDictionary.put( "numProcs",  "1");
		
		CommandConfiguration commandConfig = new CommandConfiguration(
				1, localjob, executableDictionary, true );
		
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		String hostname = addr.getHostName();
		
		Command localCommand = CommandFactory.getCommand(hostname, commandConfig);
		
		localCommand.execute();
		
	}

}
