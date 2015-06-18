/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex McCaskey (UT-Battelle, LLC.) - initial API and implementation and/or initial documentation
 *    
 *******************************************************************************/
package org.eclipse.ice.client.widgets.moose.actions;

import java.io.ByteArrayInputStream;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICContainer;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.core.model.IWorkingCopy;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.eclipse.jface.dialogs.IInputValidator;

/**
 * The NewKernelHandler subclasses AbstractHandler to provide an execute implementation 
 * that generates a new MOOSE Kernel subclass. It prompts the user for the name of the 
 * new class, then generates the class include and source stubs and places them in 
 * include/kernel and src/kernel, respectively. 
 * 
 * @author Alex McCaskey
 *
 */
public class NewKernelHandler extends AbstractHandler {

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Local Declarations
		StringBuffer sourceFileBuffer = new StringBuffer(), includeFileBuffer = new StringBuffer();
		String kernelName = "";
		IProject project = null;
		IFolder srcFolder = null, includeFolder = null, kernelIncludeFolder = null, kernelSrcFolder = null;
		IFile newIncludeFile = null, newSourceFile = null;
		TreeSelection selection = (TreeSelection) HandlerUtil
				.getCurrentSelection(event);
		Object segment = selection.getPaths()[0].getLastSegment();

		// Check that the segment is a project or a folder
		// Either way get a reference to the project
		if (segment instanceof IProject) {
			project = (IProject) segment;
		} else if (segment instanceof ICContainer) {
			ICContainer folder = (ICContainer) segment;
			project = folder.getCProject().getProject();
		} else {
			return null;
		}

		// Create and show an InputDialog to get the Kernel name to be created
		InputDialog dialog = new InputDialog(HandlerUtil
				.getActiveWorkbenchWindow(event).getShell(), "",
				"Enter the name of the new Kernel", "", new IInputValidator() {
					public String isValid(String newText) {
						return newText.equals("") ? "Please insert a valid class name"
								: null;
					}
				});
		
		// Open the dialog and get the kernel anme
		if (dialog.open() == Window.OK) {
			// User clicked OK; update the label with the input
			kernelName = dialog.getValue();
		}

		// Make sure it was valid
		if (kernelName == null) {
			return null;
		}

		// Create the Input file from that kernelName
		includeFileBuffer.append("#ifndef "+kernelName.toUpperCase()+"_H\n");
		includeFileBuffer.append("#define "+kernelName.toUpperCase()+"_H\n\n");
		includeFileBuffer.append("#include \"Kernel.h\"\n\n\n");
		includeFileBuffer.append("class " + kernelName + ";\n\n");
		includeFileBuffer.append("template<>\n");
		includeFileBuffer.append("InputParameters validParams<" + kernelName
				+ ">();\n\n");
		includeFileBuffer.append("\nclass " + kernelName
				+ " : public Kernel {\n\n");
		includeFileBuffer.append("public:\n");
		includeFileBuffer
				.append("\t"
						+ kernelName
						+ "(const std::string & name, InputParameters parameters);\n\n");
		includeFileBuffer.append("\tvirtual Real computeQpResidual();\n\n");
		includeFileBuffer.append("};\n\n");
		includeFileBuffer.append("#endif\n");

		// Create the Source file from that kernelName
		sourceFileBuffer.append("#include \"" + kernelName + ".h\"\n\n");
		sourceFileBuffer.append("template<>\n");
		sourceFileBuffer.append("InputParameters validParams<" + kernelName
				+ ">() {\n");
		sourceFileBuffer
				.append("\tInputParameters params = validParams<Kernel>();\n");
		sourceFileBuffer.append("\treturn params;\n");
		sourceFileBuffer.append("}\n\n");
		sourceFileBuffer
				.append(kernelName
						+ "::"
						+ kernelName
						+ "(const std::string & name, InputParameters parameters) : Kernel(name, parameters) {\n");
		sourceFileBuffer.append("}\n\n");
		sourceFileBuffer.append("Real " + kernelName
				+ "::computeQpResidual() {\n");
		sourceFileBuffer.append("\treturn 0.0;\n");
		sourceFileBuffer.append("}\n\n");

		// Get a reference to the src,include folders so that we can get 
		// a handle to the kernel folders
		srcFolder = project.getFolder("src");
		includeFolder = project.getFolder("include");
		kernelIncludeFolder = includeFolder.getFolder("kernel");
		kernelSrcFolder = srcFolder.getFolder("kernel");
		try {

			// Create it if it doesn't exist
			if (!kernelIncludeFolder.exists()) {
				kernelIncludeFolder.create(true, true, null);
			}

			// Create it if it doesn't exist
			if (!kernelSrcFolder.exists()) {
				kernelSrcFolder.create(true, true, null);
			}

			// Get a reference to the new Include file, create an empty file
			newIncludeFile = kernelIncludeFolder.getFile(kernelName + ".h");
			newIncludeFile.create(new ByteArrayInputStream(new byte[0]), true,
					null);
			
			// Use CDT stuff to write it 
			ITranslationUnit includeTranslationUnit = (ITranslationUnit) CoreModel
					.getDefault().create(newIncludeFile);
			IWorkingCopy workingIncCopy = includeTranslationUnit
					.getWorkingCopy();
			workingIncCopy.getBuffer()
					.setContents(includeFileBuffer.toString());
			workingIncCopy.reconcile(true, new NullProgressMonitor());
			workingIncCopy.commit(true, new NullProgressMonitor());
			workingIncCopy.destroy();

			// Get a reference to the new Source file, create an empty file
			newSourceFile = kernelSrcFolder.getFile(kernelName + ".C");
			newSourceFile.create(new ByteArrayInputStream(new byte[0]), true,
					null);
			
			// Use the CDT magic to create it
			ITranslationUnit sourceTranslationUnit = (ITranslationUnit) CoreModel
					.getDefault().create(newSourceFile);
			IWorkingCopy workingSrcCopy = sourceTranslationUnit
					.getWorkingCopy();
			workingSrcCopy.getBuffer().setContents(sourceFileBuffer.toString());
			workingSrcCopy.reconcile(true, new NullProgressMonitor());
			workingSrcCopy.commit(true, new NullProgressMonitor());
			workingSrcCopy.destroy();

			// Open them in C++ editors for the user to see. 
			IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage(), newSourceFile);
			IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage(), newIncludeFile);

		} catch (CoreException e) {
			e.printStackTrace();
		}

		return null;
	}

}
