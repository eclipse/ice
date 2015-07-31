package org.eclipse.ice.viz.service.widgets.test;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.viz.service.AbstractPlot;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.ice.viz.service.test.FakeSeries;
import org.eclipse.ice.viz.service.widgets.TreeSelectionDialogProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;

public class TestLauncher {

//	private TreeViewer regularTreeViewer;
//	private CheckboxTreeViewer checkboxTreeViewer;
//
//	private IPlot plot;
//	private IPlot plot1;
//	private IPlot plot2;
//
//	private Map<String, List<ISeries>> plot1Series;
//	private Map<String, List<ISeries>> plot2Series;
//
//	public static void main(String[] args) {
//
//		Display display = Display.getDefault();
//		Shell shell = new Shell(display);
//
//		shell.open();
//
//		new TestLauncher(shell);
//
//		while (!shell.isDisposed()) {
//			display.readAndDispatch();
//		}
//		display.dispose();
//
//		return;
//	}
//
//	public TestLauncher(Shell parent) {
//
//		parent.setLayout(new GridLayout());
//		createToolBar(parent)
//				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		createContent(parent)
//				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		parent.layout();
//
//		createPlots();
//		setPlot(plot1);
//
//		return;
//	}
//
//	private ToolBar createToolBar(Composite parent) {
//		ToolBarManager toolBarManager = new ToolBarManager();
//		toolBarManager.add(new Action("Toggle Plot") {
//			@Override
//			public void run() {
//				if (plot == plot1) {
//					setPlot(plot2);
//				} else {
//					setPlot(plot1);
//				}
//			}
//		});
//
//		final Shell shell = parent.getShell();
//		final ITreeContentProvider contentProvider = createContentProvider();
//		final ILabelProvider labelProvider = createLabelProvider();
//		final ICheckStateListener checkStateListener = createCheckStateListener();
//		final ICheckStateProvider checkStateProvider = createCheckStateProvider();
//
//		toolBarManager.add(new Action("Open Single-select Dialog") {
//			@Override
//			public void run() {
//				TreeSelectionDialogProvider provider = new TreeSelectionDialogProvider() {
//					@Override
//					public Object[] getChildren(Object parent) {
//						return contentProvider.getChildren(parent);
//					}
//					
//					@Override
//					public boolean isChecked(Object element) {
//						return checkStateProvider.isChecked(element);
//					}
//					
//					@Override
//					public String getText(Object element) {
//						return labelProvider.getText(element);
//					}
//				};
//				provider.setInput(getPlot());
//				provider.openDialog(shell);
//			}
//		});
//		toolBarManager.add(new Action("Open Multi-select Dialog") {
//			@Override
//			public void run() {
//
//				CheckedTreeSelectionDialog dialog = new CheckedTreeSelectionDialog(
//						shell, labelProvider, contentProvider) {
//					@Override
//					protected CheckboxTreeViewer createTreeViewer(
//							Composite parent) {
//						CheckboxTreeViewer viewer = super.createTreeViewer(
//								parent);
//						viewer.setCheckStateProvider(checkStateProvider);
//						viewer.addCheckStateListener(checkStateListener);
//						viewer.refresh();
//						return viewer;
//					}
//				};
//				dialog.setInput(getPlot());
//				dialog.open();
//			}
//		});
//
//		return toolBarManager.createControl(parent);
//	}
//
//	private Composite createContent(Composite parent) {
//		Composite container = new Composite(parent, SWT.BORDER);
//		container.setLayout(new FillLayout(SWT.HORIZONTAL));
//
//		ITreeContentProvider contentProvider = createContentProvider();
//		ILabelProvider labelProvider = createLabelProvider();
//
//		regularTreeViewer = new TreeViewer(container);
//		regularTreeViewer.setContentProvider(contentProvider);
//		regularTreeViewer.setLabelProvider(labelProvider);
//
//		ICheckStateListener checkStateListener = createCheckStateListener();
//		ICheckStateProvider checkStateProvider = createCheckStateProvider();
//
//		checkboxTreeViewer = new CheckboxTreeViewer(container);
//		checkboxTreeViewer.setContentProvider(contentProvider);
//		checkboxTreeViewer.setLabelProvider(labelProvider);
//		checkboxTreeViewer.setCheckStateProvider(checkStateProvider);
//		checkboxTreeViewer.addCheckStateListener(checkStateListener);
//
//		return container;
//	}
//
//	private void createPlots() {
//		plot1 = new AbstractPlot() {
//			@Override
//			public List<ISeries> getAllDependentSeries(String category) {
//				return plot1Series.get(category);
//			}
//
//			@Override
//			public List<String> getCategories() {
//				return new ArrayList<String>(plot1Series.keySet());
//			}
//		};
//
//		plot2 = new AbstractPlot() {
//			@Override
//			public List<ISeries> getAllDependentSeries(String category) {
//				return plot2Series.get(category);
//			}
//
//			@Override
//			public List<String> getCategories() {
//				return new ArrayList<String>(plot2Series.keySet());
//			}
//		};
//
//		plot1Series = createSeries(1, 5);
//		plot2Series = createSeries(3, 3);
//
//		return;
//	}
//
//	private Map<String, List<ISeries>> createSeries(int numCategories,
//			int numSeriesPerCategory) {
//
//		String category;
//		List<ISeries> seriesList;
//		Map<String, List<ISeries>> map = new TreeMap<String, List<ISeries>>();
//
//		for (int i = 0; i < numCategories; i++) {
//			category = "category" + Integer.toString(i);
//			seriesList = new ArrayList<ISeries>();
//			for (int j = 0; j < numSeriesPerCategory; j++) {
//				ISeries series = new FakeSeries(category) {
//					@Override
//					public void setEnabled(boolean enable) {
//						super.setEnabled(enable);
//						String enabling = enable ? "Enabling" : "Disabling";
//						System.out.println(enabling + " series " + getLabel());
//					}
//				};
//				series.setLabel(Integer.toString(j));
//				seriesList.add(series);
//				if (i % 2 == 1) {
//					series.setEnabled(true);
//				}
//			}
//			map.put(category, seriesList);
//		}
//
//		return map;
//	}
//
//	private void setPlot(IPlot plot) {
//		this.plot = plot;
//		regularTreeViewer.setInput(plot);
//		regularTreeViewer.refresh();
//		checkboxTreeViewer.setInput(plot);
//		checkboxTreeViewer.refresh();
//	}
//
//	private IPlot getPlot() {
//		return plot;
//	}
//
//	private ITreeContentProvider createContentProvider() {
//		return new ITreeContentProvider() {
//
//			@Override
//			public void dispose() {
//				// Nothing to do. Not used.
//			}
//
//			@Override
//			public void inputChanged(Viewer viewer, Object oldInput,
//					Object newInput) {
//				// Nothing to do. Not used.
//			}
//
//			@Override
//			public Object[] getElements(Object inputElement) {
//				Object[] elements = null;
//
//				IPlot plot = getPlot();
//
//				// Either show the categories or the plots from the lone
//				// category (if there is only one category).
//				if (inputElement == plot) {
//					List<String> categories = plot.getCategories();
//					if (categories.size() > 1) {
//						elements = categories.toArray();
//					} else if (!categories.isEmpty()) {
//						elements = plot.getAllDependentSeries(categories.get(0))
//								.toArray();
//					}
//				}
//
//				return elements != null ? elements : new Object[0];
//			}
//
//			@Override
//			public Object[] getChildren(Object parentElement) {
//				Object[] elements = null;
//
//				IPlot plot = getPlot();
//
//				// For the plot, either show the categories or the plots from
//				// the lone category (if there is only one category).
//				if (parentElement == plot) {
//					List<String> categories = plot.getCategories();
//					if (categories.size() > 1) {
//						elements = categories.toArray();
//					} else if (!categories.isEmpty()) {
//						elements = plot.getAllDependentSeries(categories.get(0))
//								.toArray();
//					}
//				}
//				// For a category, show its series.
//				else if (parentElement instanceof String) {
//					elements = plot
//							.getAllDependentSeries(parentElement.toString())
//							.toArray();
//				}
//
//				return elements != null ? elements : new Object[0];
//			}
//
//			@Override
//			public Object getParent(Object element) {
//				Object parent = null;
//
//				if (element instanceof String) {
//					parent = getPlot();
//				} else if (element instanceof ISeries) {
//					parent = ((ISeries) element).getCategory();
//				}
//
//				return parent;
//			}
//
//			@Override
//			public boolean hasChildren(Object element) {
//				boolean hasChildren = false;
//
//				IPlot plot = getPlot();
//
//				// The plot only has children if it has multiple categories or
//				// at least 1 series.
//				if (element == plot) {
//					hasChildren = !plot.getCategories().isEmpty();
//				}
//				// For a category, show its series.
//				else if (element instanceof String) {
//					hasChildren = !plot
//							.getAllDependentSeries(element.toString())
//							.isEmpty();
//				}
//				return hasChildren;
//			}
//
//		};
//	}
//
//	private ICheckStateProvider createCheckStateProvider() {
//		return new ICheckStateProvider() {
//			@Override
//			public boolean isGrayed(Object element) {
//				boolean grayed = false;
//				if (element instanceof String) {
//					List<ISeries> seriesList = getPlot()
//							.getAllDependentSeries(element.toString());
//					if (seriesList != null) {
//						for (ISeries series : seriesList) {
//							if (!series.enabled()) {
//								grayed = true;
//								break;
//							}
//						}
//					}
//				}
//				return grayed;
//			}
//
//			@Override
//			public boolean isChecked(Object element) {
//				boolean checked = false;
//				if (element instanceof String) {
//					List<ISeries> seriesList = getPlot()
//							.getAllDependentSeries(element.toString());
//					if (seriesList != null) {
//						for (ISeries series : seriesList) {
//							if (series.enabled()) {
//								checked = true;
//								break;
//							}
//						}
//					}
//				} else if (element instanceof ISeries) {
//					checked = ((ISeries) element).enabled();
//				}
//				return checked;
//			}
//		};
//	}
//
//	private ICheckStateListener createCheckStateListener() {
//		return new ICheckStateListener() {
//			@Override
//			public void checkStateChanged(CheckStateChangedEvent event) {
//				Object element = event.getElement();
//				boolean checked = event.getChecked();
//
//				IPlot plot = getPlot();
//				CheckboxTreeViewer viewer = checkboxTreeViewer;
//
//				if (element instanceof String) {
//					String category = element.toString();
//
//					// Enable/disable all series in the category.
//					for (ISeries series : plot
//							.getAllDependentSeries(category)) {
//						series.setEnabled(checked);
//						viewer.setChecked(series, series.enabled());
//					}
//					refreshCategory(category);
//				} else if (element instanceof ISeries) {
//					ISeries checkedSeries = (ISeries) element;
//					String category = checkedSeries.getCategory();
//
//					// Enable/disable the series.
//					checkedSeries.setEnabled(checked);
//					viewer.setChecked(checkedSeries, checkedSeries.enabled());
//					refreshCategory(category);
//				}
//
//				return;
//			}
//		};
//	}
//
//	private void refreshCategory(String category) {
//		CheckboxTreeViewer viewer = checkboxTreeViewer;
//		boolean checked = false;
//		boolean grayed = false;
//		for (ISeries series : plot.getAllDependentSeries(category)) {
//			if (series.enabled()) {
//				checked = true;
//				if (grayed) {
//					break;
//				}
//			} else {
//				grayed = true;
//				if (checked) {
//					break;
//				}
//			}
//		}
//		viewer.setChecked(category, checked);
//		viewer.setGrayed(category, grayed);
//		return;
//	}
//
//	private ILabelProvider createLabelProvider() {
//		return new LabelProvider() {
//			@Override
//			public String getText(Object element) {
//				String text = null;
//				if (element instanceof IPlot) {
//					text = ((IPlot) element).getPlotTitle();
//				} else if (element instanceof ISeries) {
//					text = ((ISeries) element).getLabel();
//				}
//				return text != null ? text : super.getText(element);
//			}
//		};
//	}

}
