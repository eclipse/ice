package org.eclipse.ice.viz.service;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public abstract class PlotRender {

	public final Composite parent;
	public final MultiPlot plot;

	private String category;
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

	// -------------------- //

	public PlotRender(Composite parent, MultiPlot plot) {

		this.parent = parent;
		this.plot = plot;
	}

	public void createPlotContent(int style) {

		// Create the container for the info and plot Composites.
		stackComposite = new Composite(parent, style);
		stackComposite.setFont(parent.getFont());
		stackComposite.setBackground(parent.getBackground());
		stackComposite.setLayout(new StackLayout());

		refresh();

		return;
	}

	public void setPlotCategory(String category) {
		this.category = category;
	}

	public void setPlotType(String type) {
		this.type = type;
	}

	public String getPlotCategory() {
		return category;
	}

	public String getPlotType() {
		return type;
	}

	/**
	 * This method updates the UI widgets based on the current settings. It
	 * should be called whenever the UI needs to be updated in any way.
	 * <p>
	 * This will either immediately update the UI or trigger an asynchronous
	 * update to the UI on the display's UI thread.
	 * </p>
	 */
	protected void refresh() {
		if (stackComposite != null && !stackComposite.isDisposed()) {
			// If we are not on the UI thread, update the UI asynchronously on
			// the UI thread.
			if (Display.getCurrent() == null) {
				stackComposite.getDisplay().asyncExec(new Runnable() {
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
		}
		return;
	}

	/**
	 * This method updates the UI contributions for this plot. This method
	 * should <b>only</b> called from the <i>UI thread</i> via
	 * {@link #refresh()}.
	 */
	private void refreshUI() {
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

	protected void updateInfoComposite(Composite infoComposite,
			final String message) {
		// Set the message and icon based on the state of the connection.
		final Image image;
		final Display display = infoComposite.getDisplay();

		// Set a default image.
		image = display.getSystemImage(SWT.ICON_WARNING);

		// Update the contents of the infoComposite's widgets.
		iconLabel.setImage(image);
		msgLabel.setText(message);

		return;
	}

	protected void disposeInfoComposite(Composite infoComposite) {
		infoComposite.dispose();
		iconLabel = null;
		msgLabel = null;
	}
	
	protected Composite createPlotComposite(Composite parent, int style)
			throws Exception {
		return new Composite(parent, style);
	}

	protected void updatePlotComposite(Composite plotComposite)
			throws Exception {
		int seed = (category + type).hashCode();
		Random r = new Random(seed);
		plotComposite.setBackground(new Color(plotComposite.getDisplay(), r
				.nextInt(255), r.nextInt(255), r.nextInt(255)));
	}
	
	protected void disposePlotComposite(Composite plotComposite) {
		// Nothing to do.
	}

}
