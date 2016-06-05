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
import java.util.Arrays;
import java.util.List;

import org.eclipse.january.form.AllowedValueType;
import org.eclipse.january.form.IUpdateable;
import org.eclipse.january.form.IUpdateableListener;
import org.eclipse.january.form.MatrixComponent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class is a subclass of SectionPart from org.eclipse.ui.forms that
 * renders, updates and monitors an ICE MatrixComponent that is part of a
 * ICEForm. The ICESectionPart takes place in the IManagedForm lifecycle and
 * receives is refreshed dynamically if the underlying ICEMatrixComponent has
 * been updated (i.e. - "gone stale" in the Eclipse parlance). The Java source
 * code for this class contains a private hashmap that is not represented in the
 * model because Jay can not figure out how to show a java.util.hashmap in RSA.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ICEMatrixComponentSectionPart extends SectionPart
		implements IUpdateableListener {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ICEMatrixComponentSectionPart.class);

	/**
	 * <p>
	 * This attribute is a reference to an ICE MatrixComponent that stores the
	 * data that should be displayed by this SectionPart. The MatrixComponent
	 * will also update the ICESectionPart when its state changes (i.e. -
	 * becomes "stale" in the Eclipse parlance).
	 * </p>
	 * 
	 */
	protected MatrixComponent matrixComponent;

	/**
	 * <p>
	 * The TableViewer that is used rendered by the SectionPart.
	 * </p>
	 * 
	 */
	protected TableViewer matrixViewer;

	/**
	 * <p>
	 * The Eclipse Managed Form in which the SectionPart will be rendered.
	 * </p>
	 * 
	 */
	protected IManagedForm parentForm;

	/**
	 * <p>
	 * The Composite in which the TableViewer is drawn.
	 * </p>
	 * 
	 */
	protected Composite sectionClient;

	/**
	 * <p>
	 * Class used to pass information for the rows in TableComponent to
	 * TableViewer
	 * </p>
	 * 
	 */
	private class RowWrapper {

		/**
		 * <p>
		 * Holds an ArrayList of Entries in the TableComponent for TableViewer.
		 * </p>
		 * 
		 */
		private List<Double> list;

		/**
		 * <p>
		 * </p>
		 * 
		 */
		private int rowIndex;

		/**
		 * <p>
		 * The Constructor, injects the ArrayList of Entries at this row.
		 * </p>
		 * 
		 * @param list
		 *            <p>
		 *            The list of Entries that make up this RowWrapper.
		 *            </p>
		 * 
		 */
		public RowWrapper(ArrayList<Double> row, int index) {
			this.list = row;
			rowIndex = index;
		}

		/**
		 * <p>
		 * Return the ArrayList of Entries for this row.
		 * </p>
		 * 
		 */
		public List<Double> getRowWrapper() {
			return list;
		}

		/**
		 * <p>
		 * </p>
		 * 
		 */
		public void add() {
			list.add(0.0);
		}

		/**
		 * <p>
		 * </p>
		 * 
		 */
		public void add(Double value) {
			list.add(value);
		}

		/**
		 * <p>
		 * </p>
		 * 
		 */
		public void remove() {
			list.remove(list.size() - 1);
		}

		/**
		 * <p>
		 * </p>
		 * 
		 */
		public int getRowIndex() {
			return rowIndex;
		}

	}

	/**
	 * <p>
	 * ICECellLabelProvider is a subclass of CellLabelProvider that keeps track
	 * of the current column index. This enables the correct display of tool
	 * tips that correspond to cells in the table
	 * </p>
	 * 
	 */
	private class ICECellLabelProvider extends CellLabelProvider {

		//
		/**
		 * <p>
		 * The index of the table column this CellLabelProvider corresponds to.
		 * </p>
		 * 
		 */
		private int tableColumn;

		/**
		 * <p>
		 * The Constructor, used to inject the current column index
		 * </p>
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
		 * <p>
		 * Update is called whenever data is entered into the cell. Current
		 * implementation simply sets the text as what's currently the value on
		 * the Entry
		 * </p>
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
					.get(tableColumn).toString());
		}

		/**
		 * <p>
		 * Return the tool tip when a MouseHover event occurs. Current
		 * implementation displays the proper Entry's description attribute.
		 * </p>
		 * 
		 * @param element
		 *            <p>
		 *            RowWrapper of the current row in the TableViewer
		 *            </p>
		 * 
		 */
		@Override
		public String getToolTipText(Object element) {
			return "Hello";
		}

		/**
		 * <p>
		 * Set the tool tip shift from the Mouse pointer.
		 * </p>
		 * 
		 * @param object
		 * 
		 */
		@Override
		public Point getToolTipShift(Object object) {
			return new Point(5, 5);
		}

		/**
		 * <p>
		 * Set the time it should take to pop up the tool tip.
		 * </p>
		 * 
		 * @param object
		 * 
		 */
		@Override
		public int getToolTipDisplayDelayTime(Object object) {
			return 100;
		}

		/**
		 * <p>
		 * Set the time the tool tip should stay displayed.
		 * </p>
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
	 * <p>
	 * ICECellModifier is a realization of the ICellModifier interface that
	 * enables the customization of how Cell's in a TableViewer can be modified,
	 * and how the underlying JFace Viewer Model changes upon modification.
	 * </p>
	 * 
	 */
	private class ICECellModifier implements ICellModifier {
		/**
		 * <p>
		 * Checks whether the given property of the given element can be
		 * modified.
		 * </p>
		 * 
		 * @param object
		 * 
		 */
		@Override
		public boolean canModify(Object element, String property) {
			return true;
		}

		/**
		 * <p>
		 * Returns the value for the given property of the given element.
		 * </p>
		 * 
		 * @param object
		 * 
		 */
		@Override
		public Object getValue(Object element, String property) {
			int counter = -1;

			// Iterate over the list to grab the location of the column.
			for (int i = 0; i < matrixViewer
					.getColumnProperties().length; i++) {
				if (property.equals(matrixViewer.getColumnProperties()[i])) {
					// Get the column
					counter = i;
				}
			}
			// Return value of entry given the column (counter)
			return ((RowWrapper) element).getRowWrapper().get(counter)
					.toString();
		}

		/**
		 * <p>
		 * Modifies the value for the given property of the given element.
		 * </p>
		 * 
		 * @param object
		 * 
		 */
		@Override
		public void modify(Object element, String property, Object value) {
			// Get the column index of the current Cell
			int counter = -1;
			for (int i = 0; i < matrixViewer
					.getColumnProperties().length; i++) {
				if (property.equals(matrixViewer.getColumnProperties()[i])) {
					// Get the column, set to counter
					counter = i;
				}
			}

			// The input Object element is a TableItem, cast it so we can use it
			TableItem item = (TableItem) element;

			// Return if the value is null
			if (value == null) {
				return;
			}
			// Get the data structure from the TableItem
			// to a RowWrapper.
			RowWrapper row = (RowWrapper) item.getData();

			Double input = 0.0;
			// Make sure the element is not null
			if (row != null) {
				// Set the value in the data structure

				try {
					input = Double.parseDouble((String) value);
					matrixComponent.setElementValue(row.getRowIndex(), counter,
							input);
				} catch (NumberFormatException ex) {
					logger.info("Invalid Matrix Element.");
				}

				// FIXME FUTURE GREG WORK, ADD ERRORS AND FONT CHANGE...

			}

			// Set the value on the RowWrapper instance
			row.getRowWrapper().set(counter, matrixComponent
					.getElementValue(row.getRowIndex(), counter));

			// Set the Cell's text and refresh the TableViewer
			item.setText(row.getRowWrapper().get(counter).toString());
			matrixViewer.refresh();
		}
	}

	/**
	 * <p>
	 * The Constructor
	 * </p>
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
	 */
	public ICEMatrixComponentSectionPart(Section section, FormEditor formEditor,
			IManagedForm managedForm) {
		super(section);

		// Set the ManagedForm
		if (managedForm != null) {
			parentForm = managedForm;
		} else {
			throw new RuntimeException(
					"ManagedForm in ICEMatrixComponentSectionPart constructor cannot be null.");
		}
	}

	/**
	 * <p>
	 * This operation sets up the TableViewer.
	 * </p>
	 * 
	 */
	protected void setupTableViewer() {
		// Make sure the client is initialized
		if (sectionClient == null) {
			return;
		}
		// Instantiate a new JFace TableViewer with the sectionClient as its
		// parent
		matrixViewer = new TableViewer(sectionClient,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		// Enable tooltip support for the individual ColumnViewers
		// Original version, commented out for RAP testing. ~JJB 20120827
		// ColumnViewerToolTipSupport.enableFor(matrixViewer,
		// ToolTip.NO_RECREATE);
		ColumnViewerToolTipSupport.enableFor(matrixViewer);

		// Create an array of the Column names
		String[] colNames = new String[matrixComponent.numberOfColumns()];

		// Create a numberOfColumns() TableViewerColumns, setting its title and
		// width
		for (int i = 0; i < matrixComponent.numberOfColumns(); i++) {
			// Create a column
			TableViewerColumn column = new TableViewerColumn(matrixViewer,
					SWT.CENTER);
			// Set its column title
			column.getColumn().setText("Column " + (i + 1));
			colNames[i] = String.valueOf(i);
			// Tell the column to set its own width according to the space
			// available
			column.getColumn().pack();
			// Set this Column's LabelProvider
			column.setLabelProvider(new ICECellLabelProvider(i));
		}

		// Attach a ContentProvider for this JFace Viewer
		attachContentProvider();

		// Set the Column Properties as the previously constructed column names
		matrixViewer.setColumnProperties(colNames);

		// Set up an array of CellEditors, a Combo cell editor if the
		// Elements are of a Discrete set (like an Adjacency Matrix), and a
		// regular Text Cell Editor if they are not
		CellEditor[] editors = new CellEditor[matrixComponent
				.numberOfColumns()];
		// Also create a String array list to handle conversion of
		// MatrixComponents AllowedValues from Double to String (needed by JFace
		// Viewer Model)
		List<String> stringArray = new ArrayList<String>();

		// For each Column, set the appropriate CellEditor
		for (int i = 0; i < matrixComponent.numberOfColumns(); i++) {
			if (matrixComponent
					.getAllowedValueType() == AllowedValueType.Discrete) {
				editors[i] = new ComboBoxViewerCellEditor(
						matrixViewer.getTable(), SWT.READ_ONLY);
				((ComboBoxViewerCellEditor) editors[i])
						.setLabelProvider(new LabelProvider());
				((ComboBoxViewerCellEditor) editors[i])
						.setContentProvider(new ArrayContentProvider());
				for (Double d : matrixComponent.getAllowedValues()) {
					stringArray.add(d.toString());
				}
				((ComboBoxViewerCellEditor) editors[i]).setInput(stringArray);
				stringArray.clear();
			} else {
				editors[i] = new TextCellEditor(matrixViewer.getTable());
			}
		}

		// Set the JFace TableViewer's Cell Editors
		matrixViewer.setCellEditors(editors);

		// Attach Cell Modifiers
		attachCellModifiers();

		// JFace Viewer Model requires an input Model.
		// Set it up here
		RowWrapper[] rows = new RowWrapper[matrixComponent.numberOfRows()];
		for (int i = 0; i < matrixComponent.numberOfRows(); i++) {
			rows[i] = new RowWrapper(matrixComponent.getRow(i), i);
		}

		// Set that input
		matrixViewer.setInput(rows);

		// Tell the TableViewer to show the column headers and the
		// Grid lines
		matrixViewer.getTable().setHeaderVisible(true);
		matrixViewer.getTable().setLinesVisible(true);

		// Sets the GridData to keep the table on the left and occupy an
		// appropriate amount of the SectionPart
		GridData tableViewerGridData = new GridData();
		// Sets the table to expand to fit available space vertically and
		// horizontally
		tableViewerGridData.horizontalAlignment = SWT.FILL;
		tableViewerGridData.verticalAlignment = SWT.FILL;
		// Sets the table to occupy its original cell and the one beneath it to
		// allow the "+,-" buttons to sit neatly on its right side
		tableViewerGridData.verticalSpan = 2;
		// Allows the table to occupy all horizontal space in the sectionPart
		// available; i.e. not taken by "+,-" buttons and dependent on size of
		// the window
		tableViewerGridData.grabExcessHorizontalSpace = true;
		// Sets an initial height that is unchangeable for the table
		tableViewerGridData.heightHint = 400;
		// Applies this instance of GridData to the table
		matrixViewer.getTable().setLayoutData(tableViewerGridData);

	}

	/**
	 * <p>
	 * This operation sets the MatrixComponent that should be rendered, updated
	 * and monitored by the ICESectionPart.
	 * </p>
	 * 
	 * @param matrix
	 *            <p>
	 *            The TableComponent that should be rendered by the SectionPart.
	 *            </p>
	 */
	public void setMatrixComponent(MatrixComponent matrix) {
		if (matrix == null) {
			return;
		}
		// If not null, set this SectionParts reference to
		// the MatrixComponent
		matrixComponent = matrix;
		matrixComponent.register(this);

	}

	/**
	 * <p>
	 * This operation retrieves the MatrixComponent that is currently rendered,
	 * updated and monitored by the ICESectionPart.
	 * </p>
	 * 
	 * @return
	 * 		<p>
	 *         The TableComponent that is rendered by the SectionPart.
	 *         </p>
	 */
	public MatrixComponent getMatrixComponent() {
		return matrixComponent;
	}

	/**
	 * <p>
	 * This operation reads the MatrixComponent assigned to this SectionPart and
	 * renders the view of that data for the user.
	 * </p>
	 * 
	 */
	public void renderSection() {
		// Get this SectionPart's reference to the
		// underlying Section
		Section section = getSection();

		// Set the description and title for this SectionPart
		section.setDescription(matrixComponent.getDescription());
		section.setText(matrixComponent.getName());

		// Set this SectionParts reference to the underlying SWT Composite
		sectionClient = parentForm.getToolkit().createComposite(section);

		// Set the Composite's layout
		sectionClient.setLayout(new GridLayout(2, false));

		// Construct the JFace TableViewer and its controlling buttons
		setupTableViewer();
		setupButtons();

		// Add an ExpansionListener to this Section
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				parentForm.reflow(true);
			}
		});

		// Set the Composite on this Section
		section.setClient(sectionClient);

		return;
	}

	/**
	 * <p>
	 * This operation creates the content provider that sets up the rows of the
	 * TableViewer.
	 * </p>
	 * 
	 */
	protected void attachContentProvider() {
		// Make sure we have a valid TableViewer
		if (matrixViewer == null) {
			return;
		}
		// Set the ContentProvider as a realization of the
		// IStructuredContentProvider
		// Interface. This provides input to the JFace Viewer individual Arrays
		// representing
		// rows in a table, or items in a list or tree.
		matrixViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return (RowWrapper[]) inputElement;
			}
		});
	}

	/**
	 * <p>
	 * This operation attaches the CellModifiers to the TableViewer so that
	 * Cells may be edited.
	 * </p>
	 * 
	 */
	protected void attachCellModifiers() {
		// Make sure we have a valid TableViewer
		if (matrixViewer == null) {
			return;
		}
		// Set the TableViewer's Cell Modifier realization
		matrixViewer.setCellModifier(new ICECellModifier());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org
	 * .eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!matrixViewer.getTable().isDisposed()) {
					getSection().setText(matrixComponent.getName());
					getSection()
							.setDescription(matrixComponent.getDescription());

					// Get the old input
					RowWrapper[] rows = (RowWrapper[]) matrixViewer.getInput();
					RowWrapper[] newRows = rows;
					int nCols = rows[0].getRowWrapper().size();

					// Check if we need to add or remove more columns
					if (nCols != matrixComponent.numberOfColumns()) {
						if (nCols < matrixComponent.numberOfColumns()) {
							// Add Columns
							for (int i = nCols + 1; i <= matrixComponent
									.numberOfColumns(); i++) {

								List<CellEditor> editorList = new ArrayList<CellEditor>(
										Arrays.asList(
												matrixViewer.getCellEditors()));
								// Create an Array to hold default Strings for
								// the case when this MatrixComponent has
								// Discrete AllowedValueType
								List<String> stringArray = new ArrayList<String>();
								// If this Matrix is Discrete, set up Combo Cell
								// Editors
								if (matrixComponent
										.getAllowedValueType() == AllowedValueType.Discrete) {
									CellEditor ed = new ComboBoxViewerCellEditor(
											matrixViewer.getTable(),
											SWT.READ_ONLY);
									((ComboBoxViewerCellEditor) ed)
											.setLabelProvider(
													new LabelProvider());
									((ComboBoxViewerCellEditor) ed)
											.setContentProvider(
													new ArrayContentProvider());
									// Here we need to convert the Doubles to
									// Strings, because the underlying JFace
									// Viewer model is expecting Strings.
									for (Double d : matrixComponent
											.getAllowedValues()) {
										stringArray.add(d.toString());
									}
									// Set the Input for the Discrete Combo Box
									// Editor
									((ComboBoxViewerCellEditor) ed)
											.setInput(stringArray);
									stringArray.clear();
									// Add the new Cell Editor to the list
									editorList.add(ed);
								} else {
									// If the elements do not have to be
									// Discrete, just add a new TextCellEditor
									editorList.add(new TextCellEditor(
											matrixViewer.getTable()));
								}
								// Create a primitive Array of these new
								// CellEditors
								CellEditor[] editors = new CellEditor[editorList
										.size()];
								editorList.toArray(editors);
								// Set the CellEditors
								matrixViewer.setCellEditors(editors);

								// We must do the same thing with the Column
								// Properties
								List<Object> columnPropertyList = new ArrayList<Object>(
										Arrays.asList(matrixViewer
												.getColumnProperties()));
								columnPropertyList.add(String.valueOf(
										matrixComponent.numberOfColumns() - 1));
								String[] columnProperties = new String[columnPropertyList
										.size()];
								columnPropertyList.toArray(columnProperties);
								// Set the Column Properties
								matrixViewer
										.setColumnProperties(columnProperties);

								// Add the new Column, with an Editor set
								// accordingly above
								TableViewerColumn column = new TableViewerColumn(
										matrixViewer, SWT.CENTER);
								column.getColumn().setText("Column " + i);

								// Set the ICELableProvider
								column.setLabelProvider(
										new ICECellLabelProvider(i - 1));
							}
						} else {
							// Just delete the Columns
							for (int i = nCols - 1; i >= matrixComponent
									.numberOfColumns(); i--) {
								matrixViewer.getTable().getColumn(i).dispose();
							}
						}
					}

					// Set a new input if need be. This is for when we need to
					// add or remove Rows
					if (rows.length != matrixComponent.numberOfRows()
							|| rows[0].getRowWrapper().size() != matrixComponent
									.numberOfColumns()) {
						logger.info("Altering Rows: "
								+ matrixComponent.numberOfRows() + " "
								+ matrixComponent.numberOfColumns());
						newRows = new RowWrapper[matrixComponent
								.numberOfRows()];
						for (int i = 0; i < matrixComponent
								.numberOfRows(); i++) {
							logger.info("Row: ");
							for (int j = 0; j < matrixComponent
									.numberOfColumns(); j++) {
								logger.info(
										matrixComponent.getRow(i).get(j) + " ");
							}
							logger.info("");
							newRows[i] = new RowWrapper(
									matrixComponent.getRow(i), i);
						}

					}

					// Resize the column widths
					int columnWidth = matrixViewer.getTable().getSize().x
							/ matrixViewer.getTable().getColumnCount();
					for (TableColumn col : matrixViewer.getTable()
							.getColumns()) {
						col.setWidth(columnWidth);
					}

					// Make sure the individual matrix elements are correct
					for (int i = 0; i < newRows.length; i++) {
						for (int j = 0; j < matrixComponent
								.numberOfColumns(); j++) {
							newRows[i].getRowWrapper().set(j,
									matrixComponent.getElementValue(i, j));
						}
					}

					// Set the new input
					matrixViewer.setInput(newRows);

					// Refresh the Viewer
					matrixViewer.refresh();

				}
			}
		});
	}

	/**
	 * <p>
	 * This operations sets up the add and delete row buttons for the
	 * TableViewer.
	 * </p>
	 * 
	 */
	protected void setupButtons() {
		if (sectionClient == null) {
			return;
		}
		// Distinguish between Matrices that are resizable or not,
		// and square or not. If Not Resizable, we don't need to add
		// any buttons. If square, we only need to add one set of
		// + and - buttons. If not square, add two sets of buttons.
		if (matrixComponent.isResizable()) {
			// Check it the matrix is square
			if (matrixComponent.isSquare()) {
				// Create the Add Row and Column button (both because matrix is
				// square)
				Button addButton = new Button(sectionClient, 0);
				addButton.setText("+");

				// Set a selection listener
				addButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (matrixComponent != null && matrixViewer != null) {
							// Add a Row to the MatrixComponent. Since it is
							// square, this will also add a Column
							matrixComponent.addRow();

							// We need to get the old set of CellEditors and add
							// to it
							List<CellEditor> cellEditorList = new ArrayList<CellEditor>(
									Arrays.asList(
											matrixViewer.getCellEditors()));
							// Create an Array to hold default Strings for the
							// case when this MatrixComponent has Discrete
							// AllowedValueType
							List<String> stringArray = new ArrayList<String>();
							// If this Matrix is Discrete, set up Combo Cell
							// Editors
							if (matrixComponent
									.getAllowedValueType() == AllowedValueType.Discrete) {
								CellEditor ed = new ComboBoxViewerCellEditor(
										matrixViewer.getTable(), SWT.READ_ONLY);
								((ComboBoxViewerCellEditor) ed)
										.setLabelProvider(new LabelProvider());
								((ComboBoxViewerCellEditor) ed)
										.setContentProvider(
												new ArrayContentProvider());
								// Here we need to convert the Doubles to
								// Strings, because the underlying JFace Viewer
								// model is expecting Strings.
								for (Double d : matrixComponent
										.getAllowedValues()) {
									stringArray.add(d.toString());
								}
								// Set the Input for the Discrete Combo Box
								// Editor
								((ComboBoxViewerCellEditor) ed)
										.setInput(stringArray);
								stringArray.clear();
								// Add the new Cell Editor to the list
								cellEditorList.add(ed);
							} else {
								// If the elements do not have to be Discrete,
								// just add a new TextCellEditor
								cellEditorList.add(new TextCellEditor(
										matrixViewer.getTable()));
							}
							// Create a primitive Array of these new CellEditors
							CellEditor[] editors = new CellEditor[cellEditorList
									.size()];
							cellEditorList.toArray(editors);
							// Set the CellEditors
							matrixViewer.setCellEditors(editors);

							// We must do the same thing with the Column
							// Properties
							List<Object> columnPropertyList = new ArrayList<Object>(
									Arrays.asList(matrixViewer
											.getColumnProperties()));
							columnPropertyList.add(String.valueOf(
									matrixComponent.numberOfColumns() - 1));
							String[] columnProperties = new String[columnPropertyList
									.size()];
							columnPropertyList.toArray(columnProperties);
							// Set the Column Properties
							matrixViewer.setColumnProperties(columnProperties);

							// Since we've also added a column, create a new
							// TableViewerColumn
							TableViewerColumn column = new TableViewerColumn(
									matrixViewer, SWT.CENTER);
							column.getColumn().setText("Column "
									+ matrixComponent.numberOfColumns());

							column.setLabelProvider(new ICECellLabelProvider(
									matrixComponent.numberOfColumns() - 1));

							// Create a new Input to set in the JFace Viewer
							// model that is taken from the old input
							RowWrapper[] rows = (RowWrapper[]) matrixViewer
									.getInput();
							RowWrapper[] newRows = new RowWrapper[rows.length
									+ 1];

							// Set the new Input
							for (int i = 0; i < matrixComponent
									.numberOfRows(); i++) {
								if (i == (matrixComponent.numberOfRows() - 1)) {
									// If this is the last iteration, add a new
									// RowWrapper
									newRows[i] = new RowWrapper(
											matrixComponent.getRow(i), i);
								} else {
									// Otherwise, add a column entry to each row
									if (matrixComponent
											.getAllowedValueType() == AllowedValueType.Discrete) {
										rows[i].add(matrixComponent
												.getAllowedValues().get(0));
									} else {
										rows[i].add();
									}
									newRows[i] = rows[i];
								}
							}

							// Set the new Input
							matrixViewer.setInput(newRows);

							// Resize the column widths
							int columnWidth = matrixViewer.getTable()
									.getSize().x
									/ matrixViewer.getTable().getColumnCount();
							for (TableColumn col : matrixViewer.getTable()
									.getColumns()) {
								col.setWidth(columnWidth);
							}

							// Refresh the Table
							matrixViewer.refresh();
						}
					}
				});

				// Sets the GridData to keep the "+" button on the right and
				// next the "-" button
				GridData plusButtonSquare = new GridData();
				// Sets the position of the button in its cell upward and
				// rightward
				plusButtonSquare.horizontalAlignment = SWT.END;
				plusButtonSquare.verticalAlignment = SWT.END;
				// Sets an initial height and width that are unchangeable for
				// the button
				plusButtonSquare.widthHint = 30;
				plusButtonSquare.heightHint = 30;
				// Applies this instance of GridData to the addButton
				addButton.setLayoutData(plusButtonSquare);

				// Add a Delete Row/Column Button
				Button deleteButton = new Button(sectionClient, 0);
				deleteButton.setText("-");

				// Add a selection listener
				deleteButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (matrixComponent != null && matrixViewer != null
								&& matrixComponent.numberOfRows() > 1) {
							// Make sure we don't delete a row or column if this
							// is a 1x1 matrix Delete a Row from the Matrix
							// Component This also removes a Column since this
							// matrix is square
							matrixComponent.deleteRow();

							// We need to get the old set of CellEditors and
							// add to it
							List<CellEditor> cellEditorList = new ArrayList<CellEditor>(
									Arrays.asList(
											matrixViewer.getCellEditors()));
							// Create an Array to hold default Strings for
							// the case when this MatrixComponent has Discrete
							// AllowedValueType
							List<String> stringArray = new ArrayList<String>();
							// If this Matrix is Discrete, set up Combo Cell
							// Editors
							if (matrixComponent
									.getAllowedValueType() == AllowedValueType.Discrete) {
								CellEditor ed = new ComboBoxViewerCellEditor(
										matrixViewer.getTable(), SWT.READ_ONLY);
								((ComboBoxViewerCellEditor) ed)
										.setLabelProvider(new LabelProvider());
								((ComboBoxViewerCellEditor) ed)
										.setContentProvider(
												new ArrayContentProvider());
								// Here we need to convert the Doubles to
								// Strings, because the underlying JFace Viewer
								// model is expecting Strings.
								for (Double d : matrixComponent
										.getAllowedValues()) {
									stringArray.add(d.toString());
								}
								// Set the Input for the Discrete Combo Box
								// Editor
								((ComboBoxViewerCellEditor) ed)
										.setInput(stringArray);
								stringArray.clear();
								// Add the new Cell Editor to the list
								cellEditorList.add(ed);
							} else {
								// If the elements do not have to be Discrete,
								// just add a new TextCellEditor
								cellEditorList.add(new TextCellEditor(
										matrixViewer.getTable()));
							}
							// Create a primitive Array of these new
							// CellEditors
							CellEditor[] editors = new CellEditor[cellEditorList
									.size()];
							cellEditorList.toArray(editors);
							// Set the CellEditors
							matrixViewer.setCellEditors(editors);

							// We must do the same thing with the Column
							// Properties
							List<Object> columnPropertyList = new ArrayList<Object>(
									Arrays.asList(matrixViewer
											.getColumnProperties()));
							columnPropertyList.remove(String.valueOf(
									matrixComponent.numberOfColumns()));
							String[] columnProperties = new String[columnPropertyList
									.size()];
							columnPropertyList.toArray(columnProperties);
							// Set the Column Properties
							matrixViewer.setColumnProperties(columnProperties);

							// Remove the old column
							matrixViewer
									.getTable().getColumn(matrixViewer
											.getTable().getColumnCount() - 1)
									.dispose();

							// Set up a new Input for the JFace Viewer Model
							RowWrapper[] rows = (RowWrapper[]) matrixViewer
									.getInput();
							RowWrapper[] newRows = new RowWrapper[rows.length
									- 1];

							for (int i = 0; i < matrixComponent
									.numberOfRows(); i++) {
								// Remove a column entry from the row wrappers
								rows[i].remove();
								newRows[i] = rows[i];
							}

							// Set the Input
							matrixViewer.setInput(newRows);

							// Resize the column widths
							int columnWidth = matrixViewer.getTable()
									.getSize().x
									/ matrixViewer.getTable().getColumnCount();
							for (TableColumn col : matrixViewer.getTable()
									.getColumns()) {
								col.setWidth(columnWidth);
							}

							// Refresh the Viewer
							matrixViewer.refresh();

						}
					}
				});

				// Sets the GridData to keep the "-" button on the right and
				// next the "+" button
				GridData minusSquareData = new GridData();
				// Sets the position of the button in its cell downward and
				// rightward
				minusSquareData.horizontalAlignment = SWT.END;
				minusSquareData.verticalAlignment = SWT.BEGINNING;
				// Sets an initial height and width that are unchangeable for
				// the button
				minusSquareData.widthHint = 30;
				minusSquareData.heightHint = 30;
				// Applies this instance of GridData to the deleteButton
				deleteButton.setLayoutData(minusSquareData);

			} else {
				// This is to add Buttons for a non-Square matrix, Two sets of +
				// and - buttons
				Button addNonSquareButton = new Button(sectionClient, 0);
				addNonSquareButton.setText("+");

				// Add the Selection Listener
				addNonSquareButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (matrixComponent != null && matrixViewer != null) {
							// Add just a single row to this
							// MatrixComponent
							matrixComponent.addRow();

							// Create a new input from the old input
							RowWrapper[] rows = (RowWrapper[]) matrixViewer
									.getInput();
							RowWrapper[] newRows = new RowWrapper[rows.length
									+ 1];

							// Add the old rows to the new input, but
							// add a new row at the end
							for (int i = 0; i < matrixComponent
									.numberOfRows(); i++) {
								if (i == (matrixComponent.numberOfRows() - 1)) {
									newRows[i] = new RowWrapper(
											matrixComponent.getRow(i), i);
								} else {
									newRows[i] = rows[i];
								}
							}

							// Set that Input
							matrixViewer.setInput(newRows);

							// Resize the column widths
							int columnWidth = matrixViewer.getTable()
									.getSize().x
									/ matrixViewer.getTable().getColumnCount();
							for (TableColumn col : matrixViewer.getTable()
									.getColumns()) {
								col.setWidth(columnWidth);
							}

							// Refresh the Viewer
							matrixViewer.refresh();
						}
					}
				});

				// Sets the GridData to keep the "+" button on the right and
				// next the "-" button
				GridData plusNonSquareData = new GridData();
				// Sets the position of the button in its cell upward and
				// rightward
				plusNonSquareData.horizontalAlignment = SWT.END;
				plusNonSquareData.verticalAlignment = SWT.END;
				// Sets an initial height and width that are unchangeable for
				// the button
				plusNonSquareData.widthHint = 30;
				plusNonSquareData.heightHint = 30;
				// Applies this instance of GridData to the addButton
				addNonSquareButton.setLayoutData(plusNonSquareData);

				Button deleteRowNonSquareButton = new Button(sectionClient, 0);
				deleteRowNonSquareButton.setText("-");

				deleteRowNonSquareButton
						.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								if (matrixComponent != null
										&& matrixViewer != null
										&& matrixComponent.numberOfRows() > 1) {
									// Delete a Row from the Matrix
									matrixComponent.deleteRow();

									// Create a new input from the old input
									RowWrapper[] rows = (RowWrapper[]) matrixViewer
											.getInput();
									RowWrapper[] newRows = new RowWrapper[rows.length
											- 1];

									for (int i = 0; i < matrixComponent
											.numberOfRows(); i++) {
										newRows[i] = rows[i];
									}

									// Set that new Input
									matrixViewer.setInput(newRows);

									// Resize the column widths
									int columnWidth = matrixViewer.getTable()
											.getSize().x
											/ matrixViewer.getTable()
													.getColumnCount();
									for (TableColumn col : matrixViewer
											.getTable().getColumns()) {
										col.setWidth(columnWidth);
									}

									// Refresh the Viewer
									matrixViewer.refresh();
								}
							}
						});

				// Sets the GridData to keep the "-" button on the right and
				// next the "+" button
				GridData minusNonSquareRowData = new GridData();
				// Sets the position of the button in its cell downward and
				// rightward
				minusNonSquareRowData.horizontalAlignment = SWT.END;
				minusNonSquareRowData.verticalAlignment = SWT.BEGINNING;
				// Sets an initial height and width that are unchangeable for
				// the button
				minusNonSquareRowData.widthHint = 30;
				minusNonSquareRowData.heightHint = 30;
				// Applies this instance of GridData to the deleteButton
				deleteRowNonSquareButton.setLayoutData(minusNonSquareRowData);

				// Create a button to remove columns
				Button colDeleteButton = new Button(sectionClient, 0);
				colDeleteButton.setText("-");

				colDeleteButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (matrixComponent != null && matrixViewer != null
								&& matrixComponent.numberOfColumns() > 1) {
							matrixComponent.deleteColumn();

							// We need to get the old set of CellEditors and add
							// to it
							List<CellEditor> cellEditorList = new ArrayList<CellEditor>(
									Arrays.asList(
											matrixViewer.getCellEditors()));
							// Create an Array to hold default Strings for the
							// case when this MatrixComponent has Discrete
							// AllowedValueType
							List<String> stringArray = new ArrayList<String>();
							// If this Matrix is Discrete, set up Combo Cell
							// Editors
							if (matrixComponent
									.getAllowedValueType() == AllowedValueType.Discrete) {
								CellEditor ed = new ComboBoxViewerCellEditor(
										matrixViewer.getTable(), SWT.READ_ONLY);
								((ComboBoxViewerCellEditor) ed)
										.setLabelProvider(new LabelProvider());
								((ComboBoxViewerCellEditor) ed)
										.setContentProvider(
												new ArrayContentProvider());
								// Here we need to convert the Doubles to
								// Strings, because the underlying JFace Viewer
								// model is expecting Strings.
								for (Double d : matrixComponent
										.getAllowedValues()) {
									stringArray.add(d.toString());
								}
								// Set the Input for the Discrete Combo Box
								// Editor
								((ComboBoxViewerCellEditor) ed)
										.setInput(stringArray);
								stringArray.clear();
								// Add the new Cell Editor to the list
								cellEditorList.add(ed);
							} else {
								// If the elements do not have to be Discrete,
								// just add a new TextCellEditor
								cellEditorList.add(new TextCellEditor(
										matrixViewer.getTable()));
							}
							// Create a primitive Array of these new CellEditors
							CellEditor[] editors = new CellEditor[cellEditorList
									.size()];
							cellEditorList.toArray(editors);
							// Set the CellEditors
							matrixViewer.setCellEditors(editors);

							// We must do the same thing with the Column
							// Properties
							List<Object> columnPropertyList = new ArrayList<Object>(
									Arrays.asList(matrixViewer
											.getColumnProperties()));
							columnPropertyList.remove(String.valueOf(
									matrixComponent.numberOfColumns()));
							String[] columnProperties = new String[columnPropertyList
									.size()];
							columnPropertyList.toArray(columnProperties);
							// Set the Column Properties
							matrixViewer.setColumnProperties(columnProperties);

							// Remove the old column
							matrixViewer
									.getTable().getColumn(matrixViewer
											.getTable().getColumnCount() - 1)
									.dispose();

							// Create a new Input from the old Input
							RowWrapper[] rows = (RowWrapper[]) matrixViewer
									.getInput();
							RowWrapper[] newRows = new RowWrapper[rows.length];

							for (int i = 0; i < matrixComponent
									.numberOfRows(); i++) {
								rows[i].remove();
								newRows[i] = rows[i];
							}

							// Set the new Input
							matrixViewer.setInput(newRows);

							// Resize the column widths
							int columnWidth = matrixViewer.getTable()
									.getSize().x
									/ matrixViewer.getTable().getColumnCount();
							for (TableColumn col : matrixViewer.getTable()
									.getColumns()) {
								col.setWidth(columnWidth);
							}

							// Refresh the Viewer
							matrixViewer.refresh();
						}
					}
				});

				// Sets the GridData to keep the "-" button on the right and
				// next the "+" button
				GridData colGridData3 = new GridData();
				// Sets the position of the button in its cell downward and
				// rightward
				colGridData3.horizontalAlignment = SWT.END;
				colGridData3.verticalAlignment = SWT.BEGINNING;
				// Sets an initial height and width that are unchangeable for
				// the button
				colGridData3.widthHint = 30;
				colGridData3.heightHint = 30;
				// Applies this instance of GridData to the colDeleteButton
				colDeleteButton.setLayoutData(colGridData3);

				// Create an Add Column Button
				Button colAddButton = new Button(sectionClient, 0);
				colAddButton.setText("+");

				colAddButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (matrixComponent != null && matrixViewer != null) {
							logger.info("Adding a new Column");
							matrixComponent.addColumn();

							// We need to get the old set of CellEditors and add
							// to it
							List<CellEditor> cellEditorList = new ArrayList<CellEditor>(
									Arrays.asList(
											matrixViewer.getCellEditors()));
							// Create an Array to hold default Strings for the
							// case when this MatrixComponent has Discrete
							// AllowedValueType
							List<String> stringArray = new ArrayList<String>();
							// If this Matrix is Discrete, set up Combo Cell
							// Editors
							if (matrixComponent
									.getAllowedValueType() == AllowedValueType.Discrete) {
								CellEditor ed = new ComboBoxViewerCellEditor(
										matrixViewer.getTable(), SWT.READ_ONLY);
								((ComboBoxViewerCellEditor) ed)
										.setLabelProvider(new LabelProvider());
								((ComboBoxViewerCellEditor) ed)
										.setContentProvider(
												new ArrayContentProvider());
								// Here we need to convert the Doubles to
								// Strings, because the underlying JFace Viewer
								// model is expecting Strings.
								for (Double d : matrixComponent
										.getAllowedValues()) {
									stringArray.add(d.toString());
								}
								// Set the Input for the Discrete Combo Box
								// Editor
								((ComboBoxViewerCellEditor) ed)
										.setInput(stringArray);
								stringArray.clear();
								// Add the new Cell Editor to the list
								cellEditorList.add(ed);
							} else {
								// If the elements do not have to be Discrete,
								// just add a new TextCellEditor
								cellEditorList.add(new TextCellEditor(
										matrixViewer.getTable()));
							}
							// Create a primitive Array of these new CellEditors
							CellEditor[] editors = new CellEditor[cellEditorList
									.size()];
							cellEditorList.toArray(editors);
							// Set the CellEditors
							matrixViewer.setCellEditors(editors);

							// We must do the same thing with the Column
							// Properties
							List<Object> columnPropertyList = new ArrayList<Object>(
									Arrays.asList(matrixViewer
											.getColumnProperties()));
							columnPropertyList.add(String.valueOf(
									matrixComponent.numberOfColumns() - 1));
							String[] columnProperties = new String[columnPropertyList
									.size()];
							columnPropertyList.toArray(columnProperties);
							// Set the Column Properties
							matrixViewer.setColumnProperties(columnProperties);

							// Add a new column
							TableViewerColumn column = new TableViewerColumn(
									matrixViewer, SWT.CENTER);
							column.getColumn().setText("Column "
									+ matrixComponent.numberOfColumns());
							column.setLabelProvider(new ICECellLabelProvider(
									matrixComponent.numberOfColumns() - 1));

							// Create a new Input
							RowWrapper[] rows = (RowWrapper[]) matrixViewer
									.getInput();
							RowWrapper[] newRows = new RowWrapper[rows.length];

							for (int i = 0; i < matrixComponent
									.numberOfRows(); i++) {
								if (matrixComponent
										.getAllowedValueType() == AllowedValueType.Discrete) {
									rows[i].add(matrixComponent
											.getAllowedValues().get(0));
								} else {
									rows[i].add();
								}
								newRows[i] = rows[i];
							}

							// Set the newly created input
							matrixViewer.setInput(newRows);

							// Resize the column widths
							int columnWidth = matrixViewer.getTable()
									.getSize().x
									/ matrixViewer.getTable().getColumnCount();
							for (TableColumn col : matrixViewer.getTable()
									.getColumns()) {
								col.setWidth(columnWidth);
							}

							// Viewer refresh
							matrixViewer.refresh();

						}

					}
				});

				// Sets the GridData to keep the "+" button on the right and
				// next the "-" button
				GridData colGridData2 = new GridData();
				// Sets the position of the button in its cell upward and
				// rightward
				colGridData2.horizontalAlignment = SWT.END;
				colGridData2.verticalAlignment = SWT.END;
				// Sets an initial height and width that are unchangeable for
				// the button
				colGridData2.widthHint = 30;
				colGridData2.heightHint = 30;
				// Applies this instance of GridData to the colAddButton
				colAddButton.setLayoutData(colGridData2);
			}
		}

	}
}