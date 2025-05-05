import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.apache.commons.io.FileUtils

import com.kms.katalon.core.configuration.RunConfiguration
import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector
import com.kazurayam.ks.testobject.combine.Garbage
import com.kazurayam.ks.testobject.TestObjectId

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path objectRepositoryDir = projectDir.resolve('Object Repository')

//---------------------------------------------------------------------
// We will setup additional test fixture for this testcase

// create `Object Repository/temp` directory.
Path temp = objectRepositoryDir.resolve('temp')
Files.createDirectories(temp)

// copy the files in the `Object Repository/main` directory`into the temp dir
Path main = objectRepositoryDir.resolve('main')
FileUtils.copyDirectory(main.toFile(), temp.toFile())

//---------------------------------------------------------------------
// create ObjectRepositoryGarbageCollector that selects the "temp" subfolder
ObjectRepositoryGarbageCollector gc =
		new ObjectRepositoryGarbageCollector.Builder()
			.includeObjectRepositoryFolder("temp")
			.build()

// find the garbage
Garbage garbage = gc.getGarbage()
Set<TestObjectId> testObjectIds = garbage.getAllTestObjectIds()
	
for (TestObjectId toi : testObjectIds) {
	println toi
}	
// all Test Objects in the "temp" folder are unused by any of Test Cases
assert testObjectIds.size() == 15  

//---------------------------------------------------------------------
// now we will programatically delete the garbage = unused TestObjects
for (TestObjectId toi : testObjectIds) {
	Path relativePath = toi.getRelativePath()
	Path rsFile = objectRepositoryDir.resolve(relativePath)
	assert Files.exists(rsFile)
	// now delete the "*.rs" file
	Files.delete(rsFile)
	// make sure the file has been deleted
	assert ! Files.exists(rsFile)
}

// erase the `Object Repository/temp` directory to tear down
FileUtils.deleteDirectory(temp.toFile())
