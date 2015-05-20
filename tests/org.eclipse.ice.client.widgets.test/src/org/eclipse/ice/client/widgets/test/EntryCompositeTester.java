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

import org.eclipse.ice.client.widgets.EntryComposite;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.swt.SWT;
import org.junit.Test;

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
	private Entry entry;

	/**
	 * The shared {@link EntryComposite} that will be tested.
	 */
	private EntryComposite composite;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.test.AbstractICEUITester#setupTests()
	 */
	@Override
	protected void setupTests() {
		// Set up a new Entry.
		entry = new Entry();

		// Create the composite that will be tested.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				composite = new EntryComposite(getShell(), SWT.NONE, entry);
			}
		});

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.test.AbstractICEUITester#cleanupTests()
	 */
	@Override
	protected void cleanupTests() {

		// Dispose the composite.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				composite.dispose();
			}
		});
		composite = null;

		// Dispose the Entry.
		entry = null;

		return;
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