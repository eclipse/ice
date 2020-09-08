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
import java.io.InputStream;
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
public class DependencyScraper extends AbstractMojo {

	/**
	 * Prefix used for generated temp files.
	 */
	private static final String TEMP_PREFIX = "org.eclipse.ice.dev.dependencyscraper";

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
		required = true
	)
	private File outputDirectory;

	@Parameter(
		property = "sourceDirectory",
		required = true
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
	 * Whether files should be overwritten if they already exist.
	 */
	@Parameter(
		property = "clobber",
		required = false,
		defaultValue = "false"
	)
	private boolean clobber;

	/**
	 * Set of jar files that will be searched for matching files.
	 */
	private Set<File> jarFiles;

	/**
	 * Setter for includes.
	 * @param includes to set.
	 */
	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	/**
	 * Setter for clobber.
	 * @param clobber to set.
	 */
	public void setClobber(boolean clobber) {
		this.clobber = clobber;
	}

	/**
	 * Setter for jarFiles.
	 * @param jarFiles to set.
	 */
	public void setJarFiles(Set<File> jarFiles) {
		this.jarFiles = jarFiles;
	}

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
	 * Efficient and safe copy, only overwriting if destination differs.
	 * @param is input stream source
	 * @param dest output file
	 * @throws IOException on permission errors, etc..
	 */
	private void copyInputToDestIfDiffers(InputStream is, File dest) throws IOException {
		File tempFile = File.createTempFile(TEMP_PREFIX, null);
		FileUtils.copyInputStreamToFile(is, tempFile);
		if (!FileUtils.contentEquals(tempFile, dest)) {
			FileUtils.forceDelete(dest);
			FileUtils.moveFile(tempFile, dest);
		} else {
			tempFile.delete();
		}
	}

	/**
	 * Copy InputStream to file, respecting clobber settings.
	 * @param is input stream source
	 * @param dest output file
	 * @throws IOException on permission errors, etc..
	 */
	private void copyRespectingClobber(InputStream is, File dest) throws IOException {
		if (dest.exists()) {
			if (clobber) { // Overwrite existing file
				getLog().info(String.format(
					"File %s already exists and clobber is set; overwriting.",
					dest.getName()
					));
				copyInputToDestIfDiffers(is, dest);
			} else {
				getLog().info(String.format(
					"File %s already exists and clobber is not set; skipping.",
					dest.getName()
					));
			}
		} else {
			FileUtils.copyInputStreamToFile(is, dest);
		}
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
			copyRespectingClobber(jar.getInputStream(fileInJar), target);
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
		if (jarFiles == null) {
			this.jarFiles = project.getArtifacts().stream()
				// Only process jar dependencies
				.filter(artifact -> "jar".equals(artifact.getType()))
				// Map to File
				.map(Artifact::getFile)
				.collect(Collectors.toSet());
		}
		for (File jar : jarFiles) {
			try (JarFile jarFile = new JarFile(jar, false)) {
				Set<ZipEntry> toCopy = jarFile.stream()
					// Filter out directories
					.filter(file -> !file.isDirectory())
					// Filter out any files not in the source directory
					.filter(this::startsWithSourceDirectory)
					// Filter out files not matching any include wildcards
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