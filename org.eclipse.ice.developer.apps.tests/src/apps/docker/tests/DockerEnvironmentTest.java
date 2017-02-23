/**
 */
package apps.docker.tests;

import java.io.IOException;
import java.nio.file.Paths;

import apps.JsonEnvironmentCreator;
import apps.docker.ContainerConfiguration;
import apps.docker.DockerEnvironment;
import apps.docker.DockerFactory;
import apps.docker.impl.DockerAPIImpl;
import apps.tests.EnvironmentTest;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Environment</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class DockerEnvironmentTest extends EnvironmentTest {

	private static String jsonStr = "{\n" + "   \"General\": {\n" + "       \"name\": \"mccaskey/test_env\",\n"
			+ "       \"type\": \"Docker\"\n" + "    },\n" + "    \"Application\": {\n" + "       \"name\": \"xacc\",\n"
			+ "       \"repoURL\": \"https://github.com/ORNL-QCI/xacc\",\n" + "       \"compiler\": \"gcc@6.1.0\"\n"
			+ "     },\n" + "     \"Dependencies\": [\n" + "         {\n" + "           \"name\": \"cmake\",\n"
			+ "           \"compiler\": \"gcc@6.1.0\"\n" + "         },\n" + "         {\n"
			+ "           \"name\": \"llvm\",\n" + "           \"compiler\": \"gcc@6.1.0\"\n" + "         }\n"
			+ "      ],\n" + "      \"ContainerConfig\": {\n" + "         \"name\": \"xaccdev\",\n"
			+ "         \"ephemeral\": true\n" + "      }\n" + "}";

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(DockerEnvironmentTest.class);
	}

	/**
	 * Constructs a new Environment test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DockerEnvironmentTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected DockerEnvironment getFixture() {
		return (DockerEnvironment)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(DockerFactory.eINSTANCE.createDockerEnvironment());
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

	private class FakeDockerAPI extends DockerAPIImpl {
		private boolean built = false;
		private boolean launched = false;
		
		public boolean wasBuilt() {
			return built;
		}

		public boolean wasLaunched() {
			return launched;
		}
		
		/**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 */
		@Override
		public void buildImage(String buildDir, String imagename) {
			built = true;
		}

		/**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 */
		public void launchContainer(String name, ContainerConfiguration config) {
			launched = true;
		}
	}
	
	public void testLaunchDerived() {
		String expectedFile = "from eclipseice/base-fedora\n" + 
				"run /bin/bash -c \"spack compiler find && spack install cmake %gcc@6.1.0 && spack install llvm %gcc@6.1.0 \"\n" + 
				"run git clone --recursive -b master https://github.com/ORNL-QCI/xacc xacc\n"; 
		// Get a valid Environment
		DockerEnvironment env = (DockerEnvironment) JsonEnvironmentCreator.create(jsonStr);
		FakeDockerAPI api = new FakeDockerAPI();
		env.setDocker(api);
		env.launchDerived();
		assertTrue(api.wasBuilt());
		assertTrue(api.wasLaunched());
		assertTrue(expectedFile.contentEquals(env.getDockerFileContents()));
	}
} //DockerEnvironmentTest
