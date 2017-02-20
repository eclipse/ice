/**
 */
package apps.impl;

import apps.AppsPackage;
import apps.Environment;
import apps.EnvironmentType;
import apps.ProjectLauncher;
import apps.ScienceApp;
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
 *   <li>{@link apps.impl.EnvironmentImpl#getSpackpackage <em>Spackpackage</em>}</li>
 *   <li>{@link apps.impl.EnvironmentImpl#getScienceapp <em>Scienceapp</em>}</li>
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
	protected static final String OS_EDEFAULT = null;

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
	 * The cached value of the '{@link #getSpackpackage() <em>Spackpackage</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpackpackage()
	 * @generated
	 * @ordered
	 */
	protected EList<SpackPackage> spackpackage;

	/**
	 * The cached value of the '{@link #getScienceapp() <em>Scienceapp</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScienceapp()
	 * @generated
	 * @ordered
	 */
	protected ScienceApp scienceapp;

	/**
	 * The cached value of the '{@link #getProjectlauncher() <em>Projectlauncher</em>}' reference.
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
	public EList<SpackPackage> getSpackpackage() {
		if (spackpackage == null) {
			spackpackage = new EObjectContainmentEList<SpackPackage>(SpackPackage.class, this, AppsPackage.ENVIRONMENT__SPACKPACKAGE);
		}
		return spackpackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScienceApp getScienceapp() {
		if (scienceapp != null && scienceapp.eIsProxy()) {
			InternalEObject oldScienceapp = (InternalEObject)scienceapp;
			scienceapp = (ScienceApp)eResolveProxy(oldScienceapp);
			if (scienceapp != oldScienceapp) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AppsPackage.ENVIRONMENT__SCIENCEAPP, oldScienceapp, scienceapp));
			}
		}
		return scienceapp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScienceApp basicGetScienceapp() {
		return scienceapp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScienceapp(ScienceApp newScienceapp) {
		ScienceApp oldScienceapp = scienceapp;
		scienceapp = newScienceapp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__SCIENCEAPP, oldScienceapp, scienceapp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProjectLauncher getProjectlauncher() {
		if (projectlauncher != null && projectlauncher.eIsProxy()) {
			InternalEObject oldProjectlauncher = (InternalEObject)projectlauncher;
			projectlauncher = (ProjectLauncher)eResolveProxy(oldProjectlauncher);
			if (projectlauncher != oldProjectlauncher) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AppsPackage.ENVIRONMENT__PROJECTLAUNCHER, oldProjectlauncher, projectlauncher));
			}
		}
		return projectlauncher;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProjectLauncher basicGetProjectlauncher() {
		return projectlauncher;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProjectlauncher(ProjectLauncher newProjectlauncher) {
		ProjectLauncher oldProjectlauncher = projectlauncher;
		projectlauncher = newProjectlauncher;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.ENVIRONMENT__PROJECTLAUNCHER, oldProjectlauncher, projectlauncher));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean launch() {
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
			case AppsPackage.ENVIRONMENT__SPACKPACKAGE:
				return ((InternalEList<?>)getSpackpackage()).basicRemove(otherEnd, msgs);
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
			case AppsPackage.ENVIRONMENT__SPACKPACKAGE:
				return getSpackpackage();
			case AppsPackage.ENVIRONMENT__SCIENCEAPP:
				if (resolve) return getScienceapp();
				return basicGetScienceapp();
			case AppsPackage.ENVIRONMENT__PROJECTLAUNCHER:
				if (resolve) return getProjectlauncher();
				return basicGetProjectlauncher();
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
			case AppsPackage.ENVIRONMENT__SPACKPACKAGE:
				getSpackpackage().clear();
				getSpackpackage().addAll((Collection<? extends SpackPackage>)newValue);
				return;
			case AppsPackage.ENVIRONMENT__SCIENCEAPP:
				setScienceapp((ScienceApp)newValue);
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
			case AppsPackage.ENVIRONMENT__SPACKPACKAGE:
				getSpackpackage().clear();
				return;
			case AppsPackage.ENVIRONMENT__SCIENCEAPP:
				setScienceapp((ScienceApp)null);
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
			case AppsPackage.ENVIRONMENT__SPACKPACKAGE:
				return spackpackage != null && !spackpackage.isEmpty();
			case AppsPackage.ENVIRONMENT__SCIENCEAPP:
				return scienceapp != null;
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
		result.append(')');
		return result.toString();
	}

} //EnvironmentImpl
