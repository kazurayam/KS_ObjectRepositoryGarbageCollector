# \[Katalon Studio\] Object Repository Garbage Collection

You can download a Katalon Studio project at <https://github.com/kazurayam/ObjectRepositoryGarbageCollection/releases> which presents the sample code.

## Problem to solve


The sample project has a Test Case named [Test Cases/main/TC1](https://github.com/kazurayam/ObjectRepositoryGarbageCollection/blob/develop/Scripts/main/TC1/Script1677544889443.groovy), which is as follows:

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

The script contains many lines that include such fragment `findTestObjcet(…​)`. A call `findTestObject(…​)` method refers to a **Test Object**. If you read through the script, you would find that it contains 13 lines with `findTestObject(…​)` call. However, if you read it very carefully, you will find that some lines are referring to the same Test Objects. In fact, there are 11 Test objects referred by this script.

On the other hand, you can look at the `Object Repository` directory in the Katalon Studio GUI. The directory tree would look like this:

![Object Repository with 15 Test Objects](https://kazurayam.github.io/ObjectRepositoryGarbageCollection/images/1_1_ObjectRepositoryContains15TestObjects.png)

You can find that in the `Object Repository` there are 15 Test Objects defined.

**Problem** : There are 15 Test Objects defined, whereas 11 Test Objects are used by the Test Cases. So 15 minus 11 = 4 Test Cases are not used. In other words, there are 4 garbages. *Can you tell which Test Object is garbage?*

This sample Katalon Studio project has just 15 Test Objects. It is exceptionally small scale. I believe that many Katalon users have larger projects with 50 Test Objects, 100, 200, …​ possibly even more. The more Test Objects you have, the more you would have garbages.

The garbage Test Objects will make it difficult to maintain the Katalon projects clean & fit. Users want to see the list garbage Test Objects so that they are confident that they can remove them safely. Unfortunately Katalon Studio does not provide any feature that let us know the list of garbages.
