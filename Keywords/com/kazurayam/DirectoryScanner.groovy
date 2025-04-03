package com.kazurayam

/**
 * Homage to the marvelous DirectoryScanner of Ant 
 * https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
 * 
 * @author kazurayam
 *
 */
public class DirectoryScanner implements FileScanner {

	/**
	 * Adds default exclusion to the current exclusions set
	 */
	void addDefaultExcludes()
	
	/**
	 * Return the base directory to be scanned
	 * @return
	 */
	Path getBasedir()
	
	/**
	 * Returns the relative path of the directories which matched at least one of the invcluded patterns
	 * and at least one of the exclude patterns.
	 * The returned Path is relative to the base directory.
	 */
	List<Path> getExcludedDirectories()

	/**
	 * Returns the relative path of the files which matched at least one of the invcluded patterns
	 * and at least one of the exclude patterns.
	 * The returned Path is relative to the base directory.
	 */
	List<Path> getExcludedFiles()
	
	
	List<Path> getIncludedDirectories()
	
	List<Path> getIncludedFiles()
	
}
