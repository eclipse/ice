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
package org.eclipse.eavp.viz.service.mesh.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * This class provides an {@link ISection} that displays general properties
 * about {@link IMeshPart}s. This includes their name, description, and ID.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class GeneralInfoSection extends AbstractPropertySection {

	/**
	 * The {@link IMeshPart} whose properties are being exposed.
	 */
	protected AbstractController object;

	// ---- General Info widgets ---- //
	/**
	 * A Text field for the object's name.
	 */
	private Text nameText;
	/**
	 * A Text field for the object's description.
	 */
	private Text descText;
	/**
	 * A Text field for the object's ID.
	 */
	private Label idLabel;
	// ------------------------------ //

	/**
	 * A List of disposable widgets used in the Section.
	 */
	private final List<Control> disposableControls = new ArrayList<Control>();

	/**
	 * The default constructor.
	 */
	public GeneralInfoSection() {
		// Nothing to do yet.
		return;
	}

	/**
	 * Creates the controls displayed in the section. It needs to at least
	 * initialize {@link #nameText}, {@link #descText}, and {@link #idLabel}.
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		// Call the super method first.
		super.createControls(parent, aTabbedPropertySheetPage);

		// Get the background color.
		Color bg = parent.getBackground();

		// Create a Composite to contain the location information.
		Composite composite = new Group(parent, SWT.NONE);
		disposableControls.add(composite);
		composite.setLayout(new GridLayout(2, false));
		composite.setBackground(bg);

		// Create a label for the name field.
		Label label = new Label(composite, SWT.CENTER);
		disposableControls.add(label);
		label.setText("Name:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		label.setBackground(bg);

		// Create nameText.
		nameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		disposableControls.add(nameText);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		// TODO Make this modifiable.
		nameText.setEnabled(false);

		// Create a label for the description field.
		label = new Label(composite, SWT.CENTER);
		disposableControls.add(label);
		label.setText("Description:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		label.setBackground(bg);

		// Create descText.
		descText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		disposableControls.add(descText);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Add a listener to modify the IMeshPart's description when the
		// description Text field is changed.
		descText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (object != null) {
					object.setProperty("Description", descText.getText());
				}
				return;
			}
		});

		// Create a label for the ID field.
		label = new Label(composite, SWT.CENTER);
		disposableControls.add(label);
		label.setText("ID:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		label.setBackground(bg);

		// Create idText.
		idLabel = new Label(composite, SWT.LEFT);
		disposableControls.add(idLabel);
		idLabel.setText("N/A");
		idLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		idLabel.setBackground(bg);

		return;
	}

	/**
	 * This should take an {@link IMeshPart} from the current selection and set
	 * it as the {@link #object} whose properties are exposed.
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);

		// First, make sure the selection is valid. It should be some type of
		// IStructuredSelection.
		Assert.isTrue(selection instanceof IStructuredSelection);
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		// TODO Incorporate a selection of multiple mesh parts.

		// For now, we are only dealing with the first available element in the
		// selection.
		if (!structuredSelection.isEmpty()) {
			Object element = structuredSelection.getFirstElement();
			Assert.isTrue(element instanceof MeshSelection);
			MeshSelection meshSelection = (MeshSelection) element;

			// Convert the selected IMeshPart to an ICEObject.
			object = meshSelection.selectedMeshPart;
		}

		return;
	}

	/**
	 * This should dispose of any resources used by this section.
	 */
	@Override
	public void dispose() {
		// Reset the object reference.
		object = null;

		// Dispose of all disposable controls. If they are in the list, they
		// were created in createControls.
		for (Control control : disposableControls) {
			if (!control.isDisposed()) {
				control.dispose();
			}
		}
		// Clear the list so that they are not disposed again.
		disposableControls.clear();

		return;
	}

	/**
	 * This should update all of the widgets that show data from the associated
	 * {@link IMeshPart}.
	 */
	@Override
	public void refresh() {
		// If there is a valid object, refresh all of the editable controls.
		if (object != null) {
			// Update the Text fields with their appropriate values.
			nameText.setText(object.getProperty("Name"));

			// It is permissible for an object to lack a description, in which
			// case the text is simply empty
			String desc = object.getProperty("Description");
			if (desc == null) {
				desc = "";
			}

			descText.setText(desc);
			descText.setEnabled(true);
			idLabel.setText(object.getProperty("Id"));
		}
		// Otherwise, disable all of the editable controls.
		else {
			// Reset the Text fields to default values.
			nameText.setText("N/A");
			nameText.setEnabled(false);
			descText.setText("No mesh selection.");
			descText.setEnabled(false);
			idLabel.setText("N/A");
		}

		return;
	}
}
