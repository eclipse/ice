/**
 */
package apps.impl;

import apps.AppsPackage;
import apps.PackageType;
import apps.SourcePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Source Package</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.impl.SourcePackageImpl#getName <em>Name</em>}</li>
 *   <li>{@link apps.impl.SourcePackageImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link apps.impl.SourcePackageImpl#getType <em>Type</em>}</li>
 *   <li>{@link apps.impl.SourcePackageImpl#getRepoURL <em>Repo URL</em>}</li>
 *   <li>{@link apps.impl.SourcePackageImpl#getBranch <em>Branch</em>}</li>
 *   <li>{@link apps.impl.SourcePackageImpl#getBuildCommand <em>Build Command</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SourcePackageImpl extends MinimalEObjectImpl.Container implements SourcePackage {
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
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = "latest";

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final PackageType TYPE_EDEFAULT = PackageType.OS;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected PackageType type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getRepoURL() <em>Repo URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepoURL()
	 * @generated
	 * @ordered
	 */
	protected static final String REPO_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRepoURL() <em>Repo URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepoURL()
	 * @generated
	 * @ordered
	 */
	protected String repoURL = REPO_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getBranch() <em>Branch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBranch()
	 * @generated
	 * @ordered
	 */
	protected static final String BRANCH_EDEFAULT = "master";

	/**
	 * The cached value of the '{@link #getBranch() <em>Branch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBranch()
	 * @generated
	 * @ordered
	 */
	protected String branch = BRANCH_EDEFAULT;

	/**
	 * The default value of the '{@link #getBuildCommand() <em>Build Command</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuildCommand()
	 * @generated
	 * @ordered
	 */
	protected static final String BUILD_COMMAND_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBuildCommand() <em>Build Command</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuildCommand()
	 * @generated
	 * @ordered
	 */
	protected String buildCommand = BUILD_COMMAND_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SourcePackageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.SOURCE_PACKAGE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SOURCE_PACKAGE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SOURCE_PACKAGE__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PackageType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(PackageType newType) {
		PackageType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SOURCE_PACKAGE__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRepoURL() {
		return repoURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRepoURL(String newRepoURL) {
		String oldRepoURL = repoURL;
		repoURL = newRepoURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SOURCE_PACKAGE__REPO_URL, oldRepoURL, repoURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBranch() {
		return branch;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBranch(String newBranch) {
		String oldBranch = branch;
		branch = newBranch;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SOURCE_PACKAGE__BRANCH, oldBranch, branch));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBuildCommand() {
		return buildCommand;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBuildCommand(String newBuildCommand) {
		String oldBuildCommand = buildCommand;
		buildCommand = newBuildCommand;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SOURCE_PACKAGE__BUILD_COMMAND, oldBuildCommand, buildCommand));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AppsPackage.SOURCE_PACKAGE__NAME:
				return getName();
			case AppsPackage.SOURCE_PACKAGE__VERSION:
				return getVersion();
			case AppsPackage.SOURCE_PACKAGE__TYPE:
				return getType();
			case AppsPackage.SOURCE_PACKAGE__REPO_URL:
				return getRepoURL();
			case AppsPackage.SOURCE_PACKAGE__BRANCH:
				return getBranch();
			case AppsPackage.SOURCE_PACKAGE__BUILD_COMMAND:
				return getBuildCommand();
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
			case AppsPackage.SOURCE_PACKAGE__NAME:
				setName((String)newValue);
				return;
			case AppsPackage.SOURCE_PACKAGE__VERSION:
				setVersion((String)newValue);
				return;
			case AppsPackage.SOURCE_PACKAGE__TYPE:
				setType((PackageType)newValue);
				return;
			case AppsPackage.SOURCE_PACKAGE__REPO_URL:
				setRepoURL((String)newValue);
				return;
			case AppsPackage.SOURCE_PACKAGE__BRANCH:
				setBranch((String)newValue);
				return;
			case AppsPackage.SOURCE_PACKAGE__BUILD_COMMAND:
				setBuildCommand((String)newValue);
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
			case AppsPackage.SOURCE_PACKAGE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AppsPackage.SOURCE_PACKAGE__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case AppsPackage.SOURCE_PACKAGE__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case AppsPackage.SOURCE_PACKAGE__REPO_URL:
				setRepoURL(REPO_URL_EDEFAULT);
				return;
			case AppsPackage.SOURCE_PACKAGE__BRANCH:
				setBranch(BRANCH_EDEFAULT);
				return;
			case AppsPackage.SOURCE_PACKAGE__BUILD_COMMAND:
				setBuildCommand(BUILD_COMMAND_EDEFAULT);
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
			case AppsPackage.SOURCE_PACKAGE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AppsPackage.SOURCE_PACKAGE__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case AppsPackage.SOURCE_PACKAGE__TYPE:
				return type != TYPE_EDEFAULT;
			case AppsPackage.SOURCE_PACKAGE__REPO_URL:
				return REPO_URL_EDEFAULT == null ? repoURL != null : !REPO_URL_EDEFAULT.equals(repoURL);
			case AppsPackage.SOURCE_PACKAGE__BRANCH:
				return BRANCH_EDEFAULT == null ? branch != null : !BRANCH_EDEFAULT.equals(branch);
			case AppsPackage.SOURCE_PACKAGE__BUILD_COMMAND:
				return BUILD_COMMAND_EDEFAULT == null ? buildCommand != null : !BUILD_COMMAND_EDEFAULT.equals(buildCommand);
		}
		return super.eIsSet(featureID);
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
		result.append(", version: ");
		result.append(version);
		result.append(", type: ");
		result.append(type);
		result.append(", repoURL: ");
		result.append(repoURL);
		result.append(", branch: ");
		result.append(branch);
		result.append(", buildCommand: ");
		result.append(buildCommand);
		result.append(')');
		return result.toString();
	}

} //SourcePackageImpl
