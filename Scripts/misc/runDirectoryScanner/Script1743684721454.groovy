// Test Cases/misc/runDirectoryScanner

import com.kazurayam.ant.DirectoryScanner

DirectoryScanner ds = new DirectoryScanner()
ds.setBasedir("./Object Repository")
ds.setIncludes("**/Page_CURA*/")
ds.setExcludes("**/*Service2/")
ds.scan()

List<String> includedDirectories = ds.getIncludedDirectories() as List
List<String> includedFiles = ds.getIncludedFiles() as List
List<String> excludedDirectories = ds.getExcludedDirectories() as List
List<String> excludedFiles = ds.getExcludedFiles() as List

println "Included Directories:"
includedDirectories.each { it -> println "    " + it }

println "Included Files:"
includedFiles.each { it -> println "    " + it }

println "Excluded Directories:"
excludedDirectories.each { it -> println "    " + it }

println "Excluded Files:"
excludedFiles.each { it -> println "    " + it }
