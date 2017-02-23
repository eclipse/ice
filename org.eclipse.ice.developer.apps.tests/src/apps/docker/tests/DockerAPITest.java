/**
 */
package apps.docker.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import apps.docker.DockerAPI;
import apps.docker.DockerFactory;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>API</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.docker.DockerAPI#buildImage(java.lang.String, java.lang.String) <em>Build Image</em>}</li>
 *   <li>{@link apps.docker.DockerAPI#launchContainer(java.lang.String, apps.docker.ContainerConfiguration) <em>Launch Container</em>}</li>
 *   <li>{@link apps.docker.DockerAPI#connectToExistingContainer(java.lang.String) <em>Connect To Existing Container</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class DockerAPITest extends TestCase {

	/**
	 * The fixture for this API test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerAPI fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(DockerAPITest.class);
	}

	/**
	 * Constructs a new API test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerAPITest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this API test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(DockerAPI fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this API test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerAPI getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(DockerFactory.eINSTANCE.createDockerAPI());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

	/**
	 * Tests the
	 * '{@link apps.docker.DockerAPI#buildImage(java.lang.String, java.lang.String)
	 * <em>Build Image</em>}' operation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see apps.docker.DockerAPI#buildImage(java.lang.String, java.lang.String)
	 */
	public void testBuildImage__String_String() {
		String dockerFileContents = "from eclipseice/base-fedora\n"
				+ "run /bin/bash -c \"source /root/.bashrc && spack compiler find && spack install --fake cmake\"\n"
				+ "run git clone --recursive -b master https://github.com/ORNL-QCI/xacc xacc\n";

		System.out.println("DockerFile:\n" + dockerFileContents);

		// Create the Dockerfile
		File buildFile = new File(
				System.getProperty("user.dir") + System.getProperty("file.separator") + ".tmpDockerbuild/Dockerfile");
		try {
			FileUtils.writeStringToFile(buildFile, dockerFileContents);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Build the Image
		fixture.buildImage(buildFile.getParent(), "test/test_image");
		DefaultDockerClient.Builder builder = null;
		try {
			builder = DefaultDockerClient.fromEnv();
		} catch (DockerCertificateException e) {
			e.printStackTrace();
			fail();
		}

		DefaultDockerClient dockerClient = builder.build();

		try {
			dockerClient.removeImage("test/test_image");
		} catch (DockerException | InterruptedException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Tests the '{@link apps.docker.DockerAPI#launchContainer(java.lang.String, apps.docker.ContainerConfiguration) <em>Launch Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.DockerAPI#launchContainer(java.lang.String, apps.docker.ContainerConfiguration)
	 */
	@Ignore
	public void testLaunchContainer__String_ContainerConfiguration() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
//		fail();
	}

	/**
	 * Tests the '{@link apps.docker.DockerAPI#connectToExistingContainer(java.lang.String) <em>Connect To Existing Container</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.DockerAPI#connectToExistingContainer(java.lang.String)
	 */
	@Ignore
	public void testConnectToExistingContainer__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
//		fail();
	}

} //DockerAPITest
