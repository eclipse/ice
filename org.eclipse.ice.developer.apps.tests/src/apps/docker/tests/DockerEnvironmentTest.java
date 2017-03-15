/**
 */
package apps.docker.tests;

import junit.framework.TestCase;

import com.spotify.docker.client.exceptions.DockerCertificateException;

import apps.AppsFactory;
import apps.docker.DockerEnvironment;
import apps.docker.DockerFactory;
import apps.docker.impl.DockerAPIImpl;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc --> A test case for the model object
 * '<em><b>Environment</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.docker.DockerEnvironment#execute(java.lang.String, java.lang.String[]) <em>Execute</em>}</li>
 *   <li>{@link apps.docker.DockerEnvironment#hasDocker() <em>Has Docker</em>}</li>
 *   <li>{@link apps.docker.DockerEnvironment#pullImage(java.lang.String) <em>Pull Image</em>}</li>
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
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	protected DockerEnvironment fixture = null;
	private static String jsonStr = "{\n" + "   \"General\": {\n" + "       \"name\": \"mccaskey/test_env\",\n"
			+ "       \"type\": \"Docker\"\n" + "    },\n" + "    \"Application\": {\n"
			+ "       \"type\": \"Source\",\n" + "       \"name\": \"xacc\",\n"
			+ "       \"repoURL\": \"https://github.com/ORNL-QCI/xacc\",\n"
			+ "       \"buildCommand\": \"cd xacc && mkdir build && cd build && cmake .. && make\"\n" + "     },\n"
			+ "     \"Dependencies\": [\n" + "         {\n" + "           \"type\": \"Spack\",\n"
			+ "           \"name\": \"cmake\",\n" + "           \"compiler\": \"gcc@6.3.1\"\n" + "         },\n"
			+ "         {\n" + "           \"type\": \"Spack\",\n" + "           \"name\": \"llvm\",\n"
			+ "           \"compiler\": \"gcc@6.3.1\"\n" + "         },\n" + "         {\n"
			+ "           \"type\": \"OS\",\n" + "           \"name\": \"gcc-gfortran\"\n" + "         }\n"
			+ "      ],\n" + "      \"ContainerConfig\": {\n" + "         \"name\": \"xaccdev\",\n"
			+ "         \"ephemeral\": true\n" + "      }\n" + "}";

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(DockerEnvironmentTest.class);
	}

	/**
	 * Constructs a new Environment test case with the given name. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
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
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerEnvironment getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(DockerFactory.eINSTANCE.createDockerEnvironment());
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

	/**
	 * Tests the '{@link apps.docker.DockerEnvironment#execute(java.lang.String, java.lang.String[]) <em>Execute</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.DockerEnvironment#execute(java.lang.String, java.lang.String[])
	 * @generated
	 */
	public void testExecute__String_String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

	/**
	 * Tests the '{@link apps.docker.DockerEnvironment#hasDocker() <em>Has Docker</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.DockerEnvironment#hasDocker()
	 * @generated
	 */
	public void testHasDocker() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

	/**
	 * Tests the '{@link apps.docker.DockerEnvironment#pullImage(java.lang.String) <em>Pull Image</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.docker.DockerEnvironment#pullImage(java.lang.String)
	 * @generated
	 */
	public void testPullImage__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

	private class FakeDockerAPI extends DockerAPIImpl {
		protected FakeDockerAPI() throws DockerCertificateException {
			super();
		}

		private boolean built = false;

		public boolean wasBuilt() {
			return built;
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 */
		@Override
		public boolean buildImage(String buildDir, String imagename) {
			built = true;
			return true;
		}

	}

	/**
	 * Tests the '{@link apps.IEnvironment#build() <em>Build</em>}' operation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see apps.IEnvironment#build()
	 */
	public void testBuild() {
		String expectedFile = "from eclipseice/base-fedora\n"
				+ "run /bin/bash -c \"spack compiler find && spack install cmake %gcc@6.3.1 && spack install llvm %gcc@6.3.1 \"\n"
				+ "run git clone --recursive -b master https://github.com/ORNL-QCI/xacc xacc\n";
		// Get a valid Environment
		DockerEnvironment env = (DockerEnvironment) AppsFactory.eINSTANCE.createEnvironmentManager().create(jsonStr);
		FakeDockerAPI api = null;
		try {
			api = new FakeDockerAPI();
		} catch (DockerCertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		assertNotNull(api);
		env.setDocker(api);

		assertTrue(env.build());
		assertTrue(api.wasBuilt());
		assertTrue(expectedFile.contentEquals(env.getDockerfile()));
	}

	/**
	 * Tests the '{@link apps.IEnvironment#connect() <em>Connect</em>}'
	 * operation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see apps.IEnvironment#connect()
	 */
	public void testConnect() {
	}

	/**
	 * Tests the '{@link apps.IEnvironment#delete() <em>Delete</em>}' operation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see apps.IEnvironment#delete()
	 */
	public void testDelete() {
	}

	/**
	 * Tests the '{@link apps.IEnvironment#stop() <em>Stop</em>}' operation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see apps.IEnvironment#stop()
	 */
	public void testStop() {
	}

} // DockerEnvironmentTest
