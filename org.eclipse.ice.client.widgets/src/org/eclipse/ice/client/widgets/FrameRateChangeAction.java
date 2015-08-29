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
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * <p>
 * This class is an Action that appears in a play button drop-down menu with
 * either a set frame rate or access to a custom frame rate dialog.
 * </p>
 * 
 * @author Taylor Patterson
 */
public class FrameRateChangeAction extends Action {

	/**
	 * <p>
	 * The play button whose drop-down owns the object of this class.
	 * </p>
	 */
	private PlayAction parentButton;

	/**
	 * <p>
	 * The frame rate field maintained by this instance.
	 * </p>
	 */
	private int frameRate = 0;

	/**
	 * <p>
	 * The structure that stores available and previously used frame rates.
	 * </p>
	 */
	private ArrayList<Integer> rates = new ArrayList<Integer>();

	/**
	 * <p>
	 * Field to keep the type (pre-set or custom) of this object
	 * </p>
	 */
	private boolean isCustom;

	/**
	 * <p>
	 * The constructor for custom frame rate menu items.
	 * </p>
	 * 
	 * @param parent
	 *            <p>
	 *            The play button to whom this object belongs.
	 *            </p>
	 * @param text
	 *            <p>
	 *            The text to appear in thd drop-down.
	 *            </p>
	 */
	public FrameRateChangeAction(PlayAction parent, String text) {
		// Store the parent button in a local field
		parentButton = parent;
		// Set the menu text
		this.setText(text);
		// Set the type
		isCustom = true;
		// Add a few common rates to the rates structure
		rates.add(12);
		rates.add(24);
		rates.add(30);
	}

	/**
	 * <p>
	 * The constructor for input set frame rates.
	 * </p>
	 * 
	 * @param rate
	 *            <p>
	 *            The integer frame rate value to set for this action.
	 *            </p>
	 * @param parent
	 *            <p>
	 *            The play button to whom this object belongs.
	 *            </p>
	 * @param text
	 *            <p>
	 *            The text to appear in the drop-down.
	 *            </p>
	 */
	public FrameRateChangeAction(int rate, PlayAction parent, String text) {
		// Store the parent button in a local field
		parentButton = parent;
		// Set the frame rate
		frameRate = rate;
		// Set the type
		isCustom = false;
		// Set the menu text
		this.setText(text);
	}

	/**
	 * <p>
	 * The function called whenever the item is selected from the drop-down.
	 * </p>
	 */
	@Override
	public void run() {
		// Stop if currently running
		if (parentButton.isInPlayState()) {
			parentButton.stop();
		}
		// If this is a custom rate change, present the dialog to get the frame
		// rate.
		if (this.isCustom) {
			// Present a dialog box to enter/select the frame rate
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					Shell shell = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell();
					CustomFrameRateDialog dialog = new CustomFrameRateDialog(
							shell, rates);
					if (dialog.open() == Window.OK) {
						frameRate = dialog.getFrameRate();
						// Set the new frame rate
						parentButton.setDelay(frameRate);
						// Start playing at the new frame rate
						parentButton.run();
					}
				}
			});
		} else {
			// Set the new frame rate
			parentButton.setDelay(frameRate);
			// Start playing at the new frame rate
			parentButton.run();
		}
	}

}