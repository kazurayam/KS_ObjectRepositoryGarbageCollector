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
		ObjectRepository.metaClass.'static'.invokeMethod = { String name, args ->
			switch (name) {
				case "listTestObjectIdRaw" :
					return this.listTestObjectIdRaw(args)
					break
				case "listTestObjectId" :
					return this.listTestObjectId(args)
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
				// just do what ObejctRepository is originally designed to do
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

	List<String> listTestObjectIdRaw(Object ... args) throws Exception {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.listTestObjectIdRaw("", false)
		} else if (args.length == 1) {
			return exor.listTestObjectIdRaw((String)args[0], false)
		} else {
			return exor.listTestObjectIdRaw((String)args[0], (Boolean)args[1])
		}
	}

	String listTestObjectId(Object ... args) throws Exception {
		ExtendedObjectRepository exor = new ExtendedObjectRepository()
		if (args.length == 0) {
			return exor.listTestObjectId("", false)
		} else if (args.length == 1) {
			return exor.listTestObjectId((String)args[0], false)
		} else {
			return exor.listTestObjectId((String)args[0], (Boolean)args[1])
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