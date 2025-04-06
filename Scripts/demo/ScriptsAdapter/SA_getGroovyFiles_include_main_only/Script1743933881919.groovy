import com.kazurayam.ks.testcase.ScriptsAdapter

import java.nio.file.Path
import java.nio.file.Paths

ScriptsAdapter adapter = new ScriptsAdapter.Builder()
							.includeFolder("main")
							.build()

List<Path> groovyFiles = adapter.getGroovyFiles()

groovyFiles.each { p ->
	println p.toString()
}

assert 2 == groovyFiles.size()