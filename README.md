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
.../clover-android-sdk
$ gradle clean dokkaHtmlMultiModule
```
Docs can be found in `build/dokka/htmlMultiModule`.

Latest Apps for Testing
=======================
If you are testing on your own emulator or tablet then you will need the latest apps. All apps can be downloaded at:

```
{baseURL}/developers/dev-apks
```
Ex: <https://www.clover.com/developers/dev-apks>  
Ex: <https://sandbox.dev.clover.com/developers/dev-apks>

If you are developing on sandbox please use the minimum or greater version of the engine.  
Minimum required version: com.clover.engine-1851.apk

To check the current version of the Clover engine on your Clover Devkit:  
Navigate to Settings > Storage > Apps > Clover  
Then up top by the logo it should say the version Ex: ‘version 2.0-1851'  

If you are using the minimum or greater engine version you do not need to do the following steps.
======

Targeting an Emulator or Tablet to a new environment
-----------------------

Currently, some of the dev-apks are not pointing to the environment corresponding to the `baseURL` they were downloaded from (i.e. Sandbox).  

You will need to have installed adb to be able to use the script and it must also be in your path.
Reference: https://developer.android.com/studio/command-line/adb.html

Please run the following bash script on your developer/host machine: [target_new_environment](scripts/target_new_environment)

```
This script is meant to re-target a non-Clover device's environment.

Do not attempt to run this on a Clover device.

Please select a connected device
1) 192.168.57.101:5555
2) C021UQ53170436
3) C010UC43010546
4) C021UQ50430029
#? 1

Selected device:

192.168.57.101:5555

Please select your desired target environment:
1) Prod-US
2) Prod-EU
3) Sandbox
4) Localhost
#? 3

Updating target to https://apisandbox.dev.clover.com/

You will need to 'Add Account' via Settings to set up your device
Success
```
