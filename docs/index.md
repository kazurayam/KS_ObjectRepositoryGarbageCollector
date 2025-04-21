- Table of contents
{:toc}

# \[Katalon Studio\] Object Repository Garbage Collector / User Guide

-   link to the [repository](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector)

-   link to the [Javadoc](https://kazurayam.github.io/KS_ObjectRepositoryGarbageCollector/api/com/kazurayam/ks/testobject/gc/Garbage.html)

## Problem to solve

Here is a sample Test Case script named [`Test Cases/main/TC1`](https://github.com/kazurayam/ObjectRepositoryGarbageCollection/blob/develop/katalon/Scripts/main/TC1/Script1677544889443.groovy) in a Katalon Studio project.

    // Test Cases/main/TC1

    import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
    import com.kms.katalon.core.model.FailureHandling as FailureHandling
    import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

    WebUI.openBrowser('')

    WebUI.navigateToUrl('https://katalon-demo-cura.herokuapp.com/')

    WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/a_Make Appointment'))

    WebUI.waitForElementPresent(findTestObject('Object Repository/Page_CURA Healthcare Service/a_Mark Appointments'), 3, FailureHandling.OPTIONAL)

    WebUI.setText(findTestObject('Object Repository/main/Page_CURA Healthcare Service/input_Username_username'), 'John Doe')

    WebUI.setEncryptedText(findTestObject('Object Repository/main/Page_CURA Healthcare Service/input_Password_password'), 'g3/DOGG74jC3Flrr3yH+3D/yKbOqqUNM')

    WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/button_Login'))

    WebUI.selectOptionByValue(findTestObject('Object Repository/main/Page_CURA Healthcare Service/select_Tokyo CURA Healthcare Center        _5b4107'), 
        'Hongkong CURA Healthcare Center', true)

    WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/input_Apply for hospital readmission_hospit_63901f'))

    WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/input_Medicaid_programs'))

    WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/input_Visit Date'))

    WebUI.setText(findTestObject('Object Repository/main/Page_CURA Healthcare Service/input_Visit Date'), "20230325")

    WebUI.setText(findTestObject('Object Repository/main/Page_CURA Healthcare Service/textarea_Comment_comment'), 'This is a comment')

    WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/button_Book Appointment'))

    WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/a_Go to Homepage'))

This script contains 13 statements with a fragment `findTestObject(…​)`. Each call `findTestObject(…​)` method refers to a specific **Test Object**. Let me show you a screenshot of the `Object Repository` of the Katalon project. The directory tree looks like this:

![Object Repository containing unused Test Objects](https://kazurayam.github.io/KS_ObjectRepositoryGarbageCollector/images/1_1_ObjectRepositoryContainingUnusedTestObjects.png)

This small project has 16 Test Objects in the `Object Repository`. Among these, there are several unused Test Objects. I believe that many Katalon users have larger projects with 100 Test Objects, 500 or even more. The "Web Recording" tool generates a lot of Test Objects. The generated set of Test Objects contains a lot of duplications. The tool tends to make your project cluttered.

Now I want to tidy up my Katalon project, but …​

**Problem** : *How can I tell which Test Objects are unused by any of Test Cases? How can I tell duplicating Test Objects with same locators (XPath expression, CSS selectors)? How can I be sure which Test Objects are safe to delete?*

So I developed the `ObjectRepositoryGarbageCollector`. This will scan a Katalon project, analyze the "Test Cases" and "Object Repository" folder, and inform you of unused Test Objects quickly.

## Solution

### Environment

I developed this project using the following environment:

-   macOS Sonoma 14.7.4

-   Katalon Studio Free v10.1.0 which bundles JDK17

The KS\_ObjectRepositoryGargageCollecor-x.x.x.jar requires Katalon Studio v10 and newer. The jar doesn’t run on Katalon Studio v9 and older due to JDK version.

The jar would run on Katalon Runtime Engine, not only on Katalon Studio.

### Installation

How to get started with the **Object Repository Garbage Collector** in your own Katalon Studio project?

1.  Visit the [KS\_ObjectRepositoryGarbageCollector, Releases](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/releases) page. Identify the latest release. Find the `KS_ObjectRepositoryGarbageCollector-x.x.x.jar` attached to each release. Download the jar, save it into the `Drivers` folder of your local Katalon Studio project.

2.  Visit the [MonkDirectoryScanner, Releases](https://github.com/kazurayam/MonkDirectoryScanner/releases) page. Identify the latest release. Find the `MonkDirectoryScanner-x.x.x.jar` attached to each release. Download the jar, save it into the `Drivers` folder of your local Katalon Studio project.

3.  Close your project and reopen it so that your Katalon Studio recognize the new jar files.

4.  Open the "Project" &gt; "Settings", "Library Management" dialog. Check if two jars are there: ![2 1 LibraryManagement](https://kazurayam.github.io/KS_ObjectRepositoryGarbageCollector/images/2_1_LibraryManagement.png)

5.  You want to create a Test Case to run the **ObjectRepositoryGarbageCollector** class. You can copy & reuse the code [Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC\_jsonifyGarbage](https://kazurayam.github.io/KS_ObjectRepositoryGarbageCollector/assets/Scripts/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyGarbage/Script1743835392014.groovy). Thes Test Case should run in any project.

6.  You are done! Run the test case and see the output in the console.

## Description

### Example 1: Scanning entire Object Repository, find unused Test Objects, print the result into the console

Script: `Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyGarbage`

    import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector

    import groovy.json.JsonOutput

    /**
     * A demonstration of ObjectRepositoryGarbageCollector, the simplest case
     *
     * This TestCase outputs a JSON file which contains a list of garbage Test Objects
     * in the "Object Repository" folder.
     *
     * A "garbage" means a Test Object which is not used by any scripts
     * in the "Test Cases" folder.
     */

    // the Garbage Collector instance will scan 2 folders: "Object Repository" and "Test Cases".

    // Amongst the folders in the "Object Repository" folder, the TestObjectsCase scripts contained 
    // in the subfolder that match "**/Page_CURA*" will be selected, and others are ignored
    ObjectRepositoryGarbageCollector gc = 
            new ObjectRepositoryGarbageCollector.Builder()
                .includeObjectRepositoryFolder("**/*")
                .build()

    // gc.jsonifyGarbage() triggers scanning through the entire "Object Repository".
    // All references from TestCase scripts to TestObjects will be identified.
    // Consequently, it can result a list of unused TestObjects.
    // Will output the result into a JSON string
    String json = gc.jsonifyGarbage()

    // just print the JSON into the console
    println JsonOutput.prettyPrint(json)

Output:

    4月 21, 2025 6:24:13 午後 com.kms.katalon.core.logging.KeywordLogger startTest
    情報: --------------------
    4月 21, 2025 6:24:13 午後 com.kms.katalon.core.logging.KeywordLogger startTest
    情報: START Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyGarbage
    {
        "Project name": "katalon",
        "includeObjectRepositoryFolder": [
            "**/*"
        ],
        "Number of TestCases": 28,
        "Number of TestObjects": 16,
        "Number of unused TestObjects": 5,
        "Unused TestObjects": [
            "main/Page_CURA Healthcare Service/a_Foo",
            "main/Page_CURA Healthcare Service/td_28",
            "main/Page_CURA Healthcare Service/xtra/a_Go to Homepage",
            "main/Page_CURA Healthcare Service/xtra/td_28",
            "misc/dummy1"
        ],
        "Duration seconds": 1.515
    }
    4月 21, 2025 6:24:17 午後 com.kms.katalon.core.logging.KeywordLogger endTest
    情報: END Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyGarbage

1.  The script will look into the entire `Object Repository` folder to check the contained TestObjects if used or not by any of TestCase scripts

2.  there are 28 Test Cases in the `Test Cases` folder

3.  there are 16 Test Objects in the `Object Repository` folder

4.  amongst 16, there are 5 unused Test Objects

5.  The script took approximately 1.5 seconds to finish.

### Patterns

    new ObjectRepositoryGarbageCollector.Builder()
        .includeObjectRepositoryFolder("**/*")
        .build()

this statement could be shortened to:

    new ObjectRepositoryGarbageCollector.Builder().build()

because `.includeObjectRepositoryFolder("**/*")` is assumed as default.

The pattern `"**/*"` stands for all sub-folders under the `Object Repository` folder.

The pattern language is derived from the [Apache Ant](https://ant.apache.org/) build tool. Refer to the [Ant manual](https://ant.apache.org/manual/dirtasks.html#patterns) for detail.

### Example 2: Scanning a single sub-folder of Object Repository, find unused Test Objects, print the result into a JSON file

Script: `Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyGarbage_includeFolder_single`

    import java.nio.file.Files
    import java.nio.file.Path
    import java.nio.file.Paths

    import com.kazurayam.ks.testobject.gc.Garbage
    import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
    import com.kms.katalon.core.configuration.RunConfiguration

    import groovy.json.JsonOutput

    /**
     * Similar to the GC script but 
     * the TestObjects under the specified sub-folder in the "Object Repository" are selected. 
     */
    ObjectRepositoryGarbageCollector gc = 
            new ObjectRepositoryGarbageCollector.Builder()
                .includeObjectRepositoryFolder("main/Page_CURA Healthcare Service")
                .build()

    String json = gc.jsonifyGarbage()

    Path projectDir = Paths.get(RunConfiguration.getProjectDir())
    Path classOutputDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector")
    Path outDir = classOutputDir.resolve("ORGC_jsonifyGarbage_includeFolder_single")
    Files.createDirectories(outDir)
    File outFile = outDir.resolve("garbage.json").toFile()

    outFile.text = JsonOutput.prettyPrint(json)

    Garbage garbage = gc.getGarbage()
    assert 4 == garbage.size() : "expected garbage.size()==4 but was ${garbage.size()}"

This script selects Test Objects in a single sub-folder `main/Page_CURA Healthcare Service` in the Object Repository.

Output:

    {
        "Project name": "katalon",
        "includeObjectRepositoryFolder": [
            "main/Page_CURA Healthcare Service"
        ],
        "Number of TestCases": 28,
        "Number of TestObjects": 15,
        "Number of unused TestObjects": 4,
        "Unused TestObjects": [
            "main/Page_CURA Healthcare Service/a_Foo",
            "main/Page_CURA Healthcare Service/td_28",
            "main/Page_CURA Healthcare Service/xtra/a_Go to Homepage",
            "main/Page_CURA Healthcare Service/xtra/td_28"
        ],
        "Duration seconds": 0.972
    }

### Example 3: Scanning multiple sub-folders of Object Repository, find unused Test Objects, print the result into a JSON file

Script: `Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyGarbage_includeFolder_multiple`

    import java.nio.file.Files
    import java.nio.file.Path
    import java.nio.file.Paths

    import com.kazurayam.ks.testobject.gc.Garbage
    import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
    import com.kms.katalon.core.configuration.RunConfiguration

    import groovy.json.JsonOutput

    /**
     * Similar to the GC script but
     * the TestObjects under the specified sub-folder in the "Object Repository" are selected.
     */
    ObjectRepositoryGarbageCollector gc =
            new ObjectRepositoryGarbageCollector.Builder()
                .includeObjectRepositoryFolder("main/Page_CURA Healthcare Service")
                .includeObjectRepositoryFolder("main/Page_CURA Healthcare Service/xtra")
                .build()

    String json = gc.jsonifyGarbage()

    Path projectDir = Paths.get(RunConfiguration.getProjectDir())
    Path classOutputDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector")
    Path outDir = classOutputDir.resolve("ORGC_jsonifyGarbage_includeFolder_multiple")
    Files.createDirectories(outDir)
    File outFile = outDir.resolve("garbage.json").toFile()

    outFile.text = JsonOutput.prettyPrint(json)

    Garbage garbage = gc.getGarbage()
    assert 4 == garbage.size() : "expected garbage.size()==4 but was ${garbage.size()}"

This script selects Test Objects in multiple sub-folders in the Object Repository:

1.  `main/Page_CURA Healthcare Service`

2.  `main/Page_CURA Healthcare Service/xtra`

Output:

    {
        "Project name": "katalon",
        "includeObjectRepositoryFolder": [
            "main/Page_CURA Healthcare Service",
            "main/Page_CURA Healthcare Service/xtra"
        ],
        "Number of TestCases": 28,
        "Number of TestObjects": 15,
        "Number of unused TestObjects": 4,
        "Unused TestObjects": [
            "main/Page_CURA Healthcare Service/a_Foo",
            "main/Page_CURA Healthcare Service/td_28",
            "main/Page_CURA Healthcare Service/xtra/a_Go to Homepage",
            "main/Page_CURA Healthcare Service/xtra/td_28"
        ],
        "Duration seconds": 0.406
    }

### Example 4: Scanning sub-folders selected by pattern, find unused Test Objects, print the result into a JSON file

Script: `Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyGarbage_includeFolder_pattern`

    import java.nio.file.Files
    import java.nio.file.Path
    import java.nio.file.Paths

    import com.kazurayam.ks.testobject.gc.Garbage
    import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
    import com.kms.katalon.core.configuration.RunConfiguration

    import groovy.json.JsonOutput

    /**
     * Similar to the GC script but
     * the TestObjects under the specified sub-folders in the "Object Repository" are selected. 
     * The folder names are matched with the pattern like Ant DirectoryScanner.
     */
    ObjectRepositoryGarbageCollector gc =
            new ObjectRepositoryGarbageCollector.Builder()
                .includeObjectRepositoryFolder("**/Page_CURA*")
                .build()
    String json = gc.jsonifyGarbage()

    Path projectDir = Paths.get(RunConfiguration.getProjectDir())
    Path classOutputDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector")
    Path outDir = classOutputDir.resolve("ORGC_jsonifyGarbage_includeFolder_pattern")
    Files.createDirectories(outDir)
    File outFile = outDir.resolve("garbage.json").toFile()

    outFile.text = JsonOutput.prettyPrint(json)

    Garbage garbage = gc.getGarbage()
    assert 4 == garbage.size() : "expected garbage.size()==4 but was ${garbage.size()}"

This script selects Test Objects in multiple sub-folders in the Object Repository selected by a pattern `"**/Page_CURA*"`

Output:

    {
        "Project name": "katalon",
        "includeObjectRepositoryFolder": [
            "**/Page_CURA*"
        ],
        "Number of TestCases": 28,
        "Number of TestObjects": 15,
        "Number of unused TestObjects": 4,
        "Unused TestObjects": [
            "main/Page_CURA Healthcare Service/a_Foo",
            "main/Page_CURA Healthcare Service/td_28",
            "main/Page_CURA Healthcare Service/xtra/a_Go to Homepage",
            "main/Page_CURA Healthcare Service/xtra/td_28"
        ],
        "Duration seconds": 0.275
    }

### Example 5: Scanning sub-folders selected by pattern, find unused Test Objects, get the result as `Garbage` instance, use it as you like

Script: `Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_getGarbage_includeFolder_pattern`

    import com.kazurayam.ks.testobject.TestObjectId
    import com.kazurayam.ks.testobject.gc.Garbage
    import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector

    import groovy.json.JsonOutput

    /**
     * ObjectRepositoryGarbageCollector#getGarbage() demonstration
     */

    ObjectRepositoryGarbageCollector gc =
        new ObjectRepositoryGarbageCollector.Builder()
            .includeObjectRepositoryFolder("**/Page_CURA*")
            .build()

    Garbage garbage = gc.getGarbage()

    Set<TestObjectId> testObjectIds = garbage.getAllTestObjectIds()

    for (TestObjectId toi : testObjectIds) {
        println toi
    }

    assert testObjectIds.size() == 4

This script receives an instance of `Garbage` object which contains the data reported by the `ObjectRepositoryGarbageCollector`. The script calls the [Garbage class API](https://kazurayam.github.io/KS_ObjectRepositoryGarbageCollector/api/com/kazurayam/ks/testobject/gc/Garbage.html).

Output:

    4月 21, 2025 9:49:57 午後 com.kms.katalon.core.logging.KeywordLogger startTest
    情報: --------------------
    4月 21, 2025 9:49:57 午後 com.kms.katalon.core.logging.KeywordLogger startTest
    情報: START Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_getGarbage_includeFolder_pattern
    main/Page_CURA Healthcare Service/a_Foo
    main/Page_CURA Healthcare Service/td_28
    main/Page_CURA Healthcare Service/xtra/a_Go to Homepage
    main/Page_CURA Healthcare Service/xtra/td_28
    4月 21, 2025 9:49:59 午後 com.kms.katalon.core.logging.KeywordLogger endTest
    情報: END Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_getGarbage_includeFolder_pattern

## Developers' guide

### Project structure

[This repository](https://kazurayam.github.io/KS_ObjectRepositoryGarbageCollector) contains a Gradle [Multi-project](https://docs.gradle.org/current/userguide/intro_multi_project_builds.html).

`$rootProject/settings.gradle`:

    rootProject.name = "KS_ObjectRepositoryGarbageCollector"
    include 'lib'
    include 'katalon'
    include 'docs'

The `rootProject` contains 2 sub projects: `katalon` and `lib`.

    KS_ObjectRepositoryGarbageCollector
    ├── katalon
    │   ├── Checkpoints
    │   ├── Data Files
    │   ├── Drivers
    │   ├── Include
    │   ├── Keywords
    │   ├── Libs
    │   ├── Object Repository
    │   ├── Plugins
    │   ├── Profiles
    │   ├── Reports
    │   ├── Scripts
    │   ├── Test Cases
    │   ├── Test Listeners
    │   ├── Test Suites
    │   ├── bin
    │   ├── build
    │   └── settings
    └── lib
        ├── build
        └── src

    22 directories

The sub-project `katalon` is a typical Katalon Studio project. Please note that the `Drivers` folder contains 2 additional jar files. There are a lot of codes in the `Object Repository`. It has many scripts under the `Test Cases` folder.

    katalon
    ├── Drivers
    │   ├── KS_ObjectRepositoryGarbageCollector-0.3.1-SNAPSHOT.jar
    │   └── monk-directory-scanner-0.1.0.jar
    ├── Object Repository
    │   ├── main
    │   │   └── Page_CURA Healthcare Service
    │   │       ├── a_Foo.rs
    │   │       ├── a_Go to Homepage.rs
    │   │       ├── a_Make Appointment.rs
    │   │       ├── button_Book Appointment.rs
    │   │       ├── button_Login.rs
    │   │       ├── input_Apply for hospital readmission_hospit_63901f.rs
    │   │       ├── input_Medicaid_programs.rs
    │   │       ├── input_Password_password.rs
    │   │       ├── input_Username_username.rs
    │   │       ├── input_Visit Date.rs
    │   │       ├── select_Tokyo CURA Healthcare Center        _5b4107.rs
    │   │       ├── td_28.rs
    │   │       ├── textarea_Comment_comment.rs
    │   │       └── xtra
    │   │           ├── a_Go to Homepage.rs
    │   │           └── td_28.rs
    │   └── misc
    │       └── dummy1.rs
    ├── Profiles
    │   └── default.glbl
    ├── Test Cases
    │   ├── demo
    │   │   ├── ObjectRepositoryDecorator
    │   │   │   ├── ORD_getLocatorIndex.tc
    │   │   │   ├── ORD_getLocatorIndex_filterByRegex.tc
    │   │   │   ├── ORD_getLocatorIndex_filterByString.tc
    │   │   │   ├── ORD_getTestObjectIdList.tc
    │   │   │   ├── ORD_getTestObjectIdList_filteredByRegex.tc
    │   │   │   ├── ORD_getTestObjectIdList_filteredByString.tc
    │   │   │   ├── ORD_jsonifyLocatorIndex.tc
    │   │   │   ├── ORD_jsonifyLocatorIndex_filterByRegex.tc
    │   │   │   ├── ORD_jsonifyLocatorIndex_filterByString.tc
    │   │   │   ├── ORD_jsonifyTestObjectIdList.tc
    │   │   │   ├── ORD_jsonifyTestObjectIdList_filterByRegex.tc
    │   │   │   └── ORD_jsonifyTestObjectIdList_filterByString.tc
    │   │   ├── ObjectRepositoryGarbageCollector
    │   │   │   ├── ORGC_getGarbage_includeFolder_pattern.tc
    │   │   │   ├── ORGC_jsonifyBackwardReferences_includeFolder_pattern.tc
    │   │   │   ├── ORGC_jsonifyGarbage.tc
    │   │   │   ├── ORGC_jsonifyGarbage_includeFolder_multiple.tc
    │   │   │   ├── ORGC_jsonifyGarbage_includeFolder_pattern.tc
    │   │   │   ├── ORGC_jsonifyGarbage_includeFolder_single.tc
    │   │   │   └── ORGC_jsonifyLocatorIndex.tc
    │   │   ├── ScriptsDecorator
    │   │   │   ├── SD_getGroovyFiles.tc
    │   │   │   ├── SD_getGroovyFiles_include_demo_and_main.tc
    │   │   │   └── SD_getGroovyFiles_include_main_only.tc
    │   │   └── cleanTestOutput.tc
    │   ├── main
    │   │   ├── TC0.tc
    │   │   └── TC1.tc
    │   └── misc
    │       ├── runDirectoryScanner.tc
    │       ├── runKatalonProjectDirectoryResolver.tc
    │       └── studyRunConfiguration.tc
    ├── Test Listeners
    │   └── TL1.groovy
    ├── Test Suites
    │   └── demo
    │       ├── runAll.groovy
    │       └── runAll.ts
    └── build.gradle

    18 directories, 51 files

The sub-project `lib` is a Gradle Java project that has a typical directory structure.

    lib
    ├── build
    │   ├── docs
    │   │   └── groovydoc
    │   │       ├── allclasses-frame.html
    │   │       ├── com
    │   │       │   └── kazurayam
    │   │       │       └── ks
    │   │       │           ├── configuration
    │   │       │           │   ├── KatalonProjectDirectoryResolver.html
    │   │       │           │   ├── RunConfigurationConfigurator.html
    │   │       │           │   ├── package-frame.html
    │   │       │           │   └── package-summary.html
    │   │       │           ├── reporting
    │   │       │           │   ├── Shorthand.Builder.html
    │   │       │           │   ├── Shorthand.html
    │   │       │           │   ├── package-frame.html
    │   │       │           │   └── package-summary.html
    │   │       │           ├── testcase
    │   │       │           │   ├── DigestedLine.Builder.html
    │   │       │           │   ├── DigestedLine.DigestedLineSerializer.html
    │   │       │           │   ├── DigestedLine.html
    │   │       │           │   ├── DigestedText.DigestedTextSerializer.html
    │   │       │           │   ├── DigestedText.html
    │   │       │           │   ├── ScriptsAccessor.Builder.html
    │   │       │           │   ├── ScriptsAccessor.html
    │   │       │           │   ├── ScriptsDecorator.Builder.html
    │   │       │           │   ├── ScriptsDecorator.html
    │   │       │           │   ├── TestCaseId.TestCaseIdSerializer.html
    │   │       │           │   ├── TestCaseId.html
    │   │       │           │   ├── TestCaseScriptDigester.html
    │   │       │           │   ├── TextDigester.html
    │   │       │           │   ├── package-frame.html
    │   │       │           │   └── package-summary.html
    │   │       │           └── testobject
    │   │       │               ├── Locator.LocatorSerializer.html
    │   │       │               ├── Locator.html
    │   │       │               ├── LocatorIndex.LocatorIndexSerializer.html
    │   │       │               ├── LocatorIndex.html
    │   │       │               ├── ObjectRepositoryAccessor.Builder.html
    │   │       │               ├── ObjectRepositoryAccessor.html
    │   │       │               ├── ObjectRepositoryDecorator.Builder.html
    │   │       │               ├── ObjectRepositoryDecorator.html
    │   │       │               ├── RegexOptedTextMatcher.html
    │   │       │               ├── TestObjectEssence.TestObjectEssenceSerializer.html
    │   │       │               ├── TestObjectEssence.html
    │   │       │               ├── TestObjectId.TestObjectIdSerializer.html
    │   │       │               ├── TestObjectId.html
    │   │       │               ├── gc
    │   │       │               │   ├── BackwardReferences.BackwardReferencesSerializer.html
    │   │       │               │   ├── BackwardReferences.html
    │   │       │               │   ├── Database.DatabaseSerializer.html
    │   │       │               │   ├── Database.html
    │   │       │               │   ├── ForwardReference.ForwardReferenceSerializer.html
    │   │       │               │   ├── ForwardReference.html
    │   │       │               │   ├── Garbage.GarbageSerializer.html
    │   │       │               │   ├── Garbage.html
    │   │       │               │   ├── ObjectRepositoryGarbageCollector.Builder.html
    │   │       │               │   ├── ObjectRepositoryGarbageCollector.ObjectRepositoryGarbageCollectorSerializer.html
    │   │       │               │   ├── ObjectRepositoryGarbageCollector.html
    │   │       │               │   ├── package-frame.html
    │   │       │               │   └── package-summary.html
    │   │       │               ├── package-frame.html
    │   │       │               └── package-summary.html
    │   │       ├── deprecated-list.html
    │   │       ├── groovy.ico
    │   │       ├── help-doc.html
    │   │       ├── index-all.html
    │   │       ├── index.html
    │   │       ├── inherit.gif
    │   │       ├── internal
    │   │       │   ├── GlobalVariable.html
    │   │       │   ├── package-frame.html
    │   │       │   └── package-summary.html
    │   │       ├── overview-frame.html
    │   │       ├── overview-summary.html
    │   │       ├── package-list
    │   │       └── stylesheet.css
    │   └── libs
    │       ├── KS_ObjectRepositoryGarbageCollector-0.3.1.jar
    │       └── KS_ObjectRepositoryGarbageCollector-0.3.1.jar.asc
    ├── build.gradle
    └── src
        └── main
            └── groovy
                ├── com
                │   └── kazurayam
                │       └── ks
                │           ├── configuration
                │           │   ├── KatalonProjectDirectoryResolver.groovy
                │           │   └── RunConfigurationConfigurator.groovy
                │           ├── reporting
                │           │   └── Shorthand.groovy
                │           ├── testcase
                │           │   ├── DigestedLine.groovy
                │           │   ├── DigestedText.groovy
                │           │   ├── ScriptsAccessor.groovy
                │           │   ├── ScriptsDecorator.groovy
                │           │   ├── TestCaseId.groovy
                │           │   ├── TestCaseScriptDigester.groovy
                │           │   └── TextDigester.groovy
                │           └── testobject
                │               ├── Locator.groovy
                │               ├── LocatorIndex.groovy
                │               ├── ObjectRepositoryAccessor.groovy
                │               ├── ObjectRepositoryDecorator.groovy
                │               ├── RegexOptedTextMatcher.groovy
                │               ├── TestObjectEssence.groovy
                │               ├── TestObjectId.groovy
                │               └── gc
                │                   ├── BackwardReferences.groovy
                │                   ├── Database.groovy
                │                   ├── ForwardReference.groovy
                │                   ├── Garbage.groovy
                │                   └── ObjectRepositoryGarbageCollector.groovy
                └── internal
                    └── GlobalVariable.groovy

    26 directories, 91 files

### Building jar in the `lib` project

    $ cd $rootProject/lib
    $ gradle jar

### Importing jars from the `lib` sub-project into the `katalon` sub-project

    $ cd $rootProject/katalon
    $ gradle importDrivers

See the [katalon/build.gradle](https://www.github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/tree/develop/katalon/build.gradle)

`katalon/build.gradle`

            credentials {
                username = project.findProperty('gpr.user')
                password = project.findProperty('gpr.key')
            }
        }
        maven {
            url = uri("https://maven.pkg.github.com/kazurayam/ks-object-repository-garbage-collector")
            credentials {
                username = project.findProperty('gpr.user')
                password = project.findProperty('gpr.key')
            }
        }
    }

    dependencies {
