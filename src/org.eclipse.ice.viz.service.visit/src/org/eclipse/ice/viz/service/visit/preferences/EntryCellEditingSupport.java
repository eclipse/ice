package org.eclipse.ice.viz.service.visit.preferences;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class EntryCellEditingSupport extends EditingSupport {

	/**
	 * The content provider. The methods required as an
	 * <code>EditingSupport</code> are passed to this content provider.
	 */
	protected final EntryCellContentProvider contentProvider;

	/**
	 * A <code>CellEditor</code> built around a <code>Text</code> widget.
	 */
	protected final TextCellEditor textCell;

	/**
	 * The default constructor.
	 * 
	 * @param viewer
	 *            The viewer that is using this <code>EditingSupport</code>.
	 *            <code>Control</code>s required by this class will be
	 *            constructed under this viewer's <code>Control</code> (usually
	 *            a <code>Table</code>).
	 * @param contentProvider
	 *            The content provider. The methods required as an
	 *            <code>EditingSupport</code> are passed to this content
	 *            provider.
	 */
	public EntryCellEditingSupport(ColumnViewer viewer,
			EntryCellContentProvider contentProvider) {
		super(viewer);

		this.contentProvider = contentProvider;

		// Get the viewer's Composite so we can create the CellEditors.
		Composite parent = (Composite) viewer.getControl();

		// Create the TextCellEditor.
		textCell = new TextCellEditor(parent, SWT.LEFT);

		return;
	}

	/**
	 * By default, this returns a {@link TextCellEditor}.
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {

		// If all else fails, we should return null.
		CellEditor editor = null;

		// By default, return a Text-based CellEditor.
		if (contentProvider.isValid(element)) {
			editor = textCell;
		}

		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element) {
		return contentProvider.isEnabled(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		return contentProvider.getValue(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		if (contentProvider.setValue(element, value)) {
			// Force the viewer to refresh for this specific element. This means
			// the change will be reflected in the viewer.
			getViewer().update(element, null);
		}
	}
}
