#!/usr/bin/env python
import json
import urllib
import urllib2
import os

desired_apps = ['com.clover.engine', 'com.clover.register']
if __name__ == "__main__":
    print 'Getting app list', '...'
    request = urllib2.urlopen('https://api.clover.com/v2/internal/base_android_apps')
    json_data = json.loads(request.read())
    apps = json_data['apps']
    for app in apps:
        package_name = app['packageName']
        if package_name in desired_apps :
            print 'Downloading', package_name , '...'
            urllib.urlretrieve(app['apkUrl'], filename=package_name+'.apk')

    for app in desired_apps:
        print 'Installing ', app , '...'
        os.system('adb install '+app+'.apk')

    print 'Add a clover account'
    os.system('adb shell am start -a android.settings.ADD_ACCOUNT_SETTINGS')

    print 'You can then start register from the standard application launcher'
