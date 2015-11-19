/*******************************************************************************
* Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.test;

import org.eclipse.ice.iclient.uiwidgets.IStreamingTextWidget;

/** 
 * <p>The FakeStreamingTextWidget is a realization of IStreamingTextWidget that is used for testing. It provides the widgetDisplayed() operation in addition to the IStreamingTextWidget interface for inspection.</p>
 * @author Jay Jay Billings
 */
public class FakeStreamingTextWidget implements IStreamingTextWidget {
	/** 
	 * <p>Boolean to store the display state.</p>
	 */
	private boolean displayed = false;
	/** 
	 * <p>The last message pushed to the widget.</p>
	 */
	private String errorMsg = null;

	/** 
	 * <p>True if text was pushed to this widget, false otherwise.</p>
	 */
	private boolean textPushed = false;

	/** 
	 * <p>True if the label was set, false otherwise.</p>
	 */
	private boolean labelSet = false;

	/** 
	 * <p>This operation returns true if the display operation is called for the FakeStreamingTextWidget.</p>
	 * @return <p>True if the widget was displayed, false if not.</p>
	 */
	public boolean widgetDisplayed() {
		return displayed;
	}

	/** 
	 * <p>This operation implements display() as a simple pass through that makes whether or not the method was called. Nothing is drawn on the screen.</p>
	 */
	@Override
	public void display() {

		displayed = true;

	}

	/** 
	 * <p>True if text was pushed to this widget through the IStreamingTextWidget interface, false if not.</p>
	 * @return <p>True if text was pushed, false if not.</p>
	 */
	public boolean textPushed() {
		return textPushed;
	}

	/** 
	 * <p>True if the label was set, false otherwise.</p>
	 * @return <p>True if the label was set, false otherwise.</p>
	 */
	public boolean labelSet() {
		return labelSet;
	}

	/** 
	 * (non-Javadoc)
	 * @see IStreamingTextWidget#setLabel(String label)
	 */
	@Override
	public void setLabel(String label) {

		System.out.println("FakeStreamingWidget Message: " + label);
		labelSet = true;

		return;
	}

	/** 
	 * (non-Javadoc)
	 * @see IStreamingTextWidget#postText(String sText)
	 */
	@Override
	public void postText(String sText) {

		// Write to stdout
		System.out.println("FakeStreamingWidget Message: " + sText);
		// Hoist the colors
		textPushed = true;

	}
}