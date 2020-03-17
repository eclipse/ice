package org.eclipse.ice.renderer;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Launcher {

	public static void main(String[] args) {
		
	    Injector injector = Guice.createInjector(new BasicModule());
	    RendererRunner runner  = injector.getInstance(RendererRunner.class);
		
		runner.run();
	}

}
