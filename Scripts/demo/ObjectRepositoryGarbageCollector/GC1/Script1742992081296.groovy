import java.nio.file.Files as Files
import java.nio.file.Path as Path
import java.nio.file.Paths as Paths
import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector as ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kazurayam.ks.reporting.Shorthand as Shorthand
import com.kazurayam.ks.reporting.TargetDocumentFormat

/**
 * outputs a JSON file which contains a list of garbage Test Objects
 * in the Object Repository directory.
 * A garbage Test Object is a Test Object which is unused by any of Test Cases.
 */
// the Garbage Collector instance will scan the Object Repository directory
// and the Scripts directory
ObjectRepositoryGarbageCollector gc = new ObjectRepositoryGarbageCollector.Builder().build()

// the gc.garbages() method call can compile a list of garbate Test Objects,
// output the information in a JSON string
String json = gc.garbages()

// write it into a file
Shorthand sh = new Shorthand.Builder()
				.subDir('domo/ObjectRepositoryGarbageCollector')
				.fileName('GC1.adoc')
				.docFormat(TargetDocumentFormat.ASCIIDOC)
				.syntaxHighlighting("json")
				.build()

sh.codeWithHeader('\n=== Output of TestCases/demo/ObjectRepositoryGarbageCollector/GC1\n\n gc.garbages() returned\n', json)