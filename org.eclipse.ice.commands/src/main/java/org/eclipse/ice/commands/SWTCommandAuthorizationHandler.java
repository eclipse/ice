/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This class enables the password authentication for commands through the
 * standard widget toolkit (SWT) of java.
 * 
 * @author Joe Osborn
 *
 */
public class SWTCommandAuthorizationHandler implements CommandAuthorizationHandler {

	/**
	 * A char array to hold the password that is obtained from the widget
	 */
	char[] password = null;

	/**
	 * Default constructor
	 */
	public SWTCommandAuthorizationHandler() {
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.CommandAuthorizationHandler#getPassword()}
	 */
	@Override
	public char[] getPassword() {
		// Create a new widget display
		Display display = new Display();
		Shell shell = new Shell(display);

		// Layout
		RowLayout rowLayout = new RowLayout();
		rowLayout.spacing = 10;
		rowLayout.marginLeft = 10;
		rowLayout.marginTop = 10;

		// Set the layout
		shell.setLayout(rowLayout);
		
		// Create the password field box to take the password in
		Text passwordField = new Text(shell, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		
		// Create an enter button to register the password
		Button enterPwd = new Button(shell, SWT.PUSH);
		enterPwd.setText("Enter");

		// Add to the enter button the requirement that when it is clicked,
		// the password field text is obtained
		enterPwd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Get the password and set the SWTCommandAuthorizationHandler member
				// variable
				String thePassword = passwordField.getText();
				setPassword(thePassword.toCharArray());
				
				// If enter is selected, close the widget completely since this means
				// we got the password
				display.dispose();
			}
		});

		// Create a button to show the password input, if the user desires
		Button readPwd = new Button(shell, SWT.PUSH);
		readPwd.setText("Show Password");

		// Label to cover the "show password" area
		Label labelInfo = new Label(shell, SWT.NONE);
		labelInfo.setText("????");

		// Add to the button the requirement that when it is clicked, it shows the text
		// in the box
		
		readPwd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Set the label to the text in the password field
				labelInfo.setText(passwordField.getText());
				labelInfo.pack();
			}
		});

		// Make the shell smaller, so it is not as unruly
		shell.setSize(400, 200);

		// Open the widget
		shell.open();

		// While the widget is not exited, display it
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		// Get rid of the widget
		display.dispose();

		// Return the password, which should now be set. If it isn't, will return null
		return password;
	}

	/**
	 * Setter function for the password. Allows the member variable to be set from
	 * inside the function which overrides the 'enter' button functionality in
	 * {@link org.eclipse.ice.commands.SWTCommandAuthorizationHandler#getPassword()}
	 * 
	 * @param password
	 */
	public void setPassword(char[] password) {
		this.password = password;
	}
}
