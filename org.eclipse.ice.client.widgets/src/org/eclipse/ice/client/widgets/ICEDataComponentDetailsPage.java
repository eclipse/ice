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

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.client.widgets.providers.IEntryCompositeProvider;
import org.eclipse.ice.client.widgets.providers.Default.DefaultEntryCompositeProvider;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class implements the IDetailsPage interface to provide a Details page
 * for ICE DataComponents. The only way to provide the component handle for this
 * class is through the constructor.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ICEDataComponentDetailsPage implements IDetailsPage {
	
	/**
	 * Reference to the logger service.
	 */
	protected Logger logger = LoggerFactory.getLogger(ICEDataComponentDetailsPage.class);

	/**
	 * <p>
	 * The DataComponent whose data should be displayed.
	 * </p>
	 */
	private DataComponent component;

	/**
	 * The FormEditor that should be notified when it is time to save.
	 */
	private ICEFormEditor formEditor;

	/**
	 * The managed form for this page.
	 */
	private IManagedForm mform;

	/**
	 * <p>
	 * The constructor. If the component is null, this class will be unable to
	 * render anything to the screen.
	 * </p>
	 * 
	 * @param dataComponent
	 *            <p>
	 *            The data component that this page will display.
	 *            </p>
	 * @param editor
	 *            <p>
	 *            The ICEFormEditor in which the pages will exist. This editor
	 *            is marked as dirty when the page changes.
	 *            </p>
	 */
	public ICEDataComponentDetailsPage(DataComponent dataComponent,
			ICEFormEditor editor) {
		formEditor = editor;
		component = dataComponent;
	}

	/**
	 * <p>
	 * This operation returns the component that the page is rendering or null
	 * if it was not set in the constructor.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The DataComponent used in this provider or null if it was not
	 *         set.
	 *         </p>
	 */
	public DataComponent getComponent() {
		return component;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormPart#initialize(IManagedForm form)
	 */
	@Override
	public void initialize(IManagedForm form) {
		this.mform = form;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormPart#dispose()
	 */
	@Override
	public void dispose() {

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormPart#commit(boolean onSave)
	 */
	@Override
	public void commit(boolean onSave) {

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormPart#setFormInput(Object input)
	 */
	@Override
	public boolean setFormInput(Object input) {
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IFormPart#refresh()
	 */
	@Override
	public void refresh() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.IPartSelectionListener#selectionChanged(org.eclipse.
	 * ui.forms.IFormPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {

	}

	/**
	 * @param parent
	 *            <p>
	 *            The parent composite in which this page should be drawn.
	 *            </p>
	 */
	@Override
	public void createContents(Composite parent) {

		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 2;
		layout.bottomMargin = 2;
		parent.setLayout(layout);

		FormToolkit toolkit = mform.getToolkit();
		Section section = toolkit.createSection(parent,
				Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		section.marginWidth = 10;
		section.setText(component.getName());
		section.setDescription(component.getDescription());
		TableWrapData td = new TableWrapData(TableWrapData.FILL,
				TableWrapData.TOP);
		td.grabHorizontal = true;
		section.setLayoutData(td);
		Composite client = toolkit.createComposite(section);
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		client.setLayout(glayout);

		ArrayList<IEntry> entries = component.retrieveReadyEntries();

		// Make sure that there are actually Entries to add!
		if (entries != null) {
			// Create a Control for each Entry
			for (IEntry entry : entries) {
				IEntryComposite tmpComposite = null;
				// Set an event listener to enable saving
				Listener listener = new Listener() {
					@Override
					public void handleEvent(Event e) {
						// Change the editor state
						formEditor.setDirty(true);
					}
				};

				// Create the new Entry
				IEntryCompositeProvider provider = new DefaultEntryCompositeProvider();
				if (!"default".equals(entry.getContext())) {
					try {
						for (IEntryCompositeProvider p : IEntryCompositeProvider.getProviders()) {
							if (p.getName().equals(entry.getContext())) {
								provider = p;
								break;
							}
						}
					} catch (CoreException e) {
						logger.error("Exception caught in trying to get a custom IEntryCompositeProvider.",e);
					}
				}

				
				tmpComposite = provider.getEntryComposite(client, entry, SWT.FLAT, formEditor.getToolkit());
				// Set the Listener
				tmpComposite.getComposite().addListener(SWT.Selection, listener);
			}
		}

		// Set the layout and then give the Section its client
		section.setClient(client);

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}
}