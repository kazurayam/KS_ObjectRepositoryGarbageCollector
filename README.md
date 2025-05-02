# [Katalon Studio] Object Repository Garbage Collector

This project provides a Groovy class `com.kazurayam.ks.testobject.ObjectRepositoryGarbageCollector` class
in a jar file. This class is useful to find out unused TestObjects in the "Object Repository".

## Sample Usages

### List of unused Test Objects, Garbage

I made a Test Case script named "GC":

```
import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector

import groovy.json.JsonOutput

/**
 * A demonstration of ObjectRepositoryGarbageCollector#jsonifyGarbage().
 *
 * This TestCase outputs a JSON file which contains a list of unused Test Objects.
 *
 */

// the Garbage Collector instance will scan 2 folders: "Object Repository" and "Test Cases"

ObjectRepositoryGarbageCollector gc =
        new ObjectRepositoryGarbageCollector.Builder()
            .includeFolder("**/Page_CURA*")
            .build()

// gc.jsonifyGarbage() triggers scanning through the 2 folders and analyze the files.
// All forward references from TestCase scripts to TestObject entities are identified.
// Consequently, it can result a list of unused TestObjects.
// Will output the result in a JSON string

String json = gc.jsonifyGarbage()

println JsonOutput.prettyPrint(json)
```

When I ran this, I got the following output in the console:

```
2025-05-02 20:53:28.048 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyGarbage
{
    "Garbage": [
        "main/Page_CURA Healthcare Service/a_Foo",
        "main/Page_CURA Healthcare Service/td_28",
        "main/Page_CURA Healthcare Service/xtra/a_Go to Homepage",
        "main/Page_CURA Healthcare Service/xtra/td_28",
        "misc/dummy1"
    ],
    "Run Description": {
        "Project name": "katalon",
        "includeScriptsFolder": [
            
        ],
        "includeObjectRepositoryFolder": [
            "**/*"
        ],
        "Number of TestCases": 27,
        "Number of TestObjects": 16,
        "Number of unused TestObjects": 5
    }
}
2025-05-02 20:53:29.897 INFO  c.k.k.c.keyword.builtin.CommentKeyword   - {"started at":"2025-05-02T20:53:29.438","duration seconds":0.258}
```

I found that this project contains

- 27 TestCase scripts
- 16 TestObjects
- out of 16, 5 TestObjects are unused by any of TestCase scripts.
- it took approximately 0.3 seconds to get the result.

Very quick, isn't it?

This library compiles a report that tells where you have garbage to be cleaned, but does NOT remove any files. So this library is not really a garbage collector; it is just an informer.

### List of *Locators* with container Test Objects

Another Test Case script generates a JSON titled "LocatorIndex"

```
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.reporting.Shorthand
import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput
import internal.GlobalVariable

/**
 * ObjectRepositoryGarbageCollector#jsonifyCombinedLocatorIndex() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()

String json = gc.jsonifyCombinedLocatorIndex()

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path classOutputDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector")
Path outDir = classOutputDir.resolve("ORGC_jsonifyCombinedLocatorIndex")
Files.createDirectories(outDir)
File outFile = outDir.resolve("garbage.json").toFile()

outFile.text = JsonOutput.prettyPrint(json)

```

This script produced this (trimmed):

```
{
    "CombinedLocatorIndex": {
        "Number of Locators": 13,
        "Number of Suspicious Locators": 3,
        "Locators": [
            {
                "Locator": {
                    "value": "(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[31]",
                    "selectorMethod": "XPATH"
                },
                "Number of Container TestObjects": 2,
                "Container TestObjects": [
                    {
                        "TestObjectId": "main/Page_CURA Healthcare Service/td_28",
                        "is used": false
                    },
                    {
                        "TestObjectId": "main/Page_CURA Healthcare Service/xtra/td_28",
                        "is used": false
                    }
                ]
            },
            {
                "Locator": {
                    "value": "//a[@id='btn-make-appointment']",
                    "selectorMethod": "XPATH"
                },
                "Number of Container TestObjects": 1,
                "Container TestObjects": [
                    {
                        "TestObjectId": "main/Page_CURA Healthcare Service/a_Make Appointment",
                        "is used": true,
                        "References from TestCase": [
                            {
                                "TestObjectId": "main/Page_CURA Healthcare Service/a_Make Appointment",
                                "Number of ForwardReferences": 2,
                                "ForwardReferences": [
                                    {
                                        "TestCaseId": "main/TC0",
                                        "DigestedLine": {
                                            "line": "WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/a_Make Appointment'))",
                                            "lineNo": 12,
                                            "pattern": "main/Page_CURA Healthcare Service/a_Make Appointment",
                                            "matchAt": 47,
                                            "matchEnd": 99,
                                            "matched": true,
                                            "regex": false
                                        },
                                        "TestObjectId": "main/Page_CURA Healthcare Service/a_Make Appointment"
                                    },
                                    {
                                        "TestCaseId": "main/TC1",
                                        "DigestedLine": {
                                            "line": "WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/a_Make Appointment'))",
                                            "lineNo": 11,
                                            "pattern": "main/Page_CURA Healthcare Service/a_Make Appointment",
                                            "matchAt": 47,
                                            "matchEnd": 99,
                                            "matched": true,
                                            "regex": false
                                        },
                                        "TestObjectId": "main/Page_CURA Healthcare Service/a_Make Appointment"
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
...
```

See the full file at [here](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/blob/develop/docs/assets/testOutput/demo/ObjectRepositoryGarbageCollector/ORGC_jsonifyCombinedLocatorIndex/CombinedLocatorIndex.json)

The CombinedLocatorIndex JSON tells me the following points:

1. this project contains 12 "Locators" declared in the Object Repository.
2. A Locator could be found in one or more Test Objects. If "Number of container TestObjects" has value 2 or more, it means that the Locator is duplicating. You may want to avoid duplication.
3. A TestObject may be used by zero or more Test Cases. If "Number of references from Test Case" of a TestObject is 0, it means the TestObject is unused. You may want to remove the unused TestObject.
4. This JSON could be large. The size depends on the number of TestObjects in your Object Repository. If you have 1000 Test Objects, JSON would be 500M characters or so. Such large JSON won't be useful at all.
5. The ObjectRepositoryGarbageCollector supports `includeObjectRepositoryFolder(pattern)` and `excludeObjectRepositoryFolder(pattern)`. For example:
```
ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()
```
You can specify from which sub-folders of the "Object Repository" to select Test Objects as target. Thus you can make the JSON much smaller and forcused.

### Concise list of *Suspicious Locators*

Final Test Case script generates a JSON titled "Suspicious Locator Index" which would be most useful.

```
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.testobject.combine.ObjectRepositoryGarbageCollector
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonOutput

/**
 * ObjectRepositoryGarbageCollector#jsonifySuspiciousLocatorIndex() demonstration
 */

ObjectRepositoryGarbageCollector gc =
	new ObjectRepositoryGarbageCollector.Builder()
		.includeObjectRepositoryFolder("**/Page_CURA*")
		.build()

String json = gc.jsonifySuspiciousLocatorIndex()

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path classOutputDir = projectDir.resolve("build/tmp/testOutput/demo/ObjectRepositoryGarbageCollector")
Path outDir = classOutputDir.resolve("ORGC_jsonifySuspiciousLocatorIndex")
Files.createDirectories(outDir)
File outFile = outDir.resolve("garbage.json").toFile()

outFile.text = JsonOutput.prettyPrint(json)
```

This script generated a JSON as follows:

```
{
    "SuspiciousLocatorIndex": {
        "Number of Locators": 3,
        "Number of Suspicious Locators": 3,
        "Locators": [
            {
                "Locator": {
                    "value": "(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[31]",
                    "selectorMethod": "XPATH"
                },
                "Number of Container TestObjects": 2,
                "Container TestObjects": [
                    {
                        "TestObjectId": "main/Page_CURA Healthcare Service/td_28",
                        "is used": false
                    },
                    {
                        "TestObjectId": "main/Page_CURA Healthcare Service/xtra/td_28",
                        "is used": false
                    }
                ]
            },
            {
                "Locator": {
                    "value": "//body",
                    "selectorMethod": "XPATH"
                },
                "Number of Container TestObjects": 1,
                "Container TestObjects": [
                    {
                        "TestObjectId": "misc/dummy1",
                        "is used": false
                    }
                ]
            },
            {
                "Locator": {
                    "value": "//section[@id='summary']/div/div/div[7]/p/a",
                    "selectorMethod": "XPATH"
                },
                "Number of Container TestObjects": 3,
                "Container TestObjects": [
                    {
                        "TestObjectId": "main/Page_CURA Healthcare Service/a_Foo",
                        "is used": false
                    },
                    {
                        "TestObjectId": "main/Page_CURA Healthcare Service/a_Go to Homepage",
                        "is used": true,
                        "References from TestCase": [
                            {
                                "TestObjectId": "main/Page_CURA Healthcare Service/a_Go to Homepage",
                                "Number of ForwardReferences": 1,
                                "ForwardReferences": [
                                    {
                                        "TestCaseId": "main/TC1",
                                        "DigestedLine": {
                                            "line": "WebUI.click(findTestObject('Object Repository/main/Page_CURA Healthcare Service/a_Go to Homepage'))",
                                            "lineNo": 36,
                                            "pattern": "main/Page_CURA Healthcare Service/a_Go to Homepage",
                                            "matchAt": 47,
                                            "matchEnd": 97,
                                            "matched": true,
                                            "regex": false
                                        },
                                        "TestObjectId": "main/Page_CURA Healthcare Service/a_Go to Homepage"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "TestObjectId": "main/Page_CURA Healthcare Service/xtra/a_Go to Homepage",
                        "is used": false
                    }
                ]
            }
        ]
    },
    "Run Description": {
        "Project name": "katalon",
        "includeScriptsFolder": [
            "main",
            "misc"
        ],
        "includeObjectRepositoryFolder": [
            "main",
            "misc"
        ],
        "Number of TestCases": 5,
        "Number of TestObjects": 16,
        "Number of unused TestObjects": 5
    }
}
```

Let me explain about this JSON.

1. A Locator is *suspicious* if it is declared by a Test Object which is unused (referred by none of Test Cases)
2. The "SuspiciousLocatorIndex" JSON contains only suspicious Locators. It doesn't contain trustworthy Locators. Therefore the JSON file would be far smaller than the full list of Locators. 
3. As you work on cleaning your Object Repository, the "SuspiciousLocatorIndex" will become smaller. When the index has got 0 entries, you can be sure you have finished.

## How to install the library.

1. Visit the [KS_ObjectRepositoryGargabeCollector, Releases](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/releases) page. Identify the latest version. Find a `ks-object-repository-garbage-collector-x.x.x.jar` file attached. Download the jar file, save it into the `Drivers` folder of your Katalon project.
2. Visit the [MonkDirectoryScanner, Releases](https://github.com/kazurayam/MonkDirectoryScanner/releases/tag/0.1.0). Identify the latest version. Find a `monk-directory-scanner-x.x.x.jar` file attached. Download the jar file, save into the `Drivers` folder of your katalon project.
3. Close and reopen the project. Confirm that the jars are recognized by Katalon Studio.
4. Create a Test Case script, which should be similar to the above "GC" script.
5. You are done. Run it and see how quickly you can get the result.

## Dependencies, versions, etc

You need to add the aforementioned jars developed by kazurayam.
This library depends on a few external libraries which is bundled in Katalon Studio, e.g, FastXML Jackson Databind.

This library should run on Katalon Studio Free, Katalon Studio Enterprise, and Katalon Runtime Engine. The jar is compiled by Katalon Studio Free v10.1.0 with JDK17, so the jar requires KS v10.x or above.

## More features?

I will write more comprehensive doc later.

## Disclaimer

I hope the library reports correctly. But I would not be responsible for the damages when you manually clear away what it found as “garbage”. I would recommend you to set your project backed by Git, and to store the snapshots before cleaning.
