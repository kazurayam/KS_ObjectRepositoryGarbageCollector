import com.kazurayam.ks.testobject.ObjectRepositoryExtension
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestSuiteContext

class TL1 {
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		ObjectRepositoryExtension.apply()
	}
}