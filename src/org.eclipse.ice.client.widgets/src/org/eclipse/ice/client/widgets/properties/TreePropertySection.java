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
package org.eclipse.ice.client.widgets.properties;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateable;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * This class provides a proprty <code>Section</code> that displays the
 * properties for a {@link TreeComposite}.
 * <p>
 * The properties are displayed in a JFace {@link TableViewer}. Each row in the
 * table corresponds to an {@link Entry} in one of the tree's
 * {@link DataComponent}s or data nodes.
 * </p>
 * 
 * @author Jordan
 * 
 */
public class TreePropertySection extends AbstractPropertySection implements
		IUpdateableListener {

	/**
	 * The <code>TreeComposite</code> whose properties are displayed in this
	 * section.
	 */
	private TreeComposite tree;
	/**
	 * Whether or not the <code>TreeComposite</code> is an
	 * <code>AdaptiveTreeComposite</code>. This is necessary for creating a
	 * widget that can set the type of adaptive tree.
	 */
	private boolean isAdaptive;

	// ---- Ancestor Controls ---- //
	// We keep track of these so we can fill the Properties View with the table.
	// We also do not want to use the Property View's scroll bars since the
	// table has its own horizontal and vertical scroll bars and we would like
	// to show the add and delete buttons next to the table.
	/**
	 * The ScrolledComposite that occupies the Property Viewer. Its minimum size
	 * (the size at which it starts drawing scroll bars) should be updated
	 * because of GridLayouts in this Section.
	 */
	private ScrolledComposite scrollComposite;
	/**
	 * The client Composite for the {@link #scrollComposite}.
	 */
	private Composite scrollCompositeClient;
	/**
	 * The default minimum width of {@link #scrollComposite} as defined by
	 * {@link ScrolledComposite#getMinWidth()}.
	 */
	private int scrollMinWidth;
	/**
	 * The default minimum height of {@link #scrollComposite} as defined by
	 * {@link ScrolledComposite#getMinHeight()}.
	 */
	private int scrollMinHeight;

	// --------------------------- //

	// ---- Section Controls ---- //
	/**
	 * The <code>Section</code> that contains the properties or parameters of
	 * the {@link #tree}.
	 */
	private Section section;

	/**
	 * The ToolBar shown above the {@link #tableViewer}. It should contain the
	 * add/remove buttons and any other property widgets.
	 */
	private ToolBarManager toolBarManager;
	/**
	 * The ToolBar action that adds new, blank properties to the {@link #tree}.
	 */
	private final AddPropertyAction addAction = new AddPropertyAction();
	/**
	 * The ToolBar action that removes properties that are selected in the
	 * {@link #tableViewer}.
	 */
	private final RemovePropertyAction removeAction = new RemovePropertyAction();
	/**
	 * The ToolBar action that lets the user select the type of {@link #tree}
	 * for adaptive trees.
	 */
	private final ActionTree typeTree = new ActionTree("Type");

	/**
	 * The JFace <code>TableViewer</code> that contains the properties of the
	 * {@link #tree}.
	 */
	private TableViewer tableViewer;
	/**
	 * The {@link #tableViewer}'s column for a property or parameter's name.
	 */
	private TableViewerColumn nameColumn;
	/**
	 * The {@link #tableViewer}'s column for a property or parameter's value.
	 */
	private TableViewerColumn valueColumn;

	// -------------------------- //

	/**
	 * The default constructor.
	 */
	public TreePropertySection() {

		// Initialize the widgets and initial tree to null.
		tree = null;
		isAdaptive = false;

		return;
	}

	// ---- Extends AbstractPropertySection ---- //
	/**
	 * This operation draws the (initial) controls in the properties view based
	 * on the input. In this case, there are initially no widgets to prepare.
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		// Get the default background color.
		Color background = parent.getBackground();

		// Create a section for the data composites.
		section = getWidgetFactory().createSection(parent,
				Section.SHORT_TITLE_BAR | Section.DESCRIPTION);
		section.setText("Node properties");
		section.setDescription("All properties available for "
				+ "this node can be modified here.");
		section.setBackground(background);

		// Create the Composite that contains all DataComponentComposites.
		final Composite client = new Composite(section, SWT.NONE);
		GridLayout clientLayout = new GridLayout();
		// Set the margins and spacing based on the tabbed property constants.
		clientLayout.marginLeft = ITabbedPropertyConstants.HMARGIN;
		clientLayout.marginRight = ITabbedPropertyConstants.HMARGIN;
		clientLayout.marginTop = ITabbedPropertyConstants.VMARGIN;
		clientLayout.marginBottom = ITabbedPropertyConstants.VMARGIN;
		clientLayout.horizontalSpacing = ITabbedPropertyConstants.HSPACE;
		clientLayout.verticalSpacing = ITabbedPropertyConstants.VSPACE;
		client.setLayout(clientLayout);

		// Style the client after its parent.
		client.setBackground(background);

		// Set the client area for the section.
		section.setClient(client);

		// Get the property viewer's ScrolledComposite and its first Composite
		// (its "client" Composite).
		scrollCompositeClient = section.getParent().getParent().getParent()
				.getParent();
		scrollComposite = (ScrolledComposite) scrollCompositeClient.getParent();

		// Add a listener to resize the Section's properties and update the
		// ScrollComposite's minimum bounds correctly based on the displayed
		// properties.
		scrollComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				resizePropertyView();
			}
		});

		// Create the ToolBar that contains the add/remove and other widgets.
		toolBarManager = new ToolBarManager(SWT.RIGHT);
		ToolBar toolBar = toolBarManager.createControl(client);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		toolBar.setBackground(background);

		// Create the table of properties.
		tableViewer = createTableViewer(client);
		// Set the table's layout data so it occupies all spare space in the
		// property section client.
		tableViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		// Fill the ToolBar. We do this here because the remove action needs to
		// listen to the TableViewer for its selection.
		fillToolBar(toolBarManager);
		toolBarManager.update(true);

		return;
	}

	/**
	 * This operation sets the current input displayed in the table of
	 * properties. Specifically, it sets {@link #adaptiveTree} if the input is
	 * an {@link AdaptiveTreeComposite}.
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		// Since the tree's DataComponent will still have its properties shown,
		// continue with the default setInput() operation.
		super.setInput(part, selection);

		// Get and check the selection.
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();

		// Try to get the input TreeComposite from the selection.
		if (input != null && input instanceof Component) {
			IComponentVisitor visitor = new SelectiveComponentVisitor() {
				@Override
				public void visit(TreeComposite component) {

					// If the visited tree is new, we need to replace the
					// current tree with the new one.
					if (tree != component) {
						// If possible, unregister from the current tree.
						if (tree != null) {
							tree.unregister(TreePropertySection.this);
						}

						// Set the reference to the new tree and register for
						// updates. We need the updates for keeping the type
						// ComboViewer updated.
						tree = component;
						tree.register(TreePropertySection.this);

						// Unset the isAdaptive flag.
						isAdaptive = false;
					}

					return;
				}

				@Override
				public void visit(AdaptiveTreeComposite component) {
					visit((TreeComposite) component);
					isAdaptive = true;
				}
			};
			((Component) input).accept(visitor);
		}

		addAction.setTree(tree);
		removeAction.setTree(tree);

		// Refresh the type Combo widget.
		refreshTypeWidgets();

		return;
	}

	/**
	 * Refreshes any widgets required for the current input for this property
	 * section.
	 */
	@Override
	public void refresh() {

		if (tree != null) {
			// Set the contents of the TableViewer.
			tableViewer.setInput(tree);
			// Automatically adjust the widths of the name and value columns.
			nameColumn.getColumn().pack();
			valueColumn.getColumn().pack();
			// Refresh the type Combo widget.
			refreshTypeWidgets();
		}

		// Refresh the size of the contents of the section.
		resizePropertyView();

		// Now perform the usual refresh operation.
		super.refresh();

		return;
	}

	/**
	 * Because this <code>Section</code> must alter the layout behavior of the
	 * containing Eclipse RCP View in order to properly lay out the
	 * {@link #tableViewer} and related <code>Controls</code>, this method must
	 * store any layout properties for parent <code>Composites</code> (e.g.,
	 * {@link #scrollComposite} and {@link #scrollCompositeClient}) that will be
	 * changed in {@link #resizePropertyView()}. These layout properties must be
	 * restored in {@link #aboutToBeHidden()}.
	 */
	@Override
	public void aboutToBeShown() {
		super.aboutToBeShown();

		// Store some of the default properties of the ScrolledComposite.
		scrollMinWidth = scrollComposite.getMinWidth();
		scrollMinHeight = scrollComposite.getMinHeight();

		return;
	}

	/**
	 * Restores the layout properties stored in {@link #aboutToBeShown()} and
	 * changed in {@link #resizePropertyView()}.
	 */
	@Override
	public void aboutToBeHidden() {
		super.aboutToBeHidden();

		// If possible, we need to restore the settings clobbered in
		// resizeProperties();
		if (!scrollComposite.isDisposed()) {
			// Restore the GridData of the Section's parent.
			GridData gridData = (GridData) section.getParent().getLayoutData();
			gridData.heightHint = SWT.DEFAULT;

			// Restore the default size of the ScrolledComposite's client.
			Point size = scrollCompositeClient.computeSize(SWT.DEFAULT,
					SWT.DEFAULT);
			scrollCompositeClient.setSize(size);

			// Restore the min size of the ScrolledComposite.
			scrollComposite.setMinSize(scrollMinWidth, scrollMinHeight);

			// Re-layout the ScrolledComposite.
			scrollComposite.layout();
		}

		return;
	}

	/**
	 * Disposes of the {@link #section} and its contained Widgets and clears
	 * references to any class variables.
	 */
	@Override
	public void dispose() {
		super.dispose();

		// Clear the references to the selected Tree and selected property
		// Entry.
		tree = null;

		// Clear the references to the Property View's ScrolledComposite and its
		// client.
		scrollComposite = null;
		scrollCompositeClient = null;

		// Dispose the section and clear references to all widgets. (Disposing
		// the section should dispose of its child widgets recursively.)
		section.dispose();
		section = null;
		tableViewer = null;
		nameColumn = null;
		valueColumn = null;

		return;
	}

	// ----------------------------------------- //

	// ---- ToolBar (Add/Remove/Type) widgets ---- //
	/**
	 * Fills the tool bar above the {@link #tableViewer}. The default behavior
	 * provides (in the listed order) the following actions:
	 * <ol>
	 * <li>Add - Adds a new property</li>
	 * <li>Remove - Removes the selected property(ies)</li>
	 * <li>Type (if applicable) - Sets the type for
	 * {@link AdaptiveTreeComposite}s.</li>
	 * </ol>
	 * 
	 * @param toolBarManager
	 *            The JFace {@code ToolBarManager} that handles the
	 *            {@code ToolBar}.
	 */
	private void fillToolBar(ToolBarManager toolBarManager) {

		// Create the add action tree.
		// TODO Make this an ActionTree whose default action is to add a new,
		// blank property, but with an option to add pre-defined properties.
		toolBarManager.add(addAction);

		// Create the remove action.
		tableViewer.addSelectionChangedListener(removeAction);
		toolBarManager.add(removeAction);

		// Create the type action tree (if applicable).
		refreshTypeWidgets();
		toolBarManager.add(typeTree.getContributionItem());

		return;
	}

	// ------------------------------------------- //

	/**
	 * Refreshes the contents of the {@link #type} selection widget. If the
	 * {@link #tree} is not an {@link AdaptiveTreeComposite}, then the entire
	 * {@link #typeComposite} is hidden.
	 */
	private void refreshTypeWidgets() {

		// Remove all previous types from the tree.
		typeTree.removeAll();

		// TODO Use checkboxes for the adaptive type menu.
		// TODO Show/hide the type menu instead of enabling/disabling it.

		if (tree != null && isAdaptive) {
			final AdaptiveTreeComposite adaptiveTree;
			adaptiveTree = (AdaptiveTreeComposite) tree;

			List<String> types = adaptiveTree.getTypes();
			for (final String type : types) {
				typeTree.add(new ActionTree(new Action(type) {
					@Override
					public void run() {
						adaptiveTree.setType(type);
					}
				}));
			}

			// Enable/show the ToolItem.
			typeTree.setEnabled(true);
			// typeTree.setVisible(true);
		} else {
			// Disable/hide the ToolItem.
			typeTree.setEnabled(false);
			// typeTree.setVisible(false);
		}

		return;
	}

	// ---- TreeComposite property Table widgets ---- //
	/**
	 * Creates the table that displays properties for viewing and editing.
	 * 
	 * @param client
	 *            The client <code>Composite</code> that should contain the
	 *            table of properties.
	 * @return The <code>TableViewer</code> for the table of properties.
	 */
	protected TableViewer createTableViewer(Composite client) {

		TableViewer tableViewer = null;

		if (client != null) {
			Table table;

			// Create the TableViewer and the underlying Table Control.
			tableViewer = new TableViewer(client, SWT.BORDER
					| SWT.FULL_SELECTION | SWT.V_SCROLL);
			// Set some properties for the table.
			table = tableViewer.getTable();
			table.setHeaderVisible(true);
			table.setLinesVisible(true);

			// Set up the content provider for the table viewer. Now the table
			// viewer's input can be set.
			tableViewer.setContentProvider(new TreePropertyContentProvider());

			// Enable tool tips for the Table's cells.
			ColumnViewerToolTipSupport.enableFor(tableViewer,
					ToolTip.NO_RECREATE);

			// Populate the TableViewer with its columns.
			addTableViewerColumns(tableViewer);
		}

		return tableViewer;
	}

	/**
	 * Adds columns to the provided <code>TableViewer</code>.
	 * 
	 * @param tableViewer
	 */
	protected void addTableViewerColumns(TableViewer tableViewer) {

		if (tableViewer != null) {
			TableColumn column;
			ICellContentProvider contentProvider;

			// ---- Create the name column. ---- //
			nameColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			column = nameColumn.getColumn();
			column.setText("Name");
			column.setToolTipText("The name of the property");
			column.setResizable(true);
			// Create an ICellContentProvider, then use it to generate the
			// column labels and provide editing support for the name column.
			contentProvider = new NameCellContentProvider();
			nameColumn.setLabelProvider(new CellColumnLabelProvider(
					contentProvider));
			nameColumn.setEditingSupport(new TextCellEditingSupport(
					tableViewer, contentProvider));
			// --------------------------------- //

			// ---- Create the value column. ---- //
			valueColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
			column = valueColumn.getColumn();
			column.setText("Value");
			column.setToolTipText("The value of the property");
			column.setResizable(true);
			// Create an ICellContentProvider, then use it to generate the
			// column labels and provide editing support for the value column.
			contentProvider = new ValueCellContentProvider();
			valueColumn.setLabelProvider(new CellColumnLabelProvider(
					contentProvider));
			valueColumn.setEditingSupport(new ComboCellEditingSupport(
					tableViewer, (IComboCellContentProvider) contentProvider));
			// ---------------------------------- //
		}

		return;
	}

	// ---------------------------------------------- //

	/**
	 * Gets the <code>TreeComposite</code> whose properties are displayed in
	 * this section.
	 * 
	 * @return The current tree whose properties are shown in a table.
	 */
	protected TreeComposite getTree() {
		return tree;
	}

	/**
	 * Resizes the Section's client and updates the Property Viewer's
	 * {@link ScrolledComposite} to account for GridLayouts in the client.
	 */
	private void resizePropertyView() {
		// TODO This could be simplified using the same technique used on the
		// DataComponentComposite in the ICESectionPage.

		// Disable re-drawing for the ScrolledComposite.
		scrollComposite.setRedraw(false);

		int verticalPadding = scrollComposite.getHorizontalBar().getSize().y;
		int horizontalPadding = scrollComposite.getVerticalBar().getSize().x;
		Rectangle clientArea = scrollComposite.getClientArea();
		Point clientAreaSize = new Point(clientArea.width - horizontalPadding,
				clientArea.height - verticalPadding);

		// Recompute the size of the first Composite in the ScrolledComposite
		// based on the width of the ScrolledComposite's client area.
		Point size = scrollCompositeClient.computeSize(clientAreaSize.x,
				clientAreaSize.y);

		// Update the size of the ScrolledComposite's client.
		scrollCompositeClient.setSize(size);

		// Set the minimum size at which the ScrolledComposite will start
		// drawing scroll bars. This should be the size of its client area minus
		// the spaces the scroll bars would consume.
		scrollComposite.setMinSize(clientAreaSize.x + 1, clientAreaSize.y + 1);

		// We need to call layout() so the ScrolledComposite will update.
		scrollComposite.layout();

		// Set the height hint for the Section's parent Composite. This keeps
		// the TableViewer from going beyond the bottom edge of the Properties
		// Viewer. We want to keep the add and delete buttons visible, and the
		// TableViewer already has its own scroll bars!
		Composite sectionParent = section.getParent();
		GridData gridData = (GridData) sectionParent.getLayoutData();
		GridLayout gridLayout = (GridLayout) sectionParent.getParent()
				.getLayout();
		gridData.heightHint = sectionParent.getParent().getSize().y
				- gridLayout.marginTop;

		// The parent of the Section has a FillLayout. Its parent has a
		// GridLayout, but sometimes it does not update. Tell it to layout so
		// the Section's parent will update its size.
		section.getParent().getParent().layout();

		// Enable re-drawing for the ScrolledComposite.
		scrollComposite.setRedraw(true);

		return;
	}

	// ---- Implements IUpdateableListener ---- //
	/**
	 * Listens for updates from the {@link #tree}. If the tree is an
	 * {@link AdaptiveTreeComposite}, then this method updates the {@link #type}
	 * selection widget's selection.
	 */
	@Override
	public void update(IUpdateable component) {

		// If the adaptive tree's type was changed, we should update the type
		// Combo widget.
		if (component != null && component == tree && isAdaptive) {
			// Use the UI thread to update the type ComboViewer's selection.
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					// TODO When the type tree uses radio buttons or checkboxes,
					// we need to update the selected one here.
				}
			});
		}

		return;
	}
	// ---------------------------------------- //

}
