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
package org.eclipse.ice.database.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;

/**
 * This class is a component called by the OSGi to supply the
 * org.eclipse.ice.item.test bundle the with EntityManagerFactory.
 * 
 * @author Scott F. Hull
 * 
 */

public class DatabaseItemTestComponent {
	/**
	 * The EntityManagerFactory
	 */
	static volatile EntityManagerFactory factory = null;

	/**
	 * A countdown latch for syncing the database test and the OSGi service
	 * startup.
	 */
	private static volatile CountDownLatch serviceLatch = new CountDownLatch(1);

	/**
	 * Set the EntityManagerFactory
	 */
	public static void setFactory(EntityManagerFactory emFactory) {

		System.out.println("DatabaseItemTestComponent Message: "
				+ "setFactory() called on thread "
				+ Thread.currentThread().getId());

		if (emFactory != null) {
			factory = emFactory;
			serviceLatch.countDown();
			System.out
					.println("ICE DatabaseItemTestComponent Message: EntityManagerFactory set!");
		} else {
			throw new RuntimeException(
					"ICE DatabaseItemTestComponent Message: EntityManagerFactory can not be null!");
		}
	}

	/**
	 * Get the EntityManagerFactory
	 */
	public static EntityManagerFactory getFactory() {

		try {
			serviceLatch.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out
					.println("DatabaseItemTestComponent Message:"
							+ "Interrupted while obtaining EntityManagerFactory service."
							+ " Aborting!");
			e.printStackTrace();
		}

		return factory;
	}
}
