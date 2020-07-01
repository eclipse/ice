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

import java.io.IOException;

import org.eclipse.ice.commands.CommandStatus;
import org.eclipse.ice.commands.HTTPCommandUpdateHandler;
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
	 * This function tests the HTTP post logic for sending a post call with a given
	 * message, primarily used for Commands posting updates
	 * 
	 * @throws IOException
	 */
	@Test
	public void testHTTPNotificationPostUpdate(){

		HTTPCommandUpdateHandler updater = new HTTPCommandUpdateHandler();
		updater.setHTTPAddress("https://225eee153c45329a1bcedd6da637643f.m.pipedream.net");
		updater.setMessage("job finished with status " + CommandStatus.INFOERROR);

		try {
			updater.postUpdate();
		} catch (IOException e) {
			// If an exception is thrown, test failed
			e.printStackTrace();
			fail("testHTTPNotificationPostUpdate threw an exception and thus failed.");
		}

		
	}

}
