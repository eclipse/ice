package org.eclipse.ice.projectgeneration.templates;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.pde.ui.templates.BaseOptionTemplateSection;
import org.eclipse.pde.ui.templates.StringOption;
import org.eclipse.pde.ui.templates.TemplateOption;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DataFileOption extends StringOption {

	private Text locationPathField;
	private Button browseButton;
	private Label locationLabel;
	private FileDialog fileChooser;
	private String chosenFile;
	private String description;
	
	/**
	 * Constructor
	 * 
	 * @param section
	 * @param name
	 * @param label
	 */
	public DataFileOption(BaseOptionTemplateSection section, String name, String label) {
		super(section, name, label);
		description = label;
	}	


	/**
	 * Create the user interface for the option
	 */
	public void createControl(Composite parent, int span) {
		locationLabel = new Label(parent, SWT.NONE);
		locationLabel.setText(description);
		
		locationPathField = new Text(parent, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		data.horizontalSpan = 2;
		locationPathField.setLayoutData(data);
		
		browseButton = new Button(parent, SWT.PUSH);
		browseButton.setText("Browse");
		browseButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				fileChooser = new FileDialog(parent.getShell());
				fileChooser.setText("Select File");
				fileChooser.setFilterExtensions(new String[] {"*.*"});
				chosenFile = fileChooser.open();
				locationPathField.setText(chosenFile);
				setValue(chosenFile);
			}

			@Override public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}

	
	/**
	 * Get the path from the location path field
	 * 
	 * @return the path
	 */
	public String getDataFilePath() {
		URI fieldURI;
		try {
			fieldURI = new URI(chosenFile);
		} catch (URISyntaxException e) {
			return chosenFile;
		}
		String path = fieldURI.getPath();
		return path != null ? path : chosenFile;
	}
	

	@Override
	public boolean isEmpty() {
		return getValue() == null;
	}
	
	/**
	 * Set whether the option is usable
	 */
	@Override
	public void setEnabled(boolean enabled) {
		locationLabel.setEnabled(enabled);
		locationPathField.setEnabled(enabled);
		browseButton.setEnabled(enabled);
	}
}
