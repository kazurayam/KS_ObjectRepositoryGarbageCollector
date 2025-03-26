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
 * injects some useful methods on the fly using Groovy's Meta-programming technique.
 * 
 * @author kazurayam
 */
public class ObjectRepositoryExtender {

	ExtendedObjectRepository extOR

	ObjectRepositoryExtender() {
		this(new ExtendedObjectRepository())
	}

	ObjectRepositoryExtender(ExtendedObjectRepository extOR) {
		Objects.requireNonNull(extOR)
		this.extOR = extOR
	}

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
				case "listEssenceRaw" :
					return this.listEssenceRaw(args)
					break
				case "listEssence" :
					return this.listEssence(args)
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

	private List<String> listTestObjectIdRaw(Object ... args) throws Exception {
		if (args.length == 0) {
			return extOR.listTestObjectIdRaw("", false)
		} else if (args.length == 1) {
			return extOR.listTestObjectIdRaw((String)args[0], false)
		} else {
			return extOR.listTestObjectIdRaw((String)args[0], (Boolean)args[1])
		}
	}

	private String listTestObjectId(Object ... args) throws Exception {
		if (args.length == 0) {
			return extOR.listTestObjectId("", false)
		} else if (args.length == 1) {
			return extOR.listTestObjectId((String)args[0], false)
		} else {
			return extOR.listTestObjectId((String)args[0], (Boolean)args[1])
		}
	}

	private List<TestObjectEssence> listEssenceRaw(Object ... args) throws Exception {
		if (args.length == 0) {
			return extOR.listEssenceRaw("", false)
		} else if (args.length == 1) {
			return extOR.listEssenceRaw((String)args[0], false)
		} else {
			return extOR.listEssenceRaw((String)args[0], (Boolean)args[1])
		}
	}

	private String listEssence(Object ... args) throws Exception {
		if (args.length == 0) {
			return extOR.listEssence("", false)
		} else if (args.length == 1) {
			return extOR.listEssence((String)args[0], false)
		} else {
			return extOR.listEssence((String)args[0], (Boolean)args[1])
		}
	}

	private Map<Locator, Set<TestObjectEssence>> reverseLookupRaw(Object ... args) throws IOException {
		if (args.length == 0) {
			return extOR.reverseLookupRaw("", false)
		} else if (args.length == 1) {
			return extOR.reverseLookupRaw((String)args[0], false)
		} else {
			return extOR.reverseLookupRaw((String)args[0], (Boolean)args[1])
		}
	}

	private String reverseLookup(Object ... args) throws IOException {
		if (args.length == 0) {
			return extOR.reverseLookup("", false)
		} else if (args.length == 1) {
			return extOR.reverseLookup((String)args[0], false)
		} else {
			return extOR.reverseLookup((String)args[0], (Boolean)args[1])
		}
	}
}