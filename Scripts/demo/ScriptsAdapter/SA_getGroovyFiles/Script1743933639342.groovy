import com.kazurayam.ks.testcase.ScriptsAdapter

import java.nio.file.Path
import java.nio.file.Paths

ScriptsAdapter adapter = new ScriptsAdapter.Builder().build()

List<Path> groovyFiles = adapter.getGroovyFiles()

groovyFiles.each { p ->
	println p.toString()
}

assert 45 == groovyFiles.size()