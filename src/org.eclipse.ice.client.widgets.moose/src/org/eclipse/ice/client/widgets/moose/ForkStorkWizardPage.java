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
package org.eclipse.ice.client.widgets.moose;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Alex McCaskey
 *
 */
public class ForkStorkWizardPage extends WizardPage {

	/**
	 * 
	 */
	private Composite container;

	/**
	 * 
	 */
	private Text appName;

	private Text uName;
	
	/**
	 * 
	 */
	private Text password;
	
	/**
	 * 
	 * @param pageName
	 */
	protected ForkStorkWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Please provide the name of the Moose Application you'd like to create.");
	}

	/**
	 * 
	 */
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		Label label1 = new Label(container, SWT.NONE);
		label1.setText("MooseApp Name:");

		appName = new Text(container, SWT.BORDER | SWT.SINGLE);
		appName.setText("");
		appName.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!appName.getText().isEmpty()) {
					setPageComplete(true);

				}
			}

		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		appName.setLayoutData(gd);
		
		new Label(container, SWT.NONE).setText("GitHub Username:");
		uName = new Text(container, SWT.BORDER | SWT.SINGLE);
		uName.setText(System.getProperty("user.name"));
		uName.setLayoutData(gd);
		
		new Label(container, SWT.NONE).setText("GitHub Password:");
		password = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);
		password.setText("");
		password.setLayoutData(gd);
		
		// required to avoid an error in the system
		setControl(container);
		setPageComplete(true);

	}

	/**
	 * 
	 * @return
	 */
	public String getMooseAppName() {
		return appName.getText();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getGitUserName() {
		return uName.getText();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getGitPassword() {
		return password.getText();
	}
	
}
