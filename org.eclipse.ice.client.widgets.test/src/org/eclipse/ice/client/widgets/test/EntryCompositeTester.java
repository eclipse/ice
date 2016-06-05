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

import org.eclipse.ice.client.widgets.IEntryComposite;
import org.eclipse.ice.client.widgets.StringEntryComposite;
import org.eclipse.january.form.IEntry;
import org.eclipse.january.form.StringEntry;
import org.eclipse.swt.SWT;
import org.junit.Test;
import org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester;

/**
 * This class provides some basic UI testing for the {@link EntryComposite}.
 * 
 * @author Jordan Deyton
 *
 */
public class EntryCompositeTester extends AbstractSWTTester {

	// TODO Update this test to do a little more than it does. We might not want
	// to share the same EntryComposite between test cases, either.

	/**
	 * The shared {@link Entry} that will be represented by the Entry
	 * {@link #composite}.
	 */
	private static IEntry entry;

	/**
	 * The shared {@link EntryComposite} that will be tested.
	 */
	private static IEntryComposite composite;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractUITester#beforeAllTests()
	 */
	@Override
	public void beforeAllTests() {
		super.beforeAllTests();

		// Initialize static or otherwise shared resources here.
		// Set up a new Entry.
		entry = new StringEntry();

		// Create the composite that will be tested.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				composite = new StringEntryComposite(getShell(), entry,
						SWT.NONE);
			}
		});

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractUITester#beforeEachTest()
	 */
	@Override
	public void beforeEachTest() {
		super.beforeEachTest();

		// Initialize per-test resources here.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractUITester#afterEachTest()
	 */
	@Override
	public void afterEachTest() {
		// Dispose per-test resources here.

		// Proceed with the default post-test cleanup.
		super.afterEachTest();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractUITester#afterAllTests()
	 */
	@Override
	public void afterAllTests() {
		// Dispose static or otherwise shared resources here.

		// Dispose the composite.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				composite.getComposite().dispose();
			}
		});
		composite = null;

		// Dispose the Entry.
		entry = null;

		// Proceed with the default post-tests cleanup.
		super.afterAllTests();
	}

	/**
	 * Checks that the {@link Entry} is correctly set for the
	 * {@code EntryComposite}.
	 */
	@Test
	public void checkEntry() {
		assertSame(entry, composite.getEntry());
	}
}