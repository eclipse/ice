package org.eclipse.ice.viz.service.visit.preferences;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

public class TableComponentCellEditingSupport extends EditingSupport {

	private final EntryCellEditingSupport realEditingSupport;
	private final int index;
	
	public TableComponentCellEditingSupport(ColumnViewer viewer,
			EntryCellEditingSupport realEditingSupport, int index) {
		super(viewer);
		
		this.realEditingSupport = realEditingSupport;
		this.index = index;
	}
	
	@Override
	protected CellEditor getCellEditor(Object element) {
		// If all else fails, we should return null.
		CellEditor editor = null;

		// Only try to get the CellEditor if the indexed element exists. We do
		// this because the real EditingSupport may provide editors for null
		// values.
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				editor = realEditingSupport.getCellEditor(list.get(index));
			}
		}
		
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		boolean enabled = false;
		
		// Only try to get the CellEditor if the indexed element exists. We do
		// this because the real EditingSupport may provide editors for null
		// values.
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				enabled = realEditingSupport.canEdit(list.get(index));
			}
		}
		
		return enabled;
	}

	@Override
	protected Object getValue(Object element) {
		Object value = null;
		
		// Only try to get the CellEditor if the indexed element exists. We do
		// this because the real EditingSupport may provide editors for null
		// values.
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				value = realEditingSupport.getValue(list.get(index));
			}
		}
		
		return value;
	}

	@Override
	protected void setValue(Object element, Object value) {
		// Only try to get the CellEditor if the indexed element exists. We do
		// this because the real EditingSupport may provide editors for null
		// values.
		if (element != null && element instanceof List<?>) {
			List<?> list = (List<?>) element;
			if (index >= 0 && index < list.size()) {
				realEditingSupport.setValue(list.get(index), value);
				// Force the viewer to refresh this row.
				getViewer().update(element, null);
			}
		}
		
		return;
	}

}
