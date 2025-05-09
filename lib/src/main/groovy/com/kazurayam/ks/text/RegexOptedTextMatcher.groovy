package com.kazurayam.ks.text

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * implements a pattern matching against text, returns a boolean result.
 * the pattern can be either of plain text, for a regular expression
 */
class RegexOptedTextMatcher {

	private String pattern
	private Pattern ptrn = null
	private Boolean isRegex

	RegexOptedTextMatcher(String pattern = "", Boolean isRegex = false) throws IOException {
		Objects.requireNonNull(pattern)
		this.pattern = pattern
		this.isRegex = isRegex
		if (isRegex) {
			ptrn = Pattern.compile(pattern)
		}
	}

	Boolean found(String text) {
		Objects.requireNonNull(text)
		if (pattern.length() > 0) {
			if (isRegex) {
				Matcher m = ptrn.matcher(text)
				return m.find()    // will try partial match, not entire match
			} else {
				return text.contains(pattern)
			}
		} else {
			return true
		}
	}
}
