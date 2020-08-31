package org.org.eclipse.ice.dev.dependencyscraper;


import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.Set;

public class DependencyScraperTest
{
	@Rule
	public MojoRule rule = new MojoRule() {
		@Override
		protected void before() throws Throwable { }

		@Override
		protected void after() { }
	};

	/**
	 * @throws Exception if any
	 */
	@Test
	public void testSomething() throws Exception {
		File pom = new File("target/test-classes/project-to-test/");
		assertNotNull(pom);
		assertTrue(pom.exists());

		DependencyScraper myMojo = (DependencyScraper) rule.lookupConfiguredMojo(pom, "scrape");
		assertNotNull(myMojo);
		myMojo.setJarFiles(Set.of(new File(
			"target/test-classes/project-to-test/pretend_dependency.jar"
		)));
		System.out.println(rule.getVariablesAndValuesFromObject(myMojo));
		myMojo.execute();

		File outputDirectory = (File) rule.getVariableValueFromObject(myMojo, "outputDirectory");
		assertNotNull(outputDirectory);
		assertTrue(outputDirectory.exists());
		System.out.println(outputDirectory);

		File test = new File(outputDirectory, "test.txt");
		assertTrue(test.exists());
	}
}

