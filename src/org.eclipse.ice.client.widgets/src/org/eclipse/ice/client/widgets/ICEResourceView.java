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
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import ca.odell.glazedlists.swt.DefaultEventTableViewer;

/**
 * This class is a ViewPart that creates a tree of text files and a tree of
 * image files collected as ICEResourceComponents.
 * 
 * @authors Jay Jay Billings, Taylor Patterson, Jordan Deyton
 */
public class ICEResourceView extends PlayableViewPart implements
		IUpdateableListener, IPartListener2 {

	public static final String ID = "org.eclipse.ice.client.widgets.ICEResourceView";

	/**
	 * The ResourceComponent managed by this view.
	 */
	private ResourceComponent resourceComponent;

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
	private ArrayList<NRVPropertySource> textList = new ArrayList<NRVPropertySource>();

	/**
	 * Data structure for image-based resources.
	 */
	private ArrayList<NRVPropertySource> imageList = new ArrayList<NRVPropertySource>();

	/**
	 * The ID of the most recently active item.
	 */
	private int lastFormItemID;

	/**
	 * Mapping of children in the resource tree to their parent resource.
	 */
	public Hashtable<String, ICEResource> resourceChildMap = new Hashtable<String, ICEResource>();

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
	private ListComponent<VizResource> plotList = new ListComponent<VizResource>();

	/**
	 * The Constructor
	 */
	public ICEResourceView() {

		// Call the super constructor
		super();

		return;
	}

	/**
	 * This operation sets the ResourceComponent that should be used by the
	 * ICEResourceView. It also registers the ICEResourceView with the
	 * ResourceComponent so that it can be notified of state changes through the
	 * IUpdateableListener interface.
	 * 
	 * @param component
	 *            The ResourceComponent
	 */
	public void setResourceComponent(ResourceComponent component) {

		// Make sure the ResourceComponent exists
		if (component != null) {
			// Set the component reference
			resourceComponent = component;
			// Update the input - it should not be possible to call this without
			// having resourceTreeViewer properly instantiated, so just update
			// the input.
			sortTreeContent();
			setTreeContent();
			// Register this view with the Component to receive updates
			component.register(this);
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
		// Sort the resources into structures of text and images
		sortTreeContent();
		// Display the appropriate list for the selected tab
		setTreeContent();
		// Register the tree to the tabs
		textTab.setControl(resourceTreeViewer.getControl());
		imageTab.setControl(resourceTreeViewer.getControl());
		// Register this view as a SelectionProvider
		getSite().setSelectionProvider(resourceTreeViewer);

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
		DefaultEventTableViewer listTableViewer = new DefaultEventTableViewer(
				plotList, listTable, plotList);
		// Register the table control with the plot tab
		plotTab.setControl(listTable);

		// Add this view as a part listener.
		getSite().getWorkbenchWindow().getPartService().addPartListener(this);

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
				ArrayList<PropertySource> contents;
				contents = (ArrayList<PropertySource>) inputElement;

				return contents.toArray();
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

	/**
	 * @see WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// Do nothing
		return;
	}

	/**
	 * Update resourceTreeViewer when a new resource becomes available.
	 * 
	 * @see IUpdateableListener#update(IUpdateable)
	 */
	@Override
	public void update(final IUpdateable component) {

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
					textList.add(new NRVPropertySource(i));
				} else if (i.isPictureType()) {
					imageList.add(new NRVPropertySource(i));
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
		} else {
			resourceTreeViewer.setInput(textList);
			tabFolder.setSelection(0);
			playable = false;
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

	/**
	 * This function is called whenever a Workbench part gains focus. Here, we
	 * are only interested if the part is an ICEFormEditor. Get the Form from
	 * the editor and accept its components. Call setDefaultResourceSelection().
	 * 
	 * @see IPartListener2#partActivated(IWorkbenchPartReference)
	 */
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {

		System.out.println("ICEResourceView Message: Called partActivated("
				+ partRef.getId() + ")");

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			// Get the activated editor
			ICEFormEditor activeEditor = (ICEFormEditor) partRef.getPart(false);
			// Pull the form from the editor
			Form activeForm = ((ICEFormInput) activeEditor.getEditorInput())
					.getForm();
			// Record the ID of this form
			lastFormItemID = activeForm.getItemID();

			// Set the resourceComponent to the input component and update the
			// resourceTreeViewer.
			IComponentVisitor visitor = new SelectiveComponentVisitor() {
				@Override
				public void visit(ResourceComponent component) {
					System.out.println("ICEResourceView Message: Called visit("
							+ "ResourceComponent) with component ID = "
							+ component.getId());

					// Set the resource component
					setResourceComponent(component);

					// Update the TreeViewer
					update(component);
				}
			};

			// Loop over components to find ResourceComponents
			for (Component i : activeForm.getComponents()) {
				i.accept(visitor);
			}
			// Set the tree selection to the first available resource for this
			// editor
			setDefaultResourceSelection();

			// We would like to call ICEResourcePage#selectionChanged here to
			// update the browser, but a solution to this has not been found
			// yet.
		}

		return;
	}

	/**
	 * This function is called whenever a Workbench part is closed. Here, we are
	 * only interested if the part is an ICEFormEditor. Get the Form from the
	 * editor. If this is the most recently active form, clear its resources
	 * from the resourceTreeViewer.
	 * 
	 * @see IPartListener2#partClosed(IWorkbenchPartReference)
	 */
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {

		System.out.println("ICEResourceView Message: Called partClosed("
				+ partRef.getId() + ")");

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			// Get the closed editor
			ICEFormEditor activeEditor = (ICEFormEditor) partRef.getPart(false);
			// Get the form from the editor
			Form activeForm = ((ICEFormInput) activeEditor.getEditorInput())
					.getForm();
			// If this is the most recently active form, clear the TreeViewer.
			if (activeForm.getItemID() == lastFormItemID) {
				textList.clear();
				imageList.clear();
				resourceTreeViewer.refresh();
				resourceTreeViewer.getTree().redraw();
			}
		}

		return;
	}

	/**
	 * This function is called whenever a Workbench part is covered by another
	 * part. Here, we are only interested if the part is an ICEFormEditor. For
	 * our purposes, this function call always precedes a call to partClosed, so
	 * here we are just recording the ID as the most recently active part.
	 * 
	 * @see IPartListener2#partHidden(IWorkbenchPartReference)
	 */
	@Override
	public void partHidden(IWorkbenchPartReference partRef) {

		System.out.println("ICEResourceView Message: Called partHidden("
				+ partRef.getId() + ")");

		if (partRef.getId().equals(ICEFormEditor.ID)) {
			// Get the hidden editor
			ICEFormEditor activeEditor = (ICEFormEditor) partRef.getPart(false);
			// Get the form from the editor
			Form activeForm = ((ICEFormInput) activeEditor.getEditorInput())
					.getForm();
			// Record the ID of this form
			lastFormItemID = activeForm.getItemID();
		}

		return;
	}

	/**
	 * @see IPartListener2#partBroughtToTop(IWorkbenchPartReference)
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// Do nothing.
		return;
	}

	/**
	 * @see IPartListener2#partDeactivated(IWorkbenchPartReference)
	 */
	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// Do nothing.
		return;
	}

	/**
	 * @see IPartListener2#partOpened(IWorkbenchPartReference)
	 */
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		// Do nothing.
		return;
	}

	/**
	 * @see IPartListener2#partVisible(IWorkbenchPartReference)
	 */
	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// Do nothing.
		return;
	}

	/**
	 * @see IPartListener2#partInputChanged(IWorkbenchPartReference)
	 */
	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// Do nothing.
		return;
	}

	/**
	 * This operation sets the default selection in the TreeViewer and returns
	 * the resource.
	 * 
	 * @return The ICEResource selected by this function.
	 */
	public ICEResource setDefaultResourceSelection() {

		// Local Declarations
		ICEResource resource = null;

		// If there are files, use the first one. Otherwise, if there are images
		// use the first one. Otherwise there are no resources.
		if (!textList.isEmpty()) {
			resourceTreeViewer.setSelection(
					new StructuredSelection(textList.get(0)), true);
			resource = (ICEResource) textList.get(0).getWrappedData();
		} else if (!imageList.isEmpty()) {
			resourceTreeViewer.setSelection(
					new StructuredSelection(imageList.get(0)), true);
			resource = (ICEResource) imageList.get(0).getWrappedData();
		}

		return resource;
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
	private static class NRVPropertySource extends PropertySource {

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
		public NRVPropertySource(Object obj) {

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