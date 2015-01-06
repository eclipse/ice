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
public class GenerateYAMLWizardPage extends WizardPage {

	/**
	 * 
	 */
	private Composite container;

	/**
	 * 
	 */
	private Text hostname;

	/**
	 * 
	 */
	private Text execPath;
	
	/**
	 * 
	 */
	private Text username;
	
	/**
	 * 
	 */
	private Text password;
	
	/**
	 * 
	 * @param pageName
	 */
	protected GenerateYAMLWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setDescription("Please provide the hostname and execution "
				+ "path for your MOOSE \napplications, and a username "
				+ "and password if they exist on a remote host.");
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
		label1.setText("Hostname:");

		hostname = new Text(container, SWT.BORDER | SWT.SINGLE);
		hostname.setText("localhost");
		hostname.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!hostname.getText().isEmpty()) {
					setPageComplete(true);

				}
			}

		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		hostname.setLayoutData(gd);
		
		new Label(container, SWT.NONE).setText("Execution Path:");
		execPath = new Text(container, SWT.BORDER | SWT.SINGLE);
		execPath.setText(System.getProperty("user.home") + System.getProperty("file.separator") + "projects");
		execPath.setLayoutData(gd);
		
		new Label(container, SWT.NONE).setText("Username:");
		username = new Text(container, SWT.BORDER | SWT.SINGLE);
		username.setText(System.getProperty("user.name"));
		username.setLayoutData(gd);
		
		new Label(container, SWT.NONE).setText("Password (if remote):");
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
	public String getHostName() {
		return hostname.getText();
	}
	
	/*
	 * 
	 */
	public String getExecPath() {
		return execPath.getText();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsername() {
		return username.getText();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return password.getText();
	}

}
