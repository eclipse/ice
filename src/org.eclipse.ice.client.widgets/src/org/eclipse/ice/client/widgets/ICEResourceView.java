/*******************************************************************************
 * Copyright (c) 2013, 2014- UT-Battelle, LLC.
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.client.common.PropertySource;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import ca.odell.glazedlists.swt.DefaultEventTableViewer;

/**
 * This class is a ViewPart that creates a tree of text files and a tree of
 * image files collected as ICEResourceComponents.
 * 
 * @authors Jay Jay Billings, Taylor Patterson, Jordan Deyton, Anna Wojtowicz
 */
public class ICEResourceView extends PlayableViewPart implements
		IUpdateableListener, IPartListener2, IDoubleClickListener {

	public static final String ID = "org.eclipse.ice.client.widgets.ICEResourceView";

	// ---- Current Editor and Resources ---- //
	/**
	 * The currently active ICEFormEditor. This should only be set by the
	 * IPartListener2 methods.
	 */
	private ICEFormEditor editor;

	/**
	 * The ResourceComponent managed by this view. This changes based on the
	 * currently active ICEFormEditor.
	 * <p>
	 * When the ResourceComponent is set or updated, the contents of the view
	 * should also update accordingly.
	 * </p>
	 */
	private ResourceComponent resourceComponent;
	
	/**
	 * The ICEResourcePage managed by this view. This changes based on the 
	 * currently active ICEFormEditor. This page should also refer to the same 
	 * ResourceComponent used by this view.
	 */
	private ICEResourcePage resourcePage;
	
	// -------------------------------------- //

	/**
	 * The TreeViewer for the ResourceComponent.
	 */
	private TreeViewer resourceTreeViewer;

	/**
	 * The TabFolder for managing the tabs containing the TreeViewer.
	 */
	private TabFolder tabFolder;

	/**
	 * Data structure for text-based resources.
	 */
	private final List<ResourcePropertySource> textList;

	/**
	 * Data structure for image-based resources.
	 */
	private final List<ResourcePropertySource> imageList;

	/**
	 * Mapping of children in the resource tree to their parent resource.
	 */
	private final Map<String, ICEResource> resourceChildMap;

	/**
	 * The previous button in the tool bar.
	 */
	private PreviousAction prevAction;

	/**
	 * The play button in the tool bar.
	 */
	private PlayAction playAction;

	/**
	 * The next button in the tool bar.</p>
	 */
	private NextAction nextAction;

	/**
	 * The list of VizResources that should be displayed as plots
	 */
	private final ListComponent<VizResource> plotList;

	/**
	 * The default constructor.
	 */
	public ICEResourceView() {

		// Initialize the lists of property sources.
		textList = new ArrayList<ResourcePropertySource>();
		imageList = new ArrayList<ResourcePropertySource>();

		// Initialize the map of TreeView element names to their ICEResources.
		resourceChildMap = new Hashtable<String, ICEResource>();

		// Initialize the list of VizResources.
		plotList = new ListComponent<VizResource>();

		return;
	}

	/**
	 * Sets the currently active {@link #editor}. This triggers any necessary
	 * updates to the UI asynchronously.
	 * <p>
	 * <b>Note:</b> This method should only be called when the active editor
	 * changes, e.g., via the {@link IPartListener2} implementation in this
	 * class.
	 * </p>
	 * 
	 * @param activeEditor
	 *            The new editor, or null to unset it. This is assumed to be a
	 *            new value.
	 */
	private void setActiveEditor(ICEFormEditor activeEditor) {

		// If necessary, clear the contents of the view.
		if (editor != null) {
			// Clear the ResourceComponent and related UI pieces.
			setResourceComponent(null);
			// Unset the reference to the ICEResourcePage.
			resourcePage = null;
			// Unset the reference to the active editor.
			editor = null;
		}

		// If the new active editor is valid, update the contents of the view.
		if (activeEditor != null) {

			// ---- Determine the ResourceComponent from the editor ---- //
			// Create a visitor to used to find a ResourceComponent.
			final AtomicReference<ResourceComponent> componentRef;
			componentRef = new AtomicReference<ResourceComponent>();
			IComponentVisitor visitor = new SelectiveComponentVisitor() {
				@Override
				public void visit(ResourceComponent component) {
					componentRef.set(component);
				}
			};

			// Loop over the Form Components to find a ResourceComponent.
			Form activeForm = ((ICEFormInput) activeEditor.getEditorInput())
					.getForm();
			for (Component i : activeForm.getComponents()) {
				i.accept(visitor);
				// Exit the loop when the first ResourceComponent is found.
				if (componentRef.get() != null) {
					break;
				}
			}
			// --------------------------------------------------------- //

			// If a ResourceComponent was found, update the known
			// ResourceComponent.
			if (componentRef.get() != null) {
				setResourceComponent(componentRef.get());
			}

			// Set the reference to the new active editor and its resource page.
			editor = activeEditor;
			resourcePage = editor.getResourcePage();
		}

		return;
	}

	/**
	 * Sets the current {@link #resourceComponent}. This triggers any updates to
	 * the UI asynchronously.
	 * <p>
	 * <b>Note:</b> This method should only be called when the active
	 * {@link #editor} changes, i.e., via
	 * {@link #setActiveEditor(ICEFormEditor)}.
	 * </p>
	 * 
	 * @param component
	 *            The new ResourceComponent, or null to unset it. This is
	 *            assumed to be a new value.
	 */
	private void setResourceComponent(ResourceComponent component) {

		// If necessary, clear the contents of the view and unregister from the
		// previous ResourceComponent.
		if (resourceComponent != null) {
			// Unregister from the old ResourceComponent.
			resourceComponent.unregister(this);

			// Clear the related UI pieces.
			if (resourceTreeViewer != null) {
				textList.clear();
				imageList.clear();
				resourceTreeViewer.refresh();
				resourceTreeViewer.getTree().redraw();
			}

			// Unset the reference to the old ResourceComponent.
			resourceComponent = null;
		}

		// If the new ResourceComponent is valid, update the contents of the
		// view and register for updates from it.
		if (component != null) {
			// Set the component reference
			resourceComponent = component;
			// Register this view with the Component to receive updates
			resourceComponent.register(this);
			// Trigger a UI update.
			update(resourceComponent);
		}

		return;
	}

	/**
	 * This operation retrieves the ResourceComponent that has been rendered by
	 * the ICEResourceView or null if the component does not exist.
	 * 
	 * @return The ResourceComponent or null if the component was not previously
	 *         set.
	 */
	public ResourceComponent getResourceComponent() {
		return resourceComponent;
	}

	/**
	 * This operation overrides the ViewPart.createPartControl method to create
	 * and draw the TreeViewer before registering it as a selection provider.
	 * 
	 * @param parent
	 *            The Composite used to create the TreeViewer.
	 */
	@Override
	public void createPartControl(Composite parent) {

		// Create a TabFolder to manage tabs
		tabFolder = new TabFolder(parent, SWT.NONE);

		// Create pages (TabItems) for text files and images
		TabItem textTab = new TabItem(tabFolder, SWT.NONE, 0);
		textTab.setText("Files");
		TabItem imageTab = new TabItem(tabFolder, SWT.NONE, 1);
		imageTab.setText("Images");
		TabItem plotTab = new TabItem(tabFolder, SWT.NONE, 2);
		plotTab.setText("Plots");

		// Create the tool bar and buttons for the view
		createActions();

		// Initialize the TreeViewer
		resourceTreeViewer = new TreeViewer(tabFolder);
		// Create content and label providers
		initializeTreeViewer(resourceTreeViewer);
		// Register the tree to the tabs
		textTab.setControl(resourceTreeViewer.getControl());
		imageTab.setControl(resourceTreeViewer.getControl());
		// Register this view as a SelectionProvider
		getSite().setSelectionProvider(resourceTreeViewer);
		// Registered the view as a double click listener of the TreeViewer
		resourceTreeViewer.addDoubleClickListener(this);

		// Add a listener to catch tab selection changes.
		// NOTE: In Windows, this event is fired instantly, so this listener
		// needs to be declared after everything else is initialized!
		tabFolder.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// If tabs are changed while playing, stop playing.
				if (playAction.isInPlayState()) {
					playAction.stop();
				}
				// Set the TreeViewer input to the selected tab
				setTreeContent(tabFolder.indexOf((TabItem) event.item));
			}
		});

		// Create the Table and table viewer for the Plot tab
		Table listTable = new Table(tabFolder, SWT.FLAT);
		DefaultEventTableViewer<VizResource> listTableViewer = new DefaultEventTableViewer<VizResource>(
				plotList, listTable, plotList);
		// Register the table control with the plot tab
		plotTab.setControl(listTable);

		// Check if there is currently an active ICEFormEditor. If so, update
		// the currently active editor and related UI pieces.
		IEditorPart activeEditor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (activeEditor != null && activeEditor instanceof ICEFormEditor) {
			if (activeEditor != editor) {
				setActiveEditor((ICEFormEditor) activeEditor);
			}
		} else {
			// Get a list of all the currently open editors
			IWorkbenchPage workbenchPage = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			IEditorReference[] editorRefs = workbenchPage.getEditorReferences();

			if (editorRefs != null && editorRefs.length > 0) {
				// Begin iterating through all the editors, looking for one
				// that's an ICEFormEditor
				for (IEditorReference e : editorRefs) {
					// If it's an ICEFormEditor, set it as the active editor
					if (e.getId().equals(ICEFormEditor.ID)) {
						setActiveEditor((ICEFormEditor) e);
						break;
					}
				}
			}
		}

		// Register as a listener to the part service so that the view can
		// update when the active ICEFormEditor changes.
		IPartService partService = getSite().getWorkbenchWindow()
				.getPartService();
		partService.addPartListener(this);

		return;
	}

	/**
	 * This operation creates content and label providers for a TreeViewer.
	 * 
	 * @param inputTreeViewer
	 *            The TreeViewer to have the providers added to.
	 */
	private void initializeTreeViewer(TreeViewer inputTreeViewer) {

		// Create a content provider that will show the name and its path and
		// edit date as children
		inputTreeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// Don't handle input changes
				return;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				// Cast the input and return it as an array
				return ((List<?>) inputElement).toArray();
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				// If the element is a PropertySource
				if (parentElement instanceof PropertySource) {
					PropertySource source = (PropertySource) parentElement;
					ICEResource resource = (ICEResource) source
							.getWrappedData();
					// Load the path and modification date as children
					String[] children = {
							"Path: " + resource.getPath().toASCIIString(),
							"Date: " + resource.getLastModificationDate() };

					// Map the children to their parent resource for appropriate
					// selection behavior
					resourceChildMap.put("Path: "
							+ resource.getPath().toASCIIString(), resource);
					resourceChildMap.put(
							"Date: " + resource.getLastModificationDate(),
							resource);

					return children;
				}
				return null;
			}

			@Override
			public Object getParent(Object element) {
				// Don't identify parents
				return null;
			}

			@Override
			public boolean hasChildren(Object element) {
				// Only PropertySources will have children
				if (element instanceof PropertySource) {
					return true;
				}
				return false;
			}

		});
		// Add a label provider to properly label the resources
		inputTreeViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				// Only set a label if it is an ICEResource. Otherwise we
				// shouldn't need it.... I think.
				if (element instanceof PropertySource) {
					PropertySource source = (PropertySource) element;
					ICEResource resource = (ICEResource) source
							.getWrappedData();
					return resource.getName();
				}
				return (String) element;
			}

			@Override
			public Image getImage(Object element) {
				// If it is a resource, set a "folder" picture
				if (element instanceof PropertySource) {
					return PlatformUI.getWorkbench().getSharedImages()
							.getImage(ISharedImages.IMG_OBJ_FOLDER);
				}
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_FILE);
			}

		});

		return;
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		
		// Get the associated resource
		ISelection selection = event.getSelection();
		ICEResource selectedResource = getResourceFromSelection(selection);
	
		// If it's valid, try to display it on the ResourcePage
		if (selectedResource != null) {
			try {
				resourcePage.showResource(selectedResource);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		
		return;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// Do nothing
	}

	/**
	 * Update resourceTreeViewer when a new resource becomes available.
	 * 
	 * @see IUpdateableListener#update(IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {

		// Only perform a UI update if the component is valid and the UI pieces
		// exist.
		if (component != null && component == resourceComponent
				&& resourceTreeViewer != null) {

			// Sync with the display
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					// Just do a blanket update - no need to check the component
					if (resourceTreeViewer != null) {
						System.out.println("ICEResourceView Message: "
								+ "Updating resource table.");
						sortTreeContent();
						setTreeContent();
						resourceTreeViewer.refresh();
						resourceTreeViewer.getTree().redraw();
					}
				}
			});
		}

		return;
	}

	/**
	 * This operation populates the ArrayLists for text- and image-based
	 * resources.
	 */
	private void sortTreeContent() {

		// Ensure a non-null resourceComponent
		if (resourceComponent != null) {
			// Remove the current list contents
			textList.clear();
			imageList.clear();

			// Re-populate the resource lists
			for (ICEResource i : resourceComponent.getResources()) {
				if (!i.isPictureType()) {
					textList.add(new ResourcePropertySource(i));
				} else if (i.isPictureType()) {
					imageList.add(new ResourcePropertySource(i));
				} else {
					System.out.println("ERROR: Unknown resource type.");
				}
			}
		}

		return;
	}

	/**
	 * This operation sets the input of the resourceTreeViewer.
	 */
	private void setTreeContent() {

		// If currently playing, stop.
		if (playAction.isInPlayState()) {
			playAction.stop();
		}
		// If there are no files, but there are images, set to the images tab.
		// Otherwise, default to the files tab.
		if (textList.isEmpty() && !imageList.isEmpty()) {
			resourceTreeViewer.setInput(imageList);
			tabFolder.setSelection(1);
			playable = true;

			// Select the first available image resource.
			resourceTreeViewer.setSelection(
					new StructuredSelection(imageList.get(0)), true);
		} else {
			resourceTreeViewer.setInput(textList);
			tabFolder.setSelection(0);
			playable = false;

			// Select the first available text resource.
			if (!textList.isEmpty()) {
				resourceTreeViewer.setSelection(new StructuredSelection(
						textList.get(0)), true);
			}
		}

		return;
	}

	/**
	 * This operation sets the input of the resourceTreeViewer when a tab
	 * selection change occurs.
	 * 
	 * @param tabIndex
	 *            The currently selected tab.
	 */
	private void setTreeContent(int tabIndex) {

		// If currently playing, stop.
		if (playAction.isInPlayState()) {
			playAction.stop();
		}
		if (tabIndex == 1) {
			resourceTreeViewer.setInput(imageList);
			if (!imageList.isEmpty()) {
				playable = true;
			}
		} else {
			resourceTreeViewer.setInput(textList);
			playable = false;
		}
		return;
	}

	// ---- Implements IPartListener2 ---- //
	// This class implements IPartListener2 in order to synchronize with the
	// currently active ICEFormEditor. When an ICEFormEditor is activated, this
	// view looks for the editor's ResourceComponents and lists all of their
	// ICEResources in the TreeViewer. When the currently activated
	// ICEFormEditor is closed, then the view should clear.

	/**
	 * This function is called whenever a Workbench part gains focus. Here, we
	 * are only interested if the part is an ICEFormEditor. When a new
	 * ICEFormEditor is activated, sync the view with the activated editor.
	 */
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {

		// If the activated editor is an ICEFormEditor different from the known
		// active ICEFormEditor, call the method to update the currently active
		// editor and affected UI pieces.
		IWorkbenchPart part = partRef.getPart(false);
		if (part != null && part instanceof ICEFormEditor) {
			ICEFormEditor activeEditor = (ICEFormEditor) part;
			if (activeEditor != editor) {
				setActiveEditor(activeEditor);
			}
		}

		return;
	}

	/**
	 * This function is called whenever a Workbench part is closed. If the
	 * current {@link #editor} is closed, then we need to clear the view's
	 * contents.
	 */
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {

		// If the closed editor is the known active ICEFormEditor, call the
		// method to clear the currently active editor and related UI pieces.
		IWorkbenchPart part = partRef.getPart(false);
		if (part != null && part instanceof ICEFormEditor) {
			ICEFormEditor activeEditor = (ICEFormEditor) partRef.getPart(false);
			if (activeEditor == editor) {
				setActiveEditor(null);
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partHidden(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partBroughtToTop(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partDeactivated(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partOpened(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partVisible(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partInputChanged(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// Do nothing.
	}

	// ----------------------------------- //

	/**
	 * Attempts to determine the {@link ICEResource} from the selection. The
	 * selection is assumed to be from this view, and so its selection structure
	 * applies.
	 * 
	 * @param selection
	 *            The selection to convert.
	 * @return The Resource from the selection, or null if the selection was
	 *         invalid.
	 */
	public ICEResource getResourceFromSelection(ISelection selection) {
		ICEResource selectedResource = null;
		if (selection != null && !selection.isEmpty()
				&& selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			// Strings must be looked up in the Resource View's map.
			if (element instanceof String) {
				selectedResource = resourceChildMap.get(element);
			}
			// PropertySources should wrap ICEResources.
			else if (element instanceof PropertySource) {
				PropertySource source = (PropertySource) element;
				selectedResource = (ICEResource) source.getWrappedData();
			}
		}
		return selectedResource;
	}

	/**
	 * Create and add the play, previous, and next buttons to the tool bar for
	 * this view.
	 */
	private void createActions() {

		// Get the IToolBarManager
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolBarManager = actionBars.getToolBarManager();

		// Create a previous button and add it to the tool bar
		prevAction = new PreviousAction(this);
		toolBarManager.add(prevAction);

		// Create a play button and add it to the tool bar
		playAction = new PlayAction(this);
		toolBarManager.add(playAction);

		// Create a next button and add it to the tool bar
		nextAction = new NextAction(this);
		toolBarManager.add(nextAction);

		return;
	}

	/**
	 * Set the resourceTreeViewer selection to the next item (file or image) in
	 * the currently displayed resource list. If current selection is the last
	 * item in the list, cycle to the front of the list.
	 * 
	 * @see PlayableViewPart#setToNextResource()
	 */
	public void setToNextResource() {

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// Get the currently selected resource in the view. (Or the
				// first selected resource if multiple resources are
				// selected even though this has no effect.)
				TreeItem[] currSelection = resourceTreeViewer.getTree()
						.getSelection();
				int currIndex = resourceTreeViewer.getTree().indexOf(
						currSelection[0]);

				// Set the selection to the next resource in the currently
				// displayed list or the first resource if the last resource
				// is currently selected.
				if (tabFolder.getSelectionIndex() == 1 && !imageList.isEmpty()) {
					int nextIndex = (currIndex + 1) % imageList.size();
					resourceTreeViewer.setSelection(new StructuredSelection(
							imageList.get(nextIndex)), true);
				} else if (!textList.isEmpty()) {
					int nextIndex = (currIndex + 1) % textList.size();
					resourceTreeViewer.setSelection(new StructuredSelection(
							textList.get(nextIndex)), true);
				}
			}
		});

		return;
	}

	/**
	 * Set the resourceTreeViewer selection to the previous item (file or image)
	 * in the currently displayed resource list. If current selection is the
	 * first item in the list, cycle to the back of the list.
	 * 
	 * @see PlayableViewPart#setToPreviousResource()
	 */
	public void setToPreviousResource() {

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// Get the currently selected resource in the view. (Or the
				// first selected resource if multiple resources are
				// selected even though this has no effect.)
				TreeItem[] currSelection = resourceTreeViewer.getTree()
						.getSelection();
				int currIndex = resourceTreeViewer.getTree().indexOf(
						currSelection[0]);

				// Set the selection to the previous resource in the
				// currently displayed list, or the last resource if the first
				// resource is currently selected.
				if (tabFolder.getSelectionIndex() == 1 && !imageList.isEmpty()) {
					int prevIndex = (currIndex - 1) % imageList.size();
					if (prevIndex < 0) {
						prevIndex = imageList.size() - 1;
					}
					resourceTreeViewer.setSelection(new StructuredSelection(
							imageList.get(prevIndex)), true);
				} else if (!textList.isEmpty()) {
					int prevIndex = (currIndex - 1) % textList.size();
					if (prevIndex < 0) {
						prevIndex = textList.size() - 1;
					}
					resourceTreeViewer.setSelection(new StructuredSelection(
							textList.get(prevIndex)), true);
				}
			}
		});

		return;
	}

	/**
	 * @see PlayableViewPart#removeSelection()
	 */
	public void removeSelection() {
		// Not used at this time
		return;
	}

	/**
	 * A private subclass of PropertySource to implement the superclass features
	 * used by ICEResourceView.
	 */
	private static class ResourcePropertySource extends PropertySource {

		// Property IDs
		public static final String ID_PATH = "Path";
		public static final String ID_DATE = "Date";

		// IPropertyDescriptors for the class
		protected static IPropertyDescriptor[] descriptors;
		static {
			descriptors = new IPropertyDescriptor[] {
					new PropertyDescriptor(ID_PATH, "Path"),
					new PropertyDescriptor(ID_DATE, "Date") };
		}

		/**
		 * The constructor
		 * 
		 * @param obj
		 *            The object to be wrapped by PropertySource. For this
		 *            subclass, this will be an ICEResource.
		 */
		public ResourcePropertySource(Object obj) {

			// Just call the superclass constructor
			super(obj);

			return;
		}

		/**
		 * This function returns the array of descriptors for properties.
		 * 
		 * @return The array of descriptors for properties.
		 * 
		 * @see IPropertySource#getPropertyDescriptors()
		 */
		@Override
		public IPropertyDescriptor[] getPropertyDescriptors() {
			return descriptors;
		}

		/**
		 * This function returns the value for a give property.
		 * 
		 * @param id
		 *            The object used to identify this property.
		 * @return The value for the input property
		 * 
		 * @see IPropertySource#getPropertyValue(Object)
		 */
		@Override
		public Object getPropertyValue(Object id) {

			// If the caller seeks the path property, get it from the wrapped
			// resource and return it.
			if (ID_PATH.equals(id)) {
				ICEResource resource = (ICEResource) this.getWrappedData();
				return resource.getPath().toASCIIString();
			}
			// If the caller seeks the date property, get it from the wrapped
			// resource and return it.
			else if (ID_DATE.equals(id)) {
				ICEResource resource = (ICEResource) this.getWrappedData();
				return resource.getLastModificationDate();
			}
			// Otherwise, the property is unknown.
			else {
				return super.getPropertyValue(id);
			}
		}
	}
}