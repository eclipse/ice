/**
 */
package apps.local.impl;

import apps.EnvironmentType;
import apps.ScienceApp;
import apps.SpackPackage;

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
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getType <em>Type</em>}</li>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getName <em>Name</em>}</li>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getOs <em>Os</em>}</li>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getSpackpackage <em>Spackpackage</em>}</li>
 *   <li>{@link apps.local.impl.LocalEnvironmentImpl#getScienceapp <em>Scienceapp</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LocalEnvironmentImpl extends MinimalEObjectImpl.Container implements LocalEnvironment {
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
			eNotify(new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__TYPE, oldType, type));
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
	public EList<SpackPackage> getSpackpackage() {
		if (spackpackage == null) {
			spackpackage = new EObjectContainmentEList<SpackPackage>(SpackPackage.class, this, LocalPackage.LOCAL_ENVIRONMENT__SPACKPACKAGE);
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LocalPackage.LOCAL_ENVIRONMENT__SCIENCEAPP, oldScienceapp, scienceapp));
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
			eNotify(new ENotificationImpl(this, Notification.SET, LocalPackage.LOCAL_ENVIRONMENT__SCIENCEAPP, oldScienceapp, scienceapp));
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
			case LocalPackage.LOCAL_ENVIRONMENT__SPACKPACKAGE:
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
			case LocalPackage.LOCAL_ENVIRONMENT__TYPE:
				return getType();
			case LocalPackage.LOCAL_ENVIRONMENT__NAME:
				return getName();
			case LocalPackage.LOCAL_ENVIRONMENT__OS:
				return getOs();
			case LocalPackage.LOCAL_ENVIRONMENT__SPACKPACKAGE:
				return getSpackpackage();
			case LocalPackage.LOCAL_ENVIRONMENT__SCIENCEAPP:
				if (resolve) return getScienceapp();
				return basicGetScienceapp();
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
			case LocalPackage.LOCAL_ENVIRONMENT__TYPE:
				setType((EnvironmentType)newValue);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__NAME:
				setName((String)newValue);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__OS:
				setOs((String)newValue);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__SPACKPACKAGE:
				getSpackpackage().clear();
				getSpackpackage().addAll((Collection<? extends SpackPackage>)newValue);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__SCIENCEAPP:
				setScienceapp((ScienceApp)newValue);
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
			case LocalPackage.LOCAL_ENVIRONMENT__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__OS:
				setOs(OS_EDEFAULT);
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__SPACKPACKAGE:
				getSpackpackage().clear();
				return;
			case LocalPackage.LOCAL_ENVIRONMENT__SCIENCEAPP:
				setScienceapp((ScienceApp)null);
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
			case LocalPackage.LOCAL_ENVIRONMENT__TYPE:
				return type != TYPE_EDEFAULT;
			case LocalPackage.LOCAL_ENVIRONMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case LocalPackage.LOCAL_ENVIRONMENT__OS:
				return OS_EDEFAULT == null ? os != null : !OS_EDEFAULT.equals(os);
			case LocalPackage.LOCAL_ENVIRONMENT__SPACKPACKAGE:
				return spackpackage != null && !spackpackage.isEmpty();
			case LocalPackage.LOCAL_ENVIRONMENT__SCIENCEAPP:
				return scienceapp != null;
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
			case LocalPackage.LOCAL_ENVIRONMENT___LAUNCH:
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

} //LocalEnvironmentImpl
