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
package org.eclipse.ice.client.widgets.reactoreditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.SimpleRootEditPart;
import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.ToolBar;

/**
 * This provides a super-class for specialized IAnalysisViews. Of particular
 * usefulness is the List {@link #actions}. It provides an easy way for
 * sub-classes to fill and update the parent AnalysisToolComposite's action
 * ToolBar and any context (right-click) Menu that is tied to the same actions
 * as the ToolBar. Classes extending this should override any methods here to
 * provide a more fine-tuned analysis perspective.<br>
 * <br>
 * Notes for extending this class:<br>
 * 1 - In the sub-class' constructor, you should add any Actions to the
 * ActionTree List. These will be put into the parent AnalysisToolComposite's
 * primary ToolBar and can be put into a right-click Menu.<br>
 * 2 - For {@link #createViewContent(Composite)}, you should call the super
 * method to initialize the container Composite. Otherwise, you need to either
 * initialize the container Composite yourself, or use a different container and
 * override {@link #getComposite()}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class AnalysisView implements IAnalysisView, IStateListener {

	/**
	 * The Composite that contains all tools used to display analysis
	 * information for this IAnalysisView.
	 */
	protected Composite container;

	/**
	 * The List of custom JFace Actions. This List and the ActionTrees therein
	 * are used to populate the primary ToolBar of the parent
	 * AnalysisToolComposite and the actionMenuManager, which can be used to
	 * display any type of Menu (including a context Menu or right-click Menu),
	 */
	protected List<ActionTree> actions;

	/**
	 * A MenuManager built from the List of ActionTrees (composed of JFace
	 * Actions).
	 */
	protected MenuManager actionMenuManager;

	/**
	 * A ToolBarManager built from the List of ActionTrees (composed of JFace
	 * Actions).
	 */
	protected ToolBarManager actionToolBarManager;

	/**
	 * The data source used by this IAnalysisView, e.g.,
	 * Input/Reference/Comparison.
	 */
	protected final DataSource dataSource;

	/**
	 * The broker handles events that this IAnalysisView would like to be
	 * notified about.
	 */
	protected StateBroker broker;

	/**
	 * The default constructor.
	 * 
	 * @param dataSource
	 *            The data source associated with this view (input, reference,
	 *            comparison).
	 */
	public AnalysisView(DataSource dataSource) {

		// Set the data source associated with this analysis view.
		this.dataSource = dataSource;

		// Create the list of actions (these go in the ToolBar and the
		// rightClickMenu).
		actions = new ArrayList<ActionTree>();

		// Initialize the Menu- and ToolBarManagers.
		actionToolBarManager = new ToolBarManager();
		actionMenuManager = new MenuManager();

		return;
	}

	/**
	 * Updates the ToolBarManager and MenuManager to pull JFace Actions from
	 * {@link #actions}. This method should be used if actions need to be added
	 * to or removed from the List.
	 */
	protected void updateActionManagers() {
		System.out
				.println("AnalysisView message: Updating action ToolBar-/MenuManagers.");

		// Clear the managers.
		actionToolBarManager.removeAll();
		actionMenuManager.removeAll();

		// Add all of the Actions from the ActionTree list.
		for (ActionTree action : actions) {
			actionToolBarManager.add(action.getContributionItem());
			actionMenuManager.add(action.getContributionItem());
		}

		// Force an update for them.
		actionToolBarManager.update(true);
		actionMenuManager.update(true);

		return;
	}

	/* ---- Implements IAnalysisView ---- */
	/**
	 * Fills out the parent Composite with information and widgets related to
	 * this particular IAnalysisView.
	 * 
	 * @param parent
	 *            The Composite containing this IAnalysisView.
	 */
	public void createViewContent(Composite container) {
		System.out.println("AnalysisView message: Creating view contents.");

		this.container = container;

		return;
	}

	/**
	 * Gets the name for this type of analysis view. This can be used for the
	 * display text of SWT widgets that reference this view.
	 * 
	 * @return The IAnalysisView's name.
	 */
	public String getName() {
		return "Analysis View";
	}

	/**
	 * Gets a brief description of this type of analysis view. This can be used
	 * for ToolTips for SWT widgets referencing this view.
	 * 
	 * @return The IAnalysisView's description.
	 */
	public String getDescription() {
		return "A generic view for analysis.";
	}

	/**
	 * Gets the root Composite for this IAnalysisView. This Composite should
	 * contain all of the contents modified by this IAnalysisView.
	 * 
	 * @return The IAnalysisView's main content Composite.
	 */
	public Composite getComposite() {
		return container;
	}

	/**
	 * Populates the specified ToolBar with ToolItems used to manipulate this
	 * IAnalysisView.
	 * 
	 * @param toolBar
	 *            The ToolBar to fill with ToolItems.
	 */
	public void getToolBarContributions(ToolBar toolBar) {
		System.out
				.println("AnalysisView message: Populating provided ToolBar.");

		// To populate a pre-existing ToolBar, we *have* to re-initialize the
		// ToolBarManager and pass the ToolBar as a parameter to the
		// constructor.
		actionToolBarManager = new ToolBarManager(toolBar);

		// Because we just re-initialized the ToolBarManager, we need to update
		// it here.
		updateActionManagers();

		return;
	}

	/**
	 * Each IAnalysisView typically requires some sort of data to display. This
	 * method is used to provide data to the view.
	 * 
	 * @param key
	 *            The key for the data object.
	 * @param value
	 *            The data object.
	 */
	public void setData(String key, Object value) {
		return;
	}

	/**
	 * Disposes of any resources unique to this IAnalysisView.
	 */
	public void dispose() {

		// Unregister from the broker.
		unregisterKeys();

		// Dispose of the Action managers. The ToolBarManager also disposes of
		// the associated ToolBar (created by the parent ATC).
		actionToolBarManager.dispose();
		actionMenuManager.dispose();

		// Dispose of all the Actions for this view.
		for (ActionTree action : actions) {
			action.dispose();
		}
		actions.clear();

		// Dispose of the container Composite.
		container.dispose();

		return;
	}

	/**
	 * This ISelectionProvider feeds the current selection to the Eclipse
	 * workbench. That means when its setSelection() method is called, if the
	 * selection includes an IPropertySource, the ICE Properties View will
	 * display the selection's properties.
	 */
	protected ISelectionProvider selectionProvider;

	/**
	 * Sets the ISelectionProvider used to display properties in the Properties
	 * View.
	 */
	public void setSelectionProvider(ISelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
	}

	/* ---------------------------------- */

	/* ---- Implements IStateListener ---- */
	/**
	 * Sets the broker used by this IAnalysisView. The default behavior is to
	 * unregister keys from the previous broker and register them with the new
	 * broker.
	 */
	public void setBroker(StateBroker broker) {

		// Unregister from the previous broker if necessary.
		if (this.broker != null) {
			unregisterKeys();
		}
		// Set the new broker.
		this.broker = broker;

		// Register any keys with the broker.
		registerKeys();
	}

	/**
	 * Registers any keys of interest with the current broker.
	 */
	public void registerKeys() {
		return;
	}

	/**
	 * Unregisters any keys from the current broker.
	 */
	public void unregisterKeys() {
		return;
	}

	/**
	 * This is called by the broker when a key of interest has changed.
	 */
	public void update(String key, Object value) {
		return;
	}

	/* ----------------------------------- */

	/**
	 * Given an SWT Canvas, this method creates a save file dialog and will save
	 * the Canvas as an image file. Supported extensions include PNG, JPG, and
	 * BMP.
	 * 
	 * @param canvas
	 *            The Canvas to save as an image.
	 */
	protected static void saveCanvasImage(Canvas canvas) {
		// FIXME - It's possible to get a graphical glitch where there are black
		// pixels at the bottom of the image. My guess is that the GC never
		// actually bothers to paint because that part is not visible on the
		// screen. Currently, this issue is unresolved.

		// Store the allowed extensions in a HashMap.
		HashMap<String, Integer> extensions = new HashMap<String, Integer>(4);
		extensions.put("png", SWT.IMAGE_PNG);
		extensions.put("jpg", SWT.IMAGE_JPEG);
		extensions.put("bmp", SWT.IMAGE_BMP);

		// Make the array of strings needed to pass to the file dialog.
		String[] extensionStrings = new String[extensions.keySet().size()];
		int i = 0;
		for (String extension : extensions.keySet()) {
			extensionStrings[i++] = "*." + extension;
		}

		// Create the file save dialog.
		FileDialog fileDialog = new FileDialog(canvas.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(extensionStrings);
		fileDialog.setOverwrite(true);

		// Get the path of the new/overwritten image file.
		String path = fileDialog.open();

		// Return if the user cancelled.
		if (path == null) {
			return;
		}
		// Get the image type to save from the path's extension.
		String[] splitPath = path.split("\\.");
		int extensionSWT = extensions.get(splitPath[splitPath.length - 1]);

		// Get the image from the canvas.
		Rectangle bounds = canvas.getBounds();
		Image image = new Image(null, bounds.width, bounds.height);
		GC gc = new GC(image);
		// SWT XYGraph uses this method to center the saved image.
		canvas.print(gc);
		gc.dispose();

		// Save the file.
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		loader.save(path, extensionSWT);
		image.dispose();

		return;
	}

	/**
	 * Given a GEF GraphicalViewer, this method creates a save file dialog and
	 * will save the contents of the viewer as an image file. Supported
	 * extensions include PNG, JPG, and BMP.
	 * 
	 * @param viewer
	 *            The GraphicalViewer to save as an image.
	 */
	protected static void saveViewerImage(GraphicalViewer viewer) {

		// Store the allowed extensions in a HashMap.
		HashMap<String, Integer> extensions = new HashMap<String, Integer>(4);
		extensions.put("png", SWT.IMAGE_PNG);
		extensions.put("jpg", SWT.IMAGE_JPEG);
		extensions.put("bmp", SWT.IMAGE_BMP);

		// Make the array of strings needed to pass to the file dialog.
		String[] extensionStrings = new String[extensions.keySet().size()];
		int i = 0;
		for (String extension : extensions.keySet()) {
			extensionStrings[i++] = "*." + extension;
		}

		// Create the file save dialog.
		FileDialog fileDialog = new FileDialog(viewer.getControl().getShell(),
				SWT.SAVE);
		fileDialog.setFilterExtensions(extensionStrings);
		fileDialog.setOverwrite(true);

		// Get the path of the new/overwritten image file.
		String path = fileDialog.open();

		// Return if the user cancelled.
		if (path == null) {
			return;
		}

		// Get the image type to save from the path's extension.
		String[] splitPath = path.split("\\.");
		int extensionSWT = extensions.get(splitPath[splitPath.length - 1]);

		// Get the root EditPart and its draw2d Figure.
		SimpleRootEditPart rootEditPart = (SimpleRootEditPart) viewer
				.getRootEditPart();
		IFigure figure = rootEditPart.getFigure();

		// Get the image from the Figure.
		org.eclipse.draw2d.geometry.Rectangle bounds = figure.getBounds();
		Image image = new Image(null, bounds.width, bounds.height);
		GC gc = new GC(image);
		SWTGraphics g = new SWTGraphics(gc);
		figure.paint(g);
		g.dispose();
		gc.dispose();

		// Save the file.
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		loader.save(path, extensionSWT);
		image.dispose();

		return;
	}
}
