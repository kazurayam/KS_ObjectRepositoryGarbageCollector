package com.kazurayam.ks.configuration

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.constants.StringConstants;

public class RunConfigurationConfigurator {

	public static void configure() {
		if (RunConfiguration.getProjectDir() != null) {
			// the code was invoked in the Katalon Studio runtime environment; nothing to do
		} else {
			// the code was invoked outside the Katalon Studio rumntime Environment,
			// Perhaps, in the command line with Gradle test task.
			// We need to configure the RunConfiguration class with a temporary "execution.properties" file
			File settingsFile = File.createTempFile('execution.properites-', '.tmp')
			settingsFile.deleteOnExit()
			println "settingFile=" + settingsFile.toString()
			RunConfiguration.setExecutionSettingFile(settingsFile.toString())
		}
	}
}
