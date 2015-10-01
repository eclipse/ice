package org.eclipse.ice.developer.actions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.internal.util.ProjectUtil;
import org.eclipse.egit.core.op.CloneOperation;
import org.eclipse.egit.core.op.CloneOperation.PostCloneTask;
import org.eclipse.egit.ui.Activator;
import org.eclipse.egit.ui.internal.UIText;
import org.eclipse.egit.ui.internal.clone.ProjectRecord;
import org.eclipse.egit.ui.internal.clone.ProjectUtils;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class GitHubCloneHandler extends AbstractHandler {

	private boolean noProjects;

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(GitHubCloneHandler.class);

	public GitHubCloneHandler() {
		noProjects = false;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Get the repository
		String repo = (String) event.getParameters().get("repoURLID");

		// Create the Job to be executed
		final Job job = new Job("Cloning " + repo) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				// Create the workspace file
				File workspaceFile = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()
						+ System.getProperty("file.separator")
						+ repo.substring(repo.lastIndexOf("/") + 1, repo.length()));

				try {
					CloneOperation cloneOp = new CloneOperation(new URIish(repo), true, null, workspaceFile, "master",
							"origin", 100);
					cloneOp.addPostCloneTask(new PostCloneTask() {

						@Override
						public void execute(Repository repository, IProgressMonitor monitor) throws CoreException {
							importProjects(repository, new IWorkingSet[0]);
							if (noProjects) {
								Display.getDefault().asyncExec(new Runnable() {
									public void run() {
										Shell shell = HandlerUtil.getActiveShell(event);
										openWizard("org.eclipse.egit.ui.internal.clone.GitCloneWizard", shell);
									}
								});
							}
						}

					});
					cloneOp.run(monitor);
				} catch (URISyntaxException | InvocationTargetException | InterruptedException e) {
					e.printStackTrace();
				}

				return Status.OK_STATUS;
			}
		};

		job.schedule();
		return null;
	}

	private void importProjects(final Repository repository, final IWorkingSet[] sets) {
		String repoName = org.eclipse.egit.ui.Activator.getDefault().getRepositoryUtil().getRepositoryName(repository);
		Job importJob = new WorkspaceJob(MessageFormat.format(UIText.GitCloneWizard_jobImportProjects, repoName)) {

			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				List<File> files = new ArrayList<File>();
				ProjectUtil.findProjectFiles(files, repository.getWorkTree(), true, monitor);
				if (files.isEmpty()) {
					noProjects = true;
					return Status.OK_STATUS;
				}

				Set<ProjectRecord> records = new LinkedHashSet<ProjectRecord>();
				for (File file : files) {
					records.add(new ProjectRecord(file));
				}
				try {
					ProjectUtils.createProjects(records, sets, monitor);
				} catch (InvocationTargetException e) {
					Activator.logError(e.getLocalizedMessage(), e);
				} catch (InterruptedException e) {
					Activator.logError(e.getLocalizedMessage(), e);
				}
				return Status.OK_STATUS;
			}
		};
		importJob.schedule();
	}

	public void openWizard(String id, Shell shell) {
		// First see if this is a "new wizard".
		IWizardDescriptor descriptor = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(id);
		// If not check if it is an "import wizard".
		if (descriptor == null) {
			descriptor = PlatformUI.getWorkbench().getImportWizardRegistry().findWizard(id);
		}
		// Or maybe an export wizard
		if (descriptor == null) {
			descriptor = PlatformUI.getWorkbench().getExportWizardRegistry().findWizard(id);
		}
		try {
			// Then if we have a wizard, open it.
			if (descriptor != null) {
				IWizard wizard = descriptor.createWizard();
				WizardDialog wd = new WizardDialog(shell, wizard);
				wd.setTitle(wizard.getWindowTitle());
				wd.open();
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
