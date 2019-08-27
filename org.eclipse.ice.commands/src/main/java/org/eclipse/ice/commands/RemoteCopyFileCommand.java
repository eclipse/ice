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

import java.nio.file.Paths;

/**
 * Child class for copying a file remotely over some connection. 
 * @author Joe Osborn
 *
 */
public class RemoteCopyFileCommand extends CopyFileCommand {

	/**
	 * Default constructor
	 */
	public RemoteCopyFileCommand() {
		
	}
	
	
	/**
	 * Constructor which sets the two paths, source and destination,
	 * to those given by the arguments of the constructor. See 
	 * {@link org.eclipse.ice.tests.commands.CopyFileCommand} for member
	 * variable descriptions.
	 * @param src 
	 * @param dest
	 */
	public RemoteCopyFileCommand(String src, String dest) {
		source = Paths.get(src);
		destination = Paths.get(dest);
		copy();
	}
	
	@Override
	protected void copy() {
		return;
	}
	
}
