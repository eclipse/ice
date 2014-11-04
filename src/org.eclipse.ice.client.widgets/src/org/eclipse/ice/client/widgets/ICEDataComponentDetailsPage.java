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

import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class implements the IDetailsPage interface to provide a Details page for ICE DataComponents. The only way to provide the component handle for this class is through the constructor.</p>
 * <!-- end-UML-doc -->
 * @author Jay Jay Billings
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEDataComponentDetailsPage implements IDetailsPage {
	/** 
	 * <!-- begin-UML-doc -->
	 * <p>The DataComponent whose data should be displayed.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
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
	 * <!-- begin-UML-doc -->
	 * <p>The constructor. If the component is null, this class will be unable to render anything to the screen.</p>
	 * <!-- end-UML-doc -->
	 * @param dataComponent <p>The data component that this page will display.</p>
	 * @param editor <p>The ICEFormEditor in which the pages will exist. This editor is marked as dirty when the page changes.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEDataComponentDetailsPage(DataComponent dataComponent,
			ICEFormEditor editor) {
		// begin-user-code
		formEditor = editor;
		component = dataComponent;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation returns the component that the page is rendering or null if it was not set in the constructor.</p>
	 * <!-- end-UML-doc -->
	 * @return <p>The DataComponent used in this provider or null if it was not set.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public DataComponent getComponent() {
		// begin-user-code
		return component;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IFormPart#initialize(IManagedForm form)
	 */
	public void initialize(IManagedForm form) {
		// begin-user-code
		this.mform = form;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IFormPart#dispose()
	 */
	public void dispose() {
		// begin-user-code

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IFormPart#commit(boolean onSave)
	 */
	public void commit(boolean onSave) {
		// begin-user-code

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IFormPart#setFormInput(Object input)
	 */
	public boolean setFormInput(Object input) {
		// begin-user-code
		return false;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IFormPart#setFocus()
	 */
	public void setFocus() {
		// begin-user-code

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IFormPart#refresh()
	 */
	public void refresh() {
		// begin-user-code

		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see IPartSelectionListener#selectionChanged(IFormPart part, ISelection selection)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void selectionChanged(IFormPart part, ISelection selection) {
		// begin-user-code

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param parent <p>The parent composite in which this page should be drawn.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void createContents(Composite parent) {
		// begin-user-code

		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 2;
		layout.bottomMargin = 2;
		parent.setLayout(layout);

		FormToolkit toolkit = mform.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION
				| Section.TITLE_BAR);
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

		ArrayList<Entry> entries = component.retrieveReadyEntries();

		// Make sure that there are actually Entries to add!
		if (entries != null) {
			// Create a Control for each Entry
			for (Entry entry : entries) {
				EntryComposite tmpComposite = null;
				// Set an event listener to enable saving
				Listener listener = new Listener() {
					@Override
					public void handleEvent(Event e) {
						// Change the editor state
						formEditor.setDirty(true);
					}
				};

				// Create the new Entry
				tmpComposite = new EntryComposite(client, SWT.FLAT, entry);
				// Set the Listener
				tmpComposite.addListener(SWT.Selection, listener);
			}
		}

		// Set the layout and then give the Section its client
		section.setClient(client);

		// end-user-code
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