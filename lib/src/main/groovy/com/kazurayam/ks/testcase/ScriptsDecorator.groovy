package com.kazurayam.ks.testcase

import com.kazurayam.ant.DirectoryScanner
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * ScriptsDecorator wraps ScriptsAccessor.
 * ScriptsDecorator accepts patterns that match "sub-folder" in the "Test Cases" folder.
 * ScriptsDecorator translates the pattern into new pattern struing that matches
 * .groovy files in the "Scripts" folder.
 */
class ScriptsDecorator {

	private static Logger logger = LoggerFactory.getLogger(ScriptsDecorator.class)

	private Path scriptsDir
	private List<String> includeFolderSpecification
	private ScriptsAccessor accessor

	private ScriptsDecorator(Builder builder) {
		scriptsDir = builder.scriptsDir
		includeFolderSpecification = builder.includeFolder
		Objects.requireNonNull(scriptsDir)
		Objects.requireNonNull(includeFolderSpecification)
		init()
	}

	private init() {
		// the following line is the whole reason why we need this class "ScriptsAdapter"
		List<String> patterns = translatePatterns(includeFolderSpecification)
		accessor = new ScriptsAccessor.Builder(scriptsDir)
				.includeFiles(patterns).build()
	}

	/**
	 * convert a patten for Scripts sub-folders to a pattern for Groovy files
	 * E.g, "** /ObjectRepositoryGarbageCollector" -> 
	 * 			"** /ObjectRepositoryGarbageCollector/** /*.groovy"
	 */
	protected List<String> translatePatterns(List<String> patternsForFolder) {
		List<String> patternsForFile = new ArrayList()
		patternsForFolder.each { ptrn ->
			StringBuilder sb = new StringBuilder()
			sb.append(ptrn)
			if (!ptrn.endsWith("/")) {
				sb.append("/")
			}
			sb.append("**/*.groovy")
			patternsForFile.add(sb.toString())
		}
		return patternsForFile
	}

	Path getScriptsDir() {
		return scriptsDir
	}

	List<String> getIncludeFolderSpecification() {
		return includeFolderSpecification
	}

	List<Path> getGroovyFiles() {
		return accessor.getGroovyFiles()
	}


	static class Builder {
		private Path scriptsDir
		private List<String> includeFolder
		Builder() {
			this(Paths.get(".").resolve("Scripts"))
		}
		Builder(Path dir) {
			Objects.requireNonNull(dir)
			assert Files.exists(dir)
			scriptsDir = dir
			includeFolder = new ArrayList<>()
		}
		Builder includeFolder(String pattern) {
			includeFolder.add(pattern)
			return this
		}
		Builder includeFolder(List<String> pattern) {
			includeFolder.addAll(pattern)
			return this
		}
		ScriptsDecorator build() {
			assert scriptsDir != null : "scriptsDir is left null"
			return new ScriptsDecorator(this)
		}
	}
    /**
     * The ScriptsAccessor.Builder requires the path of `Scripts` directory of
     * a Katalon project.
     * Optionally accepts the Ant-like patterns to specify the sub-folders
     * from which .groovy files under the `Scripts` dir are selected.
     * `getGroovyFiles()` method returns a list of Path of .groovy files that
     * are included by the pattern given.
     */
    static class ScriptsAccessor {

        private static Logger logger = LoggerFactory.getLogger(ScriptsAccessor.class)

        private Path scriptsDir
        private List<String> includeFilesSpecification
        private DirectoryScanner ds

        private ScriptsAccessor(Builder builder) {
            scriptsDir = builder.scriptsDir
            includeFilesSpecification = builder.includeFiles
            init()
        }

        private void init() {
            ds = new DirectoryScanner()
            ds.setBasedir(scriptsDir.toFile())
            if (includeFilesSpecification.size() > 0) {
                String[] includes = includeFilesSpecification.toArray(new String[0])
                ds.setIncludes(includes)
            }
            ds.scan()
        }

        String[] includedFiles() {
            return ds.getIncludedFiles()
        }

        List<Path> getGroovyFiles() {
            String[] includedFiles = ds.getIncludedFiles()
            List<Path> groovyFiles = new ArrayList<>()
            for (int i = 0; i < includedFiles.length; i++) {
                if (includedFiles[i].endsWith(".groovy")) {
                    Path p = scriptsDir.resolve(includedFiles[i])
                            .toAbsolutePath().normalize()
                    groovyFiles.add(p)
                } else {
                    logger.warnEnabled("found a file that does not end with '.groovy'; ${includedFiles[i]}")
                }
            }
            return groovyFiles
        }



        /**
         *
         * @author kazurayam
         */
        static class Builder {
            private Path scriptsDir
            private List<String> includeFiles
            Builder(Path scriptsDir) {
                Objects.requireNonNull(scriptsDir)
                assert Files.exists(scriptsDir)
                this.scriptsDir = scriptsDir.toAbsolutePath().normalize()
                includeFiles = new ArrayList<>()
            }

            ScriptsAccessor.Builder includeFile(String pattern) {
                Objects.requireNonNull(pattern)
                includeFiles.add(pattern)
                return this
            }

            ScriptsAccessor.Builder includeFiles(List<String> patterns) {
                Objects.requireNonNull(patterns)
                includeFiles.addAll(patterns)
                return this
            }
            ScriptsAccessor build() {
                return new ScriptsAccessor(this)
            }
        }
    }
}
