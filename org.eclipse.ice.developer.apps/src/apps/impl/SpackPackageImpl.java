/**
 */
package apps.impl;

import apps.AppsPackage;
import apps.SpackPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spack Package</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.impl.SpackPackageImpl#getName <em>Name</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getDesiredVersion <em>Desired Version</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getCompiler <em>Compiler</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getVersions <em>Versions</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getCppFlags <em>Cpp Flags</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getVirtualDependency <em>Virtual Dependency</em>}</li>
 *   <li>{@link apps.impl.SpackPackageImpl#getVirtualDependencyProvider <em>Virtual Dependency Provider</em>}</li>
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
	 * The default value of the '{@link #getDesiredVersion() <em>Desired Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesiredVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String DESIRED_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDesiredVersion() <em>Desired Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesiredVersion()
	 * @generated
	 * @ordered
	 */
	protected String desiredVersion = DESIRED_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getCompiler() <em>Compiler</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCompiler()
	 * @generated
	 * @ordered
	 */
	protected static final String COMPILER_EDEFAULT = null;

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
	 * The cached value of the '{@link #getVersions() <em>Versions</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersions()
	 * @generated
	 * @ordered
	 */
	protected EList<String> versions;

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
	public String getDesiredVersion() {
		return desiredVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDesiredVersion(String newDesiredVersion) {
		String oldDesiredVersion = desiredVersion;
		desiredVersion = newDesiredVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AppsPackage.SPACK_PACKAGE__DESIRED_VERSION, oldDesiredVersion, desiredVersion));
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
	public EList<String> getVersions() {
		if (versions == null) {
			versions = new EDataTypeUniqueEList<String>(String.class, this, AppsPackage.SPACK_PACKAGE__VERSIONS);
		}
		return versions;
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AppsPackage.SPACK_PACKAGE__NAME:
				return getName();
			case AppsPackage.SPACK_PACKAGE__DESIRED_VERSION:
				return getDesiredVersion();
			case AppsPackage.SPACK_PACKAGE__COMPILER:
				return getCompiler();
			case AppsPackage.SPACK_PACKAGE__VERSIONS:
				return getVersions();
			case AppsPackage.SPACK_PACKAGE__CPP_FLAGS:
				return getCppFlags();
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY:
				return getVirtualDependency();
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER:
				return getVirtualDependencyProvider();
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
			case AppsPackage.SPACK_PACKAGE__DESIRED_VERSION:
				setDesiredVersion((String)newValue);
				return;
			case AppsPackage.SPACK_PACKAGE__COMPILER:
				setCompiler((String)newValue);
				return;
			case AppsPackage.SPACK_PACKAGE__VERSIONS:
				getVersions().clear();
				getVersions().addAll((Collection<? extends String>)newValue);
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
			case AppsPackage.SPACK_PACKAGE__DESIRED_VERSION:
				setDesiredVersion(DESIRED_VERSION_EDEFAULT);
				return;
			case AppsPackage.SPACK_PACKAGE__COMPILER:
				setCompiler(COMPILER_EDEFAULT);
				return;
			case AppsPackage.SPACK_PACKAGE__VERSIONS:
				getVersions().clear();
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
			case AppsPackage.SPACK_PACKAGE__DESIRED_VERSION:
				return DESIRED_VERSION_EDEFAULT == null ? desiredVersion != null : !DESIRED_VERSION_EDEFAULT.equals(desiredVersion);
			case AppsPackage.SPACK_PACKAGE__COMPILER:
				return COMPILER_EDEFAULT == null ? compiler != null : !COMPILER_EDEFAULT.equals(compiler);
			case AppsPackage.SPACK_PACKAGE__VERSIONS:
				return versions != null && !versions.isEmpty();
			case AppsPackage.SPACK_PACKAGE__CPP_FLAGS:
				return CPP_FLAGS_EDEFAULT == null ? cppFlags != null : !CPP_FLAGS_EDEFAULT.equals(cppFlags);
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY:
				return VIRTUAL_DEPENDENCY_EDEFAULT == null ? virtualDependency != null : !VIRTUAL_DEPENDENCY_EDEFAULT.equals(virtualDependency);
			case AppsPackage.SPACK_PACKAGE__VIRTUAL_DEPENDENCY_PROVIDER:
				return VIRTUAL_DEPENDENCY_PROVIDER_EDEFAULT == null ? virtualDependencyProvider != null : !VIRTUAL_DEPENDENCY_PROVIDER_EDEFAULT.equals(virtualDependencyProvider);
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
		result.append(", desiredVersion: ");
		result.append(desiredVersion);
		result.append(", compiler: ");
		result.append(compiler);
		result.append(", versions: ");
		result.append(versions);
		result.append(", cppFlags: ");
		result.append(cppFlags);
		result.append(", virtualDependency: ");
		result.append(virtualDependency);
		result.append(", virtualDependencyProvider: ");
		result.append(virtualDependencyProvider);
		result.append(')');
		return result.toString();
	}

} //SpackPackageImpl
