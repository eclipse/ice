package org.eclipse.ice.item.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.eclipse.ice.item.jobLauncher.DockerClientFactory;
import org.junit.Before;
import org.junit.Test;

import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;

/**
 * This class tests the DockerClientFactory. 
 * 
 * @author Alex McCaskey
 *
 */
public class DockerClientFactoryTester {

	/**
	 * This flag triggers the test if Docker has been found 
	 * on the system. 
	 */
	private boolean dockerExistsOnThisSystem = false;
	
	/**
	 * 
	 */
	@Before
	public void before() {
		// FIXME set the docker exists flag
	}
	
	/**
	 * Check that the Docker Client can get a reference to the 
	 * underlying docker daemon.
	 */
	@Test
	public void checkGetDockerClient() {

		if (dockerExistsOnThisSystem) {
			System.out.println("DockerClientFactoryTest: Docker exists on this system - running test.");
			DockerClientFactory factory = new DockerClientFactory();
			DockerClient client = null;
			try {
				client = factory.getDockerClient();
			} catch (DockerCertificateException | IOException | InterruptedException e) {
				e.printStackTrace();
				fail();
			}
			System.out.println("DOCKER_HOST = " + client.getHost());
			assertNotNull(client);
		} else {
			System.out.println("DockerClientFactoryTest: Docker does not exist on this system - skipping test.");
			assertTrue(true);
		}
	}
}
