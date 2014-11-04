package org.eclipse.ice.client.widgets.moose;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * This class provides the MOOSE perspective. See the <code>plugin.xml</code>
 * for more perspective configuration.
 * 
 * @author Jordan H. Deyton
 *
 */
public class MOOSEPerspective implements IPerspectiveFactory {

	/**
	 * The perspective's ID as used in the <code>plugin.xml</code>.
	 */
	public static final String ID = "org.eclipse.ice.widgets.moose.MOOSEPerspective";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui
	 * .IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		// Add the perspective to top right corner.
		layout.addPerspectiveShortcut(ID);
	}

}
