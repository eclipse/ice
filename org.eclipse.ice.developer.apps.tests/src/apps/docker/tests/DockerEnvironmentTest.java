/**
 */
package apps.docker.tests;

import junit.framework.TestCase;
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

	private class FakeDockerAPI extends DockerAPIImpl {
		private boolean built = false;

		public boolean wasBuilt() {
			return built;
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 */
		@Override
		public void buildImage(String buildDir, String imagename) {
			built = true;
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
		FakeDockerAPI api = new FakeDockerAPI();
		env.setDocker(api);

		env.build();
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
