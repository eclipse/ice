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

import argparse

def parse_args():
    """ Parse command line arguements and return them. """
    parser = argparse.ArgumentParser(description="ICE Installer script.",
                formatter_class=argparse.ArgumentDefaultsHelpFormatter,
                from_file_prefix_chars='@')
    
    opts = parser.parse_args()
    return opts

def main():
    """ Run the full installer. """
    opts = parse_args()

    
if __name__ == '__main__':
    main()
