package org.eclipse.ice.parsergenerator

import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.emf.ecore.resource.Resource

class IOServiceGenerator implements IGenerator {

	String packageName;
	String itemName;
	String extName;	

	override doGenerate(Resource input, IFileSystemAccess fsa) {
		generatePluginXml(fsa)
		generatePomXml(fsa)
		generateReader(fsa)
		generateWriter(fsa)
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
		val fileName = "src/" + packageName.replace(".","/") + "/" + itemName + "Reader.java"
		fsa.generateFile(fileName,
			'''
			package «packageName».io;
			
			class «itemName»Reader extends IReader {
				
				public «itemName»Reader() {
					
				}
			
			}
			'''
		)
	}


	def generateWriter(IFileSystemAccess fsa) {
		val fileName = "src/" + packageName.replace(".","/") + "/io/" + itemName + "Writer.java"
		println(fileName)
		fsa.generateFile(fileName,
			'''
			package «packageName».io;
			
			class «itemName»Writer extends IWriter {
				
				public «itemName»Writer() {
					
				}
			
			}
			'''
		)
	}
}