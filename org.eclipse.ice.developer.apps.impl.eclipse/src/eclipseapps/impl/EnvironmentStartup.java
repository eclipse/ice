package eclipseapps.impl;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;

import apps.AppsFactory;
import apps.EnvironmentManager;
import eclipseapps.EclipseappsFactory;

public class EnvironmentStartup implements IStartup {

	@Override
	public void earlyStartup() {
		EnvironmentManager manager = AppsFactory.eINSTANCE.createEnvironmentManager();
		manager.setEnvironmentStorage(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentStorage());
		manager.setConsole(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentConsole());
		manager.loadEnvironments();
		manager.startAllStoppedEnvironments();
		
		PlatformUI.getWorkbench().addWorkbenchListener(new IWorkbenchListener() {
			@Override
			public boolean preShutdown(IWorkbench workbench, boolean forced) {
				manager.loadEnvironments();
				manager.stoppRunningEnvironments();
				manager.persistEnvironments();
				return true;				
			}

			@Override
			public void postShutdown(IWorkbench workbench) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
