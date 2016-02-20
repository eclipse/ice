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
package org.eclipse.eavp.viz.visit;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtConnectionManager;

/**
 * This class extends WizardPage to create the Wizard content to allow the user
 * to establish a VisIt client connection. This connection may be via a local or
 * remote launch or by connecting to a running VisIt session.
 * 
 * @author Taylor Patterson, Harinarayan Krishnan
 * 
 */
public class LaunchVisitWizardPage extends WizardPage {

	/**
	 * The user's selection to exit the dialog ("Launch" to launch, "Connect" to
	 * connect to service, null to cancel)
	 */
	private String dialogExitSelection;

	/**
	 * Sets new or existing window name
	 */
	private String connectionId;

	/**
	 * Sets new windowId
	 */
	private String windowId;

	/**
	 * The directory where the VisIt executable is located
	 */
	private String visItDir;

	/**
	 * The name of the host running VisIt or where VisIt should be launched
	 */
	private String hostname;

	/**
	 * The port VisIt will serve connections on
	 */
	private String port;

	/**
	 * The password for connection to VisIt
	 */
	private String password;

	/**
	 * The gateway used for out-of-network connections
	 */
	private String gateway;

	/**
	 * The local port used to connect to the gateway
	 */
	private String localGatewayPort;

	/**
	 * Flag for whether or not tunneling is used
	 */
	private boolean use_tunneling;

	/**
	 * Select existing connection or create a new one.
	 */
	private Combo connectionCombo;

	/**
	 * Select the ID of the VisIt window to display.
	 */
	private Combo windowIdCombo;

	/**
	 * The radio buttons for selecting the connection type
	 */
	private Button localRadio;
	private Button remoteRadio;
	private Button serviceRadio;

	/**
	 * The composites for defining the path to the VisIt executable
	 */
	private PathComposite localPathComp;
	private PathComposite remotePathComp;

	/**
	 * The mechanisms for defining the port for VisIt to serve to
	 */
	private PortComposite localPortComp;
	private PortComposite remotePortComp;
	private Text servicePortText;

	/**
	 * The mechanisms for defining the VisIt connection password
	 */
	private PasswordComposite localPasswordComp;
	private PasswordComposite remotePasswordComp;
	private Text servicePassText;

	/**
	 * The composites where the hostname is recorded
	 */
	private HostComposite remoteHostComp;
	private HostComposite serviceHostComp;

	/**
	 * The composites for defining out-of-network gateway parameters
	 */
	private GatewayComposite remoteGatewayComp;
	private GatewayComposite serviceGatewayComp;

	/**
	 * The constructor
	 * 
	 * @param pageName
	 *            The String ID of this WizardPage
	 */
	protected LaunchVisitWizardPage(String pageName) {

		// Call WizardPage's constructor
		super(pageName);

		setTitle("Establish a connection to VisIt");
		setDescription("Select a connection method and fill in the required " + "parameters.");

		// Set the default values
		dialogExitSelection = null;
		visItDir = "";
		hostname = "";
		port = "-1";
		password = "";
		gateway = "";
		localGatewayPort = "-1";
		use_tunneling = false;

		// Disable the "Finish" button
		setPageComplete(false);

		return;
	}

	/**
	 * This operation is called whenever the user clicks the help button on this
	 * page in the wizard. Display additional help text for the user.
	 */
	@Override
	public void performHelp() {
		MessageDialog.open(INFORMATION, getShell(), "Help", "Use this dialog to establish a connection to VisIt. "
				+ "You may launch VisIt on this machine or a " + "remote machine or connect to a VisIt session "
				+ "that was previously launched.", SWT.NONE);
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		// Create the ScrolledComposite, setting its layout data
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite
				.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 200).create());
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		// Create a Composite that contains the rest of the WizardPage and sits
		// on top of the ScrolledComposite
		Composite container = new Composite(scrolledComposite, SWT.NULL);
		container.setLayout(GridLayoutFactory.swtDefaults().numColumns(1).create());

		// Create the Launch Dialog contents
		createContents(container);

		// Set the ScrolledComposite content as the containing composite
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// Set the parent as the control for the receiver.
		setControl(parent);

		return;
	}

	/**
	 * Create and layout the widgets of the dialog.
	 * 
	 * @param parent
	 *            The parent Shell to contain the dialog
	 */
	private void createContents(final Composite parent) {

		/*------- Layout all of the widgets -------*/

		/*--- Local Launch ---*/
		// Set up the radio to launch locally
		localRadio = new Button(parent, SWT.RADIO);
		localRadio.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
		localRadio.setText("Launch Visit locally");

		// Create the local launch widget composite
		final Composite localComp = new Composite(parent, SWT.BORDER);
		localComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		localComp.setLayout(new GridLayout(1, false));

		// Create the path composite
		localPathComp = new PathComposite(localComp, false);

		// Create the port composite
		localPortComp = new PortComposite(localComp);

		// Create the password composite
		localPasswordComp = new PasswordComposite(localComp);

		// Disable this section by default
		enableSection(localComp, false);
		/*--------------------*/

		/*--- Remote Launch --*/
		// Set up the radio to launch remotely
		remoteRadio = new Button(parent, SWT.RADIO);
		remoteRadio.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		remoteRadio.setText("Launch Visit remotely");

		// Create the remote launch widget group
		final Composite remoteComp = new Composite(parent, SWT.BORDER);
		remoteComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		remoteComp.setLayout(new GridLayout(1, false));

		// Create the host composite
		remoteHostComp = new HostComposite(remoteComp);

		// Create the path composite
		remotePathComp = new PathComposite(remoteComp, true);

		// Create the port composite
		remotePortComp = new PortComposite(remoteComp);

		// Create the password composite
		remotePasswordComp = new PasswordComposite(remoteComp);

		// Create the gateway composite
		remoteGatewayComp = new GatewayComposite(remoteComp);

		// Disable this section by default
		enableSection(remoteComp, false);
		/*--------------------*/

		/*-- Service Connect -*/
		// Set up the radio to connect to a service
		serviceRadio = new Button(parent, SWT.RADIO);
		serviceRadio.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
		serviceRadio.setText("Connect to VisIt");

		// Create the connect to service widget group
		final Composite serviceComp = new Composite(parent, SWT.BORDER);
		serviceComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		serviceComp.setLayout(new GridLayout(1, false));

		// Create the host composite
		serviceHostComp = new HostComposite(serviceComp);

		// Create the port composite
		Composite servicePortComp = new Composite(serviceComp, SWT.NONE);
		servicePortComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		servicePortComp.setLayout(new GridLayout(2, false));
		// Add a Label to identify the adjacent Text
		Label servicePortLabel = new Label(servicePortComp, SWT.NONE);
		servicePortLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		servicePortLabel.setText("Port for connecting to VisIt:");
		// Add a Text for the user to input the port number
		servicePortText = new Text(servicePortComp, SWT.BORDER);
		servicePortText.setText(port);
		servicePortText.setLayoutData(new GridData(50, SWT.DEFAULT));

		// Create the password composite
		Composite servicePassComp = new Composite(serviceComp, SWT.NONE);
		servicePassComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		servicePassComp.setLayout(new GridLayout(2, false));
		// Add a label to identify the adjacent Text
		Label servicePassLabel = new Label(servicePassComp, SWT.NONE);
		servicePassLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		servicePassLabel.setText("Password to connect to VisIt service:");
		// Add a Text for the user to input the password
		servicePassText = new Text(servicePassComp, SWT.PASSWORD | SWT.BORDER);
		servicePassText.setText(password);
		servicePassText.setLayoutData(new GridData(100, SWT.DEFAULT));

		// Create the gateway composite
		serviceGatewayComp = new GatewayComposite(serviceComp);

		// Disable this section by default
		enableSection(serviceComp, false);
		/*--------------------*/
		/*-----------------------------------------*/

		/*------ Setup the button listeners -------*/
		// Create the listener for the local launch radio button. The listener
		// will enable the "Finish" button and the widgets in the local launch
		// composite. If the remote launch or connect to service composites have
		// been enabled, they will be disabled.
		localRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				// Change the help text at the top of the dialog
				setTitle("Launch VisIt on this machine.");
				setDescription("Enter the file system path to the VisIt "
						+ "executable. Optionally, set a port number and "
						+ "password to allow other users to connect to this " + "session.");

				// Enable the widgets in the local launch composite and the
				// "Finish" button
				enableSection(localComp, true);
				setPageComplete(true);

				// Disable the widgets in the remote launch and connect to
				// service composites
				enableSection(remoteComp, false);
				enableSection(serviceComp, false);
			}
		});

		// Create the listener for the remote launch radio button. The listener
		// will enable the "Finish" button and the widgets in the remote launch
		// composite. If the local launch or connect to service composites have
		// been enabled, they will be disabled.
		remoteRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				// Change the help text at the top of the dialog
				setTitle("Launch VisIt on a remote machine.");
				setDescription("Enter the host to run the VisIt session and the "
						+ "file system path to the VisIt executable. Optionally,"
						+ " set a port, password, or use an out-of-network " + "gateway.");

				// Enable the widgets in the local launch composite and the
				// "Finish" button
				enableSection(remoteComp, true);
				setPageComplete(true);

				// Disable the widgets in the remote launch and connect to
				// service composites
				enableSection(localComp, false);
				enableSection(serviceComp, false);
			}
		});

		// Create the listener for the connect to service radio button. The
		// listener will enable the "Finish" button and the widgets in the
		// connect to service composite. If the local or remote launch
		// composites have been enabled, they will be disabled.
		serviceRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				// Change the help text at the top of the dialog
				setTitle("Connect to a running VisIt client.");
				setDescription("Enter the host running VisIt and the port "
						+ "and password used for validating connections. "
						+ "Optionally, use and out-of-network gateway to " + "access the host.");

				// Enable the widgets in the local launch composite and the
				// "Finish" button
				enableSection(serviceComp, true);
				setPageComplete(true);

				// Disable the widgets in the remote launch and connect to
				// service composites
				enableSection(localComp, false);
				enableSection(remoteComp, false);
			}
		});

		/*--- Connection Management Widgets ---*/
		createConnectionManagerWidget(parent);

		return;
	}

	/**
	 * This method initializes the widgets used for recording and managing VisIt
	 * connections and VisIt window selection.
	 * 
	 * @param parent
	 *            The composite containing these widgets.
	 */
	private void createConnectionManagerWidget(Composite parent) {

		// Create a Composite to hold these widgets
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));

		// Set the Composite's layout
		GridLayout layout = new GridLayout(4, false);
		layout.marginHeight = 2;
		comp.setLayout(layout);

		// Create the Combo for managing VisIt connections
		Label connectionLabel = new Label(comp, SWT.NONE);
		connectionLabel.setText("Connection name:");
		connectionCombo = new Combo(comp, SWT.NONE);
		connectionCombo.add("New Connection...");
		connectionCombo.select(0);

		// Create the Combo for selecting VisIt windows
		Label windowIdLabel = new Label(comp, SWT.NONE);
		windowIdLabel.setText("Window ID:");
		windowIdCombo = new Combo(comp, SWT.NONE | SWT.READ_ONLY);
		windowIdCombo.setEnabled(false);
		windowIdCombo.add("New Window..");
		windowIdCombo.select(0);

		// Create a map to record the created connections
		Map<String, VisItSwtConnection> connectionMap = VisItSwtConnectionManager.getConnMap();

		// Get the existing connection names and add them to the Combo
		Set<String> keys = connectionMap.keySet();
		for (String key : keys) {
			connectionCombo.add(key);
		}

		// Add a listener to the Combo for connections
		connectionCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				// Get the selection index. If this is anything other than the
				// first item in the Combo, enable the window selection.
				int index = connectionCombo.getSelectionIndex();
				if (index == 0) {
					windowIdCombo.setEnabled(false);
				} else {
					windowIdCombo.setEnabled(true);

					// Get the connection from the Combo.
					VisItSwtConnection conn = VisItSwtConnectionManager.getConnection(connectionCombo.getItem(index));
					// Clear and repopulate the window selection Combo
					windowIdCombo.removeAll();
					windowIdCombo.add("New Window..");
					windowIdCombo.select(0);

					// The list of available window IDs
					List<Integer> windowIdList = conn.getWindowIds();

					// Add the window IDs to the combo box
					for (int i = 0; i < windowIdList.size(); ++i) {
						windowIdCombo.add(Integer.toString(windowIdList.get(i)));
					}

					// Enable the 'Finish' button
					setPageComplete(true);
				}

				return;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Not used at this time
				return;
			}
		});
	}

	/**
	 * This function retrieves the name of the VisIt connection.
	 * 
	 * @return The String ID of the VisIt connection
	 */
	public String getConnectionId() {
		return connectionId;
	}

	/**
	 * This function retrieves the VisIt window ID selected in the Combo.
	 * 
	 * @return The String ID of the VisIt window
	 */
	public String getConnectionWindowId() {
		return windowId;
	}

	/**
	 * This function retrieves the password used to connect to a VisIt client.
	 * 
	 * @return The string used as the VisIt client password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This function retrieves the gateway URL.
	 * 
	 * @return The string representation of the gateway URL.
	 */
	public String getGateway() {
		return gateway;
	}

	/**
	 * This function retrieves the gateway port.
	 * 
	 * @return The string representation of the local port used for the gateway
	 *         connection.
	 */
	public String getGatewayPort() {
		return localGatewayPort;
	}

	/**
	 * This function retrieves the whether or not tunneling is used.
	 * 
	 * @return The string representation of the boolean value for whether or not
	 *         tunneling is used.
	 */
	public String getUseTunneling() {
		return Boolean.toString(use_tunneling);
	}

	/**
	 * This function retrieves the hostname where the VisIt client will or has
	 * been launched.
	 * 
	 * @return The string representation of the hostname.
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * This function retrieves the port number that the VisIt client is
	 * broadcasting on.
	 * 
	 * @return The string representation of the port number.
	 */
	public String getVisItPort() {
		return port;
	}

	/**
	 * This function retrieves the director where the VisIt executable resides.
	 * 
	 * @return The string representation of the VisIt executable directory.
	 */
	public String getVisItDir() {
		return visItDir;
	}

	/**
	 * This function returns true if the user chose to connect to a running
	 * VisIt service and false otherwise.
	 * 
	 * @return The string representation of whether or not to connect to a
	 *         running VisIt service.
	 */
	public String getIsLaunch() {
		if ("Launch".equals(dialogExitSelection)) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * This operation is used to enable or disable a given Control. If the
	 * Control is a Composite, recursively call this method on the children of
	 * the Composite. If the Control is an ICheckComposite, keep the Text
	 * adjacent to the check button disabled if the check is not selected.
	 * 
	 * @param control
	 *            The Control to be enabled or disabled
	 * @param enable
	 *            The value to set control to (true to enable or false to
	 *            disable)
	 */
	private void enableSection(Control control, boolean enable) {

		// If control is a Composite, call this function on all of the
		// Composite's children.
		if (control instanceof Composite) {
			Composite comp = (Composite) control;
			for (Control c : comp.getChildren()) {
				enableSection(c, enable);
			}
		}
		// If this is a Text that should be disabled based on check box, keep it
		// that way.
		else if (control instanceof Text && control.getParent() instanceof ICheckComposite
				&& !((ICheckComposite) control.getParent()).isSelected()) {
			control.setEnabled(false);
		}
		// Otherwise, enable or disable this widget based on the input value.
		else {
			control.setEnabled(enable);
		}

		return;
	}

	/**
	 * This operation gets the radio selection and sets the fields of this class
	 * appropriately.
	 */
	public boolean setFinishFields() {

		// Get the selection from the existing connection combo box
		int index = connectionCombo.getSelectionIndex();

		// Get the IDs for the connection and the window
		connectionId = connectionCombo.getText();
		windowId = "New Window..".equals(windowIdCombo.getText()) ? "-1" : windowIdCombo.getText();

		// If this is a brand new connection, then assign the first window to
		// this connection. Note: Combo#getSelectionIndex() returns -1 if no
		// item is selected.
		if (index == -1) {
			windowId = "1";
		}

		// Force the user to set a connection name
		if (index == 0) {
			MessageDialog.openError(getShell(), "Invalid Key", "Please assign" + " a name to this connection.");
			return false;
		}

		// Allow the user to select a previously configured connection
		if (index > 0) {
			// TODO: handle pre-existing connection option appropriately.
			dialogExitSelection = "false";
			return true;
		}

		// Set the fields for a local launch if the local launch radio is
		// selected.
		if (localRadio.getSelection()) {
			dialogExitSelection = "Launch";
			// Get the path or prompt an error if the user has left the field
			// empty.
			if (!localPathComp.getPathString().isEmpty()) {
				visItDir = localPathComp.getPathString();
			} else {
				// Display an error prompt to enter a path
				MessageDialog.openError(getShell(), "Invalid Path", "Please enter the path to a Visit executable.");
				return false;
			}
			hostname = "localhost";
			// Get the port number or prompt an error if the user has left the
			// field empty.
			if (!localPortComp.getPortString().isEmpty()) {
				port = localPortComp.getPortString();
			} else {
				// Display an error prompt to enter a port number
				MessageDialog.openError(getShell(), "Invalid Port", "Please enter the ID of an open port for the "
						+ "Visit connection.");
				return false;
			}
			// Check if the specified port is in use, then prompt an
			// error if it is.
			try (Socket test = new Socket("localhost", Integer.valueOf(port))) {
				MessageDialog.openError(getShell(), "Port in use", "The specified port number is already in use. "
						+ "Please select a different port.");
				return false;
			} catch (IOException e) {
			}
			password = localPasswordComp.getPassword();
			use_tunneling = false;
		}
		// Set the fields for a remote launch if this is the user's
		// selection.
		else if (remoteRadio.getSelection()) {
			dialogExitSelection = "Launch";
			// Get the hostname or prompt an error if the user has left the
			// field empty.
			if (!remoteHostComp.getHostString().isEmpty()) {
				hostname = remoteHostComp.getHostString();
			} else {
				// Display an error prompt to enter a path
				MessageDialog.openError(getShell(), "Invalid Hostname", "Please enter a valid hostname.");
				return false;
			}
			// Get the path or prompt an error if the user has left the field
			// empty.
			if (!remotePathComp.getPathString().isEmpty()) {
				visItDir = remotePathComp.getPathString();
			} else {
				// Display an error prompt to enter a path
				MessageDialog.openError(getShell(), "Invalid Path", "Please enter the path to a Visit executable.");
				return false;
			}
			// Get the port number or prompt an error if the user has
			// left the field empty.
			if (!remotePortComp.getPortString().isEmpty()) {
				port = remotePortComp.getPortString();
			} else {
				// Display an error prompt to enter a port
				MessageDialog.openError(getShell(), "Invalid Port", "Please enter the ID of an open port for the "
						+ "Visit connection.");
				return false;
			}

			//TODO add better check for duplicate open ports
			// Check if the specified port is in use, then prompt an
			// error if it is.
			/*try (Socket test = new Socket(hostname, Integer.valueOf(port))) {
				MessageDialog.openError(getShell(), "Port in use", "The specified port number is already in use. "
						+ "Please select a different port.");
				return false;
			} catch (IOException e) {
			}*/

			password = remotePasswordComp.getPassword();
			use_tunneling = true;
			gateway = remoteGatewayComp.getURLString();
			localGatewayPort = remoteGatewayComp.getPortString();
		}
		// Set the fields for connecting to a running VisIt service if
		// this is the user's selection.
		else if (serviceRadio.getSelection()) {
			dialogExitSelection = "Connect";
			// Get the hostname or prompt an error if the user has left the
			// field empty.
			if (!serviceHostComp.getHostString().isEmpty()) {
				hostname = serviceHostComp.getHostString();
			} else {
				// Display an error prompt to enter a path
				MessageDialog.openError(getShell(), "Invalid Hostname", "Please enter a valid hostname.");
				return false;
			}
			// Get the port number or prompt an error if the user has
			// left the field empty.
			if (!servicePortText.getText().isEmpty()) {
				port = servicePortText.getText();
			} else {
				// Display an error prompt to enter a port
				MessageDialog.openError(getShell(), "Invalid Port", "Please enter the ID of an open port for the "
						+ "Visit connection.");
				return false;
			}

			//TODO add better check for duplicate open ports
			// try (Socket test = new Socket("localhost",
			// Integer.valueOf(port))) {
			// MessageDialog.openError(getShell(), "Port in use",
			// "The specified port number is already in use. "
			// + "Please select a different port.");
			// return false;
			// } catch (IOException e) {
			// }

			password = servicePassText.getText();
			use_tunneling = false;
			gateway = serviceGatewayComp.getURLString();
			localGatewayPort = serviceGatewayComp.getPortString();
		}
		// The "Finish" button should not be enabled without a radio
		// selection, but if this scenario occurs, just return exit as
		// if "Cancel" was selected.
		else {
			dialogExitSelection = null;
		}

		return true;
	}

	/**
	 * This class extends Composite to create a collection of widgets for
	 * selecting the path to VisIt.
	 * 
	 * @author Taylor Patterson
	 */
	private class PathComposite extends Composite {

		/**
		 * The Text widget to enter/display the path
		 */
		private Text pathText;

		/**
		 * The constructor
		 * 
		 * @param parent
		 *            The Composite containing an instance of this Composite.
		 * @param isRemote
		 *            True if the remote system dialog is needed; false
		 *            otherwise
		 */
		public PathComposite(final Composite parent, final boolean isRemote) {

			// Call Composite's constructor
			super(parent, SWT.NONE);

			// Set the layout data for placement in the parent Composite.
			setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

			// Set the layout of this Composite.
			setLayout(new GridLayout(3, false));

			// Create a Label to identify the Text purpose
			Label pathLabel = new Label(this, SWT.NONE);
			pathLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
			pathLabel.setText("Path to VisIt:");

			// Create the Text to enter and display the path to VisIt
			pathText = new Text(this, SWT.BORDER);
			pathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

			// TODO Implement a remote system browser that doesn't rely on an
			// existing VisIt connection. If this is for a remote connection,
			// don't create a 'Browse' button
			if (isRemote) {
				return;
			}

			// Create the 'Browse' button to access a DirectoryDialog to define
			// the path to VisIt
			Button pathButton = new Button(this, SWT.PUSH);
			pathButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
			pathButton.setText("Browse");
			pathButton.setToolTipText("Select the VisIt installation " + "directory");

			// Add the listener to the Button
			pathButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					// Open the file system exploration dialog
					DirectoryDialog dialog = new DirectoryDialog(parent.getShell(), SWT.OPEN);
					String res = dialog.open();
					if (res != null) {
						pathText.setText(res);
					}
				}
			});

			return;
		}

		/**
		 * This function retrieves the contents of the Text widget
		 * 
		 * @return The String contents of the Text widget for defining the path
		 *         to VisIt
		 */
		public String getPathString() {
			return pathText.getText();
		}

	}

	/**
	 * This interface is implemented Composites that contain check buttons with
	 * associated Text widgets.
	 * 
	 * @author Taylor Patterson
	 */
	private interface ICheckComposite {

		/**
		 * This method returns the boolean value of whether or not the
		 * implementing Composite has a check button.
		 * 
		 * @return True if the implementing class contains a check button
		 */
		public boolean hasCheckBox();

		/**
		 * This operation retrieves the selection value of the check button.
		 * 
		 * @return True in the check button is selected; false, otherwise.
		 */
		public boolean isSelected();
	}

	/**
	 * This class extends Composite to create a collection of widgets for
	 * setting the port for VisIt to communicate on.
	 * 
	 * @author Taylor Patterson
	 */
	private class PortComposite extends Composite implements ICheckComposite {

		/**
		 * The Text for the port number string
		 */
		private Text portText;

		/**
		 * The check button to allow/disallow port input
		 */
		private Button portButton;

		/**
		 * The constructor
		 * 
		 * @param parent
		 *            The parent Composite containing this Composite
		 */
		public PortComposite(Composite parent) {

			// Call Composite's constructor
			super(parent, SWT.NONE);

			// Set the layout data for the parent composite
			setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));

			// Set the layout for this composite
			setLayout(new GridLayout(2, false));

			// Create the check button to use or disallow setting the port
			portButton = new Button(this, SWT.CHECK);
			portButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
			portButton.setText("Set a port for connecting to VisIt:");

			// Create the Text for inputting the port number
			portText = new Text(this, SWT.BORDER);
			portText.setText("9600");
			portText.setLayoutData(new GridData(50, SWT.DEFAULT));
			portText.setEnabled(false);

			// Add a listener for the check button to enable/disable the
			// adjacent Text based on the button selection
			portButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					if (portButton.getSelection()) {
						portText.setEnabled(true);
					} else {
						portText.setText("9600");
						portText.setEnabled(false);
					}
				}
			});

			return;
		}

		/**
		 * This operation retrieves the value in the port Text
		 * 
		 * @return The String in the port Text widget
		 */
		public String getPortString() {
			return portText.getText();
		}

		/**
		 * @see ICheckComposite#hasCheckBox()
		 */
		@Override
		public boolean hasCheckBox() {
			return true;
		}

		/**
		 * @see ICheckComposite#isSelected()
		 */
		@Override
		public boolean isSelected() {
			return portButton.getSelection();
		}

	}

	/**
	 * This class extends Composite to create a collection of widgets for
	 * setting the password for incoming VisIt to connections.
	 * 
	 * @author Taylor Patterson
	 */
	private class PasswordComposite extends Composite implements ICheckComposite {

		/**
		 * The Text to input a password String
		 */
		private Text passText;

		/**
		 * The check button to allow/disallow defining a password
		 */
		private Button passButton;

		/**
		 * The constructor
		 * 
		 * @param parent
		 *            The parent Composite containing this Composite
		 */
		public PasswordComposite(Composite parent) {

			// Call Composite's constructor
			super(parent, SWT.NONE);

			// Set the layout data for the parent
			setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));

			// Set the layout of this Composite
			setLayout(new GridLayout(2, false));

			// Create the check button
			passButton = new Button(this, SWT.CHECK);
			passButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
			passButton.setText("Set a password for connecting to VisIt:");

			// Create the Text widget
			passText = new Text(this, SWT.PASSWORD | SWT.BORDER);
			passText.setLayoutData(new GridData(100, SWT.DEFAULT));
			passText.setEnabled(false);

			// Add the listener for the button to allow/disallow the use of a
			// password
			passButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					if (passButton.getSelection()) {
						passText.setEnabled(true);
					} else {
						passText.setEnabled(false);
					}
				}
			});

			return;
		}

		/**
		 * This operation retrieves the contents of the password Text widget or
		 * a default password if the widget is empty.
		 * 
		 * @return The String password
		 */
		public String getPassword() {
			if (!passText.getText().isEmpty()) {
				return passText.getText();
			} else {
				return "notused";
			}
		}

		/**
		 * @see ICheckComposite#hasCheckBox()
		 */
		@Override
		public boolean hasCheckBox() {
			return true;
		}

		/**
		 * @see ICheckComposite#isSelected()
		 */
		@Override
		public boolean isSelected() {
			return passButton.getSelection();
		}

	}

	/**
	 * This class extends Composite to create a collection of widgets for
	 * setting the host to launch VisIt on or where VisIt is running.
	 * 
	 * @author Taylor Patterson
	 */
	private class HostComposite extends Composite {

		/**
		 * The Text for setting the hostname
		 */
		private Text hostText;

		/**
		 * The constructor
		 * 
		 * @param parent
		 *            The Composite that contains this Composite
		 */
		public HostComposite(Composite parent) {

			// Call Composite's constructor
			super(parent, SWT.NONE);

			// Set the layout data for the parent
			setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));

			// Set the layout of this Composite
			setLayout(new GridLayout(2, false));

			// Add a Label to identify the adjacent Text
			Label hostLabel = new Label(this, SWT.NONE);
			hostLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
			hostLabel.setText("Hostname:");

			// Add the host Text
			hostText = new Text(this, SWT.BORDER);
			hostText.setLayoutData(new GridData(100, SWT.DEFAULT));

			return;
		}

		/**
		 * This operation retrieves the contents of the host Text widget
		 * 
		 * @return The String in the host Text
		 */
		public String getHostString() {
			return hostText.getText();
		}

	}

	/**
	 * This class extends Composite to create a collection of widgets for
	 * setting the gateway URL and port.
	 * 
	 * @author Taylor Patterson
	 */
	private class GatewayComposite extends Composite implements ICheckComposite {

		/**
		 * The Text for the gateway URL
		 */
		private Text urlText;

		/**
		 * The Text for the gateway port
		 */
		private Text gatePortText;

		/**
		 * The check button to allow/disallow using a gateway for connecting
		 */
		private Button gateButton;

		/**
		 * The constructor
		 * 
		 * @param parent
		 *            The parent Composite containing this Composite
		 */
		public GatewayComposite(Composite parent) {

			// Call Composite's constructor
			super(parent, SWT.NONE);

			// Set the layout data for the parent
			setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));

			// Set this Composite's layout
			setLayout(new GridLayout(3, false));

			// Create the check button to allow/disallow using a gateway
			gateButton = new Button(this, SWT.CHECK);
			gateButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
			gateButton.setText("Use an out-of-network gateway:");

			// Create the Text for gateway URL input
			urlText = new Text(this, SWT.BORDER);
			urlText.setLayoutData(new GridData(100, SWT.DEFAULT));
			urlText.setText("URL");
			urlText.setEnabled(false);

			// Create the Text for gateway port input
			gatePortText = new Text(this, SWT.BORDER);
			gatePortText.setLayoutData(new GridData(50, SWT.DEFAULT));
			gatePortText.setText("Port");
			gatePortText.setEnabled(false);

			// Add a listener to enable/disable the Text widgets based on the
			// check selection
			gateButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					if (gateButton.getSelection()) {
						urlText.setEnabled(true);
						gatePortText.setEnabled(true);
					} else {
						urlText.setEnabled(false);
						urlText.setText("URL");
						gatePortText.setEnabled(false);
						gatePortText.setText("Port");
					}
				}
			});

			return;
		}

		/**
		 * This operation retrieves the gateway URL
		 * 
		 * @return The String for the gateway URL
		 */
		public String getURLString() {
			if (gateButton.getSelection()) {
				return urlText.getText();
			} else {
				return "";
			}
		}

		/**
		 * This operation retrieves the gateway port
		 * 
		 * @return The String for the gateway port
		 */
		public String getPortString() {
			if (gateButton.getSelection()) {
				return gatePortText.getText();
			} else {
				return "";
			}
		}

		/**
		 * @see ICheckComposite#hasCheckBox()
		 */
		@Override
		public boolean hasCheckBox() {
			return true;
		}

		/**
		 * @see ICheckComposite#isSelected()
		 */
		@Override
		public boolean isSelected() {
			return gateButton.getSelection();
		}

	}

}
