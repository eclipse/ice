/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * <p>
 * This class is a FormPage that creates a page with an embedded Form for
 * manipulating an EMFComponent.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class EMFSectionPage extends ICEFormPage implements ISelectionListener {

	/// The ID
	public static final String ID = "org.eclipse.ice.client.widgets.ICEEMFPage";

	/**
	 * <p>
	 * Reference to the EMFComponent being visualized.
	 * </p>
	 * 
	 */
	private EMFComponent emfComponent;

	/**
	 * <p>
	 * Reference to the TreeViewer that shows the overall EMF Ecore model tree
	 * structure.
	 * </p>
	 * 
	 */
	private EMFTreeCompositeViewer emfTreeView;

	/**
	 * <p>
	 * Reference to the parent Composite that all TreeComposite DataComponent
	 * nodes will be showed on.
	 * </p>
	 * 
	 */
	private Composite parent;

	/**
	 * <p>
	 * Reference to the DataComponentComposite used for displaying the
	 * DataComponents from the EMFTreeComposite.
	 * </p>
	 * 
	 */
	private DataComponentComposite dataComposite;

	/**
	 * The console view in Eclipse.
	 */
	private IConsoleView consoleView = null;

	/**
	 * The console that will display text from this widget.
	 */
	private MessageConsole console = null;

	/**
	 * The message stream for the message console to which text should be
	 * streamed.
	 */
	private MessageConsoleStream msgStream = null;

	/**
	 * <p>
	 * The constructor.
	 * </p>
	 * 
	 * @param editor
	 *            <p>
	 *            The FormEditor for which the Page should be constructed.
	 *            </p>
	 * @param id
	 *            <p>
	 *            The id of the page.
	 *            </p>
	 * @param title
	 *            <p>
	 *            The title of the page.
	 *            </p>
	 */
	public EMFSectionPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		parent = null;
		this.editor = (ICEFormEditor) editor;
	}

	/**
	 * <p>
	 * Set the EMFComponent that this EMFSectionPage should display.
	 * </p>
	 * 
	 */
	public void setEMFComponent(EMFComponent emf) {
		emfComponent = emf;
	}

	/**
	 * <p>
	 * This operation overrides the default/abstract implementation of
	 * FormPage.createFormContents to create the contents of the EMFSectionPage.
	 * </p>
	 * 
	 * @param managedForm
	 *            <p>
	 *            The Form widget on which the ICEMasterDetailsPage exists.
	 *            </p>
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {

		// Get the Scrolled Form and set its GridLayout
		final ScrolledForm scrolledForm = managedForm.getForm();
		scrolledForm.getBody().setLayout(new GridLayout());
		scrolledForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Get the toolkit and create a parent
		// composite for the EMF Form
		FormToolkit toolkit = managedForm.getToolkit();
		parent = toolkit.createComposite(scrolledForm.getBody(), SWT.NONE);
		parent.setLayout(new ColumnLayout());
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

//		dataComposite = new DataComponentComposite(null, parent, SWT.FLAT);
//
//		// Create a listener that will mark the form as dirty when the event is
//		// received.
//		Listener listener = new Listener() {
//			@Override
//			public void handleEvent(Event e) {
//				// logger.info("Changed!");
//				// Change the editor state
//				editor.setDirty(true);
//			}
//		};
//
//		// Register the Listener
//		dataComposite.addListener(SWT.Selection, listener);
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.marginLeft = 5;
//		gridLayout.marginTop = 5;
//		gridLayout.marginRight = 5;
//		gridLayout.marginBottom = 5;
//		gridLayout.verticalSpacing = 0;
//		dataComposite.setLayout(gridLayout);

		// Make sure we show the EMF Tree Viewer when we create
		// this EMF ICEFormPage.
		try {
			getSite().getWorkbenchWindow().getActivePage()
					.showView(EMFTreeCompositeViewer.ID);
		} catch (PartInitException e1) {
			e1.printStackTrace();
		}

		// Get a reference to the EMFTreeView ViewPart from the Workbench
		// And give it the EMFComponent reference
		emfTreeView = (EMFTreeCompositeViewer) getSite().getWorkbenchWindow()
				.getActivePage().findView(EMFTreeCompositeViewer.ID);
		emfTreeView.setInput(emfComponent.getEMFTreeComposite(),
				(ICEFormEditor) getEditor());

		// Register this guy as a listener of clicks on the
		// EMFTreeView ViewPart
		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(EMFTreeCompositeViewer.ID, this);

		// Get the currently active page
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			// Load the console view
			consoleView = (IConsoleView) page
					.showView(IConsoleConstants.ID_CONSOLE_VIEW);
			// Create the console instance that will be used to display
			// text from this widget.
			console = new MessageConsole("CLI", null);
			// Add the console to the console manager
			ConsolePlugin.getDefault().getConsoleManager()
					.addConsoles(new IConsole[] { console });
			// Show the console in the view
			consoleView.display(console);
			console.activate();
			// Get an output stream for the console
			msgStream = console.newMessageStream();
			msgStream.setActivateOnWrite(true);
			msgStream.println("Streaming output console activated.");
		} catch (PartInitException e) {
			// Complain
			logger.info("EclipseStreamingTextWidget Message: Unable to "
					+ "stream text!");
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * This operation retrieves the EMFComponent that is currently managed by
	 * the EMFSectionPage.
	 * </p>
	 * 
	 * @return <p>
	 *         The EMFComponent or null if the component has not yet been set in
	 *         the page.
	 *         </p>
	 */
	public EMFComponent getEMFComponent() {
		return emfComponent;
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

//		// Only get the selection if the part is the EMFTreeView.
//		if (part.getSite().getId().equals(EMFTreeCompositeViewer.ID)) {
//
//			// Get the number of selections highlighted
//			int count = emfTreeView.getTreeViewer().getTree()
//					.getSelectionCount();
//
//			// Only work if we've got a selection
//			if (count > 0) {
//				// Get the highlighted child String
//				Object data = emfTreeView.getTreeViewer().getTree()
//						.getSelection()[0].getData();
//
//				if (data instanceof TreeComposite) {
//
//					// Get the DataComponent from this TreeComposite
//					DataComponent dataComp = emfComponent
//							.getDataFromTreeNode((TreeComposite) data);
//
//					// Make sure its not null, the DataComponentComposite is not null, 
//					// and that we don't have the same DataComponent.
//					if (dataComp != null
//							&& dataComposite != null
//							&& !dataComp.equals(dataComposite
//									.getDataComponent())) {
//						dataComposite.dispose();
//						dataComposite = new DataComponentComposite(dataComp,
//								parent, SWT.FLAT);
//						// Create a listener that will mark the form as
//						// dirty
//						// when the event is
//						// received.
//						Listener listener = new Listener() {
//							@Override
//							public void handleEvent(Event e) {
//								// logger.info("Changed!");
//								// Change the editor state
//								editor.setDirty(true);
//							}
//						};
//
//						// Register the Listener
//						dataComposite.addListener(SWT.Selection, listener);
//
//						GridLayout gridLayout = new GridLayout();
//						gridLayout.marginLeft = 5;
//						gridLayout.marginTop = 5;
//						gridLayout.marginRight = 5;
//						gridLayout.marginBottom = 5;
//						gridLayout.verticalSpacing = 0;
//						dataComposite.setLayout(gridLayout);
//						parent.layout();
//					}
//
//				}
//			}
//		}

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Must sync with the display thread
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				msgStream.println(emfComponent.saveToString() + "\n");
			}
		});

		super.doSave(monitor);
	}

	@Override
	public void dispose() {
		// UNREGISTER this guy as a listener of clicks on the
		// EMFTreeView ViewPart
		getSite().getWorkbenchWindow().getSelectionService()
				.removeSelectionListener(EMFTreeCompositeViewer.ID, this);
		//dataComposite.dispose();
		console.clearConsole();
		super.dispose();
	}

}
