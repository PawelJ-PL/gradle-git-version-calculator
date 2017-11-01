# Gradle Git Version Calculator plugin

## Description
This plugin provides method to generate semantic version based on git tags. Base tag must be valid [semantic version](http://semver.org/), but it can be prefixed.
If prefix is defined, only tags with the same prefix will be taken into account and prefix will be removed in final version.

### Version algorithm

1) Get latest tag (if prefix is defined, get latest tag matching to it)
2) If prefix is defined, delete prefix from result
3) Check whether result is valid semantic version (if no, throw exception)
4) If commit with tag isn't HEAD, add number of commits between tagged commit and HEAD to build metadata part
5) If repository is not clean, add dev suffix to build metadata part

**Example:**

repository:
```
some no commited changes
abc
cde [tag: 2.1.1]
efg [tag: xxx__5.2.1]
ghi
ijk
klm [tag: yyy__1.2.5]
```

if prefix is not set, result version will be `2.1.1+1.dev`

if prefix is set to *xxx__*, result version will be `5.2.1+2.dev`

if prefix is set to *yyy__*, result version will be `1.2.5+5.dev`

if prefix is set to *zzz__* (no tag matches) result version will be default `0.0.1-SNAPSHOT`

## Requirements
Because this plugin uses git commands, the Git must be installed and must be available as git from command line (in further version, path to git executable probably will be configurable).

## Usage
Add plugin to Your buildscript:
```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.github.gradle-git-version-calculator:gradle-git-version-calculator:0.1.0"
  }
}
```

Apply plugin:
```groovy
apply plugin: "com.github.gradle-git-version-calculator"
```

And now, You can use it, for example:

```groovy
println(gitVersionCalculator.calculateVersion())
```

or


```groovy
version gitVersionCalculator.calculateVersion("prefix")
```

## Configuration
You can configure prefix directly in Gradle script:

```groovy
gitVersionCalculator {
    prefix = "prefix"
}
```
Prefix provided as method argument has higher priority than configured in script.

## Task
This plugin add `printVersion` task, which prints on stdout version, that will be applied (it can be useful during debugging).
If You want to use prefix in this task, You should configure it as described in [Configuration](#configuration) section
