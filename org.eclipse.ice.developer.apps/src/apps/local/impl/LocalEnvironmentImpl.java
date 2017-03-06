/**
 */
package apps.local.impl;

import apps.EnvironmentConsole;
import apps.EnvironmentState;
import apps.ProjectLauncher;
import apps.local.LocalEnvironment;
import apps.local.LocalPackage;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Environment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getName <em>Name</em>}</li>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getOs <em>Os</em>}</li>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getDependentPackages <em>Dependent Packages</em>}</li>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getPrimaryApp <em>Primary App</em>}</li>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getProjectlauncher <em>Projectlauncher</em>}</li>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getState <em>State</em>}</li>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getConsole <em>Console</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LocalEnvironmentImpl extends MinimalEObjectImpl.Container implements LocalEnvironment {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getOs() <em>Os</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOs()
	 * @generated
	 * @ordered
	 */
	protected static final String OS_EDEFAULT = "fedora";

	/**
	 * The cached value of the '{@link #getOs() <em>Os</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOs()
	 * @generated
	 * @ordered
	 */
	protected String os = OS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDependentPackages() <em>Dependent Packages</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependentPackages()
	 * @generated
	 * @ordered
	 */
	protected EList<apps.Package> dependentPackages;

	/**
	 * The cached value of the '{@link #getPrimaryApp() <em>Primary App</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryApp()
	 * @generated
	 * @ordered
	 */
	protected apps.Package primaryApp;

	/**
	 * The cached value of the '{@link #getProjectlauncher() <em>Projectlauncher</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProjectlauncher()
	 * @generated
	 * @ordered
	 */
	protected ProjectLauncher projectlauncher;

	/**
	 * The default value of the '{@link #getState() <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getState()
	 * @generated
	 * @ordered
	 */
	protected static final EnvironmentState STATE_EDEFAULT = EnvironmentState.NOT_CREATED;

	/**
	 * The cached value of the '{@link #getState() <em>State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getState()
	 * @generated
	 * @ordered
	 */
	protected EnvironmentState state = STATE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getConsole() <em>Console</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConsole()
	 * @generated
	 * @ordered
	 */
	protected EnvironmentConsole console;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalEnvironmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LocalPackage.Literals.LOCAL_ENVIRONMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOs() {
		return os;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOs(String newOs) {
		String oldOs = os;
		os = newOs;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__OS, oldOs, os));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<apps.Package> getDependentPackages() {
		if (dependentPackages == null) {
			dependentPackages = new EObjectContainmentEList<apps.Package>(apps.Package.class, this, LocalPackage.LOCAL_ENVIRONMENT__DEPENDENT_PACKAGES);
		}
		return dependentPackages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public apps.Package getPrimaryApp() {
		return primaryApp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPrimaryApp(apps.Package newPrimaryApp, NotificationChain msgs) {
		apps.Package oldPrimaryApp = primaryApp;
		primaryApp = newPrimaryApp;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__PRIMARY_APP, oldPrimaryApp, newPrimaryApp);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrimaryApp(apps.Package newPrimaryApp) {
		if (newPrimaryApp != primaryApp) {
			NotificationChain msgs = null;
			if (primaryApp != null)
				msgs = ((InternalEObject)primaryApp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LocalPackage.LOCAL_ENVIRONMENT__PRIMARY_APP, null, msgs);
			if (newPrimaryApp != null)
				msgs = ((InternalEObject)newPrimaryApp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LocalPackage.LOCAL_ENVIRONMENT__PRIMARY_APP, null, msgs);
			msgs = basicSetPrimaryApp(newPrimaryApp, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__PRIMARY_APP, newPrimaryApp, newPrimaryApp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProjectLauncher getProjectlauncher() {
		return projectlauncher;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProjectlauncher(ProjectLauncher newProjectlauncher, NotificationChain msgs) {
		ProjectLauncher oldProjectlauncher = projectlauncher;
		projectlauncher = newProjectlauncher;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__PROJECTLAUNCHER, oldProjectlauncher, newProjectlauncher);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProjectlauncher(ProjectLauncher newProjectlauncher) {
		if (newProjectlauncher != projectlauncher) {
			NotificationChain msgs = null;
			if (projectlauncher != null)
				msgs = ((InternalEObject)projectlauncher).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LocalPackage.LOCAL_ENVIRONMENT__PROJECTLAUNCHER, null, msgs);
			if (newProjectlauncher != null)
				msgs = ((InternalEObject)newProjectlauncher).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LocalPackage.LOCAL_ENVIRONMENT__PROJECTLAUNCHER, null, msgs);
			msgs = basicSetProjectlauncher(newProjectlauncher, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__PROJECTLAUNCHER, newProjectlauncher, newProjectlauncher));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentState getState() {
		return state;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setState(EnvironmentState newState) {
		EnvironmentState oldState = state;
		state = newState == null ? STATE_EDEFAULT : newState;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__STATE, oldState, state));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentConsole getConsole() {
		return console;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConsole(EnvironmentConsole newConsole, NotificationChain msgs) {
		EnvironmentConsole oldConsole = console;
		console = newConsole;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__CONSOLE, oldConsole, newConsole);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConsole(EnvironmentConsole newConsole) {
		if (newConsole != console) {
			NotificationChain msgs = null;
			if (console != null)
				msgs = ((InternalEObject)console).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LocalPackage.LOCAL_ENVIRONMENT__CONSOLE, null, msgs);
			if (newConsole != null)
				msgs = ((InternalEObject)newConsole).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LocalPackage.LOCAL_ENVIRONMENT__CONSOLE, null, msgs);
			msgs = basicSetConsole(newConsole, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__CONSOLE, newConsole, newConsole));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean build() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean connect() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean delete() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean stop() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LocalPackage.LOCAL_ENVIRONMENT__DEPENDENT_PACKAGES:
				return ((InternalEList<?>)getDependentPackages()).basicRemove(otherEnd, msgs);
			case LocalPackage.LOCAL_ENVIRONMENT__PRIMARY_APP:
				return basicSetPrimaryApp(null, msgs);
			case LocalPackage.LOCAL_ENVIRONMENT__PROJECTLAUNCHER:
				return basicSetProjectlauncher(null, msgs);
			case LocalPackage.LOCAL_ENVIRONMENT__CONSOLE:
				return basicSetConsole(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case LocalPackage.LOCAL_ENVIRONMENT__NAME:
				return getName();
			case LocalPackage.LOCAL_ENVIRONMENT__OS:
				return getOs();
			case LocalPackage.LOCAL_ENVIRONMENT__DEPENDENT_PACKAGES:
				return getDependentPackages();
			case LocalPackage.LOCAL_ENVIRONMENT__PRIMARY_APP:
				return getPrimaryApp();
			case LocalPackage.LOCAL_ENVIRONMENT__PROJECTLAUNCHER:
				return getProjectlauncher();
			case LocalPackage.LOCAL_ENVIRONMENT__STATE:
				return getState();
			case LocalPackage.LOCAL_ENVIRONMENT__CONSOLE:
				return getConsole();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case LocalPackage.LOCAL_ENVIRONMENT__NAME:
				setName((String)newValue);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__OS:
				setOs((String)newValue);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__DEPENDENT_PACKAGES:
				getDependentPackages().clear();
				getDependentPackages().addAll((Collection<? extends apps.Package>)newValue);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__PRIMARY_APP:
				setPrimaryApp((apps.Package)newValue);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__PROJECTLAUNCHER:
				setProjectlauncher((ProjectLauncher)newValue);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__STATE:
				setState((EnvironmentState)newValue);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__CONSOLE:
				setConsole((EnvironmentConsole)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case LocalPackage.LOCAL_ENVIRONMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__OS:
				setOs(OS_EDEFAULT);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__DEPENDENT_PACKAGES:
				getDependentPackages().clear();
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__PRIMARY_APP:
				setPrimaryApp((apps.Package)null);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__PROJECTLAUNCHER:
				setProjectlauncher((ProjectLauncher)null);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__STATE:
				setState(STATE_EDEFAULT);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__CONSOLE:
				setConsole((EnvironmentConsole)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case LocalPackage.LOCAL_ENVIRONMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case LocalPackage.LOCAL_ENVIRONMENT__OS:
				return OS_EDEFAULT == null ? os != null : !OS_EDEFAULT.equals(os);
			case LocalPackage.LOCAL_ENVIRONMENT__DEPENDENT_PACKAGES:
				return dependentPackages != null && !dependentPackages.isEmpty();
			case LocalPackage.LOCAL_ENVIRONMENT__PRIMARY_APP:
				return primaryApp != null;
			case LocalPackage.LOCAL_ENVIRONMENT__PROJECTLAUNCHER:
				return projectlauncher != null;
			case LocalPackage.LOCAL_ENVIRONMENT__STATE:
				return state != STATE_EDEFAULT;
			case LocalPackage.LOCAL_ENVIRONMENT__CONSOLE:
				return console != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case LocalPackage.LOCAL_ENVIRONMENT___BUILD:
				return build();
			case LocalPackage.LOCAL_ENVIRONMENT___CONNECT:
				return connect();
			case LocalPackage.LOCAL_ENVIRONMENT___DELETE:
				return delete();
			case LocalPackage.LOCAL_ENVIRONMENT___STOP:
				return stop();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", os: ");
		result.append(os);
		result.append(", state: ");
		result.append(state);
		result.append(')');
		return result.toString();
	}

} //LocalEnvironmentImpl
