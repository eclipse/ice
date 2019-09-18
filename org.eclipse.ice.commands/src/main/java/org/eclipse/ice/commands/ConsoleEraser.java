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
 * This is a simple class which extends thread and is used to erase characters that
 * are printed to the console. The primary use is for erasing characters when one
 * is entering their password.
 * @author Joe Osborn
 *
 */
public class ConsoleEraser extends Thread {

	/**
	 * This is a boolean indicating that the thread should erase
	 */
	private boolean running = true;

	/**
	 * This function runs the erasing and tells the console to print a backspace
	 * followed by a space, thus "covering up" the previous character entered
	 */
	@Override
	public void run() {

		// While we want to erase
		while (running) {
			// Print out a backspace + space to "delete" the previous character
			System.out.print("\b ");

			// Wait for the next character
			try {
				Thread.currentThread().sleep(1);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	/**
	 * This function calls the thread to stop erasing by switching the boolean to
	 * false
	 */
	public synchronized void stopErasing() {
		running = false;
	}
}