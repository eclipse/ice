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
package org.eclipse.ice.client.widgets.reactoreditor.sfr;

import org.eclipse.jface.wizard.IWizard;

import org.eclipse.ice.client.widgets.reactoreditor.IWizardProvider;

import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.Material;
import org.eclipse.ice.reactor.sfr.core.MaterialBlock;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.eclipse.ice.reactor.sfr.core.assembly.PinAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.ReflectorAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRRod;

/**
 * @author tnp
 * 
 */
public class WizardProvider implements IWizardProvider, ISFRComponentVisitor {

	/**
	 * The IWizard instance that this class will initialize and provide via the
	 * {@link #getWizard(Object)} method.
	 */
	IWizard wizard;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor#visit(org.eclipse
	 * .ice .reactor.sfr.core.SFReactor)
	 */
	@Override
	public void visit(SFReactor sfrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor#visit(org.eclipse
	 * .ice .reactor.sfr.core.assembly.SFRAssembly)
	 */
	@Override
	public void visit(SFRAssembly sfrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor#visit(org.eclipse
	 * .ice .reactor.sfr.core.assembly.PinAssembly)
	 */
	@Override
	public void visit(PinAssembly sfrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor#visit(org.eclipse
	 * .ice .reactor.sfr.core.assembly.ReflectorAssembly)
	 */
	@Override
	public void visit(ReflectorAssembly sfrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor#visit(org.eclipse
	 * .ice .reactor.sfr.core.assembly.SFRPin)
	 */
	@Override
	public void visit(SFRPin sfrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor#visit(org.eclipse
	 * .ice .reactor.sfr.core.assembly.SFRRod)
	 */
	@Override
	public void visit(SFRRod sfrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor#visit(org.eclipse
	 * .ice .reactor.sfr.core.MaterialBlock)
	 */
	@Override
	public void visit(MaterialBlock sfrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor#visit(org.eclipse
	 * .ice .reactor.sfr.core.Material)
	 */
	@Override
	public void visit(Material sfrComp) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.reactor.sfr.base.ISFRComponentVisitor#visit(org.eclipse
	 * .ice .reactor.sfr.core.assembly.Ring)
	 */
	@Override
	public void visit(Ring sfrComp) {
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
			((SFRComponent) obj).accept(this);
		}
		// Handle ICEResources

		return wizard;
	}

}
