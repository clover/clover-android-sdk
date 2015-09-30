#!/usr/bin/env python
import json
import urllib
import urllib2
import os
import sys

if __name__ == "__main__":

    # Allow the user to pass a device ID as the first and only param to this script
    if (len(sys.argv) > 1):
        device_name = '-s ' + sys.argv[1]
        print 'Targeting device ' + sys.argv[1]
    else:
        device_name = ''

    if not os.path.exists("/tmp"):
        os.makedirs("/tmp")

    print 'Getting app list', '...'
    request = urllib2.urlopen('https://www.clover.com/v3/apps?expand=androidVersion')
    json_data = json.loads(request.read())
    
    apps = json_data['elements']
    for app in apps:
        if app['developer']['id'] != 'CLOVERDEV':
            continue

        package_name = app['packageName']
        versionCode = str(app['androidVersion']['version'])
        apkUrl = app['androidVersion']['apkUrl']

        apkPath = '/tmp/' + package_name + '-' + versionCode + '.apk'

        if os.path.isfile(apkPath):
            print package_name + ' version #' + versionCode + ' has already been downloaded'
        else:
            print 'Downloading', package_name , '...'
            urllib.urlretrieve(apkUrl, filename=apkPath)

        print 'Installing ' + package_name + ' version #' + versionCode + '...'
        os.system('adb ' +  device_name + ' install -r ' + apkPath)

    print 'Add a clover account'
    os.system('adb ' + device_name + ' shell am start -a android.settings.ADD_ACCOUNT_SETTINGS')

    print 'You can then start register from the standard application launcher'