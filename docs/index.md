# \[Katalon Studio\] Object Repository Garbage Collection

## Problem to solve

Let me start with explaining what problem this library is designed to solve.

I will present a sample Katalon Studio project that has a Test Case named [Test Cases/main/TC1](https://github.com/kazurayam/ObjectRepositoryGarbageCollection/blob/develop/Scripts/main/TC1/Script1677544889443.groovy). The script is as follows:

    import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
    import com.kms.katalon.core.model.FailureHandling as FailureHandling
    import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

    WebUI.openBrowser('')

    WebUI.navigateToUrl('https://katalon-demo-cura.herokuapp.com/')

    WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/a_Make Appointment'))

    WebUI.waitForElementPresent(findTestObject('Object Repository/Page_CURA Healthcare Service/a_Mark Appointments'), 3, FailureHandling.OPTIONAL)

    WebUI.setText(findTestObject('Object Repository/Page_CURA Healthcare Service/input_Username_username'), 'John Doe')

    WebUI.setEncryptedText(findTestObject('Object Repository/Page_CURA Healthcare Service/input_Password_password'), 'g3/DOGG74jC3Flrr3yH+3D/yKbOqqUNM')

    WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/button_Login'))

    WebUI.selectOptionByValue(findTestObject('Object Repository/Page_CURA Healthcare Service/select_Tokyo CURA Healthcare Center        _5b4107'), 
        'Hongkong CURA Healthcare Center', true)

    WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/input_Apply for hospital readmission_hospit_63901f'))

    WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/input_Medicaid_programs'))

    WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/input_Visit Date'))

    WebUI.setText(findTestObject('Object Repository/Page_CURA Healthcare Service/input_Visit Date'), "20230325")

    WebUI.setText(findTestObject('Object Repository/Page_CURA Healthcare Service/textarea_Comment_comment'), 'This is a comment')

    WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/button_Book Appointment'))

    WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/a_Go to Homepage'))

This script contains lines with fragment `findTestObject(…​)`. A call `findTestObject(…​)` method refers to a **Test Object**. If you read the script, you would find that it contains 13 lines with `findTestObject(…​)` call. However, if you read it carefully, you will find that some lines are referring to the same Test Objects. In fact, this script refers to 11 Test objects.

On the other hand, you can look at the `Object Repository` directory in the Katalon Studio GUI. The directory tree would look like this:

![Object Repository with 15 Test Objects](https://kazurayam.github.io/ObjectRepositoryGarbageCollection/images/1_1_ObjectRepositoryContains15TestObjects.png)

You can find that in the `Object Repository` there are 15 Test Objects defined.

**Problem** : There are 15 Test Objects defined, whereas 11 Test Objects are used by Test Cases. 15 minus 11 = 4 Test Objects are not used by Test Cases. In other words, there are 4 garbages. *How can you tell which Test Objects are garbages?*

This sample Katalon Studio project has just 15 Test Objects. It is a small project. I believe that many Katalon users have larger projects with 50 Test Objects, 100, 300, …​ possibly even more. The more Test Objects you have, the more garbages you would have. The more garbages you have, the more it becomes difficult to find them. *Large scale Katalon Studio projects always contain a lot of garbages*

In order to keep Katalon projects maintainable, users would want to remove the garbages. But they do not want to break their projects. They definitely want a tool that provides a list of unused Test Objects.

*Unfortunately Katalon Studio provides no such tool that shows you a list of unused Test Objects.*

Therefore I have developed it.
== Solution

### How to install the library

You can download the jar from the \[Releases\](<https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/releases>) page. Download the `KS_ObjectRepositoryGarbageCollector-x.x.x.jar`; locate it into the `Driviers` folder of your local Katalon Studio project.

In order to confirm if you have the library, open your project in Katalon Studio, open "Project" &gt; "Settings" to check the "Library Management". In the dialog, the jar should be found in the dialog:

![2 1 LibraryManagement](https://kazurayam.github.io/KS_ObjectRepositoryGarbageCollector/images/2_1_LibraryManagement.png)

### A demonstration: run the ObjectRepositoryGarbageCollector

Here I made a script `Test Cases/ObjectRepositoryGarbageCollector/GC1`:

    import java.nio.file.Files
    import java.nio.file.Path
    import java.nio.file.Paths

    import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector
    import com.kms.katalon.core.configuration.RunConfiguration

    import demo.Reporter

    /**
     * outputs a JSON file which contains a list of garbage Test Objects
     * in the Object Repository directory.
     * A garbage Test Object is a Test Object which is unused by any of Test Cases.
     */

    // the Garbage Collector instance will scan the Object Repository directory
    // and the Scripts directory
    ObjectRepositoryGarbageCollector gc = new ObjectRepositoryGarbageCollector.Builder().build()

    // the gc.garbages() method call can compile a list of garbate Test Objects,
    // output the information in a JSON string
    String json = gc.garbages()

    // write it into a file
    Reporter rp = new Reporter("ObjectRepositoryGarbageCollector/GC1.adoc")
    rp.report("=== Output of TestCases/demo/ObjectRepositoryGarbageCollector/GC1\n",
        "gc.garbages() returned\n",
        "```",
        json,
        "```")

When I run the script in Katalon Studio, I got the following output in the console:

### Output of TestCases/demo/ObjectRepositoryGarbageCollector/GC1

gc.garbages() returned

    {
        "stats": {
            "Number of TestCases": 30,
            "Number of TestObjects": 15,
            "Duration seconds": 2.214
        },
        "Number of garbages": 4,
        "garbages": [
            {
                "TestObjectId": "Page_CURA Healthcare Service/a_Foo"
            },
            {
                "TestObjectId": "Page_CURA Healthcare Service/td_28"
            },
            {
                "TestObjectId": "Page_CURA Healthcare Service2/a_Go to Homepage"
            },
            {
                "TestObjectId": "Page_CURA Healthcare Service2/td_28"
            }
        ]
    }
