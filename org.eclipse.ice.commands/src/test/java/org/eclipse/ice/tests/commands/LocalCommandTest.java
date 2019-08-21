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

import static org.junit.Assert.fail;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.LocalCommand;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for class {@link org.eclipse.ice.commands.LocalCommand}.
 * @author Joe Osborn
 *
 */
public class LocalCommandTest {

	// Create some class instances of a LocalCommand example
	// and dictionary with executable information to go along with it
	LocalCommand testCommand = new LocalCommand();
	Dictionary<String, String> testDictionary = new Hashtable<String, String>();
	
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
	
	
	/**
	 * Test the various methods as defined in this LocalCommandTest class
	 */
	@Test
	public void test() {
		testLaunch();
	}
	
	/**
	 * Test for method {@link org.eclipse.ice.commands.LocalCommand()}
	 */
	
	public void testLocalCommand() {
		
		// Test the default constructor
		LocalCommand defaultLocalCommand = new LocalCommand();
		
		System.out.println(defaultLocalCommand.GetConfiguration().GetCommandId());
		
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Execute(Dictionary)}
	 */
	/*
	@Test
	public void testExecute() {
		fail("Not yet implemented");
	}
	*/
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Launch()}
	 */
	@Test
	public void testLaunch() {
	
		
		// Test that, in the lack of dictionary setting, Launch returns a 
		// CommandStatus error
		CommandStatus testStatus = testCommand.Launch();
		assert(testStatus == CommandStatus.INFOERROR);
		
		
		
		
		
		
	}
	
	/**
	 * Test method for {@link org.eclipse.ice.commands.LocalCommand#Run()}
	 */
	public void testRun() {
		fail("Not yet implemented");
	}
	
}
