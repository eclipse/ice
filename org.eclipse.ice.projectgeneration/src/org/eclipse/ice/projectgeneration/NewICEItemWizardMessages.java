package org.eclipse.ice.projectgeneration;

import org.eclipse.osgi.util.NLS;

public class NewICEItemWizardMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ice.projectgeneration.messages"; //$NON-NLS-1$
	public static String NewICEItemWizard_Description;
	public static String NewICEItemWizard_Window_Title;
	public static String NewICEItemWizard_Wizard_Name;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, NewICEItemWizardMessages.class);
	}

	private NewICEItemWizardMessages() {
	}
}
