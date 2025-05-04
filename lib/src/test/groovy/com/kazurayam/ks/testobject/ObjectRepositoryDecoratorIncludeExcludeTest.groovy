package com.kazurayam.ks.testobject

import com.kazurayam.ks.configuration.RunConfigurationConfigurator
import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

import java.nio.file.Path
import java.nio.file.Paths

import static org.junit.Assert.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
class ObjectRepositoryDecoratorIncludeExcludeTest {

    private Path objectRepositoryDir

    private TestObjectId toAGoToHomepage =
            new TestObjectId(Paths.get("main/Page_CURA Healthcare Service/a_Go to Homepage.rs"))

    private TestObjectId toXtraAGoToHomepage =
            new TestObjectId(Paths.get("main/Page_CURA Healthcare Service/xtra/a_Go to Homepage.rs"))

    private TestObjectId toDummy1 =
            new TestObjectId(Paths.get("misc/dummy1.rs"))

    @BeforeClass
    static void beforeClass() {
        RunConfigurationConfigurator.configureProjectDir()
    }

    @Before
    void setup() {
        objectRepositoryDir =  KatalonProjectDirectoryResolver.getProjectDir()
                .resolve("Object Repository")
    }

    @Test
    void test_default() {
        ObjectRepositoryDecorator ord =
                new ObjectRepositoryDecorator.Builder(objectRepositoryDir).build()
        List<TestObjectId> list = ord.getTestObjectIdList()
        assertTrue(list.contains(toAGoToHomepage))
        assertTrue(list.contains(toXtraAGoToHomepage))
        assertTrue(list.contains(toDummy1))
    }

    @Test
    void test_include() {
        ObjectRepositoryDecorator ord =
                new ObjectRepositoryDecorator.Builder(objectRepositoryDir)
                        .includeFolder("**/main")
                        .build()
        List<TestObjectId> list = ord.getTestObjectIdList()
        assertTrue(list.contains(toAGoToHomepage))
        assertTrue(list.contains(toXtraAGoToHomepage))
        assertFalse(list.contains(toDummy1))
    }

    @Test
    void test_exclude() {
        ObjectRepositoryDecorator ord =
                new ObjectRepositoryDecorator.Builder(objectRepositoryDir)
                        .excludeFolder("**/misc")
                        .build()
        List<TestObjectId> list = ord.getTestObjectIdList()
        assertTrue(list.contains(toAGoToHomepage))
        assertTrue(list.contains(toXtraAGoToHomepage))
        assertFalse(list.contains(toDummy1))
    }

    @Test
    void test_include_exclude() {
        ObjectRepositoryDecorator ord =
                new ObjectRepositoryDecorator.Builder(objectRepositoryDir)
                        .includeFolder("**/Page_CURA Healthcare Service")
                        .excludeFolder("**/xtra")
                        .build()
        List<TestObjectId> list = ord.getTestObjectIdList()
        assertTrue(list.contains(toAGoToHomepage))
        assertFalse(list.contains(toXtraAGoToHomepage))
        assertFalse(list.contains(toDummy1))
    }
}
