/**
 */
package apps.impl;

import apps.AppsPackage;
import apps.Environment;
import apps.EnvironmentType;
import apps.ProjectLauncher;
import apps.SpackPackage;

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
 *   <li>{@link apps.impl.EnvironmentImpl#getType <em>Type</em>}</li>
 *   <li>{@link apps.impl.EnvironmentImpl#getName <em>Name</em>}</li>
 *   <li>{@link apps.impl.EnvironmentImpl#getOs <em>Os</em>}</li>
 *   <li>{@link apps.impl.EnvironmentImpl#getDependentPackages <em>Dependent Packages</em>}</li>
 *   <li>{@link apps.impl.EnvironmentImpl#isDevelopmentEnvironment <em>Development Environment</em>}</li>
 *   <li>{@link apps.impl.EnvironmentImpl#isGenerateProject <em>Generate Project</em>}</li>
 *   <li>{@link apps.impl.EnvironmentImpl#getPrimaryApp <em>Primary App</em>}</li>
 *   <li>{@link apps.impl.EnvironmentImpl#getProjectlauncher <em>Projectlauncher</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnvironmentImpl extends MinimalEObjectImpl.Container implements Environment {
	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final EnvironmentType TYPE_EDEFAULT = EnvironmentType.DOCKER;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected EnvironmentType type = TYPE_EDEFAULT;

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
	protected EList<SpackPackage> dependentPackages;

	/**
	 * The default value of the '{@link #isDevelopmentEnvironment() <em>Development Environment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDevelopmentEnvironment()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DEVELOPMENT_ENVIRONMENT_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isDevelopmentEnvironment() <em>Development Environment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDevelopmentEnvironment()
	 * @generated
	 * @ordered
	 */
	protected boolean developmentEnvironment = DEVELOPMENT_ENVIRONMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #isGenerateProject() <em>Generate Project</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGenerateProject()
	 * @generated
	 * @ordered
	 */
	protected static final boolean GENERATE_PROJECT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isGenerateProject() <em>Generate Project</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGenerateProject()
	 * @generated
	 * @ordered
	 */
	protected boolean generateProject = GENERATE_PROJECT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPrimaryApp() <em>Primary App</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryApp()
	 * @generated
	 * @ordered
	 */
	protected SpackPackage primaryApp;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnvironmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.ENVIRONMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvironmentType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(EnvironmentType newType) {
		EnvironmentType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__TYPE, oldType, type));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__OS, oldOs, os));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SpackPackage> getDependentPackages() {
		if (dependentPackages == null) {
			dependentPackages = new EObjectContainmentEList<SpackPackage>(SpackPackage.class, this, AppsPackage.ENVIRONMENT__DEPENDENT_PACKAGES);
		}
		return dependentPackages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDevelopmentEnvironment() {
		return developmentEnvironment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDevelopmentEnvironment(boolean newDevelopmentEnvironment) {
		boolean oldDevelopmentEnvironment = developmentEnvironment;
		developmentEnvironment = newDevelopmentEnvironment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__DEVELOPMENT_ENVIRONMENT, oldDevelopmentEnvironment, developmentEnvironment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isGenerateProject() {
		return generateProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGenerateProject(boolean newGenerateProject) {
		boolean oldGenerateProject = generateProject;
		generateProject = newGenerateProject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__GENERATE_PROJECT, oldGenerateProject, generateProject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpackPackage getPrimaryApp() {
		return primaryApp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPrimaryApp(SpackPackage newPrimaryApp, NotificationChain msgs) {
		SpackPackage oldPrimaryApp = primaryApp;
		primaryApp = newPrimaryApp;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__PRIMARY_APP, oldPrimaryApp, newPrimaryApp);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrimaryApp(SpackPackage newPrimaryApp) {
		if (newPrimaryApp != primaryApp) {
			NotificationChain msgs = null;
			if (primaryApp != null)
				msgs = ((InternalEObject)primaryApp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AppsPackage.ENVIRONMENT__PRIMARY_APP, null, msgs);
			if (newPrimaryApp != null)
				msgs = ((InternalEObject)newPrimaryApp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AppsPackage.ENVIRONMENT__PRIMARY_APP, null, msgs);
			msgs = basicSetPrimaryApp(newPrimaryApp, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__PRIMARY_APP, newPrimaryApp, newPrimaryApp));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__PROJECTLAUNCHER, oldProjectlauncher, newProjectlauncher);
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
				msgs = ((InternalEObject)projectlauncher).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AppsPackage.ENVIRONMENT__PROJECTLAUNCHER, null, msgs);
			if (newProjectlauncher != null)
				msgs = ((InternalEObject)newProjectlauncher).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AppsPackage.ENVIRONMENT__PROJECTLAUNCHER, null, msgs);
			msgs = basicSetProjectlauncher(newProjectlauncher, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__PROJECTLAUNCHER, newProjectlauncher, newProjectlauncher));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean launch() {
		
		// Execute the subclasses launch
		boolean success = launchDerived();
		
		// Make sure it worked
		if (!success) {
			return false;
		}
		
		// Generate the project if we need too
		if (generateProject) {
			projectlauncher.launchProject(primaryApp);
		}
		
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean launchDerived() {
		throw new UnsupportedOperationException("Cannot execute Environment.launchDerived. Execute Environment.launch instead.");
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AppsPackage.ENVIRONMENT__DEPENDENT_PACKAGES:
				return ((InternalEList<?>)getDependentPackages()).basicRemove(otherEnd, msgs);
			case AppsPackage.ENVIRONMENT__PRIMARY_APP:
				return basicSetPrimaryApp(null, msgs);
			case AppsPackage.ENVIRONMENT__PROJECTLAUNCHER:
				return basicSetProjectlauncher(null, msgs);
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
			case AppsPackage.ENVIRONMENT__TYPE:
				return getType();
			case AppsPackage.ENVIRONMENT__NAME:
				return getName();
			case AppsPackage.ENVIRONMENT__OS:
				return getOs();
			case AppsPackage.ENVIRONMENT__DEPENDENT_PACKAGES:
				return getDependentPackages();
			case AppsPackage.ENVIRONMENT__DEVELOPMENT_ENVIRONMENT:
				return isDevelopmentEnvironment();
			case AppsPackage.ENVIRONMENT__GENERATE_PROJECT:
				return isGenerateProject();
			case AppsPackage.ENVIRONMENT__PRIMARY_APP:
				return getPrimaryApp();
			case AppsPackage.ENVIRONMENT__PROJECTLAUNCHER:
				return getProjectlauncher();
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
			case AppsPackage.ENVIRONMENT__TYPE:
				setType((EnvironmentType)newValue);
				return;
			case AppsPackage.ENVIRONMENT__NAME:
				setName((String)newValue);
				return;
			case AppsPackage.ENVIRONMENT__OS:
				setOs((String)newValue);
				return;
			case AppsPackage.ENVIRONMENT__DEPENDENT_PACKAGES:
				getDependentPackages().clear();
				getDependentPackages().addAll((Collection<? extends SpackPackage>)newValue);
				return;
			case AppsPackage.ENVIRONMENT__DEVELOPMENT_ENVIRONMENT:
				setDevelopmentEnvironment((Boolean)newValue);
				return;
			case AppsPackage.ENVIRONMENT__GENERATE_PROJECT:
				setGenerateProject((Boolean)newValue);
				return;
			case AppsPackage.ENVIRONMENT__PRIMARY_APP:
				setPrimaryApp((SpackPackage)newValue);
				return;
			case AppsPackage.ENVIRONMENT__PROJECTLAUNCHER:
				setProjectlauncher((ProjectLauncher)newValue);
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
			case AppsPackage.ENVIRONMENT__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case AppsPackage.ENVIRONMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AppsPackage.ENVIRONMENT__OS:
				setOs(OS_EDEFAULT);
				return;
			case AppsPackage.ENVIRONMENT__DEPENDENT_PACKAGES:
				getDependentPackages().clear();
				return;
			case AppsPackage.ENVIRONMENT__DEVELOPMENT_ENVIRONMENT:
				setDevelopmentEnvironment(DEVELOPMENT_ENVIRONMENT_EDEFAULT);
				return;
			case AppsPackage.ENVIRONMENT__GENERATE_PROJECT:
				setGenerateProject(GENERATE_PROJECT_EDEFAULT);
				return;
			case AppsPackage.ENVIRONMENT__PRIMARY_APP:
				setPrimaryApp((SpackPackage)null);
				return;
			case AppsPackage.ENVIRONMENT__PROJECTLAUNCHER:
				setProjectlauncher((ProjectLauncher)null);
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
			case AppsPackage.ENVIRONMENT__TYPE:
				return type != TYPE_EDEFAULT;
			case AppsPackage.ENVIRONMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AppsPackage.ENVIRONMENT__OS:
				return OS_EDEFAULT == null ? os != null : !OS_EDEFAULT.equals(os);
			case AppsPackage.ENVIRONMENT__DEPENDENT_PACKAGES:
				return dependentPackages != null && !dependentPackages.isEmpty();
			case AppsPackage.ENVIRONMENT__DEVELOPMENT_ENVIRONMENT:
				return developmentEnvironment != DEVELOPMENT_ENVIRONMENT_EDEFAULT;
			case AppsPackage.ENVIRONMENT__GENERATE_PROJECT:
				return generateProject != GENERATE_PROJECT_EDEFAULT;
			case AppsPackage.ENVIRONMENT__PRIMARY_APP:
				return primaryApp != null;
			case AppsPackage.ENVIRONMENT__PROJECTLAUNCHER:
				return projectlauncher != null;
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
			case AppsPackage.ENVIRONMENT___LAUNCH:
				return launch();
			case AppsPackage.ENVIRONMENT___LAUNCH_DERIVED:
				return launchDerived();
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
		result.append(" (type: ");
		result.append(type);
		result.append(", name: ");
		result.append(name);
		result.append(", os: ");
		result.append(os);
		result.append(", developmentEnvironment: ");
		result.append(developmentEnvironment);
		result.append(", generateProject: ");
		result.append(generateProject);
		result.append(')');
		return result.toString();
	}

} //EnvironmentImpl
