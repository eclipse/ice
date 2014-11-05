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

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

/**
 * This provides an interface for analysis views that will be displayed inside
 * an AnalysisToolComposite. Any customized analysis views will need to
 * implement this interface.<br>
 * <br>
 * For a basic implementation, see {@link AnalysisView}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public interface IAnalysisView extends IStateListener {

	/**
	 * Fills out the parent Composite with information and widgets related to
	 * this particular IAnalysisView.
	 * 
	 * @param container
	 *            The Composite containing this IAnalysisView.
	 */
	public void createViewContent(Composite container);

	/**
	 * Gets the name for this type of analysis view. This can be used for the
	 * display text of SWT widgets that reference this view.
	 * 
	 * @return The IAnalysisView's name.
	 */
	public String getName();

	/**
	 * Gets a brief description of this type of analysis view. This can be used
	 * for ToolTips for SWT widgets referencing this view.
	 * 
	 * @return The IAnalysisView's description.
	 */
	public String getDescription();

	/**
	 * Gets the root Composite for this IAnalysisView. This Composite should
	 * contain all of the contents modified by this IAnalysisView.
	 * 
	 * @return The IAnalysisView's main content Composite.
	 */
	public Composite getComposite();

	/**
	 * Populates the specified ToolBar with ToolItems used to manipulate this
	 * IAnalysisView.
	 * 
	 * @param toolBar
	 *            The ToolBar to fill with ToolItems.
	 */
	public void getToolBarContributions(ToolBar toolBar);

	/**
	 * Each IAnalysisView typically requires some sort of data to display. This
	 * method is used to provide data to the view.
	 * 
	 * @param key
	 *            The key for the data object. The default value passed by the
	 *            AnalysisToolComposite will be a string matching the data
	 *            source ("Input", "Reference", etc.).
	 * @param value
	 *            The data object.
	 */
	public void setData(String key, Object value);

	/**
	 * Disposes of any resources unique to this IAnalysisView.
	 */
	public void dispose();

	/**
	 * Sets the ISelectionProvider to which this IAnalysisView publishes the
	 * current selection. This is used to push IPropertySources to the ICE
	 * Properties View.
	 * 
	 * @param selectionProvider
	 *            The new ISelectionProvider used by this IAnalysisView.
	 */
	public void setSelectionProvider(ISelectionProvider selectionProvider);
}
