//package org.eclipse.ice.viz.service.test;
//
//import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
//import static org.junit.Assert.*;
//
//import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.core.runtime.IStatus;
//import org.eclipse.core.runtime.jobs.Job;
//import org.eclipse.ice.viz.service.PlotEditorDialog;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.custom.CCombo;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
//import org.eclipse.swtbot.swt.finder.SWTBot;
//import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
//import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
//import org.eclipse.swtbot.swt.finder.widgets.SWTBotCCombo;
//import org.eclipse.ui.PlatformUI;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.eclipse.swtbot.swt.finder.matchers.WidgetOfType;
//
//@RunWith(SWTBotJunit4ClassRunner.class)
//public class PlotEditorDialogTester {
//	private static SWTBot bot;
//
//	@BeforeClass
//	public static void beforeClass() throws Exception {
//		bot = new SWTWorkbenchBot();
//	}
//
//	@Test
//	public void checkCombo() {
//		Display display = new Display();
//		//Shell shell = new Shell(display);
//		PlotEditorDialog dialog = new PlotEditorDialog(new Shell(display));
//		String[] serviceNames = new String[2];
//		serviceNames[0] = "ice-plot";
//		serviceNames[1] = "VisIt";
//		Composite composite = dialog.createDialogArea(new Shell(), serviceNames);
////		Job drawPlot = new Job("Plot Editor Loading and Rendering") {
////			@Override
////			protected IStatus run(IProgressMonitor monitor) {
//
////		CCombo combo = bot.widget(widgetOfType(CCombo.class));
////		SWTBotCCombo combobox = new SWTBotCCombo(combo, null);
////		combobox.setSelection(1);
////		try{bot.comboBox().setSelection(1);}
////		catch(Exception e){
////			System.out.println("Combo not found");
////		}
//	    SWTBotButton button = new SWTBotButton((Button) bot.widget(WidgetOfType(Button.class), composite));
//			try{bot.button("ok").click();}
//			catch(Exception f){System.out.println("Button not found");}
//		
//		//return null;}};
//		//drawPlot.schedule();
//		assertEquals("ice-plot",bot.comboBox().selection());
//	}
//	
//	
//
//	
//}
