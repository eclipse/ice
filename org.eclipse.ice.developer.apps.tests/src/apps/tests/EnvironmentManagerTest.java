/**
 */
package apps.tests;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;

import apps.AppsFactory;
import apps.EnvironmentBuilder;
import apps.EnvironmentManager;
import apps.EnvironmentState;
import apps.IEnvironment;
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
 *   <li>{@link apps.EnvironmentManager#listAvailableSpackPackages() <em>List Available Spack Packages</em>}</li>
 *   <li>{@link apps.EnvironmentManager#persistEnvironments() <em>Persist Environments</em>}</li>
 *   <li>{@link apps.EnvironmentManager#createEmpty(java.lang.String) <em>Create Empty</em>}</li>
 *   <li>{@link apps.EnvironmentManager#loadFromXMI(java.lang.String) <em>Load From XMI</em>}</li>
 *   <li>{@link apps.EnvironmentManager#loadEnvironments() <em>Load Environments</em>}</li>
 *   <li>{@link apps.EnvironmentManager#startAllStoppedEnvironments() <em>Start All Stopped Environments</em>}</li>
 *   <li>{@link apps.EnvironmentManager#stopRunningEnvironments() <em>Stop Running Environments</em>}</li>
 *   <li>{@link apps.EnvironmentManager#deleteEnvironment(java.lang.String) <em>Delete Environment</em>}</li>
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
			IEnvironment env = null;
			try {
				env = EnvironmentBuilder.getEnvironmentBuilder("docker").build();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		IEnvironment env = fixture.create(jsonStr);
		String xmiStr = fixture.persistToString(env.getName());
		String expected = "<?xml version=\"1.0\" encoding=\"ASCII\"?>\n"
				+ "<dockerenvironment:DockerEnvironment xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:developerappstore=\"http://eclipse.org/ice/apps\" xmlns:dockerenvironment=\"http://eclipse.org/apps/docker\" name=\"mccaskey/test_env\" state=\"Created\">\n"
				+ "  <dependentPackages xsi:type=\"developerappstore:SpackPackage\" name=\"cmake\" type=\"Spack\"/>\n"
				+ "  <dependentPackages xsi:type=\"developerappstore:SpackPackage\" name=\"llvm\" type=\"Spack\"/>\n"
				+ "  <dependentPackages xsi:type=\"developerappstore:OSPackage\" name=\"gcc-gfortran\"/>\n"
				+ "  <primaryApp xsi:type=\"developerappstore:SourcePackage\" name=\"xacc\" type=\"Source\" repoURL=\"https://github.com/ORNL-QCI/xacc\" buildCommand=\"cd xacc &amp;&amp; mkdir build &amp;&amp; cd build &amp;&amp; cmake .. &amp;&amp; make\"/>\n"
				+ "  <console/>\n" + "  <docker/>\n"
				+ "  <containerConfiguration name=\"xaccdev\" ephemeral=\"true\"/>\n"
				+ "</dockerenvironment:DockerEnvironment>\n";
		assertEquals(expected, xmiStr);
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
	 * Tests the '{@link apps.EnvironmentManager#listAvailableSpackPackages() <em>List Available Spack Packages</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#listAvailableSpackPackages()
	 */
	public void testListAvailableSpackPackages() {
		EList<String> packages = fixture.listAvailableSpackPackages();
		for (String s : packages) {
			System.out.println(s);
		}
		// FIXME Come up with a test for this.
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
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#createEmpty(java.lang.String) <em>Create Empty</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#createEmpty(java.lang.String)
	 */
	public void testCreateEmpty__String() {
		FakeEnvironmentCreator creator = new FakeEnvironmentCreator();
		fixture.setEnvironmentCreator(creator);
		IEnvironment env = fixture.createEmpty("Docker");
		assertEquals("unknown", fixture.list().get(0));
		assertEquals(fixture.list().size(), 1);
		env.setName("newName");
		assertEquals(fixture.list().size(), 1);
		assertEquals("newName", fixture.list().get(0));
		assertEquals(env.getState(), EnvironmentState.NOT_CREATED);
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#loadFromXMI(java.lang.String) <em>Load From XMI</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#loadFromXMI(java.lang.String)
	 */
	public void testLoadFromXMI__String() {
		String xmiStr = "<?xml version=\"1.0\" encoding=\"ASCII\"?>\n" + 
				"<dockerenvironment:DockerEnvironment xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:developerappstore=\"http://eclipse.org/ice/apps\" xmlns:dockerenvironment=\"http://eclipse.org/apps/docker\" name=\"mccaskey/test_env\" state=\"Created\">\n" + 
				"  <dependentPackages xsi:type=\"developerappstore:SpackPackage\" name=\"cmake\" type=\"Spack\"/>\n" + 
				"  <dependentPackages xsi:type=\"developerappstore:SpackPackage\" name=\"llvm\" type=\"Spack\"/>\n" + 
				"  <dependentPackages xsi:type=\"developerappstore:OSPackage\" name=\"gcc-gfortran\"/>\n" + 
				"  <primaryApp xsi:type=\"developerappstore:SourcePackage\" name=\"xacc\" type=\"Source\" repoURL=\"https://github.com/ORNL-QCI/xacc\" buildCommand=\"cd xacc &amp;&amp; mkdir build &amp;&amp; cd build &amp;&amp; cmake .. &amp;&amp; make\"/>\n" + 
				"  <containerConfiguration name=\"xaccdev\" ephemeral=\"true\"/>\n" + 
				"</dockerenvironment:DockerEnvironment>\n";
		
		IEnvironment env = fixture.loadFromXMI(xmiStr);
		assertEquals(fixture.list().size(), 1);
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
		assertTrue(true);
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#startAllStoppedEnvironments() <em>Start All Stopped Environments</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#startAllStoppedEnvironments()
	 */
	public void testStartAllStoppedEnvironments() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		assertTrue(true);
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#stopRunningEnvironments() <em>Stop Running Environments</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#stopRunningEnvironments()
	 */
	public void testStopRunningEnvironments() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		assertTrue(true);
	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#deleteEnvironment(java.lang.String) <em>Delete Environment</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.EnvironmentManager#deleteEnvironment(java.lang.String)
	 */
	public void testDeleteEnvironment__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		assertTrue(true);
	}

} // EnvironmentManagerTest
