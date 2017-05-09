/**
 */
package eclipseapps.impl;

import apps.AppsFactory;
import apps.IEnvironment;

import eclipseapps.EclipseEnvironmentStorage;
import eclipseapps.EclipseappsPackage;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eclipse Environment Storage</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class EclipseEnvironmentStorageImpl extends MinimalEObjectImpl.Container implements EclipseEnvironmentStorage {
	protected final Logger logger;

	private static final String preferencesId = "org.eclipse.ice.developer.apps";

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected EclipseEnvironmentStorageImpl() {
		super();
		logger = LoggerFactory.getLogger(getClass());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EclipseappsPackage.Literals.ECLIPSE_ENVIRONMENT_STORAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public void store(EList<IEnvironment> environments) {
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(preferencesId);
		try {
			prefs.clear();
			prefs.flush();
		} catch (BackingStoreException e1) {
			e1.printStackTrace();
		}
		for (IEnvironment env : environments) {
			String xmiStr = ((apps.impl.EnvironmentManagerImpl)AppsFactory.eINSTANCE.createEnvironmentManager()).persistToString(env);
			// Save this App as a Preference
			try {
				prefs.put(env.getName(), xmiStr);
				prefs.flush();
			} catch (BackingStoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public EList<IEnvironment> load() {
		// FIXME MAKE THE PREFERENCE STORE EXTENSIBLE to not have to use Eclipse
		// Get the Application preferences
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(preferencesId);
		EList<IEnvironment> environments = new BasicEList<IEnvironment>();
		if (prefs != null) {
			try {
				for (String key : prefs.keys()) {
					String pref = prefs.get(key, "");
					if (!pref.isEmpty()) {
						IEnvironment env = AppsFactory.eINSTANCE.createEnvironmentManager().loadFromXMI(pref);
						environments.add(env);
					}
				}
			} catch (BackingStoreException e) {
				logger.error(getClass().getName() + " Exception!", e);
			}
		}

		return environments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EclipseappsPackage.ECLIPSE_ENVIRONMENT_STORAGE___STORE__ELIST:
				store((EList<IEnvironment>)arguments.get(0));
				return null;
			case EclipseappsPackage.ECLIPSE_ENVIRONMENT_STORAGE___LOAD:
				return load();
		}
		return super.eInvoke(operationID, arguments);
	}

} //EclipseEnvironmentStorageImpl
