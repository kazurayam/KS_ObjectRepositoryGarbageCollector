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
				case "getTestObjectIdList" :
					return this.getTestObjectIdList(args)
					break
				case "getAllTestObjectIdSet" :
					return this.getAllTestObjectIdSet(args)
					break
				case "jsonifyTestObjectIdList" :
					return this.jsonifyTestObjectIdList(args)
					break
				case "getTestObjectEssenceList" :
					return this.getTestObjectEssenceList(args)
					break
				case "jsonifyTestObjectEssenceList" :
					return this.jsonifyTestObjectEssenceList(args)
					break
				case "getBackwardReferences" :
					return this.getBackwardReferences(args)
					break
				case "jsonifyBackwardReferences" :
					return this.jsonifyBackwardReferences(args)
					break
				default :
				// just do what ObejctRepository is originally designed to do
					def result
					try {
						MetaMethod metaMethod = delegate.metaClass.getMetaMethod(name, args)
						assert metaMethod != null, "method '${name}' is not implemented by ObjectRepository class"
						result = metaMethod.invoke(delegate, args)
					} catch (Exception e) {
						System.err.println("call to method $name raised an Exception")
						e.printStackTrace()
					}
					return result
			}
		}
	}

	private List<TestObjectId> getTestObjectIdList(Object ... args) throws Exception {
		if (args.length == 0) {
			return extOR.getTestObjectIdList("", false)
		} else if (args.length == 1) {
			return extOR.getTestObjectIdList((String)args[0], false)
		} else {
			return extOR.getTestObjectIdList((String)args[0], (Boolean)args[1])
		}
	}

	private Set<TestObject> getAllTestObjectIdSet(Object ... args) throws Exception {
		return extOR.getAllTestObjectIdSet()
	}

	private String jsonifyTestObjectIdList(Object ... args) throws Exception {
		if (args.length == 0) {
			return extOR.jsonifyTestObjectIdList("", false)
		} else if (args.length == 1) {
			return extOR.jsonifyTestObjectIdList((String)args[0], false)
		} else {
			return extOR.jsonifyTestObjectIdList((String)args[0], (Boolean)args[1])
		}
	}

	//-----------------------------------------------------------------

	private List<TestObjectEssence> getTestObjectEssenceList(Object ... args) throws Exception {
		if (args.length == 0) {
			return extOR.getTestObjectEssenceList("", false)
		} else if (args.length == 1) {
			return extOR.getTestObjectEssenceList((String)args[0], false)
		} else {
			return extOR.getTestObjectEssenceList((String)args[0], (Boolean)args[1])
		}
	}

	private String jsonifyTestObjectEssenceList(Object ... args) throws Exception {
		if (args.length == 0) {
			return extOR.jsonifyTestObjectEssenceList("", false)
		} else if (args.length == 1) {
			return extOR.jsonifyTestObjectEssenceList((String)args[0], false)
		} else {
			return extOR.jsonifyTestObjectEssenceList((String)args[0], (Boolean)args[1])
		}
	}

	//-----------------------------------------------------------------

	private Map<Locator, Set<TestObjectEssence>> getBackwardReferences(Object ... args) throws IOException {
		if (args.length == 0) {
			return extOR.getBackwardReferences("", false)
		} else if (args.length == 1) {
			return extOR.getBackwardReferences((String)args[0], false)
		} else {
			return extOR.getBackwardReferences((String)args[0], (Boolean)args[1])
		}
	}

	private String jsonifyBackwardReferences(Object ... args) throws IOException {
		if (args.length == 0) {
			return extOR.jsonifyBackwardReferences("", false)
		} else if (args.length == 1) {
			return extOR.jsonifyBackwardReferences((String)args[0], false)
		} else {
			return extOR.jsonifyBackwardReferences((String)args[0], (Boolean)args[1])
		}
	}
}