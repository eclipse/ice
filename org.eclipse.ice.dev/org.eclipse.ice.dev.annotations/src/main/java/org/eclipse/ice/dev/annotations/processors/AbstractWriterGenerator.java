package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;

public abstract class AbstractWriterGenerator implements WriterGenerator {
	
	protected ProcessingEnvironment processingEnv;
	
	AbstractWriterGenerator(ProcessingEnvironment processingEnv){
		this.processingEnv = processingEnv;
	}
	
	/**
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public JavaFileObject createFileObjectForName(String name) throws IOException {
		return processingEnv.getFiler()
				.createSourceFile(name);
	}
}
