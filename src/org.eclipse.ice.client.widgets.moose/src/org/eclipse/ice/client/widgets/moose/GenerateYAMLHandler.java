package org.eclipse.ice.client.widgets.moose;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.item.nuclear.MOOSELauncher;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 
 */
public class GenerateYAMLHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		Shell shell = window.getShell();
		
		MOOSELauncher launcher = new MOOSELauncher(ResourcesPlugin
				.getWorkspace().getRoot().getProject("default"));
		
		System.out.println(((DataComponent) launcher.getForm().getComponent(5))
				.retrieveEntry("Executable").getValue());
		
		GenerateYAMLWizard wizard = new GenerateYAMLWizard();
		WizardDialog dialog = new WizardDialog(shell, wizard);
		
		dialog.open();
		
		((DataComponent) launcher.getForm().getComponent(5)).retrieveEntry(
				"Executable").setValue("Generate YAML/action syntax");
		((TableComponent) launcher.getForm().getComponent(4)).getRow(0).get(0).setValue("megafluffy"); 
		((TableComponent) launcher.getForm().getComponent(4)).getRow(0).get(2).setValue("/home/moose"); 

		launcher.submitForm(launcher.getForm());
		
		FormStatus status = launcher.process("Launch the Job");

		if (status.equals(FormStatus.NeedsInfo)) {
			Form actionForm = launcher.getForm();
			//((DataComponent) actionForm.getComponent(1)).retrieveEntry("Username").setValue("aqw");
			//((DataComponent) actionForm.getComponent(1)).retrieveEntry("Password").setValue("u5ta6chf");
			
			launcher.submitForm(actionForm);
		}
		return null;
	}

}
