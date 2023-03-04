package com.kazurayam.ks.testobject

import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject


/**
 * This class extends the `com.kms.katalon.core.testobject.ObjectRepository` class and
 * add some useful methods on the fly by Groovy's Meta-programming technique.
 * 
 * @author kazurayam
 */
public class ObjectRepositoryExtension {

	ObjectRepositoryExtension() {}

	@Keyword
	void apply() {
		ObjectRepository.metaClass.static.invokeMethod = { String name, args ->
			switch (name) {
				case "listRaw" :
					return this.listRaw(args)
					break
				case "list" :
					return this.list(args)
					break
				case "listGistRaw" :
					return this.listGistRaw(args)
					break
				case "listGist" :
					return this.listGist(args)
					break
				case "reverseLookupRaw" :
					return this.reverseLookupRaw(args)
					break
				case "reverseLookup" :
					return this.reverseLookup(args)
					break
				default :
				// just do what ObejctRepository is designed to do
					def result
					try {
						result = delegate.metaClass.getMetaMethod(name, args).invoke(delegate, args)
					} catch (Exception e) {
						System.err.println("call to method $name raised an Exception")
						e.printStackTrace()
					}
					return result
			}
		}
	}

	List<String> listRaw(Object ... args) throws Exception {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.listRaw("", false)
		} else if (args.length == 1) {
			return exor.listRaw((String)args[0], false)
		} else {
			return exor.listRaw((String)args[0], (Boolean)args[1])
		}
	}

	String list(Object ... args) throws Exception {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.list("", false)
		} else if (args.length == 1) {
			return exor.list((String)args[0], false)
		} else {
			return exor.list((String)args[0], (Boolean)args[1])
		}
	}

	List<TestObjectGist> listGistRaw(Object ... args) throws Exception {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.listGistRaw("", false)
		} else if (args.length == 1) {
			return exor.listGistRaw((String)args[0], false)
		} else {
			return exor.listGistRaw((String)args[0], (Boolean)args[1])
		}
	}

	String listGist(Object ... args) throws Exception {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.listGist("", false)
		} else if (args.length == 1) {
			return exor.listGist((String)args[0], false)
		} else {
			return exor.listGist((String)args[0], (Boolean)args[1])
		}
	}

	Map<Locator, Set<TestObjectGist>> reverseLookupRaw(Object ... args) throws IOException {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.reverseLookupRaw("", false)
		} else if (args.length == 1) {
			return exor.reverseLookupRaw((String)args[0], false)
		} else {
			return exor.reverseLookupRaw((String)args[0], (Boolean)args[1])
		}
	}

	String reverseLookup(Object ... args) throws IOException {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.reverseLookup("", false)
		} else if (args.length == 1) {
			return exor.reverseLookup((String)args[0], false)
		} else {
			return exor.reverseLookup((String)args[0], (Boolean)args[1])
		}
	}

}