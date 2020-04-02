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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.ice.commands.Command;
import org.eclipse.ice.commands.CommandConfiguration;
import org.eclipse.ice.commands.CommandFactory;
import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.TxtFileConnectionAuthorizationHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the implementation of Email notification handling for jobs
 * as defined in the EmailHandler class
 * 
 * @author Joe Osborn
 *
 */
public class HTTPHandlerTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHTTPNotification() {
		fail("Not yet implemented");
		// Get the hostname for your local computer
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String hostname = addr.getHostName();
		
		HTTPCommandUpdateHandler updater = new HTTPCommandUpdateHandler("www.example.com/update");
		
		String pwd = System.getProperty("user.dir") + "/src/test/java/org/eclipse/ice/tests/commands/";

		// Make a simple command configuration to just run an ls -lrt
		CommandConfiguration cmdCfg = new CommandConfiguration();
		cmdCfg.setNumProcs("1");
		cmdCfg.setInstallDirectory("");
		cmdCfg.setWorkingDirectory(pwd);
		cmdCfg.setOS(System.getProperty("os.name"));
		cmdCfg.setExecutable("ls -lrt");

		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			// Add powershell interpeter if os is windows
			cmdCfg.setInterpreter("powershell.exe");
			// just use ls because powershell automatically adds the -lrt
			// and doesn't know what -lrt is anyway
			cmdCfg.setExecutable("ls");
		}
		cmdCfg.setInstallDirectory("");
		cmdCfg.setAppendInput(false);
		cmdCfg.setCommandId(1);
		cmdCfg.setErrFileName("someLsErrFile.txt");
		cmdCfg.setOutFileName("someLsOutFile.txt");
		cmdCfg.setHTTPUpdateHandler(updater);
		// Make the connection configuration
		ConnectionConfiguration ctCfg = new ConnectionConfiguration();
		ConnectionAuthorizationHandler handler = new TxtFileConnectionAuthorizationHandler();
		handler.setHostname(hostname);
		ctCfg.setAuthorization(handler);

		// Get and run the command
		CommandFactory factory = new CommandFactory();
		Command cmd = null;
		try {
			cmd = factory.getCommand(cmdCfg, ctCfg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CommandStatus status = cmd.execute();

		// Check that it properly finished
		assertEquals(CommandStatus.SUCCESS, status);
		
		// Check that the HTTP address is valid and populated
		assertEquals("check http address?");
	
	}

}
