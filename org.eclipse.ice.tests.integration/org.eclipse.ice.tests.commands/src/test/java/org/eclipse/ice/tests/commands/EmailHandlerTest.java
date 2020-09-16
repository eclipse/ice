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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.ice.commands.EmailUpdateHandler;
import org.eclipse.ice.commands.TxtFileConnectionAuthorizationHandler;
import org.eclipse.ice.tests.util.data.TestDataPath;
import org.junit.Test;

/**
 * This class tests the implementation of Email notification handling for jobs
 * as defined in the EmailHandler class
 *
 * @author Joe Osborn
 *
 */
public class EmailHandlerTest {

	/**
	 * Tests successful email notification posting
	 *
	 * @throws IOException
	 */
	@Test
	public void testEmailNotificationPostUpdate() {

		// Get a text file with credentials
		TestDataPath dataPath = new TestDataPath();
		String credFile = dataPath.resolve("commands/ice-email-creds.txt").toString();

		TxtFileConnectionAuthorizationHandler handler = new TxtFileConnectionAuthorizationHandler();
		handler.setOption(credFile);

		EmailUpdateHandler updater = new EmailUpdateHandler();
		// Just send an email to itself
		updater.setCredHandler(handler);
		updater.setMessage("This is a test updater");
		updater.setSubject("This is a test subject");
		updater.setMailPort("587"); // gmail mail port
		try {
			updater.postUpdate();
		} catch (IOException e) {
			// If exception is thrown, test failed
			e.printStackTrace();
			fail("testEmailNotificationPostUpdate failed");
		}
	}

	/**
	 * Tests bad credential error throwing
	 *
	 */
	@Test
	public void testEmailNotificationPostUpdateBadCreds()  {
		// Just make up some dummy file that doesn't actually exist
		String  credFile = "/tmp/dumFile.txt";
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			credFile = "C:\\Users\\Administrator\\dumFile.txt";

		TxtFileConnectionAuthorizationHandler handler = new TxtFileConnectionAuthorizationHandler();
		// Create and write bad values to a dummy text file
		FileWriter file;
		try {
			file = new FileWriter(credFile);
			file.write("badHost\nsomeEmail\nbaddPass");
			file.close();
		} catch (IOException e) {
			System.out.println("Couldn't create file to run test");
			e.printStackTrace();
		}

		handler.setOption(credFile);

		EmailUpdateHandler updater = new EmailUpdateHandler();
		// Setup a bad credential file
		updater.setCredHandler(handler);
		updater.setMessage("Bad email");
		updater.setSubject("This is a bad email");
		try {
			updater.postUpdate();
			/// IF it worked, the test failed
			fail("The test worked, and it should have caught an exception!");
		} catch (IOException e) {
			System.out.println("Exception correctly caught");
			e.printStackTrace();
		}

		// Delete the dummy file we made
		File fileDel = new File(credFile);
		fileDel.delete();


	}

}
