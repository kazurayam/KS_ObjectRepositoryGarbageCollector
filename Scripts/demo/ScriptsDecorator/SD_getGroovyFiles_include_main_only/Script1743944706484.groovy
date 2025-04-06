import com.kazurayam.ks.testcase.ScriptsDecorator

import java.nio.file.Path
import java.nio.file.Paths

ScriptsDecorator decorator = new ScriptsDecorator.Builder()
							.includeFolder("main")
							.build()

List<Path> groovyFiles = decorator.getGroovyFiles()

groovyFiles.each { p ->
	println p.toString()
}

assert 2 == groovyFiles.size()