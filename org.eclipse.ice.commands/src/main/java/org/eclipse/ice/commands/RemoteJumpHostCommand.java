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
package org.eclipse.ice.commands;

/**
 * This class allows commands to be performed with a "jump host", where the 
 * jump host contains the information required to process the command. This is
 * useful for jobs that have the following setup:
 * 
 * System A (local) --> System B (jump host) --> System C (remote host)
 * 
 * where System B has the necessary files etc. to process the job on System C.
 * @author Joe Osborn
 *
 */
public class RemoteJumpHostCommand extends Command {

	/**
	 * Constructor to instantiate a jump host command with a particular command
	 * configuration, connection configuration for the jump host and for the 
	 * final host.
	 * 
	 * @param commandConfig
	 * @param jumpConfig
	 * @param finalConnectionConfig
	 */
	public RemoteJumpHostCommand(CommandConfiguration commandConfig, ConnectionConfiguration jumpConfig,
			ConnectionConfiguration finalConnectionConfig) {
		
	}
	
	/**
	 * See {@link org.eclipse.ice.commands.Command#run()}
	 */
	@Override
	protected CommandStatus run() {
		return null;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#processJob()}
	 */
	@Override
	protected CommandStatus processJob() {
		return null;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#monitorJob()}
	 */
	@Override
	protected CommandStatus monitorJob() {
		return null;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#finishJob()}
	 */
	@Override
	protected CommandStatus finishJob() {
		return null;
	}

}
