package com.kazurayam.ks

import java.util.regex.Matcher
import java.util.regex.Pattern

class BiMatcher {

	private String pattern
	private Pattern ptrn = null
	private Boolean isRegex

	BiMatcher(String pattern = ".*", Boolean isRegex = false) throws IOException {
		Objects.requireNonNull(pattern)
		this.pattern = pattern
		this.isRegex = isRegex
		if (isRegex) {
			ptrn = Pattern.compile(pattern)
		}
	}

	Boolean matches(String text) {
		if (isRegex) {
			Matcher m = ptrn.matcher(text)
			return m.find()    // will try partial match, not entire match
		} else {
			return text.contains(pattern)
		}
	}
}
