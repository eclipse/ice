# ******************************************************************************
# Copyright (c) 2015 UT-Battelle, LLC.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#   Andrew Bennett - Initial API and implementation and/or initial documentation
#
# ******************************************************************************
# The Python ICE Installer
# ******************************************************************************

import sys
import argparse
import tarfile
import zipfile
import urllib2
import platform

def parse_args(args):
    """ Parse command line arguements and return them. """
    parser = argparse.ArgumentParser(description="ICE Installer script.",
                formatter_class=argparse.ArgumentDefaultsHelpFormatter,
                fromfile_prefix_chars='@')

    parser.add_argument('-u', '--update', nargs='*', default=['all'],
            choices=("all", "none", "VisIt", "HDFJava", "ICE"),
            help='The packages to update.  Leave blank to update all available packages.')
    parser.add_argument('-p', '--prefix', default="",
            help="The location to download and install ICE.")

    opts = parser.parse_args(args)
    
    # If update option was given blank set it to update everything
    if opts.update == [] or 'all' in opts.update:
        opts.update = ['ICE', 'VisIt', 'HDFJava']

    return opts


def download_packages(opts, os_type, arch_type):
    """
    Pull down the appropriate packages for the given run and OS type
    
    Args:
        opts: the list of options selected
        os_type: the operating system to download for
        arch_type: the architecture of the system
    """
    packages = opts.update
    if packages == [] or os_type == None or arch_type == None:
        return

    package_urls = {"ICE"     : "http://sourceforge.net/projects/niceproject/files/nightly/nice/",
                    "VisIt"   : "http://portal.nersc.gov/project/visit/releases/2.9.1/",
                    "HDFJava" : "http://www.hdfgroup.org/ftp/HDF5/hdf-java/current/bin/"}

    package_files = {"ICE"     : {"Windows" : {"x86_64" : "ice.product-win32.win32.x86_64.zip"     ,
                                               "x86"    : "ice.product-win32.win32.x86.zip"        },
                                  "Darwin"  : {"x86_64" : "ice.product-macosx.cocoa.x86_64.zip"    ,
                                               "x86"    : "ice.product-macosx.cocoa.x86.zip"       },
                                  "Linux"   : {"x86_64" : "ice.product-linux.gtk.x86_64.zip"       ,
                                               "x86"    : "ice.product-linux.gtk.x86.zip"          }},
                     "VisIt"   : {"Windows" : {"x86_64" : "visit2.9.1_x64.zip"                     ,
                                               "x86"    : "visit2.9.1.zip"                         },
                                  "Darwin"  : {"x86_64" : "VisIt-2.9.1.dmg"                        },
                                  "Linux"   : {"x86_64" : "visit2_9_1.linux-x86_64-rhel6.tar.gz"   }},
                     "HDFJava" : {"Windows" : {"x86_64" : "HDFView-2.11-win64-vs2012.zip"          ,
                                               "x86"    : "HDFView-2.11-win32-vs2012.zip"          },
                                  "Darwin"  : {"x86_64" : "HDFView-2.11.0.dmg"                     },
                                  "Linux"   : {"x86_64" : "HDFView-2.11-centos6-x64.tar.gz"        }}}
    files = []
    for pkg in packages:
        print "Downloading " + pkg + ":",
        fname = package_files[pkg][os_type][arch_type]
        url = package_urls[pkg] + fname
        u = urllib2.urlopen(url)
        f = open(fname, 'wb')
        fsize = int(u.info().getheaders("Content-length")[0])
        dl_size = 0
        block = 8192
        while True:
            buffer = u.read(block)
            if not buffer: break
            dl_size += len(buffer)
            f.write(buffer)
            status = r"%5.2f%% complete" % (dl_size * 100. / fsize)
            status = status + chr(8)*(len(status)+1)
            print status,
        print ""
    

def unpack_packages(opts):
    pass


def install_packages(opts):
    pass


def main():
    """ Run the full installer. """
    opts = parse_args(sys.argv[1:])
    arch_type = platform.machine()
    os_type = platform.system()

    download_packages(opts, os_type, arch_type)


    
if __name__ == '__main__':
    main()

