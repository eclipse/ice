package org.eclipse.ice.viz.service.visit.preferences;

import org.eclipse.ice.client.widgets.properties.ICellContentProvider;
import org.eclipse.swt.graphics.Image;

public class ConnectionCellContentProvider implements ICellContentProvider {

	/**
	 * The text to display when a {@link Connection} is invalid.
	 */
	protected static final String INVALID_ELEMENT_TEXT = "Invalid connection.";

	/**
	 * By default, any non-null {@link Connection} is valid.
	 */
	@Override
	public boolean isValid(Object element) {
		return element != null && element instanceof Connection;
	}

	/**
	 * By default, any valid element is enabled.
	 */
	@Override
	public boolean isEnabled(Object element) {
		return isValid(element);
	}

	/**
	 * Gets the string displayed in the cell. The default behavior is:
	 * <ul>
	 * <li>If the element's value is not null, the value is displayed.</li>
	 * <li>If the element is invalid or otherwise the value is null,
	 * {@link #INVALID_ELEMENT_TEXT} is displayed.</li>
	 * </ul>
	 */
	@Override
	public String getText(Object element) {

		Object value = getValue(element);
		String text = INVALID_ELEMENT_TEXT;

		if (value != null) {
			text = value.toString().trim();
		}

		return text;
	}

	// TODO We may want to return a more helpful tool tip.
	/**
	 * The default behavior is to return the same value displayed by
	 * {@link #getText(Object)}.
	 */
	@Override
	public String getToolTipText(Object element) {
		return getText(element);
	}

	/**
	 * Returns {@code null} for no {@code Image}.
	 */
	@Override
	public Image getImage(Object element) {
		return null;
	}

	/**
	 * Returns the connection's ID. This should be overridden by sub-classes.
	 */
	@Override
	public Object getValue(Object element) {
		Object value = null;

		if (isValid(element)) {
			value = ((Connection) element).getId();
		}

		return value;
	}

	/**
	 * By default, converts the value to a string and tries to set it as the
	 * connection's ID. This should be overridden by sub-classes.
	 */
	@Override
	public boolean setValue(Object element, Object value) {
		boolean changed = false;

		if (isValid(element) && value != null) {
			String newValue = value.toString().trim();
			Connection connection = (Connection) element;
			String oldId = connection.getId();
			connection.setId(newValue);
			changed = oldId.equals(newValue);
		}

		return changed;
	}

}
