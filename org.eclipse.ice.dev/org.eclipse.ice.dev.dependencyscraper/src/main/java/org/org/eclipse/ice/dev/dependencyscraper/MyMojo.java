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

package org.org.eclipse.ice.dev.dependencyscraper;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * Maven plugin used to search dependencies for files matching filtering rules
 * and copy to output directory.
 */
@Mojo(
	name = "scrape",
	defaultPhase = LifecyclePhase.PROCESS_SOURCES,
	requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME
)
public class MyMojo extends AbstractMojo {
	/**
	 * Current project. Used to get handle to dependencies.
	 */
	@Parameter(
		defaultValue = "${project}",
		readonly = true,
		required = true
	)
	private MavenProject project;

	/**
	 * Directory to output scraped files.
	 */
	@Parameter(
		property = "outputDirectory",
		defaultValue = "frontend/src",
		required = false
	)
	private File outputDirectory;

	@Parameter(
		property = "sourceDirectory",
		defaultValue = "frontend",
		required = false
	)
	private String sourceDirectory;

	/**
	 * List of files to include. May contain wildcards, i.e. <code>*</code> or
	 * <code>**{@literal /}*.js</code>.
	 */
	@Parameter(
		property = "includes",
		required = true
	)
	private List<String> includes;

	/**
	 * Filter for whether a ZipEntry begins with the sourceDirectory.
	 * @param file to test
	 * @return if ZipEntry path begins with sourceDirectory.
	 */
	private boolean startsWithSourceDirectory(ZipEntry file) {
		return file.getName()
			.toLowerCase(Locale.ENGLISH)
			.startsWith(sourceDirectory.toLowerCase(Locale.ENGLISH));
	}

	/**
	 * Filter for whether a ZipEntry should be included.
	 * @param file to test
	 * @return if it should be included
	 */
	private boolean shouldInclude(ZipEntry file) {
		return includes.stream()
			.anyMatch(
				include -> FilenameUtils.wildcardMatch(file.getName(), include)
			);
	}

	/**
	 * Copy file from jar to output directory.
	 * @param jar from which file will be copied
	 * @param fileInJar file to copy
	 * @throws MojoFailureException if file could not be copied, inadequate
	 *         permissions, etc.
	 */
	private void copyFileInJarToOutput(
		JarFile jar, ZipEntry fileInJar
	) throws MojoFailureException {
		String fullPath = fileInJar.getName();
		// Drop sourceDirectory from file path
		String relativePath = fullPath
			.substring(fullPath.toLowerCase(Locale.ENGLISH)
				.indexOf(sourceDirectory.toLowerCase(Locale.ENGLISH))
				+ sourceDirectory.length());
		// Determine output path
		File target = new File(outputDirectory, relativePath);
		try {
			if (target.exists()) {
				// Replace target if contents differ
				File tempFile = File.createTempFile(fullPath, null);
				FileUtils.copyInputStreamToFile(
					jar.getInputStream(fileInJar), tempFile
				);
				if (!FileUtils.contentEquals(tempFile, target)) {
					FileUtils.forceDelete(target);
					FileUtils.moveFile(tempFile, target);
				} else {
					tempFile.delete();
				}
			} else {
				FileUtils.copyInputStreamToFile(
					jar.getInputStream(fileInJar),
					target
				);
			}
		} catch (IOException e) {
			throw new MojoFailureException(
				"Failed to copy file " + fileInJar.getName()
			);
		}
	}

	/**
	 * Collect Jar files from dependencies and search through them for files
	 * matching the parameters of the plugin. Copy matching files into output
	 * directory specified by parameters.
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		Set<File> jarFiles = project.getArtifacts().stream()
			.filter(artifact -> "jar".equals(artifact.getType()))
			.map(Artifact::getFile).collect(Collectors.toSet());
		for (File jar : jarFiles) {
			try (JarFile jarFile = new JarFile(jar, false)) {
				Set<ZipEntry> toCopy = jarFile.stream()
					.filter(file -> !file.isDirectory())
					.filter(this::startsWithSourceDirectory)
					.filter(this::shouldInclude)
					.collect(Collectors.toSet());
				for (ZipEntry file : toCopy) {
					getLog().info(String.format("Copying %s", file.getName()));
					copyFileInJarToOutput(jarFile, file);
				}
			} catch (IOException e) {
				throw new MojoFailureException(
					String.format("Failed to open jar file %s!", jar.toString())
				);
			}
		}
	}
}