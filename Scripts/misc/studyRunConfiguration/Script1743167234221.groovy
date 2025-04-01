import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.TestObject as TestObject

File executionSettingsFile = File.createTempFile('GC3-', '.tmp')

executionSettingsFile.deleteOnExit()

RunConfiguration.setExecutionSettingFile(executionSettingsFile.toString())

println(RunConfiguration.getProjectDir())

TestObject tObj = findTestObject('Page_CURA Healthcare Service/button_Book Appointment')

assert tObj != null

println tObj.getObjectId()