package org.eclipse.ice.viz.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * This class manages a single rendering of an {@link IPlot}.
 * <p>
 * After creating a {@code PlotRender}, its content will need to be created via
 * {@link #createBasicContent()}. It can be updated later by calling
 * {@link #refresh()}.
 * </p>
 * <p>
 * Sub-classes should implement the required methods to populate/update the
 * {@link #plotComposite} and may also override the methods that populate/update
 * the {@link #infoComposite} to add extra informational features as necessary.
 * </p>
 * 
 * @author Jordan
 *
 */
public abstract class PlotRender {

	// TODO We may want to add a ToolBar

	/**
	 * The parent {@code Composite} that contains the plot render.
	 */
	public final Composite parent;
	/**
	 * The rendered {@code IPlot}. This cannot be changed.
	 */
	public final IPlot plot;

	/**
	 * The current plot category.
	 */
	private String category;
	/**
	 * The current plot type.
	 */
	private String type;

	// ---- UI Widgets ---- //
	/**
	 * This composite contains the {@link #plotComposite} and
	 * {@link #infoComposite} in a stack.
	 */
	private Composite stackComposite = null;
	/**
	 * The current widget used to draw the plot.
	 */
	private Composite plotComposite = null;
	/**
	 * This presents the user with helpful information about the status of the
	 * plot. It should only be visible if the plot cannot be drawn.
	 */
	private Composite infoComposite = null;
	/**
	 * Displays an icon to demonstrate the severity of the message.
	 */
	private Label iconLabel;
	/**
	 * Displays a message explaining the current state of the plot or why it
	 * cannot render anything.
	 */
	private Label msgLabel;

	/**
	 * The image to display in the {@link #iconLabel}.
	 */
	protected Image infoIcon;

	// -------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that contains the plot render.
	 * @param plot
	 *            The rendered {@code IPlot}. This cannot be changed.
	 */
	public PlotRender(Composite parent, IPlot plot) {
		// Check the parameters.
		if (parent == null || plot == null) {
			throw new NullPointerException("PlotRender error: "
					+ "Cannot render a plot that is null or "
					+ "inside a null parent Composite.");
		}

		this.parent = parent;
		this.plot = plot;

		return;
	}

	/**
	 * Sets the current plot category.
	 * <p>
	 * <b>Note:</b> A subsequent call to {@link #refresh()} will be necessary to
	 * sync the UI with this call's changes.
	 * </p>
	 * 
	 * @param category
	 *            The new plot category.
	 */
	public void setPlotCategory(String category) {
		this.category = category;
	}

	/**
	 * Sets the current plot type.
	 * <p>
	 * <b>Note:</b> A subsequent call to {@link #refresh()} will be necessary to
	 * sync the UI with this call's changes.
	 * </p>
	 * 
	 * @param type
	 *            The new plot type.
	 */
	public void setPlotType(String type) {
		this.type = type;
	}

	/**
	 * Gets the current plot category.
	 * 
	 * @return The current plot category.
	 */
	public String getPlotCategory() {
		return category;
	}

	/**
	 * Gets the current plot type.
	 * 
	 * @return The current plot type.
	 */
	public String getPlotType() {
		return type;
	}

	/**
	 * This method updates the UI widgets based on the current settings. It
	 * should be called whenever the UI needs to be updated in any way. It is
	 * the same as calling {@link #refresh(boolean) refresh(false)}.
	 * <p>
	 * This will either immediately update the UI or trigger an asynchronous
	 * update to the UI on the display's UI thread.
	 * </p>
	 */
	public void refresh() {
		refresh(false);
	}

	/**
	 * This method updates the UI widgets based on the current settings. It
	 * should be called whenever the UI needs to be updated in any way.
	 * <p>
	 * This will either immediately update the UI or trigger an asynchronous
	 * update to the UI on the display's UI thread.
	 * </p>
	 * 
	 * @param clearCache
	 *            Some sub-classes may cache certain meta data so that the
	 *            refresh operation is faster. If true, this parameter causes
	 *            the cached information to be rebuilt at the next available
	 *            opportunity.
	 */
	public void refresh(boolean clearCache) {

		// If necessary, clear the cached information.
		if (clearCache) {
			clearCache();
		}

		// If we are not on the UI thread, update the UI asynchronously on the
		// UI thread.
		if (Display.getCurrent() == null) {
			parent.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					refreshUI();
				}
			});
		}
		// If we are on the UI thread, update the UI synchronously.
		else {
			refreshUI();
		}

		return;
	}

	/**
	 * Clears any cached meta data used to speed up the refresh operation.
	 */
	protected abstract void clearCache();

	/**
	 * This method updates the UI contributions for this plot. This method
	 * should <b>only</b> called from the <i>UI thread</i> via
	 * {@link #refresh()}.
	 */
	private void refreshUI() {
		// Create the basic content if necessary.
		if (stackComposite == null) {
			createBasicContent();
		}

		// Get the StackLayout from the plot Composite.
		final StackLayout stackLayout = (StackLayout) stackComposite
				.getLayout();

		try {
			// Update the plotComposite. Create it if necessary.
			if (plotComposite == null) {
				plotComposite = createPlotComposite(stackComposite, SWT.NONE);
			}
			updatePlotComposite(plotComposite);

			// If the plotComposite was successfully updated, we can dispose the
			// infoComposite.
			if (infoComposite != null && !infoComposite.isDisposed()) {
				disposeInfoComposite(infoComposite);
				infoComposite = null;
			}

			// Update the stack layout, putting the plotComposite in front.
			if (stackLayout.topControl != plotComposite) {
				stackLayout.topControl = plotComposite;
				stackComposite.layout();
			}
		} catch (Exception e) {
			// Update the infoComposite. Create it if necessary.
			if (infoComposite == null) {
				infoComposite = createInfoComposite(stackComposite, SWT.NONE);
			}
			updateInfoComposite(infoComposite, e.getMessage());

			// Dispose the plotComposite.
			if (plotComposite != null && !plotComposite.isDisposed()) {
				disposePlotComposite(plotComposite);
				plotComposite = null;
			}

			// Update the stack layout, putting the infoComposite in front.
			if (stackLayout.topControl != infoComposite) {
				stackLayout.topControl = infoComposite;
				stackComposite.layout();
			}
		}

		return;
	}

	/**
	 * Creates the most basic content for the {@code PlotRender}.
	 * <p>
	 * <b>Note:</b> This method should <b>only</b> called from the <i>UI
	 * thread</i> ONCE via {@link #refresh()}.
	 * </p>
	 */
	private void createBasicContent() throws SWTException {

		// Create the container for the info and plot Composites.
		stackComposite = new Composite(parent, SWT.NONE);
		stackComposite.setFont(parent.getFont());
		stackComposite.setBackground(parent.getBackground());
		stackComposite.setLayout(new StackLayout());

		// Set the stackComposite's context Menu to the parent's.
		stackComposite.setMenu(parent.getMenu());

		return;
	}

	/**
	 * Creates the informational {@code Composite} that is shown when an
	 * exception occurs during calls to {@link #updatePlotComposite(Composite)}.
	 * <p>
	 * The default {@link #infoComposite} contains an icon label to the left and
	 * a {@code Composite} on the right with a default {@code GridLayout} and a
	 * message label. The label will show the message from the plot update
	 * exception.
	 * </p>
	 * <p>
	 * <b>Note:</b> If overridden, be sure to either call this super method or
	 * also override {@link #updateInfoComposite(Composite, String)}.
	 * </p>
	 * 
	 * @param parent
	 *            The parent in which the info {@code Composite} should be
	 *            created.
	 * @param style
	 *            The style to use for the info {@code Composite}.
	 * @return The new info {@code Composite}.
	 */
	protected Composite createInfoComposite(Composite parent, int style) {

		Composite infoComposite = new Composite(parent, style);
		infoComposite.setLayout(new GridLayout(2, false));

		// Create an info label with an image.
		iconLabel = new Label(infoComposite, SWT.NONE);
		iconLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
				false, false));

		// Create a Composite to contain the info message.
		Composite msgComposite = new Composite(infoComposite, SWT.NONE);
		msgComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
				false, false));
		msgComposite.setLayout(new GridLayout(1, false));

		// Create an info label with informative text.
		msgLabel = new Label(msgComposite, SWT.NONE);
		msgLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
				false));

		return infoComposite;
	}

	/**
	 * Updates the information contained in the specified informational
	 * {@code Composite}.
	 * 
	 * 
	 * @param infoComposite
	 *            The info {@code Composite} to update.
	 * @param message
	 *            The message to display in its message label.
	 */
	protected void updateInfoComposite(Composite infoComposite,
			final String message) {
		// Set the message and icon based on the state of the connection.
		final Display display = infoComposite.getDisplay();
		// If there's no icon set, default to something useful.
		final Image image = (infoIcon != null ? infoIcon : display
				.getSystemImage(SWT.ICON_WARNING));

		// Update the contents of the infoComposite's widgets.
		iconLabel.setImage(image);
		msgLabel.setText(message);

		// Force the StackLayout to refresh. We need the two boolean flags so
		// that the text will wrap properly.
		stackComposite.layout(true, true);

		return;
	}

	/**
	 * Disposes the specified info {@code Composite} and any related resources.
	 * 
	 * @param infoComposite
	 *            The info {@code Composite} to dispose.
	 */
	protected void disposeInfoComposite(Composite infoComposite) {
		infoComposite.dispose();
		iconLabel = null;
		msgLabel = null;
	}

	/**
	 * Creates the plot {@code Composite} that is shown when the associated
	 * {@link #plot}, {@link #category}, and {@link #type} are all valid.
	 * 
	 * @param parent
	 *            The parent in which the plot {@code Composite} should be
	 *            created.
	 * @param style
	 *            The style to use for the plot {@code Composite}.
	 * @return The new plot {@code Composite}.
	 * @throws Exception
	 *             If the plot is in an invalid state or otherwise cannot be
	 *             rendered, this throws an exception with an informative
	 *             message.
	 */
	protected abstract Composite createPlotComposite(Composite parent, int style)
			throws Exception;

	/**
	 * Updates the plot rendering contained in the specified plot
	 * {@code Composite}.
	 * 
	 * @param plotComposite
	 *            The plot {@code Composite} to update.
	 * @throws Exception
	 *             If the plot is in an invalid state or otherwise cannot be
	 *             rendered, this throws an exception with an informative
	 *             message.
	 */
	protected abstract void updatePlotComposite(Composite plotComposite)
			throws Exception;

	/**
	 * Disposes the specified plot {@code Composite} and any related resources.
	 * 
	 * @param plotComposite
	 *            The plot {@code Composite} to dispose.
	 */
	protected void disposePlotComposite(Composite plotComposite) {
		// Nothing to do.
	}

}
