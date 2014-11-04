package org.eclipse.ice.client.widgets;

import org.eclipse.ice.client.widgets.jme.ViewAppState;
import org.eclipse.jface.action.Action;

/**
 * This <code>Action</code> toggles the HUD for a jME {@link ViewAppState}.
 * 
 * @author Jordan Deyton
 * 
 */
public class ToggleHUDAction extends Action {

	/**
	 * The jME view whose HUD can be toggled.
	 */
	private final ViewAppState view;

	/**
	 * The default constructor.
	 * 
	 * @param view
	 *            The jME view whose HUD can be toggled.
	 */
	public ToggleHUDAction(ViewAppState view) {
		// Set the ViewAppState and update the action's text and tool tip.
		this.view = view;
		updateStrings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		// Toggle the view's HUD and update the text and tool tip.
		view.setDisplayHUD(!view.getDisplayHUD());
		updateStrings();
	}

	/**
	 * Updates the text and tool tip of the action depending on whether the HUD
	 * is visible.
	 */
	private void updateStrings() {

		// If the HUD is displayed, set the text for hiding the HUD.
		if (view.getDisplayHUD()) {
			setText("Hide HUD");
			setToolTipText("Hide the heads-up display");
		}
		// Otherwise, set the text for showing the HUD.
		else {
			setText("Show HUD");
			setToolTipText("Show the heads-up display");
		}

		return;
	}
}
