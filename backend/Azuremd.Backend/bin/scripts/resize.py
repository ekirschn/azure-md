#!/usr/bin/env python
import fileinput, sys

def change_needed(line, key, value):
	if line.startswith("%s = " % key):
		old = line.split("=")[-1].strip().replace("\"", "")

		if old != value:
			return True, old

	return False, None

def write(path, mem_size, cpu_cores):
	for line in fileinput.input(path, inplace=1):
		mem = change_needed(line, "memsize", mem_size)
		cpu = change_needed(line, "numvcores", cpu_cores)

		if mem[0]:
			print(line.replace(mem[1], mem_size))
		elif cpu[0]:
			print(line.replace(cpu[1], cpu_cores))
		else:
			print(line)

if __name__ == "__main__":
	#write(arg0, arg1, arg2)
	write(*sys.argv[1:]);
