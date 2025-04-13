package com.kazurayam.ks.configuration

import com.kms.katalon.core.configuration.RunConfiguration
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Path
import java.nio.file.Paths

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

@RunWith(JUnit4.class)
public class RunConfigurationConfiguratorTest {

	@Test
	public void test_configure() {
		RunConfigurationConfigurator.configure()
		//
		String projectDir = RunConfiguration.getProjectDir()
		println "projectDir=${projectDir}"
		assertNotNull projectDir, "RunConfiguration.getProjectDir() returned null"
		Path currentDir = Paths.get(".").normalize().toAbsolutePath()
		assertEquals currentDir.toString(), projectDir
	}
}
