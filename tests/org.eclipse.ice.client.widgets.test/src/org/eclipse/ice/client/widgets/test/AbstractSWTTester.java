/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation 
 *   
 *******************************************************************************/
package org.eclipse.ice.client.widgets.test;

import java.util.concurrent.CountDownLatch;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * This abstract class provides a basic framework for performing SWTBot tests
 * for an SWT application inside a JUnit test framework. As such, test classes
 * sub-classing this abstract class can be run as JUnit tests, JUnit plug-in
 * tests (on or off the UI thread), or SWTBot tests.
 * <p>
 * A {@link Shell} is opened (outside the default Eclipse workbench if used as a
 * plug-in test). This shell provides the "sandbox" in which UI tests are
 * performed. For instance, to test a specific {@code Composite}, you may create
 * an instance of said {@code Composite} in the shell and perform its UI tests.
 * The shell is shared among sub-class tests, and can be acquired via
 * {@link #getShell()}.
 * </p>
 * <p>
 * An {@link SWTBot} is opened for each test class instance and should be used
 * for the actual UI tests. The bot can be acquired via {@link #getBot()}.
 * </p>
 * <p>
 * <b>Note:</b> Changes to the UI must be coordinated with the main UI thread
 * using {@link Display#syncExec(Runnable)} or
 * {@link Display#asyncExec(Runnable)}. To get the shell's display, you may call
 * {@link #getDisplay()}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractSWTTester extends AbstractICEUITester<SWTBot> {

	/**
	 * The shell used when creating an {@code SWTBot} for unit tests.
	 */
	private static Shell shell;

	/**
	 * This latch is used to wait on the creation of the {@link #shell}.
	 */
	private static CountDownLatch shellLatch = new CountDownLatch(1);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractUITester#createBot()
	 */
	@Override
	protected SWTBot createBot() {
		// Wait for the CountDownLatch to hit zero, signifying that the shell
		// has been created and opened.
		try {
			shellLatch.await();
		} catch (InterruptedException e) {
			fail("AbstractICESWTTester error: "
					+ "Thread interrupted while waiting for test Shell!");
		}
		// We can now create the SWTBot for the shell.
		return new SWTBot(shell);
	}

	/**
	 * Gets the main shell for testing SWT widgets "outside" (although they may
	 * be embedded in) the Eclipse plug-in infrastructure. This should contain
	 * top-level SWT {@code Composite}s that will be tested.
	 * 
	 * @return The main shell for testing.
	 */
	protected static Shell getShell() {
		return shell;
	}

	/**
	 * Creates the shared test shell on the UI thread. This decrements the
	 * {@link #shellLatch} when the shell is opened.
	 */
	@BeforeClass
	public static void beforeSWTClass() {

		final Display display = getDisplay();
		// We have to run this *synchronously* on the UI thread. If the JUnit
		// tests are being run from the UI thread, running *a*synchronously can
		// cause a deadlock because it will wait for the CountDownLatch to hit
		// zero before creating the SWTBot.
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				// Create and open the shell.
				shell = new Shell(display);
				shell.open();

				// Notify any blocking threads that the shell was created.
				shellLatch.countDown();
			}
		});

		return;
	}

	/**
	 * Closes and unsets the {@link #shell} and resets the {@link #shellLatch}.
	 */
	@AfterClass
	public static void afterSWTClass() {

		// Close the shell. This must be done on the UI thread.
		getDisplay().syncExec(new Runnable() {
			public void run() {
				shell.close();
			}
		});

		// Reset the latch and shell.
		shellLatch = new CountDownLatch(1);
		shell = null;

		return;
	}
}