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

import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * <p>The ICEFormPage class is the base class for all FormPages in ICE.</p>
 * @author Jay Jay Billings
 */
public class ICEFormPage extends FormPage {
	
	/**
	 * Logger for handling event messages and other information.
	 */
	protected final Logger logger;
	
	/** 
	 * <p>The Form from ICE that contains the data to be displayed in this on this Page.</p>
	 */
	protected Form iceForm;
	/** 
	 * <p>A handle to the Editor that is injected in the Constructor.</p>
	 */
	protected ICEFormEditor editor;

	/** 
	 * The Constructor
	 * @param editor The FormEditor for which the Page should be constructed.
	 * @param id The id of the page.
	 * @param title The title of the page.
	 */
	public ICEFormPage(FormEditor editor, String id, String title) {

		// Call the super constructor
		super(editor, id, title);
		
		// Save the editor reference
		this.editor = (ICEFormEditor) editor;
		
		// Create the logger
		logger = LoggerFactory.getLogger(getClass());

	}
}