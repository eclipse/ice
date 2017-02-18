/*******************************************************************************
 * Copyright (c) 2017 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Robert Smith
 *******************************************************************************/
package org.eclipse.ice.geometry;

import java.util.Iterator;
import java.util.List;

import org.eclipse.eavp.geometry.view.model.IRenderElement;
import org.eclipse.eavp.viz.service.geometry.widgets.ShapeTreeView;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * A custom form editor for the Geometry Editor item, which will have custom
 * tabbed properties.
 *
 * @author Robert Smith
 *
 */
public class GeometryFormEditor extends ICEFormEditor
		implements ITabbedPropertySheetPageContributor {

	/**
	 * The ID for use in the extension point.
	 */
	public static final String ID = "org.eclipse.ice.client.widgets.geometry.GeometryFormEditor";

	/**
	 * The constructor.
	 */
	public GeometryFormEditor() {
		super();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {

		// Give the TabbedPropertySheetPage for the IPropertySheetPage
		if (adapter == IPropertySheetPage.class) {
			return new TabbedPropertySheetPage(this);
		}

		// Otherwise delegate to the superclass
		else {
			return super.getAdapter(adapter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.
	 * ITabbedPropertySheetPageContributor#getContributorId()
	 */
	@Override
	public String getContributorId() {
		return getSite().getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.ICEFormEditor#createHeaderContents(org.
	 * eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createHeaderContents(IManagedForm headerForm) {
		super.createHeaderContents(headerForm);

		// Create a Selection Provider for this part
		getSite().setSelectionProvider(new ISelectionProvider() {

			@Override
			public void addSelectionChangedListener(
					ISelectionChangedListener listener) {
				// TODO Auto-generated method stub
			}

			@Override
			public ISelection getSelection() {

				// Pull the currently selected shapes from the ShapeTreeView, so
				// that the tree view and the geometry editor's selections are
				// always in synch
				List<IRenderElement> selection = ((ShapeTreeView) PlatformUI
						.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().findView(ShapeTreeView.ID))
								.getSelectedElements();

				// Create a new selection
				return new ITreeSelection() {

					@Override
					public Object getFirstElement() {
						if (selection.isEmpty()) {
							return null;
						} else {
							return selection.get(0);
						}
					}

					@Override
					public Iterator iterator() {
						return selection.iterator();
					}

					@Override
					public int size() {
						return selection.size();
					}

					@Override
					public Object[] toArray() {
						return selection.toArray();
					}

					@Override
					public List toList() {
						return selection;
					}

					@Override
					public boolean isEmpty() {
						return selection.isEmpty();
					}

					@Override
					public TreePath[] getPaths() {
						TreePath[] paths = new TreePath[selection.size()];
						for (int i = 0; i < selection.size(); i++) {
							paths[i] = new TreePath(
									new IRenderElement[] { selection.get(i) });
						}
						return paths;
					}

					@Override
					public TreePath[] getPathsFor(Object element) {
						return new TreePath[] {
								new TreePath(new IRenderElement[] { selection
										.get(selection.indexOf(element)) }) };
					}

				};
			}

			@Override
			public void removeSelectionChangedListener(
					ISelectionChangedListener listener) {
				// TODO Auto-generated method stub
			}

			@Override
			public void setSelection(ISelection selection) {
				// TODO Auto-generated method stub
			}
		});
	}
}
