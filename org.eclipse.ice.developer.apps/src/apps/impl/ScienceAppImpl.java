/**
 */
package apps.impl;

import apps.EnvironmentPackage;
import apps.ScienceApp;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Science App</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.impl.ScienceAppImpl#getRepoURL <em>Repo URL</em>}</li>
 *   <li>{@link apps.impl.ScienceAppImpl#getName <em>Name</em>}</li>
 *   <li>{@link apps.impl.ScienceAppImpl#getBranch <em>Branch</em>}</li>
 *   <li>{@link apps.impl.ScienceAppImpl#getLocalProjectName <em>Local Project Name</em>}</li>
 *   <li>{@link apps.impl.ScienceAppImpl#isRecursiveClone <em>Recursive Clone</em>}</li>
 *   <li>{@link apps.impl.ScienceAppImpl#getAbsolutePath <em>Absolute Path</em>}</li>
 *   <li>{@link apps.impl.ScienceAppImpl#getRemotePort <em>Remote Port</em>}</li>
 *   <li>{@link apps.impl.ScienceAppImpl#getRemoteHost <em>Remote Host</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScienceAppImpl extends MinimalEObjectImpl.Container implements ScienceApp {
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
	 * The default value of the '{@link #getBranch() <em>Branch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBranch()
	 * @generated
	 * @ordered
	 */
	protected static final String BRANCH_EDEFAULT = null;

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
	 * The default value of the '{@link #getLocalProjectName() <em>Local Project Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocalProjectName()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCAL_PROJECT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLocalProjectName() <em>Local Project Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocalProjectName()
	 * @generated
	 * @ordered
	 */
	protected String localProjectName = LOCAL_PROJECT_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isRecursiveClone() <em>Recursive Clone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRecursiveClone()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RECURSIVE_CLONE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isRecursiveClone() <em>Recursive Clone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRecursiveClone()
	 * @generated
	 * @ordered
	 */
	protected boolean recursiveClone = RECURSIVE_CLONE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAbsolutePath() <em>Absolute Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbsolutePath()
	 * @generated
	 * @ordered
	 */
	protected static final String ABSOLUTE_PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAbsolutePath() <em>Absolute Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbsolutePath()
	 * @generated
	 * @ordered
	 */
	protected String absolutePath = ABSOLUTE_PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #getRemotePort() <em>Remote Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemotePort()
	 * @generated
	 * @ordered
	 */
	protected static final int REMOTE_PORT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRemotePort() <em>Remote Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemotePort()
	 * @generated
	 * @ordered
	 */
	protected int remotePort = REMOTE_PORT_EDEFAULT;

	/**
	 * The default value of the '{@link #getRemoteHost() <em>Remote Host</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemoteHost()
	 * @generated
	 * @ordered
	 */
	protected static final String REMOTE_HOST_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRemoteHost() <em>Remote Host</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemoteHost()
	 * @generated
	 * @ordered
	 */
	protected String remoteHost = REMOTE_HOST_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScienceAppImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EnvironmentPackage.Literals.SCIENCE_APP;
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
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.SCIENCE_APP__REPO_URL, oldRepoURL, repoURL));
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
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.SCIENCE_APP__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.SCIENCE_APP__BRANCH, oldBranch, branch));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocalProjectName() {
		return localProjectName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocalProjectName(String newLocalProjectName) {
		String oldLocalProjectName = localProjectName;
		localProjectName = newLocalProjectName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.SCIENCE_APP__LOCAL_PROJECT_NAME, oldLocalProjectName, localProjectName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRecursiveClone() {
		return recursiveClone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRecursiveClone(boolean newRecursiveClone) {
		boolean oldRecursiveClone = recursiveClone;
		recursiveClone = newRecursiveClone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.SCIENCE_APP__RECURSIVE_CLONE, oldRecursiveClone, recursiveClone));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAbsolutePath() {
		return absolutePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAbsolutePath(String newAbsolutePath) {
		String oldAbsolutePath = absolutePath;
		absolutePath = newAbsolutePath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.SCIENCE_APP__ABSOLUTE_PATH, oldAbsolutePath, absolutePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getRemotePort() {
		return remotePort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRemotePort(int newRemotePort) {
		int oldRemotePort = remotePort;
		remotePort = newRemotePort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.SCIENCE_APP__REMOTE_PORT, oldRemotePort, remotePort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRemoteHost() {
		return remoteHost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRemoteHost(String newRemoteHost) {
		String oldRemoteHost = remoteHost;
		remoteHost = newRemoteHost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EnvironmentPackage.SCIENCE_APP__REMOTE_HOST, oldRemoteHost, remoteHost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EnvironmentPackage.SCIENCE_APP__REPO_URL:
				return getRepoURL();
			case EnvironmentPackage.SCIENCE_APP__NAME:
				return getName();
			case EnvironmentPackage.SCIENCE_APP__BRANCH:
				return getBranch();
			case EnvironmentPackage.SCIENCE_APP__LOCAL_PROJECT_NAME:
				return getLocalProjectName();
			case EnvironmentPackage.SCIENCE_APP__RECURSIVE_CLONE:
				return isRecursiveClone();
			case EnvironmentPackage.SCIENCE_APP__ABSOLUTE_PATH:
				return getAbsolutePath();
			case EnvironmentPackage.SCIENCE_APP__REMOTE_PORT:
				return getRemotePort();
			case EnvironmentPackage.SCIENCE_APP__REMOTE_HOST:
				return getRemoteHost();
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
			case EnvironmentPackage.SCIENCE_APP__REPO_URL:
				setRepoURL((String)newValue);
				return;
			case EnvironmentPackage.SCIENCE_APP__NAME:
				setName((String)newValue);
				return;
			case EnvironmentPackage.SCIENCE_APP__BRANCH:
				setBranch((String)newValue);
				return;
			case EnvironmentPackage.SCIENCE_APP__LOCAL_PROJECT_NAME:
				setLocalProjectName((String)newValue);
				return;
			case EnvironmentPackage.SCIENCE_APP__RECURSIVE_CLONE:
				setRecursiveClone((Boolean)newValue);
				return;
			case EnvironmentPackage.SCIENCE_APP__ABSOLUTE_PATH:
				setAbsolutePath((String)newValue);
				return;
			case EnvironmentPackage.SCIENCE_APP__REMOTE_PORT:
				setRemotePort((Integer)newValue);
				return;
			case EnvironmentPackage.SCIENCE_APP__REMOTE_HOST:
				setRemoteHost((String)newValue);
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
			case EnvironmentPackage.SCIENCE_APP__REPO_URL:
				setRepoURL(REPO_URL_EDEFAULT);
				return;
			case EnvironmentPackage.SCIENCE_APP__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EnvironmentPackage.SCIENCE_APP__BRANCH:
				setBranch(BRANCH_EDEFAULT);
				return;
			case EnvironmentPackage.SCIENCE_APP__LOCAL_PROJECT_NAME:
				setLocalProjectName(LOCAL_PROJECT_NAME_EDEFAULT);
				return;
			case EnvironmentPackage.SCIENCE_APP__RECURSIVE_CLONE:
				setRecursiveClone(RECURSIVE_CLONE_EDEFAULT);
				return;
			case EnvironmentPackage.SCIENCE_APP__ABSOLUTE_PATH:
				setAbsolutePath(ABSOLUTE_PATH_EDEFAULT);
				return;
			case EnvironmentPackage.SCIENCE_APP__REMOTE_PORT:
				setRemotePort(REMOTE_PORT_EDEFAULT);
				return;
			case EnvironmentPackage.SCIENCE_APP__REMOTE_HOST:
				setRemoteHost(REMOTE_HOST_EDEFAULT);
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
			case EnvironmentPackage.SCIENCE_APP__REPO_URL:
				return REPO_URL_EDEFAULT == null ? repoURL != null : !REPO_URL_EDEFAULT.equals(repoURL);
			case EnvironmentPackage.SCIENCE_APP__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EnvironmentPackage.SCIENCE_APP__BRANCH:
				return BRANCH_EDEFAULT == null ? branch != null : !BRANCH_EDEFAULT.equals(branch);
			case EnvironmentPackage.SCIENCE_APP__LOCAL_PROJECT_NAME:
				return LOCAL_PROJECT_NAME_EDEFAULT == null ? localProjectName != null : !LOCAL_PROJECT_NAME_EDEFAULT.equals(localProjectName);
			case EnvironmentPackage.SCIENCE_APP__RECURSIVE_CLONE:
				return recursiveClone != RECURSIVE_CLONE_EDEFAULT;
			case EnvironmentPackage.SCIENCE_APP__ABSOLUTE_PATH:
				return ABSOLUTE_PATH_EDEFAULT == null ? absolutePath != null : !ABSOLUTE_PATH_EDEFAULT.equals(absolutePath);
			case EnvironmentPackage.SCIENCE_APP__REMOTE_PORT:
				return remotePort != REMOTE_PORT_EDEFAULT;
			case EnvironmentPackage.SCIENCE_APP__REMOTE_HOST:
				return REMOTE_HOST_EDEFAULT == null ? remoteHost != null : !REMOTE_HOST_EDEFAULT.equals(remoteHost);
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
		result.append(" (repoURL: ");
		result.append(repoURL);
		result.append(", name: ");
		result.append(name);
		result.append(", branch: ");
		result.append(branch);
		result.append(", localProjectName: ");
		result.append(localProjectName);
		result.append(", recursiveClone: ");
		result.append(recursiveClone);
		result.append(", absolutePath: ");
		result.append(absolutePath);
		result.append(", remotePort: ");
		result.append(remotePort);
		result.append(", remoteHost: ");
		result.append(remoteHost);
		result.append(')');
		return result.toString();
	}

} //ScienceAppImpl
