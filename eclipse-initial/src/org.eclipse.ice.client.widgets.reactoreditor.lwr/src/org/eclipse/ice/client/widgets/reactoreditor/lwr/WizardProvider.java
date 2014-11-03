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

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.ice.client.widgets.reactoreditor.IWizardProvider;

import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.reactor.ILWRComponentVisitor;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;

/**
 * @author tnp
 * 
 */
public class WizardProvider implements IWizardProvider, ILWRComponentVisitor {

	/**
	 * The IWizard instance that this class will initialize and provide via the
	 * {@link #getWizard(Object)} method.
	 */
	Wizard wizard;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.ILWRComponentVisitor#visit(org.eclipse.ice.reactor
	 * .pwr.PressurizedWaterReactor)
	 */
	@Override
	public void visit(PressurizedWaterReactor lwrComp) {

		wizard = new Wizard() {

			@Override
			public boolean performFinish() {
				// TODO Auto-generated method stub
				return false;
			}
		};

		wizard.addPage(new WizardPage("PWRPage") {

			@Override
			public void createControl(Composite parent) {
				setTitle("Add an Assembly");
				setDescription("Use this page to add an assembly to the"
						+ " selected reactor.");

				Composite composite = new Composite(parent, SWT.NONE);
				composite.setLayout(new FillLayout());

				Label testLabel = new Label(composite, SWT.NONE);
				testLabel.setText("Composite area for this page");

				setControl(composite);

				return;
			}
		});

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.ILWRComponentVisitor#visit(org.eclipse.ice.reactor
	 * .bwr.BWReactor)
	 */
	@Override
	public void visit(BWReactor lwrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.ILWRComponentVisitor#visit(org.eclipse.ice.reactor
	 * .pwr.FuelAssembly)
	 */
	@Override
	public void visit(FuelAssembly lwrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.ILWRComponentVisitor#visit(org.eclipse.ice.reactor
	 * .pwr.RodClusterAssembly)
	 */
	@Override
	public void visit(RodClusterAssembly lwrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.ILWRComponentVisitor#visit(org.eclipse.ice.reactor
	 * .LWRRod)
	 */
	@Override
	public void visit(LWRRod lwrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.ILWRComponentVisitor#visit(org.eclipse.ice.reactor
	 * .pwr.ControlBank)
	 */
	@Override
	public void visit(ControlBank lwrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.ILWRComponentVisitor#visit(org.eclipse.ice.reactor
	 * .pwr.IncoreInstrument)
	 */
	@Override
	public void visit(IncoreInstrument lwrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.ILWRComponentVisitor#visit(org.eclipse.ice.reactor
	 * .Tube)
	 */
	@Override
	public void visit(Tube lwrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.ILWRComponentVisitor#visit(org.eclipse.ice.reactor
	 * .Ring)
	 */
	@Override
	public void visit(Ring lwrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.reactoreditor.IWizardProvider#getWizard ()
	 */
	@Override
	public IWizard getWizard(Object obj) {
		// Handle IReactorComponents
		if (obj instanceof IReactorComponent) {
			((LWRComponent) obj).accept(this);
		}
		// Handle ICEResources

		return wizard;
	}
}
