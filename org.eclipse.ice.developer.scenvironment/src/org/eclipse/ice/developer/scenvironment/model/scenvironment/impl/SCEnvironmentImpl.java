/**
 */
package org.eclipse.ice.developer.scenvironment.model.scenvironment.impl;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerEnum;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.InstallerTypeConfiguration;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.SCEnvironment;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.ScenvironmentPackage;
import org.eclipse.ice.developer.scenvironment.model.scenvironment.SpackPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SC Environment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl#getImageName <em>Image Name</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl#getAvailableOSs <em>Available OSs</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl#getAvailablePackages <em>Available Packages</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl#getSelectedPackages <em>Selected Packages</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl#getAddedFiles <em>Added Files</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl#getInstallerType <em>Installer Type</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl#getConfigurationType <em>Configuration Type</em>}</li>
 *   <li>{@link org.eclipse.ice.developer.scenvironment.model.scenvironment.impl.SCEnvironmentImpl#getSpackpackage <em>Spackpackage</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SCEnvironmentImpl extends MinimalEObjectImpl.Container implements SCEnvironment {
	/**
	 * The default value of the '{@link #getImageName() <em>Image Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImageName()
	 * @generated
	 * @ordered
	 */
	protected static final String IMAGE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getImageName() <em>Image Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImageName()
	 * @generated
	 * @ordered
	 */
	protected String imageName = IMAGE_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAvailableOSs() <em>Available OSs</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailableOSs()
	 * @generated
	 * @ordered
	 */
	protected EList<String> availableOSs;

	/**
	 * The cached value of the '{@link #getAvailablePackages() <em>Available Packages</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailablePackages()
	 * @generated
	 * @ordered
	 */
	protected EList<Object> availablePackages;

	/**
	 * The cached value of the '{@link #getSelectedPackages() <em>Selected Packages</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectedPackages()
	 * @generated
	 * @ordered
	 */
	protected EList<Object> selectedPackages;

	/**
	 * The cached value of the '{@link #getAddedFiles() <em>Added Files</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddedFiles()
	 * @generated
	 * @ordered
	 */
	protected EList<String> addedFiles;

	/**
	 * The default value of the '{@link #getInstallerType() <em>Installer Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInstallerType()
	 * @generated
	 * @ordered
	 */
	protected static final InstallerEnum INSTALLER_TYPE_EDEFAULT = InstallerEnum.FILE_SYSTEM;

	/**
	 * The cached value of the '{@link #getInstallerType() <em>Installer Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInstallerType()
	 * @generated
	 * @ordered
	 */
	protected InstallerEnum installerType = INSTALLER_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getConfigurationType() <em>Configuration Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfigurationType()
	 * @generated
	 * @ordered
	 */
	protected InstallerTypeConfiguration configurationType;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SCEnvironmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenvironmentPackage.Literals.SC_ENVIRONMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setImageName(String newImageName) {
		String oldImageName = imageName;
		imageName = newImageName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenvironmentPackage.SC_ENVIRONMENT__IMAGE_NAME, oldImageName, imageName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getAvailableOSs() {
		if (availableOSs == null) {
			availableOSs = new EDataTypeUniqueEList<String>(String.class, this, ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_OSS);
		}
		return availableOSs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Object> getAvailablePackages() {
		if (availablePackages == null) {
			availablePackages = new EDataTypeUniqueEList<Object>(SpackPackage.class, this, ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_PACKAGES);
		}
		return availablePackages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Object> getSelectedPackages() {
		if (selectedPackages == null) {
			selectedPackages = new EDataTypeUniqueEList<Object>(SpackPackage.class, this, ScenvironmentPackage.SC_ENVIRONMENT__SELECTED_PACKAGES);
		}
		return selectedPackages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getAddedFiles() {
		if (addedFiles == null) {
			addedFiles = new EDataTypeUniqueEList<String>(String.class, this, ScenvironmentPackage.SC_ENVIRONMENT__ADDED_FILES);
		}
		return addedFiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InstallerEnum getInstallerType() {
		return installerType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInstallerType(InstallerEnum newInstallerType) {
		InstallerEnum oldInstallerType = installerType;
		installerType = newInstallerType == null ? INSTALLER_TYPE_EDEFAULT : newInstallerType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenvironmentPackage.SC_ENVIRONMENT__INSTALLER_TYPE, oldInstallerType, installerType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InstallerTypeConfiguration getConfigurationType() {
		return configurationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConfigurationType(InstallerTypeConfiguration newConfigurationType, NotificationChain msgs) {
		InstallerTypeConfiguration oldConfigurationType = configurationType;
		configurationType = newConfigurationType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenvironmentPackage.SC_ENVIRONMENT__CONFIGURATION_TYPE, oldConfigurationType, newConfigurationType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConfigurationType(InstallerTypeConfiguration newConfigurationType) {
		if (newConfigurationType != configurationType) {
			NotificationChain msgs = null;
			if (configurationType != null)
				msgs = ((InternalEObject)configurationType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenvironmentPackage.SC_ENVIRONMENT__CONFIGURATION_TYPE, null, msgs);
			if (newConfigurationType != null)
				msgs = ((InternalEObject)newConfigurationType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenvironmentPackage.SC_ENVIRONMENT__CONFIGURATION_TYPE, null, msgs);
			msgs = basicSetConfigurationType(newConfigurationType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenvironmentPackage.SC_ENVIRONMENT__CONFIGURATION_TYPE, newConfigurationType, newConfigurationType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SpackPackage> getSpackpackage() {
		if (spackpackage == null) {
			spackpackage = new EObjectContainmentEList<SpackPackage>(SpackPackage.class, this, ScenvironmentPackage.SC_ENVIRONMENT__SPACKPACKAGE);
		}
		return spackpackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Object> searchPackages() {
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
			case ScenvironmentPackage.SC_ENVIRONMENT__CONFIGURATION_TYPE:
				return basicSetConfigurationType(null, msgs);
			case ScenvironmentPackage.SC_ENVIRONMENT__SPACKPACKAGE:
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
			case ScenvironmentPackage.SC_ENVIRONMENT__IMAGE_NAME:
				return getImageName();
			case ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_OSS:
				return getAvailableOSs();
			case ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_PACKAGES:
				return getAvailablePackages();
			case ScenvironmentPackage.SC_ENVIRONMENT__SELECTED_PACKAGES:
				return getSelectedPackages();
			case ScenvironmentPackage.SC_ENVIRONMENT__ADDED_FILES:
				return getAddedFiles();
			case ScenvironmentPackage.SC_ENVIRONMENT__INSTALLER_TYPE:
				return getInstallerType();
			case ScenvironmentPackage.SC_ENVIRONMENT__CONFIGURATION_TYPE:
				return getConfigurationType();
			case ScenvironmentPackage.SC_ENVIRONMENT__SPACKPACKAGE:
				return getSpackpackage();
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
			case ScenvironmentPackage.SC_ENVIRONMENT__IMAGE_NAME:
				setImageName((String)newValue);
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_OSS:
				getAvailableOSs().clear();
				getAvailableOSs().addAll((Collection<? extends String>)newValue);
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_PACKAGES:
				getAvailablePackages().clear();
				getAvailablePackages().addAll((Collection<? extends Object>)newValue);
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__SELECTED_PACKAGES:
				getSelectedPackages().clear();
				getSelectedPackages().addAll((Collection<? extends Object>)newValue);
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__ADDED_FILES:
				getAddedFiles().clear();
				getAddedFiles().addAll((Collection<? extends String>)newValue);
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__INSTALLER_TYPE:
				setInstallerType((InstallerEnum)newValue);
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__CONFIGURATION_TYPE:
				setConfigurationType((InstallerTypeConfiguration)newValue);
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__SPACKPACKAGE:
				getSpackpackage().clear();
				getSpackpackage().addAll((Collection<? extends SpackPackage>)newValue);
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
			case ScenvironmentPackage.SC_ENVIRONMENT__IMAGE_NAME:
				setImageName(IMAGE_NAME_EDEFAULT);
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_OSS:
				getAvailableOSs().clear();
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_PACKAGES:
				getAvailablePackages().clear();
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__SELECTED_PACKAGES:
				getSelectedPackages().clear();
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__ADDED_FILES:
				getAddedFiles().clear();
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__INSTALLER_TYPE:
				setInstallerType(INSTALLER_TYPE_EDEFAULT);
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__CONFIGURATION_TYPE:
				setConfigurationType((InstallerTypeConfiguration)null);
				return;
			case ScenvironmentPackage.SC_ENVIRONMENT__SPACKPACKAGE:
				getSpackpackage().clear();
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
			case ScenvironmentPackage.SC_ENVIRONMENT__IMAGE_NAME:
				return IMAGE_NAME_EDEFAULT == null ? imageName != null : !IMAGE_NAME_EDEFAULT.equals(imageName);
			case ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_OSS:
				return availableOSs != null && !availableOSs.isEmpty();
			case ScenvironmentPackage.SC_ENVIRONMENT__AVAILABLE_PACKAGES:
				return availablePackages != null && !availablePackages.isEmpty();
			case ScenvironmentPackage.SC_ENVIRONMENT__SELECTED_PACKAGES:
				return selectedPackages != null && !selectedPackages.isEmpty();
			case ScenvironmentPackage.SC_ENVIRONMENT__ADDED_FILES:
				return addedFiles != null && !addedFiles.isEmpty();
			case ScenvironmentPackage.SC_ENVIRONMENT__INSTALLER_TYPE:
				return installerType != INSTALLER_TYPE_EDEFAULT;
			case ScenvironmentPackage.SC_ENVIRONMENT__CONFIGURATION_TYPE:
				return configurationType != null;
			case ScenvironmentPackage.SC_ENVIRONMENT__SPACKPACKAGE:
				return spackpackage != null && !spackpackage.isEmpty();
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
			case ScenvironmentPackage.SC_ENVIRONMENT___SEARCH_PACKAGES:
				return searchPackages();
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
		result.append(" (imageName: ");
		result.append(imageName);
		result.append(", availableOSs: ");
		result.append(availableOSs);
		result.append(", availablePackages: ");
		result.append(availablePackages);
		result.append(", selectedPackages: ");
		result.append(selectedPackages);
		result.append(", addedFiles: ");
		result.append(addedFiles);
		result.append(", installerType: ");
		result.append(installerType);
		result.append(')');
		return result.toString();
	}

} //SCEnvironmentImpl
