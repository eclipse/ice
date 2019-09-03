/**
 * /*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands;

/**
 * This enumerator provides a status for Commands, in particular in relation to whether 
 * or not a particular command executed properly (or not). Several member functions 
 * throughout this Commands API use CommandStatus to check whether or not a particular
 * step in executing a command was correctly completed.
 * 
 * The following descriptions are for the various enumerated values declared below
 * SUCCESS    - The job has finished successfully. This is only set when ProcessBuilder returns an
 * 			    exit value of 0, indicating that the job completed normally.
 * PROCESSING - The job is currently processing and setting up information necessary to be
 * 			    able to run, for example, in preparing the CommandConfiguration member variables
 * 				for job execution.
 * RUNNING    - The job is currently running. This is set after configuration is completed correctly
 * 			    and is maintained until the job completes or fails.
 * INFOERROR  - This indicates that there was some information that was not provided that the
 * 			    job needs to run, for example, if the executable could not be found in the
 * 			    working directory in which the user specified.
 * FAILED     - The job failed. This status is set if the ProcessBuilder returned an atypical
 * 			    exit value (anything other than 0). It is also set if there is any error that
 * 			    is registered during the job running.
 * CANCELED   - The job has been canceled by the user - this is never set unless specified by the 
 * 			    user specifically.
 * @author Joe Osborn
 *
 */




public enum CommandStatus{
	SUCCESS, PROCESSING, RUNNING, INFOERROR, FAILED, CANCELED;
}
