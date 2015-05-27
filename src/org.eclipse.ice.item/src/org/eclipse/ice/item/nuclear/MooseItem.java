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
package org.eclipse.ice.item.nuclear;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.item.Item;

/**
 * 
 * @author Alex McCaskey
 *
 */
@XmlRootElement(name = "MOOSEModel")
public class MooseItem extends Item {

	/**
	 * 
	 */
	@XmlElement()
	private MOOSEModel mooseModel;

	/**
	 * 
	 */
	@XmlElement()
	private MOOSELauncher mooseLauncher;

	/**
	 * Nullary constructor.
	 */
	public MooseItem() {
		this(null);
	}

	/**
	 * Parameterized constructor.
	 */
	public MooseItem(IProject projectSpace) {
		super(projectSpace);
	}

	/**
	 */
	@Override
	protected void setupForm() {
		form = new Form();
	}

	/**
	 * Sets the information that identifies the Item.
	 */
	protected void setupItemInfo() {

		// Local declarations
		String description = "The Multiphysics Object-Oriented Simulation "
				+ "Environment (MOOSE) is a multiphysics framework developed "
				+ "by Idaho National Laboratory.";

		// Set the model defaults
		setName("MOOSE Workflow");
		setDescription(description);
		setItemBuilderName("MOOSE Workflow");

		// Setup the action list. Remove key-value pair support.
		allowedActions.remove(taggedExportActionString);
		allowedActions.remove(nativeExportActionString);
		allowedActions.add("Write MOOSE File");
		allowedActions.add("Launch the Job");
		return;
	}

	/**
	 * 
	 * @param preparedForm
	 *            The Form to review.
	 * @return The Form's status.
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {
		FormStatus modelStatus = mooseModel.reviewEntries(preparedForm);
		FormStatus launcherStatus = mooseLauncher.reviewEntries(preparedForm);
		return (modelStatus != FormStatus.ReadyToProcess
				|| launcherStatus != FormStatus.ReadyToProcess ? FormStatus.InfoError
				: FormStatus.ReadyToProcess);

	}

	/**
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.Item#process(java.lang.String)
	 */
	public FormStatus process(String actionName) {
		// Local Declarations
		FormStatus retStatus = FormStatus.InfoError;

		return retStatus;
	}

	/**
	 * 
	 * @param model
	 */
	public void setModel(MOOSEModel model) {
		if (model != null) {
			mooseModel = model;

			for (Component c : mooseModel.getForm().getComponents()) {
				form.addComponent(c);
			}
		}
	}

	/**
	 * 
	 * @param launcher
	 */
	public void setLauncher(MOOSELauncher launcher) {
		if (launcher != null) {
			mooseLauncher = launcher;
			for (Component c : mooseLauncher.getForm().getComponents()) {
				
				form.addComponent(c);
			}
		}
	}
}
