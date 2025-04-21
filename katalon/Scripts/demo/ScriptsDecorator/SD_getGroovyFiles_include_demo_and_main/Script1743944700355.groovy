import com.kazurayam.ks.testcase.ScriptsDecorator

import java.nio.file.Path
import java.nio.file.Paths

ScriptsDecorator decorator = new ScriptsDecorator.Builder()
							.includeFolder("demo")
							.includeFolder("main")
							.build()

decorator.getIncludeFoldersSpecification().each { it ->
	println "includeFolder:" + it
}
						
List<Path> groovyFiles = decorator.getGroovyFiles()

groovyFiles.each { p ->
	println p.toString()
}

assert 24 == groovyFiles.size()