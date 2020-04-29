package org.eclipse.ice.dev.annotations.processors;

import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import com.google.auto.service.AutoService;

@SupportedAnnotationTypes("org.eclipse.ice.dev.annotations.DataElement")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DataElementProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		// TODO Auto-generated method stub
		return false;
	}

}
