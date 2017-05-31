/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.tests.ice.item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.item.jobLauncher.JobLauncherForm;
import org.eclipse.ice.item.jobLauncher.SuiteLauncher;
import org.junit.Test;

/**
 * This class is responsible for testing the SuiteLauncher.
 * 
 * @author Jay Jay Billings
 * 
 */
public class SuiteLauncherTester {

	/**
	 * This operation makes sure that the executables can be dynamically
	 * configured on the SuiteLauncher.
	 */
	@Test
	public void checkSettingExecutables() {
		// Create the launcher
		SuiteLauncher launcher = new SuiteLauncher(null);

		// Create the list of executables
		ArrayList<String> execs = new ArrayList<String>();
		execs.add("conj_ht");
		execs.add("conj_ht_moab");
		execs.add("testExec");

		// Set the list
		launcher.addExecutables(execs);

		// Get the list and check it
		DataComponent execComp = (DataComponent) (launcher.getForm())
				.getComponent(JobLauncherForm.parallelId + 2);
		IEntry execEntry = execComp.retrieveAllEntries().get(0);
		assertEquals(execs, execEntry.getAllowedValues());

		return;
	}

	/**
	 * This operation makes sure that SuiteLaunchers can be copied.
	 */
	@Test
	public void checkCopying() {

		// Local Declarations
		SuiteLauncher cloneItem = new SuiteLauncher(), copyItem = new SuiteLauncher();
		SuiteLauncher jobItem = new SuiteLauncher();

		// Create the list of executables
		ArrayList<String> execs = new ArrayList<String>();
		execs.add("conj_ht");
		execs.add("conj_ht_moab");
		execs.add("testExec");

		// Set the launcher details
		jobItem.setDescription("I am a job!");
		jobItem.setExecutable("LS", "/opt/bin", "DOIT!!!!");

		// Set the list
		jobItem.addExecutables(execs);

		// run clone operations
		cloneItem = (SuiteLauncher) jobItem.clone();

		// check contents
		assertEquals(jobItem.getAvailableActions(),
				cloneItem.getAvailableActions());
		assertEquals(jobItem.getDescription(), cloneItem.getDescription());
		assertTrue(jobItem.getForm().equals(cloneItem.getForm()));
		assertEquals(jobItem.getId(), cloneItem.getId());
		assertEquals(jobItem.getItemType(), cloneItem.getItemType());
		assertEquals(jobItem.getName(), cloneItem.getName());
		assertEquals(jobItem.getStatus(), cloneItem.getStatus());
		assertEquals(jobItem.getAllHosts(), cloneItem.getAllHosts());

		// Get the executable list and check it
		DataComponent execComp = (DataComponent) cloneItem.getForm()
				.getComponent(JobLauncherForm.parallelId + 2);
		IEntry execEntry = execComp.retrieveAllEntries().get(0);
		assertEquals(execs, execEntry.getAllowedValues());

		// run copy operation
		copyItem.copy(jobItem);

		// check contents
		assertEquals(jobItem.getAvailableActions(),
				copyItem.getAvailableActions());
		assertEquals(jobItem.getDescription(), copyItem.getDescription());
		assertTrue(jobItem.getForm().equals(copyItem.getForm()));
		assertEquals(jobItem.getId(), copyItem.getId());
		assertEquals(jobItem.getItemType(), copyItem.getItemType());
		assertEquals(jobItem.getName(), copyItem.getName());
		assertEquals(jobItem.getStatus(), copyItem.getStatus());
		assertEquals(jobItem.getAllHosts(), copyItem.getAllHosts());

		// Get the executable list and check it
		execComp = (DataComponent) copyItem.getForm().getComponent(
				JobLauncherForm.parallelId + 2);
		execEntry = execComp.retrieveAllEntries().get(0);
		assertEquals(execs, execEntry.getAllowedValues());

		// run copy operation by passing null
		copyItem.copy(null);

		// check contents - nothing has changed
		assertEquals(jobItem.getAvailableActions(),
				copyItem.getAvailableActions());
		assertEquals(jobItem.getDescription(), copyItem.getDescription());
		assertTrue(jobItem.getForm().equals(copyItem.getForm()));
		assertEquals(jobItem.getId(), copyItem.getId());
		assertEquals(jobItem.getItemType(), copyItem.getItemType());
		assertEquals(jobItem.getName(), copyItem.getName());
		assertEquals(jobItem.getStatus(), copyItem.getStatus());
		assertEquals(jobItem.getAllHosts(), copyItem.getAllHosts());

		// Get the executable list and check it
		execComp = (DataComponent) copyItem.getForm().getComponent(
				JobLauncherForm.parallelId + 2);
		execEntry = execComp.retrieveAllEntries().get(0);
		assertEquals(execs, execEntry.getAllowedValues());

		return;
	}

	/**
	 * This operation makes sure that SuiteLaunchers can be accurately checked
	 * for Equality.
	 */
	@Test
	public void checkEquals() {
		// Create JobLauncherItems to test
		SuiteLauncher item = new SuiteLauncher();
		SuiteLauncher equalItem = new SuiteLauncher();
		SuiteLauncher unEqualItem = new SuiteLauncher();
		SuiteLauncher transitiveItem = new SuiteLauncher();

		// Create the list of executables
		ArrayList<String> execs = new ArrayList<String>();
		execs.add("conj_ht");
		execs.add("conj_ht_moab");
		execs.add("testExec");

		// Set ids
		equalItem.setId(item.getId());
		transitiveItem.setId(item.getId());
		unEqualItem.setId(2);

		// Set names
		equalItem.setName(item.getName());
		transitiveItem.setName(item.getName());
		unEqualItem.setName("DC UnEqual");

		// Set executables
		item.addExecutables(execs);
		equalItem.addExecutables(execs);
		transitiveItem.addExecutables(execs);
		unEqualItem.addExecutables(null);

		// Assert two equal Items return true
		assertTrue(item.equals(equalItem));

		// Assert two unequal Items return false
		assertFalse(item.equals(unEqualItem));

		// Assert equals() is reflexive
		assertTrue(item.equals(item));

		// Assert the equals() is Symmetric
		assertTrue(item.equals(equalItem) && equalItem.equals(item));

		// Assert equals() is transitive
		if (item.equals(equalItem) && equalItem.equals(transitiveItem)) {
			assertTrue(item.equals(transitiveItem));
		} else {
			fail();
		}

		// Assert equals is consistent
		assertTrue(item.equals(equalItem) && item.equals(equalItem)
				&& item.equals(equalItem));
		assertTrue(!item.equals(unEqualItem) && !item.equals(unEqualItem)
				&& !item.equals(unEqualItem));

		// Assert checking equality with null is false
		assertFalse(item==null);

		// Assert that two equal objects return same hashcode
		assertTrue(item.equals(equalItem)
				&& item.hashCode() == equalItem.hashCode());

		// Assert that hashcode is consistent
		assertTrue(item.hashCode() == item.hashCode());

		// Assert that hashcodes from unequal objects are different
		assertTrue(item.hashCode() != unEqualItem.hashCode());
	}

}
