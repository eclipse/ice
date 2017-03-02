/**
 */
package apps.docker.impl;

import apps.LanguageProjectProvider;
import apps.SourcePackage;

import apps.docker.ContainerConfiguration;
import apps.docker.DockerPackage;
import apps.docker.DockerProjectLauncher;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project Launcher</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.docker.impl.DockerProjectLauncherImpl#getLanguageprojectprovider <em>Languageprojectprovider</em>}</li>
 *   <li>{@link apps.docker.impl.DockerProjectLauncherImpl#getContainerconfiguration <em>Containerconfiguration</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DockerProjectLauncherImpl extends MinimalEObjectImpl.Container implements DockerProjectLauncher {
	/**
	 * The cached value of the '{@link #getLanguageprojectprovider() <em>Languageprojectprovider</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanguageprojectprovider()
	 * @generated
	 * @ordered
	 */
	protected LanguageProjectProvider languageprojectprovider;
	/**
	 * The cached value of the '{@link #getContainerconfiguration() <em>Containerconfiguration</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainerconfiguration()
	 * @generated
	 * @ordered
	 */
	protected ContainerConfiguration containerconfiguration;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DockerProjectLauncherImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DockerPackage.Literals.DOCKER_PROJECT_LAUNCHER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LanguageProjectProvider getLanguageprojectprovider() {
		return languageprojectprovider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLanguageprojectprovider(LanguageProjectProvider newLanguageprojectprovider, NotificationChain msgs) {
		LanguageProjectProvider oldLanguageprojectprovider = languageprojectprovider;
		languageprojectprovider = newLanguageprojectprovider;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER, oldLanguageprojectprovider, newLanguageprojectprovider);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLanguageprojectprovider(LanguageProjectProvider newLanguageprojectprovider) {
		if (newLanguageprojectprovider != languageprojectprovider) {
			NotificationChain msgs = null;
			if (languageprojectprovider != null)
				msgs = ((InternalEObject)languageprojectprovider).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER, null, msgs);
			if (newLanguageprojectprovider != null)
				msgs = ((InternalEObject)newLanguageprojectprovider).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DockerPackage.DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER, null, msgs);
			msgs = basicSetLanguageprojectprovider(newLanguageprojectprovider, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER, newLanguageprojectprovider, newLanguageprojectprovider));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContainerConfiguration getContainerconfiguration() {
		if (containerconfiguration != null && containerconfiguration.eIsProxy()) {
			InternalEObject oldContainerconfiguration = (InternalEObject)containerconfiguration;
			containerconfiguration = (ContainerConfiguration)eResolveProxy(oldContainerconfiguration);
			if (containerconfiguration != oldContainerconfiguration) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DockerPackage.DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION, oldContainerconfiguration, containerconfiguration));
			}
		}
		return containerconfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContainerConfiguration basicGetContainerconfiguration() {
		return containerconfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContainerconfiguration(ContainerConfiguration newContainerconfiguration) {
		ContainerConfiguration oldContainerconfiguration = containerconfiguration;
		containerconfiguration = newContainerconfiguration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION, oldContainerconfiguration, containerconfiguration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public boolean launchProject(SourcePackage project) {
		throw new UnsupportedOperationException("This method is not meant to be called. Implementations left to subclasses.");
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DockerPackage.DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER:
				return basicSetLanguageprojectprovider(null, msgs);
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
			case DockerPackage.DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER:
				return getLanguageprojectprovider();
			case DockerPackage.DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION:
				if (resolve) return getContainerconfiguration();
				return basicGetContainerconfiguration();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DockerPackage.DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER:
				setLanguageprojectprovider((LanguageProjectProvider)newValue);
				return;
			case DockerPackage.DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION:
				setContainerconfiguration((ContainerConfiguration)newValue);
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
			case DockerPackage.DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER:
				setLanguageprojectprovider((LanguageProjectProvider)null);
				return;
			case DockerPackage.DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION:
				setContainerconfiguration((ContainerConfiguration)null);
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
			case DockerPackage.DOCKER_PROJECT_LAUNCHER__LANGUAGEPROJECTPROVIDER:
				return languageprojectprovider != null;
			case DockerPackage.DOCKER_PROJECT_LAUNCHER__CONTAINERCONFIGURATION:
				return containerconfiguration != null;
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
			case DockerPackage.DOCKER_PROJECT_LAUNCHER___LAUNCH_PROJECT__SOURCEPACKAGE:
				launchProject((SourcePackage)arguments.get(0));
				return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //DockerProjectLauncherImpl
