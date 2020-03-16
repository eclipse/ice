package gov.ornl.rse.renderer;

import com.google.inject.AbstractModule;

public class BasicModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RendererRunner.class).toInstance(new RendererRunner());;
    }
}
