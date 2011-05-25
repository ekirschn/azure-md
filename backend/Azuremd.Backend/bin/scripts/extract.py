#!/usr/bin/env python
import os, sys, subprocess
from subprocess import call

def extract():
	path = arg0 #sys.argv[1] # arg0 # pfad
	
	# 7z magic ...
	call(['7z', 'e', '-y', '-o' + '/'.join(path.split('/')[0:-1]), path])#, stdout=subprocess.PIPE)
	
if __name__ == "__main__":
	extract()