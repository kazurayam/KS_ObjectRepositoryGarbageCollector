package com.kazurayam.ks.configuration

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import groovy.json.JsonOutput

import com.kms.katalon.core.configuration.RunConfiguration
import com.kazurayam.ks.configuration.RunConfigurationConfigurator

@RunWith(JUnit4.class)
public class RunConfigurationConfiguratorTest {
	
	@Test
	public void test_configure() {
		RunConfigurationConfigurator.configure()
		String projectDir = RunConfiguration.getProjectDir()
		assertNotNull projectDir, "RunConfiguration.getProjectDir() returned null"
	}
	
}
