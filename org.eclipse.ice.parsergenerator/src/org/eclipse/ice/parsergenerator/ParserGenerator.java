package org.eclipse.ice.parsergenerator;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.function.BiFunction;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.mwe2.language.Mwe2StandaloneSetup;
import org.eclipse.emf.mwe2.launch.runtime.Mwe2Runner;
import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.entry.StringEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.FormStatus;
import org.eclipse.ice.io.serializable.IIOService;
import org.eclipse.ice.io.serializable.IOService;
import org.eclipse.ice.item.model.Model;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.ui.util.FileOpener;
import org.eclipse.xtext.util.JavaVersion;
import org.eclipse.xtext.xtext.ui.wizard.project.XtextProjectCreator;
import org.eclipse.xtext.xtext.ui.wizard.project.XtextProjectInfo;
import org.eclipse.xtext.xtext.wizard.BuildSystem;
import org.eclipse.xtext.xtext.wizard.LanguageDescriptor;
import org.eclipse.xtext.xtext.wizard.LanguageDescriptor.FileExtensions;
import org.eclipse.xtext.xtext.wizard.ProjectLayout;
import org.eclipse.xtext.xtext.wizard.SourceLayout;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

@SuppressWarnings("restriction")
@XmlRootElement(name = "ParserGenerator")
public class ParserGenerator extends Model {

	private String itemName; 
	private String itemDesc; 
    private String exportString; 
	private IIOService ioService;
	private XtextProjectInfo info;
	private XtextProjectCreator creator;
	private FileOpener fileOpener;
	
	public ParserGenerator() {
		this(null);
	}

	public ParserGenerator(IProject project) {
		super(project);
	}

	/**
	 * Adds relevant information that specify the ui provided
	 * to the user when they create the ParserGenerator Model Item
	 * in ICE.  
	 */
	@Override
	public void setupForm() {
		form = new Form();
		
		ioService = getIOService();
		if (ioService == null) {
			setIOService(new IOService());
			ioService = getIOService();
		}
	
		DataComponent projectComponent = new DataComponent();
		projectComponent.setName("Project Details");
		projectComponent.setId(0);
		projectComponent.addEntry(createEntry("Project Name", "org.eclipse.ice.example"));
		projectComponent.addEntry(createEntry("Parser Name", "Example"));
		projectComponent.addEntry(createEntry("File Extension", "ini"));
		DataComponent parserComponent = new DataComponent();
		parserComponent.setName("Parser Details");
		parserComponent.setId(1);
		parserComponent.addEntry(createEntry("Section Open", "["));
		parserComponent.addEntry(createEntry("Section Close", "]"));
		parserComponent.addEntry(createEntry("Assignment Operator", "="));
		parserComponent.addEntry(createEntry("Comment Symbol", "#"));
		form.addComponent(projectComponent);
		form.addComponent(parserComponent);
	}
	
	/**
	 * Sets the name, description, and custom action name 
	 * for the item.
	 */
	@Override
	protected void setupItemInfo() {
		itemName = "Parser Generator";
		itemDesc = "Specify parameters about files to be parsed";
		exportString = "Generate readers and writers";
		setName(itemName);
		setDescription(itemDesc);
		allowedActions.add(0, exportString);
	}

	/**
	 * The reviewEntries method is used to ensure that the form is 
	 * in an acceptable state before processing the information it
	 * contains.  If the form is not ready to process it is advisable
	 * to have this method return FormStatus.InfoError.
	 * 
	 * @param preparedForm
	 * 
	 * @return
	 */
	@Override
	protected FormStatus reviewEntries(Form preparedForm) {
		FormStatus retStatus = FormStatus.ReadyToProcess;

		// Make sure that the project details are set up
		ArrayList<IEntry> projectEntries = ((DataComponent) form.getComponent(0)).retrieveAllEntries();
		for (IEntry ent : projectEntries) {
			if (ent.getName() == "" || ent.getValue() == "") {
				retStatus = FormStatus.InfoError;
			}
		}
		
		// Make sure that the parser details are set up
		ArrayList<IEntry> parserEntries = ((DataComponent) form.getComponent(1)).retrieveAllEntries();
		for (IEntry ent : parserEntries) {
			if (ent.getName() == "" || ent.getValue() == "") {
				retStatus = FormStatus.InfoError;
			}
		}	
	
		return retStatus;
	}

	/**
	 * Use this method to process the data that has been 
	 * specified in the form. 
	 * 
	 * @param actionName
	 * @return
	 */
	@SuppressWarnings("restriction")
	@Override
	public FormStatus process(String actionName) {
		FormStatus retStatus = FormStatus.ReadyToProcess;
		Injector injector = Guice.createInjector();
		ArrayList<Component> components = form.getComponents();
		ArrayList<IEntry> projectEntries = ((DataComponent)components.get(0)).retrieveAllEntries();
		ArrayList<IEntry> parserEntries = ((DataComponent)components.get(1)).retrieveAllEntries();
		String outputName     = projectEntries.get(1).getValue();
		String projectName    = projectEntries.get(0).getValue(); 
		String parserName     = projectEntries.get(1).getValue();
		String fileExt        = projectEntries.get(2).getValue();
		
		if (actionName == exportString) {
			IFile outputFile = project.getFile(outputName);
			try {
				retStatus = FormStatus.Processing;
			
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				
				// Create a new Xtext/Plugin Project
				fileOpener = new FileOpener();
				info = new XtextProjectInfo();
				LanguageDescriptor lang = info.getLanguage();
				lang.setFileExtensions(FileExtensions.fromString(fileExt));
				lang.setName(projectName + "." + parserName);
				info.setBaseName(projectName);
				info.setWorkingSets(Arrays.asList(new IWorkingSet[] {}));
				info.setRootLocation(workspace.getRoot().getLocation().toOSString());
				info.setWorkbench(PlatformUI.getWorkbench());
				info.setEncoding(Charset.defaultCharset());
				info.setPreferredBuildSystem(BuildSystem.MAVEN);
				info.setSourceLayout(SourceLayout.PLAIN);
				info.setJavaVersion(JavaVersion.JAVA8);
				info.setProjectLayout(ProjectLayout.FLAT);
				info.getIdeProject().setEnabled(false);
				info.getIntellijProject().setEnabled(false);
				info.getWebProject().setEnabled(false);
				info.getUiProject().setEnabled(false);
				
				IRunnableWithProgress op = new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException {
						try {
							Injector injector = Guice.createInjector(new Module() {
								@Override
								public void configure(Binder binder) {
									binder.bind(IWorkspace.class).toInstance(workspace);
								}
							});
							creator = injector.getInstance(XtextProjectCreator.class);
							creator.setProjectInfo(info);
							creator.run(monitor);
							//fileOpener.selectAndReveal(creator.getResult());
						} catch(Exception e) {
							throw new InvocationTargetException(e);
						} finally {
							monitor.done();
						}
					}
				};
				op.run(new NullProgressMonitor());
			
				// Write out the grammar file
				StringJoiner sj = new StringJoiner(System.getProperty("file.separator"));
				sj.add(workspace.getRoot().getLocation().toOSString());
				sj.add(projectName);
				sj.add("src");
				for (String s : projectName.split("\\."))
					sj.add(s);
				sj.add(parserName + ".xtext");
				String grammarFilePath = sj.toString();
				String grammarText = buildGrammar(projectName, parserName, parserEntries);
				FileWriter fw = new FileWriter(new File(grammarFilePath), false);
				fw.write(grammarText);
				fw.flush();
				fw.close();
				
				// Need to register the new project as a resource
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
				
				//URI uri = (new File(grammarFilePath)).toURI();
				//System.out.println(uri.toString());
				
				// Generate the parser
				injector = new Mwe2StandaloneSetup().createInjectorAndDoEMFRegistration();
				Mwe2Runner runner = injector.getInstance(Mwe2Runner.class);
				
				/*
				 * There are issues getting the Mwe2Runner or Mwe2Launcher to work due to
				 * the new project not being registered with the emf plugins.
				 */
				
				project.refreshLocal(1, new NullProgressMonitor());
				retStatus = FormStatus.Processed;
			} catch (CoreException e) {
				logger.error(getClass().getName() + " CoreException!", e);
				retStatus = FormStatus.Unacceptable;
			} catch (Exception e) {
				logger.error(getClass().getName() + " Exception!", e);
				retStatus = FormStatus.Unacceptable;
			}
		} else {
			retStatus = super.process(actionName);
		}
		return retStatus;
	}

	private String buildGrammar(String projectName, String parserName, ArrayList<IEntry> parserData) {
		String open    = parserData.get(0).getValue();
		String close   = parserData.get(1).getValue(); 
		String assign = parserData.get(2).getValue(); 
		String comment  = parserData.get(3).getValue();
		
		String sep = System.lineSeparator();
		String header = "grammar " + projectName + "." + parserName + " with org.eclipse.xtext.common.Terminals hidden(COMMENT, WHITESPACE)" + sep + 
				"generate project \"http://www.eclipse.org/" + projectName + "\"" + sep;
		String declaration = "{Input}" + sep + "    NEWLINE*" + sep + "    sections+=Section*";
		String line = "name=ID + ASSIGN + value=TEXT";
		String section = "OPEN + ID + CLOSE" + sep + 
		             "    (NEWLINE+ lines+=Line)+" + sep + 
		             "    NEWLINE+";
		String id = "('A'..'Z' | 'a'..'z') ('A'..'Z' | 'a'..'z' | '_' | '-' | '0'..'9')"; 
		String whitespace = "(' '|'\\t')+";
		String newline = "'\\r'? '\\n'+";
		String text = "(WHITESPACE+ | STRING+)*";		
		
		BiFunction<String, String, String> build = (k,v) -> (k + ":" + sep + "    " + v + ";" + sep); 
		
		StringBuilder sb = new StringBuilder();
		sb.append(header);
		sb.append(build.apply("Input", declaration));
		
		// Intermediate nodes in parse tree
		sb.append(build.apply("Section", section));
		sb.append(build.apply("Line", line));
		
		// 'terminal' prefix denotes leaf nodes of parse tree
		sb.append(build.apply("terminal ID", id));
		sb.append(build.apply("terminal TEXT", text));
		sb.append(build.apply("terminal NEWLINE", newline));
		sb.append(build.apply("terminal WHITESPACE", whitespace));
		
		sb.append(build.apply("terminal OPEN", "'" + open + "'"));
		sb.append(build.apply("terminal CLOSE", "'" + close + "'"));
		sb.append(build.apply("terminal ASSIGN", "'" + assign + "'"));
		sb.append(build.apply("terminal COMMENT", "'" + comment + "' " + text));		
	
		return sb.toString();
	}
	
	private StringEntry createEntry(String name, String value) {
		StringEntry entry = new StringEntry();
		entry.setName(name);
		entry.setValue(value);
		entry.setDescription("");
		return entry;
	}
}
