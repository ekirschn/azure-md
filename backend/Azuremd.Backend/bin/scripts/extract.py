#!/usr/bin/env python
import os, sys, subprocess
from subprocess import check_call

def extract(path):
	check_call(['7z', 'e', '-y', '-o' + '/'.join(path.split('/')[0:-1]), path])
	os.unlink(path)
	
if __name__ == "__main__":
	extract(arg0)
	#extract(sys.argv[1])
