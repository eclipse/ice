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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This class is a subclass of SectionPart from org.eclipse.ui.forms that
 * renders, updates and monitors an ICE DataComponent that is part of a ICEForm.
 * The ICESectionPart takes place in the IManagedForm lifecycle and receives is
 * refreshed dynamically if the underlying ICEDataComponent has been updated
 * (i.e. - "gone stale" in the Eclipse parlance). The Java source code for this
 * class contains a private hashmap that is not represented in the model because
 * Jay can not figure out how to show a java.util.hashmap in RSA.
 * 
 * @author Jay Jay Billings
 */
public class ICETableComponentSectionPart extends SectionPart implements
		IUpdateableListener {
	/**
	 * This attribute is a reference to an ICE TableComponent that stores the
	 * data that should be displayed by this SectionPart. The TableComponent
	 * will also update the ICESectionPart when its state changes (i.e. -
	 * becomes "stale" in the Eclipse parlance).
	 */
	protected TableComponent tableComponent;

	/**
	 * The TableViewer that is used rendered by the SectionPart.
	 */
	protected TableViewer tableComponentViewer;

	/**
	 * The Eclipse Managed Form in which the SectionPart will be rendered.
	 */
	protected IManagedForm parentForm;

	/**
	 * The Composite in which the TableViewer is drawn.
	 */
	protected Composite sectionClient;

	/**
	 * Holds a reference to the ICEFormEditor.
	 */
	private ICEFormEditor editor;

	/**
	 * Class used to pass information for the rows in TableComponent to
	 * TableViewer
	 * 
	 */
	private class RowWrapper {

		/**
		 * Holds an ArrayList of Entries in the TableComponent for TableViewer.
		 */
		private List<Entry> list;

		/**
		 * The Constructor, injects the ArrayList of Entries at this row.
		 * 
		 * @param list
		 *            The list of Entries that make up this RowWrapper.
		 * 
		 */
		public RowWrapper(List<Entry> list) {
			this.list = list;
		}

		/**
		 * <!-- begin-UML-doc -->
		 * <p>
		 * Return the ArrayList of Entries for this row.
		 * </p>
		 * <!-- end-UML-doc -->
		 * 
		 */
		public List<Entry> getRowWrapper() {
			return list;
		}
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * ICECellLabelProvider is a subclass of CellLabelProvider that keeps track
	 * of the current column index. This enables the correct display of tool
	 * tips that correspond to cells in the table
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 */
	private class ICECellLabelProvider extends CellLabelProvider {

		//
		/**
		 * <!-- begin-UML-doc -->
		 * <p>
		 * The index of the table column this CellLabelProvider corresponds to.
		 * </p>
		 * <!-- end-UML-doc -->
		 * 
		 */
		private int tableColumn;

		/**
		 * <!-- begin-UML-doc -->
		 * <p>
		 * The Constructor, used to inject the current column index
		 * </p>
		 * <!-- end-UML-doc -->
		 * 
		 * @param index
		 *            <p>
		 *            The index of the column that pertains to this
		 *            CellLabelProvider
		 *            </p>
		 * 
		 */
		public ICECellLabelProvider(int index) {
			// Invoke the super class constructor
			super();
			// Set the current column index
			tableColumn = index;
		}

		/**
		 * <!-- begin-UML-doc -->
		 * <p>
		 * Update is called whenever data is entered into the cell. Current
		 * implementation simply sets the text as what's currently the value on
		 * the Entry
		 * </p>
		 * <!-- end-UML-doc -->
		 * 
		 * @param cell
		 *            <p>
		 *            The current cell in the TableViewerColumn
		 *            </p>
		 * 
		 */
		@Override
		public void update(ViewerCell cell) {
			cell.setText(((RowWrapper) cell.getElement()).getRowWrapper()
					.get(tableColumn).getValue());
		}

		/**
		 * <!-- begin-UML-doc -->
		 * <p>
		 * Return the tool tip when a MouseHover event occurs. Current
		 * implementation displays the proper Entry's description attribute.
		 * </p>
		 * <!-- end-UML-doc -->
		 * 
		 * @param element
		 *            <p>
		 *            RowWrapper of the current row in the TableViewer
		 *            </p>
		 * 
		 */
		@Override
		public String getToolTipText(Object element) {
			return ((RowWrapper) element).getRowWrapper().get(tableColumn)
					.getDescription();
		}

		/**
		 * <!-- begin-UML-doc -->
		 * <p>
		 * Set the tool tip shift from the Mouse pointer.
		 * </p>
		 * <!-- end-UML-doc -->
		 * 
		 * @param object
		 * 
		 */
		@Override
		public Point getToolTipShift(Object object) {
			return new Point(5, 5);
		}

		/**
		 * <!-- begin-UML-doc -->
		 * <p>
		 * Set the time it should take to pop up the tool tip.
		 * </p>
		 * <!-- end-UML-doc -->
		 * 
		 * @param object
		 * 
		 */
		@Override
		public int getToolTipDisplayDelayTime(Object object) {
			return 100;
		}

		/**
		 * <!-- begin-UML-doc -->
		 * <p>
		 * Set the time the tool tip should stay displayed.
		 * </p>
		 * <!-- end-UML-doc -->
		 * 
		 * @param object
		 * 
		 */
		@Override
		public int getToolTipTimeDisplayed(Object object) {
			return 5000;
		}
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param section
	 *            <p>
	 *            The new Section to be managed by the ICESectionPart.
	 *            </p>
	 * @param formEditor
	 *            <p>
	 *            The FormEditor that is managing all of the Pages and
	 *            SectionParts.
	 *            </p>
	 * @param managedForm
	 *            <p>
	 *            The ManagedForm for the Section.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICETableComponentSectionPart(Section section, FormEditor formEditor,
			IManagedForm managedForm) {
		// begin-user-code
		super(section);

		// Set the ManagedForm
		// Throw an exception if it is not initialized.
		if (managedForm != null) {
			parentForm = managedForm;
		} else {
			throw new RuntimeException("ManagedForm in ICEFormSectionPart "
					+ "constructor cannot be null.");
		}
		// FIXME is this safe??? SFH
		editor = (ICEFormEditor) formEditor;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the TableComponent that should be rendered, updated
	 * and monitored by the ICESectionPart.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param table
	 *            <p>
	 *            The TableComponent that should be rendered by the SectionPart.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setTableComponent(TableComponent table) {
		// begin-user-code

		// If table is null or if the table's row template has not been set,
		// return
		if (table == null || table.getRowTemplate() == null) {
			return;
		}
		// If the table is not null and the row template has been set, set the
		// tableComponent
		// to table
		tableComponent = table;
		tableComponent.register(this);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation retrieves the TableComponent that is currently rendered,
	 * updated and monitored by the ICESectionPart.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The TableComponent that is rendered by the SectionPart.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public TableComponent getTableComponent() {
		// begin-user-code
		return tableComponent;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation reads the TableComponent assigned to this SectionPart and
	 * renders the view of that data for the user.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void renderSection() {
		// begin-user-code
		// Local Declarations
		Section section = getSection();

		// Add the main description and title to the section
		section.setDescription(tableComponent.getDescription());
		section.setText(tableComponent.getName());

		// setup the composite given the IManagedForm (parentForm)
		// and the section of this sectionPart.
		sectionClient = parentForm.getToolkit().createComposite(section);

		// Set the layout to have three columns.
		// Sets the table to take up one column, and the
		// add and delete buttons take the next column.
		sectionClient.setLayout(new GridLayout(2, false));

		// Setup the tableComponentViewer and buttons
		this.setupTableViewer();
		this.setupButtons();

		// Set an expansion listener in order to control the
		// sectionPart's visibility to the user.
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				parentForm.reflow(true);
			}
		});

		// Add the sectionClient Composite to the sectionPart.
		section.setClient(sectionClient);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operations sets up the add and delete row buttons for the
	 * TableViewer.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupButtons() {
		// begin-user-code

		// Checks to make sure the sectionClient is set. If not, returns and
		// does not set up the buttons
		if (sectionClient == null) {
			return;
		}
		// The following operations sets up the buttons for adding and deleting
		// rows. It also sets up the action listeners and the behaviors for
		// each operation.

		Button addButton = new Button(sectionClient, 0);
		addButton.setText("+");

		// Setup the selection listener for adding rows.
		SelectionListener sl;
		sl = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// Make sure the tableComponent and tableviewer are initialized
				if (tableComponent != null && tableComponentViewer != null) {

					// Add a row to the tableComponent
					tableComponent.addRow();

					// Reset input for tableViewer
					RowWrapper[] rows = new RowWrapper[tableComponent
							.getRowIds().size()];
					// add rows to list - using RowWrapper class
					for (int i = 0; i < tableComponent.getRowIds().size(); i++) {
						rows[i] = (new RowWrapper(tableComponent.getRow(i)));
					}
					// Reset the input
					tableComponentViewer.setInput(rows);

					// Make the editor dirty
					editor.setDirty(true);

				}
			}
		};
		// add the listener to the button
		addButton.addSelectionListener(sl);

		// Sets the GridData to keep the "+" button on the right and next the
		// "-" button
		GridData gridData2 = new GridData();
		// Sets the position of the button in its cell upward and rightward
		gridData2.horizontalAlignment = SWT.END;
		gridData2.verticalAlignment = SWT.END;
		// Sets an initial height and width that are unchangeable for the button
		gridData2.widthHint = 30;
		gridData2.heightHint = 30;
		// Applies this instance of GridData to the addButton
		addButton.setLayoutData(gridData2);

		// Setup the delete button in order to handle row deletion to manipulate
		// TableComponent and TableViewer.
		Button deleteButton = new Button(sectionClient, 0);
		deleteButton.setText("-");

		// setup selection listener for delete button
		SelectionListener sl2;
		sl2 = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// Make sure the tableComponent and tableviewer are initialized
				if (tableComponent != null && tableComponentViewer != null) {

					// Delete row from tableComponent
					tableComponent.deleteRow(tableComponentViewer.getTable()
							.getSelectionIndex());

					// Reset input for tableViewer
					RowWrapper[] rows = new RowWrapper[tableComponent
							.getRowIds().size()];
					// add rows to list - using RowWrapper class
					for (int i = 0; i < tableComponent.getRowIds().size(); i++) {
						rows[i] = (new RowWrapper(tableComponent.getRow(i)));
					}
					// Reset the input
					tableComponentViewer.setInput(rows);

					// Make the editor dirty
					editor.setDirty(true);
				}
			}
		};
		// Add the delete row button to the selection listener
		deleteButton.addSelectionListener(sl2);

		// Sets the GridData to keep the "-" button on the right and next the
		// "+" button
		GridData gridData3 = new GridData();
		// Sets the position of the button in its cell downward and rightward
		gridData3.horizontalAlignment = SWT.END;
		gridData3.verticalAlignment = SWT.BEGINNING;
		// Sets an initial height and width that are unchangeable for the button
		gridData3.widthHint = 30;
		gridData3.heightHint = 30;
		// Applies this instance of GridData to the deleteButton
		deleteButton.setLayoutData(gridData3);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the TableViewer.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void setupTableViewer() {
		// begin-user-code

		// Return if the sectionClient is null
		if (sectionClient == null) {
			return;
		}
		// Allow for a border, horizontal scroll, vertical scroll, and
		// full selection of the row. This style configuration is for the
		// TableViewer.
		int style = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION;

		// Initialize the tableViewer and set it up with the style
		// configuration. Add it to the sectionClient composite.
		tableComponentViewer = new TableViewer(sectionClient, style);

		// Create a table from the TableViewer.
		Table table = tableComponentViewer.getTable();

		// Initialize column names for TableViewer from TableComponent.
		String[] colNames = new String[tableComponent.getColumnNames().size()];

		// Original version, commented out for RAP testing. ~JJB 20120827
		// ColumnViewerToolTipSupport.enableFor(tableComponentViewer,
		// ToolTip.NO_RECREATE);

		ColumnViewerToolTipSupport.enableFor(tableComponentViewer);

		// Add columns - use the rowTemplate and names accordingly
		for (int i = 0; i < tableComponent.getColumnNames().size(); i++) {
			// Create a column for the table
			TableViewerColumn column = new TableViewerColumn(
					tableComponentViewer, SWT.CENTER);
			// Set the text for the column name and the column tool tip from the
			// description in ICEObject.
			column.getColumn().setText(tableComponent.getColumnNames().get(i));
			column.getColumn().setToolTipText(
					tableComponent.getRowTemplate().get(i).getDescription());
			// This is used later for setting up the columnProperties in
			// TableViewer.
			colNames[i] = tableComponent.getColumnNames().get(i);

			// Add the LabelProvider for each cell
			column.setLabelProvider(new ICECellLabelProvider(i));
		}

		// Setup the content provider and label provider
		attachContentProvider();

		// Setup editors - based on columns properties
		tableComponentViewer.setColumnProperties(colNames);

		// Setup cell editors - based on rowTemplate
		// whether Discrete, Continuous, or Undefined
		CellEditor[] editorsArray = null;
		editorsArray = new CellEditor[tableComponent.getRowTemplate().size()];

		// Iterate over lists and add editors - text or menu editor
		for (int i = 0; i < tableComponent.getRowTemplate().size(); i++) {
			// Menu editor if the allowedvaluetypes are a discrete list
			if (tableComponent.getRowTemplate().get(i).getValueType() == AllowedValueType.Discrete) {

				// Setup the label, content, and input providers for
				// the combobox with the editors
				editorsArray[i] = new ComboBoxViewerCellEditor(
						tableComponentViewer.getTable(), SWT.READ_ONLY);
				((ComboBoxViewerCellEditor) editorsArray[i])
						.setLabelProvider(new LabelProvider());
				((ComboBoxViewerCellEditor) editorsArray[i])
						.setContentProvider(new ArrayContentProvider());
				((ComboBoxViewerCellEditor) editorsArray[i])
						.setInput(tableComponent.getRowTemplate().get(i)
								.getAllowedValues());

				// Text Editor - if type is Continuous or Undefined.
			} else {
				// Create a text editor
				editorsArray[i] = new TextCellEditor(
						tableComponentViewer.getTable());
			}
		}

		// Attach editors
		tableComponentViewer.setCellEditors(editorsArray);

		// set cell editors to TableViewer
		attachCellModifiers();

		// These rows will be used to wrap the arraylist of tableComponent
		// entries
		// into the RowWrapper class.
		RowWrapper[] rows = new RowWrapper[tableComponent.getRowIds().size()];

		// add rows to list - using RowWrapper class
		for (int i = 0; i < tableComponent.getRowIds().size(); i++) {
			rows[i] = (new RowWrapper(tableComponent.getRow(i)));
		}

		// Set the rows in TableViewer.
		tableComponentViewer.setInput(rows);

		// Set header and lines visible.
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Sets the GridData to keep the table on the left and occupy an
		// appropriate amount of the SectionPart
		GridData gridData1 = new GridData();
		// Sets the table to expand to fit available space vertically and
		// horizontally
		gridData1.horizontalAlignment = SWT.FILL;
		gridData1.verticalAlignment = SWT.FILL;
		// Sets the table to occupy its original cell and the one beneath it to
		// allow the "+,-" buttons to sit neatly on its right side
		gridData1.verticalSpan = 2;
		// Allows the table to occupy all horizontal space in the sectionPart
		// available; i.e. not taken by "+,-" buttons and dependent on size of
		// the window
		gridData1.grabExcessHorizontalSpace = true;
		// Sets an initial height that is unchangeable for the table
		gridData1.heightHint = 400;
		// Applies this instance of GridData to the table
		table.setLayoutData(gridData1);

		// Add a listener to the table to catch when a row is selected and
		// update the TableComponent.
		tableComponentViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						// Create a list to hold the selected row
						ArrayList<Integer> selectedRowList = new ArrayList<Integer>();
						// Update the selected row
						selectedRowList.add(tableComponentViewer.getTable()
								.getSelectionIndex());
						tableComponent.setSelectedRows(selectedRowList);
						// Log some output for debugging purposes
						if (System.getProperty("DebugICE") != null) {
							System.out.println("ICETableComponentSectionPart "
									+ "Message: " + "Selected rows = "
									+ tableComponent.getSelectedRows());
						}
					}
				});

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation creates the content provider that sets up the rows of the
	 * TableViewer.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void attachContentProvider() {
		// begin-user-code

		// If the tableComponentViewer has not been initialized, return
		if (this.tableComponentViewer == null) {
			return;
		}
		// Override the functionality of the content provider. Only one method
		// is implemented here. This is for marshalling objects into
		// RowWrappers.
		this.tableComponentViewer
				.setContentProvider(new IStructuredContentProvider() {

					@Override
					public void dispose() {
						// TODO Auto-generated method stub
					}

					@Override
					public void inputChanged(Viewer arg0, Object arg1,
							Object arg2) {
						// TODO Auto-generated method stub
					}

					@Override
					public Object[] getElements(Object element) {

						// Return the list of rowWrapper elements from an Object
						return (RowWrapper[]) element;
					}
				});

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation attaches the CellModifiers to the TableViewer so that
	 * Cells may be edited.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void attachCellModifiers() {
		// begin-user-code
		// If the tableComponentViewer has not been initialized, return
		if (this.tableComponentViewer == null) {
			return;
		}
		tableComponentViewer.setCellModifier(new ICellModifier() {

			// We are allowing all cells to be edited.
			// Case statements should be made in the data structures themselves
			// manipulating the allowedValueTypes or conditional modifiers for
			// determining whether or not a cell can be edited based on a set
			// of conditions.
			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}

			// Return the value of an object (cast and return value of entry)
			@Override
			public Object getValue(Object element, String property) {
				int counter = -1;
				// Iterate over the list to grab the location of the column.
				for (int i = 0; i < tableComponentViewer.getColumnProperties().length; i++) {
					if (property.equals(tableComponentViewer
							.getColumnProperties()[i])) {
						// Get the column
						counter = i;
					}
				}

				// Return value of entry given the column (counter)
				return ((RowWrapper) element).getRowWrapper().get(counter)
						.getValue();

			}

			// Modify the value of item in the table and in the data structure
			@Override
			public void modify(Object element, String property, Object value) {

				// Iterate over the list and get the location of the column in
				// entries
				int counter = -1;
				for (int i = 0; i < tableComponentViewer.getColumnProperties().length; i++) {
					if (property.equals(tableComponentViewer
							.getColumnProperties()[i])) {
						// Get the column, set to counter
						counter = i;
					}
				}

				// Convert to tableitem from the element
				TableItem item = (TableItem) element;
				// Return if the value is null
				if (value == null) {
					return;
				}
				// Get the data structure from the TableItem
				// to a RowWrapper.
				RowWrapper row = (RowWrapper) item.getData();

				// Make sure the element is not null
				if (row != null) {
					// Set the value in the data structure
					boolean success = row.getRowWrapper().get(counter)
							.setValue((String) value);
					if (System.getProperty("DebugICE") != null) {
						System.out.println("ICETableComponentSectionPart "
								+ "Message: " + "Setting value returned "
								+ success);
					}

					// FIXME FUTURE GREG WORK, ADD ERRORS AND FONT CHANGE...

					// Set to dirty, since the element was changed
					// Make the editor dirty
					editor.setDirty(true);
				}

				// Set the text in the column - handles appropriate values as
				// needed
				item.setText(row.getRowWrapper().get(counter).getValue());

				// Refresh changes in the table
				tableComponentViewer.refresh();
			}
		});

		return;
		// end-user-code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.AbstractFormPart#refresh()
	 */
	public void refresh() {
		super.refresh();

		// Pack the columns to fit the Table's content.
		packTableColumns();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IUpdateableListener#update(Component component)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void update(IUpdateable component) {
		// begin-user-code

		// Updating the view since the underlying data has changed

		// We must use the UI Thread
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				// Update the name and description of the SectionPart
				getSection().setText(tableComponent.getName());
				getSection().setDescription(tableComponent.getDescription());

				// Get the old data content
				RowWrapper[] rows = (RowWrapper[]) tableComponentViewer
						.getInput();

				// Compare the rows to make sure they are the same
				boolean rowStatus = true;
				if (rows.length == tableComponent.numberOfRows()) {
					for (int j = 0; j < rows.length; j++) {
						List<Entry> rowList = rows[j].list;

						// If the number of entries are not the same, needs to
						// be fixed.
						if (rowList.size() != tableComponent.getRow(j).size()) {
							rowStatus = false;
							break;
						}

						// Check the entries
						for (int k = 0; k < rowList.size(); k++) {
							// If the entries are not equal, refresh
							if (rowList.get(k).equals(
									tableComponent.getRow(j).get(k))) {
								rowStatus = false;
								break;
							}
						}
					}
				}

				// If the row length is different from the tablecomponent,
				// then we must update the tableViewer content
				if (rows.length != tableComponent.numberOfRows() || !rowStatus) {
					// Reset input for tableViewer
					RowWrapper[] rowWrappers = new RowWrapper[tableComponent
							.getRowIds().size()];
					// add rows to list - using RowWrapper class
					for (int i = 0; i < rowWrappers.length; i++) {
						rowWrappers[i] = (new RowWrapper(tableComponent
								.getRow(i)));
					}

					// Reset the input and re-pack the table.
					tableComponentViewer.setInput(rowWrappers);
					packTableColumns();

					// Mark stale to get a refresh
					markStale();
				}

			}
		});

		// end-user-code
	}

	/**
	 * Packs all columns in the {@link #tableComponentViewer}. This can be used
	 * to resize each column to fit the maximum width of its content.
	 */
	private void packTableColumns() {

		if (tableComponentViewer != null) {
			Table table = tableComponentViewer.getTable();
			for (TableColumn column : table.getColumns()) {
				column.pack();
			}
		}

		return;
	}
}