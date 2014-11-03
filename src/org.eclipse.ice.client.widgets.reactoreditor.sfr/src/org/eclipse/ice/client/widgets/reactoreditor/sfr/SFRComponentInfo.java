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
package org.eclipse.ice.client.widgets.reactoreditor.sfr;

import org.eclipse.ice.reactor.sfr.base.SFRComponent;

import org.eclipse.ice.analysistool.IDataProvider;

/**
 * This class acts as a wrapper for SFRComponents in the SFR model. Because the
 * model takes advantage of symmetries--meaning the same assembly, rod, or pin
 * may be in multiple locations--it does not restrict a component to a single
 * location. Rather, the component may be in a number of locations in its parent
 * SFRComposite.<br>
 * <br>
 * This is a problem when passing references to an SFRComponent among views that
 * need to show selections based on the location of the component, because the
 * independent views cannot tell which specific location was selected.<br>
 * <br>
 * This class solves this problem by wrapping an SFRComponent and tacking on row
 * and column attributes. When an instance is passed, the views have access to
 * both the specific location requested and the SFRComponent in that spot.
 * 
 * @author djg
 * 
 */
public class SFRComponentInfo {

	/**
	 * The row in which the SFRComponent resides.
	 */
	public final int row;
	/**
	 * The column in which the SFRComponent resides.
	 */
	public final int column;
	/**
	 * The SFRComponent in the position determined by the values row and column.
	 */
	public final SFRComponent sfrComponent;
	/**
	 * The data provider, if any, for the SFRComponent.
	 */
	public final IDataProvider dataProvider;

	/**
	 * The default constructor for an SFRComponent wrapper. This is the same as
	 * calling the other constructor but passing null for the IDataProvider.
	 * 
	 * @param row
	 *            The row in which the SFRComponent resides.
	 * @param column
	 *            The column in which the SFRComponent resides.
	 * @param sfrComponent
	 *            The SFRComponent in the position determined by the values row
	 *            and column.
	 */
	public SFRComponentInfo(int row, int column, SFRComponent sfrComponent) {
		this(row, column, sfrComponent, null);
	}

	/**
	 * The full constructor for an SFRComponent wrapper.
	 * 
	 * @param row
	 *            The row in which the SFRComponent resides.
	 * @param column
	 *            The column in which the SFRComponent resides.
	 * @param sfrComponent
	 *            The SFRComponent in the position determined by the values row
	 *            and column.
	 * @param dataProvider
	 *            The data provider, if any, for the SFRComponent.
	 */
	public SFRComponentInfo(int row, int column, SFRComponent sfrComponent,
			IDataProvider dataProvider) {
		this.row = row;
		this.column = column;
		this.sfrComponent = sfrComponent;
		this.dataProvider = dataProvider;
	}

	/**
	 * Determines whether or not this location and SFRComponent is the same as
	 * the passed argument.
	 * 
	 * @param other
	 *            The other SFRComponentInfo object to compare.
	 * @return True if the row, column, and stored SFRComponent reference are
	 *         equivalent. False otherwise or if the passed argument is
	 *         <code>null</code>.
	 */
	public boolean matches(SFRComponentInfo other) {
		if (other == null) {
			return false;
		}
		return (row == other.row && column == other.column
				&& sfrComponent == other.sfrComponent && dataProvider == other.dataProvider);
	}
}
