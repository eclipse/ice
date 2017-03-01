/**
 */
package apps.docker.impl;

import apps.docker.ContainerConfiguration;
import apps.docker.DockerPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Container Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link apps.docker.impl.ContainerConfigurationImpl#getName <em>Name</em>}</li>
 *   <li>{@link apps.docker.impl.ContainerConfigurationImpl#isEphemeral <em>Ephemeral</em>}</li>
 *   <li>{@link apps.docker.impl.ContainerConfigurationImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link apps.docker.impl.ContainerConfigurationImpl#getVolumesConfig <em>Volumes Config</em>}</li>
 *   <li>{@link apps.docker.impl.ContainerConfigurationImpl#getRemoteSSHPort <em>Remote SSH Port</em>}</li>
 *   <li>{@link apps.docker.impl.ContainerConfigurationImpl#getId <em>Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContainerConfigurationImpl extends MinimalEObjectImpl.Container implements ContainerConfiguration {
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
	 * The default value of the '{@link #isEphemeral() <em>Ephemeral</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEphemeral()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EPHEMERAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isEphemeral() <em>Ephemeral</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEphemeral()
	 * @generated
	 * @ordered
	 */
	protected boolean ephemeral = EPHEMERAL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Integer> ports;

	/**
	 * The default value of the '{@link #getVolumesConfig() <em>Volumes Config</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumesConfig()
	 * @generated
	 * @ordered
	 */
	protected static final String VOLUMES_CONFIG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVolumesConfig() <em>Volumes Config</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumesConfig()
	 * @generated
	 * @ordered
	 */
	protected String volumesConfig = VOLUMES_CONFIG_EDEFAULT;

	/**
	 * The default value of the '{@link #getRemoteSSHPort() <em>Remote SSH Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemoteSSHPort()
	 * @generated
	 * @ordered
	 */
	protected static final int REMOTE_SSH_PORT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRemoteSSHPort() <em>Remote SSH Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemoteSSHPort()
	 * @generated
	 * @ordered
	 */
	protected int remoteSSHPort = REMOTE_SSH_PORT_EDEFAULT;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContainerConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DockerPackage.Literals.CONTAINER_CONFIGURATION;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.CONTAINER_CONFIGURATION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isEphemeral() {
		return ephemeral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEphemeral(boolean newEphemeral) {
		boolean oldEphemeral = ephemeral;
		ephemeral = newEphemeral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.CONTAINER_CONFIGURATION__EPHEMERAL, oldEphemeral, ephemeral));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Integer> getPorts() {
		if (ports == null) {
			ports = new EDataTypeUniqueEList<Integer>(Integer.class, this, DockerPackage.CONTAINER_CONFIGURATION__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVolumesConfig() {
		return volumesConfig;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumesConfig(String newVolumesConfig) {
		String oldVolumesConfig = volumesConfig;
		volumesConfig = newVolumesConfig;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.CONTAINER_CONFIGURATION__VOLUMES_CONFIG, oldVolumesConfig, volumesConfig));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getRemoteSSHPort() {
		return remoteSSHPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRemoteSSHPort(int newRemoteSSHPort) {
		int oldRemoteSSHPort = remoteSSHPort;
		remoteSSHPort = newRemoteSSHPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.CONTAINER_CONFIGURATION__REMOTE_SSH_PORT, oldRemoteSSHPort, remoteSSHPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DockerPackage.CONTAINER_CONFIGURATION__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DockerPackage.CONTAINER_CONFIGURATION__NAME:
				return getName();
			case DockerPackage.CONTAINER_CONFIGURATION__EPHEMERAL:
				return isEphemeral();
			case DockerPackage.CONTAINER_CONFIGURATION__PORTS:
				return getPorts();
			case DockerPackage.CONTAINER_CONFIGURATION__VOLUMES_CONFIG:
				return getVolumesConfig();
			case DockerPackage.CONTAINER_CONFIGURATION__REMOTE_SSH_PORT:
				return getRemoteSSHPort();
			case DockerPackage.CONTAINER_CONFIGURATION__ID:
				return getId();
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
			case DockerPackage.CONTAINER_CONFIGURATION__NAME:
				setName((String)newValue);
				return;
			case DockerPackage.CONTAINER_CONFIGURATION__EPHEMERAL:
				setEphemeral((Boolean)newValue);
				return;
			case DockerPackage.CONTAINER_CONFIGURATION__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends Integer>)newValue);
				return;
			case DockerPackage.CONTAINER_CONFIGURATION__VOLUMES_CONFIG:
				setVolumesConfig((String)newValue);
				return;
			case DockerPackage.CONTAINER_CONFIGURATION__REMOTE_SSH_PORT:
				setRemoteSSHPort((Integer)newValue);
				return;
			case DockerPackage.CONTAINER_CONFIGURATION__ID:
				setId((String)newValue);
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
			case DockerPackage.CONTAINER_CONFIGURATION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DockerPackage.CONTAINER_CONFIGURATION__EPHEMERAL:
				setEphemeral(EPHEMERAL_EDEFAULT);
				return;
			case DockerPackage.CONTAINER_CONFIGURATION__PORTS:
				getPorts().clear();
				return;
			case DockerPackage.CONTAINER_CONFIGURATION__VOLUMES_CONFIG:
				setVolumesConfig(VOLUMES_CONFIG_EDEFAULT);
				return;
			case DockerPackage.CONTAINER_CONFIGURATION__REMOTE_SSH_PORT:
				setRemoteSSHPort(REMOTE_SSH_PORT_EDEFAULT);
				return;
			case DockerPackage.CONTAINER_CONFIGURATION__ID:
				setId(ID_EDEFAULT);
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
			case DockerPackage.CONTAINER_CONFIGURATION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DockerPackage.CONTAINER_CONFIGURATION__EPHEMERAL:
				return ephemeral != EPHEMERAL_EDEFAULT;
			case DockerPackage.CONTAINER_CONFIGURATION__PORTS:
				return ports != null && !ports.isEmpty();
			case DockerPackage.CONTAINER_CONFIGURATION__VOLUMES_CONFIG:
				return VOLUMES_CONFIG_EDEFAULT == null ? volumesConfig != null : !VOLUMES_CONFIG_EDEFAULT.equals(volumesConfig);
			case DockerPackage.CONTAINER_CONFIGURATION__REMOTE_SSH_PORT:
				return remoteSSHPort != REMOTE_SSH_PORT_EDEFAULT;
			case DockerPackage.CONTAINER_CONFIGURATION__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
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
		result.append(", ephemeral: ");
		result.append(ephemeral);
		result.append(", ports: ");
		result.append(ports);
		result.append(", volumesConfig: ");
		result.append(volumesConfig);
		result.append(", remoteSSHPort: ");
		result.append(remoteSSHPort);
		result.append(", id: ");
		result.append(id);
		result.append(')');
		return result.toString();
	}

} //ContainerConfigurationImpl
