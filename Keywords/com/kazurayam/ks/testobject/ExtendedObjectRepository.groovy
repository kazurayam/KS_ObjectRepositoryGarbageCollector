package com.kazurayam.ks.testobject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import groovy.json.JsonOutput

public class ExtendedObjectRepository {

	private static Logger logger = LoggerFactory.getLogger(ExtendedObjectRepository.class)

	private Path baseDir

	ExtendedObjectRepository() {
		this(Paths.get(".").resolve("Object Repository"))
	}

	ExtendedObjectRepository(Path baseDir) {
		Objects.requireNonNull(baseDir)
		assert Files.exists(baseDir)
		this.baseDir = baseDir
	}

	Path getBaseDir() {
		return baseDir
	}

	String list(String pattern, Boolean isRegex) throws IOException {
		List<String> list = listRaw(pattern, isRegex)
		String json = JsonOutput.toJson(list)
		String pp = JsonOutput.prettyPrint(json)
		return pp
	}

	List<String> listRaw(String pattern, Boolean isRegex) throws IOException {
		Path dir = getBaseDir()
		ObjectRepositoryVisitor visitor = new ObjectRepositoryVisitor(dir)
		Files.walkFileTree(dir, visitor)
		List<String> ids = visitor.getTestObjectIDs()
		//
		List<String> result = new ArrayList<>()
		BilingualMatcher bim = new BilingualMatcher(pattern, isRegex)
		ids.forEach { id ->
			logger.debug("pattern=${pattern}, isRegex=${isRegex}, id=${id}, bim.found(id)=${bim.found(id)}")
			if (bim.found(id)) {
				result.add(id)
			}
		}
		return result;
	}

}
