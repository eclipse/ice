/**
 */
package apps.docker.tests;

import junit.framework.TestCase;
import apps.JsonEnvironmentCreator;
import apps.docker.ContainerConfiguration;
import apps.docker.DockerEnvironment;
import apps.docker.DockerFactory;
import apps.docker.impl.DockerAPIImpl;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Environment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.IEnvironment#build() <em>Build</em>}</li>
 *   <li>{@link apps.IEnvironment#connect() <em>Connect</em>}</li>
 *   <li>{@link apps.IEnvironment#delete() <em>Delete</em>}</li>
 *   <li>{@link apps.IEnvironment#stop() <em>Stop</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class DockerEnvironmentTest extends TestCase {

	/**
	 * The fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerEnvironment fixture = null;
	private static String jsonStr = "{\n" + "   \"General\": {\n" + "       \"name\": \"mccaskey/test_env\",\n"
			+ "       \"type\": \"Docker\"\n" + "    },\n" + "    \"Application\": {\n" + "       \"name\": \"xacc\",\n"
			+ "       \"repoURL\": \"https://github.com/ORNL-QCI/xacc\",\n" + "       \"compiler\": \"gcc@6.1.0\"\n"
			+ "     },\n" + "     \"Dependencies\": [\n" + "         {\n" + "           \"name\": \"cmake\",\n"
			+ "           \"compiler\": \"gcc@6.3.1\"\n" + "         },\n" + "         {\n"
			+ "           \"name\": \"llvm\",\n" + "           \"compiler\": \"gcc@6.3.1\"\n" + "         }\n"
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
	 * Sets the fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(DockerEnvironment fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerEnvironment getFixture() {
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

	/**
	 * Tests the '{@link apps.IEnvironment#build() <em>Build</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.IEnvironment#build()
	 * @generated
	 */
	public void testBuild() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

	/**
	 * Tests the '{@link apps.IEnvironment#connect() <em>Connect</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.IEnvironment#connect()
	 * @generated
	 */
	public void testConnect() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

	/**
	 * Tests the '{@link apps.IEnvironment#delete() <em>Delete</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.IEnvironment#delete()
	 * @generated
	 */
	public void testDelete() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

	/**
	 * Tests the '{@link apps.IEnvironment#stop() <em>Stop</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.IEnvironment#stop()
	 * @generated
	 */
	public void testStop() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
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
				"run /bin/bash -c \"spack compiler find && spack install cmake %gcc@6.3.1 && spack install llvm %gcc@6.3.1 \"\n" + 
				"run git clone --recursive -b master https://github.com/ORNL-QCI/xacc xacc\n"; 
		// Get a valid Environment
		DockerEnvironment env = (DockerEnvironment) JsonEnvironmentCreator.create(jsonStr);
		FakeDockerAPI api = new FakeDockerAPI();
		env.setDocker(api);
//		env.launchDerived();
//		assertTrue(api.wasBuilt());
//		assertTrue(api.wasLaunched());
//		assertTrue(expectedFile.contentEquals(env.getDockerFileContents()));
	}
} //DockerEnvironmentTest
