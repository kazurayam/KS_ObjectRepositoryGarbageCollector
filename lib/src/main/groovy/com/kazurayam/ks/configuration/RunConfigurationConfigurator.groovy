package com.kazurayam.ks.configuration

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.constants.StringConstants

class RunConfigurationConfigurator {

	/**
	 * When run in the `<rootProjectDir>/lib` project, this method will
	 * configure the `RunConfiguration` instance so that the call to
	 * `getProjectDir()` returns the path of `<rootProjectDir>/katalon` directory.
	 * When run in the `<rootProjectDir>/katalon` project, this method will
	 * do nothing. Effectively a call to `RunConfiguration.getProjectDir()` will
	 * return the path of `<rootProjectDir>/katalon` directory.
	 */
	static void configureProjectDir() {
		if (RunConfiguration.getProjectDir() == null ||
				RunConfiguration.getProjectDir() == "null") {
			// the code was invoked outside the Katalon Studio rumntime Environment,
			// Perhaps, in the subproject `lib` next to the `katalon` project.
			// We want to configure the RunConfiguration instance to return the directory of
			// the `katalon` project
			Map<String, Object> executionSettingMap = new HashMap<>()
			executionSettingMap.put(StringConstants.CONF_PROPERTY_PROJECT_DIR,
					KatalonProjectDirectoryResolver.getProjectDir().toString())
			RunConfiguration.setExecutionSetting(executionSettingMap)
		} else {
			// the code was invoked inside the Katalon Studio runtime environment;
			// nothing to do
		}
	}
}
