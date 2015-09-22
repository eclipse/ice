package org.eclipse.ice.materials.ui.test;

import org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.Test;

public class MaterialsDatabaseTester extends AbstractSWTTester {

	@Test
	public void checkMaterialsTree(){
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		
		// Close the initial eclipse welcome window
	//	bot.viewByTitle("Welcome").close();
		
		bot.toolbarButtonWithTooltip("Edit Materials Database");
		
		bot.buttonWithTooltip("Edit Materials Database");
	}
}
