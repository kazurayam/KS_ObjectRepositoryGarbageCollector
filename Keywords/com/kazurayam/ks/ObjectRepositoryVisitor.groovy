package com.kazurayam.ks

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes


/**
 * visits the dir to find out files of which name ends with ".rs".
 * can return the list of rs files found. The returned list will
 * contain relative Path to the dir given. 
 * E.g,
 *     "Page_CURA Healthcare Service/a_Go to Homepage.rs"
 * will not be something like
 *     "/Users/kazurayam/tmp/myproj/Object Repository/Page_CURA Healthcare Service/a_Go to Homepage.rs" 
 */
class ObjectRepositoryVisitor extends SimpleFileVisitor<Path> {

	private Path dir
	private List<Path> rsFiles

	ObjectRepositoryVisitor(Path dir) {
		Objects.requireNonNull(dir)
		assert Files.exists(dir)
		this.dir = dir
		rsFiles = new ArrayList<Path>()
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		if (!Files.isDirectory(file) && file.getFileName().toString().endsWith(".rs")) {
			Path relative = dir.relativize(file).normalize()
			rsFiles.add(relative);
		}
		return FileVisitResult.CONTINUE;
	}

	Path getDir() {
		return dir
	}

	List<Path> getRsFiles() {
		List<Path> result = new ArrayList(rsFiles)
		Collections.sort(result)
		return result
	}
	
	List<String> getTestObjects() {
		List<String> list = new ArrayList<>()
		getRsFiles().forEach({ p ->
			list.add(p.toString().replaceAll("\\.rs", ""))
		})
		return list
	}
}
