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
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ice.client.common.properties.CellColumnLabelProvider;
import org.eclipse.ice.client.common.properties.ComboCellEditingSupport;
import org.eclipse.ice.client.common.properties.ICellContentProvider;
import org.eclipse.ice.client.common.properties.IComboCellContentProvider;
import org.eclipse.ice.client.common.properties.NameCellContentProvider;
import org.eclipse.ice.client.common.properties.TextCellEditingSupport;
import org.eclipse.ice.client.common.properties.TreeProperty;
import org.eclipse.ice.client.common.properties.TreePropertyContentProvider;
import org.eclipse.ice.client.common.properties.ValueCellContentProvider;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.SelectiveComponentVisitor;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.BasicEntryContentProvider;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(TreePropertySection.class);	
	
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

	/**
	 * The currently selected property or parameter in the {@link #tableViewer}.
	 */
	private Entry selectedEntry;

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
	 * The <code>Composite</code> that contains the {@link #type} selection
	 * widget and other related <code>Controls</code>.
	 */
	private Composite typeComposite;
	/**
	 * The widget used to select the type of the {@link #tree} if it is an
	 * {@link AdaptiveTreeComposite}.
	 */
	private ComboViewer type;

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

	/**
	 * The <code>Button</code> for adding a new <code>Entry</code> (aka property
	 * or parameter) to the {@link #tableViewer}'s underlying {@link #tree}.
	 */
	private Button add;
	/**
	 * The <code>Button</code> for deleting the {@link #selectedEntry} (aka
	 * property or parameter) from the {@link #tableViewer}'s underlying
	 * {@link #tree}.
	 */
	private Button delete;

	// -------------------------- //

	/**
	 * The currently associated {@link ICEFormEditor} to which the {@link #tree}
	 * belongs, or null if one does not exist.
	 */
	private ICEFormEditor editor;

	/**
	 * The default constructor.
	 */
	public TreePropertySection() {

		// Initialize the widgets and initial tree to null.
		tree = null;
		isAdaptive = false;
		selectedEntry = null;

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
		Color backgroundColor = parent.getBackground();

		// Create a section for the data composites.
		section = getWidgetFactory().createSection(parent,
				ExpandableComposite.SHORT_TITLE_BAR | Section.DESCRIPTION);
		section.setText("Node properties");
		section.setDescription("All properties available for "
				+ "this node can be modified here.");
		section.setBackground(backgroundColor);

		// Create the Composite that contains all DataComponentComposites.
		final Composite client = new Composite(section, SWT.NONE);
		GridLayout clientLayout = new GridLayout(2, false);
		// Set the margins and spacing based on the tabbed property constants.
		clientLayout.marginLeft = ITabbedPropertyConstants.HMARGIN;
		clientLayout.marginRight = ITabbedPropertyConstants.HMARGIN;
		clientLayout.marginTop = ITabbedPropertyConstants.VMARGIN;
		clientLayout.marginBottom = ITabbedPropertyConstants.VMARGIN;
		clientLayout.horizontalSpacing = ITabbedPropertyConstants.HSPACE;
		clientLayout.verticalSpacing = ITabbedPropertyConstants.VSPACE;
		client.setLayout(clientLayout);

		// Make the background of the section client white unless ICE is in
		// debug mode.
		if (System.getProperty("DebugICE") == null) {
			client.setBackground(backgroundColor);
		} else {
			client.setBackground(Display.getCurrent().getSystemColor(
					SWT.COLOR_RED));
		}

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

		// Create the type Combo Composite.
		typeComposite = createTypeComposite(client);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalSpan = 2;
		typeComposite.setLayoutData(gridData);
		// Refresh the contents of the type Combo and its containing Composite.
		refreshTypeWidgets();

		// Create the table of properties.
		tableViewer = createTableViewer(client);
		// Set the table's layout data so it occupies all spare space in the
		// property section client.
		tableViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		// Create the add/delete buttons.
		Composite buttonComposite = createButtons(client);
		// The button Composite shouldn't grab any space. Align it along the
		// center and top of the space to the right of the table.
		buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false));

		return;
	}

	/**
	 * This operation sets the current input displayed in the table of
	 * properties. It also sets {@link #isAdaptive} if the input is an
	 * {@link AdaptiveTreeComposite}.
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

						logger.info("TreePropertySection message: "
								+ "Setting the input to tree " + tree.getName()
								+ ".");
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

		// Enable or disable the add button depending on whether the input is
		// valid.
		if (add != null) {
			boolean canAdd = (canAdd(tree) != null);
			add.setEnabled(canAdd);
		}

		// Try to find the associated ICEFormEditor. It will be marked as dirty
		// if the properties change.
		if (part instanceof TreeCompositeViewer) {
			editor = ((TreeCompositeViewer) part).getFormEditor();
		} else {
			editor = null;
		}

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
		selectedEntry = null;

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
		add = null;
		delete = null;

		return;
	}

	// ----------------------------------------- //

	// ---- AdaptiveTreeComposite type selection widgets ---- //
	/**
	 * Creates the {@link #type} selection widget for changing the type of the
	 * current <code>AdaptiveTreeComposite</code>. These widgets are
	 * 
	 * @param client
	 *            The client <code>Composite</code> that should contain the type
	 *            selection widgets.
	 * @return The <code>Composite</code> that contains the type selection
	 *         widgets.
	 */
	private Composite createTypeComposite(Composite client) {

		// Get the client's background color.
		Color backgroundColor = client.getBackground();

		// Create the type sub-section Composite that will contain the
		// type label and combo (dropdown).
		Composite typeComposite = new Composite(client, SWT.NONE);
		typeComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
				false, true));
		typeComposite.setBackground(backgroundColor);

		// Set the layout of the Composite to a vertical fill layout.
		// This ensures the type Label is above the Combo and that both
		// consume all available space.
		FillLayout typeCompositeLayout = new FillLayout(SWT.VERTICAL);
		typeCompositeLayout.marginHeight = 5;
		typeCompositeLayout.marginWidth = 3;
		typeCompositeLayout.spacing = 5;
		typeComposite.setLayout(typeCompositeLayout);

		// Add the type Label and an empty Combo.
		Label typeLabel = new Label(typeComposite, SWT.NONE);
		typeLabel.setText("Type:");
		typeLabel.setBackground(backgroundColor);

		// Create the ComboViewer for selecting the adaptive type.
		type = new ComboViewer(typeComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		type.getCombo().setBackground(backgroundColor);
		// Use an ArrayContentProvider so we can set the List of type Strings as
		// input to the ComboViewer.
		type.setContentProvider(ArrayContentProvider.getInstance());
		// The labels for each value should just be their String value.
		type.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return (element != null ? element.toString() : "");
			}
		});

		// Add a listener to set the type of the AdaptiveTreeComposite when the
		// Combo's selection changes.
		ISelectionChangedListener listener = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (!selection.isEmpty()
						&& selection instanceof IStructuredSelection) {
					IStructuredSelection s = (IStructuredSelection) selection;
					String type = (String) s.getFirstElement();
					((AdaptiveTreeComposite) tree).setType(type);
				}
			}
		};
		type.addSelectionChangedListener(listener);

		return typeComposite;
	}

	/**
	 * Refreshes the contents of the {@link #type} selection widget. If the
	 * {@link #tree} is not an {@link AdaptiveTreeComposite}, then the entire
	 * {@link #typeComposite} is hidden.
	 */
	private void refreshTypeWidgets() {

		// Set the default empty list of types and the default empty selection.
		List<String> types = new ArrayList<String>(1);
		ISelection selection = new StructuredSelection();

		// Get the list of adaptive types if possible.
		if (tree != null && isAdaptive) {
			// Update the available types in the type Combo widget.
			AdaptiveTreeComposite adaptiveTree = (AdaptiveTreeComposite) tree;
			types = adaptiveTree.getTypes();
			String adaptiveType = adaptiveTree.getType();
			if (adaptiveType != null) {
				selection = new StructuredSelection(adaptiveType);
			}
		}

		// Set the ComboViewer's contents to the List of type Strings.
		type.setInput(types);
		type.setSelection(selection);

		// Hide the type selection widgets if the List is empty.
		boolean hide = types.isEmpty();
		typeComposite.setVisible(!hide);
		((GridData) typeComposite.getLayoutData()).exclude = hide;

		// Re-adjust the size of the type Composite.
		typeComposite.pack();
		typeComposite.getParent().layout();

		return;
	}

	// ------------------------------------------------------ //\

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
			contentProvider = new NameCellContentProvider() {
				@Override
				public boolean setValue(Object element, Object value) {
					boolean changed = super.setValue(element, value);
					// If the value changed, mark the associated ICEFormEditor
					// as dirty.
					if (changed && editor != null) {
						editor.setDirty(true);
					}
					return changed;
				}
			};
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
			contentProvider = new ValueCellContentProvider() {
				@Override
				public boolean setValue(Object element, Object value) {
					boolean changed = super.setValue(element, value);
					// If the value changed, mark the associated ICEFormEditor
					// as dirty.
					if (changed && editor != null) {
						editor.setDirty(true);
					}
					return changed;
				}
			};
			valueColumn.setLabelProvider(new CellColumnLabelProvider(
					contentProvider));
			valueColumn.setEditingSupport(new ComboCellEditingSupport(
					tableViewer, (IComboCellContentProvider) contentProvider));
			// ---------------------------------- //
		}

		return;
	}

	// ---------------------------------------------- //

	// ---- Add/Delete property widgets ---- //
	/**
	 * Creates {@link #add} and {@link #delete} buttons for changing the
	 * parameters displayed in the properties.
	 * 
	 * @param client
	 *            The client <code>Composite</code> that should contain the
	 *            buttons.
	 * @return The <code>Composite</code> that contains the add and delete
	 *         buttons.
	 */
	private Composite createButtons(Composite client) {

		// Create the two buttons to add/remove parameters next to the table.
		// Create a Composite to hold these buttons.
		Composite composite = new Composite(client, SWT.NONE);
		composite.setBackground(client.getBackground());
		// The buttons should sit in a column.
		composite.setLayout(new GridLayout());

		// Create the add button.
		add = new Button(composite, SWT.PUSH);
		add.setBackground(client.getBackground());
		add.setText("+");
		add.setToolTipText("Add a new property.");
		// Set the layout data. It should default to at least 30 pixels wide.
		GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gridData.heightHint = 30;
		gridData.widthHint = 30;
		add.setLayoutData(gridData);

		// Add should only be enabled if something's properties are displayed.
		// Delete should only be enabled if a property is selected.
		boolean canAdd = (tree != null);
		add.setEnabled(canAdd);

		// Add a listener to add a new property/parameter when the add button is
		// clicked.
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				addParameter();
			}
		});

		// Create the delete button.
		delete = new Button(composite, SWT.PUSH);
		delete.setBackground(client.getBackground());
		delete.setText("-");
		delete.setToolTipText("Delete the selected property.");
		// Set the layout data. It should default to at least 30 pixels wide.
		gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gridData.heightHint = 30;
		gridData.widthHint = 30;
		delete.setLayoutData(gridData);

		// Listen for changes to the TableViewer's selection and enable/disable
		// the delete button if a property is selected.
		delete.setEnabled(false);
		ISelectionChangedListener listener = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				// Disable the option to delete.
				selectedEntry = null;
				delete.setEnabled(false);

				// Try to get the selected Entry from the selection.
				ISelection selection = event.getSelection();
				if (!selection.isEmpty()
						&& selection instanceof IStructuredSelection) {
					Entry entry = ((TreeProperty) ((IStructuredSelection) selection)
							.getFirstElement()).getEntry();

					// If the Entry is not required, we can delete it.
					if (canDelete(entry)) {
						selectedEntry = entry;
						delete.setEnabled(true);
					}
				}

				return;
			}
		};
		tableViewer.addSelectionChangedListener(listener);

		// Add a listener to delete the currently selected property/parameter
		// when the delete button is clicked.
		delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				deleteParameter();
			}
		});

		return composite;
	}

	// TODO We may need a different requirement on when we can add an Entry to
	// the selected tree or where we can add it.
	/**
	 * Gets whether or not the specified <code>TreeComposite</code> can have an
	 * <code>Entry</code> (aka property or parameter) added to it. The
	 * <code>DataComponent</code> that can receive the new <code>Entry</code> is
	 * returned.
	 * 
	 * @param tree
	 *            The tree to which we would like to add a new property or
	 *            parameter.
	 * @return The tree's data node that can receive a new property or
	 *         parameter, or null if one cannot be added.
	 */
	private DataComponent canAdd(TreeComposite tree) {

		DataComponent dataNode = null;

		// Look for the active data node. If there is no active data node, get
		// the first available data node.
		if (tree != null) {
			dataNode = (DataComponent) tree.getActiveDataNode();
			if (dataNode == null && !tree.getDataNodes().isEmpty()) {
				dataNode = (DataComponent) tree.getDataNodes().get(0);
			}
		}

		return dataNode;
	}

	/**
	 * Adds a new <code>Entry</code> (aka property or parameter) to the active
	 * or first available data node in the {@link #tree}.
	 */
	private void addParameter() {

		// Get either the active data node or the first available data node.
		DataComponent dataNode = canAdd(tree);

		// If there is a data node, add a new Entry to it.
		if (dataNode != null && !dataNode.contains("New parameter")) {
			// Create an Entry with a BasicEntryContentProvider.
			Entry entry = new Entry(new BasicEntryContentProvider());
			// Set the Entry's initial properties.
			entry.setName("new_parameter");
			entry.setDescription("");
			entry.setTag(entry.getName());
			entry.setRequired(false);
			entry.setValue("");
			// Add the Entry to the data node.
			dataNode.addEntry(entry);
		}

		return;
	}

	/**
	 * Gets whether or not the specified <code>Entry</code> (aka property or
	 * parameter) can be deleted from its containing <code>DataComponent</code>
	 * or data node in the {@link #tree}. Any parameter can be deleted.
	 * 
	 * @param entry
	 *            The property or parameter that is a candidate for deletion.
	 * @return True if the entry can be deleted, false otherwise.
	 */
	private boolean canDelete(Entry entry) {
		return (entry != null && !entry.isRequired());
	}

	/**
	 * Deletes the <code>Entry</code> (aka property or parameter) that is
	 * currently selected in the {@link #tableViewer}. A reference to this
	 * <code>Entry</code> is stored as {@link #selectedEntry} when selected in
	 * the <code>TableViewer</code>.
	 */
	private void deleteParameter() {

		if (canDelete(selectedEntry)) {
			String entryName = selectedEntry.getName();
			// Find the data node that has the selected Entry. If the Entry is
			// found, remove it from the containing data node and stop.
			for (Component component : tree.getDataNodes()) {
				DataComponent dataNode = (DataComponent) component;
				if (dataNode.contains(entryName)
						&& dataNode.retrieveEntry(entryName) == selectedEntry) {
					dataNode.deleteEntry(entryName);
					break;
				}
			}
		}

		return;
	}

	// ------------------------------------- //

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

	/**
	 * Gets the currently associated {@link ICEFormEditor} to which the
	 * {@link #tree} belongs, or null if one does not exist.
	 * 
	 * @return The associated {@code ICEFormEditor}, or {@code null} if none.
	 */
	protected ICEFormEditor getFormEditor() {
		return editor;
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
			// Create a selection from the adaptive tree's current type.
			String adaptiveType = ((AdaptiveTreeComposite) component).getType();
			final StructuredSelection selection;
			selection = new StructuredSelection(adaptiveType);

			// Use the UI thread to update the type ComboViewer's selection.
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					type.setSelection(selection);
				}
			});
		}

		return;
	}
	// ---------------------------------------- //

}
