/**
 */
package apps;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Environment Builder</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see apps.AppsPackage#getEnvironmentBuilder()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface EnvironmentBuilder extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	IEnvironment build();


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	String name();


	public static EnvironmentBuilder[] getEnvironmentBuilders() throws CoreException {

		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(EnvironmentBuilder.class);
		EnvironmentBuilder[] builders = null;
		String id = "org.eclipse.ice.developer.apps.environmentbuilder";
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(id);

		// If the point is available, create all the builders and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			builders = new EnvironmentBuilder[elements.length];
			for (int i = 0; i < elements.length; i++) {
				builders[i] = (EnvironmentBuilder) elements[i]
						.createExecutableExtension("class");
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return builders;
	}

	public static EnvironmentBuilder getEnvironmentBuilder(String name) throws CoreException {

		/**
		 * Logger for handling event messages and other information.
		 */
		Logger logger = LoggerFactory.getLogger(EnvironmentBuilder.class);
		EnvironmentBuilder builder = null;
		String id = "org.eclipse.ice.developer.apps.environmentbuilder";
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(id);

		// If the point is available, create all the builders and load them into
		// the array.
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				builder = (EnvironmentBuilder) elements[i]
						.createExecutableExtension("class");
				if (name.equals(builder.name())) {
					return builder;
				}
			}
		} else {
			logger.error("Extension Point " + id + "does not exist");
		}

		return null;
	}
} // EnvironmentBuilder
