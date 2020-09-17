#!/usr/bin/env python

import re
import subprocess
import urllib2
import sys
import ssl
import getopt
import tempfile
import os
import sqlite3

debug = False
dry_run = False
keep_files = False
downgrade = False

version = "1.0.1"
# Version information:
#       0.1: Initial release
#       1.0.1: Update to fix downgrade with no current app
#

def which(program):
    def is_exe(fpath):
        return os.path.isfile(fpath) and os.access(fpath, os.X_OK)

    fpath, fname = os.path.split(program)
    if fpath:
        if is_exe(program):
            return program
    else:
        for path in os.environ["PATH"].split(os.pathsep):
            exe_file = os.path.join(path, program)
            if is_exe(exe_file):
                return exe_file

    return None

def run_command(command, no_print=False):
    p = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, shell=True)
    lines = []
    while True:
        l = p.stdout.readline()
        if not l and p.poll() is not None: break
        lines.append(l.strip())

    if not no_print and (debug or p.returncode != 0):
        for line in iter(lines): print_err('{} >>> {}'.format(command, line))
    return p.returncode, iter(lines)
    
def print_err(s):
    print >>sys.stderr, s

def print_help():
    print_err('Usage: ' + os.path.basename(__file__) + ' [--debug] [--version] [--dry_run] [--keep] [--help] [--downgrade]')
    print_err('\t--debug or -d    : print extended debug information')
    print_err('\t--version or -v  : print version (and exit)')
    print_err('\t--dry-run or -r  : show actions but do not perform them (do not download or install APKs')
    print_err('\t--keep or -k     : keep temporary files (temp files are located under: {})'.format(tempfile.gettempdir()))
    print_err('\t--downgrade or -o: only downgrade, if current version is less than installed version; do not update')
    print_err('\t--help or -h     : show this message (and exit)')

def run_contains(command, s):
    return_code, lines = run_command(command, no_print=True)
    for line in lines:
        if s in line: return True
    return False

def download(apk_url, apk_file):
    if apk_url.lower().startswith("https://") and hasattr(ssl, '_create_unverified_context'):
        context = ssl._create_unverified_context()
        apk_remote_file = urllib2.urlopen(apk_url, context=context)
    else:
        apk_remote_file = urllib2.urlopen(apk_url)

    output = open(apk_file, 'wb')
    output.write(apk_remote_file.read())
    output.close()

def main(argv):
    global debug
    global version
    global dry_run
    global keep_files
    global downgrade

    try:
        opts, args = getopt.getopt(argv, 'dvrkoh', ['debug', 'version', 'dry-run', 'keep', 'downgrade', 'help'])
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
            print version
            sys.exit(0)
        if opt == '--downgrade' or opt == '-o':
            downgrade = True

    adb = which('adb')
    if debug: print "adb={}".format(adb)
    if adb is None:
        print_err('adb command not found. Ensure it is in your path.')
        sys.exit(-2)

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

    print 'Getting installed versions...'
    sys.stdout.flush()
    return_code, lines = run_command(command)
    if return_code != 0:
        print_err("Failed to execute command: {}".format(command))
    for line in lines:
        match = re.search('Package \[([a-zA-Z0-9\._]+)\]', line)
        if match:
            last_pkg = match.group(1)
            if debug:
                print 'Found package: {}'.format(last_pkg)
            continue

        match = re.search('versionCode=(\d+)', line)
        if match:
            version_code = match.group(1)
            installed_versions[last_pkg] = version_code
            if debug: print 'Put installed version for pkg: {}, version code: {}'.format(last_pkg, version_code)

    print 'Getting current app data...'
    sys.stdout.flush()

    appinfo_db_file = '{}/appinfo.db'.format(tempfile.gettempdir())
    return_code, lines = run_command('{} pull /data/data/com.clover.engine/databases/appinfo.db {}'.format(adb, appinfo_db_file))
    if return_code != 0:
        print_err("Failed to pull app info DB from device")
        sys.exit(-2)

    try:
        with sqlite3.connect(appinfo_db_file) as conn:
            for row in conn.execute('select * from apps'):
                pkg = row[3]
                current_version = row[5]
                apk_url = row[7]
                installed_version = installed_versions.get(pkg, "?")

                if debug:
                    print 'Package: {}, version: {} ({})'.format(pkg, installed_version, current_version)

                if (not downgrade and installed_version == '?') \
                        or (not downgrade and int(current_version) > int(installed_version)) \
                        or (downgrade and installed_version != '?' and int(current_version) < int(installed_version)):
                    if installed_version == '?' or int(current_version) > int(installed_version):
                        print 'Updating package: {} from version: {}, to version: {}...'.format(pkg, installed_version, current_version)
                    else:
                        print 'Downgrading package: {} from version: {}, to version: {}...'.format(pkg, installed_version, current_version)
                    sys.stdout.flush()

                    if apk_url == None:
                        print_err('URL not found for package: {}, skipping'.format(pkg))
                        continue

                    apk_file = "{}/{}-{}.apk".format(tempfile.gettempdir(), pkg, current_version)
                    if debug: print 'Downloading from URL: {}, to file: {}...'.format(apk_url, apk_file)

                    try:
                        if not dry_run or keep_files: download(apk_url, apk_file)

                        adb_install_command = '{} install {} -r -d {}'.format(adb, no_streaming, apk_file)
                        return_code = 0
                        if dry_run: print adb_install_command
                        else:
                            return_code, lines = run_command(adb_install_command)
                    finally:
                        # If we are explicitly keeping or the command was not successful, keep the APK
                        if keep_files or return_code != 0: print "APK file kept at: {}".format(apk_file)
                        else: os.remove(apk_file)
    finally:
        if keep_files: print "App info DB file kept at: {}".format(appinfo_db_file)
        else :os.remove(appinfo_db_file)
    
if __name__ == "__main__":
    main(sys.argv[1:])
