package com.kazurayam.ks.configuration

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class KatalonProjectDirectoryResolver {

    private static final String KATALON_PROJECT_PATH_RELATIVE_TO_THE_LIB_PROJECT = "../katalon"

    private KatalonProjectDirectoryResolver() {}

    /**
     * When the System Property `com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver.thePath` is
     * given with a value, it will be converted to a Path value and will be returned.
     * Otherwise, the path to `../katalon` relative to the System Property `user.home` will be returned.
     */
    static Path getProjectDir() {
        Path thePath
        String thePathString = System.getProperty("com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver.thePath")
        if (thePathString != null) {
            thePath = Paths.get(thePathString).toAbsolutePath().normalize()
        } else {
            Path currentDir = Paths.get(System.getProperty('user.dir'))
            thePath = currentDir.resolve(KATALON_PROJECT_PATH_RELATIVE_TO_THE_LIB_PROJECT)
                        .toAbsolutePath().normalize()
        }
        if (Files.exists(thePath)) {
            return thePath
        } else {
            throw new IOException(thePath.toString() + " is not present")
        }
    }
}
