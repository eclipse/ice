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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.Timer;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * <p>
 * This class is an Action that employs a Timer to display the next item in a
 * ICEResourceView on a fixed time delay. After activation, this class changes
 * roles to act as a pause for the play loop.
 * </p>
 * 
 * @author Taylor Patterson
 */
public class PlayAction extends Action implements IMenuCreator {

	/**
	 * <p>
	 * The PlayableViewPart that owns an object of this class.
	 * </p>
	 */
	private PlayableViewPart viewer;

	/**
	 * <p>
	 * The delay (in milliseconds) before switching to the next item in the
	 * PlayableViewPart's list.
	 * </p>
	 */
	private int delay = 83;

	/**
	 * <p>
	 * The ActionListener called by the Timer to switch to the next item.
	 * </p>
	 */
	ActionListener playTask = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			viewer.setToNextResource();
		}
	};

	/**
	 * <p>
	 * The fixed-delay timer used to continuously call the playTask after the
	 * delay.
	 * </p>
	 */
	private Timer timer = new Timer(delay, playTask);

	/**
	 * <p>
	 * The Menu for managing the frame rate drop-down.
	 * </p>
	 */
	private Menu menu;

	/**
	 * <p>
	 * The structure for the Actions in the drop-down.
	 * </p>
	 */
	private ArrayList<Action> rateActions = new ArrayList<Action>();

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 * @param parent
	 *            <p>
	 *            The PlayableViewPart to whom the object of this class belongs.
	 *            </p>
	 */
	public PlayAction(PlayableViewPart parent) {

		// Call this class' super constructor for a drop-down menu and declare
		// this class as the menu creator
		super(null, IAction.AS_DROP_DOWN_MENU);
		setMenuCreator(this);

		// Store the calling class in the local field
		viewer = parent;

		// Set the text of the "hover" display
		this.setText("Play");

		// Set the button image
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "play.gif");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		this.setImageDescriptor(imageDescriptor);

		// Create the drop-down selections for 12, 24, and 30 fps and a
		// selection to access the custom frame rate dialog
		FrameRateChangeAction fps12 = new FrameRateChangeAction(12, this,
				"12fps");
		rateActions.add(fps12);
		FrameRateChangeAction fps24 = new FrameRateChangeAction(24, this,
				"24fps");
		rateActions.add(fps24);
		FrameRateChangeAction fps30 = new FrameRateChangeAction(30, this,
				"30fps");
		rateActions.add(fps30);
		FrameRateChangeAction fpsCustom = new FrameRateChangeAction(this,
				"Custom...");
		rateActions.add(fpsCustom);

		return;
	}

	/**
	 * <p>
	 * The function called whenever the button is clicked.
	 * </p>
	 */
	@Override
	public void run() {

		// Check that the button is in a clickable state
		if (viewer.isPlayable()) {
			// If the timer is not running, set up the play behavior
			if (!timer.isRunning()) {
				// Set the play rate
				timer.setDelay(delay);
				// Start the play loop
				timer.start();
				// Turn the play button into a pause button by changing its
				// "hover" text and image
				this.setText("Pause");
				Bundle bundle = FrameworkUtil.getBundle(getClass());
				Path imagePath = new Path("icons"
						+ System.getProperty("file.separator") + "pause.gif");
				URL imageURL = FileLocator.find(bundle, imagePath, null);
				ImageDescriptor imageDescriptor = ImageDescriptor
						.createFromURL(imageURL);
				this.setImageDescriptor(imageDescriptor);
			} else {
				// If the button is clicked while the timer is running, pause
				// instead
				stop();
			}
		}

		return;
	}

	/**
	 * <p>
	 * This function stops the timer and switches the button from a pause button
	 * to a play button.
	 * </p>
	 */
	public void stop() {
		// Stop the timer
		timer.stop();
		// Change the "hover" text
		this.setText("Play");
		// Change the button appearance
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		Path imagePath = new Path("icons"
				+ System.getProperty("file.separator") + "play.gif");
		URL imageURL = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor
				.createFromURL(imageURL);
		this.setImageDescriptor(imageDescriptor);
	}

	/**
	 * <p>
	 * Public means for modifying the delay field. Convert input frame rate to
	 * delay in milliseconds.
	 * </p>
	 * 
	 * @param frameRate
	 *            <p>
	 *            An integer frame rate to be converted to a truncated
	 *            millisecond delay.
	 *            </p>
	 */
	public void setDelay(int frameRate) {
		delay = 1000 / frameRate;
		return;
	}

	/**
	 * <p>
	 * Public access to the state of this Action.
	 * </p>
	 * 
	 * @return The run status of the timer
	 */
	public boolean isInPlayState() {
		return timer.isRunning();
	}

	/**
	 * <p>
	 * Dispose the menu.
	 * </p>
	 * 
	 * @see IMenuCreator#dispose
	 */
	@Override
	public void dispose() {

		if (menu != null) {
			menu.dispose();
			menu = null;
		}

		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IMenuCreator#getMenu(Menu)
	 */
	@Override
	public Menu getMenu(Menu parent) {
		// Do nothing
		return null;
	}

	/**
	 * <p>
	 * Refresh the menu field.
	 * </p>
	 * 
	 * @see IMenuCreator#getMenu(Control)
	 */
	@Override
	public Menu getMenu(Control parent) {

		// Dispose of the old menu
		if (menu != null) {
			menu.dispose();
		}
		// Put all the actions into a new menu
		menu = new Menu(parent);

		for (Action action : rateActions) {
			ActionContributionItem item = new ActionContributionItem(action);
			item.fill(menu, -1);
		}

		return menu;
	}

}