Clover SDK for Android
======================

This repo contains two Android Studio projects: clover-android-sdk and clover-android-sdk-examples. Both
projects use the Gradle build system and were developed using Android Studio.
If you do not have Gradle installed, you may use the Gradle wrapper executable ```gradlew``` / ```gradlew.bat```
present in this repo.

clover-android-sdk is an Android library project. To integrate it into your project, add the following dependency to your Gradle-enabled project:

```
dependencies {
    compile 'com.clover.sdk:clover-android-sdk:latest.release'
}
```

clover-android-sdk-examples is an Android application project. To build and install from the command line,

```
$ pwd
.../clover-android-sdk/clover-android-sdk-examples
$ gradle clean installDebug
```

Alternatively, you may import both the SDK and examples as an Android Studio project.

Java Documentation
=======================

You can browse the javadoc for more information, including example usages.

<https://clover.github.io/clover-android-sdk>

To generate your own local copy of the javadoc,

```
$ pwd
.../clover-android-sdk/clover-android-sdk
$ gradle clean assemble doc-clean doc
```

Latest Apps for Testing
=======================

If you are testing on your own tablet then you will need the latest apps. Please run the kickstart.py script to get the minimum apps. All apps can be downloaded at:

<https://www.clover.com/docs/dev-apks>
