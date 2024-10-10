# install_apps.py

This script is used to update / reset Clover apps on an Android emulator. It works by looking at the device's local database to understand the set of apps that should be installed and comparing this to the set of installed apps, then downloading, and sideloading apps that are not at the correct version.

The main use of this script is to initially install, and keep Clover apps updated on Android emulators. If used with the `--downgrade` option (see below) it can additional reset apps to the current server version if higher test versions are installed.

Run this script on a new emulator setup after you have added the Clover account and logged in. Give the emulator a minute or so to sync the set of current apps.

### Requirements
You must have `adb` in your path. Your emulator must allow root. You can test this by running `adb root`.
```
$ adb root
adbd is running as root
$ adb shell
generic_x86:/ # id
uid=0(root) gid=0(root) groups=0(root)...
```

## Usage
```
$ ./install_apps.py -h
Usage: install_apps.py [--debug] [--version] [--dry_run] [--keep] [--help] [--downgrade] [--serial SERIAL]
  --debug or -d               : print extended debug information
  --version or -v             : print version (and exit)
  --dry-run or -r             : show actions but do not perform them (do not download or install APKs
  --keep or -k                : keep temporary files (temp files are located under: /tmp)
  --downgrade or -o           : only downgrade, if current version is less than installed version; do not update
  --serial SERIAL or -s SERIAL: use device with given serial (overrides $ANDROID_SERIAL)
  --help or -h                : show this message (and exit)
```

### --version or -v
Print the version of the script.

### --dry-run or -r
Show all actions that would be taken but do not perform them; do not download APKs and do not install them. If used in conjunction with `--keep` APKs will be downloaded and kept (but not installed).
```
$ install_apps.py -r
Getting installed versions...
Getting current app data...
Updating package: com.clover.remote.protocol.local from version: 5153, to version: 5154...
adb install -r -d /tmp/com.clover.remote.protocol.local-5154.apk
Updating package: com.clover.auths from version: 3731, to version: 3732...
adb install -r -d /tmp/com.clover.auths-3732.apk
Updating package: com.clover.ebt from version: 3365, to version: 3366...
adb install -r -d /tmp/com.clover.ebt-3366.apk
Updating package: com.clover.tabs from version: 3731, to version: 3732...
adb install -r -d /tmp/com.clover.tabs-3732.apk
...
``` 

### --keep or -k
Run normally, but do not delete temporary files. This includes downloaded APK files and the app info DB pulled from the device. The location of the kept files is printed.
```
$ install_apps.py -k
Getting installed versions...
Getting current app data...
Updating package: com.clover.remote.protocol.local from version: 5153, to version: 5154...
APK file kept at: /tmp/com.clover.remote.protocol.local-5154.apk
Updating package: com.clover.auths from version: 3731, to version: 3732...
... 
``` 

### --downgrade or -o
Only downgrade APK versions. If an installed app's version is greater than the current server version, downgrade the app to the current version. In absence of this option app are upgraded (only).
```
$ install_apps.py -r -o
Getting installed versions...
Getting current app data...
Downgrading package: com.clover.engine from version: 32000, to version: 6528...
adb install -r -d /tmp/com.clover.engine-6528.apk
...
```

### --debug or -d
Print extended debug information. This output is extremely verbose and is probably not useful unless you are experiencing problems with the script.

### --serial SERIAL or -s SERIAL
Use the Android device with the given serial. Useful when more than one device is connected. You can list all of your connected devices with the command `$ adb devices`.

It is also possible to set the serial by setting the environment variable `ANDROID_SERIAL`. Note that the `-s` option overrides `$ANDROID_SERIAL`.

### --help or -h
Print help / usage.
