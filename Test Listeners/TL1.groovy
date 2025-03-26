import com.kazurayam.ks.testobject.ObjectRepositoryExtender
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestSuiteContext

class TL1 {
	
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		ObjectRepositoryExtender ext = new ObjectRepositoryExtender()
		ext.apply()
	}
}