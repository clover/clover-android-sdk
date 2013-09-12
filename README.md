Clover SDK for Android
======================

This repo contains two Android Studio projects: clover-android-sdk and clover-android-sdk-examples. Both
projects use the Gradle build system and were developed using Android Studio.
If you do not have Gradle installed, you may use the Gradle wrapper executable ```gradlew``` / ```gradlew.bat```
present in this repo.

clover-android-sdk is an Android library project. To integrate it into your project,
add the following dependency to your Gradle-enabled project:

dependencies {
    compile 'com.clover.sdk:clover-android-sdk:1'
}

clover-android-sdk-examples is an Android application project. To build and install from the command line,

```
$ pwd
.../clover-android-sdk/clover-android-sdk-examples
$ gradle clean installDebug
```

Alternatively, you may import both the SDK and examples as an Android Studio project.

Additional details on how to use in Eclipse forthcoming.
