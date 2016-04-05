package org.eclipse.ice.parsergenerator

import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.emf.ecore.resource.Resource

class IOServiceGenerator implements IGenerator {
	
	String pomContents = '''TODO''';
	String pluginContents = '''TODO''';
	
	String readerContents = '''TODO''';
	String writerContents = '''TODO''';
	

	override doGenerate(Resource input, IFileSystemAccess fsa) {
		generatePluginInfrastructure(fsa)
		fsa.generateFile(null, generateReader(null));
		fsa.generateFile(null, generateWriter(null));
	}
	
	
	def generatePluginInfrastructure(IFileSystemAccess fsa) {
		fsa.generateFile("plugin.xml", pluginContents)
		fsa.generateFile("pom.xml", pomContents)
	}


	def generateReader(String packageName) '''
		package «packageName»
	''' 


	def generateWriter(String packageName) '''
		package «packageName»
	''' 
}