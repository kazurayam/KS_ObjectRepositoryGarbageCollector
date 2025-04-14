package com.kazurayam.ks.configuration

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.constants.StringConstants

class RunConfigurationConfigurator {

	static void configureProjectDir() {
		if (RunConfiguration.getProjectDir() == null ||
				RunConfiguration.getProjectDir() == "null") {
			// the code was invoked outside the Katalon Studio rumntime Environment,
			// Perhaps, in the subproject `shared` next to the `katalon` project.
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
