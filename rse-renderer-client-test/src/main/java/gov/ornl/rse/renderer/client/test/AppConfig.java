package gov.ornl.rse.renderer.client.test;

import org.eclipse.ice.renderer.Renderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public VaadinRendererClient<String> vaadinRendererClient() {
		return new VaadinRendererClient<String>();
	}
	
	@Bean
	public Renderer<VaadinRendererClient<String>,String> renderer() {
		return new Renderer<VaadinRendererClient<String>,String>();
	}
	
	@Bean
	public Object object() {
		return new Object();
	}
	
}
