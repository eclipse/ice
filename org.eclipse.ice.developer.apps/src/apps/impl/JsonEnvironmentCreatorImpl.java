/**
 */
package apps.impl;

import apps.AppsFactory;
import apps.AppsPackage;
import apps.EnvironmentBuilder;
import apps.IEnvironment;
import apps.JsonEnvironmentCreator;
import apps.OSPackage;
import apps.PackageType;
import apps.SourcePackage;
import apps.SpackPackage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Json Environment Creator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class JsonEnvironmentCreatorImpl extends MinimalEObjectImpl.Container implements JsonEnvironmentCreator {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JsonEnvironmentCreatorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AppsPackage.Literals.JSON_ENVIRONMENT_CREATOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public IEnvironment create(String dataString) {
		BufferedReader br = null;
		InputStream is = new ByteArrayInputStream(dataString.getBytes());
		br = new BufferedReader(new InputStreamReader(is));
		return create(br);
	}
	
	/**
	 * Return the Environment described by the given 
	 * java Reader
	 * @param reader
	 * @return
	 */
	public IEnvironment create(Reader reader) {
		JsonParser parser = new JsonParser();
		JsonObject root = parser.parse(reader).getAsJsonObject();	
		JsonObject generalData = root.getAsJsonObject("General");
		JsonObject appData = root.getAsJsonObject("Application");
		JsonArray deps = root.getAsJsonArray("Dependencies");
		
		IEnvironment env = null;
		EnvironmentBuilder builder = null;
		String type = generalData.get("type").getAsString();
		try {
			builder = EnvironmentBuilder.getEnvironmentBuilder(type);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		env = builder.build();
		
		// Create the Environment
		if (env == null) {
			throw new UnsupportedOperationException("Cannot create the requested environment type.");
		}
		
		env.setName(generalData.get("name").getAsString());
		JsonElement os = generalData.get("os");
		if (os != null) {
			env.setOs(os.getAsString());
		}
		
		// Set the Primary App data if available
		SourcePackage p = AppsFactory.eINSTANCE.createSourcePackage();
		p.setType(PackageType.SOURCE);
		p.setName(appData.get("name").getAsString());
		p.setRepoURL(appData.get("repoURL").getAsString());
		JsonElement branch = appData.get("branch");
		if (branch != null) {
			p.setBranch(branch.getAsString());
		}
		JsonElement build = appData.get("buildCommand");
		p.setBuildCommand(build.getAsString());
		
		// Set it as the primary application
		env.setPrimaryApp(p);

		// Add dependent packages
		if (deps != null) {
			for (JsonElement s : deps) {
				PackageType pkgType = PackageType.get(s.getAsJsonObject().get("type").getAsString());
				if (pkgType.equals(PackageType.SPACK)) {
					SpackPackage pack = AppsFactory.eINSTANCE.createSpackPackage();
					pack.setType(PackageType.SPACK);
					pack.setName(s.getAsJsonObject().get("name").getAsString());
					JsonElement spackcompiler = s.getAsJsonObject().get("compiler");
					if (spackcompiler != null) {
						pack.setCompiler(spackcompiler.getAsString());
					}
					JsonElement spackversion = s.getAsJsonObject().get("version");
					if (spackversion != null) {
						pack.setVersion(spackversion.getAsString());
					}
					env.getDependentPackages().add(pack);

				} else if (pkgType.equals(PackageType.OS)) {
					OSPackage pack = AppsFactory.eINSTANCE.createOSPackage();
					pack.setType(PackageType.OS);
					pack.setName(s.getAsJsonObject().get("name").getAsString());
					JsonElement osversion = s.getAsJsonObject().get("version");
					if (osversion != null) {
						pack.setVersion(osversion.getAsString());
					}
					env.getDependentPackages().add(pack);
				} else if (pkgType.equals(PackageType.SOURCE)) {
					SourcePackage srcp = AppsFactory.eINSTANCE.createSourcePackage();
					srcp.setType(PackageType.SOURCE);
					srcp.setName(s.getAsJsonObject().get("name").getAsString());
					srcp.setRepoURL(s.getAsJsonObject().get("repoURL").getAsString());
					JsonElement srcbranch = s.getAsJsonObject().get("branch");
					if (srcbranch != null) {
						srcp.setBranch(srcbranch.getAsString());
					}
				}
			}
		}

//		if (type.equals("Docker")) {
//			ContainerConfiguration config = DockerFactory.eINSTANCE.createContainerConfiguration();
//			JsonObject containerConfig = root.getAsJsonObject("ContainerConfig");
//			if (containerConfig.get("name") != null) {
//				config.setName(containerConfig.get("name").getAsString()); 
//			}
//			if (containerConfig.get("ephemeral") != null ) {
//				config.setEphemeral(containerConfig.get("ephemeral").getAsBoolean());
//			}
//			// FIXME ADD MORE LATER
//			
//			((DockerEnvironment)env).setContainerConfiguration(config);
//		}
//		
		return env;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case AppsPackage.JSON_ENVIRONMENT_CREATOR___CREATE__STRING:
				return create((String)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} //JsonEnvironmentCreatorImpl
