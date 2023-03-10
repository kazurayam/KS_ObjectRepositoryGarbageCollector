# \[Katalon Studio\] Object Repository Garbage Collection

## Problem to solve

You can download a Katalon Studio project at <https://github.com/kazurayam/ObjectRepositoryGarbageCollection/releases> which presents the sample code.

The sample project has a Test Case named [Test Cases/main/TC1](https://github.com/kazurayam/ObjectRepositoryGarbageCollection/blob/develop/Scripts/main/TC1/Script1677544889443.groovy). The script contains many lines that include such fragment `findTestObjcet(…​)`, for example:

    WebUI.click(findTestObject('Object Repository/Page_CURA Healthcare Service/a_Make Appointment'))

The method call `findTestObject(…​)` method looks up the **Test Objects**. If you read through the script, you would find that it contains 13 lines with `findTestObject(…​)` call. However, if you read it very carefully, you will find that some lines are referring to the same Test Objects. In fact there are 11 Test objects referred by the script.

On the other hand, you can look at the `Object Repository` directory in the Katalon Studio GUI. The directory tree would look like this:

!\[Object Repository with 15 Test Objects\](<https://kazurayam.github.io/ObjectRepositoryGarbageCollection/images/1_1_ObjectRepositoryContains15TestObjects.png>)

You can find that in the `Object Repository` there are 15 Test Objects defined.

**Problem** : There are 15 Test Objects defined, whereas 11 Test Objects are used by the Test Cases. So 15 minus 11 = 4 Test Cases are not used. In other words, there are 4 garbages. *Can you tell which Test Object is garbage?*

This sample Katalon Studio project has just 15 Test Objects. It is exceptionally small scale. I believe that many Katalon users have larger projects with 50 Test Objects, 100, 200, …​ possibly even more. The more Test Objects you have, the more you would have garbages.

The garbage Test Objects will make it difficult to maintain the Katalon projects clean & fit. Users want to see the list garbage Test Objects so that they are confident that they can remove them safely. Unfortunately Katalon Studio does not provide any feature that let us know the list of garbages.
