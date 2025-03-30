package com.kazurayam.ks.testcase

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer

import java.nio.file.Path
import java.util.regex.Matcher
import java.util.regex.Pattern
import groovy.json.JsonOutput

public class SearchableText {

	private List<String> lines = new ArrayList<>()

	public SearchableText(String code) {
		Objects.requireNonNull(code)
		this.lines = toLines(code)
	}

	public SearchableText(Path file) throws IOException {
		Objects.requireNonNull(file)
		this.lines = toLines(file.toFile().text)
	}

	public SearchableText(File file) throws IOException {
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

	public List<TextSearchResult> searchText(String pattern = "", Boolean isRegex = false) {
		List<TextSearchResult> result = new ArrayList<>()
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
					TextSearchResult tsr = new TextSearchResult.Builder(line, lineNo)
							.pattern(pattern, isRegex)
							.matchFound(matchAt, matchEnd)
							.build()
					result.add(tsr)
				}
			} else {
				if (line.indexOf(pattern) > 0) {
					int matchAt = line.indexOf(pattern) + 1
					int matchEnd = matchAt +  pattern.length()
					TextSearchResult tsr = new TextSearchResult.Builder(line, lineNo)
							.pattern(pattern, isRegex)
							.matchFound(matchAt, matchEnd)
							.build()
					result.add(tsr)
				}
			}
		}
		return result
	}

	@Override
	public String toString() {
		return toJson()
	}


	public String toJson() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append(JsonOutput.toJson("SearchableText"))
		sb.append(":")
		sb.append("[")
		String sep = ""
		lines.forEach { line ->
			sb.append(sep)
			sb.append(JsonOutput.toJson(line))
			sep = ","
		}
		sb.append("]")
		sb.append("}")
		return JsonOutput.prettyPrint(sb.toString())
	}

	/*
	 static class SearchableTextSerializer extends StdSerializer<SearchableText> {
	 SearchableTextSerializer() {
	 this(null)
	 }
	 SearchableTextSerializer(Class<LocatorSerializer> t) {
	 super(t)
	 }
	 @Override
	 void serialize(SearchableText,
	 JsonGenerator gen, SerializerProvider serializer) {
	 gen.writeStartArray()
	 gen.writeEndArray()
	 }
	 }
	 */
}
