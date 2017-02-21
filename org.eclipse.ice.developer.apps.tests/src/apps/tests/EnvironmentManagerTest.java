/**
 */
package apps.tests;

import org.junit.Ignore;

import apps.AppsFactory;
import apps.EnvironmentManager;
import apps.EnvironmentType;
import apps.IEnvironment;
import apps.JsonEnvironmentCreator;
import apps.docker.DockerEnvironment;
import apps.local.LocalEnvironment;
import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc --> A test case for the model object
 * '<em><b>Manager</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 * <li>{@link apps.EnvironmentManager#createEnvironment(java.lang.String)
 * <em>Create Environment</em>}</li>
 * <li>{@link apps.EnvironmentManager#listExistingEnvironments() <em>List
 * Existing Environments</em>}</li>
 * <li>{@link apps.EnvironmentManager#loadExistingEnvironment(java.lang.String)
 * <em>Load Existing Environment</em>}</li>
 * <li>{@link apps.EnvironmentManager#loadEnvironmentFromFile(java.lang.String)
 * <em>Load Environment From File</em>}</li>
 * <li>{@link apps.EnvironmentManager#persistToXMIString(apps.IEnvironment)
 * <em>Persist To XMI String</em>}</li>
 * <li>{@link apps.EnvironmentManager#persistXMIToFile(apps.IEnvironment, java.lang.String)
 * <em>Persist XMI To File</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class EnvironmentManagerTest extends TestCase {

	private static String jsonStr = "{\n" + "   \"General\": {\n" + "       \"name\": \"mccaskey/test_env\",\n"
			+ "       \"type\": \"Docker\"\n" + "    },\n" + "    \"Application\": {\n" + "       \"name\": \"xacc\",\n"
			+ "       \"repoURL\": \"https://github.com/ORNL-QCI/xacc\",\n" + "       \"compiler\": \"gcc@6.1.0\"\n"
			+ "     },\n" + "     \"Dependencies\": [\n" + "         {\n" + "           \"name\": \"cmake\",\n"
			+ "           \"compiler\": \"gcc@6.1.0\"\n" + "         },\n" + "         {\n"
			+ "           \"name\": \"llvm\",\n" + "           \"compiler\": \"gcc@6.1.0\"\n" + "         }\n"
			+ "      ],\n" + "      \"ContainerConfig\": {\n" + "         \"name\": \"xaccdev\",\n"
			+ "         \"ephemeral\": true\n" + "      }\n" + "}";
	/**
	 * The fixture for this Environment Manager test case. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EnvironmentManager fixture = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
	 * 
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(AppsFactory.eINSTANCE.createEnvironmentManager());
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

	/**
	 * Tests the
	 * '{@link apps.EnvironmentManager#createEnvironment(java.lang.String)
	 * <em>Create Environment</em>}' operation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see apps.EnvironmentManager#createEnvironment(java.lang.String)
	 */
	public void testCreateEnvironment__String() {
		IEnvironment env = fixture.createEnvironment("Docker");
		assertTrue(env instanceof DockerEnvironment);
		assertEquals(env.getType(), EnvironmentType.DOCKER);

		env = fixture.createEnvironment("Local");
		assertTrue(env instanceof LocalEnvironment);
		assertEquals(env.getType(), EnvironmentType.LOCAL);

		try {
			fixture.createEnvironment("bad_string");
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
			return;
		}

		fail();

	}

	/**
	 * Tests the '{@link apps.EnvironmentManager#listExistingEnvironments()
	 * <em>List Existing Environments</em>}' operation. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see apps.EnvironmentManager#listExistingEnvironments()
	 */
	@Ignore
	public void testListExistingEnvironments() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		// fail();
	}

	/**
	 * Tests the
	 * '{@link apps.EnvironmentManager#loadExistingEnvironment(java.lang.String)
	 * <em>Load Existing Environment</em>}' operation. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see apps.EnvironmentManager#loadExistingEnvironment(java.lang.String)
	 */
	@Ignore
	public void testLoadExistingEnvironment__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		// fail();
	}

	/**
	 * Tests the
	 * '{@link apps.EnvironmentManager#loadEnvironmentFromFile(java.lang.String)
	 * <em>Load Environment From File</em>}' operation. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see apps.EnvironmentManager#loadEnvironmentFromFile(java.lang.String)
	 */
	public void testLoadEnvironmentFromFile__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		// fail();
	}

	/**
	 * Tests the
	 * '{@link apps.EnvironmentManager#persistToXMIString(apps.IEnvironment)
	 * <em>Persist To XMI String</em>}' operation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see apps.EnvironmentManager#persistToXMIString(apps.IEnvironment)
	 */
	public void testPersistToXMIString__IEnvironment() {
		// Get a valid Environment
		IEnvironment env = JsonEnvironmentCreator.create(jsonStr);
		String expectedXmiStr = "<?xml version=\"1.0\" encoding=\"ASCII\"?>\n" + 
				"<dockerenvironment:DockerEnvironment xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:dockerenvironment=\"http://eclipse.org/apps/docker\" name=\"mccaskey/test_env\">\n" + 
				"  <dependentPackages name=\"cmake\" compiler=\"gcc@6.1.0\"/>\n" + 
				"  <dependentPackages name=\"llvm\" compiler=\"gcc@6.1.0\"/>\n" + 
				"  <primaryApp name=\"xacc\" compiler=\"gcc@6.1.0\" repoURL=\"https://github.com/ORNL-QCI/xacc\"/>\n" + 
				"  <containerConfiguration name=\"xaccdev\" ephemeral=\"true\"/>\n" + 
				"</dockerenvironment:DockerEnvironment>\n";
		String xmiStr = fixture.persistToXMIString(env);
		assertEquals(xmiStr, expectedXmiStr);
	}

	/**
	 * Tests the
	 * '{@link apps.EnvironmentManager#persistXMIToFile(apps.IEnvironment, java.lang.String)
	 * <em>Persist XMI To File</em>}' operation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see apps.EnvironmentManager#persistXMIToFile(apps.IEnvironment,
	 *      java.lang.String)
	 * @generated
	 */
	public void testPersistXMIToFile__IEnvironment_String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		// fail();
	}

} // EnvironmentManagerTest
