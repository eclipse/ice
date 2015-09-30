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
package org.eclipse.ice.developer.moose.actions;

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
 * The ForkStorkWizardPage is a subclass of WizardPage that 
 * provides a Composite for the Wizard that takes the user's 
 * desired MOOSE app name and GitHub credentials for forking the 
 * MOOSE stork. 
 * 
 * @author Alex McCaskey
 *
 */
public class ForkStorkWizardPage extends WizardPage {

	/**
	 * Reference to the Parent Composite.
	 */
	private Composite container;

	/**
	 * The text box for the Moose application name.
	 */
	private Text appName;

	/**
	 * The text box for the GitHub user name.
	 */
	private Text uName;
	
	/**
	 * The text box for the user's password.
	 * 
	 */
	private Text password;
	
	/**
	 * The constructor
	 * @param pageName
	 */
	protected ForkStorkWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Please provide the name of the Moose Application you'd like to create.");
	}

	/**
	 * Create a Composite with fields for the application name 
	 * and GitHub user credentials. 
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
	 * Return the Moose application name
	 * @return
	 */
	public String getMooseAppName() {
		return appName.getText();
	}
	
	/**
	 * Return the GitHub user name. 
	 * @return
	 */
	public String getGitUserName() {
		return uName.getText();
	}
	
	/**
	 * Return the GitHub password. 
	 * @return
	 */
	public String getGitPassword() {
		return password.getText();
	}
	
}
