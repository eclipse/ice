package org.eclipse.ice.projectgeneration.templates;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ICEProjectResources extends ResourceBundle {

	@Override
	protected Object handleGetObject(String key) {
		if (key.equals("okKey")) return "Okay";
		if (key.equals("cancelKey")) return "Cancel";
		return null;
	}

	@Override
	public Enumeration<String> getKeys() {
		return Collections.enumeration(keySet());
	}
	
	protected Set<String> handleKeySet() {
		return new HashSet<String>(Arrays.asList("okKey","cancelKey"));
	}
}
