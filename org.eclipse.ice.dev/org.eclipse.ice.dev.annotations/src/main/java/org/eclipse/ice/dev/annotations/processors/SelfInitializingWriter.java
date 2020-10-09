package org.eclipse.ice.dev.annotations.processors;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.tools.FileObject;

/**
 * Temporary class for keeping things working while changing interfaces.
 * @author Daniel Bluhm
 */
public abstract class SelfInitializingWriter extends VelocitySourceWriter {
	protected FileObject generatedFile;
	public SelfInitializingWriter(FileObject generatedFile) {
		super();
		this.generatedFile = generatedFile;
	}

	public SelfInitializingWriter() {
		super();
	}

	public void write() throws IOException {
		try (Writer writer = generatedFile.openWriter()) {
			this.write(writer);
		}
	}

	public abstract BiFunction<FileObject, Map, List<SelfInitializingWriter>> getInitializer();
}
