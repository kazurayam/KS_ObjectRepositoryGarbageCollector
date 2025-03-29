import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.context.TestCaseContext

import internal.GlobalVariable

class TL1 {
	
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		// shorten "Test Cases/main/TC1" -> "main/TC1"
		GlobalVariable.TESTCASE_ID = testCaseContext.getTestCaseId().replace("Test Cases/", "")
	}
	
}