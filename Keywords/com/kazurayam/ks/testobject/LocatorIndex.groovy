package com.kazurayam.ks.testobject

import com.kazurayam.ks.testobject.TestObjectEssence
import java.util.Map
import java.util.Set
import groovy.json.JsonOutput

public class LocatorIndex {

	private Map<Locator, Set<TestObjectEssence>> locatorIndex

	LocatorIndex() {
		this.locatorIndex = new TreeMap<>()
	}

	Set<TestObjectEssence> get(Locator locator) {
		Objects.requireNonNull(locator)
		return locatorIndex.get(locator)
	}

	void put(Locator locator, TestObjectEssence essence) {
		Objects.requireNonNull(locator)
		Objects.requireNonNull(essence)
		if (!locatorIndex.containsKey(locator)) {
			Set<TestObjectEssence> emptySet = new TreeSet<>()
			locatorIndex.put(locator, emptySet)
		}
		Set<TestObjectEssence> set = locatorIndex.get(locator)
		set.add(essence)
	}

	public int size() {
		return locatorIndex.size()
	}

	/**
	 * will return a string representation of this object, something like:
	 <code>
	 {
	 "LocatorIndex": [
	 {
	 {
	 "Locator": "(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[31]"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/td_28",
	 "Method": "XPATH",
	 "Locator": "(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[31]"
	 }
	 },
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service2/td_28",
	 "Method": "XPATH",
	 "Locator": "(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[31]"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//a[@id='btn-make-appointment']"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/a_Make Appointment",
	 "Method": "XPATH",
	 "Locator": "//a[@id='btn-make-appointment']"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//button[@id='btn-book-appointment']"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/button_Book Appointment",
	 "Method": "XPATH",
	 "Locator": "//button[@id='btn-book-appointment']"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//button[@id='btn-login']"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/button_Login",
	 "Method": "XPATH",
	 "Locator": "//button[@id='btn-login']"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//input[@id='chk_hospotal_readmission']"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/input_Apply for hospital readmission_hospit_63901f",
	 "Method": "XPATH",
	 "Locator": "//input[@id='chk_hospotal_readmission']"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//input[@id='radio_program_medicaid']"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/input_Medicaid_programs",
	 "Method": "XPATH",
	 "Locator": "//input[@id='radio_program_medicaid']"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//input[@id='txt-password']"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/input_Password_password",
	 "Method": "XPATH",
	 "Locator": "//input[@id='txt-password']"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//input[@id='txt-username']"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/input_Username_username",
	 "Method": "XPATH",
	 "Locator": "//input[@id='txt-username']"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//input[@id='txt_visit_date']"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/input_Visit Date",
	 "Method": "XPATH",
	 "Locator": "//input[@id='txt_visit_date']"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//section[@id='summary']/div/div/div[7]/p/a"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/a_Foo",
	 "Method": "XPATH",
	 "Locator": "//section[@id='summary']/div/div/div[7]/p/a"
	 }
	 },
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/a_Go to Homepage",
	 "Method": "XPATH",
	 "Locator": "//section[@id='summary']/div/div/div[7]/p/a"
	 }
	 },
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service2/a_Go to Homepage",
	 "Method": "XPATH",
	 "Locator": "//section[@id='summary']/div/div/div[7]/p/a"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//select[@id='combo_facility']"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/select_Tokyo CURA Healthcare Center        _5b4107",
	 "Method": "XPATH",
	 "Locator": "//select[@id='combo_facility']"
	 }
	 }
	 ]
	 },
	 {
	 {
	 "Locator": "//textarea[@id='txt_comment']"
	 },
	 [
	 {
	 "TestObjectEssence": {
	 "TestObjectId": "Page_CURA Healthcare Service/textarea_Comment_comment",
	 "Method": "XPATH",
	 "Locator": "//textarea[@id='txt_comment']"
	 }
	 }
	 ]
	 }
	 ]
	 }
	 </code>
	 */
	String toJson() {
		String json = JsonOutput.toJson(locatorIndex)
		return JsonOutput.prettyPrint(json)
	}

	@Override
	String toString() {
		return this.toJson()
	}
}
