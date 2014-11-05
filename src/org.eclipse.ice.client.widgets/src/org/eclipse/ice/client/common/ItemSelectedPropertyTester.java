package org.eclipse.ice.client.common;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;

/**
 * This class provides a {@link PropertyTester} for determining if an
 * <code>Item</code> has been selected in the {@link ItemViewer}. This is
 * necessary to enable/disable the {@link DeleteItemHandler} when the selection
 * is not-empty/empty. Note that this property is tested by the
 * {@link IEvaluationService} and is used by the <code>DeleteItemHandler</code>
 * extension (<code>org.eclipse.ui.handlers</code>).
 * 
 * @author Jordan
 * 
 */
public class ItemSelectedPropertyTester extends PropertyTester implements
		ISelectionChangedListener {

	// A useful tutorial (which inspired this class) for using PropertyTesters
	// can be found here:
	// http://www.robertwloch.net/2011/01/eclipse-tips-tricks-property-testers-with-command-core-expressions/

	/**
	 * Whether or not an item is selected in the {@link ItemViewer}.
	 */
	private final AtomicBoolean itemSelected = new AtomicBoolean(false);

	/**
	 * Whether or not this tester is registered to listen for selections from
	 * the {@link ItemViewer}.
	 */
	private final AtomicBoolean registered = new AtomicBoolean(false);

	/**
	 * Returns whether or not the selection in the <code>ItemViewer</code> is
	 * empty.
	 */
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		// NOTE: This PropertyTester is evaluated periodically when the
		// containing plugin is active.

		// To speed up the speed of this test, we (initially) register as a
		// listener to the ItemViewer's selection and update itemSelected only
		// when the selection has changed.
		if (registered.compareAndSet(false, true)) {
			IViewPart itemViewer = getViewPart(ItemViewer.ID);
			if (itemViewer != null) {
				itemViewer.getSite().getSelectionProvider()
						.addSelectionChangedListener(this);
			} else {
				registered.set(false);
			}
		}

		return itemSelected.get();
	}

	/**
	 * Gets the <code>IViewPart</code> associated with the specified ID.
	 * 
	 * @param ID
	 *            The string ID of the desired <code>IViewPart</code>.
	 * @return The <code>IViewPart</code> corresponding to the specified ID, or
	 *         null if it could not be found.
	 */
	protected static IViewPart getViewPart(String ID) {

		IViewPart viewPart = null;

		// We need to check all intermediate workbench components for null lest
		// we get exceptions, particularly when the workbench is starting.
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null && !workbench.isStarting()) {
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					// Try to get the IViewPart with the specified ID.
					viewPart = page.findView(ID);
				}
			}
		}

		return viewPart;
	}

	/**
	 * When the <code>ItemViewer</code>'s selection changes, this sets
	 * {@link #itemSelected}.
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		if (event != null) {
			ISelection selection = event.getSelection();
			if (selection != null) {
				itemSelected.set(!selection.isEmpty());
			}
		}

		return;
	}

}
