/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.reactoreditor.lwr;

import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.reactor.LWRComponent;

/**
 * This class acts as a wrapper for LWRComponents in the LWR model. Because the
 * model takes advantage of symmetries--meaning the same assembly, rod, or pin
 * may be in multiple locations--it does not restrict a component to a single
 * location. Rather, the component may be in a number of locations in its parent
 * LWRComposite.<br>
 * <br>
 * This is a problem when passing references to an LWRComponent among views that
 * need to show selections based on the location of the component, because the
 * independent views cannot tell which specific location was selected.<br>
 * <br>
 * This class solves this problem by wrapping an LWRComponent and tacking on row
 * and column attributes. When an instance is passed, the views have access to
 * both the specific location requested and the LWRComponent in that spot.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class LWRComponentInfo {
	/**
	 * The row in which the LWRComponent resides.
	 */
	public final int row;
	/**
	 * The column in which the LWRComponent resides.
	 */
	public final int column;
	/**
	 * The LWRComponent in the position determined by the values row and column.
	 */
	public final LWRComponent lwrComponent;
	/**
	 * The data provider, if any, for the LWRComponent.
	 */
	public final IDataProvider dataProvider;

	/**
	 * The default constructor for an LWRComponent wrapper. This is the same as
	 * calling the other constructor but passing null for the IDataProvider.
	 * 
	 * @param row
	 *            The row in which the LWRComponent resides.
	 * @param column
	 *            The column in which the LWRComponent resides.
	 * @param lwrComponent
	 *            The LWRComponent in the position determined by the values row
	 *            and column.
	 */
	public LWRComponentInfo(int row, int column, LWRComponent lwrComponent) {
		this(row, column, lwrComponent, null);
	}

	/**
	 * The full constructor for an LWRComponent wrapper.
	 * 
	 * @param row
	 *            The row in which the LWRComponent resides.
	 * @param column
	 *            The column in which the LWRComponent resides.
	 * @param lwrComponent
	 *            The LWRComponent in the position determined by the values row
	 *            and column.
	 * @param dataProvider
	 *            The data provider, if any, for the LWRComponent.
	 */
	public LWRComponentInfo(int row, int column, LWRComponent lwrComponent,
			IDataProvider dataProvider) {
		this.row = row;
		this.column = column;
		this.lwrComponent = lwrComponent;
		this.dataProvider = dataProvider;
	}

	/**
	 * Determines whether or not this location and LWRComponent is the same as
	 * the passed argument.
	 * 
	 * @param other
	 *            The other LWRComponentInfo object to compare.
	 * @return True if the row, column, and stored LWRComponent reference are
	 *         equivalent. False otherwise or if the passed argument is
	 *         <code>null</code>.
	 */
	public boolean matches(LWRComponentInfo other) {
		if (other == null) {
			return false;
		}
		return (row == other.row && column == other.column
				&& lwrComponent == other.lwrComponent && dataProvider == other.dataProvider);
	}
}
