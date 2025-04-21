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

