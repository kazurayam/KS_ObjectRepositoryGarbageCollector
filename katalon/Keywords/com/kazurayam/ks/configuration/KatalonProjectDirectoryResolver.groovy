package com.kazurayam.ks.configuration

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

public class KatalonProjectDirectoryResolver {

	private static final String KATALON_PROJECT_RELATIVE_PATH = "../katalon"

	private KatalonProjectDirectoryResolver() {}

	public static Path resolve() {
		try {
			Class cls = Class.forName("com.kms.katalon.core.configuration.RunConfiguration")
			Object runConfiguration = cls.newInstance()
			String path = runConfiguration.getProjectDir()
			return Paths.get(path)
		} catch (Exception e) {
			Path thePath
			String thePathString = System.getProperty("com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver.thePath")
			if (thePathString != null) {
				thePath = Paths.get(thePathString).toAbsolutePath().normalize()
			} else {
				thePath = Paths.get(KATALON_PROJECT_RELATIVE_PATH).toAbsolutePath().normalize()
			}
			if (Files.exists(thePath)) {
				return thePath
			} else {
				throw new IOException(thePath + " is not present")
			}
		}
	}
}
