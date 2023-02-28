package com.kazurayam.ks

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ObjectRepository
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

/**
 * This class extends the `com.kms.katalon.core.testobject.ObjectRepository` class and
 * add some useful methods on the fly by Groovy's Meta-programming technique.
 * 
 * @author kazurayam
 */
public class ObjectRepositoryExtension {

	private ObjectRepositoryExtension() {}

	@Keyword
	static void apply() {
		ObjectRepository.metaClass.invokeMethod = { String name, args ->
			switch (name) {
				case "list" :
					return list(delegate)
					break
				default :
				// just do what ObejctRepository is designed to do
					def result
					try {
						result = delegate.meataClass.getMetaMethod(name, args).invoke(delegate, args)
					} catch (Exception e) {
						System.err.println("call to method $name raised an Exception")
						e.printStackTrace()
					}
					return result
			}
		}
	}

	/*
	 * 
	 */
	static List<String> list(ObjectRepository objectRepository) throws IOException {
		Path dir = Paths.get("./Object Repository")
		ObjectRepositoryVisitor visitor = new ObjectRepositoryVisitor(dir)
		Files.walkFileTree(dir, visitor)
		return visitor.testObjects()
	}
}


