/**
 */
package apps.tests;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import apps.AppsFactory;
import apps.Environment;
import apps.ProjectLauncher;
import apps.SpackPackage;
import apps.impl.EnvironmentImpl;
import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Environment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link apps.Environment#launch() <em>Launch</em>}</li>
 *   <li>{@link apps.Environment#launchDerived() <em>Launch Derived</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class EnvironmentTest extends TestCase {

	private class FakeProjectLauncher implements ProjectLauncher {
		private boolean launched = false;

		public boolean wasLaunched() {
			return launched;
		}
		
		@Override
		public EClass eClass() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Resource eResource() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public EObject eContainer() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public EStructuralFeature eContainingFeature() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public EReference eContainmentFeature() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public EList<EObject> eContents() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public TreeIterator<EObject> eAllContents() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean eIsProxy() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public EList<EObject> eCrossReferences() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object eGet(EStructuralFeature feature) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object eGet(EStructuralFeature feature, boolean resolve) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void eSet(EStructuralFeature feature, Object newValue) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean eIsSet(EStructuralFeature feature) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void eUnset(EStructuralFeature feature) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object eInvoke(EOperation operation, EList<?> arguments) throws InvocationTargetException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public EList<Adapter> eAdapters() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean eDeliver() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void eSetDeliver(boolean deliver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void eNotify(Notification notification) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void launchProject(SpackPackage project) {
			// TODO Auto-generated method stub
			launched = true;
		}
		
	}
	
	private class DerivedEnvironment extends EnvironmentImpl {
		public DerivedEnvironment(boolean gen) {
			generateProject = gen;
		}
		public boolean launchDerived() {
			launched = true;
			return true;
		}
		
		@Override
		public void setProjectlauncher(ProjectLauncher launcher) {
			projectlauncher = launcher;
		}
		private boolean launched = false;
		
		public boolean wasLaunched () {
			return launched;
		}
	}
	/**
	 * The fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Environment fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(EnvironmentTest.class);
	}

	/**
	 * Constructs a new Environment test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(Environment fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Environment test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Environment getFixture() {
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
		setFixture(AppsFactory.eINSTANCE.createEnvironment());
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
	 * Tests the '{@link apps.Environment#launch() <em>Launch</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.Environment#launch()
	 */
	public void testLaunch() {
		DerivedEnvironment environmentWithProject = new DerivedEnvironment(true);
		FakeProjectLauncher projectLauncher = new FakeProjectLauncher();
		environmentWithProject.setProjectlauncher(projectLauncher);
		assertTrue(environmentWithProject.launch());
		assertTrue(environmentWithProject.wasLaunched());
		assertTrue(projectLauncher.wasLaunched());
		DerivedEnvironment environmentWithOutProject = new DerivedEnvironment(false);
		projectLauncher = new FakeProjectLauncher();
		environmentWithOutProject.setProjectlauncher(projectLauncher);
		assertTrue(environmentWithOutProject.launch());
		assertTrue(environmentWithOutProject.wasLaunched());
		assertFalse(projectLauncher.wasLaunched());
	}

	/**
	 * Tests the '{@link apps.Environment#launchDerived() <em>Launch Derived</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see apps.Environment#launchDerived()
	 */
	public void testLaunchDerived() {
		assertTrue(true);
	}

} //EnvironmentTest
