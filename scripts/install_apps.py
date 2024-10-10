#!/usr/bin/env python

from __future__ import print_function

import re
import subprocess
import urllib3
import sys
import ssl
import getopt
import tempfile
import os
import shutil
import shlex

debug = False
dry_run = False
keep_files = False
downgrade = False
serial = None

version = "1.2.1"
# Version information:
#       0.1  : Initial release
#       1.0.1: Update to fix downgrade with no current app
#       1.1  : Require python3, use shutil to resolve adb executable
#       1.2  : Query apps instead of pulling apps database 
#       1.2.1: Add -s argument to specify a device serial
#

def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)

def run_command(command, no_print=False):
    p = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, shell=True, universal_newlines=True)
    lines = []
    while True:
        l = p.stdout.readline()
        if not l and p.poll() is not None: break
        lines.append(l.strip())

    if not no_print and (debug or p.returncode != 0):
        for line in iter(lines): 
            if line: eprint('{} >>> {}'.format(command, line))
    return p.returncode, iter(lines)
    
def print_help():
    eprint('Usage: ' + os.path.basename(__file__)
           + '[--debug] [--version] [--dry_run] [--keep] [--help] [--downgrade] [--serial SERIAL]')
    eprint('  --debug or -d               : print extended debug information')
    eprint('  --version or -v             : print version (and exit)')
    eprint('  --dry-run or -r             : show actions but do not perform them (do not download or install APKs')
    eprint('  --keep or -k                : keep temporary files (temp files are located under: {})'.format(tempfile.gettempdir()))
    eprint('  --downgrade or -o           : only downgrade, if current version is less than installed version; do not update')
    eprint('  --serial SERIAL or -s SERIAL: use device with given serial (overrides $ANDROID_SERIAL)')
    eprint('  --help or -h                : show this message (and exit)')

def run_contains(command, s):
    return_code, lines = run_command(command, no_print=True)
    for line in lines:
        if s in line: return True
    return False

def download(apk_url, apk_file):
    http = urllib3.PoolManager()
    r = http.request('GET', apk_url)
    if (not r.status in range(200, 299)):
        raise Exception("Got non-200 response downloading APK from: {}, status: {}".format(apk_url, r.status))

    output = open(apk_file, 'wb')
    output.write(r.data)
    output.close()

# Query for the set of apps that should be installed to the device.
#
# Returns: a dict of dicts, where the first dict maps a package 
#   name to a dict, and the mapped dict maps app keys to values.
def query_apps(adb):
    return_code, lines = run_command('{} shell content query --uri content://com.clover.apps/apps'.format(adb))
    if return_code != 0:
        eprint("Failed to query apps from device")
        sys.exit(-2)
    apps = {}    
    pattern = re.compile(r'(?P<key>\w+)=(?P<value>[^,]+)(?:,\s*)?')

    for line in lines:
        if line:
            matches = list(pattern.finditer(line))
            if matches:
                app = {}
                for match in pattern.finditer(line): app[match['key']] = match['value']
                if 'package' in app:
                    apps[app['package']] = app
                else:
                    eprint("Failed to parse query result: {}".format(line))
            else:
                eprint("Failed to query apps from device")
                sys.exit(-2)
            
    return apps

def main(argv):
    if sys.version_info < (3,5):
        sys.exit('Python 3.5+ required')

    global debug
    global version
    global dry_run
    global keep_files
    global downgrade
    global serial

    try:
        opts, args = getopt.getopt(
            argv, 'dvrkos:h', ['debug', 'version', 'dry-run', 'keep', 'downgrade', 'serial=', 'help'])
    except getopt.GetoptError:
        print_help()
        sys.exit(2)

    for opt, arg in opts:
        if opt == '--help' or opt == '-h':
            print_help()
            sys.exit(0)
        if opt == '--debug' or opt == '-d':
            debug = True
        if opt == '--dry-run' or opt == '-r':
            dry_run = True
        if opt == '--keep' or opt == '-k':
            keep_files = True
        if opt == '--version' or opt == '-v':
            print(version)
            sys.exit(0)
        if opt == '--downgrade' or opt == '-o':
            downgrade = True
        if opt == '--serial' or opt == '-s':
            serial = arg

    adb = shutil.which('adb')
    if debug: print("adb={}".format(adb))
    if adb is None:
        sys.exit('"adb" command not found. Ensure it is in your path.')
    if serial:
        adb = '{} -s {}'.format(adb, shlex.quote(serial))

    run_command('{} root'.format(adb))

    # We use --no-streaming because adb can hang in streaming mode when the install fails
    # Older adb does not support --no-streaming and will fail if it is passed, so figure out
    # if we need it.
    no_streaming = ''
    if run_contains(adb, "--no-streaming"):
        no_streaming = '--no-streaming'

    installed_versions = {}
    command = '{} shell dumpsys package'.format(adb)
    last_pkg = ''

    print('Getting installed versions...')
    sys.stdout.flush()
    return_code, lines = run_command(command)
    if return_code != 0:
        eprint("Failed to execute command: {}".format(command))
    for line in lines:
        match = re.search(r'Package \[([a-zA-Z0-9\._]+)\]', line)
        if match:
            last_pkg = match.group(1)
            if debug:
                print('Found package: {}'.format(last_pkg))
            continue

        match = re.search(r'versionCode=(\d+)', line)
        if match:
            version_code = match.group(1)
            installed_versions[last_pkg] = version_code
            if debug: print('Put installed version for package: {}, version code: {}'.format(last_pkg, version_code))

    print('Getting current app data...')
    sys.stdout.flush()
    apps = query_apps(adb)
    print('Processing {} installed apps...'.format(len(apps)))

    for app in apps.values():
        pkg = app['package']
        current_version = app['version_code']
        apk_url = app['apk_url']
        installed_version = installed_versions.get(pkg, "?")

        if debug: print('Package: {}, version: {} ({})'.format(pkg, installed_version, current_version))

        if (not downgrade and installed_version == '?') \
            or (not downgrade and int(current_version) > int(installed_version)) \
            or (downgrade and installed_version != '?' and int(current_version) < int(installed_version)):
                if installed_version == '?' or int(current_version) > int(installed_version):
                    print('Updating package: {} from version: {}, to version: {}...'.format(pkg, installed_version, current_version))
                else:
                    print('Downgrading package: {} from version: {}, to version: {}...'.format(pkg, installed_version, current_version))
                sys.stdout.flush()

                if apk_url == None:
                    eprint('URL not found for package: {}, skipping'.format(pkg))
                    continue

                apk_file = "{}/{}-{}.apk".format(tempfile.gettempdir(), pkg, current_version)
                if debug: print('Downloading from URL: {}, to file: {}...'.format(apk_url, apk_file))

                try:
                    if not dry_run or keep_files: download(apk_url, apk_file)

                    adb_install_command = '{} install {} -r -d {}'.format(adb, no_streaming, apk_file)
                    return_code = 0
                    if dry_run: print(adb_install_command)
                    else: return_code, lines = run_command(adb_install_command)
                finally:
                    # If we are explicitly keeping or the command was not successful, keep the APK
                    if keep_files or return_code != 0: print("APK file kept at: {}".format(apk_file))
                    else: os.remove(apk_file)
    
if __name__ == "__main__":
    main(sys.argv[1:])
