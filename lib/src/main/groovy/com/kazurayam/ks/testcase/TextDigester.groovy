package com.kazurayam.ks.testcase


import java.nio.file.Path
import java.util.regex.Matcher
import java.util.regex.Pattern

class TextDigester {

	private List<String> lines = new ArrayList<>()

	TextDigester(String code) {
		Objects.requireNonNull(code)
		this.lines = toLines(code)
	}

	TextDigester(Path file) throws IOException {
		Objects.requireNonNull(file)
		this.lines = toLines(file.toFile().text)
	}

	TextDigester(File file) throws IOException {
		Objects.requireNonNull(file)
		this.lines = toLines(file.text)
	}

	private List<String> toLines(String text) {
		BufferedReader br = new BufferedReader(new StringReader(text))
		List<String> lines = new ArrayList<>()
		String line
		while ((line = br.readLine()) != null) {
			lines.add(line)
		}
		return lines
	}

	List<DigestedLine> digestText(String pattern = "", Boolean isRegex = false) {
		List<DigestedLine> result = new ArrayList<>()
		if (pattern.length() == 0) {
			return result    // no search will be performed; return an empty list
		}
		Pattern ptrn = null
		if (isRegex) {
			ptrn = Pattern.compile("(.*)(" + pattern + ")(.*)")
		}
		// iterate over lines to find the matches
		for (int i = 0; i < lines.size(); i++) {
			String line = lines[i]
			int lineNo = i + 1
			// will lookup at most 1 match per line
			// 2nd and later matches, if any, will be ignored --- for easier implementation
			if (isRegex) {
				Matcher m = ptrn.matcher(line)
				if (m.find()) {
					String head = m.group(1)
					String matched = m.group(2)
					int matchAt = head.length() + 1
					int matchEnd = head.length() + matched.length()
					DigestedLine tsr = new DigestedLine.Builder(line, lineNo)
							.pattern(pattern, isRegex)
							.matchFound(matchAt, matchEnd)
							.build()
					result.add(tsr)
				}
			} else {
				if (line.indexOf(pattern) > 0) {
					int matchAt = line.indexOf(pattern) + 1
					int matchEnd = matchAt +  pattern.length()
					DigestedLine tsr = new DigestedLine.Builder(line, lineNo)
							.pattern(pattern, isRegex)
							.matchFound(matchAt, matchEnd)
							.build()
					result.add(tsr)
				}
			}
		}
		return result
	}
}
