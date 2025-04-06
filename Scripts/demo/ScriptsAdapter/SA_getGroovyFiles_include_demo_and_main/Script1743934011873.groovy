import com.kazurayam.ks.testcase.ScriptsAdapter

import java.nio.file.Path
import java.nio.file.Paths

ScriptsAdapter adapter = new ScriptsAdapter.Builder()
							.includeFolder("demo")
							.includeFolder("main")
							.build()

adapter.getIncludeFoldersSpecification().each { it ->
	println "includeFolder:" + it
}
						
List<Path> groovyFiles = adapter.getGroovyFiles()

groovyFiles.each { p ->
	println p.toString()
}

assert 24 == groovyFiles.size()