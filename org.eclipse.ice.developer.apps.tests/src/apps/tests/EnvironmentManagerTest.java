/**
 */
package apps.tests;

import apps.AppsFactory;
import apps.EnvironmentManager;
import apps.EnvironmentState;
import apps.IEnvironment;
import apps.docker.DockerEnvironment;
import apps.docker.DockerFactory;
import apps.impl.JsonEnvironmentCreatorImpl;
import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc --> A test case for the model object
 * '<em><b>Manager</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.EnvironmentManager#create(java.lang.String) <em>Create</em>}</li>
 *   <li>{@link apps.EnvironmentManager#list() <em>List</em>}</li>
 *   <li>{@link apps.EnvironmentManager#get(java.lang.String) <em>Get</em>}</li>
 *   <li>{@link apps.EnvironmentManager#loadFromFile(java.lang.String) <em>Load From File</em>}</li>
 *   <li>{@link apps.EnvironmentManager#persistToString(java.lang.String) <em>Persist To String</em>}</li>
 *   <li>{@link apps.EnvironmentManager#persistToFile(java.lang.String, java.lang.String) <em>Persist To File</em>}</li>
 *   <li>{@link apps.EnvironmentManager#connect(java.lang.String) <em>Connect</em>}</li>
 *   <li>{@link apps.EnvironmentManager#listAvailableSpackPackages() <em>List Available Spack Packages</em>}</li>
 *   <li>{@link apps.EnvironmentManager#persistEnvironments() <em>Persist Environments</em>}</li>
 *   <li>{@link apps.EnvironmentManager#createEmpty(java.lang.String) <em>Create Empty</em>}</li>
 *   <li>{@link apps.EnvironmentManager#loadFromXMI(java.lang.String) <em>Load From XMI</em>}</li>
 *   <li>{@link apps.EnvironmentManager#loadEnvironments() <em>Load Environments</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class EnvironmentManagerTest extends TestCase {

	private static String jsonStr = "{\n" + 
			"   \"General\": {\n" + 
			"       \"name\": \"mccaskey/test_env\",\n" + 
			"       \"type\": \"Docker\"\n" + 
			"    },\n" + 
			"    \"Application\": {\n" + 
			"       \"type\": \"Source\",\n" + 
			"       \"name\": \"xacc\",\n" + 
			"       \"repoURL\": \"https://github.com/ORNL-QCI/xacc\",\n" + 
			"       \"buildCommand\": \"cd xacc && mkdir build && cd build && cmake .. && make\"\n" + 
			"     },\n" + 
			"     \"Dependencies\": [\n" + 
			"         {\n" + 
			"           \"type\": \"Spack\",\n" + 
			"           \"name\": \"cmake\",\n" + 
			"           \"compiler\": \"gcc@6.3.1\"\n" + 
			"         },\n" + 
			"         {\n" + 
			"           \"type\": \"Spack\",\n" + 
			"           \"name\": \"llvm\",\n" + 
			"           \"compiler\": \"gcc@6.3.1\"\n" + 
			"         },\n" + 
			"         {\n" + 
			"           \"type\": \"OS\",\n" + 
			"           \"name\": \"gcc-gfortran\"\n" + 
			"         }\n" + 
			"      ],\n" + 
			"      \"ContainerConfig\": {\n" + 
			"         \"name\": \"xaccdev\",\n" + 
			"         \"ephemeral\": true\n" + 
			"      }\n" + 
			"}";
	
	/**
	 * The fixture for this Environment Manager test case.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EnvironmentManager fixture = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(EnvironmentManagerTest.class);
	}

	/**
	 * Constructs a new Environment Manager test case with the given name. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EnvironmentManagerTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Environment Manager test case. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void setFixture(EnvironmentManager fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Environment Manager test case. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EnvironmentManager getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(AppsFactory.eINSTANCE.createEnvironmentManager());
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

	private class FakeEnvironmentCreator extends JsonEnvironmentCreatorImpl {
		private boolean envCreated = false;
		public IEnvironment create(String dataString) {
			IEnvironment env = DockerFactory.eINSTANCE.createDockerEnvironment();
			env.setName("fake");
			envCreated = true;
			return env;
		}
		public boolean wasCreated() { return envCreated;}
	}
	
	/**
	 * Tests the '{@link apps.EnvironmentManager#create(java.lang.String) <em>Create</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#create(java.lang.String)
	 */
	public void testCreate__String() {
		FakeEnvironmentCreator creator = new FakeEnvironmentCreator();
		fixture.setEnvironmentCreator(creator);
		IEnvironment env = fixture.create("");
		assertTrue(env instanceof DockerEnvironment);
		assertEquals(env.getState(), EnvironmentState.CREATED);
		assertTrue(creator.wasCreated());
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#list() <em>List</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#list()
	 */
	public void testList() {
		FakeEnvironmentCreator creator = new FakeEnvironmentCreator();
		fixture.setEnvironmentCreator(creator);
		IEnvironment env = fixture.create("");
		assertTrue(fixture.list().size() == 1);
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#get(java.lang.String) <em>Get</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#get(java.lang.String)
	 */
	public void testGet__String() {
		FakeEnvironmentCreator creator = new FakeEnvironmentCreator();
		fixture.setEnvironmentCreator(creator);
		IEnvironment env = fixture.create("");
		assertEquals(env, fixture.get("fake"));
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#loadFromFile(java.lang.String) <em>Load From File</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#loadFromFile(java.lang.String)
	 */
	public void testLoadFromFile__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		//fail();
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#persistToString(java.lang.String) <em>Persist To String</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#persistToString(java.lang.String)
	 */
	public void testPersistToString__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		//fail();
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#persistToFile(java.lang.String, java.lang.String) <em>Persist To File</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#persistToFile(java.lang.String, java.lang.String)
	 */
	public void testPersistToFile__String_String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		//fail();
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#connect(java.lang.String) <em>Connect</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#connect(java.lang.String)
	 */
	public void testConnect__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		//fail();
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#listAvailableSpackPackages() <em>List Available Spack Packages</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#listAvailableSpackPackages()
	 */
	public void testListAvailableSpackPackages() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		//fail();
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#persistEnvironments() <em>Persist Environments</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#persistEnvironments()
	 */
	public void testPersistEnvironments() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		//fail();
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#createEmpty(java.lang.String) <em>Create Empty</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#createEmpty(java.lang.String)
	 */
	public void testCreateEmpty__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		//fail();
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#loadFromXMI(java.lang.String) <em>Load From XMI</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#loadFromXMI(java.lang.String)
	 */
	public void testLoadFromXMI__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		//fail();
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#loadEnvironments() <em>Load Environments</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#loadEnvironments()
	 */
	public void testLoadEnvironments() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		//fail();
	}

} // EnvironmentManagerTest
