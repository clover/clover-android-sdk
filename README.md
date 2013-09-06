This is the Clover SDK for Android.

WARNING: beta release, subject to change

This repo contains two Android Studio projects: clover-android-sdk and clover-android-sdk-examples. Both
projects use the Gradle build system and were developed using Android Studio. You must use Gradle 1.6.
If you do not have Gradle installed, you may use the Gradle wrapper executable ```gradlew``` / ```gradlew.bat```
present in this repo.

clover-android-sdk is an Android library project. To integrate it into your project, build and publish
its artifact to your local Maven repo, 

```
$ pwd
.../clover-android-sdk/clover-android-sdk
$ gradle clean publishToMavenLocal
```

then add it as a dependency in your build.gradle,

```
dependencies {
    compile 'com.clover.sdk:clover-android-sdk:1-SNAPSHOT'
}
```

clover-android-sdk-examples is an Android application project. To build and install from the command line,

```
$ pwd
.../clover-android-sdk/clover-android-sdk-examples
$ gradle clean installDebug
```

Alternatively, you may import both the SDK an examples as an Android Studio project.
