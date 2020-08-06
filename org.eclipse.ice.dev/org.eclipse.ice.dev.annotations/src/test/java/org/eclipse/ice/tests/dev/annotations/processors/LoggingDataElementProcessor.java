/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.tests.dev.annotations.processors;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import org.eclipse.ice.dev.annotations.processors.DataElementProcessor;

import com.cosium.logging.annotation_processor.AbstractLoggingProcessor;

/**
 * Wrapper around DataElementProcessor used to catch warnings that would
 * normally be missed.
 * @author Daniel Bluhm
 */
@SupportedAnnotationTypes({
	"org.eclipse.ice.dev.annotations.DataElement",
	"org.eclipse.ice.dev.annotations.DataField",
	"org.eclipse.ice.dev.annotations.DataField.Default",
	"org.eclipse.ice.dev.annotations.Persisted"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class LoggingDataElementProcessor extends AbstractLoggingProcessor {

	/**
	 * Wrapped processor.
	 */
	private DataElementProcessor wrappedProcessor = new DataElementProcessor();

	@Override
	public void init(final ProcessingEnvironment env) {
		wrappedProcessor.init(env);
		super.init(env);
	}

	@Override
	protected boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		return wrappedProcessor.process(annotations, roundEnv);
	}
}