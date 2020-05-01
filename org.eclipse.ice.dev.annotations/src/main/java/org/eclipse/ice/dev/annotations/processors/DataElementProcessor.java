package org.eclipse.ice.dev.annotations.processors;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;

@SupportedAnnotationTypes("org.eclipse.ice.dev.annotations.DataElement")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DataElementProcessor extends AbstractProcessor {
	
	protected Messager messager;
	
	
    @Override
    public void init(ProcessingEnvironment env) {
        messager = env.getMessager();
        super.init(env);
    }
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		messager.printMessage(Diagnostic.Kind.NOTE, "Printing: ");
		
		for (TypeElement te : annotations)
			for (Element e : roundEnv.getElementsAnnotatedWith(te))
				messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + e.toString());

		try {
			FileWriter writer = new FileWriter("/home/bkj/test.txt");
			writer.write("Howdy!");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
