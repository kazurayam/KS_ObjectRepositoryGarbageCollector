-   Table of content
    {:toc}

# \[Katalon Studio\] Object Repository Garbage Collector / User Guide

-   [repository](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector)

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

This script contains a lot of lines with fragment `findTestObject(…​)`. Each call `findTestObject(…​)` method refers to a **Test Object**. If you read the script, you would find that it contains 13 lines with `findTestObject(…​)` call.

Let me show you a screenshot of the `Object Repository` of the Katalon project. The directory tree looks like this:

![Object Repository containing unused Test Objects](https://kazurayam.github.io/KS_ObjectRepositoryGarbageCollector/images/1_1_ObjectRepositoryContainingUnusedTestObjects.png)

In the `Object Repository` there are 16 Test Objects defined.

Easily we can see that there are some unused Test Objects.

**Problem** : *How can I tell which Test Objects are unused by any of Test Cases? How can I be sure which Test Objects are safe to delete?*

This sample is small; it has only 16 Test Objects. But it is a hard job yet to look for garbage. I believe that many Katalon users have far larger projects with 100 Test Objects, 500, 1000, …​ or even more. The more Test Objects you have, it will be even worse.

So I developed this library, which will scan a katalon project and identify unused Test Objects quickly.

## Solution

### Environment

I developed this project using the following environment:

-   macOS Sonoma 14.7.4

-   Katalon Studio Free v10.1.0 which bundles JDK17

The KS\_ObjectRepositoryGargageCollecor-x.x.x.jar requires Katalon Studio v10 and newer.

It wouldn’t run on v9 and older due to JDK version.

The jar would run in Katalon Runtime Engine not only in Katalon Studio.

### Installation

How to get started with the **Object Repository Garbage Collector** in your own Katalon Studio project?

1.  Visit the [KS\_ObjectRepositoryGarbageCollector, Releases](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/releases) page. Identify the latest release. Find the `KS_ObjectRepositoryGarbageCollector-x.x.x.jar` attached to each release. Download the jar, save it into the `Drivers` folder of your local Katalon Studio project.

2.  Visit the [MonkDirectoryScanner, Releases](https://github.com/kazurayam/MonkDirectoryScanner/releases) page. Identify the latest release. Find the `MonkDirectoryScanner-x.x.x.jar` attached to each release. Download the jar, save it into the `Drivers` folder of your local Katalon Studio project.

3.  Close your project and reopen it so that your Katalon Studio recognize the new jar files.

4.  Open the "Project" &gt; "Settings", "Library Management" dialog. Check if 2 jars are there: ![2 1 LibraryManagement](https://kazurayam.github.io/KS_ObjectRepositoryGarbageCollector/images/2_1_LibraryManagement.png)

5.  You want to create a Test Case to run the **garbage collector** class. You can copy & reuse the code [Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC\_jsonifyGarbages](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/blob/develop/Scripts/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyGarbages/Script1743835392014.groovy). Thes Test Case should run in any project.

6.  You are done! Run the test case and see the output in the console.

## Examples

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
    │   ├── KS_ObjectRepositoryGarbageCollector-0.3.0-SNAPSHOT.jar
    │   └── MonkDirectoryScanner-0.1.0.jar
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
    │   │   │   ├── ORGC_getGarbages_includeFolder_pattern.tc
    │   │   │   ├── ORGC_jsonifyBackwardReferences_includeFolder_pattern.tc
    │   │   │   ├── ORGC_jsonifyGarbages.tc
    │   │   │   ├── ORGC_jsonifyGarbages_includeFolder_multiple.tc
    │   │   │   ├── ORGC_jsonifyGarbages_includeFolder_pattern.tc
    │   │   │   ├── ORGC_jsonifyGarbages_includeFolder_single.tc
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
    │   └── libs
    │       ├── KS_ObjectRepositoryGarbageCollector-0.3.0.jar
    │       └── KS_ObjectRepositoryGarbageCollector-0.3.0.jar.asc
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
                │                   ├── Garbages.groovy
                │                   └── ObjectRepositoryGarbageCollector.groovy
                └── internal
                    └── GlobalVariable.groovy

    15 directories, 26 files

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
