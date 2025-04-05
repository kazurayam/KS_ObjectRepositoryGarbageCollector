# [Katalon Studio] Object Repository Garbage Collector

This project provides a Groovy class `com.kazurayam.ks.testobject.ObjectRepositoryGarbageCollector` class
in a jar file. This class is useful to find out unused TestObjects in the "Object Repository".

## A simple demo

I made a Test Case script named "GC":

```
import com.kazurayam.ks.testobject.gc.ObjectRepositoryGarbageCollector

import groovy.json.JsonOutput

/**
 * A demonstration of ObjectRepositoryGarbageCollector.
 *
 * This TestCase outputs a JSON file which contains a list of garbage Test Objects
 * in the "Object Repository" folder.
 *
 * A "garbage" means a Test Object which is not used by any scripts
 * in the "Test Cases" folder.
 */

// the Garbage Collector instance will scan 2 folders: "Object Repository" and "Test Cases"

ObjectRepositoryGarbageCollector gc =
        new ObjectRepositoryGarbageCollector.Builder()
            .includeFolder("**/Page_CURA*")
            .build()

// gc.jsonifyGarbages() triggers scanning through the 2 folders and analyze the files.
// All forward references from TestCase scripts to TestObject entities are identified.
// Consequently, it can result a list of unused TestObjects.
// Will output the result in a JSON string

String json = gc.jsonifyGarbages()

println JsonOutput.prettyPrint(json)
```

When I ran this, I got the following output in the console:

```
{
    "Project name": "KS_ObjectRepositoryGarbageCollector",
    "Number of TestCases": 37,
    "Number of TestObjects": 15,
    "Number of unused TestObjects": 4,
    "Unused TestObjects": [
        "Page_CURA Healthcare Service/a_Foo",
        "Page_CURA Healthcare Service/td_28",
        "Page_CURA Healthcare Service2/a_Go to Homepage",
        "Page_CURA Healthcare Service2/td_28"
    ],
    "Duration seconds": 2.956
}
```

I found that this project contains

- 37 TestCase scripts
- 15 TestObjects
- out of 15, 4 TestObjects are unused by any of TestCase scripts.

It took me approximately 3 seconds to get the result.

I hope that it would take just a few minutes to scan through 3000 TestObjects. I expect, it will tell you over the half TestObjects are unused garbages.

## How to install the library.

1. Visit the [KS_ObjectRepositoryGargabeCollector, Releases](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/releases) page. Identify the latest version. Find a `KS_ObjectRepositoryGarbageCollector-x.x.x.jar` file attached. Download the jar file, save it into the `Drivers` folder of your Katalon project.
2. Visit the [MonkDirectoryScanner, Releases](https://github.com/kazurayam/MonkDirectoryScanner/releases/tag/0.1.0). Identify the latest version. Find a `MondDirectoryScanner-x.x.x.jar` file attached. Download the jar file, save into the `Drivers` folder of your katalon project.
3. Close and reopen the project. Confirm that the jars are recognized by Katalon Studio.
4. Create a Test Case script, which should be similar to the above "GC" script.
5. You are done. Run it and see how quickly you can get the result.

## Dependencies, versions, etc

You need to add the aforementioned jars developed by kazurayam.
This library depends on a few external libraries which is bundled in Katalon Studio, e.g, FastXML Jackson Databind.
This library should run on any version of Katalon Studio and Katalon Runtime Engine.

## More features?

This library supports more:

1. You can drill-down to the sub-folders of "Object Repository" to select targets from a smaller set of Test Objects. By this, you can get more concise/focused report.
2. It can report all *Forward Reference*s from TestCase scripts to TestObjects.
3. It can report all *Backward Reference*s, which is a list of TestObjects associated with list of ForwardReferences to each TestObject.
4. It can report all *Locator*s (XPath, CSS Selector) associated with list of duplicating TestObjects that implement the same locator.

I will write a more details documentation with sample codes later.

## Disclaimer

I hope the library reports correctly. But I would not be responsible for the damages when you manually clear away what it found as “garbages”. I would recommend you to set your project backed by Git, and to store the snapshots before cleaning.
