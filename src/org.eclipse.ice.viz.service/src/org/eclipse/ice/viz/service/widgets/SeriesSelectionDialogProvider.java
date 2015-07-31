package org.eclipse.ice.viz.service.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

public class SeriesSelectionDialogProvider {

	private boolean multiSelect = false;

	private IPlot plot;

	private String[] categories;
	private Map<String, ISeries[]> seriesMap = new HashMap<String, ISeries[]>();

	private ICheckStateListener checkStateListener;
	private ICheckStateProvider checkStateProvider;
	private ITreeContentProvider contentProvider;
	private ILabelProvider labelProvider;

	private CheckboxTreeViewer checkboxTreeViewer;
	private TreeViewer treeViewer;

	public SeriesSelectionDialogProvider() {
		checkStateListener = createCheckStateListener();
		checkStateProvider = createCheckStateProvider();
		contentProvider = createContentProvider();
		labelProvider = createLabelProvider();
	}

	public int showDialog(Shell shell) {
		final Dialog dialog;
		if (multiSelect) {
			CheckedTreeSelectionDialog multiSelectDialog = new CheckedTreeSelectionDialog(
					shell, labelProvider, contentProvider) {
				@Override
				protected CheckboxTreeViewer createTreeViewer(
						Composite parent) {
					checkboxTreeViewer = super.createTreeViewer(parent);
					treeViewer = checkboxTreeViewer;

					 checkboxTreeViewer
					 .setCheckStateProvider(checkStateProvider);
					checkboxTreeViewer
							.addCheckStateListener(checkStateListener);

//					// Check all series that are enabled. If all series in a
//					// category are enabled, then if the category is shown, it
//					// should be checked, too.
//					for (Entry<String, ISeries[]> e : seriesMap.entrySet()) {
//						String category = e.getKey();
//						ISeries[] seriesArray = e.getValue();
//
//						boolean checkCategory = true;
//						// Check all series that are enabled.
//						for (ISeries series : seriesArray) {
//							if (series.enabled()) {
//								checkboxTreeViewer.setChecked(series, true);
//							} else {
//								checkCategory = false;
//							}
//						}
//						// Check the category if all series are enabled.
//						if (categories != null && checkCategory) {
//							checkboxTreeViewer.setChecked(category, true);
//						}
//					}

					return checkboxTreeViewer;
				}
			};
			multiSelectDialog.setInput(plot);
			dialog = multiSelectDialog;
		} else {
			dialog = new ElementTreeSelectionDialog(shell, labelProvider,
					contentProvider);
		}

		int result = dialog.open();

		checkboxTreeViewer = null;
		treeViewer = null;

		return result;
	}

	public void setMultiSelect(boolean multi) {
		this.multiSelect = multi;
	}

	public void setPlot(IPlot plot) {
		if (plot != this.plot) {
			this.plot = plot;

			categories = null;
			seriesMap.clear();

			List<String> categoryList = plot.getCategories();
			if (categoryList.size() > 1) {
				categories = new String[categoryList.size()];
				categoryList.toArray(categories);
			}

			for (String category : categoryList) {
				List<ISeries> seriesList = plot.getAllDependentSeries(category);

				if (seriesList != null) {
					ISeries[] array = new ISeries[seriesList.size()];
					seriesList.toArray(array);
					seriesMap.put(category, array);
				}
			}
		}
		return;
	}

	public List<ISeries> getFullSelection() {
		return null;
	}

	public List<ISeries> getSelected() {
		return null;
	}

	public List<ISeries> getUnselected() {
		return null;
	}

	protected ITreeContentProvider createContentProvider() {
		return new ITreeContentProvider() {

			@Override
			public void dispose() {
				// Nothing to do. Not used.
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// Nothing to do. Not used.
			}

			@Override
			public Object[] getElements(Object inputElement) {
				Object[] elements = null;

				// Either show the categories or the plots from the lone
				// category (if there is only one category).
				if (inputElement == plot) {
					if (categories != null) {
						elements = categories;
					} else if (!seriesMap.isEmpty()) {
						elements = seriesMap.values().iterator().next();
					}
				}

				return elements != null ? elements : new Object[0];
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				Object[] elements = null;

				// For the plot, either show the categories or the plots from
				// the lone category (if there is only one category).
				if (parentElement == plot) {
					if (categories != null) {
						elements = categories;
					} else if (!seriesMap.isEmpty()) {
						elements = seriesMap.values().iterator().next();
					}
				}
				// For a category, show its series.
				else if (parentElement instanceof String) {
					elements = seriesMap.get(parentElement.toString());
				}

				return elements != null ? elements : new Object[0];
			}

			@Override
			public Object getParent(Object element) {
				Object parent = null;

				if (element instanceof String) {
					if (seriesMap.containsKey(element.toString())) {
						parent = plot;
					}
				} else if (element instanceof ISeries) {
					String category = ((ISeries) element).getCategory();
					if (seriesMap.containsKey(category)) {
						parent = category;
					}
				}

				return parent;
			}

			@Override
			public boolean hasChildren(Object element) {
				boolean hasChildren = false;
				// The plot only has children if it has multiple categories or
				// at least 1 series.
				if (element == plot) {
					hasChildren = categories != null || (!seriesMap.isEmpty()
							&& seriesMap.values().iterator().next().length > 0);
				}
				// For a category, show its series.
				else if (element instanceof String) {
					hasChildren = seriesMap.containsKey(element.toString());
				}
				return hasChildren;
			}

		};
	}

	protected ICheckStateProvider createCheckStateProvider() {
		return new ICheckStateProvider() {
			@Override
			public boolean isGrayed(Object element) {
				return true;
			}

			@Override
			public boolean isChecked(Object element) {
				boolean checked = false;
				if (element instanceof String) {
					ISeries[] seriesArray = seriesMap.get(element.toString());
					if (seriesArray != null) {
						checked = true;
						for (ISeries series : seriesArray) {
							if (!series.enabled()) {
								checked = false;
								break;
							}
						}
					}
				} else if (element instanceof ISeries) {
					checked = ((ISeries) element).enabled();
				}
				return checked;
			}
		};
	}

	protected ICheckStateListener createCheckStateListener() {
		return new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object element = event.getElement();
				boolean checked = event.getChecked();

				if (element instanceof String) {
					String category = element.toString();

					// Enable/disable all series in the category.
					CheckboxTreeViewer viewer = getCheckboxTreeViewer();
					for (ISeries series : seriesMap.get(category)) {
						viewer.setChecked(series, checked);
						series.setEnabled(checked);
					}

				} else if (element instanceof ISeries) {
					ISeries checkedSeries = (ISeries) element;
					String category = checkedSeries.getCategory();

					// Enable the series.
					checkedSeries.setEnabled(checked);

					// If all series in the category are selected, check the
					// category, too.
					CheckboxTreeViewer viewer = getCheckboxTreeViewer();
					if (checked) {
						for (ISeries series : seriesMap.get(category)) {
							if (!series.enabled()) {
								checked = false;
								break;
							}
						}
					}
					viewer.setChecked(category, checked);
				}

				return;
			}
		};
	}

	protected ILabelProvider createLabelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object element) {
				String text = null;
				if (element instanceof IPlot) {
					text = ((IPlot) element).getPlotTitle();
				} else if (element instanceof ISeries) {
					text = ((ISeries) element).getLabel();
				}
				return text != null ? text : super.getText(element);
			}
		};
	}

	private TreeViewer getTreeViewer() {
		return treeViewer;
	}

	private CheckboxTreeViewer getCheckboxTreeViewer() {
		return checkboxTreeViewer;
	}
}
