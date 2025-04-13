package com.kazurayam.ks.configuration

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class KatalonProjectDirectoryResolver {

    private static final String KATALON_PROJECT_PATH_RELATIVE_TO_SHARED_PROJECT = "../katalon"

    private KatalonProjectDirectoryResolver() {}

    static Path getProjectDir() {
        Path thePath
        String thePathString = System.getProperty("com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver.thePath")
        if (thePathString != null) {
            thePath = Paths.get(thePathString).toAbsolutePath().normalize()
        } else {
            thePath = Paths.get(KATALON_PROJECT_PATH_RELATIVE_TO_SHARED_PROJECT)
                        .toAbsolutePath().normalize()
        }
        if (Files.exists(thePath)) {
            return thePath
        } else {
            throw new IOException(thePath.toString() + " is not present")
        }
    }
}
