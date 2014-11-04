/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.jobprofile.JobProfile;
import org.eclipse.ice.item.jobprofile.JobProfileBuilder;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the JobProfileBuilder. It checks to
 * make sure that JobProfileBuilder returns the correct Item name and Item type.
 * In also tries to build a new JobProfile and check it.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */

public class JobProfileBuilderTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private JobProfileBuilder jobProfileBuilder;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Item type and Item name of the
	 * JobProfileBuilder.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkJobProfileInformation() {
		// begin-user-code

		// create a JobProfileBuilder
		jobProfileBuilder = new JobProfileBuilder();

		// Check the Item type and name. The JobProfile is a Model.
		assertEquals(ItemType.Model, jobProfileBuilder.getItemType());
		assertEquals("Job Profile", jobProfileBuilder.getItemName());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates a JobProfile and checks it to make sure it was
	 * actually built properly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkJobProfile() {
		// begin-user-code

		// create a JobProfileBuilder
		jobProfileBuilder = new JobProfileBuilder();

		// build
		JobProfile jobProfile = (JobProfile) jobProfileBuilder.build(null);

		// Check item builder name
		assertEquals(jobProfileBuilder.getItemName(),
				jobProfile.getItemBuilderName());

		// check to make sure it was build properly.
		assertNotNull(jobProfile.getForm());
		assertEquals("Create a Job Launcher", jobProfile.getForm()
				.getActionList().get(0));

		// Check the actions
		assertEquals(1, jobProfile.getAvailableActions().size());
		assertEquals("Create A JobLauncher", jobProfile.getAvailableActions()
				.get(0));
		// Make sure the JobProfile is returning the correct type.
		assertEquals(ItemType.Model, jobProfile.getItemType());

		// end-user-code
	}
}