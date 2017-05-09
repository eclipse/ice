package eclipseapps.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.ice.docker.api.DockerAPI;
import org.eclipse.ice.docker.api.DockerapiFactory;
import org.eclipse.ice.docker.api.spotify.SpotifyFactory;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;

import apps.AppsFactory;
import apps.EnvironmentConsole;
import apps.EnvironmentManager;
import eclipseapps.EclipseappsFactory;

public class EnvironmentStartup implements IStartup {

	private DockerAPI api = null;
	
	@Override
	public void earlyStartup() {
		
		// Make sure we have the proper Docker Images...
		String baseICEImage = "eclipseice/base-fedora:latest";
		
		try {
			api = SpotifyFactory.eINSTANCE.createSpotifyDockerClient();
		} catch (Exception e) {
			e.printStackTrace();
			api = null;
		}

		if (api != null) {
//			EnvironmentConsole console = EclipseappsFactory.eINSTANCE.createEclipseEnvironmentConsole();
//			api.setEnvironmentConsole(console);
			Thread dockerPullThread = new Thread(new Runnable() {
				@Override
				public void run() {
					EList<String> images = api.listAvailableImages();
					if (!images.contains(baseICEImage)) {
//						console.print(baseICEImage + " image not found locally. Pulling from Dockerhub...");
						api.pull(baseICEImage);
					}
				}
				
			});
			dockerPullThread.start();
		}
		
		
		EnvironmentManager manager = AppsFactory.eINSTANCE.createEnvironmentManager();
		manager.setEnvironmentStorage(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentStorage());
		manager.setConsole(EclipseappsFactory.eINSTANCE.createEclipseEnvironmentConsole());
		manager.loadEnvironments();
		manager.startAllStoppedEnvironments();
		
		PlatformUI.getWorkbench().addWorkbenchListener(new IWorkbenchListener() {
			@Override
			public boolean preShutdown(IWorkbench workbench, boolean forced) {
				manager.loadEnvironments();
				manager.stopRunningEnvironments();
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
