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
== Solution proposed

### How to install the library

You can download a Katalon Studio project at <https://github.com/kazurayam/ObjectRepositoryGarbageCollection/releases> which presents the sample code.

### Test Cases/sampleUse/step2

    import com.kazurayam.ks.testobject.ObjectRepositoryExtension
    import com.kms.katalon.core.testobject.ObjectRepository

    /*
     * Test Caes/sampleUse/step2
     */

    // modify com.kms.katalon.core.testobject.ObjectRepository class on the fly
    ObjectRepositoryExtension.apply()

    // step2: select Test Object with ID that match certain pattern by String.contains()
    List<String> idsSelectedByStringContains = ObjectRepository.list("button_")

    println "\n---------------- idsSelectedByStringContains -------------------------"
    idsSelectedByStringContains.forEach { s ->
        println s
    }

### Output

    Unresolved directive in s2_solution_proposed.adoc - include::stdout.txt[lines=28..32]
