package org.org.eclipse.ice.dev.dependencyscraper;


import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.After;
import org.junit.Rule;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Set;

public class DependencyScraperTest
{
	private static final Path PROJECT = Path.of("target/test-classes/project-to-test");
	private static final Path OUTPUT = Path.of("target/test-classes/project-to-test/test");

	@Rule
	public MojoRule rule = new MojoRule() {
		@Override
		protected void before() throws Throwable { }

		@Override
		protected void after() { }
	};

	/**
	 * Get an instance of the DependencyScraper with pretend jar dep already
	 * set.
	 * @return
	 * @throws Exception
	 */
	private DependencyScraper getMojo() throws Exception {
		File pom = PROJECT.toFile();
		assertNotNull(pom);
		assertTrue(pom.exists());

		DependencyScraper myMojo = (DependencyScraper) rule.lookupConfiguredMojo(pom, "scrape");
		assertNotNull(myMojo);
		myMojo.setJarFiles(Set.of(
			PROJECT.resolve("pretend_dependency.jar").toFile()
		));
		return myMojo;
	}

	@After
	public void clearOutputDirectory() throws IOException {
		if (Files.exists(OUTPUT)) {
			FileUtils.cleanDirectory(OUTPUT.toFile());
		}
	}

	/**
	 * Test that the DependencyScraper reads the pom and copies the expected
	 * file.
	 * @throws Exception if any
	 */
	@Test
	public void testExecuteWorks() throws Exception {
		DependencyScraper myMojo = getMojo();
		myMojo.execute();

		assertTrue(OUTPUT.toFile().exists());
		assertTrue(OUTPUT.resolve("test.txt").toFile().exists());
	}

	/**
	 * Test that the DependencyScraper reads the pom and runs but does not do
	 * anything when no matching jars to scrape.
	 * @throws Exception if any
	 */
	@Test
	public void testExecuteWhenNoJars() throws Exception {
		DependencyScraper myMojo = getMojo();
		myMojo.setJarFiles(null);
		myMojo.execute();

		assertFalse(OUTPUT.toFile().exists());
	}

	/**
	 * Test that the clobber option is respected.
	 * @throws Exception if any
	 */
	@Test
	public void testClobberOverwrites() throws Exception {
		DependencyScraper mojo = getMojo();
		mojo.setClobber(true);

		Path srcFile = PROJECT.resolve("already_exists_but_differs.txt");
		Path destFile = OUTPUT.resolve("already_exists_but_differs.txt");
		FileUtils.copyFile(srcFile.toFile(), destFile.toFile());

		String contents = Files.readString(destFile);
		assertEquals("a", contents);
		mojo.execute();
		contents = Files.readString(destFile);
		assertEquals("b", contents);
	}

	/**
	 * Test that the clobber won't overwrite if contents are the same.
	 * @throws Exception if any
	 */
	@Test
	public void testClobberNoOverwriteOnSameContents() throws Exception {
		DependencyScraper mojo = getMojo();
		mojo.setClobber(true);

		Path srcFile = PROJECT.resolve("already_exists_but_is_same.txt");
		Path destFile = OUTPUT.resolve("already_exists_but_is_same.txt");
		FileUtils.copyFile(srcFile.toFile(), destFile.toFile());

		String contents = Files.readString(destFile);
		assertEquals("a", contents);
		BasicFileAttributes beforeAttrs = Files.readAttributes(
			destFile, BasicFileAttributes.class
		);
		mojo.execute();
		BasicFileAttributes afterAttrs = Files.readAttributes(
			destFile, BasicFileAttributes.class
		);
		contents = Files.readString(destFile);
		assertEquals("a", contents);

		assertEquals(beforeAttrs.creationTime(), afterAttrs.creationTime());
	}

	/**
	 * Test that the clobber option is respected.
	 * @throws Exception if any
	 */
	@Test
	public void testNoClobberPreserves() throws Exception {
		DependencyScraper mojo = getMojo();
		mojo.setClobber(false);

		Path srcFile = PROJECT.resolve("already_exists_but_differs.txt");
		Path destFile = OUTPUT.resolve("already_exists_but_differs.txt");
		FileUtils.copyFile(srcFile.toFile(), destFile.toFile());

		String contents = Files.readString(destFile);
		assertEquals("a", contents);
		mojo.execute();
		contents = Files.readString(destFile);
		assertEquals("a", contents);
	}

	@Test
	public void testWildcardsAll() throws Exception {
		DependencyScraper mojo = getMojo();
		mojo.setIncludes(List.of("*"));
		mojo.execute();
		assertTrue(OUTPUT.toFile().exists());
		assertTrue(OUTPUT.resolve("test.txt").toFile().exists());
		assertTrue(OUTPUT.resolve("test.json").toFile().exists());
	}

	@Test
	public void testWildcardsByExtension() throws Exception {
		DependencyScraper mojo = getMojo();
		mojo.setIncludes(List.of("*.txt"));
		mojo.execute();
		assertTrue(OUTPUT.resolve("test.txt").toFile().exists());
		assertFalse(OUTPUT.resolve("test.json").toFile().exists());
	}

	@Test
	public void testIncludeMultiple() throws Exception {
		DependencyScraper mojo = getMojo();
		mojo.setIncludes(List.of("*.txt", "*.json"));
		mojo.execute();
		assertTrue(OUTPUT.resolve("test.txt").toFile().exists());
		assertTrue(OUTPUT.resolve("test.json").toFile().exists());
	}
}