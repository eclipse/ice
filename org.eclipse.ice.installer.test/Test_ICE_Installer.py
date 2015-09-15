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
        pass

    def test_download(self):
        pass

    def test_unpack(self):
        pass

    def test_install(self):
        pass

    def test_cleanup(self):
        pass


if __name__ == '__main__':
    unittest.main()
