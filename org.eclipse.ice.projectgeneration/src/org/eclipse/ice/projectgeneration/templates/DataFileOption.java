package org.eclipse.ice.projectgeneration.templates;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.pde.ui.templates.BaseOptionTemplateSection;
import org.eclipse.pde.ui.templates.TemplateOption;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DataFileOption extends TemplateOption {

	private Text locationPathField;
	private Button browseButton;
	private Label locationLabel;

	
	public DataFileOption(BaseOptionTemplateSection section, String name, String label) {
		super(section, name, label);
	}	

	
	@Override
	public void createControl(Composite parent, int span) {
		locationLabel = new Label(parent, SWT.NONE);
		locationLabel.setText("Default dataset to load into the form:");
		
		locationPathField = new Text(parent, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		data.horizontalSpan = 2;
		locationPathField.setLayoutData(data);
		
		browseButton = new Button(parent, SWT.PUSH);
		browseButton.setText("Browse");
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				handleLocationBrowseButtonPressed();
			}
		});
	}

	
	private void handleLocationBrowseButtonPressed() {
		String selectedFile = null;
		String dirName = getPathFromLocationField();
	}

	
	private String getPathFromLocationField() {
		URI fieldURI;
		try {
			fieldURI = new URI(locationPathField.getText());
		} catch (URISyntaxException e) {
			return locationPathField.getText();
		}
		String path = fieldURI.getPath();
		return path != null ? path : locationPathField.getText();
	}
	
	
	@Override
	public void setEnabled(boolean enabled) {
		locationLabel.setEnabled(enabled);
		locationPathField.setEnabled(enabled);
		browseButton.setEnabled(enabled);
	}
}
