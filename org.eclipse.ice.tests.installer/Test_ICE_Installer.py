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
# Test file for the Python ICE Installer
# ******************************************************************************

import os
import sys
import glob
import platform
import unittest

sys.path.append(os.path.dirname(os.path.realpath(__file__)) + \
                os.sep +os.path.pardir + os.sep + "org.eclipse.ice.installer")

import Install_ICE

class TestICEInstaller(unittest.TestCase):
    """
    Tests for the ICE Installer are stored here.  Each test should be a
    separate function within this class.
    """

    def setUp(self):
        pass

    def test_options(self):
        """ Tests Install_ICE.parse_args """        
        print("")
        print("----------------------------------------------------------------------")
        print("Testing argument parsing...")
        opts = Install_ICE.parse_args(['-u'])
        self.assertEqual(opts.update, ['ICE', 'VisIt', 'HDFJava'])

        opts = Install_ICE.parse_args(['-u', 'VisIt'])
        self.assertEqual(opts.update, ['VisIt'])

        opts = Install_ICE.parse_args(['-u', 'HDFJava', 'ICE'])
        self.assertEqual(opts.update, ['HDFJava', 'ICE'])
        self.assertEqual(opts.prefix, '.')

        opts = Install_ICE.parse_args(['-p', '/home/user/ICE', '-u'])
        self.assertEqual(opts.update, ['ICE', 'VisIt', 'HDFJava'])
        self.assertEqual(opts.prefix, '/home/user/ICE')

    def test_download(self):
        """ Tests Install_ICE.download_packages """
        print("")
        print("----------------------------------------------------------------------")
        print("  Testing package downloads...")
        opts = Install_ICE.parse_args(['-u', 'HDFJava'])
        arch_type = platform.machine()
        os_type = platform.system()

        Install_ICE.download_packages(opts, os_type, arch_type)
        n = len(glob.glob("HDFView*"))
        [os.remove(f) for f in glob.glob("HDFView*")]
        self.assertEqual(n, 1)


    def test_unpack(self):
        """ Tests Install_ICE.unpack_packages """
        print("")
        print("----------------------------------------------------------------------")
        print("  Testing package downloads...")
        opts = Install_ICE.parse_args(['-u', 'HDFJava'])
        arch_type = platform.machine()
        os_type = platform.system()
        
        pkgs = Install_ICE.download_packages(opts, os_type, arch_type)
        Install_ICE.unpack_packages(opts, pkgs)
        n = len(glob.glob("HDFView*"))
        print("Number of files found = " + str(n))
        self.assertNotEqual(0, n)
        

    def test_install(self):
        pass

    def test_cleanup(self):
        pass


if __name__ == '__main__':
    unittest.main()

