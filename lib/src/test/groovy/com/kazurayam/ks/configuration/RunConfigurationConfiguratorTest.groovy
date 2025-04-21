package com.kazurayam.ks.configuration

import com.kms.katalon.core.configuration.RunConfiguration
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Path

import static org.junit.Assert.*

@RunWith(JUnit4.class)
class RunConfigurationConfiguratorTest {

	@Test
	void test_configure() {
		RunConfigurationConfigurator.configureProjectDir()
		//
		String projectDir = RunConfiguration.getProjectDir()
		assertNotNull(projectDir)
		assertNotEquals("null", projectDir)
		assertNotNull projectDir, "RunConfiguration.getProjectDir() returned null"
		Path katalonProjectDir = KatalonProjectDirectoryResolver.getProjectDir()
		assertEquals katalonProjectDir.toString(), projectDir
	}
}
