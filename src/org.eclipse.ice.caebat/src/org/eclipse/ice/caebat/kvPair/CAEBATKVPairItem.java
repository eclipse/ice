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
package org.eclipse.ice.caebat.kvPair;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.item.Item;

/**
 * This class is a Model Item that configures a set of data components that
 * represent key-value pairs for CAEBAT input.
 * 
 * @author Jay Jay Billings
 * 
 */
@XmlRootElement(name = "CAEBATKVPairItem")
public class CAEBATKVPairItem extends Item {

	/**
	 * The required constructor.
	 * 
	 * @param projectSpace
	 *            The Eclipse Project space needed for file manipulation.
	 */
	public CAEBATKVPairItem(IProject projectSpace) {
		// Punt to the base class.
		super(projectSpace);
	}

	/**
	 * The nullary constructor.
	 */
	public CAEBATKVPairItem() {
		this(null);
	}

	/**
	 * This operation overrides the base class' operation to create a Form with
	 * Entries that will be used to generate the CAEBAT key-value pair file. It
	 * has one DataComponent per CAEBAT component.
	 */
	@Override
	public void setupForm() {

		// Create the Form
		super.setupForm();

		// Create the data components
		DataComponent thermalComponent = new DataComponent();
		DataComponent electricalComponent = new DataComponent();
		DataComponent electroChemComponent = new DataComponent();

		// Setup the information about the thermal component
		thermalComponent.setName("Thermal Properties");
		thermalComponent.setDescription("Thermal properties and setttings");
		thermalComponent.setId(1);

		// Setup the information about the electrical component
		electricalComponent.setName("Electrical Properties");
		electricalComponent
				.setDescription("Electrical properties and setttings");
		electricalComponent.setId(2);

		// Setup the information about the electroChem component
		electroChemComponent.setName("Electrochemical Properties");
		electroChemComponent
				.setDescription("Electrochemical properties and setttings");
		electroChemComponent.setId(3);

		// Create a counter for the entry ids
		int idCounter = 0;

		// Setup the entries for the thermal component. There are no bounds or
		// other information on these yet, so just let them have undefined type,
		// no allowed values, etc.

		// ELECTRICAL
		Entry electricalEntry = new Entry() {
			@Override
			public void setup() {
				setName("Electrical");
				setTag("ELECTRICAL");
				defaultValue = "1";
			}
		};
		electricalEntry.setId(++idCounter);
		// XCONDUCTIVITY
		Entry xCondEntry = new Entry() {
			@Override
			public void setup() {
				setName("X Conductivity");
				setTag("XCONDUCTIVITY");
				defaultValue = "1.7899";
			}
		};
		xCondEntry.setId(++idCounter);
		// YCONDUCTIVITY
		Entry yCondEntry = new Entry() {
			@Override
			public void setup() {
				setName("Y Conductivity");
				setTag("YCONDUCTIVITY");
				defaultValue = "1.7899";
			}
		};
		yCondEntry.setId(++idCounter);
		// ZCONDUCTIVITY
		Entry zCondEntry = new Entry() {
			@Override
			public void setup() {
				setName("Z Conductivity");
				setTag("ZCONDUCTIVITY");
				defaultValue = "1.7899";
			}
		};
		zCondEntry.setId(++idCounter);
		// HEATCAPACITY
		Entry heatCapEntry = new Entry() {
			@Override
			public void setup() {
				setName("Heat Capacity");
				setTag("HEATCAPACITY");
				defaultValue = "1100";
			}
		};
		heatCapEntry.setId(++idCounter);
		// ICSHORT
		Entry icShortEntry = new Entry() {
			@Override
			public void setup() {
				setName("IC Short");
				setTag("ICSHORT");
				defaultValue = "0";
			}
		};
		icShortEntry.setId(++idCounter);
		// NUMBCS
		Entry numBCsEntry = new Entry() {
			@Override
			public void setup() {
				setName("Number of Boundary Conditions");
				setTag("NUMBCS");
				defaultValue = "6";
			}
		};
		numBCsEntry.setId(++idCounter);
		// BCTYPE
		Entry BCTypeEntry = new Entry() {
			@Override
			public void setup() {
				setName("Boundary Condition Types");
				setTag("BCTYPE");
				defaultValue = "1,1,2,2,1,1";
			}
		};
		BCTypeEntry.setId(++idCounter);
		// BCIDS
		Entry BCIdsEntry = new Entry() {
			@Override
			public void setup() {
				setName("Boundary Condition Ids");
				setTag("BCIDS");
				defaultValue = "3,4,5,6,7,8";
			}
		};
		BCIdsEntry.setId(++idCounter);
		// AMBIENTTEMP
		Entry ambientTempEntry = new Entry() {
			@Override
			public void setup() {
				setName("Ambient Temperature");
				setTag("AMBIENTTEMP");
				defaultValue = "300,300,298,328,300,300";
			}
		};
		ambientTempEntry.setId(++idCounter);
		// CONVECTIVERATE
		Entry convectiveRateEntry = new Entry() {
			@Override
			public void setup() {
				setName("Convective Rate");
				setTag("CONVECTIVERATE");
				defaultValue = "0.0,0.0,5.0,5.0,1.0,1.0";
			}
		};
		convectiveRateEntry.setId(++idCounter);

		// Add the entries
		thermalComponent.addEntry(electricalEntry);
		thermalComponent.addEntry(xCondEntry);
		thermalComponent.addEntry(yCondEntry);
		thermalComponent.addEntry(zCondEntry);
		thermalComponent.addEntry(heatCapEntry);
		thermalComponent.addEntry(icShortEntry);
		thermalComponent.addEntry(numBCsEntry);
		thermalComponent.addEntry(BCTypeEntry);
		thermalComponent.addEntry(BCIdsEntry);
		thermalComponent.addEntry(ambientTempEntry);
		thermalComponent.addEntry(convectiveRateEntry);

		// Reset the counter for the next component;
		idCounter = 0;

		// Setup the entries for the electrical component. ICSHORT is reused
		// from above.

		// CurrentFlux
		Entry currentFluxEntry = new Entry() {
			@Override
			public void setup() {
				setName("Current Flux");
				setTag("CurrentFlux");
				defaultValue = "3602431";
			}
		};
		currentFluxEntry.setId(++idCounter);

		// Add the entries to the electrical component
		electricalComponent.addEntry(currentFluxEntry);

		// Reset the counter for the next component;
		idCounter = 0;

		// CRATE
		Entry crateEntry = new Entry() {
			@Override
			public void setup() {
				setName("CRATE");
				defaultValue = "5";
				setTag("CRATE");
			}
		};
		crateEntry.setId(++idCounter);
		// CURRDEN
		Entry currentDensityEntry = new Entry() {
			@Override
			public void setup() {
				setName("Current Density");
				setTag("CURRDEN");
				defaultValue = "79.26";
			}
		};
		currentDensityEntry.setId(++idCounter);
		// Cutoff
		Entry cutoffEntry = new Entry() {
			@Override
			public void setup() {
				setName("Cutoff");
				setTag("CUTOFF");
				defaultValue = "2.8";
			}
		};
		cutoffEntry.setId(++idCounter);
		// THICKNESS
		Entry thicknessEntry = new Entry() {
			@Override
			public void setup() {
				setName("Thickness");
				setTag("THICKNESS");
				defaultValue = "135e-6";
			}
		};
		thicknessEntry.setId(++idCounter);
		// YPolyCoefficients
		Entry yPolyEntry = new Entry() {
			@Override
			public void setup() {
				setName("Y Polynomial Coefficients");
				setTag("YPolyCoefficients");
				defaultValue = "3.6433e+02, -2.0629e+03, 1.1248e+04, -2.8978e+04, 4.0999e+04, -3.1353e+04, 9.8589e+03";
			}
		};
		yPolyEntry.setId(++idCounter);
		// UPolyCoefficients
		Entry uPolyEntry = new Entry() {
			@Override
			public void setup() {
				setName("U Polynomial Coefficients");
				setTag("UPolyCoefficients");
				defaultValue = "4.1210, -1.0206, -1.1902, 8.2938, -20.6735, 24.8684, -11.0422";
			}
		};
		uPolyEntry.setId(++idCounter);
		// YPolyDegrees
		Entry yPolyDegEntry = new Entry() {
			@Override
			public void setup() {
				setName("Y Polynomial Degrees of Freedom");
				setTag("YPolyDegrees");
				defaultValue = "6, 0";
			}
		};
		yPolyDegEntry.setId(++idCounter);
		// UPolyDegrees
		Entry uPolyDegEntry = new Entry() {
			@Override
			public void setup() {
				setName("U Polynomial Degrees of Freedom");
				setTag("UPolyDegrees");
				defaultValue = "6, 0";
			}
		};
		uPolyDegEntry.setId(++idCounter);

		// Add the entries to the electrochemical component
		electroChemComponent.addEntry(crateEntry);
		electroChemComponent.addEntry(currentDensityEntry);
		electroChemComponent.addEntry(cutoffEntry);
		electroChemComponent.addEntry(thicknessEntry);
		electroChemComponent.addEntry(yPolyEntry);
		electroChemComponent.addEntry(uPolyEntry);
		electroChemComponent.addEntry(yPolyDegEntry);
		electroChemComponent.addEntry(uPolyDegEntry);

		// Add the components to the Form
		form.addComponent(thermalComponent);
		form.addComponent(electricalComponent);
		form.addComponent(electroChemComponent);

		// We only need the export to key-value pair process,
		// so get rid of this one to conserve 'clicks'
		// Did Emmy Noether ever speak of click conservation???
		allowedActions.remove("Export to ICE Native Format");

		return;
	}

	/**
	 * This operation is used to setup the name and description of the
	 * generator.
	 */
	@Override
	public void setupItemInfo() {
		// Setup everything
		setName(CAEBATKVPairBuilder.name);
		itemType = CAEBATKVPairBuilder.type;
		setItemBuilderName(CAEBATKVPairBuilder.name);
		setDescription("A simple item to generate CAEBAT "
				+ "key-value pair files.");
	}

}
