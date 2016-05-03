package org.eclipse.ice.parsergenerator

import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.emf.ecore.resource.Resource
import java.io.File
import java.io.FileReader
import java.io.BufferedReader
import java.nio.file.Paths
import java.nio.file.Files

class IOServiceGenerator implements IGenerator {

	String packageName;
	String itemName;
	String extName;	

	override doGenerate(Resource input, IFileSystemAccess fsa) {
		generatePluginXml(fsa)
		generatePomXml(fsa)
		generateReader(fsa)
		generateWriter(fsa)
	    generateSwitch(fsa)
		updateManifest(fsa)
	}

	def setParserInfo(String packageName, String itemName, String extName) {
		this.packageName = packageName;
		this.itemName = itemName;
		this.extName = extName;
	}
	
	def generatePluginXml(IFileSystemAccess fsa) { 
		fsa.generateFile("plugin.xml",	
			'''
			<?xml version="1.0" encoding="UTF-8"?>
			<?eclipse version="3.4"?>
			<plugin>
			   <extension
			         id="org.eclipse.ice.io.«itemName»Reader"
			         name="«itemName» Reader"
			         point="org.eclipse.ice.io.reader">
			      <implementation
			            class="«packageName».«itemName»Reader">
			      </implementation>
			   </extension>
			   <extension
			         id="org.eclipse.ice.io.«itemName»Writer"
			         name="«itemName» Writer"
			         point="org.eclipse.ice.io.writer">
			      <implementation
			            class="«packageName».«itemName»Writer">
			      </implementation>
			   </extension>
			</plugin>
			'''
		)	
	}
	
	def generatePomXml(IFileSystemAccess fsa) {
		fsa.generateFile("pom.xml",
			'''
			<?xml version="1.0" encoding="UTF-8"?>
			<project
				xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
				xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
				<modelVersion>4.0.0</modelVersion>
				<parent>
					<groupId>org.eclipse.ice</groupId>
					<artifactId>org.eclipse.ice.build</artifactId>
					<version>2.1.8-SNAPSHOT</version>
					<relativePath>../org.eclipse.ice.parent/pom.xml</relativePath>
				</parent>
				<groupId>org.eclipse.ice</groupId>
				<artifactId>«packageName»</artifactId>
				<packaging>eclipse-plugin</packaging>
			</project>	
			'''
		)
	}

    def generateSwitch(IFileSystemAccess fsa) {
        val fileName = "src/" + packageName.replace(".","/") + "/io/" + itemName + "ParserSwitch.java"
        fsa.generateFile(fileName,
            '''
            package «packageName».io;
            
            import java.util.ArrayList;
            
            import org.eclipse.emf.ecore.EObject;
            import org.eclipse.emf.ecore.EPackage;
            import org.eclipse.emf.ecore.util.Switch;
            import org.eclipse.ice.datastructures.ICEObject.Component;
            import org.eclipse.ice.datastructures.entry.IEntry;
            import org.eclipse.ice.datastructures.entry.StringEntry;
            import org.eclipse.ice.datastructures.form.DataComponent;
            import org.eclipse.ice.datastructures.form.Form;
            import «packageName».io.ExampleParserSwitch.ContentWrapper;
            import «packageName».project.Line;
            import «packageName».project.ProjectPackage;
            import «packageName».project.Section;
            import «packageName».project.impl.LineImpl;
            import «packageName».project.impl.SectionImpl;
            import org.eclipse.xtext.util.XtextSwitch;
            
            public class «itemName»ParserSwitch extends Switch<ContentWrapper<?>> {
            
                private Form form;
                private DataComponent dataComp;
                private ArrayList<IEntry> entries;
                private ArrayList<Component> components;
                
                public ExampleParserSwitch() {
                    super();
                    form = new Form();
                    components = new ArrayList<Component>();
                    entries = new ArrayList<IEntry>();
                }
            
                /**
                 * Gets the internal form being built
                 */
                public Form getForm() {
                    for (Component comp : components) {
                        form.addComponent(comp);
                    }
                    for (IEntry ent : entries) {
                        dataComp.addEntry(ent);
                    }
                    form.addComponent(dataComp);
                    return form;
                }
            
                @Override 
                protected ContentWrapper<?> doSwitch(int id, EObject obj) {
                    switch(id) {
                        case ProjectPackage.SECTION: {
                            Section section = (Section) obj;
                            return caseSection(section);
                        }
                        case ProjectPackage.LINE: {
                            Line line = (Line) obj;
                            return caseLine(line);
                        }
                        default: return defaultCase(obj);
                    }
                }
                
                /**
                 * Builds a content wrapper for a data component.  Use the translate()
                 * method to extract the data.
                 * 
                 * @param section: Data parsed into the generated Section class
                 * @return an instance of the content wrapper
                 */
                public ContentWrapper<DataComponent> caseSection(Section content) {
                    ContentWrapper<DataComponent> cw = new ContentWrapper<DataComponent>(DataComponent.class, content) {
                        @Override
                        public DataComponent translate() {
                            DataComponent c = new DataComponent();
                            c.setName(((SectionImpl)content).getSectionName());
                            return c;
                        }
                    };
                    // Add existing data component to the form before abandoning it
                    if (dataComp != null) {
                        for (IEntry ent : entries) {
                            dataComp.addEntry(ent);
                        }
                        entries.clear();
                        components.add(dataComp);
                    }
                    dataComp = cw.translate();
                    
                    return cw;
                }
                
                /**
                 * Builds a content wrapper for an entry.  Use the translate()
                 * method to extract the data. 
                 * 
                 * @param line: Data parsed into the generated Line class
                 * @return an instance of the content wrapper
                 */
                public ContentWrapper<StringEntry> caseLine(Line content) {
                    ContentWrapper<StringEntry> cw = new ContentWrapper<StringEntry>(StringEntry.class, content) {
                        @Override
                        public StringEntry translate() {
                            StringEntry e = new StringEntry();
                            Line l = (LineImpl) content;
                            e.setName(l.getVarName());
                            e.setValue(l.getValue());
                            return e;
                        }
                    };
                    entries.add(cw.translate());
                    return cw;
                }
            
                /**
                 * An inner class to allow dynamic instantiation of data types.  For each new data
                 * type to be used a new extension of this class implementing the tranlate method
                 * must be created.
                 * 
                 * @param <T> The type of content to get out when the tranlate method is called
                 */
                abstract class ContentWrapper<T> {
                    private EObject content;
                    private final Class<T> type;
                    
                    public ContentWrapper(Class<T> t, EObject e) {
                        content = e;
                        type = t;
                    }
                
                    public Class<T> getContentType() {
                        return type;
                    }
                    
                    abstract T translate();
                }
            
                @Override
                protected boolean isSwitchFor(EPackage ePackage) {
                    if (ePackage.getNsURI() == "http://www.eclipse.org/org.eclipse.ice.example") {
                        return true;
                    } else {
                        return false;
                    }
                }
            
            } 
            '''
        )
    }
	
	def generateReader(IFileSystemAccess fsa) {
		val fileName = "src/" + packageName.replace(".","/") + "/io/" + itemName + "Reader.java"
		fsa.generateFile(fileName,
			'''
			/*******************************************************************************
			 * Copyright (c) 2014 UT-Battelle, LLC.
			 * All rights reserved. This program and the accompanying materials
			 * are made available under the terms of the Eclipse Public License v1.0
			 * which accompanies this distribution, and is available at
			 * http://www.eclipse.org/legal/epl-v10.html
			 *
			 * Contributors:
			 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
			 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
			 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
			 *******************************************************************************/
			package «packageName».io;
			
			import org.eclipse.ice.io.serializable.IReader;
			import org.eclipse.xtext.parser.IParseResult;
			import org.eclipse.xtext.parser.IParser;
			import org.eclipse.xtext.parser.ParseException;
			
			import com.google.inject.Inject;
			import com.google.inject.Injector;
			
			import java.io.File;
			import java.io.FileNotFoundException;
			import java.io.FileReader;
			import java.io.IOException;
			import java.util.ArrayList;
			
			import org.eclipse.core.resources.IFile;
			import org.eclipse.emf.ecore.EObject;
			import org.eclipse.ice.datastructures.entry.IEntry;
			import org.eclipse.ice.datastructures.form.Form;
			import org.eclipse.ice.example.ExampleStandaloneSetupGenerated;
			
			/**
			 * Generated class to read Example files.  This class was generated
			 * with Eclipse ICE's Parser Generator plugin.
			 */
			class «itemName»Reader implements IReader {
				
				/**
				 * Basic constructor
				 */
				public «itemName»Reader() {
					super();	
				}
						
				/**
				 * Read the given file and return a completed form.
				 * This method makes use of the inner class «itemName»Parser,
				 * which provides a wrapper for the Xtext generated parser
				 * class for «itemName».
				 */
				public Form read(IFile ifile) {
					if (ifile == null) {
						return null;
					}
					Form form = new Form();
					
					FileReader r;
					try {
						r = new FileReader(new File(ifile.getLocation().toOSString()));
						«itemName»Parser p = new «itemName»Parser();
						EObject fileContents = p.parse(r);
						TreeIterator<EObject> ti = fileContents.eAllContents();
						«itemName»ParserSwitch sw = new «itemName»ParserSwitch();
						
					    EObject eo;
					    while (ti.hasNext()) {
					        eo = ti.next();
					        sw.doSwitch(eo);
					    }
	                    form = sw.getForm();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					
					return form;
				}
				
				/**
				 * Search through the file for occurrences of the given
				 * regular expression.
				 */
				@Override
				public ArrayList<IEntry> findAll(IFile file, String regex) {
					// TODO Auto-generated method stub
					return null;
				}
			
				/**
				 * Tell us what kind of reader this is
				 */
				@Override
				public String getReaderType() {
					return "«itemName»Reader";
				}
				
				/**
				 * Provides a wrapper for the Xtext generated parser. 
				 */
				private class «itemName»Parser {
					@Inject
					private IParser parser;
					
					/**
					 * Constructor that handles dependency injection via the
					 * standalone setup object generated by Xtext
					 */
					public ExampleParser() {
						«itemName»StandaloneSetupGenerated issg = new «itemName»StandaloneSetupGenerated();
						Injector injector = issg.createInjectorAndDoEMFRegistration();
						injector.injectMembers(this);	
					}
					
					/**
					 * 
					 * @param rdr
					 * @return
					 * @throws IOException
					 */
					public EObject parse(FileReader rdr) throws IOException {
						IParseResult result = parser.parse(rdr);
						if (result.hasSyntaxErrors()) {
							throw new ParseException("ERROR: «itemName»Parser ould not parse file!");		
						}	
						return result.getRootASTElement();
					}
					
				}
			}
			'''
		)
	}


	def generateWriter(IFileSystemAccess fsa) {
		val fileName = "src/" + packageName.replace(".","/") + "/io/" + itemName + "Writer.java"
		fsa.generateFile(fileName,
			'''
			/*******************************************************************************
			 * Copyright (c) 2014 UT-Battelle, LLC.
			 * All rights reserved. This program and the accompanying materials
			 * are made available under the terms of the Eclipse Public License v1.0
			 * which accompanies this distribution, and is available at
			 * http://www.eclipse.org/legal/epl-v10.html
			 *
			 * Contributors:
			 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
			 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
			 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
			 *******************************************************************************/
			package «packageName».io;
			
			import org.eclipse.ice.io.serializable.IWriter;
			import org.eclipse.core.resources.IFile;
			import org.eclipse.ice.datastructures.form.Form;
			
			/**
			 * Writer service for Xtext generated language infrastructure.
			 * This class was generated by the Eclipse ICE Parser Generator
			 * Item.
			 */
			class «itemName»Writer implements IWriter {
			
				/**
				 * Constructor
				 */
				public «itemName»Writer() {
					super();
				}
			
				/**
				 * Write a form out to a file stipulated by the 
				 * Xtext generated grammar infrastructure.
				 */
				@Override
				public void write(Form formToWrite, IFile file) {
					// TODO Auto-generated method stub
					
				}
			
				/**
				 * Search through the given file and replace any 
				 * occurrences that match the given regex with 
				 * the given value
				 */
				@Override
				public void replace(IFile file, String regex, String value) {
					// TODO Auto-generated method stub
					
				}
			
				/**
				 * Tell us what type of writer this is.
				 */
				@Override
				public String getWriterType() {
					return "«itemName»Writer";
				}
			}
			'''
		)
	}
	
	def updateManifest(IFileSystemAccess fsa) {
		val fileName = "META-INF/MANIFEST.MF"
		fsa.generateFile(fileName, 
			'''
			Manifest-Version: 1.0
			Bundle-ManifestVersion: 2
			Bundle-Name: «packageName»
			Bundle-Vendor: My Company
			Bundle-Version: 1.0.0.qualifier
			Bundle-SymbolicName: «packageName»; singleton:=true
			Bundle-ActivationPolicy: lazy
			Require-Bundle: org.eclipse.xtext,
			 org.eclipse.xtext.xbase,
			 org.eclipse.equinox.common;bundle-version="3.5.0",
			 org.eclipse.emf.ecore,
			 org.eclipse.xtext.xbase.lib,
			 org.antlr.runtime,
			 org.eclipse.xtext.util,
			 org.eclipse.xtend.lib,
			 org.eclipse.emf.common
			Bundle-RequiredExecutionEnvironment: JavaSE-1.8
			Export-Package: «packageName».scoping,
			 «packageName».project.util,
			 «packageName».validation,
			 «packageName».generator,
			 «packageName».project.impl,
			 «packageName».services,
			 «packageName».parser.antlr.internal,
			 «packageName»,
			 «packageName».project,
			 «packageName».serializer,
			 «packageName».parser.antlr,
			 «packageName».io
			Import-Package: com.google.inject;version="1.4.0",
			 org.apache.log4j,
 			 org.eclipse.core.resources,
			 org.eclipse.ice.datastructures.entry,
			 org.eclipse.ice.datastructures.form,
			 org.eclipse.ice.io.serializable
			'''
		)
	}
}