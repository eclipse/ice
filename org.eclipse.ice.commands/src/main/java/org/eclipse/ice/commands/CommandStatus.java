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
 * @author Joe Osborn
 *
 */

public enum CommandStatus{
	SUCCESS, PROCESSING, LAUNCHING, RUNNING, INFOERROR, FAILED, CANCELED;
}


