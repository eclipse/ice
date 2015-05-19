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
package org.eclipse.ice.reactorAnalyzer;

import static org.eclipse.ice.reactorAnalyzer.ReactorAnalyzer.*;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IAnalysisTool;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * <p>
 * This class is the ItemBuilder for the ReactorAnalyzer. It constructs a
 * ReactorAnalyzer when build() is called if and only if at least one
 * IAnalysisTool is available to do the analysis work for the analyzer,
 * otherwise it will return null when build() is called.
 * </p>
 * <p>
 * The name and type of the Item created by this builder are set as final,
 * static variables on this class. Just something I'm trying out for
 * convenience...
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class ReactorAnalyzerBuilder implements ItemBuilder {
	/**
	 * <p>
	 * The set of IAnalysisTools available for the ReactorAnalyzer.
	 * </p>
	 * 
	 */
	private ArrayList<IAnalysisTool> analysisTools;

	/**
	 * <p>
	 * The name of the ReactorAnalyzer.
	 * </p>
	 * 
	 */
	public static final String name = "Reactor Analyzer";

	/**
	 * <p>
	 * The type of the Item that this builder will create.
	 * </p>
	 * 
	 */
	public static final ItemType type = ItemType.AnalysisSession;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 */
	public ReactorAnalyzerBuilder() {

		// Allocate the list of tools
		analysisTools = new ArrayList<IAnalysisTool>();

		return;

	}

	/**
	 * <p>
	 * This operation adds an IAnalysisTool to the list of analysis tools that
	 * is available to the ReactorAnalyzer.
	 * </p>
	 * 
	 * @param tool
	 *            <p>
	 *            The IAnalysisTool to add to the set.
	 *            </p>
	 */
	public void addAnalysisTool(IAnalysisTool tool) {

		// Add the tool
		if (tool != null) {
			analysisTools.add(tool);
		}

		return;

	}

	/**
	 * <p>
	 * This operation is called by the OSGI to close the associated
	 * IAnalysisTools.
	 * </p>
	 * 
	 */
	public void stop() {

		System.out
				.println("ReactorAnalyzerBuilder Message: Closing Analysis Tools!");
		for (int i = 0; i < this.analysisTools.size(); i++) {
			this.analysisTools.get(i).close();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemName()
	 */
	public String getItemName() {
		return name;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemType()
	 */
	public ItemType getItemType() {
		return type;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#build(IProject projectSpace)
	 */
	public Item build(IProject projectSpace) {

		// Local Declarations
		ReactorAnalyzer analyzer = null;

		// Make the analyzer
		analyzer = new ReactorAnalyzer(projectSpace);

		// Set the item builder name
		analyzer.setItemBuilderName(this.getItemName());

		// Set the name
		analyzer.setName(name);
		// Set the analysis tools
		analyzer.setAnalysisTools(analysisTools);

		return analyzer;

	}

}