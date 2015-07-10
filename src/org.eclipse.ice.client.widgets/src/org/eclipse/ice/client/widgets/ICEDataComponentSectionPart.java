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
package org.eclipse.ice.client.widgets;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This class is a subclass of SectionPart from org.eclipse.ui.forms that
 * renders, updates and monitors an ICE TableComponent that is part of an
 * ICEForm. The ICESectionPart takes place in the IManagedForm lifecycle and
 * receives is refreshed dynamically if the underlying ICETableComponent has
 * been updated (i.e. - "gone stale" in the Eclipse parlance). The Java source
 * code for this class contains a private hashmap that is not represented in the
 * model because Jay can not figure out how to show a java.util.hashmap in RSA.
 *
 * @author Jay Jay Billings, Anna Wojtowicz
 */
public class ICEDataComponentSectionPart extends SectionPart implements
		IUpdateableListener {
	/**
	 * This attribute is a reference to an ICE DataComponent that stores the
	 * data that should be displayed by this SectionPart. The DataComponent will
	 * also update the ICESectionPart when its state changes (i.e. - becomes
	 * "stale" in the Eclipse parlance).
	 */
	private DataComponent dataComp;
	/**
	 * The Eclipse Managed Form in which the SectionPart will be rendered.
	 */
	private IManagedForm parentForm;
	/**
	 * The FormEditor that is managing all of the Pages and SectionParts.
	 */
	private ICEFormEditor editor;

	/**
	 * SectionClient for Entries and other things drawn in the Section
	 */
	private Composite sectionClient = null;

	/**
	 * The custom data component composite that is used for this section.
	 */
	private DataComponentComposite dataComposite = null;

	/**
	 * The Constructor
	 *
	 * @param section
	 *            The new Section to be managed by the ICESectionPart.
	 * @param formEditor
	 *            The FormEditor that is managing all of the Pages and
	 *            SectionParts.
	 * @param managedForm
	 *            The ManagedForm for the Section.
	 */
	public ICEDataComponentSectionPart(Section section,
			ICEFormEditor formEditor, IManagedForm managedForm) {

		// Call the parent constructor
		super(section);

		// Set the FormEditor if it is not null and throw an exception
		// otherwise.
		if (formEditor != null) {
			editor = formEditor;
		} else {
			throw new RuntimeException("Editor in ICEFormSectionPart "
					+ "constructor cannot be null.");
		}
		// Set the ManagedForm
		if (managedForm != null) {
			parentForm = managedForm;
		} else {
			throw new RuntimeException("ManagedForm in ICEFormSectionPart "
					+ "constructor cannot be null.");
		}

		return;
	}

	/**
	 * This operation reads the DataComponent assigned to this SectionPart and
	 * renders the view of that data for the user.
	 */
	public void renderSection() {

		// Local Declarations
		final Section section = getSection();
		final FormToolkit formToolkit = editor.getHeaderForm().getToolkit();

		// Set an expansion listener
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				// To get this to layout properly the scrolled form must be
				// redrawn (reflow) and its grandparent must be laid out again.
				parentForm.getForm().reflow(true);
				parentForm.getForm().getParent().layout(true, true);
			}
		});
		// Set the section properties
		setSectionProperties();

		// Create the Section client. Note that in this case it is a
		// DataComponentComposite. Since it is a custom Composite, we must adapt
		// it to the default Form style.
		dataComposite = new DataComponentComposite(dataComp, section, SWT.FLAT);
		sectionClient = dataComposite;
		formToolkit.adapt(dataComposite);

		// Set the DataComponentComposite's reference to the FormToolKit so that
		// its child Composites can be decorated.
		dataComposite.formToolkit = formToolkit;

		// Create a listener that will mark the form as dirty when the event is
		// received.
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// Change the editor state
				editor.setDirty(true);
			}
		};
		// Register the Listener
		dataComposite.addListener(SWT.Selection, listener);

		// Add the message manager
		dataComposite.setMessageManager(editor.getHeaderForm()
				.getMessageManager());

		// Set the data composite's layout to a single column GridLayout. This
		// overrides the default layout used by DataComponentComposite.
		sectionClient.setLayout(new GridLayout());

		// Give the Section its client
		section.setClient(sectionClient);

		return;
	}

	/**
	 * This operation sets the DataComponent that should be rendered, updated
	 * and monitored by the ICESectionPart.
	 *
	 * @param component
	 *            The DataComponent
	 */
	public void setDataComponent(DataComponent component) {

		// If the Component is not null, store it and register for updates
		if (component != null) {
			// Update the data component reference
			dataComp = component;
			// Register the component so that the UI can be marked stale when it
			// changes.
			dataComp.register(this);
		}

	}

	/**
	 * This operation retrieves the DataComponent that is currently rendered,
	 * updated and monitored by the ICESectionPart.
	 *
	 * @return The DataComponent
	 */
	public DataComponent getDataComponent() {
		return dataComp;
	}

	/**
	 * This operation sets the properties (text, tooltip, etc.) for the Section
	 * based on the data from the ICE DataComponent.
	 */
	protected void setSectionProperties() {

		// Local Declarations
		Section section = getSection();

		// Add a description and title
		section.setDescription(dataComp.getDescription());
		section.setText(dataComp.getName());

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org
	 * .eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {

		// Make sure the components are the same --- FIXME: Will require a
		// DataComponent.equals() in the future!
		if (component == dataComp) {
			markStale();
		}
	}

	/**
	 * Override the refresh() operation to handle a redraw if needed.
	 */
	@Override
	public void refresh() {

		// Refresh the DataComponent view
		dataComposite.refresh();
		dataComposite.layout();

		// Update the Section properties
		setSectionProperties();

		// Refresh the section client to draw any new Entries
		sectionClient.redraw();

		// Reflow the managed form to correct for any part overlap
		parentForm.reflow(true);

		// Handle any activities performed by the base class
		super.refresh();

		return;

	}

}
