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
		//updateManifest(fsa)
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
	
	def generateReader(IFileSystemAccess fsa) {
		val fileName = "src/" + packageName.replace(".","/") + "/io/" + itemName + "Reader.java"
		fsa.generateFile(fileName,
			'''
			package «packageName».io;
			
			import org.eclipse.ice.io.IReader;
			import org.eclipse.ice.datastructures.Form;
			
			class «itemName»Reader extends IReader {
				
				public «itemName»Reader() {
					super();	
				}
						
				public Form read(IFile ifile) {
					if (ifile == null) {
						return null;
					}
					Form form = new Form();
					
					FileReader r;
					try {
						r = new FileParser(new File(ifile.getLocation().toOSString());
						«itemName»Parser p = new «itemName»Parser();
						EObject fileContents = p.parse(r)
					} catch (FileNotFoundException e1) {
						
					} catch (IOException e2) {
						
					}
					
					return form;
				}
				
				private class «itemName»Parser() {
					@Inject
					private IParser parser;
					
					public «itemName»Parser() {
						InputStandaloneSetupGenerated issg = new InputStandaloneSetupGenerated();
						Injector injector = issg.createInjectorAndDoEMFRegistration();
						injector.injectMembers(this);	
					}
					
					public EObject parser(Reader rdr) throws IOException {
						IParseResult result = parser.parse(rdr);
						if (result.hasSyntaxErrors()) {
							throw new ParseException("ERROR: «itemName»Parser ould not parse file!";		
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
			package «packageName».io;
			
			import org.eclipse.ice.io.IWriter;
			import org.eclipse.ice.datastructures.Form;
			
			class «itemName»Writer extends IWriter {
				
				public «itemName»Writer() {
					super();
				}
			
				public void write(Form form) {
					
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
			Import-Package: org.apache.log4j,
			 org.eclipse.ice.io,
			 org.eclipse.ice.datastructures
			'''
		)
	}
}