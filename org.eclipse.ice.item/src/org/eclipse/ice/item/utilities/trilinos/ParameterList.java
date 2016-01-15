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
package org.eclipse.ice.item.utilities.trilinos;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class represents a Teuchos parameter list. It is a simple data structure
 * that only holds data.
 * </p>
 * 
 * @author Jay Jay Billings
 */
@XmlRootElement(name = "ParameterList")
public class ParameterList {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ParameterList.class);

	/**
	 * <p>
	 * The name of the parameter list.
	 * </p>
	 * 
	 */
	@XmlAttribute
	public String name;
	/**
	 * <p>
	 * The set of parameters that are managed by this parameter list.
	 * </p>
	 * 
	 */
	@XmlElement(name = "Parameter")
	public ArrayList<Parameter> parameters;
	/**
	 * <p>
	 * The set of parameter lists that are child of this parameter list.
	 * </p>
	 * 
	 */
	@XmlElement(name = "ParameterList")
	public ArrayList<ParameterList> parameterLists;

	/**
	 * <p>
	 * This operations returns an ICE TreeComposite for the ParameterList. Any
	 * parameters in the list are contained in a Data Component labeled
	 * "&lt;name&gt; Parameters" with no description and id equal to 1.
	 * Subordinate parameter lists are configured as children of the tree
	 * composite.
	 * </p>
	 * 
	 * @return <p>
	 *         The tree composite.
	 *         </p>
	 */
	public TreeComposite toTreeComposite() {

		// Local Declarations
		TreeComposite treeComp = new TreeComposite();
		DataComponent dataComp = new DataComponent();

		// Setup the tree composites name
		treeComp.setName(name);
		treeComp.setDescription(name);

		// Setup the data component
		dataComp.setName(name + " Parameters");
		dataComp.setDescription(name + " Parameters");
		// Add the parameters to the list if they exist
		if (parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				Parameter param = parameters.get(i);
				IEntry paramEntry = param.toEntry();
				paramEntry.setId(i + 1);
				dataComp.addEntry(paramEntry);
			}
		} else {
			logger.info("ParameterList Message: " + name
					+ " has no parameters! WARNING!");
		}
		// Add the data component as a data node
		treeComp.addComponent(dataComp);

		// Add the subordinate parameter lists to the tree if there are any
		if (parameterLists != null) {
			for (int i = 0; i < parameterLists.size(); i++) {
				TreeComposite childTreeComp = parameterLists.get(i)
						.toTreeComposite();
				childTreeComp.setId(i + 1);
				treeComp.setNextChild(childTreeComp);
			}
		}

		return treeComp;
	}

	/**
	 * <p>
	 * This operations loads a ParameterList from an ICE TreeComposite. It is
	 * the inverse operation of toTreeComposite(). It assumes that the
	 * TreeComposite is of the same form as that created by toTreeComposite()
	 * and will fail if not.
	 * </p>
	 * 
	 * @param treeComp
	 *            <p>
	 *            The tree composite.
	 *            </p>
	 */
	public void fromTreeComposite(TreeComposite treeComp) {

		// Local Declarations
		DataComponent dataComp = null;
		ArrayList<IEntry> entries = null;
		ArrayList<TreeComposite> children = new ArrayList<TreeComposite>();

		// Only convert if the tree component is not null
		if (treeComp != null) {
			// Set the name
			name = treeComp.getName();
			// Get the parameters from the data component, but only if they
			// exist
			dataComp = (DataComponent) treeComp.getDataNodes().get(0);
			if (dataComp != null
					&& (entries = dataComp.retrieveAllEntries()) != null) {
				// Allocate the parameters array
				parameters = new ArrayList<Parameter>();
				// Convert the Entries
				for (int i = 0; i < entries.size(); i++) {
					Parameter tmpParam = new Parameter();
					tmpParam.fromEntry(entries.get(i));
					parameters.add(tmpParam);
				}
			}
			// Get the sublists from the data component, but only if they exist
			if (treeComp.getNumberOfChildren() > 0) {
				// Allocate the parameterLists array
				parameterLists = new ArrayList<ParameterList>();
				// Convert the sublists
				for (int i = 0; i < treeComp.getNumberOfChildren(); i++) {
					ParameterList tmpParameterList = new ParameterList();
					tmpParameterList.fromTreeComposite(treeComp
							.getChildAtIndex(i));
					parameterLists.add(tmpParameterList);
				}
			}
		}

		return;
	}

}