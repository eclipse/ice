/**
 */
package apps.impl;

import apps.AppsPackage;
import apps.SpackPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spack Package</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.impl.SpackPackageImpl#getName <em>Name</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getCompiler <em>Compiler</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getCppFlags <em>Cpp Flags</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getVirtualDependency <em>Virtual Dependency</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getVirtualDependencyProvider <em>Virtual Dependency Provider</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getRepoURL <em>Repo URL</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getBranch <em>Branch</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpackPackageImpl extends MinimalEObjectImpl.Container implements SpackPackage {
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
	 * The default value of the '{@link #getCompiler() <em>Compiler</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCompiler()
	 * @generated
	 * @ordered
	 */
	protected static final String COMPILER_EDEFAULT = "gcc@6.3.1";

	/**
	 * The cached value of the '{@link #getCompiler() <em>Compiler</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCompiler()
	 * @generated
	 * @ordered
	 */
	protected String compiler = COMPILER_EDEFAULT;

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
	 * The default value of the '{@link #getCppFlags() <em>Cpp Flags</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCppFlags()
	 * @generated
	 * @ordered
	 */
	protected static final String CPP_FLAGS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCppFlags() <em>Cpp Flags</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCppFlags()
	 * @generated
	 * @ordered
	 */
	protected String cppFlags = CPP_FLAGS_EDEFAULT;

	/**
	 * The default value of the '{@link #getVirtualDependency() <em>Virtual Dependency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVirtualDependency()
	 * @generated
	 * @ordered
	 */
	protected static final String VIRTUAL_DEPENDENCY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVirtualDependency() <em>Virtual Dependency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVirtualDependency()
	 * @generated
	 * @ordered
	 */
	protected String virtualDependency = VIRTUAL_DEPENDENCY_EDEFAULT;

	/**
	 * The default value of the '{@link #getVirtualDependencyProvider() <em>Virtual Dependency Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVirtualDependencyProvider()
	 * @generated
	 * @ordered
	 */
	protected static final String VIRTUAL_DEPENDENCY_PROVIDER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVirtualDependencyProvider() <em>Virtual Dependency Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVirtualDependencyProvider()
	 * @generated
	 * @ordered
	 */
	protected String virtualDependencyProvider = VIRTUAL_DEPENDENCY_PROVIDER_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpackPackageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.SPACK_PACKAGE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SPACK_PACKAGE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCompiler() {
		return compiler;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCompiler(String newCompiler) {
		String oldCompiler = compiler;
		compiler = newCompiler;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SPACK_PACKAGE__COMPILER, oldCompiler, compiler));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SPACK_PACKAGE__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCppFlags() {
		return cppFlags;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCppFlags(String newCppFlags) {
		String oldCppFlags = cppFlags;
		cppFlags = newCppFlags;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SPACK_PACKAGE__CPP_FLAGS, oldCppFlags, cppFlags));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVirtualDependency() {
		return virtualDependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVirtualDependency(String newVirtualDependency) {
		String oldVirtualDependency = virtualDependency;
		virtualDependency = newVirtualDependency;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY, oldVirtualDependency, virtualDependency));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVirtualDependencyProvider() {
		return virtualDependencyProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVirtualDependencyProvider(String newVirtualDependencyProvider) {
		String oldVirtualDependencyProvider = virtualDependencyProvider;
		virtualDependencyProvider = newVirtualDependencyProvider;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER, oldVirtualDependencyProvider, virtualDependencyProvider));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SPACK_PACKAGE__REPO_URL, oldRepoURL, repoURL));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SPACK_PACKAGE__BRANCH, oldBranch, branch));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AppsPackage.SPACK_PACKAGE__NAME:
				return getName();
			case AppsPackage.SPACK_PACKAGE__COMPILER:
				return getCompiler();
			case AppsPackage.SPACK_PACKAGE__VERSION:
				return getVersion();
			case AppsPackage.SPACK_PACKAGE__CPP_FLAGS:
				return getCppFlags();
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY:
				return getVirtualDependency();
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER:
				return getVirtualDependencyProvider();
			case AppsPackage.SPACK_PACKAGE__REPO_URL:
				return getRepoURL();
			case AppsPackage.SPACK_PACKAGE__BRANCH:
				return getBranch();
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
			case AppsPackage.SPACK_PACKAGE__NAME:
				setName((String)newValue);
				return;
			case AppsPackage.SPACK_PACKAGE__COMPILER:
				setCompiler((String)newValue);
				return;
			case AppsPackage.SPACK_PACKAGE__VERSION:
				setVersion((String)newValue);
				return;
			case AppsPackage.SPACK_PACKAGE__CPP_FLAGS:
				setCppFlags((String)newValue);
				return;
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY:
				setVirtualDependency((String)newValue);
				return;
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER:
				setVirtualDependencyProvider((String)newValue);
				return;
			case AppsPackage.SPACK_PACKAGE__REPO_URL:
				setRepoURL((String)newValue);
				return;
			case AppsPackage.SPACK_PACKAGE__BRANCH:
				setBranch((String)newValue);
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
			case AppsPackage.SPACK_PACKAGE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AppsPackage.SPACK_PACKAGE__COMPILER:
				setCompiler(COMPILER_EDEFAULT);
				return;
			case AppsPackage.SPACK_PACKAGE__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case AppsPackage.SPACK_PACKAGE__CPP_FLAGS:
				setCppFlags(CPP_FLAGS_EDEFAULT);
				return;
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY:
				setVirtualDependency(VIRTUAL_DEPENDENCY_EDEFAULT);
				return;
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER:
				setVirtualDependencyProvider(VIRTUAL_DEPENDENCY_PROVIDER_EDEFAULT);
				return;
			case AppsPackage.SPACK_PACKAGE__REPO_URL:
				setRepoURL(REPO_URL_EDEFAULT);
				return;
			case AppsPackage.SPACK_PACKAGE__BRANCH:
				setBranch(BRANCH_EDEFAULT);
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
			case AppsPackage.SPACK_PACKAGE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AppsPackage.SPACK_PACKAGE__COMPILER:
				return COMPILER_EDEFAULT == null ? compiler != null : !COMPILER_EDEFAULT.equals(compiler);
			case AppsPackage.SPACK_PACKAGE__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case AppsPackage.SPACK_PACKAGE__CPP_FLAGS:
				return CPP_FLAGS_EDEFAULT == null ? cppFlags != null : !CPP_FLAGS_EDEFAULT.equals(cppFlags);
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY:
				return VIRTUAL_DEPENDENCY_EDEFAULT == null ? virtualDependency != null : !VIRTUAL_DEPENDENCY_EDEFAULT.equals(virtualDependency);
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER:
				return VIRTUAL_DEPENDENCY_PROVIDER_EDEFAULT == null ? virtualDependencyProvider != null : !VIRTUAL_DEPENDENCY_PROVIDER_EDEFAULT.equals(virtualDependencyProvider);
			case AppsPackage.SPACK_PACKAGE__REPO_URL:
				return REPO_URL_EDEFAULT == null ? repoURL != null : !REPO_URL_EDEFAULT.equals(repoURL);
			case AppsPackage.SPACK_PACKAGE__BRANCH:
				return BRANCH_EDEFAULT == null ? branch != null : !BRANCH_EDEFAULT.equals(branch);
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
		result.append(", compiler: ");
		result.append(compiler);
		result.append(", version: ");
		result.append(version);
		result.append(", cppFlags: ");
		result.append(cppFlags);
		result.append(", virtualDependency: ");
		result.append(virtualDependency);
		result.append(", virtualDependencyProvider: ");
		result.append(virtualDependencyProvider);
		result.append(", repoURL: ");
		result.append(repoURL);
		result.append(", branch: ");
		result.append(branch);
		result.append(')');
		return result.toString();
	}

} //SpackPackageImpl
