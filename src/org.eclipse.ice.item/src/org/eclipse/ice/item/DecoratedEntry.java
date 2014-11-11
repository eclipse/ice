/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.eclipse.ice.datastructures.form.Entry;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The DecoratedEntry class is used by the Registry to manage updates that need
 * to be sent to the Entry. It partially decorates the functionality of the
 * Entry class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 */
class DecoratedEntry extends Entry {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Entry that is to be decorated.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	private Entry entry;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the Entry should be updated, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	private boolean updateFlag;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param dEntry
	 *            <p>
	 *            The Entry that should be decorated.
	 *            </p>
	 */
	public DecoratedEntry(Entry dEntry) {
		// begin-user-code
		this.entry = dEntry;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation marks the DecoratedEntry for an update.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param newState
	 *            <p>
	 *            If newState is true, the Entry will be marked to be updated.
	 *            If it is false, the Entry any updates queued for the Entry
	 *            will be set to false.
	 *            </p>
	 */
	public void setUpdateFlag(boolean newState) {
		// begin-user-code
		this.updateFlag = newState;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the DecoratedEntry is marked to be updated
	 * and false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if the Entry is marked for an update, false otherwise.
	 *         </p>
	 */
	public boolean getUpdateFlag() {
		// begin-user-code
		// TODO Auto-generated method stub
		return this.updateFlag;
		// end-user-code
	}

	/**
	 * Override the Entry.update() operation to provide the decorated behavior.
	 */
	@Override
	public void update(String updatedKey, String newValue) {
		// If the update flag is set to true, then the Entry should be updated.
		if (this.updateFlag) {

			return;
		}
	}
}