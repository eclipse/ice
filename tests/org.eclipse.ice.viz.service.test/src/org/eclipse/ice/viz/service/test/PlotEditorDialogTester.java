package org.eclipse.ice.viz.service.test;

import static org.junit.Assert.*;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class PlotEditorDialogTester {
	private static SWTBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTBot();
	}

	@Test
	public void checkCombo() {
		bot.comboBox().setSelection("ice-plot");
		assertEquals("ice-plot",bot.comboBox().selection());
		bot.button().click();
	}

}
