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

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.ice.commands.EmailUpdateHandler;
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
	 * @throws IOException
	 */
	@Test
	public void testEmailNotificationPostUpdate() throws IOException {
		
		// Get a text file with credentials
		String credFile = "/tmp/email-creds.txt";
		if(System.getProperty("os.name").toLowerCase().contains("win"))
			credFile = "C:\\Users\\Administrator\\email-creds.txt";
		
		String email = "";
		String password = "";
		String host = "";
		
		File file = new File(credFile);
		try(Scanner scanner = new Scanner(file)) {
			email = scanner.next();
			password = scanner.next();
			host = scanner.next();
		}
		
		EmailUpdateHandler updater = new EmailUpdateHandler();
		// Just send an email to itself
		updater.setEmailAddress(email);
		updater.setPassword(password);
		updater.setSmtpHost(host);
		updater.setMessage("This is a test updater");
		updater.setSubject("This is a test subject");
		updater.postUpdate();
		
		// If no exception is thrown, it completed correctly
	}
	
	/**
	 * Tests bad credential error throwing
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void testEmailNotificationPostUpdateBadCreds() throws IOException {
		
		String email = "badEmail";
		String password = "badPassword";
		String host = "some.smtp.com";
		EmailUpdateHandler updater = new EmailUpdateHandler();
		// Just send an email to itself
		updater.setEmailAddress(email);
		updater.setPassword(password);
		updater.setSmtpHost(host);
		updater.setMessage("Bad email");
		updater.setSubject("This is a bad email");
		updater.postUpdate();
		// Expect exception
	}
	

}
