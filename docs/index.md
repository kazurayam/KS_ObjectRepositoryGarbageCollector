# \[Katalon Studio\] Object Repository Garbage Collection

back to the [repository](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector)

## Problem to solve

Let me start with explaining what problem this library is designed to solve.

I will present a sample Katalon Studio project that has a Test Case named [Test Cases/main/TC1](https://github.com/kazurayam/ObjectRepositoryGarbageCollection/blob/develop/Scripts/main/TC1/Script1677544889443.groovy). The script is as follows:

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

This script contains lines with fragment `findTestObject(…​)`. A call `findTestObject(…​)` method refers to a **Test Object**. If you read the script, you would find that it contains 13 lines with `findTestObject(…​)` call. However, if you read it carefully, you will find this script refers to 11 Test objects.

Now let’s look at the `Object Repository` directory. In the Katalon Studio GUI, the directory tree looks like this:

![Object Repository with 15 Test Objects](https://kazurayam.github.io/KS_ObjectRepositoryGarbageCollector/images/1_1_ObjectRepositoryContains15TestObjects.png)

The `Object Repository` there are 15 Test Objects defined.

On the other hand, I carefully read all Test Case scripts, and I found that only 11 Test Objects are actually refered to by Test Cases. 15 minus 11 = 4 Test Objects are not used. These are garbages. Now I want to clean up the Object Repository.

**Problem** : *How can I tell which Test Objects are garbages? How can I be sure each Test Objects are safe to delete?*

This sample Katalon Studio project is a small one; it has just 15 Test Objects. I believe that many Katalon users have far larger projects with 100 Test Objects, 200, 3000, …​ possibly even more. The more Test Objects you have, the more garbages you would have. The more garbages you have, the more it becomes difficult to maintain the project clean.

Therefore I have developed this ObjectRepositoryGarbageCollector class, which will show the list of unused Test Objects.

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

## ObjectRepositoryGarbageCollector class usages

### ORGC\_jsonifyGarbages

#### Test Case script

`Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyGarbages`:

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
                .includeFolder("**/*")
                .build()

    // gc.jsonifyGarbages() triggers scanning through the entire "Object Repository".
    // All references from TestCase scripts to TestObjects will be identified.
    // Consequently, it can result a list of unused TestObjects.
    // Will output the result into a JSON string
    String json = gc.jsonifyGarbages()

    // just print the JSON into the console
    println JsonOutput.prettyPrint(json)

#### Output

#### Explanation

1.  The script will look into the entire "Object Repository" folder and check the contained TestObjects if used or not by any of TestCase scripts

2.  there are 30 Test Cases in the `Test Cases` folder

3.  there are 15 Test Objects in the `Object Repository` folder

4.  amongst 15, there are 4 unused Test Objects

5.  The script took 1.6 seconds to finish.

## ExtendedObjectRepository class usages
