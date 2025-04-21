package com.kazurayam.ks.configuration

import org.junit.Test
import static org.junit.Assert.*
import java.nio.file.Path

class KatalonProjectDirectoryResolverTest {

    @Test
    void test_getProjectDir() {
        Path katalonProjectDir = KatalonProjectDirectoryResolver.getProjectDir()
        println "katalonProjectDir=" + katalonProjectDir.toString()
        assertNotNull(katalonProjectDir)
    }

}
