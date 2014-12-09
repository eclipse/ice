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

public class GenerateYAMLWizardPage extends WizardPage {

	private Composite container;

	private Text hostname;

	private Text username;
	
	private Text password;
	
	protected GenerateYAMLWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		Label label1 = new Label(container, SWT.NONE);
		label1.setText("Put a value here.");

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
		
		username = new Text(container, SWT.BORDER | SWT.SINGLE);
		username.setText(System.getProperty("user.name"));
		username.setLayoutData(gd);
		
		password = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);
		password.setText("");
		password.setLayoutData(gd);
		
		// required to avoid an error in the system
		setControl(container);
		setPageComplete(false);

	}

	public String getHostName() {
		return hostname.getText();
	}
	
	public String getUsername() {
		return username.getText();
	}
	
	public String getPassword() {
		return password.getText();
	}

}
